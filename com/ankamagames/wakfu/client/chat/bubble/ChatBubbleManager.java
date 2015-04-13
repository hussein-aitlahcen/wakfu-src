package com.ankamagames.wakfu.client.chat.bubble;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.bubble.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.component.text.document.*;
import org.apache.commons.lang3.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.pet.*;
import com.ankamagames.wakfu.client.*;
import gnu.trove.*;

public class ChatBubbleManager extends AbstractChatBubbleManager
{
    private static final Logger m_logger;
    private static final TLongObjectHashMap<WakfuBubbleWidget> BUBBLES;
    private static final UISceneEventListener m_listener;
    
    public static void cleanAllBubbles() {
        final TLongObjectIterator<WakfuBubbleWidget> it = ChatBubbleManager.BUBBLES.iterator();
        while (it.hasNext()) {
            it.advance();
            final WakfuBubbleWidget bubble = it.value();
            bubble.unloadWidget();
        }
        ChatBubbleManager.BUBBLES.clear();
    }
    
    public static void cleanBubble(final long sourceId) {
        final WakfuBubbleWidget bubble = ChatBubbleManager.BUBBLES.remove(sourceId);
        if (bubble != null) {
            bubble.unloadWidget();
        }
    }
    
    @Nullable
    @Override
    public ChatMessage pushMessage(final ChatMessage message, final float[] color) {
        super.pushMessage(message, color);
        if (message == null) {
            return null;
        }
        final CharacterInfo characterInfo = getCharacterInfo(message);
        if (characterInfo == null) {
            return message;
        }
        final String messageContent = MultiplePartTextDocument.extractText(message.getMessage());
        final long characterId = characterInfo.getId();
        final boolean smileyDisplayed = UIChatFrame.getInstance().checkEmotesInSentence(messageContent, characterId);
        if (smileyDisplayed) {
            return message;
        }
        if (messageContent.length() == 0) {
            return null;
        }
        final WakfuBubbleWidget bubble = getOrCreateBubble(characterId, message);
        if (bubble == null) {
            ChatBubbleManager.m_logger.error((Object)("La bulle stock\u00e9e pour le mobdile de " + message.getSourceName() + " n'appartient plus \u00e0 personne !"));
            return message;
        }
        message.setMessage(StringUtils.replace(message.getMessage(), messageContent, bubble.setText(messageContent)));
        final Mobile mobile = MobileManager.getInstance().getMobile(characterId);
        if (mobile != null) {
            bubble.setTarget(mobile);
            bubble.setYOffset((int)(mobile.getVisualHeight() * 10.0f));
            bubble.setBubbleIsVisible(true);
            bubble.validateAdviser();
            bubble.setColor(new Color(color[0], color[1], color[2], (message.getPipeDestination() == 1) ? 0.0f : 0.75f));
            final boolean isNotLocalPlayerMessage = characterId != WakfuGameEntity.getInstance().getLocalPlayer().getId();
            if (isNotLocalPlayerMessage) {
                bubble.setBubbleObserver(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
            }
        }
        return message;
    }
    
    private static WakfuBubbleWidget getOrCreateBubble(final long characterId, final ChatMessage message) {
        final WakfuBubbleWidget bubble = ChatBubbleManager.BUBBLES.get(characterId);
        if (bubble == null) {
            return createBubble(characterId, message);
        }
        if (!reinitBubble(bubble, characterId, message)) {
            return null;
        }
        return bubble;
    }
    
    private static WakfuBubbleWidget createBubble(final long characterId, final ChatMessage message) {
        final String widgetId = WakfuBubbleUtils.getNewWakfuBubbleId();
        final WakfuBubbleWidget bubble = WakfuBubbleUtils.loadBubble(widgetId);
        bubble.initialize(false, message.isResizable());
        ChatBubbleManager.BUBBLES.put(characterId, bubble);
        return bubble;
    }
    
    private static boolean reinitBubble(final WakfuBubbleWidget bubble, final long characterId, final ChatMessage message) {
        final ScreenTarget target = bubble.getTarget();
        if (target == null) {
            return false;
        }
        assert characterId == ((Mobile)target).getId() : "Le mobile de " + message.getSourceName() + " ne correspond pas \u00e0 l'ancien";
        bubble.resetElapsedLifeTime();
        return true;
    }
    
    private static CharacterInfo getCharacterInfo(final ChatMessage message) {
        final String userName = message.getSourceName();
        final long sourceId = message.getSourceId();
        if (userName == null && sourceId == -1L) {
            return null;
        }
        if (sourceId == -1L) {
            return CharacterInfoManager.getInstance().getCharacterByName(userName);
        }
        return CharacterInfoManager.getInstance().getCharacter(sourceId);
    }
    
    public static WakfuBubbleWidget getLocalPlayerBubble() {
        return ChatBubbleManager.BUBBLES.get(WakfuGameEntity.getInstance().getLocalPlayer().getId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatBubbleManager.class);
        BUBBLES = new TLongObjectHashMap<WakfuBubbleWidget>();
        m_listener = new UISceneEventListener() {
            @Override
            public void onSceneInitializationComplete(final UIScene scene) {
            }
            
            @Override
            public void onProcess(final UIScene scene, final int deltaTime) {
            }
            
            @Override
            public void onResize(final UIScene scene, final int deltaWidth, final int deltaHeight) {
                ChatBubbleManager.cleanAllBubbles();
                final PetDialogDisplayer petDialogDisplayer = UITutorialFrame.getInstance().getPetDialogDisplayer();
                if (petDialogDisplayer == null) {
                    return;
                }
                petDialogDisplayer.clean();
            }
        };
        WakfuClientInstance.getInstance().getXulorScene().addEventListener(ChatBubbleManager.m_listener);
    }
}
