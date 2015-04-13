package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.background.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;

public class NationSelectionView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NATION_FIELD = "nation";
    public static final String TOTAL_CASH_FIELD = "totalCash";
    public static final String POPULATION_SIZE_FIELD = "populationSize";
    public static final String GOVERNOR_NAME_FIELD = "governorName";
    public static final String TERRITORIES_COUNT_FIELD = "territoriesCount";
    public static final String NATION_ALIGNMENTS_FIELD = "nationAlignments";
    public static final String SELECTION_ICON_FIELD = "selectionIcon";
    public static final String BACKGROUND_IMAGE_FIELD = "backgroundImage";
    public static final String[] FIELDS;
    private final int m_nationId;
    private final int m_totalCash;
    private final float m_populationPercent;
    private final String m_governorName;
    private final int m_protectorsSize;
    private final TIntByteHashMap m_alignments;
    
    public NationSelectionView(final int nationId, final int totalCash, final float populationPercent, final String governorName, final int protectorsSize, final TIntByteHashMap alignments) {
        super();
        this.m_nationId = nationId;
        this.m_totalCash = totalCash;
        this.m_populationPercent = populationPercent;
        this.m_governorName = governorName;
        this.m_protectorsSize = protectorsSize;
        this.m_alignments = alignments;
    }
    
    @Override
    public String[] getFields() {
        return NationSelectionView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("nation")) {
            return new NationFieldProvider(this.m_nationId);
        }
        if (fieldName.equals("totalCash")) {
            return WakfuTranslator.getInstance().getString("kama.shortGain", this.m_totalCash);
        }
        if (fieldName.equals("populationSize")) {
            return (int)(this.m_populationPercent * 100.0f) + " %";
        }
        if (fieldName.equals("governorName")) {
            return (this.m_governorName == null || this.m_governorName.length() == 0) ? WakfuTranslator.getInstance().getString("nation.noGovernorDesc") : this.m_governorName;
        }
        if (fieldName.equals("territoriesCount")) {
            return this.m_protectorsSize;
        }
        if (fieldName.equals("nationAlignments")) {
            final TextWidgetFormater twf = new TextWidgetFormater();
            final TIntByteIterator it = this.m_alignments.iterator();
            while (it.hasNext()) {
                it.advance();
                twf.append(WakfuTranslator.getInstance().getString(39, it.key(), new Object[0]));
                twf.append(" - ");
                twf.append(WakfuTranslator.getInstance().getString("nation.alignmentName" + NationAlignement.getFromId(it.value()).name()));
                if (it.hasNext()) {
                    twf.newLine();
                }
            }
            return twf.finishAndToString();
        }
        if (fieldName.equals("selectionIcon")) {
            return WakfuConfiguration.getInstance().getNationSelectionIconUrl(this.m_nationId);
        }
        if (fieldName.equals("backgroundImage")) {
            return WakfuConfiguration.getInstance().getDisplayBackgroundBackgroundImage(BackgroundDisplayType.SCROLL.getId());
        }
        return null;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationSelectionView.class);
        FIELDS = new String[] { "nation", "totalCash", "populationSize", "governorName", "territoriesCount", "nationAlignments", "selectionIcon", "backgroundImage" };
    }
}
