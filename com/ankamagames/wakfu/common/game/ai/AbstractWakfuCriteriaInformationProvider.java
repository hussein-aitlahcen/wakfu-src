package com.ankamagames.wakfu.common.game.ai;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.world.climate.*;
import com.ankamagames.wakfu.common.game.item.*;

public abstract class AbstractWakfuCriteriaInformationProvider
{
    protected static AbstractWakfuCriteriaInformationProvider m_instance;
    
    public static AbstractWakfuCriteriaInformationProvider getInstance() {
        return AbstractWakfuCriteriaInformationProvider.m_instance;
    }
    
    public static void registerInstance(final AbstractWakfuCriteriaInformationProvider instance) {
        AbstractWakfuCriteriaInformationProvider.m_instance = instance;
    }
    
    public abstract boolean isDay(final BasicCharacterInfo p0);
    
    public abstract StackInventory<AbstractSpellLevel, RawSpellLevel> getSpells(final BasicCharacterInfo p0);
    
    public abstract AvatarBreed getBreed(final BasicCharacterInfo p0);
    
    public abstract int getDate();
    
    public abstract int getHour();
    
    public abstract Point3 getPosition(final BasicCharacterInfo p0);
    
    public abstract int getMonsterCountInFight(final BasicCharacterInfo p0, final long p1);
    
    public abstract byte getSex(final BasicCharacterInfo p0);
    
    public abstract ArrayInventory<Item, RawInventoryItem> getEquipment(final BasicCharacterInfo p0);
    
    public abstract ClimatZoneCloudType getWeather(final BasicCharacterInfo p0);
    
    public abstract int getMonsterCountInArea(final BasicCharacterInfo p0, final long p1);
    
    public abstract int getMonsterCountInMonsterArea(final BasicCharacterInfo p0, final long p1);
    
    public abstract int getResourceCountInArea(final BasicCharacterInfo p0, final long p1);
    
    public abstract int getResouceCountInResourceArea(final BasicCharacterInfo p0, final long p1);
    
    public abstract AbstractBagContainer getInventories(final BasicCharacterInfo p0);
    
    public abstract BasicCharacterInfo getCharacterInfo(final long p0);
    
    static {
        AbstractWakfuCriteriaInformationProvider.m_instance = null;
    }
}
