# MediCare Plus 🩺📱

> **Your Health, Our Priority**

**MediCare Plus** is an intuitive native Android application designed to address critical healthcare accessibility gaps, aligned with UN **Sustainable Development Goal 3 (SDG 3): Good Health and Well-Being**. The application provides a centralized platform for managing doctor appointments, tracking medication schedules, and verifying user accounts.

---

## 🌟 Key Features & Modules

### 1. 📅 Doctor Appointment Module
*(Developed by Student ID: ITBIN-2414-0014)*
* **Doctor Selection:** Browse and select medical specialists by field.
* **Real-time Scheduling:** Select available dates and custom time slots.
* **Appointment Confirmation:** Direct confirmation view with instant cancellation support.
* **CRUD Operations:** Create bookings, Read schedules, Update details, and Delete (cancel) appointments.

### 2. 💊 Medicine Manager Module
*(Developed by Student ID: ITBIN-2414-0008)*
* **Full CRUD Management:** Single-screen interface to Add, View, Edit, and Delete medication records.
* **RecyclerView Integration:** Clean dynamic list implementation using `MedicineAdapter`.
* **Repository Pattern:** Managed local data flow through `MedicineRepository`.

### 3. 🔐 User Authentication & UI Features
* Account registration (`RegisterForm`) with OTP mobile verification (`OTPForm`).
* Secure login interface (`LoginForm`) with password visibility toggle.
* Custom gradient components and support for both Light and Dark modes.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin / Java
* **UI Design:** Figma (Prototyping & Layout design)
* **IDE:** Android Studio
* **Package Name:** `com.theekshana.mediapp`
* **Architecture/Components:** Activity-based architecture, Repository Pattern (`MedicineRepository`), Custom Adapters (`MedicineAdapter`), Vector Drawables, and Custom XML Layouts.

---

## 📁 Project Structure
com.theekshana.mediapp
├── AppointmentSuccess
├── ConfirmForm
├── DoctorAppointment
├── HomePage
├── LoadingActivity
├── LoginForm
├── MainActivity.kt
├── Medicine
├── MedicineAdapter
├── MedicineRepository
├── MedicinemanagePage
├── OTPForm
└── RegisterForm

---

## 🎨 Branding & Color Palette

* **Primary Color:** Deep Navy Blue (`#1A3A6B`)
* **Secondary Color:** Bright Blue (`#2563EB`)
* **Accent Color:** Vibrant Green (`#10B981`)

---

## 🚀 Getting Started & Setup Instructions

### Prerequisites
* **Android Studio** (Ladybug / Jellyfish or higher recommended)
* **JDK:** Version 17 or higher
* **Android SDK:** API Level 24 (Android 7.0) or higher
* An **Android Emulator** or physical Android device with USB Debugging enabled.

### Installation Steps

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/your-username/MediCare-Plus.git](https://github.com/your-username/MediCare-Plus.git)
