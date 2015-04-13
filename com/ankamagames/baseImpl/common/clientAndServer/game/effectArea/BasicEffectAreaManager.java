package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;

public abstract class BasicEffectAreaManager implements Poolable
{
    protected static final Logger m_logger;
    private ObjectPool m_pool;
    private HashMap<Long, BasicEffectArea> m_effectAreaList;
    private HashMap<Long, BasicEffectArea> m_activeEffectArea;
    private EffectAreaActionListener m_listener;
    private EffectContext m_context;
    
    protected BasicEffectAreaManager() {
        super();
        this.m_activeEffectArea = new HashMap<Long, BasicEffectArea>();
        this.m_effectAreaList = new HashMap<Long, BasicEffectArea>();
    }
    
    public EffectContext getContext() {
        return this.m_context;
    }
    
    public void setContext(final EffectContext context) {
        this.m_context = context;
    }
    
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                BasicEffectAreaManager.m_logger.error((Object)"impossible");
            }
        }
        else {
            BasicEffectAreaManager.m_logger.error((Object)("Double release de " + this.getClass().toString()));
        }
        this.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        this.m_listener = null;
        this.m_activeEffectArea.clear();
        this.m_effectAreaList.clear();
    }
    
    @Override
    public void onCheckIn() {
        this.destroyAll();
        this.m_listener = null;
    }
    
    public void setPool(final ObjectPool pool) {
        this.m_pool = pool;
    }
    
    public Collection<BasicEffectArea> getEffectAreaList() {
        return (Collection<BasicEffectArea>)this.m_effectAreaList.values();
    }
    
    public Collection<BasicEffectArea> getTargetableEffectArea() {
        final Collection<BasicEffectArea> allAreas = (Collection<BasicEffectArea>)this.m_activeEffectArea.values();
        final Collection<BasicEffectArea> res = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : allAreas) {
            if (area.isCanBeTargeted()) {
                res.add(area);
            }
        }
        return res;
    }
    
    public BasicEffectArea getTargetableEffectAreaOnPosition(final Point3 position) {
        final int x = position.getX();
        final int y = position.getY();
        return this.getTargetableEffectAreaOnPosition(x, y);
    }
    
    @Nullable
    public BasicEffectArea getTargetableEffectAreaOnPosition(final int x, final int y) {
        for (final BasicEffectArea effectArea : this.getTargetableEffectArea()) {
            if (x == effectArea.getWorldCellX() && y == effectArea.getWorldCellY()) {
                return effectArea;
            }
        }
        return null;
    }
    
    public List<BasicEffectArea> getTargetableEffectAreasListOnPosition(final Point3 position) {
        final int x = position.getX();
        final int y = position.getY();
        return this.getTargetableEffectAreasListOnPosition(x, y);
    }
    
    public List<BasicEffectArea> getTargetableEffectAreasListOnPosition(final int x, final int y) {
        final List<BasicEffectArea> res = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea effectArea : this.getTargetableEffectArea()) {
            if (x == effectArea.getWorldCellX() && y == effectArea.getWorldCellY()) {
                res.add(effectArea);
            }
        }
        return res;
    }
    
    public List<BasicEffectArea> getTargetableEffectAreasListOnPositionOfType(final int x, final int y, final int type) {
        final List<BasicEffectArea> res = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea effectArea : this.getTargetableEffectArea()) {
            if (x == effectArea.getWorldCellX() && y == effectArea.getWorldCellY() && type == effectArea.getType()) {
                res.add(effectArea);
            }
        }
        return res;
    }
    
    public List<BasicEffectArea> getEffectAreasListOnPosition(@NotNull final Point3 position) {
        final List<BasicEffectArea> res = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea effectArea : this.m_activeEffectArea.values()) {
            if (position.equals(effectArea.getWorldCellX(), effectArea.getWorldCellY())) {
                res.add(effectArea);
            }
        }
        return res;
    }
    
    public int getActiveEffectAreaCount() {
        return this.m_activeEffectArea.size();
    }
    
    public Collection<BasicEffectArea> getActiveEffectAreas() {
        return (Collection<BasicEffectArea>)this.m_activeEffectArea.values();
    }
    
    public BasicEffectArea getEffectAreaWithId(final long id) {
        return this.m_effectAreaList.get(id);
    }
    
    public BasicEffectArea getActiveEffectAreaWithId(final long id) {
        return this.m_activeEffectArea.get(id);
    }
    
    public void setListener(final EffectAreaActionListener listener) {
        this.m_listener = listener;
    }
    
    public void addEffectArea(final BasicEffectArea area) {
        if (area == null) {
            return;
        }
        if (!this.m_activeEffectArea.containsKey(area.getId())) {
            assert !this.m_activeEffectArea.containsValue(area) : "Trying to insert an effectArea already present, but with a different Id";
            area.setListener(this.m_listener);
            this.m_effectAreaList.put(area.getId(), area);
            this.m_activeEffectArea.put(area.getId(), area);
            area.onEffectAreaAddedToManager();
            if (this.m_listener != null) {
                this.m_listener.onEffectAreaAdded(area);
            }
        }
    }
    
    public void removeEffectArea(final BasicEffectArea area) {
        this.removeEffectArea(area, null);
    }
    
    public void removeEffectArea(final BasicEffectArea area, final EffectUser remover) {
        if (area == null) {
            return;
        }
        if (this.m_activeEffectArea.containsKey(area.getId())) {
            this.m_activeEffectArea.remove(area.getId());
            area.onEffectAreaRemovedFromManager();
            if (this.m_listener != null) {
                this.m_listener.onEffectAreaRemoved(area);
            }
            area.triggers(10011, null, remover);
        }
    }
    
    public void removeFromAreaList(final BasicEffectArea area) {
        if (area == null) {
            return;
        }
        this.m_effectAreaList.remove(area.getId());
    }
    
    public void removeEffectAreaOwnedByEffectUser(final EffectUser owner) {
        if (owner == null) {
            return;
        }
        final List<BasicEffectArea> areaOwned = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_activeEffectArea.values()) {
            if (area.getOwner() == owner) {
                areaOwned.add(area);
            }
        }
        for (final BasicEffectArea area : areaOwned) {
            this.removeEffectArea(area);
        }
    }
    
    public void removeEffectAreaOwnedByEffectUserIfNecessary(final EffectUser owner) {
        if (owner == null) {
            return;
        }
        final List<BasicEffectArea> areaOwned = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_activeEffectArea.values()) {
            if (area.getOwner() == owner && area.disappearWithOwner()) {
                areaOwned.add(area);
            }
        }
        for (final BasicEffectArea area : areaOwned) {
            this.removeEffectArea(area);
        }
    }
    
    public boolean containsActiveArea(final BasicEffectArea area) {
        return this.m_activeEffectArea.containsKey(area.getId());
    }
    
    public void destroyAll() {
        for (final BasicEffectArea area : this.getEffectAreaList()) {
            area.release();
        }
        this.m_activeEffectArea.clear();
        this.m_effectAreaList.clear();
    }
    
    public void checkInAndOut(final int startx, final int starty, final short startz, final int arrivalx, final int arrivaly, final short arrivalz, final EffectUser applicant) {
        final ArrayList<BasicEffectArea> areaAlreadyIn = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_activeEffectArea.values()) {
            if (area.contains(startx, starty, startz)) {
                areaAlreadyIn.add(area);
            }
        }
        final ArrayList<BasicEffectArea> areaIn = new ArrayList<BasicEffectArea>();
        final ArrayList<BasicEffectArea> areaOut = new ArrayList<BasicEffectArea>();
        final ArrayList<BasicEffectArea> areaInto = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area2 : this.m_activeEffectArea.values()) {
            if (area2.contains(applicant, arrivalx, arrivaly, arrivalz)) {
                if (!areaAlreadyIn.contains(area2)) {
                    areaIn.add(area2);
                }
                else {
                    areaInto.add(area2);
                }
            }
            else {
                if (!areaAlreadyIn.contains(area2)) {
                    continue;
                }
                areaOut.add(area2);
            }
        }
        for (final BasicEffectArea area2 : areaIn) {
            area2.triggers(10001, null, applicant);
        }
        for (final BasicEffectArea area2 : areaInto) {
            area2.triggers(10008, null, applicant);
        }
        for (final BasicEffectArea area2 : areaOut) {
            area2.triggers(10002, null, applicant);
        }
    }
    
    public boolean hasPotentialTarget(final long id) {
        return this.m_activeEffectArea.containsKey(id);
    }
    
    public boolean isActiveEffectArea(final BasicEffectArea area) {
        return this.m_activeEffectArea.containsKey(area.getId());
    }
    
    public abstract BasicEffectArea instanceNewAreaFromBaseId(final long p0);
    
    static {
        m_logger = Logger.getLogger((Class)BasicEffectAreaManager.class);
    }
}
