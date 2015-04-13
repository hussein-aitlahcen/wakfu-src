package com.ankamagames.wakfu.client.core.news;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.component.*;

class NewsRollOverDefinition
{
    public static final Logger m_logger;
    private final Rect m_triggeringArea;
    private final Container m_linkedElement;
    
    NewsRollOverDefinition(final Rect triggeringArea, final Container linkedElement) {
        super();
        this.m_triggeringArea = triggeringArea;
        this.m_linkedElement = linkedElement;
    }
    
    boolean contains(final int x, final int y) {
        return this.m_triggeringArea.contains(x, y);
    }
    
    void activate() {
        this.m_linkedElement.setVisible(true);
    }
    
    void desactivate() {
        this.m_linkedElement.setVisible(false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsRollOverDefinition.class);
    }
}
