package com.ankamagames.wakfu.client.core.krosmoz.collection;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class KrosmozFigureView extends ImmutableFieldProvider
{
    public static final String ICON_URL = "iconUrl";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SIZE = "size";
    private final KrosmozFigureData m_figure;
    private int m_size;
    
    public KrosmozFigureView(final KrosmozFigureData figure) {
        super();
        this.m_figure = figure;
        this.m_size = 0;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(130, this.m_figure.getFigureId(), new Object[0]);
        }
        if (fieldName.equals("description")) {
            return WakfuTranslator.getInstance().getString(132, this.m_figure.getFigureId(), new Object[0]);
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("krosmoz.figureIconsPath", "defaultIconPath", this.m_figure.getFigureId());
        }
        if (fieldName.equals("size")) {
            return this.m_size;
        }
        return null;
    }
    
    public KrosmozFigureData getFigure() {
        return this.m_figure;
    }
    
    public void reset() {
        this.setSize(0);
    }
    
    void addFigure() {
        this.setSize(this.m_size + 1);
    }
    
    void removeFigure() {
        this.setSize(this.m_size - 1);
    }
    
    void setSize(final int size) {
        this.m_size = size;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "size");
    }
}
