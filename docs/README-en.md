<h1 height="30" align="center">🏎️ RaceControlHQ API</h1>

<p align="right">
  <code><a href="../README.md">Home</a></code> / 
  <code><a href="README-pt.md">Portuguese</a></code> / 
  <code><a href="README-en.md">English</a></code>
</p>

**API Functional Documentation**

**Version:** 1.0  
**Author:** Mateus Silva  
**Date:** April, 2026  
**Environment:** Development

---

## 📌 Summary
1. [📌 About Version 1.0](#1-about-version-10)
2. [🎯 System Objective](#2-system-objective)
3. [📝 Functional Scope](#3-functional-scope)
4. [⚖️ Business Rules](#4-business-rules)
5. [🛠️ API Resources](#5-api-resources)
6. [💻 Tech Stack](#6-tech-stack)

---

## 1. 📌 About Version 1.0
This initial version focuses on core league management, providing full control over competition structures. The system is designed to be open and flexible for any organizer looking to manage their categories professionally.

## 2. 🎯 System Objective
**RaceControlHQ** is a solution developed to automate the complex logistics of virtual and real motorsport championships. The API centralizes the management of leagues, seasons, and competitors, providing a rules engine that ensures result integrity and driver career history.

* 👥 **Target Audience:** League organizers, simracing community admins, and sports platform developers.
* 🛠️ **Problem Solved:** Eliminates reliance on error-prone manual spreadsheets, resolves scheduling conflicts, and automates complex ranking and scoring calculations.

## 3. 📝 Functional Scope
The API covers the entire competition lifecycle:
* 🏆 **League & Season Management:** Structuring short and long-term championships.
* 👤 **Competitor Management:** Centralized database for drivers and racing teams.
* 📝 **Contracts & Entries:** Recording which driver races for which team in each season.
* 📅 **Race Calendar:** Scheduling and detailing track events.
* 🏁 **Result Recording:** Post-race performance data entry.
* 📊 **Ranking Processing:** Automatic scoring and standings calculation.

## 4. ⚖️ Business Rules (Key Rules)
To ensure data consistency, the system follows strict guidelines:
* ⏳ **Chronological Integrity:** A race can only be scheduled within the date range of its respective season.
* 🤝 **Temporary Binding:** A driver may represent different teams in different seasons, but only one team at a time within the same season.
* 🚫 **Result Consistency:** The system prevents recording results for drivers who are not properly linked to the season.
* 🆔 **Uniqueness:** Each driver has a unique Driver ID to ensure their career history is preserved across different leagues.

## 5. 🛠️ API Resources

### 📂 Module: League & Season
* ✨ **Create League:** Defines name and base structure.
* 🗓️ **Manage Season:** Creates timeframes for competitions (Start/End).
* 🔍 **Check Details:** Displays full season information and progress.

### 🏎️ Module: Pilot & Team
* 👤 **Driver Management:** Create, list, and update competitor data.
* 🛡️ **Team Management:** Create and list racing teams.
* 🔗 **Link Driver:** Assign a driver to a team for a specific season.

### 🏁 Module: Race & Results
* 🚦 **Schedule Race:** Defines date, time, circuit, and category.
* 🏆 **Record Results:** Input of finishing positions, times, and bonuses.
* 📈 **Check Standings:** Generates real-time updated scoring tables.

## 6. 💻 Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3
* **Architecture:** Domain-Driven Design (DDD)
* **Testing:** JUnit 5, Mockito (TDD Focus)

---
[Portuguese version](./README-pt.md)