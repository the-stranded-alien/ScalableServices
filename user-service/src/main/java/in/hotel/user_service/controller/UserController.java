package in.hotel.user_service.controller;

import in.hotel.user_service.dto.LoginRequest;
import in.hotel.user_service.dto.LoginResponse;
import in.hotel.user_service.model.User;
import in.hotel.user_service.service.UserService;
import in.hotel.common_library.util.JwtUtil;
import in.hotel.user_service.util.NotificationUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/user/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final NotificationUtil notificationUtil;

    public UserController(UserService userService, JwtUtil jwtUtil, NotificationUtil notificationUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.notificationUtil = notificationUtil;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (user == null || !new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getEmail(), String.valueOf(user.getId()), user.getFirstName(), user.getLastName(), user.getPhone());
        notificationUtil.sendUserLoginAudit(user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String onlyAdminCanSeeThis() {
        return "Hello Admin, this is a secret page!";
    }
}