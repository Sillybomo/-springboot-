<template>
  <div class="login-wrap">
    <el-form label-position="left" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm login-container">
      <h3 class="title">用户注册</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="ruleForm.username" auto-complete="off" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="name">
        <el-input type="text" v-model="ruleForm.name" auto-complete="off" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input type="email" v-model="ruleForm.email" auto-complete="off" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="phone">
        <el-input type="tel" v-model="ruleForm.phone" auto-complete="off" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="ruleForm.password" auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input type="password" v-model="ruleForm.confirmPassword" auto-complete="off" placeholder="确认密码"></el-input>
      </el-form-item>
      <el-form-item prop="address">
        <el-input type="text" v-model="ruleForm.address" auto-complete="off" placeholder="地址（可选）"></el-input>
      </el-form-item>
      
      <el-checkbox class="remember" v-model="agreeTerms">我已阅读并同意《用户协议》和《隐私政策》</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button type="primary" style="width:100%;" @click="submitForm('ruleForm')" :loading="registering">注册</el-button>
      </el-form-item>
      <el-form-item style="width:100%; text-align: center; margin-top: 10px;">
        <el-button type="text" @click="goToLogin" style="color: #409EFF;">
          已有账号？立即登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script type="text/ecmascript-6">
import { register } from '@/api/userMG'

export default {
  name: 'register',
  data() {
    return {
      // 定义loading默认为false
      registering: false,
      // 同意条款
      agreeTerms: false,
      ruleForm: {
        username: '',
        name: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: '',
        address: ''
      },
      // rules前端验证 - 简化版本
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' }
        ],
        address: [
          { max: 100, message: '地址长度不能超过 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  
  // 创建完毕状态(里面是操作)
  created() {
    this.$message({
      message: '请填写完整信息进行注册',
      type: 'info'
    })
  },
  
  // 里面的函数只有调用才会执行
  methods: {
    // 跳转到登录页面
    goToLogin() {
      this.$router.push({ path: '/login' })
    },

    // 提交注册表单
    submitForm(formName) {
      console.log('注册按钮被点击了！')
      console.log('表单数据：', this.ruleForm)
      console.log('同意条款：', this.agreeTerms)
      
      // 手动检查密码确认
      if (this.ruleForm.password !== this.ruleForm.confirmPassword) {
        this.$message.error('两次输入的密码不一致')
        return false
      }
      
      this.$refs[formName].validate(valid => {
        console.log('表单验证结果：', valid)
        if (valid) {
          // 检查是否同意条款
          if (!this.agreeTerms) {
            this.$message.error('请先阅读并同意用户协议和隐私政策')
            return false
          }

          this.registering = true
          console.log('开始注册...')
          
          // 准备注册数据
          const registerData = {
            username: this.ruleForm.username,
            name: this.ruleForm.name,
            email: this.ruleForm.email,
            phone: this.ruleForm.phone,
            password: this.ruleForm.password, // 直接使用原始密码，不加密
            address: this.ruleForm.address || '',
            role: 'USER' // 默认为普通用户
          }

          console.log('注册数据：', registerData)

          register(registerData).then(res => {
            console.log('注册响应：', res)
            this.registering = false
            if (res.success) {
              this.$message.success('注册成功！请登录')
              // 注册成功后跳转到登录页面
              setTimeout(() => {
                this.$router.push({ path: '/login' })
              }, 1500)
            } else {
              this.$message.error(res.message || res.msg || '注册失败')
            }
          }).catch(err => {
            this.registering = false
            console.error('注册失败：', err)
            this.$message.error('注册失败，请稍后再试')
          })
        } else {
          this.$message.error('请检查输入信息！')
          this.registering = false
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-wrap {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  padding-top: 5%;
  background-image: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+Cjxzdmcgd2lkdGg9IjEzNjFweCIgaGVpZ2h0PSI2MDlweCIgdmlld0JveD0iMCAwIDEzNjEgNjA5IiB2ZXJzaW9uPSIxLjEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPgogICAgPCEtLSBHZW5lcmF0b3I6IFNrZXRjaCA0Ni4yICg0NDQ5NikgLSBodHRwOi8vd3d3LmJvaGVtaWFuY29kaW5nLmNvbS9za2V0Y2ggLS0+CiAgICA8dGl0bGU+R3JvdXAgMjE8L3RpdGxlPgogICAgPGRlc2M+Q3JlYXRlZCB3aXRoIFNrZXRjaC48L2Rlc2M+CiAgICA8ZGVmcz48L2RlZnM+CiAgICA8ZyBpZD0iQW50LURlc2lnbi1Qcm8tMy4wIiBzdHJva2U9Im5vbmUiIHN0cm9rZS13aWR0aD0iMSIgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJldmVub2RkIj4KICAgICAgICA8ZyBpZD0i6LSm5oi35a+G56CB55m75b2VLeagoemqjCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTc5LjAwMDAwMCwgLTgyLjAwMDAwMCkiPgogICAgICAgICAgICA8ZyBpZD0iR3JvdXAtMjEiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDc3LjAwMDAwMCwgNzMuMDAwMDAwKSI+CiAgICAgICAgICAgICAgICA8ZyBpZD0iR3JvdXAtMTgiIG9wYWNpdHk9IjAuOCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoNzQuOTAxNDE2LCA1NjkuNjk5MTU4KSByb3RhdGUoLTcuMDAwMDAwKSB0cmFuc2xhdGUoLTc0LjkwMTQxNiwgLTU2OS42OTkxNTgpIHRyYW5zbGF0ZSg0LjkwMTQxNiwgNTI1LjE5OTE1OCkiPgogICAgICAgICAgICAgICAgICAgIDxlbGxpcHNlIGlkPSJPdmFsLTExIiBmaWxsPSIjQ0ZEQUU2IiBvcGFjaXR5PSIwLjI1IiBjeD0iNjMuNTc0ODc5MiIgY3k9IjMyLjQ2ODM2NyIgcng9IjIxLjc4MzA0NzkiIHJ5PSIyMS43NjYwMDgiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC0zIiBmaWxsPSIjQ0ZEQUU2IiBvcGFjaXR5PSIwLjU5OTk5OTk2NCIgY3g9IjUuOTg3NDY0NzkiIGN5PSIxMy44NjY4NjAxIiByeD0iNS4yMTczOTEzIiByeT0iNS4yMTMzMDk5NyI+PC9lbGxpcHNlPgogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0zOC4xMzU0NTE0LDg4LjM1MjAyMTUgQzQzLjg5ODQyMjcsODguMzUyMDIxNSA0OC41NzAyMzQsODMuNjgzODY0NyA0OC41NzAyMzQsNzcuOTI1NDAxNSBDNDguNTcwMjM0LDcyLjE2NjkzODMgNDMuODk4NDIyNyw2Ny40OTg3ODE2IDM4LjEzNTQ1MTQsNjcuNDk4NzgxNiBDMzIuMzcyNDgwMSw2Ny40OTg3ODE2IDI3LjcwMDY2ODgsNzIuMTY2OTM4MyAyNy43MDA2Njg4LDc3LjkyNTQwMTUgQzI3LjcwMDY2ODgsODMuNjgzODY0NyAzMi4zNzI0ODAxLDg4LjM1MjAyMTUgMzguMTM1NDUxNCw4OC4zNTIwMjE1IFoiIGlkPSJPdmFsLTMtQ29weSIgZmlsbD0iI0NGREFFNiIgb3BhY2l0eT0iMC40NSI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik02NC4yNzc1NTgyLDMzLjE3MDQ5NjMgTDExOS4xODU4MzYsMTYuNTY1NDkxNSIgaWQ9IlBhdGgtMTIiIHN0cm9rZT0iI0NGREFFNiIgc3Ryb2tlLXdpZHRoPSIxLjczOTEzMDQzIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNNDIuMTQzMTcwOCwyNi41MDAyNjgxIEw3LjcxMTkwMTYyLDE0LjU2NDA3MDIiIGlkPSJQYXRoLTE2IiBzdHJva2U9IiNFMEI0QjciIHN0cm9rZS13aWR0aD0iMC43MDI2Nzg5NjQiIG9wYWNpdHk9IjAuNyIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtZGFzaGFycmF5PSIxLjQwNTM1Nzg5OTg3MzE1MywyLjEwODAzNjk1MzQ2OTk4MSI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik02My45MjYyMTg3LDMzLjUyMTU2MSBMNDMuNjcyMTMyNiw2OS4zMjUwOTUxIiBpZD0iUGF0aC0xNSIgc3Ryb2tlPSIjQkFDQUQ5IiBzdHJva2Utd2lkdGg9IjAuNzAyNjc4OTY0IiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS1kYXNoYXJyYXk9IjEuNDA1MzU3ODk5ODczMTUzLDIuMTA4MDM2OTUzNDY5OTgxIj48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgPGcgaWQ9Ikdyb3VwLTE3IiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgxMjYuODUwOTIyLCAxMy41NDM2NTQpIHJvdGF0ZSgzMC4wMDAwMDApIHRyYW5zbGF0ZSgtMTI2Ljg1MDkyMiwgLTEzLjU0MzY1NCkgdHJhbnNsYXRlKDExNy4yODU3MDUsIDQuMzgxODg5KSIgZmlsbD0iI0NGREFFNiI+CiAgICAgICAgICAgICAgICAgICAgICAgIDxlbGxpcHNlIGlkPSJPdmFsLTQiIG9wYWNpdHk9IjAuNDUiIGN4PSI5LjEzNDgyNjUzIiBjeT0iOS4xMjc2ODA3NiIgcng9IjkuMTM0ODI2NTMiIHJ5PSI5LjEyNzY4MDc2Ij48L2VsbGlwc2U+CiAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0xOC4yNjk2NTMxLDE4LjI1NTM2MTUgQzE4LjI2OTY1MzEsMTMuMjE0MjgyNiAxNC4xNzk4NTE5LDkuMTI3NjgwNzYgOS4xMzQ4MjY1Myw5LjEyNzY4MDc2IEM0LjA4OTgwMTE0LDkuMTI3NjgwNzYgMCwxMy4yMTQyODI2IDAsMTguMjU1MzYxNSBMMTguMjY5NjUzMSwxOC4yNTUzNjE1IFoiIGlkPSJPdmFsLTQiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDkuMTM0ODI3LCAxMy42OTE1MjEpIHNjYWxlKC0xLCAtMSkgdHJhbnNsYXRlKC05LjEzNDgyNywgLTEzLjY5MTUyMSkgIj48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICAgICAgPGcgaWQ9Ikdyb3VwLTE0IiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgyMTYuMjk0NzAwLCAxMjMuNzI1NjAwKSByb3RhdGUoLTUuMDAwMDAwKSB0cmFuc2xhdGUoLTIxNi4yOTQ3MDAsIC0xMjMuNzI1NjAwKSB0cmFuc2xhdGUoMTA2LjI5NDcwMCwgMzUuMjI1NjAwKSI+CiAgICAgICAgICAgICAgICAgICAgPGVsbGlwc2UgaWQ9Ik92YWwtMiIgZmlsbD0iI0NGREFFNiIgb3BhY2l0eT0iMC4yNSIgY3g9IjI5LjExNzY0NzEiIGN5PSIyOS4xNDAyNDM5IiByeD0iMjkuMTE3NjQ3MSIgcnk9IjI5LjE0MDI0MzkiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC0yIiBmaWxsPSIjQ0ZEQUU2IiBvcGFjaXR5PSIwLjMiIGN4PSIyOS4xMTc2NDcxIiBjeT0iMjkuMTQwMjQzOSIgcng9IjIxLjU2ODYyNzUiIHJ5PSIyMS41ODUzNjU5Ij48L2VsbGlwc2U+CiAgICAgICAgICAgICAgICAgICAgPGVsbGlwc2UgaWQ9Ik92YWwtMi1Db3B5IiBzdHJva2U9IiNDRkRBRTYiIG9wYWNpdHk9IjAuNCIgY3g9IjE3OS4wMTk2MDgiIGN5PSIxMzguMTQ2MzQxIiByeD0iMjMuNzI1NDkwMiIgcnk9IjIzLjc0MzkwMjQiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC0yIiBmaWxsPSIjQkFDQUQ5IiBvcGFjaXR5PSIwLjUiIGN4PSIyOS4xMTc2NDcxIiBjeT0iMjkuMTQwMjQzOSIgcng9IjEwLjc4NDMxMzciIHJ5PSIxMC43OTI2ODI5Ij48L2VsbGlwc2U+CiAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTI5LjExNzY0NzEsMzkuOTMyOTI2OCBMMjkuMTE3NjQ3MSwxOC4zNDc1NjEgQzIzLjE2MTYzNTEsMTguMzQ3NTYxIDE4LjMzMzMzMzMsMjMuMTc5NjA5NyAxOC4zMzMzMzMzLDI5LjE0MDI0MzkgQzE4LjMzMzMzMzMsMzUuMTAwODc4MSAyMy4xNjE2MzUxLDM5LjkzMjkyNjggMjkuMTE3NjQ3MSwzOS45MzI5MjY4IFoiIGlkPSJPdmFsLTIiIGZpbGw9IiNCQUNBRDkiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICA8ZyBpZD0iR3JvdXAtOSIgb3BhY2l0eT0iMC40NSIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMTcyLjAwMDAwMCwgMTMxLjAwMDAwMCkiIGZpbGw9IiNFNkExQTYiPgogICAgICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC0yLUNvcHktMiIgY3g9IjcuMDE5NjA3ODQiIGN5PSI3LjE0NjM0MTQ2IiByeD0iNi40NzA1ODgyNCIgcnk9IjYuNDc1NjA5NzYiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTAuNTQ5MDE5NjA4LDEzLjYyMTk1MTIgQzQuMTIyNjI2ODEsMTMuNjIxOTUxMiA3LjAxOTYwNzg0LDEwLjcyMjcyMiA3LjAxOTYwNzg0LDcuMTQ2MzQxNDYgQzcuMDE5NjA3ODQsMy41Njk5NjA5NSA0LjEyMjYyNjgxLDAuNjcwNzMxNzA3IDAuNTQ5MDE5NjA4LDAuNjcwNzMxNzA3IEwwLjU0OTAxOTYwOCwxMy42MjE5NTEyIFoiIGlkPSJPdmFsLTItQ29weS0yIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgzLjc4NDMxNCwgNy4xNDYzNDEpIHNjYWxlKC0xLCAxKSB0cmFuc2xhdGUoLTMuNzg0MzE0LCAtNy4xNDYzNDEpICI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgIDwvZz4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC0xMCIgZmlsbD0iI0NGREFFNiIgY3g9IjIxOC4zODIzNTMiIGN5PSIxMzguNjg1OTc2IiByeD0iMS42MTc2NDcwNiIgcnk9IjEuNjE4OTAyNDQiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC0xMC1Db3B5LTIiIGZpbGw9IiNFMEI0QjciIG9wYWNpdHk9IjAuMzUiIGN4PSIxNzkuNTU4ODI0IiBjeT0iMTc1LjM4MTA5OCIgcng9IjEuNjE3NjQ3MDYiIHJ5PSIxLjYxODkwMjQ0Ij48L2VsbGlwc2U+CiAgICAgICAgICAgICAgICAgICAgPGVsbGlwc2UgaWQ9Ik92YWwtMTAtQ29weSIgZmlsbD0iI0UwQjRCNyIgb3BhY2l0eT0iMC4zNSIgY3g9IjE4MC4wOTgwMzkiIGN5PSIxMDIuNTMwNDg4IiByeD0iMi4xNTY4NjI3NSIgcnk9IjIuMTU4NTM2NTkiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMjguOTk4NTM4MSwyOS45NjcxNTk4IEwxNzEuMTUxMDE4LDEzMi44NzYwMjQiIGlkPSJQYXRoLTExIiBzdHJva2U9IiNDRkRBRTYiIG9wYWNpdHk9IjAuOCI+PC9wYXRoPgogICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICAgICAgPGcgaWQ9Ikdyb3VwLTEwIiBvcGFjaXR5PSIwLjc5OTk5OTk1MiIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMTA1NC4xMDA2MzUsIDM2LjY1OTMxNykgcm90YXRlKC0xMS4wMDAwMDApIHRyYW5zbGF0ZSgtMTA1NC4xMDA2MzUsIC0zNi42NTkzMTcpIHRyYW5zbGF0ZSgxMDI2LjYwMDYzNSwgNC42NTkzMTcpIj4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbC03IiBzdHJva2U9IiNDRkRBRTYiIHN0cm9rZS13aWR0aD0iMC45NDExNzY0NzEiIGN4PSI0My44MTM1NTkzIiBjeT0iMzIiIHJ4PSIxMS4xODY0NDA3IiByeT0iMTEuMjk0MTE3NiI+PC9lbGxpcHNlPgogICAgICAgICAgICAgICAgICAgIDxnIGlkPSJHcm91cC0xMiIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMzQuNTk2Nzc0LCAyMy4xMTExMTEpIiBmaWxsPSIjQkFDQUQ5Ij4KICAgICAgICAgICAgICAgICAgICAgICAgPGVsbGlwc2UgaWQ9Ik92YWwtNyIgb3BhY2l0eT0iMC40NSIgY3g9IjkuMTg1MzQ3MTgiIGN5PSI4Ljg4ODg4ODg5IiByeD0iOC40NzQ1NzYyNyIgcnk9IjguNTU2MTQ5NzMiPjwvZWxsaXBzZT4KICAgICAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTkuMTg1MzQ3MTgsMTcuNDQ1MDM4NiBDMTMuODY1NzI2NCwxNy40NDUwMzg2IDE3LjY1OTkyMzUsMTMuNjE0MzE5OSAxNy42NTk5MjM1LDguODg4ODg4ODkgQzE3LjY1OTkyMzUsNC4xNjM0NTc4NyAxMy44NjU3MjY0LDAuMzMyNzM5MTU2IDkuMTg1MzQ3MTgsMC4zMzI3MzkxNTYgTDkuMTg1MzQ3MTgsMTcuNDQ1MDM4NiBaIiBpZD0iT3ZhbC03Ij48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgPC9nPgogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0zNC42NTk3Mzg1LDI0LjgwOTY5NCBMNS43MTY2NjA4NCw0Ljc2ODc4OTQ1IiBpZD0iUGF0aC0yIiBzdHJva2U9IiNDRkRBRTYiIHN0cm9rZS13aWR0aD0iMC45NDExNzY0NzEiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICA8ZWxsaXBzZSBpZD0iT3ZhbCIgc3Ryb2tlPSIjQ0ZEQUU2IiBzdHJva2Utd2lkdGg9IjAuOTQxMTc2NDcxIiBjeD0iMy4yNjI3MTE4NiIgY3k9IjMuMjk0MTE3NjUiIHJ4PSIzLjI2MjcxMTg2IiByeT0iMy4yOTQxMTc2NSI+PC9lbGxpcHNlPgogICAgICAgICAgICAgICAgICAgIDxlbGxpcHNlIGlkPSJPdmFsLUNvcHkiIGZpbGw9IiNGN0UxQUQiIGN4PSIyLjc5NjYxMDE3IiBjeT0iNjEuMTc2NDcwNiIgcng9IjIuNzk2NjEwMTciIHJ5PSIyLjgyMzUyOTQxIj48L2VsbGlwc2U+CiAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTM0LjYzMTI0NDMsMzkuMjkyMjcxMiBMNS4wNjM2NjY2Myw1OS43ODUwODIiIGlkPSJQYXRoLTEwIiBzdHJva2U9IiNDRkRBRTYiIHN0cm9rZS13aWR0aD0iMC45NDExNzY0NzEiPjwvcGF0aD4KICAgICAgICAgICAgICAgIDwvZz4KICAgICAgICAgICAgICAgIDxnIGlkPSJHcm91cC0xOSIgb3BhY2l0eT0iMC4zMyIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMTI4Mi41MzcyMTksIDQ0Ni41MDI4NjcpIHJvdGF0ZSgtMTAuMDAwMDAwKSB0cmFuc2xhdGUoLTEyODIuNTM3MjE5LCAtNDQ2LjUwMjg2NykgdHJhbnNsYXRlKDExNDIuNTM3MjE5LCAzMjcuNTAyODY3KSI+CiAgICAgICAgICAgICAgICAgICAgPGcgaWQ9Ikdyb3VwLTE3IiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgxNDEuMzMzNTM5LCAxMDQuNTAyNzQyKSByb3RhdGUoMjc1LjAwMDAwMCkgdHJhbnNsYXRlKC0xNDEuMzMzNTM5LCAtMTA0LjUwMjc0MikgdHJhbnNsYXRlKDEyOS4zMzM1MzksIDkyLjUwMjc0MikiIGZpbGw9IiNCQUNBRDkiPgogICAgICAgICAgICAgICAgICAgICAgICA8Y2lyY2xlIGlkPSJPdmFsLTQiIG9wYWNpdHk9IjAuNDUiIGN4PSIxMS42NjY2NjY3IiBjeT0iMTEuNjY2NjY2NyIgcj0iMTEuNjY2NjY2NyI+PC9jaXJjbGU+CiAgICAgICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0yMy4zMzMzMzMzLDIzLjMzMzMzMzMgQzIzLjMzMzMzMzMsMTYuODkwMDExMyAxOC4xMDk5ODg3LDExLjY2NjY2NjcgMTEuNjY2NjY2NywxMS42NjY2NjY3IEM1LjIyMzM0NDU5LDExLjY2NjY2NjcgMCwxNi44OTAwMTEzIDAsMjMuMzMzMzMzMyBMMjMuMzMzMzMzMywyMy4zMzMzMzMzIFoiIGlkPSJPdmFsLTQiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDExLjY2NjY2NywgMTcuNTAwMDAwKSBzY2FsZSgtMSwgLTEpIHRyYW5zbGF0ZSgtMTEuNjY2NjY3LCAtMTcuNTAwMDAwKSAiPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgICAgICAgICAgPGNpcmNsZSBpZD0iT3ZhbC01LUNvcHktNiIgZmlsbD0iI0NGREFFNiIgY3g9IjIwMS44MzMzMzMiIGN5PSI4Ny41IiByPSI1LjgzMzMzMzMzIj48L2NpcmNsZT4KICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMTQzLjUsODguODEyNjY4NSBMMTU1LjA3MDUwMSwxNy42MDM4NTQ0IiBpZD0iUGF0aC0xNyIgc3Ryb2tlPSIjQkFDQUQ5IiBzdHJva2Utd2lkdGg9IjEuMTY2NjY2NjciPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMTcuNSwzNy4zMzMzMzMzIEwxMjcuNDY2MjUyLDk3LjY0NDk3MzUiIGlkPSJQYXRoLTE4IiBzdHJva2U9IiNCQUNBRDkiIHN0cm9rZS13aWR0aD0iMS4xNjY2NjY2NyI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgIDxwb2x5bGluZSBpZD0iUGF0aC0xOSIgc3Ryb2tlPSIjQ0ZEQUU2IiBzdHJva2Utd2lkdGg9IjEuMTY2NjY2NjciIHBvaW50cz0iMTQzLjkwMjU5NyAxMjAuMzAyMjgxIDE3NC45MzU0NTUgMjMxLjU3MTM0MiAzOC41IDE0Ny41MTA4NDcgMTI2LjM2Njk0MSAxMTAuODMzMzMzIj48L3BvbHlsaW5lPgogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0xNTkuODMzMzMzLDk5Ljc0NTM4NDIgTDE5NS40MTY2NjcsODkuMjUiIGlkPSJQYXRoLTIwIiBzdHJva2U9IiNFMEI0QjciIHN0cm9rZS13aWR0aD0iMS4xNjY2NjY2NyIgb3BhY2l0eT0iMC42Ij48L3BhdGg+CiAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTIwNS4zMzMzMzMsODIuMTM3MjEwNSBMMjM4LjcxOTQwNiwzNi4xNjY2NjY3IiBpZD0iUGF0aC0yNCIgc3Ryb2tlPSIjQkFDQUQ5IiBzdHJva2Utd2lkdGg9IjEuMTY2NjY2NjciPjwvcGF0aD4KICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMjY2LjcyMzQyNCwxMzIuMjMxOTg4IEwyMDcuMDgzMzMzLDkwLjQxNjY2NjciIGlkPSJQYXRoLTI1IiBzdHJva2U9IiNDRkRBRTYiIHN0cm9rZS13aWR0aD0iMS4xNjY2NjY2NyI+PC9wYXRoPgogICAgICAgICAgICAgICAgICAgIDxjaXJjbGUgaWQ9Ik92YWwtNSIgZmlsbD0iI0MxRDFFMCIgY3g9IjE1Ni45MTY2NjciIGN5PSI4Ljc1IiByPSI4Ljc1Ij48L2NpcmNsZT4KICAgICAgICAgICAgICAgICAgICA8Y2lyY2xlIGlkPSJPdmFsLTUtQ29weS0zIiBmaWxsPSIjQzFEMUUwIiBjeD0iMzkuMDgzMzMzMyIgY3k9IjE0OC43NSIgcj0iNS4yNSI+PC9jaXJjbGU+CiAgICAgICAgICAgICAgICAgICAgPGNpcmNsZSBpZD0iT3ZhbC01LUNvcHktMiIgZmlsbC1vcGFjaXR5PSIwLjYiIGZpbGw9IiNEMURFRUQiIGN4PSI4Ljc1IiBjeT0iMzMuMjUiIHI9IjguNzUiPjwvY2lyY2xlPgogICAgICAgICAgICAgICAgICAgIDxjaXJjbGUgaWQ9Ik92YWwtNS1Db3B5LTQiIGZpbGwtb3BhY2l0eT0iMC42IiBmaWxsPSIjRDFERUVEIiBjeD0iMjQzLjgzMzMzMyIgY3k9IjMwLjMzMzMzMzMiIHI9IjUuODMzMzMzMzMiPjwvY2lyY2xlPgogICAgICAgICAgICAgICAgICAgIDxjaXJjbGUgaWQ9Ik92YWwtNS1Db3B5LTUiIGZpbGw9IiNFMEI0QjciIGN4PSIxNzUuNTgzMzMzIiBjeT0iMjMyLjc1IiByPSI1LjI1Ij48L2NpcmNsZT4KICAgICAgICAgICAgICAgIDwvZz4KICAgICAgICAgICAgPC9nPgogICAgICAgIDwvZz4KICAgIDwvZz4KPC9zdmc+);
  /* background-color: #112346; */
  background-repeat: no-repeat;
  background-position: center right;
  background-size: 100%;
}
.login-container {
  border-radius: 10px;
  margin: 0px auto;
  width: 400px;
  padding: 30px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  text-align: left;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #505458;
}
.remember {
  margin: 0px 0px 25px 0px;
}
</style>
