# MyPG – PG & Room Search App (Android)

MyPG is an Android application that helps users find PGs/rooms with key details like rent, room type, gender preference, amenities, and location. The app supports two roles: **PG Seeker (Guest)** and **PG Owner (Host)**.

> Built with Kotlin + modern Android architecture and Firebase backend.

---

## ✨ Features

### ✅ Guest (PG Seeker)
- Browse PG/room listings with images and details
- Search listings and view PG cards in a smooth list UI
- Apply filters (e.g., price sort, room type, gender, etc.)
- View listing details screen
- Save favorites/bookmarks *(local storage – planned / optional)*

### ✅ Host (PG Owner)
- Add a PG listing with details (rent, room type, rules, etc.)
- Upload listing data to Firebase *(Firestore)*
- Manage/update listings *(planned)*

### 🔐 Authentication
- Login/Signup using Firebase Authentication
- Role-based flow (Host vs Guest)

---

## 🛠 Tech Stack

- **Language:** Kotlin (some modules in Java/XML if needed)
- **UI:** XML + Material Design Components  
- **Architecture:** MVVM (ViewModel + LiveData/StateFlow)
- **Backend:** Firebase Authentication, Firebase Firestore
- **Image Loading:** Coil
- **Networking (optional):** Retrofit / REST APIs (if used)
- **Local Storage (optional):** Room / SharedPreferences (for bookmarks)
- **Tools:** Android Studio, Git/GitHub

---

## 📱 Screens (Sample)

- Login / Signup
- Guest Listing Screen
- Filters Screen
- Listing Detail Screen
- Host Add Listing Screen *(if implemented)*
