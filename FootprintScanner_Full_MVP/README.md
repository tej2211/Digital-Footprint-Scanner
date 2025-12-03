# FootprintScanner - Full MVP Scaffold

This package expands the initial scaffold into a more complete MVP:
- Android app (Kotlin) with Tink-based hybrid crypto placeholders
- AdMob placeholders and consent flow sample
- FastAPI backend with Dockerfile and docker-compose
- Org-side Python unwrap script for recovery packets
- GitHub Actions workflows for Android build and backend tests
- Simple placeholder logo and app icon (SVG/PNG)

**Security note:** This is still a scaffold. Use Tink for encryption primitives; do not roll your own crypto.


## Next steps provided by assistant
- Tink-based RecoveryPacket implementation (Kotlin) included as a starting example.
- Org-side unwrap script (backend/org_unwrap.py)
- Docker Compose to run backend for local testing
- GitHub Actions workflows (Android build + backend lint)
