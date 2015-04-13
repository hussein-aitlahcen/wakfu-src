package org.apache.tools.zip;

import java.nio.*;
import java.io.*;

public interface ZipEncoding
{
    boolean canEncode(String p0);
    
    ByteBuffer encode(String p0) throws IOException;
    
    String decode(byte[] p0) throws IOException;
}
