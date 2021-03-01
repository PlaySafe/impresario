package org.companion.impresario;

import java.math.BigDecimal;

/**
 * <p>
 * General public interface of equation implementation.
 * The implementation might be a complex equation computation, or a facade of all computable functions.
 * </p>
 *
 * @see DefaultEquation
 * @see GroupEquation
 */
public interface Equation {

    BigDecimal perform(Object input) throws ConditionNotMatchException;

}
