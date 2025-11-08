// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/CandidateListResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class CandidateListResponse {
    private Long candidateId;
    private String name;
    private String category;
    private String description;
    private String suggestedByName; // 제안자 이름 (홍길동님 제안)
    private int voteCount;          // 투표 수
    private boolean isVotedByMe;    // 현재 사용자가 투표했는지 여부
    private boolean isItineraryAdded; // 일정표 추가 여부
    private boolean isAiRecommended; // AI 추천 장소 여부
}