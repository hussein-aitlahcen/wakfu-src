package com.ankamagames.baseImpl.graphics.script;

import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.function.camera.*;

public class CameraFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final CameraFunctionsLibrary m_instance;
    private AleaWorldScene m_worldScene;
    
    public AleaWorldScene getWorldScene() {
        return this.m_worldScene;
    }
    
    public void setWorldScene(final AleaWorldScene worldScene) {
        this.m_worldScene = worldScene;
    }
    
    protected CameraFunctionsLibrary() {
        super();
        this.m_worldScene = null;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new SetCamera(luaState), new ZoomOn(luaState), new GetWorldTarget(luaState), new SetWorldTarget(luaState), new GetZoomFactor(luaState), new SetZoomFactor(luaState), new AttachCameraToMobile(luaState), new SetUserZoomLocked(luaState), new SetZoomSpeedFactor(luaState), new SetMoveSpeedFactor(luaState), new ScrollTo(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    public static CameraFunctionsLibrary getInstance() {
        return CameraFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Camera";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new CameraFunctionsLibrary();
    }
}
