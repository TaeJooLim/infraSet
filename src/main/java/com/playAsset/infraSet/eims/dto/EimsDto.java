package com.playAsset.infraSet.eims.dto;

import com.playAsset.infraSet.common.dto.SessionDto;

import lombok.Data;

@Data
public class EimsDto extends SessionDto {
    private String entityId;
    private String entityPhyNm;
    private String entityLogNm;
    private String entityPackageNm;
    private String regId;
    private String regDtm;
    private String udtId;
    private String udtDtm;
}
