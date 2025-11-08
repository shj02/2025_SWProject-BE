// src/main/java/com/mongletrip/mongletrip_backend/community/PostRepository.java

package com.mongletrip.mongletrip_backend.community;

import com.mongletrip.mongletrip_backend.domain.community.Post;
import com.mongletrip.mongletrip_backend.domain.user.User;
import org.springframework.data.domain.Page; // 페이징 기능 사용
import org.springframework.data.domain.Pageable; // 페이징 기능 사용
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // API 24: 목록 조회 (페이징, 최신순)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 마이페이지: 사용자가 작성한 게시글 조회
    Page<Post> findByAuthor(User author, Pageable pageable);
}