package org.companion.impresario;

/**
 * <p>
 * This exception will be thrown during the compilation of invalid xml configuration.
 * It's a good practice to throw this exception from the constructor of any custom Function and/or Condition if
 * something went wrong in order to stop the compilation process.
 * </p>
 *
 * <pre>
 * {@code
 * class MyCustomFunction implements Function {
 *
 *     public MyCustomFunction(FunctionDefinition definition) {
 *         if ( something went wrong ) {
 *             throw new InvalidConfigurationException("Some error message");
 *         }
 *     }
 *
 *     @Override
 *     public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
 *         // Do something
 *     }
 * }
 * }
 * </pre>
 *
 * <pre>
 * {@code
 * class MyCustomCondition implements Condition {
 *
 *     public MyCustomCondition(ConditionDefinition definition) {
 *         if ( something went wrong ) {
 *             throw new InvalidConfigurationException("Some error message");
 *         }
 *     }
 *
 *     @Override
 *     public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
 *         // Do something
 *     }
 * }
 * }
 * </pre>
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
