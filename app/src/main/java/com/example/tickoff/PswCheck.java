package com.example.tickoff;

import java.util.regex.Pattern;

public class PswCheck {
    public static boolean pwdDigitCheck(String password){
        Pattern digit = Pattern.compile("[0-9]");
        if (digit.matcher(password).find()){
            return true;
        }
        return false;
    }

    public static boolean pwdLowercaseCheck(String password){
        Pattern lowercase = Pattern.compile("[a-z]");
        if (lowercase.matcher(password).find()){
            return true;
        }
        return false;
    }

    public static boolean pwdUppercaseCheck(String password){
        Pattern uppercase = Pattern.compile("[A-Z]");
        if (uppercase.matcher(password).find()){
            return true;
        }
        return false;
    }

    public static boolean pwdSpecialCharacterCheck(String password){
        String[] specialCharacters = {"!", "\"", "#" , "$", "%", "&", "\\'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";",
                "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"};

        for (int i = 0; i < specialCharacters.length; i++) {
            if (password.contains(specialCharacters[i])){
                return true;
            }
        }
        return false;
    }
}
