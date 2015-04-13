package com.ankamagames.framework.graphics.image;

import com.ankamagames.framework.kernel.core.io.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.awt.image.*;
import com.sun.opengl.util.texture.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.io.reader.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;

public class Image extends ReferenceCounter
{
    public static final Kernel KERNEL_NEAREST;
    public static final Kernel KERNEL_GAUSSIAN;
    public static final Kernel KERNEL_CUBIC;
    public static final Kernel KERNEL_AVERAGE;
    protected AsyncURL m_asyncURL;
    protected boolean m_needUpdate;
    protected static final Layer m_defaultLayer;
    private static final Logger m_logger;
    private FourCC m_format;
    private ArrayList<Layer> m_layers;
    private Point2i m_size;
    private Point2i m_powerOfTwoSize;
    private boolean m_powerOfTwo;
    private static final boolean DEBUG = false;
    
    public Image() {
        this(FourCC.RAW, Image.m_defaultLayer);
    }
    
    public static Image createImage(final BufferedImage image) {
        final ImageInfos imageInfos = new ImageInfos(image);
        imageInfos.extractInfos();
        final int gl = imageInfos.getGl();
        final TextureData td = TextureIO.newTextureData(image, gl, gl, false);
        final Buffer buffer = td.getBuffer();
        final Layer layer = new Layer(td.getWidth(), td.getHeight(), imageInfos.getBitDepth(), null, ((ByteBuffer)buffer).array(), 0, buffer.limit());
        layer.setPixelFormat(imageInfos.getLayer());
        final Image result = new Image(FourCC.RAW, layer);
        layer.removeReference();
        return result;
    }
    
    private void initSizeFromLayer() {
        if (!this.m_layers.isEmpty()) {
            this.m_size = this.getSize(0);
            this.m_powerOfTwoSize = new Point2i(this.m_size);
        }
    }
    
    public Image(final FourCC format, final Layer[] layers) {
        super();
        this.m_needUpdate = false;
        this.m_format = format;
        this.m_layers = new ArrayList<Layer>(layers.length);
        for (final Layer layer : layers) {
            this.addLayer(layer);
        }
        this.initSizeFromLayer();
    }
    
    public Image(final FourCC format, final Layer layer) {
        super();
        this.m_needUpdate = false;
        this.m_format = format;
        this.m_layers = new ArrayList<Layer>(1);
        this.addLayer(layer);
        this.initSizeFromLayer();
    }
    
    public Image(final Image image) {
        super();
        this.m_needUpdate = false;
        assert image.m_format != null : "On copie une image releas\u00e9e";
        this.m_format = image.m_format;
        final int numLayers = image.m_layers.size();
        this.m_layers = new ArrayList<Layer>(numLayers);
        for (int i = 0; i < numLayers; ++i) {
            this.addLayer(new Layer(image.getLayer(i)));
        }
        this.initSizeFromLayer();
    }
    
    public Image(final String fileName) {
        super();
        this.m_needUpdate = false;
        this.readFromFile(fileName);
    }
    
    public boolean readFromFileAsync(final String fileName) {
        try {
            this.m_size = readImageSizeFromFile(fileName);
            if (this.m_size == null) {
                return false;
            }
            this.m_asyncURL = ContentFileHelper.loadAsyncURL(fileName);
            this.m_powerOfTwoSize = new Point2i(MathHelper.nearestGreatestPowOfTwo(this.m_size.getX()), MathHelper.nearestGreatestPowOfTwo(this.m_size.getY()));
        }
        catch (MalformedURLException e) {
            return this.readFromFile(fileName);
        }
        return true;
    }
    
    public boolean readFromFile(final String fileName) {
        return this.setFrom(createImageFromFile(fileName));
    }
    
    private boolean setFrom(final Image img) {
        assert img.m_format != null : "On copie une image releas\u00e9e";
        if (img == null) {
            return false;
        }
        this.m_format = img.m_format;
        if (this.m_layers != null) {
            for (int i = 0; i < this.m_layers.size(); ++i) {
                this.m_layers.get(i).removeReference();
            }
        }
        this.m_layers = img.m_layers;
        this.initSizeFromLayer();
        img.m_layers = null;
        img.removeReference();
        return true;
    }
    
    public static Image createFromByteArray(final byte[] data, final String dataType) {
        final Image img = readImageFromByteArray(data, dataType);
        if (img == null) {
            return null;
        }
        img.initSizeFromLayer();
        return img;
    }
    
    public boolean readFromFiles(final String... filesNames) {
        assert filesNames.length > 0;
        final Layer remove = this.m_layers.remove(0);
        remove.removeReference();
        FourCC format = null;
        int width = -1;
        int height = -1;
        for (int i = 0; i < filesNames.length; ++i) {
            final Image img = createImageFromFile(filesNames[i]);
            if (img == null) {
                return false;
            }
            if (format != null && !format.equals(img.m_format)) {
                img.removeReference();
                return false;
            }
            format = img.m_format;
            assert img.m_format != null : "image releas\u00e9e";
            for (int j = 0, size = img.getNumLayers(); j < size; ++j) {
                final Layer layer = img.getLayer(j);
                if (width == -1 && height == -1) {
                    width = layer.getWidth();
                    height = layer.getHeight();
                }
                else if (width != layer.getWidth() || height != layer.getHeight()) {
                    img.removeReference();
                    return false;
                }
                this.addLayer(layer);
            }
            img.removeReference();
        }
        this.m_format = format;
        this.initSizeFromLayer();
        return true;
    }
    
    public void replaceColorWith(final int layer, final Color.ColorComparator c) {
        this.m_layers.get(layer).replaceColorWith(c);
    }
    
    public void setLayer(final int layerIndex, final Layer layer) {
        layer.addReference();
        this.m_layers.get(layerIndex).removeReference();
        this.m_layers.set(layerIndex, layer);
    }
    
    public final void addLayer(final Layer layer) {
        layer.addReference();
        this.m_layers.add(layer);
    }
    
    public final boolean isEmpty() {
        return this.m_layers == null || this.m_layers.isEmpty() || this.m_layers.get(0) == Image.m_defaultLayer;
    }
    
    public Layer getLayer(final int layerIndex) {
        assert layerIndex < this.m_layers.size();
        return this.m_layers.get(layerIndex);
    }
    
    public final int getNumLayers() {
        if (this.m_layers == null) {
            return 0;
        }
        return this.m_layers.size();
    }
    
    public final FourCC getFormat() {
        return this.m_format;
    }
    
    public final Point2i getSize(final int layerIndex) {
        return this.getLayer(layerIndex).getSize();
    }
    
    public Point2i getSize() {
        return this.m_size;
    }
    
    public Color getPixel(final int x, final int y, final int layerIndex) {
        return this.getLayer(layerIndex).getPixel(x, y);
    }
    
    public void setPowerOfTwo(final boolean powerOfTwo) {
        this.m_powerOfTwo = powerOfTwo;
    }
    
    public Point2i getPowerOfTwoSize() {
        return this.m_powerOfTwoSize;
    }
    
    public final void toPowerOfTwo() {
        for (int i = 0, num = this.getNumLayers(); i < num; ++i) {
            final Layer newLayer = toPowerOfTwo(this.getLayer(i));
            this.setLayer(i, newLayer);
            newLayer.removeReference();
        }
        if (!this.m_layers.isEmpty()) {
            this.m_powerOfTwoSize = this.getSize(0);
        }
    }
    
    private static Layer toPowerOfTwo(final Layer layer) {
        final Layer newLayer = toPowerOfTwo(layer.getData(), layer.getWidth(), layer.getHeight(), layer.getBitDepth());
        newLayer.setPixelFormat(layer.getPixelFormat());
        newLayer.setStart(layer.getStartX(), layer.getStartY());
        return newLayer;
    }
    
    public void toUpdate() {
        this.m_needUpdate = true;
    }
    
    public final void resize(final float scaleX, final float scaleY, final Kernel kernel) {
        for (final Layer layer : this.m_layers) {
            layer.resize(scaleX, scaleY, kernel);
        }
        this.initSizeFromLayer();
    }
    
    public final void flatten() {
        for (int i = this.m_layers.size() - 1; i >= 1; --i) {
            final Layer layer = this.m_layers.get(i);
            final Layer layer2 = this.m_layers.get(i - 1);
            if (layer.getBitDepth() != 32) {
                this.setLayer(i - 1, layer);
            }
            else {
                layer2.applyLayer(layer);
                this.m_layers.remove(i);
                layer.removeReference();
            }
        }
    }
    
    public static Point2i readImageSizeFromFile(final String fileName) {
        final ImageReader reader = getImageReaderFor(fileName);
        if (reader == null) {
            return null;
        }
        final Point2i imageSize = reader.getImageSize(fileName);
        if (imageSize == null) {
            Image.m_logger.error((Object)("Impossible de lire l'image " + fileName));
        }
        return imageSize;
    }
    
    public static Image createImageFromFile(final String fileName) {
        final ImageReader reader = getImageReaderFor(fileName);
        if (reader == null) {
            return null;
        }
        final Image img = reader.loadImage(fileName);
        if (img == null) {
            Image.m_logger.error((Object)("Impossible de lire l'image " + fileName));
        }
        return img;
    }
    
    public static Image readImageFromByteArray(final byte[] data, final String dataType) {
        assert data != null;
        final ImageReader reader = ImageReaderFactory.getInstance().getReader(dataType);
        if (reader == null) {
            Image.m_logger.error((Object)("No ImageReader registered for file ext (." + dataType + ")"));
            Image.m_logger.error((Object)("Did you forget to call ImageReaderFactory.getInstance().registerReader (\"" + dataType + "\", new " + dataType + "Reader ()); ?"));
            return null;
        }
        final Image img = reader.loadImage(data);
        if (img == null) {
            Image.m_logger.error((Object)"Impossible de lire les donn\u00e9es d'image.");
        }
        return img;
    }
    
    public static void BGRAToRGBA(final byte[] buffer, final int bitDepth) {
        final int byteDepth = bitDepth / 8;
        if (bitDepth == 16) {
            byte color0;
            byte color;
            for (int pixel = 0; pixel < buffer.length; buffer[pixel++] = (byte)(color << 3 | (color0 & 0x7)), buffer[pixel++] = (byte)((color & 0xE0) | color0 >> 3)) {
                color0 = buffer[pixel];
                color = buffer[pixel + 1];
            }
        }
        else if (bitDepth == 24 || bitDepth == 32) {
            for (int pixel = 0; pixel < buffer.length; pixel += byteDepth) {
                final byte Blue = buffer[pixel];
                buffer[pixel] = buffer[pixel + 2];
                buffer[pixel + 2] = Blue;
            }
        }
    }
    
    public static void flipVertically(final byte[] buffer, final int lineSize) {
        assert buffer.length % lineSize == 0 : "Unable to flip the image since the buffer length is not a muptiple of line size";
        final byte[] tempLine = new byte[lineSize];
        final int nbLines = buffer.length / lineSize;
        int endPos = buffer.length - lineSize;
        int beginPos = 0;
        for (int line = 0; line < nbLines / 2; ++line) {
            System.arraycopy(buffer, beginPos, tempLine, 0, lineSize);
            System.arraycopy(buffer, endPos, buffer, beginPos, lineSize);
            System.arraycopy(tempLine, 0, buffer, endPos, lineSize);
            endPos -= lineSize;
            beginPos += lineSize;
        }
    }
    
    public static Layer toPowerOfTwo(final byte[] buffer, final int width, final int height, final int bitDepth) {
        final int newWidth = MathHelper.nearestGreatestPowOfTwo(width);
        final int newHeight = MathHelper.nearestGreatestPowOfTwo(height);
        if (newWidth == width && newHeight == height) {
            return new Layer(newWidth, newHeight, (short)bitDepth, null, buffer, 0, buffer.length);
        }
        final int byteDepth = bitDepth / 8;
        final byte[] newBuffer = new byte[newWidth * newHeight * byteDepth];
        final int lineSize = width * byteDepth;
        final int newLineSize = newWidth * byteDepth;
        final int nbLines = buffer.length / lineSize;
        int bufferPos = 0;
        int newBufferPos = 0;
        for (int line = 0; line < nbLines; ++line) {
            System.arraycopy(buffer, bufferPos, newBuffer, newBufferPos, lineSize);
            bufferPos += lineSize;
            newBufferPos += newLineSize;
        }
        return new Layer(newWidth, newHeight, (short)bitDepth, null, newBuffer);
    }
    
    public static Layer crop(final Layer layer, final int top, final int left, final int bottom, final int right) {
        assert top >= 0 && bottom > top && bottom <= layer.getHeight();
        assert left >= 0 && right > left && right <= layer.getWidth();
        final int byteDepth = layer.getBitDepth() / 8;
        final int newWidth = right - left;
        final int newHeight = bottom - top;
        final byte[] newBuffer = new byte[newWidth * newHeight * byteDepth];
        final int lineSize = layer.getWidth() * byteDepth;
        final int nbLines = bottom - top;
        final int newLineSize = newWidth * byteDepth;
        final byte[] buffer = layer.getData();
        int bufferPos = top * lineSize + left * byteDepth;
        int newBufferPos = 0;
        for (int line = 0; line < nbLines; ++line) {
            System.arraycopy(buffer, bufferPos, newBuffer, newBufferPos, newLineSize);
            bufferPos += lineSize;
            newBufferPos += newLineSize;
        }
        final Layer resultLayer = new Layer(newWidth, newHeight, (short)layer.getBitDepth(), null, newBuffer);
        resultLayer.setPixelFormat(layer.getPixelFormat());
        return resultLayer;
    }
    
    public static void getVisibleRect(final Layer layer, final Rect rect) {
        rect.setXMin(layer.getWidth());
        rect.setYMin(layer.getHeight());
        rect.setXMax(0);
        rect.setYMax(0);
        for (int x = 0; x < layer.getWidth(); ++x) {
            for (int y = 0; y < layer.getHeight(); ++y) {
                if (layer.getAlpha(x, y) != 0) {
                    if (x < rect.getXMin()) {
                        rect.setXMin(x);
                    }
                    if (x > rect.getXMax()) {
                        rect.setXMax(x);
                    }
                    if (y < rect.getYMin()) {
                        rect.setYMin(y);
                    }
                    if (y > rect.getYMax()) {
                        rect.setYMax(y);
                    }
                }
            }
        }
        if (rect.getYMin() > rect.getYMax()) {
            rect.setXMin(0);
            rect.setXMax(0);
            rect.setYMin(0);
            rect.setYMax(0);
        }
    }
    
    public void recomputeAlphaMasks(final int alphaLevel) {
        for (final Layer layer : this.m_layers) {
            layer.computeAlphaMask(alphaLevel);
        }
    }
    
    public void onLoadComplete() {
        final String fileName = this.m_asyncURL.getURL().getFile();
        final ImageReader reader = getImageReaderFor(fileName);
        if (reader == null) {
            return;
        }
        if (!this.setFrom(reader.loadImage(this.m_asyncURL.getData()))) {
            Image.m_logger.error((Object)("Impossible de lire l'image " + fileName));
            return;
        }
        if (this.m_powerOfTwo) {
            this.toPowerOfTwo();
        }
        this.m_asyncURL = null;
        this.m_needUpdate = true;
    }
    
    private static ImageReader getImageReaderFor(final String fileName) {
        final String fileExt = FileHelper.getFileExt(fileName).toUpperCase();
        final ImageReader reader = ImageReaderFactory.getInstance().getReader(fileExt);
        if (reader == null) {
            Image.m_logger.error((Object)("No ImageReader registered for file ext (." + fileExt + ")"));
            Image.m_logger.error((Object)("Did you forget to call ImageReaderFactory.getInstance().registerReader (\"" + fileExt + "\", new " + fileExt + "Reader ()); ?"));
        }
        return reader;
    }
    
    @Override
    protected void delete() {
        super.delete();
        if (this.m_layers != null) {
            for (final Layer layer : this.m_layers) {
                layer.removeReference();
            }
            this.m_layers = null;
        }
        this.m_format = null;
        this.m_size = null;
        this.m_powerOfTwoSize = null;
        this.m_powerOfTwo = false;
    }
    
    static {
        KERNEL_NEAREST = new Kernel(new float[] { 1.0f });
        KERNEL_GAUSSIAN = new Kernel(3);
        KERNEL_CUBIC = new Kernel(3);
        KERNEL_AVERAGE = new Kernel(3);
        final float o = 1.5f;
        final float o2Square = 4.5f;
        final float[] kernelBuffer = new float[Image.KERNEL_GAUSSIAN.getSize() * Image.KERNEL_GAUSSIAN.getSize()];
        final int halfSize = Image.KERNEL_GAUSSIAN.getSize() / 2;
        float v = 0.0f;
        int k = -1;
        for (int i = -halfSize; i < halfSize + 1; ++i) {
            for (int j = -halfSize; j < halfSize + 1; ++j) {
                final float value = (float)(0.0707355302630646 * Math.exp(-(i * i + j * j) / 4.5f));
                v += value;
                kernelBuffer[++k] = value;
            }
        }
        for (k = 0; k < kernelBuffer.length; ++k) {
            final float[] array = kernelBuffer;
            final int n = k;
            array[n] /= v;
        }
        Image.KERNEL_GAUSSIAN.setKernel(kernelBuffer);
        int kernelSize = Image.KERNEL_CUBIC.getSize();
        float[] kernelBuffer2 = new float[kernelSize * kernelSize];
        int halfSize2 = kernelSize / 2;
        float v2 = 0.0f;
        int l = -1;
        for (int m = -halfSize2; m < halfSize2 + 1; ++m) {
            for (int j2 = -halfSize2; j2 < halfSize2 + 1; ++j2) {
                final float value2 = kernelSize + kernelSize * m * m + j2 * j2;
                v2 += value2;
                kernelBuffer2[++l] = value2;
            }
        }
        for (l = 0; l < kernelBuffer2.length; ++l) {
            final float[] array2 = kernelBuffer2;
            final int n2 = l;
            array2[n2] /= v2;
        }
        Image.KERNEL_CUBIC.setKernel(kernelBuffer2);
        kernelSize = Image.KERNEL_AVERAGE.getSize();
        kernelBuffer2 = new float[kernelSize * kernelSize];
        halfSize2 = kernelSize / 2;
        v2 = 0.0f;
        l = -1;
        for (int m = -halfSize2; m < halfSize2 + 1; ++m) {
            for (int j2 = -halfSize2; j2 < halfSize2 + 1; ++j2) {
                final float value2 = 1.0f;
                ++v2;
                kernelBuffer2[++l] = 1.0f;
            }
        }
        for (l = 0; l < kernelBuffer2.length; ++l) {
            final float[] array3 = kernelBuffer2;
            final int n3 = l;
            array3[n3] /= v2;
        }
        Image.KERNEL_AVERAGE.setKernel(kernelBuffer2);
        m_logger = Logger.getLogger((Class)Image.class);
        final int width = 128;
        final int height = 64;
        final byte[] data = new byte[32768];
        (m_defaultLayer = new Layer(128, 64, (short)32, null, data)).computeAlphaMask(255);
        Image.m_defaultLayer.addReference();
    }
    
    private static class ImageInfos
    {
        private final BufferedImage m_image;
        private int m_gl;
        private short m_bitDepth;
        private byte m_layer;
        
        ImageInfos(final BufferedImage image) {
            super();
            this.m_image = image;
        }
        
        public void extractInfos() {
            switch (this.m_image.getType()) {
                case 2:
                case 3:
                case 6:
                case 7: {
                    this.m_gl = 6408;
                    this.m_bitDepth = 32;
                    this.m_layer = 17;
                    break;
                }
                case 4:
                case 5: {
                    this.m_gl = 6407;
                    this.m_bitDepth = 24;
                    this.m_layer = 18;
                    break;
                }
                default: {
                    this.m_gl = 6407;
                    this.m_bitDepth = 24;
                    this.m_layer = 17;
                    break;
                }
            }
        }
        
        public int getGl() {
            return this.m_gl;
        }
        
        public short getBitDepth() {
            return this.m_bitDepth;
        }
        
        public byte getLayer() {
            return this.m_layer;
        }
        
        @Override
        public String toString() {
            return "ImageInfos{m_image=" + this.m_image + ", m_gl=" + this.m_gl + ", m_bitDepth=" + this.m_bitDepth + ", m_layer=" + this.m_layer + '}';
        }
    }
}
