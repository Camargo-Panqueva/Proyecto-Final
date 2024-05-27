package controller.logic;

import java.awt.*;
import java.util.ArrayList;

public class Tree {
    private final ArrayList<Tree> children;
    public final Point data;
    public Tree parent;

    /**
     * Creates a new Tree with the given data as root.
     *
     * @param data the data of the tree.
     */
    public Tree(final Point data){
        this.data = data;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    /**
     * Searches for the given node in the tree.
     * When the node is found, the position given as parameter is added as a child of the node.
     *
     * @param node the node to search for.
     * @param child the child to add.
     */
    public void addChild(final Point node, final Point child){
        if (child.equals(data)) {
            return;
        }
        if (node.equals(data) && children.size() < 4) {
            final Tree newChild = new Tree(child);
            newChild.parent = this;
            children.add(newChild);
        } else {
            for (Tree tree : children) {
                tree.addChild(node, child);
            }
        }
    }

    /**
     * Returns all the parents of the given leaf.
     *
     * @param leaf the leaf to search for.
     * @return the list of parents of the leaf.
     */
    public ArrayList<Tree> getAllParents(final Point leaf){
        Tree current = this.findNode(leaf);
        final ArrayList<Tree> parents = new ArrayList<>();
        parents.add(current);
        while (current.parent != null) {
            parents.add(current.parent);
            current = current.parent;
        }
        return parents;
    }

    /**
     * Searches for the given node in the tree.
     *
     * @param leaf the node to search for.
     * @return the node if found, null otherwise.
     */
    public Tree findNode(final Point leaf){
        if (data.equals(leaf)) {
            return this;
        }
        for (Tree tree : children) {
            Tree found = tree.findNode(leaf);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public boolean isLeaf(){
        return children.isEmpty();
    }

    public boolean isRoot(){
        return parent == null;
    }

    @Override
    public String toString() {
        return data.toString() + " " + children;
    }
}
