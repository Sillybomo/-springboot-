/**
* 头部菜单
*/ 
<template>
  <el-menu class="el-menu-demo" mode="horizontal" background-color="#334157" text-color="#fff" active-text-color="#fff">
    <el-button class="buttonimg">
      <img class="showimg" :src="collapsed?imgsq:imgshow" @click="toggle(collapsed)">
    </el-button>
    

    <!-- 用户头像 -->
    <!-- <el-menu-item index="avatar" class="avatar-item">
      <el-avatar
        :size="32"
        :src="getAvatarUrl(currentUser.avatar)"
        class="user-avatar"
      >
        <i class="el-icon-user"></i>
      </el-avatar>
    </el-menu-item> -->

    <el-submenu index="2" class="submenu">
      <template slot="title">{{ currentUser.username || currentUser.name || '用户' }}</template>
      <el-menu-item v-if="!isAdmin" @click="goToProfile()" index="2-1">个人中心</el-menu-item>
      <el-menu-item v-if="isAdmin" @click="goToAdmin()" index="2-4">管理后台</el-menu-item>
      <el-menu-item @click="exit()" index="2-5">退出登录</el-menu-item>
    </el-submenu>
    
  </el-menu>
</template>
<script>
import { loginout } from '../api/userMG'
import { getCurrentUser, isAdmin } from '@/utils/auth'
import { getAvatarUrl } from '@/utils/avatar'

export default {
  name: 'navcon',
  data() {
    return {
      collapsed: true,
      imgshow: require('../assets/img/show.png'),
      imgsq: require('../assets/img/sq.png'),
      user: {}
    }
  },
  computed: {
    currentUser() {
      return getCurrentUser() || {}
    },
    isAdmin() {
      return isAdmin()
    }
  },
  // 创建完毕状态(里面是操作)
  created() {
    this.user = JSON.parse(localStorage.getItem('userdata'))
  },
  methods: {
    // 获取头像URL
    getAvatarUrl(avatar) {
      return getAvatarUrl(avatar)
    },
    goToProfile() {
      this.$router.push('/profile')
    },
    goToAdmin() {
      this.$router.push('/admin/Ticket')
    },
    // 退出登录
    exit() {
      this.$confirm('退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          setTimeout(() => {
            this.$store.commit('logout', 'false')
            this.$router.push({ path: '/login' })
            this.$message({
              type: 'success',
              message: '已退出登录!'
            })
          }, 1000)
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
        })
    },
    // 切换显示
    toggle(showtype) {
      this.collapsed = !showtype
      this.$root.Bus.$emit('toggle', this.collapsed)
    }
  }
}
</script>
<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  border: none;
}
.submenu {
  float: right;
}

.avatar-item {
  float: right;
  margin-right: 10px;
  padding: 0 !important;
}

.avatar-item .user-avatar {
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.avatar-item:hover .user-avatar {
  border-color: rgba(255, 255, 255, 0.8);
  transform: scale(1.1);
}

.buttonimg {
  height: 60px;
  background-color: transparent;
  border: none;
}
.showimg {
  width: 26px;
  height: 26px;
  position: absolute;
  top: 17px;
  left: 17px;
}
.showimg:active {
  border: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .avatar-item {
    margin-right: 5px;
  }

  .avatar-item .user-avatar {
    width: 28px !important;
    height: 28px !important;
  }
}

@media (max-width: 480px) {
  .avatar-item {
    margin-right: 2px;
  }

  .avatar-item .user-avatar {
    width: 24px !important;
    height: 24px !important;
  }
}
</style>