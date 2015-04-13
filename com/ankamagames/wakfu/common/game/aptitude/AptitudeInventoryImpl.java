package com.ankamagames.wakfu.common.game.aptitude;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import gnu.trove.*;

public final class AptitudeInventoryImpl extends StackInventory<Aptitude, RawAptitude> implements AptitudeInventory
{
    private static final Logger m_logger;
    private final TIntIntHashMap m_availablePoints;
    private final TIntIntHashMap m_wonPoints;
    
    public AptitudeInventoryImpl() {
        super((short)(-1), AptitudeProvider.getInstance(), null, false, false, false);
        this.m_availablePoints = new TIntIntHashMap();
        this.m_wonPoints = new TIntIntHashMap();
    }
    
    @Override
    public int getAvailablePoints(final AptitudeType type) {
        return this.m_availablePoints.get(type.getId());
    }
    
    @Override
    public void setAvailablePoints(final AptitudeType type, final int availablePoints) {
        this.m_availablePoints.put(type.getId(), availablePoints);
    }
    
    @Override
    public int getWonPoints(final AptitudeType type) {
        return this.m_wonPoints.get(type.getId());
    }
    
    @Override
    public void setWonPoints(final AptitudeType type, final int availablePoints) {
        this.m_wonPoints.put(type.getId(), availablePoints);
    }
    
    @Override
    public Iterator<Aptitude> iterator() {
        final ArrayList<Aptitude> contents = new ArrayList<Aptitude>(this.m_contents.size());
        contents.addAll((Collection<? extends Aptitude>)this.m_contents.values());
        Collections.sort(contents);
        return contents.iterator();
    }
    
    @Override
    public ArrayList<Aptitude> getCommonAptitudes() {
        return this.getAptitudes(AptitudeType.COMMON);
    }
    
    @Override
    public ArrayList<Aptitude> getBreedAptitudes() {
        return this.getAptitudes(AptitudeType.SPELL);
    }
    
    @Override
    public ArrayList<Aptitude> getAptitudes(final AptitudeType type) {
        final ArrayList<Aptitude> breed = new ArrayList<Aptitude>();
        final Collection<Aptitude> aptitudes = (Collection<Aptitude>)this.m_contents.values();
        for (final Aptitude aptitude : aptitudes) {
            if (aptitude.getReferenceAptitude().getType() == type) {
                breed.add(aptitude);
            }
        }
        Collections.sort(breed);
        return breed;
    }
    
    @Override
    public boolean destroyWithUniqueId(final long uniqueId) {
        final Aptitude apt = (Aptitude)this.m_contents.get(uniqueId);
        if (apt == null) {
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canRemoveItem(this, (C)apt) < 0) {
            return false;
        }
        this.m_contents.remove(uniqueId);
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, apt));
        apt.release();
        return true;
    }
    
    @Override
    public boolean toRaw(final RawAptitudeInventory raw) {
        raw.clear();
        for (final Aptitude aptitude : this) {
            if (aptitude.shouldBeSerialized()) {
                final RawAptitudeInventory.Content content = new RawAptitudeInventory.Content();
                if (!aptitude.toRaw(content.aptitude)) {
                    return false;
                }
                raw.contents.add(content);
            }
        }
        final TIntIntIterator it = this.m_availablePoints.iterator();
        while (it.hasNext()) {
            it.advance();
            final RawAptitudeInventory.AvailablePoint point = new RawAptitudeInventory.AvailablePoint();
            final byte aptitudeType = (byte)it.key();
            point.aptitudeType = aptitudeType;
            point.availablePoints = it.value();
            point.wonPoints = this.m_wonPoints.get(aptitudeType);
            raw.availablePointsArray.add(point);
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawAptitudeInventory raw) {
        this.destroyAll();
        this.m_availablePoints.clear();
        this.m_wonPoints.clear();
        boolean bOk = true;
        try {
            for (final RawAptitudeInventory.Content content : raw.contents) {
                final Aptitude aptitude = (Aptitude)this.m_contentProvider.unSerializeContent((R)content.aptitude);
                if (aptitude != null) {
                    if (((StackInventory<Aptitude, R>)this).add(aptitude)) {
                        continue;
                    }
                    bOk = false;
                }
                else {
                    bOk = false;
                }
            }
        }
        catch (InventoryCapacityReachedException e) {
            AptitudeInventoryImpl.m_logger.error((Object)ExceptionFormatter.toString(e));
            bOk = false;
        }
        catch (ContentAlreadyPresentException e2) {
            AptitudeInventoryImpl.m_logger.error((Object)ExceptionFormatter.toString(e2));
            bOk = false;
        }
        for (final RawAptitudeInventory.AvailablePoint points : raw.availablePointsArray) {
            this.m_availablePoints.put(points.aptitudeType, points.availablePoints);
            this.m_wonPoints.put(points.aptitudeType, points.wonPoints);
        }
        return bOk;
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        this.m_availablePoints.clear();
        this.m_wonPoints.clear();
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId) {
        throw new UnsupportedOperationException("");
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId, final int count) {
        throw new UnsupportedOperationException("");
    }
    
    @Override
    public Aptitude getWithUniqueId(final short aptitudeId) {
        return super.getWithUniqueId(aptitudeId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AptitudeInventoryImpl.class);
    }
}
