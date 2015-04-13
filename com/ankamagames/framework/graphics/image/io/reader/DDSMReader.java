package com.ankamagames.framework.graphics.image.io.reader;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.io.DDS.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.image.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class DDSMReader extends ImageReader
{
    private static final Logger m_logger;
    
    @Override
    protected Image loadImageFromStream(final ExtendedDataInputStream bitStream) throws IOException {
        try {
            bitStream.skip(4);
            final int imgWidth = bitStream.readShort();
            final int imgHeight = bitStream.readShort();
            final int ddsSize = bitStream.readInt();
            final int maskSize = bitStream.readInt();
            final int ddsKey = bitStream.readInt();
            final DDSSurfaceHeader surfaceHeader = new DDSSurfaceHeader();
            surfaceHeader.SetFromStream(bitStream);
            final FourCC format = new FourCC(surfaceHeader.m_pixelFormat.m_fourCC);
            int blockSize;
            if (format.getID() == FourCC.stringToID("DXT1")) {
                blockSize = 8;
            }
            else {
                if (format.getID() != FourCC.stringToID("DXT2") && format.getID() != FourCC.stringToID("DXT3") && format.getID() != FourCC.stringToID("DXT4") && format.getID() != FourCC.stringToID("DXT5")) {
                    DDSMReader.m_logger.error((Object)("Unsupported format " + format.toString()));
                    return null;
                }
                blockSize = 16;
            }
            assert surfaceHeader.m_numMipMap == 1 : "DDSM file can't have mipmaps";
            final int width = surfaceHeader.m_width;
            final int height = surfaceHeader.m_height;
            final int surfaceSize = (width + 3) / 4 * ((height + 3) / 4) * blockSize;
            final byte[] surfaceData = new byte[surfaceSize];
            bitStream.readBytes(surfaceData);
            final Layer layer = new Layer(imgWidth, imgHeight, (short)32, null, surfaceData);
            final byte[] maskData = new byte[maskSize];
            bitStream.readBytes(maskData);
            layer.setAlphaMask(maskData, (byte)1);
            final Image result = new Image(format, layer);
            layer.removeReference();
            return result;
        }
        catch (IOException e) {
            DDSMReader.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected Point2i getSize(final ExtendedDataInputStream stream) throws IOException {
        stream.skip(4);
        return new Point2i(stream.readShort(), stream.readShort());
    }
    
    @Override
    protected int getHeaderSize() {
        return 8;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DDSMReader.class);
    }
}
