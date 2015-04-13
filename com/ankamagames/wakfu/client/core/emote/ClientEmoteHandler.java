package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.wakfu.common.game.emote.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.util.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.datas.*;

public class ClientEmoteHandler extends EmoteHandler implements FieldProvider
{
    public static final float[] GOOD_CELL_COLOR;
    public static final float[] BAD_CELL_COLOR;
    public static final String EMOTES_WITH_TARGET_FIELD = "emotesWithTarget";
    public static final String EMOTES_FIELD = "emotes";
    public static final String SMILEYS_FIELD = "smileys";
    public static final String[] FIELDS;
    private final TIntObjectHashMap<EmoteSmileyFieldProvider> m_smileys;
    
    public ClientEmoteHandler() {
        super();
        this.m_smileys = new TIntObjectHashMap<EmoteSmileyFieldProvider>();
        this.clear();
    }
    
    @Override
    public void clear() {
        super.clear();
        for (final SmileyEnum smileyEnum : SmileyEnum.values()) {
            final int smileyId = smileyEnum.getId();
            this.m_smileys.put(smileyId, new Smiley(smileyId, smileyEnum.getCommandText()));
        }
    }
    
    @Override
    public boolean learnEmote(final int emoteId) {
        if (super.learnEmote(emoteId)) {
            final Emote emote = ReferenceEmoteManager.INSTANCE.getEmote(emoteId);
            if (emote != null) {
                emote.addEmotePatternToCommandDescriptor();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public String[] getFields() {
        return ClientEmoteHandler.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("emotesWithTarget")) {
            for (final Emote e : ReferenceEmoteManager.INSTANCE.getEmotesWithTarget()) {
                e.setKnown(this.m_knownEmotes.contains(e.getId()));
            }
            return ReferenceEmoteManager.INSTANCE.getEmotesWithTarget();
        }
        if (fieldName.equals("emotes")) {
            for (final Emote e : ReferenceEmoteManager.INSTANCE.getEmotes()) {
                e.setKnown(this.m_knownEmotes.contains(e.getId()));
            }
            return ReferenceEmoteManager.INSTANCE.getEmotes();
        }
        if (fieldName.equals("smileys")) {
            return this.m_smileys.getValues();
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public boolean useEmote(final int emoteId, final EmoteRunnableHandler emoteRunnableHandler) {
        final EmoteSmileyFieldProvider smiley = this.m_smileys.get(emoteId);
        if (smiley != null) {
            return this.executeSmiley(emoteId);
        }
        final Emote emote = ReferenceEmoteManager.INSTANCE.getEmote(emoteId);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.hasSubscriptionRight(SubscriptionRight.ALL_EMOTES)) {
            if (!SubscriptionEmoteAndTitleLimitations.AUTHORIZED_EMOTES_FOR_ALL.contains(emoteId)) {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight"), 3);
            }
            return false;
        }
        if (emote.isNeedTarget()) {
            EmoteTargetFrame.INSTANCE.init(emote, emoteRunnableHandler);
            return true;
        }
        if (this.canUseEmote(emote, emoteRunnableHandler)) {
            emoteRunnableHandler.runEmote(emote, -1L);
            return true;
        }
        return false;
    }
    
    private boolean executeSmiley(final int smileyId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        final SmileyEnum smiley = SmileyEnum.getSmileyFromId(smileyId);
        if (smiley == null) {
            return false;
        }
        final UIChatContentMessage contentMessage = new UIChatContentMessage();
        contentMessage.setMessage(smiley.getDefaultSmiley());
        Worker.getInstance().pushMessage(contentMessage);
        return true;
    }
    
    public boolean canUseEmote(final Emote emote, final EmoteRunnableHandler emoteRunnableHandler) {
        final int emoteToExecuteId = emote.getId();
        if (emoteRunnableHandler.emoteMustBeLearned() && !this.m_knownEmotes.contains(emoteToExecuteId)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
        if (currentOccupation != null && (!currentOccupation.canPlayEmote() || emote.isMoveToTarget() || emote.isNeedTarget())) {
            return false;
        }
        final CharacterActor actor = localPlayer.getActor();
        return actor.getCurrentPath() == null && SubscriptionEmoteAndTitleLimitations.isAuthorizedEmote(localPlayer.getAccountInformationHandler().getActiveSubscriptionLevel(), emote.getId());
    }
    
    public EmoteSmileyFieldProvider getEmote(final int emoteId) {
        return ReferenceEmoteManager.INSTANCE.getEmoteOrSmiley(emoteId);
    }
    
    @Override
    public void toRaw(final CharacterSerializedEmoteInventory part) {
        throw new UnsupportedOperationException("Pas de serialisation d'Emote dans le client");
    }
    
    static {
        GOOD_CELL_COLOR = new float[] { 0.0f, 1.0f, 0.0f, 0.5f };
        BAD_CELL_COLOR = new float[] { 1.0f, 0.0f, 0.0f, 0.5f };
        FIELDS = new String[] { "emotesWithTarget", "emotes", "smileys" };
    }
}
