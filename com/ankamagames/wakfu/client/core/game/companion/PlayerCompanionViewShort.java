package com.ankamagames.wakfu.client.core.game.companion;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;

public class PlayerCompanionViewShort extends ShortCharacterView
{
    private static final Logger m_logger;
    private final byte m_sex;
    private final long m_id;
    
    public PlayerCompanionViewShort(final Citizen character) {
        super(character);
        this.m_id = character.getId();
        this.m_sex = character.getSex();
    }
    
    public PlayerCompanionViewShort(final long id, final String name, final int breedId, final byte sex) {
        super(name, breedId);
        this.m_id = id;
        this.m_sex = sex;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (!fieldName.equals("smallIconUrl")) {
            if (!fieldName.equals("characterSheetSmallIllustrationUrl")) {
                return super.getFieldValue(fieldName);
            }
        }
        try {
            return String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), String.valueOf(this.m_breedId) + String.valueOf(this.m_sex));
        }
        catch (PropertyException e) {
            PlayerCompanionViewShort.m_logger.error((Object)"Exception", (Throwable)e);
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public boolean isPlayer() {
        return true;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public ShortCharacterView getCopy() {
        final ShortCharacterView shortCharacterView = new PlayerCompanionViewShort(this.getId(), this.getName(), this.getBreedId(), this.m_sex);
        return shortCharacterView;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayerCompanionViewShort.class);
    }
}
