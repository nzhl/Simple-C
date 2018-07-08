package front.parse;

import front.ast.Fun;
import front.ast.Type;
import front.ast.judge.*;
import front.ast.stmt.*;
import front.ast.value.Value;
import front.table.StackTable;
import virtualMachine.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class TypeCheck {
//    private Fun fun;
//    private PrintWriter pw;
//    /**
//     * TODO
//     SuperTable globalTable;   // ----------> save global variable ...
//     */
//    private StackTable stackTable;    // ----------> save local variable ...
//
//    public TypeCheck(Fun fun) throws IOException {
//        this.fun = fun;
//        pw = new PrintWriter(new BufferedWriter(new FileWriter(Config.OUT)));
//
//        stackTable = new StackTable();
//        stackTable.push(new HashMap<>());
//    }
//
//    private void generate(String str){
//        pw.println(str);
//        pw.flush();
//    }
//
//    private void close(){
//        pw.close();
//    }
//
//
//    private void defVariable(String name, Type type) throws TypeException {
//        if(stackTable.peek().containsKey(name)){
//            throw new TypeException("Redefine variable " + name + " !");
//        } else{
//            stackTable.peek().put(name,type);
//            switch (type){
//                case Bool:
//                    generate(".bool " + name);
//                    break;
//                case Int:
//                    generate(".int " + name);
//                    break;
//                case Str:
//                    generate(".string " + name);
//                    break;
//            }
//            generate("store " + name);
//        }
//    }
//
//    private void assignVariable(String name,Type type) throws TypeException {
//        if(!stackTable.peek().containsKey(name)){
//            throw new TypeException("Undefined variable " + name + " !");
//        }else if(stackTable.peek().get(name) != type){
//            throw new TypeException("Can not assign " + type + " to " + stackTable.peek().get(name) + " !");
//        } else{
//            generate("store " + name);
//        }
//    }
//
//    //---------------- type check and generate the code...........................-----------------------------
//
//    public void funCheck() throws TypeException {
//        Fun cur = fun;
//        /**
//         *  function call, TODO...
//         */
//        while(cur != null){
//            stmtCheck(cur.stmt);
//            cur = cur.next;
//        }
//
//        close();
//    }
//
//    private void stmtCheck(Stmt stmt) throws TypeException {
//        Type rhs;
//        boolean flag = true;
//
//        Stmt cur = stmt;
//        while(cur != null){
//            // DefStmt, AssignStmt, IfStmt, WhileStmt,PrintStmt
//
//            switch (cur.kind){
//                case DefStmt:
//                    Def def = (Def) cur;
//                    Id id = def.getId();
//                    rhs = judgeCheck(def.getJudge());
//                    if(id.getType() != rhs){
//                        throw new TypeException("Type does not match !", def);
//                    }else {
//                        defVariable(id.getName(), rhs);
//                    }
//                    break;
//                case AssignStmt:
//                    Assign assign = (Assign)cur;
//                    id = assign.getId();
//                    rhs = judgeCheck(assign.getJudge());
//                    assignVariable(id.getName(), rhs);
//                    break;
//                case PrintStmt:
//                    Print print = (Print)cur;
//                    if(print.getInnerType() == Print.InnerType.Pid){
//                        if(!stackTable.peek().containsKey(print.getName())){
//                            throw new TypeException("Can not print undefined variable " + print.getName() + " !");
//                        }else {
//                            generate("load " + print.getName());
//                            generate("print");
//                        }
//                    }else{
//                        generate("push " + print.getName());
//                        generate("print");
//                    }
//                    break;
//                case IfStmt:
//                    If iff = (If)cur;
//                    Type type = judgeCheck(iff.getCondition());
//                    if(type != Type.Bool){
//                        throw new TypeException("The condition of if must be boolean !");
//                    }else {
//                        generate("if");
//                        stmtCheck(iff.getThen());
//                        generate("otherwise");
//                        stmtCheck(iff.getOtherwise());
//                        generate("end");
//                    }
//                    break;
//                case WhileStmt:
//                    While wh = (While)cur;
//                    generate("while");
//                    type = judgeCheck(wh.getCondition());
//                    if(type != Type.Bool){
//                        throw new TypeException("The condition of while must be boolean !");
//                    }else {
//                        generate("then");
//                        stmtCheck(wh.getThen());
//                        generate("end");
//                    }
//                    break;
//            }
//            cur = cur.next;
//        }
//    }
//
//    private Type judgeCheck(Judge judge) throws TypeException {
//        Type left;
//        Type right;
//        switch (judge.kind){
//            case ADD:
//                Add add = (Add)judge;
//                left = judgeCheck(add.left);
//                right = judgeCheck(add.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of + !");
//                }else{
//                    generate("add");
//                }
//                return Type.Int;
//            case MINUS:
//                Minus minus = (Minus) judge;
//                left = judgeCheck(minus.left);
//                right = judgeCheck(minus.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of - !");
//                }else{
//                    generate("minus");
//                }
//                return Type.Int;
//            case TIMES:
//                Times times = (Times) judge;
//                left = judgeCheck(times.left);
//                right = judgeCheck(times.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of * !");
//                }else{
//                    generate("times");
//                }
//                return Type.Int;
//            case DIVISION:
//                Division division = (Division) judge;
//                left = judgeCheck(division.left);
//                right = judgeCheck(division.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of / !");
//                }else{
//                    generate("division");
//                }
//                return Type.Int;
//            case GE:
//                Ge ge = (Ge) judge;
//                left = judgeCheck(ge.left);
//                right = judgeCheck(ge.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of >= !");
//                }else{
//                    generate("ge");
//                }
//                return Type.Bool;
//            case LE:
//                Le le = (Le) judge;
//                left = judgeCheck(le.left);
//                right = judgeCheck(le.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of <= !");
//                }else{
//                    generate("le");
//                }
//                return Type.Bool;
//            case G:
//                G g = (G) judge;
//                left = judgeCheck(g.left);
//                right = judgeCheck(g.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of > !");
//                }else{
//                    generate("g");
//                }
//                return Type.Bool;
//            case L:
//                L l = (L) judge;
//                left = judgeCheck(l.left);
//                right = judgeCheck(l.right);
//                if(left != Type.Int || right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of < !");
//                }else{
//                    generate("l");
//                }
//                return Type.Bool;
//            case EQUAL:
//                Equal equal = (Equal) judge;
//                left = judgeCheck(equal.left);
//                right = judgeCheck(equal.right);
//                if(left != right){
//                    throw new TypeException("The type must be equal in both sides of == !");
//                }else{
//                    generate("equal");
//                }
//                return Type.Bool;
//            case AND:
//                And and  = (And) judge;
//                left = judgeCheck(and.left);
//                right = judgeCheck(and.right);
//                if(left != Type.Bool || right != Type.Bool){
//                    throw new TypeException("The type must be bool in both sides of & !");
//                }else{
//                    generate("and");
//                }
//                return Type.Bool;
//            case OR:
//                Or or  = (Or) judge;
//                left = judgeCheck(or.left);
//                right = judgeCheck(or.right);
//                if(left != Type.Bool || right != Type.Bool){
//                    throw new TypeException("The type must be bool in both sides of | !");
//                }else{
//                    generate("or");
//                }
//                return Type.Bool;
//            case NEGATIVE:
//                Negative negative = (Negative) judge;
//                right = judgeCheck(negative.follow);
//                if(right != Type.Int){
//                    throw new TypeException("The type must be int in both sides of - !");
//                }else{
//                    generate("negative");
//                }
//                return Type.Int;
//            case NOT:
//                Not not = (Not) judge;
//                right = judgeCheck(not.follow);
//                if(right != Type.Bool){
//                    throw new TypeException("The type must be bool in both sides of not !");
//                }else{
//                    generate("not");
//                }
//                return Type.Bool;
//            case ID:
//                Id id = (Id) judge;
//                generate("load " + id.getName());
//                return stackTable.peek().get(id.getName());
//            case VAL:
//                Value value = (Value) judge;
//                generate("push " + value);
//                return value.type;
//        }
//        throw new TypeException("Unexpected Error !");
//    }
}
