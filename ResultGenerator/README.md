# Multi-threaded Result Generator — Web Application

This project is a multi-threaded student result generator with a web front-end. It processes student marks in parallel using Java's ExecutorService and can optionally persist results to MySQL.

## Features

- **Multi-threaded Processing**: Uses thread pools to compute student results in parallel
- **Web Interface**: Simple web UI to submit CSV data and view results
- **CSV Import/Export**: Import student data from CSV and export results
- **Optional MySQL Persistence**: Save results to MySQL database
- **RESTful API**: API endpoints for preview and export
- **System Information**: Endpoints to check system resources and thread counts

## How to Build

1. Install Maven and Java 11+.
2. From the project root run:

```bash
mvn -DskipTests package
```

This produces a jar-with-dependencies in the `target/` folder.

## How to Run Locally

Run the web server (bundled jar):

```powershell
java -jar target/result-generator-1.0.0-jar-with-dependencies.jar
```

Or on Linux/Mac:

```bash
java -jar target/result-generator-1.0.0-jar-with-dependencies.jar
```

Open http://localhost:8080/ and paste CSV data. Optionally provide a JDBC URL and credentials to persist results to MySQL.

## CSV Format

The application expects CSV data with the following format:

```
ID,Name,Math,Science,English,History,Geography
S1,John Doe,85,78,92,66,81
S2,Jane Smith,90,88,84,91,77
```

## Database Schema

The app will create four tables if they do not exist: `students`, `subjects`, `marks`, `results`.

## Deploying to Render

### Option 1: Manual Deployment (Recommended - Works Immediately)

**Use this method if you're getting "render.yaml not found" error or haven't committed the file yet.**

1. **Push your code to GitHub**:
   ```bash
   git add .
   git commit -m "Configure for Render"
   git push
   ```

2. **Create a Web Service on Render**:
   - Go to [Render Dashboard](https://dashboard.render.com)
   - Click "New +" and select "Web Service"
   - Connect your GitHub repository
   - Configure the service:
     - **Name**: `result-generator` (or any name you prefer)
     - **Runtime**: `Docker`
     - **Dockerfile Path**: `./Dockerfile`
     - **Plan**: Free (or paid if you need more resources)
     - **Health Check Path**: `/`

3. **Environment Variables** (optional):
   - `JAVA_OPTS`: `-Xmx512m` (optional, to limit memory)
   - Note: `PORT` is automatically set by Render

4. **Deploy**:
   - Click "Create Web Service"
   - Render will build and deploy your application
   - Wait for the build to complete (usually 5-10 minutes for the first build)
   - Access your app at `https://your-app-name.onrender.com`

### Option 2: Using render.yaml (Requires file to be committed)

**Note**: This method requires `render.yaml` to be committed and pushed to your repository.

1. **Commit and push render.yaml**:
   ```bash
   git add render.yaml
   git commit -m "Add Render configuration"
   git push
   ```

2. **Connect to Render**:
   - Go to [Render Dashboard](https://dashboard.render.com)
   - Click "New +" and select "Blueprint"
   - Connect your repository
   - Render will automatically detect the `render.yaml` file

3. **Deploy**:
   - Render will automatically build and deploy your application
   - The service will be available at `https://your-app-name.onrender.com`

### Render Configuration

The application is configured to:
- Read the `PORT` environment variable (automatically set by Render)
- Bind to `0.0.0.0` to accept connections from Render's load balancer
- Use Docker for consistent builds across environments

### Notes

- **Free Tier Limitations**: Render's free tier spins down services after 15 minutes of inactivity. The first request after spin-down may take longer.
- **Build Time**: First build may take 5-10 minutes as it downloads dependencies
- **Health Checks**: The app responds to GET requests on `/` for health checks
- **Database**: If you need MySQL, you can create a MySQL database service on Render and configure the connection string

## API Endpoints

- `GET /` - Web interface
- `POST /submit` - Submit CSV data and process results
- `POST /api/preview` - Preview results without saving
- `POST /api/export` - Export results (requires database)
- `GET /system-info` - Get system information (JSON)
- `GET /thread-count` - Get active thread count (JSON)

## CLI Mode

The original CLI version is available as `ResultGenerator.java`. Run it with:

```bash
javac ResultGenerator.java
java ResultGenerator --input students.csv --output results.csv --threads 4
```

Or run in interactive mode:

```bash
java ResultGenerator
```
