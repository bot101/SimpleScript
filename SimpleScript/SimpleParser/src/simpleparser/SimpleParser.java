/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleparser;


/**
 *
 * @author user
 */
public class SimpleParser {

    /**
     * @param args the command line arguments
     */

    
    
    public void actions(String action){

        switch(action){
            case "var":
                System.out.println();
                break;
                
            case "func":
                System.out.println();
                break;
             
            case "eval":
                System.out.println();
                break;
        }//https://github.com/bot101/SimpleScript.git
    }//08058641865
    
        public static void tst(){
           SimpleCleanParser tP = new SimpleCleanParser(); tP.getLine("func nns(:");
           //DaParser dP = new DaParser(); dP.funcDecNoStart();  
        }
        
    public static void main(String[] args) {
        tst();
    }
    
}
