package org.cenchev.hoamanagerapp.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.cenchev.hoamanagerapp.model.dto.AvailableHomeParkingDTO;
import org.cenchev.hoamanagerapp.model.dto.GarageFindDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationRequestDTO;
import org.cenchev.hoamanagerapp.model.dto.UserDTO;
import org.cenchev.hoamanagerapp.model.entities.Reservation;
import org.cenchev.hoamanagerapp.services.HomeService;
import org.cenchev.hoamanagerapp.services.ReservationService;
import org.cenchev.hoamanagerapp.services.UserService;
import org.cenchev.hoamanagerapp.services.impl.FormatTextCapitalWords;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/resident")
public class ResidentController {
    private final HomeService homeService;
    private final ReservationService reservationService;
    private final UserService userService;

    public ResidentController(HomeService homeService, ReservationService reservationService, UserService userService) {
        this.homeService = homeService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public String showSearchForm(@ModelAttribute("garageFindDTO") GarageFindDTO garageFindDTO) {

        return "resident/search";
    }

    @PostMapping("/search")
    public String findAvailableParkingByAddressAndDate(@Valid @ModelAttribute("garageFindDTO") GarageFindDTO garageFindDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "resident/search";
        }
        try {
            //String stateLetters = FormatTextCapitalWords.capitalizeAllLetters(garageFindDTO.getState());
            validateStartAndEndDates(garageFindDTO.getStartDate(), garageFindDTO.getEndDate());
        } catch (IllegalArgumentException e) {
            result.rejectValue("endDate", null, e.getMessage());
            return "resident/search";
        }

        // data fetching to a new GET endpoint with parameters:
        return String.format("redirect:/resident/search-results?city=%s&state=%s&startDate=%s&endDate=%s", garageFindDTO.getCity(),garageFindDTO.getState(), garageFindDTO.getStartDate(), garageFindDTO.getEndDate());
    }

    @GetMapping("/search-results")
    public String showSearchResults(@RequestParam String city,@RequestParam String state,
                                    @RequestParam String startDate, @RequestParam String endDate, Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            //String stateLetters = FormatTextCapitalWords.capitalizeAllLetters(state);
            LocalDate parsedStartDate = LocalDate.parse(startDate);
            LocalDate parsedEndDate = LocalDate.parse(endDate);
            validateStartAndEndDates(parsedStartDate, parsedEndDate);

            List<AvailableHomeParkingDTO> homeLots = homeService.findAvailableHomesByLocationAndDate(city,state, parsedStartDate, parsedEndDate);

            if (homeLots.isEmpty()) {
                model.addAttribute("noHomeLotsFound", true);
            }
            long durationDays = ChronoUnit.DAYS.between(parsedStartDate, parsedEndDate);

            model.addAttribute("homeLots", homeLots);
            model.addAttribute("city", city);
            model.addAttribute("state", state);
            model.addAttribute("days", durationDays);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);

        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format. Please use the search form.");
            return "redirect:/resident/search";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid arguments provided.");
            return "redirect:/resident/search";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/resident/search";
        }
        return "resident/search-results";
    }


    @GetMapping("/parking-details/{id}")
        public String showHomeParkingDetails(@PathVariable Long id, @RequestParam String startDate, @RequestParam String endDate, Model model, RedirectAttributes redirectAttributes) {
            try {
                LocalDate parsedStartDate = LocalDate.parse(startDate);
                LocalDate parsedEndDate = LocalDate.parse(endDate);
                validateStartAndEndDates(parsedStartDate, parsedEndDate);

                AvailableHomeParkingDTO availableHomeParkingDTO = homeService.findAvailableHomeById(id, parsedStartDate, parsedEndDate);

                long durationDays = ChronoUnit.DAYS.between(parsedStartDate, parsedEndDate);

                model.addAttribute("home", availableHomeParkingDTO);
                model.addAttribute("days", durationDays);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);

                return "resident/parking-details";


            } catch (DateTimeParseException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Incorrect date entry. Please use the search form.");
                return "redirect:/resident/search";
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/resident/search";
            } catch (EntityNotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "The selected parking is no longer available. Please start a new search.");
                return "redirect:/resident/search";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try search again.");
                return "redirect:/resident/search";
            }
        }


    /*@PostMapping("/request")
    public String sendReservationRequest(@ModelAttribute ReservationRequestDTO reservationRequestDTO, BindingResult result, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        session.setAttribute("reservationRequestDTO", reservationRequestDTO);

        if (reservationRequestDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please start a new search.");
            return "redirect:/resident/search";
        }


        try {
            Long userId = getLoggedInUserId();
            Reservation reservation = reservationService.requestReservation(reservationRequestDTO, userId);
            redirectAttributes.addFlashAttribute("bookingDTO", reservation);
            return "redirect:/resident/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/resident/search-details";
        }
    }*/

    private Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.findUserDTOByUsername(username);
        return userDTO.getId();
    }
    private void validateStartAndEndDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation start date cannot be in the past!");
        }
        if (endDate.isBefore(startDate.plusDays(1))) {
            throw new IllegalArgumentException("Incorrect reservation end date!");
        }
    }


    @GetMapping("/dashboard")
    public String dashboard(){
        //Todo may need rename to Account/USER(with user settings)
        return "/resident/dashboard";
    }


}

