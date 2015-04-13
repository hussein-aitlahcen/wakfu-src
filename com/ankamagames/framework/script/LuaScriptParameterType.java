package com.ankamagames.framework.script;

import org.keplerproject.luajava.*;

public enum LuaScriptParameterType
{
    OBJECT {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isObject(index);
        }
    }, 
    LONG {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isObject(index) || state.isNumber(index);
        }
    }, 
    STRING {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isString(index);
        }
    }, 
    INTEGER {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isNumber(index);
        }
    }, 
    NUMBER {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isNumber(index);
        }
    }, 
    BOOLEAN {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isBoolean(index);
        }
    }, 
    TABLE {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isTable(index);
        }
    }, 
    BLOOPS {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return true;
        }
    }, 
    NULL {
        @Override
        public boolean checkParam(final LuaState state, final int index) {
            return state.isNil(index);
        }
    };
    
    final boolean equals(final LuaScriptParameterType type) {
        if (this == type) {
            return true;
        }
        if (this == LuaScriptParameterType.INTEGER) {
            return type == LuaScriptParameterType.NUMBER;
        }
        return this == LuaScriptParameterType.NUMBER && type == LuaScriptParameterType.INTEGER;
    }
    
    public abstract boolean checkParam(final LuaState p0, final int p1);
}
