package com.ankamagames.framework.fileFormat.io;

import java.net.*;
import com.ankamagames.framework.java.util.*;
import org.jetbrains.annotations.*;
import java.io.*;
import java.util.zip.*;

public class FileHelper
{
    private static final boolean USE_BUFFERED_STREAMS = true;
    
    public static URL getURL(final String path) throws IOException {
        try {
            return new URL(path);
        }
        catch (MalformedURLException e) {
            final File f = new File(path);
            return f.toURI().toURL();
        }
    }
    
    public static byte[] readFile(final File file) throws IOException {
        final InputStream fileStream = new FileInputStream(file);
        final byte[] bytes = readFullStream(fileStream);
        fileStream.close();
        return bytes;
    }
    
    public static byte[] readFile(final String fileName) throws IOException {
        final InputStream fileStream = openFile(fileName);
        final byte[] bytes = readFullStream(fileStream);
        fileStream.close();
        return bytes;
    }
    
    public static byte[] readFile(final String fileName, final int size) throws IOException {
        final InputStream fileStream = openFile(fileName);
        final byte[] bytes = readStream(fileStream, size);
        fileStream.close();
        return bytes;
    }
    
    public static InputStream openFile(final String fileName) throws IOException {
        InputStream stream;
        try {
            stream = new BufferedInputStream(new URL(fileName).openStream());
        }
        catch (Exception e) {
            final String protocol = "file://";
            final int protocolIndex = fileName.indexOf("file://");
            String name = fileName;
            if (protocolIndex >= 0) {
                name = fileName.substring(protocolIndex + "file://".length());
            }
            final File file = new File(name);
            stream = new BufferedInputStream(new FileInputStream(file));
        }
        return stream;
    }
    
    public static int readDataBlock(final InputStream stream, final byte[] destinationBuffer, final int destinationOffset, final int size) throws IOException {
        int sizeRead = 0;
        int offset = 0;
        do {
            sizeRead = stream.read(destinationBuffer, destinationOffset + offset, size - offset);
            offset += sizeRead;
        } while (sizeRead != -1 && offset != size);
        return offset;
    }
    
    public static byte[] readFullStream(final FileInputStream stream) throws IOException {
        final int totalSize = stream.available();
        final byte[] fileBuffer = new byte[totalSize];
        int offset = 0;
        int sizeRead;
        do {
            sizeRead = stream.read(fileBuffer, offset, totalSize - offset);
            offset += sizeRead;
        } while (sizeRead != 0 && offset != totalSize);
        assert offset == totalSize;
        return fileBuffer;
    }
    
    public static byte[] readFullStream(final InputStream stream) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int len;
        while ((len = stream.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        return out.toByteArray();
    }
    
    public static byte[] readStream(final InputStream stream, final int size) throws IOException {
        final int totalSize = Math.min(size, stream.available());
        final byte[] fileBuffer = new byte[totalSize];
        int offset = 0;
        int sizeRead;
        do {
            sizeRead = stream.read(fileBuffer, offset, totalSize - offset);
            offset += sizeRead;
        } while (sizeRead != 0 && offset != totalSize);
        assert offset == totalSize;
        return fileBuffer;
    }
    
    public static boolean isExistingFile(final String path) {
        final File file = new File(path);
        return file.exists() || URLUtils.urlExists(path);
    }
    
    public static void deleteFile(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isFile()) {
            return;
        }
        file.delete();
    }
    
    public static void deleteDirectory(final String path) {
        deleteDirectory(path, false);
    }
    
    public static void deleteDirectory(final String path, final boolean onlySubFiles) {
        final File directory = new File(path);
        if (!directory.exists()) {
            return;
        }
        final File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (final File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file.getPath());
            }
            else {
                file.delete();
            }
        }
        if (!onlySubFiles) {
            directory.delete();
        }
    }
    
    public static String getTempName() {
        final CRC32 crc32 = new CRC32();
        crc32.update(new StringBuilder().append(System.nanoTime()).toString().getBytes());
        return new StringBuilder().append(crc32.getValue()).toString();
    }
    
    public static void copyFile(final String srcFileName, final String dstFileName) throws IOException {
        final byte[] fileBuffer = readFile(srcFileName);
        final FileOutputStream stream = createFileOutputStream(dstFileName);
        stream.write(fileBuffer);
        stream.close();
    }
    
    public static void writeBooleanAsSI8(final OutputBitStream bitStream, final boolean b) throws IOException {
        if (b) {
            bitStream.writeByte((byte)1);
        }
        else {
            bitStream.writeByte((byte)0);
        }
    }
    
    public static String getName(final String filePath) {
        final int nameIndex = getLastSeparatorIndex(filePath);
        return filePath.substring(nameIndex + 1);
    }
    
    public static String getPath(final String filePath) {
        final int nameIndex = getLastSeparatorIndex(filePath);
        if (nameIndex == -1) {
            return "";
        }
        return filePath.substring(0, nameIndex);
    }
    
    public static String getPathWithSeparator(final String filePath) {
        final int nameIndex = getLastSeparatorIndex(filePath);
        return filePath.substring(0, nameIndex + 1);
    }
    
    public static String getFileExt(final String filePath) {
        final int extPos = filePath.lastIndexOf(46);
        if (extPos == -1) {
            return "";
        }
        return filePath.substring(extPos + 1);
    }
    
    public static String getNameWithoutExt(final String filePath) {
        final int nameIndex = getLastSeparatorIndex(filePath) + 1;
        final int extPos = filePath.lastIndexOf(46);
        if (extPos < nameIndex) {
            return filePath.substring(nameIndex);
        }
        return filePath.substring(nameIndex, extPos);
    }
    
    public static String getFullNameWithoutExt(final String filePath) {
        final int extPos = filePath.lastIndexOf(46);
        if (extPos < 0) {
            return filePath;
        }
        return filePath.substring(0, extPos);
    }
    
    public static String getParentPath(final String filePath) {
        final String path = filePath.replace('\\', '/');
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash == path.length() - 1) {
            lastSlash = path.substring(0, lastSlash).lastIndexOf(47);
        }
        return path.substring(0, lastSlash);
    }
    
    public static String getDirectory(final String filePath) {
        final String path = filePath.replace('\\', '/');
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash == path.length() - 1) {
            lastSlash = path.substring(0, lastSlash).lastIndexOf(47);
            return path.substring(lastSlash + 1, path.length() - 1);
        }
        return path.substring(lastSlash + 1, path.length());
    }
    
    private static int getLastSeparatorIndex(final String filePath) {
        final int nameIndex = filePath.lastIndexOf(47);
        if (nameIndex >= 0) {
            return nameIndex;
        }
        return filePath.lastIndexOf(92);
    }
    
    public static FileOutputStream createFileOutputStream(final String fileName) throws IOException {
        final File file = new File(fileName);
        final File parentFile = file.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }
        return new FileOutputStream(file);
    }
    
    public static void write(@NotNull final String filePath, @NotNull final byte[] data) throws IOException {
        final FileOutputStream fos = createFileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }
    
    public static boolean deleteDirectory(final File path) {
        if (!path.exists()) {
            return false;
        }
        final File[] files = path.listFiles();
        if (files == null) {
            return false;
        }
        for (final File f : files) {
            if (f.isDirectory()) {
                deleteDirectory(f);
            }
            else {
                f.delete();
            }
        }
        return path.delete();
    }
    
    public static byte[] compress(final byte[] data) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream(data.length / 2);
        final DeflaterOutputStream deflater = new DeflaterOutputStream(os);
        deflater.write(data);
        deflater.close();
        return os.toByteArray();
    }
    
    public static byte[] uncompress(final byte[] compressedData) throws IOException {
        final InputStream istream = new ByteArrayInputStream(compressedData);
        final InflaterInputStream unzip = new InflaterInputStream(istream);
        final byte[] uncompress = readFullStream(unzip);
        istream.close();
        return uncompress;
    }
}
