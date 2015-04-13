package com.ankamagames.baseImpl.common.clientAndServer.game.loot;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.util.*;

public class DropTable<D extends Dropable>
{
    protected static final Logger m_logger;
    private static final Accumulator TMP_DROP_ACCUMULATOR;
    private static final TIntIntHashMap TMP_DROP_HELPER;
    private int m_id;
    protected final TIntObjectHashMap<D> m_drops;
    
    public DropTable(final int id) {
        super();
        this.m_drops = new TIntObjectHashMap<D>();
        this.m_id = id;
    }
    
    public DropTable() {
        super();
        this.m_drops = new TIntObjectHashMap<D>();
    }
    
    public void addDrop(final D drop) {
        this.m_drops.put(drop.getId(), drop);
    }
    
    @Nullable
    public D drop(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        final int dropId = this.dropId(dropUser, dropTarget, dropContent, dropContext);
        return (D)((dropId == -1) ? null : ((D)this.m_drops.get(dropId)));
    }
    
    public boolean hasDrops(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        return !this.m_drops.isEmpty();
    }
    
    protected boolean isDroppable(final D drop, final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        final short dropWeight = drop.getDropWeight();
        final SimpleCriterion criterion = drop.getCriterion();
        return dropWeight > 0 && (criterion == null || criterion.isValid(dropUser, dropTarget, dropContent, dropContext));
    }
    
    public int dropId(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        if (this.m_drops.isEmpty()) {
            return -1;
        }
        DropTable.TMP_DROP_HELPER.clear();
        DropTable.TMP_DROP_ACCUMULATOR.reset();
        if (!this.m_drops.isEmpty()) {
            this.m_drops.forEachEntry(new TIntObjectProcedure<D>() {
                @Override
                public boolean execute(final int key, final D drop) {
                    final short dropWeight = drop.getDropWeight();
                    if (DropTable.this.isDroppable(drop, dropUser, dropTarget, dropContent, dropContext)) {
                        DropTable.TMP_DROP_ACCUMULATOR.accumulate(dropWeight);
                        DropTable.TMP_DROP_HELPER.put(DropTable.TMP_DROP_ACCUMULATOR.getValue(), key);
                    }
                    return true;
                }
            });
        }
        final int rateSum = DropTable.TMP_DROP_ACCUMULATOR.getValue();
        if (rateSum == 0) {
            return -1;
        }
        final int roll = DiceRoll.roll(rateSum);
        final int[] ints = DropTable.TMP_DROP_HELPER.keys();
        Arrays.sort(ints);
        int selectedKey = -1;
        for (int i = 0, length = ints.length; i < length && selectedKey == -1; ++i) {
            if (ints[i] >= roll && roll > ((i > 0) ? ints[i - 1] : 0)) {
                selectedKey = ints[i];
            }
        }
        if (selectedKey == -1) {
            DropTable.m_logger.warn((Object)"Roll sur une DropTable non vide mais avec des poids de drop \u00e0 0");
            return -1;
        }
        final int id = DropTable.TMP_DROP_HELPER.get(selectedKey);
        if (id == -1) {
            DropTable.m_logger.warn((Object)"Roll \u00e9trange sur une dropTable : \u00e0 v\u00e9rifier");
        }
        return id;
    }
    
    public D getDrop(final int id) {
        return this.m_drops.get(id);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public TIntObjectHashMap<D> getDrops() {
        return this.m_drops;
    }
    
    public int size() {
        return this.m_drops.size();
    }
    
    @Override
    public String toString() {
        return "DropTable{m_id=" + this.m_id + ", m_drops=" + this.m_drops.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)DropTable.class);
        TMP_DROP_ACCUMULATOR = new Accumulator();
        TMP_DROP_HELPER = new TIntIntHashMap();
    }
}
