package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.drugdesign.layout.MainLayout;
import com.drugdesign.services.DockingService;
import com.drugdesign.model.DockingResult;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Molecular Docking | DrugCompass")
public class DockingView extends MainLayout {
    
    private final DockingService dockingService;
    
    // UI Components
    private ComboBox<String> toolSelector;
    private Upload receptorUpload;
    private Upload ligandUpload;
    private TextField receptorFileLabel;
    private TextField ligandFileLabel;
    private NumberField centerX, centerY, centerZ;
    private NumberField sizeX, sizeY, sizeZ;
    private NumberField exhaustiveness;
    private NumberField numModes;
    private Checkbox energyRange;
    private Button runButton;
    private ProgressBar progressBar;
    private Grid<DockingResult> resultsGrid;
    private VerticalLayout viewerPanel;
    private SplitLayout splitLayout;
    private HorizontalLayout controlPanel;
    private VerticalLayout resultsPanel;
    private Tabs toolTabs;
    private VerticalLayout paramsPanel;
    
    private Path receptorPath;
    private Path ligandPath;
    
    public DockingView() {
        this.dockingService = new DockingService();
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        // Header
        Header header = new Header();
        header.addClassName("module-header");
        
        H2 title = new H2("🧬 Molecular Docking");
        Paragraph desc = new Paragraph("Virtual screening and binding pose prediction using state-of-the-art docking tools");
        desc.addClassName("header-description");
        
        header.add(title, desc);
        
        // Tool Selection Tabs
        toolTabs = new Tabs(
            new Tab("AutoDock Vina"),
            new Tab("Smina"),
            new Tab("CB-Dock"),
            new Tab("SwissDock"),
            new Tab("PyRx")
        );
        toolTabs.addClassName("tool-tabs");
        toolTabs.addSelectedChangeListener(e -> updateToolParams());
        
        // Split Layout: Left = Controls, Right = Viewer
        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(40);
        
        // Left Panel - Controls
        controlPanel = new VerticalLayout();
        controlPanel.setWidth("100%");
        controlPanel.setPadding(true);
        controlPanel.addClassName("control-panel");
        
        // File Upload Section
        Section uploadSection = new Section();
        uploadSection.addClassName("config-section");
        
        H4 uploadHeader = new H4("📁 Input Files");
        uploadHeader.addClassName("section-title");
        
        receptorFileLabel = new TextField("Receptor (PDB)");
        receptorFileLabel.setReadOnly(true);
        receptorFileLabel.setWidth("100%");
        
        Button uploadReceptor = new Button("Upload Receptor", VaadinIcon.UPLOAD.create());
        uploadReceptor.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        uploadReceptor.addClickListener(e -> uploadReceptorFile());
        
        ligandFileLabel = new TextField("Ligand (SDF/MOL)");
        ligandFileLabel.setReadOnly(true);
        ligandFileLabel.setWidth("100%");
        
        Button uploadLigand = new Button("Upload Ligand", VaadinIcon.UPLOAD.create());
        uploadLigand.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        uploadLigand.addClickListener(e -> uploadLigandFile());
        
        uploadSection.add(uploadHeader, receptorFileLabel, uploadReceptor, ligandFileLabel, uploadLigand);
        
        // Docking Parameters Section
        Section paramsSection = new Section();
        paramsSection.addClassName("config-section");
        
        H4 paramsHeader = new H4("⚙️ Docking Parameters");
        paramsHeader.addClassName("section-title");
        
        HorizontalLayout centerLayout = new HorizontalLayout();
        centerX = new NumberField("Center X");
        centerY = new NumberField("Center Y");
        centerZ = new NumberField("Center Z");
        centerX.setValue(0.0); centerY.setValue(0.0); centerZ.setValue(0.0);
        centerLayout.add(centerX, centerY, centerZ);
        
        HorizontalLayout sizeLayout = new HorizontalLayout();
        sizeX = new NumberField("Size X");
        sizeY = new NumberField("Size Y");
        sizeZ = new NumberField("Size Z");
        sizeX.setValue(20.0); sizeY.setValue(20.0); sizeZ.setValue(20.0);
        sizeLayout.add(sizeX, sizeY, sizeZ);
        
        exhaustiveness = new NumberField("Exhaustiveness");
        exhaustiveness.setValue(32.0);
        exhaustiveness.setMin(1);
        exhaustiveness.setMax(128);
        
        numModes = new NumberField("Max Modes");
        numModes.setValue(10.0);
        numModes.setMin(1);
        numModes.setMax(100);
        
        energyRange = new Checkbox("Show energy range");
        energyRange.setValue(true);
        
        paramsSection.add(paramsHeader, centerLayout, sizeLayout, exhaustiveness, numModes, energyRange);
        
        // Action Buttons
        runButton = new Button("▶ Run Docking", VaadinIcon.PLAY.create());
        runButton.addThemeVariants(ButtonVariant.LUMBO_SUCCESS, ButtonVariant.LUMBO_LARGE);
        runButton.setWidth("100%");
        runButton.addClickListener(e -> runDocking());
        
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(false);
        
        // Learning Tooltip
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("How Docking Works", new Paragraph(
            "1. Prepare receptor (remove water, add hydrogens)\n" +
            "2. Define binding site using grid box\n" +
            "3. Prepare ligand (proper tautomers/charges)\n" +
            "4. Run docking with chosen parameters\n" +
            "5. Analyze binding modes and scores"
        ));
        learningAccordion.add("Grid Box Tips", new Paragraph(
            "• Center on known binding site residues\n" +
            "• Size should encompass entire binding pocket\n" +
            "• Larger box = slower but more thorough\n" +
            "• Typical size: 20-30 Å"
        ));
        learningAccordion.add("Best Practices", new Paragraph(
            "• Use PDBQT format for Vina/Smina\n" +
            "• Set exhaustiveness ≥ 32 for final docking\n" +
            "• Generate ≥ 10 binding modes\n" +
            "• Re-dock native ligand to validate"
        ));
        
        controlPanel.add(uploadSection, paramsSection, runButton, progressBar, learningAccordion);
        
        // Right Panel - Results & Viewer
        resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setHeight("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        // Results Grid
        H4 resultsHeader = new H4("📊 Docking Results");
        resultsHeader.addClassName("section-title");
        
        resultsGrid = new Grid<>(DockingResult.class);
        resultsGrid.setWidth("100%");
        resultsGrid.setHeight("300px");
        resultsGrid.setColumns("mode", "bindingAffinity", "rmsdLb", "rmsdUb");
        resultsGrid.getColumnByKey("mode").setHeader("Mode");
        resultsGrid.getColumnByKey("bindingAffinity").setHeader("ΔG (kcal/mol)");
        resultsGrid.getColumnByKey("rmsdLb").setHeader("RMSD L.B.");
        resultsGrid.getColumnByKey("rmsdUb").setHeader("RMSD U.B.");
        
        resultsGrid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) displayResult(e.getValue());
        });
        
        // 3D Viewer Placeholder
        viewerPanel = new VerticalLayout();
        viewerPanel.setWidth("100%");
        viewerPanel.setHeight("400px");
        viewerPanel.addClassName("viewer-panel");
        viewerPanel.add(new Text("🧬 3D Viewer - Select a result to visualize"));
        
        resultsPanel.add(resultsHeader, resultsGrid, viewerPanel);
        
        splitLayout.addToPrimary(controlPanel);
        splitLayout.addToSecondary(resultsPanel);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, toolTabs, splitLayout);
        mainContainer.setFlexGrow(1, splitLayout);
        
        add(mainContainer);
    }
    
    private void uploadReceptorFile() {
        FileTypeFilter filter = new FileTypeFilter(Arrays.asList("pdb", "pdbqt"), "Protein Files");
        
        Dialog uploadDialog = new Dialog();
        uploadDialog.setHeaderTitle("Upload Receptor");
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("chemical/x-pdb", ".pdb", ".pdbqt");
        
        Button confirm = new Button("Use This File", e -> {
            try {
                String filename = buffer.getFileName();
                Path tempDir = Files.createTempDirectory("docking_");
                receptorPath = tempDir.resolve(filename);
                Files.copy(buffer.getInputStream(), receptorPath);
                receptorFileLabel.setValue(filename);
                Notification.show("Receptor loaded: " + filename, 3000, Notification.Position.TOP_CENTER);
                uploadDialog.close();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        uploadDialog.add(upload, confirm);
        uploadDialog.open();
    }
    
    private void uploadLigandFile() {
        Dialog uploadDialog = new Dialog();
        uploadDialog.setHeaderTitle("Upload Ligand");
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("chemical/x-sdf", ".sdf", ".mol", ".pdbqt");
        
        Button confirm = new Button("Use This File", e -> {
            try {
                String filename = buffer.getFileName();
                Path tempDir = Files.createTempDirectory("docking_");
                ligandPath = tempDir.resolve(filename);
                Files.copy(buffer.getInputStream(), ligandPath);
                ligandFileLabel.setValue(filename);
                Notification.show("Ligand loaded: " + filename, 3000, Notification.Position.TOP_CENTER);
                uploadDialog.close();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        uploadDialog.add(upload, confirm);
        uploadDialog.open();
    }
    
    private void updateToolParams() {
        Tab selected = (Tab) toolTabs.getSelectedTab();
        String tool = selected.getLabel();
        
        // Update parameter hints based on selected tool
        Notification.show("Selected: " + tool, 1500, Notification.Position.TOP_CENTER);
    }
    
    private void runDocking() {
        if (receptorPath == null || ligandPath == null) {
            Notification.show("Please upload both receptor and ligand files", 5000)
                .addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        }
        
        Tab selected = (Tab) toolTabs.getSelectedTab();
        String tool = selected.getLabel();
        
        runButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setValue(0.3);
        
        // Run docking in background
        UI.getCurrent().access(() -> {
            try {
                DockingResult[] results = dockingService.runVinaDocking(
                    receptorPath, ligandPath,
                    centerX.getValue().doubleValue(),
                    centerY.getValue().doubleValue(),
                    centerZ.getValue().doubleValue(),
                    sizeX.getValue().doubleValue(),
                    sizeY.getValue().doubleValue(),
                    sizeZ.getValue().doubleValue(),
                    exhaustiveness.getValue().intValue(),
                    numModes.getValue().intValue()
                );
                
                UI.getCurrent().access(() -> {
                    resultsGrid.setItems(Arrays.asList(results));
                    progressBar.setValue(1.0);
                    Notification.show("Docking complete! Found " + results.length + " poses", 5000)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    runButton.setEnabled(true);
                });
            } catch (Exception ex) {
                UI.getCurrent().access(() -> {
                    Notification.show("Docking failed: " + ex.getMessage(), 5000)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    runButton.setEnabled(true);
                    progressBar.setVisible(false);
                });
            }
        });
    }
    
    private void displayResult(DockingResult result) {
        viewerPanel.removeAll();
        
        H4 resultTitle = new H4("Mode " + result.getMode() + " - ΔG: " + result.getBindingAffinity() + " kcal/mol");
        viewerPanel.add(resultTitle);
        
        // Embed NGL.js viewer
        IFrame nglViewer = new IFrame();
        nglViewer.setSrc("https://nglviewer.org/ngl/?viewport=0.3,0.6,0.8");
        nglViewer.setWidth("100%");
        nglViewer.setHeight("350px");
        nglViewer.addClassName("ngl-iframe");
        
        viewerPanel.add(nglViewer);
        
        // Add download button
        Button downloadBtn = new Button("Download Pose PDB", VaadinIcon.DOWNLOAD.create());
        downloadBtn.addClickListener(e -> {
            Notification.show("Downloading pose...", 2000);
        });
        viewerPanel.add(downloadBtn);
    }
    
    // Inner classes for UI components
    private static class FileTypeFilter {
        private final List<String> extensions;
        private final String description;
        
        public FileTypeFilter(List<String> extensions, String description) {
            this.extensions = extensions;
            this.description = description;
        }
    }
    
    private static class Section extends VerticalLayout {
        public Section() {
            addClassName("config-section");
        }
    }
    
    private static class Accordion extends VerticalLayout {
        private final Map<String, Paragraph> items = new LinkedHashMap<>();
        
        public Accordion() {
            addClassName("learning-accordion");
        }
        
        public void add(String title, Paragraph content) {
            Button item = new Button(title, VaadinIcon.CHEVRON_DOWN.create());
            item.addClassName("accordion-item");
            VerticalLayout contentPanel = new VerticalLayout(content);
            contentPanel.setVisible(false);
            contentPanel.addClassName("accordion-content");
            
            item.addClickListener(e -> {
                boolean isVisible = contentPanel.isVisible();
                contentPanel.setVisible(!isVisible);
                item.setIcon(VaadinIcon.CHEVRON_DOWN.create());
            });
            
            items.put(title, content);
            add(item, contentPanel);
        }
    }
}
