package com.ankamagames.framework.graphics.image.io.reader;

import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.sun.opengl.util.texture.*;
import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class PNGReader extends ImageReader
{
    private static final Logger m_logger;
    
    @Override
    public Image loadImage(final String fileName) {
        try {
            final InputStream stream = ContentFileHelper.openFile(fileName);
            final Image result = createImage(stream);
            stream.close();
            return result;
        }
        catch (IOException e) {
            PNGReader.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    private static Image createImage(final InputStream stream) throws IOException {
        final TextureData td = TextureIO.newTextureData(stream, 6408, 6408, false, "png");
        final Buffer buffer = td.getBuffer();
        final Layer layer = new Layer(td.getWidth(), td.getHeight(), (short)32, null, ((ByteBuffer)buffer).array(), 0, buffer.limit());
        final Image result = new Image(FourCC.PNG, layer);
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
            PNGReader.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected Point2i getSize(final ExtendedDataInputStream stream) throws IOException {
        stream.order(ByteOrder.BIG_ENDIAN);
        final byte[] header = new byte[8];
        stream.readBytes(header);
        if (!checkHeader(header)) {
            return null;
        }
        stream.skip(8);
        return new Point2i(stream.readInt(), stream.readInt());
    }
    
    private static boolean checkHeader(final byte[] header) {
        return header[0] == -119 && header[1] == 80 && header[2] == 78 && header[3] == 71 && header[4] == 13 && header[5] == 10 && header[6] == 26 && header[7] == 10;
    }
    
    @Override
    protected int getHeaderSize() {
        return 24;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PNGReader.class);
    }
}
