package com.ankamagames.framework.script;

import org.apache.log4j.*;
import java.util.concurrent.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.keplerproject.luajava.*;
import gnu.trove.*;
import com.ankamagames.framework.script.events.*;

public class LuaScript
{
    private static final Logger m_logger;
    private static final int INFINITE_LOOP = -1;
    private final int m_id;
    private State m_state;
    private int m_time;
    private String m_source;
    private JavaFunctionsLibrary[] m_libraries;
    private int m_taskIdGenerator;
    private final LuaState m_luaState;
    private boolean m_needToInterrupt;
    private final TIntObjectHashMap<WaitingTask> m_waitingTasks;
    private final ConcurrentLinkedQueue<TimedTask> m_timedTasks;
    private final ArrayList<LuaScriptEventListener> m_scriptEventListener;
    private boolean m_silentError;
    private Integer m_fightId;
    private ArrayList<Object> m_createdObjects;
    
    LuaScript(final int id, final LuaState luastate, final JavaFunctionsLibrary[] libraries) {
        super();
        this.m_taskIdGenerator = 0;
        this.m_needToInterrupt = false;
        this.m_waitingTasks = new TIntObjectHashMap<WaitingTask>();
        this.m_timedTasks = new ConcurrentLinkedQueue<TimedTask>();
        this.m_scriptEventListener = new ArrayList<LuaScriptEventListener>();
        this.m_silentError = false;
        assert luastate != null;
        this.m_id = id;
        this.m_luaState = luastate;
        this.m_state = State.NOT_LOADED;
        this.m_luaState.openBase();
        this.m_luaState.openMath();
        this.m_luaState.openTable();
        this.m_luaState.openOs();
        this.m_taskIdGenerator = 0;
        this.m_libraries = libraries;
    }
    
    void registerLibraries(final JavaFunctionsLibrary... libraries) {
        if (libraries != null) {
            for (final JavaFunctionsLibrary library : libraries) {
                try {
                    library.importLibs(this.m_luaState);
                }
                catch (Exception e) {
                    LuaScript.m_logger.error((Object)e);
                }
            }
        }
    }
    
    private void finish() {
        if (!this.m_luaState.isClosed()) {
            this.m_luaState.close();
        }
        this.m_state = State.DONE;
        for (int i = 0; i < this.m_scriptEventListener.size(); ++i) {
            this.m_scriptEventListener.get(i).onLuaScriptFinished(this);
        }
    }
    
    private void onError(final LuaState luaState, final LuaScriptErrorType errorType) {
        String msg = null;
        if (!luaState.isClosed() && luaState.getTop() >= 1 && luaState.isString(-1)) {
            msg = luaState.toString(-1);
            luaState.pop(1);
        }
        if (msg == null) {
            msg = "";
        }
        for (int i = 0; i < this.m_scriptEventListener.size(); ++i) {
            this.m_scriptEventListener.get(i).onLuaScriptError(this, errorType, msg);
        }
        this.interrupt();
    }
    
    public final void tryToFinish() {
        if (this.m_timedTasks.isEmpty() && this.m_waitingTasks.isEmpty()) {
            this.finish();
        }
    }
    
    public final void tryToMakeIdle() {
        if (this.m_timedTasks.isEmpty() && this.m_waitingTasks.isEmpty()) {
            this.m_state = State.IDLE;
        }
    }
    
    final boolean isSilentError() {
        return this.m_silentError;
    }
    
    final void setSilentError(final boolean silentError) {
        this.m_silentError = silentError;
    }
    
    public final int getId() {
        return this.m_id;
    }
    
    public final int getTime() {
        return this.m_time;
    }
    
    public final void interrupt() {
        this.m_needToInterrupt = true;
    }
    
    public final TimedTask registerTimedTask(final int time, final int loopCount, final String funcName, final LuaValue[] args) {
        if (this.m_state == State.IDLE) {
            this.m_state = State.READING_FUNCTIONS;
        }
        final TimedTask timedTask = new TimedTask(time, loopCount, funcName, args);
        this.m_timedTasks.add(timedTask);
        return timedTask;
    }
    
    public final int registerWaitingTask(final String funcName, final LuaValue[] args) {
        if (this.m_state == State.IDLE) {
            this.m_state = State.READING_FUNCTIONS;
        }
        final WaitingTask task = new WaitingTask(funcName, args);
        this.m_waitingTasks.put(task.getId(), task);
        return task.getId();
    }
    
    public final boolean unregisterTask(final Task toRemove) {
        if (toRemove instanceof WaitingTask) {
            this.m_waitingTasks.remove(toRemove.getId());
            return true;
        }
        return toRemove instanceof TimedTask && this.m_timedTasks.remove(toRemove);
    }
    
    public final boolean unregisterTask(final int taskId) {
        if (this.m_waitingTasks.remove(taskId) != null) {
            return true;
        }
        final Iterator<TimedTask> iter = this.m_timedTasks.iterator();
        while (iter.hasNext()) {
            if (iter.next().getId() == taskId) {
                iter.remove();
                return true;
            }
        }
        return false;
    }
    
    final State getState() {
        return this.m_state;
    }
    
    final void loadCommand(final String command) {
        if (command != null && this.m_luaState.LloadString(command) == 0) {
            this.m_state = State.LOADED;
        }
        else {
            this.onError(this.m_luaState, LuaScriptErrorType.SYNTAX_ERROR);
        }
    }
    
    public void loadBinary(final String name, final byte[] data) {
        this.m_source = name;
        if (name != null && this.m_luaState.LloadBuffer(data, name) == 0) {
            this.m_state = State.LOADED;
        }
        else {
            this.onError(this.m_luaState, LuaScriptErrorType.SYNTAX_ERROR);
        }
    }
    
    final void loadFile(final String fileName, final boolean silentError) throws Exception {
        this.m_source = fileName;
        if (fileName != null && this.m_luaState.LloadFile(fileName) == 0) {
            this.m_state = State.LOADED;
        }
        else {
            this.onError(this.m_luaState, LuaScriptErrorType.SYNTAX_ERROR);
        }
    }
    
    final void start(final Map<String, Object> contextVariables) {
        if (this.m_state == State.LOADED) {
            if (contextVariables != null) {
                for (final Map.Entry<String, Object> var : contextVariables.entrySet()) {
                    try {
                        this.m_luaState.pushObjectValue(var.getValue());
                        this.m_luaState.setGlobal((String)var.getKey());
                    }
                    catch (LuaException e) {
                        LuaScript.m_logger.error((Object)"Impossible de d?finir une variable de contexte pour un script", (Throwable)e);
                    }
                }
            }
            final int status = this.m_luaState.resume(0);
            this.m_state = State.RUNNING;
            for (final LuaScriptEventListener listener : this.m_scriptEventListener) {
                listener.onLuaScriptLoaded(this);
            }
            if (status != 0) {
                this.onError(this.m_luaState, LuaScriptErrorType.RUNTIME_ERROR);
            }
            else {
                this.tryToFinish();
            }
        }
        else if (!this.m_needToInterrupt) {
            this.m_luaState.pushString("No file loaded");
            this.onError(this.m_luaState, LuaScriptErrorType.NOT_LOADED_ERROR);
        }
    }
    
    public void executeWaitingTask(final int taskId) {
        final WaitingTask waitingTask = this.m_waitingTasks.remove(taskId);
        if (waitingTask == null) {
            return;
        }
        waitingTask.execute();
    }
    
    public void cancelWaitingTask(final int taskId) {
        this.m_waitingTasks.remove(taskId);
    }
    
    public void cancelWaitingTasks() {
        this.m_waitingTasks.clear();
    }
    
    final void update(final int deltaTime) {
        if (this.m_needToInterrupt) {
            switch (this.m_state) {
                case IDLE: {
                    break;
                }
                case READING_FUNCTIONS: {
                    this.m_state = State.IDLE;
                    break;
                }
                default: {
                    this.forceClose();
                    break;
                }
            }
        }
        else {
            this.m_time += deltaTime;
            switch (this.m_state) {
                case READING_FUNCTIONS: {
                    final Iterator<TimedTask> iter = this.m_timedTasks.iterator();
                    while (iter.hasNext()) {
                        if (iter.next().update()) {
                            iter.remove();
                        }
                    }
                    this.tryToMakeIdle();
                    break;
                }
                case RUNNING: {
                    final Iterator<TimedTask> iter = this.m_timedTasks.iterator();
                    while (iter.hasNext()) {
                        if (iter.next().update()) {
                            iter.remove();
                        }
                    }
                    this.tryToFinish();
                    break;
                }
            }
        }
    }
    
    public String getSource() {
        return this.m_source;
    }
    
    public void setSource(final String source) {
        this.m_source = source;
    }
    
    public LuaState getLuaState() {
        return this.m_luaState;
    }
    
    public LuaValue getValue(final String varName) {
        if (this.m_luaState.isClosed()) {
            LuaScript.m_logger.error((Object)("Tente de r?cup?rer une variable (" + varName + ") alors que le script est ferm?"));
            return null;
        }
        this.m_luaState.getGlobal(varName);
        LuaValue value = null;
        try {
            value = LuaValue.createFrom(this.m_luaState, -1);
        }
        catch (LuaException e) {
            LuaScript.m_logger.error((Object)("Variable " + varName + " inconnue?"), (Throwable)e);
        }
        this.m_luaState.pop(1);
        return value;
    }
    
    private boolean isFunction() {
        if (!this.m_luaState.isFunction(-1) && !this.m_luaState.isJavaFunction(-1)) {
            this.m_luaState.Lwhere(1);
            this.m_luaState.pop(1);
            return false;
        }
        return true;
    }
    
    public final LuaValue[] runFunction(final String funcName, final LuaValue[] params, final LuaTable... obj) {
        if (this.m_state == State.NOT_LOADED || this.m_state == State.DONE) {
            LuaScript.m_logger.error((Object)("Le script devrait ?tre charg? avant d'appeler une fonction. (loadFile) " + this.m_state));
            return null;
        }
        if (this.m_state == State.LOADED) {
            this.m_luaState.resume(0);
            this.m_state = State.READING_FUNCTIONS;
        }
        if (obj != null) {
            for (int i = 0; i < obj.length; ++i) {
                if (obj[i] != null) {
                    obj[i].pushIn(this.m_luaState);
                }
            }
        }
        if (funcName.contains(".")) {
            final String[] func = StringUtils.split(funcName, "\\.");
            this.m_luaState.pushString(func[0]);
            this.m_luaState.getTable((int)LuaState.LUA_GLOBALSINDEX);
            if (!this.m_luaState.isTable(-1)) {
                LuaScript.m_logger.error((Object)(func[0] + " n'est pas une librairie connue"));
                this.m_luaState.remove(-1);
                switch (this.m_state) {
                    case RUNNING: {
                        this.tryToFinish();
                        break;
                    }
                    case READING_FUNCTIONS: {
                        this.tryToMakeIdle();
                        break;
                    }
                }
                return null;
            }
            this.m_luaState.pushString(func[1]);
            this.m_luaState.getTable(-2);
            this.m_luaState.remove(-2);
        }
        else {
            this.m_luaState.getGlobal(funcName);
        }
        if (!this.isFunction()) {
            LuaScript.m_logger.error((Object)("Fonction inconnue " + funcName + " dans le script " + this.m_source + " ligne " + this.getCurrentLine()), (Throwable)new Exception());
            return null;
        }
        int paramCount = 0;
        if (params != null) {
            paramCount = params.length;
            for (int j = 0; j < paramCount; ++j) {
                if (params[j] == null) {
                    this.m_luaState.pushNil();
                }
                else {
                    params[j].pushIn(this.m_luaState);
                }
            }
        }
        if (this.m_luaState.pcall(paramCount, (int)LuaState.LUA_MULTRET, 0) != 0) {
            this.onError(this.m_luaState, LuaScriptErrorType.RUNTIME_ERROR);
        }
        final int resultLength = this.m_luaState.getTop();
        final LuaValue[] returnValues = new LuaValue[resultLength];
        for (int k = 0; k < resultLength; ++k) {
            try {
                returnValues[k] = LuaValue.createFrom(this.m_luaState, -1);
            }
            catch (LuaException e) {
                LuaScript.m_logger.error((Object)("Error retrieving a function(" + funcName + ") result : " + e));
            }
            this.m_luaState.pop(1);
        }
        return returnValues;
    }
    
    public String getCurrentLine() {
        this.m_luaState.Lwhere(1);
        String line = this.m_luaState.toString(-1);
        this.m_luaState.pop(1);
        if (line != null && line.length() > 0) {
            final String[] s = StringUtils.split(line, ':');
            if (s.length > 1) {
                line = s[1];
            }
        }
        return line;
    }
    
    public void runFunction(final String funcName) {
        this.runFunction(funcName, null, new LuaTable[0]);
    }
    
    public boolean addLuaScriptEventListener(final LuaScriptEventListener eventListener) {
        return this.m_scriptEventListener.add(eventListener);
    }
    
    public final void clearLuaScriptEventListener() {
        this.m_scriptEventListener.clear();
    }
    
    public final boolean containsLuaScriptEventListener(final LuaScriptEventListener eventListener) {
        return this.m_scriptEventListener.contains(eventListener);
    }
    
    public final boolean removeLuaScriptEventListener(final LuaScriptEventListener eventListener) {
        return this.m_scriptEventListener.remove(eventListener);
    }
    
    public final void forceClose() {
        this.m_timedTasks.clear();
        this.m_waitingTasks.clear();
        this.tryToFinish();
    }
    
    public int getFightId() {
        if (this.m_fightId == null) {
            this.m_fightId = -1;
            final LuaObject fightIdObject = this.m_luaState.getLuaObject("fightId");
            if (fightIdObject != null) {
                if (fightIdObject.isNumber()) {
                    this.m_fightId = (int)fightIdObject.getNumber();
                }
                else if (fightIdObject.isJavaObject()) {
                    try {
                        this.m_fightId = Integer.parseInt(fightIdObject.toString());
                    }
                    catch (NumberFormatException e) {
                        LuaScript.m_logger.error((Object)("Impossible de recuperer un id de combat sur un objet non transformable en entier : " + fightIdObject));
                    }
                }
            }
        }
        return this.m_fightId;
    }
    
    public JavaFunctionsLibrary[] getLibraries() {
        return this.m_libraries;
    }
    
    public void addCreatedObject(final Object object) {
        if (this.m_createdObjects == null) {
            this.m_createdObjects = new ArrayList<Object>();
        }
        this.m_createdObjects.add(object);
    }
    
    public void foreachCreatedObject(final TObjectProcedure<Object> procedure) {
        if (this.m_createdObjects == null) {
            return;
        }
        for (int i = 0, size = this.m_createdObjects.size(); i < size; ++i) {
            if (!procedure.execute(this.m_createdObjects.get(i))) {
                return;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LuaScript.class);
    }
    
    enum State
    {
        NOT_LOADED, 
        LOADED, 
        READING_FUNCTIONS, 
        IDLE, 
        RUNNING, 
        DONE;
    }
    
    final class WaitingTask implements Task
    {
        public final int m_taskId;
        private final String m_funcName;
        private final LuaValue[] m_args;
        private ArrayList<FilterableEvent> m_listenedEvents;
        
        WaitingTask(final String funcName, final LuaValue[] args) {
            super();
            this.m_funcName = funcName;
            this.m_args = args;
            this.m_taskId = ++LuaScript.this.m_taskIdGenerator;
        }
        
        @Override
        public int getId() {
            return this.m_taskId;
        }
        
        @Override
        public void execute() {
            if (LuaScript.this.m_luaState.isClosed()) {
                LuaScript.m_logger.error((Object)("Tentative d'execution d'une WaitingTask sur un script ferm? id=" + LuaScript.this.m_id));
                return;
            }
            if (this.m_funcName.contains(".")) {
                final String[] func = StringUtils.split(this.m_funcName, '.');
                LuaScript.this.m_luaState.pushString(func[0]);
                LuaScript.this.m_luaState.getTable((int)LuaState.LUA_GLOBALSINDEX);
                if (!LuaScript.this.m_luaState.isTable(-1)) {
                    LuaScript.m_logger.error((Object)(func[0] + " n'est pas une librairie connue"));
                    LuaScript.this.m_luaState.remove(-1);
                    switch (LuaScript.this.m_state) {
                        case RUNNING: {
                            LuaScript.this.tryToFinish();
                            break;
                        }
                        case READING_FUNCTIONS: {
                            LuaScript.this.tryToMakeIdle();
                            break;
                        }
                    }
                    return;
                }
                LuaScript.this.m_luaState.pushString(func[1]);
                LuaScript.this.m_luaState.getTable(-2);
                LuaScript.this.m_luaState.remove(-2);
            }
            else {
                LuaScript.this.m_luaState.pushString(this.m_funcName);
                LuaScript.this.m_luaState.getTable((int)LuaState.LUA_GLOBALSINDEX);
            }
            if (LuaScript.this.isFunction()) {
                final int paramCount = (this.m_args == null) ? 0 : this.m_args.length;
                for (int i = 0; i < paramCount; ++i) {
                    this.m_args[i].pushIn(LuaScript.this.m_luaState);
                }
                if (LuaScript.this.m_luaState.pcall(paramCount, (int)LuaState.LUA_MULTRET, 0) != 0) {
                    LuaScript.this.onError(LuaScript.this.m_luaState, LuaScriptErrorType.RUNTIME_ERROR);
                }
            }
            else {
                LuaScript.m_logger.error((Object)("Fonction inconnue " + this.m_funcName + " dans le script " + LuaScript.this.m_source));
            }
            LuaScript.this.m_luaState.pop(LuaScript.this.m_luaState.getTop());
            switch (LuaScript.this.m_state) {
                case RUNNING: {
                    LuaScript.this.tryToFinish();
                    break;
                }
                case READING_FUNCTIONS: {
                    LuaScript.this.tryToMakeIdle();
                    break;
                }
            }
        }
    }
    
    public final class TimedTask implements Task
    {
        public final int m_taskId;
        private final int m_tickTime;
        private int m_waitTime;
        private int m_loopCount;
        private final String m_funcName;
        private final LuaValue[] m_args;
        private ArrayList<FilterableEvent> m_listenedEvents;
        
        TimedTask(final int time, final int loopCount, final String funcName, final LuaValue[] args) {
            super();
            this.m_tickTime = time;
            this.m_waitTime = LuaScript.this.m_time + time;
            this.m_loopCount = loopCount;
            this.m_funcName = funcName;
            this.m_args = args;
            this.m_taskId = ++LuaScript.this.m_taskIdGenerator;
        }
        
        @Override
        public int getId() {
            return this.m_taskId;
        }
        
        @Override
        public void execute() {
            if (this.m_funcName.contains(".")) {
                final String[] func = StringUtils.split(this.m_funcName, '.');
                LuaScript.this.m_luaState.pushString(func[0]);
                LuaScript.this.m_luaState.getTable((int)LuaState.LUA_GLOBALSINDEX);
                if (!LuaScript.this.m_luaState.isTable(-1)) {
                    LuaScript.m_logger.error((Object)(func[0] + " n'est pas une librairie connue"));
                    LuaScript.this.m_luaState.remove(-1);
                    switch (LuaScript.this.m_state) {
                        case RUNNING: {
                            LuaScript.this.tryToFinish();
                            break;
                        }
                        case READING_FUNCTIONS: {
                            LuaScript.this.tryToMakeIdle();
                            break;
                        }
                    }
                    return;
                }
                LuaScript.this.m_luaState.pushString(func[1]);
                LuaScript.this.m_luaState.getTable(-2);
                LuaScript.this.m_luaState.remove(-2);
            }
            else {
                LuaScript.this.m_luaState.pushString(this.m_funcName);
                LuaScript.this.m_luaState.getTable((int)LuaState.LUA_GLOBALSINDEX);
            }
            if (LuaScript.this.isFunction()) {
                final int paramCount = (this.m_args == null) ? 0 : this.m_args.length;
                for (int i = 0; i < paramCount; ++i) {
                    this.m_args[i].pushIn(LuaScript.this.m_luaState);
                }
                if (LuaScript.this.m_luaState.pcall(paramCount, (int)LuaState.LUA_MULTRET, 0) != 0) {
                    LuaScript.this.onError(LuaScript.this.m_luaState, LuaScriptErrorType.RUNTIME_ERROR);
                }
            }
            else {
                LuaScript.m_logger.error((Object)("Fonction inconnue " + this.m_funcName + " dans le script " + LuaScript.this.m_source));
            }
            LuaScript.this.m_luaState.pop(LuaScript.this.m_luaState.getTop());
            switch (LuaScript.this.m_state) {
                case RUNNING: {
                    LuaScript.this.tryToFinish();
                    break;
                }
                case READING_FUNCTIONS: {
                    LuaScript.this.tryToMakeIdle();
                    break;
                }
            }
        }
        
        boolean update() {
            if (LuaScript.this.m_time >= this.m_waitTime) {
                this.execute();
                if (this.m_loopCount == -1) {
                    this.m_waitTime += this.m_tickTime;
                }
                else {
                    --this.m_loopCount;
                    if (this.m_loopCount <= 0) {
                        return true;
                    }
                    this.m_waitTime += this.m_tickTime;
                }
            }
            return false;
        }
    }
    
    public interface Task
    {
        void execute();
        
        int getId();
    }
}
