// src/main/java/com/mongletrip/mongletrip_backend/community/CommentRepository.java

package com.mongletrip.mongletrip_backend.community;

import com.mongletrip.mongletrip_backend.domain.community.Comment;
import com.mongletrip.mongletrip_backend.domain.community.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // API 25: 특정 게시글의 모든 댓글 조회 (작성 시간 순)
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);

    // 게시글 삭제 시 댓글 연쇄 삭제용
    void deleteByPost(Post post);
}