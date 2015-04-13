package com.ankamagames.xulor2.util.xmlToJava;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import java.io.*;
import java.util.*;

public class ThemeStyleMethodDocument extends MethodDocument<ThemeStyleMethodDocument>
{
    private static final String DOCUMENT_PARSER_VAR_NAME = "doc";
    private boolean m_first;
    
    public ThemeStyleMethodDocument(final DocumentEntry tagElement, final BasicElement param, final String paramName, final String methodName, final ThemeStyleMethodDocument parentMethod, final boolean first) {
        super(tagElement, param, paramName, methodName, parentMethod, first);
        this.m_first = first;
    }
    
    @Override
    protected void init(final boolean first) {
        if (first) {
            this.addGeneratedCommandLine(new ClassVariable(DocumentParser.class, "doc", "parser", true));
            this.addGeneratedCommandLine(new ClassMethodCall(null, "push", "elementMaps", new String[] { "currentElementMap" }));
        }
        this.addGeneratedCommandLine(new ClassVariable(ElementMap.class, "elementMap", "elementMaps.peek()"));
    }
    
    public String getDocumentParserVarName() {
        return "doc";
    }
    
    @Override
    public void setParam(final BasicElement param, final String paramName) {
        this.m_param = param;
        this.m_paramName = paramName;
        if (this.m_param != null) {
            this.m_rootParent = this.addVar(param);
        }
        if (this.m_param != null && this.m_paramName != null) {
            this.setVarValue(this.m_paramName, this.m_param);
        }
    }
    
    @Override
    public void generateMethod(final PrintWriter writer) {
        this.m_definedVars.clear();
        if (this.m_first) {
            writer.println("\tpublic void " + this.getMethodName() + "(ElementMap currentElementMap, DocumentParser parser, Widget " + this.m_paramName + ") {");
        }
        else {
            writer.println("\tpublic BasicElement " + this.getMethodName() + "(BasicElement " + this.m_paramName + ") {");
        }
        for (final ClassCommand commandLine : this.m_commandLines) {
            writer.println("\t\t" + commandLine.getCommand(this));
        }
        if (!this.m_first) {
            writer.println("\t\treturn " + this.m_returnValue + ";");
        }
        writer.println("\t}");
    }
}
