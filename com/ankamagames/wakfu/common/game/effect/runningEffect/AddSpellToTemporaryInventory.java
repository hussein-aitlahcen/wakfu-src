package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class AddSpellToTemporaryInventory extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private short m_spellLevel;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AddSpellToTemporaryInventory.PARAMETERS_LIST_SET;
    }
    
    public AddSpellToTemporaryInventory() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(2) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putShort(AddSpellToTemporaryInventory.this.m_spellLevel);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                AddSpellToTemporaryInventory.this.m_spellLevel = buffer.getShort();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public AddSpellToTemporaryInventory newInstance() {
        AddSpellToTemporaryInventory re;
        try {
            re = (AddSpellToTemporaryInventory)AddSpellToTemporaryInventory.m_staticPool.borrowObject();
            re.m_pool = AddSpellToTemporaryInventory.m_staticPool;
        }
        catch (Exception e) {
            re = new AddSpellToTemporaryInventory();
            re.m_pool = null;
            re.m_isStatic = false;
            re.m_spellLevel = 0;
            AddSpellToTemporaryInventory.m_logger.error((Object)("Erreur lors d'un checkOut sur un AddSpellToTemporaryInventory : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final short level = (short)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        switch (level) {
            case -1: {
                if (this.getTarget() != null && this.getTarget() instanceof BasicFighter) {
                    this.m_spellLevel = ((BasicFighter)this.getTarget()).getLevel();
                    break;
                }
                this.m_spellLevel = 0;
                break;
            }
            case -2: {
                if (this.getCaster() != null && this.getCaster() instanceof BasicFighter) {
                    this.m_spellLevel = ((BasicFighter)this.getCaster()).getLevel();
                    break;
                }
                this.m_spellLevel = 0;
                break;
            }
            case -3: {
                this.m_spellLevel = this.getContainerLevel();
                break;
            }
            default: {
                this.m_spellLevel = level;
                break;
            }
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
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        if (!target.hasTemporarySpellInventory()) {
            target.createTemporaryInventory();
        }
        final boolean success = target.addSpellToTemporaryInventory(this.m_value, this.m_spellLevel);
        if (!success) {
            AddSpellToTemporaryInventory.m_logger.info((Object)("Unable to add spell " + this.m_value + " to temporary inventory of " + this.m_target));
            this.setNotified();
        }
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
        this.m_value = 0;
        this.m_spellLevel = 0;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<AddSpellToTemporaryInventory>() {
            @Override
            public AddSpellToTemporaryInventory makeObject() {
                return new AddSpellToTemporaryInventory();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Id du sort \u00e0 ajouter, et niveau", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id du sort", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau : -1 = niveau de la cible, -2 = niveau du caster, -3 = niveau du sort courant", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
