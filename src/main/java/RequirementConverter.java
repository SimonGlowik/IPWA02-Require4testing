import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(forClass = Requirement.class)
public class RequirementConverter implements Converter<Requirement> {

    @Override
    public Requirement getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        RequirementController requirementController = context.getApplication()
                .evaluateExpressionGet(context, "#{requirementController}", RequirementController.class);

        return requirementController.findRequirementById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Requirement requirement) {
        if (requirement == null || requirement.getId() == null) {
            return "";
        }
        return String.valueOf(requirement.getId());
    }
}