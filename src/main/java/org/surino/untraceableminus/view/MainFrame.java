package org.surino.untraceableminus.view;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class MainFrame extends JFrame {

	@SuppressWarnings("unused")
	private final PersonPanel personPanel;

    public MainFrame(PersonPanel personPanel) {
    	this.personPanel = personPanel;
        
    	setTitle("UntraceableMinus Desktop by Luke");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        add(personPanel, BorderLayout.CENTER);

    }
}
