/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.xslf.usermodel;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.util.Dimension2DDouble;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Units;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode;

@Internal
public class XSLFTexturePaint implements PaintStyle.TexturePaint {
    private final CTBlipFillProperties blipFill;
    private final PackagePart parentPart;
    private final CTBlip blip;
    private final CTSchemeColor phClr;
    private final XSLFTheme theme;
    private final XSLFSheet sheet;

    public XSLFTexturePaint(final CTBlipFillProperties blipFill, final PackagePart parentPart, CTSchemeColor phClr, final XSLFTheme theme, final XSLFSheet sheet) {
        this.blipFill = blipFill;
        this.parentPart = parentPart;
        blip = blipFill.getBlip();
        this.phClr = phClr;
        this.theme = theme;
        this.sheet = sheet;
    }


    private PackagePart getPart() {
        try {
            String blipId = blip.getEmbed();
            PackageRelationship rel = parentPart.getRelationship(blipId);
            return parentPart.getRelatedPart(rel);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getImageData() {
        try {
            return getPart().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContentType() {
        if (blip == null || !blip.isSetEmbed() || blip.getEmbed().isEmpty()) {
            return null;
        }
        /* TOOD: map content-type */
        return getPart().getContentType();
    }

    @Override
    public int getAlpha() {
        return (blip.sizeOfAlphaModFixArray() > 0)
                ? blip.getAlphaModFixArray(0).getAmt()
                : 100000;
    }

    @Override
    public boolean isRotatedWithShape() {
        return blipFill.isSetRotWithShape() && blipFill.getRotWithShape();
    }

    @Override
    public Dimension2D getScale() {
        CTTileInfoProperties tile = blipFill.getTile();
        return (tile == null) ? null : new Dimension2DDouble(
                tile.isSetSx() ? tile.getSx()/100_000. : 1,
                tile.isSetSy() ? tile.getSy()/100_000. : 1);
    }

    @Override
    public Point2D getOffset() {
        CTTileInfoProperties tile = blipFill.getTile();
        return (tile == null) ? null : new Point2D.Double(
                tile.isSetTx() ? Units.toPoints(tile.getTx()) : 0,
                tile.isSetTy() ? Units.toPoints(tile.getTy()) : 0);
    }

    @Override
    public PaintStyle.FlipMode getFlipMode() {
        CTTileInfoProperties tile = blipFill.getTile();
        switch (tile == null || tile.getFlip() == null ? STTileFlipMode.INT_NONE : tile.getFlip().intValue()) {
            default:
            case STTileFlipMode.INT_NONE:
                return PaintStyle.FlipMode.NONE;
            case STTileFlipMode.INT_X:
                return PaintStyle.FlipMode.X;
            case STTileFlipMode.INT_Y:
                return PaintStyle.FlipMode.Y;
            case STTileFlipMode.INT_XY:
                return PaintStyle.FlipMode.XY;
        }
    }

    @Override
    public PaintStyle.TextureAlignment getAlignment() {
        CTTileInfoProperties tile = blipFill.getTile();
        return (tile == null || !tile.isSetAlgn()) ? null
                : PaintStyle.TextureAlignment.fromOoxmlId(tile.getAlgn().toString());
    }

    @Override
    public Insets2D getInsets() {
        return getRectVal(blipFill.getSrcRect());
    }

    @Override
    public Insets2D getStretch() {
        return getRectVal(blipFill.isSetStretch() ? blipFill.getStretch().getFillRect() : null);
    }

    @Override
    public List<ColorStyle> getDuoTone() {
        if (blip.sizeOfDuotoneArray() == 0) {
            return null;
        }
        List<ColorStyle> colors = new ArrayList<>();
        CTDuotoneEffect duoEff = blip.getDuotoneArray(0);
        for (CTSchemeColor phClrDuo : duoEff.getSchemeClrArray()) {
            colors.add(new XSLFColor(phClrDuo, theme, phClr, sheet).getColorStyle());
        }
        return colors;
    }


    private static Insets2D getRectVal(CTRelativeRect rect) {
        return rect == null ? null : new Insets2D(
            getRectVal(rect::isSetT, rect::getT),
            getRectVal(rect::isSetL, rect::getL),
            getRectVal(rect::isSetB, rect::getB),
            getRectVal(rect::isSetR, rect::getR)
        );
    }

    private static int getRectVal(Supplier<Boolean> isSet, Supplier<Integer> val) {
        return isSet.get() ? val.get() : 0;
    }
}
