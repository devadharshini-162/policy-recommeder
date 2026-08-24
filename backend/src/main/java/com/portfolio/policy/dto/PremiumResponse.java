package com.portfolio.policy.dto;
import java.util.Map;
public record PremiumResponse(double basePremium, double finalPremium, Breakdown breakdown, String recommendedTier, double confidence, Map<String, TierInfo> tierData) { public record Breakdown(double ageFactor, double smokerFactor, double bmiFactor, double conditionsLoading) {} }
