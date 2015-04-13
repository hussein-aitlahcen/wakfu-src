package com.ankamagames.baseImpl.common.clientAndServer.utils;

public abstract class ConstantDefinition<E>
{
    private int m_id;
    private E m_object;
    
    protected ConstantDefinition() {
        super();
    }
    
    public ConstantDefinition(final int id, final E object, final Constants<E> constants) {
        super();
        this.m_id = id;
        this.m_object = object;
        constants.addConstantDefinition(this);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public E getObject() {
        return this.m_object;
    }
    
    public abstract String getAdminDescription();
}
