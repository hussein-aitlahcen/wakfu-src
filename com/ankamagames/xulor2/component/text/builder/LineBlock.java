package com.ankamagames.xulor2.component.text.builder;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.text.builder.selection.*;
import java.awt.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class LineBlock implements Poolable
{
    private static final Logger m_logger;
    private static final ObjectPool m_pool;
    private ObjectPool m_currentPool;
    private final ArrayList<AbstractContentBlock> m_contentBlocks;
    private SelectionBlock m_selectionBlock;
    private final Rectangle m_bounds;
    private int m_baseLine;
    private Alignment5 m_alignment;
    private int m_spacesCount;
    
    private LineBlock() {
        super();
        this.m_contentBlocks = new ArrayList<AbstractContentBlock>();
        this.m_selectionBlock = null;
        this.m_bounds = new Rectangle();
        this.m_baseLine = 0;
    }
    
    public static LineBlock checkOut() {
        LineBlock lineBlock;
        try {
            lineBlock = (LineBlock)LineBlock.m_pool.borrowObject();
            lineBlock.m_currentPool = LineBlock.m_pool;
        }
        catch (Exception e) {
            LineBlock.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            lineBlock = new LineBlock();
            lineBlock.onCheckOut();
        }
        return lineBlock;
    }
    
    public void release() {
        try {
            if (this.m_currentPool != null) {
                this.m_currentPool.returnObject(this);
            }
            else {
                this.onCheckIn();
            }
        }
        catch (Exception e) {
            LineBlock.m_logger.warn((Object)"Probl\u00e8me lors du retour dans un pool", (Throwable)e);
            this.onCheckIn();
        }
    }
    
    public int getContentPartLeftFromIndex(final TextRenderer textRenderer, final AbstractContentBlock block, final int index) {
        int x = 0;
        for (int i = 0; i < this.m_contentBlocks.size(); ++i) {
            final AbstractContentBlock currentBlock = this.m_contentBlocks.get(i);
            if (currentBlock == block) {
                return x + currentBlock.getContentBlockPartLeftFromIndex(textRenderer, index);
            }
            x += currentBlock.getContentBlockPartLeftFromIndex(textRenderer, currentBlock.getLength());
        }
        return x;
    }
    
    public TextContentBlock addTextBlock(final String text, final int x, final int width) {
        return this.addTextBlock(text, null, 0, 0, x, width);
    }
    
    public TextContentBlock addTextBlock(final String text, final TextDocumentPart documentPart, final int beginIndex, final int endIndex, final int x, final int width) {
        final TextContentBlock textBlock = new TextContentBlock();
        textBlock.setLineBlock(this);
        textBlock.setText(text);
        textBlock.setDocumentPart(documentPart);
        textBlock.setStartIndex(beginIndex);
        textBlock.setEndIndex(endIndex);
        textBlock.setX(x);
        textBlock.setWidth(width);
        this.addContentBlock(textBlock);
        return textBlock;
    }
    
    public ImageContentBlock addImageBlock(final ImageDocumentPart documentPart, final int x) {
        final ImageContentBlock imageBlock = new ImageContentBlock();
        imageBlock.setLineBlock(this);
        imageBlock.setDocumentPart(documentPart);
        imageBlock.setX(x);
        this.addContentBlock(imageBlock);
        return imageBlock;
    }
    
    private void addContentBlock(final AbstractContentBlock block) {
        final Rectangle bounds = this.m_bounds;
        bounds.width += block.getWidth();
        this.m_contentBlocks.add(block);
    }
    
    public void removeContentBlockAt(final int index) {
        final AbstractContentBlock block = this.m_contentBlocks.remove(index);
        final Rectangle bounds = this.m_bounds;
        bounds.width -= block.getWidth();
    }
    
    public void clearBlocks() {
        this.m_contentBlocks.clear();
        this.m_bounds.width = 0;
        this.clearSelectionBlock();
    }
    
    public void clearSelectionBlock() {
        this.m_selectionBlock = null;
    }
    
    public SelectionBlock getSelectionBlock() {
        return this.m_selectionBlock;
    }
    
    public void setSelectionBlock(final int x, final int width, final boolean isCursor) {
        (this.m_selectionBlock = new SelectionBlock()).setLineBlock(this);
        this.m_selectionBlock.setX(x);
        this.m_selectionBlock.setWidth(width);
        this.m_selectionBlock.setCursor(isCursor);
    }
    
    public Rectangle getBounds() {
        return this.m_bounds;
    }
    
    public int getX() {
        return this.m_bounds.x;
    }
    
    public void setX(final int x) {
        this.m_bounds.x = x;
    }
    
    public int getY() {
        return this.m_bounds.y;
    }
    
    public void setY(final int y) {
        this.m_bounds.y = y;
    }
    
    public int getHeight() {
        return this.m_bounds.height;
    }
    
    public void setHeight(final int height) {
        this.m_bounds.height = height;
    }
    
    public int getWidth() {
        return this.m_bounds.width;
    }
    
    public int getBaseLine() {
        return this.m_baseLine;
    }
    
    public void setBaseLine(final int baseLine) {
        this.m_baseLine = baseLine;
    }
    
    public Alignment5 getAlignment() {
        return this.m_alignment;
    }
    
    public void setAlignment(final Alignment5 alignment) {
        this.m_alignment = alignment;
    }
    
    public boolean isEmpty() {
        return this.m_contentBlocks.isEmpty();
    }
    
    public int getSpacesCount() {
        return this.m_spacesCount;
    }
    
    public void setSpacesCount(final int spacesCount) {
        this.m_spacesCount = spacesCount;
    }
    
    public int getTextBlockCount() {
        return this.m_contentBlocks.size();
    }
    
    public AbstractContentBlock getBlock(final int index) {
        return this.m_contentBlocks.get(index);
    }
    
    public AbstractContentBlock getFirstBlock() {
        if (!this.m_contentBlocks.isEmpty()) {
            return this.m_contentBlocks.get(0);
        }
        return null;
    }
    
    public AbstractContentBlock getLastBlock() {
        if (!this.m_contentBlocks.isEmpty()) {
            return this.m_contentBlocks.get(this.m_contentBlocks.size() - 1);
        }
        return null;
    }
    
    public int getLength() {
        int length = 0;
        for (int i = 0; i < this.m_contentBlocks.size(); ++i) {
            length += this.m_contentBlocks.get(i).getLength();
        }
        return length;
    }
    
    public void recomputeLineWidth() {
        this.m_bounds.width = 0;
        for (final AbstractContentBlock textBlock : this.m_contentBlocks) {
            final Rectangle bounds = this.m_bounds;
            bounds.width += textBlock.getWidth();
        }
    }
    
    public final ArrayList<AbstractContentBlock> getContentBlocks() {
        return this.m_contentBlocks;
    }
    
    @Override
    public void onCheckOut() {
        this.m_baseLine = 0;
    }
    
    @Override
    public void onCheckIn() {
        this.m_contentBlocks.clear();
        this.m_selectionBlock = null;
        this.m_bounds.setBounds(0, 0, 0, 0);
        this.m_alignment = null;
        this.m_spacesCount = 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0, size = this.m_contentBlocks.size(); i < size; ++i) {
            sb.append(this.m_contentBlocks.get(i).toString());
        }
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)LineBlock.class);
        m_pool = new MonitoredPool(new ObjectFactory<LineBlock>() {
            @Override
            public LineBlock makeObject() {
                return new LineBlock(null);
            }
        });
    }
}
