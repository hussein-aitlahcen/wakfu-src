package com.ankamagames.framework.script.action;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.*;
import com.ankamagames.framework.script.*;

public class ScriptedAction extends TimedAction implements LuaScriptEventListener
{
    public static final int NO_SCRIPT_ID = -1;
    public static final int NO_SCRIPT_FILE_ID = -1;
    private int m_scriptFileId;
    private JavaFunctionsLibrary[] m_libraries;
    private Map<String, Object> m_contextVariables;
    private int m_waitingEndScript;
    protected boolean m_fireActionFinishedOnScriptFinished;
    private final LightWeightMap<String, LuaValue> m_readValues;
    private static final String EXECUTION_TIME = "execution_Time";
    protected LuaScript m_script;
    private final boolean m_actionExectuted;
    
    public ScriptedAction(final int uniqueId, final int type, final int actionId) {
        super(uniqueId, type, actionId);
        this.m_scriptFileId = -1;
        this.m_waitingEndScript = -1;
        this.m_fireActionFinishedOnScriptFinished = true;
        this.m_readValues = new LightWeightMap<String, LuaValue>(1);
        this.m_actionExectuted = false;
    }
    
    public ScriptedAction() {
        super(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        this.m_scriptFileId = -1;
        this.m_waitingEndScript = -1;
        this.m_fireActionFinishedOnScriptFinished = true;
        this.m_readValues = new LightWeightMap<String, LuaValue>(1);
        this.m_actionExectuted = false;
    }
    
    public void addJavaFunctionsLibrary(final JavaFunctionsLibrary... libraries) {
        if (this.m_libraries == null) {
            this.m_libraries = libraries;
            return;
        }
        final int oldSize = this.m_libraries.length;
        final JavaFunctionsLibrary[] newLibraries = new JavaFunctionsLibrary[oldSize + libraries.length];
        System.arraycopy(this.m_libraries, 0, newLibraries, 0, oldSize);
        System.arraycopy(libraries, 0, newLibraries, oldSize, libraries.length);
        this.m_libraries = newLibraries;
    }
    
    public int getScriptFileId() {
        return this.m_scriptFileId;
    }
    
    public void setScriptFileId(final int scriptId) {
        this.m_scriptFileId = scriptId;
    }
    
    public void setContextVariables(final Map<String, Object> contextVariables) {
        this.m_contextVariables = contextVariables;
    }
    
    public long onRun() {
        if (this.m_actionExectuted) {
            ScriptedAction.m_logger.error((Object)("Interruption d'une boucle infinie dans une action de script actionId=" + this.getActionId() + " scriptId=" + this.getScriptFileId()));
            return 0L;
        }
        if (this.m_scriptFileId != 0 && this.m_scriptFileId != -1) {
            try {
                this.m_script = LuaManager.getInstance().runScript(this.m_scriptFileId, this.m_libraries, this.m_contextVariables, this, false);
                if (this.m_script != null) {
                    final LuaValue executionTime = this.m_readValues.get("execution_Time");
                    if (executionTime != null && executionTime.getType() == LuaScriptParameterType.NUMBER) {
                        this.m_fireActionFinishedOnScriptFinished = false;
                        return (long)executionTime.getValue();
                    }
                    this.m_waitingEndScript = this.m_script.getId();
                    this.m_fireActionFinishedOnScriptFinished = true;
                    return -1L;
                }
            }
            catch (AssertionError e) {
                ScriptedAction.m_logger.error((Object)"ERREUR CRITIQUE DANS UN SCRIPT", (Throwable)e);
            }
        }
        this.m_waitingEndScript = -1;
        this.fireActionFinishedEvent();
        return -1L;
    }
    
    @Override
    public void onLuaScriptFinished(final LuaScript script) {
        if (this.m_waitingEndScript != script.getId() && this.m_waitingEndScript != -1) {
            ScriptedAction.m_logger.error((Object)("on tente de finir une action de script(" + this.m_waitingEndScript + ") demand\u00e9 par la fin d'un autre script(" + script.getId() + ")"));
        }
        script.removeLuaScriptEventListener(this);
        final LuaValue executionTime = this.m_readValues.get("execution_Time");
        if (executionTime != null && executionTime.getType() == LuaScriptParameterType.NUMBER) {
            this.m_fireActionFinishedOnScriptFinished = false;
            return;
        }
        this.m_waitingEndScript = -1;
        if (this.m_fireActionFinishedOnScriptFinished) {
            this.fireActionFinishedEvent();
            this.m_fireActionFinishedOnScriptFinished = false;
        }
        this.m_script = null;
    }
    
    @Override
    public void onLuaScriptError(final LuaScript script, final LuaScriptErrorType errorType, final String message) {
        assert this.m_waitingEndScript == -1;
        script.removeLuaScriptEventListener(this);
        this.m_waitingEndScript = -1;
        this.fireActionFinishedEvent();
    }
    
    @Override
    public void onLuaScriptLoaded(final LuaScript script) {
        this.m_readValues.put("execution_Time", script.getValue("execution_Time"));
    }
    
    public int getWaitingEndScript() {
        return this.m_waitingEndScript;
    }
    
    @Override
    protected void onActionFinished() {
    }
}
