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

import com.vektorsoft.lockdown.app.ResourceConstants;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;

/**
 * Wizard for initial creation of keyring.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class KeyringCreateWizard extends Wizard {
    
    private final WizardPane languageSelectionPage;
    private final WizardPane mnemonicWordsPage;
    private final WizardPane printPage;
    private final WizardPane keyringPasswordPage;
    private final WizardPane keyringCreatePage;
    
    private final ResourceBundle resources;

    public KeyringCreateWizard() throws IOException {
        resources = ResourceBundle.getBundle(ResourceConstants.KEYRING_CREATE_WIZARD_STRINGS);
        setTitle(resources.getString("create.keyring.wizard.title"));
        
        languageSelectionPage = new WizardPane();
        Node page1Content = FXMLLoader.load(getClass().getResource(KeyringCreateController.WIZARD_PAGE_ONE_URL), resources);
        languageSelectionPage.setHeaderText(resources.getString("page1.header"));
        languageSelectionPage.setContent(page1Content);
        
        
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource(KeyringCreateController.WIZARD_PAGE_TWO_URL), resources);
        Node page2Content = loader2.load();
        mnemonicWordsPage = new ControlledWizardPane(loader2.getController(), KeyringCreateController.WIZ_WORDS_PAGE);
        mnemonicWordsPage.setHeaderText(resources.getString("page2.header"));
        mnemonicWordsPage.setContent(page2Content);
        
        FXMLLoader loader3 = new FXMLLoader(getClass().getResource(KeyringCreateController.WIZARD_PAGE_THREE_URL), resources);
        Node page3content = loader3.load();
        printPage = new ControlledWizardPane(loader3.getController(), KeyringCreateController.WIZ_QR_CODE_PAGE);
        printPage.setHeaderText(resources.getString("page3.header"));
        printPage.setContent(page3content);
        
        FXMLLoader loader4 = new FXMLLoader(getClass().getResource(KeyringCreateController.WIZARD_PAGE_FOUR_URL), resources);
        Node page4content = loader4.load();
        keyringPasswordPage = new ControlledWizardPane(loader4.getController(), KeyringCreateController.WIZ_PASSWORD_PAGE);
        keyringPasswordPage.setHeaderText(resources.getString("page4.header"));
        keyringPasswordPage.setContent(page4content);
        
        FXMLLoader loader5 = new FXMLLoader(getClass().getResource(KeyringCreateController.WIZARD_PAGE_FIVE_URL), resources);
        Node page5content = loader5.load();
        keyringCreatePage = new ControlledWizardPane(loader5.getController(), KeyringCreateController.WIZ_KEYRING_CREATE_PAGE);
        keyringCreatePage.setHeaderText(resources.getString("page5.header"));
        keyringCreatePage.setContent(page5content);
        
        setFlow(new LinearFlow(languageSelectionPage, mnemonicWordsPage, printPage, keyringPasswordPage, keyringCreatePage));
    }
}
