package ecore2owl.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Transformation.Ecore2OWLTransform;
import Transformation.EcoreIns2OWLInsTransform;
import Transformation.OWL2EcoreTransform;
import Transformation.OWLIns2EcoreInsTransform;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSeparator;

public class Ecore2OWL_GUI {

	private JFrame frmEcoreowlConverter;
	private JTextField textField_Ecore2OWL;
	private JTextField textField_Owl2Ecore;
	private JTextField textField_EcoreIns2OwlIns;
	private JTextField textField_OwlIns2EcoreIns;
	
	private JTextArea textArea_Results; 
	
	public String pathEcoreFolder;
	public String ecoreModelName;
	
	public String pathOwlFolder;
	public String owlOntologyName;

	public String pathEcoreInsFolder;
	public String ecoreInsName;
	
	public String pathOwlInsFolder;
	public String owlInsName;
	
	public boolean isEcoreSelected;
	public boolean isOWLSelected;
	public boolean isEcoreInsSelected;
	public boolean isOWLInsSelected;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ecore2OWL_GUI window = new Ecore2OWL_GUI();
					window.frmEcoreowlConverter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ecore2OWL_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEcoreowlConverter = new JFrame();
		frmEcoreowlConverter.setTitle("Ecore2OWL Converter Beta");
		frmEcoreowlConverter.setBounds(100, 100, 820, 460);
		frmEcoreowlConverter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEcoreowlConverter.getContentPane().setLayout(null);
		
		JLabel label_Ecore2Owl = new JLabel("Select Ecore metamodel (.ecore)");
		label_Ecore2Owl.setBounds(10, 97, 283, 14);
		frmEcoreowlConverter.getContentPane().add(label_Ecore2Owl);
		
		JLabel label_Owl2Ecore = new JLabel("Select Ontology (.owl)");
		label_Owl2Ecore.setBounds(10, 137, 283, 14);
		frmEcoreowlConverter.getContentPane().add(label_Owl2Ecore);
		
		JLabel label_EcoreIns2OwlIns = new JLabel("Select Ecore Model Instance (.xmi)");
		label_EcoreIns2OwlIns.setBounds(10, 177, 283, 14);
		frmEcoreowlConverter.getContentPane().add(label_EcoreIns2OwlIns);
		
		JLabel label_OwlIns2EcoreIns = new JLabel("Select Ontology Instance (.owl) ");
		label_OwlIns2EcoreIns.setBounds(10, 217, 283, 14);
		frmEcoreowlConverter.getContentPane().add(label_OwlIns2EcoreIns);
		
		JLabel lblTransformationDirection = new JLabel("Transformation Direction");
		lblTransformationDirection.setBounds(10, 22, 166, 14);
		frmEcoreowlConverter.getContentPane().add(lblTransformationDirection);

		JRadioButton rdbtnEcore2Owl = new JRadioButton("ecore2owl");
		JRadioButton rdbtnOwl2Ecore = new JRadioButton("owl2ecore");
		JRadioButton rdbtnEcoreIns2OWLIns = new JRadioButton("ecoreInstance2owlInstance");
		JRadioButton rdbtnOWLIns2EcoreIns = new JRadioButton("owlInstance2ecoreInstance");
		
		JButton btnEcore2OWLFile = new JButton("File System");
		JButton btnOwl2EcoreFile = new JButton("File System");
		JButton btnEcoreIns2OwlIns = new JButton("File System");
		JButton btnOwlIns2EcoreIns = new JButton("File System");
		
		
		textField_Ecore2OWL = new JTextField();
		textField_Ecore2OWL.setBounds(354, 94, 310, 20);
		frmEcoreowlConverter.getContentPane().add(textField_Ecore2OWL);
		textField_Ecore2OWL.setColumns(10);

		textField_Owl2Ecore = new JTextField();
		textField_Owl2Ecore.setColumns(10);
		textField_Owl2Ecore.setBounds(354, 134, 310, 20);
		frmEcoreowlConverter.getContentPane().add(textField_Owl2Ecore);
		
		textField_EcoreIns2OwlIns = new JTextField();
		textField_EcoreIns2OwlIns.setColumns(10);
		textField_EcoreIns2OwlIns.setBounds(354, 174, 310, 20);
		frmEcoreowlConverter.getContentPane().add(textField_EcoreIns2OwlIns);
		
		textField_OwlIns2EcoreIns = new JTextField();
		textField_OwlIns2EcoreIns.setColumns(10);
		textField_OwlIns2EcoreIns.setBounds(354, 214, 310, 20);
		frmEcoreowlConverter.getContentPane().add(textField_OwlIns2EcoreIns);
		
		
		JButton btnRunTransformation = new JButton("Run Transformation");
			
		textArea_Results = new JTextArea();
		textArea_Results.setEditable(false);
		textArea_Results.setBounds(10, 294, 784, 100);

		JScrollPane scrollTextArea_Results = new JScrollPane(textArea_Results);
		scrollTextArea_Results.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		scrollTextArea_Results.setBounds(10, 294, 784, 100);
		
		frmEcoreowlConverter.getContentPane().add(scrollTextArea_Results);

		isEcoreSelected = false;
		isOWLSelected = false;
		isEcoreInsSelected = false;
		isOWLInsSelected = false;
		
		///////////////////////////////////////
		
		rdbtnEcore2Owl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rdbtnEcore2Owl.setSelected(true);
				rdbtnOwl2Ecore.setSelected(false);
				rdbtnEcoreIns2OWLIns.setSelected(false);
				rdbtnOWLIns2EcoreIns.setSelected(false);
				
				textField_Ecore2OWL.setVisible(true);
				textField_Owl2Ecore.setVisible(true);
				textField_EcoreIns2OwlIns.setVisible(false);
				textField_OwlIns2EcoreIns.setVisible(false);
				
				textField_Ecore2OWL.setText(null);
				textField_Owl2Ecore.setText(null);
				
				btnEcore2OWLFile.setVisible(true);
				btnOwl2EcoreFile.setVisible(true);
				btnEcoreIns2OwlIns.setVisible(false);
				btnOwlIns2EcoreIns.setVisible(false);
				
				label_Ecore2Owl.setVisible(true);
				label_Owl2Ecore.setVisible(true);
				label_EcoreIns2OwlIns.setVisible(false);
				label_OwlIns2EcoreIns.setVisible(false);
				
				isEcoreSelected = false;
				isOWLSelected = false;
				isEcoreInsSelected = false;
				isOWLInsSelected = false;		
				
				label_Ecore2Owl.setText("Select Ecore metamodel (.ecore) (INPUT)");
				label_Owl2Ecore.setText("Select Ontology (.owl) (OUTPUT)");		
				
				textArea_Results.setText(null);
				textArea_Results.setText(">> Transformation from Ecore meta-model to OWL selected");
				textArea_Results.append("\n>> Please select .ecore and .owl files");
			}
		});
		rdbtnEcore2Owl.setBounds(354, 18, 109, 23);
		frmEcoreowlConverter.getContentPane().add(rdbtnEcore2Owl);
			
		rdbtnOwl2Ecore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				rdbtnEcore2Owl.setSelected(false);
				rdbtnOwl2Ecore.setSelected(true);
				rdbtnEcoreIns2OWLIns.setSelected(false);
				rdbtnOWLIns2EcoreIns.setSelected(false);
				
				textField_Ecore2OWL.setVisible(true);
				textField_Owl2Ecore.setVisible(true);
				textField_EcoreIns2OwlIns.setVisible(false);
				textField_OwlIns2EcoreIns.setVisible(false);
				
				textField_Ecore2OWL.setText(null);
				textField_Owl2Ecore.setText(null);
				
				btnEcore2OWLFile.setVisible(true);
				btnOwl2EcoreFile.setVisible(true);
				btnEcoreIns2OwlIns.setVisible(false);
				btnOwlIns2EcoreIns.setVisible(false);
				
				label_Ecore2Owl.setVisible(true);
				label_Owl2Ecore.setVisible(true);
				label_EcoreIns2OwlIns.setVisible(false);
				label_OwlIns2EcoreIns.setVisible(false);
				
				
				isEcoreSelected = false;
				isOWLSelected = false;
				isEcoreInsSelected = false;
				isOWLInsSelected = false;
				
				
				label_Ecore2Owl.setText("Select Ecore metamodel (.ecore) (OUTPUT)");
				label_Owl2Ecore.setText("Select Ontology (.owl) (INPUT)");
			
				textArea_Results.setText(null);
				textArea_Results.setText(">> Transformation from OWL meta-model to Ecore selected");
				textArea_Results.append("\n>> Please select .owl and .ecore files");
				
			}
		});
		rdbtnOwl2Ecore.setBounds(561, 18, 109, 23);
		frmEcoreowlConverter.getContentPane().add(rdbtnOwl2Ecore);
		
		rdbtnEcoreIns2OWLIns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				rdbtnEcore2Owl.setSelected(false);
				rdbtnOwl2Ecore.setSelected(false);
				rdbtnEcoreIns2OWLIns.setSelected(true);
				rdbtnOWLIns2EcoreIns.setSelected(false);
				
				textField_Ecore2OWL.setVisible(true);
				textField_Owl2Ecore.setVisible(true);
				textField_EcoreIns2OwlIns.setVisible(true);
				textField_OwlIns2EcoreIns.setVisible(true);
				
				textField_Ecore2OWL.setText(null);
				textField_Owl2Ecore.setText(null);
				textField_EcoreIns2OwlIns.setText(null);
				textField_OwlIns2EcoreIns.setText(null);
				
				btnEcore2OWLFile.setVisible(true);
				btnOwl2EcoreFile.setVisible(true);
				btnEcoreIns2OwlIns.setVisible(true);
				btnOwlIns2EcoreIns.setVisible(true);
				
				label_Ecore2Owl.setVisible(true);
				label_Owl2Ecore.setVisible(true);
				label_EcoreIns2OwlIns.setVisible(true);
				label_OwlIns2EcoreIns.setVisible(true);
				
				isEcoreSelected = false;
				isOWLSelected = false;
				isEcoreInsSelected = false;
				isOWLInsSelected = false;				
				
				label_Ecore2Owl.setText("Select Ecore metamodel (.ecore) (INPUT)");
				label_Owl2Ecore.setText("Select Ontology (.owl) (INPUT)");
				label_EcoreIns2OwlIns.setText("Select Ecore Model Instance (.xmi) (INPUT)");
				label_OwlIns2EcoreIns.setText("Select Ontology Instance (.owl) (OUTPUT)");

				textArea_Results.setText(null);
				textArea_Results.setText(">> Transformation from Ecore instance to OWL instance selected");
				textArea_Results.append("\n>> Please select .xmi and .owl files");
			}
		});
		rdbtnEcoreIns2OWLIns.setBounds(354, 44, 178, 23);
		frmEcoreowlConverter.getContentPane().add(rdbtnEcoreIns2OWLIns);

		rdbtnOWLIns2EcoreIns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rdbtnEcore2Owl.setSelected(false);
				rdbtnOwl2Ecore.setSelected(false);
				rdbtnEcoreIns2OWLIns.setSelected(false);
				rdbtnOWLIns2EcoreIns.setSelected(true);
				
				textField_Ecore2OWL.setVisible(true);
				textField_Owl2Ecore.setVisible(false);
				textField_EcoreIns2OwlIns.setVisible(true);
				textField_OwlIns2EcoreIns.setVisible(true);
				
				textField_Ecore2OWL.setText(null);
				textField_EcoreIns2OwlIns.setText(null);
				textField_OwlIns2EcoreIns.setText(null);
				
				btnEcore2OWLFile.setVisible(true);
				btnOwl2EcoreFile.setVisible(false);
				btnEcoreIns2OwlIns.setVisible(true);
				btnOwlIns2EcoreIns.setVisible(true);
				
				isEcoreSelected = false;
				isOWLSelected = false;
				isEcoreInsSelected = false;
				isOWLInsSelected = false;		
				
				label_Ecore2Owl.setVisible(true);
				label_Owl2Ecore.setVisible(false);
				label_EcoreIns2OwlIns.setVisible(true);
				label_OwlIns2EcoreIns.setVisible(true);
				
				label_Ecore2Owl.setText("Select Ecore metamodel (.ecore) (INPUT)");
				label_EcoreIns2OwlIns.setText("Select Ecore Model Instance (.xmi) (OUTPUT)");
				label_OwlIns2EcoreIns.setText("Select Ontology Instance (.owl) (INPUT)");
				
				textArea_Results.setText(null);
				textArea_Results.setText("Transformation from OWL Instance meta-model to Ecore Instance selected");
				
				textArea_Results.setText(null);
				textArea_Results.setText(">> Transformation from OWL instance to Ecore instance selected");
				textArea_Results.append("\n>> Please select .owl and .xmi files");
			}
		});
		rdbtnOWLIns2EcoreIns.setBounds(561, 44, 178, 23);
		frmEcoreowlConverter.getContentPane().add(rdbtnOWLIns2EcoreIns);
				
		
		/////////////////////////////////
		
		
		
		btnEcore2OWLFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				OpenFile of = new OpenFile();
				
				try {
					of.PickMe();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				if(getExtension(of.sb.toString()).compareTo("ecore") == 0) {
						
					String pathEcoreModel = of.sb.toString();
					
					pathOwlFolder = pathEcoreModel.substring(0,pathEcoreModel.lastIndexOf("\\") + 1);
					owlOntologyName = pathEcoreModel.substring(pathEcoreModel.lastIndexOf("\\") + 1, pathEcoreModel.indexOf(".ecore"));
					
					textField_Ecore2OWL.setText(of.sb.toString());						
					
					if(rdbtnEcore2Owl.isSelected()) {
						textField_Owl2Ecore.setText(pathOwlFolder + owlOntologyName + ".owl");
						textArea_Results.append("\n>> Ecore file location - Selected \n>> OWL file location - Selected");
						
						isEcoreSelected = true;
						isOWLSelected = true;	
					}	
					
					if(rdbtnOwl2Ecore.isSelected() || rdbtnEcoreIns2OWLIns.isSelected() || rdbtnOWLIns2EcoreIns.isSelected())
						isEcoreSelected = true;
						
						
				}
				
				else {
					textArea_Results.append("\n>> Bad File Format Error. Please select an .ecore meta-model file.");
					textField_Ecore2OWL.setText(null);
					textField_Owl2Ecore.setText(null);
					
					isEcoreSelected = false;
					isOWLSelected = false;
				}
					
				
			}
		});
		btnEcore2OWLFile.setBounds(673, 92, 89, 23);
		frmEcoreowlConverter.getContentPane().add(btnEcore2OWLFile);
		
		btnOwl2EcoreFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
								
				OpenFile of = new OpenFile();
				
				try {
					of.PickMe();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(getExtension(of.sb.toString()).compareTo("owl") == 0) {
					
					String pathOWLOntology = of.sb.toString();
					
					pathEcoreFolder = pathOWLOntology.substring(0,pathOWLOntology.lastIndexOf("\\") + 1);
					ecoreModelName = pathOWLOntology.substring(pathOWLOntology.lastIndexOf("\\") + 1, pathOWLOntology.indexOf(".owl"));
										
					textField_Owl2Ecore.setText(of.sb.toString());
					
					if(rdbtnOwl2Ecore.isSelected()) {
						textField_Ecore2OWL.setText(pathEcoreFolder + ecoreModelName + ".ecore");
						textArea_Results.append("\n>> OWL file location - Selected \n>> Ecore file location - Selected");
						
						isEcoreSelected = true;
						isOWLSelected = true;	
					}	
					
					if(rdbtnEcore2Owl.isSelected() || rdbtnEcoreIns2OWLIns.isSelected())
						isOWLSelected = true;

	
				}
				
				else {
					textArea_Results.append("\n>> Bad File Format Error. Please select an .owl meta-model file.");						
					textField_Owl2Ecore.setText(null);				
					
					isOWLSelected = false;
				}
				
			}
		});
		btnOwl2EcoreFile.setBounds(673, 132, 89, 23);
		frmEcoreowlConverter.getContentPane().add(btnOwl2EcoreFile);

		btnEcoreIns2OwlIns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				OpenFile of = new OpenFile();
				
				try {
					of.PickMe();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
								
				
				if(getExtension(of.sb.toString()).compareTo("xmi") == 0) {
					
					String pathEcoreInsModel = of.sb.toString();
					
					pathOwlInsFolder = pathEcoreInsModel.substring(0,pathEcoreInsModel.lastIndexOf("\\") + 1);
					owlInsName = pathEcoreInsModel.substring(pathEcoreInsModel.lastIndexOf("\\") + 1, pathEcoreInsModel.lastIndexOf(".xmi"));
					
					textField_EcoreIns2OwlIns.setText(of.sb.toString());					
					
					if(rdbtnEcoreIns2OWLIns.isSelected()) {						
						textField_OwlIns2EcoreIns.setText(pathOwlInsFolder + owlInsName + ".owl");
						textArea_Results.append("\n>> Ecore Instance file location - Selected \n>> OWL Instance file location - Selected");
						
						isEcoreInsSelected = true;
						isOWLInsSelected = true;	
					}
					
					if(rdbtnOWLIns2EcoreIns.isSelected())
						isEcoreInsSelected = true;

				}
				
				else {
					textArea_Results.setText("Bad File Format Error. Please select an .xmi ecore instance model file.");
					textField_EcoreIns2OwlIns.setText(null);
					textField_OwlIns2EcoreIns.setText(null);
					
					isEcoreInsSelected = false;
					isOWLInsSelected = false;	
				}
				
			}
		});
		btnEcoreIns2OwlIns.setBounds(673, 172, 89, 23);
		frmEcoreowlConverter.getContentPane().add(btnEcoreIns2OwlIns);
		
		btnOwlIns2EcoreIns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				OpenFile of = new OpenFile();
				
				try {
					of.PickMe();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(getExtension(of.sb.toString()).compareTo("owl") == 0) {
					
					String pathOwlInsOntology = of.sb.toString();
					
					pathEcoreInsFolder = pathOwlInsOntology.substring(0,pathOwlInsOntology.lastIndexOf("\\") + 1);
					ecoreInsName = pathOwlInsOntology.substring(pathOwlInsOntology.lastIndexOf("\\") + 1, pathOwlInsOntology.lastIndexOf(".owl"));
					
					textField_OwlIns2EcoreIns.setText(of.sb.toString());
					
					if(rdbtnOWLIns2EcoreIns.isSelected()) {
						textField_EcoreIns2OwlIns.setText(pathEcoreInsFolder + ecoreInsName + ".xmi");
						
						textArea_Results.append("\n>> OWL Instance file location - Selected \n>> Ecore Instance file location - Selected");

						isOWLInsSelected = true;							
						isEcoreInsSelected = true;

					}
										
					if(rdbtnEcoreIns2OWLIns.isSelected())
						isOWLInsSelected = true;
					
				}
				
				else {
					textArea_Results.setText("Bad File Format Error. Please select an .owl instance model file.");
					textField_OwlIns2EcoreIns.setText(null);
					textField_EcoreIns2OwlIns.setText(null);
					
					isOWLInsSelected = false;							
					isEcoreInsSelected = false;
				}			
			}
		});
		btnOwlIns2EcoreIns.setBounds(673, 212, 89, 23);
		frmEcoreowlConverter.getContentPane().add(btnOwlIns2EcoreIns);
		
		
		//////////////////////////////////
		
		btnRunTransformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(rdbtnEcore2Owl.isSelected()) {
									
					if(isEcoreSelected && isOWLSelected) {
						
						boolean isSuccessful = false;
						
						String pathEcoreModel = textField_Ecore2OWL.getText();
						String pathOWLOntology = textField_Owl2Ecore.getText();
						
						pathEcoreFolder = pathEcoreModel.substring(0,pathEcoreModel.lastIndexOf("\\") + 1);
						ecoreModelName = pathEcoreModel.substring(pathEcoreModel.lastIndexOf("\\") + 1, pathEcoreModel.indexOf(".ecore"));
						
						owlOntologyName = pathOWLOntology.substring(pathOWLOntology.lastIndexOf("\\") + 1, pathOWLOntology.lastIndexOf(".owl"));
						pathOwlFolder = pathOWLOntology.substring(0,pathOWLOntology.lastIndexOf("\\") + 1);
						
						pathEcoreFolder = pathEcoreFolder.replaceAll("\\\\", "/");
						pathOwlFolder = pathOwlFolder.replaceAll("\\\\", "/");
						
						try {
							Ecore2OWLTransform ecore2owlTransformer = new Ecore2OWLTransform(pathEcoreFolder, ecoreModelName, pathOwlFolder, owlOntologyName);
							
							textArea_Results.append("\n\n>> Transformation Complete..........................OWL files generated successfully");
						} catch (IOException | ParserConfigurationException | SAXException | TransformerException e1) {
							// TODO Auto-generated catch block
	
							e1.printStackTrace();
							textArea_Results.append("\n\n>> Transformation Unsuccessful");
						}					
					}
					
					else
						textArea_Results.append("\n>> Please select the inputs to Ecore to OWL transformation");
				
				}
				
				if(rdbtnOwl2Ecore.isSelected()) {
					
					if(isEcoreSelected && isOWLSelected) {
						
						String pathEcoreModel = textField_Ecore2OWL.getText();
						String pathOWLOntology = textField_Owl2Ecore.getText();
						
						pathEcoreFolder = pathEcoreModel.substring(0,pathEcoreModel.lastIndexOf("\\") + 1);
						ecoreModelName = pathEcoreModel.substring(pathEcoreModel.lastIndexOf("\\") + 1, pathEcoreModel.indexOf(".ecore"));
						
						owlOntologyName = pathOWLOntology.substring(pathOWLOntology.lastIndexOf("\\") + 1, pathOWLOntology.lastIndexOf(".owl"));
						pathOwlFolder = pathOWLOntology.substring(0,pathOWLOntology.lastIndexOf("\\") + 1);
						
						pathEcoreFolder = pathEcoreFolder.replaceAll("\\\\", "/");
						pathOwlFolder = pathOwlFolder.replaceAll("\\\\", "/");
						
						
						try {
							OWL2EcoreTransform owl2ecoreTransformer = new OWL2EcoreTransform(pathEcoreFolder, ecoreModelName, pathOwlFolder, owlOntologyName);
							
							textArea_Results.append("\n\n>> Transformation Complete..........................Ecore file generated successfully");
							
						} catch (IOException | ParserConfigurationException | SAXException | java.lang.ArrayStoreException | TransformerException e1 ) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							textArea_Results.append("\n\n>> Transformation Unsuccessful");
						}						
					}
					
					else
						textArea_Results.append("\n>> Please select the inputs to OWL to Ecore transformation");
					
					
				}
				
				
				if(rdbtnEcoreIns2OWLIns.isSelected()) {
					
					if(isEcoreSelected && isOWLSelected && isEcoreInsSelected && isOWLInsSelected) {
						
						String pathEcoreModel = textField_Ecore2OWL.getText();
						String pathOwlOntology = textField_Owl2Ecore.getText();
						
						String pathEcoreInsModel = textField_EcoreIns2OwlIns.getText();
						String pathOwlInsOntology = textField_OwlIns2EcoreIns.getText();

						pathEcoreFolder = pathEcoreModel.substring(0,pathEcoreModel.lastIndexOf("\\") + 1);
						ecoreModelName = pathEcoreModel.substring(pathEcoreModel.lastIndexOf("\\") + 1, pathEcoreModel.indexOf(".ecore"));
						
						pathOwlFolder = pathOwlOntology.substring(0,pathOwlOntology.lastIndexOf("\\") + 1);
						owlOntologyName = pathOwlOntology.substring(pathOwlOntology.lastIndexOf("\\") + 1, pathOwlOntology.indexOf(".owl"));
					
						
						pathEcoreInsFolder = pathEcoreInsModel.substring(0,pathEcoreInsModel.lastIndexOf("\\") + 1);
						ecoreInsName = pathEcoreInsModel.substring(pathEcoreInsModel.lastIndexOf("\\") + 1, pathEcoreInsModel.indexOf(".xmi"));
						
						pathOwlInsFolder = pathOwlInsOntology.substring(0,pathOwlInsOntology.lastIndexOf("\\") + 1);
						owlInsName = pathOwlInsOntology.substring(pathOwlInsOntology.lastIndexOf("\\") + 1, pathOwlInsOntology.lastIndexOf(".owl"));
							
						pathEcoreFolder = pathEcoreFolder.replaceAll("\\\\", "/");
						pathOwlFolder = pathOwlFolder.replaceAll("\\\\", "/");
						pathEcoreInsFolder = pathEcoreInsFolder.replaceAll("\\\\", "/");
						pathOwlInsFolder = pathOwlInsFolder.replaceAll("\\\\", "/");
						
						try {
							EcoreIns2OWLInsTransform ecoreIns2owlInsTransformer = new EcoreIns2OWLInsTransform(pathEcoreFolder, ecoreModelName, pathOwlFolder, owlOntologyName, pathEcoreInsFolder, ecoreInsName, pathOwlInsFolder, owlInsName);
						
							textArea_Results.append("\n\n>> Transformation Complete..........................OWL instance file generated successfully");
						} catch (IOException | ParserConfigurationException | SAXException | TransformerException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							textArea_Results.append("\n\n>> Transformation Unsuccessful");
						}
						
						
					}
					
					else
						textArea_Results.append("\n>> Please select the inputs to Ecore instance to OWL instance transformation");
				}
				
				
				if(rdbtnOWLIns2EcoreIns.isSelected()) {
				
					if(isEcoreSelected && isEcoreInsSelected && isOWLInsSelected) {
						
						String pathEcoreModel = textField_Ecore2OWL.getText();
						
						String pathEcoreInsModel = textField_EcoreIns2OwlIns.getText();
						String pathOwlInsOntology = textField_OwlIns2EcoreIns.getText();

						pathEcoreFolder = pathEcoreModel.substring(0,pathEcoreModel.lastIndexOf("\\") + 1);
						ecoreModelName = pathEcoreModel.substring(pathEcoreModel.lastIndexOf("\\") + 1, pathEcoreModel.indexOf(".ecore"));
						
						pathEcoreInsFolder = pathEcoreInsModel.substring(0,pathEcoreInsModel.lastIndexOf("\\") + 1);
						ecoreInsName = pathEcoreInsModel.substring(pathEcoreInsModel.lastIndexOf("\\") + 1, pathEcoreInsModel.indexOf(".xmi"));
						
						pathOwlInsFolder = pathOwlInsOntology.substring(0,pathOwlInsOntology.lastIndexOf("\\") + 1);
						owlInsName = pathOwlInsOntology.substring(pathOwlInsOntology.lastIndexOf("\\") + 1, pathOwlInsOntology.lastIndexOf(".owl"));
							
						pathEcoreFolder = pathEcoreFolder.replaceAll("\\\\", "/");
						pathEcoreInsFolder = pathEcoreInsFolder.replaceAll("\\\\", "/");
						pathOwlInsFolder = pathOwlInsFolder.replaceAll("\\\\", "/");
						
						try {
							OWLIns2EcoreInsTransform owlIns2EcoreInsTransformer = new OWLIns2EcoreInsTransform(pathEcoreFolder, ecoreModelName, pathEcoreInsFolder, ecoreInsName, pathOwlInsFolder, owlInsName);
					
							textArea_Results.append("\n\n>> Transformation Complete..........................Ecore instance file generated successfully");
						} catch (IOException | ParserConfigurationException | SAXException | TransformerException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							textArea_Results.append("\n\n>> Transformation Unsuccessful");
						}
						
					}
					
					else
						textArea_Results.append("\n>> Please select the inputs to OWL instance to ecore instance transformation");

				}
				
								
			}
		});
		btnRunTransformation.setBounds(354, 245, 310, 23);
		frmEcoreowlConverter.getContentPane().add(btnRunTransformation);
		
	}

	private String getExtension(String FilePath) {
		
		String extension = "";

		int i = FilePath.lastIndexOf('.');
		if (i > 0) {
		    extension = FilePath.substring(i+1);
		}
		
		
		return extension;
	}
}
