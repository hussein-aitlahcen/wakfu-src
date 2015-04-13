package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;

public interface IMetaItem<AEG extends AbstractEffectGroup>
{
    int getId();
    
    Iterator<AEG> variableEffectsIterator();
    
    int[] getSubIds();
}
