#  Expense Tracker — Android App

A clean, offline-first personal finance management application built natively for Android using **Java** and **SQLite**. Track your income and expenses, view real-time balance summaries, analyze spending statistics, convert currencies, and manage transactions — all without requiring an internet connection.

---

##  Features

- **User Authentication** — Register and log in with local account management via SQLite
- **Dashboard Overview** — At-a-glance total balance, income, and expense summaries (in NRS) with quick-action cards
- **Add Transactions** — Log income or expense entries with category, amount, description, and date
- **Transaction History** — View, update, and delete past transactions in a scrollable RecyclerView list
- **Delete Confirmation** — Safe deletion using `DialogFragment` confirmation dialogs
- **Budget Summary** — Category-wise breakdown of expenses and income displayed in a `ListView`, with toggle between expense/income views
- **Category Grid** — Visual `GridView` display of all categories with totals and transaction counts
- **Statistics** — Monthly income vs. expense analysis presented in a `TableLayout` with net balance calculations using `ConstraintLayout`
- **Currency Converter** — Convert NRS to 8 major currencies (USD, EUR, GBP, INR, AUD, CAD, JPY, CNY) using live exchange rates via REST API and JSON parsing
- **Bottom Navigation** — Seamless switching between Dashboard and Transaction History using Fragments
- **Options Menu** — Quick access to Statistics, Currency Converter, Logout, Refresh, and About from the toolbar
- **Session Management** — Persistent login sessions using `SharedPreferences`

---

##  Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java |
| UI | XML Layouts, Material Design Components, CardView, ConstraintLayout |
| Database | SQLite (via `SQLiteOpenHelper`) |
| Architecture | Activities + Fragments |
| Navigation | BottomNavigationView |
| Session | SharedPreferences |
| Networking | `HttpURLConnection` + `org.json` (for Currency Converter API) |
| Lists | RecyclerView, ListView, GridView |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

---

## 📱 Screens

| Screen | Description |
|--------|-------------|
| **Login** | Email & password authentication |
| **Register** | New user sign-up with name, email & password |
| **Dashboard** | Balance card with income/expense breakdown + 6 quick-action cards |
| **Transactions** | Full transaction history with delete support |
| **Add Transaction** | Form to add new income or expense entries with category selection |
| **Budget Summary** | ListView-based category breakdown with expense/income toggle |
| **Category Grid** | GridView showing all categories with spending totals |
| **Statistics** | Monthly income vs. expense table with totals and net balance |
| **Currency Converter** | Convert NRS amounts to foreign currencies using live API rates |

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/jinesh-basnet/Expense-tracker.git
   ```
2. Open the project in **Android Studio**
3. Build and run on an emulator or physical device (API 24+)

> **Note:** The Currency Converter feature requires an active internet connection. All other features work fully offline.

---

##  Project Structure

```
app/src/main/java/com/example/mobileprogrammingproject/
├── LoginActivity.java                     User login screen
├── RegisterActivity.java                  User registration screen
├── HomeActivity.java                      Main host activity with bottom nav & toolbar
├── DashboardFragment.java                Dashboard with balance summary & quick-actions
├── TransactionFragment.java             #Transaction history list
├── AddTransactionActivity.java           Add/edit transaction form
├── TransactionDeleteDialogFragment.java  Delete confirmation dialog
├── Transaction.java                      Transaction data model
├── TransactionAdapter.java               RecyclerView adapter for transactions
├── BudgetSummaryActivity.java            Budget summary with ListView
├── BudgetSummaryAdapter.java             ListView adapter for budget categories
├── CategoryGridActivity.java             Category grid with GridView
├── CategoryGridAdapter.java              GridView adapter for category items
├── StatisticsActivity.java               Monthly statistics with TableLayout
├── CurrencyConverterActivity.java        Currency converter with API integration
└── DatabaseHelper.java                   SQLite database helper (users + transactions)
```

```
app/src/main/res/layout/
├── activity_login.xml                   Login screen layout
├── activity_register.xml                 Registration screen layout
├── activity_home.xml                     Main container with bottom navigation
├── fragment_dashboard.xml                Dashboard fragment layout
├── fragment_transaction.xml              Transaction list fragment layout
├── activity_add_transaction.xml          Add transaction form layout
├── activity_budget_summary.xml           Budget summary with ListView
├── activity_category_grid.xml            Category grid with GridView
├── activity_statistics.xml               Statistics with ConstraintLayout & TableLayout
├── activity_currency_converter.xml       Currency converter layout
├── item_transaction.xml                  Transaction list item
├── item_budget_summary.xml               Budget summary list item
└── item_category_grid.xml                Category grid item
```

---

##  Android Components Used

| Component | Usage |
|-----------|-------|
| **Activity** | LoginActivity, RegisterActivity, HomeActivity, AddTransactionActivity, BudgetSummaryActivity, CategoryGridActivity, StatisticsActivity, CurrencyConverterActivity |
| **Fragment** | DashboardFragment, TransactionFragment |
| **DialogFragment** | TransactionDeleteDialogFragment |
| **RecyclerView** | Transaction history list |
| **ListView** | Budget summary by category |
| **GridView** | Category grid display |
| **BottomNavigationView** | Dashboard ↔ Transactions navigation |
| **Options Menu** | Toolbar overflow menu (Refresh, Statistics, Currency, Logout, About) |
| **SharedPreferences** | User session persistence |
| **SQLiteOpenHelper** | Local database for users and transactions |
| **TableLayout** | Monthly statistics table |
| **ConstraintLayout** | Statistics screen layout |
| **CardView** | Dashboard cards and summary displays |
| **HttpURLConnection** | REST API call for currency exchange rates |
| **JSONObject** | Parsing API JSON response |

---

## 📄 License

This project is open source and available for educational purposes.
