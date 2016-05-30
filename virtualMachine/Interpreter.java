package virtualMachine;

import front.ast.Fun;
import front.lex.LexException;
import front.lex.Lexer;
import front.parse.ParseException;
import front.parse.Parser;
import front.parse.TypeCheck;
import front.parse.TypeException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Interpreter {
    public static void main(String[] args) {
        File fh = new File(Config.SRC);

        /**
         *  sleep is just for fun, it's free to delete ...
         */
        try {
            System.out.println("----------------->   Check your syntax ...");
            Lexer lexer = new Lexer(new FileReader(fh));
            lexer.lex();
            //Thread.sleep(1000);
            System.out.println("----------------->   Finish ...\n\n\n");
            System.out.println("----------------->   Generate the abstract syntax tree ...");
            Parser parser = new Parser(lexer.getTokens());
            Fun fun = parser.parse();
            //Thread.sleep(1000);
            System.out.println("----------------->   Finish ...\n\n\n");
            TypeCheck typeCheck = new TypeCheck(fun);
            System.out.println("----------------->   Type check ...");
            typeCheck.funCheck();
            //Thread.sleep(1000);
            System.out.println("----------------->   Have generated the aim code successfully !!!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (LexException e) {
            e.printStackTrace();
            System.err.println("Please correct your code !");
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Please correct your code !");
        } catch (TypeException e) {
            e.printStackTrace();
            System.err.println("Please correct your code !");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

        System.out.println("\n\n-----------------   Running result   ----------------- \n\n");

        StackMachine.run();
    }

}
