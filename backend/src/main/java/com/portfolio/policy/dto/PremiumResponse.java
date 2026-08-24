package com.portfolio.policy.dto;
public record PremiumResponse(double basePremium, double finalPremium, Breakdown breakdown, String recommendedTier, double confidence) { public record Breakdown(double ageFactor, double smokerFactor, double bmiFactor, double conditionsLoading) {} }
