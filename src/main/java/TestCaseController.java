import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class TestCaseController implements Serializable {

    private final EntityManager entityManager;
    private String assignee; // To hold the assignee input
    private List<TestCase> filteredTestCases; // To store the filtered test cases

    public TestCaseController() {
        entityManager = Persistence.createEntityManagerFactory(Testmanagement.PERSISTENCE_UNIT_NAME)
                .createEntityManager();
    }

    private TestCase testCase = new TestCase();

    // Getter and Setter for the TestCase instance
    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<TestCase> getFilteredTestCases() {
        return filteredTestCases;
    }

    public void filterTestCasesByAssignee() {
        Query query = entityManager.createQuery("SELECT t FROM Testrun t", Testrun.class);
        var allTestRuns = (List<Testrun>) query.getResultList();
        filteredTestCases = allTestRuns.stream()
                .filter(testRun -> testRun.getAssignee() != null && testRun.getAssignee().equals(assignee))
                .flatMap(testRun -> testRun.getTestCases().stream())
                .toList();

        if (filteredTestCases.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "No entries are found under this filter: " + assignee, null));
        }
    }

    public void saveResults() {
        if (filteredTestCases == null || filteredTestCases.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "No changes to save.", null));
            return;
        }

        entityManager.getTransaction().begin();
        for (TestCase testCase : filteredTestCases) {
            entityManager.merge(testCase);
        }
        entityManager.getTransaction().commit();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success: Results have been saved successfully", null));
    }

    public List<Requirement> getRequirementsList() {
        Query query = entityManager.createQuery("SELECT r FROM Requirement r", Requirement.class);
        return query.getResultList();
    }

    public String saveTestCase() {
        if (testCase != null) {
            entityManager.getTransaction().begin();
            entityManager.persist(testCase);
            entityManager.getTransaction().commit();
            testCase = new TestCase();
            return "testcase_creator.xhtml?faces-redirect=true";
        }
        return null;
    }
}