package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

public class BitOperatorFunctionsLibrary extends JavaFunctionsLibrary
{
    @Override
    public String getName() {
        return "BitOperator";
    }
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new Or(luaState), new And(luaState), new Shift(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    private static class Shift extends JavaFunctionEx
    {
        Shift(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "Shift";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("direction (>>, <<, >>>)", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("shift", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            if (paramCount != 3) {
                BitOperatorFunctionsLibrary.m_logger.error((Object)"nombre de param?tres incorrect");
            }
            else {
                long number = this.getParamLong(0);
                final String dir = this.getParamString(1);
                final int decalage = this.getParamInt(2);
                if (dir.equals("<<")) {
                    number <<= decalage;
                }
                else if (dir.equals(">>")) {
                    number >>= decalage;
                }
                else if (dir.equals(">>>")) {
                    number >>>= decalage;
                }
                this.addReturnValue(number);
            }
        }
    }
    
    private static class And extends JavaFunctionEx
    {
        And(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "And";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            if (paramCount == 2) {
                this.addReturnValue(this.getParamLong(0) & this.getParamLong(1));
            }
        }
    }
    
    private static class Or extends JavaFunctionEx
    {
        Or(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "Or";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("number", LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            if (paramCount == 2) {
                this.addReturnValue(this.getParamLong(0) | this.getParamLong(1));
            }
        }
    }
}
