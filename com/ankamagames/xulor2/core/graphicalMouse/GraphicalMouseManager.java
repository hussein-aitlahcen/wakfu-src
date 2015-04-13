package com.ankamagames.xulor2.core.graphicalMouse;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.alignment.*;
import gnu.trove.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.core.*;
import java.net.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.layout.*;

public class GraphicalMouseManager
{
    protected static final Logger m_logger;
    private static GraphicalMouseManager m_graphicalMouseManager;
    private Widget m_widget;
    private boolean m_show;
    private Alignment9 m_hotPoint;
    private int m_xOffset;
    private int m_yOffset;
    private int m_x;
    private int m_y;
    private byte m_currentFactoryType;
    protected TByteObjectHashMap<GraphicalMouseFactory> m_factories;
    
    protected GraphicalMouseManager() {
        super();
        this.m_show = false;
        this.m_hotPoint = Alignment9.SOUTH_WEST;
        this.m_xOffset = 0;
        this.m_yOffset = 0;
        this.m_x = 0;
        this.m_y = 0;
        this.m_currentFactoryType = -1;
        this.m_factories = new TByteObjectHashMap<GraphicalMouseFactory>();
        GraphicalMouseManager.m_graphicalMouseManager = this;
        this.m_factories.put((byte)1, new GraphicalMouseFactory<ImageData>() {
            @Override
            public Widget createWidget(final ImageData data) {
                if (data == null) {
                    return null;
                }
                if (data.getIconUrl() == null) {
                    return null;
                }
                return GraphicalMouseManager.this.getImageTemplate(data.getIconUrl());
            }
            
            @Override
            public void destroyWidget(final Widget w) {
                w.destroySelfFromParent();
            }
        });
        this.m_factories.put((byte)2, new GraphicalMouseFactory<TextData>() {
            @Override
            public Widget createWidget(final TextData data) {
                if (data == null) {
                    return null;
                }
                if (data.getText() == null) {
                    return null;
                }
                return GraphicalMouseManager.this.getTextTemplate(data.getText());
            }
            
            @Override
            public void destroyWidget(final Widget w) {
                w.destroySelfFromParent();
            }
        });
        this.m_factories.put((byte)3, new GraphicalMouseFactory<ImageTextData>() {
            @Override
            public Widget createWidget(final ImageTextData data) {
                if (data == null) {
                    return null;
                }
                if (data.getText() == null || data.getIconUrl() == null) {
                    return null;
                }
                return GraphicalMouseManager.this.getImageAndTextTemplate(data.getIconUrl(), data.getText());
            }
            
            @Override
            public void destroyWidget(final Widget w) {
                w.destroySelfFromParent();
            }
        });
    }
    
    public void setWidget(final Widget widget) {
        this.m_widget = widget;
    }
    
    public static GraphicalMouseManager getInstance() {
        if (GraphicalMouseManager.m_graphicalMouseManager == null) {
            GraphicalMouseManager.m_graphicalMouseManager = new GraphicalMouseManager();
        }
        return GraphicalMouseManager.m_graphicalMouseManager;
    }
    
    public void setXY(final int mouseX, final int mouseY) {
        this.m_x = mouseX;
        this.m_y = mouseY;
        this.updatePosition();
    }
    
    public void updatePosition() {
        if (this.m_widget != null) {
            final int x = this.m_x - this.m_hotPoint.getX(this.m_widget.getWidth()) + this.m_xOffset;
            final int y = this.m_y - this.m_hotPoint.getY(this.m_widget.getHeight()) + this.m_yOffset - this.m_widget.getHeight() / 2;
            this.m_widget.setPosition(x, y);
        }
    }
    
    public void showMouseInformation(final String iconUrl, final String text, final int xOffset, final int yOffset, final Alignment9 hotPointPosition) {
        final GraphicalMouseData data = this.createDataFrom(iconUrl, text);
        if (data == null) {
            GraphicalMouseManager.m_logger.warn((Object)("Impossible de cr\u00e9er les data pour iconUrl=" + iconUrl + " et text=" + text));
            return;
        }
        this.showMouseInformation(data, xOffset, yOffset, hotPointPosition);
    }
    
    public void showMouseInformation(final GraphicalMouseData data, final int xOffset, final int yOffset, final Alignment9 hotPointPosition) {
        this.hide();
        this.m_currentFactoryType = data.getType();
        final GraphicalMouseFactory factory = this.m_factories.get(data.getType());
        if (factory == null) {
            return;
        }
        Widget widget;
        try {
            widget = factory.createWidget(data);
        }
        catch (Exception e) {
            GraphicalMouseManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            return;
        }
        if (widget == null) {
            return;
        }
        this.setWidget(widget);
        this.setXOffset(xOffset);
        this.setYOffset(yOffset);
        this.setHotPoint(hotPointPosition);
        this.show();
    }
    
    private GraphicalMouseData createDataFrom(final String iconUrl, final String text) {
        if (iconUrl != null && text != null) {
            return new ImageTextData(iconUrl, text);
        }
        if (iconUrl != null) {
            return new ImageData(iconUrl);
        }
        if (text != null) {
            return new TextData(text);
        }
        return null;
    }
    
    public void show() {
        if (this.m_show) {
            return;
        }
        final LayeredContainer display = Xulor.getInstance().getScene().getMasterRootContainer().getLayeredContainer();
        display.addWidgetToLayer(this.m_widget, 40000);
        this.m_show = true;
    }
    
    public void hide() {
        if (this.m_widget == null || !this.m_show) {
            return;
        }
        final GraphicalMouseFactory factory = this.m_factories.get(this.m_currentFactoryType);
        if (factory == null) {
            return;
        }
        factory.destroyWidget(this.m_widget);
        this.m_widget = null;
        this.m_show = false;
    }
    
    public Alignment9 getHotPoint() {
        return this.m_hotPoint;
    }
    
    public void setHotPoint(final Alignment9 hotPoint) {
        this.m_hotPoint = hotPoint;
        this.updatePosition();
    }
    
    public int getXOffset() {
        return this.m_xOffset;
    }
    
    public void setXOffset(final int offset) {
        this.m_xOffset = offset;
        this.updatePosition();
    }
    
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    public void setYOffset(final int offset) {
        this.m_yOffset = offset;
        this.updatePosition();
    }
    
    private Widget getImageTemplate(final String iconUrl) {
        return this.getImageAndTextTemplate(iconUrl, null, null);
    }
    
    private Widget getTextTemplate(final String text) {
        return this.getImageAndTextTemplate(null, text, null);
    }
    
    private Widget getImageAndTextTemplate(final String iconUrl, final String text) {
        return this.getImageAndTextTemplate(iconUrl, text, null);
    }
    
    private Widget getImageTemplate(final String iconUrl, ElementMap elementMap) {
        if (elementMap == null) {
            elementMap = this.createElementMap();
        }
        try {
            final Image image = new Image();
            final URL url = ContentFileHelper.getURL(iconUrl);
            final PixmapElement pixmapElement = new PixmapElement();
            pixmapElement.onCheckOut();
            pixmapElement.setPixmap(new Pixmap(TextureLoader.getInstance().loadTexture(url)));
            image.onCheckOut();
            image.setNonBlocking(true);
            image.setExpandable(false);
            image.add(pixmapElement);
            image.computeMinSize();
            image.setId("image");
            elementMap.add(image.getId(), image);
            image.setElementMap(elementMap);
            image.setSizeToPrefSize();
            return image;
        }
        catch (MalformedURLException e) {
            GraphicalMouseManager.m_logger.warn((Object)("URL malform\u00e9e : \"" + iconUrl + "\""));
            return null;
        }
    }
    
    private Widget getTextTemplate(final String text, ElementMap elementMap) {
        if (elementMap == null) {
            elementMap = this.createElementMap();
        }
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setExpandable(false);
        textView.setNonBlocking(true);
        textView.setStyle("White14Bordered");
        textView.setText(text);
        textView.setSizeToPrefSize();
        textView.setId("text");
        elementMap.add(textView.getId(), textView);
        textView.setElementMap(elementMap);
        textView.onChildrenAdded();
        return textView;
    }
    
    private ElementMap createElementMap() {
        return new ElementMap("test", Xulor.getInstance().getEnvironment());
    }
    
    private Widget getImageAndTextTemplate(final String iconUrl, final String text, ElementMap elementMap) {
        if (elementMap == null) {
            elementMap = this.createElementMap();
        }
        final RowLayout rowLayout = new RowLayout();
        rowLayout.onCheckOut();
        rowLayout.setAlign(Alignment9.NORTH);
        rowLayout.setHorizontal(false);
        final Container container = new Container();
        container.onCheckOut();
        container.setLayoutManager(rowLayout);
        if (iconUrl != null) {
            final Widget image = this.getImageTemplate(iconUrl, elementMap);
            final RowLayoutData data = new RowLayoutData();
            data.onCheckOut();
            data.setAlign(Alignment9.WEST);
            image.setExpandable(false);
            image.setLayoutData(data);
            container.add(image);
        }
        if (text != null) {
            final Widget textWidget = this.getTextTemplate(text, elementMap);
            final RowLayoutData data = new RowLayoutData();
            data.onCheckOut();
            data.setAlign(Alignment9.WEST);
            textWidget.setExpandable(false);
            textWidget.setLayoutData(data);
            container.add(textWidget);
        }
        container.setElementMap(elementMap);
        container.setPack(true);
        container.onAttributesInitialized();
        container.onChildrenAdded();
        return container;
    }
    
    public void setText(final String text) {
        if (this.m_widget == null) {
            return;
        }
        final EventDispatcher eventDispatcher = this.m_widget.getElementMap().getElement("text");
        if (eventDispatcher == null) {
            return;
        }
        ((TextView)eventDispatcher).setText(text);
        this.updatePosition();
    }
    
    public void setIconUrl(final String iconUrl) {
        if (this.m_widget == null) {
            return;
        }
        final EventDispatcher eventDispatcher = this.m_widget.getElementMap().getElement("image");
        if (eventDispatcher == null) {
            return;
        }
        try {
            final URL url = ContentFileHelper.getURL(iconUrl);
            final PixmapElement pixmapElement = new PixmapElement();
            pixmapElement.onCheckOut();
            pixmapElement.setPixmap(new Pixmap(TextureLoader.getInstance().loadTexture(url)));
            eventDispatcher.add(pixmapElement);
            this.updatePosition();
        }
        catch (MalformedURLException e) {
            GraphicalMouseManager.m_logger.warn((Object)("URL malform\u00e9e : \"" + iconUrl + "\""));
        }
    }
    
    public boolean isNull() {
        return this.m_widget == null;
    }
    
    public boolean isVisible() {
        return this.m_widget != null && this.m_widget.getVisible();
    }
    
    static {
        m_logger = Logger.getLogger((Class)GraphicalMouseManager.class);
    }
}
