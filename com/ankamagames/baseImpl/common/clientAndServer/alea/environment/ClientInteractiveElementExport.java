package com.ankamagames.baseImpl.common.clientAndServer.alea.environment;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;

public class ClientInteractiveElementExport extends InteractiveElementExport
{
    private final TShortIntHashMap m_actions;
    private final BinarSerialPart LAND_MARK_DATA;
    
    @Override
    protected BinarSerialPart createGlobalData() {
        return new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (ClientInteractiveElementExport.this.m_actions == null) {
                    buffer.put((byte)0);
                    return;
                }
                final int nActions = ClientInteractiveElementExport.this.m_actions.size();
                if (nActions > 127) {
                    ClientInteractiveElementExport$1.m_logger.error((Object)("Nombre d'actions " + nActions + " > 255"));
                    return;
                }
                buffer.put((byte)nActions);
                final TShortIntIterator it = ClientInteractiveElementExport.this.m_actions.iterator();
                while (it.hasNext()) {
                    it.advance();
                    buffer.putShort(it.key());
                    buffer.putInt(it.value());
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public ClientInteractiveElementExport(final short worldId, final int x, final int y, final short z, final short initialState, final boolean initiallyVisible, final boolean initiallyUsable, final boolean blockingMovement, final boolean blockingLos, final byte direction, final short activationPattern, final ArrayList<Point3> positions, final String parameter, final TShortIntHashMap actions, final int[] properties, final int templateId) {
        super(worldId, x, y, z, initialState, initiallyVisible, initiallyUsable, blockingMovement, blockingLos, direction, activationPattern, positions, parameter, properties, templateId);
        this.LAND_MARK_DATA = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putShort(ClientInteractiveElementExport.this.m_worldId);
                buffer.putInt(ClientInteractiveElementExport.this.m_x);
                buffer.putInt(ClientInteractiveElementExport.this.m_y);
                buffer.putShort(ClientInteractiveElementExport.this.m_z);
                final byte[] parameters = StringUtils.toUTF8(ClientInteractiveElementExport.this.m_parameter);
                assert parameters.length < 65535 : "chaine des parametres trop longue";
                buffer.putShort((short)(parameters.length & 0xFFFF));
                buffer.put(parameters);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int expectedSize() {
                return 14 + StringUtils.toUTF8(ClientInteractiveElementExport.this.m_parameter).length;
            }
        };
        this.m_actions = actions;
    }
    
    public byte[] serializeLandMarks() {
        return this.build(this.LAND_MARK_DATA);
    }
}
