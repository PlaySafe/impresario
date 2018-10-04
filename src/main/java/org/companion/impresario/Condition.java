package org.companion.impresario;

import java.util.Map;

/**
 * General interface for all conditions, the implementation can arbitrary decorate condition as tree.
 * The implementation must provide a constructor for {@link ConditionDefinition}.
 * Nevertheless, the implementation can implement overload constructor as well.
 */
interface Condition {

    /**
     * Checks all conditions both itself and/or the its dependent conditions.
     *
     * @param input       the arbitrary object for checking condition
     * @param definitions the user definition. 1<upper>st</upper> key is the definition name, and the 2<upper>nd</upper> key is the definition key
     * @return <code>true</code> if all condition in the tree match, otherwise <code>false</code>
     *
     * @throws ConditionNotMatchException if one of the pre-condition doesn't match
     */
    boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException;

}
