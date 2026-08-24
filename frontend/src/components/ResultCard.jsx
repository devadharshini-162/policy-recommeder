import React from 'react';
const money=n=>new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:0}).format(n);
export default function ResultCard({result}){
  const b=result.breakdown;
  const tiers = result.tierData || {};
  const rec = result.recommendedTier;
  const recInfo = tiers[rec];
  
  return (
    <>
      <aside className="card result">
        <p className="eyebrow">Your estimated annual premium</p>
        <h2>{money(result.finalPremium)}</h2>
        <p>Base premium: {money(result.basePremium)}</p>
        <h3>How we calculated it</h3>
        <ul>
          <li>Age factor <strong>×{b.ageFactor}</strong></li>
          <li>Smoker factor <strong>×{b.smokerFactor}</strong></li>
          <li>BMI factor <strong>×{b.bmiFactor}</strong></li>
          <li>Conditions loading <strong>+{money(b.conditionsLoading)}</strong></li>
        </ul>
        <div className="tier">
          <span>Recommended tier</span>
          <strong>{result.recommendedTier}</strong>
          <small>{Math.round(result.confidence*100)}% model confidence</small>
        </div>
      </aside>
      
      {Object.keys(tiers).length > 0 && (
        <div className="tiers-comparison">
          {["BASIC", "STANDARD", "PREMIUM"].map(tierKey => {
            const info = tiers[tierKey];
            if (!info) return null;
            const isRec = tierKey === rec;
            return (
              <div key={tierKey} className={`tier-card ${isRec ? 'recommended' : ''}`}>
                {isRec && <span className="badge">RECOMMENDED FOR YOU</span>}
                <h3>{tierKey}</h3>
                <p className="tagline">"{info.tagline}"</p>
                
                {isRec && (
                  <div className="ideal-for explain">
                    <strong>Why this tier?</strong><br/>
                    {info.idealFor}
                  </div>
                )}
                {!isRec && (
                  <div className="ideal-for">
                    {info.idealFor}
                  </div>
                )}

                <div className="tier-section">
                  <h4>Benefits</h4>
                  <ul>{info.benefits.map((benefit, i) => <li key={i}>{benefit}</li>)}</ul>
                </div>
                
                <div className="tier-section">
                  <h4>Risks / Trade-offs</h4>
                  <ul>{info.risks.map((risk, i) => <li key={i}>{risk}</li>)}</ul>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </>
  );
}
