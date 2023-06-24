package com.playAsset.infraSet.eims.svc;

import java.util.List;

import com.playAsset.infraSet.eims.dto.EimsDto;
import com.playAsset.infraSet.eims.dto.EimsFieldDto;
import com.playAsset.infraSet.eims.dto.ListEimsDto;

public interface EimsSvc {
    public void insertEntity(ListEimsDto entityList) throws Exception;
    public List<EimsDto> selectListEntity();
    public List<EimsFieldDto> selectListFieldEntity(String entityId);
    public String deployEntity(String entityId) throws Exception;
    public void deleteEntityField(EimsFieldDto entity) throws Exception;
}