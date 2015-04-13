package com.ankamagames.xulor2.core.event;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.regex.*;
import com.ankamagames.xulor2.*;
import java.lang.reflect.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;

public class CallBack
{
    public static final String[] EMPTY_STRING_ARRAY;
    private static final Pattern m_funcPattern;
    public static final String FUNCTION_SEPARATOR = ";";
    protected static final Logger m_logger;
    protected String[] m_funcs;
    protected String m_func;
    protected ElementMap m_elementMap;
    
    public CallBack() {
        super();
        this.m_func = null;
        this.m_elementMap = null;
    }
    
    public void setCallBackFunc(final String func) {
        this.m_func = func;
        this.m_funcs = StringUtils.split(func, ';');
    }
    
    public void setCallBackFunc(final String func, final ElementMap elementMap) {
        this.m_func = func;
        this.m_funcs = StringUtils.split(func, ';');
        this.m_elementMap = elementMap;
    }
    
    public void setElementMap(final ElementMap elementMap) {
        this.m_elementMap = elementMap;
    }
    
    public Object invokeCallBack() {
        Object result = null;
        for (final String func : this.m_funcs) {
            final Matcher matcher = CallBack.m_funcPattern.matcher(func);
            if (!matcher.matches()) {
                CallBack.m_logger.error((Object)("Erreur de syntaxe : '" + func + "' n'est pas du type 'package:method(param1,param2,...)'."));
            }
            else {
                final String packageName = matcher.group(2);
                final String methodName = matcher.group(3);
                final String parametersString = matcher.group(5);
                String[] parameters;
                if (parametersString != null) {
                    parameters = StringUtils.split(parametersString, ',');
                }
                else {
                    parameters = CallBack.EMPTY_STRING_ARRAY;
                }
                result = this.processCallBack(packageName, methodName, parameters);
            }
        }
        return result;
    }
    
    private Object processCallBack(final String packageName, final String methodName, final String[] parameters) {
        final List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
        final List<Object> args = new ArrayList<Object>();
        this.fillParameters(parameters, parameterTypes, args);
        final Class<?> actionClass = Xulor.getInstance().getActionClass(packageName);
        if (actionClass == null) {
            CallBack.m_logger.error((Object)("La m\u00e9thode '" + ((packageName != null) ? (packageName + ":") : "") + methodName + "(" + typeArrayToString(parameterTypes) + ")' est inconnue !"));
            return null;
        }
        final Method[] arr$;
        final Method[] methods = arr$ = actionClass.getMethods();
        for (final Method method : arr$) {
            if (method.getName().equals(methodName)) {
                boolean isValid = false;
                final Class<?>[] methodParameterTypes = method.getParameterTypes();
                if (methodParameterTypes.length <= parameterTypes.size()) {
                    for (int i = 0; i < methodParameterTypes.length; ++i) {
                        final Class<?> currentType = methodParameterTypes[i];
                        if (parameterTypes.size() <= i) {
                            isValid = false;
                        }
                        else if (currentType.isArray()) {
                            if (currentType.isAssignableFrom(parameterTypes.get(i))) {
                                isValid = true;
                            }
                            else {
                                int tabSize = 0;
                                for (int paraTypesSize = parameterTypes.size(), z = i; z < paraTypesSize && currentType.getComponentType().isAssignableFrom(parameterTypes.get(z)); ++z) {
                                    ++tabSize;
                                }
                                if (tabSize > 0) {
                                    final Object array = Array.newInstance(currentType.getComponentType(), tabSize);
                                    for (int z2 = 0; z2 < tabSize; ++z2) {
                                        parameterTypes.remove(i);
                                        Array.set(array, z2, args.remove(i));
                                    }
                                    args.add(i, array);
                                    parameterTypes.add(i, currentType);
                                }
                                else {
                                    isValid = false;
                                }
                            }
                        }
                        else {
                            isValid = methodParameterTypes[i].isAssignableFrom(parameterTypes.get(i));
                        }
                        if (!isValid) {
                            break;
                        }
                    }
                    if (methodParameterTypes.length != parameterTypes.size()) {
                        isValid = false;
                    }
                    if (isValid) {
                        try {
                            return method.invoke(null, args.toArray());
                        }
                        catch (Exception e) {
                            CallBack.m_logger.error((Object)("Erreur lors du invokeCallBack sur la m\u00e9thode " + method.getName() + " de la classe " + actionClass.getName()), (Throwable)e);
                            if (e.getCause() != null) {
                                CallBack.m_logger.error((Object)"Raison : ", e.getCause());
                            }
                            return null;
                        }
                    }
                }
            }
        }
        CallBack.m_logger.error((Object)("La m\u00e9thode '" + ((packageName != null) ? (packageName + ":") : "") + methodName + "(" + typeArrayToString(parameterTypes) + ")' est inconnue !"));
        return null;
    }
    
    protected void fillParameters(final String[] parameters, final List<Class<?>> parameterTypes, final List<Object> args) {
        for (int i = 0; i < parameters.length; ++i) {
            EventDispatcher element = null;
            if (this.m_elementMap != null) {
                element = this.m_elementMap.getElement(parameters[i]);
            }
            if (element != null) {
                final Object value = element.getElementValue();
                if (value != null) {
                    parameterTypes.add(value.getClass());
                    args.add(value);
                }
            }
            else if (parameters[i].length() != 0) {
                parameterTypes.add(String.class);
                args.add(parameters[i]);
            }
        }
    }
    
    private static String typeArrayToString(final List<Class<?>> types) {
        final StringBuilder builder = new StringBuilder();
        for (final Class<?> type : types) {
            builder.append(',').append(type.getName());
        }
        return builder.toString().substring(1);
    }
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
        m_funcPattern = Pattern.compile("(^([a-zA-Z.]+):)?([a-zA-Z]+){1}(\\((([_a-zA-Z0-9\\-\\.]+([,]?[\\s]*[_a-zA-Z0-9\\-\\.]+)*)*)\\))?");
        m_logger = Logger.getLogger((Class)CallBack.class);
    }
}
