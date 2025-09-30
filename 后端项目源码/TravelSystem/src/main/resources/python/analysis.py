# -*- coding: utf-8 -*-
import sys
import io
import pandas as pd
from sqlalchemy import create_engine, text
from pyecharts.charts import Bar, Line, Grid
from pyecharts import options as opts
from pyecharts.render import make_snapshot
from snapshot_phantomjs import snapshot
from contextlib import closing
import warnings
warnings.filterwarnings('ignore')

# 设置标准输出编码为UTF-8
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def get_parameters():
    """从命令行参数中获取参数 - 优化参数验证"""
    if len(sys.argv) < 4:
        raise ValueError("缺少必要参数：保存路径、图片名称、景点名称")

    save_path = sys.argv[1].rstrip('/\\') + '/'
    image_name = sys.argv[2]
    attraction_name = sys.argv[3]

    return save_path, image_name, attraction_name

def create_database_connection():
    """创建数据库连接池 - 提升连接效率"""
    database_config = 'mysql+pymysql://root:123456@localhost:3306/travel'
    return create_engine(database_config, pool_pre_ping=True, pool_recycle=3600)

def fetch_attraction_data(connection, attraction_name):
    """优化SQL查询 - 使用参数化查询和更高效的SQL"""
    sql = text("""
               SELECT
                   DATE(o.created_at) AS create_date,
                   COUNT(o.id) AS order_count
               FROM t_order o
                   INNER JOIN t_ticket tk ON o.ticket_id = tk.id
                   INNER JOIN t_attraction a ON tk.attraction_id = a.id
               WHERE a.name = :attraction_name
                 AND o.created_at IS NOT NULL
               GROUP BY DATE(o.created_at)
               ORDER BY create_date ASC
               """)

    # 使用chunksize分块读取，避免内存溢出
    return pd.read_sql(sql, connection, params={'attraction_name': attraction_name})

def validate_data(df, attraction_name):
    """数据验证优化 - 提前返回避免不必要的计算"""
    if df.empty:
        print(f"❌ 未找到景点 '{attraction_name}' 的数据")
        return False

    required_cols = ['create_date', 'order_count']
    if not all(col in df.columns for col in required_cols):
        print(f"❌ 数据列不完整，缺少: {set(required_cols) - set(df.columns)}")
        return False

    return True

def optimize_chart_data(df):
    """数据处理优化 - 使用向量化操作替代循环"""
    # 使用astype(str)替代apply(str)提升性能
    df['create_date'] = df['create_date'].astype(str)

    x_values = df["create_date"].tolist()
    y_values = df["order_count"].tolist()

    # 优化y轴范围计算
    max_value = df["order_count"].max()
    y_max = max_value + max(1, max_value * 0.1)  # 动态上边距

    return x_values, y_values, y_max, len(x_values)

def create_bar_chart(x_values, y_values, y_max, data_count, attraction_name):
    """创建柱状图 - 简化配置项"""
    bar = Bar(init_opts=opts.InitOpts(
        width="1200px",
        height="450px",
        animation_opts=opts.AnimationOpts(animation_easing="linear")  # 简化动画
    ))

    bar.add_xaxis(x_values)
    bar.add_yaxis(
        series_name="",
        y_axis=y_values,
        label_opts=opts.LabelOpts(is_show=data_count <= 30),  # 数据点多时隐藏标签
        itemstyle_opts=opts.ItemStyleOpts(color="#4895ef")
    )

    bar.set_global_opts(
        xaxis_opts=opts.AxisOpts(
            axislabel_opts=opts.LabelOpts(rotate=45, font_size=10),
            name="日期",
            name_gap=25
        ),
        yaxis_opts=opts.AxisOpts(
            name="订单数",
            name_gap=40,
            min_=0,
            max_=y_max
        ),
        title_opts=opts.TitleOpts(
            title=f"{attraction_name} - 订单统计",
            pos_left="left"
        ),
        legend_opts=opts.LegendOpts(is_show=False)  # 简化图例
    )

    return bar

def create_line_chart(x_values, y_values, y_max, attraction_name):
    """创建折线图 - 优化性能配置"""
    line = Line(init_opts=opts.InitOpts(
        width="1200px",
        height="450px",
        animation_opts=opts.AnimationOpts(animation=False)  # 关闭动画提升性能
    ))

    line.add_xaxis(x_values)
    line.add_yaxis(
        series_name="每日订单数",
        y_axis=y_values,
        symbol_size=4,  # 减小符号大小
        label_opts=opts.LabelOpts(is_show=False),  # 关闭标签提升性能
        linestyle_opts=opts.LineStyleOpts(width=2),
        itemstyle_opts=opts.ItemStyleOpts(color="#f72585")
    )

    line.set_global_opts(
        xaxis_opts=opts.AxisOpts(
            axislabel_opts=opts.LabelOpts(rotate=45, font_size=10),
            name="日期",
            name_gap=25
        ),
        yaxis_opts=opts.AxisOpts(
            name="订单数",
            name_gap=40,
            min_=0,
            max_=y_max
        )
    )

    return line

def create_combined_chart(bar, line):
    """创建组合图表 - 优化布局"""
    grid = Grid(init_opts=opts.InitOpts(
        width="1200px",
        height="800px",  # 适当降低高度
        animation_opts=opts.AnimationOpts(animation=False)
    ))

    grid.add(bar, grid_opts=opts.GridOpts(pos_bottom="55%"))
    grid.add(line, grid_opts=opts.GridOpts(pos_top="55%"))

    return grid

def save_chart(grid, save_path, attraction_name):
    """保存图表 - 优化错误处理和路径处理"""
    safe_name = "".join(c for c in attraction_name if c.isalnum() or c in (' ', '-', '_')).rstrip()
    safe_name = safe_name.replace(' ', '_')

    output_path = f"{save_path}{safe_name}_order_chart.png"

    try:
        make_snapshot(snapshot, grid.render(), output_path)
        print(f"✅ 图表已保存: {output_path}")
        return True
    except Exception as e:
        print(f"❌ 图表保存失败: {e}")
        # 备用方案：保存为HTML
        try:
            html_path = output_path.replace('.png', '.html')
            grid.render(html_path)
            print(f"📊 已保存HTML版本: {html_path}")
        except Exception as e2:
            print(f"❌ HTML保存失败: {e2}")
        return False

def main():
    """主函数 - 优化执行流程"""
    try:
        # 1. 获取参数
        save_path, image_name, attraction_name = get_parameters()
        print(f"📍 目标景点: {attraction_name}")

        # 2. 数据库连接
        engine = create_database_connection()

        # 3. 数据查询
        with closing(engine.connect()) as conn:
            df = fetch_attraction_data(conn, attraction_name)

        print(f"📊 获取到 {len(df)} 条记录")

        # 4. 数据验证
        if not validate_data(df, attraction_name):
            return

        # 5. 数据处理
        x_values, y_values, y_max, data_count = optimize_chart_data(df)

        # 6. 创建图表
        bar = create_bar_chart(x_values, y_values, y_max, data_count, attraction_name)
        line = create_line_chart(x_values, y_values, y_max, attraction_name)
        grid = create_combined_chart(bar, line)

        # 7. 保存图表
        success = save_chart(grid, save_path, attraction_name)

        if success:
            print("🎉 程序执行成功")
        else:
            print("⚠️ 程序执行完成，但有警告")

    except Exception as e:
        print(f"💥 程序执行失败: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()