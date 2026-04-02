# DrugCompass 🔬

## Computational Drug Design Platform

A comprehensive Java-based web application for computational drug discovery and design, featuring professional scientific UI built with Vaadin.

### 🌟 Features

#### 🔬 Molecular Docking
- **AutoDock Vina** - Virtual screening and binding pose prediction
- **Smina** - Fast, open-source docking
- **CB-Dock** - Automated cavity detection docking
- **SwissDock** - Web-based blind docking
- **PyRx** - User-friendly docking interface

#### ⚡ Molecular Dynamics
- **GROMACS** integration for MD simulations
- System preparation (pdb2gmx, solvate, ions)
- NVT/NPT equilibration
- Production MD with trajectory analysis
- RMSD, RMSF, Radius of Gyration analysis

#### 🤖 QSAR & Machine Learning
- Dataset upload (CSV format)
- Feature selection (Morgan FP, MACCS, 2D descriptors)
- Multiple algorithms: Random Forest, XGBoost, Neural Networks
- Cross-validation (5-Fold, 10-Fold, LOO)
- Performance metrics (AUC-ROC, R², RMSE)

#### 💊 ADMET Prediction
- Lipinski Rule of 5 evaluation
- BBB permeability prediction
- Aqueous solubility estimation
- CYP metabolism profiling
- hERG blockade risk
- Bioavailability prediction

#### 🧭 Pharmacophore Modeling
- Feature detection from active compounds
- Pharmacophore generation and visualization
- 3D pharmacophore display
- Database searching

#### 🔍 Similarity Search
- Multiple fingerprint types (Morgan, Daylight, MACCS, PubChem)
- Various similarity metrics (Tanimoto, Dice, Cosine)
- Similarity heatmaps
- Cluster analysis

#### ⚗️ Free Energy Perturbation (FEP)
- Ligand parameterization
- Alchemical pathway design
- TI, BAR, MBAR analysis methods
- ΔΔG calculations

#### 💉 DMPK Analysis
- Absorption parameters
- Distribution (Vd, PPB)
- Metabolism (CYP enzymes)
- Excretion (CL, t½)
- PK parameter calculator

#### 🚀 AI-Powered Tools
- Molecular generation (VAE, GAN placeholders)
- Activity prediction with ChemBERTa, MolBERT
- Retrosynthesis planning
- Drug-target interaction prediction
- Active learning for screening

#### 📚 Learning Center
- Step-by-step tutorials
- Tool documentation
- Video tutorials
- Sample datasets
- Installation guides

### 🛠️ Technology Stack

| Layer | Technology |
|-------|------------|
| Frontend | Vaadin 24 (Pure Java) |
| Backend | Spring Boot 3.2 |
| Build | Maven |
| Chemistry | CDK 2.9 |
| ML | DeepLearning4j 1.0.0-beta7 |
| External Tools | GROMACS, AutoDock Vina, Smina |

### 📋 Requirements

- **Java 17+**
- **Maven 3.8+**
- **GROMACS** (for Molecular Dynamics)
- **AutoDock Vina** (for Docking)
- **Python 3.9+** with RDKit (optional, for Python-based tools)

### 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/yourusername/drugcompass.git
cd drugcompass

# Build with Maven
mvn clean package -DskipTests

# Run the application
java -jar target/drugcompass-1.0.0.jar

# Or run with Maven
mvn spring-boot:run
```

Access the application at: **http://localhost:8080**

### 🔧 Configuration

External tool paths can be configured in:
- **Settings → Tool Paths** (in-app)
- `application.properties` file
- Environment variables

### 📁 Project Structure

```
drugcompass/
├── src/main/java/com/drugdesign/
│   ├── DrugCompassApplication.java     # Main application
│   ├── layout/
│   │   └── MainLayout.java              # Main navigation layout
│   ├── views/
│   │   ├── DashboardView.java           # Home dashboard
│   │   ├── DockingView.java             # Molecular docking
│   │   ├── DynamicsView.java            # Molecular dynamics
│   │   ├── QSARView.java                # QSAR modeling
│   │   ├── ADMETView.java               # ADMET prediction
│   │   ├── PharmacophoreView.java       # Pharmacophore
│   │   ├── SimilarityView.java          # Similarity search
│   │   ├── FEPView.java                 # Free energy
│   │   ├── DMPKView.java                # DMPK analysis
│   │   ├── AIToolsView.java             # AI tools
│   │   ├── LearningView.java            # Learning center
│   │   └── SettingsView.java            # Settings
│   ├── model/
│   │   ├── DockingResult.java
│   │   ├── MolecularDescriptor.java
│   │   └── SimilarityResult.java
│   └── services/
│       ├── DockingService.java
│       ├── MolecularDynamicsService.java
│       ├── ChemistryService.java
│       └── SimilarityService.java
├── src/main/resources/
│   ├── application.properties
│   ├── styles.css
│   └── templates/
├── pom.xml
└── README.md
```

### 🎨 UI Theme

Professional scientific dark theme featuring:
- Deep blue primary color (#1a237e)
- Teal accent color (#00bcd4)
- Dark background (#121212)
- Elevated card design
- Smooth animations

### 📊 Modules Overview

| Module | Tools | Features |
|--------|-------|----------|
| Docking | Vina, Smina, CB-Dock | Grid optimization, exhaustiveness |
| Dynamics | GROMACS | RMSD, RMSF, H-bonds |
| QSAR | DeepLearning4j, sklearn | Classification, regression |
| ADMET | CDK, SwissADME | Lipinski, BBB, solubility |
| Pharmacophore | Phase, LigandScout | Feature detection |
| Similarity | CDK fingerprints | Tanimoto, Dice, clustering |
| FEP | GROMACS TI | ΔG calculations |
| DMPK | pkCSM, ADMET | Full PK profiling |
| AI | ChemBERTa, MolBERT | Generation, prediction |

### 📚 Documentation

Detailed documentation is available in the Learning Center:
- Tool installation guides
- Tutorial workflows
- Best practices
- Example datasets
- API references

### 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

### 📄 License

This project is licensed under the MIT License.

### 🙏 Acknowledgments

Special thanks to:
- **The CDK Project** - Chemistry Development Kit
- **AutoDock Vina** - Molecular docking
- **GROMACS** - Molecular dynamics
- **DeepLearning4j** - Deep learning for Java
- **Vaadin** - Web framework

### 📧 Contact

For questions and support, please open an issue on GitHub.

---

**Built with ❤️ for the computational drug discovery community**

🔬🧬⚗️💊🚀
