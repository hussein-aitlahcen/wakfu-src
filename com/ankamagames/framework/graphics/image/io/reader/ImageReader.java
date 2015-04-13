package com.ankamagames.framework.graphics.image.io.reader;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class ImageReader
{
    private static final Logger m_logger;
    protected byte[] m_buffer;
    
    public Point2i getImageSize(String fileName) {
        fileName = fileName.replace('\\', '/');
        try {
            return this.getImageSize(ContentFileHelper.readFile(fileName, this.getHeaderSize()));
        }
        catch (IOException ex) {
            ImageReader.m_logger.error((Object)("Erreur au chargement de l'image " + fileName), (Throwable)ex);
            return null;
        }
    }
    
    public Point2i getImageSize(final byte[] data) {
        try {
            return this.getSize(ExtendedDataInputStream.wrap(data));
        }
        catch (IOException ex) {
            ImageReader.m_logger.error((Object)"Erreur au chargement de l'image", (Throwable)ex);
            return null;
        }
    }
    
    public Image loadImage(String fileName) {
        fileName = fileName.replace('\\', '/');
        try {
            return this.loadImage(ContentFileHelper.readFile(fileName));
        }
        catch (IOException ex) {
            ImageReader.m_logger.error((Object)("Erreur au chargement de l'image " + fileName), (Throwable)ex);
            return null;
        }
    }
    
    public final Image loadImage(final byte[] data) {
        try {
            return this.loadImageFromStream(ExtendedDataInputStream.wrap(data));
        }
        catch (IOException ex) {
            ImageReader.m_logger.error((Object)"Erreur au chargement de l'image", (Throwable)ex);
            return null;
        }
    }
    
    protected abstract Image loadImageFromStream(final ExtendedDataInputStream p0) throws IOException;
    
    protected abstract Point2i getSize(final ExtendedDataInputStream p0) throws IOException;
    
    protected abstract int getHeaderSize();
    
    public final byte[] getData() {
        return this.m_buffer;
    }
    
    protected final byte[] readAndRemoveStrideAndFlipVertically(final ExtendedDataInputStream stream, final int width, final int height, final int bitDepth) throws IOException {
        final int lineSize = getLineSize(width, bitDepth);
        final int lineStride = (lineSize + 3) / 4 * 4;
        final int bytesPerLineToSkip = lineStride - lineSize;
        final byte[] buffer = new byte[height * lineSize];
        for (int destPos = buffer.length - lineSize; destPos >= 0; destPos -= lineSize) {
            if (stream.readBytes(buffer, destPos, lineSize) != lineSize) {
                ImageReader.m_logger.error((Object)"read error");
            }
            if (stream.skip(bytesPerLineToSkip) != bytesPerLineToSkip) {
                ImageReader.m_logger.error((Object)"skip error");
            }
        }
        return buffer;
    }
    
    protected final byte[] readAndFlipVertically(final ExtendedDataInputStream stream, final int width, final int height, final int bitDepth) throws IOException {
        final int lineSize = getLineSize(width, bitDepth);
        final byte[] buffer = new byte[height * lineSize];
        for (int destPos = buffer.length - lineSize; destPos >= 0; destPos -= lineSize) {
            if (stream.readBytes(buffer, destPos, lineSize) != lineSize) {
                ImageReader.m_logger.error((Object)"read error");
            }
        }
        return buffer;
    }
    
    protected final byte[] removeStrideAndFlipVertically(final int width, final int height, final int bitDepth, final int offset) {
        assert this.m_buffer != null;
        final int lineSize = getLineSize(width, bitDepth);
        final int lineStride = (lineSize + 3) / 4 * 4;
        final byte[] buffer = new byte[height * lineSize];
        int srcPos = offset + lineStride * (height - 1);
        for (int destPos = 0; destPos < buffer.length; destPos += lineSize) {
            System.arraycopy(this.m_buffer, srcPos, buffer, destPos, lineSize);
            srcPos -= lineStride;
        }
        return buffer;
    }
    
    protected final byte[] flipVertically(final int width, final int height, final int bitDepth, final int offset) {
        assert this.m_buffer != null;
        final int lineSize = getLineSize(width, bitDepth);
        final byte[] buffer = new byte[height * lineSize];
        int srcPos = offset + lineSize * (height - 1);
        for (int destPos = 0; destPos < buffer.length; destPos += lineSize) {
            System.arraycopy(this.m_buffer, srcPos, buffer, destPos, lineSize);
            srcPos -= lineSize;
        }
        return buffer;
    }
    
    protected static float getDepthInBytes(final int bitDepth) {
        return bitDepth / 8.0f;
    }
    
    protected static int getLineSize(final int width, final int bitDepth) {
        return (int)(width * getDepthInBytes(bitDepth));
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageReader.class);
    }
}
