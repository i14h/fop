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

package org.apache.fop.layoutengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * Utility class for layout engine tests.
 */
public final class LayoutEngineTestUtils {

    private LayoutEngineTestUtils() {
    }

    private static class FilenameHandler extends DefaultHandler {
        private StringBuffer buffer = new StringBuffer(128);
        private boolean readingFilename = false;
        private List filenames;

        public FilenameHandler(List filenames) {
            this.filenames = filenames;
        }

        public void startElement(String namespaceURI, String localName, String qName,
                Attributes atts) throws SAXException {
            if (qName != null && qName.equals("file")) {
                buffer.setLength(0);
                readingFilename = true;
            } else {
                throw new RuntimeException(
                        "Unexpected element while reading disabled testcase file names: " + qName);
            }
        }

        public void endElement(String namespaceURI, String localName, String qName)
                throws SAXException {
            if (qName != null && qName.equals("file")) {
                readingFilename = false;
                filenames.add(buffer.toString());
            } else {
                throw new RuntimeException(
                        "Unexpected element while reading disabled testcase file names: " + qName);
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (readingFilename) {
                buffer.append(ch, start, length);
            }
        }
    }

    public static IOFileFilter decorateWithDisabledList(IOFileFilter filter) throws IOException {
        String disabled = System.getProperty("fop.layoutengine.disabled");
        if (disabled != null && disabled.length() > 0) {
            filter = new AndFileFilter(new NotFileFilter(
                    new NameFileFilter(LayoutEngineTestUtils.readDisabledTestcases(new File(
                            disabled)))),
                    filter);
        }
        return filter;
    }

    public static String[] readDisabledTestcases(File f) throws IOException {
        List lines = new java.util.ArrayList();
        Source stylesheet = new StreamSource(
                new File("test/layoutengine/disabled-testcase2filename.xsl"));
        Source source = new StreamSource(f);
        Result result = new SAXResult(new FilenameHandler(lines));
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesheet);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            throw new RuntimeException(tce.getMessage());
        } catch (TransformerException te) {
            throw new RuntimeException(te.getMessage());
        }
        return (String[]) lines.toArray(new String[lines.size()]);
    }

    /**
     * @return a Collection of File instances containing all the test cases set up for processing.
     * @throws IOException if there's a problem gathering the list of test files
     */
    public static Collection<File[]> getTestFiles() throws IOException {
        File mainDir = new File("test/layoutengine");
        IOFileFilter filter;
        String single = System.getProperty("fop.layoutengine.single");
        String startsWith = System.getProperty("fop.layoutengine.starts-with");
        if (single != null) {
            filter = new NameFileFilter(single);
        } else if (startsWith != null) {
            filter = new PrefixFileFilter(startsWith);
            filter = new AndFileFilter(filter, new SuffixFileFilter(".xml"));
            filter = decorateWithDisabledList(filter);
        } else {
            filter = new SuffixFileFilter(".xml");
            filter = decorateWithDisabledList(filter);
        }
        String testset = System.getProperty("fop.layoutengine.testset");
        if (testset == null) {
            testset = "standard";
        }
        Collection<File> files = FileUtils.listFiles(new File(mainDir, testset + "-testcases"),
                filter, TrueFileFilter.INSTANCE);
        String privateTests = System.getProperty("fop.layoutengine.private");
        if ("true".equalsIgnoreCase(privateTests)) {
            Collection privateFiles = FileUtils.listFiles(
                    new File(mainDir, "private-testcases"),
                    filter, TrueFileFilter.INSTANCE);
            files.addAll(privateFiles);
        }

        Collection<File[]> parametersForJUnit4 = new ArrayList<File[]>();
        for (File f : files) {
            parametersForJUnit4.add(new File[] { f });
        }

        return parametersForJUnit4;
    }

}
