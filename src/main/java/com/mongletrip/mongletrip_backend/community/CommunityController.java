// src/main/java/com/mongletrip/mongletrip_backend/community/CommunityController.java

package com.mongletrip.mongletrip_backend.community;

import com.mongletrip.mongletrip_backend.common.util.SecurityUtil;
import com.mongletrip.mongletrip_backend.community.dto.CommentRequest;
import com.mongletrip.mongletrip_backend.community.dto.PostDetailResponse;
import com.mongletrip.mongletrip_backend.community.dto.PostListResponse;
import com.mongletrip.mongletrip_backend.community.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // --- 게시글 (Post) API ---

    /**
     * API 24: 게시글 목록 조회 (GET /api/community/posts) - Page 60
     * @param pageable (page=0, size=10, sort=createdAt,desc 등)
     */
    @GetMapping("/posts")
    public ResponseEntity<PostListResponse> getPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PostListResponse response = communityService.getPosts(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * API 25: 게시글 상세 조회 (GET /api/community/posts/{postId}) - Page 62
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();
        PostDetailResponse response = communityService.getPostDetail(userId, postId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 26: 게시글 작성 (POST /api/community/posts) - Page 61
     */
    @PostMapping("/posts")
    public ResponseEntity<Long> createPost(@RequestBody PostRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long postId = communityService.createPost(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    /**
     * API 27: 게시글 수정 (PUT /api/community/posts/{postId}) - Page 64
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        communityService.updatePost(userId, postId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * API 28: 게시글 삭제 (DELETE /api/community/posts/{postId}) - Page 65
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();
        communityService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    // --- 댓글 (Comment) API ---

    /**
     * API 29: 댓글 작성 (POST /api/community/posts/{postId}/comments) - Page 62
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long commentId = communityService.createComment(userId, postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    /**
     * API 30: 댓글 삭제 (DELETE /api/community/comments/{commentId}) - Page 63
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        communityService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }

    // --- 공감 (Like) API ---

    /**
     * API 31: 공감 토글 (POST /api/community/posts/{postId}/like)
     */
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();
        boolean isLiked = communityService.toggleLike(userId, postId);
        return ResponseEntity.ok(isLiked ? "공감되었습니다." : "공감 취소되었습니다.");
    }
}