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

package com.vektorsoft.lockdown.app.init;

import com.vektorsoft.lockdown.app.LockdownConstants;
import com.vektorsoft.lockdown.app.LockdownUtil;
import com.vektorsoft.lockdown.crypto.seed.EntropyInfo;
import com.vektorsoft.lockdown.crypto.seed.Mnemonic;
import com.vektorsoft.lockdown.crypto.seed.MnemonicLanguage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javafx.scene.Scene;

/**
 * Initializes application with runtime configuration.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class Initializer {
    
    private static final  Initializer INSTANCE;
    
    private Initializer() {
        // private to prevent initialization
    }
    
    static {
        INSTANCE = new Initializer();
    }
    
    public static Initializer instance() {
        return INSTANCE;
    }
    
    private MnemonicLanguage mnemonicLanguage;
    private String[] mnemonicWords;
    private String mnemonicPassword;
    private Scene scene;

    
    public boolean checkFileStructure() {
        File appDir = new File(LockdownUtil.getUserHomeDirectory(), LockdownConstants.APP_DIRECTORY);
        File keyringFile = new File(appDir, LockdownConstants.KEYRING_FILE);
        
        return keyringFile.exists();
    }
    
    public boolean validateKeyring() {
        return false;
    }
    
    /**
     * Create keyring file structure.
     */
    public void createKeyring() {
        
    }

    public void setMnemonicLanguage(MnemonicLanguage mnemonicLanguage) {
        this.mnemonicLanguage = mnemonicLanguage;
    }

    public void setMnemonicPassword(String mnemonicPassword) {
        this.mnemonicPassword = mnemonicPassword;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        System.out.println("Window: " + scene.getWindow());
    }

    public Scene getScene() {
        return scene;
    }
    
    /**
     * Returns combination of mnemonic words and password. This is used for QR code generation. Returned
     * string has the following format:
     * <pre>
     *  <code>
     *      "word1 word2 word3 ..... word12{}password"
     *  </code>
     * </pre>
     * 
     * @return combination of mnemonic words and password
     */
    public String getMnemonicWithPassword() {
        StringBuilder sb = new StringBuilder();
        for(String word : mnemonicWords) {
            sb.append(word).append(" ");
        }
        sb.append("{}");
        sb.append(mnemonicPassword);
        
        return sb.toString();
    }
    
    public String getMnemonicWords() {
        StringBuilder sb = new StringBuilder();
        for(String word : mnemonicWords) {
            sb.append(word).append(" ");
        }
        return sb.toString();
    }

    public String getMnemonicPassword() {
        return mnemonicPassword;
    }
    
    public String[] generateMnemonicWords() throws IOException, NoSuchAlgorithmException {
        if(mnemonicWords == null) {
            Mnemonic mnemonic = new Mnemonic();
            mnemonicWords = mnemonic.generateMnemonic(mnemonicLanguage, EntropyInfo.ENTROPY_128_BITS);
        }
        
        return mnemonicWords;
    }
    
}
