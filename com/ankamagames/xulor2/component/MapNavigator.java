package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.xulor2.component.mapOverlay.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;

public class MapNavigator extends MapOverlay<MapNavigatorMesh2>
{
    public static final String TAG = "MapNavigator";
    
    @Override
    public String getTag() {
        return "MapNavigator";
    }
    
    @Override
    protected void setMeshCenter() {
        ((MapNavigatorMesh2)this.m_mesh).setCenter(this.m_originX, this.m_originY);
    }
    
    @Override
    protected void createMaskTexture(final String path, final URL url, final DocumentEntry child) {
    }
    
    @Override
    protected void setBaseMapDisplayer() {
        ((MapNavigatorMesh2)this.m_mesh).setMapDisplayer(new Entity3DMapDisplayer());
    }
    
    @Override
    protected void endSetPath() {
        super.endSetPath();
        this.m_needsToComputeImage = true;
    }
    
    @Override
    protected void createPixmap(final TextureInfo textureInfo, final Texture texture) {
        super.createPixmap(textureInfo, texture);
        ((MapNavigatorMesh2)this.m_mesh).setPixmap(this.m_pixmap);
    }
    
    @Override
    public void setMapRect(final int minX, final int minY, final int width, final int height) {
        super.setMapRect(minX, minY, width, height);
        this.setMeshCenter();
    }
    
    @Override
    protected void computeImageMesh() {
        ((MapNavigatorMesh2)this.m_mesh).setSize(this.m_size.width, this.m_size.height);
        super.computeImageMesh();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_mesh = (T)new MapNavigatorMesh2();
        ((MapNavigatorMesh2)this.m_mesh).onCheckOut();
        ((MapNavigatorMesh2)this.m_mesh).setModulationColor(new Color(Color.WHITE));
        this.m_enableTooltip = false;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        if (this.m_needsToComputeImage) {
            this.computeImage();
        }
        final float isoCenterX = this.getIsoCenterX();
        final float isoCenterY = this.getIsoCenterY();
        final float isoCenterZ = this.getIsoCenterZ();
        final float targetScreenX = (isoCenterX - isoCenterY) * 86.0f / 2.0f;
        final float targetScreenY = -(isoCenterX + isoCenterY) * 43.0f / 2.0f + isoCenterZ * 10.0f;
        ((MapNavigatorMesh2)this.m_mesh).setXY(targetScreenX, targetScreenY);
        return true;
    }
}
