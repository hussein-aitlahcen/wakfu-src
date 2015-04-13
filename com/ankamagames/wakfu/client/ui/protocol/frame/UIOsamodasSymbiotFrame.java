package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.BreedSpecific.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;

public class UIOsamodasSymbiotFrame implements MessageFrame, DialogUnloadListener
{
    private static UIOsamodasSymbiotFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private final ArrayList<String> m_openedMonsterDetail;
    private UISpellLevelSelectionMessage m_selectSpellSelectionMessage;
    public static final int DEGOB_SPELL_REFERENCE_ID = 787;
    
    public UIOsamodasSymbiotFrame() {
        super();
        this.m_openedMonsterDetail = new ArrayList<String>();
    }
    
    public static UIOsamodasSymbiotFrame getInstance() {
        return UIOsamodasSymbiotFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16814: {
                final LocalPlayerCharacter localPlayerInfo = WakfuGameEntity.getInstance().getLocalPlayer();
                final UIMessage msg = (UIMessage)message;
                final byte index = msg.getByteValue();
                final Symbiot symbiot = localPlayerInfo.getSymbiot();
                if (symbiot != null) {
                    if (index != symbiot.getCurrentCreatureIndex()) {
                        symbiot.setCurrentCreatureFromIndex(index);
                        final OsamodasSymbiotSelectCreatureMessage netMessage = new OsamodasSymbiotSelectCreatureMessage();
                        netMessage.setCreatureId(index);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                    }
                    if (localPlayerInfo.getCurrentFight() != null && this.m_selectSpellSelectionMessage != null && !WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastInteractionFrame.getInstance())) {
                        Worker.getInstance().pushMessage(this.m_selectSpellSelectionMessage);
                        localPlayerInfo.updateShortcutBars();
                    }
                }
                return false;
            }
            case 16815: {
                final Symbiot symbiot2 = WakfuGameEntity.getInstance().getLocalPlayer().getSymbiot();
                final UIMessage msg = (UIMessage)message;
                if (symbiot2 == null || symbiot2.size() > 1) {
                    return false;
                }
                return false;
            }
            case 16816: {
                final UIMessage msg = (UIMessage)message;
                final byte index2 = msg.getByteValue();
                if (WakfuGameEntity.getInstance().getLocalPlayer().isWaitingForResult()) {
                    return false;
                }
                final LocalPlayerCharacter localPlayerInfo2 = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayerInfo2.getSymbiot() != null) {
                    final SymbiotInvocationCharacteristics fromIndex = (SymbiotInvocationCharacteristics)localPlayerInfo2.getSymbiot().getCreatureParametersFromIndex(index2);
                    if (localPlayerInfo2.getSymbiot().getCreatureParametersFromIndex(index2) != null) {
                        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.osaFreeCreature", fromIndex.getShortDescription()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                        messageBoxControler.addEventListener(new MessageBoxEventListener() {
                            @Override
                            public void messageBoxClosed(final int type, final String userEntry) {
                                if (type == 8) {
                                    UIOsamodasSymbiotFrame.this.openCloseCreatureDescription(fromIndex);
                                    localPlayerInfo2.getSymbiot().destroyCreaturesOnIndex(index2);
                                }
                            }
                        });
                    }
                }
                return false;
            }
            case 16817: {
                final LocalPlayerCharacter localPlayerInfo = WakfuGameEntity.getInstance().getLocalPlayer();
                final UIMessage msg = (UIMessage)message;
                final byte index = msg.getByteValue();
                if (localPlayerInfo.getSymbiot() != null) {
                    final BasicInvocationCharacteristics creatureToRename = localPlayerInfo.getSymbiot().getCreatureParametersFromIndex(index);
                    if (creatureToRename != null && ((SymbiotInvocationCharacteristics)creatureToRename).canBeRenamed()) {
                        if (WordsModerator.getInstance().validateName(msg.getStringValue()) && msg.getStringValue().length() > 0) {
                            creatureToRename.setName(msg.getStringValue());
                            final OsamodasSymbiotRenameCreatureMessage netMessage2 = new OsamodasSymbiotRenameCreatureMessage();
                            netMessage2.setCreatureIndex(index);
                            netMessage2.setCreatureName(msg.getStringValue());
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                        }
                        else {
                            final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.operationNotPermited");
                            final ChatMessage error = new ChatMessage(errorMessage);
                            error.setPipeDestination(3);
                            ChatManager.getInstance().pushMessage(error);
                        }
                    }
                }
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
            final UISpellLevelSelectionMessage selectionMessage = new UISpellLevelSelectionMessage();
            selectionMessage.setSpell(WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(787));
            selectionMessage.setId(18002);
            this.m_selectSpellSelectionMessage = selectionMessage;
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("osamodasSymbiotDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIOsamodasSymbiotFrame.getInstance());
                    }
                    else if (id.startsWith("monsterDetailDialog") && UIOsamodasSymbiotFrame.this.m_openedMonsterDetail.contains(id)) {
                        UIOsamodasSymbiotFrame.this.m_openedMonsterDetail.remove(id);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            SymbiotView.getInstance().setSymbiot(WakfuGameEntity.getInstance().getLocalPlayer().getSymbiot());
            PropertiesProvider.getInstance().setPropertyValue("osamodasSymbiot", SymbiotView.getInstance());
            Xulor.getInstance().load("osamodasSymbiotDialog", Dialogs.getDialogPath("osamodasSymbiotDialog"), 32769L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.osamodasSymbiot", OsamodasSymbiotDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            for (final String id : (ArrayList)this.m_openedMonsterDetail.clone()) {
                if (Xulor.getInstance().isLoaded(id)) {
                    Xulor.getInstance().unload(id);
                }
            }
            this.m_openedMonsterDetail.clear();
            Xulor.getInstance().unload("osamodasSymbiotDialog");
            PropertiesProvider.getInstance().removeProperty("osamodasSymbiot");
            PropertiesProvider.getInstance().removeProperty("osamodasSymbiot.describedCreature");
            Xulor.getInstance().removeActionClass("wakfu.osamodasSymbiot");
            this.m_selectSpellSelectionMessage = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setSpellLaunchMessage(final UISpellLevelSelectionMessage selectionMessage) {
        this.m_selectSpellSelectionMessage = selectionMessage;
        if (WakfuGameEntity.getInstance().getLocalPlayer().getSymbiot().getCurrentCreatureIndex() != -1) {
            Worker.getInstance().pushMessage(this.m_selectSpellSelectionMessage);
        }
    }
    
    public void highLightContainer() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("osamodasSymbiotDialog");
        if (map == null) {
            return;
        }
        final Container container = (Container)map.getElement("mainContainer");
        if (container == null) {
            return;
        }
        final Color c = new Color(0.531f, 0.812f, 0.835f, 1.0f);
        final Color c2 = new Color(Color.WHITE.get());
        container.removeTweensOfType(ModulationColorTween.class);
        final AbstractTween t = new ModulationColorTween(c, c2, container.getAppearance(), 0, 500, 5, TweenFunction.PROGRESSIVE);
        container.getAppearance().addTween(t);
    }
    
    public void openCloseCreatureDescription(final SymbiotInvocationCharacteristics info) {
        final String dialogId = "monsterDetailDialog" + WakfuGameEntity.getInstance().getLocalPlayer().getSymbiot().getIndexByCreature(info);
        if (Xulor.getInstance().isLoaded(dialogId)) {
            Xulor.getInstance().unload(dialogId);
            this.m_openedMonsterDetail.remove(dialogId);
        }
        else {
            this.m_openedMonsterDetail.add(dialogId);
            Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("monsterDetailDialog"), 1L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("monsterDetail", info, dialogId);
        }
    }
    
    @Override
    public void dialogUnloaded(final String id) {
        this.m_openedMonsterDetail.remove(id);
    }
    
    static {
        UIOsamodasSymbiotFrame.m_instance = new UIOsamodasSymbiotFrame();
    }
}
