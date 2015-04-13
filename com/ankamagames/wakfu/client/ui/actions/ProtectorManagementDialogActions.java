package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.protector.wallet.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.xulor2.component.list.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.protector.ecosystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.protector.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;

@XulorActionsTag
public class ProtectorManagementDialogActions
{
    public static final String PACKAGE = "wakfu.protectorManagement";
    private static final Logger m_logger;
    
    public static void selectMerchantItem(final ItemEvent itemEvent) {
        PropertiesProvider.getInstance().setPropertyValue("selectedProtectorMerchantItem", itemEvent.getItemValue());
    }
    
    public static void validateBudgetAllocation(final KeyEvent e, final Button b) {
        final TextEditor te = e.getTarget();
        final int allocation = PrimitiveConverter.getInteger(te.getText(), -1);
        final boolean haveEnoughCash = allocation <= ProtectorView.getInstance().getProtector().getWallet().getAmountOfCash();
        b.setEnabled(haveEnoughCash);
    }
    
    public static void allocateBudget(final Event e, final TextEditor ed, final WalletView wallet) {
        final int value = PrimitiveConverter.getInteger(ed.getText(), -1);
        if (value == -1) {
            ProtectorManagementDialogActions.m_logger.warn((Object)("Impossible d'allouer le budget : " + ed.getText() + " n'est pas une valeur valide."));
            return;
        }
        if (value > wallet.getHandler().getAmountOfCash()) {
            ProtectorManagementDialogActions.m_logger.warn((Object)("Impossible d'allouer le budget : on demande " + value + "alors qu'il ne reste que " + wallet.getHandler().getAmountOfCash()));
            return;
        }
        final UIMessage msg = new UIMessage();
        msg.setId(16011);
        msg.setIntValue(value);
        msg.setByteValue(wallet.getContext().idx);
        Worker.getInstance().pushMessage(msg);
        Xulor.getInstance().unload("protectorBudgetAllocateDialog");
    }
    
    public static void openAllocateBudget(final Event e, final WalletView view) {
        PropertiesProvider.getInstance().setPropertyValue("selectedProtectorBudget", view);
        Xulor.getInstance().load("protectorBudgetAllocateDialog", Dialogs.getDialogPath("protectorBudgetAllocateDialog"), 256L, (short)10000);
    }
    
    public static void validateBudgetTransfer(final KeyEvent e, final Button b) {
        final TextEditor te = e.getTarget();
        final int allocation = PrimitiveConverter.getInteger(te.getText(), -1);
        final boolean haveEnoughCash = allocation <= ProtectorView.getInstance().getProtector().getWallet().getAmountOfCash();
        b.setEnabled(haveEnoughCash);
    }
    
    public static void transferBudget(final Event e, final TextEditor ed, final ComboBox territoryCombo) {
        final int value = PrimitiveConverter.getInteger(ed.getText(), -1);
        if (value == -1) {
            ProtectorManagementDialogActions.m_logger.warn((Object)("Impossible de transf\u00e9rer le budget : " + ed.getText() + " n'est pas une valeur valide."));
            return;
        }
        final ProtectorWalletHandler handler = ProtectorView.getInstance().getProtector().getWallet();
        if (value > handler.getAmountOfCash()) {
            ProtectorManagementDialogActions.m_logger.warn((Object)("Impossible de transf\u00e9rer le budget : on demande " + value + "alors qu'il ne reste que " + handler.getAmountOfCash()));
            return;
        }
        final ProtectorInListView view = (ProtectorInListView)territoryCombo.getSelectedValue();
        final UIMessage msg = new UIMessage();
        msg.setId(16012);
        msg.setLongValue(view.getProtectorId());
        msg.setIntValue(value);
        Worker.getInstance().pushMessage(msg);
        ed.setText("0");
    }
    
    public static void openTransferBudget(final Event e) {
        final EventDispatcher window = Xulor.getInstance().load("protectorBudgetTransferDialog", Dialogs.getDialogPath("protectorBudgetTransferDialog"), 256L, (short)10000);
        final ComboBox comboBox = (ComboBox)window.getElementMap().getElement("territoryCombo");
        comboBox.getList().setListFilter(new ListFilter() {
            @Override
            public boolean accept(final Object value) {
                final ProtectorInListView view = (ProtectorInListView)value;
                return ProtectorView.getInstance().getProtector().getId() != view.getProtectorId();
            }
        });
    }
    
    public static void changeChallengeCategory(final SelectionChangedEvent e, final ProtectorChallengeMerchantInventoryView view) {
        if (e.isSelected()) {
            final RadioButton button = e.getTarget();
            final byte id = PrimitiveConverter.getByte(button.getValue());
            final ChallengeCategory category = ChallengeCategory.fromId(id);
            view.setSelected(category);
        }
    }
    
    public static void changeZoologyCategory(final SelectionChangedEvent e, final ProtectorEcosystemView view) {
        if (e.isSelected()) {
            final RadioButton button = e.getTarget();
            final boolean isMonster = PrimitiveConverter.getBoolean(button.getValue());
            view.setSelected(isMonster);
        }
    }
    
    public static void reintroduce(final Event e, final ProtectorEcosystemView view, final ProtectorEcosystemElement element) {
        final UIMessage msg = new UIMessage();
        msg.setId(16010);
        msg.setIntValue(element.getFamilyId());
        msg.setBooleanValue(element.isMonster());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void protect(final Event e, final ProtectorEcosystemView view, final ProtectorEcosystemElement element) {
        final UIMessage msg = new UIMessage();
        msg.setId(16008);
        msg.setIntValue(element.getFamilyId());
        msg.setBooleanValue(element.isMonster());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void unprotect(final Event e, final ProtectorEcosystemView view, final ProtectorEcosystemElement element) {
        final UIMessage msg = new UIMessage();
        msg.setId(16009);
        msg.setIntValue(element.getFamilyId());
        msg.setBooleanValue(element.isMonster());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void validMerchantItemPurchase(final MouseEvent e, final ProtectorMerchantInventory protectorMerchantInventory, final ProtectorMerchantItemView item) {
        if (e.getButton() != 1) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("selectedProtectorMerchantItem", item);
        final UIProtectorMerchantInventoryMessage uiMessage = new UIProtectorMerchantInventoryMessage();
        uiMessage.setId(16004);
        uiMessage.setProtectorMerchantInventory(protectorMerchantInventory);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void tabSelected(final Event e) {
        if (e.getTarget() instanceof RadioButton) {
            PropertiesProvider.getInstance().setPropertyValue("selectedProtectorMerchantItem", null);
        }
    }
    
    public static void setTaxValue(final SliderMovedEvent e) {
        setTaxValue(e.getValue(), ProtectorView.getInstance().getCurrentTax(TaxContext.MARKET_ADD_ITEM_CONTEXT));
        final TaxView taxView = ProtectorView.getInstance().getCurrentTax(TaxContext.FLEA_ADD_ITEM_CONTEXT);
        setTaxValue(e.getValue(), taxView);
        PropertiesProvider.getInstance().firePropertyValueChanged(taxView, "taxValue", "taxPercentage", "taxPercentageLongDesc");
    }
    
    private static void setTaxValue(final float value, final TaxView taxView) {
        final Tax tax = taxView.getTax();
        tax.setValue(value * 5.0f / 100.0f);
    }
    
    public static void applyTaxes(final Event e) {
        UIMessage.send((short)16005);
    }
    
    public static void onBoughtChallengeSelectionChange(final SelectionChangedEvent e, final ChallengeDataModelView view) {
        final UIMessage msg = new UIMessage();
        msg.setId(16006);
        msg.setIntValue(view.getModel().getId());
        msg.setBooleanValue(e.isSelected());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void applyLaws(final Event e) {
        UIMessage.send((short)16007);
        enableApplyLawsButton(e, false);
    }
    
    public static void openChallengeDescription(final MouseEvent e, final FieldProvider fp) {
        if (e.getButton() == 1) {
            return;
        }
        AbstractChallengeView challenge = null;
        if (fp instanceof AbstractChallengeView) {
            challenge = (AbstractChallengeView)fp;
        }
        else if (fp instanceof ProtectorChallengeItemView) {
            final ProtectorChallengeItemView challengeItemView = (ProtectorChallengeItemView)fp;
            challenge = ChallengeViewManager.INSTANCE.getChallengeView(challengeItemView.getChallengeId());
        }
        if (challenge != null) {
            UIAchievementsFrame.getInstance().loadQuestDescription(challenge);
        }
    }
    
    public static void openClimateBonusDescription(final MouseEvent e, final ProtectorClimateItemView climate) {
        if (e.getButton() == 1) {
            return;
        }
        if (climate != null) {
            final EventDispatcher popup = Xulor.getInstance().load("climateBonusDetailDialog" + climate.getClimateBuffId(), Dialogs.getDialogPath("climateBonusDetailDialog"), 17L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("climateDetail", climate, popup.getElementMap());
        }
    }
    
    private static void enableApplyLawsButton(final Event e, final boolean enable) {
        final Button button = (Button)e.getCurrentTarget().getElementMap().getElement("applyLawsButton");
        button.setEnabled(enable);
    }
    
    public static void displayPage(final SelectionChangedEvent e) {
        if (e.isSelected()) {
            final RadioButton radioButton = e.getTarget();
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", radioButton.getValue(), radioButton.getElementMap());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorManagementDialogActions.class);
    }
}
