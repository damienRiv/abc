
package abc.weaving.aspectinfo;

import polyglot.util.Position;

import soot.*;

/** An aspect in the program. */
public class Aspect extends Syntax {
    private AbcClass cl;
    private Per per;

    public Aspect(AbcClass cl, Per per, Position pos) {
	super(pos);
	this.cl = cl;
	this.per = per;
	per.registerSetupAdvice(this);
    }

    public AbcClass getInstanceClass() {
	return cl;
    }

    public Per getPer() {
	return per;
    }

    public String toString() {
	return "aspect "+cl.getName()+" "+per;
    }
}
