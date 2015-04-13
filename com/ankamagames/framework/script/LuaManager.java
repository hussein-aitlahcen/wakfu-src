package com.ankamagames.framework.script;

import com.ankamagames.framework.kernel.core.resource.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import org.jetbrains.annotations.*;
import java.util.*;
import gnu.trove.*;
import org.keplerproject.luajava.*;
import java.io.*;

public class LuaManager extends FileHashCache<byte[]> implements Runnable, LuaScriptEventListener
{
    private static final Logger SCRIPT_LOGGER;
    public static final String SCRIPT_VAR_NAME = "script";
    public static final String SCRIPT_FILE_EXTENTION = ".lua";
    private static final int CACHE_MAX_SIZE = 2097152;
    private static final int CLOCK_DELAY = 30;
    public static final FilenameFilter FILENAME_FILTER;
    private static final LuaManager m_instance;
    private final TIntObjectHashMap<LuaScript> m_scripts;
    private final List<LuaScriptEventListener> m_listeners;
    private long m_lastClockTime;
    private final TObjectProcedure<LuaScript> m_forceCloseProcedure;
    private final UpdateScript m_updateScriptProcedure;
    
    private LuaManager() {
        super(0L, false);
        this.m_scripts = new TIntObjectHashMap<LuaScript>();
        this.m_listeners = new ArrayList<LuaScriptEventListener>();
        this.m_forceCloseProcedure = new ForceCloseProcedure();
        this.m_updateScriptProcedure = new UpdateScript();
        ProcessScheduler.getInstance().schedule(this, 30L);
    }
    
    public void useCompiledScript() {
        this.setUseCache(2097152L);
    }
    
    public static LuaManager getInstance() {
        return LuaManager.m_instance;
    }
    
    @Override
    protected final FileHashCache.Reference createReference(final byte[] data) throws Exception {
        return new Reference(data);
    }
    
    @Override
    public final String getExtension() {
        return ".lua";
    }
    
    @Override
    public final FilenameFilter getFilenameFilter() {
        return LuaManager.FILENAME_FILTER;
    }
    
    public final void addEventListener(final LuaScriptEventListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public final void removeEventListener(final LuaScriptEventListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public final LuaScript getScript(final int id) {
        synchronized (this.m_scripts) {
            return this.m_scripts.get(id);
        }
    }
    
    String createFilename(final int scriptId) {
        assert this.getPath() != null;
        return String.format("%d%s", scriptId, ".lua");
    }
    
    public final LuaScript loadScript(final int fileId, final JavaFunctionsLibrary[] libraries, final boolean silentError) {
        assert this.getPath() != null;
        return this.loadScript(this.createFilename(fileId), libraries, silentError);
    }
    
    @Nullable
    public LuaScript loadScript(final String fileName, final JavaFunctionsLibrary[] libraries, final boolean silentError) {
        final String fileAbsolutePath = this.getPath() + fileName;
        final LuaScript s = this.createScript(libraries);
        if (!this.isUseCache()) {
            JavaFunctionLoader.INSTANCE.findUsage(fileName, fileAbsolutePath);
        }
        s.registerLibraries((JavaFunctionsLibrary[])JavaFunctionLoader.INSTANCE.getLibs(fileName));
        s.registerLibraries(libraries);
        try {
            s.loadBinary(fileName, this.getData(fileName));
            s.setSource(fileName);
            s.setSilentError(silentError);
        }
        catch (Exception e) {
            LuaManager.m_logger.error((Object)("Impossible de charger le fichier " + fileName), (Throwable)e);
            return null;
        }
        if (s.getState() != LuaScript.State.LOADED) {
            return null;
        }
        final int id = s.getId();
        synchronized (this.m_scripts) {
            this.m_scripts.put(id, s);
        }
        return s;
    }
    
    public final LuaScript runCommand(final String command, @Nullable final JavaFunctionsLibrary[] libraries, @Nullable final Map<String, Object> variables, final boolean silentError) {
        final LuaScript s = this.loadCommand(command, libraries, silentError);
        if (s != null) {
            s.setSource("Command : " + command);
            s.start(variables);
        }
        else {
            LuaManager.m_logger.error((Object)("Le Script de commande [" + command + "] n'existe pas"));
        }
        return s;
    }
    
    private LuaScript loadCommand(final String command, final JavaFunctionsLibrary[] libraries, final boolean silentError) {
        final LuaScript s = this.createScript(libraries);
        s.registerLibraries((JavaFunctionsLibrary[])JavaFunctionLoader.INSTANCE.findUsage(command));
        s.loadCommand(command);
        s.setSilentError(silentError);
        if (s.getState() != LuaScript.State.LOADED) {
            return null;
        }
        final int id = s.getId();
        synchronized (this.m_scripts) {
            this.m_scripts.put(id, s);
        }
        return s;
    }
    
    public final LuaScript runScript(final String fileName, final LuaScriptEventListener eventListener) {
        return this.runScript(fileName, null, null, eventListener, false);
    }
    
    public final LuaScript runScript(final String fileName) {
        return this.runScript(fileName, null, null);
    }
    
    public final LuaScript runScript(final String fileName, @Nullable final LuaScriptEventListener eventListener, final boolean silentError) {
        return this.runScript(fileName, null, null, eventListener, silentError);
    }
    
    public final LuaScript runScript(final String fileName, final boolean silentError) {
        return this.runScript(fileName, null, silentError);
    }
    
    public final LuaScript runScript(final int fileId, final LuaScriptEventListener eventListener) {
        return this.runScript(fileId, null, null, eventListener, false);
    }
    
    public final LuaScript runScript(final int fileId) {
        return this.runScript(fileId, null, null);
    }
    
    public final LuaScript runScript(final int fileId, @Nullable final LuaScriptEventListener eventListener, final boolean silentError) {
        return this.runScript(fileId, null, null, eventListener, silentError);
    }
    
    public final LuaScript runScript(final int fileId, final boolean silentError) {
        return this.runScript(fileId, null, silentError);
    }
    
    public final LuaScript runScript(final int fileId, @Nullable final JavaFunctionsLibrary[] libraries, @Nullable final LuaScriptEventListener eventListener) {
        return this.runScript(fileId, libraries, null, eventListener, false);
    }
    
    public final LuaScript runScript(final int fileId, @Nullable final JavaFunctionsLibrary[] libraries, @Nullable final Map<String, Object> variables, @Nullable final LuaScriptEventListener eventListener, final boolean silentError) {
        assert this.getPath() != null;
        return this.runScript(this.createFilename(fileId), libraries, variables, eventListener, silentError);
    }
    
    public final LuaScript runScript(final int fileId, final JavaFunctionsLibrary[] libraries, final Map<String, Object> variables, final boolean silentError) {
        return this.runScript(fileId, libraries, variables, null, silentError);
    }
    
    public final LuaScript runScript(final String fileName, @Nullable final JavaFunctionsLibrary[] libraries, @Nullable final LuaScriptEventListener eventListener) {
        return this.runScript(fileName, libraries, null, eventListener, false);
    }
    
    public final LuaScript runScript(final String fileName, final JavaFunctionsLibrary... libraries) {
        return this.runScript(fileName, libraries, null);
    }
    
    public final LuaScript runScript(final String fileName, @Nullable final JavaFunctionsLibrary[] libraries, @Nullable final Map<String, Object> variables, final LuaScriptEventListener eventListener, final boolean silentError) {
        final LuaScript s = this.loadScript(fileName, libraries, silentError);
        if (s != null) {
            if (eventListener != null) {
                s.addLuaScriptEventListener(eventListener);
            }
            s.start(variables);
        }
        else {
            LuaManager.m_logger.error((Object)("Le Script de [" + fileName + "] n'existe pas"));
        }
        return s;
    }
    
    public final void interruptScript(final int id) {
        final LuaScript s = this.getScript(id);
        if (s != null) {
            s.interrupt();
        }
    }
    
    public final int getNextFreeId() {
        int i = 1;
        synchronized (this.m_scripts) {
            while (this.m_scripts.containsKey(i)) {
                ++i;
            }
        }
        return i;
    }
    
    public void update(final int deltaTime) {
        synchronized (this.m_scripts) {
            this.m_updateScriptProcedure.setDeltaTime(deltaTime);
            this.m_scripts.forEachEntry(this.m_updateScriptProcedure);
        }
    }
    
    void removeScript(final int id) {
        this.m_scripts.remove(id);
    }
    
    private LuaScript createScript(final JavaFunctionsLibrary... libraries) {
        final LuaState luaState = LuaStateFactory.newLuaState();
        final int id = this.getNextFreeId();
        final LuaScript script = new LuaScript(id, luaState, libraries);
        script.registerLibraries(libraries);
        script.addLuaScriptEventListener(this);
        luaState.pushJavaObject((Object)script);
        luaState.setGlobal("script");
        return script;
    }
    
    public final void interrupt() {
        ProcessScheduler.getInstance().remove(this);
    }
    
    @Override
    public final void run() {
        final long clockTime = System.currentTimeMillis();
        final int deltaTime = (int)(clockTime - this.m_lastClockTime);
        this.update(deltaTime);
        this.m_lastClockTime = clockTime;
    }
    
    @Override
    public void onLuaScriptError(final LuaScript script, final LuaScriptErrorType errorType, final String message) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            assert this.m_listeners.get(i) != null;
            this.m_listeners.get(i).onLuaScriptError(script, errorType, message);
        }
        if (!script.isSilentError()) {
            LuaManager.SCRIPT_LOGGER.error((Object)("[LUA] " + errorType + " Fichier " + script.getSource() + ' ' + message));
        }
        LuaManager.m_logger.error((Object)("Erreur dans un script (" + script.getSource() + " ) : erreur " + errorType + ' ' + message), (Throwable)new Exception("callStack"));
    }
    
    @Override
    public void onLuaScriptFinished(final LuaScript script) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onLuaScriptFinished(script);
        }
    }
    
    @Override
    public void onLuaScriptLoaded(final LuaScript script) {
    }
    
    public void closeAllScripts() {
        synchronized (this.m_scripts) {
            this.m_scripts.forEachValue(this.m_forceCloseProcedure);
            this.m_scripts.clear();
        }
    }
    
    static {
        SCRIPT_LOGGER = Logger.getLogger("LUA");
        FILENAME_FILTER = new LuaFileFilter();
        m_instance = new LuaManager();
    }
    
    private static class LuaFileFilter implements FilenameFilter
    {
        @Override
        public boolean accept(final File file, final String name) {
            return name.endsWith(".lua");
        }
    }
    
    private static class ForceCloseProcedure implements TObjectProcedure<LuaScript>
    {
        @Override
        public boolean execute(final LuaScript object) {
            object.forceClose();
            return true;
        }
    }
    
    private class Reference extends FileHashCache.Reference
    {
        Reference(final byte[] data) {
            super((FileHashCache<byte[]>)LuaManager.this, data);
        }
        
        @Override
        public long estimatedSize() {
            try {
                return ((byte[])(Object)this.m_data).length;
            }
            catch (NullPointerException ignored) {
                return 0L;
            }
        }
    }
    
    private class UpdateScript implements TIntObjectProcedure<LuaScript>
    {
        private int m_deltaTime;
        
        @Override
        public boolean execute(final int id, final LuaScript script) {
            script.update(this.m_deltaTime);
            if (script.getState() == LuaScript.State.DONE) {
                LuaManager.this.removeScript(id);
            }
            return true;
        }
        
        public void setDeltaTime(final int deltaTime) {
            this.m_deltaTime = deltaTime;
        }
        
        @Override
        public String toString() {
            return "updateScript{m_deltaTime=" + this.m_deltaTime + '}';
        }
    }
}
