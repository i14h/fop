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

package org.apache.fop.complexscripts.fonts;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.fop.complexscripts.gdef.GDEFTestCase;
import org.apache.fop.complexscripts.gpos.GPOSTestCase;
import org.apache.fop.complexscripts.gsub.GSUBTestCase;
import org.apache.fop.complexscripts.util.TTXFileTestCase;

/**
 * Test suite for fonts functionality related to complex scripts.
 */
public class FontsTestSuite {

    /**
     * Builds the test suite
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Basic functionality test suite for complex scripts fonts related functionality");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(TTXFileTestCase.class));
        //suite.addTest(new TestSuite(GDEFTestCase.class));
        suite.addTest(new TestSuite(GSUBTestCase.class));
        //suite.addTest(new TestSuite(GPOSTestCase.class));
        //$JUnit-END$
        return suite;
    }

}
