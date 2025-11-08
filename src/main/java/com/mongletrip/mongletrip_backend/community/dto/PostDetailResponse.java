// src/main/java/com/mongletrip/mongletrip_backend/community/dto/PostDetailResponse.java

package com.mongletrip.mongletrip_backend.community.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private boolean isLikedByMe; // 현재 사용자의 공감 여부
    private boolean isMyPost;    // 현재 사용자의 작성 여부

    private List<CommentDetail> comments;

    @Getter
    @Builder
    public static class CommentDetail {
        private Long commentId;
        private String authorName;
        private String content;
        private LocalDateTime createdAt;
        private boolean isMyComment;
        private boolean isPostAuthor; // 게시글 작성자인지 여부
    }
}