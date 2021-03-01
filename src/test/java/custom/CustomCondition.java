package custom;

import org.companion.impresario.Condition;
import org.companion.impresario.ConditionDefinition;
import org.companion.impresario.ConditionNotMatchException;
import org.companion.impresario.Function;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Use to ensure the out-of-package visibility of the {@link ConditionDefinition} methods.
 * This is not a test class, but should not have any compilation error.
 * </p>
 */
public class CustomCondition implements Condition {

    CustomCondition(ConditionDefinition definition) {
        String parameter = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameter, Collections.emptyList());
        List<Condition> preConditions = definition.getPreConditions().getOrDefault(parameter, Collections.emptyList());
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        return false;
    }
}
