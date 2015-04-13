package com.ankamagames.wakfu.client.alea.graphics.havenWorldMini;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;

public class DisplayedScreenMultiElement extends DisplayedScreenElement
{
    private static final Logger m_logger;
    private static final ElementProperties DEFAULT_ELEMENT;
    public static final ObjectFactory Factory;
    private EntityGroup m_entity;
    private HavenWorldTopology m_havenWorld;
    private HavenWorldImagesLibrary m_imageOffsets;
    private int m_startX;
    private int m_startY;
    private int m_width;
    private int m_height;
    
    public void setImageOffsets(final HavenWorldImagesLibrary imageOffsets) {
        this.m_imageOffsets = imageOffsets;
    }
    
    public void setCoordinates(final int cellX, final int cellY, final short cellZ, final int altitudeOrder) {
        final ScreenElement screenElt = ScreenElement.Factory.newPooledInstance();
        screenElt.setCoordinates(cellX, cellY, cellZ);
        screenElt.setProperties(DisplayedScreenMultiElement.DEFAULT_ELEMENT);
        screenElt.setAltitudeOrder((byte)(altitudeOrder + 1));
        this.setElement(screenElt);
        screenElt.removeReference();
    }
    
    private void setData(final HavenWorldTopology havenWorld, final int startX, final int startY, final int width, final int height) {
        this.m_havenWorld = havenWorld;
        this.m_startX = startX;
        this.m_startY = startY;
        this.m_width = width;
        this.m_height = height;
    }
    
    @Override
    public Entity getEntitySprite() {
        return this.m_entity;
    }
    
    @Override
    public void createSprite() {
        if (this.m_entity != null) {
            this.m_entity.removeReference();
        }
        this.m_entity = EntityGroup.Factory.newPooledInstance();
        ((DisplayedScreenElement)(this.m_entity.m_owner = this)).resetColor();
        this.initializeEntity(this.m_entity);
        this.createSprites();
        this.m_entity.computeBounds();
    }
    
    private void createSprites() {
        final float offsetZ = this.getElement().getCellZ() * 10.0f;
        for (int y = 0; y < this.m_height; ++y) {
            for (int x = 0; x < this.m_width; ++x) {
                final int patchId = this.m_havenWorld.getPatchId(this.m_startX + x, this.m_startY + y);
                MiniSpriteFactory.addPatch(this.m_entity, patchId, x, y, offsetZ, this.m_imageOffsets.getPatchTextureOffset(patchId));
            }
        }
        this.m_havenWorld.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct b) {
                this.addSpriteFor(b);
                return true;
            }
            
            private void addSpriteFor(final AbstractBuildingStruct b) {
                final Rect bounds = b.getCellBounds();
                final int patchCoordX = PartitionPatch.getPatchCoordFromCellX(bounds.m_xMax);
                if (patchCoordX < DisplayedScreenMultiElement.this.m_startX || patchCoordX >= DisplayedScreenMultiElement.this.m_startX + DisplayedScreenMultiElement.this.m_width) {
                    return;
                }
                final int patchCoordY = PartitionPatch.getPatchCoordFromCellY(bounds.m_yMax);
                if (patchCoordY < DisplayedScreenMultiElement.this.m_startY || patchCoordY >= DisplayedScreenMultiElement.this.m_startY + DisplayedScreenMultiElement.this.m_height) {
                    return;
                }
                final int editorGroupId = b.getEditorGroupId();
                MiniSpriteFactory.addBuilding(DisplayedScreenMultiElement.this.m_entity, editorGroupId, b.getCellX() - DisplayedScreenMultiElement.this.m_startX * 9, b.getCellY() - DisplayedScreenMultiElement.this.m_startY * 9, offsetZ, DisplayedScreenMultiElement.this.m_imageOffsets.getBuildingTextureOffset(editorGroupId));
            }
        });
    }
    
    @Override
    public boolean fineHitTest(final int x, final int y) {
        return false;
    }
    
    @Override
    public void applyLighting(final float[] colors) {
        if (this.m_entity == null) {
            return;
        }
        for (final Entity entity : this.m_entity.getChildList()) {
            DisplayedScreenElement.applyLightingTo((EntitySprite)entity, colors);
        }
    }
    
    @Override
    protected void updateEntityColor(final float[] color) {
        for (final Entity child : this.m_entity.getChildList()) {
            final EntitySprite sprite = (EntitySprite)child;
            sprite.setColor(color[0], color[1], color[2], color[3]);
            sprite.updateMaterial();
        }
    }
    
    @Override
    protected void checkin() {
        super.checkin();
        if (this.m_entity != null) {
            this.m_entity.removeReference();
            this.m_entity = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisplayedScreenMultiElement.class);
        DEFAULT_ELEMENT = ElementProperties.createDefault(0, false, false);
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<DisplayedScreenMultiElement>
    {
        public ObjectFactory() {
            super(DisplayedScreenMultiElement.class);
        }
        
        @Override
        public DisplayedScreenMultiElement create() {
            return new DisplayedScreenMultiElement();
        }
        
        public DisplayedScreenMultiElement newPooledInstance(final HavenWorldTopology havenWorld, final int startX, final int startY, final int width, final int height) {
            final DisplayedScreenMultiElement elt = this.newPooledInstance();
            elt.setData(havenWorld, startX, startY, width, height);
            return elt;
        }
    }
}
