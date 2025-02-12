public class Teacher extends LibraryMember {
    private String subject;

    public Teacher(String name, int memberId, String subject) {
        super(name, memberId);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String toString() {
        return super.toString() + ", subject='" + subject + "'";
    }
}
