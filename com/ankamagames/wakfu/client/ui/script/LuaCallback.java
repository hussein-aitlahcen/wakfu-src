package com.ankamagames.wakfu.client.ui.script;

import com.ankamagames.framework.script.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.event.*;

public class LuaCallback implements EventListener
{
    private final LuaScript m_script;
    private final String m_funcName;
    private LuaValue[] m_params;
    
    public LuaCallback(final LuaScript script, final String funcName, final LuaValue[] params) {
        super();
        this.m_script = script;
        this.m_funcName = funcName;
        this.m_params = params;
    }
    
    LuaCallback(final LuaScript script, final String funcName) {
        super();
        this.m_script = script;
        this.m_funcName = funcName;
        this.m_params = null;
    }
    
    @Override
    public boolean run(final Event event) {
        this.m_script.runFunction(this.m_funcName, this.m_params, this.convertEventForLua(event));
        return false;
    }
    
    public boolean run() {
        this.m_script.runFunction(this.m_funcName, this.m_params, new LuaTable[0]);
        return false;
    }
    
    public void setParams(final LuaValue[] params) {
        this.m_params = params;
    }
    
    private LuaTable convertEventForLua(final Event event) {
        switch (event.getType()) {
            case MOUSE_CLICKED:
            case MOUSE_DOUBLE_CLICKED:
            case MOUSE_RELEASED:
            case MOUSE_PRESSED:
            case MOUSE_MOVED:
            case MOUSE_WHEELED:
            case MOUSE_ENTERED:
            case MOUSE_EXITED: {
                final LuaTable luaEvent = new LuaTable("event");
                final MouseEvent e = (MouseEvent)event;
                luaEvent.addField("button", e.getButton());
                luaEvent.addField("x", e.getScreenX());
                luaEvent.addField("y", e.getScreenY());
                luaEvent.addField("rotation", e.getRotations());
                return luaEvent;
            }
            case ACTIVATION_CHANGED: {
                final LuaTable luaEvent = new LuaTable("event");
                final ActivationChangedEvent e2 = (ActivationChangedEvent)event;
                return luaEvent;
            }
            case COLOR_CHANGED: {
                final LuaTable luaEvent = new LuaTable("event");
                final ColorChangedEvent e3 = (ColorChangedEvent)event;
                final Color color = e3.getColorElement().getColor();
                luaEvent.addField("r", color.getRed());
                luaEvent.addField("g", color.getGreen());
                luaEvent.addField("b", color.getBlue());
                luaEvent.addField("a", color.getAlpha());
                return luaEvent;
            }
            case SLIDER_MOVED: {
                final LuaTable luaEvent = new LuaTable("event");
                final SliderMovedEvent e4 = (SliderMovedEvent)event;
                luaEvent.addField("value", e4.getValue());
                return luaEvent;
            }
            case KEY_PRESSED:
            case KEY_RELEASED:
            case KEY_TYPED: {
                final LuaTable luaEvent = new LuaTable("event");
                final KeyEvent e5 = (KeyEvent)event;
                luaEvent.addField("keyChar", e5.getKeyChar());
                luaEvent.addField("keyCode", e5.getKeyCode());
                luaEvent.addField("modifiers", e5.getModifiers());
                return luaEvent;
            }
            default: {
                return null;
            }
        }
    }
}
