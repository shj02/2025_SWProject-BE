// src/main/java/com/mongletrip/mongletrip_backend/domain/community/PostLike.java

package com.mongletrip.mongletrip_backend.domain.community;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "post_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"}) // 중복 공감 방지
})
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}