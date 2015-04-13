package com.ankamagames.framework.graphics.engine.texture;

import com.ankamagames.framework.graphics.engine.geometry.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class Pixmap
{
    private int m_width;
    private int m_height;
    public float m_left;
    public float m_right;
    public float m_bottom;
    public float m_top;
    private Texture m_texture;
    private int m_x;
    private int m_y;
    private boolean m_computed;
    private boolean m_useFullTexture;
    private boolean m_flipHorizontaly;
    private boolean m_flipVerticaly;
    private GeometrySprite.SpriteRotation m_rotation;
    private final ArrayList<PixmapChangeClient> m_clients;
    
    public Pixmap() {
        super();
        this.m_flipHorizontaly = false;
        this.m_flipVerticaly = false;
        this.m_rotation = GeometrySprite.SpriteRotation.NONE;
        this.m_clients = new ArrayList<PixmapChangeClient>();
        this.m_x = 0;
        this.m_y = 0;
        this.m_width = -1;
        this.m_height = -1;
        this.m_useFullTexture = true;
    }
    
    public Pixmap(final Texture texture) {
        super();
        this.m_flipHorizontaly = false;
        this.m_flipVerticaly = false;
        this.m_rotation = GeometrySprite.SpriteRotation.NONE;
        this.m_clients = new ArrayList<PixmapChangeClient>();
        assert texture != null;
        (this.m_texture = texture).addReference();
        this.m_x = 0;
        this.m_y = 0;
        this.m_width = -1;
        this.m_height = -1;
        this.m_useFullTexture = true;
        this.computeCoordinates();
    }
    
    public Pixmap(final Texture texture, final int x, final int y, final int width, final int height) {
        super();
        this.m_flipHorizontaly = false;
        this.m_flipVerticaly = false;
        this.m_rotation = GeometrySprite.SpriteRotation.NONE;
        this.m_clients = new ArrayList<PixmapChangeClient>();
        assert texture != null;
        (this.m_texture = texture).addReference();
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
        this.m_useFullTexture = false;
        this.computeCoordinates();
    }
    
    public void addClient(final PixmapChangeClient client) {
        assert client != null : "Client can't be null";
        assert !this.m_clients.contains(client) : "Client already registered for this pixmap";
        this.m_clients.add(client);
    }
    
    public void removeClient(final PixmapChangeClient client) {
        assert client != null : "Client can't be null";
        this.m_clients.remove(client);
    }
    
    public Texture getTexture() {
        return this.m_texture;
    }
    
    public int getRealHeight() {
        return this.m_height;
    }
    
    public int getRealWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_rotation.isAffectWidthAndHeight() ? this.m_width : this.m_height;
    }
    
    public int getWidth() {
        return this.m_rotation.isAffectWidthAndHeight() ? this.m_height : this.m_width;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public float getLeft() {
        return this.m_left;
    }
    
    public float getRight() {
        return this.m_right;
    }
    
    public float getBottom() {
        return this.m_bottom;
    }
    
    public float getTop() {
        return this.m_top;
    }
    
    public void setTexture(final Texture texture) {
        if (texture != null) {
            texture.addReference();
        }
        if (this.m_texture != null) {
            this.m_texture.removeReference();
        }
        this.m_texture = texture;
        this.m_computed = false;
    }
    
    public void setX(final int x) {
        this.m_x = x;
        this.m_computed = false;
    }
    
    public void setY(final int y) {
        this.m_y = y;
        this.m_computed = false;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
        this.m_computed = false;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
        this.m_computed = false;
    }
    
    public void setFlipVerticaly(final boolean flip) {
        this.m_flipVerticaly = flip;
        this.m_computed = false;
    }
    
    public void setFlipHorizontaly(final boolean flip) {
        this.m_flipHorizontaly = flip;
        this.m_computed = false;
    }
    
    public boolean isFlipHorizontaly() {
        return this.m_flipHorizontaly;
    }
    
    public boolean isFlipVerticaly() {
        return this.m_flipVerticaly;
    }
    
    public GeometrySprite.SpriteRotation getRotation() {
        return this.m_rotation;
    }
    
    public void setRotation(final GeometrySprite.SpriteRotation rotation) {
        if (rotation != this.m_rotation) {
            this.m_rotation = rotation;
            this.m_computed = false;
        }
    }
    
    public void setToFullTexture(final boolean useFullTexture) {
        this.m_useFullTexture = useFullTexture;
        this.m_computed = false;
    }
    
    public boolean usesFullTexture() {
        return this.m_useFullTexture;
    }
    
    public boolean needsCompute() {
        return !this.m_computed;
    }
    
    public void computeCoordinates() {
        if (this.m_texture == null) {
            return;
        }
        if (this.m_useFullTexture) {
            final Point2i textureInitialSize = this.m_texture.getSize();
            final Point2i textureSize = this.m_texture.getPowerOfTwoSize();
            final float width = textureSize.getX();
            final float height = textureSize.getY();
            this.m_x = 0;
            this.m_y = 0;
            this.m_width = textureInitialSize.getX();
            this.m_height = textureInitialSize.getY();
            this.m_top = 0.0f;
            this.m_left = 0.0f;
            this.m_bottom = this.m_height / height;
            this.m_right = this.m_width / width;
        }
        else {
            final Point2i textureSize2 = this.m_texture.getPowerOfTwoSize();
            final float width2 = textureSize2.getX();
            final float height2 = textureSize2.getY();
            this.m_left = this.m_x / width2;
            this.m_right = (this.m_x + this.m_width) / width2;
            this.m_top = this.m_y / height2;
            this.m_bottom = (this.m_y + this.m_height) / height2;
        }
        if (this.m_flipHorizontaly) {
            final float tmpLeft = this.m_left;
            this.m_left = this.m_right;
            this.m_right = tmpLeft;
        }
        if (this.m_flipVerticaly) {
            final float tmpTop = this.m_top;
            this.m_top = this.m_bottom;
            this.m_bottom = tmpTop;
        }
        this.m_computed = true;
        for (int i = this.m_clients.size() - 1; i >= 0; --i) {
            this.m_clients.get(i).pixmapChanged(this);
        }
    }
}
