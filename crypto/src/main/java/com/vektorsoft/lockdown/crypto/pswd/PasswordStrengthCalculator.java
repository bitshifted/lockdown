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
 * Simple password strength calculator based on http://www.passwordmeter.com. It calculates password strength as 
 * a score from 0 to 100.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class PasswordStrengthCalculator {
    
    private static final Pattern MIDDLE_SYMBOLS_PATTER = Pattern.compile("[^A-Za-z]+");
    
    private int uppercaseCount = 0;
    private int lowercaseCount = 0;
    private int numbersCount = 0;
    private int symbolsCOunt = 0;

    /**
     * Calculates strength of a given password in range 0 to 100.
     * 
     * @param password password to measure
     * @return strength
     */
    public int calculateStrength(String password) {
        int score = 0;
        char[] chars = password.toCharArray();
        AdditionCriteria addCriteria = new AdditionCriteria(chars.length);
        for(char ch : chars) {
            int type = Character.getType(ch);
            switch(type) {
                case Character.LOWERCASE_LETTER:
                    lowercaseCount++;
                    break;
                case Character.UPPERCASE_LETTER:
                    uppercaseCount++;
                    break;
                case Character.DECIMAL_DIGIT_NUMBER:
                    numbersCount++;
                    break;
                    default:
                        symbolsCOunt++;
            }
        }
        addCriteria.lowercaseLetters(lowercaseCount);
        addCriteria.uppercaseLetters(uppercaseCount);
        addCriteria.numbers(numbersCount);
        addCriteria.symbols(symbolsCOunt);
        addCriteria.middleNumberOrSYmbol(findMiddleSymbolsCount(password));
        score += addCriteria.getAdditionScore();
        
        return score;
    }
    
    private int findMiddleSymbolsCount(String password) {
        Matcher matcher = MIDDLE_SYMBOLS_PATTER.matcher(password);
        int count = 0;
        while(matcher.find()) {
            String group = matcher.group();
            count += (group.length() - 1);
        }
        return count;
    }
    
}
