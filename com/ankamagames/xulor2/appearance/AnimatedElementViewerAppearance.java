package com.ankamagames.xulor2.appearance;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public final class AnimatedElementViewerAppearance extends DecoratorAppearance
{
    public static final String TAG = "AnimatedElementViewerAppearance";
    private boolean m_flipHorizontal;
    private boolean m_flipVertical;
    public static final int DIRECTION_HASH;
    public static final int FILE_PATH_HASH;
    public static final int ANIM_NAME_HASH;
    public static final int OFFSET_X_HASH;
    public static final int OFFSET_Y_HASH;
    public static final int SCALE_HASH;
    public static final int USE_DEFAULT_MATERIAL_HASH;
    public static final int USE_BLEND_PREMULT;
    public static final int FLIP_HORIZONTAL_HASH;
    public static final int FLIP_VERTICAL_HASH;
    private String m_filePath;
    private String m_animName;
    private float m_offsetX;
    private float m_offsetY;
    private float m_scale;
    private int m_direction;
    private boolean m_useBlendPremult;
    
    public AnimatedElementViewerAppearance() {
        super();
        this.m_useBlendPremult = true;
    }
    
    @Override
    public String getTag() {
        return "AnimatedElementViewerAppearance";
    }
    
    public final String getFilePath() {
        return this.m_filePath;
    }
    
    public final void setFilePath(final String filePath) {
        this.m_filePath = filePath;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setFilePath(filePath);
        }
    }
    
    public String getAnimName() {
        return this.m_animName;
    }
    
    public void setAnimName(final String animName) {
        this.m_animName = animName;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setAnimName(this.m_animName);
        }
    }
    
    public float getOffsetX() {
        return this.m_offsetX;
    }
    
    public void setOffsetX(final float offsetX) {
        this.m_offsetX = offsetX;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setOffsetX(this.m_offsetX);
        }
    }
    
    public float getOffsetY() {
        return this.m_offsetY;
    }
    
    public void setOffsetY(final float offsetY) {
        this.m_offsetY = offsetY;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setOffsetY(this.m_offsetY);
        }
    }
    
    public float getScale() {
        return this.m_scale;
    }
    
    public void setScale(final float scale) {
        this.m_scale = scale;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setScale(this.m_scale);
        }
    }
    
    public void setFlipHorizontal(final boolean flipHorizontal) {
        this.m_flipHorizontal = flipHorizontal;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setFlipHorizontal(this.m_flipHorizontal);
        }
    }
    
    public void setFlipVertical(final boolean flipVertical) {
        this.m_flipVertical = flipVertical;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setFlipVertical(this.m_flipVertical);
        }
    }
    
    public int getDirection() {
        return this.m_direction;
    }
    
    public void setDirection(final int direction) {
        this.m_direction = direction;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setDirection(this.m_direction);
        }
    }
    
    public void setUseBlendPremult(final boolean useBlendPremult) {
        this.m_useBlendPremult = useBlendPremult;
        if (this.m_widget != null) {
            this.getAnimatedElementViewer().setUseBlendPremult(this.m_useBlendPremult);
        }
    }
    
    public final AnimatedElementViewer getAnimatedElementViewer() {
        return (AnimatedElementViewer)this.m_widget;
    }
    
    @Override
    public final void setWidget(final Widget widget) {
        super.setWidget(widget);
        final AnimatedElementViewer animatedElementViewer = this.getAnimatedElementViewer();
        animatedElementViewer.setFilePath(this.m_filePath);
        animatedElementViewer.setAnimName(this.m_animName);
        animatedElementViewer.setOffsetX(this.m_offsetX);
        animatedElementViewer.setOffsetY(this.m_offsetY);
        animatedElementViewer.setScale(this.m_scale);
        animatedElementViewer.setFlipHorizontal(this.m_flipHorizontal);
        animatedElementViewer.setFlipVertical(this.m_flipVertical);
        animatedElementViewer.setDirection(this.m_direction);
        animatedElementViewer.setUseBlendPremult(this.m_useBlendPremult);
    }
    
    @Override
    public final void copyElement(final BasicElement d) {
        final AnimatedElementViewerAppearance e = (AnimatedElementViewerAppearance)d;
        super.copyElement(e);
        if (this.m_filePath != null) {
            e.setFilePath(this.m_filePath);
            e.setAnimName(this.m_animName);
            e.setOffsetX(this.m_offsetX);
            e.setOffsetY(this.m_offsetY);
            e.setScale(this.m_scale);
            e.setFlipHorizontal(this.m_flipHorizontal);
            e.setFlipVertical(this.m_flipVertical);
            e.setDirection(this.m_direction);
            e.setUseBlendPremult(this.m_useBlendPremult);
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AnimatedElementViewerAppearance.FILE_PATH_HASH) {
            this.setFilePath(value);
        }
        else if (hash == AnimatedElementViewerAppearance.ANIM_NAME_HASH) {
            this.setAnimName(value);
        }
        else if (hash == AnimatedElementViewerAppearance.DIRECTION_HASH) {
            this.setDirection(PrimitiveConverter.getInteger(value));
        }
        else if (hash == AnimatedElementViewerAppearance.OFFSET_X_HASH) {
            this.setOffsetX(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewerAppearance.OFFSET_Y_HASH) {
            this.setOffsetY(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewerAppearance.SCALE_HASH) {
            this.setScale(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewerAppearance.USE_BLEND_PREMULT) {
            this.setUseBlendPremult(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewerAppearance.FLIP_HORIZONTAL_HASH) {
            this.setFlipHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AnimatedElementViewerAppearance.FLIP_VERTICAL_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setFlipVertical(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AnimatedElementViewerAppearance.FILE_PATH_HASH) {
            this.setFilePath((String)value);
        }
        else if (hash == AnimatedElementViewerAppearance.ANIM_NAME_HASH) {
            this.setAnimName((String)value);
        }
        else if (hash == AnimatedElementViewerAppearance.DIRECTION_HASH) {
            this.setDirection(PrimitiveConverter.getInteger(value));
        }
        else if (hash == AnimatedElementViewerAppearance.OFFSET_X_HASH) {
            this.setOffsetX(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewerAppearance.OFFSET_Y_HASH) {
            this.setOffsetY(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewerAppearance.SCALE_HASH) {
            this.setScale(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewerAppearance.USE_BLEND_PREMULT) {
            this.setUseBlendPremult(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewerAppearance.FLIP_HORIZONTAL_HASH) {
            this.setFlipHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AnimatedElementViewerAppearance.FLIP_VERTICAL_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setFlipVertical(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        DIRECTION_HASH = "direction".hashCode();
        FILE_PATH_HASH = "filePath".hashCode();
        ANIM_NAME_HASH = "animName".hashCode();
        OFFSET_X_HASH = "offsetX".hashCode();
        OFFSET_Y_HASH = "offsetY".hashCode();
        SCALE_HASH = "scale".hashCode();
        USE_DEFAULT_MATERIAL_HASH = "useDefaultMaterial".hashCode();
        USE_BLEND_PREMULT = "blendPremult".hashCode();
        FLIP_HORIZONTAL_HASH = "flipHorizontal".hashCode();
        FLIP_VERTICAL_HASH = "flipVertical".hashCode();
    }
}
