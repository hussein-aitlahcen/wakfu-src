package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.nation.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class NationDialogActions
{
    public static final String PACKAGE = "wakfu.nation";
    public static final byte GOVERNMENT_PAGE_INDEX = 0;
    public static final byte DOMESTIC_POLICY_PAGE_INDEX = 1;
    public static final byte LAWS_PAGE_INDEX = 2;
    public static final byte INTERNATIONNAL_POLICY_PAGE_INDEX = 3;
    private static boolean m_tween;
    
    public static void onKeyPress(final Event event) {
        if (event.getType() == Events.KEY_PRESSED) {
            final TextEditor textEditor = event.getTarget();
            final KeyEvent keyPressedEvent = (KeyEvent)event;
            if (!checkText(textEditor, keyPressedEvent.getKeyChar() == '\n')) {}
            final String text = textEditor.getText();
            if (text != null && text.length() > 0) {
                PropertiesProvider.getInstance().setPropertyValue("speechDirty", true);
            }
        }
    }
    
    public static boolean checkText(final TextEditor te, final boolean lastCharIsCarriage) {
        boolean valid = true;
        String text = te.getText();
        int count = lastCharIsCarriage ? 1 : 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == '\n' && ++count > 3) {
                text = text.substring(0, i) + " " + text.substring(i + 1, text.length());
            }
        }
        if (count > 3) {
            valid = false;
            te.setText(text);
            msgBox(WakfuTranslator.getInstance().getString("carriageReturnLimitError"), te);
        }
        return valid;
    }
    
    private static void msgBox(final String msg, final TextEditor te) {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(msg, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                FocusManager.getInstance().setFocused(te);
            }
        });
    }
    
    public static void cancelGovernorSpeech(final Event e) {
        NationDisplayer.getInstance().setTemporarySpeech(null);
        NationDisplayer.getInstance().updateGovernor();
        PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", true);
        PropertiesProvider.getInstance().setPropertyValue("speechDirty", false);
    }
    
    public static void cancelMarshalSpeech(final Event e) {
        NationDisplayer.getInstance().setTemporarySpeech(null);
        NationDisplayer.getInstance().updateMarshal();
        PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", true);
        PropertiesProvider.getInstance().setPropertyValue("speechDirty", false);
    }
    
    public static void cancelGeneralSpeech(final Event e) {
        NationDisplayer.getInstance().setTemporarySpeech(null);
        NationDisplayer.getInstance().updateGeneral();
        PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", true);
        PropertiesProvider.getInstance().setPropertyValue("speechDirty", false);
    }
    
    public static void validateGovernorSpeech(final Event e, final TextEditor te) {
        validate(e, te, 18403, NationDisplayer.getInstance().getGovernor());
    }
    
    public static void validateMarshalSpeech(final Event e, final TextEditor te) {
        validate(e, te, 18417, NationDisplayer.getInstance().getMarshal());
    }
    
    public static void validateGeneralSpeech(final Event e, final TextEditor te) {
        validate(e, te, 18418, NationDisplayer.getInstance().getGeneral());
    }
    
    public static void validate(final Event e, final TextEditor te, final int messageId, final GovernmentMemberInfoFieldProvider member) {
        if (!checkText(te, false)) {
            return;
        }
        final String text = te.getText();
        final String censoredSentence = WakfuWordsModerator.makeValidSentence(text);
        te.setText(censoredSentence);
        if (censoredSentence.length() == 0 && text.length() != 0) {
            msgBox(WakfuTranslator.getInstance().getString("error.censoredSentence"), te);
            return;
        }
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("nation.speechWarning"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final UIMessage msg = new UIMessage();
                    msg.setId(messageId);
                    msg.setStringValue(te.getText());
                    Worker.getInstance().pushMessage(msg);
                    NationDisplayer.getInstance().setTemporarySpeech(null);
                    member.setCurrentSpeech(te.getText());
                    NationDisplayer.getInstance().updateMember(member);
                }
            }
        });
    }
    
    public static void changeTitle(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final UIMessage uiMessage = new UIMessage();
            uiMessage.setId(18404);
            final GovernorHonorificTitle honorificTitle = ((GovernorHonorificTitleView)event.getValue()).getTitle();
            uiMessage.setShortValue((short)((honorificTitle == null) ? -1 : honorificTitle.getId()));
            Worker.getInstance().pushMessage(uiMessage);
        }
    }
    
    public static void switchGovernmentVisibility(final Event e, final Button switchButton, final Container container) {
        switchVisibility(switchButton, container);
    }
    
    public static void switchEconomyVisibility(final Event e, final Button switchButton, final Container container) {
        if (switchVisibility(switchButton, container)) {
            UIMessage.send((short)18406);
        }
    }
    
    public static void switchGovernorPopularityVisibility(final Event e, final Button switchButton, final Container container) {
        if (switchVisibility(switchButton, container)) {
            UIMessage.send((short)18415);
        }
    }
    
    public static void switchOtherNationLaws(final Event e, final Button switchButton, final Container container, final NationDisplayer.LawsView lawsView) {
        if (switchVisibility(switchButton, container)) {
            NationDisplayer.getInstance().onOtherNationLawsOpenned(lawsView.getLocalNation());
            final UIMessage uiMessage = new UIMessage();
            uiMessage.setId(18414);
            uiMessage.setIntValue(lawsView.getLocalNation().getNationId());
            Worker.getInstance().pushMessage(uiMessage);
        }
        else {
            NationDisplayer.getInstance().onOtherNationLawsClosed(lawsView.getLocalNation());
        }
    }
    
    public static boolean switchVisibility(final Button switchButton, final Container container) {
        final boolean visible = !container.isVisible();
        container.setVisible(visible);
        switchButton.setStyle(visible ? "YellowUpArrow" : "YellowDownArrow");
        return visible;
    }
    
    public static void displayPage(final Event e, final String value) {
        final Byte pageIndex = Byte.valueOf(value);
        final int currentPageIndex = PropertiesProvider.getInstance().getIntProperty("nationCurrentPageIndex");
        if (pageIndex == currentPageIndex) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("nationCurrentPageIndex", pageIndex);
        final CitizenComportment citizen = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
        final Protector protector = ProtectorView.getInstance().getProtector();
        final int nationId = (protector == null) ? -1 : protector.getCurrentNation().getNationId();
        if (pageIndex == 1) {
            final NationProtectorsInformationRequestMessage msg = new NationProtectorsInformationRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
            UINationFrame.getInstance().setNetEnable(false);
            NationDisplayer.getInstance().setWaitingForDiplomacyInformation(false);
        }
        else if (pageIndex == 2) {
            NationDisplayer.getInstance().clearLaws();
            PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.getInstance(), "laws");
            if (citizen.getNationId() != nationId) {
                final OtherNationLawsRequestMessage msg2 = new OtherNationLawsRequestMessage();
                msg2.setNationId(citizen.getNationId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg2);
                UINationFrame.getInstance().setNetEnable(false);
            }
        }
        else if (pageIndex == 3) {
            NationDisplayer.getInstance().setWaitingForDiplomacyInformation(true);
            final NationDiplomacyInformationRequestMessage msg3 = new NationDiplomacyInformationRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg3);
            UINationFrame.getInstance().setNetEnable(false);
        }
        else {
            NationDisplayer.getInstance().setWaitingForDiplomacyInformation(false);
        }
    }
    
    public static void detailRankMember(final Event e, final NationRankDisplayer rank) {
        final UIMessage msg = new UIMessage();
        msg.setId(18409);
        msg.setLongValue(rank.getRankId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void revokeRankMember(final Event e, final NationRankDisplayer rank) {
        final NationGovernment government = NationDisplayer.getInstance().getNation().getGovernment();
        final GovernmentInfo localMember = government.getMember(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString((localMember.getRank().getId() == rank.getRankId()) ? "nation.governmentResignationConfirmation" : "nation.governmentRevokeConfirmation", rank.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final UIMessage msg = new UIMessage();
                    msg.setId(18408);
                    msg.setLongValue(rank.getRankId());
                    Worker.getInstance().pushMessage(msg);
                }
            }
        });
    }
    
    public static void addToRank(final Event e, final NationRankDisplayer rank, final TextEditor ed) {
        if (e.getType() == Events.KEY_PRESSED && ((KeyEvent)e).getKeyCode() != 10) {
            return;
        }
        final String value = ed.getText();
        if (value != null && value.length() > 0) {
            final UIMessage msg = new UIMessage();
            msg.setId(18407);
            msg.setLongValue(rank.getRankId());
            msg.setStringValue(value);
            Worker.getInstance().pushMessage(msg);
            ed.setText("");
            FocusManager.getInstance().setFocused(null);
        }
    }
    
    public static void switchVisualizeSpeech(final Event e, final TextEditor te) {
        final boolean visible = PropertiesProvider.getInstance().getBooleanProperty("speechVisualisationMode");
        PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", !visible);
        if (!visible) {
            NationDisplayer.getInstance().setTemporarySpeech(te.getText());
            NationDisplayer.getInstance().updateGovernor();
        }
    }
    
    public static void switchVisualizeMarshalSpeech(final Event e, final TextEditor te) {
        final boolean visible = PropertiesProvider.getInstance().getBooleanProperty("speechVisualisationMode");
        PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", !visible);
        if (!visible) {
            NationDisplayer.getInstance().setTemporarySpeech(te.getText());
            NationDisplayer.getInstance().updateMarshal();
        }
    }
    
    public static void switchVisualizeGeneralSpeech(final Event e, final TextEditor te) {
        final boolean visible = PropertiesProvider.getInstance().getBooleanProperty("speechVisualisationMode");
        PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", !visible);
        if (!visible) {
            NationDisplayer.getInstance().setTemporarySpeech(te.getText());
            NationDisplayer.getInstance().updateGeneral();
        }
    }
    
    public static void cancelLawPoints(final Event e) {
        UIMessage.send((short)18411);
    }
    
    public static void validateLawPoints(final Event e) {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(NationDisplayer.getInstance().getNation().getLawManager().isFirstChange() ? "nation.firstLawChangesConfirmation" : "nation.lawChangesConfirmation"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 537L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                switch (type) {
                    case 8: {
                        UIMessage.send((short)18412);
                        break;
                    }
                }
            }
        });
    }
    
    public static void chooseLaw(final SelectionChangedEvent e, final LawView lawView) {
        if (e.isSelected() == lawView.isGovernorActivated()) {
            return;
        }
        if (!e.isSelected() || lawView.getLaw().getLawPointCost() + NationDisplayer.getInstance().getCurrentLawPoints() <= 50) {
            lawView.setActivationStatut(e.isSelected());
            final UIMessage uiMessage = new UIMessage();
            uiMessage.setLongValue(lawView.getLaw().getId());
            uiMessage.setId(18410);
            Worker.getInstance().pushMessage(uiMessage);
            return;
        }
        if (NationDialogActions.m_tween) {
            return;
        }
        final ElementMap map = e.getCurrentTarget().getElementMap();
        if (map == null) {
            return;
        }
        final ToggleButton tb = e.getCurrentTarget();
        final Label label = (Label)map.getElement("lawPointsLabel");
        final Color c = new Color(1.0f, 0.2f, 0.2f, 1.0f);
        final Color c2 = Color.WHITE;
        label.removeTweensOfType(ModulationColorTween.class);
        tb.removeTweensOfType(ModulationColorTween.class);
        final AbstractTween t = new ModulationColorTween(c, c2, label, 0, 250, 5, TweenFunction.PROGRESSIVE);
        label.addTween(t);
        final AbstractTween t2 = new ModulationColorTween(c, c2, tb, 0, 250, 5, TweenFunction.PROGRESSIVE);
        tb.addTween(t2);
        NationDialogActions.m_tween = true;
        t2.addTweenEventListener(new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                switch (e) {
                    case TWEEN_ENDED: {
                        NationDialogActions.m_tween = false;
                        tb.setSelected(false);
                        t2.removeTweenEventListener(this);
                        break;
                    }
                }
            }
        });
    }
    
    public static void suggestAlliance(final Event e, final NationDiplomacyView nationDiplomacyView) {
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("nation.suggestAllianceConfirmation", nationDiplomacyView.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final UIChangeNationRelationShipMessage uiMessage = new UIChangeNationRelationShipMessage();
                    uiMessage.setNationId(nationDiplomacyView.getNationId());
                    uiMessage.setAlignmentId(NationAlignement.ALLIED.getId());
                    Worker.getInstance().pushMessage(uiMessage);
                }
            }
        });
    }
    
    public static void declareWar(final Event e, final NationDiplomacyView nationDiplomacyView) {
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("nation.declareWarConfirmation", nationDiplomacyView.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final UIChangeNationRelationShipMessage uiMessage = new UIChangeNationRelationShipMessage();
                    uiMessage.setNationId(nationDiplomacyView.getNationId());
                    uiMessage.setAlignmentId(NationAlignement.ENEMY.getId());
                    Worker.getInstance().pushMessage(uiMessage);
                }
            }
        });
    }
    
    public static void validProposal(final Event e, final NationDiplomacyView nationDiplomacyView) {
        final UIChangeNationRelationShipMessage uiMessage = new UIChangeNationRelationShipMessage();
        uiMessage.setNationId(nationDiplomacyView.getNationId());
        uiMessage.setAlignmentId(nationDiplomacyView.isWaitingForAllianceAnswer() ? NationAlignement.ALLIED.getId() : NationAlignement.ENEMY.getId());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void cancelProposal(final Event e, final NationDiplomacyView nationDiplomacyView) {
        final UIChangeNationRelationShipMessage uiMessage = new UIChangeNationRelationShipMessage();
        uiMessage.setNationId(nationDiplomacyView.getNationId());
        uiMessage.setAlignmentId(nationDiplomacyView.getLocalAlignment().getId());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void changeOpinion(final Event e, final String opinionId) {
        NationDisplayer.getInstance().setCurrentOpinionId(Byte.parseByte(opinionId));
    }
    
    public static void confirmOpinionChange(final Event e) {
        UIMessage.send((short)18416);
    }
    
    public static void onZoneOver(final PopupEvent e, final ProtectorInListView protectorInListView) {
        PropertiesProvider.getInstance().setPropertyValue("protectorInList", protectorInListView);
    }
    
    public static void onZoneOut(final PopupEvent e) {
        PropertiesProvider.getInstance().removeProperty("protectorInList");
    }
    
    public static void onFocusGain(final FocusChangedEvent e, final Button b) {
        b.setEnabled(true);
    }
}
