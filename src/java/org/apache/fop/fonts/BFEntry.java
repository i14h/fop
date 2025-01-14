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

package org.apache.fop.fonts;

/**
 * This is just a holder class for bfentries, groups of characters of a base font (bf).
 */
public class BFEntry {

    private int unicodeStart;
    private int unicodeEnd;
    private int glyphStartIndex;

    /**
     * Main constructor.
     * @param unicodeStart Unicode start index
     * @param unicodeEnd Unicode end index
     * @param glyphStartIndex glyph start index
     */
    public BFEntry(int unicodeStart, int unicodeEnd, int glyphStartIndex) {
        this.unicodeStart = unicodeStart;
        this.unicodeEnd = unicodeEnd;
        this.glyphStartIndex = glyphStartIndex;
    }

    /**
     * Returns the unicodeStart.
     * @return the Unicode start index
     */
    public int getUnicodeStart() {
        return unicodeStart;
    }

    /**
     * Returns the unicodeEnd.
     * @return the Unicode end index
     */
    public int getUnicodeEnd() {
        return unicodeEnd;
    }

    /**
     * Returns the glyphStartIndex.
     * @return the glyph start index
     */
    public int getGlyphStartIndex() {
        return glyphStartIndex;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BFEntry: ");
        sb.append ( "{ UC[" );
        sb.append ( unicodeStart );
        sb.append ( ',' );
        sb.append ( unicodeEnd );
        sb.append ( "]: GC[" );
        sb.append ( glyphStartIndex );
        sb.append ( ',' );
        sb.append ( glyphStartIndex + ( unicodeEnd - unicodeStart ) );
        sb.append ( "] }" );
        return sb.toString();
    }

}
