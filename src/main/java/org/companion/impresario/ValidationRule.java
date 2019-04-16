package org.companion.impresario;

public interface ValidationRule {

    boolean validate(Object input) throws ConditionNotMatchException;

}
