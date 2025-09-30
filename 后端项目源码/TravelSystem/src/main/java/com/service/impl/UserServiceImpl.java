package com.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.po.Order;
import com.entity.po.User;
import com.entity.query.UserQuery;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.User;
import com.entity.query.UserQuery;
import com.entity.vo.PaginationResultVO;
import com.service.UserService;
import com.mapper.UserMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_user
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

    //根据用户名和密码登录
    public User login(User user){
        //封装查询条件
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        queryWrapper.eq("password",user.getPassword());
        //执行查询
        User user2=userMapper.selectOne(queryWrapper);
        System.out.println("----正在查询数据库----");
        return user2;
    }


	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<User> findListByParam(UserQuery param){
		return userMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(UserQuery param){
		return userMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
    @Override
    public PaginationResultVO<User> findListByPage(UserQuery param){
        // 参数校验
        if (param == null) {
            throw new IllegalArgumentException("查询参数不能为空");
        }
        
        // 设置默认分页参数
        if (param.getCurrentPage() == null || param.getCurrentPage() <= 0) {
            param.setCurrentPage(1);
        }
        if (param.getPageSize() == null || param.getPageSize() <= 0) {
            param.setPageSize(10);
        }
        PaginationResultVO<User> page = new PaginationResultVO<>();
        try {


            // 构建模糊查询条件

            List<String> conditions = new ArrayList<>();

            // 1. 用户名模糊查询
            if (param.getUsernameFuzzy() != null && !param.getUsernameFuzzy().isEmpty()) {
                String safeKeyword = param.getUsernameFuzzy()
                        .replace("'", "''")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                conditions.add("u.username LIKE '%" + safeKeyword + "%'");
            }

            // 2. 用户姓名模糊查询
            if (param.getNameFuzzy() != null && !param.getNameFuzzy().isEmpty()) {
                String safeKeyword = param.getNameFuzzy()
                        .replace("'", "''")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                conditions.add("u.name LIKE '%" + safeKeyword + "%'");
            }

            // 3. 用户手机号模糊查询
            if (param.getPhoneFuzzy() != null && !param.getPhoneFuzzy().isEmpty()) {
                String safeKeyword = param.getPhoneFuzzy()
                        .replace("'", "''")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                conditions.add("u.phone LIKE '%" + safeKeyword + "%'");
            }


            // 4. 用户角色精确查询
            if (param.getRole() != null && !param.getRole().isEmpty()) {
                // 等值查询不需要转义特殊字符
                conditions.add("u.role = '" + param.getRole() + "'");
            }


            // 组合所有条件
            System.out.println("conditions: " + conditions);
            String whereConditions = conditions.isEmpty() ? null : String.join(" AND ", conditions);
            System.out.println("whereConditions: " + whereConditions);

            // 查询总记录数
            Long total = userMapper.selectJoinCount("t_user u", null, whereConditions);
            if (total == null) total = 0L;


            // 创建分页对象
            if(param.getSimplePage() == null) {
                SimplePage<User> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
                param.setSimplePage(sPage);
            }
            // 设置分页结果
            page.setTotal(total);
            page.setCurrent(param.getCurrentPage());
            page.setPageSize(param.getPageSize());
            page.setTotalPages(param.getSimplePage().getTotalPages());

            // 执行多表联查（带分页）
            int offset = (param.getCurrentPage() - 1) * param.getPageSize();
            List<Map<String, Object>> result = userMapper.selectJoinPage(
                    "t_user u",
                    "u.*", // 选择列：产品所有字段 + 分类名称
                    null,
                    whereConditions,
                    "u.id DESC", // 按商品ID倒序排列
                    offset,
                    param.getPageSize()
            );

            // 转换Map结果到Order对象
            List<User> userList = new ArrayList<>();
            for (Map<String, Object> map : result) {
                User user = new User();
                // 设置订单基础字段
                user.setId((Long) map.get("id"));
                user.setUsername((String) map.get("username"));
                user.setPassword((String) map.get("password"));
                user.setName((String) map.get("name"));
                user.setEmail((String) map.get("email"));
                user.setPhone((String) map.get("phone"));
                user.setAddress((String) map.get("address"));
                 user.setRole((String) map.get("role"));
                 user.setAvatar((String) map.get("avatar"));

                 // 处理balance字段类型转换
                 Object balanceObj = map.get("balance");
                 if (balanceObj instanceof Integer) {
                     user.setBalance((Integer) balanceObj);
                 } else if (balanceObj instanceof Long) {
                     user.setBalance(((Long) balanceObj).intValue());
                 } else if (balanceObj instanceof BigDecimal) {
                     user.setBalance(((BigDecimal) balanceObj).intValue());
                 } else {
                     user.setBalance(0); // 不能转换时设置为默认值0
                 }

                // 修复时间字段的类型转换 - 处理Timestamp到Date的转换
                Object createdAtObj = map.get("created_at");
                if (createdAtObj instanceof java.sql.Timestamp) {
                    user.setCreatedAt(new Date(((java.sql.Timestamp) createdAtObj).getTime()));
                } else if (createdAtObj instanceof java.sql.Date) {
                    user.setCreatedAt((Date) createdAtObj);
                }

                Object updatedAtObj = map.get("updated_at");
                if (updatedAtObj instanceof java.sql.Timestamp) {
                    user.setUpdatedAt(new Date(((java.sql.Timestamp) updatedAtObj).getTime()));
                } else if (updatedAtObj instanceof java.sql.Date) {
                    user.setUpdatedAt((Date) updatedAtObj);
                }

                // 设置关联的用户名
                user.setUsername((String) map.get("username"));

                userList.add(user);
            }
            page.setList(userList);



        } catch (Exception e) {
            e.printStackTrace(); // 打印详细异常信息
            System.out.println("分页查询失败: " + e.getMessage());
            return new PaginationResultVO<>();
        }
        return page;
    }

	/** 
	 * 新增
	 */
	@Override
	public Long add(User bean){
		return userMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<User> list){
		return userMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<User> list){
		return userMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public User findById(Long id){
		return userMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( User entity, Long id){
		return userMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return userMapper.deleteById(id);
	}

	/**
	 * 根据用户名查找用户
	 */
	@Override
	public User findByUsername(String username) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		return userMapper.selectOne(queryWrapper);
	}

	/**
	 * 根据邮箱查找用户
	 */
	@Override
	public User findByEmail(String email) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("email", email);
		return userMapper.selectOne(queryWrapper);
	}

	/**
	 * 根据手机号查找用户
	 */
	@Override
	public User findByPhone(String phone) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("phone", phone);
		return userMapper.selectOne(queryWrapper);
	}
}