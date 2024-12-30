package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class CircleMenu extends JPanel {
    public static CircleMenu instance;

    private static final int DIAMETER = 300;
    
    private static final String[] LABELS = {"Info", "Pricing", "Reviews", "Rating"};
    private static final Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};
    
    private Map<Integer, Map<String, String>> productDetails = new HashMap<>();
    
    private int highlightedIndex = -1;
    private int selectedIndex = -1;
    private int selectedProductIndex = -1;
    
    private JLabel titleLabel;
    private JButton toggleButton;
    private boolean isButtonVisible = false;
    
    private JLabel privacyLabel;
    private boolean privacyModeActive = false;
    
    JFrame infoFrame;
    JFrame pricingFrame;
    JFrame reviewsFrame;
    JFrame ratingFrame;

    public CircleMenu() {
        instance = this;
        
        FillProductDetails();
        
        privacyLabel = new JLabel("Current user is underage", SwingConstants.CENTER);
        privacyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        privacyLabel.setForeground(Color.RED);
        privacyLabel.setVisible(false); // Hidden by default
        add(privacyLabel, BorderLayout.CENTER);
        
        titleLabel = new JLabel("Selected Product Index: None", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);

        add(titleLabel, BorderLayout.NORTH);
        
        toggleButton = new JButton("Admin!");
        toggleButton.setBounds(getPreferredSize().width - 130, getPreferredSize().height - 70, 120, 40);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CameraForm().setVisible(true);
            }
        });
        add(toggleButton);
        toggleButtonVisibility(isButtonVisible);

        setPreferredSize(new Dimension(DIAMETER + 50, DIAMETER + 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!privacyModeActive) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 25, y = 25;
            int startAngle = 90;
            int arcAngle = 360 / LABELS.length;

            for (int i = 0; i < LABELS.length; i++) {
                // Draw each slice
                g2d.setColor(COLORS[i]);
                g2d.fill(new Arc2D.Double(x, y, DIAMETER, DIAMETER, startAngle, arcAngle, Arc2D.PIE));

                // Highlight the slice if necessary
                if (i == highlightedIndex) {
                    g2d.setColor(new Color(255, 255, 255, 100)); // Semi-transparent white overlay
                    g2d.fill(new Arc2D.Double(x, y, DIAMETER, DIAMETER, startAngle, arcAngle, Arc2D.PIE));
                }

                // Draw the label
                double theta = Math.toRadians(startAngle + arcAngle / 2.0);
                int labelX = (int) (x + DIAMETER / 2 + Math.cos(theta) * DIAMETER / 3);
                int labelY = (int) (y + DIAMETER / 2 - Math.sin(theta) * DIAMETER / 3);
                g2d.setColor(Color.BLACK);
                g2d.drawString(LABELS[i], labelX - 20, labelY); // Adjust offset for label positioning

                startAngle += arcAngle;
            }
        }
    }
    
    public void toggleButtonVisibility(boolean isVisible) {
        isButtonVisible = isVisible;
        toggleButton.setVisible(isVisible);
        repaint();
    }
    
    public void togglePrivacyScreen(boolean canAccess) {
        privacyModeActive = !canAccess;
        privacyLabel.setVisible(privacyModeActive);
        toggleComponentsVisibility(!privacyModeActive);
        toggleButtonVisibility(false);
        repaint();
    }
    
    public boolean getPublicModeActive(){
        return privacyModeActive;
    }
    
    protected void FillProductDetails(){
        ArrayList<Camera> cameraData = Camera.ReadCamerasFromFile();
        
        for (int i = 0; i < cameraData.size(); i++){
            Map<String, String> cameraDetails = new HashMap();
            cameraDetails.put("Info", "Model: " + cameraData.get(i).getModel() + "\n" + "Resolution: " + cameraData.get(i).getResolution() + "\n" + "Sensor: " + cameraData.get(i).getSensor() + "\n" + "Features: " + cameraData.get(i).getFeatures());
            cameraDetails.put("Pricing", 
                "Price: " + String.format("%.2f", cameraData.get(i).getPrice()) + "\n" +
                "Discount: " + String.format("%.2f", cameraData.get(i).getDiscount() * 100f) + "% \n" + 
                "Final Price: " + String.format("%.2f", cameraData.get(i).getPrice() - (cameraData.get(i).getPrice() * cameraData.get(i).getDiscount()))
            );

            
            StringBuilder reviewsText = new StringBuilder();
            for (String review : cameraData.get(i).getReviews()) {
                reviewsText.append(review).append("\n");
            }
            cameraDetails.put("Reviews", reviewsText.toString());
            
            StringBuilder ratingsText = new StringBuilder();
            for (String rating : cameraData.get(i).getRatings()) {
                ratingsText.append(rating).append("\n");
            }
            cameraDetails.put("Rating", ratingsText.toString());
            
            productDetails.put(i + 2, cameraDetails);
        }
    }
    
    public void SetSelectedProductIndex(int selectedIndex) {
        if (!productDetails.containsKey(selectedIndex) || selectedIndex == selectedProductIndex){
            return;
        }
        
        selectedProductIndex = selectedIndex;

        titleLabel.setText("Selected Product Index: " + (selectedProductIndex == -1 ? "None" : selectedProductIndex));
        ResetWindows();

        repaint();
        
        if (Client.instance != null){
            Client.instance.sendJson("ModelIndex", Integer.toString(selectedProductIndex - 2));
        }
    }
    
    private Map<String, String> GetSelectedProductDetails(){
        return productDetails.get(selectedProductIndex);
    }

    public void highlightTileByRotation(double rotationDegrees) {
        int index = (int) (rotationDegrees / (360 / LABELS.length)) % LABELS.length;
        highlightedIndex = index;
        repaint();
    }

    public void hoverOption(int index) {
        if (index >= 0 && index < LABELS.length) {
            highlightedIndex = index;
            repaint();
        }
    }

    public void selectOption() {
        if (highlightedIndex != -1) {
            selectedIndex = highlightedIndex;
        }
        
        switch (selectedIndex){
            case 0:
                openInfoWindow();
                break;
                
            case 1:
                openPricingWindow();
                break;
                
            case 2:
                openReviewsWindow();
                break;
                
            case 3:
                openRatingWindow();
                break;
        }
    }
    
    private void openInfoWindow() {
        if (selectedProductIndex == -1){
            return;
        }
        
        if (infoFrame == null) {
            String infoData = GetSelectedProductDetails().get("Info");
            
            infoFrame = new JFrame("Camera Info");
            infoFrame.setSize(300, 200);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea infoText = new JTextArea();
            infoText.setText(infoData);
            infoText.setEditable(false);

            infoFrame.add(new JScrollPane(infoText));
        }

        if (!infoFrame.isVisible()) {
            infoFrame.setVisible(true);
        } else {
            infoFrame.toFront(); // Bring the already visible frame to the front
        }
    }

    private void openPricingWindow() {
        if (selectedProductIndex == -1){
            return;
        }
        
        if (pricingFrame == null) {
            String pricingData = GetSelectedProductDetails().get("Pricing");
            
            pricingFrame = new JFrame("Camera Pricing");
            pricingFrame.setSize(300, 200);
            pricingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea pricingText = new JTextArea();
            pricingText.setText(pricingData);
            pricingText.setEditable(false);

            pricingFrame.add(new JScrollPane(pricingText));
        }

        if (!pricingFrame.isVisible()) {
            pricingFrame.setVisible(true);
        } else {
            pricingFrame.toFront();
        }
    }

    private void openReviewsWindow() {
        if (selectedProductIndex == -1){
            return;
        }
        
        if (reviewsFrame == null) {
            String reviewsData = GetSelectedProductDetails().get("Reviews");
            
            reviewsFrame = new JFrame("Camera Reviews");
            reviewsFrame.setSize(300, 200);
            reviewsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea reviewsText = new JTextArea();
            reviewsText.setText(reviewsData);
            reviewsText.setEditable(false);

            reviewsFrame.add(new JScrollPane(reviewsText));
        }

        if (!reviewsFrame.isVisible()) {
            reviewsFrame.setVisible(true);
        } else {
            reviewsFrame.toFront();
        }
    }

    private void openRatingWindow() {
        if (selectedProductIndex == -1){
            return;
        }
        
        if (ratingFrame == null) {
            String ratingsData = GetSelectedProductDetails().get("Rating");
            
            ratingFrame = new JFrame("Camera Rating");
            ratingFrame.setSize(300, 200);
            ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea ratingText = new JTextArea();
            ratingText.setText(ratingsData);
            ratingText.setEditable(false);

            ratingFrame.add(new JScrollPane(ratingText));
        }

        if (!ratingFrame.isVisible()) {
            ratingFrame.setVisible(true);
        } else {
            ratingFrame.toFront();
        }
    }
    
    private void toggleComponentsVisibility(boolean visible) {
        titleLabel.setVisible(visible);
        toggleButton.setVisible(visible);

        if (infoFrame != null) infoFrame.setVisible(visible);
        if (pricingFrame != null) pricingFrame.setVisible(visible);
        if (reviewsFrame != null) reviewsFrame.setVisible(visible);
        if (ratingFrame != null) ratingFrame.setVisible(visible);
    }
    
    private void ResetWindows(){
        if (infoFrame != null) {
            infoFrame.dispose();
            infoFrame = null;
        }

        if (pricingFrame != null) {
            pricingFrame.dispose();
            pricingFrame = null;
        }

        if (reviewsFrame != null) {
            reviewsFrame.dispose();
            reviewsFrame = null;
        }

        if (ratingFrame != null) {
            ratingFrame.dispose();
            ratingFrame = null;
        }
    }
}
