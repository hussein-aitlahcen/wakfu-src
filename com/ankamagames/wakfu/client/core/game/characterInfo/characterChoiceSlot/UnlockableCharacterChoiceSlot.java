package com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot;

import com.ankamagames.wakfu.client.core.game.webShop.*;
import org.jetbrains.annotations.*;

public class UnlockableCharacterChoiceSlot extends AbstractCharacterChoiceSlot implements Comparable<UnlockableCharacterChoiceSlot>
{
    public static final String ARTICLE = "article";
    private final Article m_article;
    private final int m_weight;
    
    protected UnlockableCharacterChoiceSlot(final Article article, final int weight) {
        super(CharacterChoiceSlotType.UNLOCKABLE_SLOT);
        this.m_article = article;
        this.m_weight = weight;
    }
    
    public Article getArticle() {
        return this.m_article;
    }
    
    @Override
    public String[] getFields() {
        return UnlockableCharacterChoiceSlot.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("article")) {
            return this.m_article;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public int compareTo(final UnlockableCharacterChoiceSlot o) {
        return this.m_weight - o.m_weight;
    }
}
