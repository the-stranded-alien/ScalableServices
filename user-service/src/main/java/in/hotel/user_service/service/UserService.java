package in.hotel.user_service.service;

import in.hotel.user_service.model.Role;
import in.hotel.user_service.model.User;
import in.hotel.user_service.repository.UserRepository;
import in.hotel.user_service.util.NotificationUtil;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final NotificationUtil notificationUtil;

    public UserService(UserRepository userRepo, NotificationUtil notificationUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.notificationUtil = notificationUtil;
    }

    public List<User> getAllUsers() {
        notificationUtil.sendAllUserViewedAudit();
        return userRepo.findAll();
    }

    public User createUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        notificationUtil.sendCreateUserAudit(user.getUsername());
        notificationUtil.sendCreateUserNotification(user.getUsername());
        return userRepo.save(user);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
    
}