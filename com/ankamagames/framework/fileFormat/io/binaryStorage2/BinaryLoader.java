package com.ankamagames.framework.fileFormat.io.binaryStorage2;

import org.jetbrains.annotations.*;

public interface BinaryLoader<B extends BinaryData>
{
    @Nullable
    B createFromId(int p0);
}
