package com.example.Motor.PHapp;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {
    private final String username;
    private final String passwordHash; // Store the hashed password

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray()); // Hash the password
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isValid(String inputPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(inputPassword.toCharArray(), passwordHash);
        return result.verified;
    }
}