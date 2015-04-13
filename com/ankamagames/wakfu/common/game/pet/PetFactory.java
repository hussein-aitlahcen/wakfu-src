package com.ankamagames.wakfu.common.game.pet;

import java.util.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class PetFactory
{
    public static final PetFactory INSTANCE;
    private final ArrayList<PetCreationListener> m_listeners;
    
    private PetFactory() {
        super();
        this.m_listeners = new ArrayList<PetCreationListener>();
    }
    
    public boolean addListener(final PetCreationListener listener) {
        return this.m_listeners.add(listener);
    }
    
    public Pet createPet(final PetDefinition def) {
        final PetModel pet = new PetModel();
        pet.initialize(def);
        this.firePetCreation(pet);
        return pet;
    }
    
    public Pet createPet(final RawPet raw) {
        final PetModel pet = new PetModel();
        pet.fromRaw(raw);
        this.firePetCreation(pet);
        return pet;
    }
    
    public Pet copyPet(final Pet pet) {
        final PetModel newPet = new PetModel();
        newPet.initialize(pet.getDefinition());
        newPet.setName(pet.getName());
        newPet.setColorItemRefId(pet.getColorItemRefId());
        newPet.setEquippedRefItemId(pet.getEquippedRefItemId());
        newPet.setHealth(pet.getHealth());
        newPet.setXp(pet.getXp());
        newPet.setLastMealDate(pet.getLastMealDate());
        newPet.setLastHungryDate(pet.getLastHungryDate());
        newPet.setSleepRefItemId(pet.getSleepRefItemId());
        newPet.setSleepDate(pet.getSleepDate());
        this.firePetCreation(newPet);
        return newPet;
    }
    
    private void firePetCreation(final PetModel pet) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onPetCreated(pet);
        }
    }
    
    @Override
    public String toString() {
        return "PetFactory{m_listeners=" + this.m_listeners.size() + '}';
    }
    
    static {
        INSTANCE = new PetFactory();
    }
}
