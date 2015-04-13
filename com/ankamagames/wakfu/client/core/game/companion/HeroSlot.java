package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.configuration.*;

class HeroSlot extends ImmutableFieldProvider
{
    public static final String HERO = "hero";
    public static final String IS_UPGRADED = "isUpgraded";
    public static final String AEV_ID = "aevId";
    private final byte m_index;
    private CharacterView m_hero;
    private boolean m_upgraded;
    
    public HeroSlot(final byte index) {
        super();
        this.m_index = index;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("hero")) {
            return this.m_hero;
        }
        if (fieldName.equals("isUpgraded")) {
            return this.isUpgraded();
        }
        if (fieldName.equals("aevId")) {
            return this.getAevId();
        }
        return null;
    }
    
    public String getAevId() {
        return "heroAEV" + this.m_index;
    }
    
    public CharacterView getHero() {
        return this.m_hero;
    }
    
    public void setHero(final CharacterView hero) {
        this.m_hero = hero;
    }
    
    public boolean isUpgraded() {
        return this.m_upgraded || SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED);
    }
    
    public void setUpgraded(final boolean upgraded) {
        this.m_upgraded = upgraded;
    }
    
    @Override
    public String toString() {
        return "HeroSlot{m_hero=" + this.m_hero + ", m_upgraded=" + this.m_upgraded + '}';
    }
}
