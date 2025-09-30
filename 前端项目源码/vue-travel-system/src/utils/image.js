/**
 * 图片处理工具函数
 */

// 默认景点图片
export const DEFAULT_ATTRACTION_IMAGE = 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400'

/**
 * 获取景点图片URL
 * @param {string} imageUrl - 图片路径
 * @returns {string} - 可用的图片URL
 */
export function getAttractionImageUrl(imageUrl) {
  if (!imageUrl) return DEFAULT_ATTRACTION_IMAGE
  
  // 如果是完整URL，直接返回
  if (imageUrl.startsWith('http')) {
    return imageUrl
  }
  
  // 如果是static目录下的图片，直接返回
  if (imageUrl.startsWith('/static/') || imageUrl.startsWith('/attraction/')) {
    return imageUrl
  }
  
  // 如果是attraction\开头的路径（Windows路径分隔符），转换为URL格式
  if (imageUrl.startsWith('attraction\\') || imageUrl.startsWith('attraction/')) {
    const fileName = imageUrl.replace(/\\/g, '/') // 统一使用正斜杠
    return '/static/' + fileName
  }
  
  // 如果是相对路径，尝试转换为require路径
  if (imageUrl.startsWith('/src/assets/')) {
    console.warn('景点图片路径已过时，请使用新的路径格式:', imageUrl)
    return DEFAULT_ATTRACTION_IMAGE
  }
  
  // 如果是其他相对路径，尝试直接使用
  if (imageUrl.startsWith('/')) {
    return imageUrl
  }
  
  // 默认返回默认图片
  return DEFAULT_ATTRACTION_IMAGE
}

/**
 * 获取通用图片URL
 * @param {string} imageUrl - 图片路径
 * @param {string} defaultImage - 默认图片URL
 * @returns {string} - 可用的图片URL
 */
export function getImageUrl(imageUrl, defaultImage = DEFAULT_ATTRACTION_IMAGE) {
  if (!imageUrl) return defaultImage
  
  // 如果是完整URL，直接返回
  if (imageUrl.startsWith('http')) {
    return imageUrl
  }
  
  // 如果是static目录下的图片，转换为可访问的URL
  if (imageUrl.startsWith('static/')) {
    return '/' + imageUrl
  }
  
  // 如果是attraction\开头的路径（Windows路径分隔符），转换为URL格式
  if (imageUrl.startsWith('attraction\\') || imageUrl.startsWith('attraction/')) {
    const fileName = imageUrl.replace(/\\/g, '/') // 统一使用正斜杠
    return '/static/' + fileName
  }
  
  // 如果是/static/、/avatar/、/attraction/开头的路径，直接返回
  if (imageUrl.startsWith('/static/') || imageUrl.startsWith('/avatar/') || imageUrl.startsWith('/attraction/')) {
    return imageUrl
  }
  

  // 如果是其他相对路径，尝试直接使用
  if (imageUrl.startsWith('/')) {
    return imageUrl
  }
  
  // 默认返回默认图片
  return defaultImage
}

/**
 * 标准化图片路径（用于数据库存储）
 * @param {string} imageUrl - 原始图片路径
 * @param {string} category - 图片分类（avatar, attraction, product等）
 * @returns {string} - 标准化的相对路径
 */
export function normalizeImagePath(imageUrl, category = 'images') {
  if (!imageUrl) return ''
  
  // 如果已经是标准格式，直接返回
  if (imageUrl.startsWith(`/${category}/`)) {
    return imageUrl
  }
  
  // 如果是完整URL，提取文件名
  if (imageUrl.startsWith('http')) {
    const fileName = imageUrl.split('/').pop()
    return `/${category}/${fileName}`
  }
  
  // 如果是其他相对路径，标准化格式
  if (imageUrl.startsWith('/')) {
    const fileName = imageUrl.split('/').pop()
    return `/${category}/${fileName}`
  }
  
  // 如果只是文件名，添加分类前缀
  return `/${category}/${imageUrl}`
}
