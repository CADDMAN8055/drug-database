package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;
import com.drugdesign.services.SimilarityService;
import com.drugdesign.model.SimilarityResult;

import java.nio.file.*;
import java.util.*;

@PageTitle("Similarity Search | DrugCompass")
public class SimilarityView extends MainLayout {
    
    private final SimilarityService similarityService;
    
    private TextField querySmiles;
    private Upload databaseUpload;
    private ComboBox<String> fingerprintType;
    private ComboBox<String> similarityMetric;
    private NumberField threshold;
    private NumberField maxResults;
    private Grid<SimilarityResult> resultsGrid;
    private ProgressBar progressBar;
    private VerticalLayout heatmapPanel;
    
    public SimilarityView() {
        this.similarityService = new SimilarityService();
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("🔍 Similarity Search");
        Paragraph desc = new Paragraph("Find similar compounds using various fingerprint and similarity metrics");
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
        
        // Query Input
        Section querySection = new Section();
        H4 queryHeader = new H4("🧪 Query Compound");
        queryHeader.addClassName("section-title");
        
        querySmiles = new TextField("Query SMILES");
        querySmiles.setPlaceholder("Enter SMILES or upload SDF");
        querySmiles.setWidth("100%");
        
        Button searchBtn = new Button("🔍 Search", VaadinIcon.SEARCH.create());
        searchBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY, ButtonVariant.LUMBO_LARGE);
        searchBtn.setWidth("100%");
        searchBtn.addClickListener(e -> runSearch());
        
        Button loadSample = new Button("Load Sample", VaadinIcon.DOWNLOAD.create());
        loadSample.addClickListener(e -> {
            querySmiles.setValue("CC(=O)Oc1ccccc1C(=O)O"); // Aspirin
        });
        
        querySection.add(queryHeader, querySmiles, loadSample, searchBtn);
        
        // Parameters
        Section paramsSection = new Section();
        H4 paramsHeader = new H4("⚙️ Search Parameters");
        paramsHeader.addClassName("section-title");
        
        fingerprintType = new ComboBox<>("Fingerprint Type");
        fingerprintType.setItems("Morgan (ECFP4)", "Daylight", "MACCS Keys", "PubChem", "Extended");
        fingerprintType.setValue("Morgan (ECFP4)");
        fingerprintType.setWidth("100%");
        
        similarityMetric = new ComboBox<>("Similarity Metric");
        similarityMetric.setItems("Tanimoto", "Dice", "Cosine", "Tversky", "Euclidean");
        similarityMetric.setValue("Tanimoto");
        similarityMetric.setWidth("100%");
        
        threshold = new NumberField("Similarity Threshold");
        threshold.setValue(0.7);
        threshold.setMin(0);
        threshold.setMax(1);
        threshold.setStep(0.05);
        
        maxResults = new NumberField("Max Results");
        maxResults.setValue(50.0);
        maxResults.setMin(1);
        maxResults.setMax(1000);
        
        paramsSection.add(fingerprintType, similarityMetric, threshold, maxResults);
        
        // Results Preview
        Section previewSection = new Section();
        H4 previewHeader = new H4("🗺️ Similarity Heatmap");
        previewHeader.addClassName("section-title");
        
        heatmapPanel = new VerticalLayout();
        heatmapPanel.setWidth("100%");
        heatmapPanel.setHeight("200px");
        heatmapPanel.addClassName("viewer-panel");
        heatmapPanel.add(new Text("🔬 Run a search to generate similarity heatmap"));
        
        previewSection.add(heatmapPanel);
        
        // Learning
        Accordion learningAccordion = new Accordion();
        learningAccordion.add("Fingerprints Explained", new Paragraph(
            "• Morgan/ECFP4: Circular fingerprints, great for activity prediction\n" +
            "• Daylight: Path-based, traditional fingerprint\n" +
            "• MACCS: 166-bit structural keys\n" +
            "• PubChem: 881-bit structural features"
        ));
        learningAccordion.add("Tanimoto vs Dice", new Paragraph(
            "• Tanimoto: (A∩B)/(A∪B) - standard for chemical fingerprints\n" +
            "• Dice: (2×A∩B)/(A+B) - gives higher scores, more intuitive\n" +
            "• Cosine: normalized dot product\n" +
            "• Tversky: asymmetric similarity (good for substructure)"
        ));
        learningAccordion.add("Best Practices", new Paragraph(
            "• Tanimoto > 0.85 = likely similar activity\n" +
            "• Tanimoto > 0.7 = potential similarity\n" +
            "• Use ECFP4 for activity cliffs\n" +
            "• MACCS for structural similarity"
        ));
        
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        
        controlPanel.add(querySection, paramsSection, previewSection, progressBar, learningAccordion);
        
        // Results Panel
        VerticalLayout resultsPanel = new VerticalLayout();
        resultsPanel.setWidth("100%");
        resultsPanel.setPadding(true);
        resultsPanel.addClassName("results-panel");
        
        H4 resultsHeader = new H4("📊 Search Results");
        resultsHeader.addClassName("section-title");
        
        resultsGrid = new Grid<>();
        resultsGrid.setWidth("100%");
        resultsGrid.setHeight("500px");
        resultsGrid.addColumn(SimilarityResult::getCompound2).setHeader("Compound");
        resultsGrid.addColumn(r -> String.format("%.3f", r.getTanimoto())).setHeader("Tanimoto");
        resultsGrid.addColumn(r -> String.format("%.3f", r.getDice())).setHeader("Dice");
        resultsGrid.addColumn(r -> String.format("%.3f", r.getCosine())).setHeader("Cosine");
        resultsGrid.addColumn(SimilarityResult::getFingerprint).setHeader("Fingerprint");
        
        resultsPanel.add(resultsHeader, resultsGrid);
        
        splitLayout.addToPrimary(controlPanel);
        splitLayout.addToSecondary(resultsPanel);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, splitLayout);
        mainContainer.setFlexGrow(1, splitLayout);
        
        add(mainContainer);
    }
    
    private void runSearch() {
        String smiles = querySmiles.getValue();
        if (smiles == null || smiles.isEmpty()) {
            Notification.show("Please enter a query SMILES", 5000).addThemeVariants(NotificationVariant.LUMBO_WARNING);
            return;
        }
        
        progressBar.setVisible(true);
        searchBtn.setEnabled(false);
        
        UI.getCurrent().access(() -> {
            try {
                SimilarityResult[] results = similarityService.searchSimilarity(
                    smiles,
                    fingerprintType.getValue(),
                    similarityMetric.getValue(),
                    threshold.getValue().doubleValue(),
                    maxResults.getValue().intValue()
                );
                
                UI.getCurrent().access(() -> {
                    resultsGrid.setItems(Arrays.asList(results));
                    progressBar.setVisible(false);
                    searchBtn.setEnabled(true);
                    heatmapPanel.removeAll();
                    heatmapPanel.add(new Text("✅ Found " + results.length + " similar compounds"));
                    Notification.show("Search complete!", 3000).addThemeVariants(NotificationVariant.LUMBO_SUCCESS);
                });
            } catch (Exception ex) {
                UI.getCurrent().access(() -> {
                    Notification.show("Search failed: " + ex.getMessage(), 5000).addThemeVariants(NotificationVariant.LUMBO_ERROR);
                    progressBar.setVisible(false);
                    searchBtn.setEnabled(true);
                });
            }
        });
    }
    
    private Button searchBtn; // For access in lambda
    
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
