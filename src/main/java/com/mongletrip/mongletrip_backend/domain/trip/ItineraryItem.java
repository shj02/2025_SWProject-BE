// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/ItineraryItem.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "itinerary_item")
public class ItineraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private LocalDate scheduleDate;

    private String title;
    private String placeName;
    private LocalTime startTime;
    private Integer estimatedDuration;
    private String memo;

    private Long lastEditorId;

    @Setter // 수정 기능을 위해 Setter를 사용합니다.
    private int orderIndex;             // Day별 순서 (일정 순서 변경 시 사용)

    // 일정 수정 시 사용할 Builder 패턴 (Service에서 toBuilder()와 함께 사용)
    @Builder(builderMethodName = "updateBuilder", buildMethodName = "updateBuild")
    public ItineraryItem(String title, String placeName, LocalTime startTime, Integer estimatedDuration, String memo, Long lastEditorId, int orderIndex) {
        this.title = title;
        this.placeName = placeName;
        this.startTime = startTime;
        this.estimatedDuration = estimatedDuration;
        this.memo = memo;
        this.lastEditorId = lastEditorId;
        this.orderIndex = orderIndex;
    }
}