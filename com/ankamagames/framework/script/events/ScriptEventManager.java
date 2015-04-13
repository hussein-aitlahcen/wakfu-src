package com.ankamagames.framework.script.events;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.script.*;

public class ScriptEventManager
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final ScriptEventManager m_instance;
    private final HashMap<FilterableEvent, LUAFunctions> m_boundFunctions;
    
    private ScriptEventManager() {
        super();
        this.m_boundFunctions = new HashMap<FilterableEvent, LUAFunctions>();
    }
    
    public static ScriptEventManager getInstance() {
        return ScriptEventManager.m_instance;
    }
    
    public final void addLuaFunction(final FilterableEvent event, final LuaScript script, final String funcName, final LuaValue[] params, final boolean unloadOnRun) {
        LUAFunctions func = this.m_boundFunctions.get(event);
        if (func == null) {
            func = new LUAFunctions();
            this.m_boundFunctions.put(event, func);
        }
        func.addFunction(script, funcName, params, unloadOnRun);
    }
    
    public final LUAFunctions LUAFunctionsAlreadyExist(final FilterableEvent event) {
        return this.m_boundFunctions.get(event);
    }
    
    public final LUAFunctions removeLUAFunctions(final FilterableEvent event) {
        return this.m_boundFunctions.remove(event);
    }
    
    public void fireEvent(final FilterableEvent event) {
        final LUAFunctions luaFunctions = this.m_boundFunctions.get(event);
        if (luaFunctions != null && luaFunctions.run(event)) {
            this.m_boundFunctions.remove(event);
        }
    }
    
    public void removeScriptFromEvent(final FilterableEvent event, final LuaScript script) {
        final LUAFunctions luaFunctions = this.m_boundFunctions.get(event);
        if (luaFunctions != null && luaFunctions.removeScript(script)) {
            this.m_boundFunctions.remove(event);
        }
    }
    
    public void clean() {
        this.m_boundFunctions.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScriptEventManager.class);
        m_instance = new ScriptEventManager();
    }
}
