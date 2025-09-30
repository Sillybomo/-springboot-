<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>购物车管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 搜索筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="cart-search" label-width="100px">
      <el-form-item label="景点名称：">
        <el-input size="small" v-model="searchForm.attractionName" placeholder="输入景点名称"></el-input>
      </el-form-item>
      <el-form-item label="添加时间：">
        <el-date-picker
          size="small"
          v-model="searchForm.createTimeRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          clearable
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchCart">搜索</el-button>
        <el-button size="small" type="default" icon="el-icon-refresh" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 批量操作区域 -->
    <div style="margin-bottom: 20px;">
      <el-button size="small" type="primary" icon="el-icon-shopping-cart-2" :disabled="selectedItems.length === 0" @click="batchCheckout">
        批量结算（{{ selectedItems.length }}项）
      </el-button>
      <el-button size="small" type="danger" icon="el-icon-delete" :disabled="selectedItems.length === 0" @click="batchDelete">
        批量删除（{{ selectedItems.length }}项）
      </el-button>
      <el-button size="small" type="warning" icon="el-icon-circle-close" @click="clearAll">
        清空购物车
      </el-button>
    </div>

    <!-- 购物车表格列表 -->
    <el-table
      size="small"
      :data="cartList"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="加载购物车中..."
      style="width: 100%; margin-bottom: 20px;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="50"></el-table-column>
      <!-- 序号列 -->
      <el-table-column sortable label="序号" width="70">
        <template slot-scope="scope">
          {{ (pageParam.currentPage - 1) * pageParam.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <!-- 景点图片（通过关联查询获取） -->
      <el-table-column label="景点图片" width="100" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 60px; height: 45px; border-radius: 4px;"
            :src="getAttractionImageUrl(scope.row.attraction && scope.row.attraction.imageUrl)"
            :alt="scope.row.attraction && scope.row.attraction.name"
            fit="cover"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <!-- 景点信息（通过关联查询获取） -->
      <el-table-column sortable label="景点名称" width="150">
        <template slot-scope="scope">
          <div style="font-weight: 600; color: #333;">
            {{ scope.row.attraction ? scope.row.attraction.name : '未知景点' }}
          </div>
          <div style="font-size: 12px; color: #666; margin-top: 4px;" v-if="scope.row.attraction">
            <i class="el-icon-location-outline"></i>
            {{ scope.row.attraction.city }}, {{ scope.row.attraction.province }}
          </div>
        </template>
      </el-table-column>
      <el-table-column sortable label="景点评分" width="120">
        <template slot-scope="scope">
          <el-rate 
            :value="scope.row.attraction ? parseFloat(scope.row.attraction.rating) : 0" 
            disabled 
            show-score 
            text-color="#ff9900"
            :max="5"
            style="display: flex; align-items: center;"
            v-if="scope.row.attraction && scope.row.attraction.rating"
          ></el-rate>
          <span v-else style="color: #999; font-size: 12px;">暂无评分</span>
        </template>
      </el-table-column>
      <!-- 门票价格（通过关联查询获取） -->
      <el-table-column sortable label="门票价格" width="100">
        <template slot-scope="scope">
          <span style="color: #f56c6c; font-weight: 600;">
            ¥{{ scope.row.ticket ? scope.row.ticket.price.toFixed(2) : '0.00' }}
          </span>
        </template>
      </el-table-column>
      <!-- 购票数量（实体类字段） -->
      <el-table-column sortable prop="quantity" label="数量" width="120">
        <template slot-scope="scope">
          <el-input-number
            v-model="scope.row.quantity"
            :min="1"
            :max="99"
            size="mini"
            @change="updateQuantity(scope.row)"
          ></el-input-number>
        </template>
      </el-table-column>
      <!-- 小计金额（计算得出） -->
      <el-table-column sortable label="小计金额" width="100">
        <template slot-scope="scope">
          <span style="color: #f56c6c; font-weight: 600; font-size: 14px;">
            ¥{{ getItemTotal(scope.row).toFixed(2) }}
          </span>
        </template>
      </el-table-column>
      <!-- 门票类型（通过关联查询获取） -->
      <el-table-column sortable label="门票类型" width="100">
        <template slot-scope="scope">
          <el-tag size="mini" :type="getTicketTypeTag(scope.row.ticket)">
            {{ scope.row.ticket ? scope.row.ticket.ticketType : '未知类型' }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 加入时间（实体类字段createdAt） -->
      <el-table-column sortable prop="createdAt" label="添加时间" width="100">
        <template slot-scope="scope">{{ normalizeToYMD(scope.row.createdAt) }}</template>
      </el-table-column>
      <!-- 更新时间（实体类字段updatedAt） -->
      <el-table-column sortable prop="updatedAt" label="更新时间" width="100">
        <template slot-scope="scope">{{ normalizeToYMD(scope.row.updatedAt) }}</template>
      </el-table-column>
      <!-- 景点描述（通过关联查询获取） -->
      <el-table-column label="景点描述" width="200">
        <template slot-scope="scope">
          <div class="description-text">
            {{ scope.row.attraction ? (scope.row.attraction.description || '暂无介绍') : '暂无介绍' }}
          </div>
        </template>
      </el-table-column>
      <!-- 操作列 -->
      <el-table-column align="center" label="操作" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-shopping-cart-2"
            @click="handleCheckout(scope.row)"
          >
            结算
          </el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDeleteItem(scope.row)"
          >
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 购物车统计信息 -->
    <div class="cart-summary" v-if="cartList.length > 0">
      <div class="summary-item">
        <span>总计商品：<strong>{{ getTotalItems() }}</strong> 件</span>
      </div>
      <div class="summary-item">
        <span>总计金额：<strong style="color: #f56c6c; font-size: 18px;">¥{{ getTotalAmount().toFixed(2) }}</strong></span>
      </div>
    </div>

    <!-- 空购物车状态 -->
    <div v-if="!loading && cartList.length === 0" class="empty-cart">
      <el-empty description="购物车还是空的">
        <el-button type="primary" @click="goShopping">去逛逛</el-button>
      </el-empty>
    </div>

    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageParam" @callFather="handlePageChange" v-if="cartList.length > 0"></Pagination>

    <!-- 结算确认弹窗 -->
    <el-dialog
      title="订单结算确认"
      :visible.sync="checkoutDialogVisible"
      width="50%"
      :before-close="closeCheckoutDialog"
    >
      <div class="checkout-content">
        <h4>订单明细：</h4>
        <el-table :data="checkoutItems" size="small" border style="margin-bottom: 20px;">
          <el-table-column label="景点名称" width="180">
            <template slot-scope="scope">
              {{ scope.row.attraction ? scope.row.attraction.name : '未知景点' }}
            </template>
          </el-table-column>
          <el-table-column label="单价" width="100">
            <template slot-scope="scope">
              ¥{{ scope.row.ticket ? scope.row.ticket.price.toFixed(2) : '0.00' }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
          <el-table-column label="小计" width="100">
            <template slot-scope="scope">
              ¥{{ getItemTotal(scope.row).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="门票类型" width="100">
            <template slot-scope="scope">
              {{ scope.row.ticket ? scope.row.ticket.ticketType : '未知类型' }}
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 全局联系信息 -->
        <div class="global-contact-info">
          <h4>联系信息（将应用到所有订单）：</h4>
          <el-form label-width="120px" :model="checkoutForm" :rules="checkoutRules" ref="checkoutForm">
            <el-form-item label="联系人姓名" prop="contactName">
              <el-input v-model="checkoutForm.contactName" placeholder="请输入联系人姓名"></el-input>
            </el-form-item>
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="checkoutForm.contactPhone" placeholder="请输入联系电话"></el-input>
            </el-form-item>
          </el-form>
        </div>

        <!-- 订单详细信息 -->
        <div class="order-details">
          <h4>订单详细信息：</h4>
          <div v-for="(item, index) in checkoutItems" :key="item.id" class="order-item">
            <div class="order-header">
              <span class="order-title">{{ item.attraction ? item.attraction.name : '未知景点' }}</span>
              <span class="order-price">¥{{ getItemTotal(item).toFixed(2) }}</span>
            </div>
            
            <el-form :model="item.orderForm" :rules="orderItemRules" :ref="`orderForm${index}`" label-width="100px" size="small">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="参观日期" :prop="`visitDate`">
                    <el-date-picker
                      v-model="item.orderForm.visitDate"
                      type="date"
                      placeholder="选择参观日期"
                      value-format="yyyy-MM-dd"
                      style="width: 100%;"
                    >
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="门票类型">
                    <el-input :value="item.ticket ? item.ticket.ticketType : '未知类型'" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-form-item label="订单备注">
                <el-input
                  type="textarea"
                  rows="2"
                  v-model="item.orderForm.remark"
                  placeholder="请输入该订单的备注信息（可选）"
                ></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="checkout-total">
          <span style="font-size: 16px;">订单总金额：<strong style="color: #f56c6c; font-size: 20px;">¥{{ getCheckoutTotal().toFixed(2) }}</strong></span>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeCheckoutDialog">取消</el-button>
        <el-button size="small" type="primary" :loading="checkoutLoading" @click="submitCheckout">确认下单</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination'
import { CartList, CartDelete, CartUpdate, CartBatchDelete, CartClear } from "@/api/cart.js"
import { CreateOrder } from "@/api/order.js"
import { getCurrentUser } from '@/utils/auth'
import { getAttractionImageUrl } from '@/utils/image'

export default {
  name: "Cart",
  
  components: { Pagination },
  data() {
    return {
      loading: false,
      checkoutLoading: false,
      checkoutDialogVisible: false,
      cartList: [],
      selectedItems: [],
      checkoutItems: [],
      
      // 搜索表单
      searchForm: {
        attractionName: "",
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      },
      
      pageParam: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      
      // 结算表单
      checkoutForm: {
        contactName: "",
        contactPhone: "",
        token: localStorage.getItem('logintoken')
      },
      
      checkoutRules: {
        contactName: [
          { required: true, message: '请输入联系人姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '姓名长度在2到20个字符', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      
      // 订单项目表单验证规则
      orderItemRules: {
        visitDate: [
          { required: true, message: '请选择参观日期', trigger: 'change' }
        ],
        remark: [
          { max: 200, message: '备注不能超过200个字符', trigger: 'blur' }
        ]
      },
      
      // 默认图片
      defaultImage: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400'
    };
  },
  
  created() {
    this.getCartList();
  },
  
  methods: {
    /**
     * 从token中获取用户ID
     * 使用auth.js中的getCurrentUser方法保持一致性
     */
    getUserIdFromToken() {
      const user = getCurrentUser()
      return user ? user.id : null
    },
    
    /**
     * 获取景点图片URL
     */
    getAttractionImageUrl(imageUrl) {
      return getAttractionImageUrl(imageUrl)
    },
    
    /**
     * 格式化购物车项目数据（处理多表联查返回的数据结构）
     */
    formatCartItem(item) {
      return {
        id: item.cartId,
        userId: item.userId,
        ticketId: item.ticketId,
        quantity: item.quantity,
        createdAt: item.createdAt,
        updatedAt: item.updatedAt,
        // 景点信息
        attraction: {
          id: item.attractionId,
          name: item.attractionName,
          imageUrl: item.attractionImageUrl,
          city: item.attractionCity,
          province: item.attractionProvince,
          rating: item.attractionRating,
          description: item.attractionDescription
        },
        // 门票信息
        ticket: {
          id: item.ticketId,
          ticketType: item.ticketType,
          price: item.ticketPrice,
          discountPrice: item.ticketDiscountPrice,
          description: item.ticketDescription,
          stock: item.ticketStock
        }
      }
    },
    
    /**
     * 获取购物车列表（包含关联的景点和门票信息）
     */
    getCartList() {
      this.loading = true;
      
      // 获取用户ID（从路由参数或localStorage）
      const userId = this.$route.query.userId || this.getUserIdFromToken();
      
      const params = {
        ...this.searchForm,
        userId: userId, // 添加用户ID参数
        needJoin: true, // 启用多表联查
        currentPage: this.pageParam.currentPage,
        pageSize: this.pageParam.pageSize,
        startTime: this.searchForm.createTimeRange[0] || "",
        endTime: this.searchForm.createTimeRange[1] || ""
      };
      if (!params.startTime) delete params.startTime;
      if (!params.endTime) delete params.endTime;
      delete params.createTimeRange;

      CartList(params)
        .then(res => {
          this.loading = false;
          if (res.success && res.data) {
            // 处理多表联查返回的数据
            if (Array.isArray(res.data)) {
              // 直接返回数组的情况
              this.cartList = res.data.map(item => this.formatCartItem(item));
            } else if (res.data.list) {
              // 分页返回的情况
              this.cartList = res.data.list.map(item => this.formatCartItem(item));
              this.pageParam.total = res.data.total || 0;
              this.pageParam.currentPage = res.data.current || 1;
            } else {
              this.cartList = [];
            }
          } else {
            this.$message.warning(res.message || "获取购物车列表失败");
          }
        })
        .catch(err => {
          this.loading = false;
          console.error("获取购物车失败：", err);
          this.$message.error("购物车加载失败，请稍后再试");
        });
    },

    /**
     * 搜索购物车
     */
    searchCart() {
      this.pageParam.currentPage = 1;
      this.getCartList();
    },

    /**
     * 重置搜索条件
     */
    resetSearch() {
      this.searchForm = {
        attractionName: "",
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      };
      this.pageParam.currentPage = 1;
      this.getCartList();
    },

    /**
     * 分页变更
     */
    handlePageChange(parm) {
      this.pageParam.currentPage = parm.currentPage;
      this.pageParam.pageSize = parm.pageSize;
      this.getCartList();
    },

    /**
     * 选择项变更
     */
    handleSelectionChange(selection) {
      this.selectedItems = selection;
    },

    /**
     * 更新数量（使用实体类字段）
     */
    updateQuantity(row) {
      const params = {
        quantity: row.quantity,
        token: localStorage.getItem('logintoken')
      };
      
      CartUpdate(row.id, params)
        .then(res => {
          if (res.success) {
            this.$message.success("数量更新成功");
            // 更新本地数据的updatedAt字段
            row.updatedAt = new Date();
          } else {
            this.$message.warning(res.message || "数量更新失败");
            this.getCartList(); // 重新加载数据
          }
        })
        .catch(err => {
          console.error("更新数量失败：", err);
          this.$message.error("数量更新失败，请稍后再试");
          this.getCartList(); // 重新加载数据
        });
    },

    /**
     * 获取单项小计金额
     */
    getItemTotal(row) {
      if (!row.ticket || !row.ticket.price) return 0;
      return row.ticket.price * row.quantity;
    },

    /**
     * 获取门票类型标签样式
     */
    getTicketTypeTag(ticket) {
      if (!ticket) return 'info';
      const typeMap = {
        '成人票': 'success',
        '学生票': 'primary',
        '儿童票': 'warning',
        '老人票': 'info'
      };
      return typeMap[ticket.ticketType] || 'default';
    },

    /**
     * 单项结算
     */
    handleCheckout(row) {
      this.checkoutItems = [this.prepareCheckoutItem(row)];
      this.loadUserInfo();
      this.checkoutDialogVisible = true;
    },

    /**
     * 批量结算
     */
    batchCheckout() {
      if (this.selectedItems.length === 0) {
        this.$message.warning("请选择要结算的商品");
        return;
      }
      this.checkoutItems = this.selectedItems.map(item => this.prepareCheckoutItem(item));
      this.loadUserInfo();
      this.checkoutDialogVisible = true;
    },

    /**
     * 准备结算项目数据
     */
    prepareCheckoutItem(item) {
      return {
        ...item,
        orderForm: {
          visitDate: '',
          remark: ''
        }
      }
    },

    /**
     * 加载用户信息并回显
     */
    loadUserInfo() {
      const user = getCurrentUser()
      if (user) {
        this.checkoutForm.contactName = user.name || user.username || ''
        this.checkoutForm.contactPhone = user.phone || user.contactPhone || ''
      }
    },

    /**
     * 提交结算 - 创建订单
     */
    async submitCheckout() {
      // 验证全局联系信息表单
      const globalValid = await this.$refs.checkoutForm.validate().catch(() => false)
      if (!globalValid) return

      // 验证每个订单项目的表单
      const orderFormValidations = this.checkoutItems.map((item, index) => {
        return this.$refs[`orderForm${index}`][0].validate().catch(() => false)
      })
      
      const orderFormResults = await Promise.all(orderFormValidations)
      if (orderFormResults.some(valid => !valid)) {
        this.$message.warning('请填写完整的订单信息')
        return
      }

      this.checkoutLoading = true
      
      try {
        // 获取当前用户ID
        const userId = this.getUserIdFromToken()
        if (!userId) {
          this.$message.warning('无法获取用户信息，请重新登录')
          this.checkoutLoading = false
          return
        }

        // 为每个购物车项目创建订单
        const orderPromises = this.checkoutItems.map(async (item) => {
          // 生成订单号
          const orderNo = this.generateOrderNo()
          
          // 计算总金额
          const totalAmount = this.getItemTotal(item)
          
          // 构建订单数据，使用每个订单的个性化信息
          const orderData = {
            orderNo: orderNo,
            userId: userId,
            ticketId: item.ticketId,
            quantity: item.quantity,
            totalAmount: totalAmount,
            status: 'UNPAID', // 未支付状态
            contactName: this.checkoutForm.contactName, // 全局联系信息
            contactPhone: this.checkoutForm.contactPhone, // 全局联系信息
            visitDate: item.orderForm.visitDate, // 个性化参观日期
            remark: item.orderForm.remark || `景点：${item.attraction ? item.attraction.name : '未知景点'}，门票类型：${item.ticket ? item.ticket.ticketType : '未知类型'}` // 个性化备注
          }
          
          return CreateOrder(orderData)
        })

        // 等待所有订单创建完成
        const results = await Promise.all(orderPromises)
        
        // 检查是否所有订单都创建成功
        const failedOrders = results.filter(res => !res.success)
        if (failedOrders.length > 0) {
          this.$message.error(`有 ${failedOrders.length} 个订单创建失败`)
          this.checkoutLoading = false
          return
        }

        // 所有订单创建成功，删除购物车中的对应项目
        const cartIds = this.checkoutItems.map(item => item.id)
        await this.deleteCartItems(cartIds)

        this.$message.success(`订单创建成功！共创建 ${results.length} 个订单`)
        this.checkoutDialogVisible = false
        this.getCartList() // 重新加载购物车
        this.selectedItems = [] // 清空选择
        
      } catch (error) {
        console.error('创建订单失败:', error)
        this.$message.error('创建订单失败，请稍后再试')
      } finally {
        this.checkoutLoading = false
      }
    },

    /**
     * 生成订单号
     */
    generateOrderNo() {
      const now = new Date()
      const timestamp = now.getTime()
      const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      return `ORD${timestamp}${random}`
    },

    /**
     * 删除购物车项目
     */
    async deleteCartItems(cartIds) {
      try {
        const deletePromises = cartIds.map(id => CartDelete(id))
        await Promise.all(deletePromises)
      } catch (error) {
        console.error('删除购物车项目失败:', error)
        // 即使删除失败也不影响订单创建，只记录错误
      }
    },

    /**
     * 关闭结算弹窗
     */
    closeCheckoutDialog() {
      this.checkoutDialogVisible = false;
      this.checkoutItems = [];
      this.$nextTick(() => {
        this.$refs.checkoutForm.resetFields();
      });
    },

    /**
     * 删除单项（使用实体类主键id）
     */
    handleDeleteItem(row) {
      const attractionName = row.attraction ? row.attraction.name : '该商品';
      this.$confirm(`确定要将【${attractionName}】从购物车中移除吗？`, "移除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          CartDelete(row.id)
            .then(res => {
              if (res.success) {
                this.$message.success("移除成功");
                this.getCartList();
              } else {
                this.$message.warning(res.message || "移除失败");
              }
            })
            .catch(err => {
              console.error("删除购物车项失败：", err);
              this.$message.error("移除失败，请稍后再试");
            });
        })
        .catch(() => {
          this.$message.info("已取消移除");
        });
    },

    /**
     * 批量删除
     */
    batchDelete() {
      if (this.selectedItems.length === 0) {
        this.$message.warning("请选择要删除的商品");
        return;
      }

      this.$confirm(`确定要删除选中的 ${this.selectedItems.length} 个商品吗？`, "批量删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const ids = this.selectedItems.map(item => item.id);
          
          CartBatchDelete({ ids, token: localStorage.getItem('logintoken') })
            .then(res => {
              if (res.success) {
                this.$message.success("批量删除成功");
                this.getCartList();
                this.selectedItems = [];
              } else {
                this.$message.warning(res.message || "批量删除失败");
              }
            })
            .catch(err => {
              console.error("批量删除失败：", err);
              this.$message.error("批量删除失败，请稍后再试");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    /**
     * 清空购物车
     */
    clearAll() {
      if (this.cartList.length === 0) {
        this.$message.info("购物车已经是空的");
        return;
      }

      this.$confirm("确定要清空整个购物车吗？此操作不可恢复！", "清空购物车", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          CartClear({ token: localStorage.getItem('logintoken') })
            .then(res => {
              if (res.success) {
                this.$message.success("购物车已清空");
                this.getCartList();
                this.selectedItems = [];
              } else {
                this.$message.warning(res.message || "清空失败");
              }
            })
            .catch(err => {
              console.error("清空购物车失败：", err);
              this.$message.error("清空失败，请稍后再试");
            });
        })
        .catch(() => {
          this.$message.info("已取消清空");
        });
    },

    /**
     * 去购物
     */
    goShopping() {
      this.$router.push('/');
    },

    /**
     * 获取总商品数量
     */
    getTotalItems() {
      return this.cartList.reduce((total, item) => total + item.quantity, 0);
    },

    /**
     * 获取总金额
     */
    getTotalAmount() {
      return this.cartList.reduce((total, item) => total + this.getItemTotal(item), 0);
    },

    /**
     * 获取结算总金额
     */
    getCheckoutTotal() {
      return this.checkoutItems.reduce((total, item) => total + this.getItemTotal(item), 0);
    },

    /**
     * 日期格式化工具
     */
    normalizeToYMD(value) {
      if (!value) return "";
      if (typeof value === "number") {
        const timestamp = value.toString().length === 10 ? value * 1000 : value;
        const d = new Date(timestamp);
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
      }
      if (Object.prototype.toString.call(value) === "[object Date]") {
        const d = value;
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
      }
      if (typeof value === "string") {
        return value.length >= 10 ? value.substring(0, 10) : value;
      }
      return "";
    }
  }
};
</script>

<style scoped>
.cart-search {
  margin-top: 10px;
  margin-bottom: 20px;
}

.description-text {
  max-width: 180px;
  line-height: 1.4;
  font-size: 12px;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.cart-summary {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 30px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
}

.summary-item {
  font-size: 14px;
  color: #333;
}

.empty-cart {
  text-align: center;
  padding: 60px 0;
}

.checkout-content h4 {
  color: #333;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
}

.global-contact-info {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
}

.order-details {
  margin-bottom: 20px;
}

.order-item {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  margin-bottom: 15px;
  padding: 15px;
  background: #fff;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.order-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.order-price {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

.checkout-total {
  text-align: right;
  padding: 15px 0;
  border-top: 1px solid #e9ecef;
  margin-top: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.el-dialog__body .el-form-item {
  margin-bottom: 15px;
}

.order-item .el-form-item {
  margin-bottom: 10px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 20px;
}
</style>
