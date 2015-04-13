package com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter.*;
import java.io.*;

public abstract class AffectorAttributesRW<T extends Affector> extends AttributesReaderWriter<T>
{
    private static final Logger m_logger;
    protected static final byte ATTRACTION_FORCE_ID = 1;
    protected static final byte BOOST_FORCE_ID = 2;
    protected static final byte CIRCLE_PATH_ID = 3;
    protected static final byte COLOR_FADER_ID = 4;
    protected static final byte DEFORMER_ID = 5;
    protected static final byte DIRECTION_FOLLOWER_ID = 6;
    protected static final byte FRICTIONAL_FORCE_ID = 7;
    protected static final byte LINEAR_FORCE_ID = 8;
    protected static final byte REBOUND_ID = 9;
    protected static final byte ROTOR_FORCE_ID = 10;
    protected static final byte LIGHT_RADIUS_DEFORMER_ID = 11;
    protected static final byte CURVE_ID = 12;
    protected static final byte ROTATION_ID = 13;
    protected static final byte ROTATION_INTERPOLATION_ID = 14;
    
    public static Affector fromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final byte type = istream.readByte();
        switch (type) {
            case 1: {
                return AttractionForceAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 2: {
                return BoostForceAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 3: {
                return CirclePathAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 4: {
                return ColorFaderAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 5: {
                return DeformerAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 6: {
                return DirectionFollowerAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 7: {
                return FrictionalForceAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 8: {
                return LinearForceAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 9: {
                return ReboundAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 10: {
                return RotorForceAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 11: {
                return LightRadiusDeformerAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 12: {
                return CurveAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 13: {
                return RotationAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            case 14: {
                return RotationInterpolationAttributeRW.m_instance.createFromStream(istream, levelPercent);
            }
            default: {
                AffectorAttributesRW.m_logger.error((Object)("type d'affecteur inconnu " + type));
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AffectorAttributesRW.class);
    }
}
