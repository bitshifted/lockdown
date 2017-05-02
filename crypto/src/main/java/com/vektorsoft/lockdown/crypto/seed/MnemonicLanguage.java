/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vektorsoft.lockdown.crypto.seed;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public enum MnemonicLanguage {
    ENGLISH ("English"),
    SPANISH ("Espanol"),
    CHINESE ("Chinese"),
    JAPANESE ("Japanese");
    
    private MnemonicLanguage(String title) {
        this.title = title;
    }
    
    private final String title;

    @Override
    public String toString() {
        return title;
    }
    
    
    
}
