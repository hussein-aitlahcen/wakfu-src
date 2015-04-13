package com.ankamagames.wakfu.client.core.news;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import javax.imageio.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.appearance.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.fileFormat.news.*;

public class NewsItemView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    private static final float WIDTH_PERCENTAGE = 100.0f;
    private static final float HEIGHT_PERCENTAGE = 100.0f;
    public static final String TITLE_FIELD = "title";
    public static final String HAS_VIDEO_FIELD = "hasVideo";
    public static final String VIDEO_FIELD = "video";
    private static final String[] FIELDS;
    private final ArrayList<NewsTextElementView> m_newsTextElementViews;
    private final ArrayList<NewsVideoElementView> m_newsVideoElementViews;
    private final NewsItem m_newsItem;
    private Container m_container;
    
    public NewsItemView(final NewsItem newsItem) {
        super();
        this.m_newsTextElementViews = new ArrayList<NewsTextElementView>();
        this.m_newsVideoElementViews = new ArrayList<NewsVideoElementView>();
        this.m_newsItem = newsItem;
        for (final NewsElement newsElement : newsItem.getElements()) {
            switch (newsElement.getType()) {
                case TEXT: {
                    this.m_newsTextElementViews.add(new NewsTextElementView(newsElement));
                    continue;
                }
                case VIDEO: {
                    this.m_newsVideoElementViews.add(new NewsVideoElementView(newsElement));
                    continue;
                }
                default: {
                    continue;
                }
            }
        }
    }
    
    public ArrayList<NewsTextElementView> getNewsTextElementViews() {
        return this.m_newsTextElementViews;
    }
    
    public ArrayList<NewsVideoElementView> getNewsVideoElementViews() {
        return this.m_newsVideoElementViews;
    }
    
    @Override
    public String[] getFields() {
        return NewsItemView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return this.m_newsItem.getTitle();
        }
        if (fieldName.equals("hasVideo")) {
            return this.hasVideo();
        }
        if (!fieldName.equals("video")) {
            return null;
        }
        if (!this.hasVideo()) {
            return null;
        }
        return this.getVideoElement();
    }
    
    public NewsVideoElementView getVideoElement() {
        return this.m_newsVideoElementViews.get(0);
    }
    
    public boolean hasVideo() {
        return !this.m_newsVideoElementViews.isEmpty();
    }
    
    public Container build() {
        this.m_container = Container.checkOut();
        final StaticLayoutData staticLayoutData = new StaticLayoutData();
        staticLayoutData.onCheckOut();
        staticLayoutData.setSize(new Dimension(100.0f, 100.0f));
        this.m_container.setLayoutData(staticLayoutData);
        final NewsElementBackground background = this.m_newsItem.getBackground();
        if (background == null) {
            NewsItemView.m_logger.warn((Object)("No background for news " + this.m_newsItem));
            return this.m_container;
        }
        if (!background.hasImage()) {
            final PlainBackground pb = new PlainBackground();
            pb.onCheckOut();
            pb.setColor(background.getColor());
            this.m_container.getAppearance().add(pb);
            this.m_container.onChildrenAdded();
            return this.m_container;
        }
        final NewsImage newsImage = background.getImage();
        if (newsImage == null) {
            NewsItemView.m_logger.warn((Object)("No image for news background " + this.m_newsItem));
            return this.m_container;
        }
        final File newsImageFile = newsImage.getFile();
        try {
            final URL url = newsImageFile.toURI().toURL();
            final BufferedImage bufferedImage = ImageIO.read(url);
            final Texture texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(url.toString()), Image.createImage(bufferedImage), true);
            final PixmapElement pixmapElement = new PixmapElement();
            pixmapElement.onCheckOut();
            pixmapElement.setTexture(texture);
            final com.ankamagames.xulor2.component.Image image = new com.ankamagames.xulor2.component.Image();
            image.onCheckOut();
            image.setNonBlocking(true);
            image.setExpandable(false);
            image.add(pixmapElement);
            image.onChildrenAdded();
            image.computeMinSize();
            image.setSizeToPrefSize();
            this.m_container.add(image);
            this.m_container.onChildrenAdded();
        }
        catch (MalformedURLException e) {
            NewsItemView.m_logger.warn((Object)("URL error for \"" + newsImageFile + '\"'), (Throwable)e);
        }
        catch (IOException e2) {
            NewsItemView.m_logger.warn((Object)("Read error for \"" + newsImageFile + '\"'), (Throwable)e2);
        }
        return this.m_container;
    }
    
    public void destroyAllWidgets() {
        if (this.m_container != null) {
            this.m_container.destroySelfFromParent();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsItemView.class);
        FIELDS = new String[] { "title", "hasVideo", "video" };
    }
}
