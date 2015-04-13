package com.ankamagames.wakfu.client.ui.mru;

import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import java.awt.*;

public interface MRUable
{
    AbstractMRUAction[] getMRUActions();
    
    boolean isMRUPositionable();
    
    Point getMRUScreenPosition();
    
    short getMRUHeight();
}
