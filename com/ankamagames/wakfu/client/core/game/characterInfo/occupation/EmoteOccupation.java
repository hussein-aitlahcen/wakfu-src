package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.sound.group.music.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;

public class EmoteOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final CharacterInfo m_character;
    private int m_emoteId;
    private final HashMap<String, Object> m_vars;
    
    public EmoteOccupation(final CharacterInfo character) {
        super();
        this.m_vars = new HashMap<String, Object>();
        this.m_character = character;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 20;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    @Override
    public void begin() {
        EmoteOccupation.m_logger.info((Object)("[EMOTE] Lancement de l'occupation pour le joueur " + this.m_character));
        this.m_character.cancelCurrentOccupation(false, true);
        this.m_character.playEmote(this.m_emoteId, this.m_vars, true);
        this.m_character.setCurrentOccupation(this);
        if (this.needFadeMusic()) {
            this.fadeMusic();
        }
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        if (sendMessage) {
            final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
            netMsg.setModificationType((byte)3);
            netMsg.setOccupationType(this.getOccupationTypeId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        EmoteOccupation.m_logger.info((Object)("[EMOTE] Arr\u00eat de l'occupation pour le joueur " + this.m_character));
        if (this.needFadeMusic()) {
            this.resetFadeMusic();
        }
        return true;
    }
    
    public void build(final byte[] rawData) {
        final ByteBuffer bb = ByteBuffer.wrap(rawData);
        this.m_emoteId = bb.getInt();
        for (int varSize = bb.get(), i = 0; i < varSize; ++i) {
            final byte[] data = new byte[bb.getInt()];
            bb.get(data);
            this.m_vars.put(StringUtils.fromUTF8(data), bb.getLong());
        }
    }
    
    private boolean needFadeMusic() {
        final Emote emote = ReferenceEmoteManager.INSTANCE.getEmote(this.m_emoteId);
        if (emote == null) {
            EmoteOccupation.m_logger.error((Object)("Emote inconnu: " + this.m_emoteId));
            return false;
        }
        final boolean isLocalPlayer = this.m_character == WakfuGameEntity.getInstance().getLocalPlayer();
        return isLocalPlayer && emote.isMusical();
    }
    
    private void fadeMusic() {
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.fade(musicGroup.getGain() * 0.2f, 700.0f);
    }
    
    private void resetFadeMusic() {
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final float gain = wakfuGamePreferences.getFloatValue(KeyPreferenceStoreEnum.MUSIC_VOLUME_PREFERENCE_KEY);
        musicGroup.fade(gain, 700.0f);
    }
    
    @Override
    public boolean canPlayEmote() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EmoteOccupation.class);
    }
}
