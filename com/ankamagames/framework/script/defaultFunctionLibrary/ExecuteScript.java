package com.ankamagames.framework.script.defaultFunctionLibrary;

import org.apache.log4j.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class ExecuteScript extends JavaFunctionEx
{
    private static final Logger m_logger;
    public static final String NAME = "executeScript";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    private static final String EXECUTION_TIME = "execution_Time";
    
    public ExecuteScript(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "executeScript";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ExecuteScript.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return ExecuteScript.RESULT;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int scriptFileId = this.getParamInt(0);
        if (scriptFileId == 0 || scriptFileId == -1) {
            this.addReturnValue(0);
            return;
        }
        final Map<String, Object> contextVariables = new HashMap<String, Object>();
        final LuaValue[] tableText = this.getParamTable(1);
        if (tableText.length % 2 != 0) {
            this.writeError(ExecuteScript.m_logger, "La liste des parametres du script n'est pas paire, il faut (nom, valeur) pour chaque parametre");
            this.addReturnValue(0L);
            return;
        }
        for (int i = 0; i < tableText.length; i += 2) {
            contextVariables.put((String)tableText[i].getValue(), tableText[i + 1].getValue());
        }
        contextVariables.put("fightId", this.getScriptObject().getFightId());
        final JavaFunctionsLibrary[] libraries = this.getScriptObject().getLibraries();
        try {
            final LuaValue[] executionTimeArray = { null };
            final LuaScript script = LuaManager.getInstance().runScript(scriptFileId, libraries, contextVariables, createListener(executionTimeArray), false);
            if (script == null) {
                this.writeError(ExecuteScript.m_logger, scriptFileId + " inconnu ");
                this.addReturnValue(0L);
                return;
            }
            LuaValue executionTime;
            if (executionTimeArray[0] != null) {
                executionTime = executionTimeArray[0];
            }
            else {
                executionTime = script.getValue("execution_Time");
            }
            if (executionTime == null || executionTime.getType() != LuaScriptParameterType.NUMBER) {
                this.addReturnValue(0L);
                return;
            }
            this.addReturnValue((long)executionTime.getValue());
        }
        catch (AssertionError e) {
            ExecuteScript.m_logger.error((Object)"ERREUR CRITIQUE DANS UN SCRIPT", (Throwable)e);
        }
    }
    
    private static LuaScriptEventListener createListener(final LuaValue[] executionTimeArray) {
        return new LuaScriptEventListener() {
            @Override
            public void onLuaScriptError(final LuaScript script, final LuaScriptErrorType errorType, final String message) {
            }
            
            @Override
            public void onLuaScriptFinished(final LuaScript script) {
            }
            
            @Override
            public void onLuaScriptLoaded(final LuaScript script) {
                executionTimeArray[0] = script.getValue("execution_Time");
            }
        };
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExecuteScript.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("ScriptName", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("Params", null, LuaScriptParameterType.TABLE, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("executionTime", null, LuaScriptParameterType.LONG, false) };
    }
}
