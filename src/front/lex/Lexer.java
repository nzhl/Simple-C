package front.lex;

import java.io.*;
import java.util.ArrayList;

public class Lexer{

    private static final int MAX_BUF_LEN = 256;
    private char[] buffer = new char[MAX_BUF_LEN];
    private int bufLen;
    /**
     * the current lexing column from buffer
     */
    private int cursor;
    private Reader reader;
    private int row;
    private int column;
    private boolean eof = false;

    private ArrayList<Token> tokens = new ArrayList<>();

    public Lexer(Reader reader){
        this.reader = reader;
        row = 1;
        column = 1;
        cursor = 0;
    }

    /**
     * when all the chars in the buffer has been analysed,  refresh the buffer to get more chars...
     */
    private void refresh() throws IOException {
        bufLen = reader.read(buffer, 0, MAX_BUF_LEN);
        if(bufLen == -1){
            eof = true;
        }
        cursor = 0;
    }
    private char getNext() throws IOException {
        if(cursor == bufLen){
            refresh();
        }

        if(!eof) {
            return buffer[cursor++];
        }else{
            return 0;
        }
    }
    private char peekNext() throws IOException {
        if(cursor == bufLen){
            refresh();
        }

        if(!eof) {
            return buffer[cursor];
        }else{
            return 0;
        }
    }

    private void eatSpace() throws IOException {
        while(true){
            char ch = getNext();
            if(eof){
                return;
            }else if(ch == '\n'){
                row++;
                column = 1;
            }else if(Character.isWhitespace(ch)){
                column++;
            }else {
                cursor--;
                return;
            }
        }
    }

    private void lexInlineComment() throws IOException {
        while (true) {
            char ch = getNext();
            if (ch == '\n') {
                row++;
                column = 1;
                return;
            }
            column++;
        }
    }

    private boolean isIdentifier(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || isNumber(ch) || ch == '_';
    }

    private boolean isNumber(char ch){
        return ch >= '0' && ch <= '9';
    }

    private void lexString() throws IOException, LexException {
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        while(true) {
            char ch = getNext();
            column++;
            if (eof) {
                throw new LexException("unexpected eof in row : " + row + " column : " + column + " !");
            }
            if (!escape) {
                if (ch == '\n') {
                    throw new LexException("unexpected \\n in row : " + row + " column : " + column + " !");
                }
                if (ch == '"') {
                    tokens.add(new Token(Token.Type.STRING, sb.toString(), row, column));
                    return;
                }
                if (ch == '\\') {
                    escape = true;
                }
                sb.append(ch);
            } else {
                if (ch == '\n') {
                    row++;
                    column = 1;
                    sb.deleteCharAt(sb.length() - 1);
                } else {
                    sb.append(ch);
                }
                escape = false;
            }
        }
    }

    private void lexNumber(char ch0) throws IOException, LexException {
        int num = ch0 - '0';
        while(true){
            char ch = getNext();
            if(eof) {
                throw new LexException("unexpected eof in row : " + row + " column : " + column + " !");
            } else if(isNumber(ch)){
                column++;
                num = num * 10 + (ch - '0');
            } else{
                cursor--;
                tokens.add(new Token(num, row, column));
                return;
            }
        }
    }

    private void lexID(char ch0) throws LexException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(ch0);
        while(true){
            char ch = getNext();
            if(eof){
                throw new LexException("unexpected eof in row : " + row + " column : " + column + " !");
            }else if(isIdentifier(ch)){
                column++;
                sb.append(ch);
            }else{
                cursor--;
                tokens.add(new Token(Token.Type.ID, sb.toString(), row, column));
                return;
            }
        }
    }

    //-----------------------------------------------------------------------------------------

    public void lex() throws IOException, LexException {
        while(true){
            eatSpace();
            char ch = getNext();
            if(eof){ return; }
            switch (ch){
                case ';': case ',':
                case '{': case '}':
                case '[': case ']':
                case '(': case ')':
                case ':':
                    tokens.add(new Token(Token.Type.PUNCTUATION, String.valueOf(ch), row, ++column));
                    break;
                case '+': case '@':
                case '*': case '/':
                case '~': case '.':
                    tokens.add(new Token(Token.Type.OPERATOR, String.valueOf(ch), row, ++column));
                    break;
                case '-':
                    if (peekNext() == '-') {
                        lexInlineComment();
                    } else {
                        tokens.add(new Token(Token.Type.OPERATOR, String.valueOf(ch), row, ++column));
                    }
                    break;
                case '=':
                    if (peekNext() == '>') {
                        getNext();
                        column += 2;
                        tokens.add(new Token(Token.Type.OPERATOR, "=>", row, column));
                    } else {
                        tokens.add(new Token(Token.Type.OPERATOR, String.valueOf(ch), row, ++column));
                    }
                    break;
                case '>': case '<':
                    if (peekNext() == '=') {
                        getNext();
                        column += 2;
                        tokens.add(new Token(Token.Type.OPERATOR, ch + "=", row, column));
                    } else if (ch == '<' && peekNext() == '-') {
                        getNext();
                        column += 2;
                        tokens.add(new Token(Token.Type.OPERATOR, "<-", row, column));
                    }
                    else {
                        tokens.add(new Token(Token.Type.OPERATOR, String.valueOf(ch), row, ++column));
                    }
                    break;
                case '"':
                    column++;
                    lexString();
                    break;
                default:
                    column++;
                    if(isNumber(ch)){
                        lexNumber(ch);
                    }else if(isIdentifier(ch)) {
                        lexID(ch);
                    }else{
                        throw new LexException("Unexpected character '" + ch + "' at row " + row + " column " + column + " !");
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

