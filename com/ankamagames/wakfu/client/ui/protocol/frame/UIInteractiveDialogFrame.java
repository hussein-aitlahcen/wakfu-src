package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import com.ankamagames.wakfu.common.game.dialog.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.dialog.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import java.awt.event.*;
import com.ankamagames.wakfu.client.core.game.dialog.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.fight.handler.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public class UIInteractiveDialogFrame implements MessageFrame, FightEventRunnable
{
    protected static final Logger m_logger;
    private static final UIInteractiveDialogFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private WakfuClientDialogView m_dialogView;
    private ElementMap m_elementMap;
    private EventListener m_mouseClickedListener;
    private KeyboardController m_keyBoardControler;
    private boolean m_answersDisplayed;
    private DialogSourceType m_sourceType;
    private long m_sourceId;
    
    public static UIInteractiveDialogFrame getInstance() {
        return UIInteractiveDialogFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (UIFrameMouseKey.isKeyOrMouseMessage(message)) {
            return false;
        }
        switch (message.getId()) {
            case 16147: {
                final UIMessage uiMessage = (UIMessage)message;
                final int value = uiMessage.getIntValue();
                if (value == -1) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                    return false;
                }
                if (value == -2) {
                    this.m_dialogView.setNextQuestionIndex();
                    getInstance().processDialogStart();
                    return false;
                }
                final ValidateDialogRequestMessage validateDialogResultMessage = new ValidateDialogRequestMessage();
                validateDialogResultMessage.setDialogId(this.m_dialogView.getDialogId());
                validateDialogResultMessage.setChoiceId(value);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(validateDialogResultMessage);
                this.m_dialogView.reset();
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventDialogChoiceSelected(this.m_dialogView.getDialogId(), value));
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("interactiveDialogDialog")) {
                        if (UIInteractiveDialogFrame.this.m_dialogView != null) {
                            final CancelDialogRequestMessage msg = new CancelDialogRequestMessage();
                            msg.setDialogId(UIInteractiveDialogFrame.this.m_dialogView.getDialogId());
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                        }
                        WakfuGameEntity.getInstance().removeFrame(UIInteractiveDialogFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_elementMap = Xulor.getInstance().load("interactiveDialogDialog", Dialogs.getDialogPath("interactiveDialogDialog"), 257L, (short)18000).getElementMap();
            this.m_keyBoardControler = new KeyboardController() {
                @Override
                public boolean keyTyped(final KeyEvent keyEvent) {
                    return false;
                }
                
                @Override
                public boolean keyPressed(final KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() != 27) {
                        if (UIInteractiveDialogFrame.this.m_answersDisplayed && UIInteractiveDialogFrame.this.m_dialogView.isInsignificantAnswer()) {
                            InteractiveDialogActions.selectChoice(null, UIInteractiveDialogFrame.this.m_dialogView.getWakfuClientDialogChoiceView().get(0));
                        }
                        else {
                            UIInteractiveDialogFrame.this.displaySpeakerTextImmediately();
                        }
                    }
                    return false;
                }
                
                @Override
                public boolean keyReleased(final KeyEvent keyEvent) {
                    return false;
                }
            };
            this.m_mouseClickedListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    UIInteractiveDialogFrame.this.displaySpeakerTextImmediately();
                    return false;
                }
            };
            MasterRootContainer.getInstance().addEventListener(Events.MOUSE_PRESSED, this.m_mouseClickedListener, true);
            WakfuClientInstance.getInstance().getRenderer().pushKeyboardController(this.m_keyBoardControler, true);
            this.processDialogStart();
            Xulor.getInstance().putActionClass("wakfu.interactiveDialog", InteractiveDialogActions.class);
            RunnableFightListener.INSTANCE.addEventListener(FightEventType.FIGHTER_JOIN_FIGHT, this);
        }
    }
    
    private void displaySpeakerTextImmediately() {
        if (!this.m_answersDisplayed) {
            final TextView textView = (TextView)this.m_elementMap.getElement("textView");
            textView.displayAllTextImmediately();
            this.displayAnswers(true);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_dialogView = null;
            MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_PRESSED, this.m_mouseClickedListener, true);
            WakfuClientInstance.getInstance().getRenderer().removeKeyboardController(this.m_keyBoardControler);
            Xulor.getInstance().unload("interactiveDialogDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.interactiveDialog");
            InteractiveDialogScriptManager.INSTANCE.stopSound();
            CharacterInfo info = null;
            if (this.m_sourceType == DialogSourceType.NPC) {
                info = CharacterInfoManager.getInstance().getCharacter(this.m_sourceId);
            }
            else if (this.m_sourceType == DialogSourceType.PROTECTOR) {
                final Protector protector = ProtectorManager.INSTANCE.getProtector((int)this.m_sourceId);
                if (protector != null) {
                    info = protector.getNpc();
                }
            }
            if (info != null) {
                final CharacterActor characterActor = info.getActor();
                characterActor.setCanPlaySound(true);
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void startDialog(final int dialogId, final DialogSourceType sourceType, final long sourceId) {
        this.m_sourceId = sourceId;
        this.m_sourceType = sourceType;
        CharacterInfo info = null;
        if (sourceType == DialogSourceType.NPC) {
            info = CharacterInfoManager.getInstance().getCharacter(sourceId);
        }
        else if (sourceType == DialogSourceType.PROTECTOR) {
            final Protector protector = ProtectorManager.INSTANCE.getProtector((int)sourceId);
            if (protector != null) {
                info = protector.getNpc();
            }
        }
        if (info != null) {
            info.getActor().setCanPlaySound(false);
        }
        this.m_dialogView = new WakfuClientDialogView(DialogManager.INSTANCE.getDialog(dialogId), sourceType, sourceId);
        PropertiesProvider.getInstance().setPropertyValue("currentDialog", this.m_dialogView);
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    public void setNextDialog(final int dialogId) {
        if (this.m_dialogView == null) {
            UIInteractiveDialogFrame.m_logger.error((Object)("On tente de passer \u00e0 un dialogue d'id=" + dialogId + " alors qu'il n'y a pas de dialog en cours !"));
            return;
        }
        if (dialogId == 0) {
            WakfuGameEntity.getInstance().removeFrame(this);
            return;
        }
        this.m_dialogView.setCurrentDialog(DialogManager.INSTANCE.getDialog(dialogId));
        this.processDialogStart();
    }
    
    public void processDialogStart() {
        this.m_answersDisplayed = false;
        final TextView textView = (TextView)this.m_elementMap.getElement("textView");
        textView.addTimedCharDisplayListener(new TextWidget.TimedCharDisplayListener() {
            @Override
            public void onTimedCharDisplayedEnd() {
                textView.removeTimedCharDisplayListener(this);
                UIInteractiveDialogFrame.this.displayAnswers(true);
            }
        });
        if (this.m_dialogView != null) {
            InteractiveDialogScriptManager.INSTANCE.stopSound();
            InteractiveDialogScriptManager.INSTANCE.playDialogSound(this.m_dialogView.getFullId());
        }
    }
    
    private void displayAnswers(final boolean visible) {
        for (final WakfuClientDialogChoiceView wakfuClientDialogChoiceView : this.m_dialogView.getWakfuClientDialogChoiceView()) {
            wakfuClientDialogChoiceView.setVisible(visible);
        }
        this.m_answersDisplayed = true;
    }
    
    @Override
    public void runSpellCastEvent(final BasicCharacterInfo caster, final long spellElement) {
    }
    
    @Override
    public void runFighterEvent(final BasicCharacterInfo fighter) {
        if (fighter != WakfuGameEntity.getInstance().getLocalPlayer()) {
            return;
        }
        WakfuGameEntity.getInstance().removeFrame(this);
    }
    
    @Override
    public void runFightEvent() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIInteractiveDialogFrame.class);
        m_instance = new UIInteractiveDialogFrame();
    }
}
