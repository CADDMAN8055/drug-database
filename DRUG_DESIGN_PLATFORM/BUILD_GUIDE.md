# DrugCompass - Quick Build Guide

## Build Commands

```bash
cd /path/to/drugcompass

# Clean and build
mvn clean package

# Skip tests for faster build
mvn clean package -DskipTests

# Run directly
mvn spring-boot:run
```

## Access

After building successfully:
- Open browser to: http://localhost:8080
- Default theme: Dark Scientific

## First Run Checklist

1. ☑️ Install Java 17+
2. ☑️ Install Maven 3.8+
3. ☐ Configure external tools (optional):
   - AutoDock Vina: `Settings → Tool Paths`
   - GROMACS: `Settings → Tool Paths`
4. ☐ Load sample datasets from Learning Center
5. ☐ Start exploring modules!

## Troubleshooting

**Port 8080 already in use:**
```bash
# Find and kill the process
lsof -i :8080
kill -9 <PID>
```

**CDK errors:**
```bash
# Ensure all dependencies downloaded
mvn dependency:resolve
```

**Memory issues:**
```bash
# Increase heap size
export MAVEN_OPTS="-Xmx4g"
mvn clean package
```

## Module Access URLs

- Dashboard: http://localhost:8080/
- Docking: http://localhost:8080/docking
- Dynamics: http://localhost:8080/dynamics
- QSAR: http://localhost:8080/qsar
- ADMET: http://localhost:8080/admet
- Pharmacophore: http://localhost:8080/pharmacophore
- Similarity: http://localhost:8080/similarity
- FEP: http://localhost:8080/fep
- DMPK: http://localhost:8080/dmpk
- AI Tools: http://localhost:8080/ai-tools
- Learning: http://localhost:8080/learning
- Settings: http://localhost:8080/settings
