package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.buff.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class ProtectorBuffView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String STYLE = "style";
    public static final String EFFECT = "effect";
    public static final String ENABLED = "enabled";
    private final ProtectorBuff m_buff;
    private final Protector m_protector;
    private final boolean m_displayIcon;
    
    public ProtectorBuffView(final ProtectorBuff buff, final Protector protector) {
        this(buff, protector, true);
    }
    
    public ProtectorBuffView(final ProtectorBuff buff, final Protector protector, final boolean displayIcon) {
        super();
        this.m_buff = buff;
        this.m_protector = protector;
        this.m_displayIcon = displayIcon;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    private ProtectorSatisfactionLevel getSatisfactionLevel() {
        return getSatisfactionLevel(this.m_buff, this.m_protector);
    }
    
    public static ProtectorSatisfactionLevel getSatisfactionLevel(final ProtectorBuff buff, final Protector protector) {
        if (buff == null) {
            return ProtectorSatisfactionLevel.UNSATISFIED;
        }
        if (buff.getCriterion() == null || protector == null) {
            return ProtectorSatisfactionLevel.UNDEFINED;
        }
        if (buff.getCriterion().isValid(protector, protector, protector.getSatisfactionManager(), null)) {
            return protector.getSatisfactionLevel();
        }
        if (protector.getSatisfactionLevel() == ProtectorSatisfactionLevel.HALF_SATISFIED) {
            return ProtectorSatisfactionLevel.SATISFIED;
        }
        return ProtectorSatisfactionLevel.HALF_SATISFIED;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("effect")) {
            return this.getProtectorBuffs();
        }
        if (fieldName.equals("enabled")) {
            return this.isEnabled();
        }
        if (!fieldName.equals("style")) {
            return null;
        }
        if (!this.m_displayIcon) {
            return "";
        }
        if (this.m_buff != null && this.m_buff.getOrigin() == BuffOrigin.SHUKRUTE) {
            return "";
        }
        switch (this.getSatisfactionLevel()) {
            case UNSATISFIED: {
                return "PassportMDCBadBuff";
            }
            case HALF_SATISFIED: {
                return "PassportMDCMediumBuff";
            }
            case UNDEFINED:
            case SATISFIED: {
                return "PassportMDCGoodBuff";
            }
            default: {
                return "PassportMDCBadBuff";
            }
        }
    }
    
    private boolean isEnabled() {
        return isEnabled(this.m_protector, this.m_buff);
    }
    
    public static boolean isEnabled(final Protector protector, final ProtectorBuff buff) {
        if (protector == null || (buff == null && protector.getNationBuffs().length == 0)) {
            return true;
        }
        for (final ProtectorBuff protBuff : protector.getNationBuffs()) {
            if (buff == protBuff) {
                return true;
            }
        }
        return getSatisfactionLevel(buff, protector).getId() <= protector.getSatisfactionLevel().getId();
    }
    
    private String getProtectorBuffs() {
        return getProtectorBuffs(this.m_buff, this.m_displayIcon);
    }
    
    public static String getProtectorBuffs(final ProtectorBuff buff, final boolean displayIcon) {
        if (buff != null) {
            final DefaultContainerWriter<ProtectorBuff> writer = new DefaultContainerWriter<ProtectorBuff>(buff, buff.getId(), buff.getLevel());
            final ArrayList<String> effects = writer.writeContainer();
            final TextWidgetFormater sb = new TextWidgetFormater();
            for (int i = 0, size = effects.size(); i < size; ++i) {
                if (i != 0) {
                    sb.newLine();
                }
                sb.append(effects.get(i));
            }
            return sb.finishAndToString();
        }
        if (displayIcon) {
            return WakfuTranslator.getInstance().getString("protector.noBuff");
        }
        return WakfuTranslator.getInstance().getString("protector.noWill");
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorBuffView.class);
    }
    
    public static class ProtectorBuffViewComparator implements Comparator<ProtectorBuffView>
    {
        public static final ProtectorBuffViewComparator INSTANCE;
        
        @Override
        public int compare(final ProtectorBuffView o1, final ProtectorBuffView o2) {
            return o1.getSatisfactionLevel().getId() - o2.getSatisfactionLevel().getId();
        }
        
        static {
            INSTANCE = new ProtectorBuffViewComparator();
        }
    }
}
