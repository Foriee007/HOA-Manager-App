package org.cenchev.hoamanagerapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.cenchev.hoamanagerapp.model.dto.HomeDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationRequestDTO;
import org.cenchev.hoamanagerapp.model.dto.UserDTO;
import org.cenchev.hoamanagerapp.services.HomeService;
import org.cenchev.hoamanagerapp.services.ReservationService;
import org.cenchev.hoamanagerapp.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final HomeService homeService;

    public ReservationController(ReservationService reservationService, UserService userService, HomeService homeService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.homeService = homeService;
    }

    @PostMapping("/request")
    public String initiateBooking(@ModelAttribute ReservationRequestDTO reservationRequestDTO, HttpSession session) {
        session.setAttribute("reservationRequestDTO", reservationRequestDTO);
        return "redirect:/reservation/submit";
    }

    @GetMapping("/submit")
    public String showSubmitReservation(Model model, RedirectAttributes redirectAttributes, HttpSession session){
        ReservationRequestDTO reservationRequestDTO = (ReservationRequestDTO) session.getAttribute("reservationRequestDTO");

        if (reservationRequestDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please start a new search.");
            return "redirect:/resident/search";
        }
        long durationDays = ChronoUnit.DAYS.between(reservationRequestDTO.getStartDate(), reservationRequestDTO.getEndDate());
        model.addAttribute("days", durationDays);
        HomeDTO homeDTO = homeService.findHomeDtoById(reservationRequestDTO.getHomeId());
        model.addAttribute("reservationRequestDTO", reservationRequestDTO);
        model.addAttribute("homeDTO", homeDTO);
        model.addAttribute("days", durationDays);

        return "reservation/confirm-reservation";

    }

    @PostMapping("/submit")
    public String sendReservationRequest( Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        ReservationRequestDTO reservationRequestDTO = (ReservationRequestDTO) session.getAttribute("reservationRequestDTO");
        // (@ModelAttribute ReservationRequestDTO reservationRequestDTO,BindingResult result,)
        if (reservationRequestDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please start a new search.");
            return "redirect:/resident/search";
        }
        /*if (result.hasErrors()) {
            HomeDTO homeDTO = homeService.findHomeDtoById(reservationRequestDTO.getHomeId()); // Error fixed with @Transactional
            model.addAttribute("reservationRequestDTO", reservationRequestDTO);
            model.addAttribute("homeDTO", homeDTO);
            return "reservation/confirm-reservation";
        }*/

        try {
            Long userId = getLoggedInUserId();
            ReservationDTO reservations = reservationService.confirmReservation(reservationRequestDTO, userId);
            redirectAttributes.addFlashAttribute("reservationDTO", reservations);  // Att payment method  and replace with PaymentDTO in future

            return "redirect:/resident/dashboard";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/reservation/submit";
        }
    }

    private Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.findUserDTOByUsername(username);
        return userDTO.getId();
    }
}
