package com.ankamagames.framework.graphics.image;

import org.apache.log4j.*;
import java.awt.image.*;
import com.ankamagames.framework.fileFormat.io.*;
import javax.imageio.*;
import java.io.*;

public final class AlphaBitmapData
{
    protected static final Logger m_logger;
    public static final byte CURRENT_VERSION = 1;
    private boolean m_alphaPremultiplied;
    private byte[] m_data;
    private int m_width;
    private int m_height;
    
    public AlphaBitmapData(final int width, final int height, final byte[] data) {
        super();
        this.m_alphaPremultiplied = false;
        this.m_width = width;
        this.m_height = height;
        this.m_data = data;
    }
    
    public AlphaBitmapData(final BufferedImage image, final boolean premultAlpha) {
        super();
        this.m_alphaPremultiplied = false;
        this.fromBufferedImage(image, premultAlpha);
    }
    
    public AlphaBitmapData(final ExtendedDataInputStream stream) throws IOException {
        super();
        this.m_alphaPremultiplied = false;
        this.read(stream);
    }
    
    public BufferedImage toBufferedImage() {
        return ImageUtilities.toImage(this.m_width, this.m_height, this.m_data, ImageUtilities.PixelFormat.ARGB);
    }
    
    private void fromBufferedImage(BufferedImage image, final boolean premultAlpha) {
        if (image != null) {
            this.m_width = image.getWidth();
            this.m_height = image.getHeight();
            if (premultAlpha) {
                if (image.getType() != 3) {
                    image = ImageUtilities.convertToARGB_PRE(image);
                }
                else if (image.getType() != 2) {
                    image = ImageUtilities.convertToARGB(image);
                }
            }
            else if (image.getType() != 2) {
                image = ImageUtilities.convertToARGB(image);
            }
            this.m_alphaPremultiplied = image.isAlphaPremultiplied();
            final DataBufferInt buffer = (DataBufferInt)image.getData().getDataBuffer();
            this.m_data = new byte[this.m_width * this.m_height * 4];
            for (int i = 0; i < buffer.getSize(); ++i) {
                final int offset = 4 * i;
                final int value = buffer.getElem(i);
                this.m_data[offset] = (byte)(value >> 16 & 0xFF);
                this.m_data[offset + 1] = (byte)(value >> 8 & 0xFF);
                this.m_data[offset + 2] = (byte)(value & 0xFF);
                this.m_data[offset + 3] = (byte)(value >> 24 & 0xFF);
            }
        }
        else {
            this.m_width = 0;
            this.m_height = 0;
            this.m_data = null;
        }
    }
    
    public byte[] getData() {
        return this.m_data;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    @Override
    public String toString() {
        return "AlphaBitmapData (" + this.m_width + "x" + this.m_height + ") @" + Integer.toHexString(super.hashCode());
    }
    
    public void write(final OutputBitStream stream) throws IOException {
        stream.writeUnsignedByte((short)1);
        stream.writeBooleanBit(this.m_alphaPremultiplied);
        stream.writeUnsignedShort(this.m_width);
        stream.writeUnsignedShort(this.m_height);
        if (this.m_data != null) {
            stream.writeUnsignedInt(this.m_data.length);
            stream.writeBytes(this.m_data);
        }
        else {
            stream.writeUnsignedInt(0L);
        }
    }
    
    public void read(final ExtendedDataInputStream inStream) throws IOException {
        final short version = inStream.readUnsignedByte();
        if (version != 1) {
            AlphaBitmapData.m_logger.error((Object)"Exception", (Throwable)new Exception("Version incorrecte:" + version + " courante:" + 1));
        }
        this.m_alphaPremultiplied = inStream.readBooleanBit();
        this.m_width = inStream.readUnsignedShort();
        this.m_height = inStream.readUnsignedShort();
        final int length = (int)inStream.readUnsignedInt();
        if (length > 0) {
            this.m_data = inStream.readBytes(length);
        }
    }
    
    public void premultAlpha() {
        if (this.m_data != null && !this.m_alphaPremultiplied) {
            this.m_alphaPremultiplied = true;
            for (int i = 0; i < this.m_data.length; i += 4) {
                final byte a = this.m_data[i + 3];
                this.m_data[i] = (byte)(this.m_data[i] * a / 255);
                this.m_data[i + 1] = (byte)(this.m_data[i + 1] * a / 255);
                this.m_data[i + 2] = (byte)(this.m_data[i + 2] * a / 255);
            }
        }
    }
    
    public void demultiplyAlpha() {
        if (this.m_data != null && this.m_alphaPremultiplied) {
            this.m_alphaPremultiplied = false;
            for (int i = 0; i < this.m_data.length; i += 4) {
                final byte a = this.m_data[i + 3];
                if (a != 0) {
                    this.m_data[i] = (byte)(this.m_data[i] * 255 / a);
                    this.m_data[i + 1] = (byte)(this.m_data[i + 1] * 255 / a);
                    this.m_data[i + 2] = (byte)(this.m_data[i + 2] * 255 / a);
                }
                else {
                    this.m_data[i] = -1;
                    this.m_data[i + 1] = -1;
                    this.m_data[i + 2] = -1;
                }
            }
        }
    }
    
    public boolean isAlphaPremultiplied() {
        return this.m_alphaPremultiplied;
    }
    
    public float getAlphaValue(final int x, final int y) {
        if (x >= this.m_width || y >= this.m_height || this.m_data == null) {
            return 0.0f;
        }
        final byte a = this.m_data[4 * (x + y * this.m_width) + 3];
        return a / 255.0f;
    }
    
    @Override
    public boolean equals(final Object alphaBitmapData) {
        if (this == alphaBitmapData) {
            return true;
        }
        if (!(alphaBitmapData instanceof AlphaBitmapData)) {
            return false;
        }
        final AlphaBitmapData image = (AlphaBitmapData)alphaBitmapData;
        if (this.getWidth() != image.getWidth() || this.getHeight() != image.getHeight()) {
            return false;
        }
        final byte[] data1 = this.getData();
        final byte[] data2 = image.getData();
        for (int i = 0; i < data1.length; ++i) {
            if (data1[i] != data2[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        assert false : "Pas d'insertion possible en tant que clef dans une HashMap/HashTable";
        return super.hashCode();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AlphaBitmapData.class);
    }
    
    public static class OldVersionReader
    {
        public static AlphaBitmapData read1(final ExtendedDataInputStream inStream, final int dataLength) throws IOException {
            final byte[] data = inStream.readBytes(dataLength);
            final ByteArrayInputStream buffer = new ByteArrayInputStream(data);
            return new AlphaBitmapData(ImageIO.read(buffer), true);
        }
        
        public static AlphaBitmapData read2(final ExtendedDataInputStream inStream) throws IOException {
            final int width = inStream.readUnsignedShort();
            final int height = inStream.readUnsignedShort();
            final int length = (int)inStream.readUnsignedInt();
            byte[] datas = null;
            if (length > 0) {
                datas = inStream.readBytes(length);
            }
            return new AlphaBitmapData(width, height, datas);
        }
    }
}
