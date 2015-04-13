package com.ankamagames.wakfu.common.datas.specific;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.rawData.*;

public class ImageCharacteristics extends BasicInvocationCharacteristics
{
    private static ImageCharacteristics m_instance;
    private static final Logger m_logger;
    protected FighterCharacteristicManager m_imageCharac;
    protected byte m_sex;
    protected int m_gfxId;
    
    public static ImageCharacteristics getDefaultInstance() {
        return ImageCharacteristics.m_instance;
    }
    
    public static void setDefaultInstance(final ImageCharacteristics instance) {
        if (ImageCharacteristics.m_instance == null) {
            ImageCharacteristics.m_instance = instance;
        }
    }
    
    public ImageCharacteristics newInstance(final short id, final String name, final int hp, final short level, final BasicCharacterInfo summoner, final SpellInventory<AbstractSpellLevel> spellInventory) {
        return new ImageCharacteristics(id, name, hp, level, summoner, spellInventory);
    }
    
    public ImageCharacteristics newInstance() {
        return new ImageCharacteristics();
    }
    
    public ImageCharacteristics newInstance(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        return new ImageCharacteristics(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    public ImageCharacteristics() {
        super();
        this.m_imageCharac = new FighterCharacteristicManager();
    }
    
    protected ImageCharacteristics(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super();
        this.m_imageCharac = new FighterCharacteristicManager();
    }
    
    protected ImageCharacteristics(final short id, final String name, final int hp, final short level, final BasicCharacterInfo summoner, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, summoner.getLevel());
        this.m_imageCharac = new FighterCharacteristicManager();
        this.setName(summoner.getName());
        final CharacteristicManager<FighterCharacteristic> summonerCharacteristics = summoner.getCharacteristics();
        for (final FighterCharacteristicType type : FighterCharacteristicType.values()) {
            if (summonerCharacteristics.contains(type)) {
                if (this.m_imageCharac.contains(type)) {
                    this.m_imageCharac.getCharacteristic((CharacteristicType)type).set(summonerCharacteristics.getCharacteristic(type));
                }
            }
        }
    }
    
    @Override
    public void initializeSummoning(final BasicCharacterInfo summoning, final BasicCharacterInfo summoner) {
        super.initializeSummoning(summoning, summoner);
        this.initializeImageCharacteristics(summoning);
        this.initializeImageSpells(summoning);
        summoning.addProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE);
    }
    
    private void initializeImageCharacteristics(final BasicCharacterInfo summoning) {
        summoning.getCharacteristics().copy(this.m_imageCharac);
    }
    
    private void initializeImageSpells(final BasicCharacterInfo summoning) {
    }
    
    @Override
    public byte getSex() {
        return this.m_sex;
    }
    
    @Override
    public boolean toRaw(final RawInvocationCharacteristic rawCharacteristics) {
        rawCharacteristics.clear();
        super.toRaw(rawCharacteristics);
        rawCharacteristics.IMAGEINVOC = new RawInvocationCharacteristic.IMAGEINVOC();
        rawCharacteristics.IMAGEINVOC.imagedata.sex = this.m_sex;
        rawCharacteristics.IMAGEINVOC.imagedata.gfxId = this.m_gfxId;
        this.m_imageCharac.toRaw(rawCharacteristics.IMAGEINVOC.imagedata.imageCharac);
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawInvocationCharacteristic raw) {
        final boolean ok = super.fromRaw(raw);
        this.m_sex = raw.IMAGEINVOC.imagedata.sex;
        this.m_gfxId = raw.IMAGEINVOC.imagedata.gfxId;
        this.m_imageCharac.fromRaw(raw.IMAGEINVOC.imagedata.imageCharac);
        return ok;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageCharacteristics.class);
    }
}
