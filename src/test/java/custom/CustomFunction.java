package custom;

import org.companion.impresario.Condition;
import org.companion.impresario.ConditionNotMatchException;
import org.companion.impresario.Function;
import org.companion.impresario.FunctionDefinition;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Use to ensure the out-of-package visibility of the {@link FunctionDefinition} methods.
 * This is not a test class, but should not have any compilation error.
 * </p>
 */
public class CustomFunction implements Function {

    CustomFunction(FunctionDefinition definition) {
        String parameter = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameter, Collections.emptyList());
        Condition preConditions = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        return null;
    }
}
