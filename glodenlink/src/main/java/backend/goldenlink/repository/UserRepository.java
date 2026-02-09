package backend.goldenlink.repository;

import backend.goldenlink.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 아이디로 사용자 찾기 (로그인 기능 구현 시 필요)
    Optional<User> findByUserId(String userId);
}