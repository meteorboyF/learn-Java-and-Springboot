// FIX 3: Add the missing no-arg constructor to the @Entity class
// The fix: add public Student() {} alongside the parameterised constructor.

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    private Double gpa;
    private String major;

    // FIXED: No-arg constructor added.
    // JPA/Hibernate requires this to instantiate Student objects via reflection
    // when loading rows from the database (SELECT * FROM students).
    // Hibernate process:
    //   1. new Student()      ← uses this constructor
    //   2. student.setName("Alice")    ← fills fields
    //   3. student.setGpa(3.85)
    //   etc.
    // Without step 1, Hibernate throws: "No default constructor for entity: Student"
    public Student() {}  // REQUIRED — do not remove!

    // Parameterised constructor — still useful for creating new students in service/seeder.
    // Notice id is NOT a parameter — DB assigns it.
    public Student(String name, String studentId, Double gpa, String major) {
        this.name = name;
        this.studentId = studentId;
        this.gpa = gpa;
        this.major = major;
    }

    // RULE: If you define ANY constructor in Java, the compiler stops auto-generating
    //       the no-arg constructor. You MUST add it manually.
    // RULE: EVERY @Entity class MUST have a public no-arg constructor.
    // EXAM: This is one of the most commonly lost marks on the JPA exam question.

    public Long   getId()        { return id; }
    public String getName()      { return name; }
    public void   setName(String n)  { this.name = n; }
    public String getStudentId() { return studentId; }
    public void   setStudentId(String s) { this.studentId = s; }
    public Double getGpa()       { return gpa; }
    public void   setGpa(Double g)   { this.gpa = g; }
    public String getMajor()     { return major; }
    public void   setMajor(String m) { this.major = m; }
}
