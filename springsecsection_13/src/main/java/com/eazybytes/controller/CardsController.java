package com.eazybytes.controller;

import com.eazybytes.model.Cards;
import com.eazybytes.model.Customer;
import com.eazybytes.repository.CardsRepository;
import com.eazybytes.repository.CustomerRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SecurityRequirement(name = "keycloak")
public class CardsController {

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myCards")
    public List<Cards> getCardDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers != null && !customers.isEmpty()) {
            List<Cards> cards = cardsRepository.findByCustomerId(customers.get(0).getId());
            if (cards != null ) {
                return cards;
            }
        }
        return null;
    }
    
    @PostMapping("/myCards")
    public List<Cards> postCardDetails(@RequestParam String email) {
    	System.out.println("post work-----------------------------------");
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers != null && !customers.isEmpty()) {
            List<Cards> cards = cardsRepository.findByCustomerId(customers.get(0).getId());
            if (cards != null ) {
                return cards;
            }
        }
        return null;
    }

}
