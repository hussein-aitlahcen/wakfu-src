package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.xp.character.*;
import com.ankamagames.wakfu.common.game.xp.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class PvPBuffGain extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final HashMap<AbstractSpellLevel, Short> m_gainsPerSpell;
    private int m_hpGain;
    private int m_hpToAdd;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PvPBuffGain.PARAMETERS_LIST_SET;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        boolean atLeastOne = false;
        final AbstractCharacteristic hpCharac = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
        if (hpCharac != null) {
            final int previousPlainMax = hpCharac.plainMax();
            final int previousMax = hpCharac.max();
            hpCharac.updateMaxValue(this.m_hpGain);
            this.m_hpGain = hpCharac.plainMax() - previousPlainMax;
            hpCharac.add(this.m_hpToAdd = hpCharac.max() - previousMax);
        }
        for (final Map.Entry<AbstractSpellLevel, Short> entry : this.m_gainsPerSpell.entrySet()) {
            final AbstractSpellLevel spellLevel = entry.getKey();
            final Short levelGain = entry.getValue();
            if (levelGain <= 0) {
                PvPBuffGain.m_logger.error((Object)"Error dans PvPBuffGain : gain <= 0 ??");
            }
            else {
                atLeastOne = true;
                spellLevel.addLevelGain(levelGain);
            }
        }
        if (atLeastOne) {
            this.updateTargetElementMasteries();
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
        if (!this.m_executed) {
            return;
        }
        final AbstractCharacteristic hpCharac = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
        if (hpCharac != null) {
            hpCharac.substract(this.m_hpToAdd);
            hpCharac.updateMaxValue(-this.m_hpGain);
        }
        for (final Map.Entry<AbstractSpellLevel, Short> entry : this.m_gainsPerSpell.entrySet()) {
            final AbstractSpellLevel spellLevel = entry.getKey();
            final Short levelGain = entry.getValue();
            if (levelGain <= 0) {
                PvPBuffGain.m_logger.error((Object)"Error dans PvPBuffGain : gain <= 0 ??");
            }
            else {
                spellLevel.addLevelGain(-levelGain);
            }
        }
        this.updateTargetElementMasteries();
    }
    
    private void updateTargetElementMasteries() {
        final BasicCharacterInfo targetCharacterInfo = (BasicCharacterInfo)this.m_target;
        for (final Elements element : Elements.values()) {
            targetCharacterInfo.updateElementMastery(element);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null || this.m_target == null) {
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        final short effectLevel = this.getContainerLevel();
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, effectLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (!this.computeSpellGains(target, this.m_value)) {
            PvPBuffGain.m_logger.warn((Object)("Unable to apply spellGain to " + this.m_target));
        }
        final Breed targetBreed = target.getBreed();
        if (targetBreed instanceof AvatarBreed) {
            final SecondaryCharacsCalculator calculator = ((AvatarBreed)targetBreed).getSecondaryCharacsCalculator();
            this.m_hpGain = calculator.previewModificationForLevelUp(FighterCharacteristicType.HP, target.getLevel(), (short)this.m_value);
        }
        else {
            this.m_hpGain = Math.max(0, this.m_value - target.getLevel()) * 5;
        }
    }
    
    private boolean computeSpellGains(final BasicCharacterInfo target, final int wantedLevel) {
        if (!(this.m_target instanceof PlayerCharacterLevelable)) {
            return false;
        }
        if (target.getLevel() >= wantedLevel) {
            return false;
        }
        final Iterable<? extends AbstractSpellLevel> spellInventory = this.getTargetSpellInventory();
        if (spellInventory == null) {
            return false;
        }
        final long targetXp = ((PlayerCharacterLevelable)target).getCurrentXp();
        long totalVirtualXp = 0L;
        for (final Elements element : Elements.values()) {
            if (element.isElemental()) {
                final long treeVirtualXp = SpellXpComputer.getTreeVirtualXp(target, targetXp, element);
                totalVirtualXp += treeVirtualXp;
            }
        }
        if (totalVirtualXp == 0L) {
            return false;
        }
        final long maxXpNow = SpellXpComputer.getMaxSpellXpFromPlayerXp(targetXp);
        final long unpenalizedXpLimitNow = maxXpNow / 3L;
        final long maxXpAtWantedLevel = SpellXpComputer.getMaxSpellXpFromPlayerLevel((short)this.m_value);
        final long unpenalizedXpLimitAtWantedLevel = maxXpAtWantedLevel / 3L;
        final double gainFactor = maxXpAtWantedLevel / totalVirtualXp;
        if (gainFactor <= 1.0) {
            PvPBuffGain.m_logger.error((Object)("Gain in a PvPBuffGain <= 1 (" + gainFactor + ")"));
            return false;
        }
        for (final AbstractSpellLevel spell : spellInventory) {
            final Elements element2 = spell.getElement();
            if (!element2.isElemental()) {
                continue;
            }
            final short currentSpellLevel = spell.getLevelWithoutGain();
            if (currentSpellLevel == 0) {
                continue;
            }
            final long spellXp = spell.getXp();
            final long spellVirtualXp = SpellXpComputer.realXpToVirtualXpDiff(0L, spellXp, unpenalizedXpLimitNow);
            final long spellVirtualXpBuffed = Math.round(spellVirtualXp * gainFactor);
            final long realXpAtWantedLevel = Math.round(SpellXpComputer.virtualXpToRealXpDiff(0L, spellVirtualXpBuffed, unpenalizedXpLimitAtWantedLevel));
            final short newSpellLevel = SpellXpTable.getInstance().getLevelByXp(realXpAtWantedLevel);
            if (currentSpellLevel >= newSpellLevel) {
                continue;
            }
            this.m_gainsPerSpell.put(spell, (short)(newSpellLevel - currentSpellLevel));
        }
        return true;
    }
    
    private Iterable<? extends AbstractSpellLevel> getTargetSpellInventory() {
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return null;
        }
        return ((BasicCharacterInfo)this.m_target).getPermanentSpellInventory();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    private AbstractSpellLevel getFirstWithReferenceId(final Iterable<? extends AbstractSpellLevel> spellInventory, final int spellRefId) {
        for (final AbstractSpellLevel spellLevel : spellInventory) {
            if (spellLevel.getSpellId() == spellRefId) {
                return spellLevel;
            }
        }
        return null;
    }
    
    @Override
    public PvPBuffGain newInstance() {
        PvPBuffGain re;
        try {
            re = (PvPBuffGain)PvPBuffGain.m_staticPool.borrowObject();
            re.m_pool = PvPBuffGain.m_staticPool;
        }
        catch (Exception e) {
            re = new PvPBuffGain();
            re.m_pool = null;
            PvPBuffGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un " + PvPBuffGain.class + " : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_gainsPerSpell.clear();
        this.m_hpToAdd = 0;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_gainsPerSpell.clear();
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    public PvPBuffGain() {
        super();
        this.m_gainsPerSpell = new HashMap<AbstractSpellLevel, Short>();
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(PvPBuffGain.this.m_hpGain);
                buffer.putInt(PvPBuffGain.this.m_hpToAdd);
                buffer.put((byte)PvPBuffGain.this.m_gainsPerSpell.size());
                for (final Map.Entry<AbstractSpellLevel, Short> entry : PvPBuffGain.this.m_gainsPerSpell.entrySet()) {
                    buffer.putInt(entry.getKey().getReferenceId());
                    buffer.putShort(entry.getValue());
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                PvPBuffGain.this.m_hpGain = buffer.getInt();
                PvPBuffGain.this.m_hpToAdd = buffer.getInt();
                final Iterable<? extends AbstractSpellLevel> spellInventory = PvPBuffGain.this.getTargetSpellInventory();
                if (spellInventory == null) {
                    PvPBuffGain$2.m_logger.error((Object)"Unable to get target spell inventory");
                }
                final int spellsCount = buffer.get();
                PvPBuffGain.this.m_gainsPerSpell.clear();
                for (int i = 0; i < spellsCount; ++i) {
                    final int spellRefId = buffer.getInt();
                    final short levelGain = buffer.getShort();
                    final AbstractSpellLevel spellLevel = PvPBuffGain.this.getFirstWithReferenceId(spellInventory, spellRefId);
                    if (spellLevel == null) {
                        PvPBuffGain$2.m_logger.error((Object)("Unable to find spellf or PvPBuffGain : " + spellRefId));
                    }
                    else {
                        PvPBuffGain.this.m_gainsPerSpell.put(spellLevel, levelGain);
                    }
                }
            }
            
            @Override
            public int expectedSize() {
                return 9 + PvPBuffGain.this.m_gainsPerSpell.size() * 6;
            }
        };
        this.setTriggersToExecute();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PvPBuffGain>() {
            @Override
            public PvPBuffGain makeObject() {
                return new PvPBuffGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Buff PvP", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Niveau \u00e0 simuler", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
