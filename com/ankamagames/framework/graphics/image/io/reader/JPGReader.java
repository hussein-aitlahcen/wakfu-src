package com.ankamagames.framework.graphics.image.io.reader;

import org.apache.log4j.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.sun.opengl.util.texture.*;
import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class JPGReader extends ImageReader
{
    private static final Logger m_logger;
    
    @Override
    public Image loadImage(final String fileName) {
        BufferedImage image = null;
        try {
            final InputStream input = ContentFileHelper.openFile(fileName);
            image = ImageIO.read(input);
            input.close();
        }
        catch (IOException e) {
            JPGReader.m_logger.error((Object)"Exception", (Throwable)e);
        }
        if (image == null) {
            return null;
        }
        return Image.createImage(image);
    }
    
    private static Image createImage(final InputStream stream) throws IOException {
        final TextureData td = TextureIO.newTextureData(stream, 6407, 6407, false, "jpg");
        final Buffer buffer = td.getBuffer();
        final Layer layer = new Layer(td.getWidth(), td.getHeight(), (short)24, null, ((ByteBuffer)buffer).array(), 0, buffer.limit());
        Image.BGRAToRGBA(layer.getData(), layer.getBitDepth());
        final Image result = new Image(FourCC.JPG, layer);
        layer.removeReference();
        return result;
    }
    
    @Override
    protected Image loadImageFromStream(final ExtendedDataInputStream stream) throws IOException {
        try {
            stream.order(ByteOrder.BIG_ENDIAN);
            return createImage(new RegularExtendedDataInputStream(stream));
        }
        catch (IOException e) {
            JPGReader.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected Point2i getSize(final ExtendedDataInputStream stream) throws IOException {
        stream.order(ByteOrder.BIG_ENDIAN);
        final byte first = stream.readByte();
        final byte second = stream.readByte();
        if (first != -1 || second != -40) {
            return new Point2i(0, 0);
        }
        while (true) {
            int discardedBytes = 0;
            for (int marker = stream.readByte(); marker != -1; marker = stream.readByte()) {
                ++discardedBytes;
            }
            int marker;
            do {
                marker = stream.readByte();
            } while (marker == -1);
            if (discardedBytes != 0) {
                return new Point2i(0, 0);
            }
            switch (marker) {
                case -64:
                case -63:
                case -62:
                case -61:
                case -59:
                case -58:
                case -57:
                case -55:
                case -54:
                case -53:
                case -51:
                case -50:
                case -49: {
                    stream.skip(3);
                    final short height = stream.readShort();
                    final short width = stream.readShort();
                    return new Point2i(width, height);
                }
                case -39:
                case -38: {
                    return new Point2i(0, 0);
                }
                default: {
                    final int length = stream.readShort();
                    if (length < 2) {
                        return new Point2i(0, 0);
                    }
                    stream.skip(length - 2);
                    continue;
                }
            }
        }
    }
    
    @Override
    protected int getHeaderSize() {
        return Integer.MAX_VALUE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)JPGReader.class);
    }
}
