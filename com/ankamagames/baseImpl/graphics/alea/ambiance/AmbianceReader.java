package com.ankamagames.baseImpl.graphics.alea.ambiance;

import gnu.trove.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.nio.*;

public class AmbianceReader
{
    private String m_filename;
    private final TIntIntHashMap m_index;
    
    public AmbianceReader() {
        super();
        this.m_index = new TIntIntHashMap();
    }
    
    public final void load(final String filename) throws IOException {
        this.m_filename = filename;
        this.loadFrom(this.openFile());
    }
    
    private void loadFrom(final ByteBuffer buffer) {
        this.m_index.clear();
        for (int count = buffer.getInt(), i = 0; i < count; ++i) {
            final int ambienceId = buffer.getInt();
            final int location = buffer.getInt();
            this.m_index.put(ambienceId, location);
        }
    }
    
    public final Ambiance readAmbiance(final int ambianceId) throws IOException {
        return this.readAmbiance(ambianceId, this.openFile());
    }
    
    private final Ambiance readAmbiance(final int ambianceId, final ByteBuffer buffer) throws IOException {
        final int position = this.m_index.get(ambianceId);
        if (position > 0) {
            buffer.position(position);
            return this.createAmbiance(buffer, ambianceId);
        }
        return null;
    }
    
    public final Ambiance getAmbianceFrom(final ByteBuffer buffer, final int ambianceId) throws IOException {
        this.loadFrom(buffer);
        return this.readAmbiance(ambianceId, buffer);
    }
    
    private Ambiance createAmbiance(final ByteBuffer buffer, final int ambianceId) {
        final Ambiance ambiance = new Ambiance(ambianceId);
        for (int count = buffer.get() & 0xFF, i = 0; i < count; ++i) {
            final int effectId = buffer.getInt();
            ambiance.addEffect(AmbianceEffectManager.getInstance().getEffect(effectId));
        }
        return ambiance;
    }
    
    private ByteBuffer openFile() throws IOException {
        final byte[] bytes = ContentFileHelper.readFile(this.m_filename);
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }
    
    public final int getIndexSize() {
        return this.m_index.size();
    }
}
