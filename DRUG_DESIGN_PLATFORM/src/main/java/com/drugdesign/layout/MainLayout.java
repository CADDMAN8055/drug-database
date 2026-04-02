package com.drugdesign.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.nav.NavItem;
import com.vaadin.flow.component.nav.Navbar;
import com.vaadin.flow.component.nav.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java渡边彻郎.*;

public class MainLayout extends AppLayout {
    public MainLayout() {
        // Header with title
        H1 title = new H1("🔬 DrugCompass");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
        
        DrawerToggle toggle = new DrawerToggle();
        
        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.Background.BASE);
        
        addToNavbar(header);
        
        // Navigation items
        SideNav nav = new SideNav();
        
        nav.addItem(new SideNavItem("📊 Dashboard", VaadinIcon.DASHBOARD, "/"));
        nav.addItem(new SideNavItem("🧬 Molecular Docking", VaadinIcon.LAB, "/docking"));
        nav.addItem(new SideNavItem("⚡ Molecular Dynamics", VaadinIcon.LIGHTNING, "/dynamics"));
        nav.addItem(new SideNavItem("🤖 QSAR & ML", VaadinIcon.ROBOT, "/qsar"));
        nav.addItem(new SideNavItem("💊 ADMET Prediction", VaadinIcon.CAPSULES, "/admet"));
        nav.addItem(new SideNavItem("🧭 Pharmacophore", VaadinIcon.MAP, "/pharmacophore"));
        nav.addItem(new SideNavItem("🔍 Similarity Search", VaadinIcon.SEARCH, "/similarity"));
        nav.addItem(new SideNavItem("⚗️ FEP", VaadinIcon.FLASK, "/fep"));
        nav.addItem(new SideNavItem("💉 DMPK", VaadinIcon.HEART, "/dmpk"));
        nav.addItem(new SideNavItem("🚀 AI Tools", VaadinIcon.CLOUD, "/ai-tools"));
        nav.addItem(new SideNavItem("📚 Learning Center", VaadinIcon.BOOK, "/learning"));
        nav.addItem(new SideNavItem("⚙️ Settings", VaadinIcon.COG, "/settings"));
        
        addToDrawer(nav);
    }
}
