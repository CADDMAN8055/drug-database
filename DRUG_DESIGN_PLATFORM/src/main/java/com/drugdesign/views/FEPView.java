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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;

import java.util.*;

@PageTitle("FEP | DrugCompass")
public class FEPView extends MainLayout {
    
    public FEPView() {
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("⚗️ Free Energy Perturbation");
        Paragraph desc = new Paragraph("Calculate binding free energy differences using alchemical transformations");
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
        
        // Ligand Input
        Section ligandSection = new Section();
        H4 ligandHeader = new H4("🧪 Ligand Pair");
        ligandHeader.addClassName("section-title");
        
        TextField ligand1Smiles = new TextField("Ligand 1 (Reference)");
        ligand1Smiles.setPlaceholder("SMILES of reference ligand");
        ligand1Smiles.setWidth("100%");
        
        TextField ligand2Smiles = new TextField("Ligand 2 (Variant)");
        ligand2Smiles.setPlaceholder("SMILES of variant ligand");
        ligand2Smiles.setWidth("100%");
        
        ligandSection.add(ligand1Smiles, ligand2Smiles);
        
        // FEP Configuration
        Section fepSection = new Section();
        H4 fepHeader = new H4("⚙️ FEP Parameters");
        fepHeader.addClassName("section-title");
        
        TextField topology = new TextField("Topology");
        topology.setReadOnly(true);
        topology.setValue("GROMOS54A7");
        
        TextField solvent = new TextField("Solvent");
        solvent.setReadOnly(true);
        solvent.setValue("SPC/E Water");
        
        HorizontalLayout lambdaRow = new HorizontalLayout();
        NumberField lambdaWindows = new NumberField("Lambda Windows");
        lambdaWindows.setValue(8.0);
        lambdaWindows.setMin(4);
        lambdaWindows.setMax(24);
        
        NumberField simulationTime = new NumberField("Time per λ (ns)");
        simulationTime.setValue(5.0);
        simulationTime.setMin(1);
        simulationTime.setMax(50);
        
        lambdaRow.add(lambdaWindows, simulationTime);
        
        ComboBox<String> method = new ComboBox<>("Method");
        method.setItems("TI (Thermodynamic Integration)", "FEP/REST", "BAR", "MBAR");
        method.setValue("TI (Thermodynamic Integration)");
        method.setWidth("100%");
        
        fepSection.add(topology, solvent, lambdaRow, method);
        
        // Buttons
        Button setupBtn = new Button("⚙️ Setup FEP", VaadinIcon.COG.create());
        setupBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        setupBtn.setWidth("100%");
        
        Button runBtn = new Button("⚡ Run FEP", VaadinIcon.PLAY.create());
        runBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS, ButtonVariant.LUMBO_LARGE);
        runBtn.setWidth("100%");
        runBtn.addClickListener(e -> {
            Notification.show("FEP calculation started... (simulated)", 3000);
        });
        
        // Learning
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("What is FEP?", new Paragraph(
            "Free Energy Perturbation (FEP) calculates binding free energy\n" +
            "differences between similar ligands by transforming one into\n" +
            "another through alchemical intermediates:\n\n" +
            "ΔΔG = G(variant) - G(reference)\n\n" +
            "More accurate than docking for ranking analogs."
        ));
        learningAccordion.add("FEP Workflow", new Paragraph(
            "1. Prepare ligand structures\n" +
            "2. Generate topology and parameters\n" +
            "3. Define λ windows for transformation\n" +
            "4. Run MD at each λ\n" +
            "5. Analyze with TI, BAR, or MBAR\n" +
            "6. Report ΔΔG and uncertainty"
        ));
        learningAccordion.add("Best Practices", new Paragraph(
            "• Use ≥8 λ windows for smooth transformation\n" +
            "• 5-10 ns per window for convergence\n" +
            "• Check overlap between adjacent λ\n" +
            "• Compare forward and backward transformations\n" +
            "• ΔΔG < 1 kcal/mol = challenging"
        ));
        
        controlPanel.add(ligandSection, fepSection, setupBtn, runBtn, learningAccordion);
        
        // Results Panel
        VerticalLayout resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        H4 resultsHeader = new H4("📊 FEP Results");
        resultsHeader.addClassName("section-title");
        
        // ΔΔG Display
        Card deltaCard = new Card();
        deltaCard.addClassName("result-card");
        VerticalLayout deltaLayout = new VerticalLayout();
        deltaLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        H4 deltaLabel = new H4("ΔΔG (Variant - Reference)");
        Paragraph deltaValue = new Paragraph("-2.45 ± 0.32 kcal/mol");
        deltaValue.addClassName("large-result");
        
        Paragraph interpretation = new Paragraph("✅ Variant binds more strongly");
        interpretation.addClassName(LumoUtility.TextColor.SUCCESS);
        
        deltaLayout.add(deltaLabel, deltaValue, interpretation);
        deltaCard.add(deltaLayout);
        
        // Lambda Profile
        VerticalLayout chartPanel = new VerticalLayout();
        chartPanel.setWidth("100%");
        chartPanel.setHeight("200px");
        chartPanel.addClassName("viewer-panel");
        chartPanel.add(new Text("📈 λ vs ΔG Profile Graph"));
        
        // Detailed Results
        com.vaadin.flow.component.grid.Grid<String> detailGrid = new com.vaadin.flow.component.grid.Grid<>();
        detailGrid.setWidth("100%");
        detailGrid.setHeight("150px");
        detailGrid.addColumn(s -> s).setHeader("λ");
        detailGrid.addColumn(s -> s).setHeader("ΔG (kcal/mol)");
        detailGrid.addColumn(s -> s).setHeader("dG/dλ");
        detailGrid.setItems(Arrays.asList(
            "0.00\t0.00 ± 0.00\t-0.12",
            "0.14\t-0.45 ± 0.08\t-0.89",
            "0.29\t-1.23 ± 0.12\t-1.45",
            "0.43\t-2.01 ± 0.15\t-1.78",
            "0.57\t-2.89 ± 0.18\t-2.12",
            "0.71\t-3.45 ± 0.21\t-1.98",
            "0.86\t-4.12 ± 0.25\t-1.56",
            "1.00\t-4.89 ± 0.28\t-1.23"
        ));
        
        resultsPanel.add(resultsHeader, deltaCard, chartPanel, detailGrid);
        
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
