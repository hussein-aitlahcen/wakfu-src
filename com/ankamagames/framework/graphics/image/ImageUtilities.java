package com.ankamagames.framework.graphics.image;

import org.apache.log4j.*;
import com.sun.opengl.util.texture.*;
import java.awt.geom.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.awt.*;
import java.awt.color.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import javax.imageio.metadata.*;
import java.util.*;
import javax.imageio.*;
import com.ankamagames.framework.graphics.image.io.writer.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.nio.*;
import java.io.*;

public class ImageUtilities
{
    protected static final Logger m_logger;
    
    public static BufferedImage addAlphaImageBorder(final BufferedImage srcImage, final int borderSize) {
        final BufferedImage newImage = new BufferedImage(srcImage.getWidth() + borderSize * 2, srcImage.getHeight() + borderSize * 2, 2);
        final Graphics bg = newImage.getGraphics();
        bg.drawImage(srcImage, borderSize, borderSize, null);
        bg.dispose();
        return newImage;
    }
    
    public static void alphaDemultiply(final BufferedImage image) {
        if (image != null) {
            for (int ty = 0; ty < image.getHeight(); ++ty) {
                final int y = ty + image.getMinY();
                for (int tx = 0; tx < image.getWidth(); ++tx) {
                    final int x = tx + image.getMinX();
                    int c = image.getRGB(x, y);
                    final float a = (c >> 24 & 0xFF) / 255.0f;
                    if (a != 0.0f) {
                        float r = (c >> 16 & 0xFF) / 255.0f;
                        float g = (c >> 8 & 0xFF) / 255.0f;
                        float b = (c & 0xFF) / 255.0f;
                        r = ((r / a < 1.0f) ? (r / a) : 1.0f);
                        g = ((g / a < 1.0f) ? (g / a) : 1.0f);
                        b = ((b / a < 1.0f) ? (b / a) : 1.0f);
                        c = ((int)(a * 255.0f) << 24 | (int)(r * 255.0f) << 16 | (int)(g * 255.0f) << 8 | (int)(b * 255.0f));
                        image.setRGB(x, y, c);
                    }
                }
            }
        }
    }
    
    public static void alphaPremultiply(final BufferedImage image) {
        if (image != null) {
            for (int ty = 0; ty < image.getHeight(); ++ty) {
                final int y = ty + image.getMinY();
                for (int tx = 0; tx < image.getWidth(); ++tx) {
                    final int x = tx + image.getMinX();
                    final int c = image.getRGB(x, y);
                    final float a = (c >> 24 & 0xFF) / 255.0f;
                    final float r = (c >> 16 & 0xFF) / 255.0f * a;
                    final float g = (c >> 8 & 0xFF) / 255.0f * a;
                    final float b = (c & 0xFF) / 255.0f * a;
                    image.setRGB(x, y, (int)(a * 255.0f) << 24 | (int)(r * 255.0f) << 16 | (int)(g * 255.0f) << 8 | (int)(b * 255.0f));
                }
            }
        }
    }
    
    public static BufferedImage convertToARGB(final BufferedImage srcImage) {
        final BufferedImage newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), 2);
        final Graphics bg = newImage.getGraphics();
        bg.drawImage(srcImage, 0, 0, null);
        bg.dispose();
        return newImage;
    }
    
    public static BufferedImage convertToARGB_PRE(final BufferedImage srcImage) {
        final BufferedImage newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), 3);
        final Graphics bg = newImage.getGraphics();
        bg.drawImage(srcImage, 0, 0, null);
        bg.dispose();
        return newImage;
    }
    
    public static BufferedImage fromTga(final InputStream inputStream) {
        try {
            final TextureData td = TextureIO.newTextureData(inputStream, false, "tga");
            final int h = td.getHeight();
            final int w = td.getWidth();
            final int lineSize = 4 * w;
            final ByteBuffer buffer = ByteBuffer.allocate(lineSize * h);
            final ByteBuffer input = (ByteBuffer)td.getBuffer();
            final byte[] line = new byte[w * 4];
            int offset = lineSize * h;
            for (int y = 0; y < h; ++y) {
                offset -= lineSize;
                input.get(line);
                buffer.position(offset);
                buffer.put(line);
            }
            buffer.rewind();
            return toImage(w, h, buffer.array(), PixelFormat.ABGR);
        }
        catch (IOException e) {
            ImageUtilities.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    public static BufferedImage fromTga(final String fileName) {
        try {
            final InputStream inputStream = new FileInputStream(fileName);
            return fromTga(inputStream);
        }
        catch (FileNotFoundException e) {
            ImageUtilities.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    public static BufferedImage copy(final BufferedImage source, final boolean verticallyFlipped) {
        if (source == null) {
            return null;
        }
        final int width = source.getWidth();
        final int height = source.getHeight();
        BufferedImage img = new BufferedImage(width, height, 2);
        img.getGraphics().drawImage(source, 0, 0, width, height, 0, 0, width, height, null);
        if (verticallyFlipped) {
            final AffineTransform tx = AffineTransform.getScaleInstance(1.0, -1.0);
            tx.translate(0.0, -source.getHeight(null));
            final AffineTransformOp op = new AffineTransformOp(tx, 1);
            img = op.filter(img, null);
        }
        return img;
    }
    
    public static Rect getClipPixelRectFromAlpha(final BufferedImage srcImage, final int alphaMin) {
        final int width = srcImage.getWidth();
        int ymax;
        final int height = ymax = srcImage.getHeight();
        int ymin = -1;
        int xmax = width;
        int xmin = -1;
    Label_0085:
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                final int alpha = (srcImage.getRGB(x, y) & 0xFF000000) >> 24 & 0xFF;
                if (alpha > alphaMin) {
                    ymin = y;
                    break Label_0085;
                }
            }
        }
    Label_0149:
        for (int y = height - 1; y >= 0; --y) {
            for (int x = 0; x < width; ++x) {
                final int alpha = (srcImage.getRGB(x, y) & 0xFF000000) >> 24 & 0xFF;
                if (alpha > alphaMin) {
                    ymax = y;
                    break Label_0149;
                }
            }
        }
    Label_0212:
        for (int x2 = 0; x2 < width; ++x2) {
            for (int y2 = 0; y2 < height; ++y2) {
                final int alpha = (srcImage.getRGB(x2, y2) & 0xFF000000) >> 24 & 0xFF;
                if (alpha > alphaMin) {
                    xmin = x2;
                    break Label_0212;
                }
            }
        }
    Label_0276:
        for (int x2 = width - 1; x2 >= 0; --x2) {
            for (int y2 = 0; y2 < height; ++y2) {
                final int alpha = (srcImage.getRGB(x2, y2) & 0xFF000000) >> 24 & 0xFF;
                if (alpha > alphaMin) {
                    xmax = x2;
                    break Label_0276;
                }
            }
        }
        if (xmin == -1 || ymin == -1) {
            return new Rect(0, 0, 0, 0);
        }
        return new Rect(xmin, xmax, ymin, ymax);
    }
    
    public static BufferedImage toImage(final int w, final int h, final byte[] data, final PixelFormat pixelFormat) {
        if (w == 0 || h == 0) {
            return null;
        }
        final DataBuffer buffer = new DataBufferByte(data, w * h);
        final int pixelStride = 4;
        final int scanlineStride = 4 * w;
        final WritableRaster raster = Raster.createInterleavedRaster(buffer, w, h, scanlineStride, 4, pixelFormat.getFormat(), null);
        final ColorSpace colorSpace = ColorSpace.getInstance(1000);
        final boolean hasAlpha = true;
        final boolean isAlphaPremultiplied = false;
        final int transparency = 3;
        final int transferType = 0;
        final ColorModel colorModel = new ComponentColorModel(colorSpace, true, false, 3, 0);
        return new BufferedImage(colorModel, raster, false, null);
    }
    
    public static void writeJpeg(final BufferedImage image, final OutputStream output, final float quality) throws IOException {
        assert quality >= 0.0f && quality <= 1.0f;
        final Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
        final ImageWriter writer = iter.next();
        final ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(2);
        iwp.setCompressionQuality(quality);
        writer.setOutput(new MemoryCacheImageOutputStream(output));
        writer.write(null, new IIOImage(image, null, null), iwp);
        writer.dispose();
    }
    
    public static void writeTga(final BufferedImage image, final String path) throws Exception {
        final TextureData td = new TextureData(6408, 6408, false, image);
        final Buffer buffer = td.getBuffer();
        final Layer layer = new Layer(td.getWidth(), td.getHeight(), (short)32, null, ((ByteBuffer)buffer).array(), 0, buffer.limit());
        final TGAWriter writer = new TGAWriter();
        layer.computeAlphaMask(0);
        com.ankamagames.framework.graphics.image.Image.BGRAToRGBA(layer.getData(), layer.getBitDepth());
        com.ankamagames.framework.graphics.image.Image.flipVertically(layer.getData(), layer.getWidth() * 4);
        final FileOutputStream fos = FileHelper.createFileOutputStream(path);
        final OutputBitStream stream = new OutputBitStream(fos);
        writer.ToStream(stream, new com.ankamagames.framework.graphics.image.Image(FourCC.TGA, layer));
        layer.removeReference();
        stream.close();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageUtilities.class);
    }
    
    public enum PixelFormat
    {
        ARGB {
            @Override
            int[] getFormat() {
                return new int[] { 0, 1, 2, 3 };
            }
        }, 
        ABGR {
            @Override
            int[] getFormat() {
                return new int[] { 2, 1, 0, 3 };
            }
        };
        
        abstract int[] getFormat();
    }
}
