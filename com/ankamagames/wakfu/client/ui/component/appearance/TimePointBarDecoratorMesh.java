package com.ankamagames.wakfu.client.ui.component.appearance;

import com.ankamagames.xulor2.decorator.mesh.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.material.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;

public class TimePointBarDecoratorMesh extends AbstractDecoratorMesh
{
    private static final Logger m_logger;
    protected Entity3D m_entity;
    private Color m_modulationColor;
    private Pixmap m_borderBubblePixmap;
    private Pixmap m_oppositeBorderBubblePixmap;
    private Pixmap m_linkPixmap;
    private Pixmap m_doubleBubblePixmap;
    private boolean m_arePixmapInitialized;
    private boolean m_areBaseGeometriesInitialized;
    private int[] m_pixelSeparations;
    private int[] m_displayedTurns;
    private float[] m_alphas;
    private int m_width;
    private int m_height;
    private int m_leftInsets;
    private int m_rightInsets;
    private int m_topInsets;
    private int m_bottomInsets;
    
    public TimePointBarDecoratorMesh() {
        super();
        this.m_arePixmapInitialized = false;
        this.m_areBaseGeometriesInitialized = false;
    }
    
    public void setPixelSeparations(final int[] pixelSeparation, final int[] displayedTurns, final float[] alphas) {
        this.m_pixelSeparations = pixelSeparation;
        this.m_alphas = alphas;
        this.checkJunctions(displayedTurns);
        this.m_displayedTurns = displayedTurns;
        this.checkNumJunctions();
        this.moveAllJunctions();
    }
    
    public float[] getAlphas() {
        return this.m_alphas;
    }
    
    public int[] getPixelSeparations() {
        return this.m_pixelSeparations;
    }
    
    public void setBorderBubblePixmap(final Pixmap pixmap) {
        this.m_borderBubblePixmap = pixmap;
        this.checkForPixmapInitialization();
    }
    
    public void setLinkPixmap(final Pixmap pixmap) {
        this.m_linkPixmap = pixmap;
        this.checkForPixmapInitialization();
    }
    
    public void setDoubleBubblePixmap(final Pixmap pixmap) {
        this.m_doubleBubblePixmap = pixmap;
        this.checkForPixmapInitialization();
    }
    
    public void setOppositeBorderBubblePixmap(final Pixmap oppositeBorderBubblePixmap) {
        this.m_oppositeBorderBubblePixmap = oppositeBorderBubblePixmap;
        this.checkForPixmapInitialization();
    }
    
    public Pixmap getBorderBubblePixmap() {
        return this.m_borderBubblePixmap;
    }
    
    public Pixmap getOppositeBorderBubblePixmap() {
        return this.m_oppositeBorderBubblePixmap;
    }
    
    public Pixmap getLinkPixmap() {
        return this.m_linkPixmap;
    }
    
    public Pixmap getDoubleBubblePixmap() {
        return this.m_doubleBubblePixmap;
    }
    
    private void checkForPixmapInitialization() {
        this.m_arePixmapInitialized = (this.m_borderBubblePixmap != null && this.m_doubleBubblePixmap != null && this.m_linkPixmap != null && this.m_oppositeBorderBubblePixmap != null);
    }
    
    private void checkJunctions(final int[] displayedTurns) {
        int newIndex = displayedTurns.length - 1;
        int oldIndex = this.m_displayedTurns.length - 1;
        while (oldIndex >= 0 || newIndex >= 0) {
            final int oldDisplayedTurn = (this.m_displayedTurns.length > oldIndex && oldIndex >= 0) ? this.m_displayedTurns[oldIndex] : 0;
            final int newDisplayedTurn = (displayedTurns.length > newIndex && newIndex >= 0) ? displayedTurns[newIndex] : 0;
            if (newIndex < 0 || (oldIndex >= 0 && oldDisplayedTurn < newDisplayedTurn)) {
                this.deleteJunctionPoint(oldIndex);
                --oldIndex;
            }
            else if (oldIndex < 0 || (newIndex >= 0 && oldDisplayedTurn > newDisplayedTurn)) {
                this.createJunctionPoint(oldIndex);
                --newIndex;
            }
            else {
                --oldIndex;
                --newIndex;
            }
        }
    }
    
    private void checkNumJunctions() {
        final int numGeometries = this.m_entity.getNumGeometries();
        final int numJunctions = numGeometries - 3;
        if (numJunctions < this.m_pixelSeparations.length) {
            TimePointBarDecoratorMesh.m_logger.warn((Object)("mauvais nombre de jonctions : " + numJunctions + "/" + this.m_pixelSeparations.length));
        }
        else if (numJunctions > this.m_pixelSeparations.length) {
            TimePointBarDecoratorMesh.m_logger.warn((Object)("mauvais nombre de jonctions : " + numJunctions + "/" + this.m_pixelSeparations.length));
        }
    }
    
    private void moveJunctionPoint(final int index, final int left, final int startY) {
        final int numGeometries = this.m_entity.getNumGeometries();
        final int numJunctions = numGeometries - 3;
        if (index < 0 || index >= numJunctions) {
            return;
        }
        final GeometrySprite doubleGeom = (GeometrySprite)this.m_entity.getGeometry(index + 3);
        doubleGeom.setTopLeft(startY, left);
        doubleGeom.setColor(1.0f, 1.0f, 1.0f, this.m_alphas[index]);
    }
    
    public void createJunctionPoint(final int index) {
        if (!this.m_arePixmapInitialized) {
            return;
        }
        final int numJunctions = this.m_entity.getNumGeometries() - 3;
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        geom.setSize(this.m_doubleBubblePixmap.getWidth(), this.m_doubleBubblePixmap.getHeight());
        geom.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        geom.setTextureCoordinates(this.m_doubleBubblePixmap.getTop(), this.m_doubleBubblePixmap.getLeft(), this.m_doubleBubblePixmap.getBottom(), this.m_doubleBubblePixmap.getRight(), this.m_doubleBubblePixmap.getRotation());
        this.m_entity.addTexturedGeometry(geom, this.m_doubleBubblePixmap.getTexture(), null);
        geom.removeReference();
        if (numJunctions > 0) {
            for (int i = numJunctions; i >= index && i > 0; --i) {
                final GeometrySprite geom2 = (GeometrySprite)this.m_entity.getGeometry(i + 3);
                final GeometrySprite geom3 = (GeometrySprite)this.m_entity.getGeometry(i - 1 + 3);
                geom2.setBounds(geom3.getTop(), geom3.getLeft(), geom3.getWidth(), geom3.getHeight());
            }
        }
    }
    
    public void deleteJunctionPoint(final int i) {
        final int numGeometries = this.m_entity.getNumGeometries();
        final int numJunctions = numGeometries - 3;
        if (numJunctions <= 0) {
            return;
        }
        this.m_entity.removeTextureGeometry(this.m_entity.getGeometry(3 + i));
    }
    
    private void createBaseGeometries() {
        this.addGeometry(this.m_width - this.m_borderBubblePixmap.getWidth(), 0, this.m_borderBubblePixmap.getWidth(), this.m_borderBubblePixmap.getHeight(), this.m_borderBubblePixmap);
        this.addGeometry(this.m_width - this.m_oppositeBorderBubblePixmap.getWidth(), 0, this.m_oppositeBorderBubblePixmap.getWidth(), this.m_oppositeBorderBubblePixmap.getHeight(), this.m_oppositeBorderBubblePixmap);
        this.addGeometry(this.m_width - this.m_linkPixmap.getWidth(), 0, this.m_linkPixmap.getWidth(), 0, this.m_linkPixmap);
        this.m_areBaseGeometriesInitialized = true;
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        this.m_leftInsets = margin.left + border.left + padding.left;
        this.m_bottomInsets = margin.bottom + border.bottom + padding.bottom;
        this.m_rightInsets = margin.right + border.right + padding.right;
        this.m_topInsets = margin.top + border.top + padding.top;
        this.m_width = size.width - this.m_leftInsets - this.m_rightInsets;
        this.m_height = size.height - this.m_topInsets - this.m_bottomInsets;
        if (!this.m_arePixmapInitialized) {
            return;
        }
        if (!this.m_areBaseGeometriesInitialized) {
            this.createBaseGeometries();
        }
        this.moveAllGeometries();
    }
    
    private void moveAllGeometries() {
        GeometrySprite gs = (GeometrySprite)this.m_entity.getGeometry(0);
        gs.setTopLeft(this.m_bottomInsets + this.m_borderBubblePixmap.getHeight(), this.m_leftInsets + this.m_width - this.m_borderBubblePixmap.getWidth());
        gs = (GeometrySprite)this.m_entity.getGeometry(1);
        gs.setTopLeft(this.m_bottomInsets + this.m_height, this.m_leftInsets + this.m_width - this.m_oppositeBorderBubblePixmap.getWidth());
        gs = (GeometrySprite)this.m_entity.getGeometry(2);
        gs.setBounds(this.m_bottomInsets + this.m_height - this.m_oppositeBorderBubblePixmap.getHeight(), this.m_leftInsets + this.m_width - this.m_oppositeBorderBubblePixmap.getWidth(), this.m_linkPixmap.getWidth(), this.m_height - this.m_oppositeBorderBubblePixmap.getHeight() - this.m_borderBubblePixmap.getHeight());
        this.moveAllJunctions();
    }
    
    private void moveAllJunctions() {
        if (this.m_pixelSeparations != null && this.m_pixelSeparations.length > 0) {
            for (int i = 0, length = this.m_pixelSeparations.length; i < length; ++i) {
                this.moveJunctionPoint(i, this.m_leftInsets + this.m_width - this.m_linkPixmap.getWidth(), this.m_bottomInsets + this.m_pixelSeparations[i]);
            }
        }
    }
    
    private void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap) {
        if (pixmap == null || pixmap.getTexture() == null) {
            return;
        }
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        geom.setBounds(top, left, width, height);
        if (this.m_modulationColor != null) {
            geom.setColor(this.m_modulationColor.getRed(), this.m_modulationColor.getGreen(), this.m_modulationColor.getBlue(), this.m_modulationColor.getAlpha());
        }
        else {
            geom.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        geom.setTextureCoordinates(pixmap.getTop(), pixmap.getLeft(), pixmap.getBottom(), pixmap.getRight(), pixmap.getRotation());
        this.m_entity.addTexturedGeometry(geom, pixmap.getTexture(), null);
        geom.removeReference();
    }
    
    @Override
    public Entity getEntity() {
        return this.m_entity;
    }
    
    @Override
    public void onCheckIn() {
        this.m_borderBubblePixmap = null;
        this.m_oppositeBorderBubblePixmap = null;
        this.m_doubleBubblePixmap = null;
        this.m_linkPixmap = null;
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_modulationColor = null;
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null : "Object already checked out";
        this.m_entity = Entity3D.Factory.newPooledInstance();
        this.m_arePixmapInitialized = false;
        this.m_areBaseGeometriesInitialized = false;
        this.m_displayedTurns = new int[0];
        this.m_alphas = new float[0];
        this.m_pixelSeparations = new int[0];
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimePointBarDecoratorMesh.class);
    }
}
