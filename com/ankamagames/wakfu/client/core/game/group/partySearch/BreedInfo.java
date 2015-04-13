package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class BreedInfo extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NAME = "name";
    public static final String ICON_URL = "iconUrl";
    private final Breed m_breed;
    
    protected BreedInfo(final Breed breed) {
        super();
        this.m_breed = breed;
    }
    
    @Override
    public String[] getFields() {
        return BreedInfo.NO_FIELDS;
    }
    
    public Breed getBreed() {
        return this.m_breed;
    }
    
    public String getName() {
        if (AvatarBreed.getBreedFromId(this.m_breed.getBreedId()) != AvatarBreed.NONE) {
            return WakfuTranslator.getInstance().getString("breed." + this.m_breed.getBreedId());
        }
        if (this.m_breed.getBreedId() == AvatarBreed.NONE.getBreedId()) {
            return WakfuTranslator.getInstance().getString("allBreeds");
        }
        return ((MonsterBreed)this.m_breed).getName();
    }
    
    public String getIconUrl() {
        if (AvatarBreed.getBreedFromId(this.m_breed.getBreedId()) != AvatarBreed.NONE) {
            return WakfuConfiguration.getInstance().getIconUrl("breedPortraitIllustrationPath", "defaultIconPath", this.m_breed.getBreedId() + "0");
        }
        if (this.m_breed.getBreedId() == AvatarBreed.NONE.getBreedId()) {
            return null;
        }
        return WakfuConfiguration.getInstance().getIconUrl("companionIconsPath", "defaultIconPath", this.m_breed.getBreedId());
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("iconUrl")) {
            return this.getIconUrl();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "BreedInfo{m_breed=" + this.m_breed + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)BreedInfo.class);
    }
}
