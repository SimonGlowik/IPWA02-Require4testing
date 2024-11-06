import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class CurrentUser implements Serializable {

    boolean tester, testmanager, requirementManager, testcase_creator;

    public void reset() {
        this.tester = false;
        this.testmanager = false;
        this.requirementManager = false;
        this.testcase_creator = false;
    }

    boolean isTestcase_creator() {
        return testcase_creator;
    }
    boolean isTester() {
        return tester;
    }
    boolean isTestmanager() {
        return testmanager;
    }
    boolean isRequirementManager() {
        return requirementManager;
    }
    boolean isNotValid() {
        return !isTester() && !isTestmanager() && !isRequirementManager() && !isTestcase_creator();
    }
}
