package backend.goldenlink.repository;

import backend.goldenlink.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록 조회
    List<Comment> findByBoardId(Long boardId);
}
