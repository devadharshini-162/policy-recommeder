from pathlib import Path
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

import json

app = FastAPI(title="Policy Tier ML Service")
model_path = Path(__file__).with_name("tier_model.pkl")
if not model_path.exists():
    raise RuntimeError("Model is missing. Run: python train.py")
model = joblib.load(model_path)

tier_info_path = Path(__file__).with_name("tier_info.json")
with open(tier_info_path, "r") as f:
    tier_info = json.load(f)

class TierInput(BaseModel):
    age: int = Field(ge=18, le=75)
    incomeBracket: str
    healthRiskScore: int = Field(ge=0, le=100)
    sumInsured: float = Field(gt=0)
    coverageType: str

@app.get("/health")
def health(): return {"status": "ok"}

@app.post("/predict-tier")
def predict_tier(data: TierInput):
    if data.incomeBracket.upper() not in {"LOW", "MID", "HIGH"} or data.coverageType.upper() not in {"LIFE", "HEALTH", "VEHICLE"}:
        raise HTTPException(400, "Use incomeBracket LOW/MID/HIGH and coverageType LIFE/HEALTH/VEHICLE")
    row = pd.DataFrame([{"age": data.age, "incomeBracket": data.incomeBracket.upper(), "healthRiskScore": data.healthRiskScore, "sumInsured": data.sumInsured, "coverageType": data.coverageType.upper()}])
    tier = model.predict(row)[0]
    confidence = float(model.predict_proba(row)[0].max())
    return {
        "recommendedTier": tier,
        "confidence": round(confidence, 3),
        "tierData": tier_info.get(data.coverageType.upper(), {})
    }
