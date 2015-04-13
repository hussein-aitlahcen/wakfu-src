package com.ankamagames.wakfu.client.core.news;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.fileFormat.news.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.news.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class NewsDisplayer extends ImmutableFieldProvider
{
    public static final String CURRENT_NEW_FIELD = "currentNew";
    public static final String LIST_FIELD = "list";
    public static final String HAS_PREVIOUS_NEW_FIELD = "hasPreviousNew";
    public static final String HAS_NEXT_NEW_FIELD = "hasNextNew";
    public static final String VIDEO_SOUND_VOLUME_VALUE_FIELD = "videoSoundVolumeValue";
    public static final String[] FIELDS;
    private ArrayList<NewsItemView> m_newsItemViewList;
    private NewsItemView m_currentNew;
    private EventListener m_timeChangedListener;
    private ArrayList<NewsRollOverDefinition> m_rollOverMap;
    private Container m_rollOverContainer;
    
    public NewsDisplayer(final NewsChannel channel) {
        super();
        this.m_newsItemViewList = new ArrayList<NewsItemView>();
        this.m_timeChangedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final long seconds = (long)((ValueChangedEvent)event).getValue() / 1000L;
                NewsDisplayer.this.checkNewsTextElementsVisibility(seconds);
                return false;
            }
        };
        this.m_rollOverMap = new ArrayList<NewsRollOverDefinition>();
        this.m_rollOverContainer = null;
        for (final NewsItem newsItem : channel.getItems()) {
            this.m_newsItemViewList.add(new NewsItemView(newsItem));
        }
    }
    
    @Override
    public String[] getFields() {
        return NewsDisplayer.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentNew")) {
            return this.m_currentNew;
        }
        if (fieldName.equals("list")) {
            return this.m_newsItemViewList;
        }
        if (fieldName.equals("hasPreviousNew")) {
            return this.hasPreviousNew();
        }
        if (fieldName.equals("hasNextNew")) {
            return this.hasNextNew();
        }
        return null;
    }
    
    public boolean hasNextNew() {
        return this.m_newsItemViewList.size() > 1 && this.m_currentNew != this.m_newsItemViewList.get(this.m_newsItemViewList.size() - 1);
    }
    
    public boolean hasPreviousNew() {
        return this.m_newsItemViewList.size() > 1 && this.m_currentNew != this.m_newsItemViewList.get(0);
    }
    
    private void destroyNewsWidget() {
        this.m_currentNew.destroyAllWidgets();
        this.m_rollOverMap.clear();
        if (this.m_rollOverContainer != null) {
            this.m_rollOverContainer.destroySelfFromParent();
            this.m_rollOverContainer.removeAllEventListeners();
            this.m_rollOverContainer = null;
        }
        for (final NewsElementView newsElementView : this.m_currentNew.getNewsVideoElementViews()) {
            newsElementView.destroyAllWidgets();
        }
        for (final NewsElementView newsElementView : this.m_currentNew.getNewsTextElementViews()) {
            newsElementView.destroyAllWidgets();
        }
    }
    
    public void populateContainerWithNews(final Container newsContainer) {
        if (this.m_currentNew == null) {
            return;
        }
        newsContainer.add(this.m_currentNew.build());
        newsContainer.onChildrenAdded();
        final Widget b = (Widget)newsContainer.getElementMap().getElement("bigPLayButton");
        b.setVisible(false);
        for (final NewsElementView newsElementView : this.m_currentNew.getNewsVideoElementViews()) {
            newsContainer.add(newsElementView.build(newsContainer, this));
            newsContainer.onChildrenAdded();
            final VideoWidget videoWidget = ((NewsVideoElementView)newsElementView).getVideoWidget();
            videoWidget.addEventListener(Events.VALUE_CHANGED, this.m_timeChangedListener, false);
            b.setVisible(true);
        }
        for (final NewsElementView newsElementView : this.m_currentNew.getNewsTextElementViews()) {
            newsContainer.add(newsElementView.build(newsContainer, this));
            newsContainer.onChildrenAdded();
        }
    }
    
    private void checkNewsTextElementsVisibility(final long pastSeconds) {
        for (final NewsElementView newsElementView : this.m_currentNew.getNewsTextElementViews()) {
            newsElementView.checkTimingVisibility(pastSeconds);
        }
    }
    
    void registerElementForRollOver(final Rect triggeringArea, final Container elementToDisplay, final Container parentContainer) {
        if (this.m_rollOverContainer == null) {
            this.m_rollOverContainer = Container.checkOut();
            final Dimension parentSize = ((StaticLayoutData)parentContainer.getLayoutData()).getSize();
            final StaticLayoutData sld3 = new StaticLayoutData();
            sld3.onCheckOut();
            sld3.setSize(new Dimension(parentSize));
            sld3.setX(0);
            sld3.setY(0);
            this.m_rollOverContainer.setLayoutData(sld3);
            this.m_rollOverContainer.setNonBlocking(false);
            final EventListener movementListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    final MouseEvent mouseEvent = (MouseEvent)event;
                    final int x = mouseEvent.getX(NewsDisplayer.this.m_rollOverContainer);
                    final int y = (int)parentSize.getHeight() - mouseEvent.getY(NewsDisplayer.this.m_rollOverContainer);
                    for (final NewsRollOverDefinition def : NewsDisplayer.this.m_rollOverMap) {
                        if (def.contains(x, y)) {
                            def.activate();
                        }
                        else {
                            def.desactivate();
                        }
                    }
                    return false;
                }
            };
            this.m_rollOverContainer.addEventListener(Events.MOUSE_MOVED, movementListener, false);
            this.m_rollOverContainer.addEventListener(Events.MOUSE_ENTERED, movementListener, false);
            this.m_rollOverContainer.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    for (final NewsRollOverDefinition def : NewsDisplayer.this.m_rollOverMap) {
                        def.desactivate();
                    }
                    return false;
                }
            }, false);
            parentContainer.add(this.m_rollOverContainer);
            parentContainer.onChildrenAdded();
        }
        elementToDisplay.setVisible(false);
        final NewsRollOverDefinition rollOver = new NewsRollOverDefinition(triggeringArea, elementToDisplay);
        this.m_rollOverMap.add(rollOver);
    }
    
    public void setCurrentNew(final NewsItemView currentNew) {
        if (this.m_currentNew != null) {
            this.destroyNewsWidget();
        }
        this.m_currentNew = currentNew;
    }
    
    public NewsItemView getCurrentNew() {
        return this.m_currentNew;
    }
    
    public void clean() {
        this.destroyNewsWidget();
        PropertiesProvider.getInstance().removeProperty("news");
    }
    
    public NewsItemView getFirstNew() {
        return this.m_newsItemViewList.get(0);
    }
    
    public NewsItemView getPreviousNew() {
        final int index = this.m_newsItemViewList.indexOf(this.m_currentNew) - 1;
        if (index < 0) {
            return null;
        }
        return this.m_newsItemViewList.get(index);
    }
    
    public NewsItemView getNextNew() {
        final int index = this.m_newsItemViewList.indexOf(this.m_currentNew) + 1;
        if (index > this.m_newsItemViewList.size() - 1) {
            return null;
        }
        return this.m_newsItemViewList.get(index);
    }
    
    public void resetCurrentNew() {
        final UISelectNewMessage uiSelectNewMessage = new UISelectNewMessage(this.m_currentNew);
        uiSelectNewMessage.setBooleanValue(true);
        Worker.getInstance().pushMessage(uiSelectNewMessage);
    }
    
    static {
        FIELDS = new String[] { "currentNew", "list", "hasPreviousNew", "hasNextNew", "videoSoundVolumeValue" };
    }
}
