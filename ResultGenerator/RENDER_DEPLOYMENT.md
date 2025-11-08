# Quick Guide: Deploying to Render

## Prerequisites

1. A GitHub account
2. A Render account (sign up at https://render.com - free tier available)
3. Your code pushed to a GitHub repository

## Step-by-Step Deployment

### 1. Push Code to GitHub

```bash
git init
git add .
git commit -m "Initial commit: Multi-threaded result generator"
git branch -M main
git remote add origin https://github.com/yourusername/result-generator.git
git push -u origin main
```

### 2. Deploy to Render (Using render.yaml - Easiest)

1. **Sign in to Render**: Go to https://dashboard.render.com

2. **Create Blueprint**:
   - Click "New +" button
   - Select "Blueprint"
   - Connect your GitHub account (if not already connected)
   - Select your repository: `result-generator`
   - Click "Apply"

3. **Render will automatically**:
   - Detect the `render.yaml` file
   - Create the web service
   - Build the Docker image
   - Deploy your application

4. **Wait for deployment**:
   - First build takes 5-10 minutes
   - You'll see build logs in real-time
   - Once deployed, you'll get a URL like: `https://result-generator.onrender.com`

### 3. Deploy to Render (Manual Method)

1. **Sign in to Render**: Go to https://dashboard.render.com

2. **Create Web Service**:
   - Click "New +" button
   - Select "Web Service"
   - Connect your GitHub account (if not already connected)
   - Select your repository: `result-generator`

3. **Configure Service**:
   - **Name**: `result-generator` (or your preferred name)
   - **Region**: Choose closest to your users
   - **Branch**: `main` (or your default branch)
   - **Runtime**: `Docker`
   - **Dockerfile Path**: `./Dockerfile`
   - **Docker Context**: `.` (root directory)
   - **Plan**: `Free` (or choose a paid plan)
   - **Health Check Path**: `/`

4. **Environment Variables** (Optional):
   - `JAVA_OPTS`: `-Xmx512m` (optional, for memory management)
   - Note: `PORT` is automatically set by Render, don't override it

5. **Create Service**:
   - Click "Create Web Service"
   - Wait for build and deployment (5-10 minutes first time)

### 4. Verify Deployment

1. **Check Health**:
   - Visit your service URL: `https://your-app-name.onrender.com`
   - You should see the web interface

2. **Test the Application**:
   - Paste CSV data in the format:
     ```
     ID,Name,Math,Science,English
     S1,John Doe,85,78,92
     S2,Jane Smith,90,88,84
     ```
   - Click "Submit" to process results

3. **Check Logs**:
   - Go to Render Dashboard
   - Click on your service
   - View "Logs" tab to see application logs

## Troubleshooting

### Build Fails

- **Check Dockerfile**: Ensure it's in the root directory
- **Check Maven build**: Look at build logs for Maven errors
- **Check Java version**: Application uses Java 17

### Application Won't Start

- **Check PORT**: Application should read `PORT` from environment (already configured)
- **Check logs**: View logs in Render dashboard for errors
- **Check health check**: Ensure `/` endpoint returns 200

### Slow First Request

- **Free tier limitation**: Render free tier spins down after 15 minutes of inactivity
- **Solution**: First request after spin-down takes 30-60 seconds to wake up
- **Upgrade**: Consider paid plan for always-on service

### Memory Issues

- **Set JAVA_OPTS**: Add environment variable `JAVA_OPTS` with value `-Xmx512m`
- **Upgrade plan**: Free tier has limited memory, consider paid plan for larger datasets

## Environment Variables Reference

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `PORT` | Yes | 8080 | Port number (automatically set by Render) |
| `JAVA_OPTS` | No | - | Java runtime options (e.g., `-Xmx512m`) |

## Custom Domain (Optional)

1. Go to your service in Render dashboard
2. Click "Settings"
3. Scroll to "Custom Domains"
4. Add your domain and follow DNS configuration instructions

## Monitoring

- **Logs**: Available in Render dashboard under your service
- **Metrics**: View CPU, memory, and request metrics in dashboard
- **Alerts**: Set up email alerts for service issues

## Support

- Render Docs: https://render.com/docs
- Render Community: https://community.render.com
- Render Status: https://status.render.com

