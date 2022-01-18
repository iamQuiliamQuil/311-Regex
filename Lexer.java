import java.util.*;
import java.util.LinkedList;
import java.util.Arrays;

public class Lexer {

  LinkedList<Lexeme> lexList;

  //allows me to pass an iterator by reference
  static class IntWrapper{
    int i;

    void plusEquals(int toBeAdded){
      i += toBeAdded;
    }
      IntWrapper(int i){
        this.i = i;
      }
  }

  public static class Lexeme{
    char[] letters;
    Boolean hasStar;
    Boolean isLastLexeme;

    void giveStar(){
      hasStar = true;
    }

    void indicateLast(){
      isLastLexeme = true;
    }

    Boolean contains(char lookFor){
      for (char current: letters){
        if (current == lookFor){
          return true;
        }
      }
      return false;
    }

    Lexeme(char[] letters){
      this.letters = letters;
      hasStar = false;
      isLastLexeme = false;
    }
  }

  void lexRegex(String regex){
    //carriage return is appended to facilitate 'else' case in State Table
    regex += '\n';
    int length = regex.length();
    char regexCharArray[] = new char[length];
    regexCharArray = regex.toCharArray();

    Lexeme currentLexeme;

    //no iterator in for() becasue different lexemes have different amounts
    //of chars and therefore need different amounts of interation
    for (IntWrapper iWrap = new IntWrapper(0); iWrap.i < length; ){
      switch(regexCharArray[iWrap.i]){
        case'[':
          lexList.add(buildBracketsLexeme(regexCharArray, iWrap));
          break;
        case'*':
          currentLexeme = lexList.getLast();
          currentLexeme.giveStar();
          //note - this means that a lexeme with brackets can have a star
          iWrap.plusEquals(1);
          break;
        case'+':
          //convert a+ to aa*
          replicateLastNode();
          currentLexeme = lexList.getLast();
          currentLexeme.giveStar();
          iWrap.plusEquals(1);
          break;
        case'\n':
          lexList.add(buildEndLexeme(iWrap));
          break;
        default:
          lexList.add(buildLiteralLexeme(regexCharArray, iWrap));
          break;
      }
    }
  }

  void replicateLastNode(){
    Lexeme currentLexeme = lexList.getLast();
    Lexeme clone = new Lexeme(currentLexeme.letters);
    lexList.add(clone);
  }

  Lexeme buildBracketsLexeme(char[] regexCharArray, IntWrapper iWrap){
    int lengthInBrackets = 0;
    for(int j = 1; regexCharArray[iWrap.i+j] != ']'; j++){
      lengthInBrackets++;
    }

    char letters[] = new char[lengthInBrackets];
    for (int j = 0; j < lengthInBrackets; j++){
      letters[j] = regexCharArray[iWrap.i+j+1];
    }
    Lexeme bracketsLexeme = new Lexeme(letters);

    iWrap.plusEquals(lengthInBrackets+2);

    return bracketsLexeme;
  }

  Lexeme buildLiteralLexeme(char[] regexCharArray, IntWrapper iWrap){
    //storing one and only one value in
    char letters[] = new char[1];
    letters[0] = regexCharArray[iWrap.i];
    Lexeme bracketsLexeme = new Lexeme(letters);

    iWrap.plusEquals(1);

    return bracketsLexeme;
  }

  Lexeme buildEndLexeme(IntWrapper iWrap){
    char letters[] = new char[0];
    Lexeme endLexeme = new Lexeme(letters);
    endLexeme.indicateLast();

    iWrap.plusEquals(1);

    return endLexeme;
  }

  Lexer(String regex){
    lexList = new LinkedList<Lexeme>();
    lexRegex(regex);
  }
}
