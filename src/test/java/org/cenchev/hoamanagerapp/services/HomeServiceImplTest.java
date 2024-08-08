package org.cenchev.hoamanagerapp.services;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.exceptions.ObjectAlreadyExistsException;
import org.cenchev.hoamanagerapp.model.dto.*;
import org.cenchev.hoamanagerapp.model.entities.*;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;
import org.cenchev.hoamanagerapp.repository.HomeRepository;
import org.cenchev.hoamanagerapp.repository.ImageRepository;
import org.cenchev.hoamanagerapp.services.impl.HomeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HomeServiceImplTest {

    @Mock
    private HomeRepository homeRepository;
    @Mock
    private CloudinaryImageService cloudinaryImageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private AddressService addressService;
    @Mock
    private GarageService garageService;
    @Mock
    private UserService userService;
    @Mock
    private PropertyManagerService propertyManagerService;
    @Mock
    private ParkAvailabilityService parkAvailabilityService;

    @InjectMocks
    private HomeServiceImpl homeService;

    private Home home;
    private HomeRegistrationDTO homeRegistrationDTO;
    private PropertyManager propertyManager;
    private User user;
    private AddressDTO addressDTO;
    private Address address;
    private GarageDTO garageDTO;
    private Image image;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User();
        user.setUsername("user1");

        propertyManager = new PropertyManager();
        propertyManager.setUser(user);

        home = new Home();
        home.setId(1L);
        home.setName("Home 1");
        home.setPropertyManager(propertyManager);

        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setAddressLine("123 Main St");
        addressDTO.setCity("City");
        addressDTO.setState("State");
        addressDTO.setUsZipCode("12345");

        address = new Address();
        address.setId(1L);
        address.setAddressLine("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setUsZipCode("12345");

        garageDTO = new GarageDTO();
        garageDTO.setId(1L);
        garageDTO.setHomeId(1L);
        garageDTO.setSpaceType(SpaceType.EV);
        garageDTO.setPricePerDay(20.0);
        garageDTO.setParkingSpotCount(2);

        homeRegistrationDTO = new HomeRegistrationDTO();
        homeRegistrationDTO.setName("Home 1");
        homeRegistrationDTO.setAddressDTO(addressDTO);
        homeRegistrationDTO.setGarageDTOS(Collections.singletonList(garageDTO));
        homeRegistrationDTO.setGaragePhotoUrl(mock(MultipartFile.class));

        image = new Image();
        image.setUrl("http://res.cloudinary.com/der4tss5f/image/upload/v1721515760/p6pnf1tqugb9m0caxizj.jpg");
    }

    @Test
    public void testSaveHome() throws IOException {
        when(homeRepository.findByName(homeRegistrationDTO.getName())).thenReturn(Optional.empty());
        when(addressService.saveAddress(any(AddressDTO.class))).thenReturn(address);
        when(userService.findUserByUsername(anyString())).thenReturn(user);
        when(propertyManagerService.findByUser(any(User.class))).thenReturn(propertyManager);
        when(cloudinaryImageService.uploadImage(any(MultipartFile.class))).thenReturn("image_url");
        when(imageRepository.saveAndFlush(any(Image.class))).thenReturn(image);
        when(homeRepository.save(any(Home.class))).thenReturn(home);
        when(garageService.saveGarageSpots(anyList(), any(Home.class))).thenReturn(Collections.singletonList(new Garage()));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user1");
        SecurityContextHolder.setContext(securityContext);

        Home savedHome = homeService.saveHome(homeRegistrationDTO);

        assertNotNull(savedHome);
        assertEquals(home.getId(), savedHome.getId());
        verify(homeRepository, times(2)).save(any(Home.class));
    }

    @Test
    public void testSaveHome_NameAlreadyExists() {
        when(homeRepository.findByName(homeRegistrationDTO.getName())).thenReturn(Optional.of(home));

        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class, () -> {
            homeService.saveHome(homeRegistrationDTO);
        });

        assertEquals("The name you entered is already registered!", exception.getMessage());
        verify(homeRepository, never()).save(any(Home.class));
    }

    @Test
    public void testFindAllHomesByManagerId() {
        when(homeRepository.findAllByPropertyManagerId(anyLong())).thenReturn(Collections.singletonList(home));

        List<HomeDTO> homeDTOs = homeService.findAllHomesByManagerId(1L);

        assertFalse(homeDTOs.isEmpty());
        assertEquals(1, homeDTOs.size());
        assertEquals("user1", homeDTOs.get(0).getManagerUsername());
    }

    @Test
    public void testFindAllHomesByManagerId_NoHomesFound() {
        when(homeRepository.findAllByPropertyManagerId(anyLong())).thenReturn(Collections.emptyList());

        List<HomeDTO> homeDTOs = homeService.findAllHomesByManagerId(1L);

        assertTrue(homeDTOs.isEmpty());
    }

    @Test
    public void testFindHomeByIdAndPropertyManagerId() {
        when(homeRepository.findByIdAndPropertyManager_Id(anyLong(), anyLong())).thenReturn(Optional.of(home));

        HomeDTO homeDTO = homeService.findHomeByIdAndPropertyManagerId(1L, 1L);

        assertNotNull(homeDTO);
        assertEquals(home.getId(), homeDTO.getId());
    }

    @Test
    public void testFindHomeByIdAndPropertyManagerId_HomeNotFound() {
        when(homeRepository.findByIdAndPropertyManager_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            homeService.findHomeByIdAndPropertyManagerId(1L, 1L);
        });

        assertEquals("Home not found", exception.getMessage());
    }

    @Test
    public void testUpdateHomeByManagerId() throws IOException {
        when(homeRepository.findByIdAndPropertyManager_Id(anyLong(), anyLong())).thenReturn(Optional.of(home));
        when(addressService.updateAddress(any(AddressDTO.class))).thenReturn(address);
        when(garageService.updateGarageSpots(any(GarageDTO.class))).thenReturn(new Garage());
        when(homeRepository.save(any(Home.class))).thenReturn(home);

        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setId(1L);
        homeDTO.setName("Updated Home");
        homeDTO.setAddressDTO(addressDTO);
        homeDTO.setGarageDTOS(Collections.singletonList(garageDTO));

        HomeDTO updatedHomeDTO = homeService.updateHomeByManagerId(homeDTO, 1L, "new_image_url");

        assertNotNull(updatedHomeDTO);
        assertEquals(home.getId(), updatedHomeDTO.getId());
        assertEquals("Updated Home", updatedHomeDTO.getName());
        verify(homeRepository, times(1)).save(any(Home.class));
    }

    @Test
    public void testUpdateHomeByManagerId_HomeNotFound() {
        when(homeRepository.findByIdAndPropertyManager_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setId(1L);
        homeDTO.setName("Updated Home");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            homeService.updateHomeByManagerId(homeDTO, 1L, "new_image_url");
        });

        assertEquals("Could not found Home with Id selected to update.", exception.getMessage());
    }

    @Test
    public void testDeleteHomeByIdAndManagerId() {
        when(homeRepository.findByIdAndPropertyManager_Id(anyLong(), anyLong())).thenReturn(Optional.of(home));
        doNothing().when(homeRepository).delete(any(Home.class));

        homeService.deleteHomeByIdAndManagerId(1L, 1L);

        verify(homeRepository, times(1)).delete(any(Home.class));
    }

    @Test
    public void testDeleteHomeByIdAndManagerId_HomeNotFound() {
        when(homeRepository.findByIdAndPropertyManager_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            homeService.deleteHomeByIdAndManagerId(1L, 1L);
        });

        assertEquals("Home not found", exception.getMessage());
    }

    @Test
    public void testFindAvailableHomesByLocationAndDate() {
        List<Home> homes = Collections.singletonList(home);
        when(homeRepository.findHomesByCityAndState(anyString(), anyString())).thenReturn(homes);

        List<AvailableHomeParkingDTO> availableHomeParkingDTOs = homeService.findAvailableHomesByLocationAndDate("City", "State", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        assertFalse(availableHomeParkingDTOs.isEmpty());
        assertEquals(1, availableHomeParkingDTOs.size());
    }

    @Test
    public void testFindAvailableHomeById() {
        when(homeRepository.findById(anyLong())).thenReturn(Optional.of(home));

        AvailableHomeParkingDTO availableHomeParkingDTO = homeService.findAvailableHomeById(1L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        assertNotNull(availableHomeParkingDTO);
        assertEquals(home.getId(), availableHomeParkingDTO.getId());
    }

    @Test
    public void testFindAvailableHomeById_HomeNotFound() {
        when(homeRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            homeService.findAvailableHomeById(1L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        });

        assertEquals("Could not found home  with 1", exception.getMessage());
    }
}
