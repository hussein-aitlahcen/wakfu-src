package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.*;
import org.keplerproject.luajava.*;

final class Prepare extends JavaFunctionEx
{
    private static final Logger m_logger;
    private boolean m_actAsButton;
    private int m_duration;
    private boolean m_closeOnClick;
    private String m_endFunc;
    
    Prepare(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "prepare";
    }
    
    @Override
    public String getDescription() {
        return "Pr?pare une bulle de texte (avec interaction de l'utilisateur) associe ? un mobile.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile sur lequel attacher la bulle", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actAsButton", "La bulle se comportera comme la somme des boutons qui lui seront attach?s par la suite", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("duration", "Dur?e d'affichage de la bulle", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("closeOnClick", "Ferme si on clique sur la bulle", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("onEndFunc", "Fonction a appeler ? la fermeture de la fenetre", LuaScriptParameterType.STRING, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle cr??e", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final Mobile mobile = MobileManager.getInstance().getMobile(this.getParamLong(0));
        if (mobile == null) {
            this.writeError(Prepare.m_logger, "Le mobile n'existe pas " + this.getParamLong(0));
            this.addReturnNilValue();
            return;
        }
        this.initializeParamsWithDefaultValue();
        this.getParamsValue(paramCount);
        final int bubbleId = AdviserManager.getInstance().getNewUniqueId();
        final String dialogId = "interactiveBubbleDialog" + bubbleId;
        final InteractiveBubble bubble = this.createBubble(dialogId);
        this.addEndFuncRunner(dialogId);
        createWidget(bubble);
        this.setBubbleParams(mobile, bubble);
        addBubbleToManager(bubbleId, bubble);
        this.addReturnValue(bubbleId);
    }
    
    private void addEndFuncRunner(final String dialogId) throws LuaException {
        if (this.m_endFunc != null) {
            BubbleManager.getInstance().putInEndFunctionRunners(dialogId.hashCode(), new BubbleClosedListener(this.m_endFunc, this.getScriptObject(), null));
        }
    }
    
    private InteractiveBubble createBubble(final String dialogId) {
        return (InteractiveBubble)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("interactiveBubbleDialog"), this.m_duration, 8256L, (short)30000);
    }
    
    private static void addBubbleToManager(final int id, final InteractiveBubble bubble) {
        BubbleManager.getInstance().putInInteractiveBubbles(id, bubble);
    }
    
    private static void createWidget(final InteractiveBubble bubble) {
        MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer(bubble, 25000);
    }
    
    private void setBubbleParams(final Mobile mobile, final InteractiveBubble bubble) {
        bubble.setTarget(mobile);
        bubble.setActAsButton(this.m_actAsButton);
        bubble.setVisible(false);
        bubble.setCloseOnClick(this.m_closeOnClick);
    }
    
    private void initializeParamsWithDefaultValue() throws LuaException {
        this.m_duration = Integer.MAX_VALUE;
        this.m_actAsButton = this.getParamBool(1);
        this.m_closeOnClick = false;
        this.m_endFunc = null;
    }
    
    private void getParamsValue(final int paramCount) {
        if (paramCount >= 3) {
            final LuaObject obj = this.getParam(4);
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
        if (paramCount >= 4) {
            final LuaObject obj = this.getParam(5);
            if (obj.isBoolean()) {
                this.m_closeOnClick = obj.getBoolean();
            }
            else if (obj.isString()) {
                this.m_endFunc = obj.getString();
            }
        }
        if (paramCount >= 5) {
            final LuaObject obj = this.getParam(6);
            if (obj.isString()) {
                this.m_endFunc = obj.getString();
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Prepare.class);
    }
}
