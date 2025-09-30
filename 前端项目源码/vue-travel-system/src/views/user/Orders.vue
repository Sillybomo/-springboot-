<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的订单</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 搜索筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="order-search" label-width="100px">
      <el-form-item label="订单号：">
        <el-input size="small" v-model="searchForm.orderNo" placeholder="输入订单号"></el-input>
      </el-form-item>
      <el-form-item label="订单状态：">
        <el-select size="small" v-model="searchForm.status" placeholder="选择订单状态" clearable>
          <el-option label="全部状态" value=""></el-option>
          <el-option label="未支付" value="UNPAID"></el-option>
          <el-option label="已支付" value="PAID"></el-option>
          <el-option label="已发货" value="DELIVERED"></el-option>
          <el-option label="已完成" value="COMPLETED"></el-option>
          <el-option label="已取消" value="CANCELLED"></el-option>
          <el-option label="退票中" value="REFUND"></el-option>
          <el-option label="退票被拒" value="REFUND_REJECTED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间：">
        <el-date-picker
          size="small"
          v-model="searchForm.createTimeRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="getOrderList">搜索</el-button>
        <el-button size="small" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 批量操作区域 -->
    <div style="margin-bottom: 20px;">
      <el-button size="small" type="primary" icon="el-icon-refresh" @click="getOrderList">
        刷新列表
      </el-button>
    </div>

    <!-- 订单表格列表 -->
    <el-table
      :data="orderList"
      v-loading="loading"
      border
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      
      <el-table-column label="订单号" width="180" >
        <template slot-scope="scope">
          <el-link type="primary" @click="viewOrderDetail(scope.row)">
            {{ scope.row.orderNo }}
          </el-link>
        </template>
      </el-table-column>
      
      <el-table-column label="景点信息" width="200">
        <template slot-scope="scope">
          <div class="attraction-info">
            <div class="attraction-name">{{ scope.row.attraction ? scope.row.attraction.name : '未知景点' }}</div>
            <div class="ticket-type">{{ scope.row.ticket ? scope.row.ticket.ticketType : '未知类型' }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="订单状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="门票图片" width="100" align="center">
        <template slot-scope="scope">
          <el-image
            :src="getAttractionImageUrl(scope.row.attraction ? scope.row.attraction.imageUrl : null)"
            :preview-src-list="[getAttractionImageUrl(scope.row.attraction ? scope.row.attraction.imageUrl : null)]"
            fit="cover"
            style="width: 60px; height: 40px; border-radius: 4px;"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      
      <el-table-column label="单价" width="100" align="center">
        <template slot-scope="scope">
          ¥{{ scope.row.ticket ? scope.row.ticket.price.toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      
      <el-table-column prop="quantity" label="数量" width="80" align="center"></el-table-column>
      
      <el-table-column label="总金额" width="120" align="center">
        <template slot-scope="scope">
          <span class="total-amount">¥{{ scope.row.totalAmount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      

      
      <el-table-column label="联系人" width="120">
        <template slot-scope="scope">
          <div>{{ scope.row.contactName }}</div>
          <div class="contact-phone">{{ scope.row.contactPhone }}</div>
        </template>
      </el-table-column>
      
      <el-table-column label="参观日期" width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.visitDate || '-' }}
        </template>
      </el-table-column>
      
      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="300" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-view"
            @click="viewOrderDetail(scope.row)"
          >
            详情
          </el-button>
          <el-button
            v-if="scope.row.status === 'UNPAID'"
            size="mini"
            type="success"
            icon="el-icon-money"
            @click="payOrder(scope.row)"
          >
            支付
          </el-button>
           <el-button
             v-if="scope.row.status === 'UNPAID'"
             size="mini"
             type="danger"
             icon="el-icon-close"
             @click="cancelOrder(scope.row)"
           >
             取消
           </el-button>
           <el-button
             v-if="scope.row.status === 'PAID'"
             size="mini"
             type="warning"
             icon="el-icon-refresh-left"
             @click="applyRefund(scope.row)"
           >
             申请退票
           </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageParam" @callFather="handlePageChange" v-if="orderList.length > 0"></Pagination>

    <!-- 空状态 -->
    <div v-if="orderList.length === 0 && !loading" class="empty-orders">
      <el-empty description="暂无订单数据">
        <el-button type="primary" @click="goToHome">去首页逛逛</el-button>
      </el-empty>
    </div>

    <!-- 订单详情弹窗 -->
    <el-dialog
      title="订单详情"
      :visible.sync="detailDialogVisible"
      width="60%"
      :before-close="closeDetailDialog"
    >
      <div class="order-detail-content" v-if="currentOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.status)">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="景点名称">{{ currentOrder.attraction ? currentOrder.attraction.name : '未知景点' }}</el-descriptions-item>
          <el-descriptions-item label="门票类型">{{ currentOrder.ticket ? currentOrder.ticket.ticketType : '未知类型' }}</el-descriptions-item>
          <el-descriptions-item label="单价">¥{{ currentOrder.ticket ? currentOrder.ticket.price.toFixed(2) : '0.00' }}</el-descriptions-item>
          <el-descriptions-item label="数量">{{ currentOrder.quantity }}</el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ currentOrder.totalAmount.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ currentOrder.contactName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="参观日期">{{ currentOrder.visitDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentOrder.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="备注信息" :span="2">{{ currentOrder.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 景点图片 -->
        <div class="attraction-image" v-if="currentOrder.attraction && currentOrder.attraction.imageUrl">
          <h4>景点图片</h4>
          <el-image
            :src="getAttractionImageUrl(currentOrder.attraction.imageUrl)"
            :preview-src-list="[getAttractionImageUrl(currentOrder.attraction.imageUrl)]"
            fit="cover"
            style="width: 300px; height: 200px; border-radius: 8px;"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDetailDialog">关闭</el-button>
        <el-button v-if="currentOrder && currentOrder.status === 'UNPAID'" size="small" type="primary" @click="payOrder(currentOrder)">立即支付</el-button>
      </div>
    </el-dialog>

    <!-- 支付确认弹窗 -->
    <el-dialog
      title="支付确认"
      :visible.sync="payDialogVisible"
      width="40%"
    >
      <div class="pay-content">
        <h4>订单信息</h4>
        <p><strong>订单号：</strong>{{ currentOrder ? currentOrder.orderNo : '' }}</p>
        <p><strong>景点：</strong>{{ currentOrder ? (currentOrder.attraction ? currentOrder.attraction.name : '未知景点') : '' }}</p>
        <p><strong>门票类型：</strong>{{ currentOrder ? (currentOrder.ticket ? currentOrder.ticket.ticketType : '未知类型') : '' }}</p>
        <p><strong>数量：</strong>{{ currentOrder ? currentOrder.quantity : 0 }}</p>
        <p><strong>总金额：</strong><span class="pay-amount">¥{{ currentOrder ? currentOrder.totalAmount.toFixed(2) : '0.00' }}</span></p>
        
        <el-divider></el-divider>
        
        <h4>支付信息</h4>
        <div class="balance-info">
          <p><strong>当前余额：</strong><span :class="userBalance >= (currentOrder ? currentOrder.totalAmount : 0) ? 'balance-sufficient' : 'balance-insufficient'">¥{{ userBalance }}</span></p>
          <p v-if="payMethod === 'balance'" class="balance-tip">
            <i class="el-icon-check"></i> 将使用账户余额支付
          </p>
          <p v-else-if="payMethod === 'alipay'" class="balance-tip">
            <i class="el-icon-bank-card"></i> 将使用支付宝支付
          </p>
          <p v-else-if="payMethod === 'wechat'" class="balance-tip">
            <i class="el-icon-chat-line-square"></i> 将使用微信支付
          </p>
        </div>
        
        <el-divider></el-divider>
        
        <h4>支付方式</h4>
        <el-radio-group v-model="payMethod">
          <el-radio label="balance" :disabled="userBalance <= 0">
            <i class="el-icon-wallet"></i> 余额支付
            <span v-if="userBalance > 0" class="balance-amount">(可用余额: ¥{{ userBalance }})</span>
            <span v-else class="no-balance">(余额不足)</span>
          </el-radio>
          <el-radio label="alipay">
            <i class="el-icon-bank-card"></i> 支付宝
          </el-radio>
          <el-radio label="wechat">
            <i class="el-icon-chat-line-square"></i> 微信支付
          </el-radio>
        </el-radio-group>
        
        <div class="payment-tip">
          <el-alert
            title="支付说明"
            type="info"
            :closable="false"
            show-icon
          >
            <p>• 您可以选择使用账户余额或第三方支付方式</p>
            <p>• 余额支付：直接从账户余额扣除，无需额外操作</p>
            <p>• 第三方支付：使用支付宝或微信支付完成付款</p>
            <p>• 支付成功后，订单状态将自动更新</p>
          </el-alert>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="payDialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="payLoading" @click="confirmPay">确认支付</el-button>
      </div>
    </el-dialog>

    <!-- 退票申请弹窗 -->
    <el-dialog
      title="申请退票"
      :visible.sync="refundDialogVisible"
      width="500px"
    >
      <el-form :model="refundForm" label-width="100px">
        <el-form-item label="订单号">
          <span>{{ currentOrder ? currentOrder.orderNo : '' }}</span>
        </el-form-item>
        <el-form-item label="景点名称">
          <span>{{ currentOrder ? (currentOrder.attraction ? currentOrder.attraction.name : '未知景点') : '' }}</span>
        </el-form-item>
        <el-form-item label="门票类型">
          <span>{{ currentOrder ? (currentOrder.ticket ? currentOrder.ticket.ticketType : '未知类型') : '' }}</span>
        </el-form-item>
        <el-form-item label="订单金额">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ currentOrder ? currentOrder.totalAmount.toFixed(2) : '0.00' }}</span>
        </el-form-item>
        <el-form-item label="退票原因" required>
          <el-input
            type="textarea"
            v-model="refundForm.reason"
            placeholder="请输入退票原因"
            rows="4"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-alert
            title="退票说明"
            type="info"
            :closable="false"
            show-icon
          >
            <p>1. 退票申请提交后，需要管理员审核</p>
            <p>2. 审核通过后，退款将原路返回到您的账户余额</p>
            <p>3. 审核时间通常为1-3个工作日</p>
          </el-alert>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="refundDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="refundLoading" @click="confirmRefund">提交申请</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination'
import { OrderList, UpdateOrder } from "@/api/order.js"
import { getCurrentUser } from '@/utils/auth'
import { GetUserBalance, ApplyRefund, UpdateUserBalance } from '@/api/userBalance'
import { getAttractionImageUrl } from '@/utils/image'

export default {
  name: "Orders",
  
  components: { Pagination },
  data() {
    return {
      loading: false,
      payLoading: false,
      orderList: [],
      selectedItems: [],
      detailDialogVisible: false,
      payDialogVisible: false,
      currentOrder: null,
      payMethod: 'alipay',
      userBalance: 0,
      refundDialogVisible: false,
      refundLoading: false,
      refundForm: {
        reason: ''
      },
      
      // 搜索表单
      searchForm: {
        orderNo: "",
        status: "",
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      },
      
      pageParam: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      
      // 默认图片
      defaultImage: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400'
    };
  },
  
  created() {
    this.getOrderList();
    this.loadUserBalance();
    
    // 监听余额更新事件（用于退票后更新余额）
    this.$root.Bus.$on('updateUserBalance', (newBalance) => {
      this.userBalance = newBalance;
    });
  },
  
  methods: {
    /**
     * 从token中获取用户ID
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
     * 获取订单列表
     */
    async getOrderList() {
      this.loading = true;
      
      try {
        // 获取当前用户ID
        const userId = this.getUserIdFromToken();
        if (!userId) {
          this.$message.warning('无法获取用户信息，请重新登录');
          this.loading = false;
          return;
        }
        
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

        const response = await OrderList(params);
        
        if (response && response.success && response.data) {
          // 处理多表联查返回的数据
          if (Array.isArray(response.data)) {
            this.orderList = response.data.map(item => this.formatOrderItem(item));
          } else if (response.data.list) {
            this.orderList = response.data.list.map(item => this.formatOrderItem(item));
            this.pageParam.total = response.data.total || 0;
            this.pageParam.currentPage = response.data.current || 1;
          } else {
            this.orderList = [];
          }
        } else {
          this.$message.warning(response.message || "获取订单列表失败");
          this.orderList = [];
        }
      } catch (error) {
        console.error("获取订单列表失败：", error);
        this.$message.error("订单加载失败，请稍后再试");
        this.orderList = [];
      } finally {
        this.loading = false;
      }
    },
    
    /**
     * 格式化订单项目数据（处理多表联查返回的数据结构）
     */
    formatOrderItem(item) {
      return {
        id: item.id,
        orderNo: item.orderNo,
        userId: item.userId,
        ticketId: item.ticketId,
        quantity: item.quantity,
        totalAmount: item.totalAmount,
        status: item.status,
        contactName: item.contactName,
        contactPhone: item.contactPhone,
        visitDate: item.visitDate,
        remark: item.remark,
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
     * 重置搜索
     */
    resetSearch() {
      this.searchForm = {
        orderNo: "",
        status: "",
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      };
      this.pageParam.currentPage = 1;
      this.getOrderList();
    },
    
    /**
     * 分页变更处理
     */
    handlePageChange(parm) {
      this.pageParam.currentPage = parm.currentPage;
      this.pageParam.pageSize = parm.pageSize;
      this.getOrderList();
    },
    
    /**
     * 选择变更处理
     */
    handleSelectionChange(selection) {
      this.selectedItems = selection;
    },
    
    /**
     * 查看订单详情
     */
    viewOrderDetail(row) {
      this.currentOrder = row;
      this.detailDialogVisible = true;
    },
    
    /**
     * 关闭详情弹窗
     */
    closeDetailDialog() {
      this.detailDialogVisible = false;
      this.currentOrder = null;
    },
    
    /**
     * 支付订单
     */
    payOrder(row) {
      this.currentOrder = row;
      // 根据余额情况设置默认支付方式
      if (this.userBalance >= row.totalAmount) {
        this.payMethod = 'balance'; // 余额充足时默认选择余额支付
      } else {
        this.payMethod = 'alipay'; // 余额不足时默认选择支付宝
      }
      this.payDialogVisible = true;
    },
    
    /**
     * 确认支付
     */
    async confirmPay() {
      if (!this.currentOrder) return;
      
      this.payLoading = true;
      
      try {
        // 根据用户选择的支付方式进行支付
        if (this.payMethod === 'balance') {
          // 用户选择余额支付
          if (this.userBalance < this.currentOrder.totalAmount) {
            this.$message.error('余额不足，请选择其他支付方式或先充值');
            return;
          }
          await this.payWithBalance();
        } else {
          // 用户选择第三方支付
          await this.payWithThirdParty();
        }
      } catch (error) {
        console.error('支付失败:', error);
        this.$message.error('支付失败，请稍后再试');
      } finally {
        this.payLoading = false;
      }
    },
    
    /**
     * 使用余额支付
     */
    async payWithBalance() {
      try {
        // 更新订单状态为已支付
        const updateData = {
          status: 'PAID',
          paidAt: new Date().toISOString()
        };
        
        const response = await UpdateOrder(this.currentOrder.id, updateData);
        
        if (response && response.success) {
          // 扣除余额并更新后端
          await this.deductBalance(this.currentOrder.totalAmount);
          
          this.$message.success('余额支付成功！');
          this.payDialogVisible = false;
          this.detailDialogVisible = false;
          this.getOrderList(); // 重新加载订单列表
        } else {
          this.$message.error(response.message || '支付失败');
        }
      } catch (error) {
        console.error('余额支付失败:', error);
        this.$message.error('余额支付失败，请稍后再试');
      }
    },
    
    /**
     * 使用第三方支付
     */
    async payWithThirdParty() {
      // 模拟第三方支付窗口
      this.$message.info(`正在跳转到${this.payMethod === 'alipay' ? '支付宝' : '微信支付'}...`);
      
      // 模拟支付过程
      await new Promise(resolve => setTimeout(resolve, 3000));
      
      // 模拟支付成功
      const paymentSuccess = Math.random() > 0.1; // 90%成功率
      
      if (paymentSuccess) {
        // 支付成功，更新订单状态
        const updateData = {
          status: 'PAID',
          paidAt: new Date().toISOString()
        };
        
        const response = await UpdateOrder(this.currentOrder.id, updateData);
        
        if (response && response.success) {
          this.$message.success(`${this.payMethod === 'alipay' ? '支付宝' : '微信支付'}支付成功！`);
          this.payDialogVisible = false;
          this.detailDialogVisible = false;
          this.getOrderList();
        } else {
          this.$message.error(response.message || '支付失败');
        }
      } else {
        this.$message.error('支付失败，请重试');
      }
    },
    
    /**
     * 扣除余额
     */
    async deductBalance(amount) {
      try {
        const user = getCurrentUser();
        if (!user || !user.id) {
          throw new Error('用户信息不存在');
        }
        
        // 调用后端API扣除余额
        const response = await UpdateUserBalance(user.id, { amount: -amount });
        
        if (response && response.success) {
          // 更新本地余额显示
          this.userBalance = response.data;
          // 通知头部导航更新余额
          this.$root.Bus.$emit('updateUserBalance', this.userBalance);
        } else {
          throw new Error(response.message || '扣除余额失败');
        }
      } catch (error) {
        console.error('扣除余额失败:', error);
        throw error;
      }
    },
    
    /**
     * 取消订单
     */
    async cancelOrder(row) {
      this.$confirm(`确定要取消订单 ${row.orderNo} 吗？`, "取消确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(async () => {
        try {
          const updateData = {
            status: 'CANCELLED',
            cancelledAt: new Date().toISOString()
          };
          
          const response = await UpdateOrder(row.id, updateData);
          
          if (response && response.success) {
            this.$message.success("订单已取消");
            this.getOrderList();
          } else {
            this.$message.error(response.message || "取消失败");
          }
        } catch (error) {
          console.error("取消订单失败：", error);
          this.$message.error("取消失败，请稍后再试");
        }
      }).catch(() => {
        this.$message.info("已取消操作");
      });
    },
    
    
    /**
     * 跳转到首页
     */
    goToHome() {
      this.$router.push('/');
    },
    
    /**
     * 获取状态类型
     */
    getStatusType(status) {
      const statusMap = {
        'UNPAID': 'warning',
        'PAID': 'success',
        'DELIVERED': 'primary',
        'COMPLETED': 'info',
        'CANCELLED': 'danger'
      };
      return statusMap[status] || 'info';
    },
    
    /**
     * 获取状态文本
     */
    getStatusText(status) {
      const statusMap = {
        'UNPAID': '未支付',
        'PAID': '已支付',
        'DELIVERED': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REFUND': '退票中',
        'REFUND_REJECTED': '退票被拒'
      };
      return statusMap[status] || '未知状态';
    },
    
    /**
     * 格式化日期
     */
    formatDate(date) {
      if (!date) return '-';
      const d = new Date(date);
      return d.toLocaleString('zh-CN');
    },
    
    /**
     * 加载用户余额
     */
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
    
    /**
     * 申请退票
     */
    applyRefund(row) {
      this.currentOrder = row
      this.refundForm.reason = ''
      this.refundDialogVisible = true
    },
    
    /**
     * 确认退票申请
     */
    async confirmRefund() {
      if (!this.refundForm.reason.trim()) {
        this.$message.warning('请输入退票原因')
        return
      }
      
      this.refundLoading = true
      
      try {
        const response = await ApplyRefund(this.currentOrder.id, this.refundForm.reason)
        
        if (response && response.success) {
          this.$message.success('退票申请已提交，请等待管理员审核')
          this.refundDialogVisible = false
          this.getOrderList() // 重新加载订单列表
        } else {
          this.$message.error(response.message || '提交退票申请失败')
        }
      } catch (error) {
        console.error('提交退票申请失败:', error)
        this.$message.error('提交退票申请失败，请稍后再试')
      } finally {
        this.refundLoading = false
      }
    }
  }
}
</script>

<style scoped>
.order-search {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.attraction-info {
  line-height: 1.4;
}

.attraction-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.ticket-type {
  font-size: 12px;
  color: #666;
}

.contact-phone {
  font-size: 12px;
  color: #666;
  margin-top: 2px;
}

.total-amount {
  font-weight: 600;
  color: #f56c6c;
}

.empty-orders {
  text-align: center;
  padding: 60px 0;
}

.order-detail-content {
  padding: 20px 0;
}

.attraction-image {
  margin-top: 20px;
  text-align: center;
}

.attraction-image h4 {
  margin-bottom: 15px;
  color: #333;
}

.pay-content h4 {
  color: #333;
  margin-bottom: 15px;
}

.pay-amount {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}

.el-descriptions {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}

.balance-info {
  margin: 15px 0;
}

.balance-sufficient {
  color: #67c23a;
  font-weight: bold;
}

.balance-insufficient {
  color: #f56c6c;
  font-weight: bold;
}

.balance-tip {
  margin: 8px 0;
  font-size: 14px;
  color: #67c23a;
}

.balance-tip.insufficient {
  color: #f56c6c;
}

.balance-tip i {
  margin-right: 5px;
}

.payment-tip {
  margin-top: 15px;
}

.payment-tip .el-alert {
  margin-top: 10px;
}

.payment-tip p {
  margin: 5px 0;
  font-size: 13px;
}

.balance-amount {
  color: #67c23a;
  font-size: 12px;
  margin-left: 5px;
}

.no-balance {
  color: #f56c6c;
  font-size: 12px;
  margin-left: 5px;
}

.el-radio.is-disabled .balance-amount,
.el-radio.is-disabled .no-balance {
  color: #c0c4cc;
}
</style>
