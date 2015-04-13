package com.ankamagames.xulor2.util.xmlToJava;

import com.ankamagames.xulor2.core.*;
import java.io.*;
import java.util.*;

public class ThemeInitMethodDocument extends AbstractMethodDocument<ThemeInitMethodDocument>
{
    private DocumentParser m_documentParser;
    
    public ThemeInitMethodDocument(final DocumentParser param, final String paramName, final String methodName, final ThemeInitMethodDocument parentMethod, final boolean first) {
        super(paramName, methodName, parentMethod, first);
        this.m_documentParser = param;
    }
    
    @Override
    public void generateMethod(final PrintWriter writer) {
        this.m_definedVars.clear();
        writer.println("\tpublic void " + this.getMethodName() + "(DocumentParser " + this.m_paramName + ") {");
        for (final ClassCommand commandLine : this.m_commandLines) {
            writer.println("\t\t" + commandLine.getCommand(this));
        }
        writer.println("\t}");
    }
}
