package com.portfolio.policy.dto;
import jakarta.validation.constraints.*;
public record TierRequest(@NotNull @Min(18) @Max(75) Integer age, @NotBlank String incomeBracket, @NotNull @Min(0) @Max(100) Integer healthRiskScore, @NotNull @Positive Double sumInsured, @NotBlank String coverageType) {}
