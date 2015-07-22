package net.sciencestudio.xrd.specs.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.WordUtils;

import commonenvironment.AbstractFile;
import commonenvironment.IOOperations;

import scidraw.drawing.common.Palette;
import swidget.dialogues.AboutDialogue;
import swidget.dialogues.fileio.SwidgetIO;
import swidget.icons.IconFactory;
import swidget.icons.StockIcon;
import swidget.widgets.DropdownImageButton;
import swidget.widgets.ImageButton;
import swidget.widgets.ToolbarImageButton;
import swidget.widgets.DropdownImageButton.Actions;

import eventful.EventfulEnumListener;
import eventful.EventfulListener;

import net.sciencestudio.xrd.specs.SPEcsController;
import net.sciencestudio.xrd.image.XRDImage.PaletteScale;


public class MainWindow extends JFrame
{

	private JButton zoomIn, zoomOut, zoomNormal, zoomFit;
	private JSpinner multAmount;
	private JLabel multLabel;
	private Map<Palette, JRadioButtonMenuItem> paletteOptions;
	private JRadioButtonMenuItem linear, log, square;
	private JMenuItem copy, close, saveMenu;
	private JButton save;
	
	private TabBar tabs;
	
	
	public MainWindow()
	{
		setTitle("Specs");
		setIconImage(IconFactory.getImage("logo"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initGUI();
		
	}
	

	private void initGUI()
	{

		createMenu();
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());		
		
		c.add(createToolbar(), BorderLayout.NORTH);
		
		tabs = new TabBar();
		c.add(tabs, BorderLayout.CENTER);
		
		tabs.addChangeListener(new ChangeListener() {
		
			public void stateChanged(ChangeEvent e)
			{
				if (tabs.getCurrentImage() == null){
					disableWhenNoImages();
					return;
				} else {
					enableWhenImages();
				}
				updateUIOnChange(tabs.getCurrentImage().controller);
			}
		});
		
		
		setPreferredSize(new Dimension(800, 380));
		
		disableWhenNoImages();
		
		pack();
		setLocationRelativeTo(null);
		

		this.setVisible(true);
		
		
	}
	
	private void disableWhenNoImages()
	{
		if (tabs.getCurrentImage() != null) return;
		
		close.setEnabled(false);
		zoomFit.setEnabled(false);
		zoomIn.setEnabled(false);
		zoomOut.setEnabled(false);
		zoomNormal.setEnabled(false);
		save.setEnabled(false);
		saveMenu.setEnabled(false);
		multAmount.setEnabled(false);
		multLabel.setEnabled(false);
	}
	
	private void enableWhenImages()
	{
		if (tabs.getCurrentImage() == null) return;
		
		close.setEnabled(true);
		save.setEnabled(true);
		saveMenu.setEnabled(true);
		
		updateUIOnChange(tabs.getCurrentImage().controller);
		
	}
	
	private void actionOpenImage()
	{
		
		AbstractFile file = SwidgetIO.openFile(
				this, 
				"Select SPE File", 
				new String[][]{{"spe"}, {"tif", "tiff"}}, 
				new String[]{"SPE Images", "TIFF Images"}, 
				"~/"
			);	
		if (file == null) return;
			
		
		final ImageView newImage = new ImageView(this);
		
		tabs.addTab(new File(file.getFileName()).getName(), newImage);
		
		newImage.controller.addListener(new EventfulListener() {
						
			public void change()
			{
				if (tabs.getCurrentImage() == null) return;
				if (! newImage.equals(tabs.getCurrentImage())) return;
				updateUIOnChange(newImage.controller);

			}
		});
		
		newImage.controller.loadXRDImage(file.getFileName());
		

		
	}
	
	private void actionOpenIndexing()
	{
		if (tabs.getCurrentImage() == null) return;
		AbstractFile file = SwidgetIO.openFile(
				this, 
				"Select IND File", 
				new String[][]{{"ind"}}, 
				new String[] {"IND Files"}, 
				"~/"
			);
		if (file == null) return;

		tabs.getCurrentImage().controller.loadXRDIndexData(file.getFileName());
		
	}
	
	private void actionOpenPeakSearching()
	{
		if (tabs.getCurrentImage() == null) return;
		AbstractFile file = SwidgetIO.openFile(
				this, 
				"Select DAT File", 
				new String[][]{{"dat"}}, 
				new String[] {"DAT Files"}, 
				"~/"
			);
		if (file == null) return;

		tabs.getCurrentImage().controller.loadXRDPeakData(file.getFileName());
	}
	
	private void updateUIOnChange(SPEcsController controller)
	{
				

		zoomFit.setEnabled(true);
		zoomFit.setSelected(false);
		zoomIn.setEnabled(true);
		zoomOut.setEnabled(true);
		zoomNormal.setEnabled(true);
		multAmount.setEnabled(true);
		multLabel.setEnabled(true);
		
		save.setEnabled(controller.hasImage());
		saveMenu.setEnabled(controller.hasImage());
		copy.setEnabled(controller.hasImage());
		
		for (Palette p : Palette.values())
		{
			paletteOptions.get(p).setSelected(controller.getPaletteType() == p);
		}
		
		linear.setSelected(controller.getPaletteScale() == PaletteScale.LINEAR);
		log.setSelected(controller.getPaletteScale() == PaletteScale.LOG);
		square.setSelected(controller.getPaletteScale() == PaletteScale.SQUARE);
		
	}
	
	private void createMenu()
	{
		
		JMenuBar menuBar;
		
		// Create the menu bar.
		menuBar = new JMenuBar();
		
		final JMenu file = new JMenu("File");
		
		final JMenuItem open = new JMenuItem("Open XRD Image", IconFactory.getMenuIcon(StockIcon.DOCUMENT_OPEN));
		open.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				actionOpenImage();
			}
		});
		file.add(open);
		
		final JMenuItem openindexing = new JMenuItem("Open IND File", IconFactory.getMenuIcon(StockIcon.DOCUMENT_IMPORT));
		openindexing.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				actionOpenIndexing();
			}
		});
		file.add(openindexing);
		
		saveMenu = new JMenuItem("Save Picture", IconFactory.getMenuIcon(StockIcon.DOCUMENT_SAVE));
		saveMenu.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				tabs.getCurrentImage().actionSaveImage();
			}
		});
		file.add(saveMenu);
		
		
		
		file.addSeparator();
		
		close = new JMenuItem("Close Image", IconFactory.getImageIcon("blank"));
		close.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				ImageView view = tabs.getCurrentImage();
				view.controller.removeAllListeners();
				tabs.closeCurrentTab();
				close.setEnabled(tabs.getCurrentImage() != null);
				
			}
		});
		close.setEnabled(false);
		file.add(close);
		
		final JMenuItem exit = new JMenuItem("Quit", IconFactory.getMenuIcon(StockIcon.WINDOW_CLOSE));
		exit.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		file.add(exit);
		
		
		menuBar.add(file);
		
		
		
		
		final JMenu edit = new JMenu("Edit");
		
		copy = new JMenuItem("Copy Image", IconFactory.getMenuIcon(StockIcon.EDIT_COPY));
		copy.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				tabs.getCurrentImage().controller.copyImageToClipboard(
						tabs.getCurrentImage().getImageWidth(),
						tabs.getCurrentImage().getImageHeight()
					);
			}
		});
		edit.add(copy);
		
		menuBar.add(edit);
		
		
		
		
		
		
		final JMenu view = new JMenu("View");
		
		JMenu paletteMenu = new JMenu("Colour Palettes");
		
		JRadioButtonMenuItem paletteItem;
		paletteOptions = new LinkedHashMap<Palette, JRadioButtonMenuItem>();
		
		for (Palette p : Palette.values())
		{
			paletteItem = createPaletteMenuItem(p);
			if (p == Palette.THERMAL) paletteItem.setSelected(true);
			
			paletteMenu.add(paletteItem);
			paletteOptions.put(p, paletteItem);
		}
		
		view.add(paletteMenu);
		view.addSeparator();
		
		
		
		
		linear = new JRadioButtonMenuItem("Linear Scale", true);
		linear.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				tabs.getCurrentImage().controller.setPaletteScale(PaletteScale.LINEAR);
			}
		});
		view.add(linear);
		
		log = new JRadioButtonMenuItem("Log Scale");
		log.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				tabs.getCurrentImage().controller.setPaletteScale(PaletteScale.LOG);
			}
		});
		view.add(log);
		
		square = new JRadioButtonMenuItem("Squared Scale");
		square.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				tabs.getCurrentImage().controller.setPaletteScale(PaletteScale.SQUARE);
			}
		});
		view.add(square);
		
		menuBar.add(view);
		
		
		this.setJMenuBar(menuBar);
		
	}
	
	
	
	private JRadioButtonMenuItem createPaletteMenuItem(final Palette p)
	{
		JRadioButtonMenuItem paletteItem;
				
		paletteItem = new JRadioButtonMenuItem(WordUtils.capitalizeFully(p.toString()));
		paletteItem.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e)
			{
				if (tabs.getCurrentImage() == null) return;
				tabs.getCurrentImage().controller.setPalette(p);
			}
		});
		
		
		return paletteItem;
		
	}
	
	
	private JToolBar createToolbar()
	{
		
		JToolBar toolbar = new JToolBar();
		toolbar.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		toolbar.setFloatable(false);
		
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		
		
		final JButton open;
		open = new ToolbarImageButton(StockIcon.DOCUMENT_OPEN, "Open XRD Image");
		open.addActionListener(e -> actionOpenImage());
		toolbar.add(open, c);
		c.gridx++;
		

		JPopupMenu overlaymenu = new JPopupMenu();
		JMenuItem indexingMI = new JMenuItem("Indexed Points");
		indexingMI.addActionListener(e -> actionOpenIndexing());
		
		JMenuItem peaksMI = new JMenuItem("All Points");
		peaksMI.addActionListener(e -> actionOpenPeakSearching());
		
		overlaymenu.add(indexingMI);
		overlaymenu.add(peaksMI);
		
		final DropdownImageButton openindexing;
		openindexing = new DropdownImageButton(StockIcon.DOCUMENT_IMPORT, "Overlay", "Overlay data from FOXMAS", overlaymenu);
		openindexing.addListener(message -> {
			if (message == Actions.MAIN) actionOpenIndexing();
		});
		
		toolbar.add(openindexing, c);
		c.gridx++;
		
		
		
		save = new ToolbarImageButton(StockIcon.DOCUMENT_SAVE, "Save Picture");
		save.addActionListener(e -> {
			if (tabs.getCurrentImage() == null) return;
			tabs.getCurrentImage().actionSaveImage();
		});
		toolbar.add(save, c);
		c.gridx++;
		
		
		
		toolbar.add(Box.createHorizontalStrut(50), c);
		c.gridx++;

		
		
		JPanel zoomPanel = new JPanel();
		zoomPanel.setOpaque(false);
		zoomPanel.setLayout(new GridLayout(1, 4));
		
		
		zoomIn = new ToolbarImageButton(StockIcon.ZOOM_IN, "In", "Enlarge the image");
		zoomIn.addActionListener(e -> {
			if (tabs.getCurrentImage() == null) return;
			tabs.getCurrentImage().controller.zoomIn();
		});
		zoomIn.setEnabled(false);
		zoomPanel.add(zoomIn);
		
		zoomOut = new ToolbarImageButton(StockIcon.ZOOM_OUT, "Out", "Shrink the image");
		zoomOut.addActionListener(e -> {
			if (tabs.getCurrentImage() == null) return;
			tabs.getCurrentImage().controller.zoomOut();
		});
		zoomOut.setEnabled(false);
		zoomPanel.add(zoomOut);
		
		zoomNormal = new ToolbarImageButton(StockIcon.ZOOM_ORIGINAL, "Normal", "Show the image at its normal size");
		zoomNormal.addActionListener(e -> {
			if (tabs.getCurrentImage() == null) return;
			tabs.getCurrentImage().controller.zoomNormal();
		});
		zoomNormal.setEnabled(false);
		zoomPanel.add(zoomNormal);
		
		zoomFit = new ToolbarImageButton(StockIcon.ZOOM_BEST_FIT, "Fit", "");
		zoomFit.addActionListener(e ->
		{
			if (tabs.getCurrentImage() == null) return;
			tabs.getCurrentImage().controller.zoomFit();
		});
		zoomFit.setEnabled(false);
		zoomPanel.add(zoomFit);
		

		toolbar.add(zoomPanel, c);
		c.gridx++;
		
		
		
		//scaling controls
		toolbar.add(Box.createHorizontalStrut(50), c);
		c.gridx++;
		
	
		JPanel multPanel = new JPanel();
		multPanel.setOpaque(false);
		multPanel.setLayout(new BorderLayout());

		multLabel = new JLabel("Scale:");
		multLabel.setEnabled(false);
		multAmount = new JSpinner(new SpinnerNumberModel(1, 0.1, 10.0, 0.1));
		multAmount.addChangeListener(e -> {
			if (tabs.getCurrentImage() == null) return;
			//tabs.getCurrentImage().controller.
		});
		multAmount.setEnabled(false);
		multPanel.add(multAmount, BorderLayout.CENTER);
		multPanel.add(multLabel, BorderLayout.WEST);
		
		c.fill = GridBagConstraints.VERTICAL;
		toolbar.add(multPanel, c);
		c.gridx++;
		
		
		//about
		c.weightx = 1.0;
		toolbar.add(Box.createHorizontalGlue(), c);
		c.gridx++;
		c.weightx = 0.0;
		
		
		ImageButton about = new ToolbarImageButton(StockIcon.MISC_ABOUT, "About");
		about.addActionListener(e -> {
			new AboutDialogue(
					MainWindow.this,
					"Specs 3",
					"XRD Visualisation Program",
					"www.sciencestudioproject.com",
					"Copyright (c) 2011 by<br>The University of Western Ontario<br>and<br>The Canadian Light Source Inc.",
					IOOperations.readTextFromJar("/xrd/specs/licence.txt"),
					IOOperations.readTextFromJar("/xrd/specs/credits.txt"),
					"logo",
					"",
					"3.0",
					"",
					"January 2012"
					);
		});
		toolbar.add(about, c);
		c.gridx++;
		
		
		
		return toolbar;
		
	}
	
}
