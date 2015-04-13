package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import org.jetbrains.annotations.*;

public class CharacterCreationDefinition extends ImmutableFieldProvider
{
    public static final String BREED_INFO_FIELD = "breedInfo";
    public static final String ENABLED = "enabled";
    private final AvatarBreed m_breed;
    private boolean m_enabled;
    private int m_sortOrder;
    
    public CharacterCreationDefinition(final AvatarBreed breed) {
        super();
        this.m_breed = breed;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setSortOrder(final int sortOrder) {
        this.m_sortOrder = sortOrder;
    }
    
    public int getSortOrder() {
        return this.m_sortOrder;
    }
    
    @Override
    public String[] getFields() {
        return CharacterCreationDefinition.NO_FIELDS;
    }
    
    public AvatarBreed getBreed() {
        return this.m_breed;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("breedInfo")) {
            return AvatarBreedInfoManager.getInstance().getBreedInfo(this.m_breed.getBreedId());
        }
        if (fieldName.equals("enabled")) {
            return this.m_enabled;
        }
        return null;
    }
}
