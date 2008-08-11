package org.jastadd.plugin.AST;

public interface ISelectionNode extends IJastAddNode {
	public int selectionLine();
	public int selectionColumn();
	public int selectionLength();
	// TODO add methods selectionLineEnd()..
}
