package com.playAsset.infraSet.meta.svc;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.playAsset.infraSet.meta.dto.ListDto;
import com.playAsset.infraSet.meta.dto.MetaTermDto;

public interface MetaSvc {
    public void synchronizeMetaTerm() throws Exception;
    public void updateApprovalAllMetaTerm();
    public List<MetaTermDto> selectListTmMetaTerm(MetaTermDto metaTermEntity);
    public void insertListMetaWord(ListDto listEntity) throws Exception;
    public void insertExcelUpload(MultipartFile excelFile)  throws Exception;
    public Map<String, List<String>> selectListMetaCommonCd() throws Exception;
    public void deleteListTmMetaTerm(List<String> chkArr) throws Exception;
    public void createTable(ListDto fieldListEntity, String tableNm, String tableComment) throws Exception;
}