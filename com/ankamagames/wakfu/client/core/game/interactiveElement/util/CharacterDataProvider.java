package com.ankamagames.wakfu.client.core.game.interactiveElement.util;

import com.ankamagames.wakfu.common.game.characterInfo.*;

public interface CharacterDataProvider
{
    AbstractCharacterData getCharacterData();
    
    CharacterStatueProvider getStatueProvider();
}
