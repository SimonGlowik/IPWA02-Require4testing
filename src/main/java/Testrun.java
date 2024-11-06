import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Testruns")
public class Testrun implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    //many testcases can be assigned to a testrun
    @ManyToMany
    private List<TestCase> testCases;
    private TestrunStatus status;
    private String Assignee;


    public Testrun() {
    }

   public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public Long getId() {
        return id;
    }

    public void setName (String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setStatus(TestrunStatus status) {
        this.status = status;
    }
    public TestrunStatus getStatus() {
        return status;
    }
    public void setAssignee(String assignee) {
        Assignee = assignee;
    }
    public String getAssignee() {
        return Assignee;
    }
}
