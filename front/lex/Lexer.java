package front.lex;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Lexer{
    private static final int BufMaxLen = 256;
    private int bufLen;
    private char[] buffer = new char[BufMaxLen];
    private int cursor;   // -------------------------->  the current lexing pos from buffer
    private Reader reader;
    private int line;
    private int pos;
    private StringBuilder sb = new StringBuilder();
    private boolean EOF = false;

    public ArrayList<Token> tokens = new ArrayList<>();

    public Lexer(Reader reader){
        this.reader = reader;
        line = 1;
        pos = 1;
        cursor = 0;
    }

    /**
     * when all the chars in the buffer has been analysed,  refresh the buffer to get more chars...
     */
    private void refresh() throws IOException {
        bufLen = reader.read(buffer, 0, BufMaxLen);
        if(bufLen == -1){
            EOF = true;
        }
        cursor = 0;
    }
    private int nextChar() throws IOException {
        if(cursor == bufLen){
            refresh();
        }

        if(!EOF) {
            return buffer[cursor++];
        }else{
            return -1;
        }
    }

    private void eatSpace() throws IOException {
        int ch;
        while(true){
            ch = nextChar();
            if(ch == -1){
                EOF = true;
                return;
            }else if(ch == '\n'){
                line++;
                pos = 1;
            }else if(Character.isWhitespace((char)ch)){
                pos++;
            }else {
                cursor--;
                return;
            }
        }
    }

    private boolean isIdentifier(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || isNumber(ch) || ch == '_';
    }

    private boolean isNumber(char ch){
        return ch >= '0' && ch <= '9';
    }

    private boolean isLogicalOperator(char ch){
        return ch == '=' || ch == '>' || ch == '<' || ch == '!';
    }

    private boolean isArithmeticOperator(char ch){
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private void strGenerate() throws IOException, LexException {
        boolean escape = false;
        nextChar();             // ---> ignore the left quote
        int ch;
        while(true){
            ch = nextChar();
            pos++;
            if(ch == -1 || (char)ch == '\n') {
                throw new LexException("expected \" in line : " + line + " pos : " + pos + " !");
            }else if((char)ch == '"' && !escape){
                tokens.add(new Token(Token.Type.STR ,sb.toString(), line, pos-1));
                sb = new StringBuilder();
                return;
            }else if((char)ch == '\\'){
                pos--;
                escape = true;
            }else{
                escape = false;
                sb.append((char)ch);
            }
        }
    }

    private void numGenerate() throws IOException, LexException {
        int ch;
        while(true){
            ch = nextChar();
            if(ch == -1) {
                throw new LexException("unexpected EOF in line : " + line + " pos : " + pos + " !");
            } else if(isNumber((char)ch)){
                pos++;
                sb.append((char)ch);
            } else{
                cursor--;
                tokens.add(new Token(Token.Type.NUM, sb.toString(), line, pos));
                sb = new StringBuilder();
                return;
            }
        }
    }

    private void idGenerate() throws LexException, IOException {
        int ch;
        while(true){
            ch = nextChar();
            if(ch == -1){
                throw new LexException("unexpected EOF in line : " + line + " pos : " + pos + " !");
            }else if(isIdentifier((char)ch)){
                pos++;
                sb.append((char)ch);
            }else{
                cursor--;
                tokens.add(new Token(Token.Type.ID, sb.toString(), line, pos));
                sb = new StringBuilder();
                return;
            }
        }
    }

    private void logGenerator() throws IOException {
        int temp1;
        int temp2;
        temp1 = nextChar();
        temp2 = nextChar();
        if(temp1 == '='){
            if(temp2 == '='){
                pos += 2;
                tokens.add(new Token(Token.Type.LOP, "==", line, pos));
            }else{
                cursor--;
                pos++;
                tokens.add(new Token(Token.Type.ASSIGN, "=", line, pos));
            }
        }else if(temp1 == '>'){
            if(temp2 == '='){
                pos += 2;
                tokens.add(new Token(Token.Type.ROP, ">=", line, pos));
            }else{
                cursor--;
                pos++;
                tokens.add(new Token(Token.Type.ROP, ">", line, pos));
            }
        }else if(temp1 == '<') {
            if (temp2 == '=') {
                pos += 2;
                tokens.add(new Token(Token.Type.ROP, "<=", line, pos));
            } else {
                cursor--;
                pos++;
                tokens.add(new Token(Token.Type.ROP, "<", line, pos));
            }
        }else if(temp1 == '!'){
            if(temp2 == '='){
                pos += 2;
                tokens.add(new Token(Token.Type.LOP, "!=", line, pos));
            } else{
                cursor--;
                pos++;
                tokens.add(new Token(Token.Type.LOP, "!", line, pos));
            }
        }
    }

    private void ariGenerator() throws IOException {
        int ch = nextChar();
        pos++;
        switch ((char)ch){
            case '+':
                tokens.add(new Token(Token.Type.AOP, "+", line, pos));
                return;
            case '-':
                tokens.add(new Token(Token.Type.AOP, "-", line, pos));
                return;
            case '*':
                tokens.add(new Token(Token.Type.AOP, "*", line, pos));
                return;
            case '/':
                tokens.add(new Token(Token.Type.AOP, "/", line, pos));
                return;
        }
    }


    //-----------------------------------------------------------------------------------------


    public void lex() throws IOException, LexException {
        int ch;
        while(true){
            eatSpace();
            ch = nextChar();
            if(ch == -1){
                return;
            }
            switch ((char)ch){
                case ';':
                    pos++;
                    tokens.add(new Token(Token.Type.SEMI, ";", line, pos));
                    break;
                /**
                 *      bracket...
                 */
                case '{':
                    pos++;
                    tokens.add(new Token(Token.Type.BRA, "{", line, pos));
                    break;
                case '}':
                    pos++;
                    tokens.add(new Token(Token.Type.BRA, "}", line, pos));
                    break;
                case '(':
                    pos++;
                    tokens.add(new Token(Token.Type.BRA, "(", line, pos));
                    break;
                case ')':
                    pos++;
                    tokens.add(new Token(Token.Type.BRA, ")", line, pos));
                    break;

                case '|':
                    pos++;
                    tokens.add(new Token(Token.Type.LOP, "|", line, pos));
                    break;

                case '&':
                    pos++;
                    tokens.add(new Token(Token.Type.LOP, "&", line, pos));
                    break;

                /**
                 *      string ...
                 */

                case '"':
                    cursor--;
                    strGenerate();
                    break;

                /**
                 *      number first then identifier to simplify checking process...
                 */
                default:
                    cursor--;
                    if(isLogicalOperator((char)ch)){
                        logGenerator();
                    }else if(isArithmeticOperator((char)ch)){
                        ariGenerator();
                    } else if(isNumber((char)ch)){
                        numGenerate();
                    }else if(isIdentifier((char)ch)) {
                        idGenerate();
                    }else{
                        throw new LexException("Unexpected character '" + (char)ch + "' at line " + line + " pos " + pos + " !");
                    }
            }
        }
    }

    //------------------------------------------------------------------------------

    public ArrayList<Token> getTokens(){
        return tokens;
    }

    public void print(){
        for(Token each : tokens){
            System.out.println(each);
        }
    }
}

