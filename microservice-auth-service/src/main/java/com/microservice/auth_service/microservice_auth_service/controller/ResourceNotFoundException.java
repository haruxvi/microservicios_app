package com.microservice.auth_service.microservice_auth_service.controller;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
