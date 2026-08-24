# Insurance Policy Premium Calculator

A portfolio project with a Spring Boot API, MariaDB quote storage, a FastAPI/scikit-learn tier recommender, and a React UI. Premiums are rules-based and their factors are returned for explainability; policy tiers are predicted by the local ML service.

## Prerequisites

- Java 17+, Maven 3.9+, MariaDB 10.6+
- Python 3.10+
- Node.js 18+

## 1. Create the database

```sql
CREATE DATABASE policy_recommender;
```

The backend also uses `createDatabaseIfNotExist=true` by default, but creating it explicitly makes credentials/setup clearer.

## 2. Start the ML service (port 8000)

```bash
cd ml-service
python -m venv .venv
source .venv/bin/activate        # Windows: .venv\\Scripts\\activate
pip install -r requirements.txt
python train.py                  # writes tier_model.pkl and an 800-row CSV
uvicorn main:app --reload --port 8000
```

Test it at `http://localhost:8000/docs` or `GET /health`.

## 3. Start Spring Boot (port 8080)

```bash
cd backend
DB_USERNAME=policy_app DB_PASSWORD=policy_password mvn spring-boot:run
```

Optional variables: `DB_URL` (default `jdbc:mariadb://localhost:3306/policy_recommender`), `ML_SERVICE_URL` (default `http://127.0.0.1:8000`), and `SERVER_PORT` (default `8080`).

Endpoints:

- `POST /api/calculate-premium` calculates and persists a quote, including ML tier recommendation.
- `POST /api/recommend-tier` proxies a tier-only prediction to FastAPI.
- `GET /api/quotes` returns newest saved quotes first.

Example calculation payload:

```json
{"age":35,"gender":"FEMALE","smoker":false,"bmi":24.5,"preExistingConditions":["Asthma"],"coverageType":"HEALTH","sumInsured":500000,"incomeBracket":"MID","healthRiskScore":28}
```

Premium rules: LIFE/HEALTH/VEHICLE base premiums are ₹5,000/₹4,000/₹3,000; age factors are 1.0 (≤30), 1.2 (≤45), 1.5 (≤60), 2.0 (61+); smoker is 1.5; BMI is 1.15 above 30 and 1.3 above 35; each pre-existing condition adds ₹500.

## 4. Start React (port 5173)

```bash
cd frontend
npm install
npm run dev
```

Open the shown Vite URL (normally `http://localhost:5173`). Set `VITE_API_URL` if the backend is not on `http://localhost:8080`.

## Project layout

```
backend/     Spring Boot API and JPA quote entity
ml-service/  synthetic-data trainer and FastAPI inference endpoint
frontend/    Vite React interface
```
