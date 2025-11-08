// src/main/java/com/mongletrip/mongletrip_backend/community/dto/PostRequest.java

package com.mongletrip.mongletrip_backend.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
}