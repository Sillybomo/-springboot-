<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>退票管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户退票列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 页面说明 -->
    <el-alert
      title="退票管理"
      type="info"
      description="此页面仅显示退票中的订单，您可以处理用户的退票申请"
      :closable="false"
      style="margin-bottom: 20px;">
    </el-alert>

    <!-- 顶部信息区域 -->
    <el-form :inline="true" :model="searchForm" class="user-search" label-width="100px">
      <el-form-item label="订单编号:">
        <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable style="width: 200px;"></el-input>
      </el-form-item>
      <el-form-item label="用户名:">
        <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable style="width: 150px;"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="searchOrder">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 订单表格列表 -->
    <el-table
      size="small"
      :data="orderList"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="加载退票订单中..."
      style="width: 100%; margin-bottom: 20px;"
    >
      <!-- 表格列保持不变 -->
      <el-table-column align="center" type="selection" width="50"></el-table-column>
      <el-table-column sortable label="序号" width="70">
        <template slot-scope="scope">
          {{ (pageParam.currentPage - 1) * pageParam.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column sortable prop="orderNo" label="订单编号" width="100"></el-table-column>
      <el-table-column sortable prop="ticketId" label="票务ID" width="90"></el-table-column>
      <el-table-column sortable prop="quantity" label="购票数量" width="100"></el-table-column>
      <el-table-column sortable prop="totalAmount" label="订单金额(元)" width="117">
        <template slot-scope="scope">{{ scope.row.totalAmount.toFixed(2) }}</template>
      </el-table-column>
      <el-table-column sortable prop="status" label="订单状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ formatStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column sortable prop="visitDate" label="参观日期" width="100">
        <template slot-scope="scope">{{ normalizeToYMD(scope.row.visitDate) }}</template>
      </el-table-column>
      <el-table-column sortable prop="contactName" label="联系人" width="100"></el-table-column>
      <el-table-column sortable prop="contactPhone" label="联系电话" width="100"></el-table-column>
      <el-table-column sortable prop="createdAt" label="下单时间" width="100">
        <template slot-scope="scope">{{ normalizeToYMD(scope.row.createdAt) }}</template>
      </el-table-column>
      <el-table-column sortable prop="updatedAt" label="更新时间" width="100">
        <template slot-scope="scope">{{ normalizeToYMD(scope.row.updatedAt) }}</template>
      </el-table-column>
      <!-- 操作列 -->
      <el-table-column align="center" label="操作" width="200">
        <template slot-scope="scope">
          <!-- 退票中状态：显示处理按钮 -->
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-check"
            @click="handleProcessRefund(scope.row)"
            v-if="scope.row.status === 'REFUND'"
          >
            处理退票
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageParam" @callFather="handlePageChange"></Pagination>

    <!-- 处理退票弹窗 -->
    <el-dialog
      title="处理退票"
      :visible.sync="refundDialogVisible"
      width="40%"
      :before-close="closeRefundDialog"
    >
      <el-form label-width="120px" :model="refundForm" :rules="refundRules" ref="refundForm">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="refundForm.orderNo" disabled placeholder="订单编号"></el-input>
        </el-form-item>
          <el-form-item label="订单ID" prop="id">
          <el-input v-model="refundForm.id" disabled placeholder="订单ID"></el-input>
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="refundForm.userId" disabled placeholder="用户ID"></el-input>
        </el-form-item>
        <el-form-item label="退票金额" prop="refundAmount">
          <el-input v-model="refundForm.refundAmount" disabled placeholder="退票金额"></el-input>
        </el-form-item>
        <el-form-item label="处理结果" prop="processResult">
          <el-radio-group v-model="refundForm.processResult">
            <el-radio label="APPROVED">同意退票</el-radio>
            <el-radio label="REJECTED">拒绝退票</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理备注" prop="processRemark">
          <el-input
            type="textarea"
            rows="3"
            v-model="refundForm.processRemark"
            placeholder="请输入处理备注"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeRefundDialog">取消</el-button>
        <el-button size="small" type="primary" :loading="refundLoading" @click="submitRefundProcess">确认处理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import { OrderList, OrderDelivery } from "@/api/refundTicket.js";
        
export default {
  name: "RefundTicket",
  components: { Pagination },
  data() {
    return {
      loading: false,
      refundLoading: false,
      refundDialogVisible: false,
      orderList: [],
      id: "",
      // 搜索表单 - 移除状态选择，固定为退票状态
      searchForm: {
        orderNo: "",
        userName: "",  // 修改为用户名查询
        ticketId: "",
        // status 固定为 REFUND，不在表单中显示
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      },
      
      pageParam: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      
      // 退票处理表单
      refundForm: {
        orderNo: "",
        userId: "",
        id: "",
        refundAmount: "",
        processResult: "APPROVED",
        processRemark: "",
        token: localStorage.getItem('logintoken')
      },
      
      refundRules: {
        processResult: [
          { required: true, message: '请选择处理结果', trigger: 'change' }
        ],
        processRemark: [
          { max: 200, message: '备注不能超过200个字符', trigger: 'blur' }
        ]
      },
      
      // 状态映射
      statusMap: {
        'UNPAID': '未支付',
        'PAID': '已支付',
        'DELIVERED': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REFUND': '退票中',
        'REFUND_REJECTED': '退票被拒'
      }
    };
  },
  created() {
    // 组件创建时自动查询退票状态的订单
    this.getOrderList();
  },
  methods: {
    /**
     * 获取订单列表（只查询退票状态的订单）
     */
    getOrderList() {
      this.loading = true;
      const params = {
        ...this.searchForm,
        currentPage: this.pageParam.currentPage,
        pageSize: this.pageParam.pageSize,
        startTime: this.searchForm.createTimeRange[0] || "",
        endTime: this.searchForm.createTimeRange[1] || ""
      };
      
      // 强制设置为退票状态，确保只查询退票中的订单
      params.status = "REFUND";
      
      if (!params.startTime) delete params.startTime;
      if (!params.endTime) delete params.endTime;
      delete params.createTimeRange;

      OrderList(params)
        .then(res => {
          this.loading = false;
          if (res.success && res.data) {
            this.orderList = res.data.list || [];
            this.pageParam.total = res.data.total || 0;
            this.pageParam.currentPage = res.data.current || 1;
            
            // 如果查询结果为空，显示提示信息
            if (this.orderList.length === 0) {
              this.$message.info("暂无退票中的订单");
            }
          } else {
            this.$message.warning(res.message || "获取退票列表失败");
          }
        })
        .catch(err => {
          this.loading = false;
          console.error("获取退票列表失败：", err);
          this.$message.error("退票列表加载失败，请稍后再试");
        });
    },

    /**
     * 搜索订单
     */
    searchOrder() {
      this.pageParam.currentPage = 1;
      this.getOrderList();
    },

    /**
     * 重置搜索条件
     */
    resetSearch() {
      this.searchForm = {
        orderNo: "",
        userName: "",  // 修改为用户名查询
        ticketId: "",
        // 重置时保持退票状态，不显示在表单中
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      };
      this.pageParam.currentPage = 1;
      this.getOrderList();
    },

    /**
     * 分页变更
     */
    handlePageChange(parm) {
      this.pageParam.currentPage = parm.currentPage;
      this.pageParam.pageSize = parm.pageSize;
      this.getOrderList();
    },

    /**
     * 处理退票
     */
    handleProcessRefund(row) {
      this.refundForm = {
        orderNo: row.orderNo,
        userId: row.userId,
        id : row.id,
        refundAmount: row.totalAmount.toFixed(2),
        processResult: "APPROVED",
        processRemark: "",
        token: localStorage.getItem('logintoken')
      };
      this.refundDialogVisible = true;
    },

    /**
     * 提交退票处理
     */
    submitRefundProcess() {
      this.$refs.refundForm.validate(valid => {
        if (!valid) return;

        this.refundLoading = true;
        const data = {
          processResult: this.refundForm.processResult,
          processRemark: this.refundForm.processRemark
        };

        OrderDelivery(this.refundForm.id, data)
          .then(res => {
            this.refundLoading = false;
            if (res.success) {
              this.$message.success("退票处理成功");
              this.refundDialogVisible = false;
              this.getOrderList();
            } else {
              this.$message.warning(res.message || "退票处理失败");
            }
          })
          .catch(err => {
            this.refundLoading = false;
            console.error("退票处理失败：", err);
            this.$message.error("退票处理失败，请稍后再试");
          });
      });
    },

    /**
     * 关闭退票处理弹窗
     */
    closeRefundDialog() {
      this.refundDialogVisible = false;
      this.$nextTick(() => {
        this.$refs.refundForm.resetFields();
      });
    },

    /**
     * 格式化状态显示
     */
    formatStatus(status) {
      return this.statusMap[status] || status;
    },

    /**
     * 根据状态获取标签样式
     */
    getStatusTagType(status) {
      const typeMap = {
        'UNPAID': 'warning',
        'PAID': 'primary',
        'DELIVERED': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger',
        'REFUND': 'warning',
        'REFUND_REJECTED': 'danger'
      };
      return typeMap[status] || 'default';
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
.user-search {
  margin-top: 10px;
  margin-bottom: 20px;
}
.el-dialog__body .el-form-item {
  margin-bottom: 15px;
}
</style>
