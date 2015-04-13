package com.ankamagames.wakfu.client.core.script.video;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

class StartVideoLoading extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "startVideoLoading";
    private static final String DESC = "[WIP] Charge une vid\u00e9o donn\u00e9e et affiche une interface de chargement";
    private static final LuaScriptParameterDescriptor[] START_VIDEO_LOADING_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    
    StartVideoLoading(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "startVideoLoading";
    }
    
    @Override
    public String getDescription() {
        return "[WIP] Charge une vid\u00e9o donn\u00e9e et affiche une interface de chargement";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return StartVideoLoading.START_VIDEO_LOADING_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    }
    
    @Nullable
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final String videoName = this.getParamString(0);
        try {
            final String videoPath = String.format(WakfuConfiguration.getInstance().getString("videosPath"), videoName);
            UIVideoLoadingFrame.getInstance().setVideoPath(ContentFileHelper.transformFileNameForVlc(videoPath));
            WakfuGameEntity.getInstance().pushFrame(UIVideoLoadingFrame.getInstance());
        }
        catch (PropertyException e) {
            StartVideoLoading.m_logger.error((Object)"Impossible de r?cup?rer le chemin vers les videos");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)StartVideoLoading.class);
        START_VIDEO_LOADING_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("videoName", "Le nom de la vid\u00e9o \u00e0 charger", LuaScriptParameterType.STRING, false) };
    }
}
