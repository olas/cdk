/* Generated By:JJTree: Do not edit this line. ASTAtomicMass.java */

package org.openscience.cdk.smiles.smarts.parser;

public class ASTAtomicMass extends SimpleNode {
    private int mass;

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public ASTAtomicMass(int id) {
        super(id);
    }

    public ASTAtomicMass(SMARTSParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. * */
    public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}