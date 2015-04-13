package com.ankamagames.xulor2.component;

import java.awt.im.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.text.builder.*;
import com.ankamagames.xulor2.component.text.document.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.event.*;
import java.awt.datatransfer.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import java.awt.event.*;
import java.text.*;
import java.awt.font.*;
import java.awt.*;
import org.jetbrains.annotations.*;
import java.io.*;

public class TextEditor extends AbstractSelectableTextWidget implements InputMethodListener, InputMethodRequests
{
    public static final String TAG = "TextEditor";
    protected String m_rawAppendText;
    protected String m_ghostText;
    protected boolean m_ghostTextVisible;
    public static final int AUTO_HORIZONTAL_SCROLLED_HASH;
    public static final int MAX_CHARS_HASH;
    public static final int MAX_CHARACTERS_HASH;
    public static final int PASSWORD_HASH;
    public static final int RESTRICT_HASH;
    public static final int EDITABLE_HASH;
    public static final int UNICODE_RESTRICT_HASH;
    public static final int GHOST_TEXT_HASH;
    
    public void appendText(String text) {
        if (text == null) {
            text = "";
        }
        if (this.m_rawText != null) {
            this.m_rawText += text;
        }
        else {
            if (this.m_rawAppendText == null) {
                this.m_rawAppendText = "";
            }
            this.m_rawAppendText += text;
        }
        this.setNeedsToPreProcess();
    }
    
    @Override
    public String getTag() {
        return "TextEditor";
    }
    
    @Override
    public void setText(final Object text) {
        super.setText(text);
        if (text == null || (this.m_ghostText != null && !text.equals(this.m_ghostText))) {
            this.m_ghostTextVisible = false;
        }
        this.m_rawAppendText = null;
    }
    
    @Override
    public void setSelectable(final boolean selectable) {
        super.setSelectable(true);
    }
    
    public int getMaxCharacters() {
        return this.getTextBuilder().getDocument().getMaxCharacters();
    }
    
    public void setMaxCharacters(final int maxCharacters) {
        this.getTextBuilder().getDocument().setMaxCharacters(maxCharacters);
    }
    
    @Deprecated
    public void setMaxChars(final int max) {
        this.setMaxCharacters(max);
    }
    
    @Deprecated
    public int getMaxChars() {
        return this.getMaxCharacters();
    }
    
    public String getRestrict() {
        return this.getTextBuilder().getDocument().getRestrict();
    }
    
    public void setRestrict(final String restrict) {
        this.getTextBuilder().getDocument().setRestrict(restrict);
    }
    
    public boolean getUnicodeRestrict() {
        return this.getTextBuilder().getDocument().isUnicodeRestrict();
    }
    
    public void setUnicodeRestrict(final boolean unicodeRestrict) {
        this.getTextBuilder().getDocument().setUnicodeRestrict(unicodeRestrict);
    }
    
    public boolean getPassword() {
        return this.getTextBuilder().getDocument().isPassword();
    }
    
    public void setPassword(final boolean password) {
        this.getTextBuilder().getDocument().setPassword(password);
    }
    
    public boolean getAutoHorizontalScrolled() {
        return this.getTextBuilder().isAutoHorizontalScrolled();
    }
    
    public void setAutoHorizontalScrolled(final Boolean autoHorizontalScrolled) {
        this.getTextBuilder().setAutoHorizontalScrolled(autoHorizontalScrolled);
    }
    
    public void setEditable(final boolean editable) {
        this.getTextBuilder().setEditable(editable);
    }
    
    public boolean getEditable() {
        return this.getTextBuilder().isEditable();
    }
    
    @Override
    public String getText() {
        if (this.m_ghostTextVisible) {
            return "";
        }
        return super.getText();
    }
    
    public String getGhostText() {
        return this.m_ghostText;
    }
    
    public void resetGhostText() {
        if (this.m_ghostText != null && this.m_ghostText.length() > 0) {
            this.setText(this.m_ghostText);
            this.m_ghostTextVisible = true;
        }
    }
    
    public void setGhostText(final String ghostText) {
        this.m_ghostText = ghostText;
        this.resetGhostText();
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        this.applyAppendText();
        return super.preProcess(deltaTime);
    }
    
    protected void applyAppendText() {
        if (this.m_rawAppendText != null) {
            this.getTextBuilder().appendRawText(this.m_rawAppendText);
            this.m_rawAppendText = null;
        }
    }
    
    @Override
    protected void applySetText() {
        super.applySetText();
        if (FocusManager.getInstance().getFocused() == this) {
            this.ensureSelectionIsDisplayed();
        }
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.setFocusable(true);
    }
    
    @Override
    public void onCheckIn() {
        final Widget focused = FocusManager.getInstance().getFocused();
        if (focused == this) {
            this.disableIME();
        }
        super.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TextWidgetAppearance app = TextWidgetAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.setTextBuilder(new TextBuilder(new MultiplePartTextDocument()));
        this.getTextBuilder().setTextWidget(this);
        this.getTextBuilder().setEditable(true);
        this.setSelectable(true);
        this.setAutoHorizontalScrolled(true);
        final TextRenderStates renderStates = new TextRenderStates(this);
        this.m_textEntity.setPreRenderStates(renderStates);
        this.m_textEntity.setPostRenderStates(renderStates);
    }
    
    private void ensureSelectionIsDisplayed() {
        final TextDocument document = this.getTextBuilder().getDocument();
        if (document.isEmpty()) {
            this.getTextBuilder().addEmptyTextPart();
        }
        if (!document.hasSelection()) {
            if (this.isSelectOnFocus()) {
                this.selectAll();
            }
            else {
                final AbstractDocumentPart lastDocumentPart = document.getLastPart();
                this.getTextBuilder().setSelectionEnd(lastDocumentPart, lastDocumentPart.getLength());
                this.getTextBuilder().setSelectionStartAtSelectionEnd();
            }
        }
    }
    
    @Override
    protected void processFocusChangedEvent(final FocusChangedEvent focusChangedEvent) {
        super.processFocusChangedEvent(focusChangedEvent);
        if (focusChangedEvent.getFocused() && this.getTextBuilder().isEditable()) {
            this.ensureSelectionIsDisplayed();
            this.checkGhostTextNeeded();
            this.activateIME();
        }
        else {
            this.disableIME();
        }
    }
    
    private void disableIME() {
        try {
            Xulor.getInstance().getAppUI().getGLComponent().getInputContext().setCompositionEnabled(false);
        }
        catch (Exception ex) {}
        Xulor.getInstance().getAppUI().getGLComponent().removeInputMethodListener(this);
        if (Xulor.getInstance().getAppUI().getGLComponent().getCurrentInputMethodRequests() == this) {
            Xulor.getInstance().getAppUI().getGLComponent().setCurrentInputMethodRequests(null);
        }
    }
    
    private void activateIME() {
        Xulor.getInstance().getAppUI().getGLComponent().setCurrentInputMethodRequests(this);
        Xulor.getInstance().getAppUI().getGLComponent().addInputMethodListener(this);
        try {
            Xulor.getInstance().getAppUI().getGLComponent().getInputContext().setCompositionEnabled(true);
        }
        catch (Exception ex) {}
    }
    
    @Override
    protected boolean processKeyReleased(final KeyEvent event) {
        if (!event.hasAlt() && !event.hasAltGraph() && !event.hasMeta() && !event.hasCtrl() && !Character.isIdentifierIgnorable(event.getKeyChar()) && event.getKeyChar() != '\uffff') {
            MasterRootContainer.getInstance().setKeyEventConsumed(true);
        }
        return super.processKeyReleased(event);
    }
    
    @Override
    protected boolean processKeyPressedEvent(final KeyEvent event) {
        if (super.processKeyPressedEvent(event)) {
            switch (event.getKeyCode()) {
                case 127: {
                    MasterRootContainer.getInstance().setKeyEventConsumed(true);
                    return false;
                }
                case 86: {
                    if (event.hasCtrl()) {
                        final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                        try {
                            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                                final String text = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                                if (text != null && text.length() != 0) {
                                    this.getTextBuilder().replaceSelectionBy(text);
                                }
                            }
                        }
                        catch (UnsupportedFlavorException e) {
                            TextEditor.m_logger.debug((Object)"Probl\u00e8me au paste du texte");
                        }
                        catch (IOException e2) {
                            TextEditor.m_logger.debug((Object)"Probl\u00e8me au paste du texte");
                        }
                        MasterRootContainer.getInstance().setKeyEventConsumed(true);
                        return false;
                    }
                    break;
                }
                case 88: {
                    if (event.hasCtrl()) {
                        if (!this.getTextBuilder().getDocument().isPassword()) {
                            this.copySelectedTextToClipboard();
                            this.getTextBuilder().replaceSelectionBy("");
                        }
                        MasterRootContainer.getInstance().setKeyEventConsumed(true);
                        return false;
                    }
                    break;
                }
                case 65: {
                    if (event.hasCtrl()) {
                        this.selectAll();
                        MasterRootContainer.getInstance().setKeyEventConsumed(true);
                        return false;
                    }
                    break;
                }
            }
        }
        if (!event.hasAlt() && !event.hasAltGraph() && !event.hasMeta() && !event.hasCtrl() && !Character.isIdentifierIgnorable(event.getKeyChar()) && event.getKeyChar() != '\uffff') {
            MasterRootContainer.getInstance().setKeyEventConsumed(true);
        }
        return true;
    }
    
    @Override
    protected boolean processKeyTypedEvent(final KeyEvent event) {
        if (super.processKeyTypedEvent(event) && this.getTextBuilder().isEditable()) {
            switch (event.getKeyChar()) {
                case '\b': {
                    this.getTextBuilder().removeLeftSelectionSubPart();
                    return false;
                }
                case '\u007f': {
                    this.getTextBuilder().removeRightSelectionSubPart();
                    MasterRootContainer.getInstance().setKeyEventConsumed(true);
                    return false;
                }
                case '\t': {
                    return false;
                }
                case '\n': {
                    if (!this.getMultiline()) {
                        return true;
                    }
                    break;
                }
            }
            this.getTextBuilder().replaceSelectionBy(String.valueOf(event.getKeyChar()));
            return false;
        }
        return true;
    }
    
    public boolean isGhostTextVisible() {
        return this.m_ghostTextVisible;
    }
    
    private void checkGhostTextNeeded() {
        if (this.m_ghostText == null || !this.m_ghostTextVisible) {
            return;
        }
        this.setTextImmediate("");
        this.m_ghostTextVisible = false;
    }
    
    @Override
    public void copyElement(final BasicElement t) {
        final TextEditor e = (TextEditor)t;
        super.copyElement(e);
        e.setMaxCharacters(this.getMaxCharacters());
        e.setPassword(this.getPassword());
        e.setAutoHorizontalScrolled(this.getAutoHorizontalScrolled());
        e.setRestrict(this.getRestrict());
        e.setUnicodeRestrict(this.getUnicodeRestrict());
        if (this.m_rawAppendText != null) {
            e.m_rawAppendText = this.m_rawAppendText;
        }
        if (this.m_ghostText != null) {
            e.setGhostText(this.getGhostText());
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TextEditor.AUTO_HORIZONTAL_SCROLLED_HASH) {
            this.setAutoHorizontalScrolled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextEditor.MAX_CHARACTERS_HASH || hash == TextEditor.MAX_CHARS_HASH) {
            this.setMaxCharacters(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TextEditor.PASSWORD_HASH) {
            this.setPassword(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextEditor.RESTRICT_HASH) {
            this.setRestrict(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == TextEditor.EDITABLE_HASH) {
            this.setEditable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextEditor.GHOST_TEXT_HASH) {
            this.setGhostText(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != TextEditor.UNICODE_RESTRICT_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setUnicodeRestrict(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TextEditor.AUTO_HORIZONTAL_SCROLLED_HASH) {
            this.setAutoHorizontalScrolled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextEditor.MAX_CHARACTERS_HASH || hash == TextEditor.MAX_CHARS_HASH) {
            this.setMaxCharacters(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TextEditor.PASSWORD_HASH) {
            this.setPassword(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextEditor.RESTRICT_HASH) {
            this.setRestrict((String)value);
        }
        else if (hash == TextEditor.EDITABLE_HASH) {
            this.setEditable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextEditor.GHOST_TEXT_HASH) {
            this.setGhostText((String)value);
        }
        else {
            if (hash != TextEditor.UNICODE_RESTRICT_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setUnicodeRestrict(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean appendXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TextEditor.TEXT_HASH) {
            this.appendText(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return super.appendXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean appendPropertyAttribute(final int hash, final Object value) {
        if (hash == TextEditor.TEXT_HASH) {
            this.appendText(String.valueOf(value));
            return true;
        }
        return super.appendPropertyAttribute(hash, value);
    }
    
    @Override
    public void inputMethodTextChanged(final InputMethodEvent event) {
        int committedCharacters = event.getCommittedCharacterCount();
        final AttributedCharacterIterator text = event.getText();
        if (text == null) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        char c = text.first();
        while (committedCharacters-- > 0) {
            builder.append(c);
            c = text.next();
        }
        if (builder.length() > 0) {
            this.getTextBuilder().replaceSelectionBy(builder.toString());
            return;
        }
        while (c != '\uffff') {
            builder.append(c);
            c = text.next();
        }
        this.getTextBuilder().replaceSelectionBy(builder.toString());
        for (int i = 0; i < builder.length(); ++i) {
            this.getTextBuilder().moveSelectionEndToLeft();
        }
    }
    
    @Override
    public void caretPositionChanged(final InputMethodEvent event) {
    }
    
    @Override
    public Rectangle getTextLocation(final TextHitInfo offset) {
        final int windowX = Xulor.getInstance().getAppUI().getAppFrame().getX();
        final int windowY = Xulor.getInstance().getAppUI().getAppFrame().getY();
        final int widgetY = Xulor.getInstance().getAppUI().getAppFrame().getHeight() - this.getScreenY();
        final TextDocument document = this.getTextBuilder().getDocument();
        final int width = document.getTextRenderedWidth(0, document.getSelectionEndSubPartIndex());
        return new Rectangle(windowX + this.getScreenX() + width, windowY + widgetY, this.getWidth(), -this.getHeight() / 2);
    }
    
    @Nullable
    @Override
    public TextHitInfo getLocationOffset(final int x, final int y) {
        final int selectionStartSubPartIndex = this.getTextBuilder().getDocument().getSelectionStartSubPartIndex();
        return TextHitInfo.afterOffset(selectionStartSubPartIndex);
    }
    
    @Override
    public int getInsertPositionOffset() {
        return 0;
    }
    
    @Nullable
    @Override
    public AttributedCharacterIterator getCommittedText(final int beginIndex, final int endIndex, final AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }
    
    @Override
    public int getCommittedTextLength() {
        return 0;
    }
    
    @Nullable
    @Override
    public AttributedCharacterIterator cancelLatestCommittedText(final AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }
    
    @Nullable
    @Override
    public AttributedCharacterIterator getSelectedText(final AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }
    
    static {
        AUTO_HORIZONTAL_SCROLLED_HASH = "autoHorizontalScrolled".hashCode();
        MAX_CHARS_HASH = "maxChars".hashCode();
        MAX_CHARACTERS_HASH = "maxCharacters".hashCode();
        PASSWORD_HASH = "password".hashCode();
        RESTRICT_HASH = "restrict".hashCode();
        EDITABLE_HASH = "editable".hashCode();
        UNICODE_RESTRICT_HASH = "unicodeRestrict".hashCode();
        GHOST_TEXT_HASH = "ghostText".hashCode();
    }
}
