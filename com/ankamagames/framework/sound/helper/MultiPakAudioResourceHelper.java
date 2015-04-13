package com.ankamagames.framework.sound.helper;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.*;
import com.ankamagames.framework.sound.stream.*;

public class MultiPakAudioResourceHelper implements AudioResourceHelper
{
    private static final Logger m_logger;
    private PakFile[] m_pakFiles;
    private final String[] m_extension;
    
    public MultiPakAudioResourceHelper(final String... extension) {
        super();
        this.m_extension = extension;
    }
    
    public void prepare(final String indexFile) throws IOException {
        final String base = FileHelper.getParentPath(indexFile);
        final ArrayList<PakFile> files = new ArrayList<PakFile>();
        try {
            final InputStream istream = ContentFileHelper.openFile(indexFile);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0) {
                    final File f = new File(base, line);
                    if (!f.exists()) {
                        continue;
                    }
                    files.add(new PakFile(f.getAbsolutePath()));
                }
            }
            reader.close();
            istream.close();
        }
        catch (IOException e) {
            MultiPakAudioResourceHelper.m_logger.error((Object)"", (Throwable)e);
        }
        this.m_pakFiles = files.toArray(new PakFile[files.size()]);
    }
    
    @Override
    public AudioStreamProvider fromId(final long id) throws IOException {
        if (this.m_pakFiles == null || this.m_pakFiles.length == 0) {
            MultiPakAudioResourceHelper.m_logger.error((Object)"PakFile non d\u00e9finie !");
            return null;
        }
        for (int i = 0, size = this.m_extension.length; i < size; ++i) {
            final String fileName = id + "." + this.m_extension[i];
            int j = this.m_pakFiles.length - 1;
            while (j >= 0) {
                final PakFile pakFile = this.m_pakFiles[j];
                final String base = pakFile.getDataPath() + "!/";
                FileLoadingLogger.logUsageOf(base + fileName);
                if (pakFile.containsFile(fileName)) {
                    if (ContentFileHelper.FORCE_TUTORIAL && pakFile.getDataPath().contains("_full")) {
                        MultiPakAudioResourceHelper.m_logger.error((Object)("     **** TUTO SOUND ****  manque key=" + fileName + " path=" + pakFile.getDataPath() + " !!!!!"));
                        return null;
                    }
                    return new PakAudioStreamProvider(pakFile, fileName, base + fileName);
                }
                else {
                    --j;
                }
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MultiPakAudioResourceHelper.class);
    }
}
