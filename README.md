# Library Management System

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-blue.svg)
![Swing](https://img.shields.io/badge/Swing-GUI-green.svg)
![Maven](https://img.shields.io/badge/Maven-3.8.6-red.svg)

A robust Library Management System built with Java Swing and PostgreSQL. This application provides an intuitive graphical interface to manage books and borrowing records in a library setting.

## 📚 Features

### Book Management
- Add new books with details (ID, title, author, publication year, category)
- Update existing book records
- Delete books from the database
- Search books by ID, title, author, or category
- View all books in a paginated table

### Borrowing Management
- Record book borrowings with borrower details
- Set borrow and return dates using calendar interface
- Track borrowed books and due dates
- Search borrowing records by borrower name or book details
- Update or delete borrowing records

## 🔧 Technologies Used

- **Java 17**: Core programming language
- **Swing**: GUI toolkit for Java
- **PostgreSQL**: Database management system
- **JDBC**: Database connectivity
- **Maven**: Dependency management and build automation
- **JCalendar**: Date picker component

## 📋 Prerequisites

Before running this application, you need to have installed:

1. Java Development Kit (JDK) 17 or later
2. PostgreSQL 12 or later
3. Maven 3.6 or later

## ⚙️ Configuration

The application connects to a PostgreSQL database with these default settings:

```
Database: perpustakaan
Username: postgres
Password: postgres
Host: localhost
Port: 5432
```

You can modify these settings in `src/main/java/com/example/DatabaseConnection.java`.

## 🚀 Getting Started

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system
```

2. Build the project with Maven:
```bash
mvn clean package
```

3. Run the application:
```bash
java -jar target/demo-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 📖 Usage Guide

### Adding a New Book
1. Navigate to the "Manajemen Buku" tab
2. Fill in the book details in the form fields
3. Click "Tambah" button
4. The book will be added to the database and appear in the table

### Recording a Book Borrowing
1. Navigate to the "Peminjaman" tab
2. Fill in the borrower's name
3. Set the borrow date and expected return date
4. Select a book from the dropdown menu
5. Click "Tambah" button
6. The borrowing record will be added to the database

## 🏗️ Project Structure

```
src/main/java/
└── com/
    └── example/
        ├── Main.java                 # Application entry point
        ├── DatabaseConnection.java   # Database connection handling
        ├── model/                    # Data models
        │   ├── Buku.java             # Book entity
        │   └── Peminjaman.java       # Borrowing entity
        ├── dao/                      # Data Access Objects
        │   ├── BukuDAO.java          # Book database operations
        │   └── PeminjamanDAO.java    # Borrowing database operations
        └── ui/                       # User Interface components
            ├── MainApp.java          # Main application window
            ├── BukuPanel.java        # Book management panel
            └── PeminjamanPanel.java  # Borrowing management panel
```

## 🛠️ Future Enhancements

- User authentication system
- Book availability status tracking
- Overdue notices and fine calculation
- Barcode scanning for books
- Report generation for library statistics
- Dark mode UI option

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- Your Name - Initial work

## 👏 Acknowledgments

- JCalendar library by Kai Toedter
- All the contributors who spend time to improve this project