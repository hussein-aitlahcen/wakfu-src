package com.ankamagames.baseImpl.graphics.script;

import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.function.mobile.*;

public class MobileFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final MobileFunctionsLibrary m_instance;
    
    public static MobileFunctionsLibrary getInstance() {
        return MobileFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new MoveMobile(luaState), new SetMobileAnimation(luaState), new SetMobileAnimationSuffix(luaState), new SetMobileAnimationSpeed(luaState), new SetMobileStaticAnimationKey(luaState), new SetMobileToStaticAnimation(luaState), new SetMobileToHitAnimation(luaState), new GetMobileDirection(luaState), new SetMobileDirection(luaState), new SetMobileLookAt(luaState), new GetMobilePosition(luaState), new SetMobilePosition(luaState), new SetMobileVisible(luaState), new SetMobileMovementStyle(luaState), new GetMobileHeight(luaState), new GetMobileStatus(luaState), new OnPathEnded(luaState), new GetDistanceBetweenMobile(luaState), new SetMobileNext4Direction(luaState), new LinkMobile(luaState), new UnlinkMobile(luaState), new IncrementMobileDeltaZ(luaState), new SetMobileRadius(luaState), new SetMobileStatus(luaState), new SetMobileAlpha(luaState), new GenerateClientMobileId(luaState), new SetPartVisible(luaState), new SetPartColor(luaState), new SetPartColorByte(luaState), new SetCustomWalkStyle(luaState), new GetMobileAlpha(luaState), new GetAnimationDuration(luaState), new SetMobileJumpCapacity(luaState), new GetMobileJumpCapacity(luaState), new OnCellTransition(luaState), new AddMobileCreationCallback(luaState), new AddMobileDestructionCallback(luaState), new IsMobile(luaState), new IsAnimatedElement(luaState), new AddCubicSplineTweenToMobile(luaState), new SetCarriedMobile(luaState), new ThrowCarriedMobile(luaState), new GetMobileRadius(luaState), new IsMobileVisible(luaState), new HasAnimation(luaState), new ApplyEquipment(luaState), new GetDirectionTo(luaState), new GetDirection8To(luaState), new SetDirectionParticleSystemVisibility(luaState), new GetCarriedMobileId(luaState), new RemovePartColor(luaState), new SetMobileCanPlaySound(luaState), new IsMoving(luaState), new GetMobileAtCoordinates(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Mobile";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new MobileFunctionsLibrary();
    }
}
