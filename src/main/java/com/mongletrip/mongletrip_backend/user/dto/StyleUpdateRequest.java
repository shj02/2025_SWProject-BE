// src/main/java/com/mongletrip/mongletrip_backend/user/dto/StyleUpdateRequest.java

package com.mongletrip.mongletrip_backend.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class StyleUpdateRequest {
    private List<String> travelStyles;
}