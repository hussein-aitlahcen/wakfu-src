package com.ankamagames.wakfu.common.game.companion;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CompanionController
{
    private static final Logger m_logger;
    protected final CompanionModel m_companionModel;
    
    public CompanionController(@NotNull final CompanionModel companionModel) {
        super();
        this.m_companionModel = companionModel;
    }
    
    public void addXp(final long xpToAdd) throws CompanionException {
        this.m_companionModel.addXp(xpToAdd);
    }
    
    public void setXp(final long xp) throws CompanionException {
        this.m_companionModel.setXp(xp);
    }
    
    public boolean setName(final String name) throws CompanionException {
        if (!WordsModerator.getInstance().validateName(name)) {
            return false;
        }
        if (name.equals(this.m_companionModel.getName())) {
            return false;
        }
        this.m_companionModel.setName(name);
        return true;
    }
    
    public boolean addToEquipment(final byte equipmentPosition, final ItemEquipment companionEquipment, final Item itemToAdd) throws CompanionException {
        boolean error = false;
        try {
            ((ArrayInventoryWithoutCheck<Item, R>)companionEquipment).addAt(itemToAdd, equipmentPosition);
        }
        catch (InventoryCapacityReachedException e) {
            error = true;
            CompanionController.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        catch (ContentAlreadyPresentException e2) {
            error = true;
            CompanionController.m_logger.error((Object)"Exception levee", (Throwable)e2);
        }
        catch (PositionAlreadyUsedException e3) {
            error = true;
            CompanionController.m_logger.error((Object)"Exception levee", (Throwable)e3);
        }
        if (error) {
            ((ArrayInventoryWithoutCheck<Item, R>)companionEquipment).remove(itemToAdd);
            throw new CompanionException("Erreur lors de l'ajout de l'objet " + itemToAdd + " \u00e0 la position donn\u00e9e " + equipmentPosition + " sur le companion " + this.m_companionModel);
        }
        return error;
    }
    
    public void removeItemFromEquipment(final BasicCharacterInfo player, final long itemUid, final long destBagId, final short destPosition) throws CompanionException {
        final ItemEquipment equipment = this.m_companionModel.getItemEquipment();
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getWithUniqueId(itemUid);
        if (item == null) {
            throw new CompanionException("Objet inconnu " + itemUid);
        }
        final AbstractBag bag = player.getBags().getBagFromUid(destBagId);
        if (bag == null) {
            throw new CompanionException("Le sac de destination n'existe pas " + destBagId);
        }
        if (!bag.canAdd(item, destPosition)) {
            throw new CompanionException("L'item  " + item + " ne peut pas \u00eatre ajout\u00e9 dans le sac " + destBagId + " \u00e0 la position " + destPosition);
        }
        boolean error = false;
        try {
            bag.addAt(item, destPosition);
        }
        catch (InventoryCapacityReachedException e) {
            error = true;
            CompanionController.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        catch (ContentAlreadyPresentException e2) {
            error = true;
            CompanionController.m_logger.error((Object)"Exception levee", (Throwable)e2);
        }
        catch (PositionAlreadyUsedException e3) {
            error = true;
            CompanionController.m_logger.error((Object)"Exception levee", (Throwable)e3);
        }
        if (error) {
            throw new CompanionException("Erreur lors de l'ajout de l'objet " + item + " \u00e0 la position donn\u00e9e " + destPosition + " dans le sac " + bag);
        }
        ((ArrayInventoryWithoutCheck<Item, R>)equipment).remove(item);
    }
    
    @Override
    public String toString() {
        return "CompanionController{m_companionModel=" + this.m_companionModel + '}';
    }
    
    public boolean mustRemoveCompanionFromManager() {
        return !this.m_companionModel.isUnlocked() && !FreeCompanionManager.INSTANCE.isFreeCompanion(this.m_companionModel.getBreedId()) && this.m_companionModel.getItemEquipment().isEmpty();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionController.class);
    }
}
