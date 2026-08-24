package com.portfolio.policy.dto;
import jakarta.validation.constraints.*;
import java.util.List;
public record QuoteRequest(@NotNull @Min(18) @Max(75) Integer age, @NotBlank String gender, @NotNull Boolean smoker, @NotNull @Positive Double bmi, List<String> preExistingConditions, @NotBlank String coverageType, @NotNull @Positive Double sumInsured, @NotBlank String incomeBracket, @NotNull @Min(0) @Max(100) Integer healthRiskScore) {}
