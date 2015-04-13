package com.ankamagames.xulor2.util.xmlToJava;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import java.io.*;
import java.util.*;

public class MethodDocument<T extends MethodDocument> extends AbstractMethodDocument<T>
{
    protected BasicElement m_param;
    protected String m_rootParent;
    protected String m_rootTagElement;
    protected String m_returnValue;
    
    public MethodDocument(final DocumentEntry tagElement, final BasicElement param, final String paramName, final String methodName, final T parentMethod, final boolean first) {
        super(paramName, methodName, parentMethod, first);
        this.m_returnValue = null;
        this.init(first);
        this.m_rootTagElement = this.addVar(tagElement);
        this.setParam(param, paramName);
        this.m_returnValue = null;
    }
    
    protected void init(final boolean first) {
        if (first) {
            this.addGeneratedCommandLine(new ClassVariable(Environment.class, "env", "environment", true));
            this.addGeneratedCommandLine(new ClassMethodCall(null, "push", "elementMaps", new String[] { "currentElementMap" }));
        }
        this.addGeneratedCommandLine(new ClassVariable(ElementMap.class, "elementMap", "elementMaps.peek()"));
    }
    
    @Override
    String addVar(final Object var) {
        final String key = super.addVar(var);
        if (this.m_returnValue == null && var instanceof EventDispatcher) {
            this.m_returnValue = key;
        }
        return key;
    }
    
    public BasicElement getParam() {
        return this.m_param;
    }
    
    public String getRootParent() {
        return this.m_rootParent;
    }
    
    public String getRootTagElement() {
        return this.m_rootTagElement;
    }
    
    public String getReturnValue() {
        return this.m_returnValue;
    }
    
    @Override
    public void setVarValue(final String key, final Object value) {
        super.setVarValue(key, value);
        if (this.m_returnValue == null && value instanceof EventDispatcher) {
            this.m_returnValue = key;
        }
    }
    
    public void setParam(final BasicElement param, final String paramName, final boolean overRideRootParentName) {
        this.m_param = param;
        this.m_paramName = paramName;
        if (this.m_param != null) {
            this.m_rootParent = this.addVar(param);
        }
        if (overRideRootParentName) {
            this.m_rootParent = paramName;
        }
        if (this.m_param != null && this.m_paramName != null) {
            this.setVarValue(this.m_paramName, this.m_param);
        }
    }
    
    public void setParam(final BasicElement param, final String paramName) {
        this.setParam(param, paramName, false);
    }
    
    @Override
    public void generateMethod(final PrintWriter writer) {
        this.m_definedVars.clear();
        if (this.m_paramName == null && this.m_param == null) {
            writer.println("\tpublic BasicElement " + this.getMethodName() + "(Environment environment, ElementMap currentElementMap) {");
        }
        else {
            writer.println("\tpublic BasicElement " + this.getMethodName() + "(BasicElement " + this.m_paramName + ") {");
        }
        for (final ClassCommand commandLine : this.m_commandLines) {
            writer.println("\t\t" + commandLine.getCommand(this));
        }
        writer.println("\t\treturn " + this.m_returnValue + ";");
        writer.println("\t}");
    }
}
