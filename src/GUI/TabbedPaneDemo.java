package GUI;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import benchmark.BenchmarkBusyException;
import benchmark.BenchmarkControlSingleton;
import benchmark.hdd.*;
import benchmark.hdd.HDDReadSpeed.ReadOptions;

public class TabbedPaneDemo extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6658427642554200375L;
	static JLabel quicklabel1;
	static JLabel quicklabel2;
	static JLabel quicklabel3;
	static JLabel seqlabel1;
	static JLabel seqlabel2;
	static JLabel seqlabel3;
	static JLabel randlabel1;
	static JLabel randlabel2;
	static JLabel randlabel3;
	static int i=0;
	private int ourHeight;
	private int ourWidth;
	private DefaultTableModel model;

	GridLayout experimentLayout = new GridLayout(0,2);

	private JTabbedPane tabbedPane;

	public TabbedPaneDemo(int ourHeight, int ourWidth) {
		super(new GridLayout(1, 1));
		quicklabel1 = new JLabel("AvgSpeed:   0.000 MB/S");
		quicklabel2 = new JLabel("MinSpeed:   0.000 MB/S");
		quicklabel3 = new JLabel("MaxSpeed:   0.000 MB/S");
		seqlabel1 = new JLabel("AvgSpeed:   0.000 MB/S");
		seqlabel2 = new JLabel("MinSpeed:   0.000 MB/S");
		seqlabel3 = new JLabel("MaxSpeed:   0.000 MB/S");
		randlabel1 = new JLabel("AvgSpeed:   0.000 MB/S");
		randlabel2 = new JLabel("MinSpeed:   0.000 MB/S");
		randlabel3 = new JLabel("MaxSpeed:   0.000 MB/S");
		this.ourHeight=ourHeight;
		this.ourWidth=ourWidth;
		tabbedPane = new JTabbedPane();
		//ImageIcon icon = createImageIcon("images/middle.gif"); //adding some icon for tabs

		JComponent panel1 = QuickLaunchTab(); //creates the first tab
		tabbedPane.addTab("Quick test", null, panel1,    //null=no image //we add the first tab
				"Does nothing");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = SequentialTab();
		tabbedPane.addTab("Sequential test", null, panel2,
				"Reads/Writes sequentially files and measures speed");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = RandomTab();
		tabbedPane.addTab("Random test", null, panel3,
				"Reads/Writes randomly files and measures speed");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JComponent panel4 = TestHistoryTab();
		tabbedPane.addTab("History of tests", null, panel4,
				"Check to see all the tests you've run");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		//Add the tabbed pane to this panel.	
		add(tabbedPane);

		//The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	protected JComponent QuickLaunchTab(){
		JPanel textPanel=new JPanel();   //texts
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.PAGE_AXIS));

		JPanel panel = new JPanel(new BorderLayout());
		JButton startButton=new JButton("Start");

		textPanel.add(quicklabel1);
		textPanel.add(quicklabel2);
		textPanel.add(quicklabel3);


		panel.add(startButton,BorderLayout.SOUTH);
		panel.add(textPanel,BorderLayout.EAST);

		final QuickChart qc = new QuickChart();
		ChartPanel cp = qc.myQuickChart();

		startButton.addActionListener(new ActionListener() {   //the functionality of the button
			public void actionPerformed(ActionEvent e) { 
				drawAverageSpeedq("0.000 MB/S");
				drawMinimumSpeedq("0.000 MB/S");
				HDDReadSpeed hrs = new HDDReadSpeed();
				final HDDWriteSpeed hws = new HDDWriteSpeed();
				final BenchmarkControlSingleton bc = BenchmarkControlSingleton.getInstance();
				try {
					bc.runBenchmark(hrs,ReadOptions.NIO,BenchmarkControlSingleton.sizeStringToInt("16K"),new QuickTabData(qc.dataset,"read",BenchmarkControlSingleton.sizeStringToInt("16K")));
					Thread th = new Thread()
					{
						public void run() {
							while(bc.busyBench()); //wait until current I/O operation finishes
							try {
								bc.runBenchmark(hws,"fb",true,BenchmarkControlSingleton.sizeStringToInt("16K"),BenchmarkControlSingleton.sizeStringToLong("2048M"),new QuickTabData(qc.dataset,"write",BenchmarkControlSingleton.sizeStringToInt("16K")));
							} catch (BenchmarkBusyException e) {
								e.printStackTrace();
							}

						}
					};
					th.start();

				} catch (BenchmarkBusyException ex) {
					System.err.println(ex.getMessage());
				}
			} 
		});
		panel.add(cp);

		return panel;
	}

	protected JComponent SequentialTab(){
		JPanel textPanel=new JPanel();   //texts
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.PAGE_AXIS));

		JPanel panel = new JPanel(new BorderLayout());
		JButton startButton2=new JButton("Start");

		textPanel.add(seqlabel1);
		textPanel.add(seqlabel2);
		textPanel.add(seqlabel3);

		textPanel.add(new JLabel("\n"));

		textPanel.add(new JLabel("Buffer Size: "));
		String[] buffersizes = BenchmarkControlSingleton.getBufferSizes(); //getFileSizes, getBuffersizes
		final JComboBox<String> buffsizeCB = new JComboBox<String>(buffersizes);
		buffsizeCB.setMaximumSize( buffsizeCB.getPreferredSize() );
		buffsizeCB.setVisible(true);
		textPanel.add(buffsizeCB);

		textPanel.add(new JLabel("\n"));

		final JLabel fileszLB = new JLabel("File size:");
		fileszLB.setVisible(false);

		final JComboBox<String> fileSizeCB = new JComboBox<String>(BenchmarkControlSingleton.getFileSizes());
		fileSizeCB.setMaximumSize(fileSizeCB.getPreferredSize());
		fileSizeCB.setVisible(false);
		textPanel.add(new JLabel("\n"));

		textPanel.add(new JLabel("Operation: "));
		String[] operations = {"read","write"};
		final JComboBox<String> operationCB = new JComboBox<String>(operations);
		operationCB.setMaximumSize( operationCB.getPreferredSize() );
		operationCB.setVisible(true);
		operationCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String oper = (String) operationCB.getSelectedItem();
				switch(oper) {
				case "read":{
					fileszLB.setVisible(false);
					fileSizeCB.setVisible(false);
				}break;
				case "write":{
					fileszLB.setVisible(true);
					fileSizeCB.setVisible(true);
				}break;
				}
			}

		});
		textPanel.add(operationCB);
		textPanel.add(fileszLB);
		textPanel.add(fileSizeCB);

		panel.add(startButton2,BorderLayout.SOUTH);
		panel.add(textPanel,BorderLayout.EAST);

		final SeqChart sqc = new SeqChart();
		ChartPanel cp = sqc.mySeqChart();
		panel.add(cp);

		startButton2.addActionListener(new ActionListener() {   //the functionality of the button
			public void actionPerformed(ActionEvent x) { 
				drawAverageSpeeds("0.000 MB/S");
				drawMinimumSpeeds("0.000 MB/S");
				String choice = (String)buffsizeCB.getSelectedItem();
				String op = (String)operationCB.getSelectedItem();
				String fileSz = (String)fileSizeCB.getSelectedItem();
				switch(op) {
				case "read":{
					HDDReadSpeed hrs = new HDDReadSpeed();
					BenchmarkControlSingleton bc = BenchmarkControlSingleton.getInstance();
					try {
						bc.runBenchmark(hrs,ReadOptions.NIO,BenchmarkControlSingleton.sizeStringToInt(choice),new SeqTabData(sqc.dataset,"read",BenchmarkControlSingleton.sizeStringToInt(choice)));
					} catch (BenchmarkBusyException e) {
						System.err.println(e.getMessage());
					}
				}break;
				case "write":{
					HDDWriteSpeed hws = new HDDWriteSpeed();
					BenchmarkControlSingleton bc = BenchmarkControlSingleton.getInstance();
					try {
						bc.runBenchmark(hws,"fb",true,BenchmarkControlSingleton.sizeStringToInt(choice),BenchmarkControlSingleton.sizeStringToLong(fileSz),new SeqTabData(sqc.dataset,"write",BenchmarkControlSingleton.sizeStringToInt(choice)));
					} catch (BenchmarkBusyException e) {
						System.err.println(e.getMessage());
					}
				}break;
				}
			} 
		});

		return panel;
	}

	protected JComponent RandomTab(){
		JPanel textPanel=new JPanel();   //texts
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.PAGE_AXIS));

		JPanel panel = new JPanel(new BorderLayout());
		JButton startButton=new JButton("Start");

		textPanel.add(randlabel1);
		textPanel.add(randlabel2);
		textPanel.add(randlabel3);
		
		final RandTabChart rc = new RandTabChart();
		ChartPanel cp = rc.myRandChart();
				
		panel.add(startButton,BorderLayout.SOUTH);
		panel.add(textPanel,BorderLayout.EAST);
		
		startButton.addActionListener(new ActionListener() {   //the functionality of the button
			public void actionPerformed(ActionEvent e) { 
				drawAverageSpeedq("0.000 MB/S");
				drawMinimumSpeedq("0.000 MB/S");
				final HDDRandomAccess hrs = new HDDRandomAccess();
				final BenchmarkControlSingleton bc = BenchmarkControlSingleton.getInstance();
				try {
					bc.runBenchmark(hrs,"r","fs",BenchmarkControlSingleton.sizeStringToInt("32K"),new RandTabData(rc.dataset,"read",BenchmarkControlSingleton.sizeStringToInt("16K")));
					Thread th = new Thread()
					{
						public void run() {
							while(bc.busyBench()); //wait until current I/O operation finishes
							try {
								bc.runBenchmark(hrs,"w","fs",BenchmarkControlSingleton.sizeStringToInt("32K"),new RandTabData(rc.dataset,"write",BenchmarkControlSingleton.sizeStringToInt("16K")));
							} catch (BenchmarkBusyException e) {
								e.printStackTrace();
							}

						}
					};
					th.start();

				} catch (BenchmarkBusyException ex) {
					System.err.println(ex.getMessage());
				}
			} 
		});
		panel.add(cp);

		return panel;
	}

	protected JComponent TestHistoryTab(){
		JPanel textPanel=new JPanel();   //texts
		JPanel panel = new JPanel(new BorderLayout());
		JTable table = new JTable(new DefaultTableModel(new Object[]{"Operation", "Average Speed","Maximum speed","Minimum speed"},0));
		model = (DefaultTableModel) table.getModel();

		JScrollPane tableContainer = new JScrollPane(table);
		panel.add(tableContainer, BorderLayout.CENTER);

		return panel;
	}

	public void addTableDataRow(String... strings){
			model.addRow(strings);
	}

	class QuickChart{
		XYSeriesCollection dataset = new XYSeriesCollection();
		JFreeChart chart;
		public ChartPanel myQuickChart(){

			// Add the series to your data set
			dataset = new XYSeriesCollection();

			// Generate the graph
			chart = ChartFactory.createXYLineChart(
					"I/O Speed", // Title
					"MB", // x-axis Label
					"MB/s", // y-axis Label
					dataset, // Dataset
					PlotOrientation.VERTICAL, // Plot Orientation
					true, // Show Legend
					true, // Use tooltips
					false // Configure chart to generate URLs?
					);
			return (new ChartPanel (chart));
		}
	}
	class SeqChart{
		XYSeriesCollection dataset = new XYSeriesCollection();
		JFreeChart chart;
		public ChartPanel mySeqChart(){		

			// Generate the graph
			chart = ChartFactory.createXYLineChart(
					"I/O Speeds", // Title
					"MB", // x-axis Label
					"MB/s", // y-axis Label
					dataset, // Dataset
					PlotOrientation.VERTICAL, // Plot Orientation
					true, // Show Legend
					true, // Use tooltips
					false // Configure chart to generate URLs?
					);
			return (new ChartPanel (chart));
		}
	}
	class RandTabChart{
		XYSeriesCollection dataset;
		JFreeChart chart;
		public ChartPanel myRandChart(){

			dataset = new XYSeriesCollection();

			// Generate the graph
			chart = ChartFactory.createXYLineChart(
					"I/O Operations", // Title
					"MB", // x-axis Label
					"MB/s", // y-axis Label
					dataset, // Dataset
					PlotOrientation.VERTICAL, // Plot Orientation
					true, // Show Legend
					true, // Use tooltips
					false // Configure chart to generate URLs?
					);
			return (new ChartPanel (chart));
		}
	}
	public void addQuickChartData(XYSeries ser, double x,double y){
		ser.add(x,y);
	}

	public void addSeqChartData(XYSeries ser,double x,double y){
		ser.add(x,y);
	}
	public void addRandChartData(XYSeries ser,double x,double y){
		ser.add(x,y);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	/*protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TabbedPaneDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    } */

	public static void drawAverageSpeedq(String s){
		quicklabel1.setText("AvgSpeed:      "+s);
	}

	public static void drawMaximumSpeedq(String s){
		quicklabel2.setText("MaxSpeed:      "+s);
	}

	public static void drawMinimumSpeedq(String s){
		quicklabel3.setText("MinSpeed:      "+s);
	}

	public static void drawAverageSpeeds(String s){
		seqlabel1.setText("AvgSpeed:      "+s);
	}

	public static void drawMaximumSpeeds(String s){
		seqlabel2.setText("MaxSpeed:      "+s);
	}

	public static void drawMinimumSpeeds(String s){
		seqlabel3.setText("MinSpeed:      "+s);
	}

	public static void drawAverageSpeedr(String s){
		randlabel1.setText("AvgSpeed:      "+s);
	}

	public static void drawMaximumSpeedr(String s){
		randlabel2.setText("MaxSpeed:      "+s);
	}

	public static void drawMinimumSpeedr(String s){
		randlabel3.setText("MinSpeed:      "+s);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	class SeqTabData implements UpdateChart {
		private double min=Double.MAX_VALUE,avg,avgcnt,max;
		private String operation;
		private int bufferSize;
		private String opname;
		private XYSeries cser;
		public SeqTabData(XYSeriesCollection xysc, String operation, int bufferSize){
			int uniqName = xysc.getSeriesCount();
			this.operation = operation;
			this.bufferSize = bufferSize;
			this.opname = operation+bufferSize+"_"+uniqName;
			cser = new XYSeries(operation+bufferSize+"_"+uniqName);
			xysc.addSeries(cser);
		}
		@Override
		public void updateData(double x,double y){
			addSeqChartData(cser,y,x);
			avg+=x;
			avgcnt++;
			if(x<min){
				drawMinimumSpeeds(x + "MB/s");
				min=x;
			}else if(x>max){
				drawMaximumSpeeds(x + "MB/s");
				max=x;
			}
			updateAverage();
		}
		public void updateAverage(){
			if(avgcnt>0){
				drawAverageSpeeds(avg/avgcnt + "MB/s");
			}
		}
		public void addToTableView(){
			addTableDataRow(opname,seqlabel1.getText(),seqlabel2.getText(),seqlabel3.getText());
		}
	}

	class QuickTabData implements UpdateChart {
		private double min=Double.MAX_VALUE,avg,avgcnt,max;
		private String operation;
		private int bufferSize;
		private XYSeries cser;
		private String opname;
		public QuickTabData(XYSeriesCollection xysc, String operation, int bufferSize){
			int uniqName = xysc.getSeriesCount();
			this.operation = operation;
			this.bufferSize = bufferSize;
			this.opname = operation+bufferSize+"_"+uniqName;
			cser = new XYSeries(operation+bufferSize+"_"+uniqName);
			xysc.addSeries(cser);
		}
		@Override
		public void updateData(double x,double y){
			addQuickChartData(cser,y,x);
			avg+=x;
			avgcnt++;
			if(x<min){
				drawMinimumSpeedq(x + "MB/s");
				min=x;
			}else if(x>max){
				drawMaximumSpeedq(x + "MB/s");
				max=x;
			}
			updateAverage();
		}
		public void updateAverage(){
			if(avgcnt>0){
				drawAverageSpeedq(avg/avgcnt + "MB/s");
			}
		}
		@Override
		public void addToTableView() {
			addTableDataRow(opname,quicklabel1.getText(),quicklabel2.getText(),quicklabel3.getText());
			
		}
	}
	class RandTabData implements UpdateChart {
		private double min=Double.MAX_VALUE,avg,avgcnt,max;
		private String operation;
		private int bufferSize;
		private String opname;
		private XYSeries cser;
		public RandTabData(XYSeriesCollection xysc, String operation, int bufferSize){
			int uniqName = xysc.getSeriesCount();
			this.operation = operation;
			this.bufferSize = bufferSize;
			this.opname = operation+"_rand_"+bufferSize+"_"+uniqName;
			cser = new XYSeries(operation+bufferSize+"_"+uniqName);
			xysc.addSeries(cser);
		}
		@Override
		public void updateData(double x,double y){
			addRandChartData(cser,y,x);
			avg+=x;
			avgcnt++;
			if(x<min){
				drawMinimumSpeedr(x + "MB/s");
				min=x;
			}else if(x>max){
				drawMaximumSpeedr(x + "MB/s");
				max=x;
			}
			updateAverage();
		}
		public void updateAverage(){
			if(avgcnt>0){
				drawAverageSpeedr(avg/avgcnt + "MB/s");
			}
		}
		@Override
		public void addToTableView() {
			addTableDataRow(opname,randlabel1.getText(),randlabel2.getText(),randlabel3.getText());
		}
	}
}
