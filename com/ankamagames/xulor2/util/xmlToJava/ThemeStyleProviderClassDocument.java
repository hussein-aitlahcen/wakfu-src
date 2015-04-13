package com.ankamagames.xulor2.util.xmlToJava;

import java.io.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;

public class ThemeStyleProviderClassDocument extends AbstractClassDocument<ThemeStyleProviderMethodDocument>
{
    private int m_depth;
    
    public ThemeStyleProviderClassDocument(final PrintWriter p, final String name, final String packageName) {
        super(p, name, packageName);
        this.m_depth = 0;
        this.addImport(StyleProvider.class);
        this.addImport(StyleSetter.class);
        this.addImport(HashMap.class);
        this.m_currentMethodDocument = (T)new ThemeStyleProviderMethodDocument("init", null);
        this.m_methodDocuments.add(this.m_currentMethodDocument);
    }
    
    @Override
    public void addGeneratedCommandLine(final ClassCommand commandLine) {
        if (((ThemeStyleProviderMethodDocument)this.m_currentMethodDocument).isFull()) {
            this.pushMethod();
            ++this.m_depth;
        }
        super.addGeneratedCommandLine(commandLine);
    }
    
    public void pushMethod() {
        this.pushMethod(new ThemeStyleProviderMethodDocument("init" + this.m_guid++, (ThemeStyleProviderMethodDocument)this.m_currentMethodDocument));
    }
    
    @Override
    public void popMethod() {
        final ClassMethodCall cmc = new ClassMethodCall(null, ((ThemeStyleProviderMethodDocument)this.m_currentMethodDocument).getMethodName(), null);
        super.popMethod();
        ((ThemeStyleProviderMethodDocument)this.m_currentMethodDocument).addGeneratedCommandLine(cmc);
    }
    
    @Override
    public void generateClass() {
        for (int i = this.m_depth - 1; i >= 0; --i) {
            this.popMethod();
        }
        for (final ThemeStyleProviderMethodDocument doc : this.m_methodDocuments) {
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
        this.m_printWriter.println("public class " + this.m_name + " implements StyleProvider {");
        this.m_printWriter.println();
        this.m_printWriter.println("\tprivate HashMap<String, StyleSetter> m_setters = new HashMap<String, StyleSetter>();");
        this.m_printWriter.println("\tpublic " + this.m_name + "() {");
        this.m_printWriter.println("\t\tinit();");
        this.m_printWriter.println("\t}");
        this.m_printWriter.println("\tpublic StyleSetter getStyleSetter(String style) {");
        this.m_printWriter.println("\t\treturn m_setters.get(style);");
        this.m_printWriter.println("\t}");
        this.m_printWriter.println();
        for (final ThemeStyleProviderMethodDocument doc : this.m_methodDocuments) {
            doc.generateMethod(this.m_printWriter);
            this.m_printWriter.println();
        }
        this.m_printWriter.println("}");
        this.m_printWriter.flush();
    }
}
