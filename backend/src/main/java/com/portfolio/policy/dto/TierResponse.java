package com.portfolio.policy.dto;
import java.util.Map;
public record TierResponse(String recommendedTier, double confidence, Map<String, TierInfo> tierData) {}
