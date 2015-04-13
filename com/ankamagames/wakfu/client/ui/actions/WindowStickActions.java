package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.windowStick.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.maths.*;

@XulorActionsTag
public class WindowStickActions
{
    public static final String PACKAGE = "wakfu.windowStick";
    private static final byte HORIZONTAL_VERTICAL = 0;
    private static final byte LEFT_RIGHT = 1;
    
    public static void stickWindow(final WindowStickEvent event) {
        windowStick(event, "", (byte)0);
    }
    
    private static String computeDialogId(final Window w, final Alignment9 align, final byte type) {
        switch (type) {
            case 0: {
                switch (align) {
                    case NORTH:
                    case SOUTH: {
                        return w.getHorizontalDialog();
                    }
                    default: {
                        return w.getVerticalDialog();
                    }
                }
                break;
            }
            case 1: {
                final String id = w.getElementMap().getId();
                switch (align) {
                    case WEST: {
                        return w.getVerticalDialog();
                    }
                    case EAST: {
                        return w.getHorizontalDialog();
                    }
                    default: {
                        return id;
                    }
                }
                break;
            }
            default: {
                return null;
            }
        }
    }
    
    private static Window windowStick(final WindowStickEvent event, final String suffix, final byte switchType) {
        final Window window = event.getTarget();
        final String id = window.getElementMap().getId();
        final WindowStickData stickData = window.getStickData();
        final Alignment9 align = event.getAlign();
        final int dragButton = MasterRootContainer.getInstance().getDragButton();
        final String dialogId = computeDialogId(window, align, switchType);
        if (id.startsWith(dialogId)) {
            return window;
        }
        Xulor.getInstance().unload(id, false);
        final Window newWindow = (Window)Xulor.getInstance().load(dialogId + suffix, Dialogs.getDialogPath(dialogId), 40976L, (short)10000);
        newWindow.setHorizontalDialog(window.getHorizontalDialog());
        newWindow.setVerticalDialog(window.getVerticalDialog());
        WindowStickManager.getInstance().addWindow(newWindow, stickData.isMainWindow(), false);
        WindowStickManager.getInstance().saveWindowData(newWindow);
        final ArrayList<WindowMovePoint> movePoints = newWindow.getMovePoints();
        if (!movePoints.isEmpty()) {
            final WindowMovePoint wmp = movePoints.get(0);
            final EventListener changeListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    wmp.removeEventListener(event.getType(), this, false);
                    final int mouseX = MouseManager.getInstance().getX() - wmp.getX(newWindow) - wmp.getWidth() / 2;
                    final int mouseY = MouseManager.getInstance().getY() - wmp.getY(newWindow) - wmp.getHeight() / 2;
                    newWindow.setPosition(mouseX, mouseY);
                    MasterRootContainer.getInstance().setDragged(wmp, dragButton);
                    wmp.setDragMousePosition(MouseManager.getInstance().getX(), MouseManager.getInstance().getY());
                    return false;
                }
            };
            wmp.setEnablePositionEvents(true);
            wmp.setEnableResizeEvents(true);
            wmp.addEventListener(Events.RESIZED, changeListener, false);
            wmp.addEventListener(Events.REPOSITIONED, changeListener, false);
        }
        return newWindow;
    }
    
    public static void stickShortcutBar(final WindowStickEvent event, final ShortcutBar bar) {
        final byte index = (byte)(bar.getIndex() - bar.getType().getFirstIndex());
        final Window window = windowStick(event, String.valueOf(index), (byte)0);
        UIShortcutBarFrame.getInstance().setShortcutBarVertical(index, window.getElementMap().getId().startsWith(window.getVerticalDialog()));
        UIShortcutBarFrame.getInstance().updateAdditionalShortcutBar(index);
    }
    
    public static void stickStateBar(final WindowStickEvent event) {
        final Window oldWindow = event.getTarget();
        final Window window = windowStick(event, "", (byte)0);
        if (oldWindow != window) {
            window.addEventListener(Events.RESIZED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    final Container parent = window.getContainer();
                    final int x = MathHelper.clamp(window.getX(), 0, parent.getWidth() - window.getWidth());
                    final int y = MathHelper.clamp(window.getY(), 0, parent.getHeight() - window.getHeight());
                    window.setPosition(x, y);
                    return false;
                }
            }, false);
        }
    }
    
    public static void stickFollowAchievements(final WindowStickEvent event) {
        final Window oldWindow = event.getTarget();
        final Window window = windowStick(event, "", (byte)1);
        if (oldWindow != window) {
            window.addEventListener(Events.RESIZED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    final Container parent = window.getContainer();
                    final WindowStickData stickData = window.getStickData();
                    final WindowStickManager.StickAlignment align = (stickData != null) ? stickData.getAlign() : null;
                    int x;
                    int y;
                    if (align == null || align == WindowStickManager.StickAlignment.NONE) {
                        x = MathHelper.clamp(window.getX(), 0, parent.getWidth() - window.getWidth());
                        y = MathHelper.clamp(window.getY(), 0, parent.getHeight() - window.getHeight());
                    }
                    else {
                        x = align.getBorderX(window.getX(), window);
                        y = align.getBorderY(window.getY(), window);
                    }
                    window.setPosition(x, y);
                    return false;
                }
            }, false);
            window.setEnableResizeEvents(true);
        }
    }
}
