package Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Camera implements Serializable {
    private static final long serialVersionUID = 3570081285649915464L;
    
    private String model;
    private String resolution;
    private String sensor;
    private String features;
    
    private float price;
    private float discount;
    
    private ArrayList<String> reviews;
    
    private ArrayList<String> ratings;
    
    public Camera() {
        this.reviews = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public Camera(String model, String resolution, String sensor, String features, float price, float discount, ArrayList<String> reviews, ArrayList<String> ratings) {
        this.model = model;
        this.resolution = resolution;
        this.sensor = sensor;
        this.features = features;
        this.price = price;
        this.discount = discount;
        this.reviews = reviews;
        this.ratings = ratings;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<String> ratings) {
        this.ratings = ratings;
    }
    
    protected static ArrayList<Camera> ReadCamerasFromFile() {
        ArrayList<Camera> camerasList = new ArrayList<Camera>();

        File file = new File("cameras.dat");

        if (file.length() == 0) {
            // File is empty, return an empty list
            return camerasList;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            camerasList = (ArrayList<Camera>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return camerasList;
    }
    
    protected static boolean WriteCameraToFile(ArrayList<Camera> camerasList) {
        try {
            File file = new File("cameras.dat");

            if (!file.exists()) {
                System.out.println("File not found. Creating a new file.");
                file.createNewFile();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(camerasList);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
