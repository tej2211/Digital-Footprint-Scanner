from fastapi import FastAPI, HTTPException, File, UploadFile
from pydantic import BaseModel
from .hibp_client import check_email_breaches, check_password_pwned
from .notifications import send_fcm_notification
import os

app = FastAPI(title="FootprintScanner Backend")

class ScanEmailRequest(BaseModel):
    email: str

@app.post("/scan/email")
async def scan_email(req: ScanEmailRequest):
    breaches = await check_email_breaches(req.email)
    return {"email": req.email, "breaches": breaches}

@app.post("/upload_recovery")
async def upload_recovery(file: UploadFile = File(...)):
    # Save recovery packet encrypted blob for manual processing with user consent.
    data = await file.read()
    path = os.path.join("/data/recovery_uploads", file.filename)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(data)
    return {"ok": True, "path": path}
