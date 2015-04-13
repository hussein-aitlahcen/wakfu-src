package com.ankamagames.wakfu.common.game.effect.runningEffect;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class AddSpellToInventory extends WakfuRunningEffect
{
    private static final Logger m_logger;
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_spellId;
    private short m_spellLevel;
    private AbstractSpellLevel m_spellLevelToAdd;
    public BinarSerialPart SPELL_INFO;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AddSpellToInventory.PARAMETERS_LIST_SET;
    }
    
    public AddSpellToInventory() {
        super();
        this.SPELL_INFO = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer bb) {
                bb.putInt(AddSpellToInventory.this.m_spellId);
                bb.putShort(AddSpellToInventory.this.m_spellLevel);
            }
            
            @Override
            public void unserialize(final ByteBuffer bb, final int version) {
                AddSpellToInventory.this.m_spellId = bb.getInt();
                AddSpellToInventory.this.m_spellLevel = bb.getShort();
            }
            
            @Override
            public int expectedSize() {
                return 6;
            }
        };
    }
    
    @Override
    public AddSpellToInventory newInstance() {
        AddSpellToInventory re;
        try {
            re = (AddSpellToInventory)AddSpellToInventory.m_staticPool.borrowObject();
            re.m_pool = AddSpellToInventory.m_staticPool;
        }
        catch (Exception e) {
            re = new AddSpellToInventory();
            re.m_pool = null;
            re.m_isStatic = false;
            AddSpellToInventory.m_logger.error((Object)("Erreur lors d'un checkOut sur un AddSpellToInventory : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_spellId = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_spellLevel = (short)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else {
            this.m_spellLevel = this.getContainerLevel();
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        this.m_spellLevelToAdd = AbstractSpellManager.getInstance().getDefaultSpellLevel(this.m_spellId, this.m_spellLevel);
        if (this.m_spellLevelToAdd == null) {
            AddSpellToInventory.m_logger.error((Object)("Impossible de cr\u00e9er le sort d'id " + this.m_spellId + " pour le level " + this.m_spellLevel));
            return;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        final SpellInventory<AbstractSpellLevel> targetSpellInventory = (SpellInventory<AbstractSpellLevel>)target.getSpellInventory();
        try {
            if (!targetSpellInventory.contains(this.m_spellLevelToAdd)) {
                targetSpellInventory.add(this.m_spellLevelToAdd);
            }
        }
        catch (Exception e) {
            AddSpellToInventory.m_logger.error((Object)"Exception levee", (Throwable)e);
            this.m_spellLevelToAdd.release();
            this.m_spellLevelToAdd = null;
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_spellLevelToAdd == null) {
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        final SpellInventory<AbstractSpellLevel> targetSpellInventory = (SpellInventory<AbstractSpellLevel>)target.getSpellInventory();
        targetSpellInventory.remove(this.m_spellLevelToAdd);
        this.m_spellLevelToAdd.release();
        super.unapplyOverride();
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
        super.onCheckIn();
        this.m_spellId = 0;
        this.m_spellLevel = 0;
        this.m_spellLevelToAdd = null;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.SPELL_INFO;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddSpellToInventory.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<AddSpellToInventory>() {
            @Override
            public AddSpellToInventory makeObject() {
                return new AddSpellToInventory();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Sort a ajouter (level = level du container)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Sort a ajouter", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Level", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
