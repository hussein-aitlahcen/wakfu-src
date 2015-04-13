package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.*;
import java.awt.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;

public class CalendarWidget extends Container implements ContentClient, FontClient, ColorClient, Alignment9Client
{
    public static final String SELECTION_COLOR = "selection";
    public static final String TAG = "calendar";
    private static int DAYS_IN_WEEK;
    private static int MAX_DAYS_IN_MONTH;
    private ArrayList<DateComponent> m_content;
    private ArrayList<DateCell> m_cells;
    private ItemRendererManager m_rendererManager;
    private Calendar m_calendar;
    private int m_vgap;
    private int m_hgap;
    private int m_columnCount;
    private int m_rowCount;
    private Dimension m_cellSize;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    private boolean m_calendarIsDirty;
    private boolean m_valuesAreDirty;
    private int m_mouseOverRenderableIndex;
    private int m_selectedRenderableIndex;
    private Insets m_dateMargin;
    private Alignment9 m_labelAlign;
    private TextRenderer m_labelTextRenderer;
    private Color m_labelColor;
    private ListOverMesh m_selectedMesh;
    public static final int CELL_SIZE_HASH;
    public static final int CONTENT_HASH;
    public static final int HGAP_HASH;
    public static final int VGAP_HASH;
    public static final int DATE_MARGIN_HASH;
    public static final int CALENDAR_HASH;
    
    public CalendarWidget() {
        super();
        this.m_cells = new ArrayList<DateCell>();
        this.m_rendererManager = new ItemRendererManager();
        this.m_calendar = new GregorianCalendar();
        this.m_vgap = 0;
        this.m_hgap = 0;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_mouseOverRenderableIndex = -1;
        this.m_selectedRenderableIndex = -1;
        this.m_dateMargin = null;
        this.m_labelAlign = Alignment9.SOUTH_EAST;
        this.m_labelTextRenderer = null;
        this.m_labelColor = null;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof ItemRenderer) {
            this.m_rendererManager.addRenderer((ItemRenderer)e);
        }
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        if (this.m_selectedMesh != null && this.m_selectedRenderableIndex != -1) {
            this.m_entity.addChild(this.m_selectedMesh.getEntity());
        }
    }
    
    @Override
    public String getTag() {
        return "calendar";
    }
    
    public Calendar getCalendar() {
        return this.m_calendar;
    }
    
    public void setCalendar(final Calendar calendar) {
        this.m_calendar = calendar;
        this.m_calendarIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setCellSize(final Dimension cellSize) {
        this.m_cellSize = cellSize;
        this.m_calendarIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Dimension getCellSize() {
        return this.m_cellSize;
    }
    
    public int getVgap() {
        return this.m_vgap;
    }
    
    public void setVgap(final int vgap) {
        this.m_vgap = vgap;
        this.m_calendarIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public int getHgap() {
        return this.m_hgap;
    }
    
    public void setHgap(final int hgap) {
        this.m_hgap = hgap;
        this.m_calendarIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap elementMap) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = elementMap;
    }
    
    public void setContent(final DateComponent[] content) {
        if (content != null) {
            this.m_content = new ArrayList<DateComponent>(content.length);
            for (final DateComponent value : content) {
                this.m_content.add(value);
            }
        }
        else {
            this.m_content = new ArrayList<DateComponent>(0);
        }
        this.m_valuesAreDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setContent(final Iterable<? extends DateComponent> content) {
        if (content != null) {
            this.m_content = new ArrayList<DateComponent>();
            final Iterator<? extends DateComponent> it = content.iterator();
            while (it.hasNext()) {
                this.m_content.add((DateComponent)it.next());
            }
        }
        else {
            this.m_content = new ArrayList<DateComponent>(0);
        }
        this.m_valuesAreDirty = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public void setFont(final TextRenderer renderer) {
        if (renderer == this.m_labelTextRenderer) {
            return;
        }
        this.m_labelTextRenderer = renderer;
        for (int i = 0, size = this.m_cells.size(); i < size; ++i) {
            this.m_cells.get(i).getLabel().setFont(renderer);
        }
    }
    
    @Override
    public void setColor(final Color c, final String name) {
        if (name == null || name.equals("text")) {
            if (c == this.m_labelColor) {
                return;
            }
            this.m_labelColor = c;
            for (int i = 0, size = this.m_cells.size(); i < size; ++i) {
                this.m_cells.get(i).getLabel().setColor(c, "text");
            }
        }
        else if (name.equals("selection")) {
            if (c != null) {
                if (this.m_selectedMesh == null) {
                    (this.m_selectedMesh = new ListOverMesh()).onCheckOut();
                    this.setNeedsToResetMeshes();
                }
                this.m_selectedMesh.setColor(c);
            }
            else {
                if (this.m_selectedMesh != null) {
                    this.m_selectedMesh.onCheckIn();
                }
                this.m_selectedMesh = null;
                this.setNeedsToResetMeshes();
            }
        }
    }
    
    @Override
    public void setAlign(final Alignment9 align) {
        if (this.m_labelAlign == align) {
            return;
        }
        this.m_labelAlign = align;
        for (int i = 0, size = this.m_cells.size(); i < size; ++i) {
            this.m_cells.get(i).getLabel().setAlign(align);
        }
    }
    
    public Insets getDateMargin() {
        return this.m_dateMargin;
    }
    
    public void setDateMargin(final Insets dateMargin) {
        this.m_dateMargin = dateMargin;
    }
    
    public int getYearOver() {
        return this.m_calendar.get(1);
    }
    
    public int getMonthOver() {
        return this.m_calendar.get(2) + 1;
    }
    
    public int getDayOver() {
        if (this.m_selectedRenderableIndex != -1) {
            return this.m_selectedRenderableIndex + 1;
        }
        return -1;
    }
    
    private void setSelectedDate(final int day) {
        this.m_selectedRenderableIndex = day - 1;
        this.m_calendar.set(5, day);
        if (this.m_selectedMesh != null) {
            final Container c = this.m_cells.get(this.m_selectedRenderableIndex).getContainer();
            this.m_selectedMesh.setPositionSize(c.getPosition(), c.getSize(), this.getAppearance().getTotalInsets());
            this.setNeedsToResetMeshes();
        }
    }
    
    private void updateCells() {
        final int monthDuration = this.m_calendar.getActualMaximum(5);
        final int size = this.m_cells.size();
        if (monthDuration > size) {
            for (int i = size; i < monthDuration; ++i) {
                final DateCell cell = new DateCell();
                final RenderableContainer renderable = new RenderableContainer();
                renderable.onCheckOut();
                renderable.setNonBlocking(this.m_nonBlocking);
                renderable.setRendererManager(this.m_rendererManager);
                final Container c = Container.checkOut();
                final StaticLayout sl = new StaticLayout();
                sl.onCheckOut();
                c.add(sl);
                c.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        CalendarWidget.this.m_mouseOverRenderableIndex = CalendarWidget.this.m_cells.indexOf(cell);
                        return false;
                    }
                }, false);
                c.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        CalendarWidget.this.m_mouseOverRenderableIndex = -1;
                        return false;
                    }
                }, false);
                c.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        CalendarWidget.this.setSelectedDate(CalendarWidget.this.m_cells.indexOf(cell) + 1);
                        return false;
                    }
                }, false);
                StaticLayoutData sld = new StaticLayoutData();
                sld.onCheckOut();
                sld.setSize(new Dimension(100.0f, 100.0f));
                renderable.add(sld);
                final Label l = new Label();
                l.onCheckOut();
                l.setNonBlocking(true);
                l.setFont(this.m_labelTextRenderer);
                l.setColor(this.m_labelColor, "text");
                l.setAlign(this.m_labelAlign);
                l.setText(String.valueOf(i + 1));
                sld = new StaticLayoutData();
                sld.onCheckOut();
                sld.setSize(new Dimension(100.0f, 100.0f));
                l.add(sld);
                if (this.m_dateMargin != null) {
                    final DecoratorAppearance app = l.getAppearance();
                    final Margin margin = Margin.checkOut();
                    margin.setInsets(this.m_dateMargin);
                    app.add(margin);
                }
                c.add(renderable);
                c.add(l);
                cell.setRenderable(renderable);
                cell.setContainer(c);
                cell.setLabel(l);
                this.m_cells.add(cell);
                this.add(c);
            }
        }
        else if (monthDuration < size) {
            for (int i = monthDuration; i < size; ++i) {
                final DateCell cell = this.m_cells.remove(this.m_cells.size() - 1);
                cell.getContainer().destroySelfFromParent();
            }
            if (this.m_mouseOverRenderableIndex >= this.m_cells.size()) {
                this.m_mouseOverRenderableIndex = -1;
            }
        }
        this.setSelectedDate(this.m_calendar.get(5));
    }
    
    public void updateValues() {
        final int[] ids = new int[this.m_content.size()];
        for (int i = 0, size = this.m_content.size(); i < size; ++i) {
            final DateComponent dateComponent = this.m_content.get(i);
            ids[i] = dateComponent.getDayInMonth();
            final int cellIndex = dateComponent.getDayInMonth() - 1;
            final RenderableContainer container = this.m_cells.get(cellIndex).getRenderable();
            final Object value = dateComponent.getContent();
            container.setContentProperty(this.m_contentProperty + "#" + cellIndex, this.m_contentPropertyElementMap);
            container.setContent(value);
        }
        int cellIndex2 = 0;
        final int cellSize = this.m_cells.size();
        int day = 0;
        for (int j = 0, size2 = this.m_content.size(); j < size2; ++j) {
            for (day = this.m_content.get(j).getDayInMonth() - 1; cellIndex2 < day && cellIndex2 < cellSize; ++cellIndex2) {
                final RenderableContainer renderable = this.m_cells.get(cellIndex2).getRenderable();
                renderable.setContentProperty(this.m_contentProperty + "#" + cellIndex2, this.m_contentPropertyElementMap);
                renderable.setContent(null);
            }
            cellIndex2 = day + 1;
        }
        final int incr = (this.m_content.size() != 0) ? 1 : 0;
        for (int k = day + incr, size3 = this.m_cells.size(); k < size3; ++k) {
            final RenderableContainer renderable2 = this.m_cells.get(k).getRenderable();
            renderable2.setContentProperty(this.m_contentProperty + "#" + k, this.m_contentPropertyElementMap);
            renderable2.setContent(null);
        }
    }
    
    private void computeRowsAndColumnsCount() {
        this.m_columnCount = CalendarWidget.DAYS_IN_WEEK;
        final int numFullWeeks = CalendarWidget.MAX_DAYS_IN_MONTH / CalendarWidget.DAYS_IN_WEEK;
        int rem = CalendarWidget.MAX_DAYS_IN_MONTH - numFullWeeks * CalendarWidget.DAYS_IN_WEEK;
        if (rem > 1) {
            rem = 2;
        }
        this.m_rowCount = numFullWeeks + rem;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        final boolean invalidate = false;
        if (this.m_calendarIsDirty) {
            this.updateCells();
        }
        if (this.m_valuesAreDirty) {
            this.updateValues();
        }
        if (this.m_calendarIsDirty) {
            this.invalidateMinSize();
        }
        this.m_calendarIsDirty = false;
        this.m_valuesAreDirty = false;
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final CalendarWidget e = (CalendarWidget)c;
        super.copyElement(e);
        e.setAlign(this.m_labelAlign);
        e.setCellSize((Dimension)this.m_cellSize.clone());
        e.setCalendar(this.m_calendar);
        e.setHgap(this.m_hgap);
        e.setVgap(this.m_vgap);
        e.setDateMargin(this.m_dateMargin);
        for (int i = e.m_widgetChildren.size() - 1; i >= 0; --i) {
            final Widget w = e.m_widgetChildren.get(i);
            w.destroySelfFromParent();
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final CalendarLayout tl = new CalendarLayout();
        tl.onCheckOut();
        this.add(tl);
        final TextWidgetAppearance app = new TextWidgetAppearance();
        app.onCheckOut();
        this.add(app);
        this.m_calendar.setTime(new Date());
        this.m_calendar.set(5, 1);
        this.computeRowsAndColumnsCount();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_content != null) {
            this.m_content.clear();
            this.m_content = null;
        }
        this.m_cells.clear();
        this.m_cells = null;
        this.m_cellSize = null;
        this.m_rendererManager = null;
        this.m_labelColor = null;
        this.m_labelAlign = null;
        this.m_labelTextRenderer = null;
        this.m_dateMargin = null;
        if (this.m_selectedMesh != null) {
            this.m_selectedMesh.onCheckIn();
            this.m_selectedMesh = null;
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == CalendarWidget.CELL_SIZE_HASH) {
            this.setCellSize(cl.convertToDimension(value));
        }
        else if (hash == CalendarWidget.HGAP_HASH) {
            this.setHgap(PrimitiveConverter.getInteger(value));
        }
        else if (hash == CalendarWidget.VGAP_HASH) {
            this.setVgap(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != CalendarWidget.DATE_MARGIN_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setDateMargin(cl.convertToInsets(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == CalendarWidget.CONTENT_HASH) {
            if (value == null || value.getClass().isArray()) {
                this.setContent((DateComponent[])value);
            }
            else {
                if (!(value instanceof Iterable)) {
                    return false;
                }
                this.setContent((Iterable<? extends DateComponent>)value);
            }
        }
        else {
            if (hash != CalendarWidget.CALENDAR_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setCalendar((Calendar)value);
        }
        return true;
    }
    
    static {
        CalendarWidget.DAYS_IN_WEEK = 7;
        CalendarWidget.MAX_DAYS_IN_MONTH = 31;
        CELL_SIZE_HASH = "cellSize".hashCode();
        CONTENT_HASH = "content".hashCode();
        HGAP_HASH = "hgap".hashCode();
        VGAP_HASH = "vgap".hashCode();
        DATE_MARGIN_HASH = "dateMargin".hashCode();
        CALENDAR_HASH = "calendar".hashCode();
    }
    
    private static class DateCell
    {
        private RenderableContainer m_renderable;
        private Container m_container;
        private Label m_label;
        
        public RenderableContainer getRenderable() {
            return this.m_renderable;
        }
        
        public void setRenderable(final RenderableContainer renderable) {
            this.m_renderable = renderable;
        }
        
        public Container getContainer() {
            return this.m_container;
        }
        
        public void setContainer(final Container container) {
            this.m_container = container;
        }
        
        public Label getLabel() {
            return this.m_label;
        }
        
        public void setLabel(final Label label) {
            this.m_label = label;
        }
    }
    
    public class CalendarLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final int width = CalendarWidget.this.m_cellSize.width * CalendarWidget.this.m_columnCount + CalendarWidget.this.m_hgap * (CalendarWidget.this.m_columnCount - 1);
            final int height = CalendarWidget.this.m_cellSize.height * CalendarWidget.this.m_rowCount + CalendarWidget.this.m_vgap * (CalendarWidget.this.m_rowCount - 1);
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            return this.getContentMinSize(container);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (CalendarWidget.this.m_cells == null) {
                return;
            }
            int startOffset = 0;
            if (CalendarWidget.this.m_calendar != null) {
                final int day = CalendarWidget.this.m_calendar.get(5);
                CalendarWidget.this.m_calendar.set(5, 1);
                startOffset = (CalendarWidget.this.m_calendar.get(7) - CalendarWidget.this.m_calendar.getFirstDayOfWeek() + CalendarWidget.DAYS_IN_WEEK) % CalendarWidget.DAYS_IN_WEEK;
                CalendarWidget.this.m_calendar.set(5, day);
            }
            int xCell = startOffset;
            int yCell = CalendarWidget.this.m_rowCount - 1;
            for (int i = 0; i < CalendarWidget.this.m_cells.size(); ++i) {
                final Container container = CalendarWidget.this.m_cells.get(i).getContainer();
                final int x = (CalendarWidget.this.m_cellSize.width + CalendarWidget.this.m_hgap) * xCell;
                final int y = (CalendarWidget.this.m_cellSize.height + CalendarWidget.this.m_vgap) * yCell;
                container.setPosition(x, y);
                container.setSize(CalendarWidget.this.m_cellSize.width, CalendarWidget.this.m_cellSize.height);
                if (++xCell == CalendarWidget.this.m_columnCount) {
                    xCell = 0;
                    --yCell;
                }
            }
            CalendarWidget.this.setSelectedDate(CalendarWidget.this.m_selectedRenderableIndex + 1);
        }
    }
}
