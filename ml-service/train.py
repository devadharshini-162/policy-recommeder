"""Generate a reproducible synthetic data set and train the policy tier model."""
from pathlib import Path
import random
import joblib
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder
from sklearn.tree import DecisionTreeClassifier

random.seed(42)
rows = []
for _ in range(800):
    age = random.randint(18, 75)
    income = random.choices(["LOW", "MID", "HIGH"], weights=[.3, .45, .25])[0]
    risk = random.randint(0, 100)
    insured = random.randrange(100_000, 2_100_000, 50_000)
    coverage = random.choice(["LIFE", "HEALTH", "VEHICLE"])
    # The model learns this intentionally simple quoting policy.
    score = {"LOW": 0, "MID": 1, "HIGH": 2}[income] + (insured >= 1_000_000) + (risk < 45) + (age < 55)
    tier = "PREMIUM" if score >= 4 else "STANDARD" if score >= 2 else "BASIC"
    rows.append([age, income, risk, insured, coverage, tier])

columns = ["age", "incomeBracket", "healthRiskScore", "sumInsured", "coverageType"]
df = pd.DataFrame(rows, columns=columns + ["policyTier"])
preprocess = ColumnTransformer([("categories", OneHotEncoder(handle_unknown="ignore"), ["incomeBracket", "coverageType"])], remainder="passthrough")
model = Pipeline([("preprocess", preprocess), ("classifier", DecisionTreeClassifier(max_depth=5, random_state=42))])
model.fit(df[columns], df.policyTier)
joblib.dump(model, Path(__file__).with_name("tier_model.pkl"))
df.to_csv(Path(__file__).with_name("synthetic_policy_data.csv"), index=False)
print("Saved tier_model.pkl and synthetic_policy_data.csv (800 rows)")
