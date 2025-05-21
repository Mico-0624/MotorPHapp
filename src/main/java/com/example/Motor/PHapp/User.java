/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Motor.PHapp;

/**
 *
 * @author Mico
 */
public class User {
    private final String username;
    private final String password;

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Method from the diagram
    // '+' indicates public visibility
    public boolean verifyLogin(String enteredUsername, String enteredPassword) {
        // Simple verification logic
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    // Optional: Getters if needed elsewhere (not strictly in diagram but good practice)
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
