package com.ankamagames.wakfu.client.core.criteria;

import com.ankamagames.wakfu.common.game.ai.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.world.climate.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class WakfuCriteriaInformationProvider extends AbstractWakfuCriteriaInformationProvider
{
    @Override
    public boolean isDay(final BasicCharacterInfo character) {
        return false;
    }
    
    @Override
    public StackInventory<AbstractSpellLevel, RawSpellLevel> getSpells(final BasicCharacterInfo character) {
        return null;
    }
    
    @Override
    public AvatarBreed getBreed(final BasicCharacterInfo character) {
        return (AvatarBreed)character.getBreed();
    }
    
    @Override
    public int getDate() {
        if (!WakfuGameCalendar.getInstance().isSynchronized()) {
            return 0;
        }
        final GameDateConst date = WakfuGameCalendar.getInstance().getDate();
        return date.getYear() * 10000 + date.getMonth() * 100 + date.getDay();
    }
    
    @Override
    public int getHour() {
        if (!WakfuGameCalendar.getInstance().isSynchronized()) {
            return 0;
        }
        final GameDateConst date = WakfuGameCalendar.getInstance().getDate();
        return date.getHours() * 100 + date.getMinutes();
    }
    
    @Override
    public Point3 getPosition(final BasicCharacterInfo character) {
        return character.getPosition();
    }
    
    @Override
    public int getMonsterCountInFight(final BasicCharacterInfo character, final long breedId) {
        return 0;
    }
    
    @Override
    public byte getSex(final BasicCharacterInfo character) {
        return character.getSex();
    }
    
    @Override
    public ArrayInventory<Item, RawInventoryItem> getEquipment(final BasicCharacterInfo character) {
        return null;
    }
    
    @Override
    public ClimatZoneCloudType getWeather(final BasicCharacterInfo character) {
        return ClimatZoneCloudType.CLOUDLESS;
    }
    
    @Override
    public int getMonsterCountInArea(final BasicCharacterInfo character, final long breedId) {
        return 15;
    }
    
    @Override
    public int getMonsterCountInMonsterArea(final BasicCharacterInfo character, final long breedId) {
        return 0;
    }
    
    @Override
    public int getResourceCountInArea(final BasicCharacterInfo character, final long resourceId) {
        return 15;
    }
    
    @Override
    public int getResouceCountInResourceArea(final BasicCharacterInfo character, final long resourceId) {
        return 0;
    }
    
    @Override
    public AbstractBagContainer getInventories(final BasicCharacterInfo character) {
        return null;
    }
    
    @Override
    public BasicCharacterInfo getCharacterInfo(final long characterId) {
        return CharacterInfoManager.getInstance().getCharacter(characterId);
    }
}
