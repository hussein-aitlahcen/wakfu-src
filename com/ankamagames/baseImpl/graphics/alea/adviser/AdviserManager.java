package com.ankamagames.baseImpl.graphics.alea.adviser;

import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import gnu.trove.*;

public class AdviserManager implements RenderProcessHandler<AleaWorldScene>
{
    private static final AdviserManager m_instance;
    private int m_idGenerator;
    private final Map<WorldPositionable, HashSet<Adviser>> m_advisers;
    private final TIntObjectHashMap<Adviser> m_adviserById;
    private final ArrayList<Adviser> m_adviserList;
    private final ArrayList<AdviserObserver> m_observers;
    
    private AdviserManager() {
        super();
        this.m_idGenerator = 0;
        this.m_observers = new ArrayList<AdviserObserver>(5);
        this.m_advisers = new HashMap<WorldPositionable, HashSet<Adviser>>();
        this.m_adviserById = new TIntObjectHashMap<Adviser>();
        this.m_adviserList = new ArrayList<Adviser>();
    }
    
    public static AdviserManager getInstance() {
        return AdviserManager.m_instance;
    }
    
    public int getNewUniqueId() {
        return ++this.m_idGenerator;
    }
    
    public void addAdviserObserver(final AdviserObserver obs) {
        if (obs == null) {
            return;
        }
        this.m_observers.add(obs);
    }
    
    public void removeAdviserObserver(final AdviserObserver obs) {
        if (obs == null) {
            return;
        }
        this.m_observers.remove(obs);
    }
    
    private void fireAdviserEvent(final Adviser ad, final AdviserEvent.AdviserEventType type) {
        if (ad == null || type == null) {
            return;
        }
        final int size = this.m_observers.size();
        if (size == 0) {
            return;
        }
        final AdviserEvent event = new AdviserEvent(ad, type);
        for (int i = 0; i < size; ++i) {
            this.m_observers.get(i).onAdviserEvent(event);
        }
    }
    
    public int addAdviser(final Adviser adviser) {
        final WorldPositionable target = adviser.getTarget();
        HashSet<Adviser> list = this.m_advisers.get(target);
        if (list == null) {
            list = new HashSet<Adviser>();
            this.m_advisers.put(target, list);
        }
        if (!list.contains(adviser)) {
            adviser.setId(this.getNewUniqueId());
            list.add(adviser);
            this.m_adviserById.put(adviser.getId(), adviser);
            this.m_adviserList.add(adviser);
            this.fireAdviserEvent(adviser, AdviserEvent.AdviserEventType.ADVISER_ADDED);
        }
        return adviser.getId();
    }
    
    public final HashSet<Adviser> getAdvisers(final WorldPositionable target) {
        return this.m_advisers.get(target);
    }
    
    public boolean containsAdviserOfType(final WorldPositionable target, final int type) {
        final HashSet<Adviser> advisers = this.m_advisers.get(target);
        if (advisers == null) {
            return false;
        }
        for (final Adviser adviser : advisers) {
            if (adviser.getTypeId() == type) {
                return true;
            }
        }
        return false;
    }
    
    public int countAdviserOfType(final WorldPositionable target, final int type) {
        final HashSet<Adviser> advisers = this.m_advisers.get(target);
        if (advisers == null) {
            return 0;
        }
        final Iterator<Adviser> iter = advisers.iterator();
        int count = 0;
        while (iter.hasNext()) {
            final Adviser adviser = iter.next();
            if (adviser.getTypeId() == type) {
                ++count;
            }
        }
        return count;
    }
    
    public final void clear() {
        final TIntObjectIterator<Adviser> it = this.m_adviserById.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().cleanUp();
        }
        this.m_advisers.clear();
        this.m_adviserList.clear();
        this.m_adviserById.clear();
    }
    
    public final Adviser getAdviser(final int id) {
        return this.m_adviserById.get(id);
    }
    
    public void removeTypedAdvisers(final int type) {
        final TIntObjectIterator<Adviser> iter = this.m_adviserById.iterator();
        while (iter.hasNext()) {
            iter.advance();
            final Adviser adviser = iter.value();
            if (adviser.getTypeId() == type) {
                final HashSet<Adviser> targetedAdvisers = this.m_advisers.get(adviser.getTarget());
                assert targetedAdvisers != null;
                targetedAdvisers.remove(adviser);
                this.m_adviserList.remove(adviser);
                iter.remove();
                adviser.cleanUp();
                this.fireAdviserEvent(adviser, AdviserEvent.AdviserEventType.ADVISER_REMOVED);
            }
        }
    }
    
    public void removeTypedAdvisers(final WorldPositionable target, final int type) {
        final HashSet<Adviser> advisers = this.m_advisers.get(target);
        if (advisers != null) {
            final Iterator<Adviser> iter = advisers.iterator();
            while (iter.hasNext()) {
                final Adviser adviser = iter.next();
                if (adviser.getTypeId() == type) {
                    this.m_adviserById.remove(adviser.getId());
                    this.m_adviserList.remove(adviser);
                    iter.remove();
                    adviser.cleanUp();
                    this.fireAdviserEvent(adviser, AdviserEvent.AdviserEventType.ADVISER_REMOVED);
                }
            }
        }
    }
    
    public void removeFirstTypedAdvisers(final WorldPositionable target, final int type) {
        final HashSet<Adviser> advisers = this.m_advisers.get(target);
        if (advisers != null) {
            final Iterator<Adviser> iter = advisers.iterator();
            while (iter.hasNext()) {
                final Adviser adviser = iter.next();
                if (adviser.getTypeId() == type) {
                    this.m_adviserById.remove(adviser.getId());
                    this.m_adviserList.remove(adviser);
                    iter.remove();
                    adviser.cleanUp();
                    this.fireAdviserEvent(adviser, AdviserEvent.AdviserEventType.ADVISER_REMOVED);
                }
            }
        }
    }
    
    public final void removeTargetedAdvisers(final WorldPositionable target) {
        final HashSet<Adviser> advisers = this.m_advisers.remove(target);
        if (advisers != null) {
            for (final Adviser adviser : advisers) {
                this.m_adviserById.remove(adviser.getId());
                this.m_adviserList.remove(adviser);
                adviser.cleanUp();
                this.fireAdviserEvent(adviser, AdviserEvent.AdviserEventType.ADVISER_REMOVED);
            }
        }
    }
    
    public final void removeAdviser(final Adviser adviser) {
        assert adviser != null;
        this.removeAdviser(adviser.getId());
    }
    
    public void removeAdviser(final int id) {
        final Adviser adviser = this.m_adviserById.remove(id);
        if (adviser != null) {
            this.m_adviserList.remove(adviser);
            final HashSet<Adviser> targetedAdvisers = this.m_advisers.get(adviser.getTarget());
            assert targetedAdvisers != null;
            targetedAdvisers.remove(adviser);
            if (targetedAdvisers.isEmpty()) {
                this.m_advisers.remove(adviser.getTarget());
            }
            adviser.cleanUp();
            this.fireAdviserEvent(adviser, AdviserEvent.AdviserEventType.ADVISER_REMOVED);
        }
    }
    
    @Override
    public void prepareBeforeRendering(final AleaWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        for (int i = 0, size = this.m_adviserList.size(); i < size; ++i) {
            final Adviser adviser = this.m_adviserList.get(i);
            if (adviser.getTarget() instanceof Mobile) {
                final Mobile mob = (Mobile)adviser.getTarget();
                if (mob != null && !mob.isVisible()) {
                    continue;
                }
            }
            final Point2 pt = IsoCameraFunc.getScreenPosition(scene, adviser.getWorldX(), adviser.getWorldY(), adviser.getAltitude());
            adviser.setPosition(pt.m_x, pt.m_y, adviser.getXOffset(), adviser.getYOffset());
            final Entity entity = adviser.getEntity();
            if (entity != null) {
                entity.m_cellX = adviser.getWorldX();
                entity.m_cellY = adviser.getWorldY();
                scene.addEntity(entity, false);
            }
        }
    }
    
    @Override
    public void process(final AleaWorldScene scene, final int deltaTime) {
        final TIntArrayList toRemove = new TIntArrayList();
        for (int i = 0, size = this.m_adviserList.size(); i < size; ++i) {
            final Adviser adviser = this.m_adviserList.get(i);
            if (!adviser.isAlive()) {
                toRemove.add(adviser.getId());
            }
            else {
                adviser.process(scene, deltaTime);
            }
        }
        for (int i = toRemove.size() - 1; i >= 0; --i) {
            this.removeAdviser(toRemove.getQuick(i));
        }
    }
    
    static {
        m_instance = new AdviserManager();
    }
}
