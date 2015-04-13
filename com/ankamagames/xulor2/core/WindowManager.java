package com.ankamagames.xulor2.core;

import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import java.awt.*;

public class WindowManager
{
    private static final int BORDER_GAP = 10;
    private static final int CASCADE_GAP = 30;
    private RootContainer m_root;
    private HashMap<String, ArrayList<Widget>> m_controlGroups;
    private HashMap<String, Stack<Widget>> m_cascadeGroups;
    
    public WindowManager(final RootContainer rc) {
        super();
        this.m_controlGroups = new HashMap<String, ArrayList<Widget>>();
        this.m_cascadeGroups = new HashMap<String, Stack<Widget>>();
        this.m_root = rc;
    }
    
    public static int getX(final int x) {
        return Xulor.getInstance().getAppUI().getXPosOnScreen() + x;
    }
    
    public static int getY(final int y) {
        return Xulor.getInstance().getAppUI().getYPosOnScreen() + y;
    }
    
    public void clean() {
    }
    
    public void addWidgetToControlGroup(final Widget widget, final String controlGroupName) {
        ArrayList<Widget> widgets = this.m_controlGroups.get(controlGroupName);
        if (widgets != null) {
            if (!widgets.contains(widget)) {
                widgets.add(widget);
            }
        }
        else {
            widgets = new ArrayList<Widget>();
            widgets.add(widget);
            this.m_controlGroups.put(controlGroupName, widgets);
        }
    }
    
    public void removeWidgetFromControlGroup(final Widget widget, final String controlGroupName) {
        final ArrayList<Widget> widgets = this.m_controlGroups.get(controlGroupName);
        if (widgets != null) {
            widgets.remove(widget);
            if (widgets.size() <= 0) {
                this.m_controlGroups.remove(controlGroupName);
            }
        }
    }
    
    public void addWidgetToCascadeGroup(final Widget widget, final String cascadeGroupName) {
        Stack<Widget> widgets = this.m_cascadeGroups.get(cascadeGroupName);
        if (widgets != null) {
            if (widgets.contains(widget)) {
                widgets.remove(widget);
            }
            widgets.push(widget);
        }
        else {
            widgets = new Stack<Widget>();
            widgets.push(widget);
            this.m_cascadeGroups.put(cascadeGroupName, widgets);
        }
    }
    
    public void removeWidgetFromCascadeGroup(final Widget widget, final String cascadeGroupName) {
        final Stack<Widget> widgets = this.m_cascadeGroups.get(cascadeGroupName);
        if (widgets != null) {
            widgets.remove(widget);
            if (widgets.size() <= 0) {
                this.m_cascadeGroups.remove(cascadeGroupName);
            }
        }
    }
    
    public Point provideFreeWindowPosition(final Widget referentWidget, final Widget widgetToInsert) {
        final int xOrigin = referentWidget.getDisplayX();
        final int yOrigin = referentWidget.getDisplayY();
        Rectangle rectangle = new Rectangle(xOrigin + referentWidget.getWidth() + 10, yOrigin, widgetToInsert.getWidth(), widgetToInsert.getHeight(), null);
        boolean isValidPosition = rectangle.x + rectangle.width <= this.m_root.getWidth() && this.isValidPosition(rectangle, widgetToInsert);
        if (!isValidPosition) {
            rectangle = new Rectangle(xOrigin, yOrigin + referentWidget.getHeight() + 10, widgetToInsert.getWidth(), widgetToInsert.getHeight(), null);
            isValidPosition = (rectangle.y + rectangle.height <= this.m_root.getHeight() && this.isValidPosition(rectangle, widgetToInsert));
        }
        if (!isValidPosition) {
            rectangle = new Rectangle(xOrigin - widgetToInsert.getWidth() - 10, yOrigin, widgetToInsert.getWidth(), widgetToInsert.getHeight(), null);
            isValidPosition = (rectangle.x > 0 && this.isValidPosition(rectangle, widgetToInsert));
        }
        if (!isValidPosition) {
            rectangle = new Rectangle(xOrigin, yOrigin - widgetToInsert.getHeight() - 10, widgetToInsert.getWidth(), widgetToInsert.getHeight(), null);
            isValidPosition = (rectangle.y > 0 && this.isValidPosition(rectangle, widgetToInsert));
        }
        if (isValidPosition) {
            return new Point(rectangle.x, rectangle.y);
        }
        return null;
    }
    
    public Point provideValidCascadeWindowPosition(final Widget widgetToInsert) {
        final Widget referentWidget = this.findLastWidgetLoaded(widgetToInsert);
        if (referentWidget != null) {
            final int xOrigin = referentWidget.getDisplayX();
            final int yOrigin = referentWidget.getDisplayY();
            final Rectangle rectangle = new Rectangle(xOrigin + 30, yOrigin - (widgetToInsert.getHeight() - referentWidget.getHeight()) - 30, widgetToInsert.getWidth(), widgetToInsert.getHeight(), null);
            if (rectangle.y <= 0) {
                rectangle.y = 0;
            }
            if (rectangle.x + rectangle.width > this.m_root.getWidth()) {
                rectangle.x = this.m_root.getWidth() - widgetToInsert.getWidth();
            }
            return new Point(rectangle.x, rectangle.y);
        }
        return widgetToInsert.getPosition();
    }
    
    public boolean isValidPosition(final Rectangle rectangle, final Widget widgetToInsert) {
        final Container parent = this.m_root.getLayeredContainer().getContainerFromWidget(widgetToInsert);
        if (parent == null) {
            return false;
        }
        final ArrayList<Widget> list = parent.getWidgetChildren();
        for (int i = 0, size = list.size(); i < size; ++i) {
            final Widget widget = list.get(i);
            if (widget != widgetToInsert && this.belongToControlGroup(widget) && (widget.getWidth() <= 1000 || widget.getHeight() <= 700 || widget.getX() != 0 || widget.getY() != 0) && this.rectIntersects(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), (int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight())) {
                return false;
            }
        }
        return true;
    }
    
    private boolean rectIntersects(final int x1, final int y1, int w1, int h1, final int x2, final int y2, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0 || w1 <= 0 || h1 <= 0) {
            return false;
        }
        w2 += x2;
        h2 += y2;
        w1 += x1;
        h1 += y1;
        return (w2 < x2 || w2 > x1) && (h2 < y2 || h2 > y1) && (w1 < x1 || w1 > x2) && (h1 < y1 || h1 > y2);
    }
    
    public boolean belongToControlGroup(final Widget element) {
        if (element.getElementMap() != null) {
            final String elementMapId = element.getElementMap().getId();
            for (final String key : this.m_controlGroups.keySet()) {
                if (elementMapId.startsWith(key)) {
                    return this.m_controlGroups.get(key).contains(element);
                }
            }
        }
        return false;
    }
    
    public Widget findLastWidgetLoaded(final EventDispatcher element) {
        if (element.getElementMap() != null) {
            final String elementMapId = element.getElementMap().getId();
            for (final String key : this.m_cascadeGroups.keySet()) {
                if (elementMapId.startsWith(key) && this.m_cascadeGroups.get(key).size() > 1) {
                    final Stack<Widget> widgetsStack = this.m_cascadeGroups.get(key);
                    for (final Widget widget : widgetsStack) {
                        if (widget == element) {
                            final int index = widgetsStack.indexOf(widget) - 1;
                            return (index < 0) ? null : widgetsStack.get(index);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public void recomputeGroup(final String controlGroup, final Widget referentWidget) {
        final ArrayList<Widget> oldList = this.m_controlGroups.get(controlGroup);
        if (oldList == null) {
            return;
        }
        if (referentWidget != null) {
            this.layoutWidgets(oldList, referentWidget);
        }
    }
    
    private void layoutWidgets(final ArrayList<Widget> widgets, final Widget referentWidget) {
        int height = referentWidget.getHeight();
        int width = referentWidget.getWidth();
        if (height == 0) {
            height = referentWidget.getPrefSize().height;
        }
        if (width == 0) {
            width = referentWidget.getPrefSize().width;
        }
        final Rectangle[] workAreas = this.getSortedWorkSpaces(referentWidget.getX(), referentWidget.getY(), width, height, this.m_root.getWidth(), this.m_root.getHeight());
        int currentIndex = 0;
        final int size = widgets.size();
        for (final Rectangle rect : workAreas) {
            if (currentIndex >= size) {
                break;
            }
            currentIndex += this.fillAreaWithWidgets(rect, widgets, currentIndex);
        }
        if (currentIndex < size) {
            for (int i = currentIndex; i < size; ++i) {
                final Widget w = widgets.get(i);
                if (w != referentWidget) {
                    w.setPosition(this.provideValidCascadeWindowPosition(w));
                }
            }
        }
    }
    
    private int fillAreaWithWidgets(final Rectangle area, final ArrayList<Widget> list, int offset) {
        if (list.size() == 0 || offset < 0 || offset >= list.size()) {
            return 0;
        }
        int packed = 0;
        Widget w = null;
        for (int i = offset, size = list.size(); i < size; ++i) {
            final Widget w2 = list.get(i);
            if (w2.getWidth() != 0 && w2.getHeight() != 0 && w2.getVisible()) {
                w = w2;
                break;
            }
            ++packed;
            ++offset;
        }
        if (w == null) {
            return packed;
        }
        int lastX = area.getAlignX(w.getWidth());
        int lastY = area.getAlignY(w.getHeight());
        final int deltaXOffScreen = Integer.MAX_VALUE;
        final int deltaYOffScreen = Integer.MAX_VALUE;
        while (area.isInsideX(lastX)) {
            while (area.isInsideY(lastY)) {
                if (this.isPositionValid((int)area.getX(), (int)area.getY(), (int)area.getWidth(), (int)area.getHeight(), lastX, lastY, w.getWidth(), w.getHeight())) {
                    w.setPosition(lastX, lastY);
                    ++packed;
                    ++offset;
                    lastY += area.getDeltaY(w.getHeight());
                    for (int j = offset, size2 = list.size(); j < size2; ++j) {
                        final Widget w3 = list.get(j);
                        if (w3.getWidth() != 0 && w3.getHeight() != 0 && w3.getVisible()) {
                            w = w3;
                            break;
                        }
                        ++packed;
                        ++offset;
                    }
                    if (offset == list.size()) {
                        return packed;
                    }
                    continue;
                }
                else {
                    final int xOffScreen = (lastX < 0) ? Math.abs(lastX) : ((lastX + w.getWidth() > this.m_root.getWidth()) ? (lastX + w.getWidth() - this.m_root.getWidth()) : -1);
                    final int yOffScreen = (lastY < 0) ? Math.abs(lastY) : ((lastY + w.getHeight() > this.m_root.getHeight()) ? (lastY + w.getHeight() - this.m_root.getHeight()) : -1);
                    if (xOffScreen != -1 && yOffScreen != -1 && xOffScreen < deltaXOffScreen && yOffScreen < deltaYOffScreen) {
                        ++packed;
                        break;
                    }
                    break;
                }
            }
            lastY = area.getAlignY(w.getHeight());
            lastX += area.getDeltaX(w.getWidth());
        }
        return packed;
    }
    
    private boolean isPositionValid(final int rectX, final int rectY, final int rectWidth, final int rectHeight, final int x, final int y, final int width, final int height) {
        return this.intersects(rectX, rectY, rectWidth, rectHeight, x, y, width, height) && this.isCompletlyOnScreen(x, y, width, height);
    }
    
    private boolean intersects(final int rectX, final int rectY, final int rectWidth, final int rectHeight, final int x, final int y, final int width, final int height) {
        return this.intersectsHorizontaly(rectX, rectWidth, x, width) && this.intersectsVerticaly(rectY, rectHeight, y, height);
    }
    
    private boolean intersectsVerticaly(final int rectY, final int rectHeight, final int y, final int height) {
        return y < rectY + rectHeight && y + height > rectY;
    }
    
    private boolean intersectsHorizontaly(final int rectX, final int rectWidth, final int x, final int width) {
        return x < rectX + rectWidth && x + width > rectX;
    }
    
    private boolean isCompletlyOnScreen(final int x, final int y, final int width, final int height) {
        return x >= 0 && y >= 0 && x + width < this.m_root.getWidth() && y + height < this.m_root.getHeight();
    }
    
    private Rectangle[] getSortedWorkSpaces(final int originX, final int originY, final int width, final int height, final int totalWidth, final int totalHeight) {
        final Rectangle[] rectangles = { new Rectangle(originX, originY + height, width, totalHeight - originY - height, Alignment4.SOUTH), new Rectangle(originX, 0, width, originY, Alignment4.NORTH), new Rectangle(0, originY, originX, height, Alignment4.EAST), new Rectangle(originX + width, originY, totalWidth - originX - width, height, Alignment4.WEST) };
        Arrays.sort(rectangles, RectangleAreaComparator.COMPARATOR);
        return rectangles;
    }
    
    private static class RectangleAreaComparator implements Comparator<Rectangle>
    {
        public static RectangleAreaComparator COMPARATOR;
        
        @Override
        public int compare(final Rectangle o1, final Rectangle o2) {
            return (int)(o2.getHeight() * o2.getWidth() - o1.getHeight() * o1.getWidth());
        }
        
        static {
            RectangleAreaComparator.COMPARATOR = new RectangleAreaComparator();
        }
    }
    
    private static class Rectangle extends java.awt.Rectangle
    {
        private final Alignment4 m_align;
        
        public Rectangle(final int x, final int y, final int width, final int height, final Alignment4 align) {
            super(x, y, width, height);
            this.m_align = align;
        }
        
        public Alignment4 getAlign() {
            return this.m_align;
        }
        
        public int getAlignX(final int width) {
            int ret = (int)this.getX();
            if (this.m_align == Alignment4.EAST) {
                ret += (int)(this.getWidth() - width);
            }
            return ret;
        }
        
        public int getAlignY(final int height) {
            int ret = (int)this.getY();
            if (this.m_align == Alignment4.NORTH) {
                ret += (int)(this.getHeight() - height);
            }
            return ret;
        }
        
        public boolean isInsideX(final int x) {
            if (this.m_align == Alignment4.EAST) {
                return x > this.getX();
            }
            return x < this.getX() + this.getWidth();
        }
        
        public boolean isInsideY(final int y) {
            if (this.m_align == Alignment4.NORTH) {
                return y > this.getY();
            }
            return y < this.getY() + this.getHeight();
        }
        
        public int getDeltaX(final int x) {
            if (this.m_align == Alignment4.EAST) {
                return -x;
            }
            return x;
        }
        
        public int getDeltaY(final int y) {
            if (this.m_align == Alignment4.NORTH) {
                return -y;
            }
            return y;
        }
    }
}
