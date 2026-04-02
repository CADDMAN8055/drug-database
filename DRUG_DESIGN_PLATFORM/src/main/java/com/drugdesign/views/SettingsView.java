package com.drugdesign.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.drugdesign.layout.MainLayout;

@PageTitle("Settings | DrugCompass")
public class SettingsView extends MainLayout {
    
    public SettingsView() {
        setSizeFull();
        buildUI();
    }
    
    private void buildUI() {
        H2 title = new H2("⚙️ Settings");
        Paragraph desc = new Paragraph("Configure your computational drug design environment");
        desc.addClassName("header-description");
        
        Header header = new Header();
        header.add(title, desc);
        header.addClassName("module-header");
        
        // Settings Tabs
        Tabs settingsTabs = new Tabs(
            new Tab("🔧 Tool Paths"),
            new Tab("🌐 APIs"),
            new Tab("🎨 Appearance"),
            new Tab("📂 Defaults"),
            new Tab("ℹ️ About")
        );
        settingsTabs.addClassName("module-tabs");
        
        VerticalLayout contentArea = new VerticalLayout();
        contentArea.setWidth("100%");
        contentArea.setPadding(true);
        
        // Tool Paths Tab
        VerticalLayout toolPathsTab = createToolPathsTab();
        
        // APIs Tab
        VerticalLayout apisTab = createAPIsTab();
        
        // Appearance Tab
        VerticalLayout appearanceTab = createAppearanceTab();
        
        // Defaults Tab
        VerticalLayout defaultsTab = createDefaultsTab();
        
        // About Tab
        VerticalLayout aboutTab = createAboutTab();
        
        // Tab map
        java.util.Map<Tab, VerticalLayout> tabMap = new java.util.HashMap<>();
        tabMap.put(settingsTabs.getTabAt(0), toolPathsTab);
        tabMap.put(settingsTabs.getTabAt(1), apisTab);
        tabMap.put(settingsTabs.getTabAt(2), appearanceTab);
        tabMap.put(settingsTabs.getTabAt(3), defaultsTab);
        tabMap.put(settingsTabs.getTabAt(4), aboutTab);
        
        settingsTabs.addSelectedChangeListener(e -> {
            Tab selected = e.getSelectedTab();
            contentArea.removeAll();
            if (selected != null) {
                contentArea.add(tabMap.get(selected));
            }
        });
        
        contentArea.add(toolPathsTab);
        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("100%");
        mainContainer.setPadding(false);
        mainContainer.add(header, settingsTabs, contentArea);
        
        add(mainContainer);
    }
    
    private VerticalLayout createToolPathsTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        H3 header = new H3("🔧 External Tool Paths");
        
        TextField vinaPath = new TextField("AutoDock Vina");
        vinaPath.setValue("/usr/bin/vina");
        vinaPath.setWidth("100%");
        vinaPath.setPlaceholder("/path/to/vina");
        
        TextField sminaPath = new TextField("Smina");
        sminaPath.setPlaceholder("/path/to/smina");
        sminaPath.setWidth("100%");
        
        TextField gmxPath = new TextField("GROMACS");
        gmxPath.setValue("/usr/bin/gmx");
        gmxPath.setWidth("100%");
        
        TextField pythonPath = new TextField("Python");
        pythonPath.setValue("python3");
        pythonPath.setWidth("100%");
        
        TextField rdkitPath = new TextField("RDKit Python");
        rdkitPath.setPlaceholder("python -c 'from rdkit import...'");
        rdkitPath.setWidth("100%");
        
        TextField schrodingerPath = new TextField("Schrödinger (optional)");
        schrodingerPath.setPlaceholder("/opt/schrodinger2023-1");
        schrodingerPath.setWidth("100%");
        
        Button testAllBtn = new Button("🔍 Test All Paths", VaadinIcon.SEARCH.create());
        testAllBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        testAllBtn.addClickListener(e -> {
            Notification.show("Testing tool availability...", 3000);
        });
        
        Button saveBtn = new Button("💾 Save Settings", VaadinIcon.DOWNLOAD.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS);
        saveBtn.addClickListener(e -> {
            Notification.show("Settings saved!", 3000).addThemeVariants(NotificationVariant.LUMBO_SUCCESS);
        });
        
        Section section = new Section();
        section.add(vinaPath, sminaPath, gmxPath, pythonPath, rdkitPath, schrodingerPath, testAllBtn, saveBtn);
        
        tab.add(header, section);
        return tab;
    }
    
    private VerticalLayout createAPIsTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        H3 header = new H3("🌐 External API Configuration");
        
        TextField swissadmeKey = new TextField("SwissADME API Key");
        swissadmeKey.setPlaceholder("Optional - for extended features");
        swissadmeKey.setWidth("100%");
        
        TextField pubchemKey = new TextField("PubChem PUG REST API Key");
        pubchemKey.setPlaceholder("Optional - for higher rate limits");
        pubchemKey.setWidth("100%");
        
        Checkbox useCache = new Checkbox("Cache API results locally");
        useCache.setValue(true);
        
        Checkbox offlineMode = new Checkbox("Offline mode (disable external APIs)");
        
        Button testApiBtn = new Button("🧪 Test API Connection", VaadinIcon.CLOUD.create());
        testApiBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        
        Button saveBtn = new Button("💾 Save", VaadinIcon.DOWNLOAD.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS);
        
        Section section = new Section();
        section.add(swissadmeKey, pubchemKey, useCache, offlineMode, testApiBtn, saveBtn);
        
        tab.add(header, section);
        return tab;
    }
    
    private VerticalLayout createAppearanceTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        H3 header = new H3("🎨 Theme & Display");
        
        ComboBox<String> theme = new ComboBox<>("Color Theme");
        theme.setItems("Dark (Scientific)", "Light", "High Contrast", "Custom");
        theme.setValue("Dark (Scientific)");
        theme.setWidth("100%");
        
        ComboBox<String> accentColor = new ComboBox<>("Accent Color");
        accentColor.setItems("Teal", "Blue", "Purple", "Green", "Orange");
        accentColor.setValue("Teal");
        accentColor.setWidth("100%");
        
        Checkbox animations = new Checkbox("Enable animations");
        animations.setValue(true);
        
        Checkbox compactMode = new Checkbox("Compact layout");
        
        NumberField fontSize = new NumberField("Font Size");
        fontSize.setValue(14.0);
        fontSize.setMin(10);
        fontSize.setMax(24);
        
        Button previewBtn = new Button("👁️ Preview", VaadinIcon.EYE.create());
        previewBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        
        Button saveBtn = new Button("💾 Save", VaadinIcon.DOWNLOAD.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS);
        
        Section section = new Section();
        section.add(theme, accentColor, animations, compactMode, fontSize, previewBtn, saveBtn);
        
        tab.add(header, section);
        return tab;
    }
    
    private VerticalLayout createDefaultsTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        H3 header = new H3("📂 Default Settings");
        
        TextField defaultOutput = new TextField("Output Directory");
        defaultOutput.setValue("/home/user/drugdesign/output");
        defaultOutput.setWidth("100%");
        
        ComboBox<String> defaultFingerprint = new ComboBox<>("Default Fingerprint");
        defaultFingerprint.setItems("Morgan (ECFP4)", "Daylight", "MACCS Keys", "PubChem");
        defaultFingerprint.setValue("Morgan (ECFP4)");
        defaultFingerprint.setWidth("100%");
        
        NumberField defaultExhaustiveness = new NumberField("Docking Exhaustiveness");
        defaultExhaustiveness.setValue(32.0);
        defaultExhaustiveness.setMin(1);
        defaultExhaustiveness.setMax(128);
        
        ComboBox<String> defaultForceField = new ComboBox<>("GROMACS Force Field");
        defaultForceField.setItems("AMBER99SB-ILDN", "CHARMM36", "OPLS-AA", "GROMOS96");
        defaultForceField.setValue("AMBER99SB-ILDN");
        defaultForceField.setWidth("100%");
        
        Checkbox autoSave = new Checkbox("Auto-save results");
        autoSave.setValue(true);
        
        Button saveBtn = new Button("💾 Save", VaadinIcon.DOWNLOAD.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMBO_SUCCESS);
        
        Section section = new Section();
        section.add(defaultOutput, defaultFingerprint, defaultExhaustiveness, 
                   defaultForceField, autoSave, saveBtn);
        
        tab.add(header, section);
        return tab;
    }
    
    private VerticalLayout createAboutTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidth("100%");
        tab.setPadding(true);
        
        H3 header = new H3("ℹ️ About DrugCompass");
        
        Card versionCard = new Card();
        versionCard.addClassName("about-card");
        
        VerticalLayout versionLayout = new VerticalLayout();
        versionLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        H4 versionTitle = new H4("DrugCompass");
        Paragraph version = new Paragraph("Version 1.0.0");
        Paragraph tagline = new Paragraph("Computational Drug Design Platform");
        
        versionLayout.add(versionTitle, version, tagline);
        versionCard.add(versionLayout);
        
        Section techSection = new Section();
        H4 techHeader = new H4("🛠️ Technologies");
        techHeader.addClassName("section-title");
        
        Paragraph techList = new Paragraph(
            "• Vaadin 24 - Web Framework\n" +
            "• Spring Boot 3 - Backend\n" +
            "• CDK 2.9 - Chemoinformatics\n" +
            "• DeepLearning4j - Neural Networks\n" +
            "• GROMACS - Molecular Dynamics\n" +
            "• AutoDock Vina - Molecular Docking"
        );
        
        techSection.add(techHeader, techList);
        
        Section creditSection = new Section();
        H4 creditHeader = new H4("🙏 Acknowledgments");
        creditHeader.addClassName("section-title");
        
        Paragraph credits = new Paragraph(
            "This platform integrates world-class open-source tools:\n" +
            "• The CDK Project\n" +
            "• AutoDock Vina\n" +
            "• GROMACS\n" +
            "• RDKit\n" +
            "• DeepLearning4j\n" +
            "• Vaadin Components"
        );
        
        creditSection.add(creditHeader, credits);
        
        Button checkUpdateBtn = new Button("🔄 Check for Updates", VaadinIcon.REFRESH.create());
        checkUpdateBtn.addThemeVariants(ButtonVariant.LUMBO_PRIMARY);
        
        tab.add(versionCard, techSection, creditSection, checkUpdateBtn);
        return tab;
    }
    
    private static class Section extends VerticalLayout {
        public Section() { addClassName("config-section"); }
    }
}
