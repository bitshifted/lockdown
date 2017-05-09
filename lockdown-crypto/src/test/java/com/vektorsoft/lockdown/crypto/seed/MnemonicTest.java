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
import com.vektorsoft.lockdown.crypto.seed.EntropyInfo;
import com.vektorsoft.lockdown.crypto.seed.Mnemonic;
import com.vektorsoft.lockdown.crypto.seed.MnemonicLanguage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;

/**
 * Test cases for {@link Mnemonic} class.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class MnemonicTest {

    @BeforeClass
    public static void init() {
        LockdownCrypto.instance().initialize(new BouncyCastleProvider());
    }
    
    /**
     * Run 100 rounds of menmonic generation and make sure each yields different words each time.
     * 
     * @throws Exception 
     */
    @Test
    public void mnemonicGeneration() throws Exception {
        Mnemonic mnemonic = new Mnemonic();
        Set<String> hashes = new HashSet<>();
        for(int i = 0;i < 100;i++) {
            String[] words = mnemonic.generateMnemonic(MnemonicLanguage.ENGLISH, EntropyInfo.ENTROPY_128_BITS);
            String out = Arrays.toString(words);
            
            hashes.add(out);
        }
        assertEquals(100, hashes.size());
        
    }
}
