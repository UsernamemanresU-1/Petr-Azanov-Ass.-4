import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private String libraryName;
    private String location;
    private List<LibraryMember> members;
    private List<Book> books;

    public Library(String libraryName, String location) {
        this.libraryName = libraryName;
        this.location = location;
        this.members = new ArrayList<>();
        this.books = new ArrayList<>();
    }

    public void addMember(LibraryMember member) {
        members.add(member);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<LibraryMember> filterMembersByName(String name) {
        return members.stream()
                .filter(member -> member.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> sortBooksByTitle() {
        return books.stream()
                .sorted((book1, book2) -> book1.getTitle().compareTo(book2.getTitle()))
                .collect(Collectors.toList());
    }

    public Book searchBookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);  // Возвращает null, если книга не найдена
    }

    @Override
    public String toString() {
        StringBuilder libraryInfo = new StringBuilder();
        libraryInfo.append("Library Name: ").append(libraryName).append("\n");
        libraryInfo.append("Location: ").append(location).append("\n");
        libraryInfo.append("Total Members: ").append(members.size()).append("\n");
        libraryInfo.append("Total Books: ").append(books.size()).append("\n");
        return libraryInfo.toString();
    }

    public List<LibraryMember> getMembers() {
        return members;
    }

    public List<Book> getBooks() {
        return books;
    }
}
