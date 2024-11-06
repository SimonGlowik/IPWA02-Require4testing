import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class TestrunController implements Serializable {

    private final EntityManager entityManager;

    public TestrunController() {
        entityManager = Persistence.createEntityManagerFactory(Testmanagement.PERSISTENCE_UNIT_NAME)
                .createEntityManager();
    }

    private Testrun testrun = new Testrun();

    // Getter and Setter for the Testrun instance
    public Testrun getTestrun() {
        return testrun;
    }

    public void setTestrun(Testrun testrun) {
        this.testrun = testrun;
    }

    // Method to provide status options, assuming TestrunStatus is an enum
    public TestrunStatus[] getStatusOptions() {
        return TestrunStatus.values();
    }

    // Retrieve all requirements for dropdown selection in the testrun form
    public List<Requirement> getRequirementsList() {
        Query query = entityManager.createQuery("SELECT r FROM Requirement r", Requirement.class);
        return query.getResultList();
    }

    // Retrieve all test cases for selection in the testrun form
    public List<TestCase> getTestCasesList() {
        Query query = entityManager.createQuery("SELECT tc FROM TestCase tc", TestCase.class);
        return query.getResultList();
    }

    public String saveTestrun() {
        if (testrun != null) {
            entityManager.getTransaction().begin();  // Begin transaction
            entityManager.persist(testrun);
            entityManager.getTransaction().commit();  // Commit transaction
            testrun = new Testrun();  // Reset for the next entry
            return "testmanager.xhtml?faces-redirect=true";
        }
        return null;
    }

    public List<Testrun> getAllTestruns() {
        Query query = entityManager.createQuery("SELECT t FROM Testrun t", Testrun.class);
        return query.getResultList();
    }
}