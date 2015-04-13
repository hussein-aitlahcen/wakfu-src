package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class NetCraftFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static NetCraftFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 15710: {
                final CraftLearnedMessage msg = (CraftLearnedMessage)message;
                this.craftLearned(localPlayer, msg);
                return false;
            }
            case 15712: {
                final CraftLearnedRecipeMessage msg2 = (CraftLearnedRecipeMessage)message;
                this.craftLearnedRecipe(localPlayer, msg2);
                return false;
            }
            case 15714: {
                final CraftXpGainedMessage msg3 = (CraftXpGainedMessage)message;
                this.xpGained(localPlayer, msg3);
                return false;
            }
            case 15716: {
                final CraftOccupationStartedMessage msg4 = (CraftOccupationStartedMessage)message;
                this.craftOccupationStarted(localPlayer, msg4);
                return false;
            }
            case 15718: {
                final CraftOccupationResultMessage msg5 = (CraftOccupationResultMessage)message;
                this.craftOccupationResult(localPlayer, msg5);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void craftOccupationResult(final LocalPlayerCharacter localPlayer, final CraftOccupationResultMessage msg) {
        final CraftResult error = msg.getResult();
        switch (error) {
            case TABLE_TOO_FAR: {
                break;
            }
            case UNKNOWN_CRAFT: {
                break;
            }
            case UNKNOWN_RECIPE: {
                break;
            }
            case CHARACTER_CANNOT_USE: {
                break;
            }
            case UNLEARN_CRAFT: {
                break;
            }
            case RECIPE_TABLE_NOT_ALLOWED: {
                break;
            }
            case RECIPE_CRITERION_UNAVAILABLE: {
                break;
            }
            case NO_REQUIRED_ITEM: {
                break;
            }
            case BAD_INGREDIENTS: {
                break;
            }
            case CRAFT_END_TOO_EARLY: {
                break;
            }
            case SUCCESS_ROLL_FAILED: {
                UICraftTableFrame.getInstance().applyCraftButtonAps("6001039.xps");
                WakfuSoundManager.getInstance().playGUISound(600125L);
                final RecipeView recipeView = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
                String recipeName;
                if (recipeView == null) {
                    recipeName = WakfuTranslator.getInstance().getString(46, UICraftTableFrame.getInstance().getRecipeIdFromIngredients(), new Object[0]);
                }
                else {
                    recipeName = recipeView.getName();
                }
                final String message = WakfuTranslator.getInstance().getString("craft.knownRecipeExecutionFailure", recipeName);
                final ChatMessage chatMessage = new ChatMessage(message);
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
                UICraftTableFrame.getInstance().stopCraft();
                return;
            }
            case FAKE_CRAFT_FAILED: {
                UICraftTableFrame.getInstance().applyCraftButtonAps("6001039.xps");
                WakfuSoundManager.getInstance().playGUISound(600125L);
                final String message2 = WakfuTranslator.getInstance().getString("craft.unknownRecipeExecutionFailure");
                final ChatMessage chatMessage2 = new ChatMessage(message2);
                chatMessage2.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage2);
                UICraftTableFrame.getInstance().stopCraft();
                return;
            }
            case FAILING_SECRET_CRAFT_FAILED: {
                UICraftTableFrame.getInstance().applyCraftButtonAps("6001039.xps");
                WakfuSoundManager.getInstance().playGUISound(600125L);
                final String message2 = WakfuTranslator.getInstance().getString("craft.unknownRecipeCantBeCraftedHere");
                final ChatMessage chatMessage2 = new ChatMessage(message2);
                chatMessage2.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage2);
                UICraftTableFrame.getInstance().stopCraft();
                return;
            }
            case NO_ERROR: {
                final boolean perfect = ReferenceItemManager.getInstance().getReferenceItem(msg.getResultItemRefId()).getReferenceItemDisplayer().isPerfect();
                final RecipeView recipeView2 = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
                String recipeName2;
                if (recipeView2 == null) {
                    recipeName2 = WakfuTranslator.getInstance().getString(46, UICraftTableFrame.getInstance().getRecipeIdFromIngredients(), new Object[0]);
                }
                else {
                    recipeName2 = recipeView2.getName();
                }
                final String message3 = WakfuTranslator.getInstance().getString("craft.knownRecipeExecutionSuccess", recipeName2);
                final ChatMessage chatMessage3 = new ChatMessage(message3);
                chatMessage3.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage3);
                UICraftTableFrame.getInstance().onCraftSuccess(perfect);
                return;
            }
            case FORCED_CANCEL: {
                final String message2 = WakfuTranslator.getInstance().getString("craft.errorOccupation");
                final ChatMessage chatMessage2 = new ChatMessage(message2);
                chatMessage2.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage2);
                if (WakfuGameEntity.getInstance().hasFrame(UICraftTableFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UICraftTableFrame.getInstance());
                }
                return;
            }
            case UNKNOWN: {
                final String message2 = WakfuTranslator.getInstance().getString("craft.errorOccupation");
                final ChatMessage chatMessage2 = new ChatMessage(message2);
                chatMessage2.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage2);
                break;
            }
            default: {
                NetCraftFrame.m_logger.error((Object)("Reception d'un message d'erreur de craft " + error + " non g\u00e9r\u00e9 par le client"));
                break;
            }
        }
        UICraftTableFrame.getInstance();
        UICraftTableFrame.cancelCraft();
    }
    
    private void craftLearned(final LocalPlayerCharacter localPlayer, final CraftLearnedMessage msg) {
        final int refCraftId = msg.getRefCraftId();
        final ReferenceCraft refCraft = CraftManager.INSTANCE.getCraft(refCraftId);
        if (refCraft == null) {
            NetCraftFrame.m_logger.error((Object)("R\u00e9ception d'un message d'apprentissage pour le m\u00e9tier " + refCraftId + " inconnu du manager"));
            return;
        }
        localPlayer.getCraftHandler().learnCraft(refCraft);
    }
    
    private void craftLearnedRecipe(final LocalPlayerCharacter localPlayer, final CraftLearnedRecipeMessage msg) {
        final int refCraftId = msg.getRefCraftId();
        final int recipeId = msg.getRecipeId();
        final ReferenceCraft refCraft = CraftManager.INSTANCE.getCraft(refCraftId);
        if (refCraft == null) {
            NetCraftFrame.m_logger.error((Object)("R\u00e9ception d'un message d'apprentissage de recette pour le metier " + refCraftId + " inconnu du manager"));
            return;
        }
        final CraftRecipe recipe = refCraft.getRecipe(recipeId);
        if (recipe == null) {
            NetCraftFrame.m_logger.error((Object)("R\u00e9ception d'un message d'apprentissage de la recette " + refCraftId + " inconnue du manager"));
            return;
        }
        localPlayer.getCraftHandler().learnRecipe(refCraftId, recipeId);
    }
    
    private void xpGained(final LocalPlayerCharacter localPlayer, final CraftXpGainedMessage msg) {
        final int craftId = msg.getRefCraftId();
        final long xpAdded = msg.getXpAdded();
        localPlayer.getCraftHandler().addXp(craftId, xpAdded);
    }
    
    private void craftOccupationStarted(final LocalPlayerCharacter localPlayer, final CraftOccupationStartedMessage msg) {
        final long tableId = msg.getTableId();
        final int recipeId = msg.getRecipeId();
        final byte recipeType = msg.getRecipeType();
        final long duration = msg.getDuration();
        final CraftInteractiveElement craftTable = (CraftInteractiveElement)LocalPartitionManager.getInstance().getInteractiveElement(tableId);
        if (!craftTable.getApproachPoints().contains(localPlayer.getPosition())) {
            NetCraftFrame.m_logger.error((Object)("D\u00e9Snchro de position? le joueur " + localPlayer + " essaye d'interagir avec la machine " + craftTable + " sans se trouver dans le pattern d'activation"));
            return;
        }
        final int craftId = craftTable.getCraftId();
        final ReferenceCraft referenceCraft = CraftManager.INSTANCE.getCraft(craftId);
        if (referenceCraft == null) {
            NetCraftFrame.m_logger.error((Object)("Le joueur " + localPlayer + " essaye d'utiliser la recette " + recipeId + " du m\u00e9tier " + craftId + " alors que celui-ci n'existe pas"));
            return;
        }
        CraftRecipe recipe;
        if (recipeType == -1) {
            recipe = new FakeCraftRecipe(craftId, localPlayer.getCraftHandler().getLevel(craftId));
        }
        else if (recipeType == -2) {
            final CraftRecipe secretRecipe = referenceCraft.getRecipe(recipeId);
            recipe = new FailingSecretCraftRecipe(secretRecipe, craftId, secretRecipe.getRequiredLevel());
        }
        else {
            recipe = referenceCraft.getRecipe(recipeId);
        }
        if (recipe == null) {
            NetCraftFrame.m_logger.error((Object)("Le joueur " + localPlayer + " essaye d'utiliser la recette " + recipeId + " du m\u00e9tier " + craftId + " alors que celle-ci n'existe pas"));
            return;
        }
        final CraftOccupation occupation = new CraftOccupation(craftTable, recipe, duration);
        if (occupation.isAllowed()) {
            localPlayer.setCurrentOccupation(occupation);
            occupation.begin();
        }
        else {
            occupation.cancel();
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetCraftFrame.class);
        NetCraftFrame.INSTANCE = new NetCraftFrame();
    }
}
