package com.ankamagames.framework.graphics.engine.text;

import org.apache.log4j.*;
import java.awt.*;
import java.awt.font.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class GlyphVectorCache
{
    private static final Logger m_logger;
    public static final GlyphVectorCache INSTANCE;
    private RemoveUnusedProcedure m_removeUnusedProcedure;
    private static final int TIME_BEFORE_CLEANUP = 15000;
    private int m_timeSinceLastCleanUp;
    private final THashMap<Font, THashMap<String, CachedGlyphVector>> m_vectorMap;
    
    private GlyphVectorCache() {
        super();
        this.m_removeUnusedProcedure = new RemoveUnusedProcedure();
        this.m_timeSinceLastCleanUp = 0;
        this.m_vectorMap = new THashMap<Font, THashMap<String, CachedGlyphVector>>();
    }
    
    public GlyphVector getGlyphVector(final char value, final Font font, final FontRenderContext context) {
        return this.getGlyphVector(String.valueOf(value), font, context);
    }
    
    public GlyphVector getGlyphVector(final String value, final Font font, final FontRenderContext context) {
        THashMap<String, CachedGlyphVector> map = this.m_vectorMap.get(font);
        if (map == null) {
            map = new THashMap<String, CachedGlyphVector>();
            this.m_vectorMap.put(font, map);
        }
        CachedGlyphVector vector = map.get(value);
        if (vector == null) {
            vector = CachedGlyphVector.checkOut(font.createGlyphVector(context, value), value);
            map.put(value, vector);
        }
        vector.setUsed(true);
        return vector.getVector();
    }
    
    public void update(final int deltaTime) {
        this.m_timeSinceLastCleanUp += deltaTime;
        if (this.m_timeSinceLastCleanUp > 15000) {
            if (!this.m_vectorMap.isEmpty()) {
                this.m_vectorMap.forEachValue(new TObjectProcedure<THashMap<String, CachedGlyphVector>>() {
                    @Override
                    public boolean execute(final THashMap<String, CachedGlyphVector> object) {
                        object.forEachValue(GlyphVectorCache.this.m_removeUnusedProcedure);
                        return true;
                    }
                });
            }
            this.m_timeSinceLastCleanUp = 0;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GlyphVectorCache.class);
        INSTANCE = new GlyphVectorCache();
    }
    
    private static class CachedGlyphVector implements Releasable
    {
        private static final ObjectPool m_pool;
        private boolean m_used;
        private GlyphVector m_vector;
        private String m_value;
        
        public static CachedGlyphVector checkOut(final GlyphVector vector, final String value) {
            CachedGlyphVector c;
            try {
                c = (CachedGlyphVector)CachedGlyphVector.m_pool.borrowObject();
            }
            catch (Exception e) {
                GlyphVectorCache.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
                c = new CachedGlyphVector();
                c.onCheckOut();
            }
            c.m_vector = vector;
            c.m_value = value;
            return c;
        }
        
        public GlyphVector getVector() {
            return this.m_vector;
        }
        
        public void setUsed(final boolean used) {
            this.m_used = used;
        }
        
        public boolean isUsed() {
            return this.m_used;
        }
        
        private String getValue() {
            return this.m_value;
        }
        
        @Override
        public void onCheckOut() {
            this.m_used = true;
        }
        
        @Override
        public void onCheckIn() {
            this.m_vector = null;
            this.m_value = null;
        }
        
        @Override
        public void release() {
            try {
                CachedGlyphVector.m_pool.returnObject(this);
            }
            catch (Exception e) {
                this.onCheckIn();
            }
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<CachedGlyphVector>() {
                @Override
                public CachedGlyphVector makeObject() {
                    return new CachedGlyphVector();
                }
            });
        }
    }
    
    private class RemoveUnusedProcedure implements TObjectProcedure<CachedGlyphVector>
    {
        @Override
        public boolean execute(final CachedGlyphVector vector) {
            if (!vector.isUsed()) {
                final GlyphVector glyphVector = vector.getVector();
                final THashMap<String, CachedGlyphVector> map = GlyphVectorCache.this.m_vectorMap.get(glyphVector.getFont());
                if (map != null) {
                    map.remove(vector.getValue());
                }
                vector.release();
            }
            else {
                vector.setUsed(false);
            }
            return true;
        }
    }
}
