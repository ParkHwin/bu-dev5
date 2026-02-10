package backend.goldenlink.repository;

import backend.goldenlink.dto.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 전체 게시글 조회 (최신순)
    List<Board> findAllByOrderByCreatedAtDesc();

    // 카테고리별 조회 (INFO, QUESTION, NOTICE)
    List<Board> findByCategoryOrderByCreatedAtDesc(String category);

    // 제목 검색
    List<Board> findByTitleContaining(String keyword);

    //페이징 기능
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Board> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    Page<Board> findByTitleContainingOrderByCreatedAtDesc(String keyword, Pageable pageable);
}
