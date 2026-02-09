package backend.goldenlink.controller;

import backend.goldenlink.dto.Comment;
import backend.goldenlink.dto.User;
import backend.goldenlink.service.CommentService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    // ✅ 댓글 작성 (로그인한 사용자만)
    @PostMapping
    public Comment createComment(@RequestParam Long boardId,
                                 @RequestParam String commentDto,
                                 HttpSession session) {

        // 1. 세션에서 로그인 유저 가져오기
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        return commentService.createComment(boardId, loginUser, commentDto);
    }

    // ✅ 특정 게시글 댓글 조회 (모든 사용자 가능)
    @GetMapping("/board/{boardId}")
    public List<Comment> getCommentsByBoard(@PathVariable Long boardId) {

        return commentService.getCommentsByBoardId(boardId);
    }


    // ✅ 댓글 삭제 (작성자 또는 관리자만)
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long commentId,
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        commentService.deleteComment(commentId, loginUser);
    }


    // ✅ 댓글 수정 (작성자만 가능)
    @PutMapping("/{commentId}")
    public Comment updateComment(
            @PathVariable Long commentId,
            @RequestBody Comment commentDto,
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        return commentService.updateComment(commentId, loginUser, commentDto.getContent());
    }
}
