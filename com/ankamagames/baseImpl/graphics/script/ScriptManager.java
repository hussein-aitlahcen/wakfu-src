package com.ankamagames.baseImpl.graphics.script;

import org.apache.log4j.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.script.*;

public class ScriptManager
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final String SCENARIO_PATH = "scenario/";
    private final TIntIntHashMap m_scripts;
    private final TIntObjectHashMap<ScriptData> m_events;
    private static final ScriptManager m_instance;
    
    private ScriptManager() {
        super();
        this.m_scripts = new TIntIntHashMap();
        this.m_events = new TIntObjectHashMap<ScriptData>();
    }
    
    public static ScriptManager getInstance() {
        return ScriptManager.m_instance;
    }
    
    public String createScenarioFileName(final int scenarioId) {
        assert LuaManager.getInstance().getPath() != null;
        return String.format("%s%d%s", "scenario/", scenarioId, LuaManager.getInstance().getExtension());
    }
    
    @Nullable
    public LuaScript getScript(final int scenarioId, final boolean forceLoad) {
        LuaScript s = null;
        final int i = this.m_scripts.get(scenarioId);
        if (i > 0) {
            s = LuaManager.getInstance().getScript(i);
            if (s != null) {
                return s;
            }
        }
        if (forceLoad) {
            s = LuaManager.getInstance().loadScript(this.createScenarioFileName(scenarioId), null, false);
            if (s != null) {
                this.m_scripts.put(scenarioId, s.getId());
                return s;
            }
            ScriptManager.m_logger.trace((Object)("On a pas trouv\u00e9 le script du sc\u00e9nario " + scenarioId));
        }
        return s;
    }
    
    public void runEvent(final int scenarioId, final int eventId, final long[] params, final boolean isReward) {
        final LuaScript s = this.getScript(scenarioId, true);
        if (s == null) {
            ScriptManager.m_logger.error((Object)("runEvent : impossible de trouver un script d'id de sc\u00e9nario " + scenarioId));
            return;
        }
        final LuaValue[] typedParams = new LuaValue[params.length];
        for (int i = 0; i < typedParams.length; ++i) {
            typedParams[i] = new LuaValue(params[i]);
        }
        String signature = null;
        if (isReward) {
            signature = "reward_" + scenarioId + "_" + eventId;
        }
        else {
            signature = "event_" + scenarioId + "_" + eventId;
        }
        s.runFunction(signature.replaceAll("-", "_"), typedParams, new LuaTable[0]);
    }
    
    public void addEvent(final int eventId, final String scriptName, final String functionName) {
        this.m_events.put(eventId, new ScriptData(scriptName, functionName));
    }
    
    public void clear() {
        this.m_events.clear();
        this.m_scripts.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScriptManager.class);
        m_instance = new ScriptManager();
    }
    
    private static class ScriptData
    {
        private final String m_scriptName;
        private final String m_functionName;
        private final int m_paramCount;
        
        public ScriptData(final String scriptName, final String functionName, final int paramCount) {
            super();
            this.m_functionName = functionName;
            this.m_paramCount = paramCount;
            this.m_scriptName = scriptName;
        }
        
        public ScriptData(final String scriptName, final String functionName) {
            super();
            this.m_functionName = functionName;
            this.m_paramCount = 0;
            this.m_scriptName = scriptName;
        }
        
        public String getFunctionName() {
            return this.m_functionName;
        }
        
        public int getParamCount() {
            return this.m_paramCount;
        }
        
        public String getScriptName() {
            return this.m_scriptName;
        }
    }
}
