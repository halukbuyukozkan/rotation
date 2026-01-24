package com.buyukozkan.rotation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo", description = "Endpoints for testing JWT security")
public class DemoController {

    @Operation(summary = "Secured Endpoint", description = "This endpoint requires a valid JWT token")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    public ResponseEntity<String> sayHello(Authentication authentication) {
        return ResponseEntity.ok("Hello! You are authenticated as: " + authentication.getName());
    }
}
