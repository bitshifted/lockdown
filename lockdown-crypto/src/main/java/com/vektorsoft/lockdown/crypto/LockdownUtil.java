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

package com.vektorsoft.lockdown.crypto;

import java.math.BigInteger;
import java.util.Base64;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class LockdownUtil {

    public static  String toHexString(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    
    /**
     * Encodes given array as Base64 with ISO-8859-1 character set.
     * 
     * @param array byte array to encode
     * @return encoded array
     */
    public static String toBase64String(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }
    
    /**
     * Converts Base64 encoded string to byte array.
     * 
     * @param encoded
     * @return 
     */
    public static byte[] fromBase64String(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }
}
