package com.ankamagames.xulor2.util.xmlToJava;

import java.util.*;
import java.io.*;

public abstract class AbstractMethodDocument<T extends AbstractMethodDocument> implements DocumentVariableAccessor
{
    public static final int COMMAND_LIMIT = 500;
    protected String m_paramName;
    protected String m_methodName;
    protected T m_parentMethod;
    protected final ArrayList<ClassCommand> m_commandLines;
    protected final ArrayList<T> m_children;
    protected final HashMap<String, Object> m_vars;
    protected final HashSet<String> m_definedVars;
    protected int m_guid;
    private int m_mark;
    
    public AbstractMethodDocument(final String paramName, final String methodName, final T parentMethod, final boolean first) {
        super();
        this.m_commandLines = new ArrayList<ClassCommand>();
        this.m_children = new ArrayList<T>();
        this.m_vars = new HashMap<String, Object>();
        this.m_definedVars = new HashSet<String>();
        this.m_guid = 0;
        this.m_paramName = paramName;
        this.m_methodName = methodName;
        this.m_parentMethod = parentMethod;
    }
    
    void addGeneratedCommandLine(final ClassCommand commandLine) {
        this.m_commandLines.add(commandLine);
    }
    
    void addChildMethod(final T doc) {
        this.m_children.add(doc);
    }
    
    String addVar(final Object var) {
        String key = "var";
        if (var != null) {
            key += var.getClass().getSimpleName();
        }
        final String baseName = key;
        do {
            key = baseName + this.m_guid;
            ++this.m_guid;
        } while (this.m_vars.containsKey(key));
        this.m_vars.put(key, var);
        return key;
    }
    
    public String getParamName() {
        return this.m_paramName;
    }
    
    public T getParentMethod() {
        return this.m_parentMethod;
    }
    
    public Object getVar(final String key) {
        return this.m_vars.get(key);
    }
    
    @Override
    public String getUnusedVarName() {
        String key = null;
        do {
            key = "var" + this.m_guid;
            ++this.m_guid;
        } while (this.m_vars.containsKey(key));
        this.m_vars.put(key, null);
        return key;
    }
    
    public boolean isFull() {
        return this.m_commandLines.size() >= 500;
    }
    
    public void setVarValue(final String key, final Object value) {
        this.m_vars.put(key, value);
    }
    
    @Override
    public boolean isVarDefined(final String varName) {
        return this.m_definedVars.contains(varName);
    }
    
    @Override
    public void setVarDefined(final String varName) {
        this.m_definedVars.add(varName);
    }
    
    public ArrayList<ClassCommand> getCommands() {
        return this.m_commandLines;
    }
    
    public ArrayList<T> getChildren() {
        return this.m_children;
    }
    
    public String getMethodName() {
        return this.m_methodName;
    }
    
    public void mark() {
        this.m_mark = this.m_commandLines.size();
    }
    
    public void resetMark() {
        this.m_mark = -1;
    }
    
    public void deleteCommandsFromMark() {
        if (this.m_mark == -1) {
            return;
        }
        for (int i = this.m_commandLines.size() - 1; i >= this.m_mark; --i) {
            this.m_commandLines.remove(i);
        }
    }
    
    public abstract void generateMethod(final PrintWriter p0);
}
