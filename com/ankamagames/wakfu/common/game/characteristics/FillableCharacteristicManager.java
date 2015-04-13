package com.ankamagames.wakfu.common.game.characteristics;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public class FillableCharacteristicManager implements CharacteristicManager<AbstractCharacteristic>
{
    private final TIntObjectHashMap<AbstractCharacteristic> m_characteristics;
    
    public FillableCharacteristicManager(final Iterable<CharacteristicType> characs) {
        super();
        this.m_characteristics = new TIntObjectHashMap<AbstractCharacteristic>();
        for (final CharacteristicType c : characs) {
            this.m_characteristics.put(c.getId(), CharacteristicFactory.newCharacteristic(c));
        }
    }
    
    public FillableCharacteristicManager(final CharacteristicType[] characs) {
        super();
        this.m_characteristics = new TIntObjectHashMap<AbstractCharacteristic>();
        for (final CharacteristicType c : characs) {
            this.m_characteristics.put(c.getId(), CharacteristicFactory.newCharacteristic(c));
        }
    }
    
    @Override
    public void makeDefault() {
        final TIntObjectIterator<AbstractCharacteristic> iterator = this.m_characteristics.iterator();
        int i = this.m_characteristics.size();
        while (i-- > 0) {
            iterator.advance();
            final AbstractCharacteristic charac = iterator.value();
            charac.makeDefault();
        }
    }
    
    @Override
    public boolean contains(final CharacteristicType charac) {
        return charac != null && this.m_characteristics.contains(charac.getId());
    }
    
    @Override
    public AbstractCharacteristic getCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.get(charac.getId());
    }
    
    @Override
    public int getCharacteristicValue(final CharacteristicType charac) throws UnsupportedOperationException {
        final AbstractCharacteristic cha = this.m_characteristics.get(charac.getId());
        if (cha != null) {
            return cha.value();
        }
        throw new UnsupportedOperationException("caract\u00e9ristique inexistante : " + charac.getId());
    }
    
    @Override
    public int getCharacteristicMaxValue(final CharacteristicType charac) throws UnsupportedOperationException {
        final AbstractCharacteristic cha = this.m_characteristics.get(charac.getId());
        if (cha != null) {
            return cha.max();
        }
        throw new UnsupportedOperationException("caract\u00e9ristique inexistante : " + charac.getId());
    }
    
    public void setListener(final CharacteristicUpdateListener listener) {
        final TIntObjectIterator<AbstractCharacteristic> iterator = this.m_characteristics.iterator();
        int i = this.m_characteristics.size();
        while (i-- > 0) {
            iterator.advance();
            final AbstractCharacteristic charac = iterator.value();
            charac.addListener(listener);
        }
    }
    
    public void clearListeners() {
        if (this.m_characteristics.isEmpty()) {
            return;
        }
        this.m_characteristics.forEachValue(new TObjectProcedure<AbstractCharacteristic>() {
            @Override
            public boolean execute(final AbstractCharacteristic characteristic) {
                characteristic.clearListener();
                return true;
            }
        });
    }
    
    public TIntObjectIterator<AbstractCharacteristic> iterator() {
        return this.m_characteristics.iterator();
    }
    
    @Override
    public boolean toRaw(final RawCharacteristics rawCharacteristics, final CharacteristicType... characteristicTypes) {
        throw new UnsupportedOperationException("On ne peut pas s\u00e9rialiser un characteristic manager d'objet");
    }
    
    @Override
    public boolean toRaw(final RawCharacteristics raw) {
        throw new UnsupportedOperationException("On ne peut pas s\u00e9rialiser un characteristic manager d'objet");
    }
    
    @Override
    public boolean fromRaw(final RawCharacteristics raw) {
        throw new UnsupportedOperationException("On ne peut pas s\u00e9rialiser un characteristic manager d'objet");
    }
    
    @Override
    public void copy(final FighterCharacteristicManager doubleCharac) {
        throw new UnsupportedOperationException("On ne peut pas invoquer le double d'un objet ");
    }
}
