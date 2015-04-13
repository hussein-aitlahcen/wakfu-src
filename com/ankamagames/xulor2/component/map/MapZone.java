package com.ankamagames.xulor2.component.map;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.material.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class MapZone
{
    private static final Logger m_logger;
    private ArrayList<MapZoneElement> m_elements;
    private PartitionListMapZoneDescription m_mapZoneDesc;
    private int m_minX;
    private int m_maxX;
    private int m_minY;
    private int m_maxY;
    private int m_centerX;
    private int m_centerY;
    private int m_minScreenX;
    private int m_maxScreenX;
    private int m_minScreenY;
    private int m_maxScreenY;
    private boolean m_selected;
    private boolean m_selectionChanged;
    private int m_geometryIndex;
    private float m_lineWidth;
    private Pixmap m_pixmap;
    private boolean m_visible;
    private boolean m_enableInteractions;
    private float m_centerWorldX;
    private float m_centerWorldY;
    protected AnimatedElementWithDirection m_animatedElement;
    private String m_anmPath;
    private String m_animName1;
    private String m_animName2;
    private long m_highlightSoundId;
    
    private void loadAnimatedElement() {
        (this.m_animatedElement = new AnimatedElementWithDirection(0L)).setGfxId(FileHelper.getNameWithoutExt(this.m_anmPath));
        try {
            this.m_animatedElement.load(this.m_anmPath, false);
        }
        catch (IOException e) {
            MapZone.m_logger.error((Object)"Unable to load anm file", (Throwable)e);
            this.m_animatedElement.dispose();
            this.m_animatedElement = null;
            return;
        }
        this.m_animatedElement.setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
        this.m_animatedElement.setDirection(Direction8.getDirectionFromIndex(1));
        this.m_animatedElement.setAnimation(this.getAnimName());
        this.m_animatedElement.setMaterial(Material.WHITE_NO_SPECULAR);
        this.m_animatedElement.getAnmEntity().updateMaterial(this.m_animatedElement.getMaterial());
    }
    
    private String getAnimName() {
        return this.m_selected ? this.m_animName2 : this.m_animName1;
    }
    
    public MapZone() {
        super();
        this.m_elements = new ArrayList<MapZoneElement>();
        this.m_minScreenX = Integer.MAX_VALUE;
        this.m_maxScreenX = Integer.MIN_VALUE;
        this.m_minScreenY = Integer.MAX_VALUE;
        this.m_maxScreenY = Integer.MIN_VALUE;
        this.m_selected = false;
        this.m_selectionChanged = true;
        this.m_lineWidth = 1.0f;
        this.m_pixmap = null;
        this.m_visible = true;
        this.m_enableInteractions = true;
        final int n = Integer.MAX_VALUE;
        this.m_minY = n;
        this.m_minX = n;
        final int n2 = Integer.MIN_VALUE;
        this.m_maxY = n2;
        this.m_maxX = n2;
    }
    
    public ArrayList<MapZoneElement> getElements() {
        return this.m_elements;
    }
    
    public void setMapZoneDescription(final PartitionListMapZoneDescription desc) {
        this.m_mapZoneDesc = desc;
    }
    
    public void addElement(final short x, final short y, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
        this.m_elements.add(new MapZoneElement(x, y, x1, y1, x2, y2, x3, y3, x4, y4));
        this.m_minX = Math.min(this.m_minX, x);
        this.m_maxX = Math.max(this.m_maxX, x);
        this.m_minY = Math.min(this.m_minY, y);
        this.m_maxY = Math.max(this.m_maxY, y);
        this.minMaxScreen((int)x1, (int)y2, (int)x3, (int)y4);
    }
    
    private void minMaxScreen(final int x1, final int y2, final int x3, final int y4) {
        this.m_minScreenX = Math.min(this.m_minScreenX, x1);
        this.m_maxScreenX = Math.max(this.m_maxScreenX, x3);
        this.m_minScreenY = Math.min(this.m_minScreenY, y4);
        this.m_maxScreenY = Math.max(this.m_maxScreenY, y2);
    }
    
    public void computeCenter() {
        Collections.sort(this.m_elements);
        final int centerX = (this.m_minX + this.m_maxX) / 2;
        final int centerY = (this.m_minY + this.m_maxY) / 2;
        MapZoneElement found = null;
        final int n = Integer.MAX_VALUE;
        this.m_minScreenY = n;
        this.m_minScreenX = n;
        final int n2 = Integer.MIN_VALUE;
        this.m_maxScreenY = n2;
        this.m_maxScreenX = n2;
        for (int i = this.m_elements.size() - 1; i >= 0; --i) {
            final MapZoneElement element = this.m_elements.get(i);
            this.minMaxScreen((int)element.getX1(), (int)element.getY2(), (int)element.getX3(), (int)element.getY4());
            if (element.m_x == centerX && element.m_y == centerY) {
                found = element;
                break;
            }
        }
        if (found == null && this.m_elements.size() > 0) {
            for (int i = this.m_elements.size() / 2; i >= 0; --i) {
                final MapZoneElement element = this.m_elements.get(i);
                if (element.m_x == centerX) {
                    found = element;
                    break;
                }
            }
        }
        if (found == null && this.m_elements.size() > 0) {
            found = this.m_elements.get(this.m_elements.size() / 2);
        }
        if (found != null) {
            this.m_centerX = found.m_x;
            this.m_centerY = found.m_y;
            this.m_centerWorldX = (found.getX1() + found.getX3()) / 2.0f;
            this.m_centerWorldY = (found.getY1() + found.getY3()) / 2.0f;
        }
    }
    
    public void setEnableInteractions(final boolean enableInteractions) {
        this.m_enableInteractions = enableInteractions;
    }
    
    public boolean contains(final int x, final int y) {
        if (!this.m_enableInteractions) {
            return false;
        }
        for (int i = 0, size = this.m_elements.size(); i < size; ++i) {
            if (this.m_elements.get(i).getX() == x && this.m_elements.get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }
    
    MapZoneElement get(final int x, final int y) {
        for (int i = 0, size = this.m_elements.size(); i < size; ++i) {
            final MapZoneElement elem = this.m_elements.get(i);
            if (elem.m_x == x && elem.m_y == y) {
                return elem;
            }
        }
        return null;
    }
    
    public float[] getVertices() {
        final float[] vertices = new float[this.m_elements.size() * 8];
        for (int i = 0, size = this.m_elements.size(); i < size; ++i) {
            final MapZoneElement elem = this.m_elements.get(i);
            vertices[i * 8] = elem.m_x1;
            vertices[i * 8 + 1] = elem.m_y1;
            vertices[i * 8 + 2] = elem.m_x2;
            vertices[i * 8 + 3] = elem.m_y2;
            vertices[i * 8 + 4] = elem.m_x3;
            vertices[i * 8 + 5] = elem.m_y3;
            vertices[i * 8 + 6] = elem.m_x4;
            vertices[i * 8 + 7] = elem.m_y4;
        }
        return vertices;
    }
    
    public void getBorderVertices(final TFloatArrayList vertices) {
        for (int x = this.m_minX - 1; x < this.m_maxX + 1; ++x) {
            for (int y = this.m_minY - 1; y < this.m_maxY + 1; ++y) {
                final MapZoneElement zone = this.get(x, y);
                final MapZoneElement southZone = this.get(x, y + 1);
                final MapZoneElement eastZone = this.get(x + 1, y);
                if (zone == null) {
                    if (southZone != null) {
                        vertices.add(southZone.m_x2);
                        vertices.add(southZone.m_y2);
                        vertices.add(southZone.m_x3);
                        vertices.add(southZone.m_y3);
                    }
                    if (eastZone != null) {
                        vertices.add(eastZone.m_x1);
                        vertices.add(eastZone.m_y1);
                        vertices.add(eastZone.m_x2);
                        vertices.add(eastZone.m_y2);
                    }
                }
                else {
                    if (southZone == null) {
                        vertices.add(zone.m_x1);
                        vertices.add(zone.m_y1);
                        vertices.add(zone.m_x4);
                        vertices.add(zone.m_y4);
                    }
                    if (eastZone == null) {
                        vertices.add(zone.m_x3);
                        vertices.add(zone.m_y3);
                        vertices.add(zone.m_x4);
                        vertices.add(zone.m_y4);
                    }
                }
            }
        }
    }
    
    public int getMinX() {
        return this.m_minX;
    }
    
    public int getMaxX() {
        return this.m_maxX;
    }
    
    public int getMinY() {
        return this.m_minY;
    }
    
    public int getMaxY() {
        return this.m_maxY;
    }
    
    public Color getColor() {
        return this.m_mapZoneDesc.getZoneColor();
    }
    
    public String getTextDescription() {
        return this.m_mapZoneDesc.getTextDescription();
    }
    
    public boolean isSelected() {
        return this.m_selected;
    }
    
    public void setSelected(final boolean selected) {
        if (this.m_selected == selected) {
            return;
        }
        this.m_selected = selected;
        this.m_selectionChanged = true;
    }
    
    public int getGeometryIndex() {
        return this.m_geometryIndex;
    }
    
    public void setGeometryIndex(final int geometryIndex) {
        this.m_geometryIndex = geometryIndex;
    }
    
    public float getLineWidth() {
        return this.m_lineWidth;
    }
    
    public void setLineWidth(final float lineWidth) {
        this.m_lineWidth = lineWidth;
    }
    
    public int getCenterX() {
        return this.m_centerX;
    }
    
    public int getCenterY() {
        return this.m_centerY;
    }
    
    public float getCenterWorldX() {
        return this.m_centerWorldX;
    }
    
    public float getCenterWorldY() {
        return this.m_centerWorldY;
    }
    
    public int getMinScreenX() {
        return this.m_minScreenX;
    }
    
    public int getMaxScreenX() {
        return this.m_maxScreenX;
    }
    
    public int getMinScreenY() {
        return this.m_minScreenY;
    }
    
    public int getMaxScreenY() {
        return this.m_maxScreenY;
    }
    
    public Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public void setPixmap(final Pixmap pixmap) {
        this.m_pixmap = pixmap;
    }
    
    public void setAnmPath(final String anmPath) {
        this.m_anmPath = anmPath;
    }
    
    public void setAnimName1(final String animName1) {
        this.m_animName1 = animName1;
    }
    
    public void setAnimName2(final String animName2) {
        this.m_animName2 = animName2;
    }
    
    public void setHighlightSoundId(final long highlightSoundId) {
        this.m_highlightSoundId = highlightSoundId;
    }
    
    public void cleanUp() {
        if (this.m_pixmap != null && this.m_pixmap.getTexture() != null) {
            this.m_pixmap.getTexture().removeReference();
            this.m_pixmap = null;
        }
        if (this.m_animatedElement != null) {
            this.m_animatedElement.dispose();
            this.m_animatedElement = null;
        }
    }
    
    public PartitionListMapZoneDescription getMapZoneDescription() {
        return this.m_mapZoneDesc;
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    public AnimatedElementWithDirection getAnimatedElement() {
        return this.m_animatedElement;
    }
    
    public boolean processAnim(final int deltaTime) {
        if (!this.m_visible) {
            return true;
        }
        if (!this.checkAnimation()) {
            return true;
        }
        this.updateEntity();
        if (this.m_selectionChanged) {
            if (this.m_selected && this.m_highlightSoundId != -1L) {
                XulorSoundManager.getInstance().playSound(this.m_highlightSoundId);
            }
            this.m_animatedElement.setAnimation(this.getAnimName());
            this.m_selectionChanged = false;
        }
        this.m_animatedElement.update(null, deltaTime);
        return true;
    }
    
    private boolean checkAnimation() {
        if (this.m_animatedElement == null && this.m_anmPath != null && this.m_animName1 != null && this.m_animName2 != null) {
            this.loadAnimatedElement();
        }
        return this.m_animatedElement != null;
    }
    
    protected void updateEntity() {
        final Entity entity = this.m_animatedElement.getEntity();
        final TransformerSRT transformer = (TransformerSRT)entity.getTransformer().getTransformer(0);
        transformer.setTranslation(this.m_centerWorldX, this.m_centerWorldY, 0.0f);
        float scale = 1.0f;
        if (this.m_animatedElement.getAnmInstance() != null) {
            scale = this.m_animatedElement.getAnmInstance().getScale();
        }
        final float myScale = 60.0f;
        transformer.setScale(60.0f / scale, 60.0f / scale, 0.0f);
        entity.getTransformer().setToUpdate();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapZone.class);
    }
    
    public static class MapZoneElement implements Comparable<MapZoneElement>
    {
        private short m_x;
        private short m_y;
        float m_x1;
        float m_y1;
        float m_x2;
        float m_y2;
        float m_x3;
        float m_y3;
        float m_x4;
        float m_y4;
        private MapZoneElement m_north;
        private MapZoneElement m_south;
        private MapZoneElement m_east;
        private MapZoneElement m_west;
        
        public MapZoneElement(final short x, final short y, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
            super();
            this.m_x = x;
            this.m_y = y;
            this.m_x1 = x1;
            this.m_y1 = y1;
            this.m_x2 = x2;
            this.m_y2 = y2;
            this.m_x3 = x3;
            this.m_y3 = y3;
            this.m_x4 = x4;
            this.m_y4 = y4;
        }
        
        public short getX() {
            return this.m_x;
        }
        
        public short getY() {
            return this.m_y;
        }
        
        public float getX1() {
            return this.m_x1;
        }
        
        public void setX1(final float x1) {
            this.m_x1 = x1;
        }
        
        public float getY1() {
            return this.m_y1;
        }
        
        public void setY1(final float y1) {
            this.m_y1 = y1;
        }
        
        public float getX2() {
            return this.m_x2;
        }
        
        public void setX2(final float x2) {
            this.m_x2 = x2;
        }
        
        public float getY2() {
            return this.m_y2;
        }
        
        public void setY2(final float y2) {
            this.m_y2 = y2;
        }
        
        public float getX3() {
            return this.m_x3;
        }
        
        public void setX3(final float x3) {
            this.m_x3 = x3;
        }
        
        public float getY3() {
            return this.m_y3;
        }
        
        public void setY3(final float y3) {
            this.m_y3 = y3;
        }
        
        public float getX4() {
            return this.m_x4;
        }
        
        public void setX4(final float x4) {
            this.m_x4 = x4;
        }
        
        public float getY4() {
            return this.m_y4;
        }
        
        public void setY4(final float y4) {
            this.m_y4 = y4;
        }
        
        @Override
        public int compareTo(final MapZoneElement o) {
            if (o.m_y != this.m_y) {
                return this.m_y - o.m_y;
            }
            return this.m_x - o.m_x;
        }
        
        public MapZoneElement getNorth() {
            return this.m_north;
        }
        
        public void setNorth(final MapZoneElement north) {
            this.m_north = north;
        }
        
        public MapZoneElement getSouth() {
            return this.m_south;
        }
        
        public void setSouth(final MapZoneElement south) {
            this.m_south = south;
        }
        
        public MapZoneElement getEast() {
            return this.m_east;
        }
        
        public void setEast(final MapZoneElement east) {
            this.m_east = east;
        }
        
        public MapZoneElement getWest() {
            return this.m_west;
        }
        
        public void setWest(final MapZoneElement west) {
            this.m_west = west;
        }
        
        public float[] getVertices() {
            return new float[] { this.m_x1, this.m_y1, this.m_x2, this.m_y2, this.m_x3, this.m_y3, this.m_x4, this.m_y4 };
        }
    }
}
