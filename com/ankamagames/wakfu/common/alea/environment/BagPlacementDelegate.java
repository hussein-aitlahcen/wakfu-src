package com.ankamagames.wakfu.common.alea.environment;

import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import java.util.*;

public abstract class BagPlacementDelegate
{
    private static final Logger m_logger;
    public static final byte NO_TYPE = 0;
    public static final byte IN_MARKET_TYPE = 1;
    public static final byte IN_WORLD_TYPE = 2;
    
    public static BagPlacementDelegate from(final ExtendedDataInputStream istream) throws IOException {
        final byte type = istream.readByte();
        switch (type) {
            case 2: {
                return new InWorld(istream);
            }
            case 1: {
                return new InMarket(istream);
            }
            default: {
                return null;
            }
        }
    }
    
    public static void save(final OutputBitStream ostream, final BagPlacementDelegate bagPlacement) throws IOException {
        if (bagPlacement == null) {
            ostream.writeByte((byte)0);
            return;
        }
        ostream.writeByte((byte)(bagPlacement.isMarket() ? 1 : 2));
        bagPlacement.save(ostream);
    }
    
    public static BagPlacementDelegate duplicateFrom(final BagPlacementDelegate bp) {
        if (bp == null) {
            return null;
        }
        BagPlacementDelegate b = null;
        if (bp.isMarket()) {
            b = new InMarket();
        }
        else {
            b = new InWorld();
        }
        b.duplicate(bp);
        return b;
    }
    
    public static boolean isMarket(final BagPlacementDelegate bagPlacement) {
        return bagPlacement != null && bagPlacement.isMarket();
    }
    
    public abstract boolean isMarket();
    
    public abstract void clear();
    
    public abstract void load(final ExtendedDataInputStream p0) throws IOException;
    
    public abstract void save(final OutputBitStream p0) throws IOException;
    
    protected abstract void duplicate(final BagPlacementDelegate p0);
    
    static {
        m_logger = Logger.getLogger((Class)BagPlacementDelegate.class);
    }
    
    public static final class InWorld extends BagPlacementDelegate
    {
        SterylCells m_havroSteryl;
        
        public InWorld(final SterylCells cells) {
            super();
            this.m_havroSteryl = null;
            this.m_havroSteryl = cells;
        }
        
        private InWorld() {
            super();
            this.m_havroSteryl = null;
        }
        
        private InWorld(final ExtendedDataInputStream istream) throws IOException {
            super();
            this.m_havroSteryl = null;
            this.load(istream);
        }
        
        public SterylCells getHavroSteryl() {
            return this.m_havroSteryl;
        }
        
        @Override
        public boolean isMarket() {
            return false;
        }
        
        @Override
        public void clear() {
            this.m_havroSteryl = null;
        }
        
        @Override
        public final void load(final ExtendedDataInputStream istream) throws IOException {
            this.m_havroSteryl = SterylCells.fromStream(istream);
        }
        
        @Override
        public void save(final OutputBitStream ostream) throws IOException {
            SterylCells.save(this.m_havroSteryl, ostream);
        }
        
        @Override
        protected final void duplicate(final BagPlacementDelegate bp) {
            final InWorld inworld = (InWorld)bp;
            this.clear();
            if (inworld.m_havroSteryl != null) {
                this.m_havroSteryl = new SterylCells(inworld.m_havroSteryl);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof InWorld)) {
                return false;
            }
            final InWorld inWorld = (InWorld)o;
            if (this.m_havroSteryl != null) {
                if (this.m_havroSteryl.equals(inWorld.m_havroSteryl)) {
                    return true;
                }
            }
            else if (inWorld.m_havroSteryl == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (this.m_havroSteryl != null) ? this.m_havroSteryl.hashCode() : 0;
        }
    }
    
    public static final class InMarket extends BagPlacementDelegate
    {
        PlacementDef[] m_placementDefs;
        
        public InMarket(final PlacementDef[] defs) {
            super();
            this.m_placementDefs = null;
            this.m_placementDefs = defs;
        }
        
        public InMarket() {
            super();
            this.m_placementDefs = null;
        }
        
        private InMarket(final ExtendedDataInputStream istream) throws IOException {
            super();
            this.m_placementDefs = null;
            this.load(istream);
        }
        
        @Nullable
        public Direction8 getDirectionAt(final int destX, final int destY, final short z) {
            if (this.m_placementDefs == null) {
                return null;
            }
            for (int i = 0; i < this.m_placementDefs.length; ++i) {
                final PlacementDef def = this.m_placementDefs[i];
                if (def.m_x == destX && def.m_y == destY && def.m_z == z) {
                    return def.m_direction;
                }
            }
            return null;
        }
        
        @Override
        public boolean isMarket() {
            return true;
        }
        
        @Override
        public void clear() {
            this.m_placementDefs = null;
        }
        
        @Override
        public void load(final ExtendedDataInputStream istream) throws IOException {
            final int count = istream.readByte() & 0xFF;
            if (count == 0) {
                this.m_placementDefs = null;
            }
            else {
                this.m_placementDefs = new PlacementDef[count];
                for (int i = 0; i < count; ++i) {
                    this.m_placementDefs[i] = new PlacementDef(istream);
                }
            }
        }
        
        @Override
        public void save(final OutputBitStream ostream) throws IOException {
            if (this.m_placementDefs == null) {
                ostream.writeByte((byte)0);
            }
            else {
                ostream.writeByte((byte)this.m_placementDefs.length);
                for (int i = 0; i < this.m_placementDefs.length; ++i) {
                    this.m_placementDefs[i].save(ostream);
                }
            }
        }
        
        @Override
        protected final void duplicate(final BagPlacementDelegate bp) {
            final InMarket inMarket = (InMarket)bp;
            this.clear();
            if (inMarket.m_placementDefs != null) {
                this.m_placementDefs = inMarket.m_placementDefs.clone();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof InMarket)) {
                return false;
            }
            final InMarket inMarket = (InMarket)o;
            return Arrays.equals(this.m_placementDefs, inMarket.m_placementDefs);
        }
        
        @Override
        public int hashCode() {
            return (this.m_placementDefs != null) ? Arrays.hashCode(this.m_placementDefs) : 0;
        }
    }
}
