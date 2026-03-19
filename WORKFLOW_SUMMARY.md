# 🚀 GitHub Actions CI/CD Pipeline Summary

## 📊 Luồng công việc (Workflow)

```
┌─────────────────────────────────────────────────────────────────┐
│                     DEVELOPER PUSHES CODE                       │
│              (Push to main branch on GitHub)                   │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│              GITHUB ACTIONS WORKFLOW TRIGGERED                  │
│          (.github/workflows/deploy.yml starts)                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   1. CLONE   │    │   2. BUILD   │    │ 3. GENERATE │
│   REPO       │    │   PROJECT    │    │    TAGS     │
│              │    │   (Maven)    │    │             │
│ Checkout@v4 │    │mvn package   │    │ Docker tags │
└──────────────┘    └──────────────┘    └──────────────┘
        │                    │                    │
        └────────────────────┼────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│         4. LOGIN TO DOCKER HUB                                  │
│    (Using GitHub Secrets: DOCKER_HUB_USERNAME + TOKEN)        │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│           5. BUILD DOCKER IMAGE                                 │
│    (From Dockerfile, with Maven-built JAR inside)              │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│        6. PUSH IMAGE TO DOCKER HUB                              │
│   (nguyenanhphu/nguyenanhphu:main, :latest, :commit-sha)       │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│            DONE ✅                                              │
│   Image available on Docker Hub - Ready for Deployment        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔑 Các secrete cần thiết

| Secret Name | Giá trị | Lấy từ đâu |
|------------|--------|-----------|
| `DOCKER_HUB_USERNAME` | Tên Docker Hub (VD: nguyenanhphu) | Docker Hub Profile |
| `DOCKER_HUB_TOKEN` | Personal Access Token | Docker Hub → Settings → Security |

**Cách lưu Secrets vào GitHub:**
```
GitHub Repo → Settings → Secrets and variables → Actions → New repository secret
```

---

## 📦 Docker Image Tags tự động sinh ra

Khi push code lên main branch, workflow sẽ tạo image với các tags:

```
nguyenanhphu/nguyenanhphu:main
nguyenanhphu/nguyenanhphu:latest
nguyenanhphu/nguyenanhphu:main-abc1234    (commit SHA)
```

**Để pull và chạy image:**
```bash
docker pull nguyenanhphu/nguyenanhphu:latest
docker run -p 8080:8080 nguyenanhphu/nguyenanhphu:latest
```

---

## 🛠️ Các công cụ/Action sử dụng

| Action | Vai trò | Version |
|--------|---------|---------|
| `actions/checkout@v4` | Clone repository | v4 |
| `actions/setup-java@v4` | Cài Java 25 | v4 |
| `docker/setup-buildx-action@v3` | Docker builder | v3 |
| `docker/login-action@v3` | Login Docker Hub | v3 |
| `docker/metadata-action@v5` | Generate tags | v5 |
| `docker/build-push-action@v5` | Build & push image | v5 |

---

## 📋 File cấu trúc dự án

```
d:\nguyenanhphu\
├── .github\
│   └── workflows\
│       └── deploy.yml              ← CI/CD workflow chính
├── src\
│   ├── main\java\...               ← Source code
│   └── test\java\...               ← Test code
├── pom.xml                          ← Maven configuration
├── Dockerfile                       ← Docker image definition
├── docker-compose.yml               ← Local development
├── .gitignore                       ← Git ignore rules
├── GITHUB_ACTIONS_SETUP.md          ← Hướng dẫn chi tiết
└── SETUP_CHECKLIST.md               ← Checklist
```

---

## ⚙️ Chi tiết Dockerfile

```dockerfile
# Multi-stage build - tối ưu hóa kích thước image
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage - chỉ chứa JRE
FROM eclipse-temurin:25-jre-noble
WORKDIR /app
COPY --from=builder /app/target/nguyenanhphu-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Kích thước:**
- Builder stage: ~800MB (chứa Maven)
- Final image: ~350-400MB (chỉ JRE)

---

## 🔄 Kích hoạt Workflow

Workflow sẽ **tự động chạy** khi:
✅ Push code lên branch `main`  
✅ Tạo Pull Request đến branch `main`

**Xem kết quả:**
```
GitHub Repo → Actions → Build and Push Docker Image → Click run
```

---

## 🎯 Kết quả mong đợi

Sau khi setup hoàn tất:

1. **GitHub Actions:**
   - Mỗi push đến `main` sẽ tự động trigger workflow
   - Tất cả steps chạy xanh (✅)

2. **Docker Hub:**
   - Image được upload với tags tự động
   - Có thể pull từ any server: `docker pull nguyenanhphu/nguyenanhphu:latest`

3. **Kubernetes/Deployment:**
   - Có thể deploy từ Docker Hub image
   - VD: `docker run -p 8080:8080 nguyenanhphu/nguyenanhphu:latest`

---

## 💾 Lệnh Push Code

```bash
cd d:\nguyenanhphu

# Setup local git repository
git init
git config user.name "Your Name"
git config user.email "your.email@example.com"

# Stage all files
git add .

# Create first commit
git commit -m "Initial commit: Spring Boot with Docker CI/CD"

# Add GitHub remote
git remote add origin https://github.com/AnhPhu29/DemoGitHubActions.git

# Ensure main branch (rename if needed)
git branch -M main

# Push to GitHub
git push -u origin main
```

**Sau khi push:**
- ✅ Workflow tự động chạy
- ✅ Docker image được build
- ✅ Image được push lên Docker Hub
- ✅ Hoàn tất trong ~5-10 phút

---

## 📚 Tài liệu tham khảo

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker GitHub Actions](https://github.com/docker/build-push-action)
- [Spring Boot Docker Best Practices](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Hub Repositories](https://hub.docker.com/)

---

## ✅ Ready to Deploy!

Tất cả công cụ đã sẵn sàng. Bạn có thể bắt đầu:

1. Thiết lập Docker Hub Secrets trên GitHub ✅
2. Push code lên GitHub ✅
3. Xem workflow chạy tự động ✅
4. Deploy từ Docker Hub image ✅

**Thời gian:** ~15 phút để hoàn thành setup  
**Tính năng:** CI/CD hoàn toàn tự động
