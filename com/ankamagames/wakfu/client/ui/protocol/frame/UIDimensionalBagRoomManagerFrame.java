package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;

public class UIDimensionalBagRoomManagerFrame implements MessageFrame
{
    private static final UIDimensionalBagRoomManagerFrame m_instance;
    private static final DialogUnloadListener DIALOG_UNLOAD_LISTENER;
    
    public static UIDimensionalBagRoomManagerFrame getInstance() {
        return UIDimensionalBagRoomManagerFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17007: {
                final UIItemMessage msg = (UIItemMessage)message;
                final DimensionalBagView bagView = WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag();
                final byte position = msg.getByteValue();
                final Item gemToAdd = msg.getItem();
                final boolean gemExchange = msg.getBooleanValue();
                final byte oldPos = (byte)msg.getIntValue();
                final long bagUID = gemExchange ? 0L : msg.getSourceUniqueId();
                GemControlledRoom.ModResult result;
                if (gemExchange) {
                    if (oldPos == position) {
                        return false;
                    }
                    final boolean sourceIsPrimary = bagView.getGem(oldPos, false) == null;
                    final boolean destIsPrimary = bagView.getGem(position, true) == null;
                    result = bagView.checkGemExchange(oldPos, sourceIsPrimary, position, destIsPrimary);
                    if (result == GemControlledRoom.ModResult.OK) {
                        if (hasResourcesInRoom(bagView, oldPos)) {
                            final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("dimensionalBag.warn.resources"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                            messageBoxControler.addEventListener(new MessageBoxEventListener() {
                                @Override
                                public void messageBoxClosed(final int type, final String userEntry) {
                                    if (type == 8) {
                                        bagView.exchangeGems(oldPos, sourceIsPrimary, position, destIsPrimary, false);
                                    }
                                }
                            });
                        }
                        else {
                            bagView.exchangeGems(oldPos, sourceIsPrimary, position, destIsPrimary, false);
                        }
                    }
                }
                else {
                    result = bagView.putGem(position, gemToAdd, bagUID, true, false);
                    if (result == GemControlledRoom.ModResult.GEM_SLOT_BUSY) {
                        result = bagView.putGem(position, gemToAdd, bagUID, false, false);
                    }
                }
                if (result != GemControlledRoom.ModResult.OK) {
                    Xulor.getInstance().msgBox(DimensionalBagView.resultToTranslationKey(result), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 2L, 102, 3);
                }
                bagView.updateRoomUI(position);
                return false;
            }
            case 17008: {
                final UIItemMessage msg = (UIItemMessage)message;
                final DimensionalBagView bagView = WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag();
                final byte position = msg.getByteValue();
                final long bagUID2 = msg.getDestinationUniqueId();
                final byte bagPosition = msg.getDestinationPosition();
                if (hasResourcesInRoom(bagView, position)) {
                    final MessageBoxControler messageBoxControler2 = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("dimensionalBag.warn.resources"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    messageBoxControler2.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                doRemoveGem(bagView, position, bagUID2, bagPosition);
                            }
                        }
                    });
                    break;
                }
                return doRemoveGem(bagView, position, bagUID2, bagPosition);
            }
        }
        return true;
    }
    
    private static boolean doRemoveGem(final DimensionalBagView bagView, final byte position, final long bagUID, final byte bagPosition) {
        GemControlledRoom.ModResult result = bagView.removeGem(position, bagUID, bagPosition, false, false);
        if (result == GemControlledRoom.ModResult.NO_GEM) {
            result = bagView.removeGem(position, bagUID, bagPosition, true, false);
        }
        if (result != GemControlledRoom.ModResult.OK) {
            Xulor.getInstance().msgBox(DimensionalBagView.resultToTranslationKey(result), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 2L, 102, 3);
            return false;
        }
        bagView.updateRoomUI(position);
        return false;
    }
    
    private static boolean hasResourcesInRoom(final DimensionalBagView bagView, final byte gemPosition) {
        final GemControlledRoom controlledRoom = bagView.getRoom(gemPosition);
        final short x = controlledRoom.getOriginX();
        final short y = controlledRoom.getOriginY();
        final byte width = controlledRoom.getWidth();
        final byte height = controlledRoom.getHeight();
        for (int i = x; i < x + width; ++i) {
            for (int j = y; j < y + height; ++j) {
                if (ResourceManager.getInstance().getResource(i, j) != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (isAboutToBeAdded) {
            return;
        }
        Xulor.getInstance().addDialogUnloadListener(UIDimensionalBagRoomManagerFrame.DIALOG_UNLOAD_LISTENER);
        Xulor.getInstance().load("dimensionalBagRoomManagerDialog", Dialogs.getDialogPath("dimensionalBagRoomManagerDialog"), 32769L, (short)10000);
        if (WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag() != null) {
            PropertiesProvider.getInstance().setPropertyValue("editableDimensionalBag", WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag());
        }
        Xulor.getInstance().putActionClass("wakfu.roomManager", DimensionalBagRoomManagerDialogAction.class);
        WakfuSoundManager.getInstance().windowFadeIn();
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (isAboutToBeRemoved) {
            return;
        }
        Xulor.getInstance().removeDialogUnloadListener(UIDimensionalBagRoomManagerFrame.DIALOG_UNLOAD_LISTENER);
        PropertiesProvider.getInstance().removeProperty("editableDimensionalBag");
        Xulor.getInstance().unload("dimensionalBagRoomManagerDialog");
        Xulor.getInstance().removeActionClass("wakfu.roomManager");
        WakfuSoundManager.getInstance().windowFadeOut();
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String toString() {
        return "UIDimensionalBagRoomManagerFrame{m_dialogUnloadListener=" + UIDimensionalBagRoomManagerFrame.DIALOG_UNLOAD_LISTENER + '}';
    }
    
    static {
        m_instance = new UIDimensionalBagRoomManagerFrame();
        DIALOG_UNLOAD_LISTENER = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if (id.equals("dimensionalBagRoomManagerDialog")) {
                    WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
                }
            }
        };
    }
}
