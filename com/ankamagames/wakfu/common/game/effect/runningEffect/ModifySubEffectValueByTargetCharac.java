package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class ModifySubEffectValueByTargetCharac extends ModifySubEffectValueByCharac
{
    private static final ObjectPool m_staticPool;
    
    private ModifySubEffectValueByTargetCharac() {
        super();
    }
    
    public ModifySubEffectValueByTargetCharac(final FighterCharacteristicType charac) {
        super();
        this.setTriggersToExecute();
        this.m_charac = charac;
    }
    
    @Override
    public ModifySubEffectValueByTargetCharac newInstance() {
        ModifySubEffectValueByTargetCharac re;
        try {
            re = (ModifySubEffectValueByTargetCharac)ModifySubEffectValueByTargetCharac.m_staticPool.borrowObject();
            re.m_pool = ModifySubEffectValueByTargetCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new ModifySubEffectValueByTargetCharac();
            re.m_pool = null;
            re.m_isStatic = false;
            ModifySubEffectValueByTargetCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un ModifySubEffectValueByCasterCharac : " + e.getMessage()));
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
        return this.m_target;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ModifySubEffectValueByTargetCharac>() {
            @Override
            public ModifySubEffectValueByTargetCharac makeObject() {
                return new ModifySubEffectValueByTargetCharac((ModifySubEffectValueByTargetCharac$1)null);
            }
        });
    }
}
