package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.flea.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.dimensionalBag.*;
import java.nio.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.room.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.container.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.name.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.framework.kernel.*;
import gnu.trove.*;

public class NetDimensionalBagFrame implements MessageFrame
{
    private static final boolean DEBUG_FAKE_HISTORY = false;
    public static final Comparator<SaleDayView> COMPARATOR_HISTORY;
    protected static final Logger m_logger;
    private static final NetDimensionalBagFrame m_instance;
    
    public static NetDimensionalBagFrame getInstance() {
        return NetDimensionalBagFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 10018: {
                NetDimensionalBagFrame.m_logger.info((Object)"[PERSONAL_SPACE_UPDATE_MESSAGE] : Mise \u00e0 jour du sac dimensionnel du propri\u00e9taire");
                WakfuClientInstance.getInstance();
                final LocalPlayerCharacter player = WakfuClientInstance.getGameEntity().getLocalPlayer();
                final DimensionalBagView bag = player.getVisitingDimentionalBag();
                if (bag != null) {
                    bag.updateFromRaw(((PSUpdateMessage)message).getSerializedPersonalSpace());
                }
                return false;
            }
            case 10036: {
                final InventoryToRoomGemExchangeResult msg = (InventoryToRoomGemExchangeResult)message;
                WakfuClientInstance.getInstance();
                final LocalPlayerCharacter player2 = WakfuClientInstance.getGameEntity().getLocalPlayer();
                final ClientBagContainer bagContainer = player2.getBags();
                final AbstractBag bag2 = bagContainer.getFirstContainerWith(msg.getGemItemUid());
                if (bag2 != null) {
                    final Item gem = bag2.getWithUniqueId(msg.getGemItemUid());
                    final DimensionalBagView bagView = player2.getOwnedDimensionalBag();
                    final GemControlledRoom.ModResult modResult = bagView.putGem(msg.getRoomlayoutPosition(), gem, bag2.getUid(), msg.isPrimary(), true);
                    if (modResult != GemControlledRoom.ModResult.OK) {
                        NetDimensionalBagFrame.m_logger.error((Object)"DIMENSIONAL_BAG_INVENTORY_TO_ROOM_GEM_EXCHANGE failed!");
                    }
                    else {
                        bagView.updateRoomUI(msg.getRoomlayoutPosition());
                    }
                }
                else {
                    NetDimensionalBagFrame.m_logger.error((Object)("Gemme introuvable dans l'inventaire du client : " + msg.getGemItemUid()));
                }
                return false;
            }
            case 10038: {
                final RoomToInventoryGemExchangeResult msg2 = (RoomToInventoryGemExchangeResult)message;
                WakfuClientInstance.getInstance();
                final LocalPlayerCharacter player2 = WakfuClientInstance.getGameEntity().getLocalPlayer();
                final DimensionalBagView bagView2 = player2.getOwnedDimensionalBag();
                final GemControlledRoom.ModResult modResult2 = bagView2.removeGem(msg2.getRoomLayoutPosition(), msg2.getInventoryUid(), msg2.getPosition(), msg2.isPrimary(), true);
                if (modResult2 != GemControlledRoom.ModResult.OK) {
                    NetDimensionalBagFrame.m_logger.error((Object)"DIMENSIONAL_BAG_ROOM_TO_INVENTORY_GEM_EXCHANGE failed!");
                }
                else {
                    bagView2.updateRoomUI(msg2.getRoomLayoutPosition());
                }
                return false;
            }
            case 10040: {
                final RoomsGemsExchangeResult msg3 = (RoomsGemsExchangeResult)message;
                WakfuClientInstance.getInstance();
                final LocalPlayerCharacter player2 = WakfuClientInstance.getGameEntity().getLocalPlayer();
                final DimensionalBagView bagView2 = player2.getOwnedDimensionalBag();
                final GemControlledRoom.ModResult modResult2 = bagView2.exchangeGems(msg3.getSourceRoomLayoutPosition(), msg3.isSourcePrimary(), msg3.getDestRoomLayoutPosition(), msg3.isDestPrimary(), true);
                if (modResult2 != GemControlledRoom.ModResult.OK) {
                    NetDimensionalBagFrame.m_logger.error((Object)"DIMENSIONAL_BAG_ROOMS_GEMS_EXCHANGE_RESULT failed!");
                }
                else {
                    bagView2.updateRoomUI(msg3.getSourceRoomLayoutPosition());
                    bagView2.updateRoomUI(msg3.getDestRoomLayoutPosition());
                }
                return false;
            }
            case 10042: {
                final FetchTransactionLogResultMessage msg4 = (FetchTransactionLogResultMessage)message;
                final RawTransactionLog log = msg4.getTransactionLog();
                this.displayTransactionLog(log);
                return false;
            }
            case 10048: {
                PropertiesProvider.getInstance().setPropertyValue("showRoomBagDetails", false);
                WakfuGameEntity.getInstance().removeFrame(UIManageFleaFrame.getInstance());
                if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                    Xulor.getInstance().unload("splitStackDialog");
                }
                return false;
            }
            case 10044: {
                final DimensionalBagPermissionsUpdateMessage updateMessage = (DimensionalBagPermissionsUpdateMessage)message;
                final long bagOwnerId = updateMessage.getBagOwnerId();
                final byte[] serializedPermissions = updateMessage.getSerializedPermissions();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final DimensionalBagView visitingBag = localPlayer.getVisitingDimentionalBag();
                if (visitingBag != null && visitingBag.getOwnerId() == bagOwnerId) {
                    final RawDimensionalBagPermissions rawPermissions = new RawDimensionalBagPermissions();
                    rawPermissions.unserialize(ByteBuffer.wrap(serializedPermissions));
                    visitingBag.updatePermissions(rawPermissions);
                }
                return false;
            }
            case 10016: {
                final PSKickUserResultMessage msg5 = (PSKickUserResultMessage)message;
                if (msg5.getResultCode() == 0) {
                    return false;
                }
                return false;
            }
            case 10034: {
                final PSRoomMoveContainerResultMessage msg6 = (PSRoomMoveContainerResultMessage)message;
                return false;
            }
            case 10058: {
                final ContainerSetLockResultMessage msg7 = (ContainerSetLockResultMessage)message;
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void displayTransactionLog(final RawTransactionLog log) {
        final SortedList<SaleDayView> sales = new SortedList<SaleDayView>(NetDimensionalBagFrame.COMPARATOR_HISTORY);
        for (int i = 0; i < log.transactions.size(); ++i) {
            final RawTransactionLog.Transaction transaction = log.transactions.get(i);
            final long transactionDate = transaction.transactionDate;
            final String playerName = transaction.buyerName;
            final long money = transaction.totalPrice;
            final ArrayList<RawTransactionLog.Transaction.SoldItem> items = transaction.soldItems;
            final GameDate transactionDay = GameDate.fromLong(transactionDate);
            transactionDay.trimToDay();
            SaleDayView dayView = sales.getLast();
            if (dayView == null || !dayView.getDate().equals(transactionDay)) {
                sales.add(dayView = new SaleDayView(transactionDay));
            }
            final SaleView saleView = new SaleView(GameDate.fromLong(transactionDate), playerName, money);
            for (int j = 0; j < items.size(); ++j) {
                final RawTransactionLog.Transaction.SoldItem soldItem = items.get(j);
                saleView.addItem(soldItem.refId, soldItem.quantity);
            }
            dayView.addSale(saleView);
        }
        final DimensionalBagSaleHistoryView saleHistoryView = new DimensionalBagSaleHistoryView(sales);
        UIDimensionalBagSaleHistoryFrame.getInstance().setView(saleHistoryView);
        WakfuGameEntity.getInstance().pushFrame(UIDimensionalBagSaleHistoryFrame.getInstance());
    }
    
    private void fillFakeTransactions(final SortedList<SaleDayView> sales) {
        final int NB_TRANSACTION = 200;
        final int NB_ITEM_RANDOM_MAX = 10;
        final boolean SINGLE_FAKE_ITEM = false;
        TIntObjectIterator<ReferenceItem> itemIt = ReferenceItemManager.getInstance().iterator();
        for (int i = 0; i < 200; ++i) {
            final long transactionDate = MathHelper.random(0L, System.currentTimeMillis());
            final String playerName = WakfuNameGenerator.getInstance().getRandomName();
            final long money = MathHelper.random(0L, Long.MAX_VALUE);
            final GameDate transactionDay = GameDate.fromLong(transactionDate);
            transactionDay.trimToDay();
            SaleDayView dayView = sales.getLast();
            if (dayView == null || !dayView.getDate().equals(transactionDay)) {
                sales.add(dayView = new SaleDayView(transactionDay));
            }
            final SaleView saleView = new SaleView(GameDate.fromLong(transactionDate), playerName, money);
            for (int j = 0, size = MathHelper.random(10); j < size; ++j) {
                if (!itemIt.hasNext()) {
                    itemIt = ReferenceItemManager.getInstance().iterator();
                }
                itemIt.advance();
                saleView.addItem(itemIt.key(), (short)MathHelper.random(itemIt.value().getStackMaximumHeight()));
            }
            dayView.addSale(saleView);
        }
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
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                localPlayer.setVisitingDimentionalBag(null);
            }
        }
    }
    
    static {
        COMPARATOR_HISTORY = new Comparator<SaleDayView>() {
            @Override
            public int compare(final SaleDayView o1, final SaleDayView o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        m_logger = Logger.getLogger((Class)NetDimensionalBagFrame.class);
        m_instance = new NetDimensionalBagFrame();
    }
}
