package com.ankamagames.xulor2.util.xmlToJava;

import java.util.*;

public class ClassMethodCall implements ClassCommand
{
    private final String m_method;
    private final String m_objectName;
    private final Class<?> m_type;
    private final ArrayList<String> m_params;
    
    public ClassMethodCall(final Class<?> type, final String methodName, final String objectName) {
        super();
        this.m_params = new ArrayList<String>();
        this.m_method = methodName;
        this.m_objectName = objectName;
        this.m_type = type;
    }
    
    public ClassMethodCall(final Class<?> type, final String methodName, final String objectName, final String... params) {
        super();
        this.m_params = new ArrayList<String>();
        this.m_method = methodName;
        this.m_objectName = objectName;
        this.m_type = type;
        for (int i = 0; i < params.length; ++i) {
            this.m_params.add(params[i]);
        }
    }
    
    public void addParam(final String p) {
        this.m_params.add(p);
    }
    
    @Override
    public Class<?> getTemplate() {
        return this.m_type;
    }
    
    @Override
    public String getCommand(final DocumentVariableAccessor doc) {
        final StringBuilder sb = new StringBuilder();
        if (this.m_objectName != null) {
            if (this.m_type != null) {
                sb.append("((").append(this.m_type.getSimpleName()).append(")");
            }
            sb.append(this.m_objectName);
            if (this.m_type != null) {
                sb.append(")");
            }
            sb.append(".");
        }
        sb.append(this.m_method).append("(");
        boolean first = true;
        for (final String p : this.m_params) {
            if (!first) {
                sb.append(", ");
            }
            else {
                first = false;
            }
            sb.append(p);
        }
        sb.append(");");
        return sb.toString();
    }
}
