AI Job Auto Apply 🚀

Tired of manually applying to every job? Just upload a screenshot of the job post 
and your resume — this system reads the details, writes a personalized email, 
and sends it to the HR automatically.

How It Works

1. You upload a job post screenshot + your resume
2. Tesseract OCR reads the job post image
3. PDFBox reads your resume PDF
4. Groq AI extracts — company name, job role, HR email, your skills
5. Groq AI writes a personalized email just for that job
6. You preview the email and edit if needed
7. System sends the email to HR with your resume attached

What You Can Do

- Upload one resume and apply to multiple jobs at once
- Preview the AI generated email before sending
- Edit the email if you want to customize it
- Get a summary of how many applications were sent successfully

Built With

- **Java + Spring Boot** — backend REST APIs
- **Tesseract OCR** — reads text from job post screenshots
- **PDFBox** — reads text from resume PDF
- **Groq LLM API** — extracts details and generates personalized email
- **JavaMail** — sends email via Gmail SMTP

Getting Started

What you need before running:
- Java 17+
- Maven
- Tesseract OCR installed on your machine
- Groq API key → get it free at [console.groq.com](https://console.groq.com)
- Gmail account with App Password enabled

**Step 1 — Clone the project**
```bash
git clone https://github.com/Harshavardhan86/ai-job-auto-apply.git
cd ai-job-auto-apply
```

**Step 2 — Install Tesseract**
```
Windows → https://github.com/UB-Mannheim/tesseract/wiki
Linux   → sudo apt install tesseract-ocr
```

**Step 3 — Add your config in application.properties**
```properties
# Your Groq API key (free at console.groq.com)
xai.api.key=your_groq_api_key_here

# Where Tesseract is installed
tesseract.datapath=C:\\Program Files\\Tesseract-OCR\\tessdata

# Your Gmail + App Password
spring.mail.username=your_gmail@gmail.com
spring.mail.password=your_16_digit_app_password
```

**Step 4 — Run**
```bash
mvn spring-boot:run
```

App runs at `http://localhost:8080`

---

## Try It Out

**Apply to a single job:**
```
POST http://localhost:8080/api/autoapply/apply

Body (form-data):
  jobScreenshots  →  job post screenshot (image)
  resume          →  your resume (PDF)
```

**Preview email before sending:**
```
POST http://localhost:8080/api/autoapply/preview

Body (form-data):
  jobScreenshots  →  job post screenshot
  resume          →  your resume PDF
```

**Apply to multiple jobs at once:**
```
POST http://localhost:8080/api/autoapply/apply

Body (form-data):
  jobScreenshots  →  job1.png
  jobScreenshots  →  job2.png
  jobScreenshots  →  job3.png
  resume          →  resume.pdf
```

---

## Example Response

```json
{
  "candidate": "Harshavardhan Penta",
  "totalJobs": 3,
  "successCount": 3,
  "failedCount": 0,
  "results": [
    {
      "company": "Gravitix Tech Solutions",
      "jobRole": "Software Development Engineer",
      "sentTo": "hr@gravitix.com",
      "status": "success"
    }
  ]
}
```

---

## How Gmail App Password Works

Normal Gmail password won't work for third party apps.
You need to generate an App Password:

```
1. Go to myaccount.google.com
2. Security → 2 Step Verification → turn it on
3. Search "App Passwords"
4. Generate → copy the 16 character password
5. Paste it in application.properties (without spaces)
```

---

## Coming Soon

- [ ] Save application history to MySQL database
- [ ] JWT authentication for multiple users
- [ ] React frontend UI
- [ ] Track application status (sent / viewed / replied)

---

## Author

**Harshavardhan Penta**  
[LinkedIn](https://linkedin.com/in/harshavardhanpenta) • 
[GitHub](https://github.com/Harshavardhan86)
```



Simple, clear, anyone can read and understand it without technical background.
