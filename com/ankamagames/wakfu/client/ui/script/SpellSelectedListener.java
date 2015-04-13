package com.ankamagames.wakfu.client.ui.script;

import com.ankamagames.framework.script.*;

public class SpellSelectedListener extends LuaCallback
{
    private final boolean m_blockingSelection;
    
    public SpellSelectedListener(final LuaScript script, final String funcName, final LuaValue[] params, final boolean blockingSelection) {
        super(script, funcName, params);
        this.m_blockingSelection = blockingSelection;
    }
    
    public boolean isBlockingSelection() {
        return this.m_blockingSelection;
    }
    
    @Override
    public boolean run() {
        return super.run();
    }
}
