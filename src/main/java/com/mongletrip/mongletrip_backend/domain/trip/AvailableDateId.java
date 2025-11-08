// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/AvailableDateId.java

package com.mongletrip.mongletrip_backend.domain.trip;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

// 복합키를 정의하는 클래스는 Serializable, equals, hashCode를 구현해야 합니다.
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDateId implements Serializable {

    private Long trip; // Trip 엔티티의 ID 타입과 일치
    private Long user; // User 엔티티의 ID 타입과 일치
    private LocalDate possibleDate;
}