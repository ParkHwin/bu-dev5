package backend.goldenlink.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자 (users 테이블과 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private EntityUser user;

    // 게시판 종류 (INFO, QUESTION, NOTICE)
    @Column(nullable = false, length = 20)
    private String category;

    // 제목
    @Column(nullable = false, length = 255)
    private String title;

    // 본문
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 생성일
    @Column(name = "createdAt")
    private LocalDateTime createdAt;


    // 생성일 자동 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


    // 기본 생성자 (필수)
    public Board() {

    }

    // 생성자
    public Board(EntityUser user, String category, String title, String content) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
    }


    // getter setter

    public Long getId() {
        return id;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

