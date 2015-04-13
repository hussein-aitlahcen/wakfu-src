package com.ankamagames.framework.graphics.image.io.reader;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.io.DDS.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.image.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class DDSReader extends ImageReader
{
    private static final Logger m_logger;
    
    @Override
    protected Image loadImageFromStream(final ExtendedDataInputStream bitStream) throws IOException {
        try {
            bitStream.skip(4);
            final DDSSurfaceHeader surfaceHeader = new DDSSurfaceHeader();
            surfaceHeader.SetFromStream(bitStream);
            final FourCC format = new FourCC(surfaceHeader.m_pixelFormat.m_fourCC);
            int blockSize;
            if (format.getID() == FourCC.stringToID("DXT1")) {
                blockSize = 8;
            }
            else {
                if (format.getID() != FourCC.stringToID("DXT2") && format.getID() != FourCC.stringToID("DXT3") && format.getID() != FourCC.stringToID("DXT4") && format.getID() != FourCC.stringToID("DXT5")) {
                    DDSReader.m_logger.error((Object)("Unsupported format " + format.toString()));
                    return null;
                }
                blockSize = 16;
            }
            final Layer[] layers = new Layer[surfaceHeader.m_numMipMap];
            int width = surfaceHeader.m_width;
            int height = surfaceHeader.m_height;
            for (int level = 0; level < surfaceHeader.m_numMipMap; ++level) {
                final int surfaceSize = (width + 3) / 4 * ((height + 3) / 4) * blockSize;
                final byte[] surfaceData = new byte[surfaceSize];
                bitStream.readBytes(surfaceData);
                layers[level] = new Layer(width, height, (short)32, null, surfaceData);
                width /= 2;
                height /= 2;
                if (width == 0) {
                    width = 1;
                }
                if (height == 0) {
                    height = 1;
                }
            }
            final Image result = new Image(format, layers);
            for (final Layer l : layers) {
                l.removeReference();
            }
            return result;
        }
        catch (IOException e) {
            DDSReader.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected Point2i getSize(final ExtendedDataInputStream stream) throws IOException {
        stream.skip(4);
        final DDSSurfaceHeader surfaceHeader = new DDSSurfaceHeader();
        surfaceHeader.SetFromStream(stream);
        return new Point2i(surfaceHeader.m_width, surfaceHeader.m_height);
    }
    
    @Override
    protected int getHeaderSize() {
        return 128;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DDSReader.class);
    }
}
