package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import org.keplerproject.luajava.*;

final class PrepareFloating extends JavaFunctionEx
{
    private Alignment17 m_align;
    private int m_xoffset;
    private int m_yoffset;
    private boolean m_actAsButton;
    private int m_duration;
    private boolean m_closeOnClick;
    private String m_endFunc;
    
    PrepareFloating(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "prepareFloating";
    }
    
    @Override
    public String getDescription() {
        return "Pr?pare une bulle de texte (avec interaction de l'utilisateur) ? l'?cran";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("align", "Alignement de la bulle de texte", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("screenXoffset", "D?calage en pixel vers la droite", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("screenYoffset", "D?calage en pixel vers le heut", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("actAsButton", "La bulle se comportera comme la somme des boutons qui lui seront attach?s par la suite", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("duration", "Dur?e d'affichage de la bulle", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("closeOnClick", "Ferme si on clique sur la bulle", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("onEndFunc", "Fonction ? appaler lors de la fermeture", LuaScriptParameterType.STRING, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.initializeParamsToDefaultValue();
        this.getParamsValue(paramCount);
        final int id = AdviserManager.getInstance().getNewUniqueId();
        final String dialogId = "interactiveBubbleDialog" + id;
        final InteractiveBubble bubble = this.createBubble(dialogId);
        this.addEndFunctionRunner(dialogId);
        this.createWidget(bubble);
        this.addBubbleToManager(id, bubble);
        this.setBubbleParams(bubble);
        this.addReturnValue(id);
    }
    
    private void addBubbleToManager(final int id, final InteractiveBubble bubble) {
        BubbleManager.getInstance().putInInteractiveBubbles(id, bubble);
    }
    
    private void createWidget(final InteractiveBubble bubble) {
        MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer(bubble, 25000);
    }
    
    private void addEndFunctionRunner(final String dialogId) throws LuaException {
        if (this.m_endFunc != null) {
            BubbleManager.getInstance().putInEndFunctionRunners(dialogId.hashCode(), new BubbleClosedListener(this.m_endFunc, this.getScriptObject(), null));
        }
    }
    
    private void setBubbleParams(final InteractiveBubble bubble) {
        bubble.setActAsButton(this.m_actAsButton);
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        sld.setAlign(this.m_align);
        sld.setXOffset(this.m_xoffset);
        sld.setYOffset(this.m_yoffset);
        bubble.add(sld);
        bubble.setVisible(false);
        bubble.setCloseOnClick(this.m_closeOnClick);
        bubble.setForcedDisplaySpark(false);
        bubble.setUseTargetPositionning(false);
    }
    
    private InteractiveBubble createBubble(final String dialogId) {
        return (InteractiveBubble)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("interactiveBubbleDialog2"), this.m_duration, 8256L, (short)30000);
    }
    
    private void getParamsValue(final int paramCount) {
        if (paramCount >= 5) {
            final LuaObject obj = this.getParam(6);
            if (obj.isNumber()) {
                this.m_duration = (int)obj.getNumber();
            }
            else if (obj.isBoolean()) {
                this.m_closeOnClick = obj.getBoolean();
            }
            else if (obj.isString()) {
                this.m_endFunc = obj.getString();
            }
        }
        if (paramCount >= 6) {
            final LuaObject obj = this.getParam(7);
            if (obj.isBoolean()) {
                this.m_closeOnClick = obj.getBoolean();
            }
            else if (obj.isString()) {
                this.m_endFunc = obj.getString();
            }
        }
        if (paramCount >= 7) {
            final LuaObject obj = this.getParam(8);
            if (obj.isString()) {
                this.m_endFunc = obj.getString();
            }
        }
    }
    
    private void initializeParamsToDefaultValue() throws LuaException {
        this.m_align = Alignment17.valueOf(this.getParamString(0));
        this.m_xoffset = this.getParamInt(1);
        this.m_yoffset = this.getParamInt(2);
        this.m_actAsButton = this.getParamBool(3);
        this.m_duration = Integer.MAX_VALUE;
        this.m_closeOnClick = false;
        this.m_endFunc = null;
    }
}
