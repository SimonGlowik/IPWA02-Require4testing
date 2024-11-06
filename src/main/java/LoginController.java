import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable {

    @Inject
    Testmanagement testmanagement;

    CurrentUser currentUser;

    public LoginController() {
        // Initialize CurrentUser manually
        FacesContext context = FacesContext.getCurrentInstance();
        currentUser = (CurrentUser) context.getExternalContext().getSessionMap().get("currentUser");
        if (currentUser == null) {
            currentUser = new CurrentUser();
            context.getExternalContext().getSessionMap().put("currentUser", currentUser);
        }
    }

    private static final String salt = "vXsia8c04PhBtnG3isvjlemj7Bm6rAhBR8JRkf2z";

    String user, password;
    String tempUsername;
    String failureMessage = "";

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentUser.reset();  // Clear the user data
        context.getExternalContext().invalidateSession();  // Invalidate the session
        context.getExternalContext().getSessionMap().remove("currentUser");  // Remove user from session

        return "login.xhtml?faces-redirect=true";
    }

    public void postValidateUser(ComponentSystemEvent ev) {
        UIInput temp = (UIInput) ev.getComponent();
        this.tempUsername = (String) temp.getValue();
    }

    public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password = (String) value;
        testmanagement.validateUsernameAndPassword(currentUser, tempUsername, password, salt);
        if (currentUser.isNotValid()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falsch!", null);
            throw new ValidatorException(msg);
        }
    }

    public String login() {
        if (currentUser.isTester()) {
            this.failureMessage = "";
            return "tester.xhtml?faces-redirect=true";
        } else if (currentUser.isRequirementManager()) {
            this.failureMessage = "";
            return "requirement.xhtml?faces-redirect=true";
        } else if (currentUser.isTestmanager()) {
            this.failureMessage = "";
            return "testmanager.xhtml?faces-redirect=true";
        }
        else if (currentUser.isTestcase_creator()) {
            this.failureMessage = "";
            return "testcase_creator.xhtml?faces-redirect=true";
        }
        else {
            this.failureMessage = "Passwort und Benutzername nicht erkannt.";
            return "";
        }
    }

    public static void main(String[] args) {
        if(args.length!=2) {
            System.err.println("Usage: java LoginController user pass");
            System.exit(1);
        }
    }

    public boolean isAuthenticated() {
        return currentUser != null && !currentUser.isNotValid();
    }

    public boolean hasRole(String role) {
        return switch (role) {
            case "tester" -> currentUser.isTester();
            case "requirementManager" -> currentUser.isRequirementManager();
            case "testmanager" -> currentUser.isTestmanager();
            case "testcase_creator" -> currentUser.isTestcase_creator();
            default -> false;
        };
    }

    public void checkLogin() {
        if (currentUser.isNotValid()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Authentication Required", "Please log in to access this page."));

            // Preserve messages across redirect
            context.getExternalContext().getFlash().setKeepMessages(true);

            // Redirect to the login page
            NavigationHandler navHandler = context.getApplication().getNavigationHandler();
            navHandler.handleNavigation(context, null, "login.xhtml?faces-redirect=true");
            context.renderResponse();  // Stop further processing in the lifecycle
        }
    }

    public void checkRole(String role) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!hasRole(role)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unauthorized Access", "You do not have permission to access this page."));
        }
    }

    public String getUsername() {
        if(currentUser.isTester())
            return "(Tester)";
        else if(currentUser.isRequirementManager())
            return "(Requirement Manager)";
        else if(currentUser.isTestmanager())
            return "(Test Manager)";
        else if(currentUser.isTestcase_creator())
            return "(Testcase Creator)";
        else
            return "";
    }
}