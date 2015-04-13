package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.lua.ui.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class UIDisplayButtonFrame implements MessageFrame
{
    public static final UIDisplayButtonFrame INSTANCE;
    @NotNull
    private String m_tradKey;
    private LuaScript m_script;
    private String m_callFunction;
    private boolean m_callbackOnEscape;
    private final DisplayButtonView m_displayButtonView;
    private final DialogUnloadListener m_dialogUnloadListener;
    
    public UIDisplayButtonFrame() {
        super();
        this.m_tradKey = "scenario.endKinematic";
        this.m_callbackOnEscape = true;
        this.m_displayButtonView = new DisplayButtonView(this.m_tradKey);
        this.m_dialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if ("displayButtonDialog".equals(id)) {
                    WakfuGameEntity.getInstance().removeFrame(UIDisplayButtonFrame.INSTANCE);
                }
            }
        };
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("displayButton", this.m_displayButtonView);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("displayButtonDialog", Dialogs.getDialogPath("displayButtonDialog"), 139280L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.displayButton", DisplayButtonDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            if (Xulor.getInstance().isLoaded("displayButtonDialog")) {
                Xulor.getInstance().unload("displayButtonDialog");
            }
            PropertiesProvider.getInstance().removeProperty("displayButton");
            Xulor.getInstance().removeActionClass("wakfu.displayButton");
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setTradKey(@NotNull final String tradKey) {
        this.m_tradKey = tradKey;
    }
    
    public void setScript(final LuaScript script) {
        this.m_script = script;
    }
    
    public void setCallFunction(final String callFunction) {
        this.m_callFunction = callFunction;
    }
    
    public void setCallbackOnEscape(final boolean callbackOnEscape) {
        this.m_callbackOnEscape = callbackOnEscape;
    }
    
    public boolean isCallbackOnEscape() {
        return this.m_callbackOnEscape;
    }
    
    public void callFunction() {
        this.m_script.runFunction(this.m_callFunction);
        WakfuGameEntity.getInstance().removeFrame(this);
    }
    
    static {
        INSTANCE = new UIDisplayButtonFrame();
    }
}
