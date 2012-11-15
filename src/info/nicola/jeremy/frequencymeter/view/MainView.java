package info.nicola.jeremy.frequencymeter.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;

public class MainView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4878150693297785117L;

	private DefaultValueDataset dataSet;
	
	private JPanel getPanel(){
		DialPlot dialplot = new DialPlot();
		dialplot.setDataset(dataSet);
		dialplot.setDialFrame(new StandardDialFrame());
		dialplot.setBackground(new DialBackground());
		DialTextAnnotation dialtextannotation = new DialTextAnnotation("Frequency (Hz)");
		dialtextannotation.setFont(new Font("Dialog", 1, 14));
		dialtextannotation.setRadius(0.69999999999999996D);
		dialplot.addLayer(dialtextannotation);
		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialplot.addLayer(dialvalueindicator);
		StandardDialScale standarddialscale = new StandardDialScale(0d, 500d, -120D, -300D, 10D, 4);
		standarddialscale.setMajorTickIncrement(500d);
		standarddialscale.setMinorTickCount(4);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.14999999999999999D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		dialplot.addScale(0, standarddialscale);
		dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
		DialCap dialcap = new DialCap();
		dialplot.setCap(dialcap);
		
		JPanel panel=new JPanel(new BorderLayout());
		dataSet=new DefaultValueDataset(10d);
	}
}
