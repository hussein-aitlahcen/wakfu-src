package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import org.keplerproject.luajava.*;

final class SetScreenPosition extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    SetScreenPosition(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setScreenPosition";
    }
    
    @Override
    public String getDescription() {
        return "D?place une bulle de dialogue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("align", "Alignement de la bulle", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("screenXoffset", "D?calage en pixel vers la droite", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("screenYoffset", "D?calage en pixel vers le haut", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int bubbleID = this.getParamInt(0);
        final InteractiveBubble bubble = BubbleManager.getInstance().getInteractiveBubble(bubbleID);
        if (bubble != null) {
            final Alignment17 align = Alignment17.valueOf(this.getParamString(1));
            final int xoffset = this.getParamInt(2);
            final int yoffset = this.getParamInt(3);
            final StaticLayoutData sld = new StaticLayoutData();
            sld.onCheckOut();
            sld.setAlign(align);
            sld.setXOffset(xoffset);
            sld.setYOffset(yoffset);
            bubble.add(sld);
        }
        SetScreenPosition.m_logger.info((Object)("SetScreenPosition " + bubble));
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetScreenPosition.class);
    }
}
