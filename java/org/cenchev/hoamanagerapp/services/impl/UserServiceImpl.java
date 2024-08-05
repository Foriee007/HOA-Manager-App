package org.cenchev.hoamanagerapp.services.impl;

import org.cenchev.hoamanagerapp.exceptions.UserDuplicationException;
import org.cenchev.hoamanagerapp.model.bindings.UserRegisterBindingModel;
import org.cenchev.hoamanagerapp.model.entities.PropertyManager;
import org.cenchev.hoamanagerapp.model.entities.Resident;
import org.cenchev.hoamanagerapp.model.entities.Role;
import org.cenchev.hoamanagerapp.model.entities.User;
import org.cenchev.hoamanagerapp.model.enums.RoleType;
import org.cenchev.hoamanagerapp.repository.PropertyManagerRepository;
import org.cenchev.hoamanagerapp.repository.ResidentRepository;
import org.cenchev.hoamanagerapp.repository.RoleRepository;
import org.cenchev.hoamanagerapp.repository.UserRepository;
import org.cenchev.hoamanagerapp.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PropertyManagerRepository propertyManagerRepository;
    private final ResidentRepository residentRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PropertyManagerRepository propertyManagerRepository, ResidentRepository residentRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.propertyManagerRepository = propertyManagerRepository;
        this.residentRepository = residentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegisterBindingModel registerBindingModel){
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(registerBindingModel.getUsername()));
        if (existingUser.isPresent()) {
            throw new UserDuplicationException(registerBindingModel.getUsername());
        }

        User user = mapRegistrationDtoToUser(registerBindingModel);

        if (RoleType.RESIDENT.equals(registerBindingModel.getRoleType())) {
            Resident resident = new Resident();
            resident.setUser(user);
            residentRepository.save(resident);
        } else if (RoleType.PROPERTY_MANAGER.equals(registerBindingModel.getRoleType())) {
            PropertyManager propertyManager = new PropertyManager();
            propertyManager.setUser(user);
            propertyManagerRepository.save(propertyManager);
        }

        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private User mapRegistrationDtoToUser(UserRegisterBindingModel registerBindingModel) {
        Role userRole = roleRepository.findByRoleType(registerBindingModel.getRoleType());
        User user = new User();
        user.setUsername(formatEmailText(registerBindingModel.getUsername())); //uncapitalize
        user.setPassword(passwordEncoder.encode(registerBindingModel.getPassword()));
        user.setFirstName(formatText(registerBindingModel.getFirstName()));
        user.setLastName(formatText(registerBindingModel.getLastName()));
        user.setRole(userRole);
        return user;

    }

    private String formatText(String text) {
        return StringUtils.capitalize(text.trim());
    }
    private String formatEmailText(String text) {
        return StringUtils.uncapitalize(text.trim());
    }

}
