package com.ankamagames.wakfu.client.core.game.hero;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.*;

public class HeroManagerControllerClient extends HeroManagerController<PlayerCharacter>
{
    public HeroManagerControllerClient(final long clientId) {
        super(clientId);
    }
    
    @Override
    public void addHero(final PlayerCharacter info) throws HeroException {
        super.addHero(info);
    }
    
    @Override
    public void addToParty(final PlayerCharacter info) throws HeroException {
        super.addToParty(info);
    }
}
