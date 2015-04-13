package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public final class TimelineBuffListLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.timeline.buff.list");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) {
        try {
            BinaryDocumentManager.getInstance().foreach(new TimelineBuffListBinaryData(), new LoadProcedure<TimelineBuffListBinaryData>() {
                @Override
                public void load(final TimelineBuffListBinaryData data) {
                    final int[] effectIds = data.getEffectIds();
                    if (effectIds.length > 0) {
                        final int effectId = effectIds[0];
                        final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
                        if (effect == null) {
                            TimelineBuffListLoader.m_logger.error((Object)("Probl\u00e8me de chargmeent de Timelinebuff " + data.getId()));
                        }
                        else {
                            TimelineBuffListManager.INSTANCE.addEffect(effect, data.getId());
                        }
                        TimelineBuffListManager.INSTANCE.addGfx(effectId, data.getGfxId());
                        TimelineBuffListManager.INSTANCE.setType(TimelineBuffType.getFromId(data.getTypeId()), effectId);
                        if (data.isForPlayer()) {
                            TimelineBuffListManager.INSTANCE.addPlayerEffect(effect);
                        }
                    }
                }
            });
        }
        catch (Exception e) {
            TimelineBuffListLoader.m_logger.error((Object)"", (Throwable)e);
        }
        clientInstance.fireContentInitializerDone(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimelineBuffListLoader.class);
    }
}
