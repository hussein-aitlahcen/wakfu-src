package com.ankamagames.framework.graphics.image.io.reader;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.*;

public final class ImageReaderFactory
{
    private final LightWeightMap<String, ImageReader> m_extensionToReader;
    private static final ImageReaderFactory m_instance;
    
    public static ImageReaderFactory getInstance() {
        return ImageReaderFactory.m_instance;
    }
    
    private ImageReaderFactory() {
        super();
        this.m_extensionToReader = new LightWeightMap<String, ImageReader>();
    }
    
    public void registerReader(final String fileExt, final ImageReader imageReader) {
        this.m_extensionToReader.put(fileExt, imageReader);
    }
    
    public ImageReader getReader(final String fileExt) {
        return this.m_extensionToReader.get(fileExt);
    }
    
    public void reset() {
        this.m_extensionToReader.clear();
    }
    
    public boolean isEmpty() {
        return this.m_extensionToReader.size() == 0;
    }
    
    static {
        m_instance = new ImageReaderFactory();
    }
}
