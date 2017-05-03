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
 * Contains criteria which add to password strength score.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class AdditionCriteria {

    private static final int MIN_LENGTH = 8;
    private static final int MIN_REQUIREMENTS = 3;

    private int additionScore = 0;
    private final int length;
    private int requirementsMet = 0;

    public AdditionCriteria(int length) {
        this.length = length;
        additionScore += (this.length * 4);
        if(length >= MIN_LENGTH) {
            requirementsMet++;
        }
    }

    public void uppercaseLetters(int num) {
        if (num >= 1) {
            additionScore += (length - num) * 2;
            requirementsMet++;
        }
        
    }

    public void lowercaseLetters(int num) {
        if (num >= 1) {
            additionScore += (length - num) * 2;
            requirementsMet++;
        }
    }

    public void numbers(int num) {
        if (num >= 1) {
            requirementsMet++;
            additionScore += num * 4;
        }
    }

    public void symbols(int num) {
        if (num >= 1) {
            requirementsMet++;
            additionScore += num * 6;
        }
    }
    
    public void middleNumberOrSYmbol(int num) {
        additionScore += num * 2;
    }

    public int getAdditionScore() {
        if(requirementsMet > MIN_REQUIREMENTS) {
            additionScore += requirementsMet * 2;
        }
        return additionScore;
    }

}
