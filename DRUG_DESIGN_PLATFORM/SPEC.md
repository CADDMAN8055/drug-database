# Computational Drug Design Platform - SPEC.md

## Project Overview
**Name:** DrugCompass - Computational Drug Discovery Platform  
**Type:** Java Vaadin Web Application (Browser-based)  
**Goal:** Frontend sharpening platform for computational drug design skills with professional scientific UI  
**Target Users:** Computational chemists, medicinal chemists, drug discovery researchers

## Tech Stack
- **Frontend:** Vaadin 24 (Pure Java web framework)
- **Backend:** Spring Boot 3
- **Build:** Maven
- **Charts:** Vaadin Charts / Plotly.js
- **3D Viewer:** NGL.js embedded via Vaadin
- **Styling:** Professional scientific theme (dark + accent colors)

## UI Design
- **Theme:** Professional scientific dark theme
- **Primary:** Deep blue (#1a237e)
- **Secondary:** Teal accent (#00bcd4)
- **Background:** Dark gray (#121212)
- **Cards:** Elevated dark panels with subtle borders
- **Font:** Inter / Roboto (clean, scientific)

## Modules & Features

### 1. Molecular Docking Module
Tools:
- AutoDock Vina (via subprocess)
- Smina (via subprocess)
- SwissDock (web API)
- CB-Dock (web API)
- PyRx (Python integration)

Features:
- Upload ligand (SDF, MOL, PDBQT)
- Upload receptor (PDB, PDBQT)
- Grid box configuration
- Exhaustiveness settings
- Result visualization (2D/3D)
- Binding affinity comparison
- Docking protocol documentation

### 2. Molecular Dynamics Module
Tools:
- GROMACS (via subprocess)
- Desmond (Schrödinger - placeholder)
- AMBER (placeholder)

Features:
- System preparation
- MD parameters configuration
- Trajectory analysis
- RMSD/RMSF plots
- Energy analysis
- Movie generation

### 3. QSAR/ML Models Module
Tools:
- DeepLearning4j integration
- WEKA models
- scikit-learn via Python桥接

Features:
- Dataset upload (CSV)
- Feature selection
- Model training (classification/regression)
- Cross-validation
- Performance metrics (ROC, RMSE, R²)
- Model prediction

### 4. ADMET Prediction Module
Tools:
- CDK descriptors
- SwissADME (web API)
- pkCSM (web API)
- Built-in prediction models

Features:
- Lipinski Rule of 5
- BBB permeability prediction
- Solubility prediction
- CYP inhibition
- hERG channel blockade
- DILI (Drug-induced liver injury)

### 5. Pharmacophore Modeling Module
Tools:
- Phase (Schrödinger - placeholder)
- LigandScout (placeholder)
- Built-in pharmacophore detection

Features:
- Feature detection from active compounds
- Pharmacophore generation
- Pharmacophore-based search
- 3D pharmacophore display

### 6. Similarity Search Module
Tools:
- CDK fingerprints
- RDKit via Python bridge

Features:
- Tanimoto/Dice/Distance similarity
- Morgan fingerprints
- MACCS keys
- PubChem fingerprints
- Similarity heatmap
- Cluster analysis

### 7. Free Energy Perturbation (FEP) Module
Tools:
- GROMACS alchemical transformations
- Desmond FEP (placeholder)
- Schrödinger FEP (placeholder)

Features:
- Ligand parameterization
- Alchemical pathway design
- ΔG calculation
- Convergence analysis

### 8. DMPK (Drug Metabolism & Pharmacokinetics) Module
Features:
- Absorption prediction
- Distribution (VD, PPB)
- Metabolism (CYP isoforms)
- Excretion (CL, t½)
- Drug-drug interactions
- PK parameter calculator

### 9. AI-Powered Tools Module
Features:
- MolBERT / ChemBERTa predictions
- Molecular generation (VAE, GAN placeholders)
- Retrosynthesis prediction (web API)
- Activity cliff analysis
- Drug-target interaction prediction

### 10. Learning Center
Content:
- Tool documentation (tooltips on each feature)
- Video tutorials (embedded links)
- Example workflows
- Sample datasets
- Best practices guides
- Quick-start guides per module

## Pages Structure
1. **Dashboard** - Overview, recent activity, quick links
2. **Molecular Docking** - Docking workspace
3. **Molecular Dynamics** - MD workspace
4. **QSAR & ML** - Model building
5. **ADMET** - Property prediction
6. **Pharmacophore** - Pharmacophore workspace
7. **Similarity Search** - Search interface
8. **FEP** - Free energy calculations
9. **DMPK** - Pharmacokinetics
10. **AI Tools** - Advanced AI features
11. **Learning Center** - Tutorials & docs
12. **Settings** - Tool paths, preferences

## External Tool Integration
- AutoDock Vina: `vina --receptor --ligand --center --size --exhaustiveness`
- GROMACS: `gmx mdrun`, `gmx rms`, `gmx energy`
- Python scripts for RDKit/scikit-learn
- Web APIs for SwissDock, SwissADME, pkCSM

## Development Phases
1. **Phase 1:** Core UI + Dashboard + Docking Module
2. **Phase 2:** MD + QSAR modules
3. **Phase 3:** ADMET + Pharmacophore + Similarity
4. **Phase 4:** FEP + DMPK + AI Tools
5. **Phase 5:** Learning Center + Polish
