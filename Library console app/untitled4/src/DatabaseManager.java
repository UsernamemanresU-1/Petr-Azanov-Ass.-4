import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Gtntymrf1";  // Укажи свой пароль

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to the database!");

            // Создание таблиц (если их нет)
            createTables(conn);

            // Добавление данных
            insertMember(conn, "David");
            insertBook(conn, "Machine Learning", "Author X");


            // Удаление книги
            deleteBook(conn, 3);

            // Вывод данных
            listMembers(conn);
            listBooks(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createMembersTable = "CREATE TABLE IF NOT EXISTS library_members (id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL)";
        String createBooksTable = "CREATE TABLE IF NOT EXISTS books (id SERIAL PRIMARY KEY, title VARCHAR(255) NOT NULL, author VARCHAR(255) NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createMembersTable);
            stmt.execute(createBooksTable);
        }
    }

    private static void insertMember(Connection conn, String name) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM library_members WHERE name = ?";
        String insertSql = "INSERT INTO library_members (name) VALUES (?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Member already exists: " + name);
                return;  // Не добавляем повторно
            }
        }

        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Added member: " + name);
        }
    }


    private static void insertBook(Connection conn, String title, String author) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM books WHERE title = ? AND author = ?";
        String insertSql = "INSERT INTO books (title, author) VALUES (?, ?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, title);
            checkStmt.setString(2, author);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Book already exists: " + title);
                return;  // Не добавляем повторно
            }
        }

        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();
            System.out.println("Added book: " + title);
        }
    }


    private static void updateMemberName(Connection conn, int id, String newName) throws SQLException {
        String sql = "UPDATE library_members SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated member ID " + id + " to " + newName);
        }
    }

    private static void deleteBook(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Deleted book ID: " + id);
        }
    }

    private static void listMembers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM library_members";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nLibrary Members:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
        }
    }

    private static void listBooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nBooks in Library:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") + ", Author: " + rs.getString("author"));
            }
        }
    }
}
