<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>放票管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户订单列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 搜索筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="user-search" label-width="100px">
      <el-form-item label="订单编号：">
        <el-input size="small" v-model="searchForm.orderNo" placeholder="输入订单编号"></el-input>
      </el-form-item>
      <el-form-item label="用户ID：">
        <el-input size="small" v-model="searchForm.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="订单状态：">
        <el-select size="small" v-model="searchForm.status" placeholder="选择订单状态" clearable>
          <el-option label="未支付" value="UNPAID"></el-option>
          <el-option label="已支付" value="PAID"></el-option>
          <el-option label="已发货" value="DELIVERED"></el-option>
          <el-option label="已完成" value="COMPLETED"></el-option>
          <el-option label="已取消" value="CANCELLED"></el-option>
          <el-option label="退票中" value="REFUND"></el-option>
          <el-option label="退票被拒" value="REFUND_REJECTED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchOrder">搜索</el-button>
        <el-button size="small" type="default" icon="el-icon-refresh" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 订单表格列表 -->
    <el-table
      size="small"
      :data="orderList"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="加载订单中..."
      style="width: 100%; margin-bottom: 20px;"
    >
      <el-table-column align="center" type="selection" width="50"></el-table-column>
      <!-- 序号列 -->
      <el-table-column sortable label="序号" width="70">
        <template slot-scope="scope">
          {{ (pageParam.currentPage - 1) * pageParam.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <!-- 订单核心字段（与实体类字段对应） -->
      <el-table-column sortable prop="orderNo" label="订单编号" width="100"></el-table-column>
      <el-table-column sortable prop="userId" label="用户ID" width="100"></el-table-column>
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
      <!-- 备注列 - 修复空值显示问题 -->
      <el-table-column sortable prop="remark" label="备注" width="100">
        <template slot-scope="scope">
          {{ scope.row.remark ? scope.row.remark : '无备注' }}
        </template>
      </el-table-column>
      <!-- 操作列（按状态显示按钮） -->
      <el-table-column align="center" label="操作" width="160">
        <template slot-scope="scope">
          <!-- 未支付订单：显示删除 -->
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDeleteOrder(scope.row)"
            v-if="['UNPAID', 'CANCELLED'].includes(scope.row.status)"
          >
            删除
          </el-button>
          <!-- 已支付未发货：显示发货 -->
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-truck"
            @click="handleDeliveryOrder(scope.row)"
            v-else-if="scope.row.status === 'PAID'"
          >
            发货
          </el-button>
          <!-- 已发货/已完成：显示查看 -->
          <el-button
            size="mini"
            type="default"
            icon="el-icon-view"
            @click="handleViewOrder(scope.row)"
            v-else
          >
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageParam" @callFather="handlePageChange"></Pagination>

    <!-- 发货确认弹窗 -->
    <el-dialog
      title="订单发货确认"
      :visible.sync="deliveryDialogVisible"
      width="30%"
      :before-close="closeDeliveryDialog"
    >
      <el-form label-width="120px" :model="deliveryForm" :rules="deliveryRules" ref="deliveryForm">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="deliveryForm.orderNo" disabled placeholder="订单编号"></el-input>
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="deliveryForm.userId" disabled placeholder="用户ID"></el-input>
        </el-form-item>
        <el-form-item label="参观日期" prop="visitDate">
          <el-input v-model="deliveryForm.visitDate" disabled placeholder="参观日期"></el-input>
        </el-form-item>
        <el-form-item label="发货备注" prop="remark">
          <el-input
            type="textarea"
            rows="3"
            v-model="deliveryForm.remark"
            placeholder="请输入发货备注（如物流信息）"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDeliveryDialog">取消</el-button>
        <el-button size="small" type="primary" :loading="deliveryLoading" @click="submitDelivery">确认发货</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination'
import { OrderList, OrderDelete, OrderDelivery } from "@/api/ticketManage.js"
import { req } from "@/api/axiosFun.js"

export default {
  name: "TicketOrderManage",
  
  components: { Pagination },
  data() {
    return {
      loading: false,
      deliveryLoading: false,
      deliveryDialogVisible: false,
      orderList: [],
      remark: "",
      
      // 搜索表单（与实体类字段对应）
      searchForm: {
        orderNo: "",
        userId: "",
        ticketId: "",
        status: "",
        createTimeRange: [],
        token: localStorage.getItem('logintoken')
      },
      
      pageParam: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      
      // 发货表单
      deliveryForm: {
        orderNo: "",
        userId: "",
        visitDate: "",
        remark: "",
        token: localStorage.getItem('logintoken')
      },
      
      deliveryRules: {
        remark: [
          { max: 200, message: '备注不能超过200个字符', trigger: 'blur' }
        ]
      },
      
      // 状态映射（与后端保持一致）
      statusMap: {
        'UNPAID': '未支付',
        'PAID': '已支付',
        'DELIVERED': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REFUND': '退票中' ,
        'REFUND_REJECTED': '退票被拒'
      }
    };
  },
  created() {
    this.getOrderList();
  },
  methods: {
    /**
     * 获取订单列表（适配实体类字段）
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
          } else {
            this.$message.warning(res.message || "获取订单列表失败");
          }
        })
        .catch(err => {
          this.loading = false;
          console.error("获取订单失败：", err);
          this.$message.error("订单加载失败，请稍后再试");
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
        userId: "",
        ticketId: "",
        status: "",
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
     * 删除未支付订单
     */
    handleDeleteOrder(row) {
      if (row.status !== 'UNPAID') {
        this.$message.error("仅允许删除未支付订单");
        return;
      }

      this.$confirm(`确定删除订单【${row.orderNo}】吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          // 直接传递订单ID
          OrderDelete(row.id)
            .then(res => {
              if (res.success) {
                this.$message.success("订单删除成功");
                this.getOrderList();
              } else {
                this.$message.warning(res.message || "订单删除失败");
              }
            })
            .catch(err => {
              console.error("删除订单失败：", err);
              this.$message.error("订单删除失败，请稍后再试");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    /**
     * 打开发货弹窗
     */
    handleDeliveryOrder(row) {
      if (row.status !== 'PAID') {
        this.$message.error("仅允许对已支付订单进行发货操作");
        return;
      }

      this.deliveryForm = {
        orderNo: row.orderNo,
        userId: row.userId,
        visitDate: this.normalizeToYMD(row.visitDate),
        remark: row.remark,
        token: localStorage.getItem('logintoken')
      };
      this.deliveryDialogVisible = true;
    },

    /**
     * 提交发货操作
     */
    submitDelivery() {
      this.$refs.deliveryForm.validate(valid => {
        if (!valid) return;

        this.deliveryLoading = true;
        // 查找当前订单的id（实体类主键）
        const currentOrder = this.orderList.find(item => item.orderNo === this.deliveryForm.orderNo);
        
        if (!currentOrder) {
          this.$message.error("未找到对应订单");
          this.deliveryLoading = false;
          return;
        }

        const params = {
          remark: this.deliveryForm.remark
        };

        OrderDelivery(currentOrder.id, params)
          .then(res => {
            this.deliveryLoading = false;
            if (res.success) {
              this.$message.success("订单发货成功");
              this.deliveryDialogVisible = false;
              this.getOrderList();
            } else {
              this.$message.warning(res.message || "订单发货失败");
            }
          })
          .catch(err => {
            this.deliveryLoading = false;
            console.error("发货失败：", err);
            this.$message.error("发货操作失败，请稍后再试");
          });
      });
    },

    /**
     * 关闭发货弹窗
     */
    closeDeliveryDialog() {
      this.deliveryDialogVisible = false;
      this.$nextTick(() => {
        this.$refs.deliveryForm.resetFields();
      });
    },

    /**
     * 查看订单详情
     */
    handleViewOrder(row) {
      this.$message.info(`订单【${row.orderNo}】详情：\n状态：${this.formatStatus(row.status)}\n总金额：${row.totalAmount.toFixed(2)}元`);
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