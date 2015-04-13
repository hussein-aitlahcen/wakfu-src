package com.ankamagames.baseImpl.graphics.alea.display.effects.cloud;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.image.io.reader.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;

public class BitmapCloudGenerator implements CloudGenerator
{
    protected static Logger m_logger;
    private static final String TEXTURE_PATH;
    private final Perlin2D.Function m_correctionFunction;
    private float[] m_cloudMap;
    private int m_width;
    private int m_height;
    
    public BitmapCloudGenerator(final String filename, final Perlin2D.Function correctionFunction) {
        super();
        this.m_cloudMap = null;
        this.m_correctionFunction = correctionFunction;
        final String fullTexturePath = BitmapCloudGenerator.TEXTURE_PATH + filename;
        this.loadCloudTexture(fullTexturePath);
    }
    
    void loadCloudTexture(final String texturePath) {
        final ImageReader imageReader = ImageReaderFactory.getInstance().getReader("TGA");
        final Image image = imageReader.loadImage(texturePath);
        if (image == null) {
            return;
        }
        final Layer layer = image.getLayer(0);
        this.m_width = layer.getWidth();
        this.m_height = layer.getHeight();
        this.m_cloudMap = new float[this.m_height * this.m_width];
        for (int j = 0; j < this.m_height; ++j) {
            final int y = j * this.m_width;
            for (int i = 0; i < this.m_width; ++i) {
                final Color color = layer.getPixel(i, j);
                this.m_cloudMap[y + i] = color.getRed();
            }
        }
        image.removeReference();
    }
    
    @Override
    public void generate(final float[] dest, final int width, final int height, final int offX, final int offY) {
        if (this.m_cloudMap == null) {
            return;
        }
        int dx = offX % this.m_width;
        if (dx < 0) {
            dx += this.m_width;
        }
        int dy = offY % this.m_height;
        if (dy < 0) {
            dy += this.m_height;
        }
        if (this.m_correctionFunction == null) {
            for (int j = dy; j < dy + height; ++j) {
                final int ry = j % this.m_height;
                final int y = ry * this.m_width;
                final int oy = (j - dy) * height - dx;
                for (int i = dx; i < dx + width; ++i) {
                    final int rx = i % this.m_width;
                    dest[oy + i] = this.m_cloudMap[y + rx];
                }
            }
        }
        else {
            for (int j = dy; j < dy + height; ++j) {
                final int ry = j % this.m_height;
                final int y = ry * this.m_width;
                final int oy = (j - dy) * height - dx;
                for (int i = dx; i < dx + width; ++i) {
                    final int rx = i % this.m_width;
                    dest[oy + i] = this.m_correctionFunction.compute(rx, ry, this.m_cloudMap[y + rx]);
                }
            }
        }
    }
    
    static {
        BitmapCloudGenerator.m_logger = Logger.getLogger((Class)BitmapCloudGenerator.class);
        TEXTURE_PATH = Engine.getInstance().getEffectPath() + "textures/";
    }
}
