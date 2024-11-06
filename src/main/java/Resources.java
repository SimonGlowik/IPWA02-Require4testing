import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class Resources {

    @PersistenceContext(unitName = "testmanagement")
    private EntityManager em;

    @Produces
    public EntityManager produceEntityManager() {
        return em;
    }
}