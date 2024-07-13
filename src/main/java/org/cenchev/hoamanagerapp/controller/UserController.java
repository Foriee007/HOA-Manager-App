package org.cenchev.hoamanagerapp.controller;

import org.cenchev.hoamanagerapp.service.UserService;
import org.cenchev.hoamanagerapp.utils.RedirectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    private String getAuthenticatedUserRedirectUrl(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String redirectUrl = RedirectUtil.getRedirectUrl(authentication);
            if (redirectUrl != null) {
                return "/redirect:" + redirectUrl;
            }
        }
        return null;
    }

}
