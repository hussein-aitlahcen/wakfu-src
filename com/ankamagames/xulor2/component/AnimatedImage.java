package com.ankamagames.xulor2.component;

import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class AnimatedImage extends Image
{
    public static final String TAG = "AnimatedImage";
    private ArrayList<PixmapElement> m_pixmaps;
    private long m_delay;
    private int m_offset;
    private Runnable m_runnable;
    public static final int DELAY_HASH;
    
    public AnimatedImage() {
        super();
        this.m_pixmaps = new ArrayList<PixmapElement>();
        this.m_delay = 1000L;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.addPixmap((PixmapElement)e);
            return;
        }
        super.add(e);
    }
    
    private void addPixmap(final PixmapElement pixmapElement) {
        if (!this.m_pixmaps.contains(pixmapElement)) {
            this.m_pixmaps.add(pixmapElement);
        }
        if (this.m_pixmaps.size() == 1) {
            this.setPixmap(pixmapElement);
        }
        else if (this.m_pixmaps.size() == 2) {
            this.startAnimation();
        }
    }
    
    private void startAnimation() {
        this.m_runnable = new Runnable() {
            @Override
            public void run() {
                AnimatedImage.this.setPixmap(AnimatedImage.this.m_pixmaps.get(AnimatedImage.this.m_offset));
                AnimatedImage.this.m_offset = (AnimatedImage.this.m_offset + 1) % AnimatedImage.this.m_pixmaps.size();
            }
        };
        ProcessScheduler.getInstance().schedule(this.m_runnable, this.m_delay, -1);
    }
    
    public void setDelay(final long delay) {
        this.m_delay = delay;
    }
    
    @Override
    public String getTag() {
        return "AnimatedImage";
    }
    
    @Override
    public void onCheckIn() {
        ProcessScheduler.getInstance().remove(this.m_runnable);
        this.m_pixmaps.clear();
        super.onCheckIn();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AnimatedImage.DELAY_HASH) {
            this.setDelay(PrimitiveConverter.getLong(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        DELAY_HASH = "delay".hashCode();
    }
}
