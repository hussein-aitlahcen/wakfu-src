package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class ModifySubEffectValueByCasterCharac extends ModifySubEffectValueByCharac
{
    private static final ObjectPool m_staticPool;
    
    private ModifySubEffectValueByCasterCharac() {
        super();
    }
    
    public ModifySubEffectValueByCasterCharac(final FighterCharacteristicType charac) {
        super();
        this.setTriggersToExecute();
        this.m_charac = charac;
    }
    
    @Override
    public ModifySubEffectValueByCasterCharac newInstance() {
        ModifySubEffectValueByCasterCharac re;
        try {
            re = (ModifySubEffectValueByCasterCharac)ModifySubEffectValueByCasterCharac.m_staticPool.borrowObject();
            re.m_pool = ModifySubEffectValueByCasterCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new ModifySubEffectValueByCasterCharac();
            re.m_pool = null;
            re.m_isStatic = false;
            ModifySubEffectValueByCasterCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un ModifySubEffectValueByCasterCharac : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public boolean useCaster() {
        return true;
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
    protected EffectUser getCharacHolder() {
        return this.m_caster;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ModifySubEffectValueByCasterCharac>() {
            @Override
            public ModifySubEffectValueByCasterCharac makeObject() {
                return new ModifySubEffectValueByCasterCharac((ModifySubEffectValueByCasterCharac$1)null);
            }
        });
    }
}
