package com.ankamagames.xulor2.util.xmlToJava;

public class ClassVariable implements ClassCommand
{
    private Class<?> m_type;
    private String m_name;
    private String m_command;
    private boolean m_defined;
    
    public ClassVariable(final Class<?> type, final String name, final String command) {
        this(type, name, command, false);
    }
    
    public ClassVariable(final Class<?> type, final String name, final String command, final boolean defined) {
        super();
        this.m_type = type;
        this.m_name = name;
        this.m_command = command;
        this.m_defined = defined;
    }
    
    @Override
    public Class<?> getTemplate() {
        return this.m_type;
    }
    
    @Override
    public String getCommand(final DocumentVariableAccessor doc) {
        final StringBuilder sb = new StringBuilder();
        if (!this.m_defined && !doc.isVarDefined(this.m_name)) {
            doc.setVarDefined(this.m_name);
            sb.append(this.m_type.getSimpleName()).append(" ");
        }
        sb.append(this.m_name).append(" = ");
        sb.append("(").append(this.m_type.getSimpleName()).append(") ");
        sb.append(this.m_command).append(";");
        return sb.toString();
    }
}
