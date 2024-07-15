package org.cenchev.hoamanagerapp.controller;

import jakarta.validation.Valid;
import org.cenchev.hoamanagerapp.exceptions.UserDuplicationException;
import org.cenchev.hoamanagerapp.model.bindings.UserRegisterBindingModel;
import org.cenchev.hoamanagerapp.model.enums.RoleType;
import org.cenchev.hoamanagerapp.service.UserService;
import org.cenchev.hoamanagerapp.utils.RedirectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String homePage(Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) {
            return redirect;
        }
        return "/index";
    }
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) {
            return redirect;
        }
        return "/login";
    }
    // RESIDENT Registration
    @GetMapping("/register/resident")
    public String showResidentRegistrationForm(@ModelAttribute("user") UserRegisterBindingModel registrationDTO, Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null){
            return redirect;
        }
        return "/register-resident";
    }
    @PostMapping("/register/resident")
    public String registerResidentAccount(@Valid @ModelAttribute("user") UserRegisterBindingModel registrationDTO, BindingResult result) {
        registrationDTO.setRoleType(RoleType.RESIDENT);
        return registerUser(registrationDTO, result, "register-resident", "register/resident");
    }

    //Property Manager Registration

    @GetMapping("/register/manager")
    public String showManagerRegistrationForm(@ModelAttribute("user") UserRegisterBindingModel registrationDTO, Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null){
            return redirect;
        }
        return "/register-manager";
    }

    @PostMapping("/register/manager")
    public String registerManagerAccount(@Valid @ModelAttribute("user") UserRegisterBindingModel registrationDTO, BindingResult result) {
        registrationDTO.setRoleType(RoleType.PROPERTY_MANAGER);
        return registerUser(registrationDTO, result, "register-manager", "register/manager");
    }

    private String registerUser(UserRegisterBindingModel registrationDTO, BindingResult result, String view, String redirectUrl) {
        if (result.hasErrors()) {
            return view;
        }
        try {
            userService.registerUser(registrationDTO);
        } catch (UserDuplicationException e) {
            result.rejectValue("username", "user.exists", e.getMessage());
            return view;
        }
        return "redirect:/" + redirectUrl + "?success";
    }

    private String getAuthenticatedUserRedirectUrl(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String redirectUrl = RedirectUtil.getRedirectUrl(authentication);
            if (redirectUrl != null) {
                return "redirect:" + redirectUrl; //"/redirect:" + redirectUrl;
            }
        }
        return null;
    }

}
