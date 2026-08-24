package com.portfolio.policy.repository;
import com.portfolio.policy.model.Quote; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface QuoteRepository extends JpaRepository<Quote, Long> { List<Quote> findAllByOrderByTimestampDesc(); }
