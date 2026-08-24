package com.portfolio.policy.controller;
import com.portfolio.policy.dto.*; import com.portfolio.policy.model.Quote; import com.portfolio.policy.service.*; import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api") @CrossOrigin(origins="${FRONTEND_URL:http://localhost:5173}") public class QuoteController {
 private final PremiumService premiumService; private final TierService tierService; public QuoteController(PremiumService p,TierService t){premiumService=p;tierService=t;}
 @PostMapping("/calculate-premium") public PremiumResponse calculate(@Valid @RequestBody QuoteRequest request){return premiumService.calculate(request);}
 @PostMapping("/recommend-tier") public TierResponse recommend(@Valid @RequestBody TierRequest request){return tierService.recommend(request);}
 @GetMapping("/quotes") public List<Quote> quotes(){return premiumService.history();}
}
