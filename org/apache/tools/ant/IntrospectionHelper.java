package org.apache.tools.ant;

import java.io.*;
import java.lang.reflect.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.taskdefs.*;
import java.util.*;

public final class IntrospectionHelper
{
    private static final Map<String, IntrospectionHelper> HELPERS;
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP;
    private static final int MAX_REPORT_NESTED_TEXT = 20;
    private static final String ELLIPSIS = "...";
    private final Hashtable<String, Class<?>> attributeTypes;
    private final Hashtable<String, AttributeSetter> attributeSetters;
    private final Hashtable<String, Class<?>> nestedTypes;
    private final Hashtable<String, NestedCreator> nestedCreators;
    private final List<Method> addTypeMethods;
    private final Method addText;
    private final Class<?> bean;
    protected static final String NOT_SUPPORTED_CHILD_PREFIX = " doesn't support the nested \"";
    protected static final String NOT_SUPPORTED_CHILD_POSTFIX = "\" element.";
    
    private IntrospectionHelper(final Class<?> bean) {
        super();
        this.attributeTypes = new Hashtable<String, Class<?>>();
        this.attributeSetters = new Hashtable<String, AttributeSetter>();
        this.nestedTypes = new Hashtable<String, Class<?>>();
        this.nestedCreators = new Hashtable<String, NestedCreator>();
        this.addTypeMethods = new ArrayList<Method>();
        this.bean = bean;
        final Method[] methods = bean.getMethods();
        Method addTextMethod = null;
        for (int i = 0; i < methods.length; ++i) {
            final Method m = methods[i];
            final String name = m.getName();
            final Class<?> returnType = m.getReturnType();
            final Class<?>[] args = m.getParameterTypes();
            if (args.length == 1 && Void.TYPE.equals(returnType) && ("add".equals(name) || "addConfigured".equals(name))) {
                this.insertAddTypeMethod(m);
            }
            else if (!ProjectComponent.class.isAssignableFrom(bean) || args.length != 1 || !this.isHiddenSetMethod(name, args[0])) {
                if (!this.isContainer() || args.length != 1 || !"addTask".equals(name) || !Task.class.equals(args[0])) {
                    if ("addText".equals(name) && Void.TYPE.equals(returnType) && args.length == 1 && String.class.equals(args[0])) {
                        addTextMethod = methods[i];
                    }
                    else if (name.startsWith("set") && Void.TYPE.equals(returnType) && args.length == 1 && !args[0].isArray()) {
                        final String propName = getPropertyName(name, "set");
                        AttributeSetter as = this.attributeSetters.get(propName);
                        if (as != null) {
                            if (String.class.equals(args[0])) {
                                continue;
                            }
                            if (File.class.equals(args[0])) {
                                if (Resource.class.equals(as.type)) {
                                    continue;
                                }
                                if (FileProvider.class.equals(as.type)) {
                                    continue;
                                }
                            }
                        }
                        as = this.createAttributeSetter(m, args[0], propName);
                        if (as != null) {
                            this.attributeTypes.put(propName, args[0]);
                            this.attributeSetters.put(propName, as);
                        }
                    }
                    else if (name.startsWith("create") && !returnType.isArray() && !returnType.isPrimitive() && args.length == 0) {
                        final String propName = getPropertyName(name, "create");
                        if (this.nestedCreators.get(propName) == null) {
                            this.nestedTypes.put(propName, returnType);
                            this.nestedCreators.put(propName, new CreateNestedCreator(m));
                        }
                    }
                    else if (name.startsWith("addConfigured") && Void.TYPE.equals(returnType) && args.length == 1 && !String.class.equals(args[0]) && !args[0].isArray() && !args[0].isPrimitive()) {
                        try {
                            Constructor<?> constructor = null;
                            try {
                                constructor = args[0].getConstructor((Class<?>[])new Class[0]);
                            }
                            catch (NoSuchMethodException ex) {
                                constructor = args[0].getConstructor(Project.class);
                            }
                            final String propName2 = getPropertyName(name, "addConfigured");
                            this.nestedTypes.put(propName2, args[0]);
                            this.nestedCreators.put(propName2, new AddNestedCreator(m, constructor, 2));
                        }
                        catch (NoSuchMethodException nse) {}
                    }
                    else if (name.startsWith("add") && Void.TYPE.equals(returnType) && args.length == 1 && !String.class.equals(args[0]) && !args[0].isArray() && !args[0].isPrimitive()) {
                        try {
                            Constructor<?> constructor = null;
                            try {
                                constructor = args[0].getConstructor((Class<?>[])new Class[0]);
                            }
                            catch (NoSuchMethodException ex) {
                                constructor = args[0].getConstructor(Project.class);
                            }
                            final String propName2 = getPropertyName(name, "add");
                            if (this.nestedTypes.get(propName2) == null) {
                                this.nestedTypes.put(propName2, args[0]);
                                this.nestedCreators.put(propName2, new AddNestedCreator(m, constructor, 1));
                            }
                        }
                        catch (NoSuchMethodException ex2) {}
                    }
                }
            }
        }
        this.addText = addTextMethod;
    }
    
    private boolean isHiddenSetMethod(final String name, final Class<?> type) {
        return ("setLocation".equals(name) && Location.class.equals(type)) || ("setTaskType".equals(name) && String.class.equals(type));
    }
    
    public static synchronized IntrospectionHelper getHelper(final Class<?> c) {
        return getHelper(null, c);
    }
    
    public static IntrospectionHelper getHelper(final Project p, final Class<?> c) {
        IntrospectionHelper ih = IntrospectionHelper.HELPERS.get(c.getName());
        if (ih == null || ih.bean != c) {
            ih = new IntrospectionHelper(c);
            if (p != null) {
                IntrospectionHelper.HELPERS.put(c.getName(), ih);
            }
        }
        return ih;
    }
    
    public void setAttribute(final Project p, final Object element, final String attributeName, final Object value) throws BuildException {
        final AttributeSetter as = this.attributeSetters.get(attributeName.toLowerCase(Locale.ENGLISH));
        if (as != null || value == null) {
            try {
                as.setObject(p, element, value);
            }
            catch (IllegalAccessException ie) {
                throw new BuildException(ie);
            }
            catch (InvocationTargetException ite) {
                throw extractBuildException(ite);
            }
            return;
        }
        if (element instanceof DynamicAttributeNS) {
            final DynamicAttributeNS dc = (DynamicAttributeNS)element;
            final String uriPlusPrefix = ProjectHelper.extractUriFromComponentName(attributeName);
            final String uri = ProjectHelper.extractUriFromComponentName(uriPlusPrefix);
            final String localName = ProjectHelper.extractNameFromComponentName(attributeName);
            final String qName = "".equals(uri) ? localName : (uri + ":" + localName);
            dc.setDynamicAttribute(uri, localName, qName, value.toString());
            return;
        }
        if (element instanceof DynamicObjectAttribute) {
            final DynamicObjectAttribute dc2 = (DynamicObjectAttribute)element;
            dc2.setDynamicAttribute(attributeName.toLowerCase(Locale.ENGLISH), value);
            return;
        }
        if (element instanceof DynamicAttribute) {
            final DynamicAttribute dc3 = (DynamicAttribute)element;
            dc3.setDynamicAttribute(attributeName.toLowerCase(Locale.ENGLISH), value.toString());
            return;
        }
        if (attributeName.indexOf(58) >= 0) {
            return;
        }
        final String msg = this.getElementName(p, element) + " doesn't support the \"" + attributeName + "\" attribute.";
        throw new UnsupportedAttributeException(msg, attributeName);
    }
    
    public void setAttribute(final Project p, final Object element, final String attributeName, final String value) throws BuildException {
        this.setAttribute(p, element, attributeName, (Object)value);
    }
    
    public void addText(final Project project, final Object element, String text) throws BuildException {
        if (this.addText != null) {
            try {
                this.addText.invoke(element, text);
            }
            catch (IllegalAccessException ie) {
                throw new BuildException(ie);
            }
            catch (InvocationTargetException ite) {
                throw extractBuildException(ite);
            }
            return;
        }
        text = text.trim();
        if (text.length() == 0) {
            return;
        }
        throw new BuildException(project.getElementName(element) + " doesn't support nested text data (\"" + this.condenseText(text) + "\").");
    }
    
    public void throwNotSupported(final Project project, final Object parent, final String elementName) {
        final String msg = project.getElementName(parent) + " doesn't support the nested \"" + elementName + "\" element.";
        throw new UnsupportedElementException(msg, elementName);
    }
    
    private NestedCreator getNestedCreator(final Project project, String parentUri, final Object parent, final String elementName, final UnknownElement child) throws BuildException {
        String uri = ProjectHelper.extractUriFromComponentName(elementName);
        final String name = ProjectHelper.extractNameFromComponentName(elementName);
        if (uri.equals("antlib:org.apache.tools.ant")) {
            uri = "";
        }
        if (parentUri.equals("antlib:org.apache.tools.ant")) {
            parentUri = "";
        }
        NestedCreator nc = null;
        if (uri.equals(parentUri) || uri.length() == 0) {
            nc = this.nestedCreators.get(name.toLowerCase(Locale.ENGLISH));
        }
        if (nc == null) {
            nc = this.createAddTypeCreator(project, parent, elementName);
        }
        if (nc == null && (parent instanceof DynamicElementNS || parent instanceof DynamicElement)) {
            final String qName = (child == null) ? name : child.getQName();
            final Object nestedElement = this.createDynamicElement(parent, (child == null) ? "" : child.getNamespace(), name, qName);
            if (nestedElement != null) {
                nc = new NestedCreator(null) {
                    Object create(final Project project, final Object parent, final Object ignore) {
                        return nestedElement;
                    }
                };
            }
        }
        if (nc == null) {
            this.throwNotSupported(project, parent, elementName);
        }
        return nc;
    }
    
    private Object createDynamicElement(final Object parent, final String ns, final String localName, final String qName) {
        Object nestedElement = null;
        if (parent instanceof DynamicElementNS) {
            final DynamicElementNS dc = (DynamicElementNS)parent;
            nestedElement = dc.createDynamicElement(ns, localName, qName);
        }
        if (nestedElement == null && parent instanceof DynamicElement) {
            final DynamicElement dc2 = (DynamicElement)parent;
            nestedElement = dc2.createDynamicElement(localName.toLowerCase(Locale.ENGLISH));
        }
        return nestedElement;
    }
    
    public Object createElement(final Project project, final Object parent, final String elementName) throws BuildException {
        final NestedCreator nc = this.getNestedCreator(project, "", parent, elementName, null);
        try {
            final Object nestedElement = nc.create(project, parent, null);
            if (project != null) {
                project.setProjectReference(nestedElement);
            }
            return nestedElement;
        }
        catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        }
        catch (InstantiationException ine) {
            throw new BuildException(ine);
        }
        catch (InvocationTargetException ite) {
            throw extractBuildException(ite);
        }
    }
    
    public Creator getElementCreator(final Project project, final String parentUri, final Object parent, final String elementName, final UnknownElement ue) {
        final NestedCreator nc = this.getNestedCreator(project, parentUri, parent, elementName, ue);
        return new Creator(project, parent, nc);
    }
    
    public boolean isDynamic() {
        return DynamicElement.class.isAssignableFrom(this.bean) || DynamicElementNS.class.isAssignableFrom(this.bean);
    }
    
    public boolean isContainer() {
        return TaskContainer.class.isAssignableFrom(this.bean);
    }
    
    public boolean supportsNestedElement(final String elementName) {
        return this.supportsNestedElement("", elementName);
    }
    
    public boolean supportsNestedElement(final String parentUri, final String elementName) {
        return this.isDynamic() || this.addTypeMethods.size() > 0 || this.supportsReflectElement(parentUri, elementName);
    }
    
    public boolean supportsNestedElement(final String parentUri, final String elementName, final Project project, final Object parent) {
        return (this.addTypeMethods.size() > 0 && this.createAddTypeCreator(project, parent, elementName) != null) || this.isDynamic() || this.supportsReflectElement(parentUri, elementName);
    }
    
    public boolean supportsReflectElement(String parentUri, final String elementName) {
        final String name = ProjectHelper.extractNameFromComponentName(elementName);
        if (!this.nestedCreators.containsKey(name.toLowerCase(Locale.ENGLISH))) {
            return false;
        }
        String uri = ProjectHelper.extractUriFromComponentName(elementName);
        if (uri.equals("antlib:org.apache.tools.ant")) {
            uri = "";
        }
        if ("".equals(uri)) {
            return true;
        }
        if (parentUri.equals("antlib:org.apache.tools.ant")) {
            parentUri = "";
        }
        return uri.equals(parentUri);
    }
    
    public void storeElement(final Project project, final Object parent, final Object child, final String elementName) throws BuildException {
        if (elementName == null) {
            return;
        }
        final NestedCreator ns = this.nestedCreators.get(elementName.toLowerCase(Locale.ENGLISH));
        if (ns == null) {
            return;
        }
        try {
            ns.store(parent, child);
        }
        catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        }
        catch (InstantiationException ine) {
            throw new BuildException(ine);
        }
        catch (InvocationTargetException ite) {
            throw extractBuildException(ite);
        }
    }
    
    private static BuildException extractBuildException(final InvocationTargetException ite) {
        final Throwable t = ite.getTargetException();
        if (t instanceof BuildException) {
            return (BuildException)t;
        }
        return new BuildException(t);
    }
    
    public Class<?> getElementType(final String elementName) throws BuildException {
        final Class<?> nt = this.nestedTypes.get(elementName);
        if (nt == null) {
            throw new UnsupportedElementException("Class " + this.bean.getName() + " doesn't support the nested \"" + elementName + "\" element.", elementName);
        }
        return nt;
    }
    
    public Class<?> getAttributeType(final String attributeName) throws BuildException {
        final Class<?> at = this.attributeTypes.get(attributeName);
        if (at == null) {
            throw new UnsupportedAttributeException("Class " + this.bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
        }
        return at;
    }
    
    public Method getAddTextMethod() throws BuildException {
        if (!this.supportsCharacters()) {
            throw new BuildException("Class " + this.bean.getName() + " doesn't support nested text data.");
        }
        return this.addText;
    }
    
    public Method getElementMethod(final String elementName) throws BuildException {
        final Object creator = this.nestedCreators.get(elementName);
        if (creator == null) {
            throw new UnsupportedElementException("Class " + this.bean.getName() + " doesn't support the nested \"" + elementName + "\" element.", elementName);
        }
        return ((NestedCreator)creator).method;
    }
    
    public Method getAttributeMethod(final String attributeName) throws BuildException {
        final Object setter = this.attributeSetters.get(attributeName);
        if (setter == null) {
            throw new UnsupportedAttributeException("Class " + this.bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
        }
        return ((AttributeSetter)setter).method;
    }
    
    public boolean supportsCharacters() {
        return this.addText != null;
    }
    
    public Enumeration<String> getAttributes() {
        return this.attributeSetters.keys();
    }
    
    public Map<String, Class<?>> getAttributeMap() {
        return this.attributeTypes.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap((Map<? extends String, ? extends Class<?>>)this.attributeTypes);
    }
    
    public Enumeration<String> getNestedElements() {
        return this.nestedTypes.keys();
    }
    
    public Map<String, Class<?>> getNestedElementMap() {
        return this.nestedTypes.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap((Map<? extends String, ? extends Class<?>>)this.nestedTypes);
    }
    
    public List<Method> getExtensionPoints() {
        return this.addTypeMethods.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList((List<? extends Method>)this.addTypeMethods);
    }
    
    private AttributeSetter createAttributeSetter(final Method m, final Class<?> arg, final String attrName) {
        final Class<?> reflectedArg = IntrospectionHelper.PRIMITIVE_TYPE_MAP.containsKey(arg) ? IntrospectionHelper.PRIMITIVE_TYPE_MAP.get(arg) : arg;
        if (Object.class == reflectedArg) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException {
                    throw new BuildException("Internal ant problem - this should not get called");
                }
            };
        }
        if (String.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException {
                    m.invoke(parent, value);
                }
            };
        }
        if (Character.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException {
                    if (value.length() == 0) {
                        throw new BuildException("The value \"\" is not a legal value for attribute \"" + attrName + "\"");
                    }
                    m.invoke(parent, new Character(value.charAt(0)));
                }
            };
        }
        if (Boolean.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException {
                    m.invoke(parent, Project.toBoolean(value) ? Boolean.TRUE : Boolean.FALSE);
                }
            };
        }
        if (Class.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        m.invoke(parent, Class.forName(value));
                    }
                    catch (ClassNotFoundException ce) {
                        throw new BuildException(ce);
                    }
                }
            };
        }
        if (File.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException {
                    m.invoke(parent, p.resolveFile(value));
                }
            };
        }
        if (Resource.class.equals(reflectedArg) || FileProvider.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    m.invoke(parent, new FileResource(p, p.resolveFile(value)));
                }
            };
        }
        if (EnumeratedAttribute.class.isAssignableFrom(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        final EnumeratedAttribute ea = reflectedArg.newInstance();
                        ea.setValue(value);
                        m.invoke(parent, ea);
                    }
                    catch (InstantiationException ie) {
                        throw new BuildException(ie);
                    }
                }
            };
        }
        final AttributeSetter setter = this.getEnumSetter(reflectedArg, m, arg);
        if (setter != null) {
            return setter;
        }
        if (Long.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        m.invoke(parent, new Long(StringUtils.parseHumanSizes(value)));
                    }
                    catch (NumberFormatException e4) {
                        throw new BuildException("Can't assign non-numeric value '" + value + "' to" + " attribute " + attrName);
                    }
                    catch (InvocationTargetException e) {
                        throw e;
                    }
                    catch (IllegalAccessException e2) {
                        throw e2;
                    }
                    catch (Exception e3) {
                        throw new BuildException(e3);
                    }
                }
            };
        }
        Constructor<?> c;
        boolean includeProject;
        try {
            c = reflectedArg.getConstructor(Project.class, String.class);
            includeProject = true;
        }
        catch (NoSuchMethodException nme) {
            try {
                c = reflectedArg.getConstructor(String.class);
                includeProject = false;
            }
            catch (NoSuchMethodException nme2) {
                return null;
            }
        }
        final boolean finalIncludeProject = includeProject;
        final Constructor<?> finalConstructor = c;
        return new AttributeSetter(m, arg) {
            public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                try {
                    final Object[] array3;
                    if (finalIncludeProject) {
                        final Object[] array2;
                        final Object[] array = array2 = new Object[2];
                        array[0] = p;
                        array[1] = value;
                    }
                    else {
                        array3 = new Object[] { value };
                    }
                    final Object[] args = array3;
                    final Object attribute = finalConstructor.newInstance(args);
                    if (p != null) {
                        p.setProjectReference(attribute);
                    }
                    m.invoke(parent, attribute);
                }
                catch (InvocationTargetException e) {
                    final Throwable cause = e.getCause();
                    if (cause instanceof IllegalArgumentException) {
                        throw new BuildException("Can't assign value '" + value + "' to attribute " + attrName + ", reason: " + cause.getClass() + " with message '" + cause.getMessage() + "'");
                    }
                    throw e;
                }
                catch (InstantiationException ie) {
                    throw new BuildException(ie);
                }
            }
        };
    }
    
    private AttributeSetter getEnumSetter(final Class<?> reflectedArg, final Method m, final Class<?> arg) {
        if (reflectedArg.isEnum()) {
            return new AttributeSetter(m, arg) {
                public void set(final Project p, final Object parent, final String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    Enum<?> setValue;
                    try {
                        final Enum<?> enumValue = setValue = Enum.valueOf(reflectedArg, value);
                    }
                    catch (IllegalArgumentException e) {
                        throw new BuildException("'" + value + "' is not a permitted value for " + reflectedArg.getName());
                    }
                    m.invoke(parent, setValue);
                }
            };
        }
        return null;
    }
    
    private String getElementName(final Project project, final Object element) {
        return project.getElementName(element);
    }
    
    private static String getPropertyName(final String methodName, final String prefix) {
        return methodName.substring(prefix.length()).toLowerCase(Locale.ENGLISH);
    }
    
    public static void clearCache() {
        IntrospectionHelper.HELPERS.clear();
    }
    
    private NestedCreator createAddTypeCreator(final Project project, final Object parent, final String elementName) throws BuildException {
        if (this.addTypeMethods.size() == 0) {
            return null;
        }
        final ComponentHelper helper = ComponentHelper.getComponentHelper(project);
        final MethodAndObject restricted = this.createRestricted(helper, elementName, this.addTypeMethods);
        final MethodAndObject topLevel = this.createTopLevel(helper, elementName, this.addTypeMethods);
        if (restricted == null && topLevel == null) {
            return null;
        }
        if (restricted != null && topLevel != null) {
            throw new BuildException("ambiguous: type and component definitions for " + elementName);
        }
        final MethodAndObject methodAndObject = (restricted != null) ? restricted : topLevel;
        Object rObject = methodAndObject.object;
        if (methodAndObject.object instanceof PreSetDef.PreSetDefinition) {
            rObject = ((PreSetDef.PreSetDefinition)methodAndObject.object).createObject(project);
        }
        final Object nestedObject = methodAndObject.object;
        final Object realObject = rObject;
        return new NestedCreator(methodAndObject.method) {
            Object create(final Project project, final Object parent, final Object ignore) throws InvocationTargetException, IllegalAccessException {
                if (!this.getMethod().getName().endsWith("Configured")) {
                    this.getMethod().invoke(parent, realObject);
                }
                return nestedObject;
            }
            
            Object getRealObject() {
                return realObject;
            }
            
            void store(final Object parent, final Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
                if (this.getMethod().getName().endsWith("Configured")) {
                    this.getMethod().invoke(parent, realObject);
                }
            }
        };
    }
    
    private void insertAddTypeMethod(final Method method) {
        final Class<?> argClass = method.getParameterTypes()[0];
        for (int size = this.addTypeMethods.size(), c = 0; c < size; ++c) {
            final Method current = this.addTypeMethods.get(c);
            if (current.getParameterTypes()[0].equals(argClass)) {
                if (method.getName().equals("addConfigured")) {
                    this.addTypeMethods.set(c, method);
                }
                return;
            }
            if (current.getParameterTypes()[0].isAssignableFrom(argClass)) {
                this.addTypeMethods.add(c, method);
                return;
            }
        }
        this.addTypeMethods.add(method);
    }
    
    private Method findMatchingMethod(final Class<?> paramClass, final List<Method> methods) {
        if (paramClass == null) {
            return null;
        }
        Class<?> matchedClass = null;
        Method matchedMethod = null;
        for (int size = methods.size(), i = 0; i < size; ++i) {
            final Method method = methods.get(i);
            final Class<?> methodClass = method.getParameterTypes()[0];
            if (methodClass.isAssignableFrom(paramClass)) {
                if (matchedClass == null) {
                    matchedClass = methodClass;
                    matchedMethod = method;
                }
                else if (!methodClass.isAssignableFrom(matchedClass)) {
                    throw new BuildException("ambiguous: types " + matchedClass.getName() + " and " + methodClass.getName() + " match " + paramClass.getName());
                }
            }
        }
        return matchedMethod;
    }
    
    private String condenseText(final String text) {
        if (text.length() <= 20) {
            return text;
        }
        final int ends = (20 - "...".length()) / 2;
        return new StringBuffer(text).replace(ends, text.length() - ends, "...").toString();
    }
    
    private AntTypeDefinition findRestrictedDefinition(final ComponentHelper helper, final String componentName, final List<Method> methods) {
        AntTypeDefinition definition = null;
        Class<?> matchedDefinitionClass = null;
        final List<AntTypeDefinition> definitions = helper.getRestrictedDefinitions(componentName);
        if (definitions == null) {
            return null;
        }
        synchronized (definitions) {
            for (int size = definitions.size(), i = 0; i < size; ++i) {
                final AntTypeDefinition d = definitions.get(i);
                final Class<?> exposedClass = d.getExposedClass(helper.getProject());
                if (exposedClass != null) {
                    final Method method = this.findMatchingMethod(exposedClass, methods);
                    if (method != null) {
                        if (matchedDefinitionClass != null) {
                            throw new BuildException("ambiguous: restricted definitions for " + componentName + " " + matchedDefinitionClass + " and " + exposedClass);
                        }
                        matchedDefinitionClass = exposedClass;
                        definition = d;
                    }
                }
            }
        }
        return definition;
    }
    
    private MethodAndObject createRestricted(final ComponentHelper helper, final String elementName, final List<Method> addTypeMethods) {
        final Project project = helper.getProject();
        final AntTypeDefinition restrictedDefinition = this.findRestrictedDefinition(helper, elementName, addTypeMethods);
        if (restrictedDefinition == null) {
            return null;
        }
        final Method addMethod = this.findMatchingMethod(restrictedDefinition.getExposedClass(project), addTypeMethods);
        if (addMethod == null) {
            throw new BuildException("Ant Internal Error - contract mismatch for " + elementName);
        }
        final Object addedObject = restrictedDefinition.create(project);
        if (addedObject == null) {
            throw new BuildException("Failed to create object " + elementName + " of type " + restrictedDefinition.getTypeClass(project));
        }
        return new MethodAndObject(addMethod, addedObject);
    }
    
    private MethodAndObject createTopLevel(final ComponentHelper helper, final String elementName, final List<Method> methods) {
        final Class<?> clazz = helper.getComponentClass(elementName);
        if (clazz == null) {
            return null;
        }
        final Method addMethod = this.findMatchingMethod(clazz, this.addTypeMethods);
        if (addMethod == null) {
            return null;
        }
        final Object addedObject = helper.createComponent(elementName);
        return new MethodAndObject(addMethod, addedObject);
    }
    
    static {
        HELPERS = new Hashtable<String, IntrospectionHelper>();
        PRIMITIVE_TYPE_MAP = new HashMap<Class<?>, Class<?>>(8);
        final Class<?>[] primitives = (Class<?>[])new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
        final Class<?>[] wrappers = (Class<?>[])new Class[] { Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class };
        for (int i = 0; i < primitives.length; ++i) {
            IntrospectionHelper.PRIMITIVE_TYPE_MAP.put(primitives[i], wrappers[i]);
        }
    }
    
    public static final class Creator
    {
        private NestedCreator nestedCreator;
        private Object parent;
        private Project project;
        private Object nestedObject;
        private String polyType;
        
        private Creator(final Project project, final Object parent, final NestedCreator nestedCreator) {
            super();
            this.project = project;
            this.parent = parent;
            this.nestedCreator = nestedCreator;
        }
        
        public void setPolyType(final String polyType) {
            this.polyType = polyType;
        }
        
        public Object create() {
            if (this.polyType != null) {
                if (!this.nestedCreator.isPolyMorphic()) {
                    throw new BuildException("Not allowed to use the polymorphic form for this element");
                }
                final ComponentHelper helper = ComponentHelper.getComponentHelper(this.project);
                this.nestedObject = helper.createComponent(this.polyType);
                if (this.nestedObject == null) {
                    throw new BuildException("Unable to create object of type " + this.polyType);
                }
            }
            try {
                this.nestedObject = this.nestedCreator.create(this.project, this.parent, this.nestedObject);
                if (this.project != null) {
                    this.project.setProjectReference(this.nestedObject);
                }
                return this.nestedObject;
            }
            catch (IllegalAccessException ex) {
                throw new BuildException(ex);
            }
            catch (InstantiationException ex2) {
                throw new BuildException(ex2);
            }
            catch (IllegalArgumentException ex3) {
                if (this.polyType == null) {
                    throw ex3;
                }
                throw new BuildException("Invalid type used " + this.polyType);
            }
            catch (InvocationTargetException ex4) {
                throw extractBuildException(ex4);
            }
        }
        
        public Object getRealObject() {
            return this.nestedCreator.getRealObject();
        }
        
        public void store() {
            try {
                this.nestedCreator.store(this.parent, this.nestedObject);
            }
            catch (IllegalAccessException ex) {
                throw new BuildException(ex);
            }
            catch (InstantiationException ex2) {
                throw new BuildException(ex2);
            }
            catch (IllegalArgumentException ex3) {
                if (this.polyType == null) {
                    throw ex3;
                }
                throw new BuildException("Invalid type used " + this.polyType);
            }
            catch (InvocationTargetException ex4) {
                throw extractBuildException(ex4);
            }
        }
    }
    
    private abstract static class NestedCreator
    {
        private Method method;
        
        protected NestedCreator(final Method m) {
            super();
            this.method = m;
        }
        
        Method getMethod() {
            return this.method;
        }
        
        boolean isPolyMorphic() {
            return false;
        }
        
        Object getRealObject() {
            return null;
        }
        
        abstract Object create(final Project p0, final Object p1, final Object p2) throws InvocationTargetException, IllegalAccessException, InstantiationException;
        
        void store(final Object parent, final Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        }
    }
    
    private static class CreateNestedCreator extends NestedCreator
    {
        CreateNestedCreator(final Method m) {
            super(m);
        }
        
        Object create(final Project project, final Object parent, final Object ignore) throws InvocationTargetException, IllegalAccessException {
            return this.getMethod().invoke(parent, new Object[0]);
        }
    }
    
    private static class AddNestedCreator extends NestedCreator
    {
        static final int ADD = 1;
        static final int ADD_CONFIGURED = 2;
        private Constructor<?> constructor;
        private int behavior;
        
        AddNestedCreator(final Method m, final Constructor<?> c, final int behavior) {
            super(m);
            this.constructor = c;
            this.behavior = behavior;
        }
        
        boolean isPolyMorphic() {
            return true;
        }
        
        Object create(final Project project, final Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            if (child == null) {
                final Constructor<?> constructor = this.constructor;
                final Object[] initargs;
                if (this.constructor.getParameterTypes().length == 0) {
                    final Object[] array = new Object[0];
                }
                else {
                    initargs = new Object[] { project };
                }
                child = constructor.newInstance(initargs);
            }
            if (child instanceof PreSetDef.PreSetDefinition) {
                child = ((PreSetDef.PreSetDefinition)child).createObject(project);
            }
            if (this.behavior == 1) {
                this.istore(parent, child);
            }
            return child;
        }
        
        void store(final Object parent, final Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            if (this.behavior == 2) {
                this.istore(parent, child);
            }
        }
        
        private void istore(final Object parent, final Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            this.getMethod().invoke(parent, child);
        }
    }
    
    private abstract static class AttributeSetter
    {
        private Method method;
        private Class<?> type;
        
        protected AttributeSetter(final Method m, final Class<?> type) {
            super();
            this.method = m;
            this.type = type;
        }
        
        void setObject(final Project p, final Object parent, final Object value) throws InvocationTargetException, IllegalAccessException, BuildException {
            if (this.type != null) {
                Class<?> useType = this.type;
                if (this.type.isPrimitive()) {
                    if (value == null) {
                        throw new BuildException("Attempt to set primitive " + getPropertyName(this.method.getName(), "set") + " to null on " + parent);
                    }
                    useType = IntrospectionHelper.PRIMITIVE_TYPE_MAP.get(this.type);
                }
                if (value == null || useType.isInstance(value)) {
                    this.method.invoke(parent, value);
                    return;
                }
            }
            this.set(p, parent, value.toString());
        }
        
        abstract void set(final Project p0, final Object p1, final String p2) throws InvocationTargetException, IllegalAccessException, BuildException;
    }
    
    private static class MethodAndObject
    {
        private Method method;
        private Object object;
        
        public MethodAndObject(final Method method, final Object object) {
            super();
            this.method = method;
            this.object = object;
        }
    }
}
