package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;

import java.util.*;

@PageTitle("Pharmacophore | DrugCompass")
public class PharmacophoreView extends MainLayout {
    
    public PharmacophoreView() {
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("🧭 Pharmacophore Modeling");
        Paragraph desc = new Paragraph("Detect, generate, and use pharmacophores for drug design");
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
        
        // Input Section
        Section inputSection = new Section();
        H4 inputHeader = new H4("🧪 Input");
        inputHeader.addClassName("section-title");
        
        TextField smilesInput = new TextField("Active Compound SMILES");
        smilesInput.setPlaceholder("Enter SMILES of active compound");
        smilesInput.setWidth("100%");
        
        ComboBox<String> phaseSelect = new ComboBox<>("Phase");
        phaseSelect.setItems("AutoDock Vina", "Phase (Schrödinger)", "LigandScout", "Canvas");
        phaseSelect.setValue("AutoDock Vina");
        phaseSelect.setWidth("100%");
        
        Button generateBtn = new Button("🧭 Generate Pharmacophore", VaadinIcon.MAP_MARKER.create());
        generateBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY, ButtonVariant.LUMBO_LARGE);
        generateBtn.setWidth("100%");
        generateBtn.addClickListener(e -> {
            Notification.show("Pharmacophore generation started...", 3000);
        });
        
        inputSection.add(inputHeader, smilesInput, phaseSelect, generateBtn);
        
        // Feature Selection
        Section featureSection = new Section();
        H4 featureHeader = new H4("⚙️ Pharmacophore Features");
        featureHeader.addClassName("section-title");
        
        ComboBox<String> featureType = new ComboBox<>("Feature Type");
        featureType.setItems("Hydrogen Bond Donor", "Hydrogen Bond Acceptor", "Aromatic Ring", 
                            "Hydrophobic", "Positively Charged", "Negatively Charged", "All Features");
        featureType.setValue("All Features");
        featureType.setWidth("100%");
        
        ComboBox<String> minFeatures = new ComboBox<>("Min Features");
        minFeatures.setItems("2", "3", "4", "5", "6");
        minFeatures.setValue("3");
        minFeatures.setWidth("100%");
        
        featureSection.add(featureType, minFeatures);
        
        // Search Parameters
        Section searchSection = new Section();
        H4 searchHeader = new H4("🔍 Pharmacophore Search");
        searchHeader.addClassName("section-title");
        
        ComboBox<String> searchType = new ComboBox<>("Search Type");
        searchType.setItems("Similarity", "Substructure", "Exclusion");
        searchType.setValue("Similarity");
        searchType.setWidth("100%");
        
        Button searchBtn = new Button("🔍 Search Database", VaadinIcon.SEARCH.create());
        searchBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS);
        searchBtn.setWidth("100%");
        
        searchSection.add(searchType, searchBtn);
        
        // 3D Viewer
        Section viewerSection = new Section();
        H4 viewerHeader = new H4("🧬 3D Pharmacophore");
        viewerHeader.addClassName("section-title");
        
        VerticalLayout viewer3d = new VerticalLayout();
        viewer3d.setWidth("100%");
        viewer3d.setHeight("200px");
        viewer3d.addClassName("viewer-panel");
        viewer3d.add(new Text("🧭 Pharmacophore visualization - Generate to view"));
        
        viewerSection.add(viewer3d);
        
        // Learning
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("What is Pharmacophore?", new Paragraph(
            "A pharmacophore is the ensemble of steric and electronic features\n" +
            "necessary for optimal molecular interactions with a specific\n" +
            "biological target:\n\n" +
            "• Hydrogen bond donors/acceptors\n" +
            "• Aromatic rings\n" +
            "• Hydrophobic regions\n" +
            "• Charged groups"
        ));
        learningAccordion.add("How Pharmacophore Modeling Works", new Paragraph(
            "1. Collect active compounds\n" +
            "2. Align molecules by common features\n" +
            "3. Identify conserved features\n" +
            "4. Generate 3D pharmacophore model\n" +
            "5. Use model for virtual screening"
        ));
        
        controlPanel.add(inputSection, featureSection, searchSection, viewerSection, learningAccordion);
        
        // Results Panel
        VerticalLayout resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        H4 resultsHeader = new H4("📊 Detected Features");
        resultsHeader.addClassName("section-title");
        
        com.vaadin.flow.component.grid.Grid<String> featuresGrid = new com.vaadin.flow.component.grid.Grid<>();
        featuresGrid.setWidth("100%");
        featuresGrid.setHeight("200px");
        featuresGrid.addColumn(s -> s).setHeader("Feature");
        featuresGrid.addColumn(s -> s).setHeader("Type");
        featuresGrid.addColumn(s -> s).setHeader("Location");
        featuresGrid.setItems(Arrays.asList(
            "HBD_1\tHydrogen Bond Donor\tCenter: 5.2, 3.1, 2.8",
            "HBA_1\tHydrogen Bond Acceptor\tCenter: 3.4, 4.2, 1.9",
            "AROM_1\tAromatic Ring\tCenter: 2.1, 5.6, 3.2",
            "HYDRO_1\tHydrophobic\tCenter: 6.8, 2.3, 4.1"
        ));
        
        H4 matchHeader = new H4("🔗 Database Matches");
        matchHeader.addClassName("section-title");
        
        com.vaadin.flow.component.grid.Grid<String> matchGrid = new com.vaadin.flow.component.grid.Grid<>();
        matchGrid.setWidth("100%");
        matchGrid.setHeight("250px");
        matchGrid.addColumn(s -> s).setHeader("Compound");
        matchGrid.addColumn(s -> s).setHeader("Match Score");
        matchGrid.addColumn(s -> s).setHeader("Features");
        matchGrid.setItems(Arrays.asList(
            "Compound_A\t0.95\t4/4",
            "Compound_B\t0.89\t3/4",
            "Compound_C\t0.82\t3/4"
        ));
        
        resultsPanel.add(resultsHeader, featuresGrid, matchHeader, matchGrid);
        
        splitLayout.addToPrimary(controlPanel);
        splitLayout.addToSecondary(resultsPanel);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, splitLayout);
        mainContainer.setFlexGrow(1, splitLayout);
        
        add(mainContainer);
    }
    
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
