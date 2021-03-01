package org.companion.impresario;

final class ErrorMessageBuilder {

    static String ambiguousParameter(String parameterName, Class<?> klass) {
        return new StringBuilder(128)
                .append("Ambiguous parameter '")
                .append(parameterName)
                .append("' of ")
                .append(klass.getName())
                .toString();
    }

    static String ambiguousNumberOfPreFunction(int expect, int real, Class<?> klass) {
        return new StringBuilder(128)
                .append("Ambiguous number of pre-function of ")
                .append(klass.getName())
                .append(": Must have ").append(expect)
                .append(", but defined ").append(real)
                .toString();
    }

    static String ambiguousNumberOfPreCondition(int expect, int real, Class<?> klass) {
        return new StringBuilder(128)
                .append("Ambiguous number of pre-condition of ")
                .append(klass.getName())
                .append(": Must have ").append(expect)
                .append(", but defined ").append(real)
                .toString();
    }
}
