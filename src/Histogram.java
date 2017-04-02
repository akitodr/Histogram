import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by LaraPorts on 02/04/2017.
 */
public class Histogram {

    //calcula historigrama
    int[] calculate(BufferedImage img) {
        int[] histogram = new int[256];
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color color = new Color(img.getRGB(x, y));
                int r = color.getRed();
                histogram[r] += 1;
            }
        }
        return histogram;
    }

    //calcula historigrama acumulado
    public int[] accumulated(int[] histogram) {
        int[] cumulate = new int[256];
        cumulate[0] = histogram[0];
        for (int i = 1; i < histogram.length; i++) {
            cumulate[i] += histogram[i] + cumulate[i - 1];
        }
        return cumulate;
    }

    //primeiro valor > 0(?)
    private int lowerValue(int[] histogram){
        for (int i = 0; i < histogram.length; i++) {
            if(histogram[i] != 0)
                return histogram[i];
        }
        return 0;
    }

    //calcula mapa de cores
    int[] calculateMap(int[] histogram, int pixels){
        int[] map = new int[256];
        int[] cumulate = accumulated(histogram);
        float lower = lowerValue(histogram);

        for (int i = 0; i < histogram.length; i++) {
            map[i] = Math.round(((cumulate[i] - lower) / (pixels - lower)) * 255);
    }
        return map;
    }

    //equaliza
    BufferedImage equalize(BufferedImage img){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] histogram = calculate(img);
        int[] map = calculateMap(histogram, img.getWidth() * img.getHeight());
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color color = new Color(img.getRGB(x, y));
                int newTone = map[color.getRed()];

                Color newColor = new Color(newTone, newTone, newTone);
                out.setRGB(x,y, newColor.getRGB());
            }
        }
        return out;
    }

    public void run() throws IOException {
        String PATH = "C:\\Users\\LaraPorts\\IdeaProjects\\Histogram\\img\\";
        BufferedImage image = ImageIO.read(new File(PATH, "car.png"));
        BufferedImage image1 = ImageIO.read(new File(PATH, "cars.jpg"));
        BufferedImage image2 = ImageIO.read(new File(PATH, "crowd.png"));
        BufferedImage image3 = ImageIO.read(new File(PATH, "montanha.jpg"));
        BufferedImage image4 = ImageIO.read(new File(PATH, "university.png"));
        BufferedImage newImage = equalize(image);
        BufferedImage newImage1 = equalize(image1);
        BufferedImage newImage2 = equalize(image2);
        BufferedImage newImage3 = equalize(image3);
        BufferedImage newImage4 = equalize(image4);

        ImageIO.write(newImage, "png", new File("img.png"));
        ImageIO.write(newImage1, "png", new File("img1.png"));
        ImageIO.write(newImage2, "png", new File("img2.png"));
        ImageIO.write(newImage3, "png", new File("img3.png"));
        ImageIO.write(newImage4, "png", new File("img4.png"));

        System.out.println("Arroy!");
    }

    static public void main(String[] args) throws IOException {
        new Histogram().run();
    }

}
