package com.ankamagames.wakfu.client.alea.ambiance.core;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class AbstractWakfuEffectBlockFactory extends EffectBlockFactory
{
    private static final Logger m_logger;
    
    @Override
    protected final void fill() {
        final WakfuEffectBlockEnum[] arr$;
        final WakfuEffectBlockEnum[] values = arr$ = WakfuEffectBlockEnum.values();
        for (final WakfuEffectBlockEnum c : arr$) {
            AbstractModel model = WakfuEffectBlockEnum.createModel(c);
            if (model == null) {
                model = this.createModel(c);
            }
            if (model != null) {
                if (model.getBlockType() != c.getBlockType()) {
                    AbstractWakfuEffectBlockFactory.m_logger.error((Object)("type incorrect pour le model " + model.getClass().getSimpleName() + " " + c.name()));
                }
                else {
                    this.add(model);
                }
            }
        }
    }
    
    protected abstract AbstractModel createModel(final WakfuEffectBlockEnum p0);
    
    static {
        m_logger = Logger.getLogger((Class)AbstractWakfuEffectBlockFactory.class);
    }
}
