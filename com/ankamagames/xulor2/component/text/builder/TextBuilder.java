package com.ankamagames.xulor2.component.text.builder;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.text.document.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.text.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import java.awt.*;
import java.util.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.text.*;

public final class TextBuilder implements Iterable<LineBlock>
{
    private static final int CURSOR_WIDTH = 1;
    private static final int INFINITE_MAX_WIDTH = Integer.MAX_VALUE;
    private static final String THREE_DOTS_STRING = "...";
    private static final String PARAGRAPH_SEPARATOR = "\n";
    private TextWidget m_widget;
    private Color m_defaultColor;
    private boolean m_justify;
    private Alignment5 m_defaultHorizontalAlignment;
    private Alignment5 m_verticalAlignment;
    private Orientation m_orientation;
    private final TextDocument m_document;
    private final ArrayList<LineBlock> m_lineBlocks;
    private LineBlock m_selectionEndLine;
    private int m_maxWidth;
    private int m_minWidth;
    private boolean m_multiline;
    private boolean m_selectable;
    private boolean m_editable;
    private int m_scroll;
    private final Dimension m_minSize;
    private final Dimension m_unconstrainedMinSize;
    private final Dimension m_size;
    private boolean m_needToReflow;
    private boolean m_needToReflowSelection;
    private boolean m_needToComputeMinSize;
    private boolean m_autoHorizontalScrolled;
    private int m_textOffset;
    private boolean m_enableShrinking;
    private boolean m_isTextReduced;
    private long m_charDelay;
    
    public TextBuilder(final TextDocument document) {
        super();
        this.m_widget = null;
        this.m_defaultColor = Color.BLACK;
        this.m_justify = false;
        this.m_defaultHorizontalAlignment = Alignment5.WEST;
        this.m_verticalAlignment = Alignment5.NORTH;
        this.m_orientation = Orientation.EAST;
        this.m_lineBlocks = new ArrayList<LineBlock>();
        this.m_selectionEndLine = null;
        this.m_maxWidth = Integer.MAX_VALUE;
        this.m_minWidth = -1;
        this.m_multiline = false;
        this.m_selectable = false;
        this.m_editable = false;
        this.m_scroll = 0;
        this.m_minSize = new Dimension(0, 0);
        this.m_unconstrainedMinSize = new Dimension(0, 0);
        this.m_size = new Dimension(0, 0);
        this.m_needToReflow = true;
        this.m_needToReflowSelection = true;
        this.m_needToComputeMinSize = true;
        this.m_autoHorizontalScrolled = false;
        this.m_textOffset = 0;
        this.m_enableShrinking = true;
        this.m_charDelay = 0L;
        this.m_document = document;
    }
    
    public void clearLineTextBlocks() {
        for (int i = this.m_lineBlocks.size() - 1; i >= 0; --i) {
            this.m_lineBlocks.get(i).release();
        }
        this.m_lineBlocks.clear();
    }
    
    public void setTextWidget(final TextWidget w) {
        this.m_widget = w;
    }
    
    public TextWidget getTextWidget() {
        return this.m_widget;
    }
    
    public TextRenderer getDefaultTextRenderer() {
        return (this.m_document == null) ? null : this.m_document.getDefaultTextRenderer();
    }
    
    public void setDefaultTextRenderer(final TextRenderer defaultTextRenderer) {
        if (this.m_document.getDefaultTextRenderer() != defaultTextRenderer) {
            this.m_document.setDefaultTextRenderer(defaultTextRenderer);
            this.m_needToReflow = true;
            this.m_needToComputeMinSize = true;
            this.m_widget.setNeedsToPreProcess();
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public boolean isUseHighContrast() {
        return this.m_document.isUseHighContrast();
    }
    
    public void setUseHighContrast(final boolean useHighContrast) {
        this.m_document.setUseHighContrast(useHighContrast);
    }
    
    public Color getDefaultColor() {
        return this.m_defaultColor;
    }
    
    public void setDefaultColor(final Color defaultColor) {
        this.m_defaultColor = defaultColor;
    }
    
    public void setDefaultColor(final com.ankamagames.framework.graphics.image.Color defaultColor) {
        if (defaultColor != null) {
            this.m_defaultColor = new Color(defaultColor.getRedInt(), defaultColor.getGreenInt(), defaultColor.getBlueInt(), defaultColor.getAlphaInt());
        }
        else {
            this.m_defaultColor = null;
        }
    }
    
    public Alignment5 getDefaultHorizontalAlignment() {
        return this.m_defaultHorizontalAlignment;
    }
    
    public void setDefaultHorizontalAlignment(final Alignment5 defaultHorizontalAlignment) {
        this.m_defaultHorizontalAlignment = defaultHorizontalAlignment;
        this.m_needToReflow = true;
        this.m_widget.setNeedsToMiddleProcess();
    }
    
    public Alignment5 getVerticalAlignment() {
        return this.m_verticalAlignment;
    }
    
    public void setVerticalAlignment(final Alignment5 verticalAlignment) {
        this.m_verticalAlignment = verticalAlignment;
    }
    
    public Orientation getOrientation() {
        return this.m_orientation;
    }
    
    public void setOrientation(final Orientation orientation) {
        if (orientation != null) {
            this.m_orientation = orientation;
            this.m_needToComputeMinSize = true;
            this.m_needToReflow = true;
            this.m_widget.setNeedsToPreProcess();
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public boolean isJustify() {
        return this.m_justify;
    }
    
    public void setJustify(final boolean justify) {
        this.m_justify = justify;
    }
    
    public Boolean isAutoHorizontalScrolled() {
        return this.m_autoHorizontalScrolled;
    }
    
    public void setAutoHorizontalScrolled(final boolean autoHorizontalScrolled) {
        this.m_autoHorizontalScrolled = autoHorizontalScrolled;
    }
    
    public TextDocument getDocument() {
        return this.m_document;
    }
    
    public String getRawText() {
        if (this.m_document != null) {
            return this.m_document.getRawText();
        }
        return null;
    }
    
    public boolean rawTextEquals(@NotNull final String text) {
        if (this.m_document == null) {
            return text == null;
        }
        return this.m_document.rawTextEquals(text);
    }
    
    public void setRawText(final String rawText) {
        if (this.m_document != null) {
            this.m_document.setRawText(rawText);
            this.m_needToComputeMinSize = true;
            this.m_needToReflow = true;
            this.m_widget.setNeedsToPreProcess();
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public int getFormattedLength() {
        if (this.m_document != null) {
            return this.m_document.getCharactersCount();
        }
        return 0;
    }
    
    public void appendRawText(final String rawText) {
        if (this.m_document != null) {
            this.m_document.appendRawText(rawText);
            this.m_needToComputeMinSize = true;
            this.m_needToReflow = true;
            this.m_widget.setNeedsToPreProcess();
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public void addEmptyTextPart() {
        if (this.m_document != null) {
            this.m_document.addEmptyTextPart();
            this.m_needToComputeMinSize = true;
            this.m_needToReflow = true;
            this.m_widget.setNeedsToPreProcess();
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public void setSelectionStart(final AbstractDocumentPart documentPart, final int subPartIndex) {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.setSelectionStart(documentPart, subPartIndex);
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public void setSelectionStartAtSelectionEnd() {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.setSelectionStartAtSelectionEnd();
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public void setSelectionEnd(final AbstractDocumentPart documentPart, final int subPartIndex) {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.setSelectionEnd(documentPart, subPartIndex);
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public void setSelectionEndAtSelectionStart() {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.setSelectionEndAtSelectionStart();
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public ArrayList<LineBlock> getLineBlocks() {
        return this.m_lineBlocks;
    }
    
    public int getMaxWidth() {
        return this.m_maxWidth;
    }
    
    public void setMaxWidth(final int maxWidth) {
        this.m_maxWidth = maxWidth;
        this.m_needToComputeMinSize = true;
        this.m_widget.setNeedsToPreProcess();
    }
    
    public int getMinWidth() {
        return this.m_minWidth;
    }
    
    public void setMinWidth(final int minWidth) {
        this.m_minWidth = minWidth;
        this.m_needToComputeMinSize = true;
        this.m_widget.setNeedsToPreProcess();
    }
    
    public boolean isMultiline() {
        return this.m_multiline;
    }
    
    public void setMultiline(final boolean multiline) {
        this.m_multiline = multiline;
        this.m_needToComputeMinSize = true;
        this.m_widget.setNeedsToPreProcess();
    }
    
    public boolean isSelectable() {
        return this.m_selectable || this.m_editable;
    }
    
    public void setSelectable(final boolean selectable) {
        this.m_selectable = selectable;
        this.m_needToComputeMinSize = true;
        this.m_needToReflowSelection = true;
        this.m_widget.setNeedsToPreProcess();
        this.m_widget.setNeedsToMiddleProcess();
    }
    
    public boolean isEditable() {
        return this.m_editable;
    }
    
    public void setEditable(final boolean editable) {
        this.m_editable = editable;
        this.m_needToComputeMinSize = true;
        this.m_needToReflowSelection = true;
        this.m_widget.setNeedsToPreProcess();
        this.m_widget.setNeedsToMiddleProcess();
    }
    
    public boolean isTextReduced() {
        return this.m_isTextReduced;
    }
    
    public boolean isEnableShrinking() {
        return this.m_enableShrinking;
    }
    
    public void setEnableShrinking(final boolean enableShrinking) {
        this.m_enableShrinking = enableShrinking;
    }
    
    public int getScroll() {
        if (this.isVScrollable()) {
            return this.m_scroll;
        }
        return 0;
    }
    
    public void setScroll(final int scroll) {
        this.m_scroll = Math.min(Math.max(0, scroll), this.getMaxScroll());
    }
    
    public int getMaxScroll() {
        return this.m_lineBlocks.size() - 1;
    }
    
    public boolean isVScrollable() {
        return this.isSelectable() && this.m_multiline;
    }
    
    public boolean isHScrollable() {
        return this.isSelectable() && !this.m_multiline;
    }
    
    public boolean isScrollable() {
        return this.m_enableShrinking && (this.isVScrollable() || this.isHScrollable());
    }
    
    public Dimension getSize() {
        return (Dimension)this.m_size.clone();
    }
    
    public void setSize(int width, int height) {
        width = Math.max(0, width);
        height = Math.max(0, height);
        if (width != this.m_size.getWidth() || height != this.m_size.getHeight()) {
            this.m_size.setSize(width, height);
            this.m_needToReflow = true;
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public int getOrientedWidth() {
        return this.m_orientation.isHorizontal() ? this.m_size.width : this.m_size.height;
    }
    
    public int getOrientedHeight() {
        return this.m_orientation.isHorizontal() ? this.m_size.height : this.m_size.width;
    }
    
    public Dimension getMinSize() {
        return (Dimension)this.m_minSize.clone();
    }
    
    public Dimension getUnconstrainedMinSize() {
        return this.m_unconstrainedMinSize.cloneDimension();
    }
    
    public boolean isNeedToComputeMinSize() {
        return this.m_needToComputeMinSize;
    }
    
    public boolean isNeedToReflow() {
        return this.m_needToReflow;
    }
    
    public boolean isNeedToReflowSelection() {
        return this.m_needToReflowSelection;
    }
    
    public ObjectPair<AbstractContentBlock, BlockIntersectionType> getBlockFromCoordinates(final int x, int y) {
        final ObjectPair<AbstractContentBlock, BlockIntersectionType> pair = new ObjectPair<AbstractContentBlock, BlockIntersectionType>(null, BlockIntersectionType.INSIDE);
        y = this.getTextYOffset() - this.getOrientedHeight() - y;
        for (int i = 0, size = this.m_lineBlocks.size(); i < size; ++i) {
            final LineBlock lineBlock = this.m_lineBlocks.get(i);
            if (y >= lineBlock.getBounds().getMinY() && y <= lineBlock.getBounds().getMaxY()) {
                final int lineBlockX = lineBlock.getX();
                if (lineBlock.getBounds().contains(x, y)) {
                    final ArrayList<AbstractContentBlock> contentBlocks = lineBlock.getContentBlocks();
                    for (int j = 0; j < contentBlocks.size(); ++j) {
                        final AbstractContentBlock block = contentBlocks.get(j);
                        final int blockX = block.getX() + lineBlockX;
                        if (blockX <= x && blockX + block.getWidth() >= x) {
                            pair.setFirst(block);
                            return pair;
                        }
                    }
                }
                if (x < lineBlockX) {
                    pair.setFirst(lineBlock.getFirstBlock());
                    pair.setSecond(BlockIntersectionType.OUTSIDE_LEFT);
                }
                else {
                    pair.setFirst(lineBlock.getLastBlock());
                    pair.setSecond(BlockIntersectionType.OUTSIDE_RIGHT);
                }
                return pair;
            }
        }
        if (!this.m_lineBlocks.isEmpty()) {
            pair.setFirst(this.m_lineBlocks.get(this.m_lineBlocks.size() - 1).getLastBlock());
            pair.setSecond(BlockIntersectionType.OUTSIDE_BOTTOM);
        }
        return pair;
    }
    
    public int getTextYOffset() {
        int yOffset = 0;
        if (!this.m_lineBlocks.isEmpty()) {
            if (this.isVScrollable()) {
                final LineBlock firstVisibleLine = this.m_lineBlocks.get(this.getScroll());
                if (firstVisibleLine != null) {
                    yOffset = firstVisibleLine.getY() + firstVisibleLine.getHeight();
                }
            }
            else {
                final LineBlock lastVisibleLine = this.m_lineBlocks.get(this.m_lineBlocks.size() - 1);
                if (this.m_verticalAlignment.isSouth()) {
                    yOffset = this.getOrientedHeight() + lastVisibleLine.getY();
                }
                else if (!this.m_verticalAlignment.isNorth()) {
                    yOffset = (this.getOrientedHeight() + lastVisibleLine.getY()) / 2;
                }
            }
        }
        return yOffset;
    }
    
    public void clearSelectionIndices() {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.clearSelectionIndices();
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public void replaceSelectionBy(final String text) {
        if (this.m_document != null) {
            this.m_document.replaceSelectionContentBy(text);
            this.m_needToReflow = true;
            this.m_needToComputeMinSize = true;
            this.m_widget.setNeedsToPreProcess();
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public void removeLeftSelectionSubPart() {
        if (this.m_document != null) {
            this.m_document.removeLeftSelectionSubPart();
            this.m_needToReflow = true;
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public void removeRightSelectionSubPart() {
        if (this.m_document != null) {
            this.m_document.removeRightSelectionSubPart();
            this.m_needToReflow = true;
            this.m_widget.setNeedsToMiddleProcess();
        }
    }
    
    public void moveSelectionEndToRight() {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.moveSelectionEndToRight();
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public void moveSelectionEndToLeft() {
        if (this.m_document != null) {
            this.m_needToReflowSelection |= this.m_document.moveSelectionEndToLeft();
            if (this.m_needToReflowSelection) {
                this.m_widget.setNeedsToMiddleProcess();
            }
        }
    }
    
    public void moveSelectionEndToStartOfLine() {
        if (this.m_selectionEndLine != null && !this.m_selectionEndLine.isEmpty()) {
            final AbstractContentBlock firstBlock = this.m_selectionEndLine.getFirstBlock();
            if (firstBlock != null) {
                this.setSelectionEnd(firstBlock.getDocumentPart(), firstBlock.getStartIndex());
            }
        }
    }
    
    public void moveSelectionEndToEndOfLine() {
        if (this.m_selectionEndLine == null) {
            return;
        }
        if (this.m_selectionEndLine.isEmpty()) {
            return;
        }
        final AbstractContentBlock lastBlock = this.m_selectionEndLine.getLastBlock();
        if (lastBlock != null) {
            this.setSelectionEnd(lastBlock.getDocumentPart(), lastBlock.getEndIndex());
        }
    }
    
    public void computeMinSize() {
        double width = 0.0;
        double height = 0.0;
        if (!this.m_document.isEmpty() && !this.isScrollable() && !this.isMultiline()) {
            short borderSize = 0;
            for (final AbstractDocumentPart documentPart : this.m_document) {
                switch (documentPart.getType()) {
                    case IMAGE: {
                        final ImageDocumentPart imageDocumentPart = (ImageDocumentPart)documentPart;
                        width += imageDocumentPart.getWidth();
                        height = Math.max(height, imageDocumentPart.getHeight());
                        borderSize = 0;
                        continue;
                    }
                    case TEXT: {
                        final TextDocumentPart textDocumentPart = (TextDocumentPart)documentPart;
                        TextRenderer textRenderer = textDocumentPart.getTextRenderer();
                        if (textRenderer == null) {
                            textRenderer = this.getDefaultTextRenderer();
                        }
                        if (textRenderer != null) {
                            final String text = textDocumentPart.getDisplayedText();
                            if (text != null && text.length() != 0) {
                                final int lineWidth = textRenderer.getLineWidth(text);
                                final int lineHeight = textRenderer.getLineHeight(text);
                                width += lineWidth;
                                height = Math.max(height, lineHeight);
                            }
                            else {
                                width += textRenderer.getMaxCharacterWidth();
                                height = Math.max(height, textRenderer.getMaxCharacterHeight());
                            }
                            borderSize = textRenderer.getFont().getBorderSize();
                            continue;
                        }
                        continue;
                    }
                }
            }
            width += borderSize;
        }
        else if (!this.m_document.isEmpty() && !this.isScrollable() && this.isMultiline() && this.m_minWidth > 0) {
            final Point offset = new Point(0, 0);
            boolean newLine = false;
            LineBlock currentLineTextBlock = null;
            Alignment5 currentHorizontalAlignment = this.m_defaultHorizontalAlignment;
            final int fullWidth = this.isHScrollable() ? Integer.MAX_VALUE : ((this.m_maxWidth != Integer.MAX_VALUE) ? Math.max(this.m_minWidth, this.m_maxWidth) : this.m_minWidth);
            for (final AbstractDocumentPart documentPart2 : this.m_document) {
                Alignment5 nextAlignment = documentPart2.getAlignment();
                if (nextAlignment == null) {
                    nextAlignment = this.m_defaultHorizontalAlignment;
                }
                if (nextAlignment != currentHorizontalAlignment) {
                    currentHorizontalAlignment = nextAlignment;
                    newLine = true;
                }
                int beginIndex = 0;
                switch (documentPart2.getType()) {
                    case IMAGE: {
                        final ImageDocumentPart imageDocumentPart2 = (ImageDocumentPart)documentPart2;
                        if (offset.x != 0) {
                            final int freeWidth = fullWidth - offset.x;
                            if (freeWidth < imageDocumentPart2.getWidth()) {
                                newLine = true;
                            }
                        }
                        if (newLine || currentLineTextBlock == null) {
                            if (currentLineTextBlock != null) {
                                offset.x = 0;
                                final Point point = offset;
                                point.y -= currentLineTextBlock.getHeight();
                                height += currentLineTextBlock.getHeight();
                                width = Math.max(offset.x, width);
                                currentLineTextBlock.release();
                            }
                            currentLineTextBlock = LineBlock.checkOut();
                            currentLineTextBlock.setAlignment(currentHorizontalAlignment);
                            newLine = false;
                        }
                        currentLineTextBlock.setHeight(Math.max(imageDocumentPart2.getHeight(), currentLineTextBlock.getHeight()));
                        currentLineTextBlock.addImageBlock(imageDocumentPart2, offset.x);
                        final Point point2 = offset;
                        point2.x += imageDocumentPart2.getWidth();
                        continue;
                    }
                    case TEXT: {
                        final TextDocumentPart textDocumentPart2 = (TextDocumentPart)documentPart2;
                        TextRenderer textRenderer2 = textDocumentPart2.getTextRenderer();
                        if (textRenderer2 == null) {
                            textRenderer2 = this.getDefaultTextRenderer();
                        }
                        final String displayedText = textDocumentPart2.getDisplayedText();
                        if (textRenderer2 != null && displayedText != null) {
                            int separatorPosition = 0;
                            while (separatorPosition != -1) {
                                final int previsouSeparatorPosition = separatorPosition;
                                separatorPosition = displayedText.indexOf("\n", separatorPosition + 1);
                                final String paragraph = (separatorPosition == -1) ? displayedText.substring(previsouSeparatorPosition) : displayedText.substring(previsouSeparatorPosition, separatorPosition);
                                if (offset.x != 0 && paragraph.length() != 0) {
                                    final int freeWidth2 = fullWidth - offset.x;
                                    final int maxChars = textRenderer2.getMaxVisibleTextLength(paragraph, freeWidth2, false);
                                    if (maxChars == 0) {
                                        newLine = true;
                                    }
                                }
                                newLine = (newLine || beginIndex != 0 || currentLineTextBlock == null || paragraph.startsWith("\n"));
                                if (paragraph.length() == 0) {
                                    if (newLine) {
                                        if (currentLineTextBlock != null) {
                                            offset.x = 0;
                                            final Point point3 = offset;
                                            point3.y -= currentLineTextBlock.getHeight();
                                            width = Math.max(offset.x, width);
                                            height += currentLineTextBlock.getHeight();
                                        }
                                        if (currentLineTextBlock != null) {
                                            currentLineTextBlock.release();
                                        }
                                        currentLineTextBlock = LineBlock.checkOut();
                                        currentLineTextBlock.setAlignment(currentHorizontalAlignment);
                                        newLine = false;
                                    }
                                    currentLineTextBlock.setHeight(Math.max(textRenderer2.getMaxCharacterHeight(), currentLineTextBlock.getHeight()));
                                    currentLineTextBlock.addTextBlock("", textDocumentPart2, beginIndex, beginIndex, offset.x, 0);
                                }
                                else {
                                    int textOffset = 0;
                                    while (textOffset < paragraph.length()) {
                                        if (newLine) {
                                            if (currentLineTextBlock != null) {
                                                width = Math.max(offset.x, width);
                                                final Point point4 = offset;
                                                point4.y -= currentLineTextBlock.getHeight();
                                                height += currentLineTextBlock.getHeight();
                                            }
                                            offset.x = textRenderer2.getFont().getBorderSize();
                                            if (currentLineTextBlock != null) {
                                                currentLineTextBlock.release();
                                            }
                                            currentLineTextBlock = LineBlock.checkOut();
                                            currentLineTextBlock.setAlignment(currentHorizontalAlignment);
                                            newLine = false;
                                        }
                                        int rectWidth = fullWidth - offset.x;
                                        if (rectWidth < 0) {
                                            rectWidth = Integer.MAX_VALUE;
                                        }
                                        final int length = textRenderer2.getMaxVisibleTextLength(paragraph.substring(textOffset), rectWidth);
                                        final int lastVisibleCharacterIndex = textOffset + Math.max(1, length);
                                        final String visibleText = paragraph.substring(textOffset, lastVisibleCharacterIndex);
                                        final int visibleTextWidth = textRenderer2.getLineWidth(visibleText);
                                        final int visibleTextHeight = textRenderer2.getLineHeight(visibleText);
                                        currentLineTextBlock.setHeight(Math.max(visibleTextHeight, currentLineTextBlock.getHeight()));
                                        currentLineTextBlock.setBaseLine(Math.max(currentLineTextBlock.getBaseLine(), textRenderer2.getDescent(visibleText)));
                                        currentLineTextBlock.addTextBlock(visibleText, textDocumentPart2, beginIndex, beginIndex + visibleText.length(), offset.x, visibleTextWidth);
                                        beginIndex += visibleText.length();
                                        textOffset = lastVisibleCharacterIndex;
                                        if (textOffset != paragraph.length()) {
                                            newLine = true;
                                            width = Math.max(width, offset.x + visibleTextWidth);
                                        }
                                        else {
                                            final Point point5 = offset;
                                            point5.x += visibleTextWidth;
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                        continue;
                    }
                }
            }
            if (!newLine && currentLineTextBlock != null) {
                height += currentLineTextBlock.getHeight();
                width = Math.max(offset.x, width);
            }
            if (currentLineTextBlock != null) {
                currentLineTextBlock.release();
            }
        }
        else if (!this.m_document.isEmpty() && !this.isScrollable() && this.isMultiline() && this.m_minWidth <= 0) {
            final Point offset = new Point(0, 0);
            boolean newLine = false;
            LineBlock currentLineTextBlock = null;
            Alignment5 currentHorizontalAlignment = this.m_defaultHorizontalAlignment;
            short borderSize2 = 0;
            for (final AbstractDocumentPart documentPart2 : this.m_document) {
                Alignment5 nextAlignment = documentPart2.getAlignment();
                if (nextAlignment == null) {
                    nextAlignment = this.m_defaultHorizontalAlignment;
                }
                if (nextAlignment != currentHorizontalAlignment) {
                    currentHorizontalAlignment = nextAlignment;
                    newLine = true;
                }
                int beginIndex = 0;
                switch (documentPart2.getType()) {
                    case IMAGE: {
                        final ImageDocumentPart imageDocumentPart2 = (ImageDocumentPart)documentPart2;
                        if (newLine || currentLineTextBlock == null) {
                            if (currentLineTextBlock != null) {
                                offset.x = 0;
                                final Point point6 = offset;
                                point6.y -= currentLineTextBlock.getHeight();
                                height += currentLineTextBlock.getHeight();
                            }
                            if (currentLineTextBlock != null) {
                                currentLineTextBlock.release();
                            }
                            currentLineTextBlock = LineBlock.checkOut();
                            currentLineTextBlock.setAlignment(currentHorizontalAlignment);
                            newLine = false;
                        }
                        currentLineTextBlock.setHeight(Math.max(imageDocumentPart2.getHeight(), currentLineTextBlock.getHeight()));
                        currentLineTextBlock.addImageBlock(imageDocumentPart2, offset.x);
                        final Point point7 = offset;
                        point7.x += imageDocumentPart2.getWidth();
                        borderSize2 = 0;
                        continue;
                    }
                    case TEXT: {
                        final TextDocumentPart textDocumentPart2 = (TextDocumentPart)documentPart2;
                        TextRenderer textRenderer2 = textDocumentPart2.getTextRenderer();
                        if (textRenderer2 == null) {
                            textRenderer2 = this.getDefaultTextRenderer();
                        }
                        borderSize2 = textRenderer2.getFont().getBorderSize();
                        final String displayedText = textDocumentPart2.getDisplayedText();
                        if (textRenderer2 != null && displayedText != null) {
                            int separatorPosition = 0;
                            while (separatorPosition != -1) {
                                final int previsouSeparatorPosition = separatorPosition;
                                separatorPosition = displayedText.indexOf("\n", separatorPosition + 1);
                                final String paragraph = (separatorPosition == -1) ? displayedText.substring(previsouSeparatorPosition) : displayedText.substring(previsouSeparatorPosition, separatorPosition);
                                newLine = (newLine || beginIndex != 0 || currentLineTextBlock == null || paragraph.startsWith("\n"));
                                if (paragraph.length() == 0) {
                                    if (newLine) {
                                        if (currentLineTextBlock != null) {
                                            width = Math.max(offset.x + borderSize2, width);
                                            offset.x = 0;
                                            final Point point8 = offset;
                                            point8.y -= currentLineTextBlock.getHeight();
                                            height += currentLineTextBlock.getHeight();
                                        }
                                        if (currentLineTextBlock != null) {
                                            currentLineTextBlock.release();
                                        }
                                        currentLineTextBlock = LineBlock.checkOut();
                                        currentLineTextBlock.setAlignment(currentHorizontalAlignment);
                                        newLine = false;
                                    }
                                    currentLineTextBlock.setHeight(Math.max(textRenderer2.getMaxCharacterHeight(), currentLineTextBlock.getHeight()));
                                    currentLineTextBlock.addTextBlock("", textDocumentPart2, beginIndex, beginIndex, offset.x, 0);
                                }
                                else {
                                    if (newLine) {
                                        if (currentLineTextBlock != null) {
                                            width = Math.max(offset.x + borderSize2, width);
                                            offset.x = 0;
                                            final Point point9 = offset;
                                            point9.y -= currentLineTextBlock.getHeight();
                                            height += currentLineTextBlock.getHeight();
                                        }
                                        if (currentLineTextBlock != null) {
                                            currentLineTextBlock.release();
                                        }
                                        currentLineTextBlock = LineBlock.checkOut();
                                        currentLineTextBlock.setAlignment(currentHorizontalAlignment);
                                    }
                                    currentLineTextBlock.setHeight(Math.max(textRenderer2.getLineHeight(paragraph), currentLineTextBlock.getHeight()));
                                    final Point point10 = offset;
                                    point10.x += textRenderer2.getLineWidth(paragraph);
                                    currentLineTextBlock.setBaseLine(Math.max(currentLineTextBlock.getBaseLine(), textRenderer2.getDescent(paragraph)));
                                    currentLineTextBlock.addTextBlock(paragraph, textDocumentPart2, beginIndex, beginIndex + paragraph.length(), offset.x, textRenderer2.getLineWidth(paragraph));
                                    beginIndex += paragraph.length();
                                    newLine = (separatorPosition != -1);
                                }
                            }
                            continue;
                        }
                        continue;
                    }
                }
            }
            if (currentLineTextBlock != null) {
                width = Math.max(width, offset.x + borderSize2);
                height += currentLineTextBlock.getHeight();
                offset.x = 0;
            }
            if (currentLineTextBlock != null) {
                currentLineTextBlock.release();
            }
        }
        else {
            final TextRenderer textRenderer3 = this.getDefaultTextRenderer();
            if (textRenderer3 != null) {
                width = textRenderer3.getMaxCharacterWidth();
                height = textRenderer3.getMaxCharacterHeight();
            }
        }
        if (this.m_orientation.isHorizontal()) {
            this.m_unconstrainedMinSize.setSize(width, height);
            this.m_minSize.setSize(Math.max(Math.min(this.m_maxWidth, width), this.m_minWidth), height);
        }
        else {
            this.m_unconstrainedMinSize.setSize(height, width);
            this.m_minSize.setSize(height, Math.max(Math.min(this.m_maxWidth, width), this.m_minWidth));
        }
        this.m_needToComputeMinSize = false;
    }
    
    public void reflow() {
        this.clearLineTextBlocks();
        this.m_isTextReduced = false;
        final Point offset = new Point(0, 0);
        boolean newLine = false;
        boolean wrap = false;
        LineBlock currentLineTextBlock = null;
        Alignment5 currentHorizontalAlignment = this.m_defaultHorizontalAlignment;
        final int fullWidth = this.isHScrollable() ? Integer.MAX_VALUE : this.getOrientedWidth();
        for (final AbstractDocumentPart documentPart : this.m_document) {
            Alignment5 nextAlignment = documentPart.getAlignment();
            if (nextAlignment == null) {
                nextAlignment = this.m_defaultHorizontalAlignment;
            }
            if (nextAlignment != currentHorizontalAlignment) {
                currentHorizontalAlignment = nextAlignment;
                newLine = true;
                wrap = false;
            }
            int beginIndex = 0;
            switch (documentPart.getType()) {
                case IMAGE: {
                    final ImageDocumentPart imageDocumentPart = (ImageDocumentPart)documentPart;
                    if (offset.x != 0) {
                        final int freeWidth = this.getOrientedWidth() - offset.x;
                        if (freeWidth < imageDocumentPart.getWidth()) {
                            newLine = true;
                            wrap = true;
                        }
                    }
                    if (newLine || currentLineTextBlock == null) {
                        final LineBlock lineBlock = this.newLineProcess(currentLineTextBlock, wrap, offset, 0, currentHorizontalAlignment);
                        if (lineBlock == null) {
                            return;
                        }
                        currentLineTextBlock = lineBlock;
                        newLine = false;
                    }
                    currentLineTextBlock.setHeight(Math.max(imageDocumentPart.getHeight(), currentLineTextBlock.getHeight()));
                    currentLineTextBlock.addImageBlock(imageDocumentPart, offset.x);
                    final Point point = offset;
                    point.x += imageDocumentPart.getWidth();
                    continue;
                }
                case TEXT: {
                    final TextDocumentPart textDocumentPart = (TextDocumentPart)documentPart;
                    TextRenderer textRenderer = textDocumentPart.getTextRenderer();
                    if (textRenderer == null) {
                        textRenderer = this.getDefaultTextRenderer();
                    }
                    final String displayedText = textDocumentPart.getDisplayedText();
                    if (textRenderer != null && displayedText != null) {
                        int separatorPosition = 0;
                        while (separatorPosition != -1) {
                            final int previsouSeparatorPosition = separatorPosition;
                            separatorPosition = displayedText.indexOf("\n", separatorPosition + 1);
                            final String paragraph = (separatorPosition == -1) ? displayedText.substring(previsouSeparatorPosition) : displayedText.substring(previsouSeparatorPosition, separatorPosition);
                            if (offset.x != 0 && paragraph.length() != 0) {
                                final int freeWidth2 = fullWidth - offset.x;
                                final int maxChars = textRenderer.getMaxVisibleTextLength(paragraph, freeWidth2, false);
                                if (maxChars == 0) {
                                    newLine = true;
                                }
                            }
                            newLine = (newLine || beginIndex != 0 || currentLineTextBlock == null || paragraph.startsWith("\n"));
                            wrap = (newLine && !paragraph.startsWith("\n"));
                            if (paragraph.length() == 0) {
                                if (newLine) {
                                    final LineBlock lineBlock2 = this.newLineProcess(currentLineTextBlock, wrap, offset, textRenderer.getFont().getBorderSize(), currentHorizontalAlignment);
                                    if (lineBlock2 == null) {
                                        return;
                                    }
                                    final Point point2 = offset;
                                    point2.y -= lineBlock2.getHeight();
                                    currentLineTextBlock = lineBlock2;
                                    newLine = false;
                                }
                                currentLineTextBlock.setHeight(Math.max(textRenderer.getMaxCharacterHeight(), currentLineTextBlock.getHeight()));
                                currentLineTextBlock.addTextBlock("", textDocumentPart, beginIndex, beginIndex, offset.x, 0);
                            }
                            else {
                                int textOffset = 0;
                                while (textOffset < paragraph.length()) {
                                    if (newLine) {
                                        final LineBlock lineBlock3 = this.newLineProcess(currentLineTextBlock, wrap, offset, textRenderer.getFont().getBorderSize(), currentHorizontalAlignment);
                                        if (lineBlock3 == null) {
                                            return;
                                        }
                                        currentLineTextBlock = lineBlock3;
                                        newLine = false;
                                    }
                                    int rectWidth = fullWidth - offset.x;
                                    if (rectWidth < 0) {
                                        rectWidth = Integer.MAX_VALUE;
                                    }
                                    final int length = textRenderer.getMaxVisibleTextLength(paragraph.substring(textOffset), rectWidth);
                                    final int lastVisibleCharacterIndex = textOffset + Math.max(1, length);
                                    final String visibleText = paragraph.substring(textOffset, lastVisibleCharacterIndex);
                                    final int visibleTextWidth = textRenderer.getLineWidth(visibleText) - textRenderer.getFont().getBorderSize();
                                    final int visibleTextHeight = textRenderer.getLineHeight(visibleText);
                                    currentLineTextBlock.setHeight(Math.max(visibleTextHeight, currentLineTextBlock.getHeight()));
                                    currentLineTextBlock.setBaseLine(Math.max(currentLineTextBlock.getBaseLine(), textRenderer.getDescent(visibleText)));
                                    currentLineTextBlock.addTextBlock(visibleText, textDocumentPart, beginIndex, beginIndex + visibleText.length(), offset.x, visibleTextWidth);
                                    beginIndex += visibleText.length();
                                    textOffset = lastVisibleCharacterIndex;
                                    if (textOffset != paragraph.length()) {
                                        newLine = true;
                                        wrap = true;
                                    }
                                    else {
                                        final Point point3 = offset;
                                        point3.x += visibleTextWidth;
                                    }
                                }
                            }
                        }
                        continue;
                    }
                    continue;
                }
            }
        }
        if (!newLine && currentLineTextBlock != null) {
            currentLineTextBlock.setY(offset.y - currentLineTextBlock.getHeight());
            currentLineTextBlock.setX(currentLineTextBlock.getAlignment().getX(currentLineTextBlock.getWidth(), this.getOrientedWidth()));
            this.m_lineBlocks.add(currentLineTextBlock);
            currentLineTextBlock = null;
        }
        if (currentLineTextBlock != null) {
            currentLineTextBlock.release();
        }
        this.reflowSelection();
        this.m_needToReflow = false;
    }
    
    public void reflowSelection() {
        if (this.isSelectable()) {
            this.m_selectionEndLine = null;
            final AbstractDocumentPart startPart = this.m_document.getSelectionStartPart();
            final int startPartIndex = this.m_document.getSelectionStartPartIndex();
            final int startSubPartIndex = this.m_document.getSelectionStartSubPartIndex();
            final AbstractDocumentPart endPart = this.m_document.getSelectionEndPart();
            final int endPartIndex = this.m_document.getSelectionEndPartIndex();
            final int endSubPartIndex = this.m_document.getSelectionEndSubPartIndex();
            for (int lineIdx = 0; lineIdx < this.m_lineBlocks.size(); ++lineIdx) {
                final LineBlock line = this.m_lineBlocks.get(lineIdx);
                line.clearSelectionBlock();
            }
            int startLineIdx = -1;
            int startBlockIdx = -1;
            int startCharIdx = -1;
            int startX = -1;
            for (int lineIdx2 = 0; lineIdx2 < this.m_lineBlocks.size() && startLineIdx < 0; ++lineIdx2) {
                final LineBlock line2 = this.m_lineBlocks.get(lineIdx2);
                line2.clearSelectionBlock();
                final boolean newLine = lineIdx2 == 0;
                final ArrayList<AbstractContentBlock> lineBlocks = line2.getContentBlocks();
                for (int lineBlockIdx = 0; lineBlockIdx < lineBlocks.size() && startX < 0; ++lineBlockIdx) {
                    final AbstractContentBlock lineBlock = lineBlocks.get(lineBlockIdx);
                    final AbstractDocumentPart lineBlockPart = lineBlock.getDocumentPart();
                    if (lineBlockPart == startPart) {
                        final boolean beforeBounds = startSubPartIndex < lineBlock.getStartIndex() + (newLine ? 0 : 1);
                        final boolean afterBounds = startSubPartIndex > lineBlock.getEndIndex();
                        if (!beforeBounds) {
                            if (!afterBounds) {
                                final int startIdx = startSubPartIndex - lineBlock.getStartIndex() + (newLine ? 0 : -1);
                                startX = line2.getContentPartLeftFromIndex(this.getDefaultTextRenderer(), lineBlock, startIdx);
                                startLineIdx = lineIdx2;
                                startBlockIdx = lineBlockIdx;
                                startCharIdx = startIdx;
                            }
                        }
                    }
                }
            }
            int endLineIdx = -1;
            int endBlockIdx = -1;
            int endCharIdx = -1;
            int endX = -1;
            for (int lineIdx3 = 0; lineIdx3 < this.m_lineBlocks.size() && endLineIdx < 0; ++lineIdx3) {
                final LineBlock line3 = this.m_lineBlocks.get(lineIdx3);
                line3.clearSelectionBlock();
                final boolean newLine2 = lineIdx3 == 0;
                final ArrayList<AbstractContentBlock> lineBlocks2 = line3.getContentBlocks();
                for (int lineBlockIdx2 = 0; lineBlockIdx2 < lineBlocks2.size() && endX < 0; ++lineBlockIdx2) {
                    final AbstractContentBlock lineBlock2 = lineBlocks2.get(lineBlockIdx2);
                    final AbstractDocumentPart lineBlockPart2 = lineBlock2.getDocumentPart();
                    if (lineBlockPart2 == endPart) {
                        final boolean beforeBounds2 = endSubPartIndex < lineBlock2.getStartIndex() + (newLine2 ? 0 : 1);
                        final boolean afterBounds2 = endSubPartIndex > lineBlock2.getEndIndex();
                        if (!beforeBounds2) {
                            if (!afterBounds2) {
                                final int endIdx = endSubPartIndex - lineBlock2.getStartIndex() + (newLine2 ? 0 : -1);
                                endX = line3.getContentPartLeftFromIndex(this.getDefaultTextRenderer(), lineBlock2, endIdx);
                                endLineIdx = lineIdx3;
                                endBlockIdx = lineBlockIdx2;
                                endCharIdx = endIdx;
                                this.m_selectionEndLine = line3;
                            }
                        }
                    }
                }
            }
            if (startLineIdx >= 0 && endLineIdx >= 0) {
                for (int lineIdx3 = startLineIdx; lineIdx3 <= endLineIdx; ++lineIdx3) {
                    final LineBlock line3 = this.m_lineBlocks.get(lineIdx3);
                    int selectionStart;
                    int selectionWidth;
                    boolean isCursor;
                    if (lineIdx3 == startLineIdx && startLineIdx < endLineIdx) {
                        selectionStart = startX;
                        selectionWidth = line3.getContentPartLeftFromIndex(this.getDefaultTextRenderer(), line3.getLastBlock(), line3.getLength()) - startX;
                        isCursor = false;
                    }
                    else if (startLineIdx < lineIdx3 && lineIdx3 < endLineIdx) {
                        selectionStart = 0;
                        selectionWidth = line3.getContentPartLeftFromIndex(this.getDefaultTextRenderer(), line3.getLastBlock(), line3.getLength());
                        isCursor = false;
                    }
                    else if (lineIdx3 == endLineIdx && startLineIdx < endLineIdx) {
                        selectionStart = 0;
                        selectionWidth = line3.getContentPartLeftFromIndex(this.getDefaultTextRenderer(), line3.getBlock(endBlockIdx), endCharIdx);
                        isCursor = false;
                    }
                    else {
                        if (startLineIdx != endLineIdx) {
                            continue;
                        }
                        selectionStart = startX;
                        selectionWidth = Math.max(endX - startX, 1);
                        isCursor = (selectionWidth <= 1);
                    }
                    final int textBoxWidth = this.m_widget.getAppearance().getContentWidth();
                    final int lineWidth = line3.getContentPartLeftFromIndex(this.getDefaultTextRenderer(), line3.getLastBlock(), line3.getLength());
                    if (lineWidth < Math.abs(this.m_textOffset) + textBoxWidth) {
                        this.m_textOffset = textBoxWidth - lineWidth;
                    }
                    else if (startX < Math.abs(this.m_textOffset)) {
                        this.m_textOffset = -startX;
                    }
                    else if (startX > textBoxWidth + Math.abs(this.m_textOffset)) {
                        this.m_textOffset = textBoxWidth - startX;
                    }
                    this.m_textOffset = Math.min(this.m_textOffset, 0);
                    for (int i = 0, count = line3.getTextBlockCount(); i < count; ++i) {
                        final AbstractContentBlock block = line3.getBlock(i);
                        block.setX(this.m_textOffset);
                    }
                    selectionStart += this.m_textOffset;
                    selectionStart = Math.max(selectionStart - 1, 1);
                    line3.setSelectionBlock(selectionStart, selectionWidth, isCursor);
                }
            }
        }
        this.m_needToReflowSelection = false;
    }
    
    private LineBlock newLineProcess(LineBlock currentLineTextBlock, final boolean wrap, final Point offset, final int xOffset, final Alignment5 currentHorizontalAlignment) {
        offset.x = xOffset;
        if (currentLineTextBlock != null) {
            currentLineTextBlock.setY(offset.y -= currentLineTextBlock.getHeight());
            if (this.m_justify && this.m_multiline && wrap) {
                final AbstractContentBlock lastBlock = currentLineTextBlock.getLastBlock();
                if (lastBlock != null && lastBlock.getType() == AbstractContentBlock.BlockType.TEXT) {
                    final TextContentBlock txtBlock = (TextContentBlock)lastBlock;
                    final String t = txtBlock.getText();
                    if (t != null && t.endsWith(" ")) {
                        txtBlock.setText(t.substring(0, t.length() - 1));
                        final int w = currentLineTextBlock.getWidth();
                        final int bW = txtBlock.getWidth();
                        TextRenderer textRenderer = txtBlock.getTextRenderer();
                        if (textRenderer == null) {
                            textRenderer = this.getDefaultTextRenderer();
                        }
                        final String s = txtBlock.getText();
                        final int newBW = textRenderer.getLineWidth(s);
                        txtBlock.setWidth(textRenderer.getLineWidth(s));
                        currentLineTextBlock.recomputeLineWidth();
                        final int newW = currentLineTextBlock.getWidth();
                        final int a = 0;
                    }
                }
                int lineSpacesCount = 0;
                for (final AbstractContentBlock bloc : currentLineTextBlock.getContentBlocks()) {
                    if (bloc.getType() != AbstractContentBlock.BlockType.TEXT) {
                        continue;
                    }
                    final TextContentBlock textBlock = (TextContentBlock)bloc;
                    final char[] chars = textBlock.getTextAsCharArray();
                    if (chars == null) {
                        continue;
                    }
                    final int blocSpacesCount = StringUtils.count(' ', chars);
                    textBlock.setSpacesCount(blocSpacesCount);
                    lineSpacesCount += blocSpacesCount;
                }
                currentLineTextBlock.setSpacesCount(lineSpacesCount);
            }
            currentLineTextBlock.setX(currentLineTextBlock.getAlignment().getX(currentLineTextBlock.getWidth(), this.getOrientedWidth()));
            final boolean lineIsFullyVisible = -offset.y <= this.getOrientedHeight();
            if (lineIsFullyVisible || this.isVScrollable()) {
                this.m_lineBlocks.add(currentLineTextBlock);
            }
            else {
                currentLineTextBlock.release();
            }
            if (!this.m_multiline || (!this.isVScrollable() && !lineIsFullyVisible)) {
                this.addThreeDotsToLastLineTextBlock();
                this.m_needToReflow = false;
                return null;
            }
        }
        currentLineTextBlock = LineBlock.checkOut();
        currentLineTextBlock.setAlignment(currentHorizontalAlignment);
        return currentLineTextBlock;
    }
    
    private void addThreeDotsToLastLineTextBlock() {
        if (this.m_lineBlocks.isEmpty()) {
            return;
        }
        this.m_isTextReduced = true;
        final LineBlock lineTextBlock = this.m_lineBlocks.get(this.m_lineBlocks.size() - 1);
        if (lineTextBlock != null) {
            int textBlockIndex = lineTextBlock.getTextBlockCount();
            while (textBlockIndex > 0) {
                final AbstractContentBlock block = lineTextBlock.getBlock(--textBlockIndex);
                if (block.getType() == AbstractContentBlock.BlockType.TEXT) {
                    final TextContentBlock textBlock = (TextContentBlock)block;
                    TextRenderer textRenderer = textBlock.getTextRenderer();
                    if (textRenderer == null) {
                        textRenderer = this.getDefaultTextRenderer();
                    }
                    if (textRenderer != null) {
                        final int threeDotsWidth = textRenderer.getLineWidth("...");
                        final int freeSpaceForBlock = this.getOrientedWidth() - textBlock.getX();
                        if (freeSpaceForBlock >= threeDotsWidth) {
                            final char[] chars = textBlock.getDocumentPart().getText().toCharArray();
                            int width = 0;
                            int lastVisibleCharacterIndex;
                            for (lastVisibleCharacterIndex = 0; lastVisibleCharacterIndex < chars.length; ++lastVisibleCharacterIndex) {
                                final int characterWidth = textRenderer.getCharacterWidth(chars[lastVisibleCharacterIndex]);
                                if (width + characterWidth > freeSpaceForBlock - threeDotsWidth) {
                                    break;
                                }
                                width += characterWidth;
                            }
                            final int nextOffset = lastVisibleCharacterIndex;
                            final String visibleText = textBlock.getDocumentPart().getText().substring(0, nextOffset);
                            final int newTextBlockWidth = textRenderer.getLineWidth(visibleText);
                            final int errorMargin = 5;
                            if (freeSpaceForBlock - newTextBlockWidth + 5 >= 0) {
                                textBlock.setText(visibleText);
                                textBlock.setWidth(newTextBlockWidth);
                                final TextContentBlock threeDotsTextBlock = lineTextBlock.addTextBlock("...", null, 0, 0, textBlock.getX() + newTextBlockWidth, threeDotsWidth);
                                threeDotsTextBlock.setTextRenderer(textRenderer);
                                threeDotsTextBlock.setColor(textBlock.getColor());
                                threeDotsTextBlock.setAlignment(textBlock.getAlignment());
                                threeDotsTextBlock.setUnderline(textBlock.isUnderline());
                                threeDotsTextBlock.setCrossed(textBlock.isCrossed());
                                lineTextBlock.recomputeLineWidth();
                                lineTextBlock.setX(lineTextBlock.getAlignment().getX(lineTextBlock.getWidth(), this.getOrientedWidth()));
                                return;
                            }
                        }
                    }
                }
                lineTextBlock.removeContentBlockAt(textBlockIndex);
            }
        }
    }
    
    @Override
    public Iterator<LineBlock> iterator() {
        return this.m_lineBlocks.iterator();
    }
    
    public void clean() {
        this.clearLineTextBlocks();
        this.m_document.clean();
        this.m_justify = false;
        this.m_defaultColor = Color.BLACK;
        this.m_defaultHorizontalAlignment = Alignment5.WEST;
        this.m_verticalAlignment = Alignment5.NORTH;
        this.m_orientation = Orientation.EAST;
        this.m_widget = null;
    }
    
    public TextWidget getWidget() {
        return this.m_widget;
    }
    
    public void setCharsDelay(final long delay) {
        this.m_charDelay = delay;
    }
    
    public long getCharsDelay() {
        return this.m_charDelay;
    }
    
    public enum BlockIntersectionType
    {
        OUTSIDE_TOP, 
        OUTSIDE_LEFT, 
        INSIDE, 
        OUTSIDE_RIGHT, 
        OUTSIDE_BOTTOM;
    }
}
