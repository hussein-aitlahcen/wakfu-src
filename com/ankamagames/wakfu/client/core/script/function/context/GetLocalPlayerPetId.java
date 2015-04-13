package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import org.keplerproject.luajava.*;

public final class GetLocalPlayerPetId extends JavaFunctionEx
{
    public GetLocalPlayerPetId(final LuaState state) {
        super(state);
    }
    
    @Override
    public String getName() {
        return "getPlayerPetId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("localPlayerPetId", null, LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final PetMobileView pet = WakfuGameEntity.getInstance().getLocalPlayer().getPetMobile();
        if (pet != null) {
            this.addReturnValue(pet.getMobile().getId());
        }
        else {
            this.addReturnValue(0L);
        }
    }
}
