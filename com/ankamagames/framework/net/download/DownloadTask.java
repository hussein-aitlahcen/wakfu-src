package com.ankamagames.framework.net.download;

import java.util.concurrent.*;
import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.io.*;

class DownloadTask implements Callable<DownloadInfo>
{
    public static final Logger m_logger;
    public static final int RCV_BUFFER_SIZE = 1024;
    @NotNull
    private final DownloadInfo m_downloadInfo;
    
    DownloadTask(@NotNull final DownloadInfo downloadInfo) {
        super();
        this.m_downloadInfo = downloadInfo;
    }
    
    @Override
    public DownloadInfo call() throws Exception {
        if (this.m_downloadInfo.isCacheValid()) {
            this.m_downloadInfo.setState(DownloadState.FINISHED);
            return this.m_downloadInfo;
        }
        DownloadTask.m_logger.info((Object)("Downloading " + this.m_downloadInfo));
        this.m_downloadInfo.setState(DownloadState.RUNNING);
        OutputStream outputStream;
        try {
            outputStream = this.m_downloadInfo.createOutputStream();
        }
        catch (IOException e) {
            DownloadTask.m_logger.error((Object)("Unable to create outputStream for download " + this.m_downloadInfo), (Throwable)e);
            this.m_downloadInfo.setState(DownloadState.ERROR);
            return this.m_downloadInfo;
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(this.m_downloadInfo.getRemoteURL().openStream(), 8192);
        }
        catch (FileNotFoundException e5) {
            DownloadTask.m_logger.error((Object)("Unable to download " + this.m_downloadInfo + " : file not found (404)"));
            this.m_downloadInfo.closeStream(false);
            this.m_downloadInfo.setState(DownloadState.ERROR);
            return this.m_downloadInfo;
        }
        catch (IOException e2) {
            DownloadTask.m_logger.error((Object)("Unable to open url connection for " + this.m_downloadInfo), (Throwable)e2);
            this.m_downloadInfo.closeStream(false);
            this.m_downloadInfo.setState(DownloadState.ERROR);
            return this.m_downloadInfo;
        }
        final byte[] buffer = new byte[1024];
        try {
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            if (this.m_downloadInfo.closeStream(true)) {
                this.m_downloadInfo.setState(DownloadState.FINISHED);
                DownloadTask.m_logger.info((Object)("Download finished : " + this.m_downloadInfo));
            }
            else {
                this.m_downloadInfo.setState(DownloadState.ERROR);
                DownloadTask.m_logger.error((Object)("Error while closing stream for " + this.m_downloadInfo));
            }
        }
        catch (IOException e3) {
            DownloadTask.m_logger.error((Object)("Error while downloading " + this.m_downloadInfo), (Throwable)e3);
            this.m_downloadInfo.closeStream(false);
            this.m_downloadInfo.setState(DownloadState.ERROR);
            return this.m_downloadInfo;
        }
        catch (Throwable e4) {
            DownloadTask.m_logger.error((Object)("Error while downloading " + this.m_downloadInfo), e4);
        }
        finally {
            inputStream.close();
        }
        return this.m_downloadInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DownloadTask.class);
    }
}
