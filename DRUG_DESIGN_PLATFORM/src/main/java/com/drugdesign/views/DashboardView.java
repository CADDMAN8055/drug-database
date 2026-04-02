package com.drugdesign.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.drugdesign.layout.MainLayout;

@PageTitle("Dashboard | DrugCompass")
public class DashboardView extends MainLayout {
    
    public DashboardView() {
        setSizeFull();
        addClassName("dashboard-view");
        
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setPadding(true);
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        
        // Welcome Section
        H2 welcome = new H2("🔬 Welcome to DrugCompass");
        welcome.addClassNames(LumoUtility.TextAlign.CENTER, LumoUtility.Margin.Top.XLARGE);
        
        Paragraph subtitle = new Paragraph("Your comprehensive platform for computational drug design and discovery");
        subtitle.addClassNames(LumoUtility.TextAlign.CENTER, LumoUtility.TextColor.SECONDARY);
        
        container.add(welcome, subtitle);
        
        // Quick Stats Cards
        HorizontalLayout statsRow = new HorizontalLayout();
        statsRow.setWidth("90%");
        statsRow.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        statsRow.addClassName("stats-row");
        
        statsRow.add(createStatCard("🧬", "10+ Tools", "Integrated docking & analysis"));
        statsRow.add(createStatCard("📊", "5+ Modules", "ADMET, QSAR, Dynamics"));
        statsRow.add(createStatCard("🤖", "AI Ready", "Machine learning powered"));
        statsRow.add(createStatCard("📚", "Learning Hub", "Tutorials & documentation"));
        
        container.add(statsRow);
        
        // Quick Access Cards
        Board board = new Board();
        board.setWidth("90%");
        
        Row row1 = new Row(
            createToolCard("Molecular Docking", "AutoDock Vina, Smina, CB-Dock", 
                          VaadinIcon.LAB, "/docking"),
            createToolCard("Molecular Dynamics", "GROMACS integration", 
                          VaadinIcon.LIGHTNING, "/dynamics"),
            createToolCard("QSAR Modeling", "Build ML models for drug activity", 
                          VaadinIcon.ROBOT, "/qsar")
        );
        
        Row row2 = new Row(
            createToolCard("ADMET Prediction", "Drug-like properties & safety", 
                          VaadinIcon.CAPSULES, "/admet"),
            createToolCard("Pharmacophore", "Feature-based drug design", 
                          VaadinIcon.MAP, "/pharmacophore"),
            createToolCard("Similarity Search", "Find similar compounds", 
                          VaadinIcon.SEARCH, "/similarity")
        );
        
        Row row3 = new Row(
            createToolCard("Free Energy Perturbation", "ΔG calculations", 
                          VaadinIcon.FLASK, "/fep"),
            createToolCard("DMPK Analysis", "Drug metabolism & pharmacokinetics", 
                          VaadinIcon.HEART, "/dmpk"),
            createToolCard("AI Tools", "Advanced AI-powered predictions", 
                          VaadinIcon.CLOUD, "/ai-tools")
        );
        
        board.add(row1, row2, row3);
        container.add(board);
        
        // Recent Features
        H4 recentHeader = new H4("📰 Platform Updates");
        recentHeader.addClassNames(LumoUtility.Padding.Left.LARGE, LumoUtility.Margin.Top.LARGE);
        
        Paragraph updates = new Paragraph(
            "• Version 1.0 launched with 10 computational modules\n" +
            "• Integrated CDK for molecular descriptors and fingerprints\n" +
            "• DeepLearning4j QSAR models now available\n" +
            "• Learning Center with step-by-step tutorials"
        );
        updates.addClassNames(LumoUtility.Padding.Horizontal.LARGE);
        
        container.add(recentHeader, updates);
        add(container);
    }
    
    private Card createStatCard(String emoji, String title, String desc) {
        Card card = new Card();
        card.addClassName("stat-card");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        
        Text emojiText = new Text(emoji);
        emojiText.addClassName("stat-emoji");
        
        H4 titleH = new H4(title);
        Paragraph descP = new Paragraph(desc);
        descP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        layout.add(emojiText, titleH, descP);
        card.add(layout);
        
        return card;
    }
    
    private Card createToolCard(String title, String desc, VaadinIcon icon, String link) {
        Card card = new Card();
        card.addClassName("tool-card");
        card.setClickListener(e -> getUI().ifPresent(ui -> ui.navigate(link)));
        
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.START);
        
        com.vaadin.flow.component.icon.Icon vaadinIcon = icon.create();
        vaadinIcon.addClassName("tool-icon");
        
        H4 titleH = new H4(title);
        Paragraph descP = new Paragraph(desc);
        descP.addClassName(LumoUtility.TextColor.SECONDARY);
        
        layout.add(vaadinIcon, titleH, descP);
        card.add(layout);
        
        return card;
    }
}
