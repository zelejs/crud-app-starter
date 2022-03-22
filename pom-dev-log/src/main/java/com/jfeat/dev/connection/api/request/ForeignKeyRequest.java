package com.jfeat.dev.connection.api.request;

import lombok.Data;

@Data
public class ForeignKeyRequest {
    private String CONSTRAINT_NAME;
    private String TABLE_NAME;
    private String COLUMN_NAME;
}
