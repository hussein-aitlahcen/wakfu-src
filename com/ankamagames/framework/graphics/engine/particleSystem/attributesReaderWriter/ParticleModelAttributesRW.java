package com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.models.attributesReaderWriter.*;
import java.io.*;

public abstract class ParticleModelAttributesRW<T extends ParticleModel> extends AttributesReaderWriter<T>
{
    private static final Logger m_logger;
    protected static final byte PARTICLE_BITMAP_MODEL_ID = 1;
    protected static final byte PARTICLE_BITMAP_SEQUENCE_MODEL_ID = 2;
    
    public static ParticleModel fromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final byte type = istream.readByte();
        switch (type) {
            case 1: {
                return ParticleBitmapModelAttributesRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 2: {
                return ParticleBitmapSequenceModelAttributesRW.m_instance.createFromStream(istream, levelPercent);
            }
            default: {
                ParticleModelAttributesRW.m_logger.error((Object)("type de condition inconnu " + type));
                return null;
            }
        }
    }
    
    @Override
    protected boolean equals(final T min, final T max) {
        return min.m_hotX == max.m_hotX && min.m_hotY == max.m_hotY && min.m_scaleX == max.m_scaleX && min.m_scaleY == max.m_scaleY && min.m_rotation == max.m_rotation && min.m_scaleRandomX == max.m_scaleRandomX && min.m_scaleRandomY == max.m_scaleRandomY && min.m_scaleRandomKeepRatio == max.m_scaleRandomKeepRatio && min.m_rotationRandom == max.m_rotationRandom && min.m_redColor == max.m_redColor && min.m_greenColor == max.m_greenColor && min.m_blueColor == max.m_blueColor && min.m_alphaColor == max.m_alphaColor && min.m_redColorRandom == max.m_redColorRandom && min.m_greenColorRandom == max.m_greenColorRandom && min.m_blueColorRandom == max.m_blueColorRandom && min.m_alphaColorRandom == max.m_alphaColorRandom;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParticleModelAttributesRW.class);
    }
}
