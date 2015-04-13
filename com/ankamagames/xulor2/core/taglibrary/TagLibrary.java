package com.ankamagames.xulor2.core.taglibrary;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.xulor2.core.factory.*;
import java.util.*;

public abstract class TagLibrary
{
    private static final Logger m_logger;
    protected final THashMap<String, Factory<?>> m_tags;
    protected final THashMap<Class<?>, Factory<?>> m_classToFactoryMap;
    
    protected TagLibrary() {
        super();
        this.m_tags = new THashMap<String, Factory<?>>();
        this.m_classToFactoryMap = new THashMap<Class<?>, Factory<?>>();
        this.registerTags();
    }
    
    protected abstract void registerTags();
    
    public void registerTag(final String name, final Class<?> template) {
        this.registerTag(name.toLowerCase(), new DefaultFactory(template));
    }
    
    public void registerTag(final String name, final Factory<?> factory) {
        if (this.m_tags.containsKey(name.toLowerCase())) {
            TagLibrary.m_logger.error((Object)("le tag (name=" + name + ") est d\u00e9j\u00e0 utilis\u00e9 !"));
            return;
        }
        this.m_tags.put(name.toLowerCase(), factory);
        this.m_classToFactoryMap.put(factory.getTemplate(), factory);
    }
    
    public boolean unregisterTag(final String name) {
        final Factory<?> factory = this.m_tags.remove(name);
        if (factory != null) {
            this.m_classToFactoryMap.remove(factory.getTemplate());
        }
        return null != factory;
    }
    
    public Map<String, Factory<?>> getTagClasses() {
        return this.m_tags;
    }
    
    public Factory<?> getFactory(final String name) {
        return this.m_tags.get(name.toLowerCase());
    }
    
    public Factory<?> getFactory(final Class<?> template) {
        return this.m_classToFactoryMap.get(template);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TagLibrary.class);
    }
}
