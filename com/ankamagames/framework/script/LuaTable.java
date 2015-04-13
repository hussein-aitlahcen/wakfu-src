package com.ankamagames.framework.script;

import org.keplerproject.luajava.*;
import java.util.*;

public class LuaTable
{
    private final String m_name;
    private final Map<String, Object> m_fields;
    
    public LuaTable() {
        super();
        this.m_fields = new HashMap<String, Object>();
        this.m_name = null;
    }
    
    public LuaTable(final String name) {
        super();
        this.m_fields = new HashMap<String, Object>();
        this.m_name = name;
    }
    
    public final void addField(final String fieldName, final Object value) {
        this.m_fields.put(fieldName, value);
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public final void pushIn(final LuaState luaState) {
        assert this.m_name != null;
        if (this.m_name.length() == 0) {
            for (final Map.Entry<String, Object> entry : this.m_fields.entrySet()) {
                new LuaValue(entry.getValue()).pushIn(luaState);
                luaState.setGlobal((String)entry.getKey());
            }
        }
        else {
            luaState.newTable();
            for (final Map.Entry<String, Object> entry : this.m_fields.entrySet()) {
                luaState.pushString((String)entry.getKey());
                new LuaValue(entry.getValue()).pushIn(luaState);
                luaState.setTable(-3);
            }
            luaState.setGlobal(this.m_name);
        }
    }
}
