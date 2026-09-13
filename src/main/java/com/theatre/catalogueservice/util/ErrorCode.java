package com.theatre.catalogueservice.util;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DEFAULT("ERR_00", "Internal Server Error"),
    PRODUCTION_NOT_FOUND("PRD_01", "Production Not Found");

    final String errorCode;
    final String errorDescription;

    ErrorCode(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

}
