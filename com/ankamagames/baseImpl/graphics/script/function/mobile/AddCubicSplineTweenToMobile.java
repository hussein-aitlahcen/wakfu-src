package com.ankamagames.baseImpl.graphics.script.function.mobile;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddCubicSplineTweenToMobile extends JavaFunctionEx
{
    private static final String NAME = "addCubicSplineTweenToMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public AddCubicSplineTweenToMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addCubicSplineTweenToMobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddCubicSplineTweenToMobile.PARAMS;
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
        final float startVectorX = (float)this.getParamDouble(5);
        final float startVectorY = (float)this.getParamDouble(6);
        final float startVectorZ = (float)this.getParamDouble(7);
        final float endVectorX = (float)this.getParamDouble(8);
        final float endVectorY = (float)this.getParamDouble(9);
        final float endVectorZ = (float)this.getParamDouble(10);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        final CubicSplineTween trajectory = new CubicSplineTween(mobile);
        trajectory.setFinalPosition(new Vector3(destX, destY, destZ));
        trajectory.setInitialPosition(new Vector3(mobile.getWorldCellX(), mobile.getWorldCellY(), mobile.getWorldCellAltitude()));
        trajectory.setDuration(duration);
        trajectory.setInitialVelocity(new Vector3(startVectorX, startVectorY, startVectorZ));
        trajectory.setFinalVelocity(new Vector3(endVectorX, endVectorY, endVectorZ));
        TweenManager.getInstance().addTween(trajectory);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("destX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destZ", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("duration", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startVectorX", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("startVectorY", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("startVectorZ", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("endVectorX", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("endVectorY", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("endVectorZ", null, LuaScriptParameterType.NUMBER, false) };
    }
}
