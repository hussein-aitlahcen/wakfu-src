package com.ankamagames.wakfu.common.game.pet;

import com.ankamagames.wakfu.common.game.pet.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class PetController
{
    private final PetHolder m_petHolder;
    
    public PetController(final PetHolder holder) {
        super();
        this.m_petHolder = holder;
    }
    
    public final void setName(final String name) throws PetException, PetControllerException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        if (name.equals(pet.getName())) {
            throw new PetControllerException("Le changement de nom demand\u00e9 est identique \u00e0 l'ancien");
        }
        final boolean invalidName = !WordsModerator.getInstance().validateName(name);
        if (invalidName) {
            throw new PetControllerException("Nom de familier " + name + " invalid\u00e9 par le " + WordsModerator.class.getSimpleName());
        }
        pet.setName(name);
    }
    
    public final void setXp(final int xp) throws PetException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        pet.setXp(xp);
    }
    
    public final void setLastMealDate(final GameDateConst date) {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        pet.setLastMealDate(date);
    }
    
    public final void resetHungryDate() {
        this.setLastHungryDate(WakfuGameCalendar.getInstance().getDate());
    }
    
    public final void setLastHungryDate(final GameDateConst date) {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        pet.setLastHungryDate(this.normalizeHungryDate(date));
    }
    
    private GameDateConst normalizeHungryDate(final GameDateConst date) {
        final GameDate gameDate = new GameDate(date);
        gameDate.setHours(23);
        gameDate.setMinutes(59);
        gameDate.setSeconds(59);
        return gameDate;
    }
    
    public final void setEquipment(final int equipmentRefId) throws PetControllerException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        if (!pet.getDefinition().containsEquipment(equipmentRefId) && !pet.getDefinition().containsReskinItem(equipmentRefId)) {
            throw new PetControllerException("On ne peut pas \u00e9quiper le familier avec l'item " + equipmentRefId);
        }
        pet.setEquippedRefItemId(equipmentRefId);
    }
    
    public final void removeEquipment() throws PetControllerException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        if (pet.getEquippedRefItemId() <= 0) {
            throw new PetControllerException("Le Familier n'a pas d'\u00e9quipement");
        }
        pet.setEquippedRefItemId(0);
    }
    
    public final void setHealth(final int health) {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        pet.setHealth(health);
    }
    
    public void setColorItem(final int colorItemRefId) throws PetControllerException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        if (colorItemRefId == 0) {
            return;
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        if (!pet.getDefinition().containsColorItem(colorItemRefId)) {
            throw new PetControllerException("On ne peut appliquer de couleur sur le familier avec l'item " + colorItemRefId);
        }
        pet.setColorItemRefId(colorItemRefId);
    }
    
    public final void setSleepRefItemId(final int sleepRefItemId) throws PetControllerException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        if (pet.isSleeping()) {
            throw new PetControllerException("Le familier est d\u00e9j\u00e0 endormi");
        }
        if (pet.getSleepRefItemId() > 0) {
            throw new PetControllerException("Le familier est d\u00e9j\u00e0 endormi par l'item " + pet.getSleepRefItemId());
        }
        if (!pet.getDefinition().containsSleepItem(sleepRefItemId)) {
            throw new PetControllerException("On ne peut pas endormir le familier avec l'item " + sleepRefItemId);
        }
        pet.setSleepRefItemId(sleepRefItemId);
    }
    
    public final void setSleepDate(final GameDateConst date) throws PetControllerException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        if (pet.isSleeping()) {
            throw new PetControllerException("Le familier est d\u00e9j\u00e0 endormi");
        }
        if (!pet.getSleepDate().isNull()) {
            throw new PetControllerException("Le familier est d\u00e9j\u00e0 endormi depuis " + pet.getSleepDate());
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        final GameIntervalConst sleepInterval = pet.getDefinition().getSleepItemInterval(pet.getSleepRefItemId());
        final GameDate projectedEndDate = new GameDate(date);
        projectedEndDate.add(sleepInterval);
        if (projectedEndDate.before(now)) {
            throw new PetControllerException("Impossible de red\u00e9finir une date se terminant dans le pass\u00e9");
        }
        pet.setSleepDate(date);
    }
    
    public final void removeSleepRefItemId() {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        pet.setSleepRefItemId(0);
    }
    
    public final void removeSleepDate() {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        final PetModel pet = (PetModel)this.m_petHolder.getPet();
        pet.setSleepDate(GameDate.NULL_DATE);
    }
    
    protected void addPetListener(final PetModelListener listener) throws PetException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        this.m_petHolder.getPet().addListener(listener);
    }
    
    protected void removePetListener(final PetModelListener listener) throws PetException {
        if (!this.m_petHolder.hasPet()) {
            throw new PetException("Il n'y a aucun pet avec lequel interagir");
        }
        this.m_petHolder.getPet().removeListener(listener);
    }
    
    public PetHolder getPetHolder() {
        return this.m_petHolder;
    }
    
    @Override
    public String toString() {
        return "PetController{m_petHolder=" + this.m_petHolder + '}';
    }
}
