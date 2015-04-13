package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import java.net.*;
import org.jetbrains.annotations.*;

interface NewsImageFactory
{
    public static final Logger m_logger = Logger.getLogger((Class)NewsImageFactory.class);
    
    @Nullable
    NewsImage createImage(@Nullable URL p0, @Nullable String p1, long p2);
}
