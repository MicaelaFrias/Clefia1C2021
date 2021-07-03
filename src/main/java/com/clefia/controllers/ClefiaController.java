package com.clefia.controllers;

import com.clefia.services.IClefiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClefiaController {
    @Autowired
    private IClefiaService clefiaService;

    @RequestMapping("/encrypt")
    public String encrypt(@RequestParam(name="encryptMode") String encryptMode, Model model) {
        model.addAttribute("encryptMode", encryptMode);
        return "index";
    }
}
