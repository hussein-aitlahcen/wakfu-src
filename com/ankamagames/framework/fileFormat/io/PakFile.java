package com.ankamagames.framework.fileFormat.io;

import java.io.*;
import gnu.trove.*;

public class PakFile
{
    private static final int BUFFER_SIZE = 4096;
    private final TIntIntHashMap m_fileOffsetMap;
    private final String m_dataPath;
    private final int m_bufferSize;
    
    public PakFile(final String dataFilePath) throws IOException {
        this(dataFilePath, 4096);
    }
    
    public PakFile(final String dataFilePath, final int bufferSize) throws IOException {
        super();
        this.m_fileOffsetMap = new TIntIntHashMap(0, 1.0f);
        final int headerSize = 4;
        final byte[] data = ContentFileHelper.readFile(dataFilePath, 4);
        final ExtendedDataInputStream header = ExtendedDataInputStream.wrap(data);
        final int indexLength = header.readInt();
        final int startDataPosition = indexLength + 4;
        final ExtendedDataInputStream is = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(dataFilePath, startDataPosition));
        is.skip(4);
        final int entriesCount = is.readInt();
        this.m_fileOffsetMap.ensureCapacity(entriesCount);
        for (int i = 0; i < entriesCount; ++i) {
            this.m_fileOffsetMap.put(is.readInt(), is.readInt() + startDataPosition);
        }
        this.m_dataPath = dataFilePath.replace('\\', '/');
        this.m_bufferSize = bufferSize;
    }
    
    public String getDataPath() {
        return this.m_dataPath;
    }
    
    public PakInputStream openStream(final String fileName) throws IOException {
        final int offset = this.m_fileOffsetMap.get(fileName.hashCode());
        return this.getPakInputStream(offset);
    }
    
    public PakInputStream getPakInputStream(final int offset) throws IOException {
        final BufferedRandomAccessReader reader = new BufferedRandomAccessReader(new File(this.m_dataPath), this.m_bufferSize);
        reader.seek(offset);
        final byte[] lengthBuffer = new byte[8];
        for (int readBytes = 0; readBytes != 8; readBytes += reader.read(lengthBuffer, readBytes, 8 - readBytes)) {}
        final long length = ExtendedDataInputStream.wrap(lengthBuffer).readLong();
        return new PakInputStream(reader, offset + 8, length);
    }
    
    public boolean containsFile(final String path) {
        return this.m_fileOffsetMap.containsKey(path.hashCode());
    }
    
    public void foreachIndex(final TIntIntProcedure procedure) {
        this.m_fileOffsetMap.forEachEntry(procedure);
    }
}
