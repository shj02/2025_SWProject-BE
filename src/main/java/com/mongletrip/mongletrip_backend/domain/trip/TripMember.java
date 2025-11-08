// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/TripMember.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User; // User 엔티티 import
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trip_member")
public class TripMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip; // 어떤 여행에 참여했는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 누가 참여했는지

    @Column(nullable = false)
    private boolean isCreator; // 방장 여부

    private LocalDateTime joinDate;

    @Builder
    public TripMember(Trip trip, User user, boolean isCreator) {
        this.trip = trip;
        this.user = user;
        this.isCreator = isCreator;
        this.joinDate = LocalDateTime.now();
    }
}