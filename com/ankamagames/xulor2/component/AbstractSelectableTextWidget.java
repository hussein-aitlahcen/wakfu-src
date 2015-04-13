package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.text.builder.content.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.component.text.document.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.text.builder.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import java.awt.*;
import java.awt.datatransfer.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.util.*;

public abstract class AbstractSelectableTextWidget extends TextWidget implements ClipboardOwner
{
    private static final int MAX_DELAY_BETWEEN_FOCUS_AND_PRESS = 50;
    protected EventListener m_mouseReleasedListener;
    private EventListener m_focusListener;
    private EventListener m_mousePressedListener;
    private EventListener m_mouseDraggedListener;
    private EventListener m_mouseEnteredListener;
    private EventListener m_mouseMovedListener;
    private EventListener m_mouseExitedListener;
    private EventListener m_keyReleasedListener;
    private EventListener m_keyPressedListener;
    private EventListener m_keyTypedListener;
    private long m_lastFocusGainedTime;
    protected boolean m_needToProcessSelectOnFocus;
    private boolean m_enableOnlySelectablePartInteraction;
    private AbstractContentBlock m_blockUnderMouse;
    private boolean m_selectOnFocus;
    public static final int SELECTABLE_HASH;
    public static final int SELECT_ON_FOCUS_HASH;
    public static final int ENABLE_ONLY_SELECTABLE_PART_INTERACTION_HASH;
    
    public AbstractSelectableTextWidget() {
        super();
        this.m_lastFocusGainedTime = 0L;
        this.m_needToProcessSelectOnFocus = false;
        this.m_enableOnlySelectablePartInteraction = false;
        this.m_blockUnderMouse = null;
        this.m_selectOnFocus = false;
    }
    
    public boolean getSelectable() {
        return this.getTextBuilder().isSelectable();
    }
    
    public void setSelectable(final boolean selectable) {
        this.getTextBuilder().setSelectable(selectable);
        this.setFocusable(selectable);
        if (selectable) {
            this.setCursorType(CursorFactory.CursorType.TEXT);
        }
        else {
            this.setCursorType(CursorFactory.CursorType.DEFAULT);
        }
    }
    
    public boolean isSelectOnFocus() {
        return this.m_selectOnFocus;
    }
    
    public void setSelectOnFocus(final boolean selectOnFocus) {
        this.m_selectOnFocus = selectOnFocus;
    }
    
    public boolean isEnableOnlySelectablePartInteraction() {
        return this.m_enableOnlySelectablePartInteraction;
    }
    
    public void setEnableOnlySelectablePartInteraction(final boolean enableOnlySelectablePartInteraction) {
        this.m_enableOnlySelectablePartInteraction = enableOnlySelectablePartInteraction;
    }
    
    @Nullable
    @Override
    public Widget getWidget(final int x, final int y) {
        this.m_blockUnderMouse = null;
        if (this.m_unloading) {
            return null;
        }
        if (!this.m_visible || this.m_nonBlocking || !this.getAppearance().insideInsets(x, y) || MasterRootContainer.getInstance().isMovePointMode()) {
            return null;
        }
        this.m_blockUnderMouse = this.getContentBlockUnderMouse(x, -y);
        if (this.m_enableOnlySelectablePartInteraction && (this.m_blockUnderMouse == null || this.m_blockUnderMouse.getType() != AbstractContentBlock.BlockType.TEXT || ((TextContentBlock)this.m_blockUnderMouse).getDocumentPart() == null || ((TextContentBlock)this.m_blockUnderMouse).getDocumentPart().getId() == null)) {
            return null;
        }
        return this;
    }
    
    public AbstractContentBlock getBlockUnderMouse() {
        return this.m_blockUnderMouse;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_blockUnderMouse = null;
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_focusListener = null;
        this.m_mousePressedListener = null;
        this.m_mouseReleasedListener = null;
        this.m_mouseDraggedListener = null;
        this.m_mouseEnteredListener = null;
        this.m_mouseMovedListener = null;
        this.m_mouseExitedListener = null;
        this.m_keyReleasedListener = null;
        this.m_keyPressedListener = null;
        this.m_keyTypedListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_enableOnlySelectablePartInteraction = false;
        this.addListeners();
    }
    
    public void selectAll() {
        if (this.getSelectable()) {
            final TextDocument document = this.getTextBuilder().getDocument();
            if (document.isEmpty()) {
                this.getTextBuilder().addEmptyTextPart();
            }
            this.getTextBuilder().setSelectionStart(document.getFirstPart(), 0);
            final AbstractDocumentPart lastDocumentPart = document.getLastPart();
            this.getTextBuilder().setSelectionEnd(lastDocumentPart, lastDocumentPart.getLength());
        }
    }
    
    private Point getOrientedMouseCoodinates(final MouseEvent event) {
        int orientedMouseX = 0;
        int orientedMouseY = 0;
        switch (this.getTextBuilder().getOrientation()) {
            case NORTH: {
                orientedMouseX = event.getY(this) - this.m_appearance.getBottomInset();
                orientedMouseY = this.m_size.width - event.getX(this) - this.m_appearance.getLeftInset() - this.m_appearance.getRightInset();
                break;
            }
            case EAST: {
                orientedMouseX = event.getX(this) - this.m_appearance.getLeftInset();
                orientedMouseY = event.getY(this) - this.m_appearance.getBottomInset();
                break;
            }
            case SOUTH: {
                orientedMouseX = this.m_size.height - event.getY(this) - this.m_appearance.getBottomInset() - this.m_appearance.getTopInset();
                orientedMouseY = event.getX(this) - this.m_appearance.getLeftInset();
                break;
            }
            case WEST: {
                orientedMouseX = this.m_size.width - event.getX(this) - this.m_appearance.getLeftInset() - this.m_appearance.getRightInset();
                orientedMouseY = this.m_size.height - event.getY(this) - this.m_appearance.getBottomInset() - this.m_appearance.getTopInset();
                break;
            }
        }
        return new Point(orientedMouseX, orientedMouseY);
    }
    
    protected void addListeners() {
        this.m_focusListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                AbstractSelectableTextWidget.this.processFocusChangedEvent((FocusChangedEvent)event);
                return false;
            }
        };
        this.addEventListener(Events.FOCUS_CHANGED, this.m_focusListener, false);
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final Point orientedMouseCoordiantes = AbstractSelectableTextWidget.this.getOrientedMouseCoodinates((MouseEvent)event);
                final ObjectPair<AbstractContentBlock, TextBuilder.BlockIntersectionType> pair = AbstractSelectableTextWidget.this.getTextBuilder().getBlockFromCoordinates(orientedMouseCoordiantes.x, -orientedMouseCoordiantes.y);
                final AbstractContentBlock block = pair.getFirst();
                if (block != null) {
                    int contentBlockPartIndex = 0;
                    switch (pair.getSecond()) {
                        case OUTSIDE_TOP:
                        case OUTSIDE_LEFT: {
                            contentBlockPartIndex = 0;
                            break;
                        }
                        case INSIDE: {
                            contentBlockPartIndex = block.getContentBlockPartIndexFromCoordinates(AbstractSelectableTextWidget.this.getTextBuilder().getDefaultTextRenderer(), orientedMouseCoordiantes.x - block.getX() - block.getLineBlock().getX());
                            break;
                        }
                        case OUTSIDE_RIGHT:
                        case OUTSIDE_BOTTOM: {
                            contentBlockPartIndex = block.getLength();
                            break;
                        }
                    }
                    AbstractSelectableTextWidget.this.processMouseReleaseEvent(block, contentBlockPartIndex);
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_mousePressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final Point orientedMouseCoordiantes = AbstractSelectableTextWidget.this.getOrientedMouseCoodinates((MouseEvent)event);
                final ObjectPair<AbstractContentBlock, TextBuilder.BlockIntersectionType> pair = AbstractSelectableTextWidget.this.getTextBuilder().getBlockFromCoordinates(orientedMouseCoordiantes.x, -orientedMouseCoordiantes.y);
                final AbstractContentBlock block = pair.getFirst();
                final TextBuilder.BlockIntersectionType blockIntersectionType = pair.getSecond();
                if (block != null) {
                    int contentBlockPartIndex = 0;
                    switch (blockIntersectionType) {
                        case OUTSIDE_TOP:
                        case OUTSIDE_LEFT: {
                            contentBlockPartIndex = 0;
                            break;
                        }
                        case INSIDE: {
                            contentBlockPartIndex = block.getContentBlockPartIndexFromCoordinates(AbstractSelectableTextWidget.this.getTextBuilder().getDefaultTextRenderer(), orientedMouseCoordiantes.x - block.getX() - block.getLineBlock().getX());
                            break;
                        }
                        case OUTSIDE_RIGHT:
                        case OUTSIDE_BOTTOM: {
                            contentBlockPartIndex = block.getLength();
                            break;
                        }
                    }
                    AbstractSelectableTextWidget.this.processMousePressedEvent(block, contentBlockPartIndex);
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        this.m_mouseDraggedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() != AbstractSelectableTextWidget.this) {
                    return false;
                }
                final Point orientedMouseCoordiantes = AbstractSelectableTextWidget.this.getOrientedMouseCoodinates((MouseEvent)event);
                final ObjectPair<AbstractContentBlock, TextBuilder.BlockIntersectionType> pair = AbstractSelectableTextWidget.this.getTextBuilder().getBlockFromCoordinates(orientedMouseCoordiantes.x, -orientedMouseCoordiantes.y);
                final AbstractContentBlock block = pair.getFirst();
                final TextBuilder.BlockIntersectionType blockIntersectionType = pair.getSecond();
                if (block != null) {
                    int contentBlockPartIndex = 0;
                    switch (blockIntersectionType) {
                        case OUTSIDE_TOP:
                        case OUTSIDE_LEFT: {
                            contentBlockPartIndex = 0;
                            break;
                        }
                        case INSIDE: {
                            contentBlockPartIndex = block.getContentBlockPartIndexFromCoordinates(AbstractSelectableTextWidget.this.getTextBuilder().getDefaultTextRenderer(), orientedMouseCoordiantes.x - block.getX() - block.getLineBlock().getX());
                            break;
                        }
                        case OUTSIDE_RIGHT:
                        case OUTSIDE_BOTTOM: {
                            contentBlockPartIndex = block.getLength();
                            break;
                        }
                    }
                    AbstractSelectableTextWidget.this.processMouseDraggedEvent(block, contentBlockPartIndex);
                }
                return true;
            }
        };
        this.addEventListener(Events.MOUSE_DRAGGED, this.m_mouseDraggedListener, false);
        this.m_mouseEnteredListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() != AbstractSelectableTextWidget.this) {
                    return false;
                }
                final AbstractContentBlock underMouse = AbstractSelectableTextWidget.this.m_blockUnderMouse;
                if (underMouse != null && underMouse.getType() == AbstractContentBlock.BlockType.TEXT && ((TextContentBlock)underMouse).getDocumentPart() != null && ((TextContentBlock)underMouse).getDocumentPart().getId() != null) {
                    AbstractSelectableTextWidget.this.setCursorType(CursorFactory.CursorType.HAND);
                }
                else {
                    AbstractSelectableTextWidget.this.setCursorType(CursorFactory.CursorType.DEFAULT);
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_ENTERED, this.m_mouseEnteredListener, false);
        this.m_mouseMovedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() != AbstractSelectableTextWidget.this) {
                    return false;
                }
                final AbstractContentBlock underMouse = AbstractSelectableTextWidget.this.m_blockUnderMouse;
                if (underMouse != null && underMouse.getType() == AbstractContentBlock.BlockType.TEXT && ((TextContentBlock)underMouse).getDocumentPart() != null && ((TextContentBlock)underMouse).getDocumentPart().getId() != null) {
                    AbstractSelectableTextWidget.this.setCursorType(CursorFactory.CursorType.HAND);
                }
                else {
                    AbstractSelectableTextWidget.this.setCursorType(AbstractSelectableTextWidget.this.getTextBuilder().isSelectable() ? CursorFactory.CursorType.TEXT : CursorFactory.CursorType.DEFAULT);
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_MOVED, this.m_mouseMovedListener, false);
        this.m_mouseExitedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() == AbstractSelectableTextWidget.this && AbstractSelectableTextWidget.this.getTextBuilder().isSelectable()) {
                    AbstractSelectableTextWidget.this.setCursorType(AbstractSelectableTextWidget.this.getTextBuilder().isSelectable() ? CursorFactory.CursorType.TEXT : CursorFactory.CursorType.DEFAULT);
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_EXITED, this.m_mouseExitedListener, false);
        this.m_keyReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                AbstractSelectableTextWidget.this.processKeyReleased((KeyEvent)event);
                return false;
            }
        };
        this.addEventListener(Events.KEY_RELEASED, this.m_keyReleasedListener, true);
        this.m_keyPressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                AbstractSelectableTextWidget.this.processKeyPressedEvent((KeyEvent)event);
                return false;
            }
        };
        this.addEventListener(Events.KEY_PRESSED, this.m_keyPressedListener, true);
        this.m_keyTypedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                AbstractSelectableTextWidget.this.processKeyTypedEvent((KeyEvent)event);
                return false;
            }
        };
        this.addEventListener(Events.KEY_TYPED, this.m_keyTypedListener, true);
    }
    
    public AbstractContentBlock getContentBlockUnderMouse(final MouseEvent event) {
        if (event.getTarget() != this) {
            return null;
        }
        final Point orientedMouseCoordiantes = this.getOrientedMouseCoodinates(event);
        return this.getContentBlockUnderMouse(orientedMouseCoordiantes.x, -orientedMouseCoordiantes.y);
    }
    
    private AbstractContentBlock getContentBlockUnderMouse(final int x, final int y) {
        final ObjectPair<AbstractContentBlock, TextBuilder.BlockIntersectionType> pair = this.getTextBuilder().getBlockFromCoordinates(x, y);
        return (pair == null) ? null : pair.getFirst();
    }
    
    protected void processFocusChangedEvent(final FocusChangedEvent focusChangedEvent) {
        this.m_lastFocusGainedTime = (focusChangedEvent.getFocused() ? System.currentTimeMillis() : 0L);
        if (!focusChangedEvent.getFocused()) {
            this.getTextBuilder().clearSelectionIndices();
        }
    }
    
    protected void processMouseReleaseEvent(final AbstractContentBlock block, final int contentBlockPartIndex) {
        if (this.m_needToProcessSelectOnFocus) {
            this.selectAll();
            this.m_lastFocusGainedTime = 0L;
            this.m_needToProcessSelectOnFocus = false;
        }
    }
    
    protected void processMousePressedEvent(final AbstractContentBlock block, final int contentBlockPartIndex) {
        this.m_needToProcessSelectOnFocus = (System.currentTimeMillis() - this.m_lastFocusGainedTime < 50L && this.m_selectOnFocus);
        if (!this.m_needToProcessSelectOnFocus && this.getSelectable()) {
            this.getTextBuilder().setSelectionStart(block.getDocumentPart(), block.getStartIndex() + contentBlockPartIndex);
            this.getTextBuilder().setSelectionEnd(block.getDocumentPart(), block.getStartIndex() + contentBlockPartIndex);
        }
    }
    
    protected void processMouseDraggedEvent(final AbstractContentBlock block, final int contentBlockPartIndex) {
        this.m_lastFocusGainedTime = 0L;
        this.m_needToProcessSelectOnFocus = false;
        if (this.getSelectable()) {
            this.getTextBuilder().setSelectionEnd(block.getDocumentPart(), block.getStartIndex() + contentBlockPartIndex);
        }
    }
    
    protected boolean processKeyReleased(final KeyEvent event) {
        return false;
    }
    
    protected boolean processKeyPressedEvent(final KeyEvent event) {
        switch (event.getKeyCode()) {
            case 37: {
                if (event.hasAlt() || event.hasAltGraph() || event.hasMeta()) {
                    return true;
                }
                if (event.hasCtrl()) {
                    this.jump(true, event.hasShift());
                    return false;
                }
                this.getTextBuilder().moveSelectionEndToLeft();
                if (!event.hasShift()) {
                    this.getTextBuilder().setSelectionStartAtSelectionEnd();
                }
                MasterRootContainer.getInstance().setKeyEventConsumed(true);
                return false;
            }
            case 39: {
                if (event.hasAlt() || event.hasAltGraph() || event.hasMeta()) {
                    return true;
                }
                if (event.hasCtrl()) {
                    this.jump(false, event.hasShift());
                    return false;
                }
                this.getTextBuilder().moveSelectionEndToRight();
                if (!event.hasShift()) {
                    this.getTextBuilder().setSelectionStartAtSelectionEnd();
                }
                MasterRootContainer.getInstance().setKeyEventConsumed(true);
                return false;
            }
            case 38:
            case 40: {
                MasterRootContainer.getInstance().setKeyEventConsumed(true);
                return false;
            }
            case 36: {
                this.getTextBuilder().moveSelectionEndToStartOfLine();
                if (!event.hasShift()) {
                    this.getTextBuilder().setSelectionStartAtSelectionEnd();
                }
                MasterRootContainer.getInstance().setKeyEventConsumed(true);
                return false;
            }
            case 35: {
                this.getTextBuilder().moveSelectionEndToEndOfLine();
                if (!event.hasShift()) {
                    this.getTextBuilder().setSelectionStartAtSelectionEnd();
                }
                MasterRootContainer.getInstance().setKeyEventConsumed(true);
                return false;
            }
            case 67: {
                if (event.hasCtrl()) {
                    if (!this.getTextBuilder().getDocument().isPassword()) {
                        this.copySelectedTextToClipboard();
                    }
                    MasterRootContainer.getInstance().setKeyEventConsumed(true);
                    return false;
                }
                break;
            }
        }
        return true;
    }
    
    private void jump(final boolean left, final boolean shiftActivated) {
        final TextDocument textDocument = this.getTextBuilder().getDocument();
        final int selectionSubPartIndex = left ? textDocument.getSelectionStartSubPartIndex() : textDocument.getSelectionEndSubPartIndex();
        final int selectionPartIndex = textDocument.getSelectionEndPartIndex();
        int currentSelectionSubPartIndex = selectionSubPartIndex;
        int i = selectionPartIndex;
        while (true) {
            if (left) {
                if (i < 0) {
                    break;
                }
            }
            else if (i >= textDocument.getPartSize()) {
                break;
            }
            final AbstractDocumentPart part = textDocument.getPartAt(selectionPartIndex);
            if (part.getType() == DocumentPartType.IMAGE) {
                textDocument.setSelectionStart(part, 0);
                textDocument.setSelectionEnd(part, 0);
            }
            else if (part.getType() == DocumentPartType.TEXT) {
                final String text = ((TextDocumentPart)part).getText();
                boolean checkValid = false;
                boolean first = true;
                char currentChar;
                for (char lastChar = (currentSelectionSubPartIndex >= text.length()) ? ' ' : text.charAt(currentSelectionSubPartIndex); !checkValid; checkValid = (left ? (lastChar != ' ' && currentChar == ' ' && !first) : (lastChar == ' ' && currentChar != ' ')), first = false, lastChar = currentChar) {
                    currentSelectionSubPartIndex += (left ? -1 : 1);
                    if (currentSelectionSubPartIndex < 0) {
                        break;
                    }
                    if (currentSelectionSubPartIndex > text.length() - 1) {
                        break;
                    }
                    currentChar = text.charAt(currentSelectionSubPartIndex);
                }
                if (currentSelectionSubPartIndex < 0) {
                    currentSelectionSubPartIndex = (left ? 0 : text.length());
                }
                else if (currentSelectionSubPartIndex > text.length()) {
                    currentSelectionSubPartIndex = text.length();
                }
                else {
                    currentSelectionSubPartIndex += (left ? 1 : 0);
                }
                if (!shiftActivated) {
                    this.getTextBuilder().setSelectionStart(part, currentSelectionSubPartIndex);
                }
                this.getTextBuilder().setSelectionEnd(part, currentSelectionSubPartIndex);
                break;
            }
            i += (left ? -1 : 1);
        }
    }
    
    protected boolean processKeyTypedEvent(final KeyEvent event) {
        if (event.hasCtrl()) {
            MasterRootContainer.getInstance().setKeyEventConsumed(true);
            MasterRootContainer.getInstance().setMovePointMode(false);
            return false;
        }
        return true;
    }
    
    @Override
    public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
    }
    
    protected void copySelectedTextToClipboard() {
        final String selectedText = this.getTextBuilder().getDocument().getSelectedText();
        if (selectedText != null && selectedText.length() != 0) {
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            final StringSelection stringSelection = new StringSelection(selectedText);
            clipboard.setContents(stringSelection, this);
        }
    }
    
    @Override
    public void copyElement(final BasicElement a) {
        final AbstractSelectableTextWidget e = (AbstractSelectableTextWidget)a;
        super.copyElement(e);
        e.m_selectOnFocus = this.m_selectOnFocus;
        e.setSelectable(this.getTextBuilder().isSelectable());
        e.removeEventListener(Events.FOCUS_CHANGED, this.m_focusListener, false);
        e.removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        e.removeEventListener(Events.MOUSE_DRAGGED, this.m_mouseDraggedListener, false);
        e.removeEventListener(Events.MOUSE_ENTERED, this.m_mouseEnteredListener, false);
        e.removeEventListener(Events.MOUSE_MOVED, this.m_mouseMovedListener, false);
        e.removeEventListener(Events.MOUSE_EXITED, this.m_mouseExitedListener, false);
        e.removeEventListener(Events.KEY_RELEASED, this.m_keyReleasedListener, false);
        e.removeEventListener(Events.KEY_PRESSED, this.m_keyPressedListener, false);
        e.removeEventListener(Events.KEY_TYPED, this.m_keyTypedListener, false);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AbstractSelectableTextWidget.SELECTABLE_HASH) {
            this.setSelectable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AbstractSelectableTextWidget.SELECT_ON_FOCUS_HASH) {
            this.setSelectOnFocus(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AbstractSelectableTextWidget.ENABLE_ONLY_SELECTABLE_PART_INTERACTION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setEnableOnlySelectablePartInteraction(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AbstractSelectableTextWidget.SELECTABLE_HASH) {
            this.setSelectable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AbstractSelectableTextWidget.SELECT_ON_FOCUS_HASH) {
            this.setSelectOnFocus(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AbstractSelectableTextWidget.ENABLE_ONLY_SELECTABLE_PART_INTERACTION_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setEnableOnlySelectablePartInteraction(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        SELECTABLE_HASH = "selectable".hashCode();
        SELECT_ON_FOCUS_HASH = "selectOnFocus".hashCode();
        ENABLE_ONLY_SELECTABLE_PART_INTERACTION_HASH = "enableOnlySelectablePartInteraction".hashCode();
    }
}
