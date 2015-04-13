package com.ankamagames.wakfu.client.core.preferences;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;

public class WakfuGraphicalPresets
{
    public enum Level implements FieldProvider
    {
        CUSTOM((byte)0), 
        LOW((byte)1), 
        MEDIUM((byte)2), 
        HIGH((byte)3);
        
        private static final String NAME = "name";
        private final byte m_id;
        
        private Level(final byte id) {
            this.m_id = id;
        }
        
        public byte getId() {
            return this.m_id;
        }
        
        public static Level getFromId(final byte id) {
            for (final Level l : values()) {
                if (l.m_id == id) {
                    return l;
                }
            }
            return null;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return WakfuTranslator.getInstance().getString("options.graphicalPresetsLevel." + this.m_id);
            }
            return null;
        }
        
        @Override
        public void setFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void prependFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void appendFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public boolean isFieldSynchronisable(final String fieldName) {
            return false;
        }
    }
}
