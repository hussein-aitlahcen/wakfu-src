package com.ankamagames.framework.fileFormat;

import org.apache.log4j.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.fileFormat.io.*;
import org.apache.tools.ant.*;
import java.util.*;
import java.io.*;

public class MultiPakIndexer
{
    private static final Logger m_logger;
    public static final byte[] COMPRESS_HEADER;
    private final THashMap<String, String> m_files;
    private String m_contentPrefix;
    
    public MultiPakIndexer() {
        super();
        this.m_files = new THashMap<String, String>(100000, 1.0f);
        this.m_contentPrefix = "";
    }
    
    public String getContentPrefix() {
        return this.m_contentPrefix;
    }
    
    public void setContentPrefix(@NotNull final String contentPrefix) {
        this.m_contentPrefix = contentPrefix;
    }
    
    public String get(final String key) {
        final String prefixForJar = "jar:file:";
        return this.get(key, "jar:file:");
    }
    
    public String getAbsolute(final String key, final String prefixForJar) {
        if (key == null || key.charAt(0) != '@') {
            return key;
        }
        final String[] split = StringUtils.splitByWholeSeparator(key, "!/", 0);
        final String p = split[0];
        final String id = getKey(p, split[1]);
        final String fileExt = FileHelper.getFileExt(p);
        String path = this.m_contentPrefix + key.substring(1);
        path = new File(path).getAbsolutePath();
        path = path.replace('\\', '/');
        if (fileExt.equals("jar")) {
            path = prefixForJar + path;
        }
        final String value = this.m_files.get(id);
        if (value == null) {
            return path;
        }
        if (ContentFileHelper.FORCE_TUTORIAL && value.equals("_full")) {
            MultiPakIndexer.m_logger.error((Object)("     **** TUTO ****  manque key=" + key + " path=" + path + " !!!!!"));
            return "@not_found:" + key;
        }
        return transformPath(path, value, fileExt);
    }
    
    public String get(final String key, final String prefixForJar) {
        if (key == null || key.charAt(0) != '@') {
            return key;
        }
        final String[] split = StringUtils.splitByWholeSeparator(key, "!/", 0);
        final String p = split[0];
        final String id = getKey(p, split[1]);
        final String fileExt = FileHelper.getFileExt(p);
        String path = this.m_contentPrefix + key.substring(1);
        if (fileExt.equals("jar")) {
            path = prefixForJar + path;
        }
        final String value = this.m_files.get(id);
        if (value == null) {
            return path;
        }
        if (ContentFileHelper.FORCE_TUTORIAL && value.equals("_full")) {
            MultiPakIndexer.m_logger.error((Object)("     **** TUTO ****  manque key=" + key + " path=" + path + " !!!!!"));
            return "@not_found:" + key;
        }
        return transformPath(path, value, fileExt);
    }
    
    public void readFrom(final String path) throws IOException {
        final int i = path.indexOf("*");
        String baseDir;
        String subPath;
        if (i != -1) {
            baseDir = new File(path.substring(0, i)).getCanonicalPath();
            subPath = path.substring(i);
        }
        else {
            baseDir = ".";
            subPath = new File(path).getCanonicalPath();
        }
        final DirectoryScanner ds = new DirectoryScanner();
        ds.setIncludes(new String[] { subPath });
        ds.setBasedir(new File(baseDir));
        ds.setCaseSensitive(true);
        ds.scan();
        for (final String file : ds.getIncludedFiles()) {
            MultiPakIndexer.m_logger.info((Object)("lecture de l'index " + file));
            this.readFromFile(new File(baseDir, file));
        }
        this.m_files.compact();
    }
    
    private void readFromFile(final File file) {
        try {
            final InputStream istream = FileHelper.openFile(file.getAbsolutePath());
            final BufferedReader reader = getBufferedReader(istream);
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] pair = StringUtils.split(line, '=');
                if (pair.length != 2) {
                    MultiPakIndexer.m_logger.warn((Object)("Erreur avec la ligne " + line + " (" + file + ")"));
                }
                else {
                    final String key = getKey(file.getName(), pair[0]);
                    if (this.m_files.contains(key)) {
                        MultiPakIndexer.m_logger.warn((Object)("Erreur avec la ligne " + line + " (" + file + ") cl\u00e9e d\u00e9j\u00e0 existante value=" + this.m_files.get(key)));
                    }
                    else {
                        this.m_files.put(key, pair[1].intern());
                    }
                }
            }
            reader.close();
            istream.close();
        }
        catch (IOException e) {
            MultiPakIndexer.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    private static BufferedReader getBufferedReader(final InputStream istream) throws IOException {
        istream.mark(0);
        if (isCompressIndex(istream)) {
            final byte[] bytes = FileHelper.uncompress(FileHelper.readFullStream(istream));
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
        }
        istream.reset();
        return new BufferedReader(new InputStreamReader(istream));
    }
    
    private static String getKey(final String pakName, final String indexEntry) {
        return FileHelper.getNameWithoutExt(pakName) + "/" + indexEntry;
    }
    
    private static boolean isCompressIndex(final InputStream istream) throws IOException {
        final byte[] header = new byte[MultiPakIndexer.COMPRESS_HEADER.length];
        FileHelper.readDataBlock(istream, header, 0, header.length);
        return Arrays.equals(header, MultiPakIndexer.COMPRESS_HEADER);
    }
    
    public static String transformPath(final String jarPath, final String type, final String extension) {
        return StringUtils.replaceOnce(jarPath, '.' + extension, type + '.' + extension);
    }
    
    public static void write(final String key, final String value, final PrintStream out) {
        out.append(key).append('=').append(value).append("\n");
    }
    
    public static void writeHeader(final OutputStream os, final boolean compressIndex) throws IOException {
        if (!compressIndex) {
            return;
        }
        os.write(MultiPakIndexer.COMPRESS_HEADER);
        os.flush();
    }
    
    public static void main(final String[] args) {
        final MultiPakIndexer task = new MultiPakIndexer();
        try {
            task.readFrom("../contents/**/*.index");
        }
        catch (IOException e) {
            MultiPakIndexer.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MultiPakIndexer.class);
        COMPRESS_HEADER = new byte[] { 109, 105, 99 };
    }
}
