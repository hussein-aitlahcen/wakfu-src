package com.ankamagames.xulor2.component.text.document;

import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.text.*;
import org.jetbrains.annotations.*;

public abstract class TextDocument implements Iterable<AbstractDocumentPart>
{
    private static final Logger m_logger;
    private final ArrayList<AbstractDocumentPart> m_parts;
    protected int m_maxCharacters;
    private int m_charactersCount;
    protected Pattern m_restrictPattern;
    protected boolean m_unicodeRestrict;
    protected boolean m_password;
    private int m_selectionStartPartIndex;
    private int m_selectionStartSubPartIndex;
    private int m_selectionEndPartIndex;
    private int m_selectionEndSubPartIndex;
    private boolean m_enableAWTFont;
    private TextRenderer m_defaultTextRenderer;
    private boolean m_useHighContrast;
    
    public TextDocument() {
        super();
        this.m_parts = new ArrayList<AbstractDocumentPart>();
        this.m_maxCharacters = Integer.MAX_VALUE;
        this.m_charactersCount = 0;
        this.m_selectionStartPartIndex = -1;
        this.m_selectionStartSubPartIndex = 0;
        this.m_selectionEndPartIndex = -1;
        this.m_selectionEndSubPartIndex = 0;
        this.m_enableAWTFont = false;
    }
    
    public void addEmptyTextPart() {
        final TextDocumentPart textDocumentPart = new TextDocumentPart(this, null, false);
        textDocumentPart.setText("");
        this.addPart(textDocumentPart);
    }
    
    protected void addPart(final AbstractDocumentPart documentPart) {
        this.m_parts.add(documentPart);
        this.m_charactersCount += documentPart.getLength();
    }
    
    protected void addPartAt(final int index, final AbstractDocumentPart documentPart) {
        this.m_parts.add(index, documentPart);
    }
    
    public void removePart(final AbstractDocumentPart documentPart) {
        if (this.m_parts.remove(documentPart)) {
            documentPart.clean();
        }
        this.m_charactersCount -= documentPart.getLength();
    }
    
    @Nullable
    public AbstractDocumentPart getPartAt(final int index) {
        try {
            return this.m_parts.get(index);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    protected void removeAllParts() {
        for (final AbstractDocumentPart part : this.m_parts) {
            part.clean();
        }
        this.m_parts.clear();
        this.m_charactersCount = 0;
    }
    
    public int getCharactersCount() {
        return this.m_charactersCount;
    }
    
    public int getMaxCharacters() {
        return this.m_maxCharacters;
    }
    
    public void setMaxCharacters(final int maxCharacters) {
        this.m_maxCharacters = maxCharacters;
    }
    
    public Pattern getRestrictPattern() {
        return this.m_restrictPattern;
    }
    
    @Nullable
    public String getRestrict() {
        if (this.m_restrictPattern != null) {
            return this.m_restrictPattern.pattern();
        }
        return null;
    }
    
    public void setRestrict(final String restrict) {
        if (restrict == null) {
            this.m_restrictPattern = null;
            return;
        }
        if (this.m_unicodeRestrict) {
            this.m_restrictPattern = Pattern.compile(restrict, 64);
        }
        else {
            this.m_restrictPattern = Pattern.compile(restrict);
        }
    }
    
    public void setUnicodeRestrict(final boolean unicodeRestrict) {
        if (unicodeRestrict == this.m_unicodeRestrict) {
            return;
        }
        this.m_unicodeRestrict = unicodeRestrict;
        if (this.m_restrictPattern != null) {
            this.setRestrict(this.m_restrictPattern.pattern());
        }
    }
    
    public boolean isPassword() {
        return this.m_password;
    }
    
    public void setPassword(final boolean password) {
        this.m_password = password;
    }
    
    public boolean isEnableAWTFont() {
        return this.m_enableAWTFont;
    }
    
    public void setEnableAWTFont(final boolean enableAWTFont) {
        this.m_enableAWTFont = enableAWTFont;
    }
    
    public boolean isUnicodeRestrict() {
        return this.m_unicodeRestrict;
    }
    
    public boolean isEmpty() {
        return this.m_parts.isEmpty();
    }
    
    @Nullable
    public AbstractDocumentPart getFirstPart() {
        if (!this.m_parts.isEmpty()) {
            return this.m_parts.get(0);
        }
        return null;
    }
    
    @Nullable
    public AbstractDocumentPart getLastPart() {
        if (!this.m_parts.isEmpty()) {
            return this.m_parts.get(this.m_parts.size() - 1);
        }
        return null;
    }
    
    @Nullable
    public AbstractDocumentPart getSelectionStartPart() {
        final int selectionStartPartIndex = this.getSelectionStartPartIndex();
        if (selectionStartPartIndex != -1) {
            return this.getPartAt(selectionStartPartIndex);
        }
        return null;
    }
    
    public int getSelectionStartPartIndex() {
        if (this.checkSelectionInversion()) {
            return this.m_selectionEndPartIndex;
        }
        return this.m_selectionStartPartIndex;
    }
    
    public int getSelectionStartSubPartIndex() {
        if (this.checkSelectionInversion()) {
            return this.m_selectionEndSubPartIndex;
        }
        return this.m_selectionStartSubPartIndex;
    }
    
    public boolean setSelectionStart(final AbstractDocumentPart documentPart, int subPartIndex) {
        final int part = this.m_parts.indexOf(documentPart);
        final int adjustedSubPartIndex = MathHelper.clamp(subPartIndex, -1, this.m_charactersCount);
        if (adjustedSubPartIndex != subPartIndex) {
            TextDocument.m_logger.warn((Object)"setSelectionStart avec une valeur d'index invalide : ", (Throwable)new Exception());
            subPartIndex = adjustedSubPartIndex;
        }
        boolean changed = false;
        if (part != this.m_selectionStartPartIndex || this.m_selectionStartSubPartIndex != subPartIndex) {
            changed = true;
        }
        this.m_selectionStartPartIndex = part;
        this.m_selectionStartSubPartIndex = subPartIndex;
        return changed;
    }
    
    public boolean setSelectionStartAtSelectionEnd() {
        final boolean changed = this.m_selectionEndPartIndex != this.m_selectionStartPartIndex || this.m_selectionStartSubPartIndex != this.m_selectionEndSubPartIndex;
        this.m_selectionStartPartIndex = this.m_selectionEndPartIndex;
        this.m_selectionStartSubPartIndex = this.m_selectionEndSubPartIndex;
        return changed;
    }
    
    @Nullable
    public AbstractDocumentPart getSelectionEndPart() {
        final int selectionEndPartIndex = this.getSelectionEndPartIndex();
        if (selectionEndPartIndex != -1) {
            return this.getPartAt(selectionEndPartIndex);
        }
        return null;
    }
    
    public int getSelectionEndPartIndex() {
        if (this.checkSelectionInversion()) {
            return this.m_selectionStartPartIndex;
        }
        return this.m_selectionEndPartIndex;
    }
    
    public int getSelectionEndSubPartIndex() {
        if (this.checkSelectionInversion()) {
            return this.m_selectionStartSubPartIndex;
        }
        return this.m_selectionEndSubPartIndex;
    }
    
    public boolean setSelectionEndSubPartIndex(final int selectionEndSubPartIndex) {
        if (this.m_selectionEndSubPartIndex != selectionEndSubPartIndex) {
            this.m_selectionEndSubPartIndex = selectionEndSubPartIndex;
            return true;
        }
        return false;
    }
    
    public boolean setSelectionStartSubPartIndex(final int selectionStartSubPartIndex) {
        if (this.m_selectionStartSubPartIndex != selectionStartSubPartIndex) {
            this.m_selectionStartSubPartIndex = selectionStartSubPartIndex;
            return true;
        }
        return false;
    }
    
    public boolean setSelectionEnd(final AbstractDocumentPart documentPart, int subPartIndex) {
        final int partIndex = this.m_parts.indexOf(documentPart);
        final int adjustedSubPartIndex = MathHelper.clamp(subPartIndex, -1, this.m_charactersCount);
        if (adjustedSubPartIndex != subPartIndex) {
            TextDocument.m_logger.warn((Object)"setSelectionEnd avec une valeur d'index invalide : ", (Throwable)new Exception());
            subPartIndex = adjustedSubPartIndex;
        }
        final boolean changed = partIndex != this.m_selectionEndPartIndex || this.m_selectionEndSubPartIndex != subPartIndex;
        this.m_selectionEndPartIndex = partIndex;
        this.m_selectionEndSubPartIndex = subPartIndex;
        return changed;
    }
    
    public boolean setSelectionEndAtSelectionStart() {
        final boolean changed = this.m_selectionEndPartIndex != this.m_selectionStartPartIndex || this.m_selectionEndSubPartIndex != this.m_selectionStartSubPartIndex;
        this.m_selectionEndPartIndex = this.m_selectionStartPartIndex;
        this.m_selectionEndSubPartIndex = this.m_selectionStartSubPartIndex;
        return changed;
    }
    
    public boolean hasSelection() {
        return this.m_selectionStartPartIndex != -1 && this.m_selectionEndPartIndex != -1;
    }
    
    public boolean isSelectionEmpty() {
        return this.m_selectionStartPartIndex == this.m_selectionEndPartIndex && this.m_selectionStartSubPartIndex == this.m_selectionEndSubPartIndex;
    }
    
    @Nullable
    public String getSelectedText() {
        if (!this.hasSelection() || this.isSelectionEmpty()) {
            return null;
        }
        final int selectionStartPartIndex = this.getSelectionStartPartIndex();
        final int selectionEndPartIndex = this.getSelectionEndPartIndex();
        final int selectionStartSubPartIndex = this.getSelectionStartSubPartIndex();
        final int selectionEndSubPartIndex = this.getSelectionEndSubPartIndex();
        if (selectionStartPartIndex != selectionEndPartIndex) {
            final StringBuilder builder = new StringBuilder();
            for (int index = selectionStartPartIndex; index <= selectionEndPartIndex; ++index) {
                final AbstractDocumentPart documentPart = this.getPartAt(index);
                if (documentPart.getType() == DocumentPartType.TEXT) {
                    final String displayedText = ((TextDocumentPart)documentPart).getDisplayedText();
                    if (index == selectionStartPartIndex) {
                        builder.append(displayedText.substring(selectionStartSubPartIndex));
                    }
                    else if (index == selectionEndPartIndex) {
                        builder.append(displayedText.substring(0, selectionEndSubPartIndex));
                    }
                    else {
                        builder.append(displayedText);
                    }
                }
            }
            return builder.toString();
        }
        final AbstractDocumentPart documentPart2 = this.getSelectionStartPart();
        if (documentPart2 != null && documentPart2.getType() == DocumentPartType.TEXT) {
            final String displayedText2 = ((TextDocumentPart)documentPart2).getDisplayedText();
            return displayedText2.substring(selectionStartSubPartIndex, selectionEndSubPartIndex);
        }
        return null;
    }
    
    public void setDefaultTextRenderer(final TextRenderer renderer) {
        this.m_defaultTextRenderer = renderer;
    }
    
    public TextRenderer getDefaultTextRenderer() {
        return this.m_defaultTextRenderer;
    }
    
    public boolean isUseHighContrast() {
        return this.m_useHighContrast;
    }
    
    public void setUseHighContrast(final boolean useHighContrast) {
        this.m_useHighContrast = useHighContrast;
    }
    
    public abstract String getRawText();
    
    public boolean rawTextEquals(@NotNull final String text) {
        return text.equals(this.getRawText());
    }
    
    public abstract void setRawText(final String p0);
    
    public abstract void appendRawText(final String p0);
    
    @Override
    public Iterator<AbstractDocumentPart> iterator() {
        return this.m_parts.iterator();
    }
    
    public boolean clearSelectionIndices() {
        final boolean changed = this.m_selectionEndPartIndex != -1 || this.m_selectionStartPartIndex != -1 || this.m_selectionEndSubPartIndex != 0 || this.m_selectionStartSubPartIndex != 0;
        this.m_selectionStartPartIndex = -1;
        this.m_selectionStartSubPartIndex = 0;
        this.m_selectionEndPartIndex = -1;
        this.m_selectionEndSubPartIndex = 0;
        return changed;
    }
    
    private boolean checkSelectionInversion() {
        return this.hasSelection() && (this.m_selectionEndPartIndex < this.m_selectionStartPartIndex || (this.m_selectionStartPartIndex == this.m_selectionEndPartIndex && this.m_selectionEndSubPartIndex < this.m_selectionStartSubPartIndex));
    }
    
    public void replaceSelectionContentBy(final String text) {
        if (!this.hasSelection()) {
            return;
        }
        final AbstractDocumentPart startDocumentPart = this.getSelectionStartPart();
        final int selectionStartPartIndex = this.getSelectionStartPartIndex();
        final int selectionEndPartIndex = this.getSelectionEndPartIndex();
        final int selectionStartSubPartIndex = this.getSelectionStartSubPartIndex();
        final int selectionEndSubPartIndex = this.getSelectionEndSubPartIndex();
        int selectionLength = 0;
        boolean startPartRemoved = false;
        if (selectionStartPartIndex == selectionEndPartIndex) {
            final AbstractDocumentPart documentPart = this.getSelectionStartPart();
            if (documentPart != null) {
                selectionLength = selectionEndSubPartIndex - selectionStartSubPartIndex;
                if (documentPart.removeSubPartFromToIndices(selectionStartSubPartIndex, selectionEndSubPartIndex)) {
                    this.removePart(documentPart);
                    startPartRemoved = true;
                }
            }
        }
        else {
            for (int index = selectionEndPartIndex; index >= selectionStartPartIndex; --index) {
                final AbstractDocumentPart documentPart2 = this.getPartAt(index);
                if (index == selectionStartPartIndex) {
                    selectionLength += documentPart2.getLength() - 1 - selectionStartSubPartIndex;
                    if (documentPart2.removeSubPartFromIndex(selectionStartSubPartIndex)) {
                        this.removePart(documentPart2);
                        startPartRemoved = true;
                    }
                }
                else if (index == selectionEndPartIndex) {
                    selectionLength += selectionEndSubPartIndex + 1;
                    if (documentPart2.removeSubPartToIndex(selectionEndSubPartIndex)) {
                        this.removePart(documentPart2);
                    }
                }
                else {
                    selectionLength += documentPart2.getLength();
                    this.removePart(documentPart2);
                }
            }
        }
        this.m_charactersCount -= selectionLength;
        TextDocumentPart textDocumentPart;
        if (startDocumentPart.getType() == DocumentPartType.TEXT) {
            textDocumentPart = (TextDocumentPart)startDocumentPart;
        }
        else {
            textDocumentPart = new TextDocumentPart(this, null, false);
            startPartRemoved = true;
        }
        int insertTextLength = textDocumentPart.setTextAt(text, selectionStartSubPartIndex);
        if (startPartRemoved) {
            this.addPartAt(selectionStartPartIndex, textDocumentPart);
        }
        this.m_charactersCount += insertTextLength;
        if (this.m_charactersCount > this.m_maxCharacters) {
            final int removedCharactersCount = this.m_charactersCount - this.m_maxCharacters;
            textDocumentPart.removeSubPartFromToIndices(selectionStartSubPartIndex, selectionStartSubPartIndex + removedCharactersCount);
            insertTextLength -= removedCharactersCount;
            this.m_charactersCount -= removedCharactersCount;
        }
        this.setSelectionStart(textDocumentPart, selectionStartSubPartIndex);
        this.setSelectionEndAtSelectionStart();
        this.moveSelectionEndToRight(insertTextLength);
        this.setSelectionStartAtSelectionEnd();
    }
    
    public void removeLeftSelectionSubPart() {
        if (this.isSelectionEmpty()) {
            this.moveSelectionEndToLeft();
        }
        this.replaceSelectionContentBy("");
    }
    
    public void removeRightSelectionSubPart() {
        if (this.isSelectionEmpty()) {
            this.moveSelectionEndToRight();
        }
        this.replaceSelectionContentBy("");
    }
    
    public boolean moveSelectionEndToRight(final int subPartCount) {
        boolean changed = false;
        for (int i = 0; i < subPartCount; ++i) {
            changed |= this.moveSelectionEndToRight();
        }
        return changed;
    }
    
    public boolean moveSelectionEndToRight() {
        final AbstractDocumentPart currentPart = this.getPartAt(this.m_selectionEndPartIndex);
        if (currentPart == null) {
            return false;
        }
        if (this.m_selectionEndSubPartIndex + 1 <= currentPart.getLength()) {
            ++this.m_selectionEndSubPartIndex;
            return true;
        }
        if (currentPart == this.getLastPart()) {
            return false;
        }
        ++this.m_selectionEndPartIndex;
        this.m_selectionEndSubPartIndex = 1;
        return true;
    }
    
    public boolean moveSelectionEndToLeft() {
        final AbstractDocumentPart currentPart = this.getPartAt(this.m_selectionEndPartIndex);
        if (currentPart == null) {
            return false;
        }
        if (this.m_selectionEndSubPartIndex - 1 >= 0) {
            --this.m_selectionEndSubPartIndex;
            return true;
        }
        if (currentPart == this.getFirstPart()) {
            return false;
        }
        final AbstractDocumentPart newPart = this.getPartAt(this.m_selectionEndPartIndex - 1);
        if (newPart == null) {
            return false;
        }
        --this.m_selectionEndPartIndex;
        this.m_selectionEndSubPartIndex = newPart.getLength() - 1;
        return true;
    }
    
    public int getPartSize() {
        return this.m_parts.size();
    }
    
    public int getTextRenderedWidth(final int from, final int to) {
        final String substring = this.getRawText().substring(from, to);
        int width = 0;
        for (final char c : substring.toCharArray()) {
            width += this.getDefaultTextRenderer().getCharacterWidth(c);
        }
        return width;
    }
    
    public void clean() {
        for (final AbstractDocumentPart part : this.m_parts) {
            part.clean();
        }
        this.m_parts.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)TextDocument.class);
    }
}
