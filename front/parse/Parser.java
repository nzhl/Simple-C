package front.parse;

import front.ast.Fun;
import front.ast.judge.Id;
import front.ast.Type;
import front.ast.judge.*;
import front.ast.stmt.*;
import front.ast.value.Bool;
import front.ast.value.Int;
import front.ast.value.Str;
import front.lex.Token;
import java.util.ArrayList;
import static front.ast.Type.*;

public class Parser {
    private ArrayList<Token> tokens;
    private int length;
    private int index;
    private Fun fun;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        length = tokens.size();
        index = 0;
    }

    private Token nextToken(){
        if(index == length){
            return null;
        }else {
            return tokens.get(index++);
        }
    }

    private Type typeJudge(Token token){
        switch (token.value){
            case "int":
                return Int;
            case "bool":
                return Bool;
            case "string":
                return Str;
            default:
                return null;
        }
    }

    //--------------------------------------------parse--------------------------------------------------------

    public Fun parse() throws ParseException {
        fun = funParse();
        while(nextToken() != null){
            index--;
            fun.add(funParse());
        }
        return fun;
    }

    private Fun funParse() throws ParseException {
        Token token = nextToken();
        Type type;
        String id;
        Stmt stmt;

        /**
         *      func -> type id () block
         */

        if(token.type != Token.Type.TYPE){
            throw new ParseException("expected function return type in line : " + token.line + " pos : " + token.pos + " !");
        }else {
            type = typeJudge(token);
        }

        token = nextToken();
        if(token.type != Token.Type.ID){
            throw new ParseException("expected function name in line : " + token.line + " pos : " + token.pos + " !");
        }else{
            id = token.value;
        }

        token = nextToken();
        if(!token.value.equals("(")){
            throw new ParseException("expected left bracket in line : " + token.line + " pos : " + token.pos + " !");
        }
        token = nextToken();
        if(!token.value.equals(")")){
            throw new ParseException("expected right bracket in line : " + token.line + " pos : " + token.pos + " !");
        }

        stmt = blockParse();

        Fun fun = new Fun(type, id, stmt);
        return fun;
    }

    private Stmt blockParse() throws ParseException {
        Stmt stmt;
        Token token = nextToken();
        if(!token.value.equals("{")){
            throw new ParseException("expected left bracket in line : " + token.line + " pos : " + token.pos + " !");
        }

        stmt = stmtsParse();

        token = nextToken();
        if(!token.value.equals("}")){
            throw new ParseException("expected right bracket in line : " + token.line + " pos : " + token.pos + " !");
        }

        return stmt;
    }

    private Stmt stmtsParse() throws ParseException {
        Stmt stmt = stmtParse();
        while(!nextToken().value.equals("}")){
            index--;
            stmt.add(stmtParse());
        }
        index--;
        return stmt;
    }

    private Stmt stmtParse() throws ParseException {
        /**

         stmt -> type id;
         | type id = judge;
         | id = judge;
         | if(judge) block
         | if(judge) block else block
         | while(judge) block
         | print id;
         | print num;
         | print boolean;
         | print str;

         */

        Token token = nextToken();
        if (token.type == Token.Type.TYPE) {
            Type type = typeJudge(token);
            Id id;
            Judge judge;
            // defStmt

            token = nextToken();
            if (token.type != Token.Type.ID) {
                throw new ParseException("expected id in line : " + token.line + " pos : " + token.pos + " !");
            } else {
                id = new Id(type, token.value);
                token = nextToken();
                if (token.type == Token.Type.SEMI) {
                    return new Def(id, null);
                } else if (token.value.equals("=")) {
                    judge = judgeParse();
                    token = nextToken();
                    if (token.type != Token.Type.SEMI) {
                        throw new ParseException("expected semicolon in line : " + token.line + " pos : " + token.pos + " !");
                    }
                    return new Def(id, judge);
                } else {
                    throw new ParseException("Parsing error in line : " + token.line + " pos : " + token.pos + " !");
                }
            }
        } else if (token.type == Token.Type.ID) {
            Id id = new Id(null, token.value);
            token = nextToken();
            if (!token.value.equals("=")) {
                throw new ParseException("Expected '=' in line : " + token.line + " pos : " + token.pos + " !");
            }
            Assign assign = new Assign(id, judgeParse());
            token = nextToken();
            if (!token.value.equals(";")) {
                throw new ParseException("expected semicolon in line : " + token.line + " pos : " + token.pos + " !");
            }
            return assign;
        } else if (token.value.equals("while")) {
            token = nextToken();
            if (!token.value.equals("(")) {
                throw new ParseException("expected left bracket in line : " + token.line + " pos : " + token.pos + " !");
            }

            Judge condition = judgeParse();

            token = nextToken();
            if (!token.value.equals(")")) {
                throw new ParseException("expected right bracket in line : " + token.line + " pos : " + token.pos + " !");
            }
            return new While(condition, blockParse());

        } else if (token.value.equals("print")) {
            token = nextToken();
            if (token.type == Token.Type.ID) {
                Id id = new Id(typeJudge(token), token.value);
                token = nextToken();
                if (!token.value.equals(";")) {
                    throw new ParseException("expected ; in line : " + token.line + " pos : " + token.pos + " !");
                }
                return new Print(Print.InnerType.Pid, id.getName());
            } else if (token.type == Token.Type.NUM || token.type == Token.Type.STR || token.type == Token.Type.BOOL) {
                String str = token.value;
                token = nextToken();
                if (!token.value.equals(";")) {
                    throw new ParseException("expected ; in line : " + token.line + " pos : " + token.pos + " !");
                }
                return new Print(Print.InnerType.Pvalue, str);

            } else {
                throw new ParseException("Print error in line : " + token.line + " pos : " + token.pos + " !");
            }
        } else if (token.value.equals("if")) {
            token = nextToken();
            if (!token.value.equals("(")) {
                throw new ParseException("expected left bracket in line : " + token.line + " pos : " + token.pos + " !");
            }

            Judge condition = judgeParse();

            token = nextToken();
            if (!token.value.equals(")")) {
                throw new ParseException("expected right bracket in line : " + token.line + " pos : " + token.pos + " !");
            }

            Stmt then = blockParse();

            token = nextToken();
            if (token.value.equals("else")) {
                return new If(condition, then, blockParse());
            }

            index--;
            return new If(condition, then, null);
        }
        throw new ParseException("Parsing error bracket in line : " + token.line + " pos : " + token.pos + " !");
    }

    //------------------------------------------------- judge --------------------------------------------------

    private Judge judgeParse() throws ParseException {
        Judge re = joinParse();
        Token token = nextToken();
        while (token.value.equals("|")){
            re = new Or(re, joinParse());
            token = nextToken();
        }
        index--;
        return re;
    }

    private Judge joinParse() throws ParseException {
        Judge re = equalityParse();
        Token token = nextToken();
        while (token.value.equals("&")){
            re = new And(re, joinParse());
            token = nextToken();
        }
        index--;
        return re;
    }

    private Judge equalityParse() throws ParseException {
        Judge re = relParse();
        Token token = nextToken();
        while (token.value.equals("==") || token.value.equals("!=")){
            if(token.value.equals("==")){
                re = new Equal(re, relParse());
            }else{
                re = new Not(new Equal(re, relParse()));
            }
            token = nextToken();
        }
        index--;
        return re;
    }

    private Judge relParse() throws ParseException {
        Judge re = exprParse();
        Token token = nextToken();
        if(token.type == Token.Type.ROP) {
            switch (token.value) {
                case ">=":
                    return new Ge(re, exprParse());
                case "<=":
                    return new Le(re, exprParse());
                case ">":
                    return new G(re, exprParse());
                case "<":
                    return new L(re, exprParse());
                default:
                    throw new ParseException("Parsing error in line : " + token.line + " pos : " + token.pos + " !");
            }
        }else{
            index--;
            return re;
        }
    }

    //------------------------------------------------- expr --------------------------------------------------

    private Judge exprParse() throws ParseException {
        Judge re = termParse();
        Token token = nextToken();
        while(token.value.equals("+") || token.value.equals("-")){
            if(token.value.equals("+")){
                re = new Add(re, termParse());
            }else{
                re = new Minus(re, termParse());
            }
            token = nextToken();
        }
        index--;
        return re;
    }

    private Judge termParse() throws ParseException {
        Judge re = unaryParse();
        Token token = nextToken();
        while(token.value.equals("*") || token.value.equals("/")){
            if(token.value.equals("*")){
                re = new Times(re, unaryParse());
            }else{
                re = new Division(re, unaryParse());
            }
            token = nextToken();
        }
        index--;
        return re;
    }

    private Judge unaryParse() throws ParseException {
        Judge re;
        Token token = nextToken();
        if(token.value.equals("!")){
            re = new Not(unaryParse());
        }else if(token.value.equals("-")){
            re = new Negative(unaryParse());
        }else{
            index--;
            re = factorParse();
        }
        return re;
    }

    private Judge factorParse() throws ParseException {
        Token token = nextToken();
        if(token.type == Token.Type.NUM){
            return new Int(token.value);
        }else if(token.type == Token.Type.BOOL){
            return new Bool(token.value);
        }else if(token.type == Token.Type.STR){
            return new Str(token.value);
        }else if(token.type == Token.Type.ID){
            return new Id(typeJudge(token), token.value);
        }else if(token.value.equals("(")){
            Judge judge = judgeParse();
            token = nextToken();
            if(!token.value.equals(")")) {
                throw new ParseException("expected right bracket in line : " + token.line + " pos : " + token.pos + " !");
            }
            return judge;
        }else{
            throw new ParseException("Parsing error in line : " + token.line + " pos : " + token.pos + " !");
        }
    }

    //---------------------------------------  pretty print ---------------------------------------------

    public void printFun(){
        Fun cur = fun;
        while(cur != null){
            System.out.println(cur.type + " " + cur.id + "()" + "{");
            printStmt(cur.stmt);
            System.out.println("}");
            cur = cur.next;
        }
    }

    private void printStmt(Stmt stmt){
        while(stmt != null){
            switch (stmt.kind){
                case AssignStmt:
                    Assign assign = (Assign)stmt;
                    System.out.println(assign.getId() + " = " + assign.getJudge());
                    break;
                case DefStmt:
                    Def def = (Def)stmt;
                    System.out.println(def.getId() + " = " + def.getJudge());
                    break;
                case IfStmt:
                    If iff = (If) stmt;
                    System.out.println("if(" + iff.getCondition() + "){");
                    printStmt(iff.getThen());
                    System.out.println("}");
                    System.out.println("else{");
                    printStmt(iff.getOtherwise());
                    System.out.println("}");
                    break;
                case WhileStmt:
                    While wh = (While)stmt;
                    System.out.println("while(" + wh.getCondition() + "){");
                    printStmt(wh.getThen());
                    System.out.println("}");
                    break;
                case PrintStmt:
                    Print print = (Print)stmt;
                    String str = "print : ";
                    if(print.getInnerType() == Print.InnerType.Pid) {
                        str += "id :";
                    }
                    str += print.getName();
                    System.out.println(str);
            }
            stmt = stmt.next;
        }
    }
}



