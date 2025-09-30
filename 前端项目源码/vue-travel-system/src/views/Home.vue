<template>
  <div class="home-container">
    <!-- 头部横幅区域 -->
    <div class="banner-section">
      <div class="banner-content">
        <h1 class="banner-title">发现美丽景点</h1>
        <p class="banner-subtitle">探索世界，从这里开始</p>
        
        <!-- 右上角用户区域 -->
        <div class="user-area" :class="{ 'floating': showFloatingUser }">
          <!-- 未登录状态 -->
          <div v-if="!isLoggedIn" class="login-section">
            <el-button type="primary" size="small" @click="goToLogin">
              <i class="el-icon-user"></i>
              登录
            </el-button>
          </div>

          <!-- 已登录状态 -->
          <div v-else class="user-section">
            <!-- 浮动模式下，整个区域都可点击 -->
            <el-dropdown
              v-if="showFloatingUser"
              @command="handleUserCommand"
              trigger="click"
              class="floating-dropdown"
            >
              <span class="floating-user-trigger">
                <!-- 用户头像 -->
                <el-avatar
                  :size="28"
                  :src="getAvatarUrl(currentUser.avatar)"
                  class="user-avatar"
                >
                  <i class="el-icon-user"></i>
                </el-avatar>
                <i class="el-icon-arrow-down el-icon--right user-dropdown-icon"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">
                  <i class="el-icon-user"></i>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="cart" v-if="!isAdmin">
                  <i class="el-icon-shopping-cart-2"></i>
                  我的购物车
                  <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
                </el-dropdown-item>
                <el-dropdown-item command="orders" v-if="!isAdmin">
                  <i class="el-icon-s-order"></i>
                  我的订单
                </el-dropdown-item>
                <el-dropdown-item command="admin" v-if="isAdmin">
                  <i class="el-icon-setting"></i>
                  管理后台
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <i class="el-icon-switch-button"></i>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>

            <!-- 非浮动模式下的原始结构 -->
            <div v-else class="user-info">
              <!-- 用户头像 -->
              <el-avatar
                :size="32"
                :src="getAvatarUrl(currentUser.avatar)"
                class="user-avatar"
              >
                <i class="el-icon-user"></i>
              </el-avatar>
              <span class="username">{{ currentUser.username || currentUser.name || '用户' }}</span>
              <el-dropdown @command="handleUserCommand" trigger="click">
                <span class="user-dropdown">
                  <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="profile">
                    <i class="el-icon-user"></i>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="cart" v-if="!isAdmin">
                    <i class="el-icon-shopping-cart-2"></i>
                    我的购物车
                    <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="orders" v-if="!isAdmin">
                    <i class="el-icon-s-order"></i>
                    我的订单
                  </el-dropdown-item>
                  <el-dropdown-item command="admin" v-if="isAdmin">
                    <i class="el-icon-setting"></i>
                    管理后台
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <i class="el-icon-switch-button"></i>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>

        </div>
      </div>
    </div>

    <!-- 浮动购物车按钮 -->
    <div class="floating-cart" :class="{ 'show': showFloatingCart }" @click="goToCart">
      <div class="cart-icon">
        <i class="el-icon-shopping-cart-2"></i>
        <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
      </div>
    </div>

    <!-- 浮动AI助手按钮 -->
    <div class="floating-ai" :class="{ 'show': showFloatingAI }" @click="goToAIChat">
      <div class="ai-icon">
        <i class="el-icon-chat-dot-round"></i>
      </div>
    </div>

    <!-- 热门景点推荐 -->
    <div class="hot-section" v-if="hotAttractions.length > 0">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">热门景点推荐</h2>
          <p class="section-subtitle">精选热门目的地，不容错过</p>
        </div>
        <div class="hot-attractions">
          <div 
            v-for="attraction in hotAttractions" 
            :key="attraction.id"
            class="hot-card"
          >
            <div class="hot-card-image">
              <img :src="getAttractionImageUrl(attraction.imageUrl)" :alt="attraction.name" />
              <div class="hot-badge">HOT</div>
            </div>
            <div class="hot-card-content">
              <h3 class="hot-card-title">{{ attraction.name }}</h3>
              <p class="hot-card-location">
                <i class="el-icon-location-outline"></i>
                {{ attraction.city }}, {{ attraction.province }}
              </p>
              <div class="hot-card-rating" v-if="attraction.rating">
                <el-rate 
                  :value="parseFloat(attraction.rating)" 
                  disabled 
                  show-score 
                  text-color="#ff9900"
                  :max="5"
                ></el-rate>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-filter-section">
      <div class="container">
        <div class="search-area">
          <h3 class="search-title">搜索景点</h3>
          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item>
              <el-input
                v-model="searchForm.name"
                placeholder="输入景点名称"
                size="medium"
                style="width: 300px;"
                @keyup.enter="searchAttractions"
              >
                <i slot="prefix" class="el-icon-search"></i>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.province" 
                placeholder="选择省份" 
                clearable
                size="medium"
                style="width: 150px;"
                @change="searchAttractions"
              >
                <el-option label="全部省份" value=""></el-option>
                <el-option 
                  v-for="province in provinces" 
                  :key="province" 
                  :label="province" 
                  :value="province"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.city" 
                placeholder="选择城市" 
                clearable
                size="medium"
                style="width: 150px;"
                @change="searchAttractions"
              >
                <el-option label="全部城市" value=""></el-option>
                <el-option 
                  v-for="city in cities" 
                  :key="city" 
                  :label="city" 
                  :value="city"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" @click="searchAttractions">搜索</el-button>
              <el-button size="medium" @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 景点列表区域 -->
    <div class="attractions-section" id="attractions-list">
      <div class="container">
        <div class="list-header">
          <h3 class="list-title">
            {{ getListTitle() }}
          </h3>
          <div class="list-info">
            <span class="total-count">共 {{ pageParam.total }} 个景点</span>
            <span class="current-page-info">
              第 {{ pageParam.currentPage }} 页，共 {{ Math.ceil(pageParam.total / pageParam.pageSize) }} 页
            </span>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="loading-spinner">
            <i class="el-icon-loading loading-icon"></i>
            <p>正在加载...</p>
          </div>
        </div>

        <!-- 搜索无结果 -->
        <div v-else-if="attractionList.length === 0 && !loading" class="no-results">
          <el-empty description="没有找到相关景点">
            <el-button type="primary" @click="resetSearch">重置搜索</el-button>
          </el-empty>
        </div>

        <!-- 景点卡片列表 -->
        <div v-else class="attractions-grid">
          <div 
            v-for="attraction in paginatedAttractions" 
            :key="attraction.id"
            class="attraction-card"
          >
            <div class="card-image">
              <img :src="getAttractionImageUrl(attraction.imageUrl)" :alt="attraction.name" />
            </div>
            
            <div class="card-content">
              <h4 class="card-title">{{ attraction.name }}</h4>
              <div class="card-location">
                <i class="el-icon-location-outline"></i>
                <span>{{ attraction.city }}, {{ attraction.province }}</span>
              </div>
              <p class="card-description">{{ attraction.description || '暂无介绍' }}</p>
              
              <div class="card-rating" v-if="attraction.rating">
                <el-rate 
                  :value="parseFloat(attraction.rating)" 
                  disabled 
                  show-score 
                  text-color="#ff9900" 
                  score-template="{value} 分"
                  :max="5"
                ></el-rate>
              </div>
              
              <div class="card-details">
                <div class="card-hours" v-if="attraction.openingHours">
                  <i class="el-icon-time"></i>
                  <span>{{ attraction.openingHours }}</span>
                </div>
                
                <div class="card-address" v-if="attraction.address">
                  <i class="el-icon-map-location"></i>
                  <span>{{ attraction.address }}</span>
                </div>
              </div>
              
              <!-- 门票信息区域 -->
              <div class="tickets-section" v-if="attractionTickets[attraction.id] && attractionTickets[attraction.id].length > 0">
                <h5 class="tickets-title">门票信息</h5>
                <div class="tickets-list">
                  <div 
                    v-for="ticket in attractionTickets[attraction.id]" 
                    :key="ticket.id"
                    class="ticket-item"
                  >
                    <div class="ticket-info">
                      <div class="ticket-type">{{ ticket.ticketType }}</div>
                      <div class="ticket-price">
                        <span class="current-price">¥{{ ticket.price }}</span>
                        <span v-if="ticket.discountPrice && ticket.discountPrice < ticket.price" class="original-price">¥{{ ticket.discountPrice }}</span>
                      </div>
                      <div class="ticket-description">{{ ticket.description || '暂无描述' }}</div>
                      <div class="ticket-stock">库存: {{ ticket.stock || 0 }} 张</div>
                    </div>
                    <div class="ticket-actions">
                      <el-input-number
                        v-model="selectedTickets[attraction.id + '_' + ticket.id]"
                        :min="0"
                        :max="ticket.stock || 0"
                        size="small"
                        style="width: 100px; margin-right: 10px;"
                        @change="updateTicketSelection(attraction.id, ticket.id, $event)"
                      ></el-input-number>
                      <el-button 
                        type="primary" 
                        size="small" 
                        icon="el-icon-shopping-cart-2"
                        :disabled="!selectedTickets[attraction.id + '_' + ticket.id] || selectedTickets[attraction.id + '_' + ticket.id] <= 0"
                        @click="addToCart(attraction, ticket)"
                      >
                        立即预订
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 无门票提示 -->
              <div v-else class="no-tickets">
                <p>该景点暂无可用门票</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页组件 -->
        <div v-if="attractionList.length > 0" class="pagination-wrapper">
          <Pagination v-bind:child-msg="pageParam" @callFather="handlePageChange"></Pagination>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { attractionAPI } from '@/api/attraction'
import { AddToCart, GetCartCount } from '@/api/cart'
import Pagination from '@/components/Pagination'
import { GetTicketsByAttractionId } from '@/api/ticket'
import { isLoggedIn, saveRedirectPath, getCurrentUser, getUserRole, isAdmin as checkIsAdmin } from '@/utils/auth'
import { loginout } from '@/api/userMG'
import { getAttractionImageUrl } from '@/utils/image'
import { getAvatarUrl } from '@/utils/avatar'

export default {
  name: 'Home',
  components: {
    Pagination
  },
  data() {
    return {
      loading: false,
      attractionList: [],
      hotAttractions: [],
      provinces: [],
      cities: [],
      
      // 搜索表单
      searchForm: {
        name: '',
        province: '',
        city: '',
        token: localStorage.getItem('logintoken')
      },
      
      // 分页参数
      pageParam: {
        currentPage: 1,
        pageSize: 8,
        total: 0
      },
      
      // 默认图片
      defaultImage: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400',
      
      // 购物车数量
      cartCount: 0,
      
      // 景点门票信息映射
      attractionTickets: {},
      
      // 门票选择状态
      selectedTickets: {},
      
      // 用户登录状态
      isLoggedIn: false,
      currentUser: null,
      isAdmin: false,
      
      // 浮动购物车控制
      showFloatingCart: false,

      // 浮动AI助手控制
      showFloatingAI: true,

      // 浮动用户区域控制
      showFloatingUser: false,

    }
  },
  
  computed: {
    // 前端分页处理
    paginatedAttractions() {
      const start = (this.pageParam.currentPage - 1) * this.pageParam.pageSize
      const end = start + this.pageParam.pageSize
      return this.attractionList.slice(start, end)
    }
  },
  
  created() {
    this.loadInitialData()
    // 初始化AI助手按钮显示状态
    this.showFloatingAI = true
  },
  
  mounted() {
    // 添加滚动监听
    window.addEventListener('scroll', this.handleScroll)
  },
  
  beforeDestroy() {
    // 移除滚动监听
    window.removeEventListener('scroll', this.handleScroll)
  },
  
  methods: {
    /**
     * 获取景点图片URL
     */
    getAttractionImageUrl(imageUrl) {
      return getAttractionImageUrl(imageUrl)
    },
    
    /**
     * 获取用户头像URL
     */
    getAvatarUrl(avatar) {
      return getAvatarUrl(avatar)
    },
    
    /**
     * 处理滚动事件
     */
    handleScroll() {
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop
      // 当滚动超过150px时显示浮动购物车和用户区域
      this.showFloatingCart = scrollTop > 150
      // 当滚动超过150px时用户区域变为浮动模式
      this.showFloatingUser = scrollTop > 150

      // AI助手按钮始终显示（可以随时使用）
      this.showFloatingAI = true
    },
    
    /**
     * 加载初始数据
     */
    async loadInitialData() {
      this.loading = true
      try {
        // 检查用户登录状态
        this.checkUserStatus()
        
        await this.getAttractionList()
        this.loadProvinces()
        this.loadHotAttractions()
        
        // 只有登录用户才加载购物车数量
        if (this.isLoggedIn) {
          this.loadCartCount()
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },
    
    /**
     * 获取景点列表
     */
    async getAttractionList() {
      this.loading = true
      
      try {
        // 构建查询参数 - 使用正确的字段名
        const queryParams = {}
        
        // 景点名称使用 nameFuzzy 进行模糊查询
        if (this.searchForm.name && this.searchForm.name.trim()) {
          queryParams.nameFuzzy = this.searchForm.name.trim()  // 改为 nameFuzzy
        }
        
        // 省份和城市使用精确查询
        if (this.searchForm.province) {
          queryParams.province = this.searchForm.province
        }
        if (this.searchForm.city) {
          queryParams.city = this.searchForm.city
        }
      
        // 添加默认排序
        queryParams.orderBy = 'rating DESC, created_at DESC'
        
        console.log('请求参数:', queryParams)
        
        const response = await attractionAPI.getAttractionList(queryParams)
        console.log('API响应:', response)
        
        if (response && response.data && response.data.success && response.data.code === 200) {
          this.attractionList = response.data.data || []
          this.pageParam.total = this.attractionList.length
          
          // 确保rating是数字类型
          this.attractionList.forEach(item => {
            if (item.rating) {
              item.rating = parseFloat(item.rating)
            }
          })
          
          // 为每个景点加载门票信息
          this.loadTicketsForAttractions()
        } else {
          this.$message.warning((response && response.data && response.data.message) || '获取景点列表失败')
          this.attractionList = []
          this.pageParam.total = 0
        }
        
      } catch (error) {
        console.error('获取景点列表失败:', error)
        this.$message.error('网络错误，请稍后重试')
        this.attractionList = []
        this.pageParam.total = 0
      } finally {
        this.loading = false
      }
    },
    
    /**
     * 加载省份和城市列表
     */
    loadProvinces() {
      if (this.attractionList.length > 0) {
        this.provinces = [...new Set(this.attractionList.map(item => item.province).filter(Boolean))]
        this.cities = [...new Set(this.attractionList.map(item => item.city).filter(Boolean))]
      }
    },
    
    /**
     * 加载热门景点
     */
    loadHotAttractions() {
      if (this.attractionList.length > 0) {
        this.hotAttractions = [...this.attractionList]
          .sort((a, b) => (parseFloat(b.rating) || 0) - (parseFloat(a.rating) || 0))
          .slice(0, 3)
      }
    },
    
    /**
     * 搜索景点
     */
    searchAttractions() {
      this.pageParam.currentPage = 1
      this.getAttractionList().then(() => {
        // 搜索完成后自动滚动到结果区域
        this.$nextTick(() => {
          const element = document.getElementById('attractions-list')
          if (element) {
            element.scrollIntoView({ behavior: 'smooth', block: 'start' })
          }
        })
      })
    },
    
    /**
     * 重置搜索
     */
    resetSearch() {
      this.searchForm = {
        name: '',
        province: '',
        city: '',
        token: localStorage.getItem('logintoken')
      }
      this.pageParam.currentPage = 1
      this.getAttractionList().then(() => {
        // 重置后也滚动到结果区域
        this.$nextTick(() => {
          const element = document.getElementById('attractions-list')
          if (element) {
            element.scrollIntoView({ behavior: 'smooth', block: 'start' })
          }
        })
      })
    },
    
    /**
     * 分页变更处理
     */
    handlePageChange(parm) {
      this.pageParam.currentPage = parm.currentPage
      this.pageParam.pageSize = parm.pageSize
      
      // 分页切换后滚动到列表顶部
      this.$nextTick(() => {
        const element = document.getElementById('attractions-list')
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' })
        }
      })
    },
    
    /**
     * 加载购物车数量
     */
    async loadCartCount() {
      const token = localStorage.getItem('logintoken')
      if (!token) {
        this.cartCount = 0
        return
      }
      
      try {
        // 获取当前用户ID
        const userId = this.getUserIdFromToken(token)
        if (!userId) {
          this.cartCount = 0
          return
        }
        
        // 通过用户ID查询购物车数量
        const response = await GetCartCount({ userId })
        if (response && response.success) {
          this.cartCount = response.data || 0
        } else {
          this.cartCount = 0
        }
      } catch (error) {
        console.error('获取购物车数量失败:', error)
        this.cartCount = 0
      }
    },
    
    /**
     * 为所有景点加载门票信息
     */
    async loadTicketsForAttractions() {
      const promises = this.attractionList.map(async (attraction) => {
        try {
          const response = await GetTicketsByAttractionId(attraction.id)
          if (response && response.success && response.data) {
            this.$set(this.attractionTickets, attraction.id, response.data)
          } else {
            this.$set(this.attractionTickets, attraction.id, [])
          }
        } catch (error) {
          console.error(`获取景点 ${attraction.name} 的门票失败:`, error)
          this.$set(this.attractionTickets, attraction.id, [])
        }
      })
      
      await Promise.all(promises)
    },
    
    /**
     * 更新门票选择数量
     */
    updateTicketSelection(attractionId, ticketId, quantity) {
      const key = `${attractionId}_${ticketId}`
      this.$set(this.selectedTickets, key, quantity || 0)
    },

    /**
     * 从token中获取用户ID
     * 使用auth.js中的getCurrentUser方法保持一致性
     */
    getUserIdFromToken(token) {
      const user = getCurrentUser()
      return user ? user.id : null
    },
    
    /**
     * 添加到购物车（立即预订）
     */
    async addToCart(attraction, ticket) {
      const token = localStorage.getItem('logintoken')
      if (!token) {
        this.$message.warning('请先登录后再预订')
        this.$router.push('/login')
        return
      }
      
      const quantity = this.selectedTickets[`${attraction.id}_${ticket.id}`] || 1
      
      if (quantity <= 0) {
        this.$message.warning('请选择门票数量')
        return
      }
      
      try {
        // 1. 从token中解析用户ID
        const userId = this.getUserIdFromToken(token)
        
        if (!userId) {
          this.$message.warning('无法获取用户信息，请重新登录')
          this.$router.push('/login')
          return
        }
        
        // 2. 构建购物车数据
        const cartData = {
          userId: userId,
          ticketId: ticket.id,
          quantity: quantity
        }
        
        const response = await AddToCart(cartData)
        
        if (response && response.success) {
          this.$message.success(`已添加到购物车！`)
          // 清空选择的数量
          this.$set(this.selectedTickets, `${attraction.id}_${ticket.id}`, 0)
          // 更新购物车数量
          this.loadCartCount()
        } else {
          this.$message.error(response.message || '添加到购物车失败')
        }
      } catch (error) {
        console.error('添加到购物车失败:', error)
        this.$message.error('网络错误，请稍后重试')
      }
    },
    
    /**
     * 检查用户登录状态
     */
    checkUserStatus() {
      this.isLoggedIn = isLoggedIn()
      if (this.isLoggedIn) {
        this.currentUser = getCurrentUser()
        this.isAdmin = checkIsAdmin()
      } else {
        this.currentUser = null
        this.isAdmin = false
      }
    },
    
    /**
     * 跳转到登录页面
     */
    goToLogin() {
      this.$router.push('/login')
    },
    
    /**
     * 处理用户下拉菜单命令
     */
    handleUserCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/profile')
          break
        case 'cart':
          this.goToCart()
          break
        case 'orders':
          this.$router.push('/orders')
          break
        case 'admin':
          this.$router.push('/admin/Ticket')
          break
        case 'logout':
          this.handleLogout()
          break
        default:
          break
      }
    },
    
    /**
     * 处理退出登录
     */
    async handleLogout() {
      this.$confirm('确定要退出登录吗？', '退出确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // 调用后端退出登录接口
          await loginout()
        } catch (error) {
          console.error('退出登录失败:', error)
        } finally {
          // 清除本地存储的用户信息
          localStorage.removeItem('logintoken')
          localStorage.removeItem('userdata')
          localStorage.removeItem('userInfo')
          
          // 更新用户状态
          this.checkUserStatus()
          this.cartCount = 0
          
          this.$message.success('已退出登录')
          
          // 刷新页面以清除所有状态
          this.$router.go(0)
        }
      }).catch(() => {
        this.$message.info('已取消退出')
      })
    },
    
    /**
     * 跳转到购物车页面
     */
    goToCart() {
      if (!isLoggedIn()) {
        // 保存当前路径，登录后跳转回来
        saveRedirectPath('/cart')
        this.$message.warning('请先登录后再查看购物车')
        this.$router.push('/login')
        return
      }

      // 获取当前用户ID
      const userId = this.getUserIdFromToken(localStorage.getItem('logintoken'))
      if (userId) {
        // 传递用户ID作为查询参数
        this.$router.push({
          path: '/cart',
          query: { userId: userId }
        })
      } else {
        this.$message.warning('无法获取用户信息，请重新登录')
        this.$router.push('/login')
      }
    },

    /**
     * 跳转到AI聊天页面
     */
    goToAIChat() {
      // AI聊天功能对登录状态不做限制，用户可以直接使用
      this.$router.push('/charts/mychart')
    },
    

    /**
     * 获取列表标题
     */
    getListTitle() {
      if (this.searchForm.name) {
        return `"${this.searchForm.name}" 的搜索结果`
      }
      if (this.searchForm.province || this.searchForm.city) {
        return `${this.searchForm.province || ''}${this.searchForm.city || ''} 的景点`
      }
      return '全部景点'
    }
  }
}
</script>


<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* 头部横幅区域 */
.banner-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60px 0;
  text-align: center;
  color: white;
}

.banner-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
}

.banner-title {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.3);
}

.banner-subtitle {
  font-size: 18px;
  opacity: 0.9;
}

/* 用户区域样式 */
.user-area {
  position: absolute;
  top: 20px;
  right: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: all 0.3s ease;
}

.user-area.floating {
  position: fixed;
  top: 15px;
  right: 20px;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 25px;
  padding: 8px 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  width: auto;
  min-width: 80px;
  /* 移除cursor: pointer，让子元素处理点击事件 */
}

/* 浮动购物车样式 */
.floating-cart {
  position: fixed;
  right: 30px;
  bottom: 30px;
  z-index: 1000;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.3s ease;
  cursor: pointer;
}

/* 当用户区域浮动时，调整按钮位置 */
.floating-ai.show + .floating-cart {
  bottom: 30px; /* 购物车按钮位置不变 */
}

.floating-cart.show {
  opacity: 1;
  transform: translateY(0);
}

.floating-cart .cart-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  border: 3px solid rgba(255, 255, 255, 0.3);
  position: relative;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.floating-cart .cart-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.4);
}

.floating-cart .cart-icon i {
  font-size: 24px;
  color: white;
  position: relative;
}

.floating-cart .cart-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ff4757;
  color: white;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  min-width: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

/* 浮动AI助手样式 */
.floating-ai {
  position: fixed;
  right: 30px;
  bottom: 120px; /* 位于购物车按钮上方，留出用户区域空间 */
  z-index: 1000;
  cursor: pointer;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.3s ease;
  animation: ai-float 3s ease-in-out infinite; /* 添加浮动动画 */
}

.floating-ai.show {
  opacity: 1;
  transform: translateY(0);
}

.floating-ai .ai-icon {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  border: 3px solid rgba(255, 255, 255, 0.3);
  position: relative;
  box-shadow: 0 8px 25px rgba(238, 90, 36, 0.3);
}

.floating-ai .ai-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 12px 35px rgba(238, 90, 36, 0.4);
}

.floating-ai .ai-icon i {
  font-size: 24px;
  color: white;
  position: relative;
  animation: ai-pulse 2s ease-in-out infinite; /* 添加图标脉动效果 */
}

@keyframes ai-float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes ai-pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.05);
  }
}

.login-section {
  display: flex;
  align-items: center;
}

.user-section {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 25px;
  padding: 8px 15px;
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

/* 浮动模式下的用户区域样式 */
.user-area.floating .user-info {
  background: transparent;
  border: none;
  padding: 0;
  backdrop-filter: none;
}

.floating-dropdown {
  width: 100%;
  height: 100%;
}

.floating-user-trigger {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 20px;
  transition: background-color 0.3s ease;
  width: 100%;
  height: 100%;
  position: relative;
  z-index: 10;
  box-sizing: border-box;
}

.floating-user-trigger:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.floating-user-trigger:active {
  background-color: rgba(0, 0, 0, 0.1);
  transform: scale(0.98);
}

/* 购物车徽章样式 */
.cart-badge {
  display: inline-block;
  background: #ff4757;
  color: white;
  border-radius: 10px;
  padding: 1px 6px;
  font-size: 11px;
  font-weight: bold;
  margin-left: 5px;
  min-width: 16px;
  text-align: center;
  line-height: 16px;
  height: 16px;
  animation: cart-badge-pulse 2s infinite;
}

@keyframes cart-badge-pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.user-dropdown-icon {
  color: #666;
  font-size: 14px;
  transition: transform 0.3s ease;
}

.floating-user-trigger:hover .user-dropdown-icon {
  transform: translateY(2px);
}

.user-avatar {
  margin-right: 8px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* 浮动模式下不显示悬停效果 */
.user-area.floating .user-info:hover {
  background: transparent;
  transform: none;
}

.username {
  color: white;
  font-size: 14px;
  font-weight: 500;
  margin-right: 8px;
}

.user-dropdown {
  color: white;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  color: #f0f0f0;
}


/* 容器样式 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 热门景点区域 */
.hot-section {
  padding: 60px 0;
  background: white;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin: 0 0 8px 0;
}

.section-subtitle {
  font-size: 16px;
  color: #666;
  margin: 0;
}

.hot-attractions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.hot-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 25px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.hot-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 35px rgba(0,0,0,0.15);
}

.hot-card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.hot-card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.hot-card:hover .hot-card-image img {
  transform: scale(1.1);
}

.hot-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: #ff6b6b;
  color: white;
  padding: 4px 10px;
  border-radius: 15px;
  font-size: 11px;
  font-weight: 700;
}

.hot-card-content {
  padding: 20px;
}

.hot-card-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.hot-card-location {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.hot-card-rating {
  margin-top: 8px;
}

/* 搜索筛选区域 */
.search-filter-section {
  padding: 30px 0;
  background: #fff;
  border-bottom: 2px solid #f0f0f0;
}

.search-area {
  text-align: center;
}

.search-title {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
}

.search-form {
  justify-content: center;
  margin: 0;
}

.search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 15px;
}

/* 景点列表区域 */
.attractions-section {
  padding: 40px 0 80px;
  background: #f8f9fa;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.list-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.list-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 5px;
}

.total-count,
.current-page-info {
  color: #666;
  font-size: 14px;
}

.attractions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 25px;
}

.attraction-card {
  background: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  transition: all 0.3s ease;
}

.attraction-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.attraction-card:hover .card-image img {
  transform: scale(1.05);
}

.card-content {
  padding: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 10px 0;
}

.card-location {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 13px;
  margin-bottom: 8px;
}

.card-description {
  color: #666;
  font-size: 13px;
  line-height: 1.5;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-rating {
  margin-bottom: 10px;
}

.card-details {
  margin-bottom: 12px;
}

.card-hours,
.card-address {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 12px;
  margin-bottom: 4px;
}

.card-footer {
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
}

.action-buttons {
  display: flex;
  justify-content: center;
}

/* 门票信息样式 */
.tickets-section {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.tickets-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin: 0 0 10px 0;
}

.tickets-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.ticket-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.ticket-info {
  flex: 1;
}

.ticket-type {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.ticket-price {
  margin-bottom: 4px;
}

.current-price {
  font-size: 14px;
  font-weight: 600;
  color: #e74c3c;
}

.original-price {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
  margin-left: 8px;
}

.ticket-description {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.ticket-stock {
  font-size: 11px;
  color: #999;
}

.ticket-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.no-tickets {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 13px;
}

/* 加载和空状态样式 */
.loading-container {
  padding: 60px 0;
  text-align: center;
}

.loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.loading-icon {
  font-size: 36px;
  color: #667eea;
}

.no-results {
  padding: 60px 0;
  text-align: center;
}

.pagination-wrapper {
  margin-top: 40px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner-title {
    font-size: 32px;
  }

  .user-area {
    top: 15px;
    right: 15px;
    gap: 10px;
  }

  .user-area.floating {
    top: 10px;
    right: 15px;
    padding: 6px 12px;
    min-width: 60px;
  }

  .floating-user-trigger {
    padding: 2px 6px;
  }

  .user-info {
    padding: 6px 12px;
  }

  .username {
    font-size: 13px;
  }
  
  .floating-cart {
    right: 20px;
    bottom: 20px;
  }

  .floating-cart .cart-icon {
    width: 50px;
    height: 50px;
  }

  .floating-cart .cart-icon i {
    font-size: 20px;
  }

  .floating-ai {
    right: 20px;
    bottom: 90px; /* 在移动端调整位置，避免与用户区域重叠 */
  }

  .floating-ai .ai-icon {
    width: 50px;
    height: 50px;
  }

  .floating-ai .ai-icon i {
    font-size: 20px;
  }
  
  .search-form {
    flex-direction: column;
    align-items: center;
  }
  
  .search-form .el-form-item {
    margin-right: 0;
    margin-bottom: 15px;
  }
  
  .list-header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
  
  .list-info {
    align-items: center;
  }
  
  .hot-attractions {
    grid-template-columns: 1fr;
  }
  
  .attractions-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .banner-section {
    padding: 40px 0;
  }
  
  .hot-section,
  .attractions-section {
    padding: 40px 0;
  }
  
  .container {
    padding: 0 15px;
  }
  
  .banner-title {
    font-size: 28px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>

