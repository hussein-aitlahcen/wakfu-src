package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagShelf extends MerchantDisplay
{
    protected static final Logger m_logger;
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagShelf.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public DimensionalBagShelf makeObject() {
            DimensionalBagShelf element;
            try {
                element = (DimensionalBagShelf)Factory.m_pool.borrowObject();
                element.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagShelf.m_logger.error((Object)"Erreur lors de l'extraction d'un DimensionalBagShelf du pool", (Throwable)e);
                element = new DimensionalBagShelf();
            }
            return element;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagShelf>() {
                @Override
                public DimensionalBagShelf makeObject() {
                    return new DimensionalBagShelf();
                }
            });
        }
    }
}
