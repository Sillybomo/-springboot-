# -*- coding: utf-8 -*-
import sys
import pandas as pd
from sqlalchemy import create_engine
from pyecharts.charts import Pie
from pyecharts import options as opts
from pyecharts.globals import ThemeType
from pyecharts.render import make_snapshot
from snapshot_phantomjs import snapshot

# 数据库连接配置
database_config = 'mysql+pymysql://root:123456@localhost:3306/travel'
connection = create_engine(database_config)

# 执行SQL查询
sql = """
SELECT
    a.`name` AS attraction_name,
    IFNULL(COUNT(DISTINCT o.id), 0) AS order_count
FROM t_attraction a
LEFT JOIN t_ticket tk ON a.id = tk.attraction_id
LEFT JOIN t_order o ON tk.id = o.ticket_id
GROUP BY a.id, a.`name`
ORDER BY order_count DESC, a.`name` ASC
LIMIT 10
"""
df = pd.read_sql(sql, connection)

def get_attraction_names():
    """获取前10景点名称列表"""
    names = df['attraction_name'].tolist()
    return ','.join(names)

def generate_pie_chart():
    """生成饼图"""
    # 创建饼状图
    pie = Pie(init_opts=opts.InitOpts(theme=ThemeType.DARK, width="1000px", height="600px"))

    # 准备数据
    data_pair = [(row['attraction_name'], row['order_count']) for _, row in df.iterrows()]

    pie.add(
        series_name="订单数量",
        data_pair=data_pair,
        radius=["30%", "70%"],
        label_opts=opts.LabelOpts(formatter="{b}: {c} ({d}%)", position="outside")
    )

    pie.set_global_opts(
        title_opts=opts.TitleOpts(title="订单数前十景点占比", subtitle="按订单数量降序排列", pos_left="center"),
        legend_opts=opts.LegendOpts(orient="vertical", pos_left="left")
    )

    # 保存为图片
    path = sys.argv[2] + sys.argv[3]

    try:
        make_snapshot(snapshot, pie.render(), path)
        print("图片生成成功")
    except Exception as e:
        print("图片生成失败:", str(e))

# 根据参数决定执行模式
if len(sys.argv) > 1:
    mode = sys.argv[1]
    if mode == "get_names":
        # 返回景点名称列表
        print(get_attraction_names())
    else:
        # 生成饼图
        generate_pie_chart()
else:
    # 默认生成饼图
    generate_pie_chart()

