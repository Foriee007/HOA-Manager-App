package org.cenchev.hoamanagerapp.controllers;

import jakarta.validation.Valid;
import org.cenchev.hoamanagerapp.model.dto.AvailableHomeParkingDTO;
import org.cenchev.hoamanagerapp.model.dto.GarageFindDTO;
import org.cenchev.hoamanagerapp.services.HomeService;
import org.cenchev.hoamanagerapp.services.impl.FormatTextCapitalWords;
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

    public ResidentController(HomeService homeService) {
        this.homeService = homeService;
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
            String stateLetters = FormatTextCapitalWords.capitalizeAllLetters(state);
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
            return "redirect:resident/search";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid arguments provided.");
            return "redirect:resident/search";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:resident/search";
        }
        return "resident/search-results";
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

