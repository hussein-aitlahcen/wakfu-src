package com.ankamagames.wakfu.common.game.skill;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import org.apache.commons.pool.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class AbstractSkill<ReferenceSkill extends AbstractReferenceSkill> implements Levelable, Poolable, Releasable, InventoryContent, RawConvertible<RawSkill>
{
    protected static final Logger m_logger;
    private static final short[] RANKS_FLOOR_LEVELS;
    protected ObjectPool m_pool;
    private ReferenceSkill m_referenceSkill;
    private AvatarBreed m_ownerBreed;
    private LevelableImpl m_levelableImpl;
    private Levelable m_linkedLevelable;
    
    public AbstractSkill() {
        super();
        this.m_linkedLevelable = null;
    }
    
    protected abstract AbstractSkill<ReferenceSkill> newInstance();
    
    @Override
    public void onCheckOut() {
        this.m_referenceSkill = null;
        this.m_ownerBreed = null;
        this.m_linkedLevelable = null;
    }
    
    @Override
    public void onCheckIn() {
        this.m_levelableImpl = null;
        this.m_referenceSkill = null;
        this.m_ownerBreed = null;
        this.m_linkedLevelable = null;
    }
    
    @Override
    public short getLevel() {
        if (this.m_linkedLevelable != null) {
            return (short)(this.m_levelableImpl.getLevel() + this.m_linkedLevelable.getLevel());
        }
        return this.m_levelableImpl.getLevel();
    }
    
    public byte getRankLevel() {
        short level;
        byte rank;
        for (level = this.getActualLevel(), rank = 0; rank < AbstractSkill.RANKS_FLOOR_LEVELS.length && AbstractSkill.RANKS_FLOOR_LEVELS[rank] <= level; ++rank) {}
        return (byte)(rank - 1);
    }
    
    public short getActualLevel() {
        return this.m_levelableImpl.getLevel();
    }
    
    public Levelable getLinkedLevelable() {
        return this.m_linkedLevelable;
    }
    
    public void setLinkedLevelable(final Levelable linkedLevelable) {
        this.m_linkedLevelable = linkedLevelable;
    }
    
    public ReferenceSkill getReferenceSkill() {
        return this.m_referenceSkill;
    }
    
    @Override
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
                this.m_pool = null;
            }
            catch (Exception e) {
                AbstractSkill.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + " normalement impossible"));
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public long getUniqueId() {
        return this.m_referenceSkill.getId();
    }
    
    @Override
    public int getReferenceId() {
        return this.m_referenceSkill.getId();
    }
    
    protected void initializeFromReferenceSkill(final ReferenceSkill referenceSkill, final short level, final long xp) {
        this.m_referenceSkill = referenceSkill;
        this.m_levelableImpl = new LevelableImpl(this.getXpTable());
        this.setLevelAndXp(level, xp);
    }
    
    @Override
    public boolean fromRaw(final RawSkill rawSkill) {
        final ReferenceSkill referenceSkill = this.getReferenceSkillManager().getReferenceSkill(rawSkill.referenceId);
        if (referenceSkill == null) {
            AbstractSkill.m_logger.error((Object)("Impossible de d\u00e9s\u00e9rialiser un skill : skill reference d'id " + rawSkill.referenceId + " inconnu"));
            return false;
        }
        this.initializeFromReferenceSkill(referenceSkill, rawSkill.level, rawSkill.xp);
        return true;
    }
    
    @Override
    public boolean toRaw(final RawSkill rawSkill) {
        final Levelable levelable = this.getLinkedLevelable();
        this.setLinkedLevelable(null);
        rawSkill.referenceId = this.m_referenceSkill.getId();
        rawSkill.level = this.getLevel();
        rawSkill.xp = this.getXp();
        this.setLinkedLevelable(levelable);
        return true;
    }
    
    public abstract AbstractReferenceSkillManager<ReferenceSkill> getReferenceSkillManager();
    
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
    public InventoryContent getClone() {
        final AbstractSkill<ReferenceSkill> skill = this.newInstance();
        skill.initializeFromReferenceSkill(this.m_referenceSkill, this.getLevel(), this.getXp());
        skill.m_ownerBreed = this.m_ownerBreed;
        return skill;
    }
    
    @Override
    public InventoryContent getCopy(final boolean pooled) {
        return this.getClone();
    }
    
    @Override
    public boolean shouldBeSerialized() {
        return true;
    }
    
    @Override
    public float getXpGainMultiplier() {
        return 1.0f;
    }
    
    public void setOwnerBreed(final AvatarBreed ownerBreed) {
        this.m_ownerBreed = ownerBreed;
    }
    
    @Override
    public XpTable getXpTable() {
        if (this.m_referenceSkill != null) {
            return this.m_referenceSkill.getType().getXpTableRef();
        }
        return NulllXpTable.getInstance();
    }
    
    @Override
    public float getCurrentLevelPercentage() {
        return this.m_levelableImpl.getCurrentLevelPercentage();
    }
    
    @Override
    public XpModification setLevelAndXp(final short level, final long xp) {
        return this.m_levelableImpl.setLevelAndXp(this.coercedLevel(level), xp);
    }
    
    @Override
    public XpModification setLevel(final short level, final boolean linkXp) {
        return this.m_levelableImpl.setLevel(this.coercedLevel(level), linkXp);
    }
    
    private short coercedLevel(final short level) {
        return MathHelper.clamp(level, this.getXpTable().getMinLevel(), this.getXpTable().getMaxLevel());
    }
    
    @Override
    public long getXp() {
        return this.m_levelableImpl.getXp();
    }
    
    @Override
    public XpModification addXp(final long xpAdded) {
        return this.m_levelableImpl.addXp(xpAdded);
    }
    
    public String getCurrentLevelString() {
        return this.m_levelableImpl.getCurrentLevelString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractSkill.class);
        RANKS_FLOOR_LEVELS = new short[] { 0, 10, 20, 40, 60, 80, 100, 125, 150, 175, 200, 225, 250, 275, 300 };
    }
}
