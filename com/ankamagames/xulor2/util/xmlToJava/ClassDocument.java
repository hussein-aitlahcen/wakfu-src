package com.ankamagames.xulor2.util.xmlToJava;

import com.ankamagames.framework.fileFormat.document.*;
import java.io.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;

public class ClassDocument extends AbstractClassDocument
{
    private DocumentEntry m_tagElement;
    
    public ClassDocument(final PrintWriter p, final String name, final String packageName, final DocumentEntry tagElement) {
        super(p, name, packageName);
        this.initMethod(tagElement);
        this.addImport(BasicElement.class);
        this.addImport(EventDispatcher.class);
        this.addImport(BasicElementFactory.class);
        this.addImport(DocumentEntry.class);
        this.addImport(Stack.class);
    }
    
    protected void initMethod(final DocumentEntry tagElement) {
        this.m_currentMethodDocument = (T)new AbstractMethodDocument(tagElement, (BasicElement)null, (String)null, "getElement", (T)null, true);
        this.m_methodDocuments.add(this.m_currentMethodDocument);
    }
    
    public String getRootParent() {
        return ((MethodDocument)this.m_methodDocuments.get(0)).getRootParent();
    }
    
    public String getRootTagElement() {
        return ((MethodDocument)this.m_methodDocuments.get(0)).getRootTagElement();
    }
    
    public void pushMethod(final DocumentEntry tagElement, final BasicElement parent, final String parentName) {
        this.pushMethod(new MethodDocument<MethodDocument>(tagElement, parent, parentName, "method" + this.m_guid++, (MethodDocument)this.m_currentMethodDocument, false));
    }
    
    @Override
    public void popMethod() {
        final ClassMethodCall cmc = new ClassMethodCall(null, this.m_currentMethodDocument.getMethodName(), null, new String[] { this.m_currentMethodDocument.getParamName() });
        super.popMethod();
        this.m_currentMethodDocument.addGeneratedCommandLine(cmc);
    }
    
    @Override
    public void generateClass() {
        for (int i = 0, size = this.m_methodDocuments.size(); i < size; ++i) {
            final MethodDocument doc = (MethodDocument)this.m_methodDocuments.get(i);
            final ArrayList<ClassCommand> commands = (ArrayList<ClassCommand>)doc.getCommands();
            for (int j = 0, jsize = commands.size(); j < jsize; ++j) {
                final Class<?> imported = commands.get(j).getTemplate();
                if (imported != null) {
                    this.addImport(imported);
                }
            }
        }
        if (this.m_packageName != null) {
            this.m_printWriter.println("package " + this.m_packageName + ";");
        }
        this.m_printWriter.println();
        final Iterator<Class<?>> it = this.m_imports.iterator();
        while (it.hasNext()) {
            this.m_printWriter.println("import " + it.next().getCanonicalName() + ";");
        }
        this.m_printWriter.println();
        this.m_printWriter.println("public class " + this.m_name + " implements BasicElementFactory {");
        this.m_printWriter.println();
        this.m_printWriter.println("private Stack<ElementMap> elementMaps = new Stack<ElementMap>();");
        this.m_printWriter.println("private Environment env;");
        this.m_printWriter.println();
        for (int k = 0, size2 = this.m_methodDocuments.size(); k < size2; ++k) {
            final MethodDocument doc2 = (MethodDocument)this.m_methodDocuments.get(k);
            doc2.generateMethod(this.m_printWriter);
            this.m_printWriter.println();
        }
        this.m_printWriter.println("}");
        this.m_printWriter.flush();
    }
}
