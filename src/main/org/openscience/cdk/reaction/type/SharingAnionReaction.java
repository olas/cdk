/*
 *  $RCSfile$
 *  $Author: egonw $
 *  $Date: 2006-03-29 10:27:08 +0200 (Wed, 29 Mar 2006) $
 *  $Revision: 5855 $
 *
 *  Copyright (C) 2008 Miguel Rojas <miguelrojasch@users.sf.net>
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.reaction.type;


import java.util.ArrayList;
import java.util.Iterator;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.reaction.IReactionMechanism;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.ReactionSpecification;
import org.openscience.cdk.reaction.mechanism.SharingElectronMechanism;
import org.openscience.cdk.tools.LoggingTool;

/**
 * <p>IReactionProcess which participate in movement resonance. 
 * This reaction could be represented as [A+]-B| => A=[B+]. Due to 
 * deficiency of charge of the atom A, the lone pair electron of the atom A is 
 * desplaced.</p>
 * <p>Make sure that the molecule has the correspond lone pair electrons
 * for each atom. You can use the method: <pre> LonePairElectronChecker </pre>
 * 
 * <pre>
 *  IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
 *  setOfReactants.addMolecule(new Molecule());
 *  IReactionProcess type = new SharingAnionReaction();
 *  Object[] params = {Boolean.FALSE};
    type.setParameters(params);
 *  IReactionSet setOfReactions = type.initiate(setOfReactants, null);
 *  </pre>
 * 
 * <p>We have the possibility to localize the reactive center. Good method if you
 * want to localize the reaction in a fixed point</p>
 * <pre>atoms[0].setFlag(CDKConstants.REACTIVE_CENTER,true);</pre>
 * <p>Moreover you must put the parameter Boolean.TRUE</p>
 * <p>If the reactive center is not localized then the reaction process will
 * try to find automatically the possible reactive center.</p>
 * 
 * 
 * @author         Miguel Rojas
 * 
 * @cdk.created    2006-05-05
 * @cdk.module     reaction
 * @cdk.svnrev  $Revision: 9162 $
 * @cdk.set        reaction-types
 * 
 **/
public class SharingAnionReaction implements IReactionProcess{
	private LoggingTool logger;
	private boolean hasActiveCenter;
	private IReactionMechanism mechanism;

	/**
	 * Constructor of the SharingAnionReaction object.
	 *
	 */
	public SharingAnionReaction(){
		logger = new LoggingTool(this);
		mechanism = new SharingElectronMechanism();
	}
	/**
	 *  Gets the specification attribute of the SharingAnionReaction object.
	 *
	 *@return    The specification value
	 */
	public ReactionSpecification getSpecification() {
		return new ReactionSpecification(
				"http://almost.cubic.uni-koeln.de/jrg/Members/mrc/reactionDict/reactionDict#SharingAnion",
				this.getClass().getName(),
				"$Id: SharingAnionReaction.java,v 1.6 2006/04/01 08:26:47 mrc Exp $",
				"The Chemistry Development Kit");
	}
	
	/**
	 *  Sets the parameters attribute of the SharingAnionReaction object.
	 *
	 *@param  params            The parameter is if the molecule has already fixed the center active or not. It 
	 *							should be set before to initiate the reaction with a setFlag:  CDKConstants.REACTIVE_CENTER
	 *@exception  CDKException  Description of the Exception
	 */
	public void setParameters(Object[] params) throws CDKException {
		if (params.length > 1) {
			throw new CDKException("SharingAnionReaction only expects one parameter");
		}
		if (!(params[0] instanceof Boolean)) {
			throw new CDKException("The parameter 1 must be of type boolean");
		}
		hasActiveCenter = ((Boolean) params[0]).booleanValue();
	}


	/**
	 *  Gets the parameters attribute of the SharingAnionReaction object.
	 *
	 *@return    The parameters value
	 */
	public Object[] getParameters() {
		Object[] params = new Object[1];
		params[0] = new Boolean (hasActiveCenter);
		return params;
	}
	
	/**
	 *  Initiate process.
	 *  It is needed to call the addExplicitHydrogensToSatisfyValency
	 *  from the class tools.HydrogenAdder.
	 *
	 *@param  reactants         reactants of the reaction.
	 *@param  agents            agents of the reaction (Must be in this case null).
	 *
	 *@exception  CDKException  Description of the Exception
	 */
	public IReactionSet initiate(IMoleculeSet reactants, IMoleculeSet agents) throws CDKException{

		logger.debug("initiate reaction: SharingAnionReaction");
		
		if (reactants.getMoleculeCount() != 1) {
			throw new CDKException("SharingAnionReaction only expects one reactant");
		}
		if (agents != null) {
			throw new CDKException("SharingAnionReaction don't expects agents");
		}
		
		IReactionSet setOfReactions = DefaultChemObjectBuilder.getInstance().newReactionSet();
		IMolecule reactant = reactants.getMolecule(0);
		/* if the parameter hasActiveCenter is not fixed yet, set the active centers*/
		if(!hasActiveCenter){
			setActiveCenters(reactant);
		}
		
		Iterator<IAtom> atomis = reactant.atoms();
		while(atomis.hasNext()){
			IAtom atomi = atomis.next();
			if(atomi.getFlag(CDKConstants.REACTIVE_CENTER) && atomi.getFormalCharge() == -1 &&
					reactant.getConnectedSingleElectronsCount(atomi) == 0 &&
					reactant.getConnectedLonePairsList(atomi).size() > 0){
				
				Iterator<IBond> bondis = reactant.getConnectedBondsList(atomi).iterator();
				while(bondis.hasNext()){
					IBond bondi = bondis.next();
					if(bondi.getFlag(CDKConstants.REACTIVE_CENTER) && bondi.getOrder() == IBond.Order.SINGLE){
						IAtom atomj = bondi.getConnectedAtom(atomi);
						if(atomj.getFlag(CDKConstants.REACTIVE_CENTER) && atomj.getFormalCharge() == 1 &&
							reactant.getConnectedSingleElectronsCount(atomj) == 0){
							
							ArrayList<IAtom> atomList = new ArrayList<IAtom>();
		                	atomList.add(atomi);
		                	atomList.add(atomj);
		                	ArrayList<IBond> bondList = new ArrayList<IBond>();
		                	bondList.add(bondi);

							IMoleculeSet moleculeSet = reactant.getBuilder().newMoleculeSet();
							moleculeSet.addMolecule(reactant);
							IReaction reaction = mechanism.initiate(moleculeSet, atomList, bondList);
							if(reaction == null)
								continue;
							else
								setOfReactions.addReaction(reaction);
						}
					}
				}
			}
		}
		
		return setOfReactions;	
		
		
	}
	/**
	 * set the active center for this molecule. 
	 * The active center will be those which correspond with [A+]-B|. 
	 * <pre>
	 * A: Atom with positive charge
	 * -: Single bond
	 * B: Atom with lone pair electrons
	 *  </pre>
	 * 
	 * @param reactant The molecule to set the activity
	 * @throws CDKException 
	 */
	private void setActiveCenters(IMolecule reactant) throws CDKException {
		Iterator<IAtom> atomis = reactant.atoms();
		while(atomis.hasNext()){
			IAtom atomi = atomis.next();
			if(atomi.getFormalCharge() == -1 &&
					reactant.getConnectedSingleElectronsCount(atomi) == 0 &&
					reactant.getConnectedLonePairsList(atomi).size() > 0){

				Iterator<IBond> bondis = reactant.getConnectedBondsList(atomi).iterator();
				while(bondis.hasNext()){
					IBond bondi = bondis.next();
					if(bondi.getOrder() == IBond.Order.SINGLE){
						IAtom atomj = bondi.getConnectedAtom(atomi);
						if(atomj.getFormalCharge() == 1 &&
							reactant.getConnectedSingleElectronsCount(atomj) == 0){
							atomi.setFlag(CDKConstants.REACTIVE_CENTER,true);
							atomj.setFlag(CDKConstants.REACTIVE_CENTER,true);
							bondi.setFlag(CDKConstants.REACTIVE_CENTER,true);
						}
					}
				}
			}
		}
	}
	/**
	 *  Gets the parameterNames attribute of the SharingAnionReaction object.
	 *
	 *@return    The parameterNames value
	 */
	public String[] getParameterNames() {
		String[] params = new String[1];
		params[0] = "hasActiveCenter";
		return params;
	}


	/**
	 *  Gets the parameterType attribute of the SharingAnionReaction object.
	 *
	 *@param  name  Description of the Parameter
	 *@return       The parameterType value
	 */
	public Object getParameterType(String name) {
		return new Boolean(false);
	}
}