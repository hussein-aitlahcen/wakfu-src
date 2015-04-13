package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public final class InteractiveElementLoader implements ContentInitializer
{
    private WakfuClientInteractiveElementFactoryConfiguration m_config;
    private static final Logger m_logger;
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.interactiveElement");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        (this.m_config = new WakfuClientInteractiveElementFactoryConfiguration()).setFactories(WakfuClientInteractiveElementTypes.values());
        this.m_config.setViewFactories(WakfuClientInteractiveElementViewTypes.values());
        try {
            BinaryDocumentManager.getInstance().foreach(new InteractiveElementModelBinaryData(), new LoadProcedure<InteractiveElementModelBinaryData>() {
                @Override
                public void load(final InteractiveElementModelBinaryData ibs) {
                    final int viewModelId = ibs.getId();
                    final short viewTypeId = ibs.getType();
                    final int gfx = ibs.getGfx();
                    final int color = ibs.getColor();
                    final byte height = ibs.getHeight();
                    final int particleId = ibs.getParticleId();
                    final int particleOffsetZ = ibs.getParticleOffsetZ();
                    InteractiveElementLoader.this.m_config.setViewProperties(viewModelId, viewTypeId, gfx, height, color, particleId, particleOffsetZ);
                    if (InteractiveElementLoader.m_logger.isTraceEnabled()) {
                        InteractiveElementLoader.m_logger.trace((Object)("Loaded view id=" + viewModelId + " type=" + viewTypeId + " gfx=" + gfx + " color=" + color + " height=" + height));
                    }
                }
            });
        }
        catch (Exception e) {
            InteractiveElementLoader.m_logger.error((Object)"Erreur lors de la lecture du fichier de vues d'\u00e9l\u00e9ments interactifs", (Throwable)e);
        }
        ((InteractiveElementFactory<T, WakfuClientInteractiveElementFactoryConfiguration>)WakfuClientInteractiveElementFactory.getInstance()).configure(this.m_config);
        this.m_config = null;
        clientInstance.fireContentInitializerDone(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementLoader.class);
    }
}
