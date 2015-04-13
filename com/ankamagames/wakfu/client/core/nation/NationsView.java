package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import gnu.trove.*;
import java.util.*;

public class NationsView extends ImmutableFieldProvider
{
    public static final String NATIONS = "nations";
    public static final NationsView INSTANCE;
    private final Collection<NationFieldProvider> m_nations;
    private final TIntObjectHashMap<NationFieldProvider> m_nationsById;
    
    private NationsView() {
        super();
        this.m_nations = new ArrayList<NationFieldProvider>();
        this.m_nationsById = new TIntObjectHashMap<NationFieldProvider>();
        this.addNation(new NationFieldProvider(30));
        this.addNation(new NationFieldProvider(31));
        this.addNation(new NationFieldProvider(32));
        this.addNation(new NationFieldProvider(33));
        this.addNation(new NationFieldProvider(34));
    }
    
    private void addNation(final NationFieldProvider nation) {
        this.m_nations.add(nation);
        this.m_nationsById.put(nation.getNationId(), nation);
    }
    
    public NationFieldProvider getNation(final int nationId) {
        return this.m_nationsById.get(nationId);
    }
    
    @Override
    public String[] getFields() {
        return NationsView.NO_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("nations")) {
            return this.m_nations;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "NationsView{m_nations=" + this.m_nations + ", m_nationsById=" + this.m_nationsById + '}';
    }
    
    static {
        INSTANCE = new NationsView();
    }
}
