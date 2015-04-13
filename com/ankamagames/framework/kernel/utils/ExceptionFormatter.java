package com.ankamagames.framework.kernel.utils;

public final class ExceptionFormatter
{
    private static final int m_stackDepthOffset;
    private static final String[] packageNames;
    private static final String[] shortPackageNames;
    public static boolean SIMPLIFY_CLASS_NAMES;
    
    public static String toString(final Throwable t) {
        return toString(t, Integer.MAX_VALUE);
    }
    
    public static String toString(final Throwable t, final int maxStackTraceElements) {
        if (t == null) {
            return " [null Throwable in ExceptionFormatter.toString()]";
        }
        final StackTraceElement[] stackElements = t.getStackTrace();
        final StringBuilder sb = new StringBuilder();
        sb.append(' ').append(t.toString());
        if (stackElements.length == 0) {
            sb.append("emptyStacktrace, current is ").append(currentStackTrace(maxStackTraceElements));
        }
        else {
            sb.append(formatStackTrace(stackElements, 0, maxStackTraceElements));
        }
        if (t.getCause() != null) {
            sb.append(" caused by ").append(toString(t.getCause(), maxStackTraceElements));
        }
        return sb.toString();
    }
    
    public static String currentStackTrace() {
        return currentStackTrace(1, Integer.MAX_VALUE);
    }
    
    public static String currentStackTrace(final int maxStackTraceElements) {
        return currentStackTrace(1, maxStackTraceElements);
    }
    
    public static String currentStackTrace(final int excludedStackTraceElements, final int maxStackTraceElements) {
        final StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
        return formatStackTrace(stackElements, 2 + ExceptionFormatter.m_stackDepthOffset + excludedStackTraceElements, maxStackTraceElements);
    }
    
    private static String formatStackTrace(final StackTraceElement[] stackElements, final int excludedStackTraceElements, final int maxStackTraceElements) {
        final StringBuilder builder = new StringBuilder("stackTrace=");
        formatStackTrace(builder, stackElements, excludedStackTraceElements, maxStackTraceElements, ' ');
        return builder.toString();
    }
    
    private static void formatStackTrace(final StringBuilder builder, final StackTraceElement[] stackElements, final int excludedStackElements, final int maxStackElements, final char separator) {
        for (int i = excludedStackElements; i < stackElements.length && i - excludedStackElements < maxStackElements; ++i) {
            formatStackTraceElementTo(builder, stackElements[i], separator);
        }
    }
    
    private static StringBuilder formatStackTraceElementTo(final StringBuilder stringBuilder, final StackTraceElement stackElement, final char separator) {
        return stringBuilder.append(' ').append(simplifyClassName(stackElement.getClassName())).append('.').append(stackElement.getMethodName()).append('(').append(stackElement.getFileName()).append(':').append(stackElement.getLineNumber()).append(')').append(separator);
    }
    
    static String simplifyClassName(final String className) {
        if (!ExceptionFormatter.SIMPLIFY_CLASS_NAMES) {
            return className;
        }
        String shortClassName = shortenClassName(className);
        for (int i = 0; i < ExceptionFormatter.packageNames.length; ++i) {
            final String packageName = ExceptionFormatter.packageNames[i];
            if (className.startsWith(packageName)) {
                shortClassName = '[' + ExceptionFormatter.shortPackageNames[i] + ']' + shortClassName;
                break;
            }
        }
        return shortClassName;
    }
    
    private static String shortenClassName(final String fullClassName) {
        final int cut = fullClassName.lastIndexOf(46);
        return (cut == -1) ? fullClassName : fullClassName.substring(cut + 1);
    }
    
    static {
        final String deepestElementMethodName = Thread.currentThread().getStackTrace()[0].getMethodName();
        m_stackDepthOffset = ("getStackTrace".equals(deepestElementMethodName) ? 0 : 1);
        packageNames = new String[] { "com.ankamagames.wakfu.server.game", "com.ankamagames.wakfu.server.ia", "com.ankamagames.wakfu.server", "com.ankamagames.wakfu.client", "com.ankamagames.wakfu.common", "com.ankamagames.baseImpl", "com.ankamagames.framework" };
        shortPackageNames = new String[] { "GAME", "IA", "SRV", "CLI", "COMM", "BIMP", "FWK" };
        ExceptionFormatter.SIMPLIFY_CLASS_NAMES = true;
    }
}
