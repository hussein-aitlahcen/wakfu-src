package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class ProtectorManagerBase<TProtector extends ProtectorBase>
{
    protected static final Logger m_logger;
    private final TIntObjectHashMap<TProtector> m_protectors;
    private final List<ProtectorManagerObserver<? super TProtector>> m_observers;
    private final List<ProtectorManagerObserver<? super TProtector>> m_observersToRemove;
    
    protected ProtectorManagerBase() {
        super();
        this.m_protectors = new TIntObjectHashMap<TProtector>();
        this.m_observers = new ArrayList<ProtectorManagerObserver<? super TProtector>>();
        this.m_observersToRemove = new ArrayList<ProtectorManagerObserver<? super TProtector>>();
    }
    
    public void addObserver(final ProtectorManagerObserver<? super TProtector> observer) {
        if (!this.m_observers.contains(observer)) {
            this.m_observers.add(observer);
        }
    }
    
    public void shouldRemove(final ProtectorManagerObserver<? super TProtector> observer) {
        this.m_observersToRemove.add(observer);
    }
    
    private void processRemoveObservers() {
        this.m_observers.removeAll(this.m_observersToRemove);
        this.m_observersToRemove.clear();
    }
    
    public boolean registerProtector(final TProtector protector) {
        final int id = protector.getId();
        final TProtector p = this.m_protectors.get(id);
        if (p != null && p != protector) {
            ProtectorManagerBase.m_logger.error((Object)("Un protecteur avec cet ID (" + id + ") est d\u00e9j\u00e0 pr\u00e9sent enregistr\u00e9, et n'est pas celui-ci."));
            return false;
        }
        if (protector.getTerritory() == null) {
            ProtectorManagerBase.m_logger.error((Object)("Tentative d'enregistrement d'un protecteur avec des donn\u00e9es incorrectes : " + protector));
            return false;
        }
        this.m_protectors.put(id, protector);
        try {
            this.processRemoveObservers();
            for (int i = 0, size = this.m_observers.size(); i < size; ++i) {
                this.m_observers.get(i).onProtectorRegistered((Object)protector);
            }
            this.processRemoveObservers();
        }
        catch (Exception e) {
            ProtectorManagerBase.m_logger.error((Object)("Exception lev\u00e9e par un observer lors de l'enregistrement d'un protecteur (ID=" + id + ')'), (Throwable)e);
        }
        return true;
    }
    
    public void findProtectors(final ProtectorSearchCriterion<? super TProtector> criterion, final Collection<? super TProtector> foundProtectors) {
        this.m_protectors.forEachEntry(new TIntObjectProcedure<TProtector>() {
            @Override
            public boolean execute(final int id, final TProtector protector) {
                if (criterion == null || criterion.match(protector)) {
                    foundProtectors.add(protector);
                }
                return true;
            }
        });
    }
    
    @Nullable
    public TProtector getProtector(final int id) {
        return this.m_protectors.get(id);
    }
    
    public void clear() {
        if (!this.m_observers.isEmpty()) {
            this.processRemoveObservers();
            this.m_protectors.forEachValue(new TObjectProcedure<TProtector>() {
                @Override
                public boolean execute(final TProtector protector) {
                    for (final ProtectorManagerObserver<? super TProtector> observer : ProtectorManagerBase.this.m_observers) {
                        try {
                            observer.onProtectorUnregistered((Object)protector);
                        }
                        catch (Exception e) {
                            ProtectorManagerBase.m_logger.error((Object)("Exception lev\u00e9e lors du d\u00e9-enregitrement d'un protecteur ID=" + protector.getId()));
                        }
                    }
                    return true;
                }
            });
            this.processRemoveObservers();
        }
        this.m_protectors.clear();
    }
    
    public TIntObjectIterator<TProtector> getProtectorIterator() {
        return this.m_protectors.iterator();
    }
    
    public TProtector getRandomProtector() {
        final ProtectorBase[] values = this.m_protectors.getValues(new ProtectorBase[this.m_protectors.size()]);
        return (TProtector)values[MathHelper.random(values.length)];
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorManagerBase.class);
    }
}
