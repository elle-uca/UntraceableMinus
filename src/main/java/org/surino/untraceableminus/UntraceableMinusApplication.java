package org.surino.untraceableminus;

import javax.swing.SwingUtilities;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.surino.untraceableminus.view.MainFrame;
import org.surino.untraceableminus.view.SplashScreen;

@SpringBootApplication
public class UntraceableMinusApplication {

	public static void main(String[] args) {
        // 1) Mostra lo splash
        SplashScreen splash = new SplashScreen();
        splash.showSplash();
     // Simulazione caricamento while Spring parte (progress finto)
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                splash.setProgress(i, "Caricamento... " + i + "%");
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ignored) {}
            }
        }).start();
		
		 ConfigurableApplicationContext context =
		            new SpringApplicationBuilder(UntraceableMinusApplication.class)
		                .headless(false)
		                .web(WebApplicationType.NONE)  
		                .run(args);

		        // Mostra la finestra Swing nel thread della GUI
		        SwingUtilities.invokeLater(() -> {
		            MainFrame frame = context.getBean(MainFrame.class);
		            splash.close();
		            frame.setVisible(true);
		        });
	}

}
