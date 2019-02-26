package front.parse;

import front.lex.Token;
import front.parse.ast.*;
import front.parse.ast.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Parser {
    private ArrayList<Token> tokens;
    private ArrayList<Class> program;
    private int cursor;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        program = new ArrayList<>();
        cursor = 0;
    }

    public ArrayList<Class> getProgram() {
        return program;
    }

    private Token getNext(){
        if(cursor == tokens.size()){
            return Token.EOF;
        }else {
            return tokens.get(cursor++);
        }
    }
    private Token peekNext(){
        if(cursor == tokens.size()){
            return Token.EOF;
        }else {
            return tokens.get(cursor);
        }
    }

    private void checkNext(Token.Type type, String value) throws ParseException {
        Token token = peekNext();
        if (token.getType() != type || !token.getValue().equals(value)) {
            throw new ParseException("Expected "+ type + " \"" + value + "\", but got " + peekNext());
        }
        getNext();
    }
    //--------------------------------------------parse--------------------------------------------------------
    public void parse() throws ParseException {
        do {
            Class curClass = parseClass();
            checkNext(Token.Type.PUNCTUATION, ";");
            program.add(curClass);
        } while ("class".equals(peekNext().getValue()));
    }

    private Class parseClass() throws ParseException {
        checkNext(Token.Type.KEYWORD, "class");
        String className = parseID();
        String classParent = null;
        HashMap<String, Attribute> attributes = new HashMap<>();
        HashMap<String, Method> methods= new HashMap<>();

        if ("inherits".equals(peekNext().getValue())) {
            getNext();
            classParent = parseID();
        }
        checkNext(Token.Type.PUNCTUATION, "{");

        // parse feature >= 0
        while (peekNext().getType() == Token.Type.ID) {
            String name = parseID();
            if(":".equals(peekNext().getValue())) {
                // attribute definition

                getNext();
                String type = parseID();
                Expression value = null;
                if ("<-".equals(peekNext().getValue())) {
                    getNext();
                    value = parseExpr();
                }
                attributes.put(name, new Attribute(name, type, value));
            } else  {
                // method definition
                ArrayList<Attribute> params = new ArrayList<>();
                String returnType;
                Expression body;
                checkNext(Token.Type.PUNCTUATION, "(");
                if (! ")".equals(peekNext().getValue())) {
                    while (true) {
                        Attribute param = parseFormal();
                        params.add(param);
                        if (",".equals(peekNext().getValue())) {
                            checkNext(Token.Type.PUNCTUATION, ",");
                        } else {
                            break;
                        }
                    }
                }
                checkNext(Token.Type.PUNCTUATION, ")");
                checkNext(Token.Type.PUNCTUATION, ":");
                returnType = parseID();
                checkNext(Token.Type.PUNCTUATION, "{");
                body = parseExpr();
                checkNext(Token.Type.PUNCTUATION, "}");
                methods.put(name, new Method(name, params, returnType, body));
            }
            checkNext(Token.Type.PUNCTUATION, ";");
        }

        checkNext(Token.Type.PUNCTUATION, "}");
        return new Class(className, classParent, attributes, methods);
    }

    private Attribute parseFormal() throws ParseException {
        String id = parseID();
        checkNext(Token.Type.PUNCTUATION, ":");
        String type = parseID();
        return new Attribute(id, type);
    }

    private Expression parseExprPrime4() throws ParseException {
        Expression leftSide = null;
        Token token = peekNext();
        if (token.getType() == Token.Type.ID) {
            String name = parseID();
            if ("<-".equals(peekNext().getValue())) {
                // id <- expr
                checkNext(Token.Type.OPERATOR, "<-");
                leftSide =  new Assignment(name, parseExpr());
            } else if ("(".equals(peekNext().getValue())) {
                // id([expr (,expr)*])
                checkNext(Token.Type.PUNCTUATION, "(");
                ArrayList<Expression> arguments = new ArrayList<>();
                if (!")".equals(peekNext().getValue())) {
                    arguments.add(parseExpr());
                    while (",".equals(peekNext().getValue())) {
                        checkNext(Token.Type.PUNCTUATION, ",");
                        arguments.add(parseExpr());
                    }
                }
                checkNext(Token.Type.PUNCTUATION, ")");
                leftSide =  new Invocation(null, null, name, arguments);
            } else {
                // id
                leftSide =  new Literal(3, name);
            }
        } else if ("if".equals(token.getValue())) {
            // if expr then expr else expr fi
            checkNext(Token.Type.KEYWORD, "if");
            Expression condition = parseExpr();
            checkNext(Token.Type.KEYWORD, "then");
            Expression thenBody = parseExpr();
            checkNext(Token.Type.KEYWORD, "else");
            Expression elseBody = parseExpr();
            checkNext(Token.Type.KEYWORD, "fi");
            leftSide =  new IfStatement(condition, thenBody, elseBody);
        } else if ("while".equals(token.getValue())) {
            checkNext(Token.Type.KEYWORD, "while");
            Expression condition = parseExpr();
            checkNext(Token.Type.KEYWORD, "loop");
            Expression body = parseExpr();
            checkNext(Token.Type.KEYWORD, "pool");
            leftSide =  new WhileStatement(condition, body);
        } else if ("{".equals(token.getValue())) {
            // { (expr;)+ }
            checkNext(Token.Type.PUNCTUATION, "{");
            ArrayList<Expression> expressions = new ArrayList<>();
            expressions.add(parseExpr());
            checkNext(Token.Type.PUNCTUATION, ";");
            while (!"}".equals(peekNext().getValue())) {
                expressions.add(parseExpr());
                checkNext(Token.Type.PUNCTUATION, ";");
            }
            checkNext(Token.Type.PUNCTUATION, "}");
            leftSide =  new Expressions(expressions);
        } else if ("let".equals(token.getValue())) {
            // let ID : Type [<-expr] (, ID: type [<- expr])* in expr
            HashMap<String, Attribute> variables = new HashMap<>();
            Expression body;
            checkNext(Token.Type.KEYWORD, "let");
            String name = parseID();
            checkNext(Token.Type.PUNCTUATION, ":");
            String type = parseID();
            Expression value = null;
            if ("<-".equals(peekNext().getValue())) {
                checkNext(Token.Type.OPERATOR, "<-");
                value = parseExpr();
            }
            variables.put(name, new Attribute(name, type, value));
            while (",".equals(peekNext().getValue())) {
                checkNext(Token.Type.PUNCTUATION, ",");
                name = parseID();
                checkNext(Token.Type.PUNCTUATION, ":");
                type = parseID();
                if ("<-".equals(peekNext().getValue())) {
                    checkNext(Token.Type.OPERATOR, "<-");
                    value = parseExpr();
                } else {
                    value = null;
                }
                variables.put(name, new Attribute(name, type, value));
            }
            checkNext(Token.Type.KEYWORD, "in");
            body = parseExpr();
            leftSide =  new LetStatement(variables, body);
        } else if ("case".equals(token.getValue())) {
            // case Expr of (id: type => expr ;)+ esac
            checkNext(Token.Type.KEYWORD, "case");
            Expression expression = parseExpr();
            checkNext(Token.Type.KEYWORD, "of");
            HashMap<Attribute, Expression> patterns = new HashMap<>();
            String name = parseID();
            checkNext(Token.Type.PUNCTUATION, ":");
            String type = parseID();
            checkNext(Token.Type.OPERATOR, "=>");
            patterns.put(new Attribute(name, type), parseExpr());
            checkNext(Token.Type.PUNCTUATION, ";");
            while (!"esac".equals(peekNext().getValue())) {
                name = parseID();
                checkNext(Token.Type.PUNCTUATION, ":");
                type = parseID();
                checkNext(Token.Type.OPERATOR, "=>");
                patterns.put(new Attribute(name, type), parseExpr());
                checkNext(Token.Type.PUNCTUATION, ";");
            }
            checkNext(Token.Type.KEYWORD, "esac");
            leftSide =  new CaseStatement(expression, patterns);
        } else if ("new".equals(token.getValue())) {
            checkNext(Token.Type.KEYWORD, "new");
            String type = parseID();
            leftSide =  new NewStatement(type);
        } else if ("isvoid".equals(token.getValue())) {
            checkNext(Token.Type.KEYWORD, "isvoid");
            leftSide =  new IsvoidStatement(parseExpr());
        } else if ("~".equals(token.getValue())) {
            checkNext(Token.Type.OPERATOR, "~");
            leftSide = new NegativeStatement(parseExpr());
        } else if ("not".equals(token.getValue())) {
            checkNext(Token.Type.KEYWORD, "not");
            leftSide =  new NotStatement(parseExpr());
        } else if ("(".equals(token.getValue())) {
            checkNext(Token.Type.PUNCTUATION, "(");
            Expression expression = parseExpr();
            checkNext(Token.Type.PUNCTUATION, ")");
            leftSide =  new BracketStatement(expression);
        } else if (token.getType() == Token.Type.NUMBER) {
            getNext();
            leftSide =  new Literal(token.getNumber());
        } else if (token.getType() == Token.Type.STRING) {
            getNext();
            leftSide =  new Literal(1, token.getValue());
        } else if ("true".equals(token.getValue()) || "false".equals(token.getValue())) {
            checkNext(Token.Type.KEYWORD, token.getValue());
            leftSide =  new Literal(2, token.getValue());
        } else {
            throw new ParseException("Unknow token " + peekNext() + "!");
        }
        return leftSide;
    }

    private Expression parseExpr() throws ParseException {
        Expression leftSide = parseExprPrime1();
        ArrayList<String> list =  new ArrayList<>(Arrays.asList("<=", ">=", "<", ">", "="));
        while (list.contains(peekNext().getValue())) {
            if ("<=".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.LE, leftSide, parseExprPrime1());
            } else if ("<".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.LT, leftSide, parseExprPrime1());
            } else if (">=".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.GE, leftSide, parseExprPrime1());
            } else if (">".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.GT, leftSide, parseExprPrime1());
            } else {
                // ("=".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.EQ, leftSide, parseExprPrime1());
            }
        }
        return leftSide;
    }

    private Expression parseExprPrime1() throws ParseException {
        Expression leftSide = parseExprPrime2();
        ArrayList<String> list =  new ArrayList<>(Arrays.asList("+", "-"));
        while (list.contains(peekNext().getValue())) {
            if ("+".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.ADD, leftSide, parseExprPrime2());
            } else {
                // ("-".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.MINUS, leftSide, parseExprPrime2());
            }
        }
        return leftSide;
    }

    private Expression parseExprPrime2() throws ParseException {
        Expression leftSide = parseExprPrime3();
        ArrayList<String> list =  new ArrayList<>(Arrays.asList("+", "-"));
        while (list.contains(peekNext().getValue())) {
            if ("*".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.TIMES, leftSide, parseExprPrime3());
            } else {
                // if ("/".equals(peekNext().getValue())) {
                getNext();
                leftSide = new Operator(Operator.OP.DIVISION, leftSide, parseExprPrime3());
            }
        }
        return leftSide;
    }

    private Expression parseExprPrime3() throws ParseException {
        // expr[@type].ID([expr (,expr)*])
        Expression leftSide = parseExprPrime4();

        while ("@".equals(peekNext().getValue()) || ".".equals(peekNext().getValue())) {
            String asType = null;
            if ("@".equals(peekNext().getValue())) {
                getNext();
                asType = parseID();
            }
            checkNext(Token.Type.OPERATOR, ".");
            String methodName = parseID();
            ArrayList<Expression> arguments = new ArrayList<>();
            checkNext(Token.Type.PUNCTUATION, "(");
            if (!")".equals(peekNext().getValue())) {
                arguments.add(parseExpr());
                while (",".equals(peekNext().getValue())) {
                    getNext();
                    arguments.add(parseExpr());
                }
            }
            checkNext(Token.Type.PUNCTUATION, ")");
            leftSide =  new Invocation(leftSide, asType, methodName, arguments);
        }
        return leftSide;
    }

    private String parseID() throws ParseException {
        Token token = peekNext();
        if (peekNext().getType() != Token.Type.ID) {
            throw new ParseException("Expected type but got " + token);
        }
        return getNext().getValue();
    }
}
