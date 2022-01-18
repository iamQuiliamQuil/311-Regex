import java.util.*;
import java.util.LinkedList;
import java.util.Arrays;

public class StateMachine {


    LinkedList<Lexer.Lexeme> lexList;
    String currentExpression;

  String useLexemeAsState(Lexer.Lexeme state, char currChar){
    if(state.isLastLexeme){
      return "Success";
    } else if(state.contains(currChar)){
      if (state.hasStar){
        return "Maintain State";
      }
      return "Update State";
    } else if (state.hasStar) {
      return "Lambda";
    } else {
      return "Failure";
    }
  }

  Boolean hasRegexWithLambdaTransitions(char[] searchThrough, int startIndex){
    char currChar = searchThrough[startIndex];
    int stateName = 0;
    Lexer.Lexeme state;

    String stateTransition = "Placeholder so Java stops yelling at me";
    while (stateTransition != "Success" && stateTransition != "Failure"){

      state = lexList.get(stateName);
      if (startIndex >= searchThrough.length){
        currentExpression = "";
        return false;
      }
      currChar = searchThrough[startIndex];

      stateTransition = useLexemeAsState(state, currChar);

      switch (stateTransition){
        case "Update State":
          currentExpression+=currChar;
          stateName++;
          startIndex++;
          break;
        case "Maintain State":
          currentExpression+=currChar;
          startIndex++;
          break;
        case "Lambda":
          stateName++;
          break;
        case "Success":
          return true;
        case "Failure":
          currentExpression = "";
          return false;
        default:
          System.out.println("Something has gone horribly wrong in hasRegexWithLambdaTransitions(), StateMachine.java");
          return false;
      }
    }
    System.out.println("Something has gone horribly wrong in hasRegexWithLambdaTransitions(), StateMachine.java");
    return false; //should never happen
  }

  //must be called immediately after expression is found otherwise program will continue to stack expressions
  void printAndClearCurrentExpression(){
    System.out.println(currentExpression);
    currentExpression = "";
  }


    StateMachine(LinkedList<Lexer.Lexeme> lexList){
      this.lexList = lexList;
      currentExpression = "";
    }
}
