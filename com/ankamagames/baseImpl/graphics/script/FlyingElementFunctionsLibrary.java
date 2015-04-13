package com.ankamagames.baseImpl.graphics.script;

import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class FlyingElementFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final FlyingElementFunctionsLibrary m_instance;
    
    public static FlyingElementFunctionsLibrary getInstance() {
        return FlyingElementFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Flying";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new AddFlyingImageIE(luaState), new AddFlyingImageMob(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        m_instance = new FlyingElementFunctionsLibrary();
    }
    
    private static class AddFlyingImageIE extends JavaFunctionEx
    {
        AddFlyingImageIE(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addFlyingImageIE";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("interactiveElementId", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("imagePath", null, LuaScriptParameterType.STRING, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final String path = this.getParamString(1);
            final ClientInteractiveAnimatedElementSceneView ie = AnimatedElementSceneViewManager.getInstance().getElement(id);
            final FlyingImage.DefaultFlyingImageDeformer deformer = new FlyingImage.DefaultFlyingImageDeformer();
            final FlyingImage flyingImage = new FlyingImage(path, -1, -1, deformer, 3000);
            flyingImage.setTarget(ie);
            AdviserManager.getInstance().addAdviser(flyingImage);
        }
    }
    
    private static class AddFlyingImageMob extends JavaFunctionEx
    {
        AddFlyingImageMob(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addFlyingImageMob";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("imagePath", null, LuaScriptParameterType.STRING, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final String path = this.getParamString(1);
            final Mobile mobile = MobileManager.getInstance().getMobile(id);
            final FlyingImage.DefaultFlyingImageDeformer deformer = new FlyingImage.DefaultFlyingImageDeformer();
            final FlyingImage flyingImage = new FlyingImage(path, -1, -1, deformer, 3000);
            flyingImage.setTarget(mobile);
            AdviserManager.getInstance().addAdviser(flyingImage);
        }
    }
}
