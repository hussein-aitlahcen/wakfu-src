package com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.conditions.attributesReaderWriter.*;
import java.io.*;

public abstract class ConditionAttributesRW<T extends AffectorCondition> extends AttributesReaderWriter<T>
{
    private static final Logger m_logger;
    protected static final byte LIFE_CONDITION_ID = 1;
    protected static final byte POSITION_CONDITION_ID = 2;
    
    public static AffectorCondition fromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final byte type = istream.readByte();
        switch (type) {
            case 1: {
                return LifeConditionAttributesRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 2: {
                return PositionConditionAttributesRW.m_instance.createFromStream(istream, levelPercent);
            }
            default: {
                ConditionAttributesRW.m_logger.error((Object)("type de condition inconnu " + type));
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ConditionAttributesRW.class);
    }
}
