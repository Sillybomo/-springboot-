/**
* 左边菜单
*/ 
<template>
  <el-menu default-active="2" :collapse="collapsed" collapse-transition router :default-active="$route.path" unique-opened class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b">
    <div class="logobox">
      <img class="logoimg" src="../assets/img/logo.png" alt="">
    </div>
    
    <!-- 返回首页按钮 -->
    <el-menu-item index="home" @click="goHome" class="home-button">
      <i class="el-icon-house"></i>
      <span>返回首页</span>
    </el-menu-item>
    
    <!-- AI智能助手按钮 -->
    <el-menu-item index="ai" @click="goAi" class="ai-button">
      <i class="el-icon-chat-line-square"></i>
      <span>AI智能助手</span>
    </el-menu-item>
    
    <!-- 用户余额显示和充值 -->
    <div v-if="!isAdmin" class="balance-section">
      <div class="balance-display">
        <i class="el-icon-wallet"></i>
        <span>余额: ¥{{ userBalance }}</span>
        <el-button 
          type="primary" 
          size="mini" 
          icon="el-icon-plus" 
          @click="showRechargeDialog"
          class="recharge-btn"
        >
          充值
        </el-button>
      </div>
    </div>
    
    <el-submenu v-for="menu in allmenu" :key="menu.menuid" :index="menu.menuname">
      <template slot="title">
        <i class="iconfont" :class="menu.icon"></i>
        <span>{{menu.menuname}}</span>
      </template>
      <el-menu-item-group>
        <el-menu-item v-for="chmenu in menu.menus" :index="chmenu.url" :key="chmenu.menuid">
          <i class="iconfont" :class="chmenu.icon"></i>
          <span>{{chmenu.menuname}}</span>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    
    <!-- 充值弹窗 -->
    <el-dialog
      title="充值余额"
      :visible.sync="rechargeDialogVisible"
      width="500px"
    >
      <el-form :model="rechargeForm" :rules="rechargeRules" ref="rechargeForm" label-width="100px">
        <el-form-item label="当前余额">
          <span style="color: #f56c6c; font-size: 16px; font-weight: bold;">¥{{ userBalance }}</span>
        </el-form-item>
        
        <el-form-item label="充值方式" prop="method">
          <el-radio-group v-model="rechargeForm.method" @change="onPaymentMethodChange">
            <el-radio label="alipay">
              <i class="el-icon-bank-card"></i> 支付宝
            </el-radio>
            <el-radio label="wechat">
              <i class="el-icon-chat-line-square"></i> 微信支付
            </el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="充值金额" prop="amount" v-if="rechargeForm.method">
          <div class="amount-selection">
            <div class="quick-amounts">
              <el-button 
                v-for="amount in quickAmounts" 
                :key="amount"
                :type="rechargeForm.amount === amount ? 'primary' : 'default'"
                size="small"
                @click="selectAmount(amount)"
                class="amount-btn"
              >
                ¥{{ amount }}
              </el-button>
            </div>
            <el-input-number
              v-model="rechargeForm.amount"
              :min="1"
              :max="10000"
              :precision="2"
              placeholder="请输入充值金额"
              style="width: 100%; margin-top: 10px;"
            ></el-input-number>
          </div>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          :loading="rechargeLoading" 
          @click="confirmRecharge"
          :disabled="!rechargeForm.method || !rechargeForm.amount"
        >
          确认充值
        </el-button>
      </div>
    </el-dialog>
    
    <!-- 支付二维码弹窗 -->
    <el-dialog
      :title="paymentDialogTitle"
      :visible.sync="paymentDialogVisible"
      width="400px"
      :close-on-click-modal="false"
    >
      <div class="payment-content">
        <div class="payment-info">
          <p><strong>充值金额：</strong><span class="amount">¥{{ rechargeForm.amount }}</span></p>
          <p><strong>支付方式：</strong>{{ rechargeForm.method === 'alipay' ? '支付宝' : '微信支付' }}</p>
        </div>
        
        <div class="qr-code-container">
          <img :src="qrCodeImage" alt="支付二维码" class="qr-code" />
          <p class="qr-tip">请使用{{ rechargeForm.method === 'alipay' ? '支付宝' : '微信' }}扫描二维码完成支付</p>
        </div>
        
        <div class="payment-tips">
          <el-alert
            title="支付提示"
            type="info"
            :closable="false"
            show-icon
          >
            <p>• 请确保支付金额与订单金额一致</p>
            <p>• 支付成功后余额将自动到账</p>
            <p>• 如有问题请联系客服</p>
          </el-alert>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelPayment">取消支付</el-button>
        <el-button type="success" :loading="paymentLoading" @click="confirmPayment">支付完成</el-button>
      </div>
    </el-dialog>
  </el-menu>
</template>
<script>
import { menu } from '../api/userMG'
import { getCurrentUser, isAdmin } from '@/utils/auth'
import { GetUserBalance, RechargeBalance } from '@/api/userBalance'

export default {
  name: 'leftnav',
  data() {
    return {
      collapsed: false,
      allmenu: [],
      userBalance: 0,
      rechargeDialogVisible: false,
      rechargeLoading: false,
      rechargeForm: {
        amount: 0,
        method: ''
      },
      rechargeRules: {
        amount: [
          { required: true, message: '请输入充值金额', trigger: 'blur' },
          { type: 'number', min: 1, message: '充值金额不能少于1元', trigger: 'blur' }
        ],
        method: [
          { required: true, message: '请选择充值方式', trigger: 'change' }
        ]
      },
      paymentDialogVisible: false,
      paymentLoading: false,
      quickAmounts: [10, 20, 50, 100, 200, 500],
      qrCodeImage: '',
      paymentDialogTitle: ''
    }
  },
  computed: {
    isAdmin() {
      return isAdmin()
    }
  },
  // 创建完毕状态(里面是操作)
  created() {
    this.generateMenu()
    this.loadUserBalance()
    
    this.$root.Bus.$on('toggle', value => {
      this.collapsed = !value
    })
    
    // 监听充值弹窗事件
    this.$root.Bus.$on('showRechargeDialog', () => {
      this.showRechargeDialog()
    })
    
    // 监听余额更新事件
    this.$root.Bus.$on('updateUserBalance', (newBalance) => {
      this.userBalance = newBalance
    })
  },
  methods: {
    // 返回首页
    goHome() {
      this.$router.push('/')
    },
    
    // AI智能助手
    goAi() {
      this.$router.push('/charts/mychart')
    },
    
    // 加载用户余额
    async loadUserBalance() {
      if (this.isAdmin) return
      
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
    
    // 显示充值弹窗
    showRechargeDialog() {
      this.rechargeForm.amount = 0
      this.rechargeForm.method = ''
      this.rechargeDialogVisible = true
    },
    
    // 支付方式改变
    onPaymentMethodChange() {
      this.rechargeForm.amount = 0
    },
    
    // 选择快速金额
    selectAmount(amount) {
      this.rechargeForm.amount = amount
    },
    
    // 确认充值
    async confirmRecharge() {
      const valid = await this.$refs.rechargeForm.validate().catch(() => false)
      if (!valid) return
      
      // 显示支付二维码弹窗
      this.showPaymentDialog()
    },
    
    // 显示支付二维码弹窗
    showPaymentDialog() {
      this.paymentDialogTitle = `${this.rechargeForm.method === 'alipay' ? '支付宝' : '微信支付'}扫码支付`
      
      // 根据支付方式设置二维码图片
      if (this.rechargeForm.method === 'alipay') {
        this.qrCodeImage = require('../assets/img/ALi.jpg')
      } else if (this.rechargeForm.method === 'wechat') {
        this.qrCodeImage = require('../assets/img/WeChat.jpg')
      }
      
      this.paymentDialogVisible = true
      this.rechargeDialogVisible = false
    },
    
    // 取消支付
    cancelPayment() {
      this.paymentDialogVisible = false
      this.rechargeDialogVisible = true
    },
    
    // 确认支付完成
    async confirmPayment() {
      this.paymentLoading = true
      try {
        // 模拟支付验证过程
        await new Promise(resolve => setTimeout(resolve, 2000))
        
        // 调用充值API
        const user = getCurrentUser()
        if (user && user.id) {
          const response = await RechargeBalance(user.id, this.rechargeForm.amount)
          if (response && response.success) {
            this.userBalance = response.data
            this.$message.success(`${this.rechargeForm.method === 'alipay' ? '支付宝' : '微信支付'}充值成功！`)
            this.paymentDialogVisible = false
            this.rechargeDialogVisible = false
            
            // 通知其他组件更新余额
            this.$root.Bus.$emit('updateUserBalance', this.userBalance)
          } else {
            this.$message.error(response.message || '充值失败')
          }
        }
      } catch (error) {
        console.error('充值失败:', error)
        this.$message.error('充值失败，请稍后再试')
      } finally {
        this.paymentLoading = false
      }
    },
    
    /**
     * 根据用户角色生成菜单
     */
    generateMenu() {
      const user = getCurrentUser()
      const userIsAdmin = isAdmin()
      
      let menuData = []
      
      if (userIsAdmin) {
        // 管理员菜单
        menuData = [
          {
            menuid: 71,
            icon: 'li-icon-xitongguanli',
            menuname: '系统后台管理',
            hasThird: null,
            url: null,
            menus: [
              {
                menuid: 157,
                icon: 'icon-provider-manage',
                menuname: 'pyecharts数据可视化',
                hasThird: 'N',
                url: '/charts/pyecharts',
                menus: null
              },
              {
                menuid: 174,
                icon: 'icon-cms-manage',
                menuname: '放票管理',
                hasThird: 'N',
                url: '/admin/Ticket',
                menus: null
              },
              {
                menuid: 72,
                icon: 'icon-cus-manage',
                menuname: '退票管理',
                hasThird: 'N',
                url: '/admin/RefundTicket',
                menus: null
              },
              {
                menuid: 73,
                icon: 'icon-cus-manage',
                menuname: '用户管理',
                hasThird: 'N',
                url: '/admin/UserManage',
                menus: null
              }
            ]
          }
        ]
      } else {
        // 普通用户菜单
        menuData = [
          {
            menuid: 80,
            icon: 'li-icon-shopping-cart',
            menuname: '用户中心',
            hasThird: null,
            url: null,
            menus: [
              {
                menuid: 81,
                icon: 'li-icon-dingdanguanli',
                menuname: '购物车',
                hasThird: 'N',
                url: '/cart',
                menus: null
              },
              {
                menuid: 82,
                icon: 'icon-order-manage',
                menuname: '我的订单',
                hasThird: 'N',
                url: '/orders',
                menus: null
              },
              {
                menuid: 83,
                icon: 'icon-cus-manage',
                menuname: '个人中心',
                hasThird: 'N',
                url: '/profile',
                menus: null
              }
            ]
          }
        ]
      }
      
      this.allmenu = menuData
    }
  }
}
</script>
<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 240px;
  min-height: 400px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  border: none;
  text-align: left;
}
.el-menu-item-group__title {
  padding: 0px;
}
.el-menu-bg {
  background-color: #1f2d3d !important;
}
.el-menu {
  border: none;
}
.logobox {
  height: 40px;
  line-height: 40px;
  color: #9d9d9d;
  font-size: 20px;
  text-align: center;
  padding: 20px 0px;
}
.logoimg {
  height: 40px;
}

/* 新增样式 */
.home-button {
  margin: 10px 0;
  border-radius: 4px;
}
.home-button:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}
.home-button i {
  margin-right: 8px;
}

.ai-button {
  margin: 10px 0;
  border-radius: 4px;
}
.ai-button:hover {
  background-color: rgba(255, 107, 107, 0.2) !important;
}
.ai-button i {
  color: #ff6b6b;
  margin-right: 8px;
}

.balance-section {
  margin: 15px 10px;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.balance-display {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}

.balance-display i {
  margin-right: 8px;
  color: #f56c6c;
}

.recharge-btn {
  margin-left: 10px;
  font-size: 12px;
  padding: 4px 8px;
}

.amount-selection {
  margin-top: 10px;
}

.quick-amounts {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 10px;
}

.amount-btn {
  min-width: 60px;
}

.payment-content {
  text-align: center;
}

.payment-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.payment-info p {
  margin: 5px 0;
  font-size: 14px;
}

.amount {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.qr-code-container {
  margin: 20px 0;
}

.qr-code {
  width: 200px;
  height: 200px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.qr-tip {
  margin-top: 10px;
  color: #666;
  font-size: 14px;
}

.payment-tips {
  margin-top: 20px;
}

.payment-tips p {
  margin: 3px 0;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .balance-display {
    font-size: 12px;
  }
  
  .recharge-btn {
    font-size: 10px;
    padding: 2px 6px;
  }
}

@media (max-width: 480px) {
  .balance-section {
    margin: 10px 5px;
    padding: 8px;
  }
  
  .balance-display {
    font-size: 11px;
  }
  
  .recharge-btn {
    font-size: 9px;
    padding: 2px 4px;
  }
}
</style>