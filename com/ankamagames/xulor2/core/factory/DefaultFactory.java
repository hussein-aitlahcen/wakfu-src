package com.ankamagames.xulor2.core.factory;

import com.ankamagames.xulor2.core.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.converter.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import java.lang.reflect.*;

public class DefaultFactory<T extends BasicElement> implements Factory
{
    private static final Logger m_logger;
    public static final boolean USE_POOLS = true;
    public static final String CHECKOUT_METHOD_NAME = "checkOut";
    private final ConverterLibrary m_cvtlib;
    private final ArrayList<Method> m_setters;
    private final ArrayList<Method> m_getters;
    private final ArrayList<Method> m_prependers;
    private final ArrayList<Method> m_appenders;
    private final Class m_template;
    private Method m_checkOut;
    protected Class[] m_parameterPriority;
    
    public DefaultFactory(final Class template, final ConverterLibrary cvtlib) {
        super();
        this.m_setters = new ArrayList<Method>();
        this.m_getters = new ArrayList<Method>();
        this.m_prependers = new ArrayList<Method>();
        this.m_appenders = new ArrayList<Method>();
        this.m_checkOut = null;
        this.m_parameterPriority = new Class[] { Object.class, String.class, Float.TYPE, Double.TYPE, Boolean.TYPE, Character.TYPE, Long.TYPE, Byte.TYPE, Integer.TYPE };
        this.m_cvtlib = cvtlib;
        this.m_template = template;
        this.registerSetters();
        this.registerGetters();
        this.registerPrependers();
        this.registerAppenders();
        this.registerNewMethod();
    }
    
    public DefaultFactory(final Class template) {
        this(template, ConverterLibrary.getInstance());
    }
    
    protected int priority(final Class<?> type) {
        for (int i = 0; i < this.m_parameterPriority.length; ++i) {
            if (type.isAssignableFrom(this.m_parameterPriority[i])) {
                return i;
            }
        }
        return -1;
    }
    
    protected void registerGetters() {
        final Method[] methods = this.m_template.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            final String methodeName = methods[i].getName();
            if (methodeName.startsWith("get")) {
                this.m_getters.add(methods[i]);
            }
        }
    }
    
    protected void registerNewMethod() {
        try {
            this.m_checkOut = this.m_template.getDeclaredMethod("checkOut", (Class[])new Class[0]);
        }
        catch (Exception ex) {}
    }
    
    protected void registerSetters() {
        this.registerAccessors(this.m_setters, "set");
    }
    
    protected void registerPrependers() {
        this.registerAccessors(this.m_prependers, "prepend");
    }
    
    protected void registerAppenders() {
        this.registerAccessors(this.m_appenders, "append");
    }
    
    @Override
    public T newInstance() throws Exception {
        T ret;
        if (this.m_checkOut != null) {
            ret = (T)this.m_checkOut.invoke(null, new Object[0]);
        }
        else {
            ret = this.m_template.newInstance();
            ret.onCheckOut();
        }
        return ret;
    }
    
    @Override
    public T newInstance(final ClassDocument doc, final String varName) throws Exception {
        doc.addImport(this.m_template);
        T ret;
        if (this.m_checkOut != null) {
            ret = (T)this.m_checkOut.invoke(null, new Object[0]);
            doc.addGeneratedCommandLine(new ClassVariable(this.m_template, varName, this.m_template.getSimpleName() + ".checkOut()"));
        }
        else {
            ret = this.m_template.newInstance();
            doc.addGeneratedCommandLine(new ClassVariable(this.m_template, varName, "new " + this.m_template.getSimpleName() + "()"));
            if (ret instanceof Poolable) {
                doc.addGeneratedCommandLine(new ClassMethodCall(null, "onCheckOut", varName));
                ret.onCheckOut();
            }
        }
        return ret;
    }
    
    @Override
    public T newInstance(final Object parameter) throws Exception {
        final Class pType = parameter.getClass();
        final Constructor[] ctors = this.m_template.getConstructors();
        for (int i = 0; i < ctors.length; ++i) {
            final Class<?>[] paraTypes = (Class<?>[])ctors[i].getParameterTypes();
            if (0 < paraTypes.length && paraTypes[0].isAssignableFrom(pType)) {
                return ctors[i].newInstance(parameter);
            }
        }
        return this.m_template.newInstance();
    }
    
    @Override
    public T newInstance(final Object... parameter) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (parameter == null) {
            return this.m_template.newInstance();
        }
        final Class[] pTypes = new Class[parameter.length];
        final Constructor[] constructors = this.m_template.getConstructors();
        Constructor ctor = null;
        for (int i = 0; i < pTypes.length; ++i) {
            pTypes[i] = parameter[i].getClass();
        }
        for (int i = 0; ctor == null && i < constructors.length; ++i) {
            final Class<?>[] cParams = (Class<?>[])constructors[i].getParameterTypes();
            if (cParams.length == pTypes.length) {
                ctor = constructors[i];
                for (int j = 0; ctor != null && j < cParams.length; ++j) {
                    if (cParams[j].equals(Object.class)) {
                        if (!cParams[j].equals(pTypes[j])) {
                            ctor = null;
                        }
                    }
                    else if (!cParams[j].isAssignableFrom(pTypes[j])) {
                        ctor = null;
                    }
                }
            }
        }
        if (ctor != null) {
            return ctor.newInstance(parameter);
        }
        throw new IllegalArgumentException("Impossible de trouver de constructeur pour les types : " + pTypes);
    }
    
    @Override
    public Class getTemplate() {
        return this.m_template;
    }
    
    @Override
    public Method getSetter(final Class template) {
        Method method = null;
        for (int size = this.m_setters.size(), i = 0; i < size; ++i) {
            final Method m = this.m_setters.get(i);
            final Class[] paraTypes = m.getParameterTypes();
            if (paraTypes != null && 0 < paraTypes.length && template.equals(paraTypes[0])) {
                method = m;
                break;
            }
        }
        return method;
    }
    
    @Override
    public Method getSetter(final String name) {
        return this.getAccessor(this.m_setters, name);
    }
    
    public Method getGetter(final String name) {
        return this.getAccessor(this.m_getters, name);
    }
    
    @Override
    public Method guessSetter(final String name) {
        return this.guessAccessor(this.m_setters, name, "set");
    }
    
    @Override
    public Method guessSetter(final String name, final Class type) {
        return this.guessAccessor(this.m_setters, name, "set", type, null);
    }
    
    @Override
    public Method guessSetter(final String name, final Class type, final Class excludedTypes) {
        return this.guessAccessor(this.m_setters, name, "set", type, excludedTypes);
    }
    
    @Override
    public Method guessGetter(final String name) {
        return this.guessAccessor(this.m_getters, name, "get");
    }
    
    @Override
    public Method guessGetter(final String name, final Class type) {
        return this.guessAccessor(this.m_getters, name, "get", type, null);
    }
    
    @Override
    public Method guessAppender(final String name) {
        return this.guessAccessor(this.m_appenders, name, "append");
    }
    
    @Override
    public Method guessAppender(final String name, final Class type) {
        return this.guessAccessor(this.m_appenders, name, "append", type, null);
    }
    
    @Override
    public Method guessPrepender(final String name) {
        return this.guessAccessor(this.m_prependers, name, "prepend");
    }
    
    @Override
    public Method guessPrepender(final String name, final Class type) {
        return this.guessAccessor(this.m_prependers, name, "prepend", type, null);
    }
    
    private Method getAccessor(final ArrayList<Method> methodsList, final String name) {
        for (int size = methodsList.size(), i = 0; i < size; ++i) {
            final Method m = methodsList.get(i);
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }
    
    private ArrayList<Method> getAccessors(final ArrayList<Method> methodsList, final String name) {
        final ArrayList<Method> methods = new ArrayList<Method>();
        for (int size = methodsList.size(), i = 0; i < size; ++i) {
            final Method m = methodsList.get(i);
            if (m.getName().equalsIgnoreCase(name)) {
                methods.add(m);
            }
        }
        return methods;
    }
    
    private Method guessAccessor(final ArrayList<Method> methodsList, String name, final String accessorId) {
        Method method = null;
        name = accessorId + name;
        for (int size = methodsList.size(), i = 0; i < size; ++i) {
            final Method m = methodsList.get(i);
            if (m.getName().equalsIgnoreCase(name)) {
                method = m;
                break;
            }
        }
        return method;
    }
    
    private Method guessAccessor(final ArrayList<Method> methodsList, String name, final String accessorId, final Class<?> type, final Class<?> excludedTypes) {
        Method candidateMethod = null;
        name = accessorId + name;
        for (int size = methodsList.size(), i = 0; i < size; ++i) {
            final Method currentMethod = methodsList.get(i);
            if (currentMethod.getName().equalsIgnoreCase(name)) {
                final Class<?>[] parameterTypes = currentMethod.getParameterTypes();
                if (parameterTypes.length > 0) {
                    final Class<?> param = parameterTypes[0];
                    if (excludedTypes != null && excludedTypes.isAssignableFrom(param)) {
                        continue;
                    }
                    if (type == null) {
                        return currentMethod;
                    }
                    if (param.isAssignableFrom(type)) {
                        return currentMethod;
                    }
                }
                candidateMethod = currentMethod;
            }
        }
        return candidateMethod;
    }
    
    private void registerAccessors(final ArrayList<Method> methodsList, final String accessorId) {
        final Method[] methods = this.m_template.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            final Method method = methods[i];
            final String methodName = method.getName();
            if (methodName.startsWith(accessorId) && method.getParameterTypes().length == 1) {
                int addIndex = methodsList.size();
                if (ConverterLibrary.getInstance().hasConverter(method.getParameterTypes()[0])) {
                    addIndex = 0;
                }
                if (methodsList.contains(method)) {
                    DefaultFactory.m_logger.warn((Object)("La classe " + this.m_template.getSimpleName() + " poss\u00e8de plusieurs fonctions poss\u00e9dant le nom " + method.getName()));
                }
                methodsList.add(addIndex, method);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultFactory.class);
    }
}
