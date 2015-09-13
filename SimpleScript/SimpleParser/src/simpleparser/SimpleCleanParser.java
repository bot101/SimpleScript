/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author user
 */
public class SimpleCleanParser {
   
  //Class Fields
  private String theLine, foundString;
  private Pattern scanner;
  private Matcher matcher;
  private final String [] keywords;
  
  //RegEx patterns
  
  //Is it a function or variable. Not up to stats though, still needs to be improved
  String funcVar  = "((?:[a-z][a-z]+))";
  
  //Variable manipulation patterns
  String varDec = ".*?var.*?((?:[a-z][a-z0-9_]*))";
  String varInitEq = ".*?(=)";
  String varInitAss = ".*?((?:[a-z][a-z0-9_]*)).*?((?:[a-z][a-z0-9_]*)).*?(=).*?((?:[a-z][a-z0-9_]*))"; //".*?(=).*?((?:[a-z][a-z0-9_]*))";
  String varMulInit = "(.*?),";
  
  //Function manipulation patterns
  String fDecNPNS = ".*?(?:[a-z][a-z0-9_]*).*?((?:[a-z][a-z0-9_]*))(\\(.*\\))";
  String fDecPNS = ".*?(?:[a-z][a-z0-9_]*).*?(?:[a-z][a-z0-9_]*).*?((?:[a-z][a-z0-9_]*))";
  String fDecMPNS = ".*?((?:[a-z][a-z0-9_]*),(?:[a-z][a-z0-9_]*))";
  String fDecNS = ".*?func.*?((?:[a-z][a-z0-9_]+)).*?(\\(\\)).*?(:)";  //".*?(?:[a-z][a-z0-9_]*).*?((?:[a-z][a-z0-9_]*))";
  String fDecS = ".*?(\\)).*?(:)";
  String fScope = "(a)"; //not yer functional
                          
  
  //Function Fields
 //-----------------
  private int level;
  String varFuncName;
  
  //Variable flags
  boolean isVar, isAssign, isArg, isMulAss, isEq;
  
  //Function flags
 boolean isFunc, isFuncName, isFuncSx, isFuncNP, isFuncP, isFuncMP;
  //Expression manipulation patterns
  
  //Expressions field
  boolean isEx;
  
  
  public SimpleCleanParser(){
        this.isAssign = false;
        this.isArg = false;
        this.isMulAss = false;
        this.isEq = false;
        this.isVar = false;
        this.keywords = new String[]{"var", "func", "ret"}; //Language keywords
  }

  public void getLine(String line){
      theLine = line;
      varOrFunc(theLine); //temporarilt call from here
  }
  //Variable Manipulation
  
  private void regExPrep(String pattern, String line){
      scanner = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                                 matcher = scanner.matcher(line);
  }
  
  private String varOrFunc(String line){ //to be made private later
      
      regExPrep(funcVar, line); //var keyword encountered
      if (matcher.find())
        {
            String match = matcher.group(1);//System.out.println(matcher.find());
            
            keywords:
            if(match.equals("var")){ //Why is matcher.group(1) throwing illegal state exception??
                isVar = true;
                
                regExPrep(varDec, line); //variable declaration
                if(matcher.find()){
                    System.out.println("Variable name: " + matcher.group(1));
                    //foundString = matcher.group(1);
                    for (String keyword : keywords) {
                        if (matcher.group(1).equals(keyword)) {
                            System.out.println("ERROR!!, keyword used in variable name!");
                            break keywords; //ERROR, keyword used in variable name!!!
                        }
                    }
                    isArg = true;
                } else {
                    //Error Syntax Error here
                }
                
                regExPrep(varInitEq, line); //variable declaration and equal operator              
                if (matcher.find())
                   {
                       System.out.println("Variable assignment initialized: " + matcher.group(1));
                       for (String keyword : keywords) {
                            if (matcher.group(1).equals(keyword)) {
                                System.out.println("ERROR!!, keyword used in assignment statement!");
                                break keywords; //ERROR, keyword used in variable name!!!
                            }
                        }
                       isEq = true;
                       
                }
                
                regExPrep(varInitAss, line); //variable declaration ant initialization          
                if (matcher.find())
                   {
                       System.out.println("Variable " + matcher.group(2) + " initialized to " + matcher.group(4));
                       for (String keyword : keywords) {
                                        if (matcher.group(4).equals(keyword)) {
                                System.out.println("ERROR!!, keyword used in assignment value!");
                                break keywords; //ERROR, keyword used in variable name!!!
                            }
                        }
                       isAssign = true;
                       
                }
                
                regExPrep(varMulInit, line); //variable multideclare      
                if (matcher.find())
                   {
                       for (String keyword : keywords) {
                            if (!matcher.group(1).equals(keyword)) {
                                System.out.println("ERROR!!, keyword used in multiple assignment statements!");
                                break keywords; //ERROR, keyword used in variable name!!!
                            }
                        }
                       isMulAss = true;
                       
                }
                
        }
        
            //########################Function Manipulation###############
           // if(matcher.find()){
               keywords:
                if(match.equals("func")){ //
                    System.out.println("Func found");
                    isFunc = true;
                    regExPrep(fDecNS, line); //func vari syntax check  
                        if (matcher.find())
                           {
                               System.out.println("Func name found");
                               for (String keyword : keywords) {
                                    if (matcher.group(1).equals(keyword)) {
                                        System.out.println(matcher.group(1) + " ERROR!!, Keyword used in function name!");
                                        break keywords;
                                    }
                                }
                               
                               System.out.println(matcher.group(2).charAt(0));
                               char open = matcher.group(2).charAt(0);
                               String l = matcher.group(3);
                               char close = matcher.group(3).charAt(l.length()-2);
                               System.out.println(close);
                               char colon = matcher.group(3).charAt(l.length()-1);
                               if (!(open == '(')) {
                                             System.out.println("ERROR!!, Syntax error: Expected '('");
                                             break keywords; 
                                         }
                               if(!isClosed()){
                                   
                               }

                               isFuncName = true;

                               regExPrep(fDecNPNS, line); //func declaration with no parameters
                               if (matcher.find())
                                {
                                    System.out.println("Func parameterless declaration");
                                    //System.out.println(matcher.group(3));
                                    for (String keyword : keywords) {
                                         if (matcher.group(1).equals(keyword)) {
                                             System.out.println("ERROR!!, Syntax 1 error in function declaration!");
                                             break keywords; 
                                         }
                                    }

                                }
                                isFuncNP = true;
                            }

                               regExPrep(fDecPNS, line); //func declaration with 1 parameter
                               if (matcher.find())
                                {
                                    System.out.println("Func single parameter declaration");                                    
                                    for (String keyword : keywords) {
                                         if (matcher.group(1).equals(keyword)) {
                                             System.out.println("ERROR!!, Keyword used in function parameter!");
                                             break keywords; //ERROR, keyword used in variable name!!!
                                         }
                                     }
                                    isFuncP = true;
                                }

                               regExPrep(fDecMPNS, line); //func declaration with multiparameters
                               if (matcher.find())
                                {
                                    System.out.println("Func multi parameter declaration");                                    
                                    for (String keyword : keywords) {
                                         if (matcher.group(1).equals(keyword)) {
                                             //Check if any of the multiple parameters are keywords here. 
                                             //Problem is how to get parameters
                                             break keywords; 
                                         }
                                     }
                                    
                                    isFuncMP = true;
                                }
                        }
                }
                
            //}
         
            
   
      //Reset watchers somewhere here
      return msgConstructor();
  }
  public boolean isClosed(){
      regExPrep(fDecS, theLine); //func declaration with multiparameters
      return matcher.find();
  }
  
  public void isBraced(){
      
  }
  public void resetWatchers(){
      isVar = isAssign = isArg = isMulAss = isEq = false; //Reset variable watchers
  }
  
 public String msgConstructor(){
     String msg = "Empty line", msgCase;
     if(isVar || isAssign || isArg || isMulAss || isEq) msg = "Var keyword occured here";
     System.out.println(msg);
     return msg;
 }
}
