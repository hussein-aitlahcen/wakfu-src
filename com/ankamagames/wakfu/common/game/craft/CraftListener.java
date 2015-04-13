package com.ankamagames.wakfu.common.game.craft;

import com.ankamagames.wakfu.common.game.craft.reference.*;

public interface CraftListener
{
    void onCraftLearned(ReferenceCraft p0);
    
    void onCraftXpGained(int p0, long p1);
    
    void onRecipeLearned(int p0, int p1);
}
