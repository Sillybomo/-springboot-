<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 页面说明 -->
    <el-alert
      title="个人中心"
      type="info"
      description="您可以在此修改个人信息、密码等账户设置"
      :closable="false"
      style="margin-bottom: 20px;">
    </el-alert>

    <el-row :gutter="20">
      <!-- 左侧用户信息卡片 -->
      <el-col :span="8">
        <el-card class="user-info-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-user"></i> 个人信息</span>
          </div>
          
          <div class="user-avatar">
            <el-avatar :size="100" :src="getAvatarUrl(userInfo.avatar)">
              <i class="el-icon-user"></i>
            </el-avatar>
            <el-button 
              type="text" 
              size="small" 
              @click="changeAvatar"
              style="margin-top: 10px;"
            >
              更换头像
            </el-button>
          </div>
          
          <div class="user-details">
            <h3>{{ userInfo.username || userInfo.name || '用户' }}</h3>
            <p><i class="el-icon-phone"></i> {{ userInfo.phone || '未设置' }}</p>
            <p><i class="el-icon-message"></i> {{ userInfo.email || '未设置' }}</p>
            <p><i class="el-icon-location"></i> {{ userInfo.address || '未设置' }}</p>
            <p><i class="el-icon-wallet"></i> 余额: ¥{{ userBalance.toFixed(2) }}</p>
            <p><i class="el-icon-time"></i> 注册时间: {{ formatDate(userInfo.createdAt) }}</p>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧表单区域 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-edit"></i> 账户设置</span>
          </div>

          <el-tabs v-model="activeTab" type="border-card">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="basic">
              <el-form 
                :model="basicForm" 
                :rules="basicRules" 
                ref="basicForm" 
                label-width="100px"
                class="profile-form"
              >
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="basicForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                
                <el-form-item label="真实姓名" prop="name">
                  <el-input v-model="basicForm.name" placeholder="请输入真实姓名"></el-input>
                </el-form-item>
                
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="basicForm.phone" placeholder="请输入手机号码"></el-input>
                </el-form-item>
                
                <el-form-item label="邮箱地址" prop="email">
                  <el-input v-model="basicForm.email" placeholder="请输入邮箱地址"></el-input>
                </el-form-item>
                
                <el-form-item label="地址" prop="address">
                  <el-input
                    type="textarea"
                    v-model="basicForm.address"
                    placeholder="请输入地址"
                    :rows="3"
                    maxlength="200"
                    show-word-limit
                  ></el-input>
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" :loading="basicLoading" @click="updateBasicInfo">
                    保存基本信息
                  </el-button>
                  <el-button @click="resetBasicForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="password">
              <el-form 
                :model="passwordForm" 
                :rules="passwordRules" 
                ref="passwordForm" 
                label-width="100px"
                class="profile-form"
              >
                <el-form-item label="当前密码" prop="oldPassword">
                  <el-input 
                    type="password" 
                    v-model="passwordForm.oldPassword" 
                    placeholder="请输入当前密码"
                    show-password
                  ></el-input>
                </el-form-item>
                
                <el-form-item label="新密码" prop="newPassword">
                  <el-input 
                    type="password" 
                    v-model="passwordForm.newPassword" 
                    placeholder="请输入新密码"
                    show-password
                  ></el-input>
                </el-form-item>
                
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input 
                    type="password" 
                    v-model="passwordForm.confirmPassword" 
                    placeholder="请再次输入新密码"
                    show-password
                  ></el-input>
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" :loading="passwordLoading" @click="updatePassword">
                    修改密码
                  </el-button>
                  <el-button @click="resetPasswordForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 账户安全 -->
            <el-tab-pane label="账户安全" name="security">
              <div class="security-settings">
                <div class="security-item">
                  <div class="security-info">
                    <h4><i class="el-icon-phone"></i> 手机验证</h4>
                    <p>已绑定手机: {{ userInfo.phone || '未绑定' }}</p>
                  </div>
                  <el-button 
                    type="primary" 
                    size="small"
                    :disabled="!userInfo.phone"
                    @click="changePhone"
                  >
                    {{ userInfo.phone ? '更换手机' : '绑定手机' }}
                  </el-button>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4><i class="el-icon-message"></i> 邮箱验证</h4>
                    <p>已绑定邮箱: {{ userInfo.email || '未绑定' }}</p>
                  </div>
                  <el-button 
                    type="primary" 
                    size="small"
                    :disabled="!userInfo.email"
                    @click="changeEmail"
                  >
                    {{ userInfo.email ? '更换邮箱' : '绑定邮箱' }}
                  </el-button>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4><i class="el-icon-wallet"></i> 账户余额</h4>
                    <p>当前余额: ¥{{ userBalance.toFixed(2) }}</p>
                  </div>
                  <el-button 
                    type="success" 
                    size="small"
                    @click="goToRecharge"
                  >
                    充值余额
                  </el-button>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4><i class="el-icon-lock"></i> 登录状态</h4>
                    <p>当前设备已登录，注册时间: {{ formatDate(userInfo.createdAt) }}</p>
                  </div>
                  <el-button 
                    type="warning" 
                    size="small"
                    @click="logout"
                  >
                    退出登录
                  </el-button>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <!-- 更换头像弹窗 -->
    <el-dialog
      title="更换头像"
      :visible.sync="avatarDialogVisible"
      width="400px"
    >
      <div class="avatar-upload">
        <el-upload
          class="avatar-uploader"
          action="#"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="avatarPreview" :src="avatarPreview" class="avatar-preview">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <p class="upload-tip">支持 JPG、PNG 格式，文件大小不超过 2MB</p>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="avatarDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="avatarLoading" @click="confirmAvatar">确定</el-button>
      </div>
    </el-dialog>

    <!-- 更换手机弹窗 -->
    <el-dialog
      title="更换手机号码"
      :visible.sync="phoneDialogVisible"
      width="400px"
    >
      <el-form :model="phoneForm" :rules="phoneRules" ref="phoneForm" label-width="100px">
        <el-form-item label="新手机号" prop="phone">
          <el-input v-model="phoneForm.phone" placeholder="请输入新手机号码"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input v-model="phoneForm.code" placeholder="请输入验证码">
            <el-button slot="append" :disabled="codeCountdown > 0" @click="sendCode">
              {{ codeCountdown > 0 ? `${codeCountdown}s后重发` : '发送验证码' }}
            </el-button>
          </el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="phoneDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="phoneLoading" @click="confirmPhone">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCurrentUser } from '@/utils/auth'
import { GetUserBalance } from '@/api/userBalance'
import { GetUserProfile, UpdateUserProfile, ChangePassword, UploadAvatar } from '@/api/userProfile'
import { getAvatarUrl, validateAvatarFile, generateAvatarPreview, DEFAULT_AVATAR } from '@/utils/avatar'

export default {
  name: "Profile",
  
  data() {
    return {
      activeTab: 'basic',
      userBalance: 0,
      avatarDialogVisible: false,
      phoneDialogVisible: false,
      avatarLoading: false,
      basicLoading: false,
      passwordLoading: false,
      phoneLoading: false,
      codeCountdown: 0,
      avatarPreview: '',
      avatarFile: null,
      defaultAvatar: DEFAULT_AVATAR,
      
      // 用户信息
      userInfo: {},
      
      // 基本信息表单
      basicForm: {
        username: '',
        name: '',
        phone: '',
        email: '',
        address: ''
      },
      
      // 密码修改表单
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      
      // 手机更换表单
      phoneForm: {
        phone: '',
        code: ''
      },
      
      // 表单验证规则
      basicRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        address: [
          { max: 200, message: '地址长度不能超过200个字符', trigger: 'blur' }
        ]
      },
      
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入当前密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      },
      
      phoneRules: {
        phone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { len: 6, message: '验证码为6位数字', trigger: 'blur' }
        ]
      }
    }
  },
  
  created() {
    this.loadUserInfo()
    this.loadUserBalance()
    
    // 监听余额更新事件
    this.$root.Bus.$on('updateUserBalance', (newBalance) => {
      this.userBalance = newBalance
    })
  },
  
  methods: {
    // 加载用户信息
    async loadUserInfo() {
      try {
        const user = getCurrentUser()
        if (user && user.id) {
          // 从后端获取最新用户信息
          const response = await GetUserProfile(user.id)
          if (response && response.success) {
            this.userInfo = response.data
            this.basicForm = {
              username: this.userInfo.username || '',
              name: this.userInfo.name || '',
              phone: this.userInfo.phone || '',
              email: this.userInfo.email || '',
              address: this.userInfo.address || ''
            }
          } else {
            // 如果API失败，使用本地存储的信息
            this.userInfo = { ...user }
            this.basicForm = {
              username: user.username || '',
              name: user.name || '',
              phone: user.phone || '',
              email: user.email || '',
              address: user.address || ''
            }
          }
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
        // 使用本地存储的信息作为备选
        const user = getCurrentUser()
        if (user) {
          this.userInfo = { ...user }
          this.basicForm = {
            username: user.username || '',
            name: user.name || '',
            phone: user.phone || '',
            email: user.email || '',
            address: user.address || ''
          }
        }
      }
    },
    
    // 加载用户余额
    async loadUserBalance() {
      try {
        const user = getCurrentUser()
        if (user && user.id) {
          const response = await GetUserBalance(user.id)
          if (response && response.success) {
            this.userBalance = response.data || 0
          }
        }
      } catch (error) {
        console.error('获取用户余额失败:', error)
        this.userBalance = 0
      }
    },
    
    // 更新基本信息
    async updateBasicInfo() {
      const valid = await this.$refs.basicForm.validate().catch(() => false)
      if (!valid) return
      
      this.basicLoading = true
      try {
        const user = getCurrentUser()
        if (!user || !user.id) {
          this.$message.warning('请先登录')
          return
        }
        
        // 调用后端API更新用户信息
        const response = await UpdateUserProfile(user.id, this.basicForm)
        
        if (response && response.success) {
          // 更新本地用户信息
          this.userInfo = { ...this.userInfo, ...this.basicForm }
          
          // 更新本地存储的用户信息
          const updatedUser = { ...user, ...this.basicForm }
          localStorage.setItem('userdata', JSON.stringify(updatedUser))
          
          this.$message.success('基本信息更新成功')
        } else {
          this.$message.error(response.message || '更新失败')
        }
      } catch (error) {
        console.error('更新基本信息失败:', error)
        this.$message.error('更新失败，请稍后再试')
      } finally {
        this.basicLoading = false
      }
    },
    
    // 重置基本信息表单
    resetBasicForm() {
      this.loadUserInfo()
    },
    
    // 更新密码
    async updatePassword() {
      const valid = await this.$refs.passwordForm.validate().catch(() => false)
      if (!valid) return
      
      this.passwordLoading = true
      try {
        const user = getCurrentUser()
        if (!user || !user.id) {
          this.$message.warning('请先登录')
          return
        }
        
        // 调用后端API修改密码
        const response = await ChangePassword(user.id, {
          oldPassword: this.passwordForm.oldPassword,
          newPassword: this.passwordForm.newPassword
        })
        
        if (response && response.success) {
          this.$message.success('密码修改成功')
          this.resetPasswordForm()
        } else {
          this.$message.error(response.message || '密码修改失败')
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        this.$message.error('修改失败，请稍后再试')
      } finally {
        this.passwordLoading = false
      }
    },
    
    // 重置密码表单
    resetPasswordForm() {
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.$refs.passwordForm.resetFields()
    },
    
    // 验证确认密码
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    },
    
    // 更换头像
    changeAvatar() {
      this.avatarDialogVisible = true
      this.avatarPreview = getAvatarUrl(this.userInfo.avatar)
      this.avatarFile = null
    },
    
    // 头像上传前验证
    async beforeAvatarUpload(file) {
      // 验证文件
      const validation = validateAvatarFile(file)
      if (!validation.valid) {
        this.$message.error(validation.message)
        return false
      }
      
      // 保存文件对象
      this.avatarFile = file
      
      // 生成预览图片
      try {
        this.avatarPreview = await generateAvatarPreview(file)
      } catch (error) {
        console.error('生成预览失败:', error)
        this.$message.error('预览生成失败')
        return false
      }
      
      return false // 阻止自动上传
    },
    
    // 上传头像
    async uploadAvatar() {
      this.avatarLoading = true
      try {
        const user = getCurrentUser()
        if (!user || !user.id) {
          this.$message.warning('请先登录')
          return
        }
        
        // 创建FormData对象
        const formData = new FileReader()
        formData.onload = async (e) => {
          try {
            // 将base64转换为Blob
            const response = await fetch(e.target.result)
            const blob = await response.blob()
            const file = new File([blob], 'avatar.jpg', { type: 'image/jpeg' })
            
            const uploadFormData = new FormData()
            uploadFormData.append('file', file)
            
            // 调用后端API上传头像
            const uploadResponse = await UploadAvatar(user.id, uploadFormData)
            
            if (uploadResponse && uploadResponse.success) {
              // 更新用户信息
              this.userInfo.avatar = uploadResponse.data
              
              // 更新本地存储的用户信息
              const updatedUser = { ...user, avatar: uploadResponse.data }
              localStorage.setItem('userdata', JSON.stringify(updatedUser))
              
              // 更新预览图片
              this.avatarPreview = getAvatarUrl(uploadResponse.data)
              
              this.$message.success('头像更新成功')
              this.avatarDialogVisible = false
            } else {
              this.$message.error(uploadResponse.message || '头像上传失败')
            }
          } catch (error) {
            console.error('头像上传失败:', error)
            this.$message.error('头像上传失败')
          } finally {
            this.avatarLoading = false
          }
        }
        formData.readAsDataURL(this.avatarFile)
      } catch (error) {
        console.error('头像上传失败:', error)
        this.$message.error('头像上传失败')
        this.avatarLoading = false
      }
    },
    
    // 确认头像
    confirmAvatar() {
      this.uploadAvatar()
    },
    
    // 更换手机
    changePhone() {
      this.phoneDialogVisible = true
      this.phoneForm.phone = ''
      this.phoneForm.code = ''
    },
    
    // 发送验证码
    async sendCode() {
      if (!this.phoneForm.phone) {
        this.$message.warning('请先输入手机号码')
        return
      }
      
      // 模拟发送验证码
      this.codeCountdown = 60
      const timer = setInterval(() => {
        this.codeCountdown--
        if (this.codeCountdown <= 0) {
          clearInterval(timer)
        }
      }, 1000)
      
      this.$message.success('验证码已发送')
    },
    
    // 确认更换手机
    async confirmPhone() {
      const valid = await this.$refs.phoneForm.validate().catch(() => false)
      if (!valid) return
      
      this.phoneLoading = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        this.userInfo.phone = this.phoneForm.phone
        this.basicForm.phone = this.phoneForm.phone
        this.$message.success('手机号码更新成功')
        this.phoneDialogVisible = false
      } catch (error) {
        this.$message.error('更新失败，请稍后再试')
      } finally {
        this.phoneLoading = false
      }
    },
    
    // 更换邮箱
    changeEmail() {
      this.$message.info('邮箱更换功能开发中...')
    },
    
    // 去充值
    goToRecharge() {
      this.$root.Bus.$emit('showRechargeDialog')
    },
    
    // 退出登录
    logout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('logintoken')
        localStorage.removeItem('userdata')
        this.$router.push('/login')
        this.$message.success('已退出登录')
      })
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return '未知'
      const d = new Date(date)
      return d.toLocaleDateString()
    },
    
    // 获取头像URL
    getAvatarUrl(avatar) {
      return getAvatarUrl(avatar)
    }
  }
}
</script>

<style scoped>
.user-info-card {
  height: 100%;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.card-header i {
  margin-right: 8px;
  color: #409eff;
}

.user-avatar {
  text-align: center;
  margin-bottom: 20px;
}

.user-details h3 {
  margin: 15px 0 10px 0;
  color: #303133;
  text-align: center;
}

.user-details p {
  margin: 8px 0;
  color: #606266;
  font-size: 14px;
}

.user-details i {
  margin-right: 8px;
  color: #909399;
  width: 16px;
}

.profile-form {
  padding: 20px;
}

.security-settings {
  padding: 20px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}

.security-item:last-child {
  border-bottom: none;
}

.security-info h4 {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 14px;
}

.security-info h4 i {
  margin-right: 8px;
  color: #409eff;
}

.security-info p {
  margin: 0;
  color: #606266;
  font-size: 13px;
}

.avatar-upload {
  text-align: center;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  width: 178px;
  height: 178px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  margin: 0 auto;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar-preview {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-tip {
  margin-top: 10px;
  color: #909399;
  font-size: 12px;
}

.dialog-footer {
  text-align: right;
}

.el-tabs__content {
  padding: 0;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button + .el-button {
  margin-left: 10px;
}
</style>
