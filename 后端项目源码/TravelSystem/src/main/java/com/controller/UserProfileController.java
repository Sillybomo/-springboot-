package com.controller;

import com.entity.po.User;
import com.entity.vo.ResultVO;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 用户个人信息管理控制器
 */
@RestController
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户详细信息
     */
    @GetMapping("/profile/{userId}")
    public ResultVO getUserProfile(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }
            return ResultVO.ok(user);
        } catch (Exception e) {
            return ResultVO.err(500, "获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户基本信息
     */
    @PutMapping("/profile/{userId}")
    public ResultVO updateUserProfile(@PathVariable Long userId, @RequestBody UserProfileUpdateRequest request) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }

            // 更新用户信息
            if (request.getUsername() != null) {
                user.setUsername(request.getUsername());
            }
            if (request.getName() != null) {
                user.setName(request.getName());
            }
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                user.setAddress(request.getAddress());
            }

            userService.updateById(user, userId);
            return ResultVO.ok("用户信息更新成功");
        } catch (Exception e) {
            return ResultVO.err(500, "更新用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password/{userId}")
    public ResultVO changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }

            // 验证旧密码
            if (!user.getPassword().equals(request.getOldPassword())) {
                return ResultVO.err(400, "当前密码错误");
            }

            // 更新密码
            user.setPassword(request.getNewPassword());
            userService.updateById(user, userId);

            return ResultVO.ok("密码修改成功");
        } catch (Exception e) {
            return ResultVO.err(500, "修改密码失败: " + e.getMessage());
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar/{userId}")
    public ResultVO uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }

            if (file.isEmpty()) {
                return ResultVO.err(400, "请选择要上传的文件");
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResultVO.err(400, "只能上传图片文件");
            }

            // 检查文件大小 (2MB)
            if (file.getSize() > 2 * 1024 * 1024) {
                return ResultVO.err(400, "文件大小不能超过2MB");
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResultVO.err(400, "文件名不能为空");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            // 获取项目根目录并构建上传路径
            String projectRoot = System.getProperty("user.dir");
            // 构建完整的头像上传路径 - 保存到前端项目的static目录
            String[] pathParts = {"vue-travel-system", "static", "avatar"};
            StringBuilder pathBuilder = new StringBuilder(projectRoot);
            for (String part : pathParts) {
                pathBuilder.append(File.separator).append(part);
            }
            pathBuilder.append(File.separator);
            String uploadPath = pathBuilder.toString();
            
            // 确保路径使用正确的分隔符
            uploadPath = uploadPath.replace("\\", "/");
            
            // 添加调试日志
            System.out.println("项目根目录: " + projectRoot);
            System.out.println("上传路径: " + uploadPath);
            System.out.println("路径是否存在: " + new File(uploadPath).exists());
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("创建目录: " + uploadPath + " 结果: " + created);
            }

            String filePath = uploadPath + fileName;
            System.out.println("文件保存路径: " + filePath);
            file.transferTo(new File(filePath));

            // 更新用户头像路径（相对路径，用于前端访问）
            String relativePath = "static/avatar/" + fileName;
            System.out.println("数据库存储路径: " + relativePath);
            user.setAvatar(relativePath);
            userService.updateById(user, userId);

            return ResultVO.ok("头像上传成功", relativePath);
        } catch (IOException e) {
            return ResultVO.err(500, "文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            return ResultVO.err(500, "头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 用户信息更新请求
     */
    public static class UserProfileUpdateRequest {
        private String username;
        private String name;
        private String email;
        private String phone;
        private String address;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    /**
     * 修改密码请求
     */
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        // Getters and Setters
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
