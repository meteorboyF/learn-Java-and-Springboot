// BUG 3: @Entity class missing the no-arg constructor
// Symptom: App starts but crashes when loading ANY student from the database.
//          Error: "No default constructor for entity: Student"
//          Or: HibernateException / InstantiationException at runtime.
// Location: The Student @Entity class.

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

    // BUG: Only a parameterised constructor is defined.
    // In Java, when you define ANY constructor, the compiler stops auto-generating
    // the no-arg constructor. So now Student has NO no-arg constructor.
    //
    // JPA uses reflection to create Student objects when loading rows from the DB:
    //   1. Call Student() [no-arg constructor] to create a blank instance.
    //   2. Use setters/reflection to fill in the fields from the row data.
    // Step 1 FAILS because Student() doesn't exist → JPA crashes.
    public Student(String name, String studentId, Double gpa, String major) {
        this.name = name;
        this.studentId = studentId;
        this.gpa = gpa;
        this.major = major;
    }

    // MISSING: public Student() {}  ← this is what's needed

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
