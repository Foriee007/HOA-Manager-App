package org.cenchev.hoamanagerapp.exceptions;

public class UserDuplicationException extends RuntimeException{
    private final String  username;

    public UserDuplicationException(String username) {
        super("User with email " + username + " already exist");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
