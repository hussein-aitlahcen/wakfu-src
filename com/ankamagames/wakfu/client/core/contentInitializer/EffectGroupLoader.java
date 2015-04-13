package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public final class EffectGroupLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final EffectGroupLoader m_instance;
    
    public static EffectGroupLoader getInstance() {
        return EffectGroupLoader.m_instance;
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        final EffectGroupManager manager = new EffectGroupManager();
        AbstractEffectGroupManager.setAbstractEffectGroupManager(manager);
        BinaryDocumentManager.getInstance().foreach(new EffectGroupBinaryData(), new LoadProcedure<EffectGroupBinaryData>() {
            @Override
            public void load(final EffectGroupBinaryData bs) {
                final EffectGroup effectGroup = new EffectGroup(bs.getId());
                for (final int effectId : bs.getEffectIds()) {
                    final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
                    if (effect != null) {
                        effectGroup.addEffect(effect);
                    }
                    else {
                        EffectGroupLoader.m_logger.error((Object)("Probl\u00e8me de chargmeent de effectGruop " + bs.getId()));
                    }
                }
                manager.addEffectGroup(effectGroup);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.effectGroups");
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectGroupLoader.class);
        m_instance = new EffectGroupLoader();
    }
}
