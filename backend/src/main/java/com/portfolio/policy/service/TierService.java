package com.portfolio.policy.service;
import com.portfolio.policy.dto.*; import org.springframework.beans.factory.annotation.*; import org.springframework.stereotype.Service; import org.springframework.web.client.RestClient; import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import java.util.Map;
@Service public class TierService {
 private static final Logger log = LoggerFactory.getLogger(TierService.class); private final RestClient client; @Value("${ml.service.url}") private String mlUrl;
 public TierService(RestClient client){this.client=client;}
 public TierResponse recommend(TierRequest request) {
  try {
   Map<String, Object> payload = Map.of(
    "age", request.age(), "incomeBracket", request.incomeBracket().toUpperCase(),
    "healthRiskScore", request.healthRiskScore(), "sumInsured", request.sumInsured(),
    "coverageType", request.coverageType().toUpperCase());
   Map body=client.post().uri(mlUrl+"/predict-tier").body(payload).retrieve().body(Map.class);
   return new TierResponse((String)body.get("recommendedTier"), ((Number)body.get("confidence")).doubleValue());
  }
  catch(Exception e) { log.error("Tier service request to {} failed", mlUrl, e); throw new IllegalStateException("ML recommendation service is unavailable. Verify FastAPI is running on " + mlUrl); }
 }
}
