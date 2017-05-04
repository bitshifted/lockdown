/*
 * Copyright (C) 2017 Vektorsoft Ltd. (http://www.vektorsoft.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vektorsoft.lockdown.crypto.pswd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains criteria which add to password strength score.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class AdditionCriteria {

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]+");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]+");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");
    private static final Pattern SYMBOLS_PATTERN = Pattern.compile("[^A-Za-z0-9]+");
    private static final Pattern MIDDLE_SYMBOLS_PATTERN = Pattern.compile("(?<=[A-Za-z0-9]+)[^A-Za-z]+");
    
    private static final int MIN_LENGTH = 8;
    private static final int MIN_REQUIREMENTS = 3;

    
    private static int lowercaseLetters(String password) {
        int count = 0;
        int len = password.length();
        Matcher matcher = LOWERCASE_PATTERN.matcher(password);
        while(matcher.find()) {
            count += matcher.group().length();
        }
        if(count > 0) {
            return (len - count) * 2;
        }
        return count;
        
    }
    
     private static int uppercaseLetters(String password) {
        int count = 0;
        int len = password.length();
        Matcher matcher = UPPERCASE_PATTERN.matcher(password);
        while(matcher.find()) {
            count += matcher.group().length();
        }
        if(count > 0) {
            return (len - count) * 2;
        }
        return count;
    }
     
     private static int numbers(String password) {
        int count = 0;
        Matcher matcher = NUMBER_PATTERN.matcher(password);
        while(matcher.find()) {
            count += matcher.group().length();
        }
        return count * 4;
     }
     
     private static int symbols(String password) {
        int count = 0;
        Matcher matcher = SYMBOLS_PATTERN.matcher(password);
        while(matcher.find()) {
            count += matcher.group().length();
        }
        return count * 6;
     }
     
     private static int middleSymbols(String password) {
        int count = 0;
        Matcher matcher = MIDDLE_SYMBOLS_PATTERN.matcher(password);
        while(matcher.find()) {
            count += matcher.group().length() - 1;
        }
        return count * 2;
     }
     
    
    public static int getAdditionScore(String password) {
        int score = 0;
        int reqsMet = 0;
        score += password.length() * 4; // length criteria
        int tmp =  lowercaseLetters(password);
        score += tmp;
        if(tmp > 0) {
            reqsMet++; //contains lowercase letters
        }
        tmp = uppercaseLetters(password);
        score += tmp;
        if(tmp > 0) {
            reqsMet++; // contains uppercase letters
        }
        tmp = numbers(password);
        score += tmp;
        if(tmp > 0) {
            reqsMet++; // contains numbers
        }
        tmp = symbols(password);
        score += tmp;
        if(tmp > 0) {
            reqsMet++; // contains symbols
        }
        score += middleSymbols(password);
        if(password.length() >= MIN_LENGTH) {
            reqsMet++;
            if(reqsMet > MIN_REQUIREMENTS) {
                score += reqsMet * 2;
            }
        }
        
        return score;
    }

}
