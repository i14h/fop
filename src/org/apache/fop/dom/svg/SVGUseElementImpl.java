/*-- $Id$ -- 

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================
 
    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:
 
 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.
 
 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.
 
 4. The names "FOP" and  "Apache Software Foundation"  must not be used to
    endorse  or promote  products derived  from this  software without  prior
    written permission. For written permission, please contact
    apache@apache.org.
 
 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.
 
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 James Tauber <jtauber@jtauber.com>. For more  information on the Apache 
 Software Foundation, please see <http://www.apache.org/>.
 
 */

package org.apache.fop.dom.svg;

import org.w3c.dom.svg.*;

/**
 * Use Element
 *
 */
public class SVGUseElementImpl extends GraphicElement implements SVGUseElement {
	public String link;
	String xlinkArcRole;

	public SVGUseElementImpl(String s)
	{
		link = s;
	}

	public String getXlinkType( )
	{
		return null;
	}

	public void setXlinkType( String xlinkType )
	{
	}

	public String getXlinkRole( )
	{
		return null;
	}

	public void setXlinkRole( String xlinkRole )
	{
	}

	public String getXlinkTitle( )
	{
		return null;
	}

	public void setXlinkTitle( String xlinkTitle )
	{
	}

	public String getXlinkArcRole()
	{
		return xlinkArcRole;
	}

	public void setXlinkArcRole(String xlinkArcRole)
	{
		this.xlinkArcRole = xlinkArcRole;
	}

	public String getXlinkShow( )
	{
		return null;
	}

	public void setXlinkShow( String xlinkShow )
	{
	}

	public String getXlinkActuate( )
	{
		return null;
	}

	public void setXlinkActuate( String xlinkActuate )
	{
	}

	public SVGAnimatedString getHref( )
	{
		return null;
	}

	public void setHref( SVGAnimatedString href )
	{
	}

	public SVGAnimatedLength getX( )
	{
		return null;
	}

	public void setX( SVGAnimatedLength x )
	{
	}

	public SVGAnimatedLength getY( )
	{
		return null;
	}

	public void setY( SVGAnimatedLength y )
	{
	}

	public SVGAnimatedLength getWidth( )
	{
		return null;
	}

	public void setWidth( SVGAnimatedLength width )
	{
	}

	public SVGAnimatedLength getHeight( )
	{
		return null;
	}

	public void setHeight( SVGAnimatedLength height )
	{
	}

	public SVGElementInstance getInstanceRoot( )
	{
		return null;
	}

	public SVGElementInstance getAnimatedInstanceRoot( )
	{
		return null;
	}

/*	public SVGAnimatedBoolean getExternalResourcesRequired( )
	{
	}

	public void setExternalResourcesRequired( SVGAnimatedBoolean externalResourcesRequired )
	{
	}*/
}
