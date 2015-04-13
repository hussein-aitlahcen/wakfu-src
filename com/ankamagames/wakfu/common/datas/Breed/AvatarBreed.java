package com.ankamagames.wakfu.common.datas.Breed;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.effect.*;

public enum AvatarBreed implements Breed
{
    COMMON(-2), 
    NONE(-1), 
    FECA(1), 
    OSAMODAS(2), 
    ENUTROF(3), 
    SRAM(4), 
    XELOR(5), 
    ECAFLIP(6), 
    ENIRIPSA(7), 
    IOP(8), 
    CRA(9), 
    SADIDA(10), 
    SACRIER(11), 
    PANDAWA(12), 
    ROUBLARD(13), 
    ZOBAL(14), 
    OUGINAK(15), 
    STEAMER(16), 
    SOUL(17), 
    ELIOTROPE(18);
    
    private static final int ID_FAMILY_AVATAR = -1;
    public static final int DEFAULT_BREED_HEIGHT = 6;
    public static final int DEFAULT_RADIUS = 0;
    private static final Logger m_logger;
    private final short m_breedId;
    private AvatarBreedData m_data;
    
    private AvatarBreed(final int id) {
        this.m_breedId = (short)id;
        this.m_data = AvatarBreedData.DEFAULT_DATA;
    }
    
    public static AvatarBreed getBreedFromId(final int id) {
        for (final AvatarBreed breed : values()) {
            if (breed.getBreedId() == id) {
                return breed;
            }
        }
        return AvatarBreed.NONE;
    }
    
    public boolean isInitialized() {
        return this.m_data != null;
    }
    
    public static AvatarBreed getBreedFromName(final String s) {
        final String upperName = s.toUpperCase();
        for (final AvatarBreed breed : values()) {
            if (breed.toString().equals(upperName)) {
                return breed;
            }
        }
        AvatarBreed.m_logger.warn((Object)("breed inconnue " + upperName));
        return AvatarBreed.NONE;
    }
    
    public void setData(final AvatarBreedData data) {
        this.m_data = data;
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
    @Override
    public short getBreedId() {
        return this.m_breedId;
    }
    
    @Override
    public int getFamilyId() {
        return -1;
    }
    
    @Override
    public int getBaseCharacteristicValue(final FighterCharacteristicType type) {
        return this.m_data.getBaseCharacteristicValue(type);
    }
    
    @Override
    public float getRatio(final BreedRatios type) {
        return this.m_data.getRatio(type);
    }
    
    @Override
    public int getBaseTimerCountBeforeDeath() {
        return this.m_data.getBaseTimerCountBeforeDeath();
    }
    
    @Override
    public AbstractBattlegroundBorderEffectArea getBattlegroundBorderEffectArea() {
        return this.m_data.getBattlegroundBorderEffectArea();
    }
    
    @Override
    public int[] getBaseFightProperties() {
        return PrimitiveArrays.EMPTY_INT_ARRAY;
    }
    
    @Override
    public int[] getBaseWorldProperties() {
        return PrimitiveArrays.EMPTY_INT_ARRAY;
    }
    
    @Override
    public byte getHeight() {
        return 6;
    }
    
    @Override
    public byte getPhysicalRadius() {
        return 0;
    }
    
    @Override
    public int getMaxWalkDistance() {
        return 5;
    }
    
    @Override
    public int getMaxFightWalkDistance() {
        return 0;
    }
    
    @Override
    public MovementSpeed getWalkTimeBetweenCells() {
        return MovementSpeed.NORMAL_WALK_SPEED;
    }
    
    @Override
    public MovementSpeed getRunTimeBetweenCells() {
        return MovementSpeed.NORMAL_RUN_SPEED;
    }
    
    @Override
    public int getDefeatScriptId() {
        throw new UnsupportedOperationException("Pas de script de mort pour les perso joueurs");
    }
    
    @Override
    public int getLeveledCharacteristic(final int level, final FighterCharacteristicType charac) {
        return this.m_data.getLeveledCharacteristic(level, charac);
    }
    
    public MasteryCharacsCalculator getMasteryCharacsCalculator() {
        return MasteryCharacsCalculator.INSTANCE;
    }
    
    public SecondaryCharacsCalculator getSecondaryCharacsCalculator() {
        return this.m_data.getSecondaryCharacsCalculator();
    }
    
    public void loadSecondaryCharacGains(final TByteFloatHashMap gains) {
        this.m_data.loadSecondaryCharacGains(gains);
    }
    
    public void foreachElement(final TObjectProcedure<Elements> procedure) {
        this.m_data.foreachElement(procedure);
    }
    
    public boolean hasElement(final Elements element) {
        return this.m_data.hasElement(element);
    }
    
    public String getEnumId() {
        return Short.toString(this.getBreedId());
    }
    
    public void setBaseCharacteristic(final FighterCharacteristicType charac, final int value) {
        this.m_data.setBaseCharacteristic(charac, value);
    }
    
    public short getSpellInventoryVersion() {
        return this.m_data.getSpellInventoryVersion();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AvatarBreed.class);
    }
}
