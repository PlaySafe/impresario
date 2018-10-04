package org.companion.impresario;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VariableReflector {

    private static final Pattern FIELD_PATTERN = Pattern.compile("@\\{(.*)\\}");
    private static final Pattern DEFINITION_PATTERN = Pattern.compile("^#\\{(.*)(?:\\.(.*))?\\}$");
    private static final Pattern PROPERTIES_PATTERN = Pattern.compile("\\$\\{(.*)\\}");

    static final String reflectFieldMethodOf(String text) {
        Matcher matcher = FIELD_PATTERN.matcher(text);
        if (matcher.matches()) {
            StringBuilder stringBuilder = new StringBuilder("get");
            String methodName = matcher.group(1);
            stringBuilder.append(Character.toUpperCase(methodName.charAt(0)));
            stringBuilder.append(methodName.substring(1));
            return stringBuilder.toString();
        }
        else {
            throw new IllegalArgumentException("Cannot specify method name of " + text);
        }
    }

    static final boolean isField(String text) {
        Matcher matcher = FIELD_PATTERN.matcher(text);
        return matcher.matches();
    }

    static final String reflectDefinitionNameOf(String definitionText) {
        Matcher matcher = DEFINITION_PATTERN.matcher(definitionText);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("The text '" + definitionText + "' does not refer to definition");
    }

    static final String reflectDefinitionKeyOf(String definitionText) {
        Matcher matcher = DEFINITION_PATTERN.matcher(definitionText);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        throw new IllegalArgumentException("The text '" + definitionText + "' does not refer to definition");
    }

    static final boolean isDefinition(String text) {
        Matcher matcher = DEFINITION_PATTERN.matcher(text);
        return matcher.matches();
    }

    static final String reflectPropertiesOf(String text) {
        Matcher matcher = PROPERTIES_PATTERN.matcher(text);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("The text '" + text + "' does not refer to properties");
    }

    static final boolean isProperties(String text) {
        Matcher matcher = PROPERTIES_PATTERN.matcher(text);
        return matcher.matches();
    }

    static final String invoke(Object input, String methodName) {
        try {
            Method method = input.getClass().getMethod(methodName);
            Object value = method.invoke(input);
            return (value == null) ? null : String.valueOf(value);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            String errorMsg = String.format("Cannot invoke method '%s.%s'", input.getClass().getName(), methodName);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }
}