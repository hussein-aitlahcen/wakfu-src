package com.ankamagames.framework.fileFormat.io.binaryStorage.descriptor;

import java.io.*;

public interface EntryDescriptor
{
    void write(DataOutputStream p0) throws IOException;
    
    void read(DataInputStream p0) throws IOException;
}
