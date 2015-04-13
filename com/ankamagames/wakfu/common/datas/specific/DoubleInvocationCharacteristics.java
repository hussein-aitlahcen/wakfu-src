package com.ankamagames.wakfu.common.datas.specific;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class DoubleInvocationCharacteristics extends BasicInvocationCharacteristics
{
    private static DoubleInvocationCharacteristics m_instance;
    private static final Logger m_logger;
    protected SpellInventory<AbstractSpellLevel> m_spellInventory;
    protected FighterCharacteristicManager m_doubleCharac;
    protected int m_power;
    protected byte m_sex;
    protected int m_gfxId;
    protected byte m_hairColorIndex;
    protected byte m_hairColorFactor;
    protected byte m_skinColorFactor;
    protected byte m_skinColorIndex;
    protected byte m_pupilColorIndex;
    protected byte m_clothIndex;
    protected byte m_faceIndex;
    protected TByteIntHashMap m_equipmentAppearance;
    
    public DoubleInvocationCharacteristics() {
        super();
        this.m_doubleCharac = new FighterCharacteristicManager();
    }
    
    public DoubleInvocationCharacteristics(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super();
        this.m_doubleCharac = new FighterCharacteristicManager();
        this.m_spellInventory = new SpellInventory<AbstractSpellLevel>(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    public static DoubleInvocationCharacteristics getDefaultInstance() {
        return DoubleInvocationCharacteristics.m_instance;
    }
    
    public static void setDefaultInstance(final DoubleInvocationCharacteristics instance) {
        DoubleInvocationCharacteristics.m_instance = instance;
    }
    
    public DoubleInvocationCharacteristics newInstance(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        return new DoubleInvocationCharacteristics(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    public DoubleInvocationCharacteristics newInstance() {
        return new DoubleInvocationCharacteristics();
    }
    
    public DoubleInvocationCharacteristics newInstance(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        return new DoubleInvocationCharacteristics(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    public DoubleInvocationCharacteristics(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, model.getLevel());
        this.m_doubleCharac = new FighterCharacteristicManager();
        this.setName(model.getName());
        this.m_gfxId = model.getGfxId();
        this.m_power = doublePower;
        this.m_spellInventory = spellInventory;
        this.m_sex = model.getSex();
        final double powerRatio = this.m_power / 100.0f;
        final Breed modelBreed = model.getBreed();
        this.m_doubleCharac.makeDefault();
        final FighterCharacteristicManager doubleCharacteristics = model.getDoubleCharacteristics();
        final CharacteristicManager modelCharacManager = (doubleCharacteristics != null) ? doubleCharacteristics : model.getCharacteristics();
        for (final FighterCharacteristicType type : FighterCharacteristicType.values()) {
            if (type.isExpandable()) {
                this.m_doubleCharac.getCharacteristic((CharacteristicType)type).setMax(modelCharacManager.getCharacteristicMaxValue(type));
            }
            else {
                this.m_doubleCharac.getCharacteristic((CharacteristicType)type).set(modelCharacManager.getCharacteristicValue(type));
            }
        }
        if (modelBreed instanceof AvatarBreed) {
            ((AvatarBreed)modelBreed).getSecondaryCharacsCalculator().applyForInitialize(this.m_doubleCharac, level);
        }
        this.m_doubleCharac.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).toMax();
        this.m_doubleCharac.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP).set(model.getCharacteristicValue(FighterCharacteristicType.WP));
        this.m_doubleCharac.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).toMax();
        this.m_doubleCharac.getCharacteristic((CharacteristicType)FighterCharacteristicType.KO_TIME_BEFORE_DEATH).setMax(0);
        for (final CharacBoundByLevel characBoundByLevel : CharacBoundByLevelTable.getInstance().getCharacBoundsByLevel()) {
            final byte characId = characBoundByLevel.getCharacId();
            final int bound = characBoundByLevel.getBound(level);
            final FighterCharacteristicType characType = FighterCharacteristicType.getCharacteristicTypeFromId(characId);
            if (characType != null && this.m_doubleCharac.getCharacteristic((CharacteristicType)characType) != null) {
                this.m_doubleCharac.getCharacteristic((CharacteristicType)characType).setUpperBound(bound);
            }
        }
        if (powerRatio != 1.0) {
            for (final FighterCharacteristicType type : FighterCharacteristicType.values()) {
                if (type != FighterCharacteristicType.AP && type != FighterCharacteristicType.MP) {
                    if (type != FighterCharacteristicType.WP) {
                        final FighterCharacteristic characteristic = this.m_doubleCharac.getCharacteristic((CharacteristicType)type);
                        if (type.isExpandable()) {
                            characteristic.setMax((int)Math.ceil(characteristic.max() * powerRatio));
                        }
                        else {
                            characteristic.set((int)Math.ceil(characteristic.value() * powerRatio));
                        }
                    }
                }
            }
        }
        this.m_doubleCharac.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(model.getCharacteristicValue(FighterCharacteristicType.HP));
        this.m_doubleCharac.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).setMax((int)(modelCharacManager.getCharacteristicMaxValue(FighterCharacteristicType.HP) * powerRatio));
    }
    
    @Override
    public void initializeSummoning(final BasicCharacterInfo summoning, final BasicCharacterInfo summoner) {
        super.initializeSummoning(summoning, summoner);
        this.initializeDoubleCharacteristics(summoning);
        this.initializeDoubleSpells(summoning);
    }
    
    private void initializeDoubleCharacteristics(final BasicCharacterInfo summoning) {
        summoning.getCharacteristics().copy(this.m_doubleCharac);
        if (this.getCurrentHp() > 0) {
            summoning.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(this.getCurrentHp());
        }
    }
    
    protected void initializeDoubleSpells(final BasicCharacterInfo summoning) {
        final SpellInventory<AbstractSpellLevel> spellInventory = (SpellInventory<AbstractSpellLevel>)summoning.getSpellInventory();
        if (spellInventory == null) {
            return;
        }
        final double powerRatio = this.m_power / 100.0f;
        for (final AbstractSpellLevel spellLevel : this.m_spellInventory) {
            try {
                final AbstractSpellLevel summonningSpellLevel = (AbstractSpellLevel)spellLevel.getClone();
                summonningSpellLevel.setLevel((short)(spellLevel.getLevel() * powerRatio), true);
                spellInventory.add(summonningSpellLevel);
            }
            catch (InventoryCapacityReachedException e) {
                DoubleInvocationCharacteristics.m_logger.error((Object)"InventoryCapacityReachedException lors de l'initialisation des sorts d'un double : ", (Throwable)e);
            }
            catch (ContentAlreadyPresentException e2) {
                DoubleInvocationCharacteristics.m_logger.error((Object)"ContentAlreadyPresentException lors de l'initialisation des sorts d'un double : ", (Throwable)e2);
            }
        }
    }
    
    @Override
    public byte getSex() {
        return this.m_sex;
    }
    
    @Override
    public boolean toRaw(final RawInvocationCharacteristic rawCharacteristics) {
        rawCharacteristics.clear();
        super.toRaw(rawCharacteristics);
        rawCharacteristics.DOUBLEINVOC = new RawInvocationCharacteristic.DOUBLEINVOC();
        rawCharacteristics.DOUBLEINVOC.doubledata.power = this.m_power;
        rawCharacteristics.DOUBLEINVOC.doubledata.sex = this.m_sex;
        rawCharacteristics.DOUBLEINVOC.doubledata.gfxId = this.m_gfxId;
        rawCharacteristics.DOUBLEINVOC.doubledata.haircolorindex = this.m_hairColorIndex;
        rawCharacteristics.DOUBLEINVOC.doubledata.haircolorfactor = this.m_hairColorFactor;
        rawCharacteristics.DOUBLEINVOC.doubledata.skincolorindex = this.m_skinColorIndex;
        rawCharacteristics.DOUBLEINVOC.doubledata.skincolorfactor = this.m_skinColorFactor;
        rawCharacteristics.DOUBLEINVOC.doubledata.pupilcolorindex = this.m_pupilColorIndex;
        rawCharacteristics.DOUBLEINVOC.doubledata.clothIndex = this.m_clothIndex;
        rawCharacteristics.DOUBLEINVOC.doubledata.faceIndex = this.m_faceIndex;
        rawCharacteristics.DOUBLEINVOC.doubledata.doubleType = this.getDoubleType();
        if (this.m_spellInventory != null) {
            this.m_spellInventory.toRaw(rawCharacteristics.DOUBLEINVOC.doubledata.doublespells);
        }
        this.m_doubleCharac.toRaw(rawCharacteristics.DOUBLEINVOC.doubledata.doubleCharac);
        rawCharacteristics.DOUBLEINVOC.doubledata.equipmentAppareances.clear();
        if (this.m_equipmentAppearance != null) {
            final TByteIntIterator intIterator = this.m_equipmentAppearance.iterator();
            while (intIterator.hasNext()) {
                intIterator.advance();
                final RawDoubleInvocationCharacteristic.EquipmentAppareance appearance = new RawDoubleInvocationCharacteristic.EquipmentAppareance();
                appearance.position = intIterator.key();
                appearance.refId = intIterator.value();
                rawCharacteristics.DOUBLEINVOC.doubledata.equipmentAppareances.add(appearance);
            }
        }
        return true;
    }
    
    protected byte getDoubleType() {
        return 1;
    }
    
    @Override
    public boolean fromRaw(final RawInvocationCharacteristic raw) {
        boolean ok = super.fromRaw(raw);
        this.m_power = raw.DOUBLEINVOC.doubledata.power;
        this.m_sex = raw.DOUBLEINVOC.doubledata.sex;
        this.m_gfxId = raw.DOUBLEINVOC.doubledata.gfxId;
        this.m_hairColorIndex = raw.DOUBLEINVOC.doubledata.haircolorindex;
        this.m_hairColorFactor = raw.DOUBLEINVOC.doubledata.haircolorfactor;
        this.m_skinColorIndex = raw.DOUBLEINVOC.doubledata.skincolorindex;
        this.m_skinColorFactor = raw.DOUBLEINVOC.doubledata.skincolorfactor;
        this.m_pupilColorIndex = raw.DOUBLEINVOC.doubledata.pupilcolorindex;
        this.m_clothIndex = raw.DOUBLEINVOC.doubledata.clothIndex;
        this.m_faceIndex = raw.DOUBLEINVOC.doubledata.faceIndex;
        if (this.m_spellInventory != null) {
            ok &= this.m_spellInventory.fromRaw(raw.DOUBLEINVOC.doubledata.doublespells);
        }
        ok &= this.m_doubleCharac.fromRaw(raw.DOUBLEINVOC.doubledata.doubleCharac);
        this.m_equipmentAppearance = null;
        if (raw.DOUBLEINVOC.doubledata.equipmentAppareances.size() > 0) {
            final Iterator<RawDoubleInvocationCharacteristic.EquipmentAppareance> appareanceIterator = raw.DOUBLEINVOC.doubledata.equipmentAppareances.iterator();
            this.m_equipmentAppearance = new TByteIntHashMap();
            while (appareanceIterator.hasNext()) {
                final RawDoubleInvocationCharacteristic.EquipmentAppareance equipmentAppareance = appareanceIterator.next();
                this.m_equipmentAppearance.put(equipmentAppareance.position, equipmentAppareance.refId);
            }
        }
        return ok;
    }
    
    public SpellInventory<AbstractSpellLevel> getSpellInventory() {
        return this.m_spellInventory;
    }
    
    @Override
    public void clean() {
        super.clean();
        if (this.m_spellInventory != null) {
            this.m_spellInventory.removeAll();
            this.m_spellInventory.removeAllObservers();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DoubleInvocationCharacteristics.class);
    }
}
