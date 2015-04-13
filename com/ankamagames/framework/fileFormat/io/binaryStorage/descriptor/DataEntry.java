package com.ankamagames.framework.fileFormat.io.binaryStorage.descriptor;

import java.io.*;

public final class DataEntry implements EntryDescriptor
{
    private int m_id;
    private short m_version;
    private byte[] m_data;
    
    public DataEntry(final int id, final short version, final byte[] data) {
        super();
        this.m_id = id;
        this.m_version = version;
        this.m_data = data;
    }
    
    public DataEntry() {
        super();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public short getVersion() {
        return this.m_version;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.m_id);
        out.writeShort(this.m_version);
        out.writeInt(this.m_data.length);
        out.write(this.m_data);
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.m_id = in.readInt();
        this.m_version = in.readShort();
        final int taille = in.readInt();
        in.read(this.m_data = new byte[taille]);
    }
    
    public static int skipEntry(final DataInputStream in) throws IOException {
        in.readInt();
        in.readShort();
        final int taille = in.readInt();
        in.skipBytes(taille);
        return 10 + taille;
    }
}
