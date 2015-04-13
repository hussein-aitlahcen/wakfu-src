package com.ankamagames.framework.graphics.engine.particleSystem;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper.*;
import com.ankamagames.framework.graphics.engine.states.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;

public class ParticleSystemLoader
{
    private static final Logger m_logger;
    private static final int VERSION = 1;
    public static final int LEVEL_MIN = 1;
    public static final int LEVEL_MAX = 100;
    
    public static short getCurrentVersion() {
        return 20481;
    }
    
    public static void load(final String fileName, final ParticleSystem system, final int level) throws Exception {
        final ExtendedDataInputStream istream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(fileName));
        final short version = istream.readShort();
        if (version != getCurrentVersion()) {
            throw new Exception("fichier incorrect " + fileName);
        }
        load(istream, system, level);
    }
    
    public static void load(final byte[] data, final ParticleSystem system, final int level) throws Exception {
        final ExtendedDataInputStream istream = ExtendedDataInputStream.wrap(data);
        final short version = istream.readShort();
        load(istream, system, level);
    }
    
    public static void load(final ExtendedDataInputStream istream, final ParticleSystem system, int level) throws Exception {
        if (level < 1) {
            level = 1;
        }
        else if (level > 100) {
            level = 100;
        }
        readSystemAttributes(system, level, istream);
        for (int emitterCount = istream.readByte() & 0xFF, e = 0; e < emitterCount; ++e) {
            final EmitterDefinition emitterDef = loadEmitter(istream, level);
            if (emitterDef != null) {
                system.addEmitterDefinition(emitterDef);
            }
        }
        istream.close();
    }
    
    private static void readSystemAttributes(final ParticleSystem system, final int level, final ExtendedDataInputStream istream) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final boolean geocentric = istream.readBooleanBit();
        final boolean behindMobile = istream.readBooleanBit();
        final boolean mustApplyNightColor = istream.readBooleanBit();
        final int srcBlend = istream.readInt();
        final int dstBlend = istream.readInt();
        final long textureId = istream.readLong();
        final int duration = AttributesReaderWriter.readUnsignedShort(istream, leveled, level);
        final byte renderRadius = istream.readByte();
        system.setGeocentric(geocentric);
        system.setDuration(duration);
        system.setRenderRadius(renderRadius);
        system.m_behindMobile = behindMobile;
        final ColorHelperProvider provider = new ColorHelperProvider(mustApplyNightColor ? new DelayedColor() : ImmediateColor.getInstance());
        system.setColorHelperProvider(provider);
        system.m_textureId = textureId;
        system.setBlendMode(BlendModes.fromOGL(srcBlend), BlendModes.fromOGL(dstBlend));
    }
    
    private static EmitterDefinition loadEmitter(final ExtendedDataInputStream istream, final float level) throws Exception {
        final float minLevel = istream.readByte();
        final float maxLevel = istream.readByte();
        final int dataOffset = istream.readInt();
        if (level < minLevel || level > maxLevel) {
            istream.skip(dataOffset);
            return null;
        }
        final float levelPercent = (level - minLevel) / (maxLevel - minLevel);
        final EmitterDefinition emitterDef = (EmitterDefinition)AffectorableAttributesRW.fromStream(istream, levelPercent);
        final byte modelCount = istream.readByte();
        for (int i = 0; i < modelCount; ++i) {
            final ParticleModel model = ParticleModelAttributesRW.fromStream(istream, levelPercent);
            emitterDef.addParticleModel(model);
        }
        loadAffector(istream, levelPercent, emitterDef);
        final byte lightCount = istream.readByte();
        for (int j = 0; j < lightCount; ++j) {
            final LightDefinition lightDef = (LightDefinition)AffectorableAttributesRW.fromStream(istream, levelPercent);
            loadAffector(istream, levelPercent, lightDef);
            emitterDef.setLightDefinition(lightDef);
        }
        final byte subEmitterCount = istream.readByte();
        for (int k = 0; k < subEmitterCount; ++k) {
            final EmitterDefinition subEmitterDef = loadEmitter(istream, level);
            emitterDef.addEmitterDefinition(subEmitterDef);
        }
        return emitterDef;
    }
    
    private static void loadAffector(final ExtendedDataInputStream istream, final float levelPercent, final Affectorable affectorable) throws Exception {
        final byte affectorCount = istream.readByte();
        for (int i = 0; i < affectorCount; ++i) {
            final Affector affector = AffectorAttributesRW.fromStream(istream, levelPercent);
            final byte conditionCount = istream.readByte();
            if (conditionCount == 0) {
                affector.addCondition(null);
            }
            else {
                final AffectorCondition[] conditions = new AffectorCondition[conditionCount];
                for (int c = 0; c < conditionCount; ++c) {
                    conditions[c] = ConditionAttributesRW.fromStream(istream, levelPercent);
                }
                affector.addCondition(conditions);
            }
            if (affector.isKeyFramedAffector()) {
                affectorable.addKeyFramedAffector(affector);
            }
            else {
                affectorable.addAffector(affector);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParticleSystemLoader.class);
    }
}
