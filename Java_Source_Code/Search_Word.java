
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class Search_Word {
     public static void main( String[] args ) throws IOException{
     String str = "Hello my name is John and I like to go fishing and "+
                         "hiking I have two sisters and one brother.";
    String find = "I";

    String[] sp = str.split(" +"); // "+" for multiple spaces
    for (int i = 2; i < sp.length; i++) {
        if (sp[i].equals(find)) {
            // have to check for ArrayIndexOutOfBoundsException
            String surr = (i-2 > 0 ? sp[i-2]+" " : "") +
                          (i-1 > 0 ? sp[i-1]+" " : "") +
                          sp[i] +
                          (i+1 < sp.length ? " "+sp[i+1] : "") +
                          (i+2 < sp.length ? " "+sp[i+2] : "");
            System.out.println(surr);
        }
    }
     }
    
}
