package com.ankamagames.framework.script;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import org.keplerproject.luajava.*;

public abstract class JavaFunctionsLibrary
{
    protected static final Logger m_logger;
    
    @Nullable
    public abstract String getName();
    
    public abstract String getDescription();
    
    void importLibs(final LuaState L) throws LuaException {
        final JavaFunctionEx[] func = this.createFunctions(L);
        if (func != null) {
            L.newTable();
            for (final JavaFunctionEx function : func) {
                if (function != null) {
                    assert function.getName() != null;
                    L.pushString(function.getName());
                    L.pushJavaFunction((JavaFunction)function);
                    L.setTable(-3);
                }
            }
            if (this.getName() != null) {
                L.setGlobal(this.getName());
            }
            else {
                L.setGlobal("UnknownLibrary " + this.toString());
            }
        }
        final JavaFunctionEx[] globalFunc = this.createGlobalFunctions(L);
        if (globalFunc != null) {
            for (final JavaFunctionEx function2 : globalFunc) {
                function2.register();
            }
        }
    }
    
    @Nullable
    public abstract JavaFunctionEx[] createFunctions(final LuaState p0);
    
    @Nullable
    public abstract JavaFunctionEx[] createGlobalFunctions(final LuaState p0);
    
    static {
        m_logger = Logger.getLogger((Class)JavaFunctionsLibrary.class);
    }
}
