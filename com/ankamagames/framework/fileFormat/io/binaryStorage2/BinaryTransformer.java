package com.ankamagames.framework.fileFormat.io.binaryStorage2;

public interface BinaryTransformer<B extends BinaryData, O>
{
    O loadFromBinaryForm(B p0);
}
