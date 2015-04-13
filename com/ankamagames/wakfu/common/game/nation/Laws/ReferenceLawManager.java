package com.ankamagames.wakfu.common.game.nation.Laws;

import com.ankamagames.wakfu.common.game.nation.*;
import gnu.trove.*;

public class ReferenceLawManager implements NationManagerObserver
{
    public static ReferenceLawManager INSTANCE;
    private final TLongObjectHashMap<NationLaw> m_laws;
    private final TLongHashSet m_commonLaws;
    private final TIntObjectHashMap<TLongHashSet> m_nationRestrictedLaws;
    
    private ReferenceLawManager() {
        super();
        this.m_laws = new TLongObjectHashMap<NationLaw>();
        this.m_commonLaws = new TLongHashSet();
        this.m_nationRestrictedLaws = new TIntObjectHashMap<TLongHashSet>();
    }
    
    public void registerCommonLaw(final NationLaw law) {
        this.m_laws.put(law.getId(), law);
        this.m_commonLaws.add(law.getId());
    }
    
    public void registerRestrictedLaw(final int nationId, final NationLaw law) {
        this.m_laws.put(law.getId(), law);
        TLongHashSet laws = this.m_nationRestrictedLaws.get(nationId);
        if (laws == null) {
            this.m_nationRestrictedLaws.put(nationId, laws = new TLongHashSet());
        }
        laws.add(law.getId());
    }
    
    @Override
    public void onNationRegistered(final Nation nation) {
        final NationLawsManager manager = nation.getLawManager();
        final TLongIterator it = this.m_commonLaws.iterator();
        while (it.hasNext()) {
            manager.register(this.m_laws.get(it.next()));
        }
        final TLongHashSet laws = this.m_nationRestrictedLaws.get(nation.getNationId());
        if (laws == null) {
            return;
        }
        final TLongIterator it2 = laws.iterator();
        while (it2.hasNext()) {
            manager.register(this.m_laws.get(it2.next()));
        }
    }
    
    static {
        ReferenceLawManager.INSTANCE = new ReferenceLawManager();
    }
}
