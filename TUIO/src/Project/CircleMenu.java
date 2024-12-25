package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

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
    private boolean isButtonVisible = true;
    
    JFrame infoFrame;
    JFrame pricingFrame;
    JFrame reviewsFrame;
    JFrame ratingFrame;

    public CircleMenu() {
        instance = this;
        
        FillProductDetails();
        
        titleLabel = new JLabel("Selected Product Index: None", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);

        add(titleLabel, BorderLayout.NORTH);
        
        toggleButton = new JButton("Toggle Me!");
        toggleButton.setBounds(getPreferredSize().width - 130, getPreferredSize().height - 70, 120, 40);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleButtonVisibility();
            }
        });
        add(toggleButton);

        setPreferredSize(new Dimension(DIAMETER + 50, DIAMETER + 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
    
    public void toggleButtonVisibility() {
        isButtonVisible = !isButtonVisible;
        toggleButton.setVisible(isButtonVisible);
    }
    
    private void FillProductDetails(){
        Map<String, String> camera1Details = new HashMap<>();
        camera1Details.put("Info", 
                "Model: Canon EOS R6\n" +
                "Resolution: 20.1 MP\n" +
                "Sensor: Full-Frame CMOS\n" +
                "Features: 4K Video, In-body stabilization\n" +
                "Weight: 680g");
        camera1Details.put("Pricing", 
                "Price: $2,499\n" +
                "Discount: 10% off\n" +
                "Final Price: $2,249\n" +
                "Accessories: Battery, Charger, Lens Hood (Included)");
        camera1Details.put("Reviews", 
                "1. Amazing performance for wildlife photography! - 5/5\n" +
                "2. Great video capabilities, but battery life could be better. - 4/5\n" +
                "3. Lightweight and easy to handle, worth the price. - 5/5");
        camera1Details.put("Rating", 
                "Overall Rating: 4.7/5\n" +
                "Image Quality: 5/5\n" +
                "Video Quality: 4.8/5\n" +
                "Usability: 4.5/5\n" +
                "Value for Money: 4.6/5");
        productDetails.put(2, camera1Details);
        
        Map<String, String> camera2Details = new HashMap<>();
        camera2Details.put("Info", 
                "Model: Nikon Z6 II\n" +
                "Resolution: 24.5 MP\n" +
                "Sensor: Full-Frame CMOS\n" +
                "Features: Dual processors, 4K video, Great autofocus\n" +
                "Weight: 705g");
        camera2Details.put("Pricing", 
                "Price: $1,999\n" +
                "Discount: 5% off\n" +
                "Final Price: $1,899\n" +
                "Accessories: Battery, Charger, Strap (Included)");
        camera2Details.put("Reviews", 
                "1. Excellent autofocus and great for low light. - 5/5\n" +
                "2. Build quality is top-notch, but a bit pricey. - 4/5\n" +
                "3. Highly recommend for video creators! - 5/5");
        camera2Details.put("Rating", 
                "Overall Rating: 4.6/5\n" +
                "Image Quality: 4.8/5\n" +
                "Video Quality: 4.7/5\n" +
                "Usability: 4.4/5\n" +
                "Value for Money: 4.5/5");
        productDetails.put(3, camera2Details);

        Map<String, String> camera3Details = new HashMap<>();
        camera3Details.put("Info", 
                "Model: Sony A7 III\n" +
                "Resolution: 24.2 MP\n" +
                "Sensor: Full-Frame CMOS\n" +
                "Features: 4K Video, Long battery life\n" +
                "Weight: 650g");
        camera3Details.put("Pricing", 
                "Price: $1,999\n" +
                "Discount: None\n" +
                "Final Price: $1,999\n" +
                "Accessories: Battery, Charger, 64GB SD Card (Included)");
        camera3Details.put("Reviews", 
                "1. Versatile camera, suitable for both photos and videos. - 5/5\n" +
                "2. Best value for money in the full-frame category. - 5/5\n" +
                "3. A bit heavy, but incredible image quality. - 4/5");
        camera3Details.put("Rating", 
                "Overall Rating: 4.9/5\n" +
                "Image Quality: 5/5\n" +
                "Video Quality: 4.9/5\n" +
                "Usability: 4.7/5\n" +
                "Value for Money: 4.8/5");
        productDetails.put(4, camera3Details);
    }
    
    public void SetSelectedProductIndex(int selectedIndex) {
        if (!productDetails.containsKey(selectedIndex) || selectedIndex == selectedProductIndex){
            return;
        }
        
        selectedProductIndex = selectedIndex;

        titleLabel.setText("Selected Product Index: " + (selectedProductIndex == -1 ? "None" : selectedProductIndex));
        ResetWindows();

        repaint();
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
