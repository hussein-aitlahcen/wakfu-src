package com.ankamagames.baseImpl.graphics.alea.mobile;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import org.apache.log4j.*;
import java.util.concurrent.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;

public class MobileManager extends AbstractInteractiveElementManager<Mobile> implements LitScene
{
    protected static final Logger m_logger;
    private static final MobileManager m_instance;
    protected final ConcurrentHashMap<Long, Mobile> m_mobiles;
    protected final List<Mobile> m_mobilesToInvalidate;
    private final List<Mobile> m_sortedMobiles;
    private final ArrayList<MobileCreationListener> m_permanentCreationListeners;
    private final ArrayList<MobileDestructionListener> m_permanentDestructionListeners;
    private final ArrayList<MobileCreationListener> m_creationListeners;
    private final ArrayList<MobileDestructionListener> m_destructionListeners;
    
    private MobileManager() {
        super();
        this.m_sortedMobiles = new ArrayList<Mobile>();
        this.m_mobiles = new ConcurrentHashMap<Long, Mobile>();
        this.m_mobilesToInvalidate = new ArrayList<Mobile>();
        this.m_permanentCreationListeners = new ArrayList<MobileCreationListener>();
        this.m_permanentDestructionListeners = new ArrayList<MobileDestructionListener>();
        this.m_creationListeners = new ArrayList<MobileCreationListener>();
        this.m_destructionListeners = new ArrayList<MobileDestructionListener>();
    }
    
    public static MobileManager getInstance() {
        return MobileManager.m_instance;
    }
    
    protected Collection<Mobile> getValues() {
        return this.m_mobiles.values();
    }
    
    public void addMobile(final Mobile mobile) {
        final ArrayList<Mobile> childrenMobile = mobile.getLinkedChildrenMobile();
        if (childrenMobile != null) {
            for (int i = 0, size = childrenMobile.size(); i < size; ++i) {
                final Mobile child = childrenMobile.get(i);
                if (child.isParentDisposeLinked()) {
                    this.addMobile(child);
                }
            }
        }
        this.m_mobiles.put(mobile.getId(), mobile);
        this.m_mobilesToInvalidate.remove(mobile);
        this.fireMobileCreation(mobile);
    }
    
    private void fireMobileCreation(final Mobile mobile) {
        for (int i = 0, size = this.m_creationListeners.size(); i < size; ++i) {
            this.m_creationListeners.get(i).onMobileCreation(mobile);
        }
    }
    
    public Mobile removeMobile(final long id) {
        final Mobile mobile = this.m_mobiles.remove(id);
        if (mobile != null) {
            this.m_mobilesToInvalidate.add(mobile);
            this.fireMobileDestruction(mobile);
        }
        return mobile;
    }
    
    private void fireMobileDestruction(final Mobile mobile) {
        for (int i = 0, size = this.m_destructionListeners.size(); i < size; ++i) {
            this.m_destructionListeners.get(i).onMobileDestruction(mobile);
        }
    }
    
    public Mobile removeMobile(final Mobile mobile) {
        return this.removeMobile(mobile.getId());
    }
    
    public Mobile detachMobile(final Mobile mobile) {
        final ArrayList<Mobile> childrenMobile = mobile.getLinkedChildrenMobile();
        if (childrenMobile != null) {
            for (int i = 0, size = childrenMobile.size(); i < size; ++i) {
                final Mobile child = childrenMobile.get(i);
                if (child.isParentDisposeLinked()) {
                    this.detachMobile(child);
                }
            }
        }
        final Mobile mobileToRemove = this.m_mobiles.remove(mobile.getId());
        this.fireMobileDestruction(mobile);
        return mobileToRemove;
    }
    
    public void removeAllMobiles() {
        for (final Mobile mobile : this.m_mobiles.values()) {
            this.fireMobileDestruction(mobile);
        }
        for (final Mobile mobile : this.m_mobilesToInvalidate) {
            this.fireMobileDestruction(mobile);
        }
        for (final Mobile mobile : this.m_mobiles.values()) {
            mobile.dispose();
        }
        this.m_mobiles.clear();
        for (final Mobile mobile : this.m_mobilesToInvalidate) {
            mobile.dispose();
        }
        this.m_mobilesToInvalidate.clear();
        this.m_displayedElements.clear();
        this.m_sortedMobiles.clear();
        this.m_creationListeners.clear();
        this.m_creationListeners.addAll(this.m_permanentCreationListeners);
        this.m_destructionListeners.clear();
        this.m_destructionListeners.addAll(this.m_permanentDestructionListeners);
    }
    
    public Mobile getMobile(final long id) {
        return this.m_mobiles.get(id);
    }
    
    public Collection<Mobile> getMobiles() {
        return this.m_mobiles.values();
    }
    
    public int getMobilesCount() {
        return this.m_mobiles.size();
    }
    
    public void forceRedisplayAllMobile() {
        this.m_mobilesToInvalidate.addAll(this.m_mobiles.values());
        for (int i = this.m_mobilesToInvalidate.size(); i <= 0; --i) {
            this.m_mobilesToInvalidate.get(i).forceReloadAnimation();
        }
    }
    
    @Override
    public void process(final AleaWorldScene worldScene, final int deltaTime) {
        for (int invalidateSize = this.m_mobilesToInvalidate.size(), i = 0; i < invalidateSize; ++i) {
            final Mobile mobileToInvalidate = this.m_mobilesToInvalidate.get(i);
            if (mobileToInvalidate.isCarried()) {
                mobileToInvalidate.getCarrierMobile().uncarry();
            }
            if (mobileToInvalidate.isCarrier()) {
                mobileToInvalidate.uncarry();
            }
            if (mobileToInvalidate.getLinkedChildrenMobile() != null) {
                mobileToInvalidate.unlinkChildrenMobile();
            }
            if (mobileToInvalidate.getLinkedParentMobile() != null) {
                mobileToInvalidate.getLinkedParentMobile().unlinkChildMobile(mobileToInvalidate);
            }
            this.m_displayedElements.remove(mobileToInvalidate);
            mobileToInvalidate.dispose();
        }
        this.m_mobilesToInvalidate.clear();
        this.sortMobile();
        final int sortedMobilesSize = this.m_sortedMobiles.size();
        for (int j = 0; j < sortedMobilesSize; ++j) {
            this.m_sortedMobiles.get(j).process(worldScene, deltaTime);
        }
        for (int j = 0; j < sortedMobilesSize; ++j) {
            this.m_sortedMobiles.get(j).update(worldScene, deltaTime);
        }
    }
    
    private List<Mobile> sortMobile() {
        final Collection<Mobile> mobiles = this.m_mobiles.values();
        this.m_sortedMobiles.clear();
        for (final Mobile mobile : mobiles) {
            int index = -1;
            final Mobile carrier = mobile.getCarrierMobile();
            if (carrier != null) {
                index = this.m_sortedMobiles.indexOf(carrier);
                if (index != -1) {
                    this.m_sortedMobiles.add(index + 1, mobile);
                    continue;
                }
            }
            final Mobile carried = mobile.getCarriedMobile();
            if (carried != null) {
                index = this.m_sortedMobiles.indexOf(carried);
                if (index != -1) {
                    this.m_sortedMobiles.add(index, mobile);
                    continue;
                }
            }
            final Mobile parentMobile = mobile.getLinkedParentMobile();
            if (parentMobile != null && !this.m_sortedMobiles.contains(mobile)) {
                index = this.m_sortedMobiles.indexOf(parentMobile);
                if (index != -1) {
                    this.m_sortedMobiles.add(index + 1, mobile);
                    continue;
                }
            }
            this.m_sortedMobiles.add(mobile);
            final ArrayList<Mobile> childrenMobile = mobile.getLinkedChildrenMobile();
            if (childrenMobile != null) {
                for (int i = 0, size = childrenMobile.size(); i < size; ++i) {
                    final Mobile child = childrenMobile.get(i);
                    if (this.m_sortedMobiles.remove(child)) {
                        this.m_sortedMobiles.add(child);
                    }
                }
            }
        }
        return this.m_sortedMobiles;
    }
    
    @Override
    public void prepareBeforeRendering(final AleaWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        this.m_displayedElements.clear();
        for (int i = 0; i < this.m_sortedMobiles.size(); ++i) {
            final Mobile mobile = this.m_sortedMobiles.get(i);
            if (mobile.addToScene(scene)) {
                mobile.setWatchersClipped(false);
                this.updateScreenPosition(mobile, scene);
                this.m_displayedElements.add((T)mobile);
            }
            else {
                mobile.setWatchersClipped(true);
            }
        }
    }
    
    public void addPermanentCreationListener(final MobileCreationListener listener) {
        if (!this.m_permanentCreationListeners.contains(listener)) {
            this.m_permanentCreationListeners.add(listener);
        }
    }
    
    public void addPermanentDestructionListener(final MobileDestructionListener listener) {
        if (!this.m_permanentDestructionListeners.contains(listener)) {
            this.m_permanentDestructionListeners.add(listener);
        }
    }
    
    public void addCreationListener(final MobileCreationListener listener) {
        if (!this.m_creationListeners.contains(listener)) {
            this.m_creationListeners.add(listener);
        }
    }
    
    public void removeCreationListener(final MobileCreationListener listener) {
        this.m_creationListeners.remove(listener);
    }
    
    public void addDestructionListener(final MobileDestructionListener listener) {
        if (!this.m_destructionListeners.contains(listener)) {
            this.m_destructionListeners.add(listener);
        }
    }
    
    public void removeDestructionListener(final MobileDestructionListener listener) {
        this.m_destructionListeners.remove(listener);
    }
    
    @Override
    public void setUndefinedMaskLayer() {
        for (final Mobile mobile : this.m_mobiles.values()) {
            MaskableHelper.setUndefined(mobile);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MobileManager.class);
        m_instance = new MobileManager();
    }
}
