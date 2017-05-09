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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.Normalizer;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Generates seed for particular device, from master seed. This seed is used to generate keys for device.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class DeviceSeed {
    
    private static final int DEVICE_SEED_SIZE = 768;
    private static final int DEVICE_SALT_SIZE = 16;
    private static final int KEY_SEED_SIZE = 32;
    private static final String HMAC_ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int SALT_LENGTH = 512;
    private static final Normalizer.Form NORMALIZATION_FORM = Normalizer.Form.NFKD;
    private static final int PBKDF_ITERATIONS = 4096;
    
    public static enum SeedType {
        SIGNING (0), 
        ENCRYPTION (32),
        AUTHENTICATION (64);
        
        private final int position;
        
        private SeedType(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
        
    }

    /**
     * Generates seed for the device based on master seed and device name. Device name should be unique for
     * the user, ie. user can have multiple devices, but each must have unique name.
     * 
     * 
     * @param masterSeed
     * @param deviceName
     * @return byte array which represents 768-bit seed
     */
    public byte[] generateDeviceSeed(byte[] masterSeed, String deviceName) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] deviceSalt = generateDeviceSalt(masterSeed);
        // use PBKDF to generate random bytes as salt for device seed
        String deviceNameNormal = Normalizer.normalize(deviceName, NORMALIZATION_FORM);
        PBEKeySpec keySpec = new PBEKeySpec(deviceNameNormal.toCharArray(), deviceSalt, PBKDF_ITERATIONS, SALT_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(HMAC_ALGORITHM, LockdownCrypto.instance().getProvider());
        byte[] salt = factory.generateSecret(keySpec).getEncoded();
        // use PBKDF to generate device seed
        String masterSeedString = Base64.getEncoder().encodeToString(masterSeed);
        masterSeedString = Normalizer.normalize(masterSeedString, NORMALIZATION_FORM);
        PBEKeySpec devKeySpec = new PBEKeySpec(masterSeedString.toCharArray(), salt, PBKDF_ITERATIONS, DEVICE_SEED_SIZE);
        SecretKeyFactory devFactory = SecretKeyFactory.getInstance(HMAC_ALGORITHM, LockdownCrypto.instance().getProvider());
        byte[] deviceSeed = devFactory.generateSecret(devKeySpec).getEncoded();
        
        return deviceSeed;
    }
    
    public byte[] getKeySeed(byte[] deviceSeed, SeedType type) {
        byte[] seed = new byte[KEY_SEED_SIZE];
        System.arraycopy(deviceSeed, type.position, seed, 0, KEY_SEED_SIZE);
        return seed;
    }
    
    /**
     * Generates salt bytes used to salt device name. This salt is based on master key, so it can be restored 
     * when needed from mnemonic words.
     * 
     * @param masterSeed master seed
     * @return byte array representing salt
     */
    private byte[] generateDeviceSalt(byte[] masterSeed) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(masterSeed);
        // take random 16 bytes from master seed as seed for salt PRNG
        byte[] saltSeed = new byte[DEVICE_SALT_SIZE];
        for(int i = 0;i < DEVICE_SALT_SIZE;i++) {
            saltSeed[i] = masterSeed[random.nextInt(64)];
        }
        // seed PRNG with generated seed
        random.setSeed(saltSeed);
        byte[] saltBytes = new byte[DEVICE_SALT_SIZE];
        random.nextBytes(saltBytes);
        return saltBytes;
    }
}
