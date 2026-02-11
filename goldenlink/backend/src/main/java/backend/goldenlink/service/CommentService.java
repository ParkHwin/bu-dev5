package backend.goldenlink.service;

import backend.goldenlink.dto.Board;
import backend.goldenlink.dto.Comment;
import backend.goldenlink.entity.EntityUser;
import backend.goldenlink.repository.BoardRepository;
import backend.goldenlink.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    //생성자 작성
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          BoardRepository boardRepository
                          ) {

        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;

    }

    // 댓글 작성
    public Comment createComment(Long boardId, EntityUser loginUser, String content) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 1. 권한 체크 로직
        String category = board.getCategory(); // INFO, QUESTION, NOTICE 등

        // 정보공유(INFO)가 아니면서 관리자가 아닌 경우 차단
        if (!"INFO".equals(category) && !"ADMIN".equals(loginUser.getRole())) {
            throw new RuntimeException("이 게시판은 관리자만 댓글을 작성할 수 있습니다.");
        }

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(loginUser); // 전달받은 세션 유저 바로 사용
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    // 게시글 댓글 조회
    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    // 댓글 수정
    public Comment updateComment(Long commentId,  EntityUser loginUser, String content) {
        // 1. 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        // 2. 권한 체크: (작성자 본인 아님) AND (관리자도 아님) 인 경우 에러 발생
        boolean isOwner = comment.getUser().getId().equals(loginUser.getId());
        boolean isAdmin = "ADMIN".equals(loginUser.getRole());

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("수정 권한이 없습니다. (작성자 또는 관리자만 가능)");
        }

        // 3. 내용 수정 및 저장
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId,  EntityUser loginUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        // 작성자 본인도 아니고 관리자도 아니면 삭제 불가
        boolean isOwner = comment.getUser().getId().equals(loginUser.getId());
        boolean isAdmin = "ADMIN".equals(loginUser.getRole());

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
