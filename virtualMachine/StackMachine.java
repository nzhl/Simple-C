package virtualMachine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Stack;

public class StackMachine {
    private static Stack aluStack = new Stack();
    private static HashMap memory = new HashMap();
    private static BufferedReader bf;
    private static boolean whileFlag = false;


    public static void run(){
        try {
            bf = new BufferedReader(new FileReader(Config.OUT));
            alu();
        } catch (FileNotFoundException e) {
            System.err.println("Can not find the output file, please check if you have generated the output file !");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alu() throws Exception {
        String temp;
        String temp1;

        while((temp = bf.readLine()) != null) {
            if (temp.startsWith("push")) {
                temp1 = temp.substring(5);
                if (Character.isDigit(temp1.charAt(0))) {
                    aluStack.push(Integer.parseInt(temp1));
                } else {
                    if (temp1.equals("True")) {
                        aluStack.push(true);
                    } else if (temp1.equals("False")) {
                        aluStack.push(false);
                    } else {
                        aluStack.push(temp1);
                    }
                }
            } else if (temp.startsWith("load")) {
                temp1 = temp.substring(5);
                aluStack.push(memory.get(temp1));
            } else if (temp.startsWith("store")) {
                temp1 = temp.substring(6);
                memory.put(temp1, aluStack.pop());

                /**
                 *      those first object to be pushed first the the right size of the operator...
                 */
            } else if (temp.equals("add")) {
                int t = (int) aluStack.pop() + (int) aluStack.pop();
                aluStack.push(t);
            } else if (temp.equals("minus")) {
                int t = (int) aluStack.pop();
                aluStack.push((int)aluStack.pop() - t);
            } else if (temp.equals("times")) {
                int t = (int) aluStack.pop() * (int) aluStack.pop();
                aluStack.push(t);
            } else if (temp.equals("division")) {
                try {
                    int t = (int) aluStack.pop();
                    aluStack.push((int)aluStack.pop() / t);
                } catch (ArithmeticException e) {
                    System.out.println("Can not divide by zero !");
                    System.exit(0);
                }
            } else if (temp.equals("ge")) {
                int t = (int) aluStack.pop();
                aluStack.push((int)aluStack.pop() >= t);
            } else if (temp.equals("le")) {
                int t = (int) aluStack.pop();
                aluStack.push((int)aluStack.pop() <= t);
            } else if (temp.equals("g")) {
                int t = (int) aluStack.pop();
                aluStack.push((int)aluStack.pop() > t);
            } else if (temp.equals("l")) {
                int t = (int) aluStack.pop();
                aluStack.push((int)aluStack.pop() < t);
            } else if (temp.equals("equal")) {
                aluStack.push(aluStack.pop().equals(aluStack.pop()));
            } else if (temp.equals("and")) {
                boolean t = (boolean) aluStack.pop();
                aluStack.push((boolean)aluStack.pop() && t);
            } else if (temp.equals("or")) {
                boolean t = (boolean) aluStack.pop();
                aluStack.push((boolean)aluStack.pop() || t);
            } else if (temp.equals("not")) {
                aluStack.push(!(boolean) aluStack.pop());
            } else if (temp.equals("negative")) {
                aluStack.push(-(int) aluStack.pop());
            } else if (temp.equals("print")) {
                System.out.println(aluStack.pop());
            } else if (temp.equals("if")) {
                int counter = 1;
                if (!(boolean) aluStack.pop()) {
                    while(counter != 0){
                        temp = bf.readLine();
                        if(temp.equals("if")){
                            counter++;
                        }else if(temp.equals("otherwise")){
                            counter--;
                        }
                    }
                }
            } else if(temp.equals("otherwise")){
                while(true){
                    temp = bf.readLine();
                    if(temp.equals("end")){
                        break;
                    }
                }
            } else if(temp.equals("while")){
                bf.mark(10000);
            } else if(temp.equals("then")){
                if((boolean)aluStack.pop()){
                    whileFlag = true;
                }else{
                    whileFlag = false;
                    while(!bf.readLine().equals("end"));
                }
            } else if (temp.equals("end") || temp.startsWith(".")){
                if(whileFlag){
                    bf.reset();
                }
            }else{
                throw new Exception("Unexpected error !");
            }
        }
    }
}
