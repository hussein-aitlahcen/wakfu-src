package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class BackgroundFeedbackBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_type;
    protected Page[] m_pages;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getType() {
        return this.m_type;
    }
    
    public Page[] getPages() {
        return this.m_pages;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_type = 0;
        this.m_pages = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_type = buffer.getShort();
        final int pageCount = buffer.getInt();
        this.m_pages = new Page[pageCount];
        for (int iPage = 0; iPage < pageCount; ++iPage) {
            (this.m_pages[iPage] = new Page()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.BACKGROUND_FEEDBACK.getId();
    }
    
    public static class Page
    {
        protected int m_id;
        protected short m_order;
        protected short m_template;
        protected int m_imageId;
        
        public int getId() {
            return this.m_id;
        }
        
        public short getOrder() {
            return this.m_order;
        }
        
        public short getTemplate() {
            return this.m_template;
        }
        
        public int getImageId() {
            return this.m_imageId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_order = buffer.getShort();
            this.m_template = buffer.getShort();
            this.m_imageId = buffer.getInt();
        }
    }
}
