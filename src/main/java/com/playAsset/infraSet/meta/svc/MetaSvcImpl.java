package com.playAsset.infraSet.meta.svc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.playAsset.infraSet.common.utils.StringUtil;
import com.playAsset.infraSet.meta.dao.MetaDao;
import com.playAsset.infraSet.meta.dto.ListDto;
import com.playAsset.infraSet.meta.dto.MetaTermDto;
import com.playAsset.infraSet.meta.dto.TableFieldDto;
import com.playAsset.infraSet.properties.MetaProperties;

@Service
public class MetaSvcImpl implements MetaSvc {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetaDao metaDao;

    @Autowired
    MetaProperties props;

    @Override
    public void synchronizeMetaTerm() throws Exception {
        List<String> tableNames = metaDao.selectListTableName();

        for (String name : tableNames) {

            List<TableFieldDto> fieldList = metaDao.selectListFieldInfo(name);
            for (TableFieldDto field : fieldList) {

                MetaTermDto metaTermEntity = new MetaTermDto();
                metaTermEntity.setPhyNm(StringUtil.changeCamelCase(field.getField()));
                metaTermEntity.setLogNm(field.getComment());
                metaTermEntity.setDataDecimal(0);
                metaTermEntity.setAprvReqId("sysadmin");
                metaTermEntity.setUserId("syadmin");
                
                String dataType = "";
                String dataLength = "";
                String fieldTypeNm = field.getType();
                if (fieldTypeNm.startsWith("varchar")
                || fieldTypeNm.startsWith("char")) {
                    String[] split = field.getType().split("\\(");
                    fieldTypeNm = split[0];
                    dataLength = split[1].replaceAll("\\)", "");
                }
                switch(fieldTypeNm) {
                    case "char": dataType = "String"; break;
                    case "varchar": dataType = "String"; break;
                    case "int": dataType = "int"; dataLength = "9"; break;
                    default: logger.info("자료형확인: {}", field);
                    throw new Exception("자료형을 확인해주십시오.");
                }
                metaTermEntity.setDataType(dataType);
                metaTermEntity.setDataLength(Integer.parseInt(dataLength));
                metaTermEntity.setTermSeq(metaDao.selectMetaWordSeqInq());

                try {
                    metaDao.insertMetaWord(metaTermEntity);
                } catch(Exception e) {
                    logger.info("PK Duplicated: {}", field);
                }
            }
        }
    }
    
    @Override
    public void updateApprovalAllMetaTerm() {
        MetaTermDto metaTermEntity = new MetaTermDto();
        metaTermEntity.setAprvReqId("sysadmin");
        metaTermEntity.setUserId("sysadmin");
        metaDao.updateApprovalAllMetaTerm(metaTermEntity);
    }

    @Override
    public List<MetaTermDto> selectListTmMetaTerm(MetaTermDto metaTermEntity) {
        return metaDao.selectListTmMetaTerm(metaTermEntity);
    }

    @Override
    public void insertListMetaWord(ListDto listEntity) throws Exception {
        /*
         * @Desc
         * 1.NULL체크
         */
        for (MetaTermDto meta : listEntity.getListEntity()) {
            if (StringUtil.isEmpty(meta.getPhyNm())) throw new Exception("물리명을 입력해주세요");
            if (!props.getDataTypeSet().contains(meta.getDataType())) throw new Exception("자료형을 입력해주세요");
            if (StringUtil.isEmpty(meta.getLogNm())) throw new Exception("논리명을 입력해주세요");
            if (StringUtil.isEmpty(meta.getDataLength())) throw new Exception("길이를 입력해주세요");

            String metaDataType = "";
            switch (meta.getDataType()) {
            case "CHAR": metaDataType = "String"; break;
            case "VARCHAR": metaDataType = "String"; break;
            case "INT": metaDataType = "int"; break;
            }

            meta.setDataType(metaDataType);
            meta.setTermSeq(metaDao.selectMetaWordSeqInq());
            meta.setAprvReqId("sysadmin");
            meta.setUserId("sysadmin");
            metaDao.insertMetaWord(meta);
        }
    }

    @Override
    public Map<String, List<String>> selectListMetaCommonCd() throws Exception {
        // Output Instance
        Map<String, List<String>> output = new HashMap<>();
        output.put("dataType", props.getDataTypeList());
        output.put("Type", props.getTypeList());
        return output;
    }

    @Override
    public void deleteListTmMetaTerm(List<String> chkArr) throws Exception {
        chkArr.forEach((termSeq) -> {
            metaDao.deleteTmMetaTerm(termSeq);
        });
    }

    @Override
    public void createTable(ListDto fieldListEntity, String tableNm, String tableComment) throws Exception {
        
        String executeQueryString = props.getDDLCreateTable();

        /*
         * @Desc
         * 1. 테이블의 물리명 중복 불가
         * 2. 정규식을 사용해 테이블명 생성규칙 정의
         * 3. 정규식을 사용해 컬럼명 생성규칙 정의
         */
        List<String> tableNmList = metaDao.selectListTableName();
        if (tableNmList.contains(tableNm))
            throw new Exception("중복되는 테이블명은 생성할 수 없습니다.");

        String regEx = "[A-Z0-9_]*$";
        boolean tableNmToRegEx = Pattern.matches(regEx, tableNm);
        if (tableNmToRegEx)
            throw new Exception("테이블명은 알파벳 대문자, 숫자, _로만 생성할 수 있습니다.");

        for (TableFieldDto field : fieldListEntity.getFieldListEntity()) {
            boolean fieldPhyNmToRegEx = Pattern.matches(regEx, field.getField());
            if (fieldPhyNmToRegEx)
                throw new Exception("컬럼명은 알파벳 대문자, 숫자, _로만 생성할 수 있습니다.");
        }

        /*
         * @Desc
         * 1. Property에서 미리 작성해놓은 쿼리 템플릿 조회
         * 2. String.replace 메소드를 사용해서 쿼리수정
         * 3. PK가 있으면 쿼리수정
         * 4. Mybatis.select를 사용해 테이블 생성
         */
        String columnInfo = "";
        List<String> pkFieldList = new ArrayList<>();
        for (TableFieldDto field : fieldListEntity.getFieldListEntity()) {
            String oneLineQuery = props.getOneLineQuery2();
            
            if (StringUtil.isEmpty(columnInfo))
                oneLineQuery = props.getOneLineQuery1();
            if (StringUtil.isEmpty(field.getField())) throw new Exception("컬럼명을 입력해주세요");
            if (!props.getTypeSet().contains(field.getType())) throw new Exception("자료형을 입력해주세요");
            if (StringUtil.isEmpty(field.getLength())) throw new Exception("길이를 입력해주세요");
            if (StringUtil.isEmpty(field.getComment())) throw new Exception("컬럼설명을 입력해주세요");

            String nullYn = field.isNotNull() ? "NOT NULL" : "";
            oneLineQuery = oneLineQuery.replace("Field", field.getField());
            oneLineQuery = oneLineQuery.replace("Type", field.getType());
            oneLineQuery = oneLineQuery.replace("Length", Integer.toString(field.getLength()));
            oneLineQuery = oneLineQuery.replace("NN", nullYn);
            oneLineQuery = oneLineQuery.replace("Comments", field.getComment());
            columnInfo += oneLineQuery;

            if (field.isPrimaryKey()) {
                pkFieldList.add(field.getField());
            }
        }

        executeQueryString = executeQueryString.replace("tableNm", tableNm);
        executeQueryString = executeQueryString.replace("columnInfo", columnInfo);
        executeQueryString = executeQueryString.replace("tableComment", tableComment);

        String oneLineQueryPK = "";
        if (pkFieldList.size() > 0) {
            String str = pkFieldList.toString();
            str = str.replaceAll("\\]", "");
            str = str.replaceAll("\\[", "");
            oneLineQueryPK = props.getOneLineQueryPK().replace("Field", str);
        }
        executeQueryString = executeQueryString.replace("pkInfo", oneLineQueryPK);
        
        logger.info("executeQueryString: {}", executeQueryString);
        metaDao.createTable(executeQueryString);
    }

    @Override
    public void insertExcelUpload(MultipartFile excelFile) throws Exception {
        logger.debug("excelFile.getName: {}", excelFile.getName());
        logger.debug("excelFile.getOriginalFilename: {}", excelFile.getOriginalFilename());
        logger.debug("excelFile.getSize: {}", excelFile.getSize());
    }
}
