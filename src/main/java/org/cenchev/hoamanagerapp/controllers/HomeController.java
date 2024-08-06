package org.cenchev.hoamanagerapp.controllers;

import jakarta.validation.Valid;
import org.cenchev.hoamanagerapp.model.dto.ContactFormDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    public HomeController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {

        return "admin/dashboard";
    }


    //Submit Email vs Contact Us  page
    private final JavaMailSender mailSender;

    @GetMapping("/contact-us")
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactFormDTO());
        return "contact-us";
    }

    @PostMapping("/contact-us")
    public String submitContactForm(@Valid @ModelAttribute("contactForm") ContactFormDTO contactForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "contact-us";
        }

        sendEmail(contactForm);
        model.addAttribute("message", "Your message has been sent successfully!");
        return "contact-us";
    }

    private void sendEmail(ContactFormDTO contactForm) { // To Do -> set in separate service
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("your.account@gmail.com"); // Replace with your email address
            message.setFrom("your.account@gmail.com");// Replace with your email address
            message.setSubject("New Contact Form Submission from " + contactForm.getName());
            message.setText("Email: " + contactForm.getEmail() + "\n\nMessage:\n" + contactForm.getMessage());

            mailSender.send(message);
        } catch (Exception e) {
            // Handle the exception accordingly, e.g., log it, rethrow it, or set an error message in the model
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
