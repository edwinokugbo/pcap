// package com.pcapxray.app;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import pcapxray.UI.About;
// import pcapxray.Utils.PcapUtils;

public class PcapXray implements ActionListener {

    static JFrame f;
    static JMenu fileMenu;
    static JMenu helpMenu;
    static JMenuItem openMenuItem;
    static JMenuItem exitMenuItem;
    static JMenuItem contentMenuItem;
    static JMenuItem aboutMenuItem;
    static JMenuBar mb;

    JLabel pathLabel;
    JTextField pathField;
    JButton browseButt;
    JButton analyseButt;
    JLabel outputPathLabel;
    JTextField outputPathField;
    JButton outputBrowseButt;
    JLabel optionsLabel;
    JComboBox<String> interfaceOptions;
    JComboBox<String> sourceOptions;
    JComboBox<String> destinationOptions;
    JButton zoomInButt;
    JButton zoomOutButt;

    private int top = 3, left = 5, bottom = 3, right = 5;
    private Insets i = new Insets(top, left, bottom, right);
    private String NO_INTERFACE[] = { "No Interface" };
    private String NO_SOURCE[] = { "No Source" };
    private String NO_DESTINATION[] = { "No Destination" };
    // ArrayList inFaces = new ArrayList<String>();

    PcapXray() {
        setupMainWindow();
        setupMenu();
    }

    public static void main(String[] args) {
        new PcapXray();

    }

    public static void startSetup() {

    }

    // Setup and prepare the main application window
    public void setupMainWindow() {
        f = new JFrame("PcapXray 1.0");// creating instance of JFrame
        f.setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setSize(screenSize.width / 2, screenSize.height);// 400 width and 500 height
        // f.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        // Create a panel grid to hold top panels
        JPanel topGridPanel = new JPanel(new GridLayout(3, 1));
        Border blackline = BorderFactory.createLineBorder(Color.GRAY);

        // Create the four panels in the application window
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));

        JPanel topPanelTwo = new JPanel();
        topPanelTwo.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));
        topPanelTwo.setBorder(blackline);
        // topPanelTwo.setBackground(Color.blue);

        JPanel topPanelThree = new JPanel();
        topPanelThree.setLayout(new GridBagLayout());
        // topPanelThree.setBorder(blackline);
        // topPanelThree.setBackground(Color.orange);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));
        centerPanel.setBackground(Color.red);

        topGridPanel.add(topPanel);
        topGridPanel.add(topPanelTwo);
        topGridPanel.add(topPanelThree);

        // Setup topPanel
        pathLabel = new JLabel();
        pathLabel.setText("Enter pcap file path:");
        pathField = new JTextField("", 25);
        // pathField.setBounds(40, 40, 200, 40);
        pathField.setBorder(
                BorderFactory.createCompoundBorder(pathField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        browseButt = new JButton("Browse");
        analyseButt = new JButton("Analyse");
        JProgressBar analyseProgress = new JProgressBar(0, 100);
        // analyseProgress.setBounds(40, 40, 300, 40);
        analyseProgress.setValue(10);
        analyseProgress.setStringPainted(true);

        topPanel.add(pathLabel);
        topPanel.add(pathField);
        topPanel.add(browseButt);
        topPanel.add(analyseButt);
        topPanel.add(analyseProgress);
        browseButt.setActionCommand("BrowsePcapFile");
        browseButt.addActionListener(this);

        // Setup topPanelTwo
        outputPathLabel = new JLabel();
        outputPathLabel.setText("Output Directory path:");
        outputPathField = new JTextField("", 25);
        // outputPathField.setBounds(40, 40, 200, 40);
        outputPathField.setBorder(
                BorderFactory.createCompoundBorder(pathField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        outputBrowseButt = new JButton("Browse");

        topPanelTwo.add(outputPathLabel);
        topPanelTwo.add(outputPathField);
        topPanelTwo.add(outputBrowseButt);
        outputBrowseButt.setActionCommand("BrowseOutputPath");
        outputBrowseButt.addActionListener(this);

        // Setup topPanelThree
        // First, create a panel three left ad right panels and separate the components

        JPanel p3Left = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
        JPanel p3Right = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        optionsLabel = new JLabel();
        optionsLabel.setText("Options:");
        interfaceOptions = new JComboBox<>(NO_INTERFACE);
        // interfaceOptions.setPrototypeDisplayValue("No Interface");
        sourceOptions = new JComboBox<>(NO_SOURCE);
        destinationOptions = new JComboBox<>(NO_DESTINATION);

        zoomInButt = new JButton("ZoomIn");
        zoomOutButt = new JButton("ZoomOut");
        zoomOutButt.setBounds(0, 0, 50, 30);

        p3Left.add(optionsLabel);
        p3Left.add(interfaceOptions);
        p3Left.add(sourceOptions);
        p3Left.add(destinationOptions);

        p3Right.add(zoomInButt);
        p3Right.add(zoomOutButt);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = i;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        topPanelThree.add(p3Left, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        topPanelThree.add(p3Right, gbc);

        // Setup center panel with tab pages
        JTextArea ta = new JTextArea(200, 200);
        JPanel p1 = new JPanel();
        p1.add(ta);
        JPanel p2 = new JPanel();
        JTabbedPane tp = new JTabbedPane();
        tp.setBounds(50, 50, 200, 200);
        tp.add("Map", p1);
        tp.add("Interactive Map", p2);
        centerPanel.add(tp);

        // Add penels to the window frame
        f.add(topGridPanel, BorderLayout.NORTH);
        f.add(tp, BorderLayout.CENTER);
        f.setVisible(true);

        interfaceOptions.removeAllItems();
        // sourceOptions.removeAllItems();

        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                interfaceOptions.addItem(networkInterface.getDisplayName());
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // This function defines the application's menus and submenus
    public void setupMenu() {
        mb = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_P);
        helpMenu = new JMenu("Help");
        fileMenu.setMnemonic(KeyEvent.VK_H);

        openMenuItem = new JMenuItem("Open");
        openMenuItem.setMnemonic(KeyEvent.VK_N);
        openMenuItem.setMargin(new Insets(30, 10, 10, 10));
        openMenuItem.setActionCommand("Open");

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.setActionCommand("Exit");

        contentMenuItem = new JMenuItem("Content");
        contentMenuItem.setMnemonic(KeyEvent.VK_T);
        contentMenuItem.setActionCommand("Content");

        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.setActionCommand("About");

        openMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        contentMenuItem.addActionListener(this);
        aboutMenuItem.addActionListener(this);

        fileMenu.add(openMenuItem);
        fileMenu.add(new JMenuItem("")).setEnabled(false);
        ;
        fileMenu.add(exitMenuItem);

        helpMenu.add(contentMenuItem);
        helpMenu.add(new JMenuItem("")).setEnabled(false);
        ;
        helpMenu.add(aboutMenuItem);

        mb.add(fileMenu);
        mb.add(Box.createHorizontalStrut(10));
        mb.add(helpMenu);

        f.setJMenuBar(mb);
        f.setVisible(true);// making the frame visible
    }

    public void loadPcapFile(Component ctx, JTextField tfield, String loadType) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        int i = fc.showOpenDialog(ctx);
        if (i == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String filepath = f.getPath();
            tfield.setText(filepath);

            // if (loadType == "INPUT") {
            // Pcaputils.ReadPcapFile(filepath);
            // }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Open":
                // statusLabel.setText("Open Menu clicked.");
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Content":
                // statusLabel.setText("Content Menu clicked.");
                break;
            case "About":
                About.showAbout(f);
                break;
            case "BrowsePcapFile":
                loadPcapFile((Component) e.getSource(), pathField, "INPUT");
                break;
            case "BrowseOutputPath":
                loadPcapFile((Component) e.getSource(), outputPathField, "OUTPUT");
                break;
            default:
                break;
        }

    }
}