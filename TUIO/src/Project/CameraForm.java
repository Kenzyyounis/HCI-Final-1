package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class CameraForm extends JFrame {
    private JTextField modelField, resolutionField, sensorField, featuresField, priceField, discountField;
    private JTextArea reviewsArea, ratingsArea;
    private JButton createButton, updateButton, deleteButton, loadButton, clearButton;
    private JList<String> cameraList;
    private DefaultListModel<String> cameraListModel;
    private ArrayList<Camera> cameras;

    public CameraForm() {
        // Set up the JFrame
        setTitle("Camera CRUD Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call the method when the window is closed
                if (CircleMenu.instance != null)
                    CircleMenu.instance.FillProductDetails();
                
                // Close the application
                dispose();
            }
        });

        // Initialize the camera list
        cameras = Camera.ReadCamerasFromFile();
        
        // Create the form components
        createForm();
    }

    private void createForm() {
        setLayout(new BorderLayout(10, 10)); // Add some spacing around components

        // Panel for camera details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for more flexibility
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
        
        modelField = new JTextField(20); // Set preferred column width
        resolutionField = new JTextField(20);
        sensorField = new JTextField(20);
        featuresField = new JTextField(20);
        priceField = new JTextField(20);
        discountField = new JTextField(20);
        reviewsArea = new JTextArea(3, 20);
        ratingsArea = new JTextArea(3, 20);
        
        createButton = new JButton("Create");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        loadButton = new JButton("Load");
        clearButton = new JButton("Clear");

        // Add fields with appropriate constraints
        addLabelAndField(detailsPanel, gbc, "Model:", 0, 0, modelField);
        addLabelAndField(detailsPanel, gbc, "Resolution:", 1, 0, resolutionField);
        addLabelAndField(detailsPanel, gbc, "Sensor:", 2, 0, sensorField);
        addLabelAndField(detailsPanel, gbc, "Features:", 3, 0, featuresField);
        addLabelAndField(detailsPanel, gbc, "Price:", 4, 0, priceField);
        addLabelAndField(detailsPanel, gbc, "Discount:", 5, 0, discountField);
        addLabelAndField(detailsPanel, gbc, "Reviews:", 6, 0, reviewsArea, true);
        addLabelAndField(detailsPanel, gbc, "Ratings:", 7, 0, ratingsArea, true);

        add(detailsPanel, BorderLayout.NORTH);

        // Panel for buttons with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // List to display cameras
        cameraListModel = new DefaultListModel<>();
        cameraList = new JList<>(cameraListModel);
        cameraList.setPreferredSize(new Dimension(350, 200)); // Set a preferred size
        add(new JScrollPane(cameraList), BorderLayout.CENTER);

        // Action Listeners for buttons
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCamera();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCamera();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCamera();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCameras();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(); 
            }
        });

        cameraList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = cameraList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Camera selectedCamera = cameras.get(selectedIndex);
                    populateFields(selectedCamera);
                }
            }
        });

        loadCameras();
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, int row, int col, JComponent field) {
        addLabelAndField(panel, gbc, labelText, row, col, field, false);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, int row, int col, JComponent field, boolean isTextArea) {
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = col + 1;
        if (isTextArea) {
            JScrollPane scrollPane = new JScrollPane((JTextArea) field);
            panel.add(scrollPane, gbc);
        } else {
            panel.add(field, gbc);
        }
    }


    private void createCamera() {
        try {
            String model = modelField.getText();
            String resolution = resolutionField.getText();
            String sensor = sensorField.getText();
            String features = featuresField.getText();
            float price = Float.parseFloat(priceField.getText());
            float discount = Float.parseFloat(discountField.getText());

            ArrayList<String> reviews = new ArrayList<>();
            for (String review : reviewsArea.getText().split("\n")) {
                reviews.add(review);
            }
            
            ArrayList<String> ratings = new ArrayList<>();
            for (String rating : ratingsArea.getText().split("\n")) {
                ratings.add(rating);
            }

            Camera newCamera = new Camera(model, resolution, sensor, features, price, discount, reviews, ratings);
            cameras.add(newCamera);
            Camera.WriteCameraToFile(cameras);
            loadCameras();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating camera: " + e.getMessage());
        }
    }

    private void updateCamera() {
        int selectedIndex = cameraList.getSelectedIndex();
        if (selectedIndex != -1) {
            Camera selectedCamera = cameras.get(selectedIndex);

            selectedCamera.setModel(modelField.getText());
            selectedCamera.setResolution(resolutionField.getText());
            selectedCamera.setSensor(sensorField.getText());
            selectedCamera.setFeatures(featuresField.getText());
            selectedCamera.setPrice(Float.parseFloat(priceField.getText()));
            selectedCamera.setDiscount(Float.parseFloat(discountField.getText()));

            ArrayList<String> reviews = new ArrayList<>();
            for (String review : reviewsArea.getText().split("\n")) {
                reviews.add(review);
            }
            selectedCamera.setReviews(reviews);
            
            ArrayList<String> ratings = new ArrayList<>();
            for (String rating : ratingsArea.getText().split("\n")) {
                ratings.add(rating);
            }
            selectedCamera.setRatings(ratings);

            Camera.WriteCameraToFile(cameras);
            loadCameras();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a camera to update.");
        }
    }

    private void deleteCamera() {
        int selectedIndex = cameraList.getSelectedIndex();
        if (selectedIndex != -1) {
            cameras.remove(selectedIndex);
            Camera.WriteCameraToFile(cameras);
            
            clearFields();
            
            loadCameras();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a camera to delete.");
        }
    }

    private void loadCameras() {
        cameraListModel.clear();
        cameras = Camera.ReadCamerasFromFile();
        for (Camera camera : cameras) {
            cameraListModel.addElement(camera.getModel());
        }
    }
    
    private void populateFields(Camera selectedCamera) {
        // Populate the fields with the selected camera's details
        modelField.setText(selectedCamera.getModel());
        resolutionField.setText(selectedCamera.getResolution());
        sensorField.setText(selectedCamera.getSensor());
        featuresField.setText(selectedCamera.getFeatures());
        priceField.setText(String.valueOf(selectedCamera.getPrice()));
        discountField.setText(String.valueOf(selectedCamera.getDiscount()));

        // Populate reviews
        StringBuilder reviewsText = new StringBuilder();
        for (String review : selectedCamera.getReviews()) {
            reviewsText.append(review).append("\n");
        }
        reviewsArea.setText(reviewsText.toString());
        
        // Populate ratings
        StringBuilder ratingsText = new StringBuilder();
        for (String rating : selectedCamera.getRatings()) {
            ratingsText.append(rating).append("\n");
        }
        ratingsArea.setText(ratingsText.toString());
    }
    
    private void clearFields() {
        modelField.setText("");
        resolutionField.setText("");
        sensorField.setText("");
        featuresField.setText("");
        priceField.setText("");
        discountField.setText("");
        reviewsArea.setText("");
        ratingsArea.setText("");
        
        cameraList.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CameraForm().setVisible(true);
            }
        });
    }
}
