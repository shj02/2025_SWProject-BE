// src/main/java/com/mongletrip/mongletrip_backend/community/CommunityService.java

package com.mongletrip.mongletrip_backend.community;

import com.mongletrip.mongletrip_backend.common.exception.ResourceNotFoundException;
import com.mongletrip.mongletrip_backend.domain.community.Comment;
import com.mongletrip.mongletrip_backend.domain.community.Post;
import com.mongletrip.mongletrip_backend.domain.community.PostLike;
import com.mongletrip.mongletrip_backend.domain.user.User;
import com.mongletrip.mongletrip_backend.community.dto.*;
import com.mongletrip.mongletrip_backend.community.dto.PostDetailResponse.CommentDetail;
import com.mongletrip.mongletrip_backend.community.dto.PostListResponse.PostSummary;
import com.mongletrip.mongletrip_backend.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    // --- 게시글 (Post) 기능 ---

    // API 24: 게시글 목록 조회 (GET /api/community/posts) - Page 60
    @Transactional(readOnly = true)
    public PostListResponse getPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PostSummary> summaries = postPage.getContent().stream()
                .map(post -> PostSummary.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .contentSnippet(post.getContent().substring(0, Math.min(post.getContent().length(), 100)) + "...")
                        .authorName(post.getAuthor().getName())
                        .createdAt(post.getCreatedAt())
                        .likeCount(post.getLikeCount())
                        .commentCount(post.getCommentCount())
                        .build())
                .collect(Collectors.toList());

        return PostListResponse.builder()
                .posts(summaries)
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .build();
    }

    // API 25: 게시글 상세 조회 (GET /api/community/posts/{postId}) - Page 62
    @Transactional
    public PostDetailResponse getPostDetail(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        User currentUser = userRepository.findById(userId).orElse(null); // 사용자가 없을 수도 있음 (비회원)

        // 1. 조회수 증가 (트랜잭션이므로 바로 반영)
        post.toBuilder().viewCount(post.getViewCount() + 1).build();

        // 2. 현재 사용자의 공감 여부 확인
        boolean isLiked = currentUser != null && postLikeRepository.findByPostAndUser(post, currentUser).isPresent();

        // 3. 댓글 목록 조회 및 DTO 변환
        List<CommentDetail> comments = commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .map(comment -> CommentDetail.builder()
                        .commentId(comment.getId())
                        .authorName(comment.getAuthor().getName())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .isMyComment(currentUser != null && comment.getAuthor().getId().equals(userId))
                        .isPostAuthor(comment.getAuthor().getId().equals(post.getAuthor().getId()))
                        .build())
                .collect(Collectors.toList());

        return PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getName())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .isLikedByMe(isLiked)
                .isMyPost(post.getAuthor().getId().equals(userId))
                .comments(comments)
                .build();
    }

    // API 26: 게시글 작성 (POST /api/community/posts) - Page 61
    @Transactional
    public Long createPost(Long userId, PostRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("작성자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        return postRepository.save(post).getId();
    }

    // API 27: 게시글 수정 (PUT /api/community/posts/{postId}) - Page 64
    @Transactional
    public void updatePost(Long userId, Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        // 권한 확인
        if (!post.getAuthor().getId().equals(userId)) {
            throw new SecurityException("게시글 수정 권한이 없습니다.");
        }

        postRepository.save(post.toBuilder()
                .title(request.getTitle())
                .content(request.getContent())
                .build());
    }

    // API 28: 게시글 삭제 (DELETE /api/community/posts/{postId}) - Page 65
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        // 권한 확인
        if (!post.getAuthor().getId().equals(userId)) {
            throw new SecurityException("게시글 삭제 권한이 없습니다.");
        }

        // 연관된 댓글과 공감 모두 삭제
        commentRepository.deleteByPost(post);
        postLikeRepository.deleteByPost(post);

        postRepository.delete(post);
    }

    // --- 댓글 (Comment) 기능 ---

    // API 29: 댓글 작성 (POST /api/community/posts/{postId}/comments) - Page 62
    @Transactional
    public Long createComment(Long userId, Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("작성자를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(request.getContent())
                .build();

        post.incrementCommentCount(); // 게시글의 댓글 수 증가
        postRepository.save(post);

        return commentRepository.save(comment).getId();
    }

    // API 30: 댓글 삭제 (DELETE /api/community/comments/{commentId}) - Page 63
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        Post post = comment.getPost();

        // 삭제 권한 확인: (1) 댓글 작성자 또는 (2) 게시글 작성자
        if (!comment.getAuthor().getId().equals(userId) &&
                !post.getAuthor().getId().equals(userId)) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);

        post.decrementCommentCount(); // 게시글의 댓글 수 감소
        postRepository.save(post);
    }

    // --- 공감 (Like) 기능 ---

    // API 31: 공감 토글 (POST /api/community/posts/{postId}/like)
    @Transactional
    public boolean toggleLike(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);

        if (existingLike.isPresent()) {
            // 공감 취소
            postLikeRepository.delete(existingLike.get());
            post.decrementLikeCount();
            postRepository.save(post);
            return false; // 공감 취소됨
        } else {
            // 공감
            PostLike newLike = PostLike.builder().post(post).user(user).build();
            postLikeRepository.save(newLike);
            post.incrementLikeCount();
            postRepository.save(post);
            return true; // 공감됨
        }
    }
}