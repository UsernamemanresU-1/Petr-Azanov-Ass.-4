public class Book {
    private String title;
    private String author;
    private int bookId;
    private boolean isRented;
    private LibraryMember rentedBy;

    public Book(String title, String author, int bookId) {
        this.title = title;
        this.author = author;
        this.bookId = bookId;
        this.isRented = false;
        this.rentedBy = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getBookId() {
        return bookId;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public LibraryMember getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(LibraryMember rentedBy) {
        this.rentedBy = rentedBy;
    }

    @Override
    public String toString() {
        return "Book Title: " + title + ", Author: " + author + ", Book ID: " + bookId +
                (isRented ? " (Rented by: " + rentedBy.getName() + ")" : " (Available)");
    }
}