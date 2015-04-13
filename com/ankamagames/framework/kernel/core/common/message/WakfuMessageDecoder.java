package com.ankamagames.framework.kernel.core.common.message;

import java.nio.*;

public interface WakfuMessageDecoder
{
    Message decode(ByteBuffer p0);
}
