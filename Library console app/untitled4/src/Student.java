public class Student extends LibraryMember {
    private String grade;

    public Student(String name, int memberId, String grade) {
        super(name, memberId);
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String toString() {
        return super.toString() + ", grade='" + grade + "'";
    }
}
