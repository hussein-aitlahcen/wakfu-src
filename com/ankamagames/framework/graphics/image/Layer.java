package com.ankamagames.framework.graphics.image;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class Layer extends ReferenceCounter
{
    private static final Logger m_logger;
    public static final byte RGB = 1;
    public static final byte BGR = 2;
    public static final byte RGBA = 17;
    public static final byte BGRA = 18;
    private int m_width;
    private int m_height;
    private short m_startX;
    private short m_startY;
    private Point2i m_size;
    private final int m_bitDepth;
    private Palette m_palette;
    private byte[] m_data;
    private byte m_pixelFormat;
    private AlphaMask m_alphaMask;
    
    public Layer(final int width, final int height, final short bitDepth, final Palette palette, final byte[] data) {
        super();
        this.m_pixelFormat = 17;
        this.m_width = width;
        this.m_height = height;
        this.m_size = new Point2i(this.m_width, this.m_height);
        this.m_bitDepth = bitDepth;
        this.m_palette = palette;
        this.m_data = data;
    }
    
    public Layer(final short width, final short height, final short startX, final short startY, final byte bitDepth, final Palette palette, final byte[] data) {
        this(width, height, bitDepth, palette, data);
        this.m_startX = startX;
        this.m_startY = startY;
    }
    
    public Layer(final int width, final int height, final short bitDepth, final Palette palette, final byte[] data, final int offset, final int dataSize) {
        super();
        this.m_pixelFormat = 17;
        this.m_width = width;
        this.m_height = height;
        this.m_size = new Point2i(this.m_width, this.m_height);
        this.m_bitDepth = bitDepth;
        this.m_palette = palette;
        System.arraycopy(data, offset, this.m_data = new byte[dataSize], 0, dataSize);
    }
    
    public Layer(final Layer layer) {
        super();
        this.m_pixelFormat = 17;
        this.m_width = layer.m_width;
        this.m_height = layer.m_height;
        this.m_startX = layer.m_startX;
        this.m_startY = layer.m_startY;
        this.m_pixelFormat = layer.m_pixelFormat;
        this.m_size = new Point2i(this.m_width, this.m_height);
        this.m_bitDepth = layer.m_bitDepth;
        if (layer.m_palette != null) {
            this.m_palette = new Palette(layer.m_palette);
        }
        else {
            this.m_palette = null;
        }
        if (layer.m_data != null) {
            this.m_data = new byte[layer.m_data.length];
            System.arraycopy(layer.m_data, 0, this.m_data, 0, this.m_data.length);
        }
        else {
            this.m_data = null;
        }
        if (layer.m_alphaMask != null) {
            this.m_alphaMask = new AlphaMask(layer.m_alphaMask);
        }
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public Point2i getSize() {
        return this.m_size;
    }
    
    public short getStartX() {
        return this.m_startX;
    }
    
    public short getStartY() {
        return this.m_startY;
    }
    
    public void setStart(final short startX, final short startY) {
        this.m_startX = startX;
        this.m_startY = startY;
    }
    
    public int getAlpha(final int x, final int y) {
        assert x < this.m_width;
        assert y < this.m_height;
        if (this.m_bitDepth == 32) {
            final int pixelPos = (y * this.m_width + x) * 4;
            return this.m_data[pixelPos + 3] & 0xFF;
        }
        return 255;
    }
    
    public Color getPixel(final int x, final int y) {
        final Color pixelColor = this.getPixelColor(x, y);
        if (this.getPixelFormat() == 18 || this.getPixelFormat() == 2) {
            pixelColor.setARGB(pixelColor.getABGR());
        }
        return pixelColor;
    }
    
    private Color getPixelColor(final int x, final int y) {
        assert x < this.m_width;
        assert y < this.m_height;
        final int pixelPos = (y * this.m_width + x) * ((this.m_bitDepth + 7) / 8);
        if (this.m_bitDepth == 32) {
            return new Color(this.m_data[pixelPos], this.m_data[pixelPos + 1], this.m_data[pixelPos + 2], this.m_data[pixelPos + 3]);
        }
        if (this.m_bitDepth == 24) {
            return new Color(this.m_data[pixelPos], this.m_data[pixelPos + 1], this.m_data[pixelPos + 2], (byte)(-1));
        }
        if (this.m_bitDepth == 8) {
            if (this.isPaletized()) {
                return this.m_palette.getColor(Caster.toUnsignedByte(this.m_data[pixelPos]));
            }
            return new Color(this.m_data[pixelPos], this.m_data[pixelPos], this.m_data[pixelPos], (byte)(-1));
        }
        else {
            if (this.m_bitDepth != 4) {
                return null;
            }
            if (pixelPos % 2 == 0) {
                return this.m_palette.getColor((byte)(this.m_data[pixelPos / 2] & 0xF0) >> 4);
            }
            return this.m_palette.getColor((byte)(this.m_data[pixelPos / 2] & 0xF));
        }
    }
    
    public void setPixel(final int x, final int y, final Color color) {
        assert x < this.m_width;
        assert y < this.m_height;
        final int pixelPos = (y * this.m_width + x) * ((this.m_bitDepth + 7) / 8);
        if (this.m_bitDepth == 32) {
            this.m_data[pixelPos] = color.getRedByte();
            this.m_data[pixelPos + 1] = color.getGreenByte();
            this.m_data[pixelPos + 2] = color.getBlueByte();
            this.m_data[pixelPos + 3] = color.getAlphaByte();
        }
        else if (this.m_bitDepth == 24) {
            this.m_data[pixelPos] = color.getRedByte();
            this.m_data[pixelPos + 1] = color.getGreenByte();
            this.m_data[pixelPos + 2] = color.getBlueByte();
        }
        else {
            assert false : "Unsupported color format";
        }
    }
    
    public int getBitDepth() {
        return this.m_bitDepth;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
    
    public boolean isPaletized() {
        return this.m_palette != null;
    }
    
    public Layer toRGB32() {
        if (this.m_bitDepth == 32) {
            return new Layer(this);
        }
        if (this.m_bitDepth == 24) {
            final byte[] buffer = new byte[this.m_width * this.m_height * 4];
            for (int j = 0, i = 0; i < this.m_data.length; buffer[j++] = this.m_data[i++], buffer[j++] = this.m_data[i++], buffer[j++] = this.m_data[i++], buffer[j++] = -1) {}
            final Layer resultLayer = new Layer(this.m_width, this.m_height, (short)32, null, buffer);
            resultLayer.setPixelFormat(this.getPixelFormat());
            return resultLayer;
        }
        assert false : "Unable to convert" + this.m_bitDepth + " bits to RGBA32";
        return null;
    }
    
    public void resize(final float scaleX, final float scaleY, final Kernel kernel) {
        final int newWidth = Math.round(this.getWidth() * scaleX);
        final int newHeight = Math.round(this.getHeight() * scaleY);
        assert newWidth > 0 && newHeight > 0;
        final int bytePerPixel = this.getBitDepth() >> 3;
        final byte[] newBuffer = new byte[newWidth * newHeight * bytePerPixel];
        if (kernel == null) {
            final int sizeX = (int)Math.ceil(1.0f / scaleX);
            final int sizeY = (int)Math.ceil(1.0f / scaleY);
            for (int l = 0; l < newHeight; ++l) {
                final int centerY = (int)(l / scaleY);
                for (int c = 0; c < newWidth; ++c) {
                    final int centerX = (int)(c / scaleX);
                    for (int b = 0; b < bytePerPixel; ++b) {
                        final byte data = this.applyKernel(sizeX, sizeY, centerX, centerY, b);
                        newBuffer[(l * newWidth + c) * bytePerPixel + b] = data;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < newHeight; ++i) {
                final int centerY2 = (int)(i / scaleY);
                for (int c2 = 0; c2 < newWidth; ++c2) {
                    final int centerX2 = (int)(c2 / scaleX);
                    for (int b2 = 0; b2 < bytePerPixel; ++b2) {
                        final byte data2 = this.applyKernel(kernel, centerX2, centerY2, b2);
                        newBuffer[(i * newWidth + c2) * bytePerPixel + b2] = data2;
                    }
                }
            }
        }
        this.m_width = newWidth;
        this.m_height = newHeight;
        this.m_size = new Point2i(this.m_width, this.m_height);
        this.m_data = newBuffer;
    }
    
    public void releaseData() {
        this.m_data = null;
    }
    
    public void computeAlphaMask(final int alphaLevel) {
        this.m_alphaMask = new AlphaMask(this, alphaLevel);
    }
    
    public AlphaMask getAlphaMask() {
        return this.m_alphaMask;
    }
    
    public void setAlphaMask(final byte[] buffer, final byte resize) {
        this.m_alphaMask = new AlphaMask(buffer, this.m_width, resize);
    }
    
    public void applyLayer(final Layer layer) {
        assert layer.m_width <= this.m_width && layer.m_height <= this.m_height : "layer trop grand";
        assert layer.m_bitDepth == this.m_bitDepth : "Impossible de merger des layers de profondeur diff\u00e9rente";
        if (this.m_bitDepth != 32 || layer.m_bitDepth != 32) {
            return;
        }
        for (int x = 0; x < layer.m_width; ++x) {
            for (int y = 0; y < layer.m_height; ++y) {
                final int pixelPos = (y * this.m_width + x) * ((this.m_bitDepth + 7) / 8);
                final float alphaFactor = Caster.toUnsignedByte(layer.m_data[pixelPos + 3]) / 255.0f;
                if (alphaFactor != 0.0f) {
                    this.m_data[pixelPos] = (byte)Math.min(255.0f, Caster.toUnsignedByte(this.m_data[pixelPos]) * (1.0f - alphaFactor) + Caster.toUnsignedByte(layer.m_data[pixelPos]) * alphaFactor);
                    this.m_data[pixelPos + 1] = (byte)Math.min(255.0f, Caster.toUnsignedByte(this.m_data[pixelPos + 1]) * (1.0f - alphaFactor) + Caster.toUnsignedByte(layer.m_data[pixelPos + 1]) * alphaFactor);
                    this.m_data[pixelPos + 2] = (byte)Math.min(255.0f, Caster.toUnsignedByte(this.m_data[pixelPos + 2]) * (1.0f - alphaFactor) + Caster.toUnsignedByte(layer.m_data[pixelPos + 2]) * alphaFactor);
                    this.m_data[pixelPos + 3] = (byte)Math.min(255.0f, Caster.toUnsignedByte(this.m_data[pixelPos + 3]) * (1.0f - alphaFactor) + Caster.toUnsignedByte(layer.m_data[pixelPos + 3]));
                }
            }
        }
    }
    
    public void replaceColorWith(final Color.ColorComparator comparator) {
        assert this.m_bitDepth == 24 : "Unsupported color format for this operation";
        final byte pixelFormat = this.getPixelFormat();
        for (int x = 0; x < this.m_width; ++x) {
            for (int y = 0; y < this.m_height; ++y) {
                final int pixelPos = (y * this.m_width + x) * ((this.m_bitDepth + 7) / 8);
                final byte r = this.m_data[pixelPos];
                final byte g = this.m_data[pixelPos + 1];
                final byte b = this.m_data[pixelPos + 2];
                final byte a = (byte)((this.m_bitDepth == 32) ? this.m_data[pixelPos + 3] : -1);
                if (comparator.matches(r, g, b, a, pixelFormat)) {
                    comparator.replace(this.m_data, pixelPos, pixelFormat);
                }
            }
        }
    }
    
    public void flipHorizontally() {
        final Color[][] buffer = new Color[this.m_width][this.m_height];
        for (int x = 0; x < this.m_width; ++x) {
            for (int y = 0; y < this.m_height; ++y) {
                buffer[x][y] = this.getPixelColor(this.m_width - x - 1, y);
            }
        }
        for (int x = 0; x < this.m_width; ++x) {
            for (int y = 0; y < this.m_height; ++y) {
                this.setPixel(x, y, buffer[x][y]);
            }
        }
    }
    
    public void updateData(final ByteBuffer buffer, final int width, final int height) {
        final int newWidth = MathHelper.nearestGreatestPowOfTwo(width);
        final int newHeight = MathHelper.nearestGreatestPowOfTwo(height);
        final int byteDepth = this.m_bitDepth / 8;
        if (this.m_data == null || this.m_data.length != newWidth * newHeight * byteDepth) {
            this.m_data = new byte[newWidth * newHeight * byteDepth];
        }
        final int lineSize = width * byteDepth;
        final int newLineSize = newWidth * byteDepth;
        final int nbLines = buffer.capacity() / lineSize;
        int newBufferPos = 0;
        buffer.rewind();
        final byte[] bufferData = new byte[lineSize];
        for (int line = 0; line < nbLines; ++line) {
            buffer.get(bufferData, 0, lineSize);
            System.arraycopy(bufferData, 0, this.m_data, newBufferPos, lineSize);
            newBufferPos += newLineSize;
        }
        this.m_width = width;
        this.m_height = height;
        this.m_size = new Point2i(newWidth, newHeight);
    }
    
    @Override
    protected void delete() {
        this.m_data = null;
        this.m_pixelFormat = 0;
        this.m_alphaMask = null;
        if (this.m_palette != null) {
            this.m_palette.removeReference();
            this.m_palette = null;
        }
    }
    
    private byte applyKernel(final Kernel kernel, final int x, final int y, final int component) {
        final int bytePerPixel = this.getBitDepth() >> 3;
        final int halfSize = kernel.getSize() / 2;
        final float[] kernelBuffer = kernel.getBuffer();
        float kernelRes = 0.0f;
        int k = -1;
        for (int j = 0; j < kernel.getSize(); ++j) {
            final int l = y + j - halfSize;
            if (l >= 0 && l <= this.getHeight()) {
                final int offset = l * this.getWidth();
                for (int i = 0; i < kernel.getSize(); ++i) {
                    final int c = x + i - halfSize;
                    ++k;
                    if (c >= 0 && c <= this.getWidth()) {
                        final float value = this.m_data[(offset + c) * bytePerPixel + component] & 0xFF;
                        kernelRes += kernelBuffer[k] * value;
                    }
                }
            }
            else {
                k += kernel.getSize();
            }
        }
        if (kernelRes < 0.0f) {
            kernelRes = 0.0f;
        }
        else if (kernelRes > 255.0f) {
            kernelRes = 255.0f;
        }
        if (kernelRes > 127.0f) {
            kernelRes -= 256.0f;
        }
        return (byte)kernelRes;
    }
    
    private byte applyKernel(final int sizeX, final int sizeY, final int x, final int y, final int component) {
        final int bytePerPixel = this.getBitDepth() >> 3;
        final float ratio = 1.0f / (sizeX * sizeY);
        float kernelRes = 0.0f;
        for (int j = 0; j < sizeY; ++j) {
            final int l = y + j;
            if (l >= 0 && l < this.getHeight()) {
                final int offset = l * this.getWidth();
                for (int i = 0; i < sizeX; ++i) {
                    final int c = x + i;
                    if (c >= 0 && c < this.getWidth()) {
                        final float value = this.m_data[(offset + c) * bytePerPixel + component] & 0xFF;
                        kernelRes += ratio * value;
                    }
                }
            }
        }
        if (kernelRes < 0.0f) {
            kernelRes = 0.0f;
        }
        else if (kernelRes > 255.0f) {
            kernelRes = 255.0f;
        }
        if (kernelRes > 127.0f) {
            kernelRes -= 256.0f;
        }
        return (byte)kernelRes;
    }
    
    private void paletteToRGB24(final byte[] buffer, final TypeRef<Integer> offset, final byte colorIndex) {
        int index = offset.get();
        final Color color = this.m_palette.getColor(Caster.toUnsignedByte(colorIndex));
        buffer[index++] = color.getRedByte();
        buffer[index++] = color.getGreenByte();
        buffer[index++] = color.getBlueByte();
        offset.set(index);
    }
    
    public byte getPixelFormat() {
        return this.m_pixelFormat;
    }
    
    public void setPixelFormat(final byte pixelFormat) {
        this.m_pixelFormat = pixelFormat;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Layer.class);
    }
}
