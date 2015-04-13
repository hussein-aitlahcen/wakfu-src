package com.ankamagames.wakfu.common.game.interactiveElements;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.external.*;
import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.nio.*;
import java.util.*;

public enum WakfuInteractiveElementProperty implements InteractiveElementProperty, ExportableEnum
{
    CHALLENGE_IE(0, "Element de Challenge"), 
    ALMANACH_IE(1, "Element d'almanach ");
    
    private static final Logger m_logger;
    private final byte m_id;
    private final String m_description;
    
    private WakfuInteractiveElementProperty(final int id, final String description) {
        this.m_id = (byte)id;
        this.m_description = description;
    }
    
    @Nullable
    public static WakfuInteractiveElementProperty getProperty(final byte id) {
        final WakfuInteractiveElementProperty[] props = values();
        for (int i = 0; i < props.length; ++i) {
            final WakfuInteractiveElementProperty prop = props[i];
            if (prop.m_id == id) {
                return prop;
            }
        }
        return null;
    }
    
    @Override
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static ArrayList<InteractiveElementProperty> readProperties(final ByteBuffer buffer) {
        final int propertiesCount = buffer.get() & 0xFF;
        if (propertiesCount == 0) {
            return null;
        }
        final ArrayList<InteractiveElementProperty> properties = new ArrayList<InteractiveElementProperty>(propertiesCount);
        for (int i = 0; i < propertiesCount; ++i) {
            final int propId = buffer.get() & 0xFF;
            final WakfuInteractiveElementProperty prop = getProperty((byte)propId);
            if (prop != null) {
                properties.add(prop);
            }
            else {
                WakfuInteractiveElementProperty.m_logger.warn((Object)("propri\u00e9t\u00e9 incoonu " + propId));
            }
        }
        return properties;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuInteractiveElementProperty.class);
    }
}
