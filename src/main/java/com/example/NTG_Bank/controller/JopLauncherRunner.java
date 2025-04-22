package com.example.NTG_Bank.controller;

import com.example.NTG_Bank.service.JopLauncher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JopLauncherRunner {
    private final JopLauncher jopLauncher ;

    @GetMapping("/run")
    public String runJop() {
        try {
        this.jopLauncher.run();
        return "JOP has been started successfully!";
        }
        catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

}
