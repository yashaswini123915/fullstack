# 📖 Personal Feedback Guestbook

A lightweight Full-Stack application that allows users to submit feedback through a web interface, which is then processed by a Java server and stored in a persistent SQLite database.

---

## 🎥 Project Demonstration
Check out the application in action:

[Watch the Demo Video](https://github.com/user-attachments/assets/d24cb03c-8b1c-49c0-ae76-8d5e0b0988d1)

---

## 🚀 Features
* **Asynchronous Submission:** Uses JavaScript Fetch API for a smooth user experience.
* **Custom Java Backend:** A robust HTTP Server built from scratch (no heavy frameworks).
* **Automated DB Setup:** The server automatically creates the SQLite table and adds default "Admin" values if the database is empty.
* **Persistent Storage:** All entries are stored locally in a `.db` file using JDBC.

---

## 🛠️ Tech Stack
| Layer | Technology |
| :--- | :--- |
| **Frontend** | HTML5, CSS3, JavaScript (Fetch API) |
| **Backend** | Java (HttpServer, JDBC) |
| **Database** | SQLite |

---

## 📂 Project Structure
* `index.html` / `script.js` - The Presentation Layer (Frontend).
* `Guestbook.java` - The Application Layer (Server logic).
* `guestbook.db` - The Data Layer (SQLite Database).

---

## ⚙️ How to Run
1. **Database Driver:** Ensure `sqlite-jdbc-3.7.2.jar` is added to your Java Build Path.
2. **Start Server:** Run the `Guestbook.java` file in Eclipse or your preferred IDE.
3. **Open Frontend:** Open `index.html` in any modern web browser.
4. **Submit:** Enter your name and message. Check the Java console and the database for the successful entry!

---

## 🛡️ Challenges Overcome
* **CORS Management:** Implemented custom headers to allow the browser and server to communicate securely.
* **Dependency Management:** Configured JDBC drivers for seamless SQLite connectivity.
* **Path Handling:** Resolved absolute path issues for local database file storage.

---
*Created by [Your Name]*
