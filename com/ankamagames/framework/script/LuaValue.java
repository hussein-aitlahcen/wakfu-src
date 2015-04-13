package com.ankamagames.framework.script;

import org.keplerproject.luajava.*;

public final class LuaValue
{
    private final Object m_value;
    private final LuaScriptParameterType m_type;
    
    public LuaValue(final Object value) {
        super();
        if (value == null) {
            this.m_type = LuaScriptParameterType.NULL;
            this.m_value = value;
            return;
        }
        if (value instanceof String) {
            this.m_type = LuaScriptParameterType.STRING;
            this.m_value = value;
            return;
        }
        if (value instanceof Boolean) {
            this.m_type = LuaScriptParameterType.BOOLEAN;
            this.m_value = value;
            return;
        }
        if (value instanceof Double) {
            this.m_type = LuaScriptParameterType.NUMBER;
            this.m_value = value;
            return;
        }
        if (value instanceof Integer) {
            this.m_type = LuaScriptParameterType.INTEGER;
            this.m_value = value;
            return;
        }
        if (value instanceof Long) {
            this.m_type = LuaScriptParameterType.LONG;
            this.m_value = value;
            return;
        }
        if (value instanceof Float) {
            this.m_type = LuaScriptParameterType.NUMBER;
            this.m_value = value;
            return;
        }
        if (value instanceof Short) {
            this.m_type = LuaScriptParameterType.INTEGER;
            this.m_value = value;
            return;
        }
        if (value instanceof Byte) {
            this.m_type = LuaScriptParameterType.INTEGER;
            this.m_value = value;
            return;
        }
        if (value instanceof Character) {
            this.m_type = LuaScriptParameterType.STRING;
            this.m_value = ((Character)value).toString();
            return;
        }
        this.m_type = LuaScriptParameterType.OBJECT;
        this.m_value = value;
    }
    
    public LuaScriptParameterType getType() {
        return this.m_type;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void pushIn(final LuaState luaState) {
        switch (this.getType()) {
            case NUMBER: {
                luaState.pushNumber((double)this.getValue());
                break;
            }
            case INTEGER: {
                luaState.pushNumber((double)(int)this.getValue());
                break;
            }
            case BOOLEAN: {
                luaState.pushBoolean((boolean)this.getValue());
                break;
            }
            case STRING: {
                luaState.pushString((String)this.getValue());
                break;
            }
            case OBJECT:
            case LONG: {
                luaState.pushJavaObject(this.getValue());
                break;
            }
            case NULL: {
                luaState.pushNil();
                break;
            }
        }
    }
    
    public static LuaValue createFrom(final LuaState luaState, final int stackIndex) throws LuaException {
        if (luaState.isNumber(stackIndex)) {
            return new LuaValue(luaState.toNumber(stackIndex));
        }
        if (luaState.isBoolean(stackIndex)) {
            return new LuaValue(luaState.toBoolean(stackIndex));
        }
        if (luaState.isString(stackIndex)) {
            return new LuaValue(luaState.toString(stackIndex));
        }
        if (luaState.isObject(stackIndex)) {
            return new LuaValue(luaState.toJavaObject(stackIndex));
        }
        if (luaState.isNil(stackIndex)) {
            return new LuaValue(null);
        }
        throw new LuaException("Valeur de type inconnu dans un script Lua");
    }
}
