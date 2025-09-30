export default [
  {
    path: '/user',
    component: () => import('@/views/user/Layout'), // 用户端布局
    children: [
      { path: 'home', component: () => import('@/views/user/home') }
    ]
  }
]