// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/CandidateSuggestRequest.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CandidateSuggestRequest {
    private String name;        // 장소명 (Page 32)
    private String category;    // 카테고리 (Page 32)
    private String description; // 설명 (Page 32)

    // 예상 시간이나 비용 정보가 있다면 추가 가능
}