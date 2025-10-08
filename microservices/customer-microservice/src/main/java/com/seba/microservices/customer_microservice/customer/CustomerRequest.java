package com.seba.microservices.customer_microservice.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(  String id,
    @NotNull(message = "First name is required")
    @NotBlank(message = "First name cannot be blank")
    String firstName,
    @NotNull(message = "Last name is required")
    @NotBlank(message = "First name cannot be blank")
    String lastName,
    @NotNull(message = "Email is required")
    @Email(message = "Email is not valid")
    @NotBlank(message = "First name cannot be blank")
    String email,
    String phone,
    String address,
    String city) {

}
