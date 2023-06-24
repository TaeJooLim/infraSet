package com.playAsset.infraSet.eims.svc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playAsset.infraSet.common.utils.GitUtil;
import com.playAsset.infraSet.common.utils.GitUtil.FileStatus;
import com.playAsset.infraSet.eims.dao.EimsDao;
import com.playAsset.infraSet.eims.dto.EimsDto;
import com.playAsset.infraSet.eims.dto.EimsFieldDto;
import com.playAsset.infraSet.eims.dto.ListEimsDto;
import com.playAsset.infraSet.meta.dto.MetaTermDto;
import com.playAsset.infraSet.properties.EimsProperties;
import com.playAsset.infraSet.properties.GitProperties;
import com.playAsset.infraSet.properties.MetaProperties;

@Service
public class EimsSvcImpl implements EimsSvc {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EimsDao eimsDao;

    @Autowired
    private MetaProperties props;

    @Autowired
    private GitProperties gitProps;

    @Autowired
    private EimsProperties eimsProps;

    @Override
    public void insertEntity(ListEimsDto listEimsEntity) throws Exception {

        /*
         * @Desc
         * Input Validation
         * 1. 중복된 필드명 불가
         */
        List<EimsDto> eimsDtoList = eimsDao.selectListEntity();
        Set<String> set = new HashSet<>();
        eimsDtoList.forEach(obj -> {
            set.add(obj.getEntityPhyNm());
        });
        for (MetaTermDto entity : listEimsEntity.getEntityList()) {
            if (set.contains(entity.getPhyNm()))
                throw new Exception("엔티티의 물리명은 중복될 수 없습니다.");
        }

        /*
         * @Desc
         * 1. EIMS ID 조회
         * 2. 엔티티테이블 등록
         */
        String eimsId = eimsDao.selectMaxEimsEntityId();
        EimsDto eimsEntity = new EimsDto();
        eimsEntity.setEntityId(eimsId);
        eimsEntity.setEntityPhyNm(listEimsEntity.getEntityPhyNm());
        eimsEntity.setEntityLogNm(listEimsEntity.getEntityLogNm());
        eimsEntity.setEntityPackageNm(listEimsEntity.getEntityPackageNm());
        eimsEntity.setUserId("sysadmin");
        eimsDao.insertEntity(eimsEntity);

        /*
         * @Desc
         * 엔티티필드테이블 등록
         * 1. 엔티티의 참조타입을 분기처리한다 (0: primiteType / 1: Object / 2: List)
         * 2. 엔티티의 필드들의 정보를 등록한다.
         */
        for (MetaTermDto entity : listEimsEntity.getEntityList()) {
            EimsFieldDto fieldEntity = new EimsFieldDto();
            fieldEntity.setEntityId(eimsId);
            fieldEntity.setTermPhyNm(entity.getPhyNm());
            fieldEntity.setTermLogNm(entity.getLogNm());
            fieldEntity.setTermDataType(entity.getDataType());

            String entityRefType = "";
            if (props.getDataTypeList().contains(entity.getDataType()))
                entityRefType = "0";
            else if (entity.getDataType().startsWith("Object"))
                entityRefType = "1";
            else if (entity.getDataType().startsWith("List"))
                entityRefType = "2";
            else
                throw new Exception("엔티티의 자료형을 확인해 주십시오.");
            fieldEntity.setEntityRefType(entityRefType);
            fieldEntity.setUserId("sysadmin");

            eimsDao.insertEntityField(fieldEntity);
        }
    }

    @Override
    public List<EimsDto> selectListEntity() {
        return eimsDao.selectListEntity();
    }

    @Override
    public List<EimsFieldDto> selectListFieldEntity(String entityId) {
        return eimsDao.selectListFieldEntity(entityId);
    }

    @Override
    public String deployEntity(String entityId) throws Exception {
        createIOFile(entityId);

        Git git = null;
        git = GitUtil.openGit(gitProps.getPath());

        logger.info("git pull remote repository...");
        GitUtil.pullFromRepository(git, gitProps.getUserId(), gitProps.getPasswd());

        logger.info("git indexing from localRepository...");
        Map<FileStatus, Set<String>> map = GitUtil.getIndexingList(git.getRepository());
        int indexingCnt = 0;
        for (Set<String> set: map.values()) {
            indexingCnt += set.size();
        }

        if (indexingCnt != 0) {
            logger.info("git add Index...");
            logger.info("indexing list: ");
            for (FileStatus fileStatus: map.keySet()) {
                for (String fileName: map.get(fileStatus)) {
                    logger.info("status: {} / file: {}", fileStatus, fileName);
                    GitUtil.addIndex(git, fileName, false);
                }
            }

            logger.info("git commit localRepository...");
            GitUtil.commitLocalRepository(git, "EIMS I/O 배포: ", gitProps.getUserId(), gitProps.getUserEmail());

            logger.info("git push remote repository...");
            GitUtil.pushRemoteRepository(git, gitProps.getUserId(), gitProps.getPasswd());
        }

        return "Y";
    }

    @Override
    public void deleteEntityField(EimsFieldDto entity) throws Exception {
        String entityId = entity.getEntityId();
        List<EimsFieldDto> fieldEntityList = eimsDao.selectListFieldEntity(entityId);
        if (fieldEntityList.size() == 1) {
            throw new Exception("I/O의 필드가 1건만 존재할경우, I/O 필드만 삭제할 수 없습니다. I/O를 삭제해주세요");
        }

        eimsDao.deleteEntityField(entity);
    }

    public String createIOFile(String entityId) {
        
        /*
         * @Desc
         * 엔티티배포
         * 1. 엔티티ID를 조건으로 엔티티정보와 엔티티필드정보를 조회한다.
         * 2. 프로퍼티에 등록한 실제파일생성경로와 패키지명을 조합해 엔티티가 실제로 생성되는 경로 및 폴더를 생성한다.
         * 3. File, FileWriter, PrintWriter 객체를 사용해 엔티티파일의 내용을 생성한다. (class, package, import, annotation, field)
         * 4. 파일 생성에 성공하면 GitUtil을 사용해서 생성된 엔티티파일을 Git에 Push&Commit한다.
         */
        EimsDto entityInqIn = new EimsDto();
        entityInqIn.setEntityId(entityId);
        EimsDto entityInqOut = eimsDao.selectOneEntity(entityInqIn);
        List<EimsFieldDto> list = eimsDao.selectListFieldEntity(entityId);

        String packagePath = entityInqOut.getEntityPackageNm();
        String staticPath = eimsProps.getPath() + entityInqOut.getEntityPackageNm().replaceAll("[.]", eimsProps.EIMS_SIGN_SLASH);

        try {
            File file = new File(staticPath + eimsProps.EIMS_SIGN_SLASH + entityInqOut.getEntityPhyNm() + eimsProps.EIMS_EXTENSION_JAVA);
            String createPath = "";
            for (String str : staticPath.split(eimsProps.EIMS_SIGN_SLASH)) {
                createPath += str + eimsProps.EIMS_SIGN_SLASH;
                File folder = new File(createPath);
                if (!folder.exists()) {
                    folder.mkdir();
                }
            }

            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(eimsProps.EIMS_ENTITY_PACAKGE + eimsProps.EIMS_ENTITY_SPACE + packagePath + eimsProps.EIMS_SIGN_SEMICOLON);
            printWriter.print("\n");
            printWriter.print(eimsProps.EIMS_ENTITY_IMPORT + eimsProps.EIMS_ENTITY_SPACE + eimsProps.EIMS_IMPORT_LOMBOK + eimsProps.EIMS_SIGN_SEMICOLON);
            printWriter.print("\n");
            printWriter.print(eimsProps.EIMS_ANNOTATION_LOMBOK);
            printWriter.print("\n");
            printWriter.print(eimsProps.EIMS_ENTITY_PUBLIC + eimsProps.EIMS_ENTITY_SPACE + eimsProps.EIMS_ENTITY_CLASS + eimsProps.EIMS_ENTITY_SPACE + entityInqOut.getEntityPhyNm());
            printWriter.print(eimsProps.EIMS_SIGN_ONBRACE);
            printWriter.print("\n");
            
            for (EimsFieldDto entity : list) {
                printWriter.print("\t");
                printWriter.print(eimsProps.EIMS_ENTITY_PRIVATE + eimsProps.EIMS_ENTITY_SPACE + entity.getTermDataType() + eimsProps.EIMS_ENTITY_SPACE + entity.getTermPhyNm() + eimsProps.EIMS_SIGN_SEMICOLON);
                printWriter.print("\n");
            }
            printWriter.print(eimsProps.EIMS_SIGN_OFFBRACE);
            printWriter.close();
        } catch (IOException ioe) {
            return "I/O 파일 생성에 실패했습니다." + ioe;
        }
        return "Y";
    }
}
