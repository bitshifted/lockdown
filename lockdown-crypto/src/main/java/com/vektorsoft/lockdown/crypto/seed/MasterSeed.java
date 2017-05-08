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

package com.vektorsoft.lockdown.crypto.seed;

import com.vektorsoft.lockdown.crypto.LockdownCrypto;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.Normalizer;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * Generates master seed for keyring from mnemonic words.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class MasterSeed {
    
    private static final int PBKDF_ITERATIONS = 2048;
    private static final Normalizer.Form NORMALIZATION_FORM = Normalizer.Form.NFKD;
    private static final String HMAC_ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int SEED_LENGTH = 512;
    private static final String PASSWORD_PREFIX = "mnemonic";

    public byte[] generateSeedFromMnemonic(String[] mnemonic, String password, MnemonicLanguage language) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Wordlist wordlist = new Wordlist();
        wordlist.loadWordlist(language);
        wordlist.validateWords(mnemonic);
        
        StringBuilder sb = new StringBuilder();
        for(String m : mnemonic) {
            sb.append(m);
        }
        
        StringBuilder saltBuilder = new StringBuilder(PASSWORD_PREFIX);
        if(password != null && !password.isEmpty()) {
            saltBuilder.append(password);
        }
        
        String normalizedPassword = Normalizer.normalize(sb.toString(), NORMALIZATION_FORM);
        String normalizedSalt = Normalizer.normalize(saltBuilder.toString(), NORMALIZATION_FORM);
        // initialize key generation
        PBEKeySpec keySpec = new PBEKeySpec(normalizedPassword.toCharArray(), normalizedSalt.getBytes(), PBKDF_ITERATIONS, SEED_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(HMAC_ALGORITHM, LockdownCrypto.instance().getProvider());
        byte[] seed = factory.generateSecret(keySpec).getEncoded();
        
        return seed;
        
    }
    
    public String generateSeedAsHexString(String[] mnemonic, String password, MnemonicLanguage language) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = generateSeedFromMnemonic(mnemonic, password, language);
        return toHexString(bytes);
    }
    
    private String toHexString(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
