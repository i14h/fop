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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.fop.apps.FOPException;
import org.apache.fop.complexscripts.fonts.Positionable;
import org.apache.fop.complexscripts.fonts.Substitutable;


import org.xml.sax.InputSource;

/**
 * This class is used to defer the loading of a font until it is really used.
 */
public class LazyFont extends Typeface implements FontDescriptor, Substitutable, Positionable {

    private static Log log = LogFactory.getLog(LazyFont.class);

    private String metricsFileName;
    private String fontEmbedPath;
    private boolean useKerning;
    private boolean useAdvanced;
    private EncodingMode encodingMode = EncodingMode.AUTO;
    private boolean embedded;
    private String subFontName;

    private boolean isMetricsLoaded;
    private Typeface realFont;
    private FontDescriptor realFontDescriptor;

    private FontResolver resolver;

    /**
     * Main constructor
     * @param fontInfo  the font info to embed
     * @param resolver the font resolver to handle font URIs
     */
    public LazyFont(EmbedFontInfo fontInfo, FontResolver resolver) {

        this.metricsFileName = fontInfo.getMetricsFile();
        this.fontEmbedPath = fontInfo.getEmbedFile();
        this.useKerning = fontInfo.getKerning();
        if ( resolver != null ) {
            this.useAdvanced = resolver.isComplexScriptFeaturesEnabled();
        } else {
            this.useAdvanced = fontInfo.getAdvanced();
        }
        this.encodingMode = fontInfo.getEncodingMode();
        this.subFontName = fontInfo.getSubFontName();
        this.embedded = fontInfo.isEmbedded();
        this.resolver = resolver;
    }

    /** {@inheritDoc} */
    public String toString() {
        StringBuffer sbuf = new StringBuffer(super.toString());
        sbuf.append('{');
        sbuf.append("metrics-url=" + metricsFileName);
        sbuf.append(",embed-url=" + fontEmbedPath);
        sbuf.append(",kerning=" + useKerning);
        sbuf.append(",advanced=" + useAdvanced);
        sbuf.append('}');
        return sbuf.toString();
    }   

    private void load(boolean fail) {
        if (!isMetricsLoaded) {
            try {
                if (metricsFileName != null) {
                    /**@todo Possible thread problem here */
                    FontReader reader = null;
                    if (resolver != null) {
                        Source source = resolver.resolve(metricsFileName);
                        if (source == null) {
                            String err
                                = "Cannot load font: failed to create Source from metrics file "
                                    + metricsFileName;
                            if (fail) {
                                throw new RuntimeException(err);
                            } else {
                                log.error(err);
                            }
                            return;
                        }
                        InputStream in = null;
                        if (source instanceof StreamSource) {
                            in = ((StreamSource) source).getInputStream();
                        }
                        if (in == null && source.getSystemId() != null) {
                            in = new java.net.URL(source.getSystemId()).openStream();
                        }
                        if (in == null) {
                            String err = "Cannot load font: After URI resolution, the returned"
                                + " Source object does not contain an InputStream"
                                + " or a valid URL (system identifier) for metrics file: "
                                + metricsFileName;
                            if (fail) {
                                throw new RuntimeException(err);
                            } else {
                                log.error(err);
                            }
                            return;
                        }
                        InputSource src = new InputSource(in);
                        src.setSystemId(source.getSystemId());
                        reader = new FontReader(src);
                    } else {
                        reader = new FontReader(new InputSource(
                                    new URL(metricsFileName).openStream()));
                    }
                    reader.setKerningEnabled(useKerning);
                    reader.setAdvancedEnabled(useAdvanced);
                    if (this.embedded) {
                        reader.setFontEmbedPath(fontEmbedPath);
                    }
                    reader.setResolver(resolver);
                    realFont = reader.getFont();
                } else {
                    if (fontEmbedPath == null) {
                        throw new RuntimeException("Cannot load font. No font URIs available.");
                    }
                    realFont = FontLoader.loadFont(fontEmbedPath, this.subFontName,
                            this.embedded, this.encodingMode, useKerning, useAdvanced, resolver);
                }
                if (realFont instanceof FontDescriptor) {
                    realFontDescriptor = (FontDescriptor) realFont;
                }
            } catch (FOPException fopex) {
                log.error("Failed to read font metrics file " + metricsFileName, fopex);
                if (fail) {
                    throw new RuntimeException(fopex.getMessage());
                }
            } catch (IOException ioex) {
                log.error("Failed to read font metrics file " + metricsFileName, ioex);
                if (fail) {
                    throw new RuntimeException(ioex.getMessage());
                }
            }
            realFont.setEventListener(this.eventListener);
            isMetricsLoaded = true;
        }
    }

    /**
     * Gets the real font.
     * @return the real font
     */
    public Typeface getRealFont() {
        load(false);
        return realFont;
    }

    // ---- Font ----
    /** {@inheritDoc} */
    public String getEncodingName() {
        load(true);
        return realFont.getEncodingName();
    }

    /**
     * {@inheritDoc}
     */
    public char mapChar(char c) {
        load(true);
        return realFont.mapChar(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hadMappingOperations() {
        load(true);
        return realFont.hadMappingOperations();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChar(char c) {
        load(true);
        return realFont.hasChar(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMultiByte() {
        load(true);
        return realFont.isMultiByte();
    }

    // ---- FontMetrics interface ----
    /** {@inheritDoc} */
    public String getFontName() {
        load(true);
        return realFont.getFontName();
    }

    /** {@inheritDoc} */
    public String getEmbedFontName() {
        load(true);
        return realFont.getEmbedFontName();
    }

    /** {@inheritDoc} */
    public String getFullName() {
        load(true);
        return realFont.getFullName();
    }

    /** {@inheritDoc} */
    public Set<String> getFamilyNames() {
        load(true);
        return realFont.getFamilyNames();
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxAscent(int size) {
        load(true);
        return realFont.getMaxAscent(size);
    }

    /**
     * {@inheritDoc}
     */
    public int getAscender(int size) {
        load(true);
        return realFont.getAscender(size);
    }

    /**
     * {@inheritDoc}
     */
    public int getCapHeight(int size) {
        load(true);
        return realFont.getCapHeight(size);
    }

    /**
     * {@inheritDoc}
     */
    public int getDescender(int size) {
        load(true);
        return realFont.getDescender(size);
    }

    /**
     * {@inheritDoc}
     */
    public int getXHeight(int size) {
        load(true);
        return realFont.getXHeight(size);
    }

    /**
     * {@inheritDoc}
     */
    public int getWidth(int i, int size) {
        load(true);
        return realFont.getWidth(i, size);
    }

    /**
     * {@inheritDoc}
     */
    public int[] getWidths() {
        load(true);
        return realFont.getWidths();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasKerningInfo() {
        load(true);
        return realFont.hasKerningInfo();
    }

    /**
     * {@inheritDoc}
     */
    public Map<Integer, Map<Integer, Integer>> getKerningInfo() {
        load(true);
        return realFont.getKerningInfo();
    }

    // ---- FontDescriptor interface ----
    /**
     * {@inheritDoc}
     */
    public int getCapHeight() {
        load(true);
        return realFontDescriptor.getCapHeight();
    }

    /**
     * {@inheritDoc}
     */
    public int getDescender() {
        load(true);
        return realFontDescriptor.getDescender();
    }

    /**
     * {@inheritDoc}
     */
    public int getAscender() {
        load(true);
        return realFontDescriptor.getAscender();
    }

    /** {@inheritDoc} */
    public int getFlags() {
        load(true);
        return realFontDescriptor.getFlags();
    }

    /** {@inheritDoc} */
    public boolean isSymbolicFont() {
        load(true);
        return realFontDescriptor.isSymbolicFont();
    }

    /**
     * {@inheritDoc}
     */
    public int[] getFontBBox() {
        load(true);
        return realFontDescriptor.getFontBBox();
    }

    /**
     * {@inheritDoc}
     */
    public int getItalicAngle() {
        load(true);
        return realFontDescriptor.getItalicAngle();
    }

    /**
     * {@inheritDoc}
     */
    public int getStemV() {
        load(true);
        return realFontDescriptor.getStemV();
    }

    /**
     * {@inheritDoc}
     */
    public FontType getFontType() {
        load(true);
        return realFontDescriptor.getFontType();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmbeddable() {
        load(true);
        return realFontDescriptor.isEmbeddable();
    }

    /**
     * {@inheritDoc}
     */
    public boolean performsSubstitution() {
        load(true);
        if ( realFontDescriptor instanceof Substitutable ) {
            return ((Substitutable)realFontDescriptor).performsSubstitution();
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public CharSequence performSubstitution ( CharSequence cs, String script, String language ) {
        load(true);
        if ( realFontDescriptor instanceof Substitutable ) {
            return ((Substitutable)realFontDescriptor).performSubstitution(cs, script, language);
        } else {
            return cs;
        }
    }

    /**
     * {@inheritDoc}
     */
    public CharSequence reorderCombiningMarks
        ( CharSequence cs, int[][] gpa, String script, String language ) {
        load(true);
        if ( realFontDescriptor instanceof Substitutable ) {
            return ((Substitutable)realFontDescriptor).
                reorderCombiningMarks(cs, gpa, script, language);
        } else {
            return cs;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean performsPositioning() {
        load(true);
        if ( realFontDescriptor instanceof Positionable ) {
            return ((Positionable)realFontDescriptor).performsPositioning();
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int[][]
        performPositioning ( CharSequence cs, String script, String language, int fontSize ) {
        load(true);
        if ( realFontDescriptor instanceof Positionable ) {
            return ((Positionable)realFontDescriptor)
                .performPositioning(cs, script, language, fontSize);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int[][]
        performPositioning ( CharSequence cs, String script, String language ) {
        load(true);
        if ( realFontDescriptor instanceof Positionable ) {
            return ((Positionable)realFontDescriptor)
                .performPositioning(cs, script, language);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSubsetEmbedded() {
        load(true);
        return realFont.isMultiByte();
    }

}

