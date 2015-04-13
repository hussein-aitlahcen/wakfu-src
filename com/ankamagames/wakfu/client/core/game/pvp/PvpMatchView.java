package com.ankamagames.wakfu.client.core.game.pvp;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;

public class PvpMatchView extends ImmutableFieldProvider
{
    public static final String TYPE_NAME = "typeName";
    public static final String VICTORIES = "victories";
    public static final String DEFEATS = "defeats";
    public static final String MATCH_DESC = "matchDesc";
    public static final String FULL_MATCH_DESC = "fullMatchDesc";
    private final MatchType m_matchType;
    private PvpLadderEntry m_entry;
    
    public PvpMatchView(final MatchType matchType, final PvpLadderEntry entry) {
        super();
        this.m_matchType = matchType;
        this.m_entry = entry;
    }
    
    public void setEntry(final PvpLadderEntry entry) {
        this.m_entry = entry;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "victories", "defeats", "matchDesc", "fullMatchDesc");
    }
    
    public void updateValues() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "victories", "defeats", "matchDesc", "fullMatchDesc");
    }
    
    @Override
    public String[] getFields() {
        return PvpMatchView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("typeName".equals(fieldName)) {
            return this.getTypeName();
        }
        if ("victories".equals(fieldName)) {
            return this.getNumVictories();
        }
        if ("defeats".equals(fieldName)) {
            return this.getNumDefeats();
        }
        if ("fullMatchDesc".equals(fieldName)) {
            return this.getMatchDesc(true);
        }
        if ("matchDesc".equals(fieldName)) {
            return this.getMatchDesc(false);
        }
        return null;
    }
    
    private int getNumDefeats() {
        return this.m_entry.getNumDefeats(this.m_matchType);
    }
    
    private int getNumVictories() {
        return this.m_entry.getNumVictories(this.m_matchType);
    }
    
    private String getTypeName() {
        return WakfuTranslator.getInstance().getString("matchType." + this.m_matchType.ordinal());
    }
    
    private String getMatchDesc(final boolean full) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (full) {
            sb.append(this.getTypeName()).append(WakfuTranslator.getInstance().getString("colon"));
        }
        sb.openText().addColor(Color.GREEN).append(WakfuTranslator.getInstance().getString("victories", this.getNumVictories())).closeText();
        sb.append(" / ");
        sb.addColor(Color.RED).append(WakfuTranslator.getInstance().getString("defeats", this.getNumDefeats()));
        return sb.finishAndToString();
    }
}
