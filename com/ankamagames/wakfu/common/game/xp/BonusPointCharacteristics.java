package com.ankamagames.wakfu.common.game.xp;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import gnu.trove.*;

public class BonusPointCharacteristics implements Poolable, RawConvertible<RawBonusPointCharacteristics>
{
    private static final Logger m_logger;
    public static final short BONUS_POINT_BY_LEVEL = 5;
    private static final ObjectPool m_staticPool;
    private TByteShortHashMap m_characteristicBonusPoint;
    private TByteShortHashMap m_xpBonusPoints;
    private short m_freePoint;
    
    public BonusPointCharacteristics() {
        super();
        this.m_characteristicBonusPoint = new TByteShortHashMap();
        this.m_xpBonusPoints = new TByteShortHashMap();
    }
    
    public final void release() {
        try {
            BonusPointCharacteristics.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            BonusPointCharacteristics.m_logger.error((Object)"Exception lors du returnObject d'un BonusPointCharacteristics. Impossible d'apr\u00e8s les javadoc des apache Pools");
        }
    }
    
    public static BonusPointCharacteristics checkOut() {
        try {
            final BonusPointCharacteristics xpBonusPointCharacteristics = (BonusPointCharacteristics)BonusPointCharacteristics.m_staticPool.borrowObject();
            return xpBonusPointCharacteristics;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut sur un BonusPointCharacteristics : ", e);
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_xpBonusPoints.put(FighterCharacteristicType.AIR_MASTERY.getId(), (short)0);
        this.m_xpBonusPoints.put(FighterCharacteristicType.VITALITY.getId(), (short)0);
        this.m_xpBonusPoints.put(FighterCharacteristicType.FIRE_MASTERY.getId(), (short)0);
        this.m_xpBonusPoints.put(FighterCharacteristicType.EARTH_MASTERY.getId(), (short)0);
        this.m_xpBonusPoints.put(FighterCharacteristicType.WATER_MASTERY.getId(), (short)0);
        this.m_xpBonusPoints.put(FighterCharacteristicType.WISDOM.getId(), (short)0);
        this.m_characteristicBonusPoint.put(FighterCharacteristicType.AIR_MASTERY.getId(), (short)0);
        this.m_characteristicBonusPoint.put(FighterCharacteristicType.VITALITY.getId(), (short)0);
        this.m_characteristicBonusPoint.put(FighterCharacteristicType.FIRE_MASTERY.getId(), (short)0);
        this.m_characteristicBonusPoint.put(FighterCharacteristicType.EARTH_MASTERY.getId(), (short)0);
        this.m_characteristicBonusPoint.put(FighterCharacteristicType.WATER_MASTERY.getId(), (short)0);
        this.m_characteristicBonusPoint.put(FighterCharacteristicType.WISDOM.getId(), (short)0);
    }
    
    @Override
    public void onCheckIn() {
        this.m_xpBonusPoints.clear();
        this.m_freePoint = 0;
        this.m_characteristicBonusPoint.clear();
    }
    
    public short getCharacteristicCost(final Breed breed, final byte characId) {
        if (!this.m_xpBonusPoints.containsKey(characId)) {
            return -1;
        }
        final short currentValue = (short)this.computeXpBonusPointToValue(breed, characId);
        return AvatarBonusPointDistributionTables.getInstance().getBonusPointNeededToIncrementCharacteristic(breed.getBreedId(), characId, currentValue);
    }
    
    public short getCharacteristicGain(final Breed breed, final byte characId) {
        if (!this.m_xpBonusPoints.containsKey(characId)) {
            return -1;
        }
        final short currentValue = (short)this.computeXpBonusPointToValue(breed, characId);
        return AvatarBonusPointDistributionTables.getInstance().getCharacteristicPointGainWhenUpgrade(breed.getBreedId(), characId, currentValue);
    }
    
    public XpBonusPointError addXpBonusPoint(final Breed breed, final byte characId) {
        if (!this.m_xpBonusPoints.containsKey(characId)) {
            return XpBonusPointError.CHARAC_NOT_FOUND;
        }
        if (!(breed instanceof AvatarBreed)) {
            return XpBonusPointError.NOT_AVATAR_BREED;
        }
        final short currentValue = (short)this.computeXpBonusPointToValue(breed, characId);
        final short pointNeeded = AvatarBonusPointDistributionTables.getInstance().getBonusPointNeededToIncrementCharacteristic(breed.getBreedId(), characId, currentValue);
        if (pointNeeded > this.m_freePoint) {
            return XpBonusPointError.NOT_ENOUTH_FREEPOINT;
        }
        this.m_freePoint -= pointNeeded;
        this.m_xpBonusPoints.put(characId, (short)(this.m_xpBonusPoints.get(characId) + pointNeeded));
        return XpBonusPointError.NO_ERROR;
    }
    
    public XpBonusPointError resetXpBonusPoint(final Breed breed, final byte characId) {
        if (!this.m_xpBonusPoints.containsKey(characId)) {
            return XpBonusPointError.CHARAC_NOT_FOUND;
        }
        if (!(breed instanceof AvatarBreed)) {
            return XpBonusPointError.NOT_AVATAR_BREED;
        }
        final short totalPoint = this.m_xpBonusPoints.get(characId);
        this.m_xpBonusPoints.put(characId, (short)0);
        this.m_freePoint += totalPoint;
        return XpBonusPointError.NO_ERROR;
    }
    
    public XpBonusPointError resetAllXpBonusPoint(final Breed breed) {
        for (final byte characId : this.m_xpBonusPoints.keys()) {
            final XpBonusPointError error = this.resetXpBonusPoint(breed, characId);
            if (error != XpBonusPointError.NO_ERROR) {
                return error;
            }
        }
        return XpBonusPointError.NO_ERROR;
    }
    
    public int computeXpBonusPointToValue(final Breed breed, final byte characId) {
        if (!this.m_xpBonusPoints.containsKey(characId)) {
            return -1;
        }
        if (!(breed instanceof AvatarBreed)) {
            return -1;
        }
        short value = 0;
        short cost = this.m_xpBonusPoints.get(characId);
        while (cost > 0) {
            final short bonusNeeded = AvatarBonusPointDistributionTables.getInstance().getBonusPointNeededToIncrementCharacteristic(breed.getBreedId(), characId, value);
            final short caracgain = AvatarBonusPointDistributionTables.getInstance().getCharacteristicPointGainWhenUpgrade(breed.getBreedId(), characId, value);
            cost -= bonusNeeded;
            if (cost >= 0) {
                value += caracgain;
            }
        }
        return value;
    }
    
    public void addFreePoint(final short quantity) {
        this.m_freePoint += quantity;
    }
    
    public short getFreePoint() {
        return this.m_freePoint;
    }
    
    public void addCharacteristicsPoint(final byte characId, final short quantity) {
        short value = this.m_characteristicBonusPoint.get(characId);
        value += quantity;
        this.m_characteristicBonusPoint.put(characId, value);
    }
    
    public TByteShortIterator getXpBonusPointIterator() {
        return this.m_xpBonusPoints.iterator();
    }
    
    public TByteShortIterator getCharacteristicBonusPointIterator() {
        return this.m_characteristicBonusPoint.iterator();
    }
    
    @Override
    public boolean toRaw(final RawBonusPointCharacteristics raw) {
        final boolean success = true;
        raw.clear();
        final TByteShortIterator it1 = this.m_xpBonusPoints.iterator();
        while (it1.hasNext()) {
            it1.advance();
            final RawBonusPointCharacteristics.XpBonusPoint rawXpBonusPoint = new RawBonusPointCharacteristics.XpBonusPoint();
            rawXpBonusPoint.characId = it1.key();
            rawXpBonusPoint.nbPoint = it1.value();
            raw.xpBonusPoints.add(rawXpBonusPoint);
        }
        final TByteShortIterator it2 = this.m_characteristicBonusPoint.iterator();
        while (it2.hasNext()) {
            it2.advance();
            final RawBonusPointCharacteristics.CharacteristicBonusPoint rawCharacteristicBonusPoint = new RawBonusPointCharacteristics.CharacteristicBonusPoint();
            rawCharacteristicBonusPoint.characId = it2.key();
            rawCharacteristicBonusPoint.value = it2.value();
            raw.characteristicBonusPoints.add(rawCharacteristicBonusPoint);
        }
        raw.freePoints = this.m_freePoint;
        return success;
    }
    
    @Override
    public boolean fromRaw(final RawBonusPointCharacteristics raw) {
        final boolean success = true;
        this.m_freePoint = raw.freePoints;
        for (final RawBonusPointCharacteristics.XpBonusPoint xpBonusPoint : raw.xpBonusPoints) {
            this.m_xpBonusPoints.put(xpBonusPoint.characId, xpBonusPoint.nbPoint);
        }
        for (final RawBonusPointCharacteristics.CharacteristicBonusPoint characteristicBonusPoint : raw.characteristicBonusPoints) {
            this.m_characteristicBonusPoint.put(characteristicBonusPoint.characId, characteristicBonusPoint.value);
        }
        return success;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BonusPointCharacteristics.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<BonusPointCharacteristics>() {
            @Override
            public BonusPointCharacteristics makeObject() {
                return new BonusPointCharacteristics();
            }
        }, 100);
    }
    
    public enum XpBonusPointError
    {
        NO_ERROR, 
        NOT_ENOUTH_FREEPOINT, 
        CHARAC_NOT_FOUND, 
        NOT_AVATAR_BREED;
    }
}
