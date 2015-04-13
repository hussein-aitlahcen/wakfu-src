package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;

public class ServerEventFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final ServerEventFunctionsLibrary m_instance;
    
    public static ServerEventFunctionsLibrary getInstance() {
        return ServerEventFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Server";
    }
    
    @Override
    public String getDescription() {
        return "Fonctions critiques qu'il vaut mieux ?viter d'utiliser";
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new TriggerEvent(luaState), new SetMonsterFollowEventId(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return new JavaFunctionEx[0];
    }
    
    static {
        m_instance = new ServerEventFunctionsLibrary();
    }
    
    private static class TriggerEvent extends JavaFunctionEx
    {
        TriggerEvent(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "triggerEvent";
        }
        
        @Override
        public String getDescription() {
            return "Envoie un ?v?nement destin? ? d?clencher une action cot? serveur";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            final LuaScriptParameterDescriptor[] desc = { new LuaScriptParameterDescriptor("Id de l'?v?nement", "Id de l'?v?nement, utilis? comme filtre c?t? serveur", LuaScriptParameterType.INTEGER, false) };
            return desc;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int eventId = this.getParamInt(0);
            final TriggerServerEvent netMessage = new TriggerServerEvent();
            netMessage.setEventId(eventId);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
    }
    
    private static class SetMonsterFollowEventId extends JavaFunctionEx
    {
        SetMonsterFollowEventId(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "setMonsterFollowEventId";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            final LuaScriptParameterDescriptor[] desc = { new LuaScriptParameterDescriptor("Id de l'?v?nement", null, LuaScriptParameterType.INTEGER, false) };
            return desc;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int eventId = this.getParamInt(0);
            final MRUMonsterFollowAction action = (MRUMonsterFollowAction)MRUActions.FOLLOW_MONSTER_ACTION.getModel();
            action.setEventId(eventId);
        }
    }
}
