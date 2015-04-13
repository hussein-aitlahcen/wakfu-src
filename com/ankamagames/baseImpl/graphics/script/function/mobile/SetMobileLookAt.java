package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.script.*;

public class SetMobileLookAt extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileLookAt";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    public SetMobileLookAt(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileLookAt";
    }
    
    @Override
    public String getDescription() {
        return "Oriente un mobile pour qu'il puisse regarder un point donn? de l'espace";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileLookAt.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return SetMobileLookAt.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(SetMobileLookAt.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
            return;
        }
        final int worldX = this.getParamInt(1);
        final int worldY = this.getParamInt(2);
        final boolean isEightDirections = (paramCount >= 4) ? this.getParamBool(3) : isEightDirections(mobile);
        final int dx = worldX - mobile.getWorldCellX();
        final int dy = worldY - mobile.getWorldCellY();
        Direction8 result = mobile.getDirection();
        if (isEightDirections && (dx != 0 || dy != 0)) {
            result = Vector3i.getDirection8FromVector(dx, dy);
        }
        else if (!isEightDirections) {
            result = new Vector3i(dx, dy, 0).toDirection4();
        }
        if (result != Direction8.NONE) {
            mobile.setDirection(result);
        }
        this.addReturnValue(result.m_index);
    }
    
    private static boolean isEightDirections(final Mobile mobile) {
        return mobile instanceof PathMobile && ((PathMobile)mobile).getAvailableDirections() == 8;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileLookAt.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("worldX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("isHeightDirections", "Si true dirige le perso dans un monde a 8 directions, sinon ? 4", LuaScriptParameterType.BOOLEAN, true) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("directionIndex", "Index de la nouvelle direction du personnage", LuaScriptParameterType.INTEGER, false) };
    }
}
