import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

@Named
@ApplicationScoped
public class Testmanagement implements Serializable {
    public static final String PERSISTENCE_UNIT_NAME = "testmanagement";

    private EntityManager em;

    public List<User> getAllUsers() {
        Query query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public Testmanagement() {
        // Lazy initialization, as em is injected after the constructor.
        em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)
                .createEntityManager();
        initiateDateBase();
    }

    //this method is used to create the needed users for the application
    private void initiateDateBase() {
        long userCount = (Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        if (userCount == 0) {
            em.getTransaction().begin();
            em.createNativeQuery("INSERT INTO Users (username, passwordHash, role) VALUES ('tester', 'AsuX9xcUwEm2GgfNQQjm/EFVSYBjnPJcDLYbMmRmfbtb0dArRRJaWmM43/2ZptMP9mDWPts+O5uq68baOHX9tg==', 'tester')").executeUpdate();
            em.createNativeQuery("INSERT INTO Users (username, passwordHash, role) VALUES ('testmanager', 'nTRnKNfg9RJtS2GfbPbjgFHRw0z0CSwh3nXEsKbmKs28AEcHjR1yWEXd5GJdoaPbAAzaPrxs0GhuU0NX74f40Q==', 'testmanager')").executeUpdate();
            em.createNativeQuery("INSERT INTO Users (username, passwordHash, role) VALUES ('requirementManager', 'gSA725iaR5zfoIKFPIYTiEbAcjuN2e+LUkG+c09DUa+18KB+2GHb/ABxD4YyOrMyPRbTDhZ16YSG/C/hRS7yqw==', 'requirementManager')").executeUpdate();
            em.createNativeQuery("INSERT INTO Users (username, passwordHash, role) VALUES ('testcase_creator', 'JNuiSjd6PkBS5hZ68hJpkGqbAwu3+ZYYwAqloMbdovW30XGECD/TKoXDZdqWymtcdeVS0RV7DEHEARjauIlACw==', 'testcase_creator')").executeUpdate();
            em.getTransaction().commit();
        }
    }


    static String hashPassword(String name, String pass, String salt) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digester.digest((name + pass + salt)
                    .getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(hashBytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void validateUsernameAndPassword(CurrentUser currentUser, String name, String pass, String salt) {
        String passHash = hashPassword(name, pass, salt);
        currentUser.reset();
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(name)) {
                if (user.getPasswordHash().equals(passHash)) {
                    if (user.getRole().equals("tester")) {
                        currentUser.tester = true;
                        return;
                    } else if (user.getRole().equals("testmanager")) {
                        currentUser.testmanager = true;
                        return;
                    } else if (user.getRole().equals("requirementManager")) {
                        currentUser.requirementManager = true;
                        return;
                    }
                    else if (user.getRole().equals("testcase_creator")) {
                        currentUser.testcase_creator = true;
                        return;
                    }
                    else throw new RuntimeException("Benutzer " + name + " ist falsch angelegt.");
                }
            }
        }
    }
}
