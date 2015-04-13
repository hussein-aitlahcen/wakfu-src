package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.game.item.*;

public class CharacterView extends ImmutableFieldProvider
{
    public static final String IS_COMPANION = "isCompanion";
    public static final String COMPANION_VIEW = "companionView";
    public static final String ADD_REMOVE_PARTY_ENABLED = "addRemovePartyEnabled";
    public static final String IS_IN_PARTY_FIELD = "isInParty";
    protected final CharacterInfo m_characterInfo;
    protected final ShortCharacterView m_shortCharacterView;
    
    public CharacterView(final CharacterInfo characterInfo, final ShortCharacterView shortCharacterView) {
        super();
        this.m_characterInfo = characterInfo;
        this.m_shortCharacterView = shortCharacterView;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isCompanion")) {
            return this.isCompanion();
        }
        if (fieldName.equals("companionView")) {
            return this.m_shortCharacterView;
        }
        if (fieldName.equals("addRemovePartyEnabled")) {
            return this.addRemovePartyEnabled();
        }
        if (fieldName.equals("isInParty")) {
            return this.isInParty();
        }
        return this.getCharacterInfo().getFieldValue(fieldName);
    }
    
    protected boolean isInParty() {
        final PartyComportment partyComportment = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment();
        return partyComportment.isInParty() && partyComportment.getParty().contains(this.m_characterInfo.getId());
    }
    
    protected boolean addRemovePartyEnabled() {
        if (this.isInParty()) {
            return true;
        }
        final boolean listFull = PropertiesProvider.getInstance().getBooleanProperty("companionPartyListFull");
        if (listFull) {
            return false;
        }
        final boolean isInDungeon = PropertiesProvider.getInstance().getBooleanProperty("isInDungeon");
        return !isInDungeon && (this.isCompanion() || HeroesDisplayer.INSTANCE.hasFreeUpgradedSlot() || SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED));
    }
    
    public boolean isCompanion() {
        return false;
    }
    
    public CharacterInfo getCharacterInfo() {
        return this.m_characterInfo;
    }
    
    public ItemEquipment getItemEquipment() {
        return this.m_characterInfo.getEquipmentInventory();
    }
    
    public ShortCharacterView getShortCharacterView() {
        return this.m_shortCharacterView;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CharacterView && this.m_characterInfo.getId() == ((CharacterView)obj).getCharacterInfo().getId();
    }
    
    @Override
    public String toString() {
        return "CharacterView{m_characterInfo=" + this.m_characterInfo + ", m_shortCharacterView=" + this.m_shortCharacterView + '}';
    }
}
