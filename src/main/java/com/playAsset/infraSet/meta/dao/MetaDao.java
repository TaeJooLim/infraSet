package com.playAsset.infraSet.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.playAsset.infraSet.meta.dto.MetaTermDto;
import com.playAsset.infraSet.meta.dto.TableFieldDto;

@Mapper
public interface MetaDao {
    public List<String> selectListTableName();
    public List<TableFieldDto> selectListFieldInfo(String tableName);
    public int selectMetaWordSeqInq();
    public int insertMetaWord(MetaTermDto metaTermEntity);
    public int updateApprovalAllMetaTerm(MetaTermDto metaTermEntity);
    public List<MetaTermDto> selectListTmMetaTerm(MetaTermDto metaTermEntity);
    public void deleteTmMetaTerm(String termSeq);
    public void createTable(String executeQueryString);
}