/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.area;

import java.util.List;

import org.apache.fop.fo.pagination.RegionBody;
import org.apache.fop.traits.WritingModeTraitsGetter;

/**
 * This class is a container for the areas that may be generated by
 * an fo:region-body.  It extends the RegionReference that is used
 * directly by the other region classes.
 * See fo:region-body definition in the XSL Rec for more information.
 */
public class BodyRegion extends RegionReference {

    private static final long serialVersionUID = -1848872997724078080L;

    private BeforeFloat beforeFloat;  // optional
    private MainReference mainReference; // mandatory
    private Footnote footnote; // optional
    private int columnGap;
    private int columnCount;

    /**
     * Constructor which can read traits directly
     * from an fo:region-body formatting object.
     * @param rb the region-body FO node
     * @param parent the parent region viewport
     */
    public BodyRegion(RegionBody rb, RegionViewport parent) {
        this(rb.getNameId(), rb.getRegionName(), parent, rb.getColumnCount(), rb.getColumnGap());
    }

    /**
     * Constructor which can read traits directly
     * from an fo:region-body formatting object.
     * @param regionClass the region class (as returned by Region.getNameId())
     * @param regionName the name of the region (as returned by Region.getRegionName())
     * @param parent the parent region viewport
     * @param columnCount the number of columns
     * @param columnGap the gap between columns
     */
    public BodyRegion(int regionClass, String regionName, RegionViewport parent,
            int columnCount, int columnGap) {
        super(regionClass, regionName, parent);
        this.columnCount = columnCount;
        this.columnGap = columnGap;
        mainReference = new MainReference(this);
    }

    /**
     * Get the number of columns when not spanning
     *
     * @return the number of columns
     */
    public int getColumnCount() {
        return this.columnCount;
    }

    /** @return the column-gap value */
    public int getColumnGap() {
        return this.columnGap;
    }

    /**
     * Get the main reference area.
     *
     * @return the main reference area
     */
    public MainReference getMainReference() {
        return mainReference;
    }

    /**
     * indicates whether the main reference area has any child areas added to it
     *
     * @return whether the main reference area has any child areas added to it
     */
    public boolean isEmpty() {
        return (mainReference == null || mainReference.isEmpty())
               && (footnote == null || footnote.isEmpty())
               && (beforeFloat == null || beforeFloat.isEmpty());
    }


    /**
     * Get the before float area.
     *
     * @return the before float area
     */
    public BeforeFloat getBeforeFloat() {
        if (beforeFloat == null) {
            beforeFloat = new BeforeFloat();
        }
        return beforeFloat;
    }

    /**
     * Get the footnote area.
     *
     * @return the footnote area
     */
    public Footnote getFootnote() {
        if (footnote == null) {
            footnote = new Footnote();
        }
        return footnote;
    }

    /**
     * @return the available BPD in the main reference area after the previous span reference
     * areas are subtracted.
     */
    public int getRemainingBPD() {
        int usedBPD = 0;
        List spans = getMainReference().getSpans();
        int previousSpanCount = spans.size() - 1;
        for (int i = 0; i < previousSpanCount; i++) {
            usedBPD += ((Span)spans.get(i)).getHeight();
        }
        return getBPD() - usedBPD;
    }

    /**
     * Sets the writing mode traits for the main reference area of
     * this body region area.
     * @param wmtg a WM traits getter
     */
    public void setWritingModeTraits(WritingModeTraitsGetter wmtg) {
        if ( getMainReference() != null ) {
            getMainReference().setWritingModeTraits ( wmtg );
        }
    }

    /**
     * Clone this object.
     *
     * @return a shallow copy of this object
     */
    public Object clone() {
        BodyRegion br = new BodyRegion(getRegionClass(), getRegionName(), regionViewport,
                getColumnCount(), getColumnGap());
        br.setCTM(getCTM());
        br.setIPD(getIPD());
        br.beforeFloat = beforeFloat;
        br.mainReference = mainReference;
        br.footnote = footnote;
        return br;
    }
}
