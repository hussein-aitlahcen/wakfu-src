package com.ankamagames.framework.fileFormat.io.binaryStorage2;

public interface LoadProcedure<T extends BinaryData>
{
    void load(T p0) throws Exception;
}
