package org.surino.untraceableminus;

import javax.swing.SwingUtilities;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.surino.untraceableminus.view.MainFrame;

@SpringBootApplication
public class UntraceableMinusApplication {

	public static void main(String[] args) {
		
		
		 ConfigurableApplicationContext context =
		            new SpringApplicationBuilder(UntraceableMinusApplication.class)
		                .headless(false)
		                .web(WebApplicationType.SERVLET)  
		                .run(args);

		        // Mostra la finestra Swing nel thread della GUI
		        SwingUtilities.invokeLater(() -> {
		            MainFrame frame = context.getBean(MainFrame.class);
		            frame.setVisible(true);
		        });
	}

}
