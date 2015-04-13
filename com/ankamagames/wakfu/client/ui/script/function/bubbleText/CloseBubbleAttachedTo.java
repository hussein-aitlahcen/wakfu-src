package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class CloseBubbleAttachedTo extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    CloseBubbleAttachedTo(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "closeBubbleAttachedTo";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return CloseBubbleAttachedTo.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        AnimatedElementWithDirection mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            mobile = AnimatedElementSceneViewManager.getInstance().getElement(mobileId);
        }
        if (mobile == null) {
            this.writeError(CloseBubbleAttachedTo.m_logger, "mobile ou IE " + mobileId + " inexistant");
            return;
        }
        final Collection<WakfuBubbleWidget> bubbles = BubbleManager.getInstance().getBubblesAttachedTo(mobile);
        for (final WakfuBubbleWidget bubble : bubbles) {
            Close.closeBubble(bubble.getAdviserId());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)CloseBubbleAttachedTo.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.LONG, false) };
    }
}
