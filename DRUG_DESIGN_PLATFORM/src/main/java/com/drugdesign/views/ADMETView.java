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
import com.drugdesign.services.ChemistryService;
import com.drugdesign.model.MolecularDescriptor;

import java.nio.file.*;
import java.util.*;

@PageTitle("ADMET Prediction | DrugCompass")
public class ADMETView extends MainLayout {
    
    private final ChemistryService chemistryService;
    
    private TextField smilesInput;
    private Upload sdfUpload;
    private TextField compoundLabel;
    private Grid<MolecularDescriptor> resultsGrid;
    private VerticalLayout radarChart;
    private ProgressBar progressBar;
    
    private Path uploadedSdf;
    
    public ADMETView() {
        this.chemistryService = new ChemistryService();
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("💊 ADMET Prediction");
        Paragraph desc = new Paragraph("Predict Absorption, Distribution, Metabolism, Excretion, and Toxicity properties");
        desc.addClassName("header-description");
        
        Header header = new Header();
        header.add(title, desc);
        header.addClassName("module-header");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(35);
        
        // Input Panel
        VerticalLayout inputPanel = new VerticalLayout();
        inputPanel.setWidth("100%");
        inputPanel.setPadding(true);
        inputPanel.addClassName("control-panel");
        
        // SMILES Input
        Section smilesSection = new Section();
        H4 smilesHeader = new H4("🧪 Compound Input");
        smilesHeader.addClassName("section-title");
        
        smilesInput = new TextField("SMILES");
        smilesInput.setPlaceholder("e.g., CC(=O)Oc1ccccc1C(=O)O");
        smilesInput.setWidth("100%");
        
        Button predictBtn = new Button("🔮 Predict Properties", VaadinIcon.MAGIC.create());
        predictBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY, ButtonVariant.LUMBO_LARGE);
        predictBtn.setWidth("100%");
        predictBtn.addClickListener(e -> predictProperties());
        
        HorizontalLayout smilesRow = new HorizontalLayout(smilesInput, predictBtn);
        smilesRow.setFlexGrow(1, smilesInput);
        
        Button loadBtn = new Button("Load Sample", VaadinIcon.DOWNLOAD.create());
        loadBtn.addClickListener(e -> {
            smilesInput.setValue("CC(=O)Oc1ccccc1C(=O)O"); // Aspirin
        });
        
        smilesSection.add(smilesHeader, smilesRow, loadBtn);
        
        // Quick Predictions
        Section quickSection = new Section();
        H4 quickHeader = new H4("⚡ Quick Predictions");
        quickHeader.addClassName("section-title");
        
        Grid<String> quickGrid = new Grid<>();
        quickGrid.setWidth("100%");
        quickGrid.addColumn(s -> s).setHeader("Property");
        quickGrid.addColumn(s -> s).setHeader("Value");
        quickGrid.setItems(Arrays.asList(
            "Lipinski Rule of 5\t✓ Pass",
            "Bioavailability\t✓ Good (70%)",
            "BBB Permeability\t⚠ Moderate",
            "Solubility\t✓ High",
            "CYP3A4 Inhibitor\t⚠ Yes"
        ));
        
        quickSection.add(quickGrid);
        
        // Detailed Parameters
        Section detailSection = new Section();
        H4 detailHeader = new H4("📊 Detailed ADMET");
        detailHeader.addClassName("section-title");
        
        Checkbox lipinski = new Checkbox("Lipinski Rule of 5");
        Checkbox veber = new Checkbox("Veber's Rule");
        Checkbox bbb = new Checkbox("BBB Permeability");
        Checkbox solubility = new Checkbox("Aqueous Solubility");
        Checkbox metabolism = new Checkbox("CYP Metabolism");
        Checkbox toxicity = new Checkbox("Toxicity Alerts (PAINS)");
        Checkbox hERG = new Checkbox("hERG Blockade");
        Checkbox dili = new Checkbox("DILI Risk");
        
        lipinski.setValue(true); veber.setValue(true); bbb.setValue(true);
        solubility.setValue(true); metabolism.setValue(true); toxicity.setValue(true);
        
        detailSection.add(lipinski, veber, bbb, solubility, metabolism, toxicity, hERG, dili);
        
        // Learning
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("What is ADMET?", new Paragraph(
            "ADMET encompasses the pharmacokinetic properties of drug candidates:\n\n" +
            "• Absorption - How the drug enters bloodstream\n" +
            "• Distribution - Where the drug spreads in the body\n" +
            "• Metabolism - How the drug is broken down\n" +
            "• Excretion - How the drug is eliminated\n" +
            "• Toxicity - Safety and side effects"
        ));
        learningAccordion.add("Lipinski's Rule of 5", new Paragraph(
            "A compound is likely to be orally active if:\n" +
            "• MW < 500 Da\n" +
            "• LogP < 5\n" +
            "• HBD ≤ 5\n" +
            "• HBA ≤ 10\n" +
            "Multiple violations may indicate poor oral bioavailability."
        ));
        learningAccordion.add("PAINS Alerts", new Paragraph(
            "Pan-Assay Interference Compounds (PAINS) are frequent hitters\n" +
            "that appear active in many screens but aren't genuine leads.\n" +
            "Examples: catechols, quinones, Michael acceptors."
        ));
        
        inputPanel.add(smilesSection, quickSection, detailSection, learningAccordion);
        
        // Results Panel
        VerticalLayout resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        H4 resultsHeader = new H4("📊 Prediction Results");
        resultsHeader.addClassName("section-title");
        
        resultsGrid = new Grid<>(MolecularDescriptor.class);
        resultsGrid.setWidth("100%");
        resultsGrid.setHeight("300px");
        resultsGrid.setColumns("name", "value", "unit", "category");
        resultsGrid.getColumnByKey("name").setHeader("Descriptor");
        resultsGrid.getColumnByKey("value").setHeader("Value");
        resultsGrid.getColumnByKey("unit").setHeader("Unit");
        resultsGrid.getColumnByKey("category").setHeader("Category");
        
        // Radar Chart Placeholder
        radarChart = new VerticalLayout();
        radarChart.setWidth("100%");
        radarChart.setHeight("300px");
        radarChart.addClassName("viewer-panel");
        radarChart.add(new Text("🕸️ ADMET Radar Chart - Run prediction to visualize"));
        
        // Score Card
        Card scoreCard = new Card();
        scoreCard.addClassName("score-card");
        VerticalLayout scoreLayout = new VerticalLayout();
        scoreLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        H4 scoreTitle = new H4("🧪 Drug-like Score");
        Paragraph scoreValue = new Paragraph("85/100");
        scoreValue.addClassName("score-value");
        Paragraph scoreStatus = new Paragraph("✓ Good drug-like properties");
        scoreStatus.addClassName(LumoUtility.TextColor.SUCCESS);
        
        scoreLayout.add(scoreTitle, scoreValue, scoreStatus);
        scoreCard.add(scoreLayout);
        
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        
        resultsPanel.add(resultsHeader, resultsGrid, scoreCard, progressBar, radarChart);
        
        splitLayout.addToPrimary(inputPanel);
        splitLayout.addToSecondary(resultsPanel);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, splitLayout);
        mainContainer.setFlexGrow(1, splitLayout);
        
        add(mainContainer);
    }
    
    private void predictProperties() {
        String smiles = smilesInput.getValue();
        if (smiles == null || smiles.isEmpty()) {
            Notification.show("Please enter a SMILES string", 5000).addThemeVariants(NotificationVariant.LUMBO_WARNING);
            return;
        }
        
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        
        UI.getCurrent().access(() -> {
            try {
                List<MolecularDescriptor> descriptors = chemistryService.calculateADMET(smiles);
                
                UI.getCurrent().access(() -> {
                    resultsGrid.setItems(descriptors);
                    progressBar.setVisible(false);
                    radarChart.removeAll();
                    radarChart.add(new Text("✅ ADMET Profile Generated - Visualize properties above"));
                    Notification.show("Prediction complete!", 3000).addThemeVariants(NotificationVariant.LUMBO_SUCCESS);
                });
            } catch (Exception ex) {
                UI.getCurrent().access(() -> {
                    Notification.show("Prediction failed: " + ex.getMessage(), 5000).addThemeVariants(NotificationVariant.LUMBO_ERROR);
                    progressBar.setVisible(false);
                });
            }
        });
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
