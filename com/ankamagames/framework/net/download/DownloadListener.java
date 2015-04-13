package com.ankamagames.framework.net.download;

import org.jetbrains.annotations.*;

public interface DownloadListener
{
    void onDownloadStateChanged(@NotNull DownloadInfo p0);
}
