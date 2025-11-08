// src/main/java/com/mongletrip/mongletrip_backend/trip/dto/DateStatusResponse.java

package com.mongletrip.mongletrip_backend.trip.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DateStatusResponse {

    // 전체 참여 인원 수
    private int totalMemberCount;

    // AI 추천 날짜 목록 (가장 많은 멤버가 가능한 날짜 순)
    private List<DateMatchInfo> aiRecommendations;

    // 멤버별 가능한 날짜 목록 (Page 27)
    private List<MemberPossibility> memberPossibilities;

    // 날짜별 매칭 정보 DTO
    @Getter
    @Builder
    public static class DateMatchInfo {
        private LocalDate date;
        private int possibleCount; // 가능한 멤버 수
        private int matchPercentage; // 매칭률 (0-100)
        private String dateRange; // 연속된 날짜일 경우 표시 (예: "9/11(목) - 9/13(토)")
        private boolean isRecommended; // AI 추천 여부
    }

    // 멤버별 날짜 정보 DTO
    @Getter
    @Builder
    public static class MemberPossibility {
        private Long userId;
        private String userName;
        private List<LocalDate> possibleDates;
    }
}