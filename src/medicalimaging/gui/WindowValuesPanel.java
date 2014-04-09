/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import medicalimaging.model.ImageReconUtils;

/**
 *
 * @author ericlee
 */
public class WindowValuesPanel extends JPanel implements DocumentListener{
    
    private int defaultLow;
    private int defaultHigh;
    private Image sampleImage;
    
    //Swing components
    public JTextField lowValueTextField;
    public JTextField highValueTextField;
    private JLabel sampleImageLabel;
    
    public WindowValuesPanel(Image sampleImage, int low, int high) {
        this.defaultLow = low;
        this.defaultHigh = high;
        this.sampleImage = sampleImage;
        
        this.setLayout(new GridBagLayout());
        
        if(low != high)
            this.sampleImage = ImageReconUtils.windowImage(sampleImage, low, high);
        else
            this.sampleImage = sampleImage;
        
        lowValueTextField = new JTextField(Integer.toString(low), 3);
        highValueTextField = new JTextField(Integer.toString(high), 3);
        sampleImageLabel = new JLabel(new ImageIcon(sampleImage));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 3;
        this.add(sampleImageLabel);
        
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        this.add(new JLabel("Low: "));
        
        c.gridx = 3;
        this.add(lowValueTextField);
        
        c.gridx = 2;
        c.gridy = 1;
        this.add(new JLabel("High: "));
        
        c.gridx = 3;
        this.add(highValueTextField);
        
        lowValueTextField.getDocument().addDocumentListener(this);
        highValueTextField.getDocument().addDocumentListener(this);
        
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        int low = Integer.parseInt(lowValueTextField.getText());
        int high = Integer.parseInt(highValueTextField.getText());
        
        if(low > 0 && high < 256) {
            Image image = ImageReconUtils.windowImage(sampleImage, low, high);
            sampleImageLabel.setIcon(new ImageIcon(image));
            //revalidate();
            //repaint();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {  
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        
    }
}
