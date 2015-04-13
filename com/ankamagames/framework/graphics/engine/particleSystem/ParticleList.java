package com.ankamagames.framework.graphics.engine.particleSystem;

public class ParticleList
{
    private int m_size;
    private final Particle[] m_particles;
    private int m_removedCount;
    
    ParticleList(final int capacity) {
        super();
        this.m_removedCount = 0;
        this.m_particles = new Particle[capacity];
    }
    
    public final void add(final Particle p) {
        this.m_particles[this.m_size++] = p;
    }
    
    public final void remove(final int index) {
        this.m_particles[index] = null;
        ++this.m_removedCount;
    }
    
    public final Particle get(final int index) {
        return this.m_particles[index];
    }
    
    public final int size() {
        return this.m_size;
    }
    
    public final void compact() {
        if (this.m_removedCount == 0) {
            return;
        }
        int k = 1;
        for (int i = 0; i < this.m_size - 1; ++i) {
            if (this.m_particles[i] != null) {
                ++k;
            }
            else {
                for (int j = k; j < this.m_size; ++j) {
                    if (this.m_particles[j] != null) {
                        this.m_particles[i] = this.m_particles[j];
                        this.m_particles[j] = null;
                        k = j + 1;
                        break;
                    }
                }
            }
        }
        this.m_size -= this.m_removedCount;
        this.m_removedCount = 0;
    }
    
    public final void clear() {
        this.m_size = 0;
        this.m_removedCount = 0;
    }
    
    public final boolean isFull() {
        return this.m_size >= this.m_particles.length;
    }
}
