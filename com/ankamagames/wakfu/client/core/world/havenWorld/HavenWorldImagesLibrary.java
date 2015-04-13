package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.util.*;

public class HavenWorldImagesLibrary
{
    private static final Logger m_logger;
    public static final HavenWorldImagesLibrary INSTANCE;
    private final Cache m_patches;
    private final Cache m_buildings;
    
    private HavenWorldImagesLibrary() {
        super();
        this.m_patches = new Cache("patchImagePath", "patchImageOffsetFile");
        this.m_buildings = new Cache("buildingImagePath", "buildingImageOffsetFile");
        try {
            this.m_patches.initialize();
            this.m_buildings.initialize();
        }
        catch (Exception e) {
            HavenWorldImagesLibrary.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    public float getPatchScale() {
        return this.m_patches.getScale();
    }
    
    public float getBuildingScale() {
        return this.m_buildings.getScale();
    }
    
    public Point2i getPatchCellOffset(final int patchId) {
        return this.m_patches.getCellOffset(patchId);
    }
    
    public Point2i getBuildingCellOffset(final int buildingId) {
        return this.m_buildings.getCellOffset(buildingId);
    }
    
    public Texture getPatchTexture(final int patchId) {
        return this.m_patches.getTexture(patchId);
    }
    
    public Texture getBuildingTexture(final int buildingId) {
        return this.m_buildings.getTexture(buildingId);
    }
    
    public Point2i getPatchTextureOffset(final int patchId) {
        return this.m_patches.getTextureOffset(patchId);
    }
    
    public Point2i getBuildingTextureOffset(final int groupId) {
        return this.m_buildings.getTextureOffset(groupId);
    }
    
    public void clearTextures() {
        this.m_patches.clearTextures();
        this.m_buildings.clearTextures();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldImagesLibrary.class);
        INSTANCE = new HavenWorldImagesLibrary();
    }
    
    private static class Cache
    {
        private final TIntObjectHashMap<Point2i> m_offsets;
        private final TIntObjectHashMap<Point2i> m_cellOffsets;
        private final TIntObjectHashMap<Texture> m_textures;
        private final String m_pathKey;
        private final String m_offsetPathKey;
        private float m_scale;
        
        Cache(final String pathKey, final String offsetPathKey) {
            super();
            this.m_offsets = new TIntObjectHashMap<Point2i>();
            this.m_cellOffsets = new TIntObjectHashMap<Point2i>();
            this.m_textures = new TIntObjectHashMap<Texture>();
            this.m_pathKey = pathKey;
            this.m_offsetPathKey = offsetPathKey;
        }
        
        Texture getTexture(final int id) {
            assert !this.m_offsets.isEmpty() : "appeller initialize avant";
            Texture texture = this.m_textures.get(id);
            if (texture != null) {
                return texture;
            }
            final Point2i offset = this.getTextureOffset(id);
            if (offset == null) {
                HavenWorldImagesLibrary.m_logger.error((Object)("pas de texture " + id));
                return null;
            }
            String textureFilename = null;
            try {
                textureFilename = ContentFileHelper.transformFileName(WakfuConfiguration.getInstance().getString(this.m_pathKey) + id + ".tga");
            }
            catch (PropertyException e) {
                HavenWorldImagesLibrary.m_logger.error((Object)"", (Throwable)e);
            }
            final long name = Engine.getTextureName(textureFilename);
            texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), name, textureFilename, false);
            if (texture == null) {
                HavenWorldImagesLibrary.m_logger.error((Object)("la texture " + textureFilename + " n'existe pas?"));
                return null;
            }
            if (texture.getFileName() != null) {
                texture.setCreateMask(true);
            }
            texture.addReference();
            this.m_textures.put(id, texture);
            texture.getLayer(0).setStart((short)offset.getX(), (short)offset.getY());
            return texture;
        }
        
        Point2i getCellOffset(final int id) {
            return this.m_cellOffsets.get(id);
        }
        
        public Point2i getTextureOffset(final int id) {
            return this.m_offsets.get(id);
        }
        
        float getScale() {
            return this.m_scale;
        }
        
        void clearTextures() {
            this.m_textures.forEachValue(new TObjectProcedure<Texture>() {
                @Override
                public boolean execute(final Texture object) {
                    object.removeReference();
                    return true;
                }
            });
            this.m_textures.clear();
        }
        
        void initialize() throws Exception {
            final String fileName = WakfuConfiguration.getContentPath(this.m_offsetPathKey);
            final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
            accessor.open(fileName);
            final XMLDocumentContainer document = accessor.getNewDocumentContainer();
            accessor.read(document, new DocumentEntryParser[0]);
            final XMLDocumentNode root = document.getRootNode();
            for (final DocumentEntry entry : root.getChildrenByName("offset")) {
                final int id = entry.getParameterByName("id").getIntValue();
                final int x = entry.getParameterByName("x").getIntValue();
                final int y = entry.getParameterByName("y").getIntValue();
                this.m_offsets.put(id, new Point2i(x, y));
                final int cellx = entry.getParameterByName("cell_x").getIntValue();
                final int celly = entry.getParameterByName("cell_y").getIntValue();
                this.m_cellOffsets.put(id, new Point2i(cellx, celly));
            }
            this.m_cellOffsets.compact();
            this.m_offsets.compact();
            this.m_scale = root.getParameterByName("scale").getFloatValue();
            accessor.close();
        }
    }
}
