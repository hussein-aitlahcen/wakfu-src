package com.ankamagames.wakfu.client.core.news;

import com.ankamagames.framework.fileFormat.news.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;

public class NewsTextElementView extends NewsElementView
{
    public NewsTextElementView(final NewsElement newsElement) {
        super(newsElement);
    }
    
    @Override
    public void togglePlayPauseVideo() {
        throw new UnsupportedOperationException("Pas de vid\u00e9o sur un \u00e9l\u00e9ment de texte");
    }
    
    @Override
    public Container build(final Container newsContainer, final NewsDisplayer newsDisplayer) {
        final Container container = super.build(newsContainer, newsDisplayer);
        final TextElement te = (TextElement)this.m_newsElement;
        final TextView tv = new TextView();
        tv.onCheckOut();
        tv.setEnableAWTFont(true);
        tv.setElementMap(newsContainer.getElementMap());
        tv.setText(TextWidgetFormaterUtils.fromHtml(te.getText()).toString());
        tv.setExpandable(false);
        tv.onChildrenAdded();
        final StaticLayoutData sld2 = new StaticLayoutData();
        sld2.onCheckOut();
        sld2.setSize(new Dimension(100.0f, 100.0f));
        sld2.setAlign(Alignment17.CENTER);
        tv.setLayoutData(sld2);
        container.add(tv);
        container.onChildrenAdded();
        if (te.hasTriggeringArea()) {
            newsDisplayer.registerElementForRollOver(te.getTriggeringArea(), container, newsContainer);
        }
        return container;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return null;
    }
}
