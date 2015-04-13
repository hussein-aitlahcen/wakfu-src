package com.ankamagames.baseImpl.common.clientAndServer.game.los;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class LineOfSightChecker implements Releasable
{
    private static final Logger m_logger;
    private static final Logger m_debugLogger;
    private static final boolean DEBUG_LOS = false;
    private TopologyMapInstanceSet m_topologyMapInstanceSet;
    private int m_startX;
    private int m_startY;
    private short m_startZ;
    private int m_endX;
    private int m_endY;
    private short m_endZ;
    private int m_xToCheck;
    private int m_yToCheck;
    private short m_zMinToCheck;
    private short m_zMaxToCheck;
    private boolean m_checkIfLastCellBlocked;
    private final CellVisibilityData[] m_cellVisibilityData;
    private static final ObjectPool m_staticPool;
    private LOSCheckerListener m_listener;
    
    private LineOfSightChecker() {
        super();
        this.m_checkIfLastCellBlocked = false;
        this.m_cellVisibilityData = new CellVisibilityData[32];
        this.m_listener = null;
        for (int i = 0; i < this.m_cellVisibilityData.length; ++i) {
            this.m_cellVisibilityData[i] = new CellVisibilityData();
        }
    }
    
    public static LineOfSightChecker checkOut() {
        try {
            return (LineOfSightChecker)LineOfSightChecker.m_staticPool.borrowObject();
        }
        catch (Exception e) {
            LineOfSightChecker.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    public final void release() {
        try {
            LineOfSightChecker.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            LineOfSightChecker.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    @Override
    public final void onCheckOut() {
    }
    
    @Override
    public final void onCheckIn() {
        this.m_topologyMapInstanceSet = null;
    }
    
    public final void setTopologyMapInstanceSet(final TopologyMapInstanceSet topologyMapInstanceSet) {
        this.m_topologyMapInstanceSet = topologyMapInstanceSet;
    }
    
    public final void setStartPoint(final int x, final int y, final short z) {
        this.m_startX = x;
        this.m_startY = y;
        this.m_startZ = z;
    }
    
    public final void setStartPoint(final Point3 start) {
        this.m_startX = start.getX();
        this.m_startY = start.getY();
        this.m_startZ = start.getZ();
    }
    
    public final void setEndPoint(final int x, final int y, final short z) {
        this.m_endX = x;
        this.m_endY = y;
        this.m_endZ = z;
    }
    
    public final void setEndPoint(final Point3 end) {
        this.m_endX = end.getX();
        this.m_endY = end.getY();
        this.m_endZ = end.getZ();
    }
    
    private boolean flushCellsChecks() {
        final TopologyMap map = this.m_topologyMapInstanceSet.getTopologyMapFromCell(this.m_xToCheck, this.m_yToCheck);
        if (map == null) {
            return false;
        }
        final boolean passBlockTest = (!this.m_checkIfLastCellBlocked && this.m_xToCheck == this.m_endX && this.m_yToCheck == this.m_endY) || (this.m_xToCheck == this.m_startX && this.m_yToCheck == this.m_startY);
        if (!passBlockTest && this.isSightBlocked()) {
            return false;
        }
        final int count = map.getVisibilityData(this.m_xToCheck, this.m_yToCheck, this.m_cellVisibilityData, 0);
        if (count <= 0) {
            return false;
        }
        for (int i = 0; i < count; ++i) {
            final CellVisibilityData data = this.m_cellVisibilityData[i];
            if (data.m_z > this.m_zMinToCheck) {
                if (data.m_z - data.m_height >= this.m_zMaxToCheck) {
                    return i > 0;
                }
                if (this.m_zMaxToCheck > data.m_z - data.m_height && this.m_zMinToCheck < data.m_z && !data.m_hollow) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isSightBlocked() {
        return this.m_topologyMapInstanceSet.isSightBlocked(this.m_xToCheck, this.m_yToCheck, this.m_zMinToCheck) || this.m_topologyMapInstanceSet.isSightBlocked(this.m_xToCheck, this.m_yToCheck, this.m_zMaxToCheck);
    }
    
    private boolean checkCell(final int x, final int y, final short z) {
        if (this.m_xToCheck == Integer.MAX_VALUE) {
            this.m_xToCheck = x;
            this.m_yToCheck = y;
            this.m_zMinToCheck = z;
            this.m_zMaxToCheck = z;
            return true;
        }
        if (x == this.m_xToCheck && y == this.m_yToCheck) {
            if (z < this.m_zMinToCheck) {
                this.m_zMinToCheck = z;
            }
            if (z > this.m_zMaxToCheck) {
                this.m_zMaxToCheck = z;
            }
            return true;
        }
        if (!this.flushCellsChecks()) {
            return false;
        }
        this.m_xToCheck = x;
        this.m_yToCheck = y;
        this.m_zMinToCheck = z;
        this.m_zMaxToCheck = z;
        return true;
    }
    
    public final boolean checkLOS() {
        assert this.m_topologyMapInstanceSet != null : "No TopologyMapInstanceSet defined for this LOS Check";
        this.m_xToCheck = Integer.MAX_VALUE;
        this.m_yToCheck = Integer.MAX_VALUE;
        this.m_zMinToCheck = 32767;
        this.m_zMaxToCheck = -32768;
        int pointX = this.m_startX;
        int pointY = this.m_startY;
        short pointZ = this.m_startZ;
        final int dx = this.m_endX - this.m_startX;
        final int dy = this.m_endY - this.m_startY;
        final short dz = (short)(this.m_endZ - this.m_startZ);
        int x_inc;
        int l;
        if (dx < 0) {
            x_inc = -1;
            l = -dx;
        }
        else {
            x_inc = 1;
            l = dx;
        }
        int y_inc;
        int m;
        if (dy < 0) {
            y_inc = -1;
            m = -dy;
        }
        else {
            y_inc = 1;
            m = dy;
        }
        int z_inc;
        int n;
        if (dz < 0) {
            z_inc = -1;
            n = -dz;
        }
        else {
            z_inc = 1;
            n = dz;
        }
        final int dx2 = l << 2;
        final int dy2 = m << 2;
        final int dz2 = n << 2;
        if (l >= m && l >= n) {
            int err_1 = (dy2 >>> 1) - 2 * l;
            int err_2 = (dz2 >>> 1) - 2 * l;
            for (int i = 0; i < l; ++i) {
                pointX += x_inc;
                if (err_1 < 0) {
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        err_2 -= dx2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dx2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX - x_inc, pointY, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                else if (err_1 == 0) {
                    err_1 -= dx2;
                    pointY += y_inc;
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        err_2 -= dx2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dx2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX - x_inc, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                else {
                    err_1 -= dx2;
                    pointY += y_inc;
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX - x_inc, pointY, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        if (!this.checkCell(pointX - x_inc, pointY, pointZ)) {
                            return false;
                        }
                        err_2 -= dx2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dx2;
                        pointZ += (short)z_inc;
                        final int avgY = err_1 + err_1 - dy2;
                        final int avgZ = err_2 + err_2 - dz2;
                        if (avgY > avgZ) {
                            if (!this.checkCell(pointX - x_inc, pointY, (short)(pointZ - z_inc))) {
                                return false;
                            }
                        }
                        else if (avgZ > avgY && !this.checkCell(pointX - x_inc, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX - x_inc, pointY, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                err_1 += dy2;
                err_2 += dz2;
            }
        }
        else if (m >= l && m >= n) {
            int err_1 = (dx2 >>> 1) - 2 * m;
            int err_2 = (dz2 >>> 1) - 2 * m;
            for (int i = 0; i < m; ++i) {
                pointY += y_inc;
                if (err_1 < 0) {
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        err_2 -= dy2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dy2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                else if (err_1 == 0) {
                    err_1 -= dy2;
                    pointX += x_inc;
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        err_2 -= dy2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dy2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX - x_inc, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                else {
                    err_1 -= dy2;
                    pointX += x_inc;
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        if (!this.checkCell(pointX, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        err_2 -= dy2;
                        pointZ += (short)z_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dy2;
                        pointZ += (short)z_inc;
                        final int avgX = err_1 + err_1 - dx2;
                        final int avgZ = err_2 + err_2 - dz2;
                        if (avgX > avgZ) {
                            if (!this.checkCell(pointX, pointY - y_inc, (short)(pointZ - z_inc))) {
                                return false;
                            }
                        }
                        else if (avgZ > avgX && !this.checkCell(pointX - x_inc, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY - y_inc, pointZ)) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                err_1 += dx2;
                err_2 += dz2;
            }
        }
        else {
            int err_1 = (dx2 >>> 1) - 2 * n;
            int err_2 = (dy2 >>> 1) - 2 * n;
            for (int i = 0; i < n; ++i) {
                pointZ += (short)z_inc;
                if (err_1 < 0) {
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        err_2 -= dz2;
                        pointY += y_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dz2;
                        pointY += y_inc;
                        if (!this.checkCell(pointX, pointY, (short)(pointZ - z_inc))) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                else if (err_1 == 0) {
                    err_1 -= dz2;
                    pointX += x_inc;
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        err_2 -= dz2;
                        pointY += y_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dz2;
                        pointY += y_inc;
                        if (!this.checkCell(pointX - x_inc, pointY, (short)(pointZ - z_inc))) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                else {
                    err_1 -= dz2;
                    pointX += x_inc;
                    if (err_2 < 0) {
                        if (!this.checkCell(pointX, pointY, (short)(pointZ - z_inc))) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else if (err_2 == 0) {
                        if (!this.checkCell(pointX, pointY, (short)(pointZ - z_inc))) {
                            return false;
                        }
                        err_2 -= dz2;
                        pointY += y_inc;
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                    else {
                        err_2 -= dz2;
                        pointY += y_inc;
                        final int avgX = err_1 + err_1 - dx2;
                        final int avgY2 = err_2 + err_2 - dy2;
                        if (avgX > avgY2) {
                            if (!this.checkCell(pointX, pointY - y_inc, (short)(pointZ - z_inc))) {
                                return false;
                            }
                        }
                        else if (avgY2 > avgX && !this.checkCell(pointX - x_inc, pointY, (short)(pointZ - z_inc))) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, (short)(pointZ - z_inc))) {
                            return false;
                        }
                        if (!this.checkCell(pointX, pointY, pointZ)) {
                            return false;
                        }
                    }
                }
                err_1 += dx2;
                err_2 += dy2;
            }
        }
        return this.flushCellsChecks();
    }
    
    public void setListener(final LOSCheckerListener listener) {
        this.m_listener = listener;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LineOfSightChecker.class);
        (m_debugLogger = Logger.getLogger("debug")).setLevel(Level.ALL);
        m_staticPool = new MonitoredPool(new ObjectFactory<LineOfSightChecker>() {
            @Override
            public LineOfSightChecker makeObject() {
                return new LineOfSightChecker(null);
            }
        });
    }
    
    public interface LOSCheckerListener
    {
        void onCellTested(int p0, int p1, short p2);
    }
}
