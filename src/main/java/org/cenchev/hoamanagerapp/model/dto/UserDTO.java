package org.cenchev.hoamanagerapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.cenchev.hoamanagerapp.model.entities.Role;

public class UserDTO {
    private Long id;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email address")
    private String username;

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Name must only contain letters")
    private String name;

    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Last name must only contain letters")
    private String lastName;

    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Email address cannot be empty") @Email(message = "Invalid email address") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Email address cannot be empty") @Email(message = "Invalid email address") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Name cannot be empty") @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Name must only contain letters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name cannot be empty") @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Name must only contain letters") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Last name cannot be empty") @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Last name must only contain letters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name cannot be empty") @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Last name must only contain letters") String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
