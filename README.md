Let me explore the project to understand its full scope before writing the description.

Listed directory mobileproject
Listed directory mobileprogrammingproject
Listed directory layout
Viewed AndroidManifest.xml:1-31
Viewed DatabaseHelper.java:1-130
Viewed HomeActivity.java:1-85
Viewed DashboardFragment.java:1-65
Viewed fragment_dashboard.xml:1-172
Viewed build.gradle:1-40

Now I have a thorough understanding of the project. Here's a GitHub description you can use:

---

## Short Description (for the GitHub repo "About" field)

> A modern Android Expense Tracker app built with Java & SQLite — featuring user authentication, income/expense management, dashboard analytics, and bottom navigation with Fragments & DialogFragments.

---

## Full README Description

You can use the following as your `README.md`:

---

#  Expense Tracker — Android App

A clean, offline-first personal finance management application built natively for Android using **Java** and **SQLite**. Track your income and expenses, view real-time balance summaries, and manage transactions — all without requiring an internet connection.

##  Features

- **User Authentication** — Register and log in with local account management via SQLite
- **Dashboard Overview** — At-a-glance total balance, income, and expense summaries (in NRS)
- **Add Transactions** — Log income or expense entries with category, amount, description, and date
- **Transaction History** — View, update, and delete past transactions in a scrollable list
- **Delete Confirmation** — Safe deletion using `DialogFragment` confirmation dialogs
- **Bottom Navigation** — Seamless switching between Dashboard and Transaction History using Fragments
- **Options Menu** — Quick access to Logout, Refresh, and About from the toolbar
- **Session Management** — Persistent login sessions using `SharedPreferences`

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| UI | XML Layouts, Material Design Components, CardView |
| Database | SQLite (via `SQLiteOpenHelper`) |
| Architecture | Activities + Fragments |
| Navigation | Bottom Navigation View |
| Session | SharedPreferences |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

##  Screens

| Screen | Description |
|--------|-------------|
| **Login** | Email & password authentication |
| **Register** | New user sign-up with name, email & password |
| **Dashboard** | Balance card with income/expense breakdown + quick-action cards |
| **Transactions** | Full transaction history with swipe-to-delete |
| **Add Transaction** | Form to add new income or expense entries |

##  Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/jinesh-basnet/Expense-tracker.git
   ```
2. Open the project in **Android Studio**
3. Build and run on an emulator or physical device (API 24+)

## 📂 Project Structure

```
app/src/main/java/com/example/mobileprogrammingproject/
├── LoginActivity.java              # User login screen
├── RegisterActivity.java           # User registration screen
├── HomeActivity.java               # Main host activity with bottom nav & toolbar
├── DashboardFragment.java          # Dashboard with balance summary
├── TransactionFragment.java        # Transaction history list
├── AddTransactionActivity.java     # Add/edit transaction form
├── TransactionDeleteDialogFragment.java  # Delete confirmation dialog
├── Transaction.java                # Transaction data model
├── TransactionAdapter.java         # RecyclerView adapter for transactions
└── DatabaseHelper.java             # SQLite database helper (users + transactions)
```


## 📄 License

This project is open source and available for educational purposes.

---

Feel free to copy this directly into your GitHub repository's `README.md` or trim it down to just the short description for the repo's "About" section. Want me to create the `README.md` file in your project?
