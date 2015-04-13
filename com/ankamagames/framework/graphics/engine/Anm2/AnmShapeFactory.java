package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

final class AnmShapeFactory
{
    public static AnmShape createShape(final ExtendedDataInputStream bitStream) throws IOException {
        final short id = bitStream.readShort();
        final byte dataDescriptor = bitStream.readByte();
        AnmShape shape = null;
        switch (dataDescriptor) {
            case 0: {
                shape = new AnmShape();
                break;
            }
            case 1: {
                shape = new AnmShapeR();
                break;
            }
            case 2: {
                shape = new AnmShapeT();
                break;
            }
            case 3: {
                shape = new AnmShapeRT();
                break;
            }
            case 4: {
                shape = new AnmShapeA();
                break;
            }
            case 5: {
                shape = new AnmShapeRA();
                break;
            }
            case 6: {
                shape = new AnmShapeTA();
                break;
            }
            case 7: {
                shape = new AnmShapeRTA();
                break;
            }
            case 8: {
                shape = new AnmShapeM();
                break;
            }
            case 9: {
                shape = new AnmShapeRM();
                break;
            }
            case 10: {
                shape = new AnmShapeTM();
                break;
            }
            case 12: {
                shape = new AnmShapeAM();
                break;
            }
            case 11: {
                shape = new AnmShapeRTM();
                break;
            }
            case 13: {
                shape = new AnmShapeRAM();
                break;
            }
            case 14: {
                shape = new AnmShapeTAM();
                break;
            }
            case 15: {
                shape = new AnmShapeRTAM();
                break;
            }
            case 49: {
                shape = new AnmShapeCR();
                break;
            }
            case 82: {
                shape = new AnmShapeCT();
                break;
            }
            case -77: {
                shape = new AnmShapeCRT();
                break;
            }
            default: {
                assert false : "shape type unsupported";
                shape = null;
                break;
            }
        }
        shape.load(bitStream);
        shape.setId(id);
        return shape;
    }
}
