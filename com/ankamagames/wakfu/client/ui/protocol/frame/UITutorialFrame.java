package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.pet.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.tutorial.*;
import com.ankamagames.wakfu.client.ui.protocol.message.tutorial.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.*;

public class UITutorialFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UITutorialFrame m_instance;
    private PetDialogDisplayer m_petDialogDisplayer;
    private EventListener m_petDialogEndListener;
    private ArrayList<TutorialEventListener> m_tutorialEventListeners;
    private final LinkedList<PetMessage> m_awaitingMessages;
    
    public UITutorialFrame() {
        super();
        this.m_tutorialEventListeners = new ArrayList<TutorialEventListener>();
        this.m_awaitingMessages = new LinkedList<PetMessage>();
    }
    
    public static UITutorialFrame getInstance() {
        return UITutorialFrame.m_instance;
    }
    
    public void onDialogEnd() {
        PropertiesProvider.getInstance().setPropertyValue("petBubbleMessage", null);
        if (this.m_petDialogEndListener != null) {
            this.m_petDialogEndListener.run(null);
            this.m_petDialogEndListener = null;
        }
    }
    
    public EventListener getListener() {
        return this.m_petDialogEndListener;
    }
    
    public void setPetDialogEndEventListener(final EventListener el) {
        if (this.m_petDialogEndListener != null) {
            UITutorialFrame.m_logger.error((Object)"[LD/GD] Attention ! On vient d'\u00e9craser un listener non consum\u00e9 sur le dialogue du g\u00e9lutin !");
        }
        this.m_petDialogEndListener = el;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19150: {
                final UIMessage uiMessage = (UIMessage)message;
                final String text = uiMessage.getStringValue();
                final boolean forced = uiMessage.getBooleanValue();
                this.pushPetMessage(text, forced, uiMessage.getLongValue(), PetEmotion.getFromId(uiMessage.getIntValue()));
                return false;
            }
            case 19151: {
                this.setNextPetMessage();
                return false;
            }
            case 19148: {
                WakfuSoundManager.getInstance().playGUISound(600195L);
                final TutorialView removedTutorialView = (TutorialView)PropertiesProvider.getInstance().getObjectProperty("tutorialMessageView");
                final UITutorialMessage msg = (UITutorialMessage)message;
                final TutorialView tutorialView = new TutorialView(msg.getIcon(), msg.getTitle(), msg.getDesc(), msg.getType(), msg.getEventActionId());
                if (removedTutorialView != null && removedTutorialView.equals(tutorialView)) {
                    return false;
                }
                PropertiesProvider.getInstance().setPropertyValue("tutorialMessageView", tutorialView);
                if (removedTutorialView != null) {
                    this.onTutorialRemoved(removedTutorialView);
                }
                this.onTutorialLaunched(tutorialView);
                return false;
            }
            case 19149: {
                final TutorialView tutorialView2 = (TutorialView)PropertiesProvider.getInstance().getObjectProperty("tutorialMessageView");
                PropertiesProvider.getInstance().setPropertyValue("tutorialMessageView", null);
                if (tutorialView2 != null) {
                    this.onTutorialRemoved(tutorialView2);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onTutorialRemoved(final TutorialView tutorialView) {
        final int eventActionId = tutorialView.getEventActionId();
        if (eventActionId != -1) {
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventTutorialMessageRemoved(eventActionId));
        }
        for (int i = this.m_tutorialEventListeners.size() - 1; i >= 0; --i) {
            this.m_tutorialEventListeners.get(i).onTutorialRemoved(tutorialView);
        }
        this.removeParticleDecorators();
    }
    
    private void removeParticleDecorators() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("tutorialMessageDialog");
        final Container cont = (Container)map.getElement("particleContainer");
        cont.getAppearance().removeAllDecorators();
    }
    
    private void onTutorialLaunched(final TutorialView tutorialView) {
        for (int i = this.m_tutorialEventListeners.size() - 1; i >= 0; --i) {
            this.m_tutorialEventListeners.get(i).onTutorialLaunched(tutorialView);
        }
        this.addParticleDecorator();
    }
    
    private void addParticleDecorator() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("tutorialMessageDialog");
        final Container cont = (Container)map.getElement("particleContainer");
        cont.getAppearance().removeAllDecorators();
        final ParticleDecorator particleDecorator = new ParticleDecorator();
        particleDecorator.onCheckOut();
        particleDecorator.setFile("6001057.xps");
        particleDecorator.setAlignment(Alignment9.NORTH_WEST);
        particleDecorator.setFollowBorders(true);
        particleDecorator.setSpeed(1000.0f);
        particleDecorator.setTurnNumber(1);
        cont.getAppearance().add(particleDecorator);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("petBubbleMessage", null);
            PropertiesProvider.getInstance().setPropertyValue("tutorialMessageView", null);
            this.m_petDialogDisplayer = new PetDialogDisplayer();
            Xulor.getInstance().putActionClass("wakfu.petDialog", PetDialogDialogActions.class);
            PetMessage petMessage;
            while ((petMessage = this.m_awaitingMessages.poll()) != null) {
                this.m_petDialogDisplayer.pushMessage(petMessage);
            }
            Xulor.getInstance().load("tutorialMessageDialog", Dialogs.getDialogPath("tutorialMessageDialog"), 139280L, (short)10000);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_tutorialEventListeners.clear();
            this.m_petDialogDisplayer.clean();
            this.m_petDialogDisplayer = null;
            this.m_petDialogEndListener = null;
            Xulor.getInstance().removeActionClass("wakfu.petDialog");
            Xulor.getInstance().unload("tutorialMessageDialog");
        }
    }
    
    public void pushPetMessage(final String text, final boolean forced, final long duration, final PetEmotion emotion) {
        if (this.m_petDialogDisplayer == null) {
            this.m_awaitingMessages.add(new PetMessage(text, forced, duration, emotion));
            return;
        }
        this.m_petDialogDisplayer.pushMessage(new PetMessage(text, forced, duration, emotion));
    }
    
    public void setNextPetMessage() {
        this.m_petDialogDisplayer.setNextMessage();
    }
    
    public PetDialogDisplayer getPetDialogDisplayer() {
        return this.m_petDialogDisplayer;
    }
    
    public boolean add(final TutorialEventListener tutorialEventListener) {
        return this.m_tutorialEventListeners.add(tutorialEventListener);
    }
    
    public boolean remove(final TutorialEventListener o) {
        return this.m_tutorialEventListeners.remove(o);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UITutorialFrame.class);
        UITutorialFrame.m_instance = new UITutorialFrame();
    }
    
    public static class PetMessage
    {
        private String m_text;
        private long m_duration;
        private PetEmotion m_emotion;
        private boolean m_forced;
        
        PetMessage(final String text, final boolean forced, final long duration, final PetEmotion emotion) {
            super();
            this.m_text = text;
            this.m_forced = forced;
            this.m_duration = Math.max(0L, duration);
            this.m_emotion = emotion;
        }
        
        PetMessage(final String text, final boolean forced, final long duration) {
            this(text, forced, duration, PetEmotion.NEUTRAL);
        }
        
        PetMessage(final String text, final long duration) {
            this(text, duration <= 0L, duration);
        }
        
        PetMessage(final String text) {
            this(text, 0L);
        }
        
        public boolean isForced() {
            return this.m_forced;
        }
        
        public long getDuration() {
            return this.m_duration;
        }
        
        public String getText() {
            return this.m_text;
        }
        
        public PetEmotion getEmotion() {
            return this.m_emotion;
        }
    }
    
    public interface TutorialEventListener
    {
        void onTutorialRemoved(TutorialView p0);
        
        void onTutorialLaunched(TutorialView p0);
    }
}
