package com.playAsset.infraSet.eims.ctr;

import java.util.List;

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

import com.playAsset.infraSet.eims.dto.EimsDto;
import com.playAsset.infraSet.eims.dto.EimsFieldDto;
import com.playAsset.infraSet.eims.dto.ListEimsDto;
import com.playAsset.infraSet.eims.svc.EimsSvcImpl;

@Controller
@RequestMapping("/eims")
public class EimsCtr {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EimsSvcImpl eimsSvc;

    @GetMapping("/main")
    public String getMain() {
        return "eims/main";
    }

    /**
     * 엔티티의 물리명, 논리명, 패키지명 그리고 필드들을 받아 엔티티 테이블에 저장한다.
     * @param entityList
     * @return String 성공여부
     * @throws Exception
     */
    @PostMapping("/insertEntity")
    @ResponseBody
    public String insertEntity(@ModelAttribute ListEimsDto entityList) throws Exception {
        logger.info("========== EimsCtr.insertEntity ==========");
        try {
            logger.info("entityList: {}", entityList);
            eimsSvc.insertEntity(entityList);
        } catch(Exception e) {
            return e.getMessage();
        }
        return "Y";
    }

    /**
     * 엔티티 테이블에 입력된 엔티티정보를 다건조회한다.
     * @return List 엔티티다건조회
     * @throws Exception
     */
    @GetMapping("/selectListEntity")
    @ResponseBody
    public List<EimsDto> selectListEntity() throws Exception {
        logger.info("========== EimsCtr.selectListEntity ==========");
        return eimsSvc.selectListEntity();
        // try {
        // } catch(Exception e) {
        //     return "헤더에 메시지 Set하는걸로 변경필요";
        // }
    }

    /**
     * 엔티티의 필드 정보를 다건조회한다.
     * @return List 엔티티필드다건조회
     * @throws Exception
     */
    @PostMapping("/selectListFieldEntity")
    @ResponseBody
    public List<EimsFieldDto> selectListFieldEntity(@RequestParam String entityId) throws Exception {
        logger.info("========== EimsCtr.selectListFieldEntity ==========");
        return eimsSvc.selectListFieldEntity(entityId);
        // try {
        // } catch(Exception e) {
        //     return "헤더에 메시지 Set하는걸로 변경필요";
        // }
    }

    @PostMapping("/deployEntity")
    @ResponseBody
    public String deployEntity(@RequestParam(value="chkArr[]") List<String> chkArr) throws Exception {
        logger.info("========== EimsCtr.deployEntity ==========");
        for (String entityId : chkArr) {
            try {
                eimsSvc.deployEntity(entityId);
            } catch(Exception e) {
                return "I/O 배포에 실패했습니다. " + e.getMessage();
            }
        }
        return "Y";
    }

    @PostMapping("/deleteEntityField")
    @ResponseBody
    public String deleteEntityField(@ModelAttribute EimsFieldDto eimsFieldDto) throws Exception {
        logger.info("========== EimsCtr.deleteEntityField ==========");
        logger.info(": {}", eimsFieldDto);
        try {
            eimsSvc.deleteEntityField(eimsFieldDto);
        } catch(Exception e) {
            return e.getMessage();
        }
        return "Y";
    }
}