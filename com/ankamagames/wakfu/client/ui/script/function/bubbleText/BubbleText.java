package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class BubbleText extends JavaFunctionsLibrary
{
    private static final BubbleText m_instance;
    
    private BubbleText() {
        super();
        Xulor.getInstance().addDialogUnloadListener(new BubbleTextDialogUnloadListener());
        AdviserManager.getInstance().addAdviserObserver(new BubbleTextAdviserObserver());
    }
    
    public static BubbleText getInstance() {
        return BubbleText.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new ShowText(luaState), new Prepare(luaState), new PrepareFloating(luaState), new SetText(luaState), new ChangeToUpperLayer(luaState), new SetOffset(luaState), new AddButton(luaState), new AddTimedButton(luaState), new ShowMonologue(luaState), new Show(luaState), new Close(luaState), new CloseBubbleAttachedTo(luaState), new SetScreenPosition(luaState), new AttachToMobile(luaState), new PrepareWakfuBubble(luaState), new SetCharacterSpeak(luaState), new RemoveOnClose(luaState), new SetShakingMode(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    public void clear() {
        BubbleManager.getInstance().clear();
    }
    
    @Override
    public final String getName() {
        return "BubbleText";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new BubbleText();
    }
}
