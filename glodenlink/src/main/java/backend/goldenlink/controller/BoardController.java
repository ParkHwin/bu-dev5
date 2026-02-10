package backend.goldenlink.controller;

import backend.goldenlink.dto.Board;
import backend.goldenlink.entity.EntityUser;
import backend.goldenlink.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // ✅ 게시글 작성
    @PostMapping
    public Board createBoard(@RequestBody Board board, HttpSession session) {
        EntityUser loginUser = (EntityUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return boardService.save(board, loginUser);
    }

    // ✅ 전체 조회
    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.findAll();
    }

    // ✅ 상세 조회
    @GetMapping("/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.findById(id);
    }

    // ✅ 게시글 수정
    @PutMapping("/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board board, HttpSession session) {
        EntityUser loginUser = (EntityUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return boardService.updateBoard(id, loginUser, board);
    }

    // ✅ 게시글 삭제
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Long id, HttpSession session) {
        EntityUser loginUser = (EntityUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        boardService.delete(id, loginUser);
        return "게시글이 삭제되었습니다.";
    }

    // ✅ 카테고리별 조회
    @GetMapping("/category/{category}")
    public List<Board> getByCategory(@PathVariable String category) {
        return boardService.findByCategory(category);
    }

    // ✅ 제목 검색
    @GetMapping("/search")
    public List<Board> search(@RequestParam String keyword) {
        return boardService.searchByTitle(keyword);
    }
}