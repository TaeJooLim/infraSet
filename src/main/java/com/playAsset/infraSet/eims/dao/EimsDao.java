package com.playAsset.infraSet.eims.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.playAsset.infraSet.eims.dto.EimsDto;
import com.playAsset.infraSet.eims.dto.EimsFieldDto;

@Mapper
public interface EimsDao {
    public void insertEntity(EimsDto entity);
    public void insertEntityField(EimsFieldDto entity);
    public String selectMaxEimsEntityId();
    public EimsDto selectOneEntity(EimsDto entity);
    public List<EimsDto> selectListEntity();
    public List<EimsFieldDto> selectListFieldEntity(String entityId);
    public void deleteEntityField(EimsFieldDto entity);
}