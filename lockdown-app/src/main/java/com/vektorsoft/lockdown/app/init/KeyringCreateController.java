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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vektorsoft.lockdown.crypto.seed.MnemonicLanguage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for keyring creation wizard.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class KeyringCreateController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyringCreateController.class);

    public static final String WIZARD_PAGE_ONE_URL = "/gui/fxml/init/init_wizard_pg_1.fxml";
    public static final String WIZARD_PAGE_TWO_URL = "/gui/fxml/init/init_wizard_pg_2.fxml";
    public static final String WIZARD_PAGE_THREE_URL = "/gui/fxml/init/init_wizard_pg_3.fxml";
    public static final String WIZARD_PAGE_FOUR_URL = "/gui/fxml/init/init_wizard_pg_4.fxml";

    private PasswordMismatchObservable passwordMismatchObservable;

    @FXML
    private ComboBox<MnemonicLanguage> seedLanguageCombo;
    @FXML
    private ImageView qrCodeImg;
    @FXML
    private TextArea mnemonicWordsText;
    @FXML
    private Label passwordMismatchLabel;
    @FXML
    private CheckBox mnemonicPasswordCheckbox;
    @FXML
    private PasswordField mnemonicPasswordField;
    @FXML
    private PasswordField mnemonicPassConfirmField;
    @FXML
    private Button printButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing controller with URL {}", location);
        if (location.toString().endsWith(WIZARD_PAGE_ONE_URL)) {
            for (MnemonicLanguage lang : MnemonicLanguage.values()) {
                seedLanguageCombo.getItems().add(lang);
            }
            seedLanguageCombo.valueProperty().setValue(MnemonicLanguage.ENGLISH);
            Initializer.instance().setMnemonicLanguage(MnemonicLanguage.ENGLISH);
            seedLanguageCombo.valueProperty().addListener(new ChangeListener<MnemonicLanguage>() {
                @Override
                public void changed(ObservableValue observable, MnemonicLanguage oldValue, MnemonicLanguage newValue) {
                    LOGGER.debug("Selected seed language: {}", newValue);
                    Initializer.instance().setMnemonicLanguage(newValue);
                }
            });
        } else if (location.toString().endsWith(WIZARD_PAGE_TWO_URL)) {
            LOGGER.debug("initialize page 2");
            passwordMismatchObservable = new PasswordMismatchObservable(mnemonicPasswordField.textProperty(), mnemonicPassConfirmField.textProperty());
            mnemonicPasswordCheckbox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                mnemonicPasswordField.setDisable(!newValue);
                mnemonicPassConfirmField.setDisable(!newValue);
                // clear set passwords
                if (!newValue) {
                    mnemonicPasswordField.clear();
                    mnemonicPassConfirmField.clear();
                }
            });
            passwordMismatchLabel.visibleProperty().bind(passwordMismatchObservable.matchValue());
            passwordMismatchObservable.matchValue().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) {
                    Initializer.instance().setMnemonicPassword(mnemonicPasswordField.getText());
                }
            });
        } else if (location.toString().endsWith(WIZARD_PAGE_THREE_URL)) {
            LOGGER.debug("initialize page 3");
            // generate QR code
            //initMnemonicQRCode();
            printButton.setOnAction((ActionEvent event) -> {
                LOGGER.debug("Printing menmonic info..");
                PrinterJob job = PrinterJob.createPrinterJob();
                if(job != null && job.showPrintDialog(Initializer.instance().getScene().getWindow())) {
                    job.printPage(qrCodeImg);
                    job.printPage(new Label(Initializer.instance().getMnemonicWords()));
                    job.printPage(new Label(Initializer.instance().getMnemonicPassword()));
                    job.endJob();
                }
            });
        }
    }

    public void generateMnemonicWords() {
        try {
            String[] words = Initializer.instance().generateMnemonicWords();
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                sb.append(word).append(" ");
            }
            mnemonicWordsText.setText(sb.toString());
        } catch (Exception ex) {
            LOGGER.error("Could not generate mnemonic words", ex);
        }

    }

    public PasswordMismatchObservable getPasswordMismatchObservable() {
        return passwordMismatchObservable;
    }

    public void initMnemonicQRCode() {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // options for QR code generation
            Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = writer.encode(Initializer.instance().getMnemonicWithPassword(), BarcodeFormat.QR_CODE, 200, 200, hintMap);
            BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = (Graphics2D) img.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, 200, 200);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < 200; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            qrCodeImg.setImage(SwingFXUtils.toFXImage(img, null));
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
    }

}
