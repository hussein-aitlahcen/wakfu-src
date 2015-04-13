package com.ankamagames.wakfu.client.sound.validator;

import com.ankamagames.framework.sound.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.sound.util.*;

public class CharacterInfoSoundValidator implements SoundValidator
{
    private final CharacterInfo m_info;
    private static final TLongArrayList INTERNAL_FIGHT_EXCLUDED_PREFIX_IDS;
    private static final TLongArrayList EXTERNAL_FIGHT_PREFIX_IDS;
    private static final TLongArrayList OUT_FIGHT_PREFIX_IDS;
    
    public CharacterInfoSoundValidator(final CharacterInfo info) {
        super();
        this.m_info = info;
    }
    
    @Override
    public boolean canPlayGroundSound() {
        final int localPlayerFightId = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFightId();
        return localPlayerFightId != 0 && localPlayerFightId == this.m_info.getCurrentFightId();
    }
    
    @Override
    public boolean canPlaySound(final long id) {
        final int localPlayerFightId = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFightId();
        final long prefix = SoundGroupUtils.getSoundPrefix(id);
        if (localPlayerFightId != -1) {
            return localPlayerFightId == this.m_info.getCurrentFightId() && !CharacterInfoSoundValidator.INTERNAL_FIGHT_EXCLUDED_PREFIX_IDS.contains(prefix);
        }
        if (this.m_info.getCurrentFightId() != -1) {
            return CharacterInfoSoundValidator.EXTERNAL_FIGHT_PREFIX_IDS.contains(prefix);
        }
        return CharacterInfoSoundValidator.OUT_FIGHT_PREFIX_IDS.contains(prefix);
    }
    
    static {
        INTERNAL_FIGHT_EXCLUDED_PREFIX_IDS = new TLongArrayList(new long[] { 120L, 320L, 920L });
        EXTERNAL_FIGHT_PREFIX_IDS = new TLongArrayList(new long[] { 100L, 300L, 400L, 410L, 502L });
        (OUT_FIGHT_PREFIX_IDS = new TLongArrayList(new long[] { 130L, 330L, 502L, 930L })).add(CharacterInfoSoundValidator.INTERNAL_FIGHT_EXCLUDED_PREFIX_IDS.toNativeArray());
    }
}
