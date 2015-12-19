package de.uka.ipd.sdq.beagle.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

public class BeagleAnalysis {

	private JFrame frmBeagleAnalysis;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	JButton btnStartStopAnalysis = new JButton("Start Analysis");
	JButton btnPauseAnalysis = new JButton("Pause Analysis");

	/**
	 * Launch the application. This is needed for easy testing because "Test/Preview" is a
	 * bloody simulation (not a real execution) in which all additional logic is removed.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new BeagleAnalysis();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BeagleAnalysis() {
		initialize();
		this.frmBeagleAnalysis.setVisible(true);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBeagleAnalysis = new JFrame();
		frmBeagleAnalysis.setTitle("Beagle Analysis");
		frmBeagleAnalysis.setBounds(100, 100, 786, 544);
		frmBeagleAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblMakeSureThat = new JLabel(
				"<html>In order to provide accurate measurement results, Beagle requires that the system it's running isn't busy running other software. Please make sure Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur cursus eros non odio sollicitudin varius. Nunc magna purus,</html>");
		frmBeagleAnalysis.getContentPane()
				.setLayout(new MigLayout("", "[133px][48px][555px]", "[39px][35px][][][][][][][][]"));
		frmBeagleAnalysis.getContentPane().add(lblMakeSureThat, "cell 0 0 3 1,grow");

		btnStartStopAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnStartStopAnalysis.getText().equals("Start Analysis")) {
					btnStartStopAnalysis.setText("Stop Analysis");
					btnPauseAnalysis.setEnabled(true);
				} else {
					btnStartStopAnalysis.setText("Start Analysis");
					btnPauseAnalysis.setEnabled(false);

				}
			}
		});

		JLabel lblRunBeagleOn = new JLabel("Run Beagle On");
		frmBeagleAnalysis.getContentPane().add(lblRunBeagleOn, "cell 0 2");

		JRadioButton rdbtnThisMachine = new JRadioButton("This Machine");
		buttonGroup.add(rdbtnThisMachine);
		frmBeagleAnalysis.getContentPane().add(rdbtnThisMachine, "cell 2 3");

		JRadioButton rdbtnOnADifferent = new JRadioButton("On a Different Machine Over a Network");
		buttonGroup.add(rdbtnOnADifferent);
		frmBeagleAnalysis.getContentPane().add(rdbtnOnADifferent, "flowx,cell 2 4");
		frmBeagleAnalysis.getContentPane().add(btnStartStopAnalysis, "cell 0 9,alignx left,aligny bottom");

		btnPauseAnalysis.setEnabled(false);
		frmBeagleAnalysis.getContentPane().add(btnPauseAnalysis, "cell 2 9,alignx left,aligny top");

		JButton btnChooseNetworkMachine = new JButton("Choose Network Machine");
		frmBeagleAnalysis.getContentPane().add(btnChooseNetworkMachine, "cell 2 4");
	}
}
