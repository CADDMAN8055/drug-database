package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;
import com.drugdesign.services.MolecularDynamicsService;

import java.nio.file.*;
import java.util.*;

@PageTitle("Molecular Dynamics | DrugCompass")
public class DynamicsView extends MainLayout {
    
    private final MolecularDynamicsService mdService;
    
    private Upload structureUpload;
    private TextField structureFileLabel;
    private ComboBox<String> forceFieldSelector;
    private ComboBox<String> waterModelSelector;
    private NumberField temperature;
    private NumberField pressure;
    private NumberField simulationTime;
    private ComboBox<String> ensembleType;
    private Button runButton;
    private ProgressBar progressBar;
    private Grid<String> analysisGrid;
    private VerticalLayout viewerPanel;
    
    private Path structurePath;
    
    public DynamicsView() {
        this.mdService = new MolecularDynamicsService();
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("⚡ Molecular Dynamics");
        Paragraph desc = new Paragraph("Simulate molecular motion and analyze protein-ligand dynamics with GROMACS");
        desc.addClassName("header-description");
        
        Header header = new Header();
        header.add(title, desc);
        header.addClassName("module-header");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(40);
        
        // Control Panel
        VerticalLayout controlPanel = new VerticalLayout();
        controlPanel.setWidth("100%");
        controlPanel.setPadding(true);
        controlPanel.addClassName("control-panel");
        
        // File Upload
        Section uploadSection = new Section();
        H4 uploadHeader = new H4("📁 Structure File");
        uploadHeader.addClassName("section-title");
        
        structureFileLabel = new TextField("Input Structure");
        structureFileLabel.setReadOnly(true);
        structureFileLabel.setWidth("100%");
        
        Button uploadBtn = new Button("Upload PDB/GRO", VaadinIcon.UPLOAD.create());
        uploadBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        uploadBtn.addClickListener(e -> uploadStructure());
        
        uploadSection.add(uploadHeader, structureFileLabel, uploadBtn);
        
        // Force Field
        Section forceFieldSection = new Section();
        H4 ffHeader = new H4("⚙️ Force Field & System");
        ffHeader.addClassName("section-title");
        
        forceFieldSelector = new ComboBox<>("Force Field");
        forceFieldSelector.setItems("AMBER99SB-ILDN", "CHARMM36", "OPLS-AA", "GROMOS96");
        forceFieldSelector.setValue("AMBER99SB-ILDN");
        forceFieldSelector.setWidth("100%");
        
        waterModelSelector = new ComboBox<>("Water Model");
        waterModelSelector.setItems("SPC/E", "TIP3P", "TIP4P-D", "TIP5P");
        waterModelSelector.setValue("SPC/E");
        waterModelSelector.setWidth("100%");
        
        ensembleType = new ComboBox<>("Ensemble");
        ensembleType.setItems("NVT", "NPT", "NPT-AT", "Production MD");
        ensembleType.setValue("NPT");
        ensembleType.setWidth("100%");
        
        forceFieldSection.add(forceFieldSelector, waterModelSelector, ensembleType);
        
        // Simulation Parameters
        Section simSection = new Section();
        H4 simHeader = new H4("⏱️ Simulation Parameters");
        simHeader.addClassName("section-title");
        
        temperature = new NumberField("Temperature (K)");
        temperature.setValue(300.0);
        temperature.setMin(0);
        temperature.setMax(500);
        
        pressure = new NumberField("Pressure (bar)");
        pressure.setValue(1.0);
        pressure.setMin(0);
        pressure.setMax(1000);
        
        simulationTime = new NumberField("Simulation Time (ns)");
        simulationTime.setValue(100.0);
        simulationTime.setMin(1);
        simulationTime.setMax(10000);
        
        HorizontalLayout paramsRow = new HorizontalLayout(temperature, pressure, simulationTime);
        simSection.add(paramsRow);
        
        // Buttons
        runButton = new Button("▶ Start MD Simulation", VaadinIcon.PLAY.create());
        runButton.addThemeVariants(ButtonVariant.LUMBO_SUCCESS, ButtonVariant.LUMBO_LARGE);
        runButton.setWidth("100%");
        runButton.addClickListener(e -> runSimulation());
        
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        
        // Analysis Checkboxes
        Section analysisSection = new Section();
        H4 analysisHeader = new H4("📊 Analysis to Perform");
        analysisHeader.addClassName("section-title");
        
        Checkbox rmsd = new Checkbox("RMSD");
        rmsd.setValue(true);
        Checkbox rmsf = new Checkbox("RMSF");
        Checkbox rg = new Checkbox("Radius of Gyration");
        Checkbox hbonds = new Checkbox("Hydrogen Bonds");
        Checkbox energy = new Checkbox("Energy Analysis");
        
        analysisSection.add(rmsd, rmsf, rg, hbonds, energy);
        
        // Learning Section
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("GROMACS Workflow", new Paragraph(
            "1. Prepare structure (pdb2gmx)\n" +
            "2. Define box (editconf)\n" +
            "3. Add solvent (solvate)\n" +
            "4. Add ions (genion)\n" +
            "5. Energy minimization\n" +
            "6. NVT equilibration\n" +
            "7. NPT equilibration\n" +
            "8. Production MD (mdrun)"
        ));
        learningAccordion.add("Best Practices", new Paragraph(
            "• Use appropriate force field for your system\n" +
            "• Ensure proper system neutralization\n" +
            "• 1-2 fs timestep with H-bond constraints\n" +
            "• PME for electrostatics (>1000 atoms)\n" +
            "• Save trajectories every 10-100 ps"
        ));
        learningAccordion.add("Troubleshooting", new Paragraph(
            "• System explosion → Check energy minimization\n" +
            "• Drift → Increase equilibration time\n" +
            "• Large RMSD → Check starting structure\n" +
            "• LINCS warnings → Reduce timestep"
        ));
        
        controlPanel.add(uploadSection, forceFieldSection, simSection, analysisSection, runButton, progressBar, learningAccordion);
        
        // Results Panel
        VerticalLayout resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        H4 resultsHeader = new H4("📊 Analysis Results");
        resultsHeader.addClassName("section-title");
        
        analysisGrid = new Grid<>();
        analysisGrid.setWidth("100%");
        analysisGrid.setHeight("200px");
        analysisGrid.addColumn(s -> s).setHeader("Analysis Type");
        
        viewerPanel = new VerticalLayout();
        viewerPanel.setWidth("100%");
        viewerPanel.setHeight("350px");
        viewerPanel.addClassName("viewer-panel");
        viewerPanel.add(new Text("📈 Trajectory Visualization - Run analysis to see results"));
        
        resultsPanel.add(resultsHeader, analysisGrid, viewerPanel);
        
        splitLayout.addToPrimary(controlPanel);
        splitLayout.addToSecondary(resultsPanel);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, splitLayout);
        mainContainer.setFlexGrow(1, splitLayout);
        
        add(mainContainer);
    }
    
    private void uploadStructure() {
        Dialog uploadDialog = new Dialog();
        uploadDialog.setHeaderTitle("Upload Structure");
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("chemical/x-pdb", ".pdb", ".gro");
        
        Button confirm = new Button("Use This File", e -> {
            try {
                String filename = buffer.getFileName();
                Path tempDir = Files.createTempDirectory("md_");
                structurePath = tempDir.resolve(filename);
                Files.copy(buffer.getInputStream(), structurePath);
                structureFileLabel.setValue(filename);
                Notification.show("Structure loaded: " + filename, 3000, Notification.Position.TOP_CENTER);
                uploadDialog.close();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 5000).addThemeVariants(NotificationVariant.LUMBO_ERROR);
            }
        });
        
        uploadDialog.add(upload, confirm);
        uploadDialog.open();
    }
    
    private void runSimulation() {
        if (structurePath == null) {
            Notification.show("Please upload a structure file", 5000).addThemeVariants(NotificationVariant.LUMBO_WARNING);
            return;
        }
        
        runButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setValue(0.2);
        
        UI.getCurrent().access(() -> {
            try {
                // Simulate MD steps
                mdService.runGromacsMD(structurePath, 
                    forceFieldSelector.getValue(),
                    waterModelSelector.getValue(),
                    temperature.getValue().doubleValue(),
                    pressure.getValue().doubleValue(),
                    simulationTime.getValue().doubleValue());
                
                UI.getCurrent().access(() -> {
                    progressBar.setValue(1.0);
                    Notification.show("MD Simulation complete!", 5000).addThemeVariants(NotificationVariant.LUMBO_SUCCESS);
                    runButton.setEnabled(true);
                    displayMockResults();
                });
            } catch (Exception ex) {
                UI.getCurrent().access(() -> {
                    Notification.show("Simulation failed: " + ex.getMessage(), 5000).addThemeVariants(NotificationVariant.LUMBO_ERROR);
                    runButton.setEnabled(true);
                    progressBar.setVisible(false);
                });
            }
        });
    }
    
    private void displayMockResults() {
        analysisGrid.setItems(Arrays.asList(
            "RMSD Backbone: 0.23 nm",
            "RMSF C-alpha: 0.15 nm",
            "Rg: 2.34 nm",
            "H-bonds: 45",
            "Temperature: 300.2 K"
        ));
        
        viewerPanel.removeAll();
        
        IFrame plotlyViewer = new IFrame();
        plotlyViewer.setSrc("about:blank");
        plotlyViewer.setWidth("100%");
        plotlyViewer.setHeight("300px");
        
        // Add placeholder chart
        H4 chartTitle = new H4("📈 RMSD over Time (Placeholder - GROMACS output will be displayed)");
        viewerPanel.add(chartTitle, plotlyViewer);
    }
    
    private void uploadStructure() { /* Dialog logic */ }
    
    private static class Section extends VerticalLayout {
        public Section() { addClassName("config-section"); }
    }
    
    private static class Accordion extends VerticalLayout {
        public Accordion() { addClassName("learning-accordion"); }
        public void add(String title, Paragraph content) {
            Button item = new Button(title, VaadinIcon.CHEVRON_DOWN.create());
            VerticalLayout contentPanel = new VerticalLayout(content);
            contentPanel.setVisible(false);
            item.addClickListener(e -> contentPanel.setVisible(!contentPanel.isVisible()));
            add(item, contentPanel);
        }
    }
}
