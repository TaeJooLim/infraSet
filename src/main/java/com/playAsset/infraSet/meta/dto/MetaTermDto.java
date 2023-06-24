package com.playAsset.infraSet.meta.dto;

import lombok.Data;

@Data
public class MetaTermDto {
    private String phyNm;
    private String dataType;
    private String logNm;
    private int termSeq;
    private int dataLength;
    private int dataDecimal;
    private String aprvYn;
    private String aprvReqId;
    private String aprvReqDtm;
    private String regId;
    private String regDtm;
    private String udtId;
    private String udtDtm;
    private String userId;
}