package com.ankamagames.xulor2.util.xmlToJava;

import java.io.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;

public class ThemeInitClassDocument extends AbstractClassDocument<ThemeInitMethodDocument>
{
    static final String DOC_VARNAME = "doc";
    private final DocumentParser m_documentParser;
    
    public ThemeInitClassDocument(final PrintWriter p, final String name, final String packageName, final DocumentParser doc) {
        super(p, name, packageName);
        this.m_currentMethodDocument = (T)new ThemeInitMethodDocument(doc, "doc", "initTheme", null, true);
        this.m_methodDocuments.add(this.m_currentMethodDocument);
        this.addImport(ThemeLoader.class);
        this.addImport(DocumentParser.class);
        this.addImport(Color.class);
        this.m_printWriter = p;
        this.m_documentParser = doc;
    }
    
    public String getDocumentParserVarName() {
        return "doc";
    }
    
    public DocumentParser getDocumentParser() {
        return this.m_documentParser;
    }
    
    public void pushMethod(final DocumentParser doc) {
        this.pushMethod(new ThemeInitMethodDocument(doc, "doc", "method" + this.m_guid++, (ThemeInitMethodDocument)this.m_currentMethodDocument, false));
    }
    
    @Override
    public void popMethod() {
        final ClassMethodCall cmc = new ClassMethodCall(null, ((ThemeInitMethodDocument)this.m_currentMethodDocument).getMethodName(), null, new String[] { ((ThemeInitMethodDocument)this.m_currentMethodDocument).getParamName() });
        super.popMethod();
        ((ThemeInitMethodDocument)this.m_currentMethodDocument).addGeneratedCommandLine(cmc);
    }
    
    @Override
    public void generateClass() {
        for (final ThemeInitMethodDocument doc : this.m_methodDocuments) {
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
        this.m_printWriter.println("public class " + this.m_name + " implements ThemeLoader {");
        this.m_printWriter.println();
        for (final ThemeInitMethodDocument doc : this.m_methodDocuments) {
            doc.generateMethod(this.m_printWriter);
            this.m_printWriter.println();
        }
        this.m_printWriter.println("}");
        this.m_printWriter.flush();
    }
}
