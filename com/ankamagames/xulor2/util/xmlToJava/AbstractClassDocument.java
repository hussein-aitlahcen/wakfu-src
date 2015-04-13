package com.ankamagames.xulor2.util.xmlToJava;

import java.io.*;
import java.util.*;

public abstract class AbstractClassDocument<T extends AbstractMethodDocument<T>>
{
    protected String m_name;
    protected String m_packageName;
    protected final HashSet<Class<?>> m_imports;
    protected final ArrayList<T> m_methodDocuments;
    protected T m_currentMethodDocument;
    protected PrintWriter m_printWriter;
    protected int m_guid;
    
    public AbstractClassDocument(final PrintWriter p, final String name, final String packageName) {
        super();
        this.m_imports = new HashSet<Class<?>>();
        this.m_methodDocuments = new ArrayList<T>();
        this.m_currentMethodDocument = null;
        this.m_guid = 0;
        if (name == null || name.length() == 0) {
            this.m_name = "Class0";
        }
        else {
            this.m_name = name;
        }
        this.m_packageName = packageName;
        this.m_printWriter = p;
    }
    
    protected void init() {
    }
    
    public void addImport(final Class<?> c) {
        if (!c.isPrimitive()) {
            this.m_imports.add(c);
        }
    }
    
    public void addGeneratedCommandLine(final ClassCommand commandLine) {
        this.m_currentMethodDocument.addGeneratedCommandLine(commandLine);
    }
    
    public String addVar(final Object var) {
        return this.m_currentMethodDocument.addVar(var);
    }
    
    public Object getVar(final String key) {
        return this.m_currentMethodDocument.getVar(key);
    }
    
    public String getUnusedVarName() {
        return this.m_currentMethodDocument.getUnusedVarName();
    }
    
    public void setVarValue(final String key, final Object value) {
        this.m_currentMethodDocument.setVarValue(key, value);
    }
    
    public boolean isVarDefined(final String varName) {
        return this.m_currentMethodDocument.isVarDefined(varName);
    }
    
    public void setVarDefined(final String varName) {
        this.m_currentMethodDocument.setVarDefined(varName);
    }
    
    public String getClassName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.m_name.substring(0, 1).toUpperCase());
        if (this.m_name.length() > 1) {
            sb.append(this.m_name.substring(0, this.m_name.length()));
        }
        return sb.toString();
    }
    
    public boolean isCurrentMethodFull() {
        return this.m_currentMethodDocument.isFull();
    }
    
    public void mark() {
        this.m_currentMethodDocument.mark();
    }
    
    public void resetMark() {
        this.m_currentMethodDocument.resetMark();
    }
    
    public void deleteCommandsFromMark() {
        this.m_currentMethodDocument.deleteCommandsFromMark();
    }
    
    protected void pushMethod(final T child) {
        this.m_currentMethodDocument.addChildMethod(child);
        this.m_currentMethodDocument = child;
        this.m_methodDocuments.add(this.m_currentMethodDocument);
    }
    
    public void popMethod() {
        this.m_currentMethodDocument = this.m_currentMethodDocument.getParentMethod();
    }
    
    public void generateClass() {
        for (final T doc : this.m_methodDocuments) {
            for (final ClassCommand commandLine : doc.getCommands()) {
                final Class<?> imported = commandLine.getTemplate();
                if (imported != null) {
                    this.addImport(imported);
                }
            }
        }
        if (this.m_packageName != null) {
            this.m_printWriter.println("package " + this.m_packageName + ";");
        }
        this.m_printWriter.println();
        for (final Class<?> c : this.m_imports) {
            this.m_printWriter.println("import " + c.getCanonicalName() + ";");
        }
        this.m_printWriter.println();
        this.m_printWriter.println("public class " + this.m_name + " implements BasicElementFactory {");
        this.m_printWriter.println();
        this.m_printWriter.println("private Stack<ElementMap> elementMaps = new Stack<ElementMap>();");
        this.m_printWriter.println("private Environment env;");
        this.m_printWriter.println();
        for (final T doc : this.m_methodDocuments) {
            doc.generateMethod(this.m_printWriter);
            this.m_printWriter.println();
        }
        this.m_printWriter.println("}");
        this.m_printWriter.flush();
    }
}
