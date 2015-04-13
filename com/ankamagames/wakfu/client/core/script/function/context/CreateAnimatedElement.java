package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class CreateAnimatedElement extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public CreateAnimatedElement(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "createAnimatedElement";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("type", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("spriteName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("worldX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("altitude", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("animName", null, LuaScriptParameterType.STRING, true) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", null, LuaScriptParameterType.LONG, false) };
    }
    
    public void run(final int paramCount) throws LuaException {
        final String type = this.getParamString(0);
        final String spriteName = this.getParamString(1);
        final int worldX = this.getParamInt(2);
        final int worldY = this.getParamInt(3);
        final int altitude = this.getParamInt(4);
        final String animName = (paramCount >= 6) ? this.getParamString(5) : "1_AnimStatique";
        long id;
        for (id = getId(); SimpleAnimatedElementManager.getInstance().getAnimatedElement(id) != null; id = getId()) {
            CreateAnimatedElement.m_logger.warn((Object)("contient d?j? un ?lement avec cet id " + id));
        }
        final AnimatedElement element = new AnimatedElement(id, worldX, worldY, altitude);
        try {
            element.load(getFilename(type, spriteName), true);
        }
        catch (IOException e) {
            CreateAnimatedElement.m_logger.error((Object)"", (Throwable)e);
            this.addReturnNilValue();
            return;
        }
        element.setGfxId(type + "/" + spriteName);
        element.setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
        element.setAnimation(animName);
        SimpleAnimatedElementManager.getInstance().addAnimatedElement(element);
        this.addReturnValue(id);
    }
    
    private static long getId() {
        return GUIDGenerator.getGUID();
    }
    
    private static String getConfigKey(final String type) {
        if (type.equals("interactives")) {
            return "ANMInteractiveElementPath";
        }
        if (type.equals("resources")) {
            return "ANMResourcePath";
        }
        if (type.equals("npcs")) {
            return "npcGfxPath";
        }
        if (type.equals("pets")) {
            return "petGfxPath";
        }
        if (type.equals("players")) {
            return "playerGfxPath";
        }
        if (type.equals("dynamics")) {
            return "ANMDynamicElementPath";
        }
        return null;
    }
    
    public static String getFilename(final String type, final String gfx) {
        final String configKey = getConfigKey(type.toLowerCase());
        try {
            if (configKey != null) {
                final String gfxFile = WakfuConfiguration.getInstance().getString(configKey);
                return String.format(gfxFile, gfx);
            }
        }
        catch (PropertyException e) {
            CreateAnimatedElement.m_logger.error((Object)("probl?me avec le type " + type), (Throwable)e);
        }
        CreateAnimatedElement.m_logger.error((Object)("type inconnu " + type));
        return type + "/" + gfx;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CreateAnimatedElement.class);
    }
}
