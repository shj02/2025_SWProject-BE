// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/AvailableDate.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User; // User 엔티티 import
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "available_date")
@IdClass(AvailableDateId.class) // 복합 키 사용을 위한 클래스 지정 (아래 AvailableDateId 참조)
public class AvailableDate {

    // 복합 키: Trip ID와 User ID는 TripMember에서 관리되므로, 여기서는 Date를 포함한 복합 키를 사용합니다.
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    private LocalDate possibleDate; // 사용자가 가능한 특정 날짜

    @Builder
    public AvailableDate(Trip trip, User user, LocalDate possibleDate) {
        this.trip = trip;
        this.user = user;
        this.possibleDate = possibleDate;
    }
}