package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public abstract class ShortCharacterView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String CHARACTER_SMALL_ICON_URL_FIELD = "characterSheetSmallIllustrationUrl";
    public static final String BREED_ID_FIELD = "breedId";
    public static final String DRAG_ENABLED_FIELD = "dragEnabled";
    public static final String IS_PLAYER = "isPlayer";
    protected String m_name;
    protected final int m_breedId;
    protected boolean m_dragEnabled;
    
    protected ShortCharacterView(final Citizen character) {
        super();
        this.m_dragEnabled = true;
        this.m_name = character.getName();
        this.m_breedId = character.getBreedId();
    }
    
    protected ShortCharacterView(final String name, final int breedId) {
        super();
        this.m_dragEnabled = true;
        this.m_name = name;
        this.m_breedId = breedId;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("breedId")) {
            return this.m_breedId;
        }
        if (fieldName.equals("dragEnabled")) {
            return this.m_dragEnabled;
        }
        Label_0102: {
            if (!fieldName.equals("smallIconUrl")) {
                if (!fieldName.equals("characterSheetSmallIllustrationUrl")) {
                    break Label_0102;
                }
            }
            try {
                return String.format(WakfuConfiguration.getInstance().getString("companionIconsPath"), this.m_breedId);
            }
            catch (PropertyException e) {
                ShortCharacterView.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        if (fieldName.equals("isPlayer")) {
            return this.isPlayer();
        }
        return null;
    }
    
    public String getName() {
        if (this.m_name != null && !this.m_name.isEmpty()) {
            return this.m_name;
        }
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId((short)this.m_breedId);
        return breed.getName();
    }
    
    public int getBreedId() {
        return this.m_breedId;
    }
    
    public boolean isPlayer() {
        return false;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setDragEnabled(final boolean dragEnabled) {
        this.m_dragEnabled = dragEnabled;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "dragEnabled");
    }
    
    public abstract ShortCharacterView getCopy();
    
    static {
        m_logger = Logger.getLogger((Class)ShortCharacterView.class);
    }
}
