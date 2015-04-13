package com.ankamagames.framework.graphics.image.io.writer;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.io.TGA.*;
import java.io.*;
import com.ankamagames.framework.graphics.image.*;

public class TGAWriter
{
    protected static final Logger m_logger;
    
    public void ToStream(final OutputBitStream stream, final Image image) {
        final TGAHeader header = new TGAHeader();
        final TGAFooter footer = new TGAFooter();
        final Layer layer = image.getLayer(0);
        header.m_bitDepth = (byte)layer.getBitDepth();
        header.m_height = (short)layer.getHeight();
        header.m_width = (short)layer.getWidth();
        header.m_colorMapBitDepth = 0;
        header.m_colorMapLength = 0;
        header.m_colorMapStart = 0;
        header.m_colorMapType = 0;
        header.m_startX = 0;
        header.m_startY = 0;
        header.m_imageType = 2;
        header.m_desc = 0;
        header.m_idLength = 0;
        footer.m_devArea = 0;
        footer.m_extArea = 0;
        final String signature = "TRUEVISION-XFILE";
        final byte[] signatureArray = signature.getBytes();
        System.arraycopy(signatureArray, 0, footer.m_signature, 0, signatureArray.length);
        try {
            header.toStream(stream);
            stream.writeBytes(layer.getData());
            footer.toStream(stream);
        }
        catch (IOException e) {
            TGAWriter.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TGAWriter.class);
    }
}
