package com.ankamagames.xulor2.util;

import java.util.*;
import com.ankamagames.xulor2.core.windowStick.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class GlobalUserDefinedManager
{
    private static final GlobalUserDefinedManager m_instance;
    private final ArrayList<UserDefinedManager> m_managers;
    
    public GlobalUserDefinedManager() {
        super();
        this.m_managers = new ArrayList<UserDefinedManager>();
    }
    
    public static GlobalUserDefinedManager getInstance() {
        return GlobalUserDefinedManager.m_instance;
    }
    
    public void addManager(final UserDefinedManager userDefinedManager) {
        if (!this.m_managers.contains(userDefinedManager)) {
            this.m_managers.add(userDefinedManager);
        }
    }
    
    public void removeManager(final UserDefinedManager userDefinedManager) {
        if (this.m_managers.contains(userDefinedManager)) {
            this.m_managers.remove(userDefinedManager);
        }
    }
    
    public void saveAll() {
        for (final UserDefinedManager globalUserDefinedManager : this.m_managers) {
            globalUserDefinedManager.storePreferences();
        }
    }
    
    public void onResize(final XulorScene xulorScene, final int deltaWidth, final int deltaHeight) {
        if (xulorScene == null || (deltaWidth == 0 && deltaHeight == 0)) {
            return;
        }
        WindowStickManager.getInstance().onResize(xulorScene, deltaWidth, deltaHeight);
        final float frustumWidth = xulorScene.getFrustumWidth();
        final float frustumHeight = xulorScene.getFrustumHeight();
        final float previousWidth = frustumWidth - deltaWidth;
        final float previousHeight = frustumHeight - deltaHeight;
        final Iterator<EventDispatcher> it = Xulor.getInstance().loadedElementIterator();
        while (it.hasNext()) {
            final EventDispatcher ed = it.next();
            if (!(ed instanceof Widget)) {
                continue;
            }
            final Widget widget = (Widget)ed;
            if (!(widget.getLayoutData() instanceof StaticLayoutData)) {
                continue;
            }
            final StaticLayoutData data = (StaticLayoutData)widget.getLayoutData();
            if (!data.isInitValue()) {
                continue;
            }
            if (widget instanceof Window) {
                final Window window = (Window)widget;
                if (window.getStickData() != null) {
                    continue;
                }
            }
            final int width = widget.getWidth();
            final int height = widget.getHeight();
            int x = (int)((frustumWidth - width) * widget.getXPercInParent());
            int y = (int)((frustumHeight - height) * widget.getYPercInParent());
            x = MathHelper.clamp(x, 0, (int)(frustumWidth - width));
            y = MathHelper.clamp(y, 0, (int)(frustumHeight - height));
            widget.setPosition(x, y, 0, false, false);
        }
    }
    
    static {
        m_instance = new GlobalUserDefinedManager();
    }
}
