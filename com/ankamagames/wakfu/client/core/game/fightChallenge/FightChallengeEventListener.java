package com.ankamagames.wakfu.client.core.game.fightChallenge;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import java.util.*;

public class FightChallengeEventListener implements FightChallengeContextEventListener
{
    public static final FightChallengeEventListener INSTANCE;
    private final IntObjectLightWeightMap<FightChallengeView> m_views;
    private final ArrayList<AbstractFightChallengeView> m_lastViews;
    private final Object m_mutex;
    
    private FightChallengeEventListener() {
        super();
        this.m_views = new IntObjectLightWeightMap<FightChallengeView>();
        this.m_lastViews = new ArrayList<AbstractFightChallengeView>();
        this.m_mutex = new Object();
    }
    
    @Override
    public void onFightStart() {
        this.m_lastViews.clear();
    }
    
    @Override
    public void onChallengeAdded(final int challengeId) {
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventFightChallengeStart());
        synchronized (this.m_mutex) {
            this.m_views.put(challengeId, new FightChallengeView(challengeId));
        }
        FollowedQuestsManager.INSTANCE.onFightChallengeChanged();
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("tutorialMessageDialog");
                final Widget w = (Widget)map.getElement("fightChallenges");
                final ParticleDecorator particleDecorator = new ParticleDecorator();
                particleDecorator.onCheckOut();
                particleDecorator.setFile("6001051.xps");
                particleDecorator.setAlignment(Alignment9.CENTER);
                w.getAppearance().add(particleDecorator);
            }
        }, 500L, 1);
    }
    
    @Override
    public void onChallengeRemoved(final int challengeId) {
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (FightChallengeEventListener.this.m_mutex) {
                    FightChallengeEventListener.this.m_views.remove(challengeId);
                }
                FollowedQuestsManager.INSTANCE.onFightChallengeChanged();
            }
        }, 2500L, 1);
    }
    
    @Override
    public void onChallengeStateChanged(final int challengeId, final FightChallengeState state) {
        synchronized (this.m_mutex) {
            final FightChallengeView view = this.m_views.get(challengeId);
            view.onStateChanged();
            this.sendChatMessage(challengeId, state);
            if (state == FightChallengeState.SUCCESS) {
                WakfuSoundManager.getInstance().playGUISound(600074L);
                this.highlightChallenge(challengeId);
            }
            else if (state == FightChallengeState.FAILURE) {
                WakfuSoundManager.getInstance().playGUISound(600125L);
                this.highlightChallenge(challengeId);
            }
            this.m_lastViews.add(new FightChallengeStaticView(challengeId, state, view.getRewards()));
        }
    }
    
    private void highlightChallenge(final int challengeId) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("tutorialMessageDialog");
        final List list = (List)map.getElement("fightChallengesList");
        for (int i = 0, size = list.size(); i < size; ++i) {
            final FightChallengeView viewInList = (FightChallengeView)list.getValue(i);
            if (viewInList.getId() == challengeId) {
                final RenderableContainer rc = (RenderableContainer)list.getWidget(null, i);
                HighlightUIHelpers.highlightAllWidgetsInMap(rc.getInnerElementMap());
                return;
            }
        }
    }
    
    private void sendChatMessage(final int challengeId, final FightChallengeState state) {
        TextWidgetFormater sb = new TextWidgetFormater().b().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(140, challengeId, new Object[0]));
        final String name = sb.finishAndToString();
        String msg;
        if (state == FightChallengeState.SUCCESS) {
            msg = WakfuTranslator.getInstance().getString("fightChallenges.success", name);
        }
        else {
            if (state != FightChallengeState.FAILURE) {
                return;
            }
            msg = WakfuTranslator.getInstance().getString("fightChallenges.failure", name);
        }
        sb = new TextWidgetFormater().openText().addColor(ChatConstants.CHAT_GAME_INFORMATION_COLOR);
        sb.append(msg);
        final ChatMessage message = new ChatMessage(sb.finishAndToString());
        message.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(message);
    }
    
    public Collection<FightChallengeView> getChallenges() {
        synchronized (this.m_mutex) {
            if (this.m_views.isEmpty()) {
                return null;
            }
            final ArrayList<FightChallengeView> views = new ArrayList<FightChallengeView>();
            for (final FightChallengeView view : this.m_views) {
                views.add(view);
            }
            return views;
        }
    }
    
    public Collection<AbstractFightChallengeView> getLastViews() {
        final ArrayList<AbstractFightChallengeView> views = new ArrayList<AbstractFightChallengeView>();
        views.addAll(this.m_lastViews);
        return views;
    }
    
    static {
        INSTANCE = new FightChallengeEventListener();
    }
}
