package org.companion.impresario;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The pair of condition name and implementation will be used as reference of real instance during
 * creation from {@link ConditionDefinition}. For example, if the pair of condition name and implementation like
 * </p>
 *
 * <ul>
 * <li>{@code condition_x} : {@code org.companion.impresario.ConditionABC}</li>
 * <li>{@code condition_y} : {@code org.companion.impresario.ConditionXYZ}</li>
 * </ul>
 *
 * <p>
 * Then, the {@code ConditionDefinition.name} is <i>condition_x</i>,
 * the instance of {@code org.companion.impresario.ConditionABC} will be created.
 * </p>
 */
class ConditionBuilder {

    private final Map<String, Class<? extends Condition>> conditionNameAndImplMap;

    ConditionBuilder(Map<String, Class<? extends Condition>> conditionNameAndImplMap) {
        this.conditionNameAndImplMap = Collections.unmodifiableMap(new HashMap<>(conditionNameAndImplMap));
    }

    /**
     * Creates a new condition from the pair of condition name and implementation using
     * the name of {@link ConditionDefinition}
     *
     * @param definition the user define from configuration
     * @return a new instance of condition corresponds to the name of {@link ConditionDefinition}.
     *
     * @throws IllegalArgumentException if the name doesn't match to any condition
     */
    Condition build(ConditionDefinition definition) {
        String name = definition.getName();
        Class<? extends Condition> conditionClass = conditionNameAndImplMap.get(name);
        if (conditionClass == null) {
            throw new IllegalArgumentException("No such condition - " + name);
        }
        try {
            return conditionClass.getConstructor(definition.getClass()).newInstance(definition);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
