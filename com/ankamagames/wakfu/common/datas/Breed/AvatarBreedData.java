package com.ankamagames.wakfu.common.datas.Breed;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;

public class AvatarBreedData
{
    private static final Logger m_logger;
    public static final AvatarBreedData DEFAULT_DATA;
    private final int m_area;
    private final float[] m_ratios;
    private final byte[] m_spellElements;
    private final short m_spellInventoryVersion;
    private SecondaryCharacsCalculator m_secondaryCharacsCalculator;
    private final BreedCharacteristicManager m_characteristics;
    private final TIntFloatHashMap m_characteristicsIncrements;
    
    public AvatarBreedData(final int preferedArea, final float[] characRatios, final byte[] spellElements, final short spellInventoryVersion) {
        super();
        this.m_ratios = new float[BreedRatios.values().length];
        this.m_secondaryCharacsCalculator = SecondaryCharacsCalculator.NULL_CALCULATOR;
        this.m_characteristics = new BreedCharacteristicManager();
        this.m_characteristicsIncrements = new TIntFloatHashMap();
        this.m_area = preferedArea;
        this.m_spellElements = spellElements;
        this.m_spellInventoryVersion = spellInventoryVersion;
        if (this.m_ratios.length == characRatios.length) {
            System.arraycopy(characRatios, 0, this.m_ratios, 0, this.m_ratios.length);
        }
    }
    
    public AvatarBreedData(final SecondaryCharacsCalculator calculator) {
        this(46, new float[] { 1.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f }, PrimitiveArrays.EMPTY_BYTE_ARRAY, (short)0);
        this.m_secondaryCharacsCalculator = calculator;
    }
    
    void loadSecondaryCharacGains(final TByteFloatHashMap gains) {
        gains.forEachEntry(new TByteFloatProcedure() {
            @Override
            public boolean execute(final byte characId, final float value) {
                AvatarBreedData.this.m_characteristicsIncrements.put(characId, value);
                return true;
            }
        });
        this.m_secondaryCharacsCalculator = new SecondaryCharacsCalculatorComposite(gains);
    }
    
    public short getSpellInventoryVersion() {
        return this.m_spellInventoryVersion;
    }
    
    int getBaseCharacteristicValue(final FighterCharacteristicType type) {
        return this.m_characteristics.getBaseCharacteristic(type);
    }
    
    public float getRatio(final BreedRatios type) {
        return this.m_ratios[type.ordinal()];
    }
    
    public int getBaseTimerCountBeforeDeath() {
        return this.m_characteristics.getBaseCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH);
    }
    
    public AbstractBattlegroundBorderEffectArea getBattlegroundBorderEffectArea() {
        return StaticEffectAreaManager.getInstance().getBorderCellArea(this.m_area);
    }
    
    int getLeveledCharacteristic(final int level, final FighterCharacteristicType charac) {
        final int baseCharac = this.m_characteristics.getBaseCharacteristic(charac);
        final float inc = this.m_characteristicsIncrements.get(charac.getId());
        return baseCharac + (int)Math.floor(level * inc);
    }
    
    SecondaryCharacsCalculator getSecondaryCharacsCalculator() {
        return this.m_secondaryCharacsCalculator;
    }
    
    private Elements getElement(final byte index) {
        assert index >= 0 && index < this.m_spellElements.length : "Index de spellElement invalide";
        return Elements.getElementFromId(this.m_spellElements[index]);
    }
    
    public boolean hasElement(final Elements element) {
        for (int i = 0; i < this.m_spellElements.length; ++i) {
            if (this.m_spellElements[i] == element.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public void foreachElement(final TObjectProcedure<Elements> procedure) {
        for (int i = 0; i < this.m_spellElements.length; ++i) {
            final Elements elt = this.getElement((byte)i);
            if (!procedure.execute(elt)) {
                return;
            }
        }
    }
    
    public void setBaseCharacteristic(final FighterCharacteristicType charac, final int value) {
        this.m_characteristics.setBaseCharacteristic(charac, value);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AvatarBreedData.class);
        DEFAULT_DATA = new AvatarBreedData(SecondaryCharacsCalculator.NULL_CALCULATOR);
    }
}
