package com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.definitions.attributesReaderWriter.*;
import java.io.*;

public abstract class AffectorableAttributesRW<T extends Affectorable> extends AttributesReaderWriter<T>
{
    private static final Logger m_logger;
    protected static final byte EMITTER_DEFINITION_ID = 1;
    protected static final byte LIGHT_DEFINITION_ID = 2;
    
    public static Affectorable fromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final byte type = istream.readByte();
        switch (type) {
            case 1: {
                return EmitterDefinitionAttributesRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 2: {
                return LightDefinitionAttributesRW.m_instance.createFromStream(istream, levelPercent);
            }
            default: {
                AffectorableAttributesRW.m_logger.error((Object)("type de definition inconnu " + type));
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AffectorAttributesRW.class);
    }
}
