package com.ankamagames.framework.net.download;

import org.apache.log4j.*;
import java.net.*;
import org.jetbrains.annotations.*;
import java.io.*;

public class FileDownloadInfo extends DownloadInfo
{
    public static final Logger m_logger;
    private final File m_localFile;
    private File m_tempFile;
    private final boolean m_allowOverride;
    private OutputStream m_stream;
    private final boolean m_createTemporaryFile;
    
    public FileDownloadInfo(@NotNull final URL remoteURL, final File localFile, final boolean createTemporaryFile, final boolean allowOverride) {
        super(remoteURL);
        this.m_stream = null;
        this.m_localFile = localFile;
        this.m_allowOverride = allowOverride;
        this.m_createTemporaryFile = createTemporaryFile;
    }
    
    @Override
    OutputStream createOutputStream() throws IOException {
        if (this.m_createTemporaryFile) {
            File tempFile;
            try {
                final File folder = this.m_localFile.getParentFile();
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                tempFile = File.createTempFile(new File(this.getRemoteURL().getFile()).getName(), null, folder);
                tempFile.deleteOnExit();
            }
            catch (IOException e) {
                FileDownloadInfo.m_logger.error((Object)("Unable to create temporary file for " + this));
                tempFile = null;
            }
            this.m_tempFile = ((tempFile == null) ? this.m_localFile : tempFile);
        }
        else {
            this.m_tempFile = this.m_localFile;
        }
        if (this.m_tempFile.exists()) {
            if (!this.m_allowOverride) {
                FileDownloadInfo.m_logger.error((Object)("Dest file already exists for " + this));
                throw new IOException("File already exists " + this.m_localFile);
            }
        }
        else {
            this.m_tempFile.getParentFile().mkdirs();
            this.m_tempFile.createNewFile();
            if (this.m_tempFile != this.m_localFile) {
                this.m_tempFile.deleteOnExit();
            }
        }
        return this.m_stream = new BufferedOutputStream(new FileOutputStream(this.m_tempFile, false));
    }
    
    @Override
    boolean closeStream(boolean success) {
        if (this.m_stream == null) {
            return false;
        }
        try {
            this.m_stream.flush();
            this.m_stream.close();
        }
        catch (IOException e) {
            FileDownloadInfo.m_logger.error((Object)("Error while closing stream for download " + this));
            success = false;
        }
        finally {
            this.m_stream = null;
        }
        if (!success) {
            if (this.m_tempFile != this.m_localFile) {
                this.m_tempFile.delete();
            }
            return false;
        }
        if (this.m_tempFile == this.m_localFile) {
            return true;
        }
        if (this.m_localFile.exists()) {
            if (!this.m_allowOverride) {
                FileDownloadInfo.m_logger.error((Object)("Dest file already exists for " + this));
                return false;
            }
            this.m_localFile.delete();
        }
        final boolean fileMoved = this.m_tempFile.renameTo(this.m_localFile);
        if (!fileMoved) {
            FileDownloadInfo.m_logger.error((Object)("Unable to rename temporary file " + this.m_tempFile + "as file " + this.m_localFile + " for " + this));
        }
        return fileMoved;
    }
    
    public File getAsFile() {
        return this.m_localFile;
    }
    
    @Override
    protected String getLocalToString() {
        return this.m_localFile.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)FileDownloadInfo.class);
    }
}
