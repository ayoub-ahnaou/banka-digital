package com.digital.banka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agentOauth")
@RequiredArgsConstructor
public class AgentOauthController {

    @GetMapping("/operations")
    public String getPendingOperationsOauth() {
        return "OAuth2 secured pending operations";
    }
}
