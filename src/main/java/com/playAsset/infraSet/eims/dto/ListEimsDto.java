package com.playAsset.infraSet.eims.dto;

import java.util.List;

import com.playAsset.infraSet.meta.dto.MetaTermDto;

import lombok.Data;

@Data
public class ListEimsDto {
    private String entityPhyNm;
    private String entityLogNm;
    private String entityClassNm;
    private String entityPackageNm;
    private List<MetaTermDto> entityList;
}
