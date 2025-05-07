package in.hotel.common_library.models;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserPrincipal implements UserDetails {

    private final String username;
    private final String email;
    private final String userId;
    private final String role;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(String username, String email, String userId, String role, String firstName, String lastName, String phone,
                               Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.email = email;
        this.userId = userId;

        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.authorities = authorities;
    }

    @Override public String getUsername() { return username; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public String getPassword() { return null; }

    @Override
    public String toString() {
        return "CustomUserPrincipal{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
