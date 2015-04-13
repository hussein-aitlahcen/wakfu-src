package com.ankamagames.wakfu.client.console.command.common;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class CloseWindowCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        process();
    }
    
    public static void process() {
        if (SWFWrapper.INSTANCE.isOpened()) {
            SWFWrapper.INSTANCE.unload();
            return;
        }
        final int closewindowType = DialogClosesManager.getInstance().closeWindow();
        switch (closewindowType) {
            case 1: {
                if (WakfuGameEntity.getInstance().hasFrame(UIMenuFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UIMenuFrame.getInstance());
                    return;
                }
                if (SWFWrapper.INSTANCE.getState() == 1) {
                    final String errorMsg = WakfuTranslator.getInstance().getString("krosmoz.boardGame.loadingCancelQuestion");
                    final MessageBoxData data = new MessageBoxData(102, 1, errorMsg, 24L);
                    final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                    controler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8 && SWFWrapper.INSTANCE.getState() == 1) {
                                SWFWrapper.INSTANCE.unload();
                            }
                        }
                    });
                    return;
                }
                if (WakfuProgressMonitorManager.getInstance().isRunning()) {
                    WakfuGameEntity.getInstance().logoff();
                    return;
                }
                if (WakfuGameEntity.getInstance().hasFrame(UIServerChoiceFrame.INSTANCE)) {
                    WakfuGameEntity.getInstance().removeFrame(UIServerChoiceFrame.INSTANCE);
                    NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
                    WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
                    return;
                }
                if (WakfuGameEntity.getInstance().hasFrame(UICharacterChoiceFrame.getInstance())) {
                    WakfuGameEntity.getInstance().logoff();
                    return;
                }
                if (WakfuGameEntity.getInstance().hasFrame(UICharacterCreationFrame.getInstance())) {
                    if (!UICharacterCreationFrame.getInstance().getCreationType().isCanCancelCreation()) {
                        WakfuGameEntity.getInstance().logoff();
                    }
                    else {
                        UIMessage.send((short)16513);
                    }
                    return;
                }
                if (WakfuGameEntity.getInstance().hasFrame(UIDisplayButtonFrame.INSTANCE) && UIDisplayButtonFrame.INSTANCE.isCallbackOnEscape()) {
                    final MessageBoxControler validation = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("scenario.confirmEscape"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    validation.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                UIDisplayButtonFrame.INSTANCE.callFunction();
                            }
                        }
                    });
                    return;
                }
                WakfuGameEntity.getInstance().pushFrame(UIMenuFrame.getInstance());
                break;
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
