package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.lights.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public interface FightInfo extends BasicFightInfo<CharacterInfo>
{
    InOutCellLightModifier getCellLightModifier();
    
    void endFight();
    
    Collection<CharacterInfo> getFightersNotInTeam(byte p0);
    
    Collection<CharacterInfo> getFightersInTeam(byte p0);
    
    CharacterInfo getFighterFromId(long p0);
    
    boolean shouldDisplayAreaForLocalPlayer(BasicEffectArea p0);
}
