/* $Revision$ $Author$ $Date$    
 * 
 * Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.openscience.cdk;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.AbstractElementTest;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * Checks the functionality of the Element class.
 *
 * @cdk.module test-data
 *
 * @see org.openscience.cdk.Element
 */
public class ElementTest extends AbstractElementTest {

    @BeforeClass public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {
            public IChemObject newTestObject() {
                return new Element();
            }
        });
    }

    // test constructors
    
    @Test public void testElement() {
        IElement e = new Element();
        Assert.assertTrue(e instanceof IChemObject);
    }
    
    @Test public void testElement_IElement() {
    	IElement element = new Element();
        IElement e = new Element(element);
        Assert.assertTrue(e instanceof IChemObject);
    }
    
    @Test public void testElement_String() {
        IElement e = new Element("C");
        Assert.assertEquals("C", e.getSymbol());
    }
    
    @Test public void testElement_String_int() {
        IElement e = new Element("H", 1);
        Assert.assertEquals("H", e.getSymbol());
        Assert.assertEquals(1, e.getAtomicNumber().intValue());
    }

}
