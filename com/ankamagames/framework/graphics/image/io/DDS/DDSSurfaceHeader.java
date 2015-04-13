package com.ankamagames.framework.graphics.image.io.DDS;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class DDSSurfaceHeader
{
    public static final int HEADER_SIZE = 124;
    public int m_size;
    public int m_flags;
    public int m_height;
    public int m_width;
    public int m_pitchOrLinearSize;
    public int m_depth;
    public int m_numMipMap;
    public int[] m_reserved1;
    public PixelFormat m_pixelFormat;
    public SurfaceCaps m_surfaceCaps;
    public int m_reserved2;
    
    public DDSSurfaceHeader() {
        super();
        this.m_reserved1 = new int[11];
        this.m_pixelFormat = new PixelFormat();
        this.m_surfaceCaps = new SurfaceCaps();
    }
    
    public void SetFromStream(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_size = bitStream.readInt();
        this.m_flags = bitStream.readInt();
        this.m_height = bitStream.readInt();
        this.m_width = bitStream.readInt();
        this.m_pitchOrLinearSize = bitStream.readInt();
        this.m_depth = bitStream.readInt();
        this.m_numMipMap = bitStream.readInt();
        if (this.m_numMipMap == 0) {
            this.m_numMipMap = 1;
        }
        bitStream.setOffset(bitStream.getOffset() + 44);
        this.m_pixelFormat.SetFromStream(bitStream);
        this.m_surfaceCaps.SetFromStream(bitStream);
        this.m_reserved2 = bitStream.readInt();
    }
    
    public static class PixelFormat
    {
        public int m_size;
        public int m_flags;
        public int m_fourCC;
        public int m_RGBBitCount;
        public int m_redMask;
        public int m_greenMask;
        public int m_blueMask;
        public int m_alphaMask;
        
        public void SetFromStream(final ExtendedDataInputStream bitStream) throws IOException {
            this.m_size = bitStream.readInt();
            this.m_flags = bitStream.readInt();
            this.m_fourCC = bitStream.readInt();
            this.m_RGBBitCount = bitStream.readInt();
            this.m_redMask = bitStream.readInt();
            this.m_greenMask = bitStream.readInt();
            this.m_blueMask = bitStream.readInt();
            this.m_alphaMask = bitStream.readInt();
        }
    }
    
    public static class SurfaceCaps
    {
        public int m_caps1;
        public int m_caps2;
        public int m_reserved1;
        public int m_reserved2;
        
        public void SetFromStream(final ExtendedDataInputStream bitStream) throws IOException {
            this.m_caps1 = bitStream.readInt();
            this.m_caps2 = bitStream.readInt();
            this.m_reserved1 = bitStream.readInt();
            this.m_reserved2 = bitStream.readInt();
        }
    }
}
