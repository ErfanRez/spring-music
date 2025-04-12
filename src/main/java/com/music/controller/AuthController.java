package com.music.controller;

import com.music.dto.RegisterDto;
import com.music.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("registerDto", new RegisterDto()); // Add registerDto to the model
//        return "fragments/auth-modal"; // Return the modal fragment
//    }

    @PostMapping("/register")
    public String register(
//            @Valid @ModelAttribute("registerDto") RegisterDto registerDto,
//            BindingResult bindingResult,
            RegisterDto registerDto,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            Model model
    ){
//        if (bindingResult.hasErrors()) {
//            return "/home";
//        }

        if (!registerDto.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "/home";
        }

        userService.saveUser(registerDto);

        redirectAttributes.addFlashAttribute("auth", "Registration successful");
        return "redirect:/";
    }
}
