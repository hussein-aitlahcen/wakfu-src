package com.ankamagames.wakfu.client.core.game.interactiveElement;

import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagView extends WakfuClientInteractiveAnimatedElementSceneView
{
    private final TIntHashSet m_unknowsView;
    
    public DimensionalBagView() {
        super();
        this.m_unknowsView = new TIntHashSet();
    }
    
    @Override
    public void update() {
        super.update();
        if (this.m_interactiveElement == null) {
            return;
        }
        final DimensionalBagInteractiveElement ie = (DimensionalBagInteractiveElement)this.m_interactiveElement;
        final com.ankamagames.wakfu.client.core.game.dimensionalBag.DimensionalBagView infoProvider = ie.getInfoProvider();
        if (infoProvider == null) {
            return;
        }
        final DimensionalBag bag = infoProvider.getDimensionalBag();
        if (bag == null) {
            return;
        }
        int customViewId = bag.getCustomViewModelId();
        if (this.m_unknowsView.contains(customViewId)) {
            customViewId = 408;
        }
        if (this.getViewModelId() == customViewId) {
            return;
        }
        ie.removeView(this);
        AnimatedElementSceneViewManager.getInstance().removeElement(this);
        ClientInteractiveElementView view = WakfuClientInteractiveElementFactory.getInstance().createView(customViewId);
        if (!(view instanceof DimensionalBagView)) {
            DimensionalBagView.m_logger.error((Object)("la vue " + customViewId + " n'est pas du type DimensionalBagView"));
            view = WakfuClientInteractiveElementFactory.getInstance().createView(408);
            if (view == null) {
                DimensionalBagView.m_logger.error((Object)"aucun effort: la vue 408 n'est pas du type DimensionalBagView");
            }
            this.m_unknowsView.add(customViewId);
        }
        final DimensionalBagView bagview = (DimensionalBagView)view;
        ie.addView(bagview);
        AnimatedElementSceneViewManager.getInstance().addElement(bagview);
        bagview.fadeIfOnScreen();
        MaskableHelper.setUndefined(bagview);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.canBeOccluder(this.isOccluder());
    }
    
    @Override
    public boolean isOccluder() {
        return false;
    }
    
    public static class DimensionalBagViewFactory extends ObjectFactory<ClientInteractiveElementView>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public DimensionalBagView makeObject() {
            DimensionalBagView dimensionalBagView;
            try {
                dimensionalBagView = (DimensionalBagView)DimensionalBagViewFactory.m_pool.borrowObject();
                dimensionalBagView.setPool(DimensionalBagViewFactory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagView.m_logger.error((Object)"Erreur lors de l'extraction d'un DimensionalBagView du pool", (Throwable)e);
                dimensionalBagView = new DimensionalBagView();
            }
            return dimensionalBagView;
        }
        
        static {
            DimensionalBagViewFactory.m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagView>() {
                @Override
                public DimensionalBagView makeObject() {
                    return new DimensionalBagView();
                }
            });
        }
    }
}
