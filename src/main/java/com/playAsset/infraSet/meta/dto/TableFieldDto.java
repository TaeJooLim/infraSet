package com.playAsset.infraSet.meta.dto;

import lombok.Data;

@Data
public class TableFieldDto {
    private String Field;
    private String Type;
    private String Comment;
    private int Length;
    private boolean PrimaryKey;
    private boolean NotNull;
}
