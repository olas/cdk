/* $Revision: 5889 $ $Author: egonw $ $Date: 2006-04-06 15:24:58 +0200 (Thu, 06 Apr 2006) $
 * 
 * Copyright (C) 2006-2007  Egon Willighagen <egonw@users.sf.net>
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
package org.openscience.cdk.atomtype;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

/**
 * This class tests the matching of atom types defined in the
 * structgen atom type list.
 *
 * @cdk.module test-structgen
 */
public class StructGenMatcherTest extends AbstractAtomTypeTest {

    private final static String ATOMTYPE_LIST = "structgen_atomtypes.owl";

    private final static AtomTypeFactory factory = AtomTypeFactory.getInstance(
        "org/openscience/cdk/config/data/" + ATOMTYPE_LIST, NoNotificationChemObjectBuilder.getInstance()
    );

    public String getAtomTypeListName() {
      return ATOMTYPE_LIST;
    };

    public AtomTypeFactory getFactory() {
      return factory;
    }

    public IAtomTypeMatcher getAtomTypeMatcher(IChemObjectBuilder builder) {
      return new StructGenMatcher();
    }

    private static Map<String, Integer> testedAtomTypes = new HashMap<String, Integer>();

    @Test public void testStructGenMatcher() throws Exception {
        StructGenMatcher matcher = new StructGenMatcher();
        Assert.assertNotNull(matcher);
    }

    @Test public void testFindMatchingAtomType_IAtomContainer() throws Exception {
        IMolecule mol = new Molecule();
        IAtom atom = new Atom("C");
        final IAtomType.Hybridization thisHybridization = IAtomType.Hybridization.SP3;
        atom.setHybridization(thisHybridization);
        mol.addAtom(atom);

        // just check consistency; other methods do perception testing
        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType[] types = matcher.findMatchingAtomType(mol);
        for (int i=0; i<types.length; i++) {
            IAtomType type = matcher.findMatchingAtomType(mol, mol.getAtom(i));
            Assert.assertNotNull(type);
            Assert.assertEquals(type.getAtomTypeName(), types[i].getAtomTypeName());
        }
    }

    @Test public void testFindMatchingAtomType_IAtomContainer_IAtom() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom = DefaultChemObjectBuilder.getInstance().newAtom("C");
        atom.setHydrogenCount(4);
        mol.addAtom(atom);

        StructGenMatcher atm = new StructGenMatcher();
        IAtomType matched = atm.findMatchingAtomType(mol, atom);
        Assert.assertNotNull(matched);

        Assert.assertEquals("C", matched.getSymbol());
    }

    @Test public void testN3() throws Exception {
        Molecule mol = new Molecule();
        Atom atom = new Atom("N");
        atom.setHydrogenCount(3);
        mol.addAtom(atom);

        StructGenMatcher atm = new StructGenMatcher();
        IAtomType matched = atm.findMatchingAtomType(mol, atom);
        Assert.assertNotNull(matched);

        Assert.assertEquals("N", matched.getSymbol());
    }


    @Test public void testFlourine() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        atom1.setHydrogenCount(0);
        mol.addAtom(atom1);
        for (int i = 0; i < 4; i++) {
            IAtom floruineAtom = DefaultChemObjectBuilder.getInstance().newAtom("F");
            mol.addAtom(floruineAtom);
            IBond bond = DefaultChemObjectBuilder.getInstance().newBond(floruineAtom, atom1);
            mol.addBond(bond);
        }

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "C4", matched);

        for (int i = 1; i < mol.getAtomCount(); i++) {
            IAtom atom = mol.getAtom(i);
            matched = matcher.findMatchingAtomType(mol, atom);
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "F1", matched);
        }
    }

    @Test public void testChlorine() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        atom1.setHydrogenCount(0);
        mol.addAtom(atom1);
        for (int i = 0; i < 4; i++) {
            IAtom floruineAtom = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
            mol.addAtom(floruineAtom);
            IBond bond = DefaultChemObjectBuilder.getInstance().newBond(floruineAtom, atom1);
            mol.addBond(bond);
        }

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "C4", matched);

        for (int i = 1; i < mol.getAtomCount(); i++) {
            IAtom atom = mol.getAtom(i);
            matched = matcher.findMatchingAtomType(mol, atom);
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "Cl1", matched);
        }
    }

    @Test public void testBromine() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        atom1.setHydrogenCount(0);
        mol.addAtom(atom1);
        for (int i = 0; i < 4; i++) {
            IAtom floruineAtom = DefaultChemObjectBuilder.getInstance().newAtom("Br");
            mol.addAtom(floruineAtom);
            IBond bond = DefaultChemObjectBuilder.getInstance().newBond(floruineAtom, atom1);
            mol.addBond(bond);
        }

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "C4", matched);

        for (int i = 1; i < mol.getAtomCount(); i++) {
            IAtom atom = mol.getAtom(i);
            matched = matcher.findMatchingAtomType(mol, atom);
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "Br1", matched);
        }
    }

    @Test public void testIodine() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        atom1.setHydrogenCount(0);
        mol.addAtom(atom1);
        for (int i = 0; i < 4; i++) {
            IAtom floruineAtom = DefaultChemObjectBuilder.getInstance().newAtom("I");
            mol.addAtom(floruineAtom);
            IBond bond = DefaultChemObjectBuilder.getInstance().newBond(floruineAtom, atom1);
            mol.addBond(bond);
        }

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "C4", matched);

        for (int i = 1; i < mol.getAtomCount(); i++) {
            IAtom atom = mol.getAtom(i);
            matched = matcher.findMatchingAtomType(mol, atom);
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "I1", matched);
        }
    }

    @Test public void testLithium() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("Li");
        IAtom atom2 = DefaultChemObjectBuilder.getInstance().newAtom("F");
        IBond bond = DefaultChemObjectBuilder.getInstance().newBond(atom1, atom2);
        mol.addAtom(atom1);
        mol.addAtom(atom2);
        mol.addBond(bond);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "Li1", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "F1", matched);
    }

    /*
    Tests As3, Cl1
     */
    @Test public void testArsenic() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("As");
        atom1.setHydrogenCount(0);
        mol.addAtom(atom1);
        for (int i = 0; i < 3; i++) {
            IAtom atom = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
            mol.addAtom(atom);
            IBond bond = DefaultChemObjectBuilder.getInstance().newBond(atom, atom1, IBond.Order.SINGLE);
            mol.addBond(bond);
        }

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "As3", matched);

        for (int i = 1; i < mol.getAtomCount(); i++) {
            IAtom atom = mol.getAtom(i);
            matched = matcher.findMatchingAtomType(mol, atom);
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "Cl1", matched);
        }
    }

    /*
    Tests C4, O2
     */
    @Test public void testOxygen1() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom carbon = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom o1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o2 = DefaultChemObjectBuilder.getInstance().newAtom("O");

        carbon.setHydrogenCount(1);
        o1.setHydrogenCount(1);
        o2.setHydrogenCount(0);

        IBond bond1 = DefaultChemObjectBuilder.getInstance().newBond(carbon, o1, IBond.Order.SINGLE);
        IBond bond2 = DefaultChemObjectBuilder.getInstance().newBond(carbon, o2, IBond.Order.DOUBLE);

        mol.addAtom(carbon);
        mol.addAtom(o1);
        mol.addAtom(o2);
        mol.addBond(bond1);
        mol.addBond(bond2);

        StructGenMatcher matcher = new StructGenMatcher();

        // look at the sp2 O first
        IAtomType matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "O2", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "C4", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);
    }

    /*
    Tests O2, H1
     */
    @Test public void testOxygen2() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom o1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o2 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom h1 = DefaultChemObjectBuilder.getInstance().newAtom("H");
        IAtom h2 = DefaultChemObjectBuilder.getInstance().newAtom("H");

        IBond bond1 = DefaultChemObjectBuilder.getInstance().newBond(h1, o1, IBond.Order.SINGLE);
        IBond bond2 = DefaultChemObjectBuilder.getInstance().newBond(o1, o2, IBond.Order.SINGLE);
        IBond bond3 = DefaultChemObjectBuilder.getInstance().newBond(o2, h2, IBond.Order.SINGLE);

        mol.addAtom(o1);
        mol.addAtom(o2);
        mol.addAtom(h1);
        mol.addAtom(h2);

        mol.addBond(bond1);
        mol.addBond(bond2);
        mol.addBond(bond3);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "O2", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "H1", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(3));
        assertAtomType(testedAtomTypes, "H1", matched);
    }

    /*
    Tests P4, S2, Cl1
     */
    @Test public void testP4() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom p = DefaultChemObjectBuilder.getInstance().newAtom("P");
        IAtom cl1 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        IAtom cl2 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        IAtom cl3 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        IAtom s = DefaultChemObjectBuilder.getInstance().newAtom("S");

        IBond bond1 = DefaultChemObjectBuilder.getInstance().newBond(p, cl1, IBond.Order.SINGLE);
        IBond bond2 = DefaultChemObjectBuilder.getInstance().newBond(p, cl2, IBond.Order.SINGLE);
        IBond bond3 = DefaultChemObjectBuilder.getInstance().newBond(p, cl3, IBond.Order.SINGLE);
        IBond bond4 = DefaultChemObjectBuilder.getInstance().newBond(p, s, IBond.Order.DOUBLE);

        mol.addAtom(p);
        mol.addAtom(cl1);
        mol.addAtom(cl2);
        mol.addAtom(cl3);
        mol.addAtom(s);

        mol.addBond(bond1);
        mol.addBond(bond2);
        mol.addBond(bond3);
        mol.addBond(bond4);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "P4", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(4));
        assertAtomType(testedAtomTypes, "S2", matched);

        for (int i = 1; i < 4; i++) {
            matched = matcher.findMatchingAtomType(mol, mol.getAtom(i));
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "Cl1", matched);
        }
    }

    /*
    Tests P3, O2, C4
     */
    @Test public void testP3() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom p = DefaultChemObjectBuilder.getInstance().newAtom("P");
        IAtom o1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o2 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o3 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom c1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom c2 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom c3 = DefaultChemObjectBuilder.getInstance().newAtom("C");

        c1.setHydrogenCount(3);
        c2.setHydrogenCount(3);
        c3.setHydrogenCount(3);

        IBond bond1 = DefaultChemObjectBuilder.getInstance().newBond(p, o1, IBond.Order.SINGLE);
        IBond bond2 = DefaultChemObjectBuilder.getInstance().newBond(p, o2, IBond.Order.SINGLE);
        IBond bond3 = DefaultChemObjectBuilder.getInstance().newBond(p, o3, IBond.Order.SINGLE);
        IBond bond4 = DefaultChemObjectBuilder.getInstance().newBond(c1, o1, IBond.Order.SINGLE);
        IBond bond5 = DefaultChemObjectBuilder.getInstance().newBond(c2, o2, IBond.Order.SINGLE);
        IBond bond6 = DefaultChemObjectBuilder.getInstance().newBond(c3, o3, IBond.Order.SINGLE);

        mol.addAtom(p);
        mol.addAtom(o1);
        mol.addAtom(o2);
        mol.addAtom(o3);
        mol.addAtom(c1);
        mol.addAtom(c2);
        mol.addAtom(c3);

        mol.addBond(bond1);
        mol.addBond(bond2);
        mol.addBond(bond3);
        mol.addBond(bond4);
        mol.addBond(bond5);
        mol.addBond(bond6);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        String[] atomTypes = {"P3", "O2", "O2", "O2", "C4", "C4", "C4"};
        for (int i = 0; i < mol.getAtomCount(); i++) {
            matched = matcher.findMatchingAtomType(mol, mol.getAtom(i));
            assertAtomType(testedAtomTypes, atomTypes[i], matched);
        }
    }


    /* Test Na1, Cl1 */
    @Test public void testNa1() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom na = DefaultChemObjectBuilder.getInstance().newAtom("Na");
        IAtom cl = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        IBond bond = DefaultChemObjectBuilder.getInstance().newBond(na, cl, IBond.Order.SINGLE);
        mol.addAtom(na);
        mol.addAtom(cl);
        mol.addBond(bond);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "Na1", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "Cl1", matched);
    }

    /* Test Si4, C4, Cl1 */
    @Test public void testSi4() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom si = DefaultChemObjectBuilder.getInstance().newAtom("Si");
        IAtom c1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom cl1 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        IAtom cl2 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        IAtom cl3 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");

        c1.setHydrogenCount(3);

        IBond bond1 = DefaultChemObjectBuilder.getInstance().newBond(si, c1, IBond.Order.SINGLE);
        IBond bond2 = DefaultChemObjectBuilder.getInstance().newBond(si, cl1, IBond.Order.SINGLE);
        IBond bond3 = DefaultChemObjectBuilder.getInstance().newBond(si, cl2, IBond.Order.SINGLE);
        IBond bond4 = DefaultChemObjectBuilder.getInstance().newBond(si, cl3, IBond.Order.SINGLE);

        mol.addAtom(si);
        mol.addAtom(c1);
        mol.addAtom(cl1);
        mol.addAtom(cl2);
        mol.addAtom(cl3);

        mol.addBond(bond1);
        mol.addBond(bond2);
        mol.addBond(bond3);
        mol.addBond(bond4);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "Si4", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "C4", matched);

        for (int i = 3; i < mol.getAtomCount(); i++) {
            matched = matcher.findMatchingAtomType(mol, mol.getAtom(i));
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "Cl1", matched);
        }
    }

    /* Tests S2, H1 */
    @Test public void testS2() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom s = DefaultChemObjectBuilder.getInstance().newAtom("S");
        s.setHydrogenCount(2);

        mol.addAtom(s);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "S2", matched);

        mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        s = DefaultChemObjectBuilder.getInstance().newAtom("S");
        IAtom h1 = DefaultChemObjectBuilder.getInstance().newAtom("H");
        IAtom h2 = DefaultChemObjectBuilder.getInstance().newAtom("H");
        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(s, h1, IBond.Order.SINGLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(s, h2, IBond.Order.SINGLE);

        mol.addAtom(s);
        mol.addAtom(h1);
        mol.addAtom(h2);

        mol.addBond(b1);
        mol.addBond(b2);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "S2", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "H1", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "H1", matched);
    }

    /* Tests S3, O2 */
    @Test public void testS3() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom s = DefaultChemObjectBuilder.getInstance().newAtom("S");
        IAtom o1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o2 = DefaultChemObjectBuilder.getInstance().newAtom("O");

        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(s, o1, IBond.Order.DOUBLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(s, o2, IBond.Order.DOUBLE);

        mol.addAtom(s);
        mol.addAtom(o1);
        mol.addAtom(o2);

        mol.addBond(b1);
        mol.addBond(b2);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "S3", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "O2", matched);
    }


    /* Tests S4, Cl1 */
    @Test public void testS4() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom s = DefaultChemObjectBuilder.getInstance().newAtom("S");
        mol.addAtom(s);
        for (int i = 0; i < 6; i++) {
            IAtom f = DefaultChemObjectBuilder.getInstance().newAtom("F");
            mol.addAtom(f);
            IBond bond = DefaultChemObjectBuilder.getInstance().newBond(s, f, IBond.Order.SINGLE);
            mol.addBond(bond);
        }

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "S4", matched);

        for (int i = 1; i < mol.getAtomCount(); i++) {
            matched = matcher.findMatchingAtomType(mol, mol.getAtom(i));
            assertAtomType(testedAtomTypes, "atom " + i + " failed to match", "F1", matched);
        }
    }

    /* Tests S4, O2 */
    @Test public void testS4oxide() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom s = DefaultChemObjectBuilder.getInstance().newAtom("S");
        IAtom o1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o2 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o3 = DefaultChemObjectBuilder.getInstance().newAtom("O");

        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(s, o1, IBond.Order.DOUBLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(s, o2, IBond.Order.DOUBLE);
        IBond b3 = DefaultChemObjectBuilder.getInstance().newBond(s, o3, IBond.Order.DOUBLE);

        mol.addAtom(s);
        mol.addAtom(o1);
        mol.addAtom(o2);
        mol.addAtom(o3);

        mol.addBond(b1);
        mol.addBond(b2);
        mol.addBond(b3);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "S4", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "O2", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(3));
        assertAtomType(testedAtomTypes, "O2", matched);
    }

    /* Tests N3, O2 */
    @Test public void testN3acid() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom n = DefaultChemObjectBuilder.getInstance().newAtom("N");
        IAtom o = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom h = DefaultChemObjectBuilder.getInstance().newAtom("H");


        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(n, o, IBond.Order.DOUBLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(n, h, IBond.Order.SINGLE);


        mol.addAtom(n);
        mol.addAtom(o);
        mol.addAtom(h);

        mol.addBond(b1);
        mol.addBond(b2);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "N3", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "H1", matched);
    }

    @Test public void testN3cyanide() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom n = DefaultChemObjectBuilder.getInstance().newAtom("N");
        IAtom c1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom c2 = DefaultChemObjectBuilder.getInstance().newAtom("C");


        c1.setHydrogenCount(0);
        c2.setHydrogenCount(3);

        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(n, c1, IBond.Order.TRIPLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(c1, c2, IBond.Order.SINGLE);


        mol.addAtom(n);
        mol.addAtom(c1);
        mol.addAtom(c2);

        mol.addBond(b1);
        mol.addBond(b2);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "N3", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "C4", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "C4", matched);
    }


    /* Tests N5, O2, C4 */
    @Test public void testN5() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom n = DefaultChemObjectBuilder.getInstance().newAtom("N");
        IAtom o1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom o2 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IAtom c = DefaultChemObjectBuilder.getInstance().newAtom("C");

        c.setHydrogenCount(3);

        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(n, o1, IBond.Order.DOUBLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(n, o2, IBond.Order.DOUBLE);
        IBond b3 = DefaultChemObjectBuilder.getInstance().newBond(n, c, IBond.Order.SINGLE);

        mol.addAtom(n);
        mol.addAtom(o1);
        mol.addAtom(o2);
        mol.addAtom(c);

        mol.addBond(b1);
        mol.addBond(b2);
        mol.addBond(b3);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "N5", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "O2", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(3));
        assertAtomType(testedAtomTypes, "C4", matched);
    }

    /* Test B3, F1 */
    @Test public void testB3() throws Exception {
       IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom b = DefaultChemObjectBuilder.getInstance().newAtom("B");
        IAtom f1 = DefaultChemObjectBuilder.getInstance().newAtom("F");
        IAtom f2 = DefaultChemObjectBuilder.getInstance().newAtom("F");
        IAtom f3 = DefaultChemObjectBuilder.getInstance().newAtom("F");

        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(b, f1, IBond.Order.SINGLE);
        IBond b2 = DefaultChemObjectBuilder.getInstance().newBond(b, f2, IBond.Order.SINGLE);
        IBond b3 = DefaultChemObjectBuilder.getInstance().newBond(b, f3, IBond.Order.SINGLE);

        mol.addAtom(b);
        mol.addAtom(f1);
        mol.addAtom(f2);
        mol.addAtom(f3);
        mol.addBond(b1);
        mol.addBond(b2);
        mol.addBond(b3);


        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "B3", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "F1", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(2));
        assertAtomType(testedAtomTypes, "F1", matched);

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(3));
        assertAtomType(testedAtomTypes, "F1", matched);
    }

  
    @Test public void testSe2() throws Exception {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom se = DefaultChemObjectBuilder.getInstance().newAtom("Se");
        IAtom o = DefaultChemObjectBuilder.getInstance().newAtom("O");
        IBond b1 = DefaultChemObjectBuilder.getInstance().newBond(se, o, IBond.Order.DOUBLE);
        mol.addAtom(se);
        mol.addAtom(o);
        mol.addBond(b1);

        StructGenMatcher matcher = new StructGenMatcher();
        IAtomType matched;

        matched = matcher.findMatchingAtomType(mol, mol.getAtom(0));
        assertAtomType(testedAtomTypes, "Se2", matched);


        matched = matcher.findMatchingAtomType(mol, mol.getAtom(1));
        assertAtomType(testedAtomTypes, "O2", matched);        

    }

    /**
     * The test seems to be run by JUnit in order in which they found
     * in the source. Ugly, but @AfterClass does not work because that
     * methods does cannot assert anything.
     */
    @Test public void countTestedAtomTypes() {
    	AtomTypeFactory factory = AtomTypeFactory.getInstance(
    		"org/openscience/cdk/config/data/structgen_atomtypes.xml",
            NoNotificationChemObjectBuilder.getInstance()
        );

   	    IAtomType[] expectedTypes = factory.getAllAtomTypes();        
        if (expectedTypes.length != testedAtomTypes.size()) {
       	    String errorMessage = "Atom types not tested:";
            for (IAtomType expectedType : expectedTypes) {
                if (!testedAtomTypes.containsKey(expectedType.getAtomTypeName()))
                    errorMessage += " " + expectedType.getAtomTypeName();
            }
            Assert.assertEquals(errorMessage,
    			factory.getAllAtomTypes().length,
    			testedAtomTypes.size()
    		);
    	}
    }

}
