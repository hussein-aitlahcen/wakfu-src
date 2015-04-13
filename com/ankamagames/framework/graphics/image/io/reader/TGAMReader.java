package com.ankamagames.framework.graphics.image.io.reader;

import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class TGAMReader extends ImageReader
{
    @Override
    protected final Image loadImageFromStream(final ExtendedDataInputStream bitStream) throws IOException {
        final boolean resizeMask = bitStream.readByte() == 109;
        bitStream.skip(3);
        final int imgWidth = bitStream.readShort();
        final int imgHeight = bitStream.readShort();
        final int tgaSize = bitStream.readInt();
        final int maskSize = bitStream.readInt();
        final byte maskResize = (byte)(resizeMask ? bitStream.readByte() : 1);
        final byte[] tgaData = new byte[tgaSize];
        bitStream.readBytes(tgaData);
        final Layer layer = new Layer(imgWidth, imgHeight, (short)32, null, tgaData);
        final byte[] maskData = new byte[maskSize];
        bitStream.readBytes(maskData);
        layer.setAlphaMask(maskData, maskResize);
        bitStream.close();
        final Image result = new Image(FourCC.TGAM, layer);
        layer.removeReference();
        return result;
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
}
