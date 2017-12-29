package com.halcyon.mule;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PalLongString {

    public static void main(String args[])
    {
        System.out.println("enter the long string with palindromes");
        Scanner scanner = new Scanner(System.in);
        String listString = scanner.nextLine();
        List<String> palString = new ArrayList<String>();
        String[] splitSpace = listString.split(" ");
        for(int i=0;i<splitSpace.length;i++) {
         palString.add(getStringReverse(splitSpace[i]));
        }
        String largePal= "";
        for(String item:palString)
            if(item!=null) {
                if(item.length()>largePal.length())
                {
                    largePal=item;
                }
            }
        System.out.println(largePal);//it will print the large palindrome
        }

    public static String getStringReverse(String stringName)
    {
        char[] charArray = stringName.toCharArray();
        String revString ="";
        for(int j=stringName.length()-1;j>=0;j--)
        {
            revString = revString+(charArray[j]);
        }
        if(revString.equalsIgnoreCase(stringName))
        {
            return revString;
        }
        return null;
    }
}
