package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.bindings.UserRegisterBindingModel;
import org.cenchev.hoamanagerapp.model.entities.User;

public interface UserService {
    User registerUser(UserRegisterBindingModel registerBindingModel);
    User findUserByUsername(String username);
}
