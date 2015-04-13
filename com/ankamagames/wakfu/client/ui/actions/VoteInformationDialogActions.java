package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.nation.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class VoteInformationDialogActions
{
    public static final String PACKAGE = "wakfu.voteInformation";
    
    public static void voteFor(final Event e, final CandidateInfoFieldProvider cc) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null || localPlayer.getId() != cc.getCandidateInfo().getId()) {
            final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("nation.vote.voteFor.warning"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        final UICandidateInfoMessage msg = new UICandidateInfoMessage();
                        msg.setId(18401);
                        msg.setCandidateInfo(cc);
                        Worker.getInstance().pushMessage(msg);
                    }
                }
            });
        }
        else {
            final UICandidateInfoMessage msg = new UICandidateInfoMessage();
            msg.setId(18401);
            msg.setCandidateInfo(cc);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void closeDialog(final Event e) {
        WakfuGameEntity.getInstance().removeFrame(UIVoteInformationFrame.getInstance());
    }
    
    public static void validate(final Event e, final TextEditor te) {
        final String text = te.getText();
        final String moderatedSentence = WakfuWordsModerator.makeValidSentence(text);
        if (moderatedSentence.length() == 0 && text.length() != 0) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.censoredSentence"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 2L, 102, 1);
            return;
        }
        te.setText(moderatedSentence);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("nation.vote.candidateRegistration.warning"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final UIMessage msg = new UIMessage();
                    msg.setId(18400);
                    msg.setStringValue(te.getText());
                    Worker.getInstance().pushMessage(msg);
                    te.setText("");
                    PropertiesProvider.getInstance().setPropertyValue("electionSloganDirty", false);
                }
            }
        });
    }
    
    public static void onKeyPress(final Event event) {
        if (event.getType() == Events.KEY_PRESSED) {
            final TextEditor textEditor = event.getTarget();
            if (!textEditor.getEditable()) {
                return;
            }
            final KeyEvent keyPressedEvent = (KeyEvent)event;
            String text = textEditor.getText();
            int count = (keyPressedEvent.getKeyChar() == '\n') ? 1 : 0;
            for (int i = 0; i < text.length(); ++i) {
                if (text.charAt(i) == '\n' && ++count > 3) {
                    text = text.substring(0, i) + " " + text.substring(i + 1, text.length());
                }
            }
            if (count > 3) {
                textEditor.setText(text);
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("carriageReturnLimitError"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        FocusManager.getInstance().setFocused(textEditor);
                    }
                });
            }
            PropertiesProvider.getInstance().setPropertyValue("electionSloganLength", WakfuTranslator.getInstance().getString("remainingLetters", 200 - textEditor.getText().length()));
            PropertiesProvider.getInstance().setPropertyValue("electionSloganDirty", true);
        }
    }
    
    public static void previousPage(final Event e) {
        UIMessage.send((short)16161);
    }
    
    public static void nextPage(final Event e) {
        UIMessage.send((short)16162);
    }
}
