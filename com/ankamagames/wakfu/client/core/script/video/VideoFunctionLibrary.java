package com.ankamagames.wakfu.client.core.script.video;

import org.jetbrains.annotations.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class VideoFunctionLibrary extends JavaFunctionsLibrary
{
    private static final String LIBRARY_NAME = "Video";
    private static final String DESC = "Fournit les fonctions li\u00e9es \u00e0 l'utilisation de la video dans le script LUA";
    public static final VideoFunctionLibrary INSTANCE;
    
    @Nullable
    @Override
    public String getName() {
        return "Video";
    }
    
    @Override
    public String getDescription() {
        return "Fournit les fonctions li\u00e9es \u00e0 l'utilisation de la video dans le script LUA";
    }
    
    @Nullable
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new StartVideoLoading(luaState), new AddVideoStopOrEndCallback(luaState), new PlayCinematicVideo(luaState) };
    }
    
    @Nullable
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        INSTANCE = new VideoFunctionLibrary();
    }
}
