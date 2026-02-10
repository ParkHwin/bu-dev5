package backend.goldenlink.service;

import backend.goldenlink.dto.Board;
import backend.goldenlink.entity.EntityUser;
import backend.goldenlink.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // ✅ 게시글 작성 (관리자 권한 체크 포함)
    public Board save(Board board, EntityUser loginUser) {
        // 공지사항(NOTICE)은 관리자만 작성 가능
        if ("NOTICE".equals(board.getCategory()) && !"ADMIN".equals(loginUser.getRole())) {
            throw new RuntimeException("공지사항은 관리자만 등록할 수 있습니다.");
        }

        board.setUser(loginUser); // 작성자 정보 설정
        return boardRepository.save(board);
    }

    // ✅ 기본 저장 메서드 (필요시 사용)
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    // ✅ 전체 게시글 조회 (최신순)
    public List<Board> findAll() {
        return boardRepository.findAllByOrderByCreatedAtDesc();
    }

    // ✅ 게시글 상세 조회
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. id=" + id));
    }

    // ✅ 카테고리별 조회 (INFO, QUESTION, NOTICE)
    public List<Board> findByCategory(String category) {
        return boardRepository.findByCategoryOrderByCreatedAtDesc(category);
    }

    // ✅ 제목 검색
    public List<Board> searchByTitle(String keyword) {
        return boardRepository.findByTitleContaining(keyword);
    }

    // ✅ 게시글 수정 (반환 타입을 Board로 변경 및 로직 정정)
    public Board updateBoard(Long boardId,  EntityUser loginUser, Board updateBoard) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 권한 체크: 작성자 본인이거나 관리자여야 함
        if (board.getUser().getId().longValue() != loginUser.getId().longValue()
                && !"ADMIN".equals(loginUser.getRole())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        board.setTitle(updateBoard.getTitle());
        board.setContent(updateBoard.getContent());
        board.setCategory(updateBoard.getCategory());

        return boardRepository.save(board);
    }

    // ✅ 게시글 삭제
    public void delete(Long id, EntityUser loginUser) {
        Board board = findById(id);

        // 권한 체크: 작성자 본인이거나 관리자여야 함
        if (!board.getUser().getId().equals(loginUser.getId()) && !"ADMIN".equals(loginUser.getRole())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
}