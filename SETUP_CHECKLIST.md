# ✅ GitHub Actions Setup Checklist

## Phase 1: Chuẩn bị Docker Hub
- [ ] Truy cập [Docker Hub Personal Access Tokens](https://app.docker.com/settings/personal-access-tokens)
- [ ] Tạo Personal Access Token với quyền `Read & Write`
- [ ] Copy token (chỉ hiển thị 1 lần!)

## Phase 2: Thiết lập GitHub Secrets
- [ ] Truy cập GitHub repository: https://github.com/AnhPhu29/DemoGitHubActions
- [ ] Vào **Settings** → **Secrets and variables** → **Actions**
- [ ] Thêm secret `DOCKER_HUB_USERNAME`
  - Giá trị: Tên Docker Hub của bạn (VD: nguyenanhphu)
- [ ] Thêm secret `DOCKER_HUB_TOKEN`
  - Giá trị: Personal Access Token vừa tạo
- [ ] Kiểm tra 2 secrets được liệt kê

## Phase 3: Chuẩn bị Local Repository
- [ ] Đã có project folder (d:\nguyenanhphu)
- [ ] File `.github/workflows/deploy.yml` đã được tạo ✅
- [ ] File `.gitignore` đã được cấu hình đúng ✅
- [ ] `Dockerfile` có sẵn ✅
- [ ] `pom.xml` cấu hình Java 25 ✅

## Phase 4: Push Code lên GitHub
Chạy các lệnh sau:
```bash
cd d:\nguyenanhphu

# Khởi tạo Git
git init

# Cấu hình người dùng (nếu chưa)
git config user.name "Anh Phu"
git config user.email "your.email@example.com"

# Thêm files
git add .

# Commit
git commit -m "Initial commit: Spring Boot with Docker and GitHub Actions"

# Thêm remote
git remote add origin https://github.com/AnhPhu29/DemoGitHubActions.git

# Đổi nhánh (nếu cần)
git branch -M main

# Push code
git push -u origin main
```
- [ ] `git init` thành công
- [ ] `git add .` thực thi
- [ ] `git commit` tạo commit đầu tiên
- [ ] `git remote add origin` liên kết repository
- [ ] `git push -u origin main` push code thành công

## Phase 5: Kiểm tra GitHub Actions
- [ ] Truy cập GitHub repository
- [ ] Vào tab **Actions**
- [ ] Chờ workflow `Build and Push Docker Image` chạy
- [ ] Kiểm tra log để xác nhận:
  - ✅ Checkout code thành công
  - ✅ Java setup thành công
  - ✅ Maven build thành công
  - ✅ Docker login thành công
  - ✅ Docker image build thành công
  - ✅ Push lên Docker Hub thành công

## Phase 6: Xác nhận trên Docker Hub
- [ ] Truy cập Docker Hub: https://hub.docker.com/r/nguyenanhphu/nguyenanhphu
- [ ] Kiểm tra image được upload:
  - Tag `latest` (nếu là main branch)
  - Tag `main` (branch name)
  - Tag `{commit-sha}` (commit hash)

## Phase 7: Test Auto-Deploy (Optional)
Để test workflow tự động chạy:
```bash
# Tạo thay đổi nhỏ
echo "# Test CI/CD" >> README.md

# Commit và push
git add README.md
git commit -m "Test: Trigger GitHub Actions"
git push

# Kiểm tra Actions tab sẽ tự động chạy workflow
```
- [ ] Thay đổi code
- [ ] Push lên GitHub
- [ ] Kiểm tra workflow tự động chạy trong tab Actions
- [ ] Xác nhận Docker image được update trên Docker Hub

---

## 📌 Ghi chú quan trọng

- **Kubernetes có thể sử dụng image từ Docker Hub:** `nguyenanhphu/nguyenanhphu:latest`
- **Tags tự động được tạo:** main, latest, commit-hash
- **Workflow chạy tự động khi push đến main branch**
- **Secrets được bảo mật - không hiển thị trong logs**

---

## 🆘 Nếu có vấn đề

1. **Kiểm tra GitHub Secrets:**
   - Settings → Secrets and variables → Actions
   - Đảm bảo 2 secrets tồn tại

2. **Kiểm tra Dockerfile:**
   - Phải ở root directory
   - Phải có quyền đọc

3. **Kiểm tra Docker Hub:**
   - Token có hết hạn không?
   - Quyền token có đúng không?

4. **Xem chi tiết lỗi:**
   - GitHub Actions → Workflow run → Click job
   - Xem các step logs chi tiết

---

**Dự kiến thời gian:** 15-20 phút  
**Trạng thái:** 🔄 Sẵn sàng implement
