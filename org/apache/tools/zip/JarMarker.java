package org.apache.tools.zip;

import java.util.zip.*;

public final class JarMarker implements ZipExtraField
{
    private static final ZipShort ID;
    private static final ZipShort NULL;
    private static final byte[] NO_BYTES;
    private static final JarMarker DEFAULT;
    
    public static JarMarker getInstance() {
        return JarMarker.DEFAULT;
    }
    
    public ZipShort getHeaderId() {
        return JarMarker.ID;
    }
    
    public ZipShort getLocalFileDataLength() {
        return JarMarker.NULL;
    }
    
    public ZipShort getCentralDirectoryLength() {
        return JarMarker.NULL;
    }
    
    public byte[] getLocalFileDataData() {
        return JarMarker.NO_BYTES;
    }
    
    public byte[] getCentralDirectoryData() {
        return JarMarker.NO_BYTES;
    }
    
    public void parseFromLocalFileData(final byte[] data, final int offset, final int length) throws ZipException {
        if (length != 0) {
            throw new ZipException("JarMarker doesn't expect any data");
        }
    }
    
    static {
        ID = new ZipShort(51966);
        NULL = new ZipShort(0);
        NO_BYTES = new byte[0];
        DEFAULT = new JarMarker();
    }
}
