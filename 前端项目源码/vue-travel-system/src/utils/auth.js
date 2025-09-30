/**
 * 用户认证和权限相关工具函数
 */

/**
 * 获取当前登录用户信息
 */
export function getCurrentUser() {
  try {
    const userData = localStorage.getItem('userdata')
    return userData ? JSON.parse(userData) : null
  } catch (error) {
    console.error('获取用户信息失败:', error)
    return null
  }
}

/**
 * 获取当前登录用户的角色
 */
export function getUserRole() {
  const user = getCurrentUser()
  if (!user) return null
  
  return user.role || user.roleName || 'user'
}

/**
 * 检查用户是否为管理员
 */
export function isAdmin() {
  const role = getUserRole()
  return role === 'admin' || role === 'ADMIN' || role === '管理员'
}

/**
 * 检查用户是否已登录
 */
export function isLoggedIn() {
  const token = localStorage.getItem('logintoken')
  return !!token
}

/**
 * 检查用户是否有权限访问某个页面
 */
export function hasPermission(requiredRole) {
  if (!isLoggedIn()) return false
  
  const userRole = getUserRole()
  
  // 管理员有所有权限
  if (isAdmin()) return true
  
  // 检查特定角色权限
  if (requiredRole === 'admin') {
    return isAdmin()
  }
  
  return userRole === requiredRole
}

/**
 * 根据用户角色获取默认跳转路径
 */
export function getDefaultRedirectPath() {
  if (!isLoggedIn()) return '/login'
  
  if (isAdmin()) {
    return '/admin/Ticket'
  } else {
    return '/'
  }
}

/**
 * 保存重定向路径
 */
export function saveRedirectPath(path) {
  sessionStorage.setItem('redirectPath', path)
}

/**
 * 获取并清除重定向路径
 */
export function getAndClearRedirectPath() {
  const path = sessionStorage.getItem('redirectPath')
  if (path) {
    sessionStorage.removeItem('redirectPath')
  }
  return path
}
