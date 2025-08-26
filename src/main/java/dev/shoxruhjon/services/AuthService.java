package dev.shoxruhjon.services;

import dev.shoxruhjon.models.User;
import dev.shoxruhjon.utils.PasswordUtil;

import java.util.List;
import java.util.Optional;

public class AuthService {
    private final List<User> users;
    private User currentUser;

    public AuthService(List<User> users) {
        this.users = users;
    }

    public boolean register(String fullName, String username, String password) {
        boolean exists = users.stream().anyMatch(u -> u.getUsername().equals(username));
        if (exists) return false;

        String hash = PasswordUtil.hashPassword(password);
        users.add(new User(fullName, username, hash));

        System.out.println("User:  " + users.get(0).getUsername());
        return true;
    }

    public boolean login(String username, String password) {
        String hash = PasswordUtil.hashPassword(password);
        Optional<User> found = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPasswordHash().equals(hash))
                .findFirst();

        if (found.isPresent()) {
            currentUser = found.get();
            return true;
        }
        return false;
    }

    public void logout() { currentUser = null; }

    public User getCurrentUser() { return currentUser; }
}