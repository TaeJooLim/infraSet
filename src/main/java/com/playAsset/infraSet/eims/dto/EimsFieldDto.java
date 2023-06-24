package com.playAsset.infraSet.eims.dto;

import com.playAsset.infraSet.common.dto.SessionDto;

import lombok.Data;

@Data
public class EimsFieldDto extends SessionDto {
    private String entityId;
    private String termPhyNm;
    private String termLogNm;
    private String termDataType;
    private String entityRefType;
    private String regId;
    private String regDtm;
    private String udtId;
    private String udtDtm;
}