package org.cenchev.hoamanagerapp.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.cenchev.hoamanagerapp.exceptions.HomeEntityAlreadyExistsException;
import org.cenchev.hoamanagerapp.exceptions.ObjectAlreadyExistsException;
import org.cenchev.hoamanagerapp.model.dto.GarageDTO;
import org.cenchev.hoamanagerapp.model.dto.HomeDTO;
import org.cenchev.hoamanagerapp.model.dto.HomeRegistrationDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationDTO;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;
import org.cenchev.hoamanagerapp.services.HomeService;
import org.cenchev.hoamanagerapp.services.ReservationService;
import org.cenchev.hoamanagerapp.services.UserService;
import org.cenchev.hoamanagerapp.services.impl.CloudinaryImageServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/manager")

public class ManagerController {
    private final HomeService homeService;
    private final UserService userService;
    private final CloudinaryImageServiceImpl cloudinaryImageService;
    private final ReservationService reservationService;

    public ManagerController(HomeService homeService, UserService userService, CloudinaryImageServiceImpl cloudinaryImageService, ReservationService reservationService) {
        this.homeService = homeService;
        this.userService = userService;
        this.cloudinaryImageService = cloudinaryImageService;
        this.reservationService = reservationService;
    }

    @GetMapping("/dashboard")

    public String managerDashboard() {
        return "manager/dashboard";
    }


    @GetMapping("/homes/add")
    public String newGarageForm(Model model) {

        HomeRegistrationDTO homeRegistrationDTO = new HomeRegistrationDTO();

        GarageDTO parkingEV = new GarageDTO(null, null, SpaceType.EV, 0, 0.0);
        GarageDTO parkingCar = new GarageDTO(null, null, SpaceType.REGULAR, 0, 0.0);
        homeRegistrationDTO.getGarageDTOS().add(parkingEV);
        homeRegistrationDTO.getGarageDTOS().add(parkingCar);

        model.addAttribute("home", homeRegistrationDTO);
        return "manager/add-home";
    }

    @PostMapping("/homes/add")
    public String addNewGarage(@Valid @ModelAttribute("home") HomeRegistrationDTO homeRegistrationDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            return "manager/add-home";
        }
        try {
            homeService.saveHome(homeRegistrationDTO);
            redirectAttributes.addFlashAttribute("message", " Home property (" + homeRegistrationDTO.getName() + ") successfully added");
            return "redirect:/manager/homes";
        } catch (ObjectAlreadyExistsException e){
            bindingResult.rejectValue("name", "already.exists", e.getMessage());
            return "manager/add-home";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/homes")
    public String viewHomeByPropertyManagerId(Model model) {
        Long propertyManagerId = getCurrentManagerId();
        List<HomeDTO> homesList = homeService.findAllHomesByManagerId(propertyManagerId);
        model.addAttribute("homes", homesList);

        return "manager/homes";
    }

    @GetMapping("/homes/edit/{id}")
    public String viewEditHomeForm(@PathVariable Long id, Model model) {
        Long propertyManagerId = getCurrentManagerId();
        HomeDTO homeDTO = homeService.findHomeByIdAndPropertyManagerId(id, propertyManagerId);
        model.addAttribute("home", homeDTO);
        return "manager/edit-home";
    }
    @PostMapping("/homes/edit/{id}")
    public String editHome(@PathVariable Long id, @Valid @ModelAttribute("home") HomeDTO homeDTO, BindingResult result,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           RedirectAttributes redirectAttributes,Model model) {
        if (result.hasErrors()) {
            return "manager/edit-home";
        }

        if (imageFile.isEmpty()) {
            model.addAttribute("imageFileError", "Image file cannot be empty");
            return "manager/edit-home";
        }

        try {
            Long managerId = getCurrentManagerId();
            homeDTO.setId(id);
            String imageUrl = cloudinaryImageService.uploadImage(imageFile);
            homeDTO.setImageURL(imageUrl);
            homeService.updateHomeByManagerId(homeDTO, managerId, imageUrl);
            redirectAttributes.addFlashAttribute("message", "Property Home (ID: " + id + ") updated successfully");
            return "redirect:/manager/homes";
        } catch (HomeEntityAlreadyExistsException e) {
            result.rejectValue("name", "home.exists", e.getMessage());
            return "manager/edit-home";
        } catch (EntityNotFoundException e) {
            result.rejectValue("id", "home.notfound", e.getMessage());
            return "manager/edit-home";
        } catch (IOException e) {
            result.rejectValue("imageFile", "upload.failed", "Failed to upload image");
            return "manager/edit-home";
        }
    }


    @DeleteMapping("/homes/delete/{id}")
    public String deleteHome(@PathVariable("id") Long id) {
        Long propertyManagerId = getCurrentManagerId();
        homeService.deleteHomeByIdAndManagerId(id, propertyManagerId);

        return "redirect:/manager/homes";
    }

    @GetMapping("/reservations")
    public String listReservationByManager(Model model, RedirectAttributes redirectAttributes) {
        try {
            Long managerId = getCurrentManagerId();
            List<ReservationDTO> reservationDTOS = reservationService.findReservationByManagerId(managerId);
            model.addAttribute("reservations", reservationDTOS);


            return "manager/reservations";

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservations not found. Please try again later.");
            return "redirect:/manager/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/manager/dashboard";
        }
    }

    /*private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username).getPropertyManager().getId();
    }*/

    private Long getCurrentManagerId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username).getPropertyManager().getId();
    }


}
