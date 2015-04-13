package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;

public abstract class InteractiveElementFactoryConfiguration<T extends MapInteractiveElement>
{
    protected static final Logger m_logger;
    private final TShortObjectHashMap<ObjectFactory<T>> m_typeFactories;
    
    public InteractiveElementFactoryConfiguration() {
        super();
        this.m_typeFactories = new TShortObjectHashMap<ObjectFactory<T>>();
    }
    
    public final void setFactories(final InteractiveElementDefinition<T>[] interactiveElementsEnum) {
        for (final InteractiveElementDefinition<T> def : interactiveElementsEnum) {
            this.m_typeFactories.put(def.getFactoryId(), def.getFactory());
        }
    }
    
    public final ObjectFactory<T> getFactory(final short typeId) {
        return this.m_typeFactories.get(typeId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementFactoryConfiguration.class);
    }
}
