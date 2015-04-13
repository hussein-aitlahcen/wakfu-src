package com.ankamagames.xulor2.core.windowStick;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import java.awt.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.xulor2.component.*;

public class WindowStickManager
{
    private static final WindowStickManager m_instance;
    private static final short STICK_MARGIN = 20;
    private static final int INVALID_STICK_ID = Integer.MIN_VALUE;
    private static int CURRENT_ID;
    private IntObjectLightWeightMap<StickGroup> m_stickGroups;
    private ArrayList<Window> m_windows;
    private final Rectangle m_rect;
    private static final ArrayList<Window> COPY_WINDOWS;
    private static final String stickDataPrefKeyPrefix = "stickData";
    
    private WindowStickManager() {
        super();
        this.m_stickGroups = new IntObjectLightWeightMap<StickGroup>();
        this.m_windows = new ArrayList<Window>();
        this.m_rect = new Rectangle();
    }
    
    public static WindowStickManager getInstance() {
        return WindowStickManager.m_instance;
    }
    
    public IntObjectLightWeightMap<StickGroup> getStickGroups() {
        return this.m_stickGroups;
    }
    
    public void addWindow(final Window w, final boolean mainWindow) {
        this.addWindow(w, mainWindow, true);
    }
    
    public void addWindow(final Window w, final boolean mainWindow, final boolean tryToStick) {
        w.setStickData(new WindowStickData(mainWindow));
        this.m_windows.add(w);
        w.getStickData().setStickId(Integer.MIN_VALUE);
        if (tryToStick) {
            w.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                @Override
                public void windowPostProcessed() {
                    WindowStickManager.this.updateStickData(w);
                    w.removeWindowPostProcessedListener(this);
                }
            });
        }
    }
    
    public void removeWindow(final Window w) {
        this.removeFromItsGroup(w);
        this.m_windows.remove(w);
    }
    
    public void addToGroup(final Window w, final int stickId) {
        if (w.getStickData().getStickId() == stickId) {
            return;
        }
        this.removeFromItsGroup(w);
        w.getStickData().setStickId(stickId);
        StickGroup stickGroup = this.m_stickGroups.get(stickId);
        if (stickGroup == null) {
            stickGroup = new StickGroup();
            this.m_stickGroups.put(stickId, stickGroup);
        }
        stickGroup.add(w);
    }
    
    public void removeFromItsGroup(final Window w) {
        final int id = w.getStickData().getStickId();
        final StickGroup stickGroup = this.m_stickGroups.get(id);
        if (stickGroup != null) {
            stickGroup.remove(w);
        }
        w.getStickData().setStickId(Integer.MIN_VALUE);
    }
    
    private static int generateNewStickId() {
        return ++WindowStickManager.CURRENT_ID;
    }
    
    public StickAlignment getBorderAlignmentForStickId(final int stickId) {
        final StickGroup stickGroup = this.m_stickGroups.get(stickId);
        if (stickGroup == null || stickGroup.isEmpty()) {
            return StickAlignment.NONE;
        }
        this.fillRectWithStickBounds(stickId, this.m_rect);
        return StickAlignment.getBorderStickAlignment(this.m_rect.x, this.m_rect.y, this.m_rect.width, this.m_rect.height, stickGroup.getFirst().getContainer());
    }
    
    public void fillRectWithStickBounds(final int stickId, final Rectangle rect) {
        rect.setBounds(0, 0, 0, 0);
        final StickGroup stickGroup = this.m_stickGroups.get(stickId);
        if (stickGroup == null || stickGroup.isEmpty()) {
            return;
        }
        double x1 = 2.147483647E9;
        double y1 = 2.147483647E9;
        double x2 = -2.147483648E9;
        double y2 = -2.147483648E9;
        final ArrayList<Window> windows = stickGroup.getWindows();
        for (int i = 1, size = windows.size(); i < size; ++i) {
            final Window w = windows.get(i);
            x1 = Math.min(x1, w.getX());
            y1 = Math.min(y1, w.getY());
            x2 = Math.max(x2, w.getX() + w.getWidth());
            y2 = Math.max(y2, w.getY() + w.getHeight());
        }
        rect.setBounds((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1));
    }
    
    public void processWindowMove(final Window w, final int startX, final int startY, int endX, int endY, final Point result, final boolean forceMove) {
        final WindowStickData stickData = w.getStickData();
        final int stickId = (stickData != null) ? stickData.getStickId() : Integer.MIN_VALUE;
        final StickGroup stickGroup = this.m_stickGroups.get(stickId);
        if (stickGroup != null && !stickData.isMainWindow() && !forceMove) {
            final ArrayList<Window> windows = stickGroup.getWindows();
            for (int i = windows.size() - 1; i >= 0; --i) {
                final Window groupWindow = windows.get(i);
                if (groupWindow != w) {
                    final StickAlignment align = StickAlignment.getStickAlignment(endX, endY, w, groupWindow);
                    if (align != StickAlignment.NONE) {
                        endX = align.getX(endX, groupWindow, w);
                        endY = align.getY(endY, groupWindow, w);
                    }
                }
            }
            if (endX == startX && endY == startY) {
                result.setLocation(endX, endY);
                return;
            }
            this.checkWindowStuck(w, endX, endY);
            this.checkWindowGroupStuck(stickGroup);
        }
        for (int j = 0, size = this.m_windows.size(); j < size; ++j) {
            final Window groupWindow = this.m_windows.get(j);
            if (groupWindow != w) {
                if (groupWindow.getStickData().getStickId() != w.getStickData().getStickId() || groupWindow.getStickData().getStickId() == Integer.MIN_VALUE) {
                    final StickAlignment align = StickAlignment.getStickAlignment(endX, endY, w, groupWindow);
                    if (align != StickAlignment.NONE) {
                        endX = align.getX(endX, groupWindow, w);
                        endY = align.getY(endY, groupWindow, w);
                    }
                }
            }
        }
        final StickAlignment borderStickAlignment = StickAlignment.getBorderStickAlignment(endX, endY, w, w.getContainer());
        endX = borderStickAlignment.getBorderX(endX, w);
        endY = borderStickAlignment.getBorderY(endY, w);
        if ((forceMove || w.getStickData().isMainWindow()) && w.getStickData().getStickId() != Integer.MIN_VALUE) {
            final StickGroup stickGroup2 = this.m_stickGroups.get(w.getStickData().getStickId());
            final int deltaX = endX - startX;
            final int deltaY = endY - startY;
            final ArrayList<Window> windows2 = stickGroup2.getWindows();
            for (int k = 0, size2 = windows2.size(); k < size2; ++k) {
                final Window window = windows2.get(k);
                if (window != w) {
                    window.setPosition(window.getX() + deltaX, window.getY() + deltaY);
                }
            }
        }
        result.x = endX;
        result.y = endY;
    }
    
    private void checkWindowGroupStuck(final StickGroup group) {
        final ArrayList<Window> windows = group.getWindows();
        for (int i = windows.size() - 1; i >= 0; --i) {
            WindowStickManager.COPY_WINDOWS.add(windows.get(i));
        }
        for (int i = WindowStickManager.COPY_WINDOWS.size() - 1; i >= 0; --i) {
            final Window testWindow = WindowStickManager.COPY_WINDOWS.get(i);
            this.checkWindowStuck(testWindow, testWindow.getX(), testWindow.getY());
        }
        WindowStickManager.COPY_WINDOWS.clear();
    }
    
    private void checkWindowStuck(final Window w, final int startX, final int startY) {
        final WindowStickData stickData = w.getStickData();
        if (stickData.getStickId() == Integer.MIN_VALUE) {
            return;
        }
        final ArrayList<Window> stickGroup = this.m_stickGroups.get(stickData.getStickId()).getWindows();
        boolean isStillStuck = false;
        for (int i = stickGroup.size() - 1; i >= 0; --i) {
            final Window groupWindow = stickGroup.get(i);
            if (groupWindow != w) {
                final StickAlignment align = StickAlignment.getStickAlignment(startX, startY, w, groupWindow);
                if (align != StickAlignment.NONE) {
                    isStillStuck = true;
                    break;
                }
            }
        }
        if (!isStillStuck) {
            stickGroup.remove(w);
            stickData.setStickId(Integer.MIN_VALUE);
        }
    }
    
    public void endWindowMove(final Window w, final int x, final int y) {
        final StickGroup windowGroup = this.getStickGroup(w);
        if (windowGroup != null) {
            this.checkWindowGroupStuck(windowGroup);
        }
        for (int i = 0, size = this.m_windows.size(); i < size; ++i) {
            final Window groupWindow = this.m_windows.get(i);
            if (groupWindow != w) {
                if (groupWindow.getStickData().getStickId() != w.getStickData().getStickId() || groupWindow.getStickData().getStickId() == Integer.MIN_VALUE) {
                    final StickAlignment align = StickAlignment.getStickAlignment(x, y, w, groupWindow);
                    if (align != StickAlignment.NONE) {
                        final int w1StickId = w.getStickData().getStickId();
                        final int w2StickId = groupWindow.getStickData().getStickId();
                        int newStickId = (w1StickId != Integer.MIN_VALUE) ? w1StickId : w2StickId;
                        if (newStickId == Integer.MIN_VALUE) {
                            newStickId = generateNewStickId();
                        }
                        if (w1StickId != newStickId) {
                            this.addToGroup(w, newStickId);
                        }
                        if (w2StickId != newStickId) {
                            if (w2StickId == Integer.MIN_VALUE) {
                                this.addToGroup(groupWindow, newStickId);
                            }
                            else {
                                final ArrayList<Window> windows = this.m_stickGroups.get(w2StickId).getWindows();
                                for (int j = windows.size() - 1; j >= 0; --j) {
                                    this.addToGroup(windows.get(j), newStickId);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0, size = this.m_stickGroups.size(); i < size; ++i) {
            final int key = this.m_stickGroups.getQuickKey(i);
            final StickGroup stickGroup = this.m_stickGroups.getQuickValue(i);
            final ArrayList<Window> windows2 = stickGroup.getWindows();
            final StickAlignment align2 = this.getBorderAlignmentForStickId(key);
            for (int k = 0, jSize = windows2.size(); k < jSize; ++k) {
                final Window window = windows2.get(k);
                window.getStickData().setAlign(align2);
            }
            stickGroup.recomputeRelativePosition();
        }
        for (int i = 0, size = this.m_windows.size(); i < size; ++i) {
            final Window window2 = this.m_windows.get(i);
            if (window2.getStickData().getStickId() == Integer.MIN_VALUE) {
                window2.getStickData().setAlign(StickAlignment.getBorderStickAlignment(window2.getX(), window2.getY(), window2.getWidth(), window2.getHeight(), window2.getContainer()));
            }
        }
    }
    
    private StickGroup getStickGroup(final Window w) {
        final WindowStickData stickData = w.getStickData();
        final int stickId = (stickData != null) ? stickData.getStickId() : Integer.MIN_VALUE;
        return this.m_stickGroups.get(stickId);
    }
    
    public void updateStickData(final Window w) {
        this.endWindowMove(w, w.getX(), w.getY());
    }
    
    public void onResize(final XulorScene xulorScene, final int deltaWidth, final int deltaHeight) {
        for (int i = 0, size = this.m_stickGroups.size(); i < size; ++i) {
            final StickGroup stickGroup = this.m_stickGroups.getQuickValue(i);
            final ArrayList<Window> windows = stickGroup.getWindows();
            if (windows.size() != 0) {
                int x = (int)((xulorScene.getFrustumWidth() - stickGroup.getWidth()) * stickGroup.getXPerc()) - stickGroup.getX();
                int y = (int)((xulorScene.getFrustumHeight() - stickGroup.getHeight()) * stickGroup.getYPerc()) - stickGroup.getY();
                final StickAlignment align = windows.get(0).getStickData().getAlign();
                switch (align) {
                    case NORTH_WEST:
                    case WEST:
                    case SOUTH_WEST: {
                        x = deltaWidth;
                        break;
                    }
                }
                switch (align) {
                    case SOUTH_WEST:
                    case SOUTH:
                    case SOUTH_EAST: {
                        y = deltaHeight;
                        break;
                    }
                }
                for (int j = 0, jSize = windows.size(); j < jSize; ++j) {
                    final Window window = windows.get(j);
                    window.setPosition(window.getX() + x, window.getY() + y, 0, false, false);
                }
                stickGroup.setPosition(stickGroup.m_x + x, stickGroup.m_y + y);
            }
        }
        for (int i = 0, size = this.m_windows.size(); i < size; ++i) {
            final Window window2 = this.m_windows.get(i);
            if (window2.getStickData().getStickId() == Integer.MIN_VALUE) {
                final int previousX = window2.getX();
                final int previousY = window2.getY();
                int x2 = (int)((xulorScene.getFrustumWidth() - window2.getWidth()) * window2.getXPercInParent()) - previousX;
                int y2 = (int)((xulorScene.getFrustumHeight() - window2.getHeight()) * window2.getYPercInParent()) - previousY;
                final StickAlignment align2 = window2.getStickData().getAlign();
                if (align2 != null) {
                    switch (align2) {
                        case NORTH_WEST:
                        case WEST:
                        case SOUTH_WEST: {
                            x2 = deltaWidth;
                            break;
                        }
                    }
                    switch (align2) {
                        case SOUTH_WEST:
                        case SOUTH:
                        case SOUTH_EAST: {
                            y2 = deltaHeight;
                            break;
                        }
                    }
                    window2.setPosition(previousX + x2, previousY + y2, 0, false, false);
                }
            }
        }
    }
    
    public boolean hasStickData(final String elementMapId) {
        final PreferenceStore store = Xulor.getInstance().getPreferenceStore(elementMapId);
        return store != null && store.contains("stickData" + elementMapId);
    }
    
    public boolean isVerticalData(final String elementMapId) {
        final PreferenceStore store = Xulor.getInstance().getPreferenceStore(elementMapId);
        return store != null && store.getBoolean("stickData" + elementMapId);
    }
    
    public void saveWindowData(final Window w) {
        final String id = w.getElementMap().getId();
        final PreferenceStore preferenceStore = Xulor.getInstance().getPreferenceStore(id);
        if (preferenceStore != null) {
            preferenceStore.setValue("stickData" + w.getHorizontalDialog(), id.startsWith(w.getVerticalDialog()));
        }
    }
    
    static {
        m_instance = new WindowStickManager();
        WindowStickManager.CURRENT_ID = -2147483647;
        COPY_WINDOWS = new ArrayList<Window>();
    }
    
    private static class StickGroup
    {
        private final ArrayList<Window> m_windows;
        private int m_x;
        private int m_y;
        private int m_width;
        private int m_height;
        private float m_xPerc;
        private float m_yPerc;
        
        private StickGroup() {
            super();
            this.m_windows = new ArrayList<Window>();
        }
        
        public void add(final Window w) {
            this.m_windows.add(w);
        }
        
        public void remove(final Window w) {
            this.m_windows.remove(w);
        }
        
        public boolean isEmpty() {
            return this.m_windows.isEmpty();
        }
        
        public Window getFirst() {
            if (this.m_windows.isEmpty()) {
                return null;
            }
            return this.m_windows.get(0);
        }
        
        public ArrayList<Window> getWindows() {
            return this.m_windows;
        }
        
        public void setPosition(final int x, final int y) {
            this.m_x = x;
            this.m_y = y;
        }
        
        public void recomputeRelativePosition() {
            if (this.m_windows.size() == 0) {
                return;
            }
            boolean init = false;
            int x1 = 0;
            int x2 = 0;
            int y1 = 0;
            int y2 = 0;
            Container container = null;
            for (int i = this.m_windows.size() - 1; i >= 0; --i) {
                final Window w = this.m_windows.get(i);
                if (container == null) {
                    container = w.getContainer();
                }
                if (!init) {
                    init = true;
                    x1 = w.getX();
                    x2 = w.getX() + w.getWidth();
                    y1 = w.getY();
                    y2 = w.getY() + w.getHeight();
                }
                else {
                    x1 = Math.min(x1, w.getX());
                    x2 = Math.max(x2, w.getX() + w.getWidth());
                    y1 = Math.min(y1, w.getY());
                    y2 = Math.max(y2, w.getY() + w.getHeight());
                }
            }
            if (container == null) {
                return;
            }
            this.m_x = x1;
            this.m_y = y1;
            this.m_width = x2 - x1;
            this.m_height = y2 - y1;
            this.m_xPerc = this.m_x / (container.getWidth() - this.m_width);
            this.m_yPerc = this.m_y / (container.getHeight() - this.m_height);
        }
        
        public int getX() {
            return this.m_x;
        }
        
        public int getY() {
            return this.m_y;
        }
        
        public int getWidth() {
            return this.m_width;
        }
        
        public int getHeight() {
            return this.m_height;
        }
        
        public float getXPerc() {
            return this.m_xPerc;
        }
        
        public float getYPerc() {
            return this.m_yPerc;
        }
    }
    
    public enum StickAlignment
    {
        NONE(false, false), 
        NORTH(false, true), 
        NORTH_WEST(true, true), 
        NORTH_EAST(true, true), 
        SOUTH(false, true), 
        SOUTH_WEST(true, true), 
        SOUTH_EAST(true, true), 
        WEST(true, false), 
        EAST(true, false);
        
        private boolean m_horizontal;
        private boolean m_vertical;
        
        private StickAlignment(final boolean horizontal, final boolean vertical) {
            this.m_horizontal = horizontal;
            this.m_vertical = vertical;
        }
        
        public boolean isHorizontal() {
            return this.m_horizontal;
        }
        
        public boolean isVertical() {
            return this.m_vertical;
        }
        
        private static StickAlignment getGenericStickAlignment(final boolean stuckNorth, final boolean stuckSouth, final boolean stuckWest, final boolean stuckEast) {
            if (stuckNorth) {
                if (stuckWest) {
                    return StickAlignment.NORTH_WEST;
                }
                if (stuckEast) {
                    return StickAlignment.NORTH_EAST;
                }
                return StickAlignment.NORTH;
            }
            else if (stuckSouth) {
                if (stuckWest) {
                    return StickAlignment.SOUTH_WEST;
                }
                if (stuckEast) {
                    return StickAlignment.SOUTH_EAST;
                }
                return StickAlignment.SOUTH;
            }
            else {
                if (stuckWest) {
                    return StickAlignment.WEST;
                }
                if (stuckEast) {
                    return StickAlignment.EAST;
                }
                return StickAlignment.NONE;
            }
        }
        
        public static StickAlignment getBorderStickAlignment(final int x1, final int y1, final int width, final int height, final Container parent) {
            return getGenericStickAlignment(isStuckNorth(0, 0, y1), isStuckSouth(parent.getHeight(), y1, height), isStuckWest(parent.getWidth(), x1, width), isStuckEast(0, 0, x1));
        }
        
        public static StickAlignment getBorderStickAlignment(final int x1, final int y1, final Window w, final Container parent) {
            return getGenericStickAlignment(isStuckNorth(0, 0, y1), isStuckSouth(parent.getHeight(), y1, w.getHeight()), isStuckWest(parent.getWidth(), x1, w.getWidth()), isStuckEast(0, 0, x1));
        }
        
        public static StickAlignment getStickAlignment(final int x1, final int y1, final Window w1, final Window w2) {
            return getStickAlignment(x1, y1, w1.getWidth(), w1.getHeight(), w2.getX(), w2.getY(), w2.getWidth(), w2.getHeight());
        }
        
        public static StickAlignment getStickAlignment(final int x1, final int y1, final int width1, final int height1, final int x2, final int y2, final int width2, final int height2) {
            return getGenericStickAlignment(isStuckNorth(y2, height2, y1) && collidesInX(x1, width1, x2, width2), isStuckSouth(y2, y1, height1) && collidesInX(x1, width1, x2, width2), isStuckWest(x2, x1, width1) && collidesInY(y1, height1, y2, height2), isStuckEast(x2, width2, x1) && collidesInY(y1, height1, y2, height2));
        }
        
        public int getBorderX(final int x, final Window w) {
            switch (this) {
                case SOUTH_EAST:
                case NORTH_EAST:
                case EAST: {
                    return 0;
                }
                case NORTH_WEST:
                case WEST:
                case SOUTH_WEST: {
                    return w.getContainer().getWidth() - w.getWidth();
                }
                default: {
                    return x;
                }
            }
        }
        
        public int getBorderY(final int y, final Window w) {
            switch (this) {
                case NORTH_WEST:
                case NORTH_EAST:
                case NORTH: {
                    return 0;
                }
                case SOUTH_WEST:
                case SOUTH:
                case SOUTH_EAST: {
                    return w.getContainer().getHeight() - w.getHeight();
                }
                default: {
                    return y;
                }
            }
        }
        
        public int getX(final int x, final Window w1, final Window w2) {
            switch (this) {
                case NORTH_WEST:
                case WEST:
                case SOUTH_WEST: {
                    return w1.getX() - w2.getWidth();
                }
                case SOUTH_EAST:
                case NORTH_EAST:
                case EAST: {
                    return w1.getX() + w1.getWidth();
                }
                default: {
                    return x;
                }
            }
        }
        
        public int getY(final int y, final Window w1, final Window w2) {
            switch (this) {
                case SOUTH_WEST:
                case SOUTH:
                case SOUTH_EAST: {
                    return w1.getY() - w2.getHeight();
                }
                case NORTH_WEST:
                case NORTH_EAST:
                case NORTH: {
                    return w1.getY() + w1.getHeight();
                }
                default: {
                    return y;
                }
            }
        }
        
        private static boolean isStuckNorth(final int y1, final int height1, final int y2) {
            return Math.abs(y1 + height1 - y2) < 20;
        }
        
        private static boolean isStuckSouth(final int y1, final int y2, final int height2) {
            return Math.abs(y2 + height2 - y1) < 20;
        }
        
        private static boolean isStuckEast(final int x1, final int width1, final int x2) {
            return Math.abs(x1 + width1 - x2) < 20;
        }
        
        private static boolean isStuckWest(final int x1, final int x2, final int width2) {
            return Math.abs(x2 + width2 - x1) < 20;
        }
        
        private static boolean collidesInX(final int x1, final int width1, final int x2, final int width2) {
            return (x1 <= x2 && x1 + width1 > x2) || (x2 <= x1 && x2 + width2 > x1);
        }
        
        private static boolean collidesInY(final int y1, final int height1, final int y2, final int height2) {
            return (y1 <= y2 && y1 + height1 > y2) || (y2 <= y1 && y2 + height2 > y1);
        }
    }
}
