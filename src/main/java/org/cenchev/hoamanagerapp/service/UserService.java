package org.cenchev.hoamanagerapp.service;

import org.cenchev.hoamanagerapp.model.entities.User;

import java.io.IOException;

public interface UserService {
    User registrarUser() throws IOException;
}
