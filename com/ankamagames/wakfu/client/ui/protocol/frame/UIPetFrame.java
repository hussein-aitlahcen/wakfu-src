package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.pet.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import gnu.trove.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIPetFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIPetFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private ArrayList<String> m_openedPetDetail;
    private String m_lastPetDetailDialogId;
    private TLongObjectHashMap<PetUiSupervisor> m_petUiSupervisors;
    private Article m_petFoodArticle;
    private Article m_petHealArticle;
    private Article m_petLevelUpArticle;
    final Runnable m_dateUpdateProcess;
    private Item m_colorPetItem;
    
    public UIPetFrame() {
        super();
        this.m_openedPetDetail = new ArrayList<String>();
        this.m_petUiSupervisors = new TLongObjectHashMap<PetUiSupervisor>();
        this.m_dateUpdateProcess = new Runnable() {
            @Override
            public void run() {
                for (final String s : UIPetFrame.this.m_openedPetDetail) {
                    final PetDetailDialogView petDetailDialogView = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet", s);
                    petDetailDialogView.lastMealDateChanged(null);
                    petDetailDialogView.healthChanged(-1);
                }
                final PetDetailDialogView petDetailDialogView2 = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet");
                if (petDetailDialogView2 != null) {
                    petDetailDialogView2.lastMealDateChanged(null);
                }
            }
        };
    }
    
    private void checkArticles() {
        if (this.m_petFoodArticle == null) {
            ArticlesKeyLoader.INSTANCE.getArticlesKey("petFood", new ArticlesKeyListener() {
                @Override
                public void onArticlesKey(final List<Article> articles) {
                    if (!articles.isEmpty()) {
                        UIPetFrame.this.m_petFoodArticle = articles.get(0);
                        PropertiesProvider.getInstance().setPropertyValue("petFoodArticle", UIPetFrame.this.m_petFoodArticle);
                    }
                }
                
                @Override
                public void onError() {
                }
            });
        }
        if (this.m_petHealArticle == null) {
            ArticlesKeyLoader.INSTANCE.getArticlesKey("petHeal", new ArticlesKeyListener() {
                @Override
                public void onArticlesKey(final List<Article> articles) {
                    if (!articles.isEmpty()) {
                        UIPetFrame.this.m_petHealArticle = articles.get(0);
                        PropertiesProvider.getInstance().setPropertyValue("petHealArticle", UIPetFrame.this.m_petHealArticle);
                    }
                }
                
                @Override
                public void onError() {
                }
            });
        }
        if (this.m_petLevelUpArticle == null) {
            ArticlesKeyLoader.INSTANCE.getArticlesKey("petLevelUp", new ArticlesKeyListener() {
                @Override
                public void onArticlesKey(final List<Article> articles) {
                    if (!articles.isEmpty()) {
                        UIPetFrame.this.m_petLevelUpArticle = articles.get(0);
                        PropertiesProvider.getInstance().setPropertyValue("petLevelUpArticle", UIPetFrame.this.m_petLevelUpArticle);
                    }
                }
                
                @Override
                public void onError() {
                }
            });
        }
    }
    
    private int getHungryCycles(final Pet pet) {
        final GameInterval hungryInterval = pet.getLastHungryDate().timeTo(WakfuGameCalendar.getInstance().getDate());
        return hungryInterval.getDivisionResult(pet.getDefinition().getMealMaxInterval());
    }
    
    public static UIPetFrame getInstance() {
        return UIPetFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19152: {
                final UIPetDetailMessage<PetDetailDialogView> msg = (UIPetDetailMessage<PetDetailDialogView>)message;
                final PetDetailDialogView pet = msg.getItem();
                if (pet == null) {
                    UIPetFrame.m_logger.error((Object)"Impossible de r\u00e9cup\u00e9rer le familier \u00e0 d\u00e9crire");
                    return false;
                }
                final Item item = pet.getPetItem();
                final String dialogPath = "petDialog";
                final String dialogId = this.getDialogPath(item);
                final String windowId = msg.getParentWindowId();
                for (final String detailId : this.m_openedPetDetail) {
                    Xulor.getInstance().unload(detailId);
                }
                if (!Xulor.getInstance().isLoaded(dialogId)) {
                    if (windowId == null) {
                        final Window itemWindow = (Window)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("petDialog"), 129L, (short)10000);
                        itemWindow.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                            @Override
                            public void windowPostProcessed() {
                                itemWindow.setX(Math.min(msg.getX(), MasterRootContainer.getInstance().getWidth() - itemWindow.getWidth()));
                                itemWindow.setY(Math.min(msg.getY(), MasterRootContainer.getInstance().getHeight() - itemWindow.getHeight()));
                                itemWindow.removeWindowPostProcessedListener(this);
                            }
                        });
                    }
                    else {
                        Xulor.getInstance().loadAsMultiple(dialogId, Dialogs.getDialogPath("petDialog"), (this.m_lastPetDetailDialogId == null) ? windowId : this.m_lastPetDetailDialogId, windowId, "petDialog", 129L, (short)10000);
                    }
                    this.m_lastPetDetailDialogId = dialogId;
                    PropertiesProvider.getInstance().setLocalPropertyValue("pet", pet, dialogId);
                    this.m_openedPetDetail.add(dialogId);
                    UIWebShopFrame.getInstance().requestLockForUI(dialogId);
                    getInstance().scheduleDateUpdateProcess();
                }
                else {
                    Xulor.getInstance().unload(dialogId);
                }
                return false;
            }
            case 19154: {
                final UIFeedPetMessage uiFeedPetMessage = (UIFeedPetMessage)message;
                final Item item2 = uiFeedPetMessage.getItem();
                if (item2 == null) {
                    return false;
                }
                final PetDetailDialogView pet2 = uiFeedPetMessage.getPet();
                if (pet2 == null) {
                    return false;
                }
                if (this.petCantLossHp(item2, pet2)) {
                    ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(WorldPropertyType.PET_CANT_LOST_LIFE.getId());
                    return false;
                }
                if (PetHelper.isFullLife(pet2.getPet()) && PetHelper.isHealMeal(pet2.getPet(), item2.getReferenceId())) {
                    final ChatMessage chatErrorMessage = new ChatMessage(WakfuTranslator.getInstance().getString("error.feedFullHealPet"));
                    chatErrorMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatErrorMessage);
                    return false;
                }
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (!localPlayer.getBags().contains(item2.getUniqueId())) {
                    final String errorMsg = WakfuTranslator.getInstance().getString("pet.feedUnauthorizedNotFromInventory");
                    final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                    chatErrorMsg.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                    return false;
                }
                final SimpleCriterion criterion = item2.getReferenceItem().getCriterion(ActionsOnItem.DELETE);
                if (item2.getReferenceItem().getItemAction() instanceof CompanionActivationItemAction || item2.hasPet() || (criterion != null && !criterion.isValid(localPlayer, item2, item2, localPlayer.getAchievementsContext()))) {
                    final String errorMsg2 = WakfuTranslator.getInstance().getString("pet.feedUnauthorized");
                    final ChatMessage chatErrorMsg2 = new ChatMessage(errorMsg2);
                    chatErrorMsg2.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg2);
                    return false;
                }
                final AbstractItemAction itemAction = item2.getReferenceItem().getItemAction();
                if (itemAction != null && itemAction.getType() == ItemActionConstants.PET_XP && PetHelper.isPetMaxLevel(pet2.getPet())) {
                    final String errorMsg3 = WakfuTranslator.getInstance().getString("pet.xpItem.alreadyMaxLevel");
                    final ChatMessage chatErrorMsg3 = new ChatMessage(errorMsg3);
                    chatErrorMsg3.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg3);
                    return false;
                }
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.feedPet", item2.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final PetFeedRequestMessage netMessage = new PetFeedRequestMessage();
                            netMessage.setItemId(item2.getUniqueId());
                            netMessage.setPetId(pet2.getPetItem().getUniqueId());
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                            UIPetFrame.this.checkFeedPetFeedBack(pet2, item2);
                        }
                    }
                });
                return false;
            }
            case 19153: {
                final UIPetMessage uiPetMessage = (UIPetMessage)message;
                final String name = uiPetMessage.getStringValue();
                final PetDetailDialogView pet2 = uiPetMessage.getPet();
                if (WordsModerator.getInstance().validateName(name) && name.length() > 0 && !name.equals(pet2.getName())) {
                    final PetChangeNameRequestMessage netMessage = new PetChangeNameRequestMessage();
                    netMessage.setItemId(pet2.getPetItem().getUniqueId());
                    netMessage.setCreatureName(name);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                }
                else {
                    final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.operationNotPermited");
                    final ChatMessage error = new ChatMessage(errorMessage);
                    error.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(error);
                }
                return false;
            }
            case 19155: {
                final UIItemMessage uiItemMessage = (UIItemMessage)message;
                final Item petItem = WakfuGameEntity.getInstance().getLocalPlayer().getFromEquipmentOrInventory(uiItemMessage.getLongValue());
                if (petItem == null) {
                    return false;
                }
                this.m_colorPetItem = uiItemMessage.getItem();
                final PetDetailDialogView petDetailDialogView = new PetDetailDialogView(petItem);
                petDetailDialogView.colorItemChanged(this.m_colorPetItem.getReferenceId());
                Xulor.getInstance().load("petColorPreviewDialog", Dialogs.getDialogPath("petColorPreviewDialog"), 256L, (short)30000);
                PropertiesProvider.getInstance().setLocalPropertyValue("pet", petDetailDialogView, "petColorPreviewDialog");
                PropertiesProvider.getInstance().setPropertyValue("petColorPreviewText", WakfuTranslator.getInstance().getString("pet.colorPreviewQuestion", this.m_colorPetItem.getName()));
                return false;
            }
            case 19156: {
                this.closePetPreviewDialog();
                return false;
            }
            case 19157: {
                if (this.m_colorPetItem != null) {
                    final PetChangeColorRequestMessage petChangeColorRequestMessage = new PetChangeColorRequestMessage();
                    petChangeColorRequestMessage.setItemId(this.m_colorPetItem.getUniqueId());
                    petChangeColorRequestMessage.setPetId(((UIMessage)message).getLongValue());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(petChangeColorRequestMessage);
                }
                this.closePetPreviewDialog();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean petCantLossHp(final Item item, final PetDetailDialogView pet) {
        return WakfuGameEntity.getInstance().getLocalPlayer().isActiveProperty(WorldPropertyType.PET_CANT_LOST_LIFE) && (!PetHelper.isValidMeal(pet.getPetItem().getPet(), item.getReferenceId()) || PetHelper.isPetFedTooEarly(pet.getPetItem().getPet(), WakfuGameCalendar.getInstance().getDate())) && !PetHelper.isHealMeal(pet.getPetItem().getPet(), item.getReferenceId());
    }
    
    private void checkFeedPetFeedBack(final PetDetailDialogView petView, final Item item) {
        final Pet pet = petView.getPetItem().getPet();
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        final Actor petMobile = petView.getPetMobile();
        final AbstractItemAction itemAction = item.getReferenceItem().getItemAction();
        String message;
        if (pet.getDefinition().containsSleepItem(item.getReferenceId())) {
            petMobile.setAnimation("AnimEmote-Effrayee");
            petMobile.forceReloadAnimation();
            message = WakfuTranslator.getInstance().getString("pet.chat.sleeping", TimeUtils.getShortDescription(pet.getDefinition().getSleepItemInterval(item.getReferenceId())));
        }
        else if (PetHelper.isHealMeal(pet, item.getReferenceId())) {
            petMobile.setAnimation("AnimEmote-Rire");
            petMobile.forceReloadAnimation();
            int healthBonus;
            if (itemAction != null && itemAction.getType() == ItemActionConstants.PET_HP) {
                final PetHpItemAction petHpItemAction = (PetHpItemAction)itemAction;
                healthBonus = ((petHpItemAction.getNumHp() < 0) ? pet.getDefinition().getHealth() : petHpItemAction.getNumHp());
            }
            else {
                healthBonus = pet.getDefinition().getHealItemBonus(item.getReferenceId());
            }
            message = WakfuTranslator.getInstance().getString("pet.chat.healing", healthBonus);
        }
        else if (!PetHelper.isValidMeal(pet, item.getReferenceId())) {
            petMobile.setAnimation("AnimEmote-Effrayee");
            petMobile.forceReloadAnimation();
            message = WakfuTranslator.getInstance().getString("pet.chat.feedWithBadItem", pet.getDefinition().getHealthPenalty(HealthPenaltyType.BAD_FEED));
        }
        else if (PetHelper.isPetFedTooEarly(pet, now)) {
            if (itemAction != null && itemAction.getType() == ItemActionConstants.PET_XP) {
                return;
            }
            petMobile.setAnimation("AnimEmote-Effrayee");
            petMobile.forceReloadAnimation();
            message = WakfuTranslator.getInstance().getString("pet.chat.feedNotHungry", pet.getDefinition().getHealthPenalty(HealthPenaltyType.MIN_MEAL_INTERVAL));
        }
        else if (itemAction != null && itemAction.getType() == ItemActionConstants.PET_XP) {
            petMobile.setAnimation("AnimEmote-Rire");
            petMobile.forceReloadAnimation();
            final PetXpItemAction petXpItemAction = (PetXpItemAction)itemAction;
            final int xp = petXpItemAction.getNumLevels() * pet.getDefinition().getXpPerLevel();
            message = WakfuTranslator.getInstance().getString("pet.chat.feedGood", xp);
        }
        else {
            petMobile.setAnimation("AnimEmote-Rire");
            petMobile.forceReloadAnimation();
            message = WakfuTranslator.getInstance().getString("pet.chat.feedGood", pet.getDefinition().getXpPerMeal());
        }
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    private void closePetPreviewDialog() {
        final PetDetailDialogView pddv = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet", "petColorPreviewDialog");
        pddv.clean();
        PropertiesProvider.getInstance().setLocalPropertyValue("pet", null, "petColorPreviewDialog");
        Xulor.getInstance().unload("petColorPreviewDialog");
        this.m_colorPetItem = null;
    }
    
    private String getDialogPath(final Item item) {
        return "petDialog_" + item.getUniqueId();
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
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (!id.startsWith("petDialog")) {
                        return;
                    }
                    if (id.equals(UIPetFrame.this.m_lastPetDetailDialogId)) {
                        UIPetFrame.this.m_lastPetDetailDialogId = null;
                    }
                    UIPetFrame.this.m_openedPetDetail.remove(id);
                    UIPetFrame.this.tryToUnScheduleDateUpdateProcess();
                    final PetDetailDialogView pet = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet", id);
                    final PetDetailDialogView petEquipt = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet");
                    if (petEquipt == null || pet.getPetItem().getUniqueId() != petEquipt.getPetItem().getUniqueId()) {
                        pet.clean();
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.pet", PetDialogActions.class);
            ProcessScheduler.getInstance().schedule(this.m_dateUpdateProcess, 1000L, -1);
            PropertiesProvider.getInstance().setPropertyValue("petLevelUpArticle", this.m_petLevelUpArticle);
            PropertiesProvider.getInstance().setPropertyValue("petFoodArticle", this.m_petFoodArticle);
            PropertiesProvider.getInstance().setPropertyValue("petHealArticle", this.m_petHealArticle);
            this.checkArticles();
            this.initialiseChatForPets(false);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.cleanUiPetSupervisors();
            ProcessScheduler.getInstance().remove(this.m_dateUpdateProcess);
            this.m_openedPetDetail.clear();
            this.m_lastPetDetailDialogId = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.pet");
        }
    }
    
    public void addChatPetListener(final Item petItem) {
        if (this.m_petUiSupervisors.contains(petItem.getUniqueId())) {
            return;
        }
        final PetUiSupervisor petUiSupervisor = new PetUiSupervisor(petItem);
        this.m_petUiSupervisors.put(petItem.getUniqueId(), petUiSupervisor);
    }
    
    public void removeChatPetListener(final Item petItem) {
        if (!this.m_petUiSupervisors.contains(petItem.getUniqueId())) {
            return;
        }
        petItem.getPet().removeListener(this.m_petUiSupervisors.get(petItem.getUniqueId()));
        this.m_petUiSupervisors.remove(petItem.getUniqueId());
    }
    
    public void scheduleDateUpdateProcess() {
        ProcessScheduler.getInstance().remove(this.m_dateUpdateProcess);
        ProcessScheduler.getInstance().schedule(this.m_dateUpdateProcess, 1000L, -1);
    }
    
    public boolean noPetReferencesDisplayed() {
        return this.m_openedPetDetail.isEmpty() && PropertiesProvider.getInstance().getObjectProperty("pet") == null;
    }
    
    public void tryToUnScheduleDateUpdateProcess() {
        if (!this.noPetReferencesDisplayed()) {
            return;
        }
        ProcessScheduler.getInstance().remove(this.m_dateUpdateProcess);
    }
    
    public boolean hasPetDetailDisplayed(final PetDetailDialogView pDDV) {
        return this.hasPetDetailDisplayed(pDDV.getPetItem());
    }
    
    public boolean hasPetDetailDisplayed(final Item petItem) {
        return this.m_openedPetDetail.contains(this.getDialogPath(petItem));
    }
    
    public void onPetItemChange() {
        for (final String s : this.m_openedPetDetail) {
            final PetDetailDialogView petDetailDialogView = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet", s);
            petDetailDialogView.updateFields();
        }
        final PetDetailDialogView petDetailDialogView2 = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet");
        if (petDetailDialogView2 == null) {
            return;
        }
        final Item petItem = petDetailDialogView2.getPetItem();
        if (petItem == null) {
            return;
        }
        if (petItem.getReferenceItem() == null) {
            return;
        }
        petDetailDialogView2.updateFields();
    }
    
    public void unloadPetDetailForItem(final Item petItem) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        if (this.hasPetDetailDisplayed(petItem)) {
            Xulor.getInstance().unload(this.getDialogPath(petItem));
        }
        this.onPetItemChange();
        this.initialiseChatForPets(true);
    }
    
    private void initialiseChatForPets(final boolean computeListeners) {
        this.cleanUiPetSupervisors();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final ClientBagContainer bags = localPlayer.getBags();
        if (bags == null) {
            return;
        }
        for (final Item item : bags.getAllItems()) {
            if (item.hasPet()) {
                if (computeListeners) {
                    this.removeChatPetListener(item);
                }
                this.addChatPetListener(item);
                if (!computeListeners) {
                    final Pet pet = item.getPet();
                    if (!pet.getLastHungryDate().before(pet.getLastMealDate())) {
                        if (pet.getHealth() == 0) {
                            this.displayPetWeakMessage(item);
                        }
                        else if (this.getHungryCycles(pet) > 0) {
                            this.displayPetHungryMessage(item);
                        }
                    }
                }
            }
        }
        final Item petItem = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.PET.getId());
        if (petItem != null) {
            if (computeListeners) {
                this.removeChatPetListener(petItem);
            }
            this.addChatPetListener(petItem);
            final Pet pet2 = petItem.getPet();
            if (pet2.getLastHungryDate().after(pet2.getLastMealDate())) {
                if (pet2.getHealth() == 0) {
                    this.displayPetWeakMessage(petItem);
                }
                else if (this.getHungryCycles(pet2) > 0) {
                    this.displayPetHungryMessage(petItem);
                }
            }
        }
        final Item mountItem = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.MOUNT.getId());
        if (mountItem != null) {
            if (computeListeners) {
                this.removeChatPetListener(mountItem);
            }
            this.addChatPetListener(mountItem);
            final Pet pet3 = mountItem.getPet();
            if (pet3.getLastHungryDate().after(pet3.getLastMealDate())) {
                if (pet3.getHealth() == 0) {
                    this.displayPetWeakMessage(mountItem);
                }
                else if (this.getHungryCycles(pet3) > 0) {
                    this.displayPetHungryMessage(mountItem);
                }
            }
        }
    }
    
    private void cleanUiPetSupervisors() {
        final TObjectProcedure procedure = new TObjectProcedure<PetUiSupervisor>() {
            @Override
            public boolean execute(final PetUiSupervisor petUiSupervisor) {
                petUiSupervisor.removeClock();
                return true;
            }
        };
        this.m_petUiSupervisors.forEachValue(procedure);
        this.m_petUiSupervisors.clear();
    }
    
    public void displayPetWeakMessage(final Item petItem) {
        final Pet pet = petItem.getPet();
        final String name = (pet.getName() != null && pet.getName().length() > 0) ? pet.getName() : petItem.getName();
        final String message = WakfuTranslator.getInstance().getString("pet.chat.weak", name);
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public void displayPetHungryMessage(final Item petItem) {
        final Pet pet = petItem.getPet();
        if (pet.getHealth() == 0) {
            return;
        }
        final int penalty = pet.getDefinition().getHealthPenalty(HealthPenaltyType.MAX_MEAL_INTERVAL);
        final String name = (pet.getName() != null && pet.getName().length() > 0) ? pet.getName() : petItem.getName();
        final String message = WakfuTranslator.getInstance().getString("pet.chat.hungry", name, penalty);
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public void highLightPetEquipSlots(final Item item) {
        final int referenceId = item.getReferenceId();
        for (final String itemDetailMapId : this.m_openedPetDetail) {
            final PetDetailDialogView petDetailDialogView = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet", itemDetailMapId);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(itemDetailMapId);
            final Pet pet = petDetailDialogView.getPetItem().getPet();
            if (pet.getDefinition().containsColorItem(referenceId)) {
                final Image image = (Image)map.getElement("colorImageContainer");
                this.addColorTween(image);
            }
            else if (pet.getDefinition().containsEquipment(referenceId) || pet.getDefinition().containsReskinItem(referenceId)) {
                final Image image = (Image)map.getElement("equipmentImageContainer");
                this.addColorTween(image);
            }
            else {
                if (!PetHelper.isValidMeal(pet, referenceId) && !PetHelper.isHealMeal(pet, referenceId) && !pet.getDefinition().containsSleepItem(referenceId)) {
                    continue;
                }
                final Image image = (Image)map.getElement("petPortrait");
                this.addColorTween(image);
            }
        }
    }
    
    private void addColorTween(final Image image) {
        final DecoratorAppearance appearance = image.getAppearance();
        final Color c1 = new Color(Color.WHITE.get());
        final Color c2 = new Color(Color.WHITE_QUARTER_ALPHA.get());
        appearance.removeTweensOfType(ModulationColorTween.class);
        appearance.setModulationColor(c1);
        final ModulationColorTween tween = new ModulationColorTween(c1, c2, appearance, 0, 300, -1, TweenFunction.PROGRESSIVE);
        appearance.addTween(tween);
    }
    
    public void resetPetEquipSlots() {
        for (final String itemDetailMapId : this.m_openedPetDetail) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(itemDetailMapId);
            this.removeTweens(map, "colorImageContainer");
            this.removeTweens(map, "equipmentImageContainer");
            this.removeTweens(map, "petPortrait");
        }
    }
    
    private void removeTweens(final ElementMap map, final String name) {
        try {
            final Image image = (Image)map.getElement(name);
            if (image == null || image.isUnloading()) {
                return;
            }
            image.getAppearance().removeTweensOfType(ModulationColorTween.class);
        }
        catch (Exception e) {
            UIPetFrame.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIPetFrame.class);
        UIPetFrame.m_instance = new UIPetFrame();
    }
    
    private class PetUiSupervisor implements Runnable, PetModelListener
    {
        private Item m_petItem;
        private PetController m_controller;
        
        private PetUiSupervisor(final Item petItem) {
            super();
            this.m_petItem = petItem;
            final Pet pet = this.m_petItem.getPet();
            this.m_controller = new PetController(this.m_petItem);
            pet.addListener(this);
            final GameDate gameDate = new GameDate(pet.getLastHungryDate());
            final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
            final GameInterval deltaLastHungryDate = gameDate.timeTo(now);
            final GameInterval mealMaxInterval = new GameInterval(pet.getDefinition().getMealMaxInterval());
            final int cycles = deltaLastHungryDate.getDivisionResult(mealMaxInterval);
            mealMaxInterval.multiply(cycles + 1);
            gameDate.add(mealMaxInterval);
            final long futureHungryDelay = now.timeTo(gameDate).toSeconds();
            ProcessScheduler.getInstance().schedule(this, futureHungryDelay * 1000L, 1);
        }
        
        @Override
        public void run() {
            this.m_controller.setHealth(Math.max(0, this.m_petItem.getPet().getHealth() - 1));
            UIPetFrame.this.displayPetHungryMessage(this.m_petItem);
            this.reLaunchClock();
        }
        
        private void reLaunchClock() {
            ProcessScheduler.getInstance().remove(this);
            ProcessScheduler.getInstance().schedule(this, this.m_petItem.getPet().getDefinition().getMealMaxInterval().toSeconds() * 1000L, 1);
        }
        
        @Override
        public void nameChanged(final String name) {
        }
        
        @Override
        public void colorItemChanged(final int colorItemRefId) {
        }
        
        @Override
        public void equippedItemChanged(final int equippedItem) {
        }
        
        @Override
        public void healthChanged(final int health) {
            if (health == 0) {
                UIPetFrame.this.displayPetWeakMessage(this.m_petItem);
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_petItem, this.m_petItem.getFields());
            this.reloadEquipmentBuff();
        }
        
        @Override
        public void xpChanged(final int xp) {
        }
        
        @Override
        public void lastMealDateChanged(final GameDateConst mealDate) {
            this.reLaunchClock();
        }
        
        @Override
        public void lastHungryDateChanged(final GameDateConst hungryDate) {
            final Pet pet = this.m_petItem.getPet();
            if (pet.getHealth() == 0) {
                return;
            }
            if (UIPetFrame.this.getHungryCycles(pet) > 0) {
                UIPetFrame.getInstance().displayPetHungryMessage(this.m_petItem);
            }
        }
        
        @Override
        public void sleepDateChanged(final GameDateConst sleepDate) {
        }
        
        @Override
        public void sleepItemChanged(final int sleepRefItemId) {
            this.reloadEquipmentBuff();
        }
        
        public void reloadEquipmentBuff() {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer == null) {
                return;
            }
            final Item petItem = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.PET.m_id);
            if (petItem != null && petItem == this.m_petItem) {
                localPlayer.reloadItemEffects();
                localPlayer.resetEquipmentBonusCache();
            }
            final Item mountItem = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.MOUNT.m_id);
            if (mountItem != null && mountItem == this.m_petItem) {
                localPlayer.reloadItemEffects();
                localPlayer.resetEquipmentBonusCache();
            }
        }
        
        public void removeClock() {
            ProcessScheduler.getInstance().remove(this);
        }
    }
}
