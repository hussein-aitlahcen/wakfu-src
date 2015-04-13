package com.ankamagames.wakfu.client.core.game.hero;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class HeroesManagerClientListener implements HeroesManagerListener<PlayerCharacter>
{
    private static final long OWNER_ID;
    
    @Override
    public void heroAdded(final PlayerCharacter info) {
    }
    
    @Override
    public void heroAddedToParty(final PlayerCharacter info) {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED)) {
            return;
        }
        updateHeroesPartyList();
        UICompanionsEmbeddedFrame.addCharacterView(HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(info));
    }
    
    @Override
    public void heroRemovedFromParty(final PlayerCharacter info) {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED)) {
            return;
        }
        updateHeroesPartyList();
        UICompanionsEmbeddedFrame.removeCharacterView(info.getId());
    }
    
    @Override
    public void heroRemoved(final PlayerCharacter info) {
    }
    
    private static void updateHeroesPartyList() {
        final long local = WakfuGameEntity.getInstance().getLocalPlayer().getId();
        final List<PlayerCharacter> heroes = new ArrayList<PlayerCharacter>();
        final TLongIterator it = HeroesManager.INSTANCE.getHeroesInParty(HeroesManagerClientListener.OWNER_ID).iterator();
        while (it.hasNext()) {
            final PlayerCharacter hero = HeroesManager.INSTANCE.getHero(it.next());
            if (hero.getId() != local) {
                heroes.add(hero);
            }
            else {
                heroes.add(0, hero);
            }
        }
        UICompanionsManagementFrame.INSTANCE.reflowHeroesList();
        PropertiesProvider.getInstance().setPropertyValue("heroesParty", heroes);
        PropertiesProvider.getInstance().setPropertyValue("heroesPartyIsFull", !ClientHeroUtils.canInviteHeroOrCompanion((byte)0));
        PropertiesProvider.getInstance().setPropertyValue("companionPartyListFull", !ClientHeroUtils.canInviteHeroOrCompanion((byte)5));
    }
    
    static {
        OWNER_ID = WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId();
    }
}
