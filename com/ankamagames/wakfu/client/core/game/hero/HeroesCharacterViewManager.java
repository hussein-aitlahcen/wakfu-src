package com.ankamagames.wakfu.client.core.game.hero;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;

public class HeroesCharacterViewManager
{
    private final TLongObjectHashMap<CharacterView> m_views;
    public static final HeroesCharacterViewManager INSTANCE;
    
    public HeroesCharacterViewManager() {
        super();
        this.m_views = new TLongObjectHashMap<CharacterView>();
    }
    
    public CharacterView getOrCreateCharacterView(final PlayerCompanionViewShort shortView) {
        if (shortView == null) {
            return null;
        }
        final long heroId = shortView.getId();
        CharacterView view = this.m_views.get(heroId);
        if (view == null) {
            view = new CharacterView(HeroesManager.INSTANCE.getHero(heroId), shortView);
        }
        return view;
    }
    
    public CharacterView getOrCreateCharacterView(final PlayerCharacter character) {
        if (character == null) {
            return null;
        }
        final long heroId = character.getId();
        CharacterView view = this.m_views.get(heroId);
        if (view == null) {
            view = new CharacterView(HeroesManager.INSTANCE.getHero(heroId), new PlayerCompanionViewShort(character));
        }
        return view;
    }
    
    public TLongObjectHashMap<CharacterView> getViews() {
        return this.m_views;
    }
    
    static {
        INSTANCE = new HeroesCharacterViewManager();
    }
}
