package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
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

@PageTitle("AI Tools | DrugCompass")
public class AIToolsView extends MainLayout {
    
    public AIToolsView() {
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("🚀 Advanced AI Tools");
        Paragraph desc = new Paragraph("Cutting-edge AI-powered drug discovery tools and models");
        desc.addClassName("header-description");
        
        Header header = new Header();
        header.add(title, desc);
        header.addClassName("module-header");
        
        // Tool Cards Grid
        VerticalLayout toolsGrid = new VerticalLayout();
        toolsGrid.setWidth("100%");
        toolsGrid.setPadding(true);
        toolsGrid.setSpacing(true);
        
        // Row 1: Molecular Generation, Activity Prediction, Retrosynthesis
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setWidth("100%");
        row1.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        row1.setSpacing(true);
        
        row1.add(
            createToolCard("🧬", "Molecular Generation", 
                          "Generate novel drug-like molecules using VAE, GAN, and transformer models",
                          VaadinIcon.GRID_BEAN.create(),
                          e -> openTool("Molecular Generation")),
            createToolCard("🎯", "Activity Prediction", 
                          "Predict biological activity using deep learning on molecular structure",
                          VaadinIcon.TARGET.create(),
                          e -> openTool("Activity Prediction")),
            createToolCard("🔄", "Retrosynthesis", 
                          "AI-powered retrosynthetic planning and synthetic route design",
                          VaadinIcon.EXCHANGE.create(),
                          e -> openTool("Retrosynthesis"))
        );
        
        // Row 2: Drug-Target Interaction, Property Prediction, Similarity
        HorizontalLayout row2 = new HorizontalLayout();
        row2.setWidth("100%");
        row2.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        row2.setSpacing(true);
        
        row2.add(
            createToolCard("🔗", "Drug-Target Interaction", 
                          "Predict binding affinity between drugs and protein targets",
                          VaadinIcon.CONNECT.create(),
                          e -> openTool("DTI")),
            createToolCard("📊", "Property Prediction", 
                          "Advanced ADMET prediction using graph neural networks",
                          VaadinIcon.CHART.create(),
                          e -> openTool("Property Prediction")),
            createToolCard("🔍", "Active Learning", 
                          "Smart compound selection for efficient experimental screening",
                          VaadinIcon.BRAIN.create(),
                          e -> openTool("Active Learning"))
        );
        
        // Row 3: Activity Cliffs, Molecular Optimization, Knowledge Graph
        HorizontalLayout row3 = new HorizontalLayout();
        row3.setWidth("100%");
        row3.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        row3.setSpacing(true);
        
        row3.add(
            createToolCard("🧗", "Activity Cliffs", 
                          "Identify and analyze activity cliff regions in chemical space",
                          VaadinIcon.CLIMB.create(),
                          e -> openTool("Activity Cliffs")),
            createToolCard("⚡", "Lead Optimization", 
                          "AI-guided optimization of hit compounds for improved potency",
                          VaadinIcon.LIGHTNING.create(),
                          e -> openTool("Lead Optimization")),
            createToolCard("🕸️", "Knowledge Graph", 
                          "Drug-target-disease knowledge graph for network medicine",
                          VaadinIcon.GRID.create(),
                          e -> openTool("Knowledge Graph"))
        );
        
        toolsGrid.add(row1, row2, row3);
        
        // Interactive Tool Workspace
        SplitLayout workspace = new SplitLayout();
        workspace.setSizeFull();
        workspace.setSplitterPosition(30);
        
        // Tool Configuration Panel
        VerticalLayout configPanel = new VerticalLayout();
        configPanel.setWidth("100%");
        configPanel.setPadding(true);
        configPanel.addClassName("control-panel");
        
        Section configSection = new Section();
        H4 configHeader = new H4("⚙️ AI Model Configuration");
        configHeader.addClassName("section-title");
        
        ComboBox<String> modelSelect = new ComboBox<>("AI Model");
        modelSelect.setItems(
            "ChemBERTa-2", "MolBERT", "GROVER", "Graphormer",
            "Transformer-CNN", "Ensemble (Recommended)"
        );
        modelSelect.setValue("ChemBERTa-2");
        modelSelect.setWidth("100%");
        
        ComboBox<String> taskSelect = new ComboBox<>("Task");
        taskSelect.setItems("Property Prediction", "Classification", "Generation", "Similarity");
        taskSelect.setValue("Property Prediction");
        taskSelect.setWidth("100%");
        
        Checkbox fineTune = new Checkbox("Enable Fine-tuning");
        Checkbox ensembleBoost = new Checkbox("Ensemble Boost (+5% accuracy)");
        
        TextField smilesInput = new TextField("Query SMILES");
        smilesInput.setPlaceholder("Enter compound SMILES");
        smilesInput.setWidth("100%");
        
        Button runBtn = new Button("🚀 Run AI Prediction", VaadinIcon.CLOUD.create());
        runBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS, ButtonVariant.LUMBO_LARGE);
        runBtn.setWidth("100%");
        runBtn.addClickListener(e -> {
            Notification.show("AI prediction running...", 3000);
        });
        
        configSection.add(configHeader, modelSelect, taskSelect, fineTune, ensembleBoost, smilesInput, runBtn);
        
        // Learning
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("Available AI Models", new Paragraph(
            "• ChemBERTa-2: BERT-based model for molecular properties\n" +
            "• MolBERT: Self-supervised learning on SMILES\n" +
            "• GROVER: Graph transformer for molecular representation\n" +
            "• Graphormer: Transformer architecture for graphs\n" +
            "• MolGPT: GPT-style model for molecular generation"
        ));
        learningAccordion.add("Deep Learning in Drug Discovery", new Paragraph(
            "Graph Neural Networks (GNNs) excel at learning from molecular graphs:\n\n" +
            "• Atoms = Nodes, Bonds = Edges\n" +
            "• Message passing aggregates neighborhood info\n" +
            "• Learn task-specific representations\n" +
            "• Outperform traditional fingerprints"
        ));
        
        configPanel.add(configSection, learningAccordion);
        
        // Results Panel
        VerticalLayout resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        H4 resultsHeader = new H4("📊 AI Prediction Results");
        resultsHeader.addClassName("section-title");
        
        // Prediction Cards
        HorizontalLayout predCards = new HorizontalLayout();
        predCards.setWidth("100%");
        predCards.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        
        predCards.add(
            createPredictionCard("Binding Affinity", "-9.2 kcal/mol", "High confidence"),
            createPredictionCard("Activity Class", "Active (pIC50=7.2)", "Threshold met"),
            createPredictionCard("Selectivity", "42x vs off-targets", "Good selectivity")
        );
        
        // Confidence Gauge Placeholder
        VerticalLayout gaugePanel = new VerticalLayout();
        gaugePanel.setWidth("100%");
        gaugePanel.setHeight("200px");
        gaugePanel.addClassName("viewer-panel");
        gaugePanel.add(new Text("📊 Confidence visualization would appear here"));
        
        // Detailed Predictions
        com.vaadin.flow.component.grid.Grid<String> detailGrid = new com.vaadin.flow.component.grid.Grid<>();
        detailGrid.setWidth("100%");
        detailGrid.setHeight("200px");
        detailGrid.addColumn(s -> s).setHeader("Property");
        detailGrid.addColumn(s -> s).setHeader("Prediction");
        detailGrid.addColumn(s -> s).setHeader("Confidence");
        detailGrid.setItems(Arrays.asList(
            "Solubility (LogS)\t-3.2\t±0.5\t85%",
            "BBB Permeability\tHigh\tN/A\t78%",
            "CYP3A4 Inhibition\tModerate\tN/A\t72%",
            "hERG Blockade\tLow\tN/A\t81%"
        ));
        
        resultsPanel.add(resultsHeader, predCards, gaugePanel, detailGrid);
        
        workspace.addToPrimary(configPanel);
        workspace.addToSecondary(resultsPanel);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, toolsGrid, workspace);
        mainContainer.setFlexGrow(1, workspace);
        
        add(mainContainer);
    }
    
    private Card createToolCard(String emoji, String title, String description, 
                               com.vaadin.flow.component.icon.Icon icon,
                               ComponentClickedListener listener) {
        Card card = new Card();
        card.addClassName("ai-tool-card");
        card.setClickListener(listener);
        
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.START);
        layout.addClassName("tool-card-content");
        
        H4 titleH = new H4(emoji + " " + title);
        Paragraph descP = new Paragraph(description);
        descP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        layout.add(titleH, descP);
        card.add(layout);
        
        return card;
    }
    
    private Card createPredictionCard(String title, String value, String note) {
        Card card = new Card();
        card.addClassName("prediction-card");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        Paragraph titleP = new Paragraph(title);
        titleP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        H4 valueH = new H4(value);
        Paragraph noteP = new Paragraph(note);
        noteP.addClassName(LumoUtility.TextColor.SUCCESS);
        
        layout.add(titleP, valueH, noteP);
        card.add(layout);
        
        return card;
    }
    
    private void openTool(String toolName) {
        Notification.show("Opening " + toolName + "...", 2000);
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
