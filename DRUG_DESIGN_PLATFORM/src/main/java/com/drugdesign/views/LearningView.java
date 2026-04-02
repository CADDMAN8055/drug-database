package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;

import java.util.*;

@PageTitle("Learning Center | DrugCompass")
public class LearningView extends MainLayout {
    
    public LearningView() {
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("📚 Learning Center");
        Paragraph desc = new Paragraph("Master computational drug design with tutorials, documentation, and examples");
        desc.addClassName("header-description");
        
        Header header = new Header();
        header.add(title, desc);
        header.addClassName("module-header");
        
        // Search
        HorizontalLayout searchRow = new HorizontalLayout();
        searchRow.setWidth("100%");
        searchRow.setPadding(true);
        searchRow.addClassName("control-panel");
        
        TextField search = new TextField("🔍 Search tutorials...");
        search.setWidth("500px");
        search.addClassName("search-field");
        
        ComboBox<String> category = new ComboBox<>("Category");
        category.setItems("All", "Docking", "Molecular Dynamics", "QSAR", "ADMET", "Pharmacophore", "AI/ML");
        category.setValue("All");
        
        ComboBox<String> level = new ComboBox<>("Level");
        level.setItems("All Levels", "Beginner", "Intermediate", "Advanced");
        level.setValue("All Levels");
        
        searchRow.add(search, category, level);
        
        // Tutorial Categories
        Tabs tutorialTabs = new Tabs(
            new Tab("📖 Tutorials"),
            new Tab("📖 Documentation"),
            new Tab("🎬 Video Library"),
            new Tab("📂 Sample Datasets"),
            new Tab("🔧 Tool Installation")
        );
        tutorialTabs.addClassName("module-tabs");
        
        VerticalLayout contentArea = new VerticalLayout();
        contentArea.setWidth("100%");
        contentArea.setPadding(true);
        contentArea.setSpacing(true);
        
        // Tutorials Content
        contentArea.add(createTutorialsSection());
        
        // Add tabs change listener
        tutorialTabs.addSelectedChangeListener(e -> {
            Tab selected = e.getSelectedTab();
            contentArea.removeAll();
            if (selected != null) {
                String tabName = selected.getLabel();
                if (tabName.contains("Tutorials")) contentArea.add(createTutorialsSection());
                else if (tabName.contains("Documentation")) contentArea.add(createDocsSection());
                else if (tabName.contains("Video")) contentArea.add(createVideoSection());
                else if (tabName.contains("Dataset")) contentArea.add(createDatasetSection());
                else if (tabName.contains("Installation")) contentArea.add(createInstallationSection());
            }
        });
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, searchRow, tutorialTabs, contentArea);
        
        add(mainContainer);
    }
    
    private VerticalLayout createTutorialsSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidth("100%");
        section.setSpacing(true);
        
        // Quick Start
        H3 quickStartHeader = new H3("🚀 Quick Start Guides");
        
        HorizontalLayout quickStartRow = new HorizontalLayout();
        quickStartRow.setWidth("100%");
        quickStartRow.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        
        quickStartRow.add(
            createTutorialCard("Molecular Docking in 5 Minutes", 
                             "Learn the basics of AutoDock Vina docking",
                             "Beginner", "15 min",
                             VaadinIcon.LAB.create()),
            createTutorialCard("MD Simulation Setup",
                             "Prepare your first molecular dynamics simulation",
                             "Beginner", "20 min",
                             VaadinIcon.LIGHTNING.create()),
            createTutorialCard("Building QSAR Models",
                             "Create predictive models from scratch",
                             "Intermediate", "30 min",
                             VaadinIcon.ROBOT.create())
        );
        
        // Intermediate Tutorials
        H3 interHeader = new H3("📚 Intermediate Tutorials");
        
        VerticalLayout interList = new VerticalLayout();
        interList.setSpacing(false);
        
        interList.add(createTutorialItem(
            "Advanced Docking: Custom Grid Boxes & Scoring",
            "Learn to optimize docking parameters for difficult targets",
            "30 min", "Docking", "Intermediate"
        ));
        interList.add(createTutorialItem(
            "Free Energy Calculations with GROMACS",
            "Calculate binding free energies using alchemical methods",
            "45 min", "FEP", "Intermediate"
        ));
        interList.add(createTutorialItem(
            "Pharmacophore-Based Virtual Screening",
            "Use pharmacophores to find novel actives",
            "25 min", "Pharmacophore", "Intermediate"
        ));
        
        // Advanced Tutorials
        H3 advHeader = new H3("🎓 Advanced Tutorials");
        
        VerticalLayout advList = new VerticalLayout();
        advList.setSpacing(false);
        
        advList.add(createTutorialItem(
            "Deep Learning for Drug Discovery",
            "Build GNN models with PyTorch Geometric",
            "60 min", "AI/ML", "Advanced"
        ));
        advList.add(createTutorialItem(
            "FEP+ with Schrödinger Suite",
            "Advanced free energy perturbation methods",
            "90 min", "FEP", "Advanced"
        ));
        
        section.add(quickStartHeader, quickStartRow, interHeader, interList, advHeader, advList);
        return section;
    }
    
    private Card createTutorialCard(String title, String desc, String level, String time, 
                                    com.vaadin.flow.component.icon.Icon icon) {
        Card card = new Card();
        card.addClassName("tutorial-card");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.START);
        
        com.vaadin.flow.component.html.Span levelBadge = new com.vaadin.flow.component.html.Span(level);
        levelBadge.addClassName("level-badge");
        
        H4 titleH = new H4(title);
        Paragraph descP = new Paragraph(desc);
        descP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        HorizontalLayout metaRow = new HorizontalLayout();
        metaRow.add(new Text("⏱️ " + time));
        metaRow.add(levelBadge);
        
        Button startBtn = new Button("Start Tutorial", VaadinIcon.PLAY.create());
        startBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        
        layout.add(icon, titleH, descP, metaRow, startBtn);
        card.add(layout);
        
        return card;
    }
    
    private Card createTutorialItem(String title, String desc, String time, String category, String level) {
        Card card = new Card();
        card.addClassName("tutorial-item");
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        VerticalLayout info = new VerticalLayout();
        info.setAlignItems(FlexComponent.Alignment.START);
        
        H4 titleH = new H4(title);
        Paragraph descP = new Paragraph(desc);
        descP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        HorizontalLayout badges = new HorizontalLayout();
        badges.add(new Text("📁 " + category));
        badges.add(new Text("⏱️ " + time));
        com.vaadin.flow.component.html.Span levelSpan = new com.vaadin.flow.component.html.Span(level);
        levelSpan.addClassName("level-badge");
        badges.add(levelSpan);
        
        info.add(titleH, descP, badges);
        
        Button startBtn = new Button("Start", VaadinIcon.PLAY.create());
        startBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        
        layout.add(info, startBtn);
        card.add(layout);
        
        return card;
    }
    
    private VerticalLayout createDocsSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidth("100%");
        
        H3 header = new H3("📖 Documentation");
        
        Accordion docsAccordion = new Accordion();
        docsAccordion.add("AutoDock Vina Documentation", new Paragraph(
            "AutoDock Vina is a molecular docking program that uses efficient\n" +
            "gradient-optimization algorithm for global optimization.\n\n" +
            "Key parameters:\n" +
            "• exhaustiveness: Search thoroughness (default: 32)\n" +
            "• num_modes: Maximum binding modes (default: 9)\n" +
            "• energy_range: Energy difference cutoff"
        ));
        docsAccordion.add("GROMACS Reference", new Paragraph(
            "GROMACS is a molecular dynamics package mainly designed for\n" +
            "simulations of proteins, lipids, and nucleic acids.\n\n" +
            "Key commands:\n" +
            "• gmx pdb2gmx: Generate topology\n" +
            "• gmx editconf: Define simulation box\n" +
            "• gmx solvate: Add solvent\n" +
            "• gmx mdrun: Run MD simulation"
        ));
        docsAccordion.add("CDK API Reference", new Paragraph(
            "The Chemistry Development Kit (CDK) provides Java libraries\n" +
            "for chemoinformatics and computational chemistry.\n\n" +
            "Key modules:\n" +
            "• cdk-core: Core interfaces and classes\n" +
            "• cdk-descriptor: Molecular descriptors\n" +
            "• cdk-fingerprint: Molecular fingerprints"
        ));
        docsAccordion.add("DeepLearning4j Quickstart", new Paragraph(
            "DeepLearning4j is a deep learning library for Java.\n\n" +
            "For QSAR modeling:\n" +
            "1. Generate Morgan fingerprints\n" +
            "2. Create DataSets\n" +
            "3. Build MultiLayerNetwork\n" +
            "4. Train with RemoteTensorTrainingListener"
        ));
        
        section.add(header, docsAccordion);
        return section;
    }
    
    private VerticalLayout createVideoSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidth("100%");
        
        H3 header = new H3("🎬 Video Tutorials");
        
        HorizontalLayout videoRow = new HorizontalLayout();
        videoRow.setWidth("100%");
        videoRow.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        
        videoRow.add(
            createVideoCard("Molecular Docking Tutorial", "AutoDock Vina basics", "12:45"),
            createVideoCard("GROMACS Quickstart", "Run your first MD", "18:30"),
            createVideoCard("QSAR Model Building", "Hands-on example", "25:15")
        );
        
        section.add(header, videoRow);
        return section;
    }
    
    private Card createVideoCard(String title, String desc, String duration) {
        Card card = new Card();
        card.addClassName("video-card");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.START);
        
        H4 titleH = new H4("▶ " + title);
        Paragraph descP = new Paragraph(desc);
        descP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        Text durationText = new Text("⏱️ " + duration);
        
        layout.add(titleH, descP, durationText);
        card.add(layout);
        
        return card;
    }
    
    private VerticalLayout createDatasetSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidth("100%");
        
        H3 header = new H3("📂 Sample Datasets");
        
        com.vaadin.flow.component.grid.Grid<String> datasetGrid = new com.vaadin.flow.component.grid.Grid<>();
        datasetGrid.setWidth("100%");
        datasetGrid.addColumn(s -> s).setHeader("Dataset");
        datasetGrid.addColumn(s -> s).setHeader("Description");
        datasetGrid.addColumn(s -> s).setHeader("Size");
        datasetGrid.addColumn(s -> s).setHeader("Format");
        datasetGrid.setItems(Arrays.asList(
            "COX-2 Inhibitors\tSelective cyclooxygenase-2 inhibitors\t156 compounds\tSDF",
            "Kinase Inhibitors\tBroad kinase inhibition data\t1,200 compounds\tCSV",
            "BBBP Dataset\tBlood-brain barrier permeability\t2,039 compounds\tCSV",
            "HIV Protease\tHIV-1 protease inhibitors\t46 compounds\tPDB",
            "Ames Mutagenicity\tBacterial reverse mutation data\t9,800 compounds\tCSV"
        ));
        
        Button downloadBtn = new Button("📥 Download All Datasets", VaadinIcon.DOWNLOAD.create());
        downloadBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        
        section.add(header, datasetGrid, downloadBtn);
        return section;
    }
    
    private VerticalLayout createInstallationSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidth("100%");
        
        H3 header = new H3("🔧 Tool Installation Guides");
        
        Accordion installAccordion = new Accordion();
        installAccordion.add("AutoDock Vina Installation", new Paragraph(
            "# Ubuntu/Debian\n" +
            "sudo apt install autodock-vina\n\n" +
            "# Or download from GitHub:\n" +
            "wget https://github.com/ccsb-scripps/AutoDock-Vina/releases/download/v1.2.5/vina_1.2.5_linux_x86_64.tar.gz\n" +
            "tar -xzf vina_1.2.5_linux_x86_64.tar.gz\n" +
            "sudo mv vina /usr/local/bin/"
        ));
        installAccordion.add("GROMACS Installation", new Paragraph(
            "# Ubuntu/Debian\n" +
            "sudo apt install gromacs\n\n" +
            "# Or build from source:\n" +
            "git clone https://github.com/gromacs/gromacs.git\n" +
            "mkdir build && cd build\n" +
            "cmake .. -DGMX_BUILD_OWN_FFTW=ON\n" +
            "make -j$(nproc)\n" +
            "source /path/to/gromacs/bin/GMXRC"
        ));
        installAccordion.add("Python Environment Setup", new Paragraph(
            "# Create conda environment\n" +
            "conda create -n drugdesign python=3.9\n" +
            "conda activate drugdesign\n\n" +
            "# Install packages\n" +
            "pip install rdkit scikit-learn pytorch geometric\n" +
            "pip install deepchem dgllife chemberta"
        ));
        installAccordion.add("Java/Maven Setup", new Paragraph(
            "# Ubuntu/Debian\n" +
            "sudo apt install openjdk-17 maven\n\n" +
            "# Verify installation\n" +
            "java -version\n" +
            "mvn -version"
        ));
        
        section.add(header, installAccordion);
        return section;
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
