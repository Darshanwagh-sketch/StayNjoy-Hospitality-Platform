# 🏨 StayNjoy Resorts — Luxury Hospitality Platform

A full-stack enterprise hospitality booking platform designed for luxury destinations across India (Udaipur, Goa, Jaipur, Mumbai).

Built with **Spring Boot 3 (Java 17/21)**, **Spring Security (JWT)**, **Spring Data JPA**, and **React 18 (Vite + SaaS Design System)**.

---

## 🌟 Key Features

* **Resort & Room Discovery**: Public GET access to luxury Indian resort properties, room categories, pricing, and amenities.
* **Instant Booking System**: Real-time stay duration calculations, guest info validation, and conflict prevention.
* **Automated HTML Email Receipts**: Asynchronous background dispatching of HTML booking receipts using Spring `@Async` and `JavaMailSender`.
* **Role-Based Access Control (RBAC)**:
  * **CUSTOMER**: Search hotels, reserve rooms, view stay history.
  * **HOTEL_ADMIN**: Manage resort rooms, pricing, and resort booking analytics.
  * **SUPER_ADMIN**: Manage platform users, all hotels, and system total revenue.
* **StayNjoy AI Concierge Chatbot**: Embedded floating AI assistant widget for instant customer support.
* **Modern SaaS Design**: Deep Navy (`#0F172A`) + Electric Blue (`#2563EB`) + Cyan (`#06B6D4`) UI theme with responsive cards.

---

## 🛠️ Tech Stack

* **Backend**: Java 17/21, Spring Boot 3.3.4, Spring Security + JWT, Spring Data JPA, Hibernate 6, H2 / PostgreSQL, JavaMailSender.
* **Frontend**: React 18, Vite, Tailwind CSS, Lucide Icons, Axios.

---

## 🚀 Quick Start Guide

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/StayNjoy-Hospitality-Platform.git
cd StayNjoy-Hospitality-Platform
```

### 2. Start Backend Server
```bash
cd backend
.\mvnw.cmd spring-boot:run
```
*Backend runs on `http://localhost:8080`*

### 3. Start Frontend Dev Server
```bash
cd frontend
npm install
npm run dev
```
*Frontend runs on `http://localhost:5173`*

---

## 🔐 Credentials (Seeded Data)

| Role | Email | Password |
| :--- | :--- | :--- |
| **Customer** | `john@example.com` | `user123` |
| **Hotel Admin** | `hoteladmin@tajlake.com` | `admin123` |
| **Super Admin** | `admin@hospitality.com` | `admin123` |

---

## 📄 License

This project is open-source under the MIT License.
