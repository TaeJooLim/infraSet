package com.playAsset.infraSet.meta.dto;

import java.util.List;

import lombok.Data;

@Data
public class ListDto {
    private List<MetaTermDto> listEntity;
    private List<TableFieldDto> fieldListEntity;
}