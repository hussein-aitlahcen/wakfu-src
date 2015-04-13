package com.ankamagames.baseImpl.common.clientAndServer.alea.environment;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public abstract class InteractiveElementExport extends BinarSerial
{
    protected final int m_x;
    protected final int m_y;
    protected final short m_z;
    protected final short m_worldId;
    protected final short m_initialState;
    protected final boolean m_initiallyVisible;
    protected final boolean m_initiallyUsable;
    protected final boolean m_blockingMovement;
    protected final boolean m_blockingLos;
    protected final byte m_direction;
    protected final short m_activationPattern;
    protected final ArrayList<Point3> m_positionsTrigger;
    protected final String m_parameter;
    protected final int m_templateId;
    protected final int[] m_properties;
    private final BinarSerialPart GLOBAL_DATA;
    private final BinarSerialPart SPECIFIC_DATA;
    private final BinarSerialPart SHARED_DATA;
    private final BinarSerialPart PERSISTANCE_DATA;
    private final BinarSerialPart[] PARTS;
    
    protected abstract BinarSerialPart createGlobalData();
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return this.PARTS;
    }
    
    public byte[] serialize() {
        return this.build(this.GLOBAL_DATA, this.SPECIFIC_DATA);
    }
    
    public InteractiveElementExport(final short worldId, final int x, final int y, final short z, final short initialState, final boolean initiallyVisible, final boolean initiallyUsable, final boolean blockingMovement, final boolean blockingLos, final byte direction, final short activationPattern, final ArrayList<Point3> positions, final String parameter, final int[] properties, final int templateId) {
        super();
        this.m_positionsTrigger = new ArrayList<Point3>();
        this.GLOBAL_DATA = this.createGlobalData();
        this.SPECIFIC_DATA = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putShort(InteractiveElementExport.this.m_worldId);
                buffer.putInt(InteractiveElementExport.this.m_x);
                buffer.putInt(InteractiveElementExport.this.m_y);
                buffer.putShort(InteractiveElementExport.this.m_z);
                buffer.putShort(InteractiveElementExport.this.m_initialState);
                buffer.put((byte)(InteractiveElementExport.this.m_initiallyVisible ? 1 : 0));
                buffer.put((byte)(InteractiveElementExport.this.m_initiallyUsable ? 1 : 0));
                buffer.put(InteractiveElementExport.this.m_direction);
                buffer.putShort(InteractiveElementExport.this.m_activationPattern);
                buffer.putShort((short)InteractiveElementExport.this.m_positionsTrigger.size());
                for (final Point3 pos : InteractiveElementExport.this.m_positionsTrigger) {
                    buffer.putInt(pos.getX());
                    buffer.putInt(pos.getY());
                    buffer.putShort(pos.getZ());
                }
                final byte[] parameters = StringUtils.toUTF8(InteractiveElementExport.this.m_parameter);
                assert parameters.length < 65535 : "chaine des parametres trop longue";
                buffer.putShort((short)(parameters.length & 0xFFFF));
                buffer.put(parameters);
                buffer.put((byte)InteractiveElementExport.this.m_properties.length);
                for (int i = 0; i < InteractiveElementExport.this.m_properties.length; ++i) {
                    buffer.put((byte)InteractiveElementExport.this.m_properties[i]);
                }
                buffer.putInt(InteractiveElementExport.this.m_templateId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int expectedSize() {
                final byte[] parameters = StringUtils.toUTF8(InteractiveElementExport.this.m_parameter);
                return 23 + (2 + 10 * InteractiveElementExport.this.m_positionsTrigger.size()) + (2 + parameters.length) + (1 + InteractiveElementExport.this.m_properties.length);
            }
        };
        this.SHARED_DATA = BinarSerialPart.EMPTY;
        this.PERSISTANCE_DATA = BinarSerialPart.EMPTY;
        this.PARTS = new BinarSerialPart[] { this.GLOBAL_DATA, this.SPECIFIC_DATA, this.SHARED_DATA, this.PERSISTANCE_DATA };
        this.m_worldId = worldId;
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_initialState = initialState;
        this.m_initiallyVisible = initiallyVisible;
        this.m_initiallyUsable = initiallyUsable;
        this.m_blockingMovement = blockingMovement;
        this.m_blockingLos = blockingLos;
        this.m_direction = direction;
        this.m_activationPattern = activationPattern;
        this.m_positionsTrigger.addAll(positions);
        this.m_parameter = parameter;
        this.m_properties = properties;
        this.m_templateId = templateId;
    }
}
