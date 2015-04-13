package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;

public abstract class InteractiveElementFactory<T extends MapInteractiveElement, C extends InteractiveElementFactoryConfiguration<T>>
{
    protected static final Logger m_logger;
    protected C m_configuration;
    protected final TLongObjectHashMap<InteractiveElementInfo> m_infos;
    
    public InteractiveElementFactory() {
        super();
        this.m_infos = new TLongObjectHashMap<InteractiveElementInfo>();
    }
    
    public final void configure(final C configuration) {
        this.m_configuration = configuration;
    }
    
    public C getConfiguration() {
        return this.m_configuration;
    }
    
    public final InteractiveElementInfo getInfo(final long id) {
        return this.m_infos.get(id);
    }
    
    public final void addInfo(final InteractiveElementInfo info) {
        this.m_infos.put(info.getId(), info);
    }
    
    public final void removeInfo(final long id) {
        this.m_infos.remove(id);
    }
    
    public T createInteractiveElement(final long instanceId) {
        return this.createInteractiveElement(instanceId, false);
    }
    
    public T createInteractiveElement(final long instanceId, final boolean isDummy) {
        try {
            final InteractiveElementInfo info = this.m_infos.get(instanceId);
            if (info == null) {
                InteractiveElementFactory.m_logger.error((Object)("Aucune d\u00e9finition trouv\u00e9e pour l'instance d'\u00e9lement interactif " + instanceId));
                return null;
            }
            final T element = this.createInteractiveElement(instanceId, info.m_type, info.m_data, isDummy);
            this.setAdditionalData(element, info);
            return element;
        }
        catch (RuntimeException e) {
            InteractiveElementFactory.m_logger.error((Object)("Exception lors de InteractiveElementFactory.createInteractiveElement(" + instanceId + ")"), (Throwable)e);
            return null;
        }
    }
    
    public T createDummyInteractiveElement(final long instanceId, final short type, final byte[] data) {
        return this.createInteractiveElement(instanceId, type, data, true);
    }
    
    private T createInteractiveElement(final long instanceId, final short type, final byte[] data, final boolean isDummy) {
        final ObjectFactory<T> factory = this.m_configuration.getFactory(type);
        if (factory == null) {
            InteractiveElementFactory.m_logger.error((Object)("Aucune factory d'enregistr\u00e9e pour un \u00e9l\u00e9ment interactif de type " + type));
            return null;
        }
        final T element = factory.makeObject();
        if (isDummy) {
            element.setIsDummy();
        }
        element.setId(instanceId);
        element.setModelId(type);
        element.fromBuild(data);
        return element;
    }
    
    protected abstract void setAdditionalData(final T p0, final InteractiveElementInfo p1);
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementFactory.class);
    }
}
