// src/main/java/com/mongletrip/mongletrip_backend/domain/community/Post.java

package com.mongletrip.mongletrip_backend.domain.community;

import jakarta.persistence.*;
import lombok.*;
import com.mongletrip.mongletrip_backend.domain.user.User;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    private LocalDateTime createdAt;

    // 조회수, 공감/댓글 수는 성능을 위해 엔티티에 직접 저장
    private int viewCount = 0;
    private int likeCount = 0;
    private int commentCount = 0;

    @PrePersist // 엔티티 저장 전 시간 자동 설정
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void incrementLikeCount() { this.likeCount++; }
    public void decrementLikeCount() { this.likeCount--; }
    public void incrementCommentCount() { this.commentCount++; }
    public void decrementCommentCount() { this.commentCount--; }
}