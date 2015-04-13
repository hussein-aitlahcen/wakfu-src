package org.apache.tools.zip;

import java.util.zip.*;

public interface CentralDirectoryParsingZipExtraField extends ZipExtraField
{
    void parseFromCentralDirectoryData(byte[] p0, int p1, int p2) throws ZipException;
}
