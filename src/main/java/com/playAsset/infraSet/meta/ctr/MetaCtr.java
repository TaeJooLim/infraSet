package com.playAsset.infraSet.meta.ctr;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.playAsset.infraSet.meta.dto.ListDto;
import com.playAsset.infraSet.meta.dto.MetaTermDto;
import com.playAsset.infraSet.meta.svc.MetaSvcImpl;

@Controller
@RequestMapping("/meta")
public class MetaCtr {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetaSvcImpl metaSvcImpl;

    @GetMapping("/main")
    public String getMain() {
        return "meta/main";
    }

    /**
     * DB에 생성된 테이블들의 필드들을 메타데이터(용어)로 동기화한다.
     * @param request
     * @return String 성공여부
     */
    @GetMapping("/synchronizeMetaTerm")
    @ResponseBody
    public String synchronizeMetaTerm() throws Exception {
        logger.info("========== MetaCtr.synchronizeMetaTerm ==========");
        metaSvcImpl.synchronizeMetaTerm();
        return "Y";
    }

    /**
     * 메타에 등록된 데이터들을 일괄결재한다.
     * @param request
     * @return
     */
    @GetMapping("/updateApprovalAllMetaTerm")
    @ResponseBody
    public String updateApprovalAllMetaTerm() {
        logger.info("========== MetaCtr.synchronizeMetaTerm ==========");
        metaSvcImpl.updateApprovalAllMetaTerm();
        return "Y";
    }

    /**
     * 메타용어를 다건조회한다.
     * @param metaTermEntity 메타용어조회조건
     * @return List
     */
    @GetMapping("/selectListTmMetaTerm")
    @ResponseBody
    public List<MetaTermDto> selectListTmMetaTerm(@ModelAttribute MetaTermDto metaTermEntity) {
        logger.info("========== MetaCtr.selectListTmMetaTerm ==========");
        return metaSvcImpl.selectListTmMetaTerm(metaTermEntity);
    }

    /**
     * 메타용어를 다건등록한다.
     * @param listEntity 메타용어등록정보리스트
     * @return String 성공여부
     * @throws Exception
     */
    @PostMapping("/insertListMetaWord")
    @ResponseBody
    public String insertListMetaWord(@ModelAttribute ListDto listEntity) throws Exception {
        logger.info("========== MetaCtr.insertListMetaWord ==========");
        logger.info("listEntity > {}", listEntity);
        try {
            metaSvcImpl.insertListMetaWord(listEntity);
        } catch(Exception e) {
            return e.getMessage();
        }
        return "Y";
    }

    /**
     * 메타공통코드를 조회한다.
     * @return Map 메타공통코드
     */
    @GetMapping("/selectListMetaCommonCd")
    @ResponseBody
    public Map<String, List<String>> selectListMetaCommonCd() throws Exception {
        logger.info("========== MetaCtr.selectListMetaCommonCd ==========");
        return metaSvcImpl.selectListMetaCommonCd();
    }

    /**
     * 체크박스로 선택해서 메타에 등록된 데이터들을 선택삭제한다.
     * @return Map 메타공통코드
     */
    @PostMapping("/deleteListTmMetaTerm")
    @ResponseBody
    public String deleteListTmMetaTerm(@RequestParam(value="chkArr[]") List<String> chkArr) throws Exception {
        logger.info("========== MetaCtr.deleteListTmMetaTerm ==========");
        logger.info("List<String> chkArr: {}", chkArr);
        try {
            metaSvcImpl.deleteListTmMetaTerm(chkArr);
        } catch(Exception e) {
            return e.getMessage();
        }
        return "Y";
    }

    /**
     * 테이블을 생성한다.
     * @param metaTermEntity 메타용어조회조건
     * @return List
     */
    @PostMapping("/createTable")
    @ResponseBody
    public String createTable(@ModelAttribute ListDto fieldListEntity, @RequestParam String tableNm, @RequestParam String tableComment) throws Exception {
        logger.info("========== MetaCtr.selectListTmMetaTerm ==========");
        try {
            metaSvcImpl.createTable(fieldListEntity, tableNm, tableComment);
        } catch(Exception e) {
            return e.getMessage();
        }
        return "Y";
    }

    /**
     * 엑셀을 import해서 메타데이터를 입력한다.
     * @param request 엑셀파일
     * @return String 성공여부
     */
    @PostMapping("/insertExcelUpload")
    @ResponseBody
    public String insertExcelUpload(MultipartHttpServletRequest request) {
        logger.info("========== MetaCtr.insertExcelUpload ==========");
        //MetaSvcImpl.insertExcelUpload(excelFile);
        return "Y";
    }
}