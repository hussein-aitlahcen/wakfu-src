package com.ankamagames.xulor2.component.colorpicker;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import java.awt.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;

public class BasicColorPicker extends AbstractColorPicker
{
    public static final String TAG = "basicColorPicker";
    private ArrayList<Button> m_buttons;
    private ArrayList<Container> m_containers;
    private ArrayList<PlainBackground> m_bgs;
    private boolean m_horizontal;
    private int m_numByLines;
    private EventListener m_clickListener;
    public static final int HORIZONTAL_HASH;
    public static final int NUM_BY_LINES_HASH;
    
    public BasicColorPicker() {
        super();
        this.m_horizontal = true;
        this.m_numByLines = 0;
    }
    
    @Override
    public String getTag() {
        return "basicColorPicker";
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        if (this.m_horizontal == horizontal) {
            return;
        }
        this.m_horizontal = horizontal;
        this.invalidateMinSize();
    }
    
    public int getNumByLines() {
        return this.m_numByLines;
    }
    
    public void setNumByLines(final int numByLines) {
        if (this.m_numByLines == numByLines) {
            return;
        }
        this.m_numByLines = numByLines;
        this.invalidateMinSize();
    }
    
    @Override
    protected void onColorsChanged() {
        final int buttonsSize = this.m_buttons.size();
        final int colorsSize = this.m_colors.size();
        for (int i = buttonsSize; i < colorsSize; ++i) {
            final Container c = Container.checkOut();
            c.getAppearance().setMargin(new Insets(1, 1, 0, 0));
            final StaticLayout sl = new StaticLayout();
            sl.onCheckOut();
            sl.setAdaptToContentSize(true);
            c.add(sl);
            final Container bgc = Container.checkOut();
            final StaticLayoutData sld1 = new StaticLayoutData();
            sld1.onCheckOut();
            sld1.setSize(new Dimension(100.0f, 100.0f));
            bgc.add(sld1);
            final PlainBackground pb = new PlainBackground();
            pb.onCheckOut();
            bgc.getAppearance().add(pb);
            bgc.getAppearance().setMargin(new Insets(1, 1, 1, 1));
            final Button b = new Button();
            b.onCheckOut();
            b.setElementMap(this.m_elementMap);
            final String style = this.getStyle();
            b.setStyle("basicColorPicker" + ((style != null) ? style : "") + "$button");
            b.setPrefSize(new Dimension(20, 15));
            final StaticLayoutData sld2 = new StaticLayoutData();
            sld2.onCheckOut();
            sld2.setSize(new Dimension(100.0f, 100.0f));
            b.add(sld2);
            b.onChildrenAdded();
            c.add(bgc);
            c.add(b);
            this.add(c);
            this.m_buttons.add(b);
            this.m_bgs.add(pb);
            this.m_containers.add(c);
        }
        for (int i = colorsSize; i < buttonsSize; ++i) {
            this.m_buttons.remove(this.m_buttons.size() - 1);
            this.m_bgs.remove(this.m_bgs.size() - 1);
            final Container c = this.m_containers.remove(this.m_containers.size() - 1);
            c.destroySelfFromParent();
        }
        if (buttonsSize != colorsSize) {
            this.invalidateMinSize();
        }
        assert this.m_buttons.size() == this.m_colors.size() : "m_buttons devrait avoir la m\u00eame taille que m_colors";
        for (int i = 0; i < colorsSize; ++i) {
            final PlainBackground plainBackground = this.m_bgs.get(i);
            plainBackground.setColor(this.m_colors.get(i));
        }
    }
    
    private void registerListeners() {
        this.m_clickListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final Button b = event.getTarget();
                final int index = BasicColorPicker.this.m_buttons.indexOf(b);
                if (index >= 0 && index < BasicColorPicker.this.m_colors.size()) {
                    final Color c = BasicColorPicker.this.m_colors.get(index);
                    final ItemEvent itemEvent = ItemEvent.checkOut((MouseEvent)event, BasicColorPicker.this, Events.ITEM_CLICK, c);
                    BasicColorPicker.this.dispatchEvent(itemEvent);
                    event.setSoundConsumed(itemEvent.isSoundConsumed());
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        final BasicColorPicker e = (BasicColorPicker)b;
        super.copyElement(e);
        e.m_numByLines = this.m_numByLines;
        e.m_horizontal = this.m_horizontal;
        e.removeEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_buttons = null;
        this.m_bgs = null;
        this.m_containers = null;
        this.m_clickListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final BasicColorPickerLayout layout = new BasicColorPickerLayout();
        layout.onCheckOut();
        this.add(layout);
        this.m_numByLines = 0;
        this.m_horizontal = true;
        this.m_buttons = new ArrayList<Button>();
        this.m_bgs = new ArrayList<PlainBackground>();
        this.m_containers = new ArrayList<Container>();
        this.registerListeners();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == BasicColorPicker.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != BasicColorPicker.NUM_BY_LINES_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setNumByLines(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == BasicColorPicker.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != BasicColorPicker.NUM_BY_LINES_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setNumByLines(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    static {
        HORIZONTAL_HASH = "horizontal".hashCode();
        NUM_BY_LINES_HASH = "numByLines".hashCode();
    }
    
    private class BasicColorPickerLayout extends AbstractLayoutManager
    {
        private int getFirstConstraint() {
            if (BasicColorPicker.this.m_numByLines <= 0) {
                return (int)Math.round(Math.sqrt(BasicColorPicker.this.m_containers.size()));
            }
            return BasicColorPicker.this.m_numByLines;
        }
        
        private int getSecondConstraint(final int firstConstraint) {
            return (int)Math.ceil(BasicColorPicker.this.m_containers.size() / firstConstraint);
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            if (BasicColorPicker.this.m_containers.size() == 0) {
                return new Dimension(0, 0);
            }
            int cols;
            int rows;
            if (BasicColorPicker.this.m_horizontal) {
                cols = this.getFirstConstraint();
                rows = this.getSecondConstraint(cols);
            }
            else {
                rows = this.getFirstConstraint();
                cols = this.getSecondConstraint(rows);
            }
            final Dimension minSize = BasicColorPicker.this.m_containers.get(0).getMinSize();
            minSize.setWidth(minSize.width * cols);
            minSize.setHeight(minSize.height * rows);
            return minSize;
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            if (BasicColorPicker.this.m_containers.size() == 0) {
                return new Dimension(0, 0);
            }
            int cols;
            int rows;
            if (BasicColorPicker.this.m_horizontal) {
                cols = this.getFirstConstraint();
                rows = this.getSecondConstraint(cols);
            }
            else {
                rows = this.getFirstConstraint();
                cols = this.getSecondConstraint(rows);
            }
            final Dimension prefSize = BasicColorPicker.this.m_containers.get(0).getPrefSize();
            prefSize.setWidth(prefSize.width * cols);
            prefSize.setHeight(prefSize.height * rows);
            return prefSize;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (BasicColorPicker.this.m_containers.size() == 0) {
                return;
            }
            int cols;
            int rows;
            if (BasicColorPicker.this.m_horizontal) {
                cols = this.getFirstConstraint();
                rows = this.getSecondConstraint(cols);
            }
            else {
                rows = this.getFirstConstraint();
                cols = this.getSecondConstraint(rows);
            }
            for (int i = 0, size = BasicColorPicker.this.m_containers.size(); i < size; ++i) {
                BasicColorPicker.this.m_containers.get(i).setSizeToPrefSize();
            }
            final int containersSize = BasicColorPicker.this.m_containers.size();
            final int width = BasicColorPicker.this.m_containers.get(0).getWidth();
            final int height = BasicColorPicker.this.m_containers.get(0).getHeight();
            int xOffset = 0;
            int yOffset = parent.getAppearance().getContentHeight() - height;
            Label_0371: {
                if (BasicColorPicker.this.m_horizontal) {
                    for (int y = 0; y < rows; ++y) {
                        for (int x = 0; x < cols; ++x) {
                            final int index = x + y * cols;
                            if (index >= containersSize) {
                                break Label_0371;
                            }
                            final Container container = BasicColorPicker.this.m_containers.get(index);
                            container.setPosition(xOffset, yOffset);
                            xOffset += width;
                        }
                        xOffset = 0;
                        yOffset -= height;
                    }
                }
                else {
                    for (int x2 = 0; x2 < cols; ++x2) {
                        for (int y2 = 0; y2 < rows; ++y2) {
                            final int index = x2 + y2 * cols;
                            if (index >= containersSize) {
                                break Label_0371;
                            }
                            final Container container = BasicColorPicker.this.m_containers.get(y2 + x2 * rows);
                            container.setPosition(xOffset, yOffset);
                            yOffset -= height;
                        }
                        yOffset = parent.getAppearance().getContentHeight() - height;
                        xOffset += width;
                    }
                }
            }
        }
    }
}
