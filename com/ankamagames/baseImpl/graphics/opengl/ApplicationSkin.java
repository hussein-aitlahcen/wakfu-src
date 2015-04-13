package com.ankamagames.baseImpl.graphics.opengl;

import org.apache.log4j.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.net.*;

public class ApplicationSkin
{
    public static final Logger m_logger;
    private static final String EXTENSION = ".png";
    private static final String SKIN_BORDER_TOP_LEFT = "BorderTopLeft";
    private static final String SKIN_BORDER_TOP = "BorderTop";
    private static final String SKIN_BORDER_TOP_RIGHT = "BorderTopRight";
    private static final String SKIN_BORDER_RIGHT = "BorderRight";
    private static final String SKIN_BORDER_BOTTOM_RIGHT = "BorderBottomRight";
    private static final String SKIN_BORDER_BOTTOM = "BorderBottom";
    private static final String SKIN_BORDER_BOTTOM_LEFT = "BorderBottomLeft";
    private static final String SKIN_BORDER_LEFT = "BorderLeft";
    private static final String SKIN_BTN_CLOSE = "BtnCloseDefault";
    private static final String SKIN_BTN_CLOSE_OVER = "BtnCloseOver";
    private static final String SKIN_BTN_MINIMIZE = "BtnMinimizeDefault";
    private static final String SKIN_BTN_MINIMIZE_OVER = "BtnMinimizeOver";
    private static final String SKIN_BTN_MAXIMIZE = "BtnMaximizeDefault";
    private static final String SKIN_BTN_MAXIMIZE_OVER = "BtnMaximizeOver";
    private static final String SKIN_BTN_RESTORE = "BtnRestoreDefault";
    private static final String SKIN_BTN_RESTORE_OVER = "BtnRestoreOver";
    private static final String SKIN_TITLE_BAR_2_TOP = "TitleBarTop2";
    private static final String SKIN_TITLE_BAR_2_BOTTOM = "TitleBarBottom2";
    private static final String SKIN_TITLE_BAR_2_CENTER = "TitleBarCenter2";
    private final String m_skinPath;
    public final String m_skinSuffix;
    private final ImageIcon m_titleBarTop2Icon;
    private final Image m_titleBarTop2Image;
    private final ImageIcon m_titleBarBottom2Icon;
    private final Image m_titleBarBottom2Image;
    private final ImageIcon m_titleBarCenter2Icon;
    private final Image m_titleBarCenter2Image;
    private final ImageIcon m_topLeftIcon;
    private final Image m_topLeftImage;
    private final ImageIcon m_topIcon;
    private final Image m_topImage;
    private final ImageIcon m_topRightIcon;
    private final Image m_topRightImage;
    private final ImageIcon m_leftIcon;
    private final Image m_leftImage;
    private final ImageIcon m_bottomLeftIcon;
    private final Image m_bottomLeftImage;
    private final ImageIcon m_bottomIcon;
    private final Image m_bottomImage;
    private final ImageIcon m_bottomRightIcon;
    private final Image m_bottomRightImage;
    private final ImageIcon m_rightIcon;
    private final Image m_rightImage;
    private final ImageIcon m_closeIcon;
    private final ImageIcon m_closeOverIcon;
    private final ImageIcon m_minimizeIcon;
    private final ImageIcon m_minimizeOverIcon;
    private final ImageIcon m_maximizeIcon;
    private final ImageIcon m_maximizeOverIcon;
    private final ImageIcon m_restoreIcon;
    private final ImageIcon m_restoreOverIcon;
    private final Insets m_borderInsets;
    
    public ApplicationSkin(final String skinPath) {
        this(skinPath, "", null);
    }
    
    public ApplicationSkin(final String skinPath, final String suffix, final ApplicationSkin defaultValues) {
        super();
        this.m_skinPath = skinPath;
        this.m_skinSuffix = suffix;
        this.m_titleBarTop2Icon = this.loadIcon("TitleBarTop2", (defaultValues != null) ? defaultValues.getTitleBarTop2Icon() : null);
        this.m_titleBarTop2Image = this.m_titleBarTop2Icon.getImage();
        this.m_titleBarBottom2Icon = this.loadIcon("TitleBarBottom2", (defaultValues != null) ? defaultValues.getTitleBarBottom2Icon() : null);
        this.m_titleBarBottom2Image = this.m_titleBarBottom2Icon.getImage();
        this.m_titleBarCenter2Icon = this.loadIcon("TitleBarCenter2", (defaultValues != null) ? defaultValues.getTitleBarCenter2Icon() : null);
        this.m_titleBarCenter2Image = this.m_titleBarCenter2Icon.getImage();
        this.m_topLeftIcon = this.loadIcon("BorderTopLeft", (defaultValues != null) ? defaultValues.getTopLeftIcon() : null);
        this.m_topLeftImage = this.m_topLeftIcon.getImage();
        final ImageIcon topIcon = this.loadIcon("BorderTop", (defaultValues != null) ? defaultValues.getTopIcon() : null);
        if (topIcon.getImageLoadStatus() != 8) {
            final BufferedImage img = new BufferedImage(1, 18, 1);
            for (int x = 0; x < img.getWidth(); ++x) {
                final int maxY = img.getHeight() - 1;
                img.setRGB(x, 0, 0);
                img.setRGB(x, maxY, 0);
                for (int y = 1; y < maxY; ++y) {
                    img.setRGB(x, y, 255);
                }
            }
            this.m_topIcon = new ImageIcon(img);
        }
        else {
            this.m_topIcon = topIcon;
        }
        this.m_topImage = this.m_topIcon.getImage();
        this.m_topRightIcon = this.loadIcon("BorderTopRight", (defaultValues != null) ? defaultValues.getTopRightIcon() : null);
        this.m_topRightImage = this.m_topRightIcon.getImage();
        this.m_leftIcon = this.loadIcon("BorderLeft", (defaultValues != null) ? defaultValues.getLeftIcon() : null);
        this.m_leftImage = this.m_leftIcon.getImage();
        this.m_bottomLeftIcon = this.loadIcon("BorderBottomLeft", (defaultValues != null) ? defaultValues.getBottomLeftIcon() : null);
        this.m_bottomLeftImage = this.m_bottomLeftIcon.getImage();
        this.m_bottomIcon = this.loadIcon("BorderBottom", (defaultValues != null) ? defaultValues.getBottomIcon() : null);
        this.m_bottomImage = this.m_bottomIcon.getImage();
        this.m_bottomRightIcon = this.loadIcon("BorderBottomRight", (defaultValues != null) ? defaultValues.getBottomRightIcon() : null);
        this.m_bottomRightImage = this.m_bottomRightIcon.getImage();
        this.m_rightIcon = this.loadIcon("BorderRight", (defaultValues != null) ? defaultValues.getRightIcon() : null);
        this.m_rightImage = this.m_rightIcon.getImage();
        this.m_closeIcon = this.loadIcon("BtnCloseDefault", (defaultValues != null) ? defaultValues.getCloseIcon() : null);
        this.m_closeOverIcon = this.loadIcon("BtnCloseOver", (defaultValues != null) ? defaultValues.getCloseOverIcon() : null);
        this.m_minimizeIcon = this.loadIcon("BtnMinimizeDefault", (defaultValues != null) ? defaultValues.getMinimizeIcon() : null);
        this.m_minimizeOverIcon = this.loadIcon("BtnMinimizeOver", (defaultValues != null) ? defaultValues.getMinimizeOverIcon() : null);
        this.m_maximizeIcon = this.loadIcon("BtnMaximizeDefault", (defaultValues != null) ? defaultValues.getMaximizeIcon() : null);
        this.m_maximizeOverIcon = this.loadIcon("BtnMaximizeOver", (defaultValues != null) ? defaultValues.getMaximizeOverIcon() : null);
        this.m_restoreIcon = this.loadIcon("BtnRestoreDefault", (defaultValues != null) ? defaultValues.getRestoreIcon() : null);
        this.m_restoreOverIcon = this.loadIcon("BtnRestoreOver", (defaultValues != null) ? defaultValues.getRestoreOverIcon() : null);
        final int topLeftH = this.m_topLeftImage.getHeight(null);
        final int topRightH = this.m_topRightImage.getHeight(null);
        final int topH = this.m_topImage.getHeight(null);
        final int titleBarHeight = Math.max(Math.max(topLeftH, topRightH), topH);
        if (topLeftH != titleBarHeight) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : top left image height not consistent : " + topLeftH + " != " + titleBarHeight));
        }
        if (topRightH != titleBarHeight) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : top right image height not consistent : " + topRightH + " != " + titleBarHeight));
        }
        if (topH != titleBarHeight) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : top image height not consistent : " + topH + " != " + titleBarHeight));
        }
        final int leftW = this.m_leftImage.getWidth(null);
        final int bottomLeftW = this.m_bottomLeftImage.getWidth(null);
        final int leftBorderWidth = Math.max(leftW, bottomLeftW);
        if (leftW != leftBorderWidth) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : left image width not consistent : " + leftW + " != " + leftBorderWidth));
        }
        if (bottomLeftW != leftBorderWidth) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : bottom left image width not consistent : " + bottomLeftW + " != " + leftBorderWidth));
        }
        final int bottomLeftH = this.m_bottomLeftImage.getHeight(null);
        final int bottomH = this.m_bottomImage.getHeight(null);
        final int bottomRightH = this.m_bottomRightImage.getHeight(null);
        final int bottomBorderHeight = Math.max(Math.max(bottomLeftH, bottomH), bottomRightH);
        if (bottomLeftH != bottomBorderHeight) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : bottom left image height not consistent : " + bottomLeftH + " != " + bottomBorderHeight));
        }
        if (bottomH != bottomBorderHeight) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : bottom image height not consistent : " + bottomH + " != " + bottomBorderHeight));
        }
        if (bottomRightH != bottomBorderHeight) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : bottom right image height not consistent : " + bottomRightH + " != " + bottomBorderHeight));
        }
        final int rightW = this.m_rightImage.getWidth(null);
        final int bottomRightW = this.m_bottomRightImage.getWidth(null);
        final int rightBorderWidth = Math.max(rightW, bottomRightW);
        if (rightW != rightBorderWidth) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : right image width not consistent : " + rightW + " != " + rightBorderWidth));
        }
        if (bottomRightW != rightBorderWidth) {
            ApplicationSkin.m_logger.warn((Object)("Skin pbm : bottom right image width not consistent : " + bottomRightW + " != " + rightBorderWidth));
        }
        this.m_borderInsets = new Insets(titleBarHeight, leftBorderWidth, bottomBorderHeight, rightBorderWidth);
    }
    
    private ImageIcon loadIcon(final String iconName, final ImageIcon defaultValue) {
        ImageIcon icon;
        try {
            final URL url = ContentFileHelper.getURL(this.m_skinPath + iconName + this.m_skinSuffix + ".png");
            icon = new ImageIcon(url);
        }
        catch (MalformedURLException e) {
            icon = new ImageIcon(this.m_skinPath + iconName + this.m_skinSuffix + ".png");
        }
        if (icon.getImageLoadStatus() != 8) {
            if (defaultValue != null) {
                icon = defaultValue;
            }
            else {
                ApplicationSkin.m_logger.error((Object)("Skin image not present : " + this.m_skinPath + iconName));
            }
        }
        return icon;
    }
    
    public ImageIcon getTitleBarTop2Icon() {
        return this.m_titleBarTop2Icon;
    }
    
    public Image getTitleBarTop2Image() {
        return this.m_titleBarTop2Image;
    }
    
    public ImageIcon getTitleBarBottom2Icon() {
        return this.m_titleBarBottom2Icon;
    }
    
    public Image getTitleBarBottom2Image() {
        return this.m_titleBarBottom2Image;
    }
    
    public ImageIcon getTitleBarCenter2Icon() {
        return this.m_titleBarCenter2Icon;
    }
    
    public Image getTitleBarCenter2Image() {
        return this.m_titleBarCenter2Image;
    }
    
    public ImageIcon getTopLeftIcon() {
        return this.m_topLeftIcon;
    }
    
    public Image getTopLeftImage() {
        return this.m_topLeftImage;
    }
    
    public ImageIcon getTopIcon() {
        return this.m_topIcon;
    }
    
    public Image getTopImage() {
        return this.m_topImage;
    }
    
    public ImageIcon getTopRightIcon() {
        return this.m_topRightIcon;
    }
    
    public Image getTopRightImage() {
        return this.m_topRightImage;
    }
    
    public ImageIcon getLeftIcon() {
        return this.m_leftIcon;
    }
    
    public Image getLeftImage() {
        return this.m_leftImage;
    }
    
    public ImageIcon getBottomLeftIcon() {
        return this.m_bottomLeftIcon;
    }
    
    public Image getBottomLeftImage() {
        return this.m_bottomLeftImage;
    }
    
    public ImageIcon getBottomIcon() {
        return this.m_bottomIcon;
    }
    
    public Image getBottomImage() {
        return this.m_bottomImage;
    }
    
    public ImageIcon getBottomRightIcon() {
        return this.m_bottomRightIcon;
    }
    
    public Image getBottomRightImage() {
        return this.m_bottomRightImage;
    }
    
    public ImageIcon getRightIcon() {
        return this.m_rightIcon;
    }
    
    public Image getRightImage() {
        return this.m_rightImage;
    }
    
    public ImageIcon getCloseIcon() {
        return this.m_closeIcon;
    }
    
    public ImageIcon getCloseOverIcon() {
        return this.m_closeOverIcon;
    }
    
    public ImageIcon getMinimizeIcon() {
        return this.m_minimizeIcon;
    }
    
    public ImageIcon getMinimizeOverIcon() {
        return this.m_minimizeOverIcon;
    }
    
    public ImageIcon getMaximizeIcon() {
        return this.m_maximizeIcon;
    }
    
    public ImageIcon getMaximizeOverIcon() {
        return this.m_maximizeOverIcon;
    }
    
    public ImageIcon getRestoreIcon() {
        return this.m_restoreIcon;
    }
    
    public ImageIcon getRestoreOverIcon() {
        return this.m_restoreOverIcon;
    }
    
    public Insets getBorderInsets() {
        return this.m_borderInsets;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ApplicationSkin.class);
    }
}
