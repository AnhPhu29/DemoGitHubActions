# GitHub Actions Setup Guide - Docker Hub CI/CD

## 📋 Mục lục
1. [Thiết lập GitHub Secrets](#1-thiết-lập-github-secrets)
2. [Push mã nguồn lên GitHub](#2-push-mã-nguồn-lên-github)
3. [Cấu hình Workflow](#3-cấu-hình-workflow)
4. [Giải thích deploy.yml](#4-giải-thích-deployyml)

---

## 1. Thiết lập GitHub Secrets

### Bước 1: Lấy Docker Hub Token
1. Truy cập [Docker Hub Personal Access Tokens](https://app.docker.com/settings/personal-access-tokens)
2. Click **"Generate New Token"**
3. Đặt tên token (ví dụ: `github-actions`)
4. Chọn quyền: `Read & Write`
5. Click **"Generate"** và **copy token** (chỉ hiển thị một lần!)

### Bước 2: Thêm Secrets vào GitHub
1. Truy cập repository: https://github.com/AnhPhu29/DemoGitHubActions
2. Vào **Settings** → **Secrets and variables** → **Actions**
3. Click **"New repository secret"**
4. Thêm 2 secrets:

| Secret Name              | Giá trị                          |
|--------------------------|----------------------------------|
| `DOCKER_HUB_USERNAME`    | Tên Docker Hub của bạn (VD: nguyenanhphu) |
| `DOCKER_HUB_TOKEN`       | Token vừa tạo từ bước trên      |

✅ **Xác nhận:** Bạn sẽ thấy 2 secrets được liệt kê với icon 🔒

---

## 2. Push mã nguồn lên GitHub

Chạy các lệnh sau trong thư mục dự án:

```bash
# Khởi tạo Git (nếu chưa có)
git init

# Thêm tệp vào staging area
git add .

# Tạo commit đầu tiên
git commit -m "Initial commit: Spring Boot app with Docker"

# Thêm remote repository
git remote add origin https://github.com/AnhPhu29/DemoGitHubActions.git

# Đổi tên nhánh thành main (nếu cần)
git branch -M main

# Push code lên GitHub
git push -u origin main
```

---

## 3. Cấu hình Workflow

### File: `.github/workflows/deploy.yml`

Workflow này sẽ:
✅ Kích hoạt mỗi khi có push đến nhánh `main`
✅ Build project với Maven
✅ Đăng nhập vào Docker Hub
✅ Build Docker image
✅ Push image lên Docker Hub với tags tự động

---

## 4. Giải thích deploy.yml

### Trigger Events
```yaml
on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main
```
- Chạy khi push hoặc tạo PR đến nhánh `main`

### Steps chính:

#### 1. **Checkout Code**
```yaml
- name: Checkout code
  uses: actions/checkout@v4
```
Clone repository vào workflow runner

#### 2. **Setup Java**
```yaml
- name: Set up Java
  uses: actions/setup-java@v4
  with:
    java-version: '25'
    distribution: 'temurin'
    cache: maven
```
- Cài đặt Java 25 (theo pom.xml)
- Cache Maven để tăng tốc độ build

#### 3. **Build Maven**
```yaml
- name: Build with Maven
  run: mvn clean package -DskipTests
```
Build JAR file từ source code

#### 4. **Login Docker Hub**
```yaml
- name: Login to Docker Hub
  uses: docker/login-action@v3
  with:
    username: ${{ secrets.DOCKER_HUB_USERNAME }}
    password: ${{ secrets.DOCKER_HUB_TOKEN }}
```
Đăng nhập vào Docker Hub sử dụng GitHub Secrets

#### 5. **Extract Metadata (Tags)**
```yaml
- name: Extract metadata
  id: meta
  uses: docker/metadata-action@v5
  with:
    images: ${{ secrets.DOCKER_HUB_USERNAME }}/nguyenanhphu
    tags: |
      type=ref,event=branch
      type=semver,pattern={{version}}
      type=semver,pattern={{major}}.{{minor}}
      type=sha,prefix={{branch}}-
      type=raw,value=latest,enable={{is_default_branch}}
```

**Tags tự động sinh ra:**
- `main` - từ branch name
- `latest` - cho nhánh mặc định
- `{commit-sha}` - commit hash

#### 6. **Build và Push Image**
```yaml
- name: Build and push Docker image
  uses: docker/build-push-action@v5
  with:
    context: .
    file: ./Dockerfile
    push: true
    tags: ${{ steps.meta.outputs.tags }}
    labels: ${{ steps.meta.outputs.labels }}
    cache-from: type=gha
    cache-to: type=gha,mode=max
```
- Build từ `./Dockerfile`
- Push ngay lên Docker Hub (`push: true`)
- Sử dụng cache để tăng tốc độ

---

## 🔍 Kiểm tra kết quả

### Xem kết quả Workflow:
1. Truy cập GitHub: **Actions** tab
2. Xem thông tin chi tiết của từng job
3. Logs sẽ hiển thị:
   - ✅ Build thành công
   - ✅ Đăng nhập Docker Hub
   - ✅ Docker image tạo thành công
   - ✅ Push lên Docker Hub hoàn tất

### Xem image trên Docker Hub:
Truy cập: `https://hub.docker.com/r/nguyenanhphu/nguyenanhphu`

---

## 💡 Troubleshooting

| Lỗi | Giải pháp |
|-----|---------|
| **Authentication failed** | Kiểm tra `DOCKER_HUB_TOKEN` có đúng không, có hết hạn không |
| **Build failed** | Xem Maven logs, kiểm tra Java version trong pom.xml |
| **Dockerfile not found** | Đảm bảo `Dockerfile` ở root directory |
| **Secrets không được nhận** | Đảm bảo secrets đã được thêm đúng tên |

---

## 🚀 Lệnh hữu ích

```bash
# Kiểm tra logs locally
docker build -t nguyenanhphu:test .
docker run -p 8080:8080 nguyenanhphu:test

# Push thủ công (nếu cần test)
docker login -u nguyenanhphu
docker tag nguyenanhphu:test nguyenanhphu/nguyenanhphu:v1.0
docker push nguyenanhphu/nguyenanhphu:v1.0
```

---

**Tác giả:** GitHub Copilot  
**Ngày:** 2026-03-19  
**Deploy status:** ✅ Ready for automatic CI/CD
