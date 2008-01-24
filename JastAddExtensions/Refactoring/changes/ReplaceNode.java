package changes;

import AST.ASTNode;
import AST.FileRange;

public class ReplaceNode extends ASTChange {
    
    private int index;
    private ASTNode before;
    private ASTNode after;
    
    public ReplaceNode(ASTNode before, ASTNode after) {
        this.index = before.getParent().getIndexOfChild(before);
        this.before = before;
        this.after = after;
    }

    public String prettyprint() {
        FileRange pos = new FileRange(before.compilationUnit().relativeName(),
        							  before.getStart(), before.getEnd());
        return "at "+pos+", replace node "+before.dumpTree()+" with "+after.dumpTree();
    }
    
    public void undo() {
        after.getParent().setChild(before, index);
    }

}
