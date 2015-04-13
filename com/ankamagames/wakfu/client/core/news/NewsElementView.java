package com.ankamagames.wakfu.client.core.news;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.news.*;
import java.net.*;

public abstract class NewsElementView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    protected NewsElement m_newsElement;
    private Container m_container;
    private Container m_containerBackground;
    
    public NewsElementView(final NewsElement newsElement) {
        super();
        this.m_newsElement = newsElement;
    }
    
    public NewsElement getNewsElement() {
        return this.m_newsElement;
    }
    
    public abstract void togglePlayPauseVideo();
    
    public void checkTimingVisibility(final long duration) {
        final NewsElementTiming timing = this.m_newsElement.getTiming();
        final boolean visible = timing.equals(NewsElementTiming.ALWAYS) || (duration >= timing.getStart() && (duration < timing.getEnd() || timing.getEnd() == -1));
        if (this.m_container.isVisible() != visible) {
            this.m_container.setVisible(visible);
        }
    }
    
    public Container build(final Container newsContainer, final NewsDisplayer newsDisplayer) {
        final Rect area = this.m_newsElement.getArea();
        this.m_container = Container.checkOut();
        final StaticLayoutData staticLayoutData = new StaticLayoutData();
        staticLayoutData.onCheckOut();
        staticLayoutData.setSize(new Dimension(area.width(), area.height()));
        staticLayoutData.setX(area.getXMin());
        final double containerHeight = ((StaticLayoutData)newsContainer.getLayoutData()).getSize().getHeight();
        staticLayoutData.setY((int)(containerHeight - area.getYMax()));
        this.m_container.setLayoutData(staticLayoutData);
        final StaticLayout sl = new StaticLayout();
        sl.onCheckOut();
        sl.setAdaptToContentSize(true);
        this.m_container.setLayoutManager(sl);
        final NewsElementBackground background = this.m_newsElement.getBackground();
        this.m_containerBackground = null;
        if (background != null) {
            this.m_containerBackground = Container.checkOut();
            final StaticLayoutData sld2 = new StaticLayoutData();
            sld2.onCheckOut();
            sld2.setSize(new Dimension(100.0f, 100.0f));
            sld2.setAlign(Alignment17.CENTER);
            this.m_containerBackground.setLayoutData(sld2);
            if (background.hasImage()) {
                try {
                    final URL url = background.getImage().getFile().toURL();
                    final Pixmap pixmap = new Pixmap(TextureLoader.getInstance().loadTexture(url));
                    final PixmapElement pixmapElement = new PixmapElement();
                    pixmapElement.onCheckOut();
                    pixmapElement.setPixmap(pixmap);
                    final Image image = new Image();
                    image.onCheckOut();
                    image.setNonBlocking(true);
                    image.setExpandable(false);
                    image.add(pixmapElement);
                    image.onChildrenAdded();
                    image.computeMinSize();
                    image.setSizeToPrefSize();
                    this.m_containerBackground.add(image);
                    this.m_containerBackground.onChildrenAdded();
                }
                catch (MalformedURLException e) {
                    NewsElementView.m_logger.warn((Object)("URL malform\u00e9e : \"" + background.getImage().getFile() + "\""));
                }
            }
            else {
                final PlainBackground pb = new PlainBackground();
                pb.onCheckOut();
                pb.setColor(background.getColor());
                this.m_containerBackground.getAppearance().add(pb);
            }
            this.m_container.add(this.m_containerBackground);
            this.m_container.onChildrenAdded();
        }
        final NewsElementBackground backgroundOver = this.m_newsElement.getBackgroundOver();
        if (backgroundOver != null) {
            final Container containerBackgroundOver = Container.checkOut();
            final StaticLayoutData sld2 = new StaticLayoutData();
            sld2.onCheckOut();
            sld2.setSize(new Dimension(100.0f, 100.0f));
            sld2.setAlign(Alignment17.CENTER);
            containerBackgroundOver.setLayoutData(sld2);
            containerBackgroundOver.setVisible(false);
            if (backgroundOver.hasImage()) {
                try {
                    final URL url2 = backgroundOver.getImage().getFile().toURL();
                    final Pixmap pixmap2 = new Pixmap(TextureLoader.getInstance().loadTexture(url2));
                    final PixmapElement pixmapElement2 = new PixmapElement();
                    pixmapElement2.onCheckOut();
                    pixmapElement2.setPixmap(pixmap2);
                    final Image image2 = new Image();
                    image2.onCheckOut();
                    image2.setNonBlocking(true);
                    image2.setExpandable(false);
                    image2.add(pixmapElement2);
                    image2.onChildrenAdded();
                    image2.computeMinSize();
                    image2.setSizeToPrefSize();
                    containerBackgroundOver.add(image2);
                    containerBackgroundOver.onChildrenAdded();
                }
                catch (MalformedURLException e2) {
                    NewsElementView.m_logger.warn((Object)("URL malform\u00e9e : \"" + backgroundOver.getImage().getFile() + "\""));
                }
            }
            else {
                final PlainBackground pb2 = new PlainBackground();
                pb2.onCheckOut();
                pb2.setColor(backgroundOver.getColor());
                containerBackgroundOver.getAppearance().add(pb2);
            }
            this.m_container.add(containerBackgroundOver);
            this.m_container.onChildrenAdded();
            this.m_container.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    if (NewsElementView.this.m_containerBackground != null) {
                        NewsElementView.this.m_containerBackground.setVisible(false);
                    }
                    containerBackgroundOver.setVisible(true);
                    return false;
                }
            }, false);
            this.m_container.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    if (NewsElementView.this.m_containerBackground != null) {
                        NewsElementView.this.m_containerBackground.setVisible(true);
                    }
                    containerBackgroundOver.setVisible(false);
                    return false;
                }
            }, false);
        }
        final NewsElementTiming timing = this.m_newsElement.getTiming();
        if (timing != null && timing.getStart() > 0) {
            this.m_container.setVisible(false);
        }
        return this.m_container;
    }
    
    public void destroyAllWidgets() {
        if (this.m_container != null) {
            this.m_container.destroySelfFromParent();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsElementView.class);
    }
}
