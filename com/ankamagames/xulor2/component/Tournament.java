package com.ankamagames.xulor2.component;

import java.awt.geom.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.component.tournament.*;
import com.ankamagames.xulor2.core.event.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;

public class Tournament extends Container implements ContentClient
{
    public static final String TAG = "tournament";
    private ArrayList<TournamentItem> m_content;
    private ArrayList<Line2D> m_lines;
    private ArrayList<RenderableContainer> m_renderables;
    private ItemRendererManager m_rendererManager;
    private int m_mouseOverRenderableIndex;
    private int m_vgap;
    private int m_hgap;
    private int m_rowCount;
    private int m_columnCount;
    private Dimension m_cellSize;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    private boolean m_tournamentIsDirty;
    public static final int CELL_SIZE_HASH;
    public static final int CONTENT_HASH;
    public static final int HGAP_HASH;
    public static final int VGAP_HASH;
    
    public Tournament() {
        super();
        this.m_lines = new ArrayList<Line2D>();
        this.m_renderables = new ArrayList<RenderableContainer>();
        this.m_rendererManager = new ItemRendererManager();
        this.m_mouseOverRenderableIndex = -1;
        this.m_vgap = 5;
        this.m_hgap = 10;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_tournamentIsDirty = true;
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
    }
    
    @Override
    public String getTag() {
        return "tournament";
    }
    
    public void setCellSize(final Dimension cellSize) {
        this.m_cellSize = cellSize;
        this.m_tournamentIsDirty = true;
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
        this.m_tournamentIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public int getHgap() {
        return this.m_hgap;
    }
    
    public void setHgap(final int hgap) {
        this.m_hgap = hgap;
        this.m_tournamentIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap elementMap) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = elementMap;
    }
    
    public void setContent(final TournamentData td) {
        int size = 0;
        this.m_content = (ArrayList<TournamentItem>)td.getItems().clone();
        this.m_rowCount = td.getRowCount();
        this.m_columnCount = td.getColumnCount();
        this.m_lines = (ArrayList<Line2D>)((td.getLines() != null) ? td.getLines().clone() : this.m_lines);
        size = this.m_content.size();
        final int renderableSize = this.m_renderables.size();
        if (renderableSize < size) {
            for (int i = renderableSize; i < size; ++i) {
                final RenderableContainer container = new RenderableContainer();
                container.onCheckOut();
                container.setNonBlocking(this.m_nonBlocking);
                container.setRendererManager(this.m_rendererManager);
                container.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        final RenderableContainer container = event.getCurrentTarget();
                        if (container.getItemValue() != null) {
                            Tournament.this.m_mouseOverRenderableIndex = Tournament.this.m_renderables.indexOf(container);
                        }
                        return false;
                    }
                }, false);
                container.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        Tournament.this.m_mouseOverRenderableIndex = -1;
                        return false;
                    }
                }, false);
                this.m_renderables.add(container);
                this.add(container);
            }
        }
        else if (renderableSize > size) {
            for (int i = size; i < renderableSize; ++i) {
                final RenderableContainer container = this.m_renderables.remove(this.m_renderables.size() - 1);
                container.destroySelfFromParent();
            }
            if (this.m_mouseOverRenderableIndex >= this.m_renderables.size()) {
                this.m_mouseOverRenderableIndex = -1;
            }
        }
        this.updateValues();
    }
    
    public void updateValues() {
        for (int i = 0; i < this.m_content.size(); ++i) {
            final RenderableContainer container = this.m_renderables.get(i);
            final TournamentItem item = this.m_content.get(i);
            container.setContentProperty(this.m_contentProperty + "#" + i, this.m_contentPropertyElementMap);
            container.setContent(item.getValue());
        }
    }
    
    @Override
    public void validate() {
        super.validate();
        this.computeLineCoordinates();
    }
    
    private void computeLineCoordinates() {
        final ArrayList<Line2D> lines = new ArrayList<Line2D>();
        final int fullCellWidth = this.m_cellSize.width + this.m_hgap;
        final int fullCellHeight = this.m_cellSize.height + this.m_vgap;
        for (final Line2D line : this.m_lines) {
            final float lineX1 = (float)line.getX1();
            final float lineY1 = (float)line.getY1();
            final float lineX2 = (float)line.getX2();
            final float lineY2 = (float)line.getY2();
            final float x1 = fullCellWidth * lineX1 + this.m_cellSize.width;
            final float x2 = fullCellWidth * (lineX1 + 1.0f) - this.m_hgap / 2;
            float lineY3 = this.m_rowCount - lineY1 - 1.0f;
            final float y1 = fullCellHeight * lineY3 + this.m_cellSize.height / 2;
            lineY3 = this.m_rowCount - lineY2 - 1.0f;
            final float y2 = fullCellHeight * lineY3 + this.m_cellSize.height / 2;
            final float x3 = fullCellWidth * lineX2;
            lines.add(new Line2D.Float(x1, y1, x2, y1));
            lines.add(new Line2D.Float(x2, y1, x2, y2));
            lines.add(new Line2D.Float(x2, y2, x3, y2));
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_tournamentIsDirty) {
            this.invalidateMinSize();
        }
        this.m_tournamentIsDirty = false;
        return ret;
    }
    
    private void createTestTournament() {
        final ArrayList<TournamentItem> items = new ArrayList<TournamentItem>();
        final ArrayList<Line2D> lines = new ArrayList<Line2D>();
        TournamentItem item = new TournamentItem("Arone", 1.0f, 0.0f);
        items.add(item);
        item = new TournamentItem("?", 1.0f, 7.0f);
        items.add(item);
        item = new TournamentItem("Khalim", 1.0f, 1.0f);
        items.add(item);
        lines.add(new Line2D.Float(1.0f, 0.0f, 2.0f, 0.5f));
        lines.add(new Line2D.Float(1.0f, 1.0f, 2.0f, 0.5f));
        item = new TournamentItem("Tessaran", 1.0f, 2.0f);
        items.add(item);
        item = new TournamentItem("Noreen", 1.0f, 3.0f);
        items.add(item);
        lines.add(new Line2D.Float(1.0f, 2.0f, 2.0f, 2.5f));
        lines.add(new Line2D.Float(1.0f, 3.0f, 2.0f, 2.5f));
        item = new TournamentItem("Arone", 2.0f, 0.5f);
        items.add(item);
        lines.add(new Line2D.Float(2.0f, 0.5f, 3.0f, 1.5f));
        item = new TournamentItem("Noreen", 2.0f, 2.5f);
        items.add(item);
        item = new TournamentItem("Khalim", 0.0f, 4.0f);
        items.add(item);
        item = new TournamentItem("Tessaran", 0.0f, 5.0f);
        items.add(item);
        item = new TournamentItem("Khalim", 2.0f, 4.5f);
        items.add(item);
        item = new TournamentItem("Noreen", 2.0f, 5.5f);
        items.add(item);
        item = new TournamentItem("Khalim", 3.0f, 5.0f);
        items.add(item);
        item = new TournamentItem("Arone", 3.0f, 1.5f);
        items.add(item);
        item = new TournamentItem("?", 4.0f, 3.25f);
        items.add(item);
        final TournamentData td = new TournamentData(items, lines, 8, 5);
        this.setContent(td);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TournamentLayout tl = new TournamentLayout();
        tl.onCheckOut();
        this.add(tl);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_content.clear();
        this.m_content = null;
        this.m_renderables.clear();
        this.m_renderables = null;
        this.m_lines.clear();
        this.m_lines = null;
        this.m_cellSize = null;
        this.m_rendererManager = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Tournament.CELL_SIZE_HASH) {
            this.setCellSize(cl.convertToDimension(value));
        }
        else if (hash == Tournament.HGAP_HASH) {
            this.setHgap(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != Tournament.VGAP_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVgap(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Tournament.CONTENT_HASH) {
            this.setContent((TournamentData)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        CELL_SIZE_HASH = "cellSize".hashCode();
        CONTENT_HASH = "content".hashCode();
        HGAP_HASH = "hgap".hashCode();
        VGAP_HASH = "vgap".hashCode();
    }
    
    public class TournamentLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final int width = Tournament.this.m_cellSize.width * Tournament.this.m_columnCount + Tournament.this.m_hgap * (Tournament.this.m_columnCount - 1);
            final int height = Tournament.this.m_cellSize.height * Tournament.this.m_rowCount + Tournament.this.m_vgap * (Tournament.this.m_rowCount - 1);
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            return this.getContentMinSize(container);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (Tournament.this.m_content != null) {
                for (int i = 0; i < Tournament.this.m_content.size(); ++i) {
                    final RenderableContainer container = Tournament.this.m_renderables.get(i);
                    final TournamentItem item = Tournament.this.m_content.get(i);
                    final float itemX = item.getX();
                    final float itemY = item.getY();
                    final int x = (int)((Tournament.this.m_cellSize.width + Tournament.this.m_hgap) * itemX);
                    final int y = (int)((Tournament.this.m_cellSize.height + Tournament.this.m_vgap) * (Tournament.this.m_rowCount - itemY - 1.0f));
                    container.setPosition(x, y);
                    container.setSize(Tournament.this.m_cellSize.width, Tournament.this.m_cellSize.height);
                }
            }
        }
    }
}
