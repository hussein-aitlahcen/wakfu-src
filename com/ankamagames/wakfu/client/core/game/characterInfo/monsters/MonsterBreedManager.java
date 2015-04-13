package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import com.ankamagames.wakfu.client.binaryStorage.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

public class MonsterBreedManager extends AbstractBreedManager<MonsterBreed>
{
    private final MonsterBreedUniqueInstanceLoader m_monsterBreedUniqueInstanceLoader;
    private static final MonsterBreedManager m_instance;
    private final BinaryLoader<MonsterBinaryData> m_creator;
    
    public static MonsterBreedManager getInstance() {
        return MonsterBreedManager.m_instance;
    }
    
    private MonsterBreedManager() {
        super();
        this.m_monsterBreedUniqueInstanceLoader = new MonsterBreedUniqueInstanceLoader();
        this.m_creator = new BinaryLoaderFromFile<MonsterBinaryData>(new MonsterBinaryData());
        MonsterBreedManagerProvider.setManager(this);
    }
    
    public MonsterBreed[] getBreeds() {
        return this.m_breeds.getValues(new MonsterBreed[this.m_breeds.size()]);
    }
    
    @Nullable
    @Override
    public MonsterBreed getBreedFromId(final short id) {
        if (id <= 0) {
            return null;
        }
        MonsterBreed breed = (MonsterBreed)this.m_breeds.get(id);
        if (breed != null) {
            return breed;
        }
        final MonsterBinaryData data = this.m_creator.createFromId(id);
        if (data == null) {
            return null;
        }
        breed = this.m_monsterBreedUniqueInstanceLoader.loadFromBinaryForm(data);
        if (breed != null) {
            this.addBreed(breed);
        }
        return breed;
    }
    
    public TIntObjectHashMap<MonsterBreed> getFullList() {
        final TIntObjectHashMap<MonsterBreed> map = new TIntObjectHashMap<MonsterBreed>();
        try {
            BinaryDocumentManager.getInstance().foreach(new MonsterBinaryData(), new LoadProcedure<MonsterBinaryData>() {
                @Override
                public void load(final MonsterBinaryData data) {
                    final MonsterBreed referenceItem = MonsterBreedManager.this.m_monsterBreedUniqueInstanceLoader.loadFromBinaryForm(data);
                    map.put(referenceItem.getBreedId(), referenceItem);
                }
            });
        }
        catch (Exception e) {
            MonsterBreedManager.m_logger.error((Object)"", (Throwable)e);
        }
        return map;
    }
    
    static {
        m_instance = new MonsterBreedManager();
    }
}
