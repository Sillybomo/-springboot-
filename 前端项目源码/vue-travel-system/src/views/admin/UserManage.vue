<template>
  <div class="user-management">
    <div class="header">
      <h2>用户管理</h2>
      <div class="actions">
        <el-button type="primary" @click="showCreateDialog">
          <i class="el-icon-plus"></i>
          添加用户
        </el-button>
        <el-button 
          type="danger" 
          :disabled="selectedUsers.length === 0"
          @click="handleBatchDelete"
        >
          <i class="el-icon-delete"></i>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选 -->
    <div class="search-bar">
      <el-form :model="searchForm" :inline="true" label-width="80px">
        <el-form-item label="用户名">
          <el-input 
            v-model="searchForm.username" 
            placeholder="支持模糊搜索用户名"
            clearable
            style="width: 200px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input 
            v-model="searchForm.name" 
            placeholder="支持模糊搜索姓名"
            clearable
            style="width: 200px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input 
            v-model="searchForm.phone" 
            placeholder="支持模糊搜索手机号"
            clearable
            style="width: 200px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select 
            v-model="searchForm.role" 
            placeholder="请选择角色" 
            clearable
            style="width: 150px;"
          >
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 搜索状态显示 -->
    <div v-if="hasActiveSearch" class="search-status">
      <el-tag 
        v-if="searchForm.username" 
        closable 
        @close="clearSearchField('username')"
        type="info"
        style="margin-right: 8px;"
      >
        用户名: {{ searchForm.username }}
      </el-tag>
      <el-tag 
        v-if="searchForm.name" 
        closable 
        @close="clearSearchField('name')"
        type="info"
        style="margin-right: 8px;"
      >
        姓名: {{ searchForm.name }}
      </el-tag>
      <el-tag 
        v-if="searchForm.phone" 
        closable 
        @close="clearSearchField('phone')"
        type="info"
        style="margin-right: 8px;"
      >
        手机号: {{ searchForm.phone }}
      </el-tag>
      <el-tag 
        v-if="searchForm.role" 
        closable 
        @close="clearSearchField('role')"
        type="info"
        style="margin-right: 8px;"
      >
        角色: {{ searchForm.role === 'ADMIN' ? '管理员' : '普通用户' }}
      </el-tag>
      <el-button 
        type="text" 
        @click="handleReset"
        style="margin-left: 10px; color: #909399;"
      >
        清除所有筛选
      </el-button>
    </div>

    <!-- 用户表格 -->
    <el-table 
      :data="userList" 
      v-loading="loading"
      @selection-change="handleSelectionChange"
      stripe
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="email" label="邮箱" width="200" />
      <el-table-column prop="phone" label="电话" width="130" />
      <el-table-column prop="address" label="地址" width="200" show-overflow-tooltip />
      <el-table-column prop="role" label="角色" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'primary'">
            {{ scope.row.role === 'ADMIN' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="balance" label="余额" width="120">
        <template slot-scope="scope">
          <span>¥{{ scope.row.balance || 0 }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="createdAt" label="创建时间" width="180" /> -->
      <el-table-column prop="updatedAt" label="更新时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="handleEdit(scope.row)"
            icon="el-icon-edit"
          >
            编辑
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      :visible.sync="dialogVisible" 
      width="600px"
      @close="resetForm"
    >
      <el-form 
        :model="userForm" 
        :rules="formRules" 
        ref="userFormRef"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input 
            v-model="userForm.password" 
            type="password" 
            placeholder="请输入密码" 
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="userForm.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
        </el-form-item>
        <el-form-item label="余额" prop="balance">
          <el-input-number 
            v-model="userForm.balance" 
            :min="0" 
            :precision="2"
            placeholder="请输入余额" 
          />
        </el-form-item>
      </el-form>
      
      <template slot="footer">
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>

import { UserList, UserCreate, UserUpdate, UserDelete, UserBatchDelete } from '@/api/userManage'

export default {
  name: "UserManage",
  
  data() {
    return {
      loading: false,
      submitLoading: false,
      userList: [],
      selectedUsers: [],
      dialogVisible: false,
      isEdit: false,
      userFormRef: null,
      
      // 搜索表单
      searchForm: {
        username: '',
        name: '',
        phone: '',
        role: ''
      },
      
      // 分页数据
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 用户表单
      userForm: {
        id: null,
        username: '',
        password: '',
        name: '',
        email: '',
        phone: '',
        address: '',
        role: 'USER',
        balance: 0
      },
      
      // 表单验证规则
      formRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      }
    };
  },
  
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑用户' : '添加用户';
    },
    
    // 检查是否有活跃的搜索条件
    hasActiveSearch() {
      return !!(this.searchForm.username || this.searchForm.name || this.searchForm.phone || this.searchForm.role);
    }
  },
  
  created() {
    this.fetchUserList();
  },
  
  methods: {
    // 获取用户列表
    async fetchUserList() {
      try {
        this.loading = true;
        const params = {
          currentPage: this.pagination.current,
          pageSize: this.pagination.size,
          usernameFuzzy: this.searchForm.username,
          nameFuzzy: this.searchForm.name,
          phoneFuzzy: this.searchForm.phone,
          role: this.searchForm.role
        };
        
        const response = await UserList(params);
        
        if (response.success) {
          this.userList = response.data.list || [];
          this.pagination.total = response.data.total || 0;
          this.pagination.current = response.data.current || 1;
          this.pagination.size = response.data.size || 10;
        } else {
          this.$message.error(response.message || '获取用户列表失败');
        }
      } catch (error) {
        this.$message.error('获取用户列表失败');
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1;
      this.fetchUserList();
    },

    // 重置搜索
    handleReset() {
      Object.assign(this.searchForm, {
        username: '',
        name: '',
        phone: '',
        role: ''
      });
      this.pagination.current = 1;
      this.fetchUserList();
    },

    // 清除单个搜索字段
    clearSearchField(field) {
      this.searchForm[field] = '';
      this.pagination.current = 1;
      this.fetchUserList();
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size;
      this.pagination.current = 1;
      this.fetchUserList();
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current;
      this.fetchUserList();
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedUsers = selection;
    },

    // 显示创建对话框
    showCreateDialog() {
      this.isEdit = false;
      this.dialogVisible = true;
      this.resetForm();
    },

    // 编辑用户
    handleEdit(row) {
      this.isEdit = true;
      this.dialogVisible = true;
      Object.assign(this.userForm, { ...row });
    },

    // 删除用户
    async handleDelete(row) {
      try {
        await this.$confirm(
          `确定要删除用户 "${row.name}" 吗？`,
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        );
        
        const response = await UserDelete(row.id);
        
        if (response.success) {
          this.$message.success('删除成功');
          this.fetchUserList();
        } else {
          this.$message.error(response.message || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败');
          console.error(error);
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(
          `确定要删除选中的 ${this.selectedUsers.length} 个用户吗？`,
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        );
        
        const userIds = this.selectedUsers.map(user => user.id);
        const response = await UserBatchDelete(userIds);
        
        if (response.success) {
          this.$message.success('批量删除成功');
          this.fetchUserList();
        } else {
          this.$message.error(response.message || '批量删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败');
          console.error(error);
        }
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.userFormRef.validate();
        this.submitLoading = true;
        
        let response;
        if (this.isEdit) {
          response = await UserUpdate(this.userForm.id, this.userForm);
        } else {
          response = await UserCreate(this.userForm);
        }
        
        if (response.success) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功');
          this.dialogVisible = false;
          this.fetchUserList();
        } else {
          this.$message.error(response.message || '操作失败');
        }
      } catch (error) {
        this.$message.error('操作失败');
        console.error(error);
      } finally {
        this.submitLoading = false;
      }
    },

    // 重置表单
    resetForm() {
      Object.assign(this.userForm, {
        id: null,
        username: '',
        password: '',
        name: '',
        email: '',
        phone: '',
        address: '',
        role: 'USER',
        balance: 0
      });
      if (this.$refs.userFormRef) {
        this.$refs.userFormRef.clearValidate();
      }
    }
  }
};
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
}

.actions {
  display: flex;
  gap: 10px;
}

.search-bar {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-status {
  background: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  padding: 12px 16px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
}
</style>
