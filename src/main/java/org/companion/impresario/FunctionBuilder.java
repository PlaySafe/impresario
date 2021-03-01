package org.companion.impresario;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The pair of function name and implementation will be used as reference of real instance during
 * creation from {@link FunctionDefinition}. For example, if the pair of function name and implementation like
 * </p>
 *
 * <ul>
 * <li>{@code function_x} : {@code org.companion.impresario.FunctionABC}</li>
 * <li>{@code function_y} : {@code org.companion.impresario.FunctionXYZ}</li>
 * </ul>
 *
 * <p>
 * Then, the {@code FunctionDefinition.name} is <i>function_x</i>,
 * the instance of {@code org.companion.impresario.FunctionABC} will be created.
 * </p>
 */
class FunctionBuilder {

    private final Map<String, Class<? extends Function>> functionNameAndImplMap;

    FunctionBuilder(Map<String, Class<? extends Function>> functionNameAndImplMap) {
        this.functionNameAndImplMap = Collections.unmodifiableMap(new HashMap<>(functionNameAndImplMap));
    }

    /**
     * Creates a new function from the pair of function name and implementation using
     * the name of {@link FunctionDefinition}
     *
     * @param definition the user define from configuration
     *
     * @return a new instance of function corresponds to the name of {@link FunctionDefinition}.
     *
     * @throws IllegalArgumentException if the name doesn't match to any function
     */
    Function build(FunctionDefinition definition) {
        String name = definition.getName();
        Class<? extends Function> functionClass = functionNameAndImplMap.get(name);
        if (functionClass == null) {
            throw new InvalidConfigurationException("No such function : " + name);
        }
        try {
            return functionClass.getConstructor(definition.getClass()).newInstance(definition);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidConfigurationException(e);
        }
    }
}
