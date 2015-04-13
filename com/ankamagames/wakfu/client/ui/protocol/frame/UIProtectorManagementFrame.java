package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.protector.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.xulor2.core.messagebox.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.protector.ecosystem.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;

public class UIProtectorManagementFrame implements MessageFrame
{
    private static UIProtectorManagementFrame m_instance;
    private static final Logger m_logger;
    private Protector m_protector;
    private DialogUnloadListener m_dialogUnloadListener;
    private MobileStartPathListener m_listener;
    private float m_fleaTaxValue;
    private float m_marketTaxValue;
    
    public static UIProtectorManagementFrame getInstance() {
        return UIProtectorManagementFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            ProtectorView.getInstance().updateTime();
            return false;
        }
        switch (message.getId()) {
            case 16004: {
                final ProtectorMerchantItemView protectorItemView = (ProtectorMerchantItemView)PropertiesProvider.getInstance().getObjectProperty("selectedProtectorMerchantItem");
                final ProtectorMerchantInventoryItem item = protectorItemView.getMerchantItem();
                final ProtectorMerchantInventory inventory = ((UIProtectorMerchantInventoryMessage)message).getProtectorMerchantInventory();
                if (inventory == null) {
                    return false;
                }
                final String messageKey = (inventory.getRequiredItemType() != MerchantItemType.PROTECTOR_CLIMATE_BUFFS) ? "question.confirmPurchase" : "question.confirmPurchaseReplace";
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(messageKey, protectorItemView.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final long itemUid = item.getUniqueId();
                            final ProtectorMerchantClient merchant = (ProtectorMerchantClient)UIProtectorManagementFrame.this.m_protector.getWallet().getMerchantClient(inventory.getWalletContext());
                            final ClientMerchantTransaction<Item> merchantTransaction = inventory.buy(merchant, itemUid, (short)1, null, -1L);
                            if (merchantTransaction.getError() == 0) {
                                final ProtectorBuyRequestMessage msg = new ProtectorBuyRequestMessage();
                                msg.setProtectorId(UIProtectorManagementFrame.this.m_protector.getId());
                                msg.setMerchantInventoryUid(inventory.getUid());
                                msg.setItemUid(itemUid);
                                WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(msg);
                                final ProtectorManagementRequestMessage netmsg = new ProtectorManagementRequestMessage();
                                netmsg.setProtectorId(UIProtectorManagementFrame.this.m_protector.getId());
                                WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(netmsg);
                                PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), "boughtChallenges", "challengeInventory", "boughtBuffs", "buffInventory", "climateInventory", "walletHandler");
                            }
                        }
                    }
                });
                return false;
            }
            case 16005: {
                final UIMessage uiMessage = (UIMessage)message;
                final MessageBoxControler messageBoxControler2 = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmTaxChanges"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIProtectorManagementFrame.this.applyTaxes();
                        }
                    }
                });
                return false;
            }
            case 16011: {
                final UIMessage msg = (UIMessage)message;
                final int amount = msg.getIntValue();
                final ProtectorWalletContext context = ProtectorWalletContext.fromId(msg.getByteValue());
                final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmAllocateBudget"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final ProtectorAllocateBudgetRequestMessage requestMessage = new ProtectorAllocateBudgetRequestMessage();
                            requestMessage.setBudget(UIProtectorManagementFrame.this.m_protector.getId(), context, amount);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(requestMessage);
                            UIProtectorManagementFrame.this.m_protector.getWallet().addAmount(context, amount);
                            UIProtectorManagementFrame.this.m_protector.getWallet().substractAmount(amount);
                            switch (context) {
                                case CHALLENGE: {
                                    ProtectorView.getInstance().getProtector().getChallengeMerchantInventoryView().onWalletUpdated();
                                    break;
                                }
                                case CLIMATE: {
                                    PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), "climateInventory");
                                }
                            }
                        }
                    }
                });
                return false;
            }
            case 16012: {
                final UIMessage msg = (UIMessage)message;
                final int protectorDest = (int)msg.getLongValue();
                final int amount2 = msg.getIntValue();
                final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmTransferKamas"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final ProtectorBudgetTransferRequestMessage requestMessage = new ProtectorBudgetTransferRequestMessage();
                            requestMessage.setAmount(amount2);
                            requestMessage.setSourceProtectorId(UIProtectorManagementFrame.this.m_protector.getId());
                            requestMessage.setDestProtectorId(protectorDest);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(requestMessage);
                            UIProtectorManagementFrame.this.m_protector.getWallet().substractAmount(amount2);
                            ProcessScheduler.getInstance().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(new NationProtectorsInformationRequestMessage());
                                }
                            }, 1000L, 1);
                        }
                    }
                });
                return false;
            }
            case 16006: {
                final UIMessage msg = (UIMessage)message;
                final int challengeId = msg.getIntValue();
                final boolean selected = msg.getBooleanValue();
                final TIntArrayList selectedChallenges = this.m_protector.getSelectedChallengeList();
                final int index = selectedChallenges.indexOf(challengeId);
                if (selected && index == -1) {
                    selectedChallenges.add(challengeId);
                }
                else {
                    if (selected || index == -1) {
                        return false;
                    }
                    selectedChallenges.remove(index);
                }
                final ProtectorChallengeSelectionRequestMessage netMsg = new ProtectorChallengeSelectionRequestMessage();
                netMsg.setChallengeId(challengeId);
                netMsg.setProtectorId(this.m_protector.getId());
                netMsg.setSelection(selected);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                return false;
            }
            case 16008: {
                final UIMessage msg = (UIMessage)message;
                final boolean isMonster = msg.getBooleanValue();
                final Protector protector = ProtectorView.getInstance().getProtector();
                final ProtectorEcosystemView ecosystem = protector.getEcosystemHandler().getView();
                final ProtectorEcosystemElement element = ecosystem.getElement(msg.getIntValue(), isMonster);
                if (protector.getWallet().getAmountOfCash(ProtectorWalletContext.ECOSYSTEM) >= element.getProtectPrice()) {
                    final MessageBoxControler controler2 = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmPurchaseGeneral"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    controler2.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                if (isMonster) {
                                    protector.getEcosystemHandler().requestProtectMonsterFamily(msg.getIntValue());
                                }
                                else {
                                    protector.getEcosystemHandler().requestProtectResourceFamily(msg.getIntValue());
                                }
                                NetEnabledWidgetManager.INSTANCE.setGroupEnabled("protectorEcosystemLock", false);
                            }
                        }
                    });
                }
                else {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.notEnoughKamas"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 2051L, 102, 1);
                }
                return false;
            }
            case 16009: {
                final UIMessage msg = (UIMessage)message;
                final boolean isMonster = msg.getBooleanValue();
                final Protector protector = ProtectorView.getInstance().getProtector();
                final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmUnprotect"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            if (isMonster) {
                                protector.getEcosystemHandler().requestUnprotectMonsterFamily(msg.getIntValue());
                            }
                            else {
                                protector.getEcosystemHandler().requestUnprotectResourceFamily(msg.getIntValue());
                            }
                            NetEnabledWidgetManager.INSTANCE.setGroupEnabled("protectorEcosystemLock", false);
                        }
                    }
                });
                return false;
            }
            case 16010: {
                final UIMessage msg = (UIMessage)message;
                final boolean isMonster = msg.getBooleanValue();
                final Protector protector = ProtectorView.getInstance().getProtector();
                final ProtectorEcosystemView ecosystem = protector.getEcosystemHandler().getView();
                final ProtectorEcosystemElement element = ecosystem.getElement(msg.getIntValue(), isMonster);
                if (protector.getWallet().getAmountOfCash(ProtectorWalletContext.ECOSYSTEM) >= element.getReintroducePrice()) {
                    final MessageBoxControler controler2 = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmPurchaseGeneral"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    controler2.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                if (isMonster) {
                                    protector.getEcosystemHandler().requestReintroduceMonsterFamily(msg.getIntValue());
                                }
                                else {
                                    protector.getEcosystemHandler().requestReintroduceResourceFamily(msg.getIntValue());
                                }
                                NetEnabledWidgetManager.INSTANCE.setGroupEnabled("protectorEcosystemLock", false);
                            }
                        }
                    });
                }
                else {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.notEnoughKamas"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 2051L, 102, 1);
                }
                return false;
            }
            case 16007: {
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmLawChanges"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void applyTaxes() {
        this.applyTaxes(TaxContext.MARKET_ADD_ITEM_CONTEXT);
        this.applyTaxes(TaxContext.FLEA_ADD_ITEM_CONTEXT);
    }
    
    private void applyTaxes(final TaxContext taxContext) {
        switch (taxContext) {
            case MARKET_ADD_ITEM_CONTEXT: {
                this.m_marketTaxValue = ProtectorView.getInstance().getCurrentTax(TaxContext.MARKET_ADD_ITEM_CONTEXT).getTax().getValue();
                break;
            }
            case FLEA_ADD_ITEM_CONTEXT: {
                this.m_fleaTaxValue = ProtectorView.getInstance().getCurrentTax(TaxContext.FLEA_ADD_ITEM_CONTEXT).getTax().getValue();
                break;
            }
        }
        final ProtectorChangeTaxRequestMessage protectorChangeTaxRequestMessage = new ProtectorChangeTaxRequestMessage();
        protectorChangeTaxRequestMessage.setProtectorId(this.m_protector.getId());
        protectorChangeTaxRequestMessage.addTaxChange(ProtectorView.getInstance().getCurrentTax(taxContext).getTax());
        WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(protectorChangeTaxRequestMessage);
    }
    
    @Override
    public long getId() {
        return 4L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_listener = new MobileStartPathListener() {
                @Override
                public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                    WakfuGameEntity.getInstance().removeFrame(UIProtectorManagementFrame.getInstance());
                }
            };
            character.getActor().addStartPathListener(this.m_listener);
            this.m_protector = ProtectorView.getInstance().getProtector();
            if (this.m_protector == null) {
                UIProtectorManagementFrame.m_logger.error((Object)"Impossible d'initialiser l'interface de management du protecteur, aucun protecteur n'est d\u00e9finie dans la vue !");
                return;
            }
            final ProtectorManagementRequestMessage message = new ProtectorManagementRequestMessage();
            message.setProtectorId(this.m_protector.getId());
            WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
            WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(new NationProtectorsInformationRequestMessage());
            PropertiesProvider.getInstance().setPropertyValue("selectedProtectorMerchantItem", null);
            this.m_fleaTaxValue = ProtectorView.getInstance().getCurrentTax(TaxContext.FLEA_ADD_ITEM_CONTEXT).getTax().getValue();
            this.m_marketTaxValue = ProtectorView.getInstance().getCurrentTax(TaxContext.MARKET_ADD_ITEM_CONTEXT).getTax().getValue();
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("protectorManagementDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIProtectorManagementFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("weather.manager", WeatherInfoManager.getInstance());
            NetEnabledWidgetManager.INSTANCE.createGroup("protectorEcosystemLock");
            final EventDispatcher ed = Xulor.getInstance().load("protectorManagementDialog", Dialogs.getDialogPath("protectorManagementDialog"), 32768L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", 0, ed.getElementMap());
            Xulor.getInstance().putActionClass("wakfu.protectorManagement", ProtectorManagementDialogActions.class);
            MessageScheduler.getInstance().addClock(this, 1000L, -1);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            ProtectorView.getInstance().getCurrentTax(TaxContext.FLEA_ADD_ITEM_CONTEXT).getTax().setValue(this.m_fleaTaxValue);
            ProtectorView.getInstance().getCurrentTax(TaxContext.MARKET_ADD_ITEM_CONTEXT).getTax().setValue(this.m_marketTaxValue);
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeStartListener(this.m_listener);
            this.m_listener = null;
            NetEnabledWidgetManager.INSTANCE.destroyGroup("protectorEcosystemLock");
            Xulor.getInstance().removeActionClass("wakfu.protectorManagement");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("selectedProtectorMerchantItem");
            PropertiesProvider.getInstance().removeProperty("weather.manager");
            Xulor.getInstance().unload("protectorManagementDialog");
            MessageScheduler.getInstance().removeAllClocks(this);
        }
    }
    
    static {
        UIProtectorManagementFrame.m_instance = new UIProtectorManagementFrame();
        m_logger = Logger.getLogger((Class)UIProtectorManagementFrame.class);
    }
}
