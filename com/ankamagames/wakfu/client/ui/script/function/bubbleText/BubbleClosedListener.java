package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;

final class BubbleClosedListener
{
    private final String m_func;
    private final LuaScript m_script;
    private final LuaValue[] m_params;
    
    BubbleClosedListener(final String func, final LuaScript script, final LuaValue[] params) {
        super();
        assert script != null : "BubbleClosedListener avec un script null";
        assert func != null : "BubbleClosedListener avec une fonction nulle";
        this.m_func = func;
        this.m_params = params;
        this.m_script = script;
    }
    
    public void onClose() {
        this.m_script.runFunction(this.m_func, this.m_params, new LuaTable[0]);
    }
}
