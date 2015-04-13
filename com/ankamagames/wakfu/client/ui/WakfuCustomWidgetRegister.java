package com.ankamagames.wakfu.client.ui;

import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.ui.component.appearance.*;

public class WakfuCustomWidgetRegister
{
    public static void registerCustom() {
        final XulorTagLibrary tagLibrary = XulorTagLibrary.getInstance();
        tagLibrary.registerTag("interactiveBubble", InteractiveBubble.class);
        tagLibrary.registerTag("InteractiveBubbleAppearance", InteractiveBubbleAppearance.class);
        tagLibrary.registerTag("timeline", TimelineWidget.class);
        tagLibrary.registerTag("WorldEditor", WorldEditor.class);
        tagLibrary.registerTag("timePointBar", TimePointBarWidget.class);
        tagLibrary.registerTag("timePointBarDecorator", TimePointBarDecorator.class);
    }
}
