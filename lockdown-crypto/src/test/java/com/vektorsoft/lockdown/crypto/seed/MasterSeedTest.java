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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class MasterSeedTest {
    
    private static final String PASSWORD = "password";
    private static String[] mnemonicWords;
    private static byte[] seedBytes;
    
    @BeforeClass
    public static void init() throws Exception {
        LockdownCrypto.instance().initialize(new BouncyCastleProvider());
        Mnemonic mnemonic = new Mnemonic();
        mnemonicWords = mnemonic.generateMnemonic(MnemonicLanguage.ENGLISH, EntropyInfo.ENTROPY_128_BITS);
        MasterSeed seed = new MasterSeed();
        seedBytes = seed.generateSeedFromMnemonic(mnemonicWords, PASSWORD, MnemonicLanguage.ENGLISH);
    }

    /**
     * Runs the test 100 times and verify same seed is generated each time.
     * 
     * @throws Exception if an error occurs
     */
    @Test
    public void seedGenerationTest() throws Exception {
        for(int i = 0;i < 100;i++) {
            MasterSeed seed = new MasterSeed();
            byte[] bytes = seed.generateSeedFromMnemonic(mnemonicWords, PASSWORD, MnemonicLanguage.ENGLISH);
            assertArrayEquals(seedBytes, bytes);
        }
    }
}
