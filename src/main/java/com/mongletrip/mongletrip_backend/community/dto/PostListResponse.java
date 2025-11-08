// src/main/java/com/mongletrip/mongletrip_backend/community/dto/PostListResponse.java

package com.mongletrip.mongletrip_backend.community.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostListResponse {

    private List<PostSummary> posts;
    private int totalPages;
    private long totalElements;

    @Getter
    @Builder
    public static class PostSummary {
        private Long postId;
        private String title;
        private String contentSnippet; // 내용 일부 요약
        private String authorName;
        private LocalDateTime createdAt;
        private int likeCount;
        private int commentCount;
    }
}