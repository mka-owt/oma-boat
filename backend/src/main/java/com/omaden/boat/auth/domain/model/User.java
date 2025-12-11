package com.omaden.boat.auth.domain.model;

import java.util.Objects;

public class User {
    private Long id;
    private final String username;
    private final String passwordHash;
    private final Role role;

    private User(String username, String passwordHash, Role role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        this.username = username.trim();
        this.passwordHash = passwordHash;
        this.role = role != null ? role : Role.USER;
    }

    private User(Long id, String username, String passwordHash, Role role) {
        this.id = id;
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        this.username = username.trim();
        this.passwordHash = passwordHash;
        this.role = role != null ? role : Role.USER;
    }

    public static User create(String username, String passwordHash, Role role) {
        return new User(username, passwordHash, role);
    }

    public static User reconstitute(Long id, String username, String passwordHash, Role role) {
        return new User(id, username, passwordHash, role);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public boolean verifyPassword(String password, PasswordEncoder encoder) {
        return encoder.matches(password, this.passwordHash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Interface to avoid dependency with Spring
    public interface PasswordEncoder {
        boolean matches(String rawPassword, String encodedPassword);
    }
}
