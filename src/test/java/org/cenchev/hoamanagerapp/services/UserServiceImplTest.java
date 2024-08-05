package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.exceptions.UserDuplicationException;
import org.cenchev.hoamanagerapp.model.bindings.UserRegisterBindingModel;
import org.cenchev.hoamanagerapp.model.entities.Role;
import org.cenchev.hoamanagerapp.model.entities.User;
import org.cenchev.hoamanagerapp.model.enums.RoleType;
import org.cenchev.hoamanagerapp.repository.ResidentRepository;
import org.cenchev.hoamanagerapp.repository.RoleRepository;
import org.cenchev.hoamanagerapp.repository.UserRepository;
import org.cenchev.hoamanagerapp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private UserServiceImpl toTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ResidentRepository residentRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserRegisterBindingModel registerBindingModel;
    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        registerBindingModel = new UserRegisterBindingModel();

        registerBindingModel.setUsername("testuser@app.com");
        registerBindingModel.setPassword("password1");
        registerBindingModel.setFirstName("Admin");
        registerBindingModel.setLastName("One");
        registerBindingModel.setRoleType(RoleType.RESIDENT);

        role = new Role(RoleType.RESIDENT);
        user = new User();

        user.setUsername(registerBindingModel.getUsername());
        user.setPassword("encodedPassword");
        user.setFirstName("Admin");
        user.setLastName("One");
        user.setRole(role);
    }

    //Test if registration of a new user is successful
    @Test
    public void testRegisterUserSuccess() {
        //simulating no existing user
        when(userRepository.findByUsername(registerBindingModel.getUsername())).thenReturn(null);
        //sets up the mock to return a Role object when findByRoleType
        when(roleRepository.findByRoleType(registerBindingModel.getRoleType())).thenReturn(role);
        // sets up the mock to return an encoded password
        when(passwordEncoder.encode(registerBindingModel.getPassword())).thenReturn("encodedPassword");
        //sets up the mock to return the User object when it saved
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userServiceImpl.registerUser(registerBindingModel);

        assertNotNull(registeredUser);
        assertEquals(registerBindingModel.getUsername(), registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals(registerBindingModel.getFirstName(), registeredUser.getFirstName());
        assertEquals(registerBindingModel.getLastName(), registeredUser.getLastName());
        assertEquals(role, registeredUser.getRole());
    }

    //Tests scenario where a user is exists and thrown an exception
    @Test
    public void testRegisterUserUserAlreadyExists() {
        when(userRepository.findByUsername(registerBindingModel.getUsername())).thenReturn(user);

        UserDuplicationException exception = assertThrows(UserDuplicationException.class, () -> {
            userServiceImpl.registerUser(registerBindingModel);
        });

        assertEquals("User with email " + registerBindingModel.getUsername() + " already exist", exception.getMessage());
    }
}
