package com.theatre.catalogueservice.util;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DEFAULT("ERR_00", "Internal Server Error"),
    VALIDATION_FAILED("ERR_01", "Request validation failed"),
    UNAUTHORIZED("ATH_04", "Authentication required"),
    ACCESS_DENIED("ATH_05", "You do not have permission to perform this action"),
    PRODUCTION_NOT_FOUND("PRD_01", "Production Not Found"),
    PERFORMANCE_NOT_FOUND("PRF_01", "Performance Not Found");

    final String errorCode;
    final String errorDescription;

    ErrorCode(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

}
