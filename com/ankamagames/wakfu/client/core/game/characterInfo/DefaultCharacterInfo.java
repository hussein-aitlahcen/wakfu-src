package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import org.jetbrains.annotations.*;

public class DefaultCharacterInfo extends CharacterInfo
{
    public static final CharacterInfo m_staticInstance;
    private final PlayerCharacterSerializer m_serializer;
    
    public static CharacterInfo getStaticInstance() {
        return DefaultCharacterInfo.m_staticInstance;
    }
    
    private DefaultCharacterInfo() {
        super();
        this.m_serializer = new PlayerCharacterSerializer();
        this.initializeSerializer();
    }
    
    @Override
    public void initialiseCharacteristicsToBaseValue() {
    }
    
    @Override
    public boolean returnDefaultAIControl() {
        return false;
    }
    
    @Override
    public int getKamasCount() {
        return 0;
    }
    
    @Override
    public int substractKamas(final int kamas) {
        return 0;
    }
    
    @Override
    public int addKamas(final int kamas) {
        return 0;
    }
    
    @Override
    public CharacterSerializer getSerializer() {
        return this.m_serializer;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_serializer.clear();
    }
    
    @Override
    protected String getSmileyGfxFileName() {
        return "Smiley_" + this.getBreedId() + this.getSex();
    }
    
    @Override
    protected String getGfxPathKey() {
        return "playerGfxPath";
    }
    
    @Override
    protected Breed defaultBreed() {
        return AvatarBreed.NONE;
    }
    
    @Override
    protected byte defaultCharacterType() {
        return 0;
    }
    
    @Override
    public float getWakfuGaugeValue() {
        return 0.0f;
    }
    
    @NotNull
    @Override
    public SkillCharacteristics getSkillCharacteristics() {
        return EmptySkillCharacteristics.INSTANCE;
    }
    
    @Override
    public byte getActorTypeId() {
        return 0;
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public void addFightCharacteristicsListeners() {
    }
    
    @Override
    public void removeFightCharacteristicsListeners() {
    }
    
    static {
        m_staticInstance = new DefaultCharacterInfo();
    }
}
