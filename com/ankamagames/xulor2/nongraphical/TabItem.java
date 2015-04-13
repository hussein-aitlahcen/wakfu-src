package com.ankamagames.xulor2.nongraphical;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class TabItem extends NonGraphicalElement
{
    private static Logger m_logger;
    public static final String TAG = "TabItem";
    private RadioButton m_button;
    private Container m_data;
    private PixmapElement m_buttonPixmap;
    private String m_text;
    private Boolean m_enabled;
    private boolean m_visible;
    private final ArrayList<TabItemVisibilityListener> m_tabItemVisibilityListenerArrayList;
    public static final int TEXT_HASH;
    public static final int ENABLED_HASH;
    public static final int VISIBLE_HASH;
    
    public TabItem() {
        super();
        this.m_button = null;
        this.m_data = null;
        this.m_buttonPixmap = null;
        this.m_text = null;
        this.m_enabled = true;
        this.m_visible = true;
        this.m_tabItemVisibilityListenerArrayList = new ArrayList<TabItemVisibilityListener>();
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof Container) {
            this.m_data = (Container)e;
            return;
        }
        if (e instanceof PixmapElement) {
            if (this.m_button != null) {
                this.m_button.setPixmap((PixmapElement)e);
            }
            this.m_buttonPixmap = (PixmapElement)e;
        }
        super.add(e);
    }
    
    public void addTabItemVisibilityListener(final TabItemVisibilityListener tabItemVisibilityListener) {
        if (!this.m_tabItemVisibilityListenerArrayList.contains(tabItemVisibilityListener)) {
            this.m_tabItemVisibilityListenerArrayList.add(tabItemVisibilityListener);
        }
    }
    
    public void removeTabItemVisibilityListener(final TabItemVisibilityListener tabItemVisibilityListener) {
        this.m_tabItemVisibilityListenerArrayList.remove(tabItemVisibilityListener);
    }
    
    public void onButtonVisibilityChange(final boolean visible) {
        for (int i = this.m_tabItemVisibilityListenerArrayList.size() - 1; i >= 0; --i) {
            this.m_tabItemVisibilityListenerArrayList.get(i).onVisibilityChange(visible);
        }
    }
    
    @Override
    public String getTag() {
        return "TabItem";
    }
    
    public RadioButton getButton() {
        return this.m_button;
    }
    
    public void setButton(final RadioButton button) {
        (this.m_button = button).setEnabled(this.m_enabled);
        this.m_button.setVisible(this.m_visible);
        this.onButtonVisibilityChange(this.m_visible);
        if (this.m_text != null) {
            this.m_button.setText(this.m_text);
        }
    }
    
    public Container getData() {
        return this.m_data;
    }
    
    public void setData(final Container data) {
        this.m_data = data;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public void setText(final String text) {
        this.m_text = text;
        if (this.m_button != null) {
            this.m_button.setText(this.m_text);
        }
    }
    
    private void setEnabled(final boolean b) {
        this.m_enabled = b;
        if (this.m_button != null) {
            this.m_button.setEnabled(this.m_enabled);
        }
    }
    
    private void setVisible(final boolean visible) {
        final boolean visibilityChanged = visible != this.m_visible;
        this.m_visible = visible;
        if (this.m_button != null) {
            this.m_button.setVisible(this.m_visible);
            if (visibilityChanged) {
                this.onButtonVisibilityChange(visibilityChanged);
            }
        }
    }
    
    public void resetButtonPixmap() {
        if (this.m_button != null && this.m_buttonPixmap != null) {
            this.m_button.setPixmap(this.m_buttonPixmap);
        }
    }
    
    @Override
    public void copyElement(final BasicElement t) {
        final TabItem e = (TabItem)t;
        super.copyElement(e);
        e.m_text = this.m_text;
        e.m_enabled = this.m_enabled;
        e.m_visible = this.m_visible;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TabItem.TEXT_HASH) {
            this.setText(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == TabItem.ENABLED_HASH) {
            this.setEnabled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != TabItem.VISIBLE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TabItem.TEXT_HASH) {
            this.setText(String.valueOf(value));
        }
        else if (hash == TabItem.ENABLED_HASH) {
            this.setEnabled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != TabItem.VISIBLE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_data != null) {
            this.m_data.release();
            this.m_data = null;
        }
        this.m_tabItemVisibilityListenerArrayList.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    static {
        TabItem.m_logger = Logger.getLogger((Class)TabItem.class);
        TEXT_HASH = "text".hashCode();
        ENABLED_HASH = "enabled".hashCode();
        VISIBLE_HASH = "visible".hashCode();
    }
    
    public interface TabItemVisibilityListener
    {
        void onVisibilityChange(boolean p0);
    }
}
