import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class MyImage{

    private String name;
    private double sizeInKilobytes;

    private BufferedImage source;
    private int width;
    private int height;

    public MyImage(String pathToFile) {
        try {
            File file = new File(pathToFile);
            name = file.getName();
            sizeInKilobytes = file.length() / 1024.;

            source = ImageIO.read(file);
            width = source.getWidth();
            height = source.getHeight();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public double getSizeInKilobytes() {
        return sizeInKilobytes;
    }

    public BufferedImage getSource() {
        return source;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", sizeInKilobytes=" + sizeInKilobytes+
                '}';
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public int calcCoefficientDifferenceWith(MyImage image) {
        int sizeForResize = 32;
        BufferedImage source1 = resize(source, sizeForResize, sizeForResize);

        BufferedImage sourceImage = image.getSource();
        BufferedImage source2 = resize(sourceImage, sizeForResize, sizeForResize);

        double result = 0.;
        for(int i = 0; i < sizeForResize; i++){
            for(int j = 0; j < sizeForResize; j++){
                Color color1 = new Color(source1.getRGB(i, j));
                Color color2 = new Color(source2.getRGB(i, j));

                int grey1 = (int) (color1.getRed()* 0.299 + color1.getGreen() * 0.587 + color1.getBlue() * 0.114);
                int grey2 = (int) (color2.getRed()* 0.299 + color2.getGreen() * 0.587 + color2.getBlue() * 0.114);

                result += (grey1 - grey2)*(grey1 - grey2);
            }
        }

        return (int) Math.sqrt(result/(sizeForResize*sizeForResize));
    }

    public static Comparator<MyImage> NameComparator = new Comparator<MyImage>() {

        @Override
        public int compare(MyImage myImage1, MyImage myImage2) {
            return myImage1.getName().compareTo(myImage2.getName());
        }
    };

    public static Comparator<MyImage> SizeComparator = new Comparator<MyImage>() {
        @Override
        public int compare(MyImage myImage1, MyImage myImage2) {
            return (int) (myImage1.getSizeInKilobytes()  - myImage2.getSizeInKilobytes());
        }
    };
}
