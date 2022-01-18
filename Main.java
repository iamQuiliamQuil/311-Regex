import java.util.*;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main{

  public static void main(String[] args) {
    //args[0] should be regex
    Lexer lexer = new Lexer(args[0]);
    StateMachine stateMachine = new StateMachine(lexer.lexList);

    String filepath;
    //if user gave a filepath as a third argument
    if(args.length > 2){
      filepath = args[2];
    } else {
      filepath = System.getProperty("user.dir");
    }
    //args[1] should be filename
    Path path = Paths.get(filepath, args[1]);

  Charset charset = Charset.forName("ISO-8859-1");
  try {
    List<String> lines = Files.readAllLines(path, charset);
    int lineNumber = 0;
    for (String line : lines) {
      //appends carriage return becasue it is "safe charachter" that regex will never look for
      //carriage return at end of line prevents errors by forcing a lambda transition at the end,
      //thus terminating star and plus regexes before the end of line is reached and expression is considered invalid
      line += '\n';
      char[] lineCharArray = line.toCharArray();

          int positionStart = 0, positionEnd = 0;
      lineNumber++;
      for(int i = 0; i < lineCharArray.length; i++){
        if(stateMachine.hasRegexWithLambdaTransitions(lineCharArray, i)){
          positionStart = i+1;
          positionEnd = positionStart + stateMachine.currentExpression.length() - 1;
          System.out.println("Match found on line "+lineNumber+", starting at posistion "+positionStart+" and ending at "+positionEnd+":");
          stateMachine.printAndClearCurrentExpression();
          break;
        }
      }
    }
  } catch (IOException error) {
    System.out.println(error);
  }
  }
}
