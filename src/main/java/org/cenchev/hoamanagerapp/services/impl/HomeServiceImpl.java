package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.cenchev.hoamanagerapp.exceptions.HomeEntityAlreadyExistsException;
import org.cenchev.hoamanagerapp.exceptions.ObjectAlreadyExistsException;
import org.cenchev.hoamanagerapp.model.dto.*;
import org.cenchev.hoamanagerapp.model.entities.*;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;
import org.cenchev.hoamanagerapp.repository.HomeRepository;
import org.cenchev.hoamanagerapp.repository.ImageRepository;
import org.cenchev.hoamanagerapp.services.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {
    private final HomeRepository homeRepository;
    private final CloudinaryImageService cloudinaryImageService;
    private final ImageRepository imageRepository;
    private final AddressService addressService;
    private final GarageService garageService;
    private final UserService userService;
    private final PropertyManagerService propertyManagerService;
    private final ParkAvailabilityService parkAvailabilityService;


    public HomeServiceImpl(HomeRepository homeRepository, CloudinaryImageService cloudinaryImageService, ImageRepository imageRepository, AddressService addressService, GarageService garageService, UserService userService, PropertyManagerService propertyManagerService, @Lazy ParkAvailabilityService parkAvailabilityService) {
        this.homeRepository = homeRepository;
        this.cloudinaryImageService = cloudinaryImageService;
        this.imageRepository = imageRepository;
        this.addressService = addressService;
        this.garageService = garageService;
        this.userService = userService;
        this.propertyManagerService = propertyManagerService;
        this.parkAvailabilityService = parkAvailabilityService;
    }

    @Override
    @Transactional
    public Home saveHome(HomeRegistrationDTO homeRegistrationDTO) throws IOException {
        Optional<Home> existingHome = homeRepository.findByName(homeRegistrationDTO.getName());
        if (existingHome.isPresent()) {
            throw new ObjectAlreadyExistsException("The name you entered is already registered!");
        }
        //Get home name from DTO
        Home home  = mapHomeRegistrationDtoToHome(homeRegistrationDTO);
        //Get and save home's address  table
        Address savedAddress = addressService.saveAddress(homeRegistrationDTO.getAddressDTO());
        home.setAddress(savedAddress);

        //Get current logged userId if is authenticated
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PropertyManager propertyManager = propertyManagerService.findByUser(userService.findUserByUsername(username));
        home.setPropertyManager(propertyManager);

        //Save home's photo url in table
        var photo = createImage(homeRegistrationDTO.getGaragePhotoUrl());
        imageRepository.saveAndFlush(photo);
        home.setImage(photo);

        //Save current data to provide linked Id before garage dto save.
        home= homeRepository.save(home);
        List<Garage> savedGarages = garageService.saveGarageSpots(homeRegistrationDTO.getGarageDTOS(),home);
        home.setSpot(savedGarages);

        //Successfully saved new Home
        return homeRepository.save(home);

    }

    @Override
    @Transactional
    public List<HomeDTO> findAllHomesByManagerId(Long managerId) {
        List<Home> homes = homeRepository.findAllByPropertyManager_Id(managerId);
        if (homes != null) {
            List<HomeDTO> collect = homes.stream()
                    .map(this::mapHomeToHomeDto)
                    .collect(Collectors.toList());
            return collect;
        }
        return Collections.emptyList();
    }



    @Override
    public HomeDTO mapHomeToHomeDto(Home home) { // Use for  list homes, edit home

        List<GarageDTO> garageDTOs = home.getSpot().stream()
                .map(garageService::mapGarageSpotsToGarageDTO)// convert garage to DTO format
                       .collect(Collectors.toList());  // collect results to a list

        AddressDTO addressDTO = addressService.mapAddressToAddressDto(home.getAddress()); // convert address to DTO format

        HomeDTO homeDTO = new HomeDTO();  // initializing data to  homeDTO
        homeDTO.setId(home.getId());
        homeDTO.setName(home.getName());
        homeDTO.setAddressDTO(addressDTO);
        homeDTO.setGarageDTOS(garageDTOs);
        //homeDTO.setImageURL(home.getImage().getUrl());
        //var photo = createImage(home.getImage().getUrl());
        //        imageRepository.saveAndFlush(photo);
        //        home.setImage(photo);
        homeDTO.setManagerUsername(home.getPropertyManager().getUser().getUsername());
        if (home.getImage() != null) {
            homeDTO.setImageURL(home.getImage().getUrl());
        } else {
            homeDTO.setImageURL("http://res.cloudinary.com/der4tss5f/image/upload/v1721515760/p6pnf1tqugb9m0caxizj.jpg"); // or some default value if needed
        }
        return homeDTO;
    }


    // Get saved Entity  and display in Manager Home list
    @Override
    @Transactional
    public HomeDTO findHomeByIdAndPropertyManagerId(Long id, Long propertyManagerId) {
        Home home = homeRepository.findByIdAndPropertyManager_Id(id, propertyManagerId)
                .orElseThrow(() -> new EntityNotFoundException("Home not found"));
        return mapHomeToHomeDto(home);
    }

    //Manager role - Edit existing Home
    @Override
    @Transactional
    public HomeDTO updateHomeByManagerId(HomeDTO homeDTO, Long managerId,String imageUrl) {
        Home getExistingHome = homeRepository.findByIdAndPropertyManager_Id(homeDTO.getId(), managerId)
                .orElseThrow(()-> new EntityNotFoundException("Could not found Home with Id selected to update."));
        if (homeByNameAndIdExist(homeDTO.getName(), homeDTO.getId())){
            throw new HomeEntityAlreadyExistsException("Home with that name has already been registered!");
        }
        getExistingHome.setName(homeDTO.getName());
        // Before address update check existing address ID
        Address updateHomeAddress = addressService.updateAddress(homeDTO.getAddressDTO());
        getExistingHome.setAddress(updateHomeAddress);

        // Before address update check existing garage ID
        homeDTO.getGarageDTOS().forEach(garageService::updateGarageSpots);

        if (imageUrl != null) { // imageUrl came from DTO
            Image image = new Image();
            image.setUrl(imageUrl);
            getExistingHome.setImage(image);
        }

        homeRepository.save(getExistingHome);
        return mapHomeToHomeDto(getExistingHome);
    }



    private boolean homeByNameAndIdExist(String name, Long homeId) {
        Optional<Home> checkExistingHomeName = homeRepository.findByName(name);
        return checkExistingHomeName.isPresent() && ! checkExistingHomeName.get().getId().equals(homeId);
    }

    private Home mapHomeRegistrationDtoToHome(HomeRegistrationDTO homeRegistrationDTO) {
        Home home = new Home();
        home.setName(formatText(homeRegistrationDTO.getName()));

        /*home.setAddress(mapAddressDTOToAddress(homeRegistrationDTO.getAddressDTO()));
        home.setSpot(mapGarageDTOToSpot());*/
        return home;
    }

    //Delete Entity - manager role
    @Override
    public void deleteHomeByIdAndManagerId(Long id, Long propertyManagerId) {
        Home home = homeRepository.findByIdAndPropertyManager_Id(id, propertyManagerId)
                .orElseThrow(() -> new EntityNotFoundException("Home not found"));
        homeRepository.delete(home);
    }
    // Search Available home parking  -> Resident controller POST Search-Result
    //
    @Override
    @Transactional
    public List<AvailableHomeParkingDTO> findAvailableHomesByLocationAndDate(String city, String state, LocalDate startDate, LocalDate endDate) {
        validStartAndEndDate(startDate, endDate);

        //Long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        //List<Home> homesWithAvailableParking = homeRepository.findHomesByCityAndState(city, state, startDate, endDate, numberOfDays);
        List<Home> homesWithAvailableParking = homeRepository.findHomesByCityAndState(city, state);


        return homesWithAvailableParking.stream().map(home -> mapHomeToHomesWithAvailableParking(home, startDate, endDate)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvailableHomeParkingDTO findAvailableHomeById(Long id, LocalDate startDate, LocalDate endDate) {
        validStartAndEndDate(startDate, endDate);

        Optional<Home> home = homeRepository.findById(id);
        if (home.isEmpty()){
            throw new EntityNotFoundException("Could not found home  with " + id);
        }
        Home homeAvailable = home.get();
        return mapHomeToHomesWithAvailableParking(homeAvailable,startDate,endDate);
    }

    @Override
    public Optional<Home> findHomeById(long id) {
        return homeRepository.findById(id);
    }

    @Override
    @Transactional
    public HomeDTO findHomeDtoById(long id) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Home not found"));
        return mapHomeToHomeDto(home);
    }

    // To Do... Overriding  it to achieves Run Time Polymorphism
    @Override
    @Transactional
    public AvailableHomeParkingDTO mapHomeToHomesWithAvailableParking(Home home, LocalDate startDate, LocalDate endDate) {
        List<GarageDTO> garageDTOs = home.getSpot().stream()
                .map(garageService::mapGarageSpotsToGarageDTO)// convert garage to DTO format
                .collect(Collectors.toList());

        AddressDTO addressDTO = addressService.mapAddressToAddressDto(home.getAddress());

        AvailableHomeParkingDTO availableHomeParkingDTO = new AvailableHomeParkingDTO();  // initializing data to  AvailableHomeParkingDTO
        availableHomeParkingDTO.setId(home.getId());
        availableHomeParkingDTO.setName(home.getName());
        availableHomeParkingDTO.setAddressDTO(addressDTO);
        availableHomeParkingDTO.setGarageDTOList(garageDTOs);
        availableHomeParkingDTO.setImageUrl(home.getImage() != null ? home.getImage().getUrl() : null); // Set image URL


        // Check what is Minimum available for each parking Type  for selected days
        int maxAvailableEVSpots = home.getSpot().stream().filter(spot -> spot.getSpaceType() == SpaceType.EV)
                //parkAvailability check via Query what garage id for which day is not available
                // If none match the filter we got 0
                .mapToInt(spot -> parkAvailabilityService.getMinParkingInventory(spot.getId(), startDate, endDate)).max().orElse(0);
        availableHomeParkingDTO.setMaxAvailableEVSpots(maxAvailableEVSpots);

        //Do same logic for next type "REGULAR" parking
        int maxAvailableAllDaySpots = home.getSpot().stream().filter(spot -> spot.getSpaceType() == SpaceType.REGULAR)
                .mapToInt(spot -> parkAvailabilityService.getMinParkingInventory(spot.getId(), startDate, endDate)).max().orElse(0);
        availableHomeParkingDTO.setMaxAvailableAllDaySpots(maxAvailableAllDaySpots);


        return  availableHomeParkingDTO;
    }

    @Override
    public List<Home> findAllHomesByPropertyManagerId(Long managerId) {
        List<Home> homes = homeRepository.findAllByPropertyManager_Id(managerId);
        return (homes != null) ? homes : Collections.emptyList();
    }


    private Image createImage(MultipartFile file) throws IOException {
        final String uploaded = cloudinaryImageService.uploadImage(file);
        Image garagePhoto = new Image();
        garagePhoto.setTitle(file.getName());
        garagePhoto.setUrl(uploaded);

        return garagePhoto;
    }

    private String formatText(String text) {
        return StringUtils.capitalize(text.trim());
    }

    private void validStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (endDate.isBefore(startDate.plusDays(1))) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

}
