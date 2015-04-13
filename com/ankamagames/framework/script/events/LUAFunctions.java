package com.ankamagames.framework.script.events;

import gnu.trove.*;
import com.ankamagames.framework.script.*;

public class LUAFunctions
{
    private final TIntObjectHashMap<FunctionData> m_scripts;
    
    public LUAFunctions() {
        super();
        this.m_scripts = new TIntObjectHashMap<FunctionData>();
    }
    
    public boolean removeScript(final LuaScript script) {
        this.m_scripts.remove(script.getId());
        return this.m_scripts.isEmpty();
    }
    
    public boolean run(final FilterableEvent event) {
        final FunctionData[] datas = new FunctionData[this.m_scripts.size()];
        for (final FunctionData data : this.m_scripts.getValues(datas)) {
            data.run(event);
            if (data.isUnloadOnRun()) {
                this.m_scripts.remove(data.getScriptId());
            }
        }
        return this.m_scripts.isEmpty();
    }
    
    public void addFunction(final LuaScript script, final String funcName, final LuaValue[] params, final boolean unloadOnRun) {
        this.m_scripts.put(script.getId(), new FunctionData(script, funcName, params, unloadOnRun));
    }
    
    public static class FunctionData
    {
        private final LuaScript m_script;
        private final String m_funcName;
        private final LuaValue[] m_params;
        private boolean m_unloadOnRun;
        
        public FunctionData(final LuaScript script, final String funcName, final LuaValue[] params, final boolean unloadonRun) {
            super();
            this.m_script = script;
            this.m_funcName = funcName;
            this.m_params = params;
            this.m_unloadOnRun = unloadonRun;
        }
        
        public boolean isUnloadOnRun() {
            return this.m_unloadOnRun;
        }
        
        public int getScriptId() {
            return this.m_script.getId();
        }
        
        public void setUnloadOnRun(final boolean unloadOnRun) {
            this.m_unloadOnRun = unloadOnRun;
        }
        
        public boolean run(final FilterableEvent event) {
            this.m_script.runFunction(this.m_funcName, this.m_params, event.getInfoForLUA());
            return false;
        }
    }
}
