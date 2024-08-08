package org.cenchev.hoamanagerapp.services;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.entities.PropertyManager;
import org.cenchev.hoamanagerapp.model.entities.User;
import org.cenchev.hoamanagerapp.repository.PropertyManagerRepository;
import org.cenchev.hoamanagerapp.services.impl.PropertyManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PropertyManagerServiceImplTest {

    @Mock
    private PropertyManagerRepository propertyManagerRepository;

    @InjectMocks
    private PropertyManagerServiceImpl propertyManagerService;

    private User user;
    private PropertyManager propertyManager;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");

        propertyManager = new PropertyManager();
        propertyManager.setUser(user);
    }

    @Test
    public void testFindByUser_PropertyManagerExists() {
        // Check if  property manager exists
        when(propertyManagerRepository.findByUser(user)).thenReturn(Optional.of(propertyManager));

        PropertyManager result = propertyManagerService.findByUser(user);

        assertEquals(propertyManager, result);
    }

    @Test
    public void testFindByUser_PropertyManagerNotFound() {
        // Check if  property manager does not exist
        when(propertyManagerRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            propertyManagerService.findByUser(user);
        });
    }
}
