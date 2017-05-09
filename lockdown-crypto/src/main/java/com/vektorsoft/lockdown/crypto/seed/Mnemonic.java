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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.BitSet;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class Mnemonic {
    
    private static final int WORD_INDEX_LEN = 11;

    /**
     * 
     * @param language language for mnemonic words
     * @param entropyInfo entropy information
     * @return
     * @throws IOException 
     * @throws NoSuchAlgorithmException
     */
    public String[] generateMnemonic(MnemonicLanguage language, EntropyInfo entropyInfo) throws IOException, NoSuchAlgorithmException{
        Wordlist wordlist = new Wordlist();
        wordlist.loadWordlist(language);
        
        // generate random entropy
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] entropyBytes = new byte[entropyInfo.getEntropyLength()/8];
        random.nextBytes(entropyBytes);
        BitSet entropyBits = BitSet.valueOf(entropyBytes); // bit set of entropy 
        
        // calculate SHA-265 hash of entropy data
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(entropyBytes);
        BitSet hashBits = BitSet.valueOf(hash); // bit set containing hash bits
        
        // append checksum bits to entropy bits
        BitSet allbits = new BitSet(entropyInfo.totalLength());
        for(int i = 0; i < entropyInfo.getChecksumLength(); i++) {
            // set checksum data at first checksumLength indexes
            if(hashBits.get(i)) {
                allbits.set(i);
            }
        }
        // set entropy bits at remaining indices
        int k = entropyInfo.getChecksumLength();
        for(int i = 0; i < entropyBits.size();i++) {
            if(entropyBits.get(i)) {
                allbits.set(k + i);
            }
        }
        
        String[] words = new String[entropyInfo.getWordCount()];
        int  bitIndex = 0;
        for(int i = 0;i < words.length;i++) {
            long[] arr = allbits.get(bitIndex, bitIndex + WORD_INDEX_LEN).toLongArray();
            long value = arr.length > 0 ? arr[0] : 0; // if bit set is all 0s, array is empty. Set value to 0 in that cse
            words[i] = wordlist.getWord((int)value);
            bitIndex += WORD_INDEX_LEN;
        }
        
        return words;
    }
}
