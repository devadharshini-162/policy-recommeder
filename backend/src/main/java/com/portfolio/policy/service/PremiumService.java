package com.portfolio.policy.service;
import com.portfolio.policy.dto.*; import com.portfolio.policy.model.Quote; import com.portfolio.policy.repository.QuoteRepository; import org.springframework.stereotype.Service; import java.time.LocalDateTime; import java.util.*;
@Service public class PremiumService {
 private final QuoteRepository repo; private final TierService tierService;
 public PremiumService(QuoteRepository repo, TierService tierService){this.repo=repo;this.tierService=tierService;}
 public PremiumResponse calculate(QuoteRequest r) {
  String type=r.coverageType().toUpperCase(); double base=switch(type){case "LIFE"->5000;case "HEALTH"->4000;case "VEHICLE"->3000;default->throw new IllegalArgumentException("coverageType must be LIFE, HEALTH, or VEHICLE");};
  double ageFactor=r.age()<=30?1.0:r.age()<=45?1.2:r.age()<=60?1.5:2.0; double smokerFactor=r.smoker()?1.5:1.0; double bmiFactor=r.bmi()>35?1.3:r.bmi()>30?1.15:1.0; double loading=(r.preExistingConditions()==null?0:r.preExistingConditions().size()*500);
  double finalPremium=Math.round((base*ageFactor*smokerFactor*bmiFactor+loading)*100.0)/100.0;
  TierResponse tier=tierService.recommend(new TierRequest(r.age(),r.incomeBracket(),r.healthRiskScore(),r.sumInsured(),type)); save(r, finalPremium, tier.recommendedTier());
  return new PremiumResponse(base,finalPremium,new PremiumResponse.Breakdown(ageFactor,smokerFactor,bmiFactor,loading),tier.recommendedTier(),tier.confidence(),tier.tierData());
 }
 private void save(QuoteRequest r,double premium,String tier){ Quote q=new Quote(); q.setTimestamp(LocalDateTime.now());q.setAge(r.age());q.setGender(r.gender());q.setSmoker(r.smoker());q.setBmi(r.bmi());q.setPreExistingConditions(r.preExistingConditions());q.setCoverageType(r.coverageType().toUpperCase());q.setSumInsured(r.sumInsured());q.setIncomeBracket(r.incomeBracket());q.setHealthRiskScore(r.healthRiskScore());q.setFinalPremium(premium);q.setRecommendedTier(tier);repo.save(q); }
 public List<Quote> history(){return repo.findAllByOrderByTimestampDesc();}
}
