// src/main/java/com/mongletrip/mongletrip_backend/domain/trip/CandidateVote.java

package com.mongletrip.mongletrip_backend.domain.trip;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "candidate_vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"candidate_id", "user_id"}) // 중복 투표 방지
})
public class CandidateVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}