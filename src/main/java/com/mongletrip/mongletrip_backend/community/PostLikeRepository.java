// src/main/java/com/mongletrip/mongletrip_backend/community/PostLikeRepository.java

package com.mongletrip.mongletrip_backend.community;

import com.mongletrip.mongletrip_backend.domain.community.Post;
import com.mongletrip.mongletrip_backend.domain.community.PostLike;
import com.mongletrip.mongletrip_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // API 27: 사용자가 특정 게시글에 공감했는지 확인
    Optional<PostLike> findByPostAndUser(Post post, User user);

    // 게시글 삭제 시 공감 연쇄 삭제용
    void deleteByPost(Post post);
}