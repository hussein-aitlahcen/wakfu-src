package com.ankamagames.wakfu.common.game.travel.character;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class TravelHandler
{
    protected static final Logger m_logger;
    private final EnumMap<TravelType, TIntHashSet> m_knownTravels;
    private final TIntHashSet m_knownZaaps;
    private final TIntHashSet m_knownDragos;
    private final TIntHashSet m_knownBoats;
    private final TIntHashSet m_knownCannons;
    
    public TravelHandler() {
        super();
        this.m_knownTravels = new EnumMap<TravelType, TIntHashSet>(TravelType.class);
        this.m_knownZaaps = new TIntHashSet();
        this.m_knownDragos = new TIntHashSet();
        this.m_knownBoats = new TIntHashSet();
        this.m_knownCannons = new TIntHashSet();
        this.m_knownTravels.put(TravelType.ZAAP, this.m_knownZaaps);
        this.m_knownTravels.put(TravelType.DRAGO, this.m_knownDragos);
        this.m_knownTravels.put(TravelType.BOAT, this.m_knownBoats);
        this.m_knownTravels.put(TravelType.CANNON, this.m_knownCannons);
    }
    
    public void addDiscoveredDrago(final int dragoId) {
        this.m_knownDragos.add(dragoId);
    }
    
    public void addDiscoveredZaap(final int zaapId) {
        this.m_knownZaaps.add(zaapId);
    }
    
    public void addDiscoveredCannon(final int cannonId) {
        this.m_knownCannons.add(cannonId);
    }
    
    public void addDiscoveredBoat(final int boatId) {
        this.m_knownBoats.add(boatId);
    }
    
    public boolean isDragoDiscovered(final int dragoId) {
        return this.m_knownDragos.contains(dragoId);
    }
    
    public boolean isZaapDiscovered(final int zaapId) {
        return this.m_knownZaaps.contains(zaapId);
    }
    
    public boolean isBoatDiscovered(final int boatId) {
        return this.m_knownBoats.contains(boatId);
    }
    
    public boolean isCannonDiscovered(final int cannonId) {
        return this.m_knownCannons.contains(cannonId);
    }
    
    public TIntIterator knownBoatIterator() {
        return this.m_knownBoats.iterator();
    }
    
    public TIntIterator knownZaapIterator() {
        return this.m_knownZaaps.iterator();
    }
    
    public TIntIterator knownDragoIterator() {
        return this.m_knownDragos.iterator();
    }
    
    public boolean isTravelDiscovered(final TravelType type, final int travelId) {
        final TIntHashSet set = this.m_knownTravels.get(type);
        return set == null || set.contains(travelId);
    }
    
    public boolean canUseDrago(final int dragoId) {
        return this.m_knownDragos.contains(dragoId);
    }
    
    public void clear() {
        this.m_knownZaaps.clear();
        this.m_knownDragos.clear();
        this.m_knownBoats.clear();
        this.m_knownCannons.clear();
        this.m_knownZaaps.addAll(TravelInfoManager.INSTANCE.getBaseZaapIds());
    }
    
    public void toRaw(final CharacterSerializedDiscoveredItemsInventory part) {
        TIntIterator it = this.m_knownZaaps.iterator();
        while (it.hasNext()) {
            final CharacterSerializedDiscoveredItemsInventory.Zaap zaap = new CharacterSerializedDiscoveredItemsInventory.Zaap();
            zaap.zaapId = it.next();
            part.zaaps.add(zaap);
        }
        it = this.m_knownDragos.iterator();
        while (it.hasNext()) {
            final CharacterSerializedDiscoveredItemsInventory.Drago drago = new CharacterSerializedDiscoveredItemsInventory.Drago();
            drago.dragoId = it.next();
            part.dragos.add(drago);
        }
        it = this.m_knownBoats.iterator();
        while (it.hasNext()) {
            final CharacterSerializedDiscoveredItemsInventory.Boat boat = new CharacterSerializedDiscoveredItemsInventory.Boat();
            boat.boatId = it.next();
            part.boats.add(boat);
        }
        it = this.m_knownCannons.iterator();
        while (it.hasNext()) {
            final CharacterSerializedDiscoveredItemsInventory.Cannon cannon = new CharacterSerializedDiscoveredItemsInventory.Cannon();
            cannon.cannonId = it.next();
            part.cannon.add(cannon);
        }
    }
    
    public final void fromRaw(final CharacterSerializedDiscoveredItemsInventory part) {
        for (int i = 0; i < part.zaaps.size(); ++i) {
            this.m_knownZaaps.add(part.zaaps.get(i).zaapId);
        }
        for (int i = 0; i < part.dragos.size(); ++i) {
            this.m_knownDragos.add(part.dragos.get(i).dragoId);
        }
        for (int i = 0; i < part.cannon.size(); ++i) {
            this.m_knownCannons.add(part.cannon.get(i).cannonId);
        }
        for (int i = 0; i < part.boats.size(); ++i) {
            this.m_knownBoats.add(part.boats.get(i).boatId);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TravelHandler.class);
    }
}
