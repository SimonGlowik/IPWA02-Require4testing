import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class RequirementController implements Serializable {

    private final EntityManager entityManager;

    public RequirementController() {
        entityManager = Persistence.createEntityManagerFactory(Testmanagement.PERSISTENCE_UNIT_NAME)
                .createEntityManager();
    }

    private Requirement requirement = new Requirement();

    // Getter and Setter for the Requirement instance
    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    // Method to provide status options, assuming RequirementStatus is an enum
    public RequirementStatus[] getStatusOptions() {
        return RequirementStatus.values();
    }

    // Save a new requirement to the database
    public String saveRequirement() {
        if (requirement != null) {
            entityManager.getTransaction().begin();  // Begin transaction
            entityManager.persist(requirement);
            entityManager.flush();  // Forces synchronization with the database
            entityManager.getTransaction().commit();  // Commit transaction
            requirement = new Requirement();  // Reset for the next entry
            return "requirementList.xhtml?faces-redirect=true";  // Redirect after save
        }
        return null;
    }

    public Requirement findRequirementById(Long id) {
        var requirement = entityManager.find(Requirement.class, id);
        return requirement;

    }
}