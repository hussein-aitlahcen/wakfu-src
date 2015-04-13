package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ChangePlayerSpellsByMonsterOnes extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final int NO_BREED_ID = -1;
    private BasicCharacterInfo.MonsterSpellsLevel m_spellLevel;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangePlayerSpellsByMonsterOnes.PARAMETERS_LIST_SET;
    }
    
    public ChangePlayerSpellsByMonsterOnes() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(ChangePlayerSpellsByMonsterOnes.this.m_spellLevel.ordinal());
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final int ordinal = buffer.getInt();
                for (final BasicCharacterInfo.MonsterSpellsLevel level : BasicCharacterInfo.MonsterSpellsLevel.values()) {
                    if (level.ordinal() == ordinal) {
                        ChangePlayerSpellsByMonsterOnes.this.m_spellLevel = level;
                        return;
                    }
                }
                ChangePlayerSpellsByMonsterOnes$2.m_logger.error((Object)("Unknown orndinal for MOnsterSpellLevel : " + ordinal));
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public ChangePlayerSpellsByMonsterOnes newInstance() {
        ChangePlayerSpellsByMonsterOnes re;
        try {
            re = (ChangePlayerSpellsByMonsterOnes)ChangePlayerSpellsByMonsterOnes.m_staticPool.borrowObject();
            re.m_pool = ChangePlayerSpellsByMonsterOnes.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangePlayerSpellsByMonsterOnes();
            re.m_pool = null;
            re.m_isStatic = false;
            re.m_spellLevel = BasicCharacterInfo.MonsterSpellsLevel.MONSTER_LEVEL;
            ChangePlayerSpellsByMonsterOnes.m_logger.error((Object)("Erreur lors d'un checkOut sur un ChangePlayerByMobSkinAndSpells : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            final int levelType = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_spellLevel = ((levelType == 1) ? BasicCharacterInfo.MonsterSpellsLevel.PLAYER_LEVEL : BasicCharacterInfo.MonsterSpellsLevel.MONSTER_LEVEL);
        }
        else {
            this.m_spellLevel = BasicCharacterInfo.MonsterSpellsLevel.MONSTER_LEVEL;
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            this.setNotified();
            return;
        }
        if (((BasicCharacterInfo)this.m_target).hasTemporarySpellInventory()) {
            ((BasicCharacterInfo)this.m_target).resetTemporarySpellInventory();
        }
        ((BasicCharacterInfo)this.m_target).changePlayerSpellsByMonsterOnes(this.m_value, this.m_spellLevel);
    }
    
    @Override
    public boolean canBeExecuted() {
        return this.m_target instanceof BasicCharacterInfo && super.canBeExecuted();
    }
    
    @Override
    public void unapplyOverride() {
        if (!this.m_executed) {
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        ((BasicCharacterInfo)this.m_target).resetPlayerSpellsByMonsterOnes(this.m_value);
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
    
    @Override
    public void onCheckIn() {
        this.m_value = -1;
        this.m_spellLevel = BasicCharacterInfo.MonsterSpellsLevel.MONSTER_LEVEL;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangePlayerSpellsByMonsterOnes>() {
            @Override
            public ChangePlayerSpellsByMonsterOnes makeObject() {
                return new ChangePlayerSpellsByMonsterOnes();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Id du mob, niveaux = niveau moyen du mob", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id du mob", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Id du mob, niveaux = niveau moyen du mob ou du joueur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id du mob", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveaux : 0 : niveau du mob, 1 = niveau du joueur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
