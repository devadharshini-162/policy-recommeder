package com.portfolio.policy.controller;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler { @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class}) ResponseEntity<Map<String,String>> handle(RuntimeException e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));} }
