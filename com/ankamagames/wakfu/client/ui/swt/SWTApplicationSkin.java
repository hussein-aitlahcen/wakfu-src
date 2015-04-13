package com.ankamagames.wakfu.client.ui.swt;

import org.apache.log4j.*;
import org.eclipse.swt.graphics.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class SWTApplicationSkin
{
    public static final Logger m_logger;
    private static final String EXTENSION = ".png";
    private static final String SKIN_BORDER_TOP_LEFT = "BorderTopLeftSimple";
    private static final String SKIN_BORDER_TOP = "BorderTop";
    private static final String SKIN_BORDER_TOP_RIGHT = "BorderTopRight";
    private static final String SKIN_BORDER_RIGHT = "BorderRight";
    private static final String SKIN_BORDER_BOTTOM_RIGHT = "BorderBottomRight";
    private static final String SKIN_BORDER_BOTTOM = "BorderBottom";
    private static final String SKIN_BORDER_BOTTOM_LEFT = "BorderBottomLeft";
    private static final String SKIN_BORDER_LEFT = "BorderLeft";
    private static final String SKIN_BTN_CLOSE = "BtnCloseDefault";
    private static final String SKIN_BTN_CLOSE_OVER = "BtnCloseOver";
    private final String m_skinPath;
    public final String m_skinSuffix;
    private final ImageData m_topLeftImage;
    private final ImageData m_topImage;
    private final ImageData m_topRightImage;
    private final ImageData m_leftImage;
    private final ImageData m_rightImage;
    private final ImageData m_bottomLeftImage;
    private final ImageData m_bottomImage;
    private final ImageData m_bottomRightImage;
    private final ImageData m_closeIcon;
    private final ImageData m_closeOverIcon;
    
    public SWTApplicationSkin(final String skinPath) {
        this(skinPath, "", null);
    }
    
    public SWTApplicationSkin(final String skinPath, final String suffix, final SWTApplicationSkin defaultValues) {
        super();
        this.m_skinPath = skinPath;
        this.m_skinSuffix = suffix;
        this.m_topLeftImage = this.loadIcon("BorderTopLeftSimple", (defaultValues != null) ? defaultValues.getTopLeftImage() : null);
        this.m_topImage = this.loadIcon("BorderTop", (defaultValues != null) ? defaultValues.getTopImage() : null);
        this.m_topRightImage = this.loadIcon("BorderTopRight", (defaultValues != null) ? defaultValues.getTopRightImage() : null);
        this.m_leftImage = this.loadIcon("BorderLeft", (defaultValues != null) ? defaultValues.getLeftImage() : null);
        this.m_rightImage = this.loadIcon("BorderRight", (defaultValues != null) ? defaultValues.getRightImage() : null);
        this.m_bottomLeftImage = this.loadIcon("BorderBottomLeft", (defaultValues != null) ? defaultValues.getBottomLeftImage() : null);
        this.m_bottomImage = this.loadIcon("BorderBottom", (defaultValues != null) ? defaultValues.getBottomImage() : null);
        this.m_bottomRightImage = this.loadIcon("BorderBottomRight", (defaultValues != null) ? defaultValues.getBottomRightImage() : null);
        this.m_closeIcon = this.loadIcon("BtnCloseDefault", (defaultValues != null) ? defaultValues.getCloseButtonImage() : null);
        this.m_closeOverIcon = this.loadIcon("BtnCloseOver", (defaultValues != null) ? defaultValues.getCloseButtonOverImage() : null);
    }
    
    private ImageData loadIcon(final String iconName, final ImageData defaultValue) {
        try {
            final String path = this.m_skinPath + iconName + this.m_skinSuffix + ".png";
            return new ImageData(ContentFileHelper.openFile(path));
        }
        catch (IOException e) {
            SWTApplicationSkin.m_logger.error((Object)("Skin image not present : " + this.m_skinPath + iconName));
            return defaultValue;
        }
    }
    
    public ImageData getBottomImage() {
        return this.m_bottomImage;
    }
    
    public ImageData getBottomLeftImage() {
        return this.m_bottomLeftImage;
    }
    
    public ImageData getBottomRightImage() {
        return this.m_bottomRightImage;
    }
    
    public ImageData getCloseButtonImage() {
        return this.m_closeIcon;
    }
    
    public ImageData getCloseButtonOverImage() {
        return this.m_closeOverIcon;
    }
    
    public ImageData getLeftImage() {
        return this.m_leftImage;
    }
    
    public ImageData getRightImage() {
        return this.m_rightImage;
    }
    
    public ImageData getTopImage() {
        return this.m_topImage;
    }
    
    public ImageData getTopLeftImage() {
        return this.m_topLeftImage;
    }
    
    public ImageData getTopRightImage() {
        return this.m_topRightImage;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SWTApplicationSkin.class);
    }
}
