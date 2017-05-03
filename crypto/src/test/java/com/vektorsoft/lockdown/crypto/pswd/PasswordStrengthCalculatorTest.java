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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class PasswordStrengthCalculatorTest {
    
    public PasswordStrengthCalculatorTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void simplePasswordTest() {
        String pass = "password";
        PasswordStrengthCalculator calculator = new PasswordStrengthCalculator();
        int score = calculator.calculateStrength(pass);
        assertEquals(32, score);
        
        pass = "password123";
        calculator = new PasswordStrengthCalculator();
        score = calculator.calculateStrength(pass);
        assertEquals(66, score);
    }
    
}
