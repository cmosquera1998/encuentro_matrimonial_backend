package com.encuentro.matrimonial.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1")
public class PruebaController {
    @GetMapping("/hola")
    public String index() {
        return "Hola, Bienvenido 'proyecto encuentro matrimoniales'";
    }
}