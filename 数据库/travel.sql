/*
 Navicat Premium Dump SQL

 Source Server         : bomo
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : travel

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 30/09/2025 14:04:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_attraction
-- ----------------------------
DROP TABLE IF EXISTS `t_attraction`;
CREATE TABLE `t_attraction`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '景点ID（主键，自增）',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '景点名称（如“故宫博物院”）',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '景点所在城市（如“北京”）',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '景点所在省份（如“北京市”“江苏省”）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '景点详细描述（如历史背景、游玩亮点）',
  `opening_hours` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '景点开放时间（如“08:30-17:00，周一闭馆”）',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '景点详细地址（用于导航）',
  `rating` decimal(3, 2) NULL DEFAULT NULL COMMENT '景点综合评分（0.00-5.00，由用户评价汇总计算）',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '景点封面图URL（用于前端展示）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '景点信息创建时间（如录入系统时间）',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '景点信息更新时间（如修改开放时间、评分时自动更新）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '旅游景点表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_attraction
-- ----------------------------
INSERT INTO `t_attraction` VALUES (1, '故宫博物院', '北京', '北京市', '中国明清两代的皇家宫殿，世界五大宫之首，拥有大量珍贵文物和历史建筑。', '08:30-17:00，周一闭馆（法定节假日除外）', '北京市东城区景山前街4号', 4.80, 'attraction\\img (1).jpg', '2025-09-20 00:40:02', '2025-09-21 16:57:00');
INSERT INTO `t_attraction` VALUES (2, '八达岭长城', '北京', '北京市', '明长城的精华段，居庸关的前哨，是游览长城的首选之地。', '07:30-17:30，冬季07:30-17:00', '北京市延庆区军都山关沟古道北口', 4.70, 'attraction\\img (2).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (3, '颐和园', '北京', '北京市', '保存最完整的一座皇家行宫御苑，被誉为\"皇家园林博物馆\"。', '06:30-18:00（4月1日-10月31日），07:00-17:00（11月1日-3月31日）', '北京市海淀区新建宫门路19号', 4.60, 'attraction\\img (3).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (4, '上海迪士尼乐园', '上海', '上海市', '中国内地首座迪士尼主题乐园，包含六大主题园区，适合全家游玩。', '08:30-21:00（夏季），09:00-20:30（冬季）', '上海市浦东新区川沙新镇黄赵路310号', 4.90, 'attraction\\img (4).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (5, '外滩', '上海', '上海市', '上海的城市名片，汇集了各种风格的建筑，夜晚灯光璀璨。', '全天开放，景观灯19:00-23:00', '上海市黄浦区中山东一路', 4.70, 'attraction\\img (5).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (6, '东方明珠广播电视塔', '上海', '上海市', '上海标志性文化景观之一，可360°观赏城市景观。', '09:00-21:00', '上海市浦东新区世纪大道1号', 4.50, 'attraction\\img (6).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (7, '广州塔', '广州', '广东省', '中国第一高塔，世界第二高塔，可俯瞰广州全景。', '09:30-22:30', '广州市海珠区阅江西路222号', 4.60, 'attraction\\img (7).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (8, '长隆野生动物世界', '广州', '广东省', '中国最具国际水准的野生动物园，拥有大熊猫三胞胎。', '09:30-18:00', '广州市番禺区大石镇105国道大石段593号', 4.80, 'attraction\\img (8).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (9, '深圳世界之窗', '深圳', '广东省', '集世界奇观、历史遗迹、古今名胜于一体的大型主题公园。', '09:00-22:30（节假日），09:00-22:00（平日）', '深圳市南山区华侨城深南大道9037号', 4.40, 'attraction\\img (9).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (10, '杭州西湖', '杭州', '浙江省', '中国大陆首批国家重点风景名胜区，有\"人间天堂\"之称。', '全天开放，部分景点8:00-17:00', '杭州市西湖区西湖风景区', 4.90, 'attraction\\img (10).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (11, '苏州园林（拙政园）', '苏州', '江苏省', '江南古典园林的代表作品，中国四大名园之首。', '07:30-17:30（3月1日-11月15日），07:30-17:00（11月16日-2月28日）', '苏州市姑苏区东北街178号', 4.70, 'attraction\\img (11).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (12, '南京夫子庙', '南京', '江苏省', '中国四大文庙之一，集旅游观光、美食购物于一体。', '全天开放，夫子庙大成殿8:00-21:30', '南京市秦淮区秦淮河北岸贡院街', 4.50, 'attraction\\img (12).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (13, '成都大熊猫繁育研究基地', '成都', '四川省', '中国政府实施大熊猫等濒危野生动物迁地保护工程的主要研究基地之一。', '07:30-18:00（4月-10月），08:00-17:30（11月-3月）', '成都市成华区熊猫大道1375号', 4.80, 'attraction\\img (13).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (14, '九寨沟风景区', '阿坝', '四川省', '世界自然遗产，以典型的高山湖泊、瀑布、钙化滩流等水景为主体。', '08:30-17:00（4月1日-11月15日），09:00-17:00（11月16日-3月31日）', '四川省阿坝藏族羌族自治州九寨沟县漳扎镇', 4.90, 'attraction\\img (14).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (15, '武汉东湖生态旅游区', '武汉', '湖北省', '中国最大的城中湖，湖光山色，风景秀丽。', '全天开放，部分景点9:00-17:00', '武汉市武昌区东湖路特1号', 4.60, 'attraction\\img (15).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (16, '重庆洪崖洞', '重庆', '重庆市', '以最具巴渝传统建筑特色的吊脚楼为主体，依山就势，沿江而建。', '11:00-23:00', '重庆市渝中区解放碑沧白路', 4.70, 'attraction\\img (16).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (17, '西安秦始皇兵马俑博物馆', '西安', '陕西省', '世界第八大奇迹，是中国历史上第一位皇帝的陵墓陪葬坑。', '08:30-18:00（3月16日-11月15日），08:30-17:30（11月16日-3月15日）', '西安市临潼区秦陵北路', 4.80, 'attraction\\img (17).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (18, '青岛海滨风景区', '青岛', '山东省', '中国唯一享有\"世界最美海湾\"美誉的景区，包括八大关、栈桥等景点。', '全天开放', '青岛市市南区海滨', 4.70, 'attraction\\img (18).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (19, '长沙橘子洲头', '长沙', '湖南省', '位于湘江江心，是长沙市的重要名胜。', '07:00-22:00', '长沙市岳麓区橘子洲头2号', 4.60, 'attraction\\img (19).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (20, '哈尔滨冰雪大世界', '哈尔滨', '黑龙江省', '世界上最大的冰雪主题游乐园，每年冬季开放。', '11:00-21:30（12月下旬-2月下旬）', '哈尔滨市松北区太阳岛西侧', 4.80, 'attraction\\img (20).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (21, '郑州嵩山少林寺', '郑州', '河南省', '中国佛教禅宗祖庭和中国功夫的发源地。', '08:00-17:00（3月-10月），08:00-16:30（11月-2月）', '郑州市登封市嵩山少林风景区', 4.60, 'attraction\\img (21).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (22, '沈阳故宫', '沈阳', '辽宁省', '中国仅存的两大宫殿建筑群之一，又称盛京皇宫。', '08:30-17:30（4月10日-10月10日），09:00-16:30（10月11日-4月9日）', '沈阳市沈河区沈阳路171号', 4.50, 'attraction\\img (22).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (23, '合肥黄山风景区', '黄山', '安徽省', '世界文化与自然双重遗产，以奇松、怪石、云海、温泉、冬雪\"五绝\"著称。', '06:30-17:00（3月-11月），07:00-16:30（12月-2月）', '安徽省黄山市黄山区汤口镇', 4.90, 'attraction\\img (23).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (24, '福州三坊七巷', '福州', '福建省', '中国十大历史文化名街之一，保存有大量明清建筑。', '全天开放，部分景点8:30-17:00', '福州市鼓楼区南后街', 4.50, 'attraction\\img (24).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (25, '南宁青秀山风景区', '南宁', '广西壮族自治区', '南宁市最著名的风景区，被誉为\"南宁市的绿肺\"。', '06:00-24:00', '南宁市青秀区凤岭南路6号', 4.60, 'attraction\\img (25).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (26, '昆明石林风景区', '昆明', '云南省', '世界喀斯特地貌的精华，世界自然遗产。', '08:00-18:00（3月-10月），08:00-17:30（11月-2月）', '云南省昆明市石林彝族自治县石林中路', 4.70, 'attraction\\img (26).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (27, '贵阳黄果树瀑布', '安顺', '贵州省', '中国最大的瀑布，也是世界著名大瀑布之一。', '07:30-18:00（3月1日-11月30日），08:00-17:30（12月1日-2月28日）', '贵州省安顺市镇宁布依族苗族自治县', 4.80, 'attraction\\img (27).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (28, '拉萨布达拉宫', '拉萨', '西藏自治区', '世界上海拔最高的宫殿式建筑群，世界文化遗产。', '09:00-16:00（需提前预约）', '西藏自治区拉萨市城关区北京中路35号', 4.90, 'attraction\\img (28).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (29, '兰州黄河风情线', '兰州', '甘肃省', '沿黄河两岸打造的城市风情线，包括中山桥、白塔山等景点。', '全天开放', '兰州市城关区黄河沿岸', 4.40, 'attraction\\img (29).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (30, '西宁青海湖', '海南', '青海省', '中国最大的内陆湖和咸水湖，风景壮丽。', '全天开放，最佳旅游季节5-10月', '青海省海南藏族自治州共和县', 4.80, 'attraction\\img (30).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (31, '银川西夏王陵', '银川', '宁夏回族自治区', '西夏王朝的皇家陵寝，有\"东方金字塔\"之称。', '08:00-18:30（4月1日-10月31日），08:30-17:30（11月1日-3月31日）', '宁夏回族自治区银川市西夏区贺兰山东麓', 4.30, 'attraction\\img (31).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (32, '乌鲁木齐天山天池', '昌吉', '新疆维吾尔自治区', '世界自然遗产，以高山湖泊为中心的自然风景区。', '08:30-19:00（4月-10月），09:00-18:00（11月-3月）', '新疆维吾尔自治区昌吉回族自治州阜康市', 4.70, 'attraction\\img (32).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (33, '香港迪士尼乐园', '香港', '香港特别行政区', '全球第五座、亚洲第二座迪士尼乐园。', '10:00-21:00', '香港特别行政区新界大屿山竹篙湾', 4.80, 'attraction\\img (33).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (34, '澳门大三巴牌坊', '澳门', '澳门特别行政区', '澳门的标志性建筑，原为圣保禄大教堂的前壁。', '09:00-18:00', '澳门特别行政区澳门半岛大三巴街', 4.60, 'attraction\\img (34).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (35, '台北故宫博物院', '台北', '台湾省', '收藏了大量中国历史文物，与北京故宫、沈阳故宫并称中国三大故宫。', '09:00-17:00，周一闭馆', '台湾省台北市士林区至善路二段221号', 4.70, 'attraction\\img (35).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (36, '石家庄正定古城', '石家庄', '河北省', '历史上曾与北京、保定并称\"北方三雄镇\"。', '全天开放，部分景点8:30-17:30', '河北省石家庄市正定县', 4.40, 'attraction\\img (36).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (37, '太原晋祠', '太原', '山西省', '中国现存最早的皇家祭祀园林，集中国古代祭祀建筑、园林、雕塑艺术为一体。', '08:30-17:30（4月-10月），08:30-17:00（11月-3月）', '山西省太原市晋源区晋祠镇', 4.50, 'attraction\\img (37).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (38, '呼和浩特昭君博物院', '呼和浩特', '内蒙古自治区', '纪念王昭君的博物馆，展示了昭君出塞的历史。', '09:00-17:00', '内蒙古自治区呼和浩特市玉泉区昭君路6公里处', 4.30, 'attraction\\img (38).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (39, '南昌滕王阁', '南昌', '江西省', '江南三大名楼之首，因王勃的《滕王阁序》而闻名。', '08:00-18:30（4月-10月），08:00-17:30（11月-3月）', '江西省南昌市东湖区仿古街58号', 4.40, 'attraction\\img (39).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');
INSERT INTO `t_attraction` VALUES (40, '济南趵突泉', '济南', '山东省', '济南三大名胜之一，被誉为\"天下第一泉\"。', '07:00-19:00（4月-10月），07:00-18:00（11月-3月）', '山东省济南市历下区趵突泉南路1号', 4.50, 'attraction\\img (40).jpg', '2025-09-20 00:40:02', '2025-09-21 17:02:35');

-- ----------------------------
-- Table structure for t_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车记录ID（主键，自增）',
  `user_id` bigint NOT NULL COMMENT '用户ID，关联t_user.id（标识该购物车记录所属用户）',
  `ticket_id` bigint NOT NULL COMMENT '门票ID，关联t_ticket.id（标识待购买的门票，间接关联景点）',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '购票数量（至少1张，由CHECK约束限制quantity>=1）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '门票加入购物车的时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '购物车记录更新时间（如修改购票数量时自动更新）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_ticket`(`user_id` ASC, `ticket_id` ASC) USING BTREE COMMENT '唯一索引：限制同一用户不能重复添加同一门票到购物车',
  INDEX `idx_ticket_id`(`ticket_id` ASC) USING BTREE COMMENT '普通索引：优化通过门票ID查询购物车记录的效率',
  CONSTRAINT `t_cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_cart_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `t_ticket` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_cart_chk_1` CHECK (`quantity` >= 1)
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表：存储用户待购买的门票信息，支持修改数量、结算等操作' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_cart
-- ----------------------------
INSERT INTO `t_cart` VALUES (2, 1, 5, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (3, 2, 3, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (4, 2, 7, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (5, 3, 4, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (6, 3, 8, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (7, 4, 2, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (8, 4, 6, 4, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (9, 5, 9, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (10, 5, 12, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (11, 6, 10, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (12, 6, 15, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (13, 7, 11, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (14, 7, 16, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (15, 8, 13, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (16, 8, 18, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (17, 9, 14, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (18, 9, 19, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (19, 10, 17, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (20, 10, 20, 4, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (21, 11, 21, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (22, 11, 25, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (23, 12, 22, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (24, 12, 26, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (25, 13, 23, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (26, 13, 27, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (27, 14, 24, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (28, 14, 28, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (29, 15, 29, 4, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (30, 15, 32, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (31, 16, 30, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (32, 16, 33, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (33, 17, 31, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (34, 17, 34, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (35, 18, 35, 3, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (36, 18, 37, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (37, 19, 36, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (38, 19, 38, 4, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (39, 20, 39, 2, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (40, 20, 40, 1, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_cart` VALUES (42, 1, 16, 1, '2025-09-21 00:20:31', '2025-09-21 00:20:37');
INSERT INTO `t_cart` VALUES (48, 1, 14, 1, '2025-09-22 18:04:46', '2025-09-22 18:04:46');
INSERT INTO `t_cart` VALUES (68, 1, 29, 3, '2025-09-24 17:39:40', '2025-09-24 17:39:40');

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID（主键，自增）',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号（唯一，如“ORD20240919001”，用于用户查询、系统对账）',
  `user_id` bigint NOT NULL COMMENT '用户ID，关联t_user.id（标识订单所属用户）',
  `ticket_id` bigint NOT NULL COMMENT '门票ID，关联t_ticket.id（标识订单对应的门票）',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '购票数量（至少1张，与购物车数量一致）',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额（=门票单价×数量，折扣价优先，保留2位小数）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态：UNPAID(未支付)、PAID(已支付)、DELIVERED(已出票)、COMPLETED(已消费)、CANCELLED(已取消)、REFUND(申请退款)、REFUND_REJECTED(退款驳回)',
  `visit_date` date NOT NULL COMMENT '计划游玩日期（用户选择的门票使用日期，需符合景点开放时间）',
  `contact_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人姓名（用于门票核对、售后联系）',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人手机号（用于接收验证码、订单通知）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间（用户提交订单的时间）',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单更新时间（如支付、取消、退款时自动更新）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注（用户自定义备注，如“携带老人，需优先入场”）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE COMMENT '唯一索引：确保订单编号不重复，优化订单查询效率',
  INDEX `user_id`(`user_id` ASC) USING BTREE COMMENT '普通索引：优化通过用户ID查询“我的订单”的效率',
  INDEX `ticket_id`(`ticket_id` ASC) USING BTREE COMMENT '普通索引：优化通过门票ID统计订单量的效率',
  CONSTRAINT `t_order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_order_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `t_ticket` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_order_chk_1` CHECK (`status` in (_utf8mb4'UNPAID',_utf8mb4'PAID',_utf8mb4'DELIVERED',_utf8mb4'COMPLETED',_utf8mb4'CANCELLED',_utf8mb4'REFUND',_utf8mb4'REFUND_REJECTED'))
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表：存储用户购票的核心信息，记录订单全生命周期状态' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (1, 'ORD20250920001', 1, 1, 2, 110.00, 'COMPLETED', '2025-09-10', '李明', '13800138001', '2025-09-08 10:30:00', '2025-09-10 18:00:00', '无特殊要求');
INSERT INTO `t_order` VALUES (2, 'ORD20250920002', 2, 5, 1, 40.00, 'COMPLETED', '2025-09-09', '王红', '13900139002', '2025-09-07 14:15:00', '2025-09-09 17:30:00', NULL);
INSERT INTO `t_order` VALUES (3, 'ORD20250920003', 3, 3, 2, 60.00, 'COMPLETED', '2025-09-08', '张伟', '13700137003', '2025-09-06 09:20:00', '2025-09-08 16:45:00', '携带老人');
INSERT INTO `t_order` VALUES (4, 'ORD20250920004', 4, 7, 1, 22.00, 'COMPLETED', '2025-09-07', '刘杰', '13600136004', '2025-09-05 15:30:00', '2025-09-07 15:10:00', NULL);
INSERT INTO `t_order` VALUES (5, 'ORD20250920005', 5, 4, 3, 90.00, 'COMPLETED', '2025-09-06', '晨曦', '13500135005', '2025-09-04 11:20:00', '2025-09-06 14:30:00', NULL);
INSERT INTO `t_order` VALUES (6, 'ORD20250920006', 6, 8, 2, 40.00, 'COMPLETED', '2025-09-05', '赵阳', '13400134006', '2025-09-03 16:45:00', '2025-09-05 13:20:00', NULL);
INSERT INTO `t_order` VALUES (7, 'ORD20250920007', 7, 2, 1, 28.00, 'COMPLETED', '2025-09-04', '黄娟', '13300133007', '2025-09-02 09:15:00', '2025-09-04 12:10:00', NULL);
INSERT INTO `t_order` VALUES (8, 'ORD20250920008', 8, 6, 4, 100.00, 'COMPLETED', '2025-09-03', '吴东', '13200132008', '2025-09-01 14:30:00', '2025-09-03 11:20:00', '团体票');
INSERT INTO `t_order` VALUES (9, 'ORD20250920009', 9, 9, 2, 56.00, 'COMPLETED', '2025-09-02', '周梅', '13100131009', '2025-08-31 10:20:00', '2025-09-02 10:15:00', NULL);
INSERT INTO `t_order` VALUES (10, 'ORD20250920010', 10, 12, 1, 379.00, 'COMPLETED', '2025-09-01', '徐磊', '13000130010', '2025-08-30 16:45:00', '2025-09-01 16:30:00', NULL);
INSERT INTO `t_order` VALUES (11, 'ORD20250920011', 11, 10, 2, 30.00, 'COMPLETED', '2025-08-31', '孙颖', '13800138011', '2025-08-29 09:15:00', '2025-08-31 15:45:00', NULL);
INSERT INTO `t_order` VALUES (12, 'ORD20250920012', 12, 15, 3, 837.00, 'COMPLETED', '2025-08-30', '马华', '13900139012', '2025-08-28 13:30:00', '2025-08-30 14:30:00', NULL);
INSERT INTO `t_order` VALUES (13, 'ORD20250920013', 13, 11, 1, 55.00, 'COMPLETED', '2025-08-29', '高峰', '13700137013', '2025-08-27 11:20:00', '2025-08-29 13:20:00', NULL);
INSERT INTO `t_order` VALUES (14, 'ORD20250920014', 14, 16, 2, 0.00, 'COMPLETED', '2025-08-28', '鹏飞', '13600136014', '2025-08-26 15:45:00', '2025-08-28 12:10:00', NULL);
INSERT INTO `t_order` VALUES (15, 'ORD20250920015', 15, 13, 2, 558.00, 'COMPLETED', '2025-08-27', '闫梅', '13500135015', '2025-08-25 08:30:00', '2025-08-27 11:20:00', NULL);
INSERT INTO `t_order` VALUES (16, 'ORD20250920016', 16, 18, 1, 99.00, 'COMPLETED', '2025-08-26', '何军', '13400134016', '2025-08-24 14:15:00', '2025-08-26 10:15:00', NULL);
INSERT INTO `t_order` VALUES (17, 'ORD20250920017', 17, 14, 3, 897.00, 'COMPLETED', '2025-08-25', '鲁强', '13300133017', '2025-08-23 10:20:00', '2025-08-25 16:30:00', NULL);
INSERT INTO `t_order` VALUES (18, 'ORD20250920018', 18, 19, 2, 540.00, 'COMPLETED', '2025-08-24', '方婷', '13200132018', '2025-08-22 16:30:00', '2025-08-24 15:45:00', NULL);
INSERT INTO `t_order` VALUES (19, 'ORD20250920019', 19, 17, 1, 180.00, 'COMPLETED', '2025-08-23', '袁波', '13100131019', '2025-08-21 09:45:00', '2025-08-23 14:30:00', NULL);
INSERT INTO `t_order` VALUES (20, 'ORD20250920020', 20, 20, 4, 540.00, 'COMPLETED', '2025-08-22', '唐杰', '13000130020', '2025-08-20 15:10:00', '2025-08-22 13:20:00', NULL);
INSERT INTO `t_order` VALUES (21, 'ORD20250920021', 21, 21, 2, 150.00, 'PAID', '2025-09-25', '杜伟', '13800138021', '2025-09-18 14:30:00', '2025-09-18 14:35:00', NULL);
INSERT INTO `t_order` VALUES (22, 'ORD20250920022', 22, 25, 1, 700.00, 'PAID', '2025-09-26', '朱颖', '13900139022', '2025-09-17 11:20:00', '2025-09-17 11:25:00', NULL);
INSERT INTO `t_order` VALUES (23, 'ORD20250920023', 23, 22, 3, 615.00, 'DELIVERED', '2025-09-27', '邵华', '13700137023', '2025-09-16 09:15:00', '2025-09-16 09:20:00', NULL);
INSERT INTO `t_order` VALUES (24, 'ORD20250920024', 24, 26, 2, 400.00, 'DELIVERED', '2025-09-28', '文静', '13600136024', '2025-09-15 16:45:00', '2025-09-15 16:50:00', NULL);
INSERT INTO `t_order` VALUES (25, 'ORD20250920025', 25, 23, 1, 280.00, 'UNPAID', '2025-09-29', '曹宇', '13500135025', '2025-09-14 10:20:00', '2025-09-14 10:20:00', NULL);
INSERT INTO `t_order` VALUES (26, 'ORD20250920026', 26, 27, 3, 330.00, 'UNPAID', '2025-09-30', '金辉', '13400134026', '2025-09-13 15:30:00', '2025-09-13 15:30:00', NULL);
INSERT INTO `t_order` VALUES (27, 'ORD20250920027', 27, 24, 2, 420.00, 'CANCELLED', '2025-09-24', '邱珊', '13300133027', '2025-09-12 17:45:00', '2025-09-13 09:10:00', '行程变更');
INSERT INTO `t_order` VALUES (28, 'ORD20250920028', 28, 28, 1, 140.00, 'CANCELLED', '2025-09-23', '宋涛', '13200132028', '2025-09-11 10:20:00', '2025-09-12 14:20:00', '行程变更');
INSERT INTO `t_order` VALUES (29, 'ORD20250920029', 29, 29, 4, 0.00, 'REFUND', '2025-09-22', '谢军', '13100131029', '2025-09-10 14:15:00', '2025-09-11 16:30:00', NULL);
INSERT INTO `t_order` VALUES (30, 'ORD20250920030', 30, 32, 2, 70.00, 'REFUND', '2025-09-21', '雷敏', '13000130030', '2025-09-09 08:30:00', '2025-09-10 10:15:00', NULL);
INSERT INTO `t_order` VALUES (31, 'ORD20250920031', 31, 30, 1, 45.00, 'REFUND_REJECTED', '2025-09-20', '戴红', '13800138031', '2025-09-08 13:20:00', '2025-09-09 15:45:00', NULL);
INSERT INTO `t_order` VALUES (32, 'ORD20250920032', 32, 33, 3, 84.00, 'REFUND_REJECTED', '2025-09-19', '林杰', '13900139032', '2025-09-07 19:45:00', '2025-09-08 14:30:00', NULL);
INSERT INTO `t_order` VALUES (33, 'ORD20250920033', 33, 31, 2, 130.00, 'PAID', '2025-10-01', '姜维', '13700137033', '2025-09-19 10:20:00', '2025-09-19 10:25:00', NULL);
INSERT INTO `t_order` VALUES (34, 'ORD20250920034', 34, 34, 1, 75.00, 'PAID', '2025-10-02', '樊雅', '13600136034', '2025-09-18 15:30:00', '2025-09-18 15:35:00', NULL);
INSERT INTO `t_order` VALUES (35, 'ORD20250920035', 35, 35, 3, 150.00, 'DELIVERED', '2025-10-03', '郑亮', '13500135035', '2025-09-17 16:45:00', '2025-09-17 16:50:00', NULL);
INSERT INTO `t_order` VALUES (36, 'ORD20250920036', 36, 37, 2, 300.00, 'DELIVERED', '2025-10-04', '王国', '13400134036', '2025-09-16 09:15:00', '2025-09-16 09:20:00', NULL);
INSERT INTO `t_order` VALUES (37, 'ORD20250920037', 37, 36, 1, 27.50, 'UNPAID', '2025-10-05', '常虹', '13300133037', '2025-09-15 14:30:00', '2025-09-15 14:30:00', NULL);
INSERT INTO `t_order` VALUES (38, 'ORD20250920038', 38, 38, 4, 340.00, 'UNPAID', '2025-10-06', '魏霞', '13200132038', '2025-09-14 11:20:00', '2025-09-14 11:20:00', NULL);
INSERT INTO `t_order` VALUES (39, 'ORD20250920039', 39, 39, 2, 0.00, 'COMPLETED', '2025-08-20', '钟浩', '13100131039', '2025-08-18 14:15:00', '2025-08-20 16:30:00', NULL);
INSERT INTO `t_order` VALUES (40, 'ORD20250920040', 40, 40, 1, 30.00, 'COMPLETED', '2025-08-19', '佟磊', '13000130040', '2025-08-17 10:20:00', '2025-08-19 15:45:00', NULL);
INSERT INTO `t_order` VALUES (41, 'ORD1758418629571607', 1, 4, 1, 30.00, 'CANCELLED', '2025-09-30', '李明', '13800138001', '2025-09-21 09:37:09', '2025-09-21 10:10:37', '会 | 退票原因: 不想要了 | 退票审核通过，已退款: ');
INSERT INTO `t_order` VALUES (42, 'ORD1758420172962155', 1, 1, 2, 120.00, 'PAID', '2025-09-22', '李明', '13800138001', '2025-09-21 10:02:52', '2025-09-21 10:03:28', '景点：故宫博物院，门票类型：ADULT');
INSERT INTO `t_order` VALUES (43, 'ORD1758421373603165', 1, 37, 3, 507.00, 'PAID', '2025-10-07', '李明', '13800138001', '2025-09-21 10:22:53', '2025-09-22 23:36:57', '景点：九寨沟风景区，门票类型：ADULT');
INSERT INTO `t_order` VALUES (44, 'ORD1758555394874813', 1, 1, 4, 240.00, 'CANCELLED', '2025-09-29', '李明', '13800138001', '2025-09-22 23:36:34', '2025-09-22 23:37:02', '景点：故宫博物院，门票类型：ADULT | 退票原因: 步行去了 | 退票审核通过，已退款: 同意');
INSERT INTO `t_order` VALUES (45, 'ORD1758555394875147', 1, 2, 4, 120.00, 'PAID', '2025-09-26', '李明', '13800138001', '2025-09-22 23:36:34', '2025-09-22 23:37:00', '景点：故宫博物院，门票类型：CHILD');
INSERT INTO `t_order` VALUES (46, 'ORD1758555394875607', 1, 4, 3, 90.00, 'DELIVERED', '2025-09-23', '李明', '13800138001', '2025-09-22 23:36:34', '2025-09-25 10:00:29', '景点：故宫博物院，门票类型：ELDERLY');
INSERT INTO `t_order` VALUES (47, 'ORD1758555394875813', 1, 3, 6, 180.00, 'DELIVERED', '2025-09-24', '李明', '13800138001', '2025-09-22 23:36:34', '2025-09-24 15:45:56', '景点：故宫博物院，门票类型：STUDENT');
INSERT INTO `t_order` VALUES (48, 'ORD1758700551538459', 1, 12, 2, 798.00, 'DELIVERED', '2025-09-29', '李明(测试)', '13800138001', '2025-09-24 15:55:51', '2025-09-24 16:07:07', '有老人');
INSERT INTO `t_order` VALUES (49, 'ORD1758766475866273', 1, 12, 1, 399.00, 'DELIVERED', '2025-09-26', '李明', '13800138001', '2025-09-25 10:14:35', '2025-09-25 10:17:23', '有老人,已发货');

-- ----------------------------
-- Table structure for t_payment
-- ----------------------------
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付记录ID（主键，自增）',
  `order_id` bigint NOT NULL COMMENT '订单ID，关联t_order.id（标识该支付对应的订单，一对一关联）',
  `payment_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号（唯一，如“PAY20240919001”，对接支付网关时的外部单号）',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式（如“WECHAT_PAY”微信支付、“ALIPAY”支付宝、“BALANCE”余额支付）',
  `amount` decimal(10, 2) NOT NULL COMMENT '支付金额（与订单总金额一致，保留2位小数）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付状态（如“PENDING”待支付、“SUCCESS”支付成功、“FAIL”支付失败、“REFUNDED”已退款）',
  `payment_time` timestamp NULL DEFAULT NULL COMMENT '实际支付时间（支付成功时记录，失败则为NULL）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付记录创建时间（发起支付请求的时间）',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '支付记录更新时间（如支付状态变更时自动更新）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_id`(`order_id` ASC) USING BTREE COMMENT '唯一索引：确保一个订单只对应一条支付记录',
  UNIQUE INDEX `payment_no`(`payment_no` ASC) USING BTREE COMMENT '唯一索引：确保支付单号不重复，用于与支付网关对账',
  CONSTRAINT `t_payment_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表：存储订单的支付详情，对接支付网关，同步支付状态' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_payment
-- ----------------------------
INSERT INTO `t_payment` VALUES (1, 1, 'PAY20250920001', 'WECHAT_PAY', 110.00, 'SUCCESS', '2025-09-08 10:35:00', '2025-09-08 10:30:00', '2025-09-08 10:35:00');
INSERT INTO `t_payment` VALUES (2, 2, 'PAY20250920002', 'ALIPAY', 40.00, 'SUCCESS', '2025-09-07 14:20:00', '2025-09-07 14:15:00', '2025-09-07 14:20:00');
INSERT INTO `t_payment` VALUES (3, 3, 'PAY20250920003', 'WECHAT_PAY', 60.00, 'SUCCESS', '2025-09-06 09:25:00', '2025-09-06 09:20:00', '2025-09-06 09:25:00');
INSERT INTO `t_payment` VALUES (4, 4, 'PAY20250920004', 'BALANCE', 22.00, 'SUCCESS', '2025-09-05 15:35:00', '2025-09-05 15:30:00', '2025-09-05 15:35:00');
INSERT INTO `t_payment` VALUES (5, 5, 'PAY20250920005', 'ALIPAY', 90.00, 'SUCCESS', '2025-09-04 11:25:00', '2025-09-04 11:20:00', '2025-09-04 11:25:00');
INSERT INTO `t_payment` VALUES (6, 6, 'PAY20250920006', 'WECHAT_PAY', 40.00, 'SUCCESS', '2025-09-03 16:50:00', '2025-09-03 16:45:00', '2025-09-03 16:50:00');
INSERT INTO `t_payment` VALUES (7, 7, 'PAY20250920007', 'ALIPAY', 28.00, 'SUCCESS', '2025-09-02 09:20:00', '2025-09-02 09:15:00', '2025-09-02 09:20:00');
INSERT INTO `t_payment` VALUES (8, 8, 'PAY20250920008', 'WECHAT_PAY', 100.00, 'SUCCESS', '2025-09-01 14:35:00', '2025-09-01 14:30:00', '2025-09-01 14:35:00');
INSERT INTO `t_payment` VALUES (9, 9, 'PAY20250920009', 'ALIPAY', 56.00, 'SUCCESS', '2025-08-31 10:25:00', '2025-08-31 10:20:00', '2025-08-31 10:25:00');
INSERT INTO `t_payment` VALUES (10, 10, 'PAY20250920010', 'WECHAT_PAY', 379.00, 'SUCCESS', '2025-08-30 16:50:00', '2025-08-30 16:45:00', '2025-08-30 16:50:00');
INSERT INTO `t_payment` VALUES (11, 11, 'PAY20250920011', 'ALIPAY', 30.00, 'SUCCESS', '2025-08-29 09:20:00', '2025-08-29 09:15:00', '2025-08-29 09:20:00');
INSERT INTO `t_payment` VALUES (12, 12, 'PAY20250920012', 'WECHAT_PAY', 837.00, 'SUCCESS', '2025-08-28 13:35:00', '2025-08-28 13:30:00', '2025-08-28 13:35:00');
INSERT INTO `t_payment` VALUES (13, 13, 'PAY20250920013', 'ALIPAY', 55.00, 'SUCCESS', '2025-08-27 11:25:00', '2025-08-27 11:20:00', '2025-08-27 11:25:00');
INSERT INTO `t_payment` VALUES (14, 14, 'PAY20250920014', 'BALANCE', 0.00, 'SUCCESS', '2025-08-26 15:50:00', '2025-08-26 15:45:00', '2025-08-26 15:50:00');
INSERT INTO `t_payment` VALUES (15, 15, 'PAY20250920015', 'WECHAT_PAY', 558.00, 'SUCCESS', '2025-08-25 08:35:00', '2025-08-25 08:30:00', '2025-08-25 08:35:00');
INSERT INTO `t_payment` VALUES (16, 16, 'PAY20250920016', 'ALIPAY', 99.00, 'SUCCESS', '2025-08-24 14:20:00', '2025-08-24 14:15:00', '2025-08-24 14:20:00');
INSERT INTO `t_payment` VALUES (17, 17, 'PAY20250920017', 'WECHAT_PAY', 897.00, 'SUCCESS', '2025-08-23 10:25:00', '2025-08-23 10:20:00', '2025-08-23 10:25:00');
INSERT INTO `t_payment` VALUES (18, 18, 'PAY20250920018', 'ALIPAY', 540.00, 'SUCCESS', '2025-08-22 16:35:00', '2025-08-22 16:30:00', '2025-08-22 16:35:00');
INSERT INTO `t_payment` VALUES (19, 19, 'PAY20250920019', 'WECHAT_PAY', 180.00, 'SUCCESS', '2025-08-21 09:50:00', '2025-08-21 09:45:00', '2025-08-21 09:50:00');
INSERT INTO `t_payment` VALUES (20, 20, 'PAY20250920020', 'ALIPAY', 540.00, 'SUCCESS', '2025-08-20 15:15:00', '2025-08-20 15:10:00', '2025-08-20 15:15:00');
INSERT INTO `t_payment` VALUES (21, 21, 'PAY20250920021', 'WECHAT_PAY', 150.00, 'SUCCESS', '2025-09-18 14:35:00', '2025-09-18 14:30:00', '2025-09-18 14:35:00');
INSERT INTO `t_payment` VALUES (22, 22, 'PAY20250920022', 'ALIPAY', 700.00, 'SUCCESS', '2025-09-17 11:25:00', '2025-09-17 11:20:00', '2025-09-17 11:25:00');
INSERT INTO `t_payment` VALUES (23, 23, 'PAY20250920023', 'WECHAT_PAY', 615.00, 'SUCCESS', '2025-09-16 09:20:00', '2025-09-16 09:15:00', '2025-09-16 09:20:00');
INSERT INTO `t_payment` VALUES (24, 24, 'PAY20250920024', 'ALIPAY', 400.00, 'SUCCESS', '2025-09-15 16:50:00', '2025-09-15 16:45:00', '2025-09-15 16:50:00');
INSERT INTO `t_payment` VALUES (25, 25, 'PAY20250920025', 'WECHAT_PAY', 280.00, 'PENDING', NULL, '2025-09-14 10:20:00', '2025-09-14 10:20:00');
INSERT INTO `t_payment` VALUES (26, 26, 'PAY20250920026', 'ALIPAY', 330.00, 'PENDING', NULL, '2025-09-13 15:30:00', '2025-09-13 15:30:00');
INSERT INTO `t_payment` VALUES (27, 27, 'PAY20250920027', 'WECHAT_PAY', 420.00, 'REFUNDED', '2025-09-13 09:15:00', '2025-09-12 17:45:00', '2025-09-13 09:15:00');
INSERT INTO `t_payment` VALUES (28, 28, 'PAY20250920028', 'ALIPAY', 140.00, 'REFUNDED', '2025-09-12 14:25:00', '2025-09-11 10:20:00', '2025-09-12 14:25:00');
INSERT INTO `t_payment` VALUES (29, 29, 'PAY20250920029', 'WECHAT_PAY', 0.00, 'REFUNDED', '2025-09-11 16:35:00', '2025-09-10 14:15:00', '2025-09-11 16:35:00');
INSERT INTO `t_payment` VALUES (30, 30, 'PAY20250920030', 'ALIPAY', 70.00, 'REFUNDED', '2025-09-10 10:20:00', '2025-09-09 08:30:00', '2025-09-10 10:20:00');
INSERT INTO `t_payment` VALUES (31, 31, 'PAY20250920031', 'WECHAT_PAY', 45.00, 'FAIL', NULL, '2025-09-08 13:20:00', '2025-09-09 15:45:00');
INSERT INTO `t_payment` VALUES (32, 32, 'PAY20250920032', 'ALIPAY', 84.00, 'FAIL', NULL, '2025-09-07 19:45:00', '2025-09-08 14:30:00');
INSERT INTO `t_payment` VALUES (33, 33, 'PAY20250920033', 'WECHAT_PAY', 130.00, 'SUCCESS', '2025-09-19 10:25:00', '2025-09-19 10:20:00', '2025-09-19 10:25:00');
INSERT INTO `t_payment` VALUES (34, 34, 'PAY20250920034', 'ALIPAY', 75.00, 'SUCCESS', '2025-09-18 15:35:00', '2025-09-18 15:30:00', '2025-09-18 15:35:00');
INSERT INTO `t_payment` VALUES (35, 35, 'PAY20250920035', 'WECHAT_PAY', 150.00, 'SUCCESS', '2025-09-17 16:50:00', '2025-09-17 16:45:00', '2025-09-17 16:50:00');
INSERT INTO `t_payment` VALUES (36, 36, 'PAY20250920036', 'ALIPAY', 300.00, 'SUCCESS', '2025-09-16 09:20:00', '2025-09-16 09:15:00', '2025-09-16 09:20:00');
INSERT INTO `t_payment` VALUES (37, 37, 'PAY20250920037', 'WECHAT_PAY', 27.50, 'PENDING', NULL, '2025-09-15 14:30:00', '2025-09-15 14:30:00');
INSERT INTO `t_payment` VALUES (38, 38, 'PAY20250920038', 'ALIPAY', 340.00, 'PENDING', NULL, '2025-09-14 11:20:00', '2025-09-14 11:20:00');
INSERT INTO `t_payment` VALUES (39, 39, 'PAY20250920039', 'BALANCE', 0.00, 'SUCCESS', '2025-08-18 14:20:00', '2025-08-18 14:15:00', '2025-08-18 14:20:00');
INSERT INTO `t_payment` VALUES (40, 40, 'PAY20250920040', 'WECHAT_PAY', 30.00, 'SUCCESS', '2025-08-17 10:25:00', '2025-08-17 10:20:00', '2025-08-17 10:25:00');

-- ----------------------------
-- Table structure for t_review
-- ----------------------------
DROP TABLE IF EXISTS `t_review`;
CREATE TABLE `t_review`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价ID（主键，自增）',
  `attraction_id` bigint NOT NULL COMMENT '景点ID，关联t_attraction.id（标识被评价的景点）',
  `user_id` bigint NOT NULL COMMENT '用户ID，关联t_user.id（标识评价所属用户）',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID，关联t_order.id（可选，用于验证用户是否实际购买过该景点门票，确保评价真实）',
  `rating` int NOT NULL COMMENT '评分（1-5分，由CHECK约束限制，1分最差，5分最好，用于计算景点综合评分）',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评价内容（用户对景点的文字评论，如游玩体验、建议等）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价创建时间（用户提交评价的时间）',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评价更新时间（用户修改评价内容时自动更新）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `attraction_id`(`attraction_id` ASC) USING BTREE COMMENT '普通索引：优化通过景点ID查询所有评价的效率（如景点详情页展示评价）',
  INDEX `user_id`(`user_id` ASC) USING BTREE COMMENT '普通索引：优化通过用户ID查询“我的评价”的效率',
  INDEX `order_id`(`order_id` ASC) USING BTREE COMMENT '普通索引：优化通过订单ID关联评价的效率',
  CONSTRAINT `t_review_ibfk_1` FOREIGN KEY (`attraction_id`) REFERENCES `t_attraction` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_review_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_review_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_review_chk_1` CHECK (`rating` between 1 and 5)
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户评价表：存储用户对景点的评分和评论，为其他用户提供参考' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_review
-- ----------------------------
INSERT INTO `t_review` VALUES (1, 1, 1, NULL, 5, '非常壮观，值得一游！', '2025-09-15 14:30:00', '2025-09-15 14:30:00');
INSERT INTO `t_review` VALUES (2, 2, 2, NULL, 4, '景色不错，但人有点多', '2025-09-16 09:15:00', '2025-09-16 09:15:00');
INSERT INTO `t_review` VALUES (3, 3, 3, NULL, 5, '园林设计精美，推荐参观', '2025-09-14 16:45:00', '2025-09-14 16:45:00');
INSERT INTO `t_review` VALUES (4, 4, 4, NULL, 5, '孩子很喜欢，项目很丰富', '2025-09-17 11:20:00', '2025-09-17 11:20:00');
INSERT INTO `t_review` VALUES (5, 5, 5, NULL, 4, '夜景很美，适合散步', '2025-09-13 20:10:00', '2025-09-13 20:10:00');
INSERT INTO `t_review` VALUES (6, 6, 6, NULL, 3, '观景台不错，但门票有点贵', '2025-09-18 15:30:00', '2025-09-18 15:30:00');
INSERT INTO `t_review` VALUES (7, 7, 7, NULL, 5, '视野开阔，城市全景尽收眼底', '2025-09-12 17:45:00', '2025-09-12 17:45:00');
INSERT INTO `t_review` VALUES (8, 8, 8, NULL, 5, '动物种类丰富，孩子很开心', '2025-09-11 10:20:00', '2025-09-11 10:20:00');
INSERT INTO `t_review` VALUES (9, 9, 9, NULL, 3, '世界各地微缩景观，适合拍照', '2025-09-10 14:15:00', '2025-09-10 14:15:00');
INSERT INTO `t_review` VALUES (10, 10, 10, NULL, 5, '湖光山色，美不胜收', '2025-09-09 08:30:00', '2025-09-09 08:30:00');
INSERT INTO `t_review` VALUES (11, 11, 11, NULL, 4, '古典园林典范，很有韵味', '2025-09-08 13:20:00', '2025-09-08 13:20:00');
INSERT INTO `t_review` VALUES (12, 12, 12, NULL, 4, '文化底蕴深厚，小吃很多', '2025-09-07 19:45:00', '2025-09-07 19:45:00');
INSERT INTO `t_review` VALUES (13, 13, 13, NULL, 5, '大熊猫太可爱了，值得一看', '2025-09-06 11:10:00', '2025-09-06 11:10:00');
INSERT INTO `t_review` VALUES (14, 14, 14, NULL, 5, '人间仙境，自然风光绝美', '2025-09-05 16:30:00', '2025-09-05 16:30:00');
INSERT INTO `t_review` VALUES (15, 15, 15, NULL, 4, '城市绿肺，适合休闲散步', '2025-09-04 09:45:00', '2025-09-04 09:45:00');
INSERT INTO `t_review` VALUES (16, 16, 16, NULL, 4, '建筑很有特色，夜景漂亮', '2025-09-03 20:20:00', '2025-09-03 20:20:00');
INSERT INTO `t_review` VALUES (17, 17, 17, NULL, 5, '震撼人心，历史的见证', '2025-09-02 15:10:00', '2025-09-02 15:10:00');
INSERT INTO `t_review` VALUES (18, 18, 18, NULL, 4, '海滨风光优美，气候宜人', '2025-09-01 14:30:00', '2025-09-01 14:30:00');
INSERT INTO `t_review` VALUES (19, 19, 19, NULL, 4, '红色旅游经典景区，值得参观', '2025-08-31 10:20:00', '2025-08-31 10:20:00');
INSERT INTO `t_review` VALUES (20, 20, 20, NULL, 5, '冰雪奇观，冬季必去', '2025-08-30 16:45:00', '2025-08-30 16:45:00');
INSERT INTO `t_review` VALUES (21, 21, 21, NULL, 4, '武术发源地，表演很精彩', '2025-08-29 09:15:00', '2025-08-29 09:15:00');
INSERT INTO `t_review` VALUES (22, 22, 22, NULL, 4, '清朝早期宫殿，很有历史价值', '2025-08-28 13:30:00', '2025-08-28 13:30:00');
INSERT INTO `t_review` VALUES (23, 23, 23, NULL, 5, '五岳归来不看山，黄山归来不看岳', '2025-08-27 11:20:00', '2025-08-27 11:20:00');
INSERT INTO `t_review` VALUES (24, 24, 24, NULL, 4, '历史文化街区，古色古香', '2025-08-26 15:45:00', '2025-08-26 15:45:00');
INSERT INTO `t_review` VALUES (25, 25, 25, NULL, 4, '城市绿肺，空气清新', '2025-08-25 08:30:00', '2025-08-25 08:30:00');
INSERT INTO `t_review` VALUES (26, 26, 26, NULL, 4, '喀斯特地貌奇观，值得一看', '2025-08-24 14:15:00', '2025-08-24 14:15:00');
INSERT INTO `t_review` VALUES (27, 27, 27, NULL, 5, '瀑布壮观，气势磅礴', '2025-08-23 10:20:00', '2025-08-23 10:20:00');
INSERT INTO `t_review` VALUES (28, 28, 28, NULL, 5, '世界屋脊的明珠，信仰的力量', '2025-08-22 16:30:00', '2025-08-22 16:30:00');
INSERT INTO `t_review` VALUES (29, 29, 29, NULL, 3, '黄河风情，值得一看', '2025-08-21 09:45:00', '2025-08-21 09:45:00');
INSERT INTO `t_review` VALUES (30, 30, 30, NULL, 5, '高原明珠，湖水湛蓝', '2025-08-20 15:10:00', '2025-08-20 15:10:00');
INSERT INTO `t_review` VALUES (31, 31, 31, NULL, 3, '西夏历史遗迹，有点荒凉', '2025-08-19 14:30:00', '2025-08-19 14:30:00');
INSERT INTO `t_review` VALUES (32, 32, 32, NULL, 4, '天山明珠，湖水清澈', '2025-08-18 11:20:00', '2025-08-18 11:20:00');
INSERT INTO `t_review` VALUES (33, 33, 33, NULL, 5, '主题乐园，适合全家游玩', '2025-08-17 16:45:00', '2025-08-17 16:45:00');
INSERT INTO `t_review` VALUES (34, 34, 34, NULL, 4, '历史遗迹，文化底蕴深厚', '2025-08-16 09:15:00', '2025-08-16 09:15:00');
INSERT INTO `t_review` VALUES (35, 35, 35, NULL, 5, '文物丰富，值得细细品味', '2025-08-15 13:30:00', '2025-08-15 13:30:00');
INSERT INTO `t_review` VALUES (36, 36, 36, NULL, 3, '古城风貌，正在修复中', '2025-08-14 10:20:00', '2025-08-14 10:20:00');
INSERT INTO `t_review` VALUES (37, 37, 37, NULL, 4, '历史悠久，建筑精美', '2025-08-13 15:45:00', '2025-08-13 15:45:00');
INSERT INTO `t_review` VALUES (38, 38, 38, NULL, 3, '历史故事感人，景区较小', '2025-08-12 08:30:00', '2025-08-12 08:30:00');
INSERT INTO `t_review` VALUES (39, 39, 39, NULL, 4, '江南名楼，文化底蕴深厚', '2025-08-11 14:15:00', '2025-08-11 14:15:00');
INSERT INTO `t_review` VALUES (40, 40, 40, NULL, 4, '泉城标志，值得一看', '2025-08-10 10:20:00', '2025-08-10 10:20:00');

-- ----------------------------
-- Table structure for t_ticket
-- ----------------------------
DROP TABLE IF EXISTS `t_ticket`;
CREATE TABLE `t_ticket`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '门票ID（主键，自增）',
  `attraction_id` bigint NOT NULL COMMENT '景点ID，关联t_attraction.id（标识该门票对应的景点，一个景点可对应多个门票类型）',
  `ticket_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '门票类型（如“ADULT”成人票、“CHILD”儿童票、“STUDENT”学生票、“ELDERLY”老人票）',
  `price` decimal(10, 2) NOT NULL COMMENT '门票原价（未折扣时的价格，保留2位小数）',
  `discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '门票折扣价（可选，低于原价，如活动价，无折扣则为NULL）',
  `valid_days` int NOT NULL DEFAULT 1 COMMENT '门票有效期（自购买日起生效的天数，默认1天，如“3”表示3天内有效）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '门票说明（如使用限制“1.2米以下儿童免票”、包含服务“含景区内观光车”）',
  `stock` int NOT NULL DEFAULT 0 COMMENT '门票库存（当前可售数量，下单时扣减，补货时增加，库存为0时无法购买）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '门票信息创建时间（如录入系统时间）',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '门票信息更新时间（如修改价格、库存、折扣时自动更新）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `attraction_id`(`attraction_id` ASC) USING BTREE COMMENT '普通索引：优化通过景点ID查询所有门票类型的效率（如景点详情页展示门票）',
  CONSTRAINT `t_ticket_ibfk_1` FOREIGN KEY (`attraction_id`) REFERENCES `t_attraction` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '门票信息表：存储每个景点的不同类型门票及价格、库存，是购票的核心数据来源' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_ticket
-- ----------------------------
INSERT INTO `t_ticket` VALUES (1, 1, 'ADULT', 60.00, 55.00, 1, '成人票，18-64周岁适用', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (2, 1, 'CHILD', 30.00, 28.00, 1, '儿童票，6-17周岁适用，需成人陪同', 300, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (3, 1, 'STUDENT', 30.00, NULL, 1, '学生票，凭有效学生证购买', 200, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (4, 1, 'ELDERLY', 30.00, NULL, 1, '老年票，65周岁及以上适用', 150, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (5, 2, 'ADULT', 45.00, 40.00, 1, '成人票，18-64周岁适用', 600, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (6, 2, 'CHILD', 25.00, NULL, 1, '儿童票，6-17周岁适用，需成人陪同', 400, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (7, 2, 'STUDENT', 25.00, 22.00, 1, '学生票，凭有效学生证购买', 300, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (8, 2, 'ELDERLY', 20.00, NULL, 1, '老年票，65周岁及以上适用', 200, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (9, 3, 'ADULT', 30.00, 28.00, 1, '成人票，18-64周岁适用', 800, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (10, 3, 'CHILD', 15.00, NULL, 1, '儿童票，6-17周岁适用，需成人陪同', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (11, 3, 'COMBINATION', 60.00, 55.00, 1, '联票，含园中园景点', 400, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (12, 4, 'ADULT', 399.00, 379.00, 1, '成人票，18-64周岁适用', 1000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (13, 4, 'CHILD', 299.00, 279.00, 1, '儿童票，3-17周岁适用，需成人陪同', 600, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (14, 4, 'ELDERLY', 299.00, NULL, 1, '老年票，65周岁及以上适用', 300, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (15, 4, 'SEASON', 1299.00, NULL, 90, '季票，有效期90天', 200, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (16, 5, 'FREE', 0.00, NULL, 1, '免费景点，无需购票', 10000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (17, 6, 'ADULT', 199.00, 180.00, 1, '成人票，含上球体+下球体+陈列馆', 800, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (18, 6, 'CHILD', 99.00, NULL, 1, '儿童票，1.0-1.4米适用', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (19, 6, 'VIP', 299.00, 270.00, 1, 'VIP票，含快速通道', 300, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (20, 7, 'ADULT', 150.00, 135.00, 1, '成人票，18-64周岁适用', 600, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (21, 7, 'CHILD', 75.00, NULL, 1, '儿童票，1.2-1.5米适用', 400, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (22, 7, 'TOWER', 228.00, 205.00, 1, '套票，含摩天轮体验', 300, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (23, 8, 'ADULT', 300.00, 280.00, 1, '成人票，18-64周岁适用', 800, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (24, 8, 'CHILD', 210.00, NULL, 1, '儿童票，1.0-1.5米适用', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (25, 8, 'FAMILY', 750.00, 700.00, 1, '家庭票，2大1小', 300, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (26, 9, 'ADULT', 220.00, 200.00, 1, '成人票，18-64周岁适用', 1000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (27, 9, 'CHILD', 110.00, NULL, 1, '儿童票，1.2-1.5米适用', 600, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (28, 9, 'STUDENT', 140.00, NULL, 1, '学生票，凭有效学生证购买', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (29, 10, 'FREE', 0.00, NULL, 1, '免费景点，无需购票', 10000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (30, 10, 'BOAT', 50.00, 45.00, 1, '游船票，环湖游览', 2000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (31, 11, 'ADULT', 70.00, 65.00, 1, '成人票，18-64周岁适用', 800, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (32, 11, 'CHILD', 35.00, NULL, 1, '儿童票，6-17周岁适用', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (33, 12, 'ADULT', 30.00, 28.00, 1, '成人票，含夫子庙大成殿', 1000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (34, 12, 'COMBINATION', 80.00, 75.00, 1, '联票，含多个景点', 600, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (35, 13, 'ADULT', 55.00, 50.00, 1, '成人票，18-64周岁适用', 1500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (36, 13, 'CHILD', 27.50, NULL, 1, '儿童票，6-17周岁适用', 800, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (37, 14, 'ADULT', 169.00, 150.00, 2, '成人票，2天有效', 1000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (38, 14, 'CHILD', 85.00, NULL, 2, '儿童票，6-17周岁适用，2天有效', 600, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (39, 15, 'FREE', 0.00, NULL, 1, '免费景点，无需购票', 10000, '2025-09-20 00:40:02', '2025-09-20 00:40:02');
INSERT INTO `t_ticket` VALUES (40, 15, 'CYCLE', 30.00, NULL, 1, '自行车租赁', 500, '2025-09-20 00:40:02', '2025-09-20 00:40:02');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键，自增）',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户名（唯一，用于系统登录，如“zhangSan123”）',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录密码（加密存储，如MD5、BCrypt加密后的字符串，不存储明文）',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户真实姓名（用于订单实名认证、门票核对）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱（唯一，用于账号注册验证、密码找回、订单通知）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号（可选，用于接收短信通知，如订单状态变更、验证码）',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户常用地址（可选，用于关联线下服务，如门票快递地址）',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'USER' COMMENT '用户角色（默认USER普通用户，可扩展ADMIN管理员、OPERATOR运营人员，控制权限）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户账号创建时间（注册时间）',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户信息更新时间（如修改密码、姓名、手机号时自动更新）',
  `balance` int NULL DEFAULT NULL COMMENT '用户余额（单位：分，用于余额支付，如1000表示10.00元，充值时增加，支付时扣减）',
  `avatar` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE COMMENT '唯一索引：确保登录用户名不重复，避免账号冲突',
  UNIQUE INDEX `email`(`email` ASC) USING BTREE COMMENT '唯一索引：确保用户邮箱不重复，用于唯一标识账号'
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表：存储系统所有用户的登录信息、个人信息及权限，是用户操作的基础关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'test', '123', '李明', 'liming1@example.com', '13800138001', '北京市朝阳区建国路88号', 'USER', '2025-09-20 00:40:02', '2025-09-21 16:36:29', 18426, 'static/avatar/b340e643-712a-49bf-a7f9-06f2b0eb0fbc.jpg');
INSERT INTO `t_user` VALUES (2, 'test2', '123', '王红', 'wanghong2@example.com', '13900139002', '上海市浦东新区张江高科技园区', 'USER', '2025-09-20 00:40:02', '2025-09-25 09:37:29', 15000, NULL);
INSERT INTO `t_user` VALUES (3, 'zhang_wei3', '123', '张伟', 'zhangwei3@example.com', '13700137003', '广州市天河区珠江新城', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 30000, NULL);
INSERT INTO `t_user` VALUES (4, 'liu_jie4', '123', '刘杰', 'liujie4@example.com', '13600136004', '深圳市南山区科技园', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 18000, NULL);
INSERT INTO `t_user` VALUES (5, 'chen_xi5', '123', '晨曦', 'chenxi5@example.com', '13500135005', '杭州市西湖区黄龙路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 25000, NULL);
INSERT INTO `t_user` VALUES (6, 'zhao_yang6', '123', '赵阳', 'zhaoyang6@example.com', '13400134006', '南京市玄武区中山陵', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 12000, NULL);
INSERT INTO `t_user` VALUES (7, 'huang_juan7', '123', '黄娟', 'huangjuan7@example.com', '13300133007', '成都市锦江区春熙路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 22000, NULL);
INSERT INTO `t_user` VALUES (8, 'wu_dong8', '123', '吴东', 'wudong8@example.com', '13200132008', '武汉市武昌区东湖路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 17000, NULL);
INSERT INTO `t_user` VALUES (9, 'zhou_mei9', '123', '周梅', 'zhoumei9@example.com', '13100131009', '重庆市渝中区解放碑', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 28000, NULL);
INSERT INTO `t_user` VALUES (10, 'xu_lei10', '123', '徐磊', 'xulei10@example.com', '13000130010', '西安市雁塔区大雁塔', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 19000, NULL);
INSERT INTO `t_user` VALUES (11, 'sun_ying11', '123', '孙颖', 'sunying11@example.com', '13800138011', '青岛市市南区海滨大道', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 24000, NULL);
INSERT INTO `t_user` VALUES (12, 'ma_hua12', '123', '马华', 'mahua12@example.com', '13900139012', '苏州市姑苏区平江路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 16000, NULL);
INSERT INTO `t_user` VALUES (13, 'gao_feng13', '123', '高峰', 'gaofeng13@example.com', '13700137013', '长沙市岳麓区橘子洲', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 21000, NULL);
INSERT INTO `t_user` VALUES (14, 'peng_fei14', '123', '鹏飞', 'pengfei14@example.com', '13600136014', '哈尔滨市道里区中央大街', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 26000, NULL);
INSERT INTO `t_user` VALUES (15, 'yan_mei15', '123', '闫梅', 'yanmei15@example.com', '13500135015', '郑州市金水区二七广场', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 14000, NULL);
INSERT INTO `t_user` VALUES (16, 'he_jun16', '123', '何军', 'hejun16@example.com', '13400134016', '沈阳市和平区沈阳路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 23000, NULL);
INSERT INTO `t_user` VALUES (17, 'lu_qiang17', '123', '鲁强', 'luqiang17@example.com', '13300133017', '合肥市庐阳区长江路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:41', 18000, NULL);
INSERT INTO `t_user` VALUES (18, 'fang_ting18', '123', '方婷', 'fangting18@example.com', '13200132018', '福州市鼓楼区东街口', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 27000, NULL);
INSERT INTO `t_user` VALUES (19, 'yuan_bo19', '123', '袁波', 'yuanbo19@example.com', '13100131019', '南宁市青秀区民族大道', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 15000, NULL);
INSERT INTO `t_user` VALUES (20, 'tang_jie20', '123', '唐杰', 'tangjie20@example.com', '13000130020', '昆明市五华区北京路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 20000, NULL);
INSERT INTO `t_user` VALUES (21, 'du_wei21', '123', '杜伟', 'duwei21@example.com', '13800138021', '贵阳市云岩区中华路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 29000, NULL);
INSERT INTO `t_user` VALUES (22, 'zhu_ying22', '123', '朱颖', 'zhuying22@example.com', '13900139022', '拉萨市城关区北京中路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 13000, NULL);
INSERT INTO `t_user` VALUES (23, 'shao_hua23', '123', '邵华', 'shaohua23@example.com', '13700137023', '兰州市城关区张掖路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 24000, NULL);
INSERT INTO `t_user` VALUES (24, 'wen_jing24', '123', '文静', 'wenjing24@example.com', '13600136024', '西宁市城西区五四大街', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 17000, NULL);
INSERT INTO `t_user` VALUES (25, 'cao_yu25', '123', '曹宇', 'caoyu25@example.com', '13500135025', '银川市兴庆区解放西街', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 22000, NULL);
INSERT INTO `t_user` VALUES (26, 'jin_hui26', '123', '金辉', 'jinhui26@example.com', '13400134026', '乌鲁木齐市天山区解放南路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 19000, NULL);
INSERT INTO `t_user` VALUES (27, 'qiu_shan27', '123', '邱珊', 'qiushan27@example.com', '13300133027', '香港特别行政区中西区', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 30000, NULL);
INSERT INTO `t_user` VALUES (28, 'song_tao28', '123', '宋涛', 'songtao28@example.com', '13200132028', '澳门特别行政区澳门半岛', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 25000, NULL);
INSERT INTO `t_user` VALUES (29, 'xie_jun29', '123', '谢军', 'xiejun29@example.com', '13100131029', '台北市中正区', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 16000, NULL);
INSERT INTO `t_user` VALUES (30, 'lei_min30', '123', '雷敏', 'leimin30@example.com', '13000130030', '石家庄市长安区中山东路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 21000, NULL);
INSERT INTO `t_user` VALUES (31, 'dai_hong31', '123', '戴红', 'daihong31@example.com', '13800138031', '太原市杏花岭区迎泽大街', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 28000, NULL);
INSERT INTO `t_user` VALUES (32, 'lin_jie32', '123', '林杰', 'linjie32@example.com', '13900139032', '呼和浩特市新城区新华大街', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 14000, NULL);
INSERT INTO `t_user` VALUES (33, 'jiang_wei33', '123', '姜维', 'jiangwei33@example.com', '13700137033', '南昌市西湖区中山路', 'ADMIN', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 23000, NULL);
INSERT INTO `t_user` VALUES (34, 'fan_ya34', '123', '樊雅', 'fanya34@example.com', '13600136034', '济南市历下区泉城路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 18000, NULL);
INSERT INTO `t_user` VALUES (35, 'zheng_liang35', '123', '郑亮', 'zhengliang35@example.com', '13500135035', '海口市美兰区海秀东路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 27000, NULL);
INSERT INTO `t_user` VALUES (36, 'wang_guo36', '123', '王国', 'wangguo36@example.com', '13400134036', '三亚市吉阳区解放路', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 15000, NULL);
INSERT INTO `t_user` VALUES (37, 'chang_hong37', '123', '常虹', 'changhong37@example.com', '13300133037', '成都市青羊区宽窄巷子', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 20000, NULL);
INSERT INTO `t_user` VALUES (38, 'wei_xia38', '123', '魏霞', 'weixia38@example.com', '13200132038', '杭州市上城区河坊街', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 29000, NULL);
INSERT INTO `t_user` VALUES (39, 'zhong_hao39', '123', '钟浩', 'zhonghao39@example.com', '13100131039', '苏州市工业园区金鸡湖', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 13000, NULL);
INSERT INTO `t_user` VALUES (40, 'tong_lei40', '123', '佟磊', 'tonglei40@example.com', '13000130040', '无锡市滨湖区太湖大道', 'USER', '2025-09-20 00:40:02', '2025-09-21 09:12:42', 24000, NULL);
INSERT INTO `t_user` VALUES (41, 'bomo', '123', 'bomo', 'admin@ad.com', '13000130042', '地球', 'ADMIN', '2025-09-20 08:54:31', '2025-09-21 09:12:42', 99999, NULL);
INSERT INTO `t_user` VALUES (43, '111111', 'e10adc3949ba59abbe56e057f20f883e', '里升龙', '132@qq.com', '14234223454', 'aaaaaa', 'USER', '2025-09-21 19:55:49', '2025-09-21 19:55:49', 0, NULL);
INSERT INTO `t_user` VALUES (46, '李sb', '123456', '里升龙1', '13ddd2@qq.com', '14234223452', 'dadad', 'USER', '2025-09-21 19:58:21', '2025-09-21 19:58:21', 0, NULL);
INSERT INTO `t_user` VALUES (47, 'test3', '123456', '测试员3', '13322@qq.com', '14234223459', '北京1', 'ADMIN', '2025-09-25 08:46:31', '2025-09-25 08:46:31', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
