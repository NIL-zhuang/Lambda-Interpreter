package cn.seecoder;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public AST parse() {
        AST ast = term(new ArrayList<>());
//        System.out.println(lexer.match(TokenType.EOF));
        return ast;
    }

    /**
     * Term ::= Application| LAMBDA LCID DOT Term
     */
    private AST term(ArrayList<String> ctx) {
        //check if it matches LAMBDA LCID DOT Term
        if (this.lexer.skip(TokenType.LAMBDA)) {
            if (lexer.next(TokenType.LCID)) {
                String param = lexer.token.value;
                lexer.match(TokenType.LCID);
                if (lexer.skip(TokenType.DOT)) {
                    ctx.add(0, param);
                    AST aTerm = term(ctx);
                    ctx.remove(ctx.indexOf(param));
                    return new Abstraction(new Identifier(param, param), aTerm);
                }
            }
        } else {
            // it is an application
            return application(ctx);
        }
        return null;
    }

    /**
     * application ::= atom application
     */
    private AST application(ArrayList<String> ctx) {
        AST lhs = this.atom(ctx);
        AST rhs;
        while (true) {
            rhs = atom(ctx);
            if (rhs == null) {
                return lhs;
            } else {
                lhs = new Application(lhs, rhs);
            }
        }
    }

    /**
     * atom ::= LPAREN term RPAREN
     * | LCID
     */
    private AST atom(ArrayList<String> ctx) {
        String param;
        if (this.lexer.skip(TokenType.LPAREN)) {
            // it is a term
            AST term = term(ctx);
            if (this.lexer.match((TokenType.RPAREN))) {
                return term;
            }
        } else if (this.lexer.next(TokenType.LCID)) {
            // it is an LCID
            param = lexer.token.value;
            lexer.match(TokenType.LCID);
            return new Identifier(param, String.valueOf(ctx.indexOf(param)));
        }
        return null;
    }

    public static void main(String[] args) {
        String source = "((\\n.\\f.\\x.f (n f x))(\\f.\\x.x))";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        AST ast = parser.parse();
        ast.printTree(ast, 0);
    }

}
