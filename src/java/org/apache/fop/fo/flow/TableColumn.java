/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.apache.fop.fo.flow;

// XML
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

// FOP
import org.apache.fop.datatypes.ColorType;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;

/**
 * Class modelling the fo:table-column object.
 */
public class TableColumn extends FObj {

    private ColorType backgroundColor;
    private Length columnWidth;
    private int columnOffset;
    private int numColumnsRepeated;
    private int iColumnNumber;

    /**
     * @param parent FONode that is the parent of this object
     */
    public TableColumn(FONode parent) {
        super(parent);
    }

    /**
     * @see org.apache.fop.fo.FONode#validateChildNode(Locator, String, String)
     * XSL Content Model: empty
     */
    protected void validateChildNode(Locator loc, String nsURI, String localName) 
        throws SAXParseException {
            invalidChildError(loc, nsURI, localName);
    }

    /**
     * @see org.apache.fop.fo.FObj#addProperties(Attributes)
     */
    protected void addProperties(Attributes attlist) throws SAXParseException {
        super.addProperties(attlist);

        iColumnNumber = propertyList.get(PR_COLUMN_NUMBER).getNumber().intValue();
        numColumnsRepeated =
            propertyList.get(PR_NUMBER_COLUMNS_REPEATED).getNumber().intValue();
        this.backgroundColor =
            this.propertyList.get(PR_BACKGROUND_COLOR).getColorType();
        columnWidth = this.propertyList.get(PR_COLUMN_WIDTH).getLength();

        getFOInputHandler().startColumn(this);
    }

    /**
     * @see org.apache.fop.fo.FONode#endOfNode
     */
    protected void endOfNode() throws SAXParseException {
        getFOInputHandler().endColumn(this);
    }

    /**
     * @return Length object containing column width
     */
    public Length getColumnWidth() {
        return columnWidth;
    }

    /**
     * @return column number
     */
    public int getColumnNumber() {
        return iColumnNumber;
    }

    /**
     * @return value for number of columns repeated
     */
    public int getNumColumnsRepeated() {
        return numColumnsRepeated;
    }

    public String getName() {
        return "fo:table-column";
    }

    /**
     * @see org.apache.fop.fo.FObj#getNameId()
     */
    public int getNameId() {
        return FO_TABLE_COLUMN;
    }
}

