// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/Trip.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "trip")
@Builder(toBuilder = true)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 여행 이름 (예: 제주도 우정여행)
    private String destination; // 여행지 (예: 제주도)

    @Column(unique = true, nullable = false, length = 8)
    private String inviteCode;  // 친구 초대 코드 (예: SJDKSHDK)

    private Long creatorId;     // 방을 처음 만든 사용자 ID (User FK)

    private LocalDate startDate; // 여행 시작 날짜 (날짜 확정 시 업데이트)
    private LocalDate endDate;   // 여행 종료 날짜

    @Column(nullable = false)
    private int progress = 0; // 여행 계획 진행률 (0% ~ 100%)
}