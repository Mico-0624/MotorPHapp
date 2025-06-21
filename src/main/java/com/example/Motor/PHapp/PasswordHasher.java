package com.example.Motor.PHapp;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {
    public static void main(String[] args) {
        String password = "admin123";
        String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        System.out.println("Hashed password: " + hash);
    }
}