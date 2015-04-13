package com.ankamagames.framework.net.download;

import org.jetbrains.annotations.*;

public class DefaultCacheValidator<D extends DownloadInfo> extends CacheValidator<D>
{
    public static final CacheValidator VALIDATOR;
    
    @Override
    public void setDownloadInfo(@NotNull final D downloadInfo) {
    }
    
    @Override
    public boolean isValid() {
        return false;
    }
    
    static {
        VALIDATOR = new DefaultCacheValidator();
    }
}
