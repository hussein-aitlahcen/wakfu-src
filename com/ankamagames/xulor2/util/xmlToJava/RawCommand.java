package com.ankamagames.xulor2.util.xmlToJava;

public class RawCommand implements ClassCommand
{
    private String m_rawCommand;
    
    public RawCommand(final String c) {
        super();
        this.m_rawCommand = c;
    }
    
    @Override
    public Class<?> getTemplate() {
        return null;
    }
    
    @Override
    public String getCommand(final DocumentVariableAccessor doc) {
        if (this.m_rawCommand == null) {
            return "";
        }
        return this.m_rawCommand;
    }
}
