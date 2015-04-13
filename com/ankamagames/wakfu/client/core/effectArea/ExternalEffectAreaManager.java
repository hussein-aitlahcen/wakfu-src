package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ExternalEffectAreaManager extends BasicEffectAreaManager
{
    public ExternalEffectAreaManager(final EffectAreaActionListener listener, final EffectContext context) {
        super();
        this.setListener(listener);
        this.setContext(context);
    }
    
    @Override
    public void checkInAndOut(final int startx, final int starty, final short startz, final int arrivalx, final int arrivaly, final short arrivalz, final EffectUser applicant) {
    }
    
    @Override
    public BasicEffectArea instanceNewAreaFromBaseId(final long id) {
        final BasicEffectArea<WakfuEffect, EffectAreaParameters> model = StaticEffectAreaManager.getInstance().getAreaFromId(id);
        if (model == null) {
            ExternalEffectAreaManager.m_logger.error((Object)("pas de modele de zone d'effet d'id " + id));
            return null;
        }
        return model.instanceAnother(null);
    }
}
