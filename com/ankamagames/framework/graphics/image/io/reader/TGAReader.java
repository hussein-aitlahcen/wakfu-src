package com.ankamagames.framework.graphics.image.io.reader;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.io.TGA.*;
import java.io.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class TGAReader extends ImageReader
{
    private static final Logger m_logger;
    
    @Override
    protected Image loadImageFromStream(final ExtendedDataInputStream bitStream) throws IOException {
        try {
            final TGAHeader header = new TGAHeader();
            header.setFromStream(bitStream);
            Palette palette = null;
            if (header.m_colorMapLength != 0) {
                if (header.m_bitDepth != 4 && header.m_bitDepth != 8) {
                    final int colorSize = header.m_colorMapBitDepth + 7 >> 3;
                    final int paletteSize = header.m_colorMapLength * colorSize;
                    bitStream.skip(paletteSize);
                }
                else {
                    int offset = bitStream.getOffset();
                    palette = new Palette(header.m_colorMapLength);
                    for (int colorIndex = 0; colorIndex < header.m_colorMapLength; ++colorIndex) {
                        final int b = bitStream.readByte();
                        final int g = bitStream.readByte();
                        final int r = bitStream.readByte();
                        palette.addColor(new Color(-1, r, g, b));
                        offset += 3;
                    }
                }
            }
            final byte[] buffer = this.readAndFlipVertically(bitStream, header.m_width, header.m_height, header.m_bitDepth);
            bitStream.close();
            return createImage(header, palette, buffer);
        }
        catch (IOException e) {
            TGAReader.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    private static Image createImage(final TGAHeader header, final Palette palette, final byte[] buffer) {
        final Layer layer = new Layer(header.m_width, header.m_height, header.m_startX, header.m_startY, header.m_bitDepth, palette, buffer);
        final Image result = new Image(FourCC.TGA, layer);
        layer.setPixelFormat((byte)18);
        layer.removeReference();
        return result;
    }
    
    @Override
    protected int getHeaderSize() {
        return 18;
    }
    
    @Override
    protected Point2i getSize(final ExtendedDataInputStream bitStream) throws IOException {
        final TGAHeader header = new TGAHeader();
        header.setFromStream(bitStream);
        return new Point2i(header.m_width, header.m_height);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TGAReader.class);
    }
}
