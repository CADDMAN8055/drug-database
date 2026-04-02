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
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;

import java.util.*;

@PageTitle("DMPK | DrugCompass")
public class DMPKView extends MainLayout {
    
    private TextField smilesInput;
    
    public DMPKView() {
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("💉 DMPK Analysis");
        Paragraph desc = new Paragraph("Drug Metabolism and Pharmacokinetics - understand ADME properties in detail");
        desc.addClassName("header-description");
        
        Header header = new Header();
        header.add(title, desc);
        header.addClassName("module-header");
        
        // Input Row
        HorizontalLayout inputRow = new HorizontalLayout();
        inputRow.setWidth("100%");
        inputRow.setPadding(true);
        inputRow.addClassName("control-panel");
        
        smilesInput = new TextField("Compound SMILES");
        smilesInput.setPlaceholder("Enter SMILES for DMPK analysis");
        smilesInput.setWidth("400px");
        
        Button analyzeBtn = new Button("🔬 Analyze DMPK", VaadinIcon.MICROSCOPE.create());
        analyzeBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY, ButtonVariant.LUMBO_LARGE);
        analyzeBtn.addClickListener(e -> analyzeDMPK());
        
        Button loadSample = new Button("Load Sample", VaadinIcon.DOWNLOAD.create());
        loadSample.addClickListener(e -> {
            smilesInput.setValue("CC(=O)Nc1ccc(C(=O)N(C)C)cc1"); // Example
        });
        
        inputRow.add(smilesInput, analyzeBtn, loadSample);
        
        // Tabs for different DMPK aspects
        Tabs tabs = new Tabs(
            new Tab("Absorption"),
            new Tab("Distribution"),
            new Tab("Metabolism"),
            new Tab("Excretion"),
            new Tab("PK Parameters")
        );
        tabs.addClassName("module-tabs");
        
        // Content Panels
        VerticalLayout contentArea = new VerticalLayout();
        contentArea.setWidth("100%");
        contentArea.setPadding(true);
        contentArea.setFlexGrow(1, tabs);
        
        // Absorption Tab
        VerticalLayout absorptionTab = createAbsorptionTab();
        
        // Distribution Tab
        VerticalLayout distributionTab = createDistributionTab();
        
        // Metabolism Tab
        VerticalLayout metabolismTab = createMetabolismTab();
        
        // Excretion Tab
        VerticalLayout excretionTab = createExcretionTab();
        
        // PK Parameters Tab
        VerticalLayout pkTab = createPKTab();
        
        // Tab content map
        Map<Tab, VerticalLayout> tabContent = new HashMap<>();
        tabContent.put(tabs.getTabAt(0), absorptionTab);
        tabContent.put(tabs.getTabAt(1), distributionTab);
        tabContent.put(tabs.getTabAt(2), metabolismTab);
        tabContent.put(tabs.getTabAt(3), excretionTab);
        tabContent.put(tabs.getTabAt(4), pkTab);
        
        tabs.addSelectedChangeListener(e -> {
            Tab selected = e.getSelectedTab();
            contentArea.removeAll();
            if (selected != null) {
                contentArea.add(tabContent.get(selected));
            }
        });
        
        // Default to absorption
        contentArea.add(absorptionTab);
        
        // Learning Section
        Section learningSection = new Section();
        H4 learningHeader = new H4("📚 DMPK Learning Center");
        learningHeader.addClassName("section-title");
        
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("What is DMPK?", new Paragraph(
            "DMPK (Drug Metabolism and Pharmacokinetics) studies what the\n" +
            "body does to a drug - as opposed to what the drug does to the\n" +
            "body (pharmacodynamics):\n\n" +
            "• Absorption: How the drug enters bloodstream\n" +
            "• Distribution: Where in the body it goes\n" +
            "• Metabolism: How it's chemically transformed\n" +
            "• Excretion: How it's eliminated"
        ));
        learningAccordion.add("Why DMPK Matters", new Paragraph(
            "• ~60% of drug failures are due to DMPK issues\n" +
            "• Poor solubility, low permeability = low bioavailability\n" +
            "• High clearance = short duration of action\n" +
            "• Drug-drug interactions via CYP enzymes\n" +
            "• Early DMPK optimization saves time and money"
        ));
        learningAccordion.add("Key PK Parameters", new Paragraph(
            "• AUC: Total drug exposure over time\n" +
            "• Cmax: Maximum concentration\n" +
            "• Tmax: Time to reach Cmax\n" +
            "• CL: Clearance (elimination rate)\n" +
            "• Vd: Volume of distribution\n" +
            "• t½: Half-life"
        ));
        
        learningSection.add(learningHeader, learningAccordion);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, inputRow, tabs, contentArea, learningSection);
        mainContainer.setFlexGrow(1);
        
        add(mainContainer);
    }
    
    private VerticalLayout createAbsorptionTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        com.vaadin.flow.component.grid.Grid<String> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.setWidth("100%");
        grid.addColumn(s -> s).setHeader("Parameter");
        grid.addColumn(s -> s).setHeader("Value");
        grid.addColumn(s -> s).setHeader("Interpretation");
        grid.setItems(Arrays.asList(
            "Fa (% Absorbed)\t85%\t✓ Excellent",
            "F (Bioavailability)\t72%\t✓ Good",
            "Ka (absorption rate)\t0.95 min⁻¹\t✓ Fast",
            "Tmax\t1.2 hours\tOptimal",
            "Caco-2 Papp\t25 × 10⁻⁶ cm/s\t✓ High permeability",
            "MDCK Permeability\tHigh\t✓ Good for CNS"
        ));
        
        tab.add(new H4("📊 Absorption Parameters"), grid);
        return tab;
    }
    
    private VerticalLayout createDistributionTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        com.vaadin.flow.component.grid.Grid<String> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.setWidth("100%");
        grid.addColumn(s -> s).setHeader("Parameter");
        grid.addColumn(s -> s).setHeader("Value");
        grid.addColumn(s -> s).setHeader("Interpretation");
        grid.setItems(Arrays.asList(
            "Vd (Volume of Distribution)\t2.5 L/kg\tModerate",
            "PPB (Plasma Protein Binding)\t92%\tHigh binding",
            "BBB Penetration\t0.45\t⚠ Limited",
            "Kpu (Kidney:Plasma)\t0.8\tEquilibrium",
            "Blood:Plasma Ratio\t0.85\tRBC partitioning"
        ));
        
        tab.add(new H4("📊 Distribution Parameters"), grid);
        return tab;
    }
    
    private VerticalLayout createMetabolismTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        com.vaadin.flow.component.grid.Grid<String> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.setWidth("100%");
        grid.addColumn(s -> s).setHeader("Enzyme");
        grid.addColumn(s -> s).setHeader("Activity");
        grid.addColumn(s -> s).setHeader("Note");
        grid.setItems(Arrays.asList(
            "CYP3A4\tSubstrate (70%)\tMajor metabolic route",
            "CYP2D6\tSubstrate (15%)\tPolymorphic",
            "CYP2C19\tMinor (8%)\tPolymorphic",
            "CYP2C9\tMinor (5%)\t",
            "CYP1A2\tNone\tNo interaction",
            "CYP2C8\tNone\tNo interaction"
        ));
        
        tab.add(new H4("📊 Metabolic Profile (CYP Enzymes)"), grid);
        return tab;
    }
    
    private VerticalLayout createExcretionTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        com.vaadin.flow.component.grid.Grid<String> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.setWidth("100%");
        grid.addColumn(s -> s).setHeader("Parameter");
        grid.addColumn(s -> s).setHeader("Value");
        grid.addColumn(s -> s).setHeader("Interpretation");
        grid.setItems(Arrays.asList(
            "CL (Total Clearance)\t12.5 mL/min/kg\tModerate",
            "CLh (Hepatic)\t10.2 mL/min/kg\t",
            "CLr (Renal)\t2.3 mL/min/kg\tMinor",
            "fe (% excreted unchanged)\t18%\tMostly metabolized",
            "Renal transporters\tNot involved\t"
        ));
        
        tab.add(new H4("📊 Excretion Parameters"), grid);
        return tab;
    }
    
    private VerticalLayout createPKTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        com.vaadin.flow.component.grid.Grid<String> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.setWidth("100%");
        grid.addColumn(s -> s).setHeader("Parameter");
        grid.addColumn(s -> s).setHeader("Value");
        grid.addColumn(s -> s).setHeader("Units");
        grid.setItems(Arrays.asList(
            "AUC (0-∞)\t45.2\tμg·h/mL",
            "Cmax\t8.5\tμg/mL",
            "Tmax\t1.2\thours",
            "t½ (Half-life)\t4.5\thours",
            "CL\t15.2\tmL/min/kg",
            "MRT\t6.8\thours",
            "Vss\t2.8\tL/kg"
        ));
        
        tab.add(new H4("📊 Pharmacokinetic Parameters"), grid);
        return tab;
    }
    
    private void analyzeDMPK() {
        String smiles = smilesInput.getValue();
        if (smiles == null || smiles.isEmpty()) {
            Notification.show("Please enter a SMILES string", 5000)
                .addThemeVariants(NotificationVariant.LUMBO_WARNING);
            return;
        }
        Notification.show("DMPK analysis complete! (simulated)", 3000);
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
