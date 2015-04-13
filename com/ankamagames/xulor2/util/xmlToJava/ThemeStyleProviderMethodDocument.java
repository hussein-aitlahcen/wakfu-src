package com.ankamagames.xulor2.util.xmlToJava;

import java.io.*;
import java.util.*;

public class ThemeStyleProviderMethodDocument extends AbstractMethodDocument<ThemeStyleProviderMethodDocument>
{
    public ThemeStyleProviderMethodDocument(final String methodName, final ThemeStyleProviderMethodDocument parentMethod) {
        super(null, methodName, parentMethod, false);
    }
    
    @Override
    public void generateMethod(final PrintWriter writer) {
        this.m_definedVars.clear();
        writer.println("\tpublic void " + this.getMethodName() + "() {");
        for (final ClassCommand commandLine : this.m_commandLines) {
            writer.println("\t\t" + commandLine.getCommand(this));
        }
        writer.println("\t}");
    }
}
