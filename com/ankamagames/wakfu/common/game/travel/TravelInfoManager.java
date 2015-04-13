package com.ankamagames.wakfu.common.game.travel;

import com.ankamagames.wakfu.common.game.travel.infos.*;
import gnu.trove.*;

public class TravelInfoManager
{
    public static final TravelInfoManager INSTANCE;
    private final TIntHashSet m_baseZaaps;
    private final TLongObjectHashMap<ZaapInfo> m_zaaps;
    private final TLongObjectHashMap<ZaapLink> m_zaapLinks;
    private final TLongObjectHashMap<DragoInfo> m_dragos;
    private final TLongObjectHashMap<BoatInfo> m_boats;
    private final TLongObjectHashMap<BoatLink> m_boatLinks;
    private final TLongObjectHashMap<CannonInfo> m_cannons;
    
    public TravelInfoManager() {
        super();
        this.m_baseZaaps = new TIntHashSet();
        this.m_zaaps = new TLongObjectHashMap<ZaapInfo>();
        this.m_zaapLinks = new TLongObjectHashMap<ZaapLink>();
        this.m_dragos = new TLongObjectHashMap<DragoInfo>();
        this.m_boats = new TLongObjectHashMap<BoatInfo>();
        this.m_boatLinks = new TLongObjectHashMap<BoatLink>();
        this.m_cannons = new TLongObjectHashMap<CannonInfo>();
    }
    
    public <Info extends TravelInfo> Info getInfo(final TravelType travelType, final long id) {
        switch (travelType) {
            case ZAAP: {
                return (Info)this.m_zaaps.get(id);
            }
            case DRAGO: {
                return (Info)this.m_dragos.get(id);
            }
            case BOAT: {
                return (Info)this.m_boats.get(id);
            }
            case CANNON: {
                return (Info)this.m_cannons.get(id);
            }
            default: {
                throw new IllegalArgumentException("Type de transport inconnu " + travelType);
            }
        }
    }
    
    public void addCannon(final CannonInfo cannon) {
        this.m_cannons.put(cannon.getId(), cannon);
    }
    
    public void addBoat(final BoatInfo boat) {
        this.m_boats.put(boat.getId(), boat);
    }
    
    public BoatLink addBoatLink(final BoatLink link) {
        if (!this.m_boats.containsKey(link.getStartBoatId()) || !this.m_boats.containsKey(link.getEndBoatId())) {
            throw new IllegalArgumentException("Impossible de rajouter un liens entre deux boats si ceux-ci n'existent pas");
        }
        this.m_boatLinks.put(link.getId(), link);
        return link;
    }
    
    public BoatLink getBoatLink(final long startBoatId, final long endBoatId) throws IllegalArgumentException {
        final TLongObjectIterator<BoatLink> it = this.m_boatLinks.iterator();
        while (it.hasNext()) {
            it.advance();
            final BoatLink link = it.value();
            final int start = link.getStartBoatId();
            final int end = link.getEndBoatId();
            if ((start == startBoatId && end == endBoatId) || (start == endBoatId && end == startBoatId)) {
                return link;
            }
        }
        throw new IllegalArgumentException("Impossible de trouver un lien entre les boats " + startBoatId + " et " + endBoatId);
    }
    
    public BoatLink getBoatLink(final long linkId) {
        return this.m_boatLinks.get(linkId);
    }
    
    public void foreachBoatLink(final TObjectProcedure<BoatLink> procedure) {
        this.m_boatLinks.forEachValue(procedure);
    }
    
    public void addDrago(final DragoInfo drago) {
        this.m_dragos.put(drago.getId(), drago);
    }
    
    public DragoInfo getDragoInfo(final long dragoId) {
        return this.m_dragos.get(dragoId);
    }
    
    public ZaapLink getZaapLink(final long startZaapId, final long endZaapId) throws IllegalArgumentException {
        final TLongObjectIterator<ZaapLink> it = this.m_zaapLinks.iterator();
        while (it.hasNext()) {
            it.advance();
            final ZaapLink link = it.value();
            final int start = link.getStartZaapId();
            final int end = link.getEndZaapId();
            if ((start == startZaapId && end == endZaapId) || (start == endZaapId && end == startZaapId)) {
                return link;
            }
        }
        throw new IllegalArgumentException("Impossible de trouver un lien entre les zaaps " + startZaapId + " et " + endZaapId);
    }
    
    public void addZaap(final ZaapInfo zaap, final boolean isBase) {
        this.m_zaaps.put(zaap.getId(), zaap);
        if (isBase) {
            this.m_baseZaaps.add((int)zaap.getId());
        }
    }
    
    public void addZaapLink(final ZaapLink link) {
        if (!this.m_zaaps.containsKey(link.getStartZaapId()) || !this.m_zaaps.containsKey(link.getEndZaapId())) {
            throw new IllegalArgumentException("Impossible de rajouter un liens entre deux zaaps si ces zaaps n'existent pas");
        }
        this.m_zaapLinks.put(link.getId(), link);
    }
    
    public void checkZaapLinksIntegrity() throws IllegalArgumentException {
        final TLongObjectIterator<ZaapInfo> it1 = this.m_zaaps.iterator();
        while (it1.hasNext()) {
            it1.advance();
            final TLongObjectIterator<ZaapInfo> it2 = this.m_zaaps.iterator();
            while (it2.hasNext()) {
                it2.advance();
                if (it1.key() != it2.key() && this.getZaapLink(it1.key(), it2.key()) == null) {
                    throw new IllegalArgumentException("Impossible de trouver un lien entre les zaaps " + it1.key() + " et " + it2.key());
                }
            }
        }
    }
    
    public int[] getBaseZaapIds() {
        return this.m_baseZaaps.toArray();
    }
    
    static {
        INSTANCE = new TravelInfoManager();
    }
}
