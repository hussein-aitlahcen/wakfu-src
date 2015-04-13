package com.ankamagames.wakfu.common.datas.Breed;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.util.*;
import gnu.trove.*;

public abstract class AbstractBreedManager<B extends Breed>
{
    protected static final Logger m_logger;
    protected final TShortObjectHashMap<B> m_breeds;
    protected final TIntObjectHashMap<List<B>> m_breedsByFamily;
    
    public AbstractBreedManager() {
        super();
        this.m_breeds = new TShortObjectHashMap<B>();
        this.m_breedsByFamily = new TIntObjectHashMap<List<B>>();
    }
    
    @Nullable
    public B getBreedFromId(final short id) {
        return this.m_breeds.get(id);
    }
    
    public void addBreed(final B breed) {
        if (breed == null) {
            AbstractBreedManager.m_logger.error((Object)"[esp\u00e8ce ignor\u00e9e] on tente d'ajouter une esp\u00e8ce null");
            return;
        }
        if (this.m_breeds.containsKey(breed.getBreedId())) {
            AbstractBreedManager.m_logger.error((Object)("[esp\u00e8ce ignor\u00e9e] on tente de rajouter une esp\u00e8ce dont l'id existe d\u00e9j\u00e0 pour une autre " + breed.getBreedId()));
            return;
        }
        this.m_breeds.put(breed.getBreedId(), breed);
        final int familyId = breed.getFamilyId();
        List<B> familyBreeds = this.m_breedsByFamily.get(familyId);
        if (familyBreeds == null) {
            familyBreeds = new ArrayList<B>();
            this.m_breedsByFamily.put(familyId, familyBreeds);
        }
        familyBreeds.add(breed);
    }
    
    public List<B> getFamilyBreeds(final int familyId) {
        if (!this.m_breedsByFamily.containsKey(familyId)) {
            return Collections.emptyList();
        }
        return this.m_breedsByFamily.get(familyId);
    }
    
    public void foreachBreed(final TObjectProcedure<B> procedure) {
        this.m_breeds.forEachValue(procedure);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractBreedManager.class);
    }
}
