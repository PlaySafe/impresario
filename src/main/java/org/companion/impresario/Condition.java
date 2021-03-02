package org.companion.impresario;

import java.util.Map;

/**
 * <p>
 * General interface for all conditions, the implementation can arbitrary decorate condition as tree.
 * The implementation must provide a constructor for {@link ConditionDefinition}.
 * Nevertheless, the implementation can implement overload constructor as well.
 * </p><br>
 *
 * <p>
 * Since the version4, the ConditionDefinition will provide both metadata and data follow the configuration.
 * Regarding the example code below, any parameters defined in the metadata will be available under
 * <b>definition.getMetaParameters().get(&lt;index&gt;)</b>.
 * </p><br>
 *
 * <p>
 * If no such parameter defined in metadata, and no such <b>result-as</b> attribute defined in the configuration,
 * all functions will be grouped under an empty string key. Therefore, you can access those list of function using
 * <b>definition.getPreFunctions().getOrDefault(&lt;key&gt;, Collections.emptyList())</b>.
 * </p><br>
 *
 * <pre>
 * class MyCustomCondition implements Condition {
 *
 *     private final Function function1;
 *     private final Function function2;
 *
 *     public MyCustomCondition(ConditionDefinition definition) {
 *         String parameter1 = definition.getMetaParameters().getOrDefault(0, "");
 *         String parameter2 = definition.getMetaParameters().getOrDefault(1, "");
 *
 *         this.function1 = definition.getPreFunctions().get(parameter1).get(0);
 *         this.function2 = definition.getPreFunctions().get(parameter2).get(0);
 *     }
 *
 *     &#64;Override
 *     public boolean matches(Object input, Map&lt;tring, Map&lt;String, Object&gt;&gt; definitions) throws ConditionNotMatchException {
 *         // Do Something
 *     }
 * }
 * </pre>
 */
public interface Condition {

    /**
     * Checks all conditions both itself and/or the its dependent conditions.
     *
     * @param input       the arbitrary object for checking condition
     * @param definitions the user definition. 1st key is the definition name, and the 2nd key is the definition key
     *
     * @return <code>true</code> if match all conditions in the tree, otherwise <code>false</code>
     *
     * @throws ConditionNotMatchException if one of the pre-condition doesn't match
     */
    boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException;

}
