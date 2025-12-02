package org.surino.untraceableminus.view;


import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class MainFrame extends JFrame {

	@SuppressWarnings("unused")
	private final PersonPanel personPanel;

    public MainFrame(PersonPanel personPanel) {
    	this.personPanel = personPanel;
    	
    	Image icon = new ImageIcon(
                getClass().getResource("/img/untreceable-24.png")
        ).getImage();

        setIconImage(icon);

        setSize(500, 400);
        
    	setTitle("UntraceableMinus Desktop by Luke");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        add(personPanel, BorderLayout.CENTER);

    }
}
