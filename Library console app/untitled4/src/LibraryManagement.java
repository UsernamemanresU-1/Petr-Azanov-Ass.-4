import java.sql.*;
import java.util.Scanner;

public class LibraryManagement {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Gtntymrf1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Library!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your 3-digit password: ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (authenticateUser(conn, name, password)) {
                System.out.println("Login successful!\n");

                if (name.equals("admin")) {
                    adminMenu(conn, scanner);
                } else {
                    userMenu(conn, scanner, name);
                }
            } else {
                System.out.println("Invalid credentials. Exiting.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean authenticateUser(Connection conn, String name, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM library_members WHERE name = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private static void userMenu(Connection conn, Scanner scanner, String userName) throws SQLException {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View available books");
            System.out.println("2. Rent a book");
            System.out.println("3. Return a book");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listBooks(conn, false);
                    break;
                case 2:
                    rentBook(conn, scanner, userName);
                    break;
                case 3:
                    returnBook(conn, scanner, userName);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void adminMenu(Connection conn, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add book");
            System.out.println("2. Remove book");
            System.out.println("3. Add user");
            System.out.println("4. Remove user");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook(conn, scanner);
                    break;
                case 2:
                    removeBook(conn, scanner);
                    break;
                case 3:
                    addUser(conn, scanner);
                    break;
                case 4:
                    removeUser(conn, scanner);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void listBooks(Connection conn, boolean showRented) throws SQLException {
        String sql = "SELECT * FROM books WHERE rented = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, showRented);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\nBooks:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("title") + " by " + rs.getString("author"));
            }
        }
    }

    private static void rentBook(Connection conn, Scanner scanner, String userName) throws SQLException {
        listBooks(conn, false);
        System.out.print("\nEnter book ID to rent: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE books SET rented = TRUE WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Book rented successfully.");
            } else {
                System.out.println("Book not available.");
            }
        }
    }

    private static void returnBook(Connection conn, Scanner scanner, String userName) throws SQLException {
        System.out.print("\nEnter book ID to return: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE books SET rented = FALSE WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Invalid book ID.");
            }
        }
    }
    private static void addBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("\nEnter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();

        String sql = "INSERT INTO books (title, author) VALUES (?, ?) ON CONFLICT (title) DO NOTHING";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book added successfully.");
            } else {
                System.out.println("Book already exists.");
            }
        }
    }

    private static void removeBook(Connection conn, Scanner scanner) throws SQLException {
        listBooks(conn, true);  // Показываем список книг, включая арендованные
        System.out.print("\nEnter book ID to remove: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book removed successfully.");
            } else {
                System.out.println("Invalid book ID.");
            }
        }
    }
    private static void addUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("\nEnter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a 3-digit password: ");
        String password = scanner.nextLine();

        if (password.length() != 3 || !password.matches("\\d+")) {
            System.out.println("Password must be exactly 3 digits.");
            return;
        }

        String sql = "INSERT INTO library_members (name, password) VALUES (?, ?) ON CONFLICT (name) DO NOTHING";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("User already exists.");
            }
        }
    }
    private static void removeUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("\nEnter user name to remove: ");
        String name = scanner.nextLine();

        String sql = "DELETE FROM library_members WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User removed successfully.");
            } else {
                System.out.println("User not found.");
            }
        }
    }
}

