// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/Candidate.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private String name;        // 장소명
    private String category;    // 카테고리 (예: 자연/관광)
    private String description; // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggested_by_user_id")
    private User suggestedBy;   // 제안자

    private boolean isAiRecommended; // AI 추천 장소 여부

    @Setter // 일정에 추가될 때 상태 변경을 위해 필요
    private boolean isItineraryAdded = false; // 일정표 추가 여부

    @Column(nullable = false)
    private int voteCount = 0; // 투표 수 (성능 최적화용)

    public void incrementVoteCount() {
        this.voteCount++;
    }

    public void decrementVoteCount() {
        this.voteCount--;
    }
}