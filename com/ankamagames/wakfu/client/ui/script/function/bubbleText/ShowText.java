package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

final class ShowText extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    ShowText(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "showText";
    }
    
    @Override
    public String getDescription() {
        return "Affiche une bulle de texte au dessus d'un personnage. La bulle s'effacera au bout d'un certain temps(d?pend de la taille du texte)";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("text", "Texte ? afficher", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("offsetX", "D?calage en pixel vers la droite", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("offsetY", "D?calage en pixel vers le haut", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("infiniteDuration", "Si true la bulle reste toujours affich?e", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("funcOnEnd", "Fonction ? appeler ? la fin", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", "Param?tres de fonction de fin", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle cr??e", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("displayTime", "Temps d'affichage en ms", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            final String text = BubbleTextUtils.getTranslatedText(this.getParamString(1), new Object[0]);
            final String widgetId = WakfuBubbleUtils.getNewWakfuBubbleId();
            final WakfuBubbleWidget bubble = WakfuBubbleUtils.loadBubble(widgetId);
            try {
                bubble.initialize(true, false);
                bubble.setText(text);
            }
            catch (Exception e) {
                ShowText.m_logger.warn((Object)e.getMessage());
                return;
            }
            bubble.setTarget(mobile);
            bubble.setBubbleObserver(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
            bubble.setXOffset(this.getParamInt(2));
            bubble.setYOffset(this.getParamInt(3));
            boolean isInfinite = false;
            String onEndFunc = null;
            LuaValue[] params = null;
            if (paramCount >= 5) {
                if (this.getParam(6).isBoolean()) {
                    isInfinite = this.getParamBool(4);
                }
                else if (this.getParam(6).isString()) {
                    onEndFunc = this.getParamString(4);
                    if (paramCount >= 6) {
                        params = this.getParams(5, paramCount);
                    }
                }
            }
            if (paramCount >= 6) {
                if (this.getParam(7).isString()) {
                    onEndFunc = this.getParamString(5);
                }
                if (paramCount >= 7) {
                    params = this.getParams(6, paramCount);
                }
            }
            if (isInfinite) {
                bubble.setDuration(-1);
            }
            final int adviserId = bubble.validateAdviser();
            BubbleManager.getInstance().putWakfuBubble(bubble);
            if (onEndFunc != null) {
                final BubbleClosedListener bcl = new BubbleClosedListener(onEndFunc, this.getScriptObject(), params);
                BubbleManager.getInstance().putInAdviserEventObservers(adviserId, bcl);
            }
            this.addReturnValue(bubble.getAdviserId());
            this.addReturnValue(bubble.getDuration());
        }
        else {
            this.writeError(ShowText.m_logger, "mobile " + mobileId + " inexistant");
            this.addReturnNilValue();
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShowText.class);
    }
}
