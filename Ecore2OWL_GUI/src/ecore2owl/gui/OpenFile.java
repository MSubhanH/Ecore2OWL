package ecore2owl.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class OpenFile {
	
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	
	public void PickMe() throws FileNotFoundException {
		
		fileChooser.setCurrentDirectory(new File("E:\\Git Projects\\emf4sw\\examples\\GUI Test Runs"));
		
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				
			java.io.File file = fileChooser.getSelectedFile();
			
			Scanner input = new Scanner(file);
			
			sb.append(file.getAbsolutePath());
			
			input.close();
		}
		
		
		else {
			sb.append("No file was selected");
		}
			
	}
	

}
