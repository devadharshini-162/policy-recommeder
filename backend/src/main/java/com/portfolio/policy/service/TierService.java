package com.portfolio.policy.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.policy.dto.*; import org.springframework.beans.factory.annotation.*; import org.springframework.stereotype.Service; import org.springframework.web.client.RestClient; import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import java.util.Map;
@Service public class TierService {
 private static final Logger log = LoggerFactory.getLogger(TierService.class); private final RestClient client; @Value("${ml.service.url}") private String mlUrl; private final ObjectMapper mapper;
 public TierService(RestClient client, ObjectMapper mapper){this.client=client;this.mapper=mapper;}
 public TierResponse recommend(TierRequest request) {
  try {
   Map<String, Object> payload = Map.of(
    "age", request.age(), "incomeBracket", request.incomeBracket().toUpperCase(),
    "healthRiskScore", request.healthRiskScore(), "sumInsured", request.sumInsured(),
    "coverageType", request.coverageType().toUpperCase());
   Map body=client.post().uri(mlUrl+"/predict-tier").body(payload).retrieve().body(Map.class);
   Map<String, TierInfo> tierData = mapper.convertValue(body.get("tierData"), new TypeReference<Map<String, TierInfo>>() {});
   return new TierResponse((String)body.get("recommendedTier"), ((Number)body.get("confidence")).doubleValue(), tierData);
  }
  catch(Exception e) { log.error("Tier service request to {} failed", mlUrl, e); throw new IllegalStateException("ML recommendation service is unavailable. Verify FastAPI is running on " + mlUrl); }
 }
}
