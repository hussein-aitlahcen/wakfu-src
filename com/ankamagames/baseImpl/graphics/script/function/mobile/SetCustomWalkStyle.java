package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetCustomWalkStyle extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setCustomWalkStyle";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetCustomWalkStyle(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setCustomWalkStyle";
    }
    
    @Override
    public String getDescription() {
        return "Change l'animation de marche d'un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetCustomWalkStyle.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null || !(mobile instanceof PathMobile)) {
            this.writeError(SetCustomWalkStyle.m_logger, "le mobile " + mobileId + " n'existe pas ");
            return;
        }
        final PathMobile pathMobile = (PathMobile)mobile;
        if (paramCount == 1) {
            pathMobile.resetMovementSelector();
        }
        else {
            pathMobile.setMovementSelector(true, MovementStyleManager.CUSTOM_STYLE);
            final CustomWalkMovementStyle walkStyle = (CustomWalkMovementStyle)pathMobile.getMovementStyle();
            walkStyle.setAnimation(this.getParamString(1));
            if (paramCount == 3) {
                walkStyle.setCellSpeed(this.getParamInt(2));
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetCustomWalkStyle.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationName", "Nom de l'animation", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("cellSpeed", "Temps de d?placement d'une cellule vers la suivante en ms", LuaScriptParameterType.INTEGER, true) };
    }
}
