package com.playAsset.infraSet.common.dto;

import lombok.Data;

@Data
public abstract class SessionDto {
    private String userId;
    private String userIp;
}