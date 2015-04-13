package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;

public final class InteractiveElementTemplatesLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final InteractiveElementTemplatesLoader m_instance;
    private boolean m_errors;
    
    public static InteractiveElementTemplatesLoader getInstance() {
        return InteractiveElementTemplatesLoader.m_instance;
    }
    
    private InteractiveElementTemplatesLoader() {
        super();
        this.m_errors = false;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.interactiveElementTemplates");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        try {
            BinaryDocumentManager.getInstance().foreach(new InteractiveElementTemplateBinaryData(), new LoadProcedure<InteractiveElementTemplateBinaryData>() {
                @Override
                public void load(final InteractiveElementTemplateBinaryData data) {
                    final byte[] serialized = InteractiveElementTemplatesLoader.this.serialized(data);
                    WakfuClientInteractiveElementFactory.getInstance().addInfo(new InteractiveElementInfo(data.getId(), data.getModelType(), serialized), data.getViews());
                }
            });
        }
        catch (Exception e) {
            InteractiveElementTemplatesLoader.m_logger.error((Object)"Erreur lors de la lecture du fichier de vues d'\u00e9l\u00e9ments interactifs", (Throwable)e);
        }
        clientInstance.fireContentInitializerDone(this);
    }
    
    private byte[] serialized(final InteractiveElementTemplateBinaryData data) {
        final ArrayList<Point3> positionsTrigger = new ArrayList<Point3>();
        for (final InteractiveElementTemplateBinaryData.Point3 pos : data.getPositionsTrigger()) {
            positionsTrigger.add(new Point3(pos.getX(), pos.getY(), pos.getZ()));
        }
        final ClientInteractiveElementExport export = new ClientInteractiveElementExport(data.getWorldId(), data.getX(), data.getY(), data.getZ(), data.getInitialState(), data.isInitiallyVisible(), data.isInitiallyUsable(), data.isBlockingMovement(), data.isBlockingLos(), data.getDirection(), data.getActivationPattern(), positionsTrigger, data.getParameter(), data.getActions(), data.getProperties(), data.getTemplateId());
        return export.serialize();
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementTemplatesLoader.class);
        m_instance = new InteractiveElementTemplatesLoader();
    }
}
