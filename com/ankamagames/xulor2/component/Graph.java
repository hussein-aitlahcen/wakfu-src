package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import java.util.*;

public class Graph extends Widget
{
    public static final String TAG = "graph";
    private GraphMesh m_mesh;
    private Dimension m_cellSize;
    private boolean m_needsToComputeMinSize;
    private boolean m_needsToUpdateMesh;
    private GraphData m_graphData;
    public static final int CONTENT_HASH;
    public static final int CELL_SIZE_HASH;
    
    public Graph() {
        super();
        this.m_cellSize = null;
        this.m_needsToComputeMinSize = false;
        this.m_needsToUpdateMesh = false;
        this.m_graphData = null;
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        this.m_entity.addChild(this.m_mesh.getEntity());
    }
    
    public void setContent(final GraphData data) {
        this.m_graphData = data;
        this.m_mesh.setData(data);
        this.m_needsToUpdateMesh = true;
        this.m_needsToComputeMinSize = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    public void setCellSize(final Dimension size) {
        this.m_cellSize = size;
        this.m_mesh.setCellWidth(this.m_cellSize.width);
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    @Override
    public String getTag() {
        return "graph";
    }
    
    public void clear() {
        this.m_mesh.clear();
    }
    
    private boolean computeMinSize() {
        final int numElements = (this.m_graphData != null) ? this.m_graphData.getGraphElements().size() : 0;
        final int minWidth = this.m_cellSize.width * numElements;
        final int minHeight = this.m_cellSize.height;
        final Dimension minSize = this.getContentMinSize();
        if (minSize.width == minWidth || minSize.height == minHeight) {
            return false;
        }
        this.setMinSize(new Dimension(minWidth, minHeight));
        this.m_needsToComputeMinSize = false;
        return true;
    }
    
    @Override
    public void validate() {
        super.validate();
        if (this.m_mesh != null) {
            this.m_mesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_needsToComputeMinSize) {
            final boolean minSizeChanged = this.computeMinSize();
            if (minSizeChanged && this.m_containerParent != null) {
                this.m_containerParent.invalidateMinSize();
                this.m_needsToUpdateMesh = true;
                this.setNeedsToPostProcess();
            }
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_needsToUpdateMesh) {
            this.m_mesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
            this.m_needsToUpdateMesh = false;
        }
        return ret;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_graphData = null;
        this.m_cellSize = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        (this.m_mesh = new GraphMesh()).onCheckOut();
        this.m_needsToComputeMinSize = false;
        this.m_needsToUpdateMesh = false;
        this.m_cellSize = new Dimension(0, 0);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Graph.CELL_SIZE_HASH) {
            this.setCellSize(cl.convertToDimension(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Graph.CONTENT_HASH) {
            this.setContent((GraphData)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        CONTENT_HASH = "content".hashCode();
        CELL_SIZE_HASH = "cellSize".hashCode();
    }
    
    public static class GraphData
    {
        private float m_minValue;
        private float m_maxValue;
        private ArrayList<GraphMesh.GraphElement> m_elements;
        private ArrayList<GraphMesh.GraphLine> m_lines;
        private ArrayList<GraphMesh.GraphZone> m_zones;
        
        public GraphData() {
            super();
            this.m_elements = new ArrayList<GraphMesh.GraphElement>();
            this.m_lines = new ArrayList<GraphMesh.GraphLine>();
            this.m_zones = new ArrayList<GraphMesh.GraphZone>();
        }
        
        public float getMinValue() {
            return this.m_minValue;
        }
        
        public void setMinValue(final float minValue) {
            this.m_minValue = minValue;
        }
        
        public float getMaxValue() {
            return this.m_maxValue;
        }
        
        public void setMaxValue(final float maxValue) {
            this.m_maxValue = maxValue;
        }
        
        public void addGraphLine(final GraphMesh.GraphLine line) {
            this.m_lines.add(line);
        }
        
        public void addGraphZone(final GraphMesh.GraphZone zone) {
            this.m_zones.add(zone);
        }
        
        public void addGraphElement(final GraphMesh.GraphElement element) {
            this.m_elements.add(element);
        }
        
        public ArrayList<GraphMesh.GraphElement> getGraphElements() {
            return this.m_elements;
        }
        
        public ArrayList<GraphMesh.GraphLine> getGraphLines() {
            return this.m_lines;
        }
        
        public ArrayList<GraphMesh.GraphZone> getGraphZones() {
            return this.m_zones;
        }
    }
}
