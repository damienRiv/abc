
package abc.weaving.aspectinfo;

import soot.*;

/** A weavable class. */
public class AbcClass {
    private String name;
    private SootClass sc;

    public AbcClass(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public SootClass getSootClass() {
	if (sc == null) {
	    sc = Scene.v().getSootClass(name);
	}
	return sc;
    }

    public String toString() {
	return name;
    }
}
