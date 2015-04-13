package com.ankamagames.wakfu.common.game.nation.data;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.nio.*;

public class NationSerializer
{
    private static final Logger m_logger;
    private final Nation m_nation;
    
    public NationSerializer(final Nation nation) {
        super();
        this.m_nation = nation;
    }
    
    public final byte[] build(final NationSerializationType serializationType) {
        int totalSize = 1;
        for (final NationSerializationType.Part part : serializationType.getParts()) {
            final NationPart nationPart = this.m_nation.getPart(part);
            if (nationPart != null) {
                totalSize += nationPart.serializedSize();
            }
            else {
                NationSerializer.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration de la NationPart correspondant \u00e0 " + part + " de la forme " + serializationType));
            }
        }
        final ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.put((byte)serializationType.ordinal());
        for (final NationSerializationType.Part part2 : serializationType.getParts()) {
            final NationPart nationPart2 = this.m_nation.getPart(part2);
            if (nationPart2 != null) {
                nationPart2.serialize(buffer);
            }
            else {
                NationSerializer.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration de la NationPart correspondant \u00e0 " + nationPart2 + " de la forme " + serializationType));
            }
        }
        return buffer.array();
    }
    
    public final void fromBuild(final byte[] data, final int version) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        final int serializationNumber = buffer.get() & 0xFF;
        if (serializationNumber < 0 || serializationNumber >= NationSerializationType.values().length) {
            NationSerializer.m_logger.error((Object)("Num\u00e9ro de part invalide : " + serializationNumber));
            return;
        }
        final NationSerializationType serializationType = NationSerializationType.values()[serializationNumber];
        for (final NationSerializationType.Part part : serializationType.getParts()) {
            final NationPart nationPart = this.m_nation.getPart(part);
            if (nationPart != null) {
                nationPart.unSerialize(buffer, version);
                nationPart.fireDataChanged();
            }
            else {
                NationSerializer.m_logger.error((Object)("Impossible de trouver la NationPart correspondant \u00e0 " + part));
            }
        }
    }
    
    public byte[] serializePart(final NationPart part) {
        final ByteBuffer buffer = ByteBuffer.allocate(part.serializedSize());
        part.serialize(buffer);
        return buffer.array();
    }
    
    public void unSerializePart(final NationPart part, final byte[] data, final int version) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        part.unSerialize(buffer, version);
        part.fireDataChanged();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationSerializer.class);
    }
}
