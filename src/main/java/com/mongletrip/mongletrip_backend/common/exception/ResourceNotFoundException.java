// src/main/java/com/mongletrip/mongletrip_backend/common/exception/ResourceNotFoundException.java

package com.mongletrip.mongletrip_backend.common.exception;

// 404 Not Found 오류 처리를 위한 커스텀 예외
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}