package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;

public class PartyPlayerDefinitionView extends ImmutableFieldProvider
{
    public static final String BREED = "breed";
    public static final String LEVEL = "level";
    public static final String ROLE = "role";
    public static final String NAME = "name";
    public static final String ICON_URL = "iconUrl";
    private final PartyPlayerDefinition m_definition;
    private final PartyRoleView m_partyRoleView;
    private final BreedInfo m_breed;
    
    public PartyPlayerDefinitionView(final PartyMemberInterface member) {
        this(createDefinition(member));
    }
    
    private static PartyPlayerDefinition createDefinition(final PartyMemberInterface member) {
        final PartyPlayerDefinition definition = new PartyPlayerDefinition(member.getCharacterId(), PartySearchConstants.DEFAULT_PARTY_ROLE, member.getBreedId(), member.getLevel());
        if (member.isCompanion()) {
            definition.setCompanion(true);
        }
        definition.setName(member.getName());
        return definition;
    }
    
    public PartyPlayerDefinitionView(final PartyPlayerDefinition definition) {
        super();
        this.m_definition = definition;
        this.m_partyRoleView = PartyRoleView.getView(definition.getRole());
        this.m_breed = new BreedInfo(getBreed(definition.getBreedId()));
    }
    
    private static Breed getBreed(final short breedId) {
        final AvatarBreed breed = PlayerCharacterBreedManager.getInstance().getBreedFromId(breedId);
        if (breed != null) {
            return breed;
        }
        return MonsterBreedManager.getInstance().getBreedFromId(breedId);
    }
    
    @Override
    public String[] getFields() {
        return PartyPlayerDefinitionView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("breed")) {
            return this.m_breed;
        }
        if (fieldName.equals("level")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_definition.getLevel());
        }
        if (fieldName.equals("role")) {
            return this.m_partyRoleView;
        }
        if (fieldName.equals("name")) {
            return this.m_definition.getName();
        }
        return null;
    }
    
    public PartyPlayerDefinition getDefinition() {
        return this.m_definition;
    }
    
    public BreedInfo getBreed() {
        return this.m_breed;
    }
    
    public short getLevel() {
        return this.m_definition.getLevel();
    }
    
    public PartyRole getRole() {
        return this.m_definition.getRole();
    }
    
    public void setRole(final PartyRoleView role) {
        this.m_definition.setRole(role.getRole());
    }
    
    public long getId() {
        return this.m_definition.getId();
    }
    
    @Override
    public String toString() {
        return "PlayerPartyDefinitionView{m_definition=" + this.m_definition + '}';
    }
    
    public PartyRoleView getRoleView() {
        return this.m_partyRoleView;
    }
}
