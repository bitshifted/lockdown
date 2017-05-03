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
    
    private final WizardPane page1;
    private final WizardPane page2;
    private final WizardPane page3;
    private final WizardPane page4;
    
    private final ResourceBundle resources;

    public KeyringCreateWizard() throws IOException {
        resources = ResourceBundle.getBundle("gui/lang/init/init_screen_strings");
        setTitle(resources.getString("create.keyring.wizard.title"));
        
        page1 = new WizardPane();
        Node page1Content = FXMLLoader.load(getClass().getResource(KeyringCreateController.WIZARD_PAGE_ONE_URL), resources);
        page1.setHeaderText("Please select language for seed word list");
        page1.setContent(page1Content);
        
        
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource(KeyringCreateController.WIZARD_PAGE_TWO_URL), resources);
        Node page2Content = loader2.load();
        page2 = new ControlledWizardPane(loader2.getController(), 2);
        page2.setHeaderText("Wizard page 2");
        page2.setContent(page2Content);
        
        FXMLLoader loader3 = new FXMLLoader(getClass().getResource(KeyringCreateController.WIZARD_PAGE_THREE_URL), resources);
        Node page3content = loader3.load();
        page3 = new ControlledWizardPane(loader3.getController(), 3);
        page3.setHeaderText("Wizard  page 3");
        page3.setContent(page3content);
        
        page4 = new WizardPane();
        Node page4content = FXMLLoader.load(getClass().getResource(KeyringCreateController.WIZARD_PAGE_FOUR_URL), resources);
        page4.setHeaderText("Wizard page 4");
        page4.setContent(page4content);
        
        setFlow(new LinearFlow(page1, page2, page3, page4));
    }
}
