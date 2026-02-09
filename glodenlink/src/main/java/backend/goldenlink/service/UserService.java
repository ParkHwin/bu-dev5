package backend.goldenlink.service;

import backend.goldenlink.dto.User;
import backend.goldenlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ 로그인 검증 로직
    public User login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .filter(u -> u.getPassword().equals(password)) // 비밀번호 일치 여부 확인
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다."));
    }

    // ✅ 특정 사용자 조회
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}