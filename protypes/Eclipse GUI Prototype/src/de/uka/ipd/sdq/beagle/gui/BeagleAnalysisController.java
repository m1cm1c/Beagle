package de.uka.ipd.sdq.beagle.gui;

public class BeagleAnalysisController {
	BeagleAnalysis guiView;
	BeagleConfiguration beagleConfiguration;

	protected BeagleAnalysisController(BeagleAnalysis guiView, BeagleConfiguration beagleConfiguration) {
		this.guiView = guiView;
		this.beagleConfiguration = beagleConfiguration;

	}
}
