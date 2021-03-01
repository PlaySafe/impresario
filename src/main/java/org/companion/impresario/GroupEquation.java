package org.companion.impresario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Facade to delegate the execution to the list of {@link Equation}. This will trigger execution until find the first result.
 * If there is no executable equation (might be from not match condition), or instantiate using an empty list,
 * the {@link ConditionNotMatchException} will be thrown during runtime.
 * </p>
 */
class GroupEquation implements Equation {

    private final List<Equation> equations;

    GroupEquation(List<Equation> equations) {
        this.equations = Collections.unmodifiableList(new ArrayList<>(equations));
    }

    @Override
    public BigDecimal perform(Object input) throws ConditionNotMatchException {
        for (Equation equation : equations) {
            try {
                return equation.perform(input);
            }
            catch (ConditionNotMatchException e) {
                // Intend doing nothing because some other equations might produce output
            }
        }
        throw new ConditionNotMatchException("Cannot solve equation because of no such condition matches");
    }
}
