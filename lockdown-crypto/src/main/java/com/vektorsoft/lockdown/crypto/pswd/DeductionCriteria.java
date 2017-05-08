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
 * Contains criteria which deduct from password strength score.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class DeductionCriteria {
    
    private static final Pattern LETTERS_ONLY_PATTERN = Pattern.compile("^[A-Za-z]+$");
    private static final Pattern NUMBERS_ONLY_PATTERN = Pattern.compile("^[0-9]+$");
    private static final Pattern CONSECUTIVE_LOWERCASE_PATTERN = Pattern.compile("[a-z][a-z]+");
    private static final Pattern CONSECUTIVE_UPPERCASE_PATTERN = Pattern.compile("[A-Z][A-Z]+");
    private static final Pattern CONSECUTIVE_NUMBERS_PATTERN = Pattern.compile("[0-9][0-9]+");

    
    private static int lettersOnly(String password){
        int score = 0;
        Matcher matcher = LETTERS_ONLY_PATTERN.matcher(password);
        if(matcher.matches()) {
            score -= password.length();
        }
        return score;
    }
    
    private static  int numbersOnly(String password) {
        int score = 0;
        Matcher matcher = NUMBERS_ONLY_PATTERN.matcher(password);
        if(matcher.matches()) {
            score -= password.length();
        }
        return score;
    }
    
    private static int consecutiveLowercase(String password) {
        Matcher matcher = CONSECUTIVE_LOWERCASE_PATTERN.matcher(password);
        int sum = 0;
        while(matcher.find()) {
            sum -= (matcher.group().length() - 1) * 2;
        }
        return sum;
    }
    
    private static int consecutiveUppercase(String password) {
        Matcher matcher = CONSECUTIVE_UPPERCASE_PATTERN.matcher(password);
        int sum = 0;
        while(matcher.find()) {
            sum -= (matcher.group().length() - 1) * 2;
        }
        return sum;
    }
    
     private static int consecutiveNumbers(String password) {
        Matcher matcher = CONSECUTIVE_NUMBERS_PATTERN.matcher(password);
        int sum = 0;
        while(matcher.find()) {
            sum -= (matcher.group().length() - 1) * 2;
        }
        return sum;
    }
    
    public static int getDeductionScore(String password) {
        int score = 0;
        score += lettersOnly(password);
        score += numbersOnly(password);
        score += consecutiveLowercase(password);
        score += consecutiveUppercase(password);
        score += consecutiveNumbers(password);
        
        return score;
    }
    
}
