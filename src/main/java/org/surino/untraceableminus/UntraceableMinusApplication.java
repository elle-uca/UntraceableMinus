package org.surino.untraceableminus;

import javax.swing.SwingUtilities;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.surino.untraceableminus.view.MainFrame;
import org.surino.untraceableminus.view.SplashScreenR;

import com.formdev.flatlaf.FlatLightLaf;

@SpringBootApplication
public class UntraceableMinusApplication {

	public static void main(String[] args) {
		FlatLightLaf.setup();
        // 1) Mostra lo splash
        SplashScreenR splash = new SplashScreenR();
        splash.showSplash();

		 ConfigurableApplicationContext context =
		            new SpringApplicationBuilder(UntraceableMinusApplication.class)
		                .headless(false)
		                .listeners(new BootProgressListener(splash))
		                .web(WebApplicationType.NONE)  
		                .run(args);

		        // Mostra la finestra Swing nel thread della GUI
		        SwingUtilities.invokeLater(() -> {
		            MainFrame frame = context.getBean(MainFrame.class);
		            splash.setProgress(100, "Caricamento completato.");
		            splash.close();
		            frame.setVisible(true);
		        });
	}

}
