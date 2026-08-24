package com.portfolio.policy.dto;
import java.util.List;
public record TierInfo(String tagline, List<String> benefits, List<String> risks, String idealFor) {}
