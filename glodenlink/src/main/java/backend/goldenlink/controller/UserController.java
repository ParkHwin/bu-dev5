package backend.goldenlink.controller;

import backend.goldenlink.dto.User;
import backend.goldenlink.repository.UserRepository;
import backend.goldenlink.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // ✅ 로그인 (아이디와 비밀번호로 확인 후 세션 저장)
    @PostMapping("/login")
    public String login(@RequestBody User loginRequest, HttpSession session) {
        try {
            // 서비스를 통해 로그인 처리
            User user = userService.login(loginRequest.getUserId(), loginRequest.getPassword());

            // 세션에 저장
            session.setAttribute("loginUser", user);
            return "로그인 성공: " + user.getName();

        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // ✅ 로그아웃 (세션 무효화)
    @PostMapping("/logout")
    public String logout (HttpSession session){
        session.invalidate(); // 세션 삭제
        return "로그아웃 되었습니다.";
    }

}