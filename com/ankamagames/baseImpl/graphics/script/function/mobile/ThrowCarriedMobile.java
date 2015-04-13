package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class ThrowCarriedMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "throwCarriedMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public ThrowCarriedMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "throwCarriedMobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ThrowCarriedMobile.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final int destX = this.getParamInt(1);
        final int destY = this.getParamInt(2);
        final int destZ = this.getParamInt(3);
        final int duration = this.getParamInt(4);
        final float startVectorZ = (float)this.getParamDouble(5);
        final float endVectorZ = (float)this.getParamDouble(6);
        final Mobile carrier = MobileManager.getInstance().getMobile(mobileId);
        if (carrier == null) {
            this.writeError(ThrowCarriedMobile.m_logger, "Pas de carrier trouv? avec l'id " + mobileId);
            return;
        }
        final Mobile carriedMobile = carrier.getCarriedMobile();
        if (carriedMobile == null) {
            this.writeError(ThrowCarriedMobile.m_logger, "Pas de carrier port? trouv? avec l'id " + mobileId);
            return;
        }
        carriedMobile.setVisible(true);
        carrier.uncarry(false, null);
        final CubicSplineTween trajectory = new CubicSplineTween(carriedMobile);
        trajectory.setFinalPosition(new Vector3(destX, destY, destZ));
        trajectory.setInitialPosition(new Vector3(carriedMobile.getWorldCellX(), carriedMobile.getWorldCellY(), carriedMobile.getWorldCellAltitude() + carrier.getHeight()));
        trajectory.setDuration(duration);
        trajectory.setInitialVelocity(new Vector3(0.0f, 0.0f, startVectorZ));
        trajectory.setFinalVelocity(new Vector3(0.0f, 0.0f, endVectorZ));
        TweenManager.getInstance().addTween(trajectory);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ThrowCarriedMobile.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("destX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destZ", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("duration", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startVectorZ", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("endVectorZ", null, LuaScriptParameterType.NUMBER, false) };
    }
}
