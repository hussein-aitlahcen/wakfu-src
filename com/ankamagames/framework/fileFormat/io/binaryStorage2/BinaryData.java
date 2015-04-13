package com.ankamagames.framework.fileFormat.io.binaryStorage2;

public interface BinaryData
{
    int getDataTypeId();
    
    void read(RandomByteBufferReader p0) throws Exception;
    
    void reset();
}
