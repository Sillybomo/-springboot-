/**
 * 头像处理工具函数
 */

// 默认头像
export const DEFAULT_AVATAR = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

/**
 * 获取头像URL
 * @param {string} avatar - 头像路径
 * @returns {string} - 可用的头像URL
 */
export function getAvatarUrl(avatar) {
  if (!avatar) return DEFAULT_AVATAR
  
  // 如果是完整URL，直接返回
  if (avatar.startsWith('http')) {
    return avatar
  }
  
  // 如果是static目录下的头像，转换为可访问的URL
  if (avatar.startsWith('static/avatar/')) {
    return '/' + avatar
  }
  
  // 如果是/avatar/开头的路径，直接返回
  if (avatar.startsWith('/avatar/')) {
    return avatar
  }
  
  // 如果是旧的src/assets路径，尝试转换为require路径
  if (avatar.startsWith('/src/assets/')) {
    console.warn('头像路径已过时，请使用新的路径格式:', avatar)
    return DEFAULT_AVATAR
  }
  
  // 如果是其他相对路径，尝试直接使用
  if (avatar.startsWith('/')) {
    return avatar
  }
  
  // 默认返回默认头像
  return DEFAULT_AVATAR
}

/**
 * 验证头像文件
 * @param {File} file - 文件对象
 * @returns {Object} - 验证结果
 */
export function validateAvatarFile(file) {
  const result = {
    valid: true,
    message: ''
  }
  
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    result.valid = false
    result.message = '只能上传图片文件!'
    return result
  }
  
  // 检查文件大小 (2MB)
  if (file.size > 2 * 1024 * 1024) {
    result.valid = false
    result.message = '图片大小不能超过 2MB!'
    return result
  }
  
  return result
}

/**
 * 生成头像预览URL
 * @param {File} file - 文件对象
 * @returns {Promise<string>} - 预览URL
 */
export function generateAvatarPreview(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => resolve(e.target.result)
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}
