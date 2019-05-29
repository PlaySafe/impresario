package org.companion.impresario;

import java.math.BigDecimal;

public interface Equation {

    BigDecimal perform(Object input) throws ConditionNotMatchException;

}
