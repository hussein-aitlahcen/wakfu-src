package org.apache.tools.zip;

import java.util.zip.*;

public interface ZipExtraField
{
    ZipShort getHeaderId();
    
    ZipShort getLocalFileDataLength();
    
    ZipShort getCentralDirectoryLength();
    
    byte[] getLocalFileDataData();
    
    byte[] getCentralDirectoryData();
    
    void parseFromLocalFileData(byte[] p0, int p1, int p2) throws ZipException;
}
