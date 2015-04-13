package com.ankamagames.framework.script;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import org.keplerproject.luajava.*;

public abstract class JavaFunctionEx extends JavaFunction
{
    private static final Logger m_logger;
    private static final int INCORRECT_PARAM_COUNT = -1;
    protected int m_returnValueCount;
    private final LuaScriptParameterDescriptor[] m_paramDescriptors;
    private final LuaScriptParameterDescriptor[] m_returnDescriptors;
    
    public JavaFunctionEx(final LuaState luaState) {
        super(luaState);
        this.m_returnValueCount = 0;
        this.m_paramDescriptors = this.getParameterDescriptors();
        this.m_returnDescriptors = this.getResultDescriptors();
    }
    
    public abstract String getName();
    
    public String getDescription() {
        return "NO Description... Please Dev, implement me!";
    }
    
    @Nullable
    public abstract LuaScriptParameterDescriptor[] getParameterDescriptors();
    
    @Nullable
    public abstract LuaScriptParameterDescriptor[] getResultDescriptors();
    
    public final void register() throws LuaException {
        assert this.getName() != null;
        super.register(this.getName());
    }
    
    public final int execute() throws LuaException {
        this.m_returnValueCount = 0;
        final int paramCount = this.checkParam();
        Label_0069: {
            if (paramCount >= 0) {
                try {
                    this.run(paramCount);
                    break Label_0069;
                }
                catch (LuaException ex) {
                    throw this.createLuaException(ex.toString());
                }
            }
            this.writeError(JavaFunctionEx.m_logger, "Fonction " + this.getClass().getSimpleName() + " non ?x?cut?e");
        }
        final int returnCount = (this.m_returnDescriptors == null) ? 0 : this.m_returnDescriptors.length;
        if (returnCount != this.m_returnValueCount) {
            final boolean multireturn = this.resultDescrEndsWithBLoops();
            if (this.m_returnValueCount < returnCount && !multireturn) {
                this.writeError(JavaFunctionEx.m_logger, " nombre de valeur de retour incorrect (attendu " + (multireturn ? ">=" : "") + returnCount + " lu: " + this.m_returnValueCount + " )");
            }
        }
        return this.m_returnValueCount;
    }
    
    private boolean resultDescrEndsWithBLoops() {
        return this.m_returnDescriptors[this.m_returnDescriptors.length - 1].getType() == LuaScriptParameterType.BLOOPS;
    }
    
    protected final String getLineNumber() {
        try {
            return this.getScriptObject().getCurrentLine();
        }
        catch (Exception e) {
            JavaFunctionEx.m_logger.error((Object)"Exception levee", (Throwable)e);
            return "-1";
        }
    }
    
    private LuaException createLuaException(final String msg) {
        return new LuaException(this.getStringError(msg));
    }
    
    private String getStringError(final String msg) {
        String fileName = "<inconnu>";
        try {
            fileName = this.getScriptObject().getSource();
        }
        catch (Exception ex) {
            JavaFunctionEx.m_logger.warn((Object)"pas de script associ? a cette fonction");
        }
        return "fichier=" + fileName + " ligne=" + this.getLineNumber() + " " + msg;
    }
    
    protected final void writeError(final Logger logger, final String msg) {
        logger.error((Object)this.getStringError(msg));
    }
    
    protected abstract void run(final int p0) throws LuaException;
    
    private int checkParam() {
        final LuaScriptParameterDescriptor[] paramDescr = this.m_paramDescriptors;
        if (paramDescr == null) {
            return this.checkParamCount(0, 0);
        }
        int minCount = 0;
        int maxCount = 0;
        for (int i = 0; i < paramDescr.length; ++i) {
            ++maxCount;
            if (!paramDescr[i].isOptional()) {
                ++minCount;
            }
            if (paramDescr[i].getType() == LuaScriptParameterType.BLOOPS) {
                maxCount = Integer.MAX_VALUE;
            }
        }
        if (maxCount < minCount) {
            maxCount = minCount;
        }
        return this.checkParamCount(minCount, maxCount);
    }
    
    private int checkParamCount(final int paramCountMin, final int paramCountMax) {
        assert paramCountMin <= paramCountMax;
        final int count = this.L.getTop() - 1;
        if (count >= paramCountMin && count <= paramCountMax) {
            for (int i = 0; i < paramCountMin; ++i) {
                if (!this.checkDefinitionType(i)) {
                    return -i - 1;
                }
            }
            return count;
        }
        String msg;
        if (paramCountMin == paramCountMax) {
            msg = String.format("(attendu: %d, lu: %d)", paramCountMin, count);
        }
        else if (paramCountMax == Integer.MAX_VALUE) {
            msg = String.format("(attendu au moins: %d, lu: %d)", paramCountMin, count);
        }
        else {
            msg = String.format("(attendu: %d-%d, lu: %d)", paramCountMin, paramCountMax, count);
        }
        this.writeError(JavaFunctionEx.m_logger, "nombre de param?tre incorrect " + msg);
        return -1;
    }
    
    private boolean checkDefinitionType(final int i) {
        assert i >= 0;
        final LuaScriptParameterDescriptor[] paramDescr = this.m_paramDescriptors;
        if (paramDescr == null) {
            this.writeError(JavaFunctionEx.m_logger, "La fonction n'attend pas de param?tre");
            return false;
        }
        if (i < paramDescr.length) {
            final LuaScriptParameterType definitionType = paramDescr[i].getType();
            if (this.L.isNil(i + 2)) {
                this.writeError(JavaFunctionEx.m_logger, " param?tre " + i + " est null");
                return false;
            }
            if (!definitionType.checkParam(this.L, i + 2)) {
                final String msg = String.format("mauvais type d'argument #%d: (definition: %s, fonction:%s)", i, definitionType, this.L.typeName(this.L.type(i + 2)));
                this.writeError(JavaFunctionEx.m_logger, msg);
                return false;
            }
        }
        return true;
    }
    
    public LuaValue[] getParams(final int begin, final int end) throws LuaException {
        final int count = end - begin;
        if (count <= 0) {
            return null;
        }
        final LuaValue[] args = new LuaValue[count];
        for (int i = 0; i < count; ++i) {
            final int index = begin + i + 2;
            args[i] = LuaValue.createFrom(this.L, index);
        }
        return args;
    }
    
    public final int getParamInt(final int i) throws LuaException {
        assert i >= 0;
        if (this.L.isObject(i + 2)) {
            return (int)(long)this.L.toJavaObject(i + 2);
        }
        return this.L.toInteger(i + 2);
    }
    
    public final double getParamDouble(final int i) throws LuaException {
        assert i >= 0;
        if (this.L.isObject(i + 2)) {
            return (double)this.L.toJavaObject(i + 2);
        }
        return this.L.toNumber(i + 2);
    }
    
    public final float getParamFloat(final int i) throws LuaException {
        return (float)this.getParamDouble(i);
    }
    
    public final long getParamLong(final int i) throws LuaException {
        assert i >= 0;
        if (this.L.isObject(i + 2)) {
            return (long)this.L.toJavaObject(i + 2);
        }
        if (this.L.isNumber(i + 2)) {
            return (long)this.L.toNumber(i + 2);
        }
        return 0L;
    }
    
    public final String getParamString(final int i) throws LuaException {
        assert i >= 0;
        return this.L.toString(i + 2);
    }
    
    public final LuaValue[] getParamTable(final int i) throws LuaException {
        assert i >= 0;
        final int tableIndex = i + 2;
        final LuaValue[] field = new LuaValue[this.L.objLen(tableIndex)];
        for (int k = 0; k < field.length; ++k) {
            this.L.pushNumber((double)(k + 1));
            this.L.getTable(tableIndex);
            field[k] = LuaValue.createFrom(this.L, -1);
            this.L.pop(1);
        }
        return field;
    }
    
    public final String getParamForcedAsString(final int i) throws LuaException {
        assert i >= 0;
        if (this.L.isObject(i + 2)) {
            return this.L.toJavaObject(i + 2).toString();
        }
        return this.L.toString(i + 2);
    }
    
    public final boolean getParamBool(final int i) throws LuaException {
        assert i >= 0;
        if (this.L.isObject(i + 2)) {
            return (boolean)this.L.toJavaObject(i + 2);
        }
        return this.L.toBoolean(i + 2);
    }
    
    public boolean isTable(final int i) {
        assert i >= 0;
        final int index = i + 2;
        return this.L.isTable(index);
    }
    
    public boolean isNumber(final int i) {
        assert i >= 0;
        final int index = i + 2;
        return this.L.isNumber(index);
    }
    
    public boolean isString(final int i) {
        assert i >= 0;
        final int index = i + 2;
        return this.L.isString(index);
    }
    
    public boolean isBoolean(final int i) {
        assert i >= 0;
        final int index = i + 2;
        return this.L.isBoolean(index);
    }
    
    private void checkReturnValued(final LuaScriptParameterType expectedType) throws LuaException {
        assert expectedType != null;
        if (this.m_returnDescriptors == null) {
            return;
        }
        if (this.m_returnValueCount >= this.m_returnDescriptors.length - 1 && this.resultDescrEndsWithBLoops()) {
            return;
        }
        final LuaScriptParameterType parameterType = this.m_returnDescriptors[this.m_returnValueCount].getType();
        if (!parameterType.equals(expectedType)) {
            throw this.createLuaException("Type de valeur de retour incorrecte :" + parameterType + "attendue: " + expectedType);
        }
    }
    
    protected final void addReturnValue(final boolean b) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.BOOLEAN);
        this.L.pushBoolean(b);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final int i) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.INTEGER);
        this.L.pushNumber((double)i);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final Object o) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.OBJECT);
        this.L.pushJavaObject(o);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final float f) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.NUMBER);
        this.L.pushNumber((double)f);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final long l) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.LONG);
        this.L.pushObjectValue((Object)l);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final double d) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.NUMBER);
        this.L.pushNumber(d);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final byte[] b) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.STRING);
        this.L.pushString(b);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnValue(final String s) throws LuaException {
        this.checkReturnValued(LuaScriptParameterType.STRING);
        this.L.pushString(s);
        ++this.m_returnValueCount;
    }
    
    protected final void addReturnNilValue() throws LuaException {
        this.L.pushNil();
        ++this.m_returnValueCount;
    }
    
    protected final LuaScript getScriptObject() throws LuaException {
        assert this.L != null;
        this.L.getGlobal("script");
        final LuaScript s = (LuaScript)this.L.toJavaObject(-1);
        this.L.pop(1);
        return s;
    }
    
    static {
        m_logger = Logger.getLogger((Class)JavaFunctionEx.class);
    }
}
