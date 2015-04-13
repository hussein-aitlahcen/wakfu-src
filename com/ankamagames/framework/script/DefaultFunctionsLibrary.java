package com.ankamagames.framework.script;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.script.defaultFunctionLibrary.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class DefaultFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] GET_TIMER_RESULTS;
    private static final LuaScriptParameterDescriptor[] INVOKE_PARAMS;
    private static final LuaScriptParameterDescriptor[] INVOKE_RESULTS;
    private static final LuaScriptParameterDescriptor[] REQUIRE_PARAMS;
    private static final LuaScriptParameterDescriptor[] IMPORT_PARAMS;
    private static final LuaScriptParameterDescriptor[] SET_INTERVAL_PARAMS;
    private static final LuaScriptParameterDescriptor[] SET_INTERVAL_RESULTS;
    private static final LuaScriptParameterDescriptor[] INTERRUPT_PARAMS;
    private static final LuaScriptParameterDescriptor[] TRACE_PARAMS;
    private static final LuaScriptParameterDescriptor[] IS_EQUAL_PARAMS;
    private static final LuaScriptParameterDescriptor[] IS_EQUAL_RESULTS;
    private static final LuaScriptParameterDescriptor[] IS_GREATER_PARAMS;
    private static final LuaScriptParameterDescriptor[] IS_GREATER_RESULTS;
    private static final LuaScriptParameterDescriptor[] IS_LOWER_PARAMS;
    private static final LuaScriptParameterDescriptor[] IS_LOWER_RESULTS;
    private static final LuaScriptParameterDescriptor[] SUBSTRACT_PARAMS;
    private static final LuaScriptParameterDescriptor[] SUBSTRACT_RESULTS;
    private static final LuaScriptParameterDescriptor[] ABSOLUTE_VALUE_PARAMS;
    private static final LuaScriptParameterDescriptor[] ABSOLUTE_VALUE_RESULTS;
    private static final LuaScriptParameterDescriptor[] MAX_VALUE_PARAMS;
    private static final LuaScriptParameterDescriptor[] MAX_VALUE_RESULTS;
    private static final LuaScriptParameterDescriptor[] RANDOM_PARAMS;
    private static final LuaScriptParameterDescriptor[] RANDOM_RESULTS;
    private static final LuaScriptParameterDescriptor[] RANDOM_FLOAT_PARAMS;
    private static final LuaScriptParameterDescriptor[] RANDOM_FLOAT_RESULTS;
    private static final LuaScriptParameterDescriptor[] TO_LONG_PARAMS;
    private static final LuaScriptParameterDescriptor[] TO_LONG_RESULTS;
    private static final LuaScriptParameterDescriptor[] POSITION_TO_LONG_PARAMS;
    private static final LuaScriptParameterDescriptor[] POSITION_TO_LONG_RESULTS;
    private static final LuaScriptParameterDescriptor[] LONG_TO_POSITION_PARAMS;
    private static final LuaScriptParameterDescriptor[] LONG_TO_POSITION_RESULTS;
    private static final LuaScriptParameterDescriptor[] TO_STRING_PARAMS;
    private static final LuaScriptParameterDescriptor[] TO_STRING_RESULTS;
    private static final DefaultFunctionsLibrary m_instance;
    
    @Nullable
    @Override
    public final String getName() {
        return null;
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public static DefaultFunctionsLibrary getInstance() {
        return DefaultFunctionsLibrary.m_instance;
    }
    
    @Nullable
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState state) {
        return null;
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState state) {
        return new JavaFunctionEx[] { new GetTimer(state), new Invoke(state), new SetInterval(state), new Trace(state), new Interrupt(state), new Require(state), new Import(state), new IsEqual(state), new IsLower(state), new IsGreater(state), new Substract(state), new Random(state), new RandomFloat(state), new AbsoluteValue(state), new MaxValue(state), new ToString(state), new PositionToLong(state), new LongToPosition(state), new ToLong(state), new ExecuteScript(state) };
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultFunctionsLibrary.class);
        GET_TIMER_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("time", "Dur?e d'execution du script en milliseconds", LuaScriptParameterType.INTEGER, false) };
        INVOKE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("time", "Temps d'attente avant l'appel de la fonction (en ms)", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("loopCount", "Nombre de fois ou la m?thode doit ?tre appel?e (doit ?tre = 1)", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("funcName", "Fonction ? appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcParams", "Param?tres de la fonction ? appeler", LuaScriptParameterType.BLOOPS, true) };
        INVOKE_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("taskId", "Id de la t?che", LuaScriptParameterType.INTEGER, false) };
        REQUIRE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("fileName", "Nom du fichier contenant le script", LuaScriptParameterType.INTEGER, false) };
        IMPORT_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("libName", "chaine de texte correspondant au nom de la librairie ? charger", LuaScriptParameterType.STRING, false) };
        SET_INTERVAL_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("time", "Temps d'attente avant l'appel de la fonction (en ms)", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("funcName", "Fonction ? appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcParams", "Param?tres de la fonction ? appeler", LuaScriptParameterType.BLOOPS, true) };
        SET_INTERVAL_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("taskId", "Id de la t?che", LuaScriptParameterType.INTEGER, false) };
        INTERRUPT_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("taskId", "Identifiant de la t?che ? stopper", LuaScriptParameterType.INTEGER, true) };
        TRACE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", "Un ensemble de valeurs, au format texte ou num?rique, qui seront affich?es les unes apr?s les autres.", LuaScriptParameterType.BLOOPS, true) };
        IS_EQUAL_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.NUMBER, true) };
        IS_EQUAL_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.BOOLEAN, false) };
        IS_GREATER_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.NUMBER, true) };
        IS_GREATER_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.BOOLEAN, false) };
        IS_LOWER_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.NUMBER, true) };
        IS_LOWER_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.BOOLEAN, false) };
        SUBSTRACT_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.NUMBER, true) };
        SUBSTRACT_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.LONG, false) };
        ABSOLUTE_VALUE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param", null, LuaScriptParameterType.LONG, false) };
        ABSOLUTE_VALUE_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.LONG, false) };
        MAX_VALUE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.LONG, false) };
        MAX_VALUE_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.LONG, false) };
        RANDOM_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.NUMBER, true) };
        RANDOM_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.LONG, false) };
        RANDOM_FLOAT_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("param2", null, LuaScriptParameterType.NUMBER, true) };
        RANDOM_FLOAT_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.NUMBER, false) };
        TO_LONG_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param", null, LuaScriptParameterType.OBJECT, false) };
        TO_LONG_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.LONG, false) };
        POSITION_TO_LONG_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", null, LuaScriptParameterType.NUMBER, false) };
        POSITION_TO_LONG_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.LONG, false) };
        LONG_TO_POSITION_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.LONG, false) };
        LONG_TO_POSITION_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", null, LuaScriptParameterType.NUMBER, false) };
        TO_STRING_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("param1", null, LuaScriptParameterType.BLOOPS, false) };
        TO_STRING_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", null, LuaScriptParameterType.STRING, false) };
        m_instance = new DefaultFunctionsLibrary();
    }
    
    private static class GetTimer extends JavaFunctionEx
    {
        GetTimer(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "getTimer";
        }
        
        @Override
        public String getDescription() {
            return "R?cup?re le temps ?coul? depuis le d?but d'ex?cution du script, en millisecondes.";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.GET_TIMER_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            this.addReturnValue(this.getScriptObject().getTime());
        }
    }
    
    private static class Invoke extends JavaFunctionEx
    {
        Invoke(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "invoke";
        }
        
        @Override
        public String getDescription() {
            return "Demande l'ex?cution d'une fonction LUA apr?s un interval de temps donn?, une ou plusieurs fois.";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.INVOKE_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.INVOKE_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            final int time = this.getParamInt(0);
            final int loopCount = this.getParamInt(1);
            final String funcName = this.getParamString(2);
            final LuaValue[] args = this.getParams(3, paramCount);
            final LuaScript.Task task = script.registerTimedTask(time, loopCount, funcName, args);
            this.addReturnValue(task.getId());
        }
    }
    
    private static class Require extends JavaFunctionEx
    {
        Require(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "require";
        }
        
        @Override
        public String getDescription() {
            return "Ex?cute le contenu du script pass? en param?tre, et charge les fonctions qu'il contient. Toutes les fonctions contenues dans le script appel? peuvent donc maitnenant ?tre utilis?es telles qu'elles dans le script d'origine.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.REQUIRE_PARAMS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int filename = this.getParamInt(0);
            this.L.LdoFile(LuaManager.getInstance().getPath() + filename + LuaManager.getInstance().getExtension());
        }
    }
    
    private static class Import extends JavaFunctionEx
    {
        Import(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "import";
        }
        
        @Override
        public String getDescription() {
            return "Met ? disposition toutes les fonctions contenues dans une librairie Java donn?e";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.IMPORT_PARAMS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            DefaultFunctionsLibrary.m_logger.warn((Object)"'import' ne devrait plus ?tre appel?");
        }
    }
    
    private static class SetInterval extends JavaFunctionEx
    {
        SetInterval(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "setInterval";
        }
        
        @Override
        public String getDescription() {
            return "Demande l'ex?cution d'une fonction LUA apr?s un interval de temps donn?, une infinit? de fois.";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.SET_INTERVAL_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.SET_INTERVAL_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            final int time = this.getParamInt(0);
            final String funcName = this.getParamString(1);
            final LuaValue[] args = this.getParams(2, paramCount);
            final int taskId = script.registerTimedTask(time, -1, funcName, args).m_taskId;
            this.addReturnValue(taskId);
        }
    }
    
    private static class Interrupt extends JavaFunctionEx
    {
        Interrupt(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "interrupt";
        }
        
        @Override
        public String getDescription() {
            return "Termine imm?diatement l'ex?cution d'un script";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.INTERRUPT_PARAMS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            if (script != null) {
                if (paramCount == 1) {
                    script.unregisterTask(this.getParamInt(0));
                }
                else {
                    script.interrupt();
                }
            }
        }
    }
    
    private static class Trace extends JavaFunctionEx
    {
        Trace(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "trace";
        }
        
        @Override
        public String getDescription() {
            return "Affiche du texte dans la console de debuggage. Si on donne plusieurs valeurs, elle seront s?par?es par une virgule.";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.TRACE_PARAMS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final StringBuilder builder = new StringBuilder("[ligne: " + this.getLineNumber() + ']');
            for (int i = 0; i < paramCount; ++i) {
                final String param = this.getParamForcedAsString(i);
                builder.append(", ").append((param != null) ? param : null);
            }
            DefaultFunctionsLibrary.m_logger.info((Object)builder.toString());
        }
    }
    
    private static class IsEqual extends JavaFunctionEx
    {
        IsEqual(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "isEqual";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.IS_EQUAL_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.IS_EQUAL_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final long param1 = this.getParamLong(0);
            final long param2 = this.getParamLong(1);
            final boolean result = param1 == param2;
            this.addReturnValue(result);
        }
    }
    
    private static class IsGreater extends JavaFunctionEx
    {
        IsGreater(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "isGreater";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.IS_GREATER_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.IS_GREATER_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final long param1 = this.getParamLong(0);
            final long param2 = this.getParamLong(1);
            final boolean result = param1 > param2;
            this.addReturnValue(result);
        }
    }
    
    private static class IsLower extends JavaFunctionEx
    {
        IsLower(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "isLower";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.IS_LOWER_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.IS_LOWER_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final boolean result = this.getParamLong(0) < this.getParamLong(1);
            this.addReturnValue(result);
        }
    }
    
    private static class Substract extends JavaFunctionEx
    {
        Substract(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "substract";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.SUBSTRACT_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.SUBSTRACT_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final long param1 = this.getParamLong(0);
            final long param2 = this.getParamLong(1);
            final long result = param1 - param2;
            this.addReturnValue(result);
        }
    }
    
    private static class AbsoluteValue extends JavaFunctionEx
    {
        AbsoluteValue(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "abs";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.ABSOLUTE_VALUE_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.ABSOLUTE_VALUE_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final long param = this.getParamLong(0);
            this.addReturnValue(Math.abs(param));
        }
    }
    
    private static class MaxValue extends JavaFunctionEx
    {
        MaxValue(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "max";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.MAX_VALUE_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.MAX_VALUE_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final long param1 = this.getParamLong(0);
            final long param2 = this.getParamLong(1);
            this.addReturnValue(Math.max(param1, param2));
        }
    }
    
    private static class Random extends JavaFunctionEx
    {
        Random(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "random";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.RANDOM_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.RANDOM_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final long param1 = this.getParamLong(0);
            final long param2 = this.getParamLong(1);
            final long result = MathHelper.random(param1, param2);
            this.addReturnValue(result);
        }
    }
    
    private static class RandomFloat extends JavaFunctionEx
    {
        RandomFloat(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "randomFloat";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.RANDOM_FLOAT_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.RANDOM_FLOAT_RESULTS;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final float param1 = this.getParamFloat(0);
            final float param2 = this.getParamFloat(1);
            final float result = MathHelper.random(param1, param2);
            this.addReturnValue(result);
        }
    }
    
    private static class ToLong extends JavaFunctionEx
    {
        ToLong(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "toLong";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.TO_LONG_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.TO_LONG_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            this.addReturnValue(this.getParamLong(0));
        }
    }
    
    private static class PositionToLong extends JavaFunctionEx
    {
        PositionToLong(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "positionToLong";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.POSITION_TO_LONG_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.POSITION_TO_LONG_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final int z = this.getParamInt(2);
            this.addReturnValue(PositionValue.toLong(x, y, (short)z));
        }
    }
    
    private static class LongToPosition extends JavaFunctionEx
    {
        LongToPosition(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "longToPosition";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.LONG_TO_POSITION_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.LONG_TO_POSITION_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final Point3 pos = PositionValue.fromLong(this.getParamLong(0));
            this.addReturnValue(pos.getX());
            this.addReturnValue(pos.getY());
            this.addReturnValue(pos.getZ());
        }
    }
    
    private static class ToString extends JavaFunctionEx
    {
        ToString(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "toString";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return DefaultFunctionsLibrary.TO_STRING_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return DefaultFunctionsLibrary.TO_STRING_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < paramCount; ++i) {
                final String param = this.getParamForcedAsString(i);
                builder.append((param != null) ? param : null);
            }
            this.addReturnValue(builder.toString());
        }
    }
}
