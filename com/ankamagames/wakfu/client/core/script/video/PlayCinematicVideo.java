package com.ankamagames.wakfu.client.core.script.video;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

class PlayCinematicVideo extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "playCinematicVideo";
    private static final String DESC = "Joue une video en plein \u00e9cran";
    private static final LuaScriptParameterDescriptor[] START_VIDEO_LOADING_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    
    PlayCinematicVideo(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "playCinematicVideo";
    }
    
    @Override
    public String getDescription() {
        return "Joue une video en plein \u00e9cran";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayCinematicVideo.START_VIDEO_LOADING_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    }
    
    @Nullable
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final String videoName = this.getParamString(0);
        try {
            final String videoPath = WakfuConfiguration.getInstance().getString("videosPath") + videoName;
            final String fileName = ContentFileHelper.transformFileNameForVlc(videoPath);
            UICinematicVideoFrame.INSTANCE.setVideoPath(fileName);
            WakfuGameEntity.getInstance().pushFrame(UICinematicVideoFrame.INSTANCE);
            UIMessage.send((short)19100);
        }
        catch (PropertyException e) {
            PlayCinematicVideo.m_logger.error((Object)"Impossible de r?cup?rer le chemin vers les videos", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayCinematicVideo.class);
        START_VIDEO_LOADING_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("videoName", "Le nom de la vid\u00e9o \u00e0 jouer", LuaScriptParameterType.STRING, false) };
    }
}
