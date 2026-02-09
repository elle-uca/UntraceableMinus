package org.surino.untraceableminus.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.util.Animator;

@SuppressWarnings("serial")
public class SplashScreen extends JWindow {

    private final JProgressBar progressBar;
    private final JLabel statusLabel;

    public SplashScreen() {
        // Sfondo trasparente per permettere l'opacity
        setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(new Color(245, 245, 245));

        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        // Se hai un logo, decommenta:
         logo.setIcon(new ImageIcon(getClass().getResource("/img/untreceable-splash.png")));

        statusLabel = new JLabel("Avvio…", SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(14f));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(300, 22));

        panel.add(logo, BorderLayout.NORTH);
        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    /** Mostra lo splash con fade-in */
    public void showSplash() {
        setOpacity(0f);
        setVisible(true);

        Animator fadeIn = new Animator(300, fraction -> {
            // fraction va da 0.0 a 1.0
            float alpha = fraction;
            setOpacity(alpha);
        });
        fadeIn.start();
    }

    /** Chiude lo splash con fade-out e poi dispose() */
    public void close() {
        Animator fadeOut = new Animator(300, fraction -> {
            float alpha = 1f - fraction;
            setOpacity(alpha);
            if (fraction >= 1f) {
                dispose();
            }
        });
        fadeOut.start();
    }

    /** Aggiorna progress e messaggio con animazione morbida */
    public void setProgress(int value, String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);

            int oldVal = progressBar.getValue();
            int newVal = Math.max(0, Math.min(100, value));

            Animator anim = new Animator(250, fraction -> {
                int interpolated = oldVal + (int) ((newVal - oldVal) * fraction);
                progressBar.setValue(interpolated);
            });
            anim.start();
        });
    }
}
