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

/**
 * Simple password strength calculator based on http://www.passwordmeter.com. It calculates password strength as 
 * a score from 0 to 100.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class PasswordStrengthCalculator {

    /**
     * Calculates strength of a given password in range 0 to 100.
     * 
     * @param password password to measure
     * @return strength
     */
    public static  int calculateStrength(String password) {
        int score = 0;
        score += AdditionCriteria.getAdditionScore(password);
        // deductions
        score += DeductionCriteria.getDeductionScore(password);
        if(score > 100) {
            score = 100;
        } else if (score < 0) {
            score = 0;
        }
        
        return score;
    }
    
    
    
}
