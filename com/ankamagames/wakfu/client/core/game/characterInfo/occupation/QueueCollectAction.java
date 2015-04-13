package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public interface QueueCollectAction extends VisibleChangedListener
{
    boolean isRunnable();
    
    boolean isEnabled();
    
    void run(boolean p0);
    
    CollectAction getCollectAction();
    
    AnimatedElementWithDirection getCollectedRessource();
    
    void addParticle();
    
    void removeParticle();
}
