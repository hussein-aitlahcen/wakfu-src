package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;

public abstract class AbstractSpellLevel<Spell extends AbstractSpell> implements Levelable, WakfuEffectContainer, InventoryContent, RawConvertible<RawSpellLevel>
{
    protected static final Logger m_logger;
    private static final int AP_XP_RATIO = 10;
    private static final int MP_XP_RATIO = 15;
    private static final int WP_XP_RATIO = 20;
    public static final short DEFAULT_AGGRO_WEIGHT = 1;
    public static final short DEFAULT_ALLY_EFFICACITY = 1;
    public static final short DEFAULT_FOE_EFFICACITY = 1;
    public static final byte SPELL_LEVEL = 1;
    public static final byte WEAPON_SKILL_SPELL_LEVEL = 2;
    private static final int NULL_SPELL_XP_RATIO_VALUE = 0;
    private final LevelableImpl m_levelableImpl;
    protected Spell m_spell;
    protected long m_uid;
    protected int m_levelGain;
    
    protected AbstractSpellLevel() {
        super();
        this.m_levelableImpl = new LevelableImpl(this.getXpTable());
    }
    
    public Spell getSpell() {
        return this.m_spell;
    }
    
    public abstract AbstractSpellManager<Spell> getSpellManager();
    
    @Override
    public int getContainerType() {
        if (this.m_spell == null) {
            AbstractSpellLevel.m_logger.error((Object)("ATTENTION, on essaie de v\u00e9rifier le type de conteneur mais le spell est inconnu, uid : " + this.m_uid));
            return 11;
        }
        if (this.m_spell.isPassive()) {
            return 25;
        }
        return 11;
    }
    
    @Override
    public long getEffectContainerId() {
        if (this.m_spell == null) {
            AbstractSpellLevel.m_logger.error((Object)"On cherche a r\u00e9cup\u00e9rer l'id d'un SpellLevel mais son spell associ\u00e9 est null ", (Throwable)new Exception("Exception pour etude de stack"));
            return 0L;
        }
        return getDefaultUId(this.m_spell.getId(), this.getLevel());
    }
    
    public static long getDefaultUId(final int spellId, final short level) {
        return (spellId << 16) + (level & 0xFF);
    }
    
    public static int getSpellIdFromDefautUId(final long uid) {
        return (int)(uid >> 16);
    }
    
    public static short getLevelFromDefautUId(final long uid) {
        return (short)(uid & 0xFFL);
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return this.m_spell.getEffectsForLevel(this.getLevel());
    }
    
    @Override
    public void release() {
    }
    
    protected void clear() {
        this.m_spell = null;
        this.m_levelGain = 0;
        this.m_uid = -1L;
        this.m_levelableImpl.clear();
    }
    
    @Override
    public long getUniqueId() {
        return this.m_uid;
    }
    
    @Override
    public int getReferenceId() {
        return this.m_spell.getId();
    }
    
    public abstract byte getType();
    
    @Override
    public boolean toRaw(final RawSpellLevel rawSpellLevel) {
        rawSpellLevel.type = this.getType();
        rawSpellLevel.uniqueId = this.m_uid;
        rawSpellLevel.spellId = this.m_spell.getId();
        rawSpellLevel.level = this.m_levelableImpl.getLevel();
        rawSpellLevel.xp = this.m_levelableImpl.getXp();
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawSpellLevel rawSpellLevel) {
        this.m_uid = rawSpellLevel.uniqueId;
        this.m_spell = this.getSpellManager().getSpell(rawSpellLevel.spellId);
        this.setLevelAndXp(rawSpellLevel.level, rawSpellLevel.xp);
        if (this.m_spell == null) {
            AbstractSpellLevel.m_logger.error((Object)("Impossible de d\u00e9s\u00e9rialiser un sort : sort d'id " + rawSpellLevel.spellId + " inconnu UID : " + this.m_uid));
            return false;
        }
        return true;
    }
    
    @Override
    public short getQuantity() {
        return 1;
    }
    
    @Override
    public void setQuantity(final short quantity) {
    }
    
    @Override
    public void updateQuantity(final short quantityUpdate) {
    }
    
    @Override
    public boolean canStackWith(final InventoryContent inv) {
        return false;
    }
    
    @Override
    public short getStackMaximumHeight() {
        return 1;
    }
    
    @Override
    public boolean shouldBeSerialized() {
        return true;
    }
    
    @Override
    public float getXpGainMultiplier() {
        final short level = this.getLevel();
        final short baseXpRatio = this.m_spell.getXpGainPercentage();
        if (baseXpRatio <= 0) {
            return this.m_spell.getActionPoints(level) * 10 + this.m_spell.getMovementPoints(level) * 15 + this.m_spell.getWakfuPoints(level) * 20;
        }
        return baseXpRatio;
    }
    
    @Override
    public short getAggroWeight() {
        return 1;
    }
    
    @Override
    public short getAllyEfficacity() {
        return 1;
    }
    
    @Override
    public short getFoeEfficacity() {
        return 1;
    }
    
    public void addLevelGain(final int levelGainToAdd) {
        this.m_levelGain += levelGainToAdd;
    }
    
    public int getLevelGain() {
        return this.m_levelGain;
    }
    
    @Override
    public short getLevel() {
        return (short)MathHelper.clamp(this.m_levelableImpl.getLevel() + this.m_levelGain, 0, this.m_spell.getMaxLevel());
    }
    
    public short getLevelWithoutGain() {
        return (short)Math.min(this.m_levelableImpl.getLevel(), this.m_spell.getMaxLevel());
    }
    
    public int getMaxLevel() {
        return this.m_spell.getMaxLevel();
    }
    
    public long getXpDoneInLevel() {
        return this.getXpTable().getXpInLevel(this.getXp());
    }
    
    @Override
    public float getCurrentLevelPercentage() {
        return this.m_levelableImpl.getCurrentLevelPercentage();
    }
    
    @Override
    public XpModification setLevelAndXp(final short level, final long xp) {
        return this.m_levelableImpl.setLevelAndXp(level, xp);
    }
    
    @Override
    public long getXp() {
        return this.m_levelableImpl.getXp();
    }
    
    @Override
    public XpModification addXp(final long xpAdded) {
        if (xpAdded >= 0L) {
            return this.m_levelableImpl.addXp(xpAdded);
        }
        return this.m_levelableImpl.removeXp(-xpAdded);
    }
    
    @Override
    public XpModification setLevel(short level, final boolean linkXp) {
        if (level > this.m_spell.getMaxLevel()) {
            level = (short)this.m_spell.getMaxLevel();
        }
        if (level > this.getXpTable().getMaxLevel()) {
            level = this.getXpTable().getMaxLevel();
        }
        return this.m_levelableImpl.setLevel(level, linkXp);
    }
    
    @Override
    public XpTable getXpTable() {
        return SpellXpTable.getInstance();
    }
    
    public String getCurrentLevelString() {
        return new StringBuilder().append(this.getXpTable().getXpInLevel(this.getXp())).append('/').append(this.getXpTable().getLevelExtent(this.getLevelWithoutGain())).toString();
    }
    
    public Elements getElement() {
        if (this.m_spell == null) {
            return null;
        }
        return Elements.getElementFromId(this.m_spell.getElementId());
    }
    
    public SpellCost getSpellCost(final BasicCharacterInfo user, final Object target, final Object context) {
        if (this.m_spell == null) {
            return null;
        }
        return this.m_spell.getSpellCost(this, user, target, context);
    }
    
    public long getSpellId() {
        if (this.m_spell != null) {
            return this.m_spell.getId();
        }
        return -1L;
    }
    
    public byte getCastMaxPerTurn() {
        return (byte)MathHelper.fastFloor(this.m_spell.getCastMaxPerTurn() + this.m_spell.getCastMaxPerTurnIncr() * this.getLevel());
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractSpellLevel.class);
    }
}
