package backend.goldenlink.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId; // 로그인 아이디

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    // ✅ 권한 (ADMIN 또는 USER)
    @Column(nullable = false)
    private String role;

    // 기본 생성자
    public User() {}

    // 테스트를 위한 생성자
    public User(Long id, String userId, String role) {
        this.id = id;
        this.userId = userId;
        this.role = role;
    }

    // ===== Getter / Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}