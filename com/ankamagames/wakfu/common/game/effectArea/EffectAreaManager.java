package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class EffectAreaManager extends BasicEffectAreaManager
{
    private static ObjectPool m_staticpool;
    
    public static EffectAreaManager checkOut(final EffectAreaActionListener listener, final EffectContext context) {
        EffectAreaManager manager;
        try {
            manager = (EffectAreaManager)EffectAreaManager.m_staticpool.borrowObject();
            manager.setPool(EffectAreaManager.m_staticpool);
        }
        catch (Exception e) {
            EffectAreaManager.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type EffectAreaManager : " + e.getMessage()));
            manager = new EffectAreaManager();
            manager.setPool(null);
            manager.onCheckOut();
        }
        manager.setListener(listener);
        manager.setContext(context);
        return manager;
    }
    
    @Override
    public BasicEffectArea instanceNewAreaFromBaseId(final long id) {
        final BasicEffectArea model = StaticEffectAreaManager.getInstance().getAreaFromId(id);
        if (model == null) {
            EffectAreaManager.m_logger.error((Object)("pas de modele de zone d'effet d'id " + id));
            return null;
        }
        return model.instanceAnother(null);
    }
    
    static {
        EffectAreaManager.m_staticpool = new MonitoredPool(new ObjectFactory<EffectAreaManager>() {
            @Override
            public EffectAreaManager makeObject() {
                return new EffectAreaManager();
            }
        });
    }
}
