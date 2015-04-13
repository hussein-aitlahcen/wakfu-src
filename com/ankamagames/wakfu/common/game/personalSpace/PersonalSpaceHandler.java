package com.ankamagames.wakfu.common.game.personalSpace;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.personalSpace.data.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class PersonalSpaceHandler
{
    protected static final Logger m_logger;
    protected final TIntHashSet m_knownViews;
    
    public PersonalSpaceHandler() {
        super();
        this.m_knownViews = new TIntHashSet();
    }
    
    public boolean learnView(final int viewId) {
        return this.m_knownViews.add(viewId);
    }
    
    public final boolean knowView(final int viewId) {
        return this.m_knownViews.contains(viewId);
    }
    
    public void clear() {
        this.m_knownViews.clear();
    }
    
    private void learnInnateViews() {
        final TIntHashSet innateViews = DimensionalBagModelViewManager.INSTANCE.getInnateViews();
        innateViews.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int viewId) {
                if (!PersonalSpaceHandler.this.knowView(viewId)) {
                    PersonalSpaceHandler.this.learnView(viewId);
                    PersonalSpaceHandler.m_logger.info((Object)("apprentissage de la custom de havre-sac inn\u00e9e id=" + viewId));
                }
                return true;
            }
        });
    }
    
    public void toRaw(final CharacterSerializedDimensionalBagViewInventory part) {
        final TIntIterator it = this.m_knownViews.iterator();
        while (it.hasNext()) {
            final CharacterSerializedDimensionalBagViewInventory.View view = new CharacterSerializedDimensionalBagViewInventory.View();
            view.viewId = it.next();
            part.views.add(view);
        }
    }
    
    public final void fromRaw(final CharacterSerializedDimensionalBagViewInventory part) {
        for (int i = 0; i < part.views.size(); ++i) {
            this.learnView(part.views.get(i).viewId);
        }
        this.learnInnateViews();
    }
    
    static {
        m_logger = Logger.getLogger((Class)PersonalSpaceHandler.class);
    }
}
