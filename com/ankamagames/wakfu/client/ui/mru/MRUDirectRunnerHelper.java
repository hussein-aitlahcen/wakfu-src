package com.ankamagames.wakfu.client.ui.mru;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;

public class MRUDirectRunnerHelper
{
    public static void attack() {
        MRUDirectRunnerManager.INSTANCE.start(MRUCastFightAction.class, CursorFactory.CursorType.CUSTOM16, CursorFactory.CursorType.CUSTOM17);
    }
}
