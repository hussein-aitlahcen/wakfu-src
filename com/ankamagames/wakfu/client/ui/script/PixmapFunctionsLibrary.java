package com.ankamagames.wakfu.client.ui.script;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.keplerproject.luajava.*;

public class PixmapFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final PixmapFunctionsLibrary m_instance;
    
    public static PixmapFunctionsLibrary getInstance() {
        return PixmapFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Pixmap";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetItemPath(luaState) };
    }
    
    static {
        m_instance = new PixmapFunctionsLibrary();
    }
    
    private static class GetItemPath extends JavaFunctionEx
    {
        private GetItemPath(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getItemPath";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("path", null, LuaScriptParameterType.STRING, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(this.getParamInt(0));
            final String path = WakfuConfiguration.getInstance().getItemBigIconUrl(item.getGfxId());
            this.addReturnValue(path);
        }
    }
}
