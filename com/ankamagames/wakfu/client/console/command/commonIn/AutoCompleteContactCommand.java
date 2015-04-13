package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.console.command.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.xulor2.component.text.builder.*;

public class AutoCompleteContactCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final int id = ChatWindowManager.getInstance().getCurrentWindow().getWindowId();
        final String currentChatDialogId = UIChatFrameHelper.getDialogIdFromWindowId(id);
        TextEditor chatTextEditor = null;
        final Widget focused = FocusManager.getInstance().getFocused();
        if (focused instanceof TextEditor) {
            chatTextEditor = (TextEditor)focused;
        }
        else {
            chatTextEditor = (TextEditor)Xulor.getInstance().getEnvironment().getElementMap(currentChatDialogId).getElement("textEditorRenderableContainer.chatInput");
        }
        final int promptIndex = chatTextEditor.getTextBuilder().getDocument().getSelectionEndSubPartIndex();
        final String textEditorText = chatTextEditor.getText();
        boolean tryWithoutCommasMethode = true;
        if (textEditorText != null && textEditorText.length() > 0) {
            final ChatViewManager currentWindow = ChatWindowManager.getInstance().getCurrentWindow();
            if (currentWindow != null && ChatManager.getInstance().getConsole().getCommandDescriptorSet().isAutoCompletionChild(textEditorText, (byte)127)) {
                tryWithoutCommasMethode = false;
                final int index = textEditorText.indexOf(32);
                if (index != -1 && index < textEditorText.length()) {
                    String stringToChange = textEditorText.substring(index + 1);
                    int toChangeEndIndex = stringToChange.lastIndexOf(34) + 1;
                    if (toChangeEndIndex == -1 || stringToChange.indexOf(34) == -1 || stringToChange.indexOf(34) == toChangeEndIndex) {
                        toChangeEndIndex = stringToChange.indexOf(32);
                    }
                    if (toChangeEndIndex == -1) {
                        toChangeEndIndex = stringToChange.length();
                    }
                    if (promptIndex - (textEditorText.length() - stringToChange.length()) > toChangeEndIndex) {
                        tryWithoutCommasMethode = true;
                    }
                    else {
                        stringToChange = stringToChange.substring(0, toChangeEndIndex);
                        String stringToAutoComplete = stringToChange.replaceAll("\"", "");
                        stringToAutoComplete = stringToAutoComplete.trim();
                        if (stringToAutoComplete != null && stringToAutoComplete.length() > 0) {
                            final AutoCompletionHelper autoCompletionHelper = AutoCompletionHelper.getInstance();
                            if (autoCompletionHelper.getStringToAutoComplete() == null || !autoCompletionHelper.isMatchingWithStringToAutoComplete(stringToAutoComplete)) {
                                autoCompletionHelper.initialise(stringToAutoComplete);
                            }
                            final String possibility = autoCompletionHelper.getNextPossibility();
                            chatTextEditor.setText(textEditorText.replaceFirst(stringToChange, possibility.equalsIgnoreCase(autoCompletionHelper.getStringToAutoComplete()) ? possibility : ("\"" + possibility + "\"")));
                            FocusManager.getInstance().setFocused(chatTextEditor);
                        }
                    }
                }
            }
            if (tryWithoutCommasMethode && promptIndex != -1) {
                boolean commaOnlyAtStart = false;
                int endIndex = textEditorText.indexOf(32, promptIndex);
                if (endIndex == -1) {
                    endIndex = textEditorText.length();
                }
                int startIndex = textEditorText.substring(0, promptIndex).lastIndexOf(32) + 1;
                String stringToAutoComplete = textEditorText.substring(startIndex, endIndex);
                if (stringToAutoComplete.length() == 0) {
                    return;
                }
                if (stringToAutoComplete.charAt(0) == '\"') {
                    ++startIndex;
                    stringToAutoComplete = stringToAutoComplete.substring(1);
                    commaOnlyAtStart = true;
                    if (stringToAutoComplete.length() == 0) {
                        return;
                    }
                }
                if (stringToAutoComplete.charAt(stringToAutoComplete.length() - 1) == '\"') {
                    --endIndex;
                    stringToAutoComplete = stringToAutoComplete.substring(0, stringToAutoComplete.length() - 1);
                    commaOnlyAtStart = false;
                    if (stringToAutoComplete.length() == 0) {
                        return;
                    }
                }
                stringToAutoComplete = stringToAutoComplete.trim();
                if (stringToAutoComplete != null && stringToAutoComplete.length() > 0) {
                    final AutoCompletionHelper autoCompletionHelper = AutoCompletionHelper.getInstance();
                    if (autoCompletionHelper.getStringToAutoComplete() == null || !autoCompletionHelper.isMatchingWithStringToAutoComplete(stringToAutoComplete)) {
                        autoCompletionHelper.initialise(stringToAutoComplete);
                    }
                    final String possibility = autoCompletionHelper.getNextPossibility();
                    final ObjectPair<AbstractContentBlock, TextBuilder.BlockIntersectionType> block = chatTextEditor.getTextBuilder().getBlockFromCoordinates(0, 0);
                    if (block != null) {
                        final AbstractContentBlock contentBlock = block.getFirst();
                        if (contentBlock != null) {
                            chatTextEditor.getTextBuilder().setSelectionStart(contentBlock.getDocumentPart(), startIndex);
                            chatTextEditor.getTextBuilder().setSelectionEnd(contentBlock.getDocumentPart(), endIndex);
                            chatTextEditor.getTextBuilder().replaceSelectionBy(commaOnlyAtStart ? (possibility + "\"") : possibility);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
