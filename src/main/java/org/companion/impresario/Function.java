package org.companion.impresario;

import java.util.Map;

/**
 * General interface for all functions, the implementation can arbitrary decorate function as tree.
 * The implementation must provide a constructor for {@link FunctionDefinition}.
 * Nevertheless, the implementation can implement overload constructor as well.
 */
public interface Function {

    /**
     * Perform the function specific implementation. However, the implementation should check the pre-condition,
     * if there is before operate the function logic.
     *
     * @param input       the arbitrary object for retrieving data
     * @param definitions the user definition. 1st key is the definition name, and the 2nd key is the definition key
     * @return the string result
     *
     * @throws ConditionNotMatchException if one of the pre-condition doesn't match
     */
    String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException;

}
