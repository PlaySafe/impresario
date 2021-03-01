package org.companion.impresario;

/**
 * <p>
 * Exception during function execution when the pre-condition do not match
 * </p>
 */
public class ConditionNotMatchException extends Exception {

    public ConditionNotMatchException(String message) {
        super(message);
    }

}
