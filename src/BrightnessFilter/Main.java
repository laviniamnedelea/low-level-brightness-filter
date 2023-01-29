package BrightnessFilter; // Folosim package-ul acesta pentru intreg proiectul, pentru a evita conflictele de denumiri.

import java.io.IOException;
import javafx.scene.image.Image;

public class Main{
	
	ImageForFilter myImage_got = new ImageForFilter();
	Integer level_got;
	ImageForFilter new_image = new ImageForFilter();

    
	public Main(String path, Integer level) throws IOException, InterruptedException{
		
		System.out.println(path);
		myImage_got = myImage_got.readImage(path);
		level_got = level;
	}
	
	
    public void processImage(String new_path) throws IOException {              
                if (myImage_got != null) {
                	myImage_got.setBrightness(level_got);
                	new_image = Filter.applyFilter(myImage_got);
        } 
    }
    
    public void saveImage(String new_path) throws IOException {              
        Filter.writeNewImage(new_path, new_image);
    }
}