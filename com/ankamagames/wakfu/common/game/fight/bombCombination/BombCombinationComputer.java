package com.ankamagames.wakfu.common.game.fight.bombCombination;

import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;

public interface BombCombinationComputer extends BombPositionChangedListener
{
    public static final int MINIMUM_COMBINATION_SIZE_TO_BE_ACTIVE = 3;
    
    void computeSpecialZoneAndExecuteEffectsIfNecessary(AbstractBombEffectArea p0);
    
    void addBombToCombinationAndNotifyListener(AbstractBombEffectArea p0);
    
    void removeBombFromCombinationAndNotifyListener(AbstractBombEffectArea p0);
    
    List<List<AbstractBombEffectArea>> getBombCombinations();
    
    void setListener(BombCombinationModificationListener p0);
}
