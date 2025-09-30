export default [
  {
    path: '/admin',
    component: () => import('@/views/admin/Layout'), // 管理员布局
    meta: { requireAdmin: true }, // 标记需要管理员权限
    children: [
      { path: 'user', component: () => import('@/views/admin/Ticket') }
    ]
  }
]
