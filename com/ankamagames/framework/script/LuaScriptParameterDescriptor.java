package com.ankamagames.framework.script;

public class LuaScriptParameterDescriptor
{
    private final String m_name;
    private final String m_description;
    private final LuaScriptParameterType m_type;
    private final boolean m_optional;
    
    public LuaScriptParameterDescriptor(final String name, final String description, final LuaScriptParameterType type, final boolean optional) {
        super();
        assert type != null;
        this.m_name = name;
        this.m_description = description;
        this.m_type = type;
        this.m_optional = optional;
    }
    
    public LuaScriptParameterDescriptor(final String name, final LuaScriptParameterType type, final boolean optional) {
        super();
        assert type != null;
        this.m_name = name;
        this.m_description = "";
        this.m_type = type;
        this.m_optional = optional;
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public final boolean isOptional() {
        return this.m_optional;
    }
    
    public final LuaScriptParameterType getType() {
        return this.m_type;
    }
}
