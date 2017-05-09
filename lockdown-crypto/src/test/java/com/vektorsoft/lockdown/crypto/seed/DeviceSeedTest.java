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
public class DeviceSeedTest {
    
    private static final String PASSWORD = "password";
    private static final String DEVICE_NAME = "Device";
    private static String[] mnemonicWords;
    private static byte[] seedBytes;
    private static byte[] encryptionSeedBytes;
    private static byte[] signingSeedBytes;
    private static byte[] authenticationSeedBytes;

    @BeforeClass
    public static void init() throws Exception {
        LockdownCrypto.instance().initialize(new BouncyCastleProvider());
        Mnemonic mnemonic = new Mnemonic();
        mnemonicWords = mnemonic.generateMnemonic(MnemonicLanguage.ENGLISH, EntropyInfo.ENTROPY_128_BITS);
        MasterSeed seed = new MasterSeed();
        seedBytes = seed.generateSeedFromMnemonic(mnemonicWords, PASSWORD, MnemonicLanguage.ENGLISH);
        
        DeviceSeed devSeed = new DeviceSeed();
        byte[] deviceSeedBytes = devSeed.generateDeviceSeed(seedBytes, DEVICE_NAME);
        encryptionSeedBytes = devSeed.getKeySeed(deviceSeedBytes, DeviceSeed.SeedType.ENCRYPTION);
        signingSeedBytes = devSeed.getKeySeed(deviceSeedBytes, DeviceSeed.SeedType.SIGNING);
        authenticationSeedBytes = devSeed.getKeySeed(deviceSeedBytes, DeviceSeed.SeedType.AUTHENTICATION);
    }
    
    /**
     * Run test 100 times and verify encryption seed is the same each time.
     */
    @Test
    public void deviceEncryptionSeedTest() throws Exception {
        DeviceSeed devSeed = new DeviceSeed();
        for(int i = 0;i < 100;i++) {
            byte[] devSeedBytes = devSeed.generateDeviceSeed(seedBytes, DEVICE_NAME);
            byte[] keySeed = devSeed.getKeySeed(devSeedBytes, DeviceSeed.SeedType.ENCRYPTION);
            assertArrayEquals(encryptionSeedBytes, keySeed);
        }
    }
    
    @Test
    public void deviceSigningSeedTest() throws Exception {
        DeviceSeed devSeed = new DeviceSeed();
        for(int i = 0;i < 100;i++) {
            byte[] devSeedBytes = devSeed.generateDeviceSeed(seedBytes, DEVICE_NAME);
            byte[] keySeed = devSeed.getKeySeed(devSeedBytes, DeviceSeed.SeedType.SIGNING);
            assertArrayEquals(signingSeedBytes, keySeed);
        }
    }
    
    @Test
    public void deviceAuthenticationSeedTest() throws Exception {
        DeviceSeed devSeed = new DeviceSeed();
        for(int i = 0;i < 100;i++) {
            byte[] devSeedBytes = devSeed.generateDeviceSeed(seedBytes, DEVICE_NAME);
            byte[] keySeed = devSeed.getKeySeed(devSeedBytes, DeviceSeed.SeedType.AUTHENTICATION);
            assertArrayEquals(authenticationSeedBytes, keySeed);
        }
    }
    
    /**
     * Verifies that wrong device name gives wrong seed.
     */
    @Test
    public void deviceSeedInvalidName() throws Exception {
         DeviceSeed devSeed = new DeviceSeed();
        byte[] devSeedBytes = devSeed.generateDeviceSeed(seedBytes, "some device");
        byte[] keySeed = devSeed.getKeySeed(devSeedBytes, DeviceSeed.SeedType.AUTHENTICATION);
        boolean result = true;
        for(int i = 0;i < authenticationSeedBytes.length;i++) {
            result &= (authenticationSeedBytes[i] == keySeed[i]);
        }
        assertFalse(result);
    }
}
