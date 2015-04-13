package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.alea.adviser.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.xulor2.component.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class PrepareWakfuBubble extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    PrepareWakfuBubble(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "prepareWakfuBubble";
    }
    
    @Override
    public String getDescription() {
        return "Pr?pare une bulle de dialogue ? afficher pour faire parler un personnage";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PrepareWakfuBubble.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return PrepareWakfuBubble.RESULT;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        AnimatedElementWithDirection mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            mobile = AnimatedElementSceneViewManager.getInstance().getElement(mobileId);
        }
        if (mobile == null) {
            this.writeError(PrepareWakfuBubble.m_logger, "mobile ou IE " + mobileId + " inexistant");
            this.addReturnNilValue();
            this.addReturnNilValue();
            return;
        }
        final String widgetId = WakfuBubbleUtils.getNewWakfuBubbleId();
        final WakfuBubbleWidget bubble = WakfuBubbleUtils.loadBubble(widgetId);
        try {
            bubble.initialize(true, false);
        }
        catch (Exception e) {
            PrepareWakfuBubble.m_logger.warn((Object)e.getMessage());
            this.addReturnNilValue();
            this.addReturnNilValue();
            return;
        }
        bubble.setTarget(mobile);
        bubble.setBubbleObserver(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        bubble.setXOffset(this.getParamInt(1));
        bubble.setYOffset(this.getParamInt(2) + mobile.getVisualHeight() * 10);
        bubble.setBubbleIsVisible(false);
        boolean isInfinite = false;
        String onEndFunc = null;
        if (paramCount >= 4) {
            if (this.getParam(5).isBoolean()) {
                isInfinite = this.getParamBool(3);
            }
            else if (this.getParam(5).isString()) {
                onEndFunc = this.getParamString(3);
            }
        }
        if (paramCount >= 5 && this.getParam(6).isString()) {
            onEndFunc = this.getParamString(4);
        }
        if (isInfinite) {
            bubble.setDuration(-1);
        }
        final int adviserId = bubble.validateAdviser();
        BubbleManager.getInstance().putWakfuBubble(bubble);
        if (onEndFunc != null) {
            final BubbleClosedListener bcl = new BubbleClosedListener(onEndFunc, this.getScriptObject(), null);
            BubbleManager.getInstance().putInAdviserEventObservers(adviserId, bcl);
        }
        this.addReturnValue(bubble.getAdviserId());
        this.addReturnValue(bubble.getDuration());
    }
    
    static {
        m_logger = Logger.getLogger((Class)PrepareWakfuBubble.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", "Id du personnage", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("offsetX", "D?calage en pixel vers la droite", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("offsetY", "D?calage en pixel vers le haut", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("infiniteDuration", "si true la bulle reste toujours affich?e", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("funcOnEnd", "Fonction ? appeler lors de la fermeture", LuaScriptParameterType.STRING, true) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle cr??e", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("displayTime", "Temps d'affichage en ms", LuaScriptParameterType.INTEGER, false) };
    }
}
