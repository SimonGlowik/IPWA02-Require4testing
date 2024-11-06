import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "TestCases")
public class TestCase implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    //one testcase can contain many requirements and can be empty
    @ManyToOne
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;
    private String Result;

    public TestCase() {
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }
    public Requirement getRequirement() {
        return requirement;
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
    public void setResult(String result) {
        Result = result;
    }
    public String getResult() {
        return Result;
    }
}
