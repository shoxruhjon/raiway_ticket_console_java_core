package dev.shoxruhjon.services;

import dev.shoxruhjon.models.User;

public interface IAuthService {
    boolean register(String fullName, String username, String password);
    boolean login(String username, String password);
    void logout();
    User getCurrentUser();
}
