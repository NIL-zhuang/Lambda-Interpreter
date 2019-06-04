package cn.seecoder;

public abstract class AST {
    int depth;

    AST() {
        this.depth = this.treeDepth(this);
    }

    public abstract String toString();

    public void printTree(AST root, int depth) {
        if (root != null) {
            for (int i = 0; i < depth; i++) {
                System.out.print("--");
            }
            if (root instanceof Application) {
                System.out.println("Application " + ((Application) root).lhs.toString() + " " + ((Application) root).rhs.toString());
            } else if (root instanceof Identifier) {
                System.out.println("Identifier " + ((Identifier) root).value);
            } else if (root instanceof Abstraction) {
                System.out.println("Abstraction \\" + ((Abstraction) root).param.toString() + "." + ((Abstraction) root).body.toString());
            }
            if (root instanceof Application) {
                printTree(((Application) root).lhs, depth + 1);
                printTree(((Application) root).rhs, depth + 1);
            } else {
                root.toString();
            }
        }
    }

    private int treeDepth(AST root) {
        if (root == null) {
            return 0;
        }
        if (root instanceof Application) {
            int left = treeDepth(((Application) root).lhs);
            int right = treeDepth(((Application) root).rhs);
            return (left > right) ? (left + 1) : (right + 1);
        }
        return 0;
    }
}


