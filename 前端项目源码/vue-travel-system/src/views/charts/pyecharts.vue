<template>

<div class="pyecharts-container">
<!-- 查询说明和前10景点列表 -->
<div class="query-info-section">
<div class="query-notice">
<strong>查询说明：</strong>请输入完整的景点名称进行精确查询，不支持模糊查询。
</div>
<div class="top10-attractions">
<strong>订单量排名前10的景点：</strong>
<div class="attraction-tags">
<span 
v-for="(attraction, index) in top10Attractions" 
:key="index"
class="attraction-tag"
@click="selectAttraction(attraction)"
:class="{ 'selected': searchKeyword === attraction }"
>
{{ attraction }}
</span>
</div>
</div>
</div>

<div class="image-container">
<!-- 左侧固定饼图 -->
<div class="chart-section">
<div class="chart-title">
<h3>订单数排名前十占比</h3>
</div>
<div class="chart-header">
<button @click="regeneratePie" class="regenerate-btn" :disabled="isGenerating">
{{ isGenerating ? '生成中...' : '重新生成饼图' }}
</button>
</div>
<div class="image-item">
<img v-if="pieImage!=null" :src="'api/showImages?path=' + pieImage" class="chart-image"/>
</div>
</div>
<!-- 右侧搜索结果 -->
<div class="chart-section">
<div class="chart-title">
<h3>搜索景点订单趋势</h3>
</div>
<div class="chart-header">
<div class="search-box">
<input
v-model="searchKeyword"
placeholder="请输入景点名称(可从上方添加)"
class="search-input"
/>
<button @click="mySearch" class="search-btn" :disabled="isRefreshing">
{{ isRefreshing ? '搜索中...' : '搜索' }}
</button>
<button @click="manualRefresh" class="refresh-btn" :disabled="isRefreshing || !searchKeyword.trim()" title="手动刷新">
<i class="refresh-icon" :class="{ 'spinning': isRefreshing }">🔄</i>
</button>
</div>
<div class="refresh-status" v-if="searchKeyword.trim()">
<span class="status-text">
{{ isRefreshing ? '正在刷新数据...' : '数据每30秒自动刷新' }}
</span>
</div>
</div>
<div class="image-item">
<img v-if="gridImage!=null" :src="'api/showImages?path=' + gridImage" class="chart-image"/>
</div>
</div>
</div>
</div>

</template>

<script>

import axios from 'axios';
export default {
data() {
return {
searchKeyword: '',
pieImage: null,
gridImage: null,
isGenerating: false,
isRefreshing: false,
defaultPiePath: 'D:/Study/Courses/AI/vue-travel-system/static/img/pie.png',
top10Attractions: [],
lastSearchKeyword: '',
refreshTimer: null
};
},
async created() {
// 优先尝试使用缓存图片，如果不存在再生成新的
await this.loadPieImageWithCache();
// 获取前10景点数据
await this.loadTop10Attractions();
},

beforeUnmount() {
// 组件销毁前清理定时器
this.stopAutoRefresh();
},
methods: {
// 带缓存的饼图加载
async loadPieImageWithCache() {
try {
// 先尝试直接使用缓存图片
const cacheUrl = `api/showImages?path=${this.defaultPiePath}`;
const response = await axios.head(cacheUrl);
if (response.status === 200) {
console.log('使用缓存图片');
this.pieImage = this.defaultPiePath;
return;
}
} catch (error) {
console.log('缓存图片不存在，将生成新图片');
}
// 缓存不存在，生成新图片
await this.getPieImage();
},

// 获取默认饼图
async getPieImage() {
this.isGenerating = true;
try {
const res = await axios.get('api/getPie');
console.log("返回图片名称：" + res.data);
this.pieImage = res.data;
} catch (error) {
console.error('获取饼图失败:', error);
} finally {
this.isGenerating = false;
}
},

// 重新生成饼图
async regeneratePie() {
this.isGenerating = true;
try {
const res = await axios.get('api/getPie');
console.log("重新生成图片：" + res.data);
this.pieImage = res.data;
} catch (error) {
console.error('重新生成饼图失败:', error);
} finally {
this.isGenerating = false;
}
},
// 统一搜索方法
async mySearch() {
//判断搜索关键词为空字符串或纯空格
if (!this.searchKeyword.trim()) {
this.gridImage = null;
this.lastSearchKeyword = '';
this.stopAutoRefresh();
return;
}

// 如果搜索关键词没有变化，不重复搜索
if (this.searchKeyword.trim() === this.lastSearchKeyword) {
return;
}

this.lastSearchKeyword = this.searchKeyword.trim();
await this.performSearch();
},

// 执行搜索
async performSearch() {
this.isRefreshing = true;
try {
console.log("搜索关键词:", this.searchKeyword);
const res = await axios.get('api/getGrid', { 
params: { pname: this.searchKeyword.trim() }
});
console.log("返回的grid图片地址："+res.data);
this.gridImage = res.data;
// 搜索成功后启动自动刷新
this.startAutoRefresh();
} catch (error) {
console.error('搜索失败:', error);
this.gridImage = null;
} finally {
this.isRefreshing = false;
}
},

// 启动自动刷新
startAutoRefresh() {
// 清除之前的定时器
this.stopAutoRefresh();
// 每30秒自动刷新一次
this.refreshTimer = setInterval(() => {
if (this.searchKeyword.trim() && this.searchKeyword.trim() === this.lastSearchKeyword) {
console.log('自动刷新景点趋势图...');
this.performSearch();
}
}, 30000); // 30秒
},

// 停止自动刷新
stopAutoRefresh() {
if (this.refreshTimer) {
clearInterval(this.refreshTimer);
this.refreshTimer = null;
}
},

// 手动刷新
async manualRefresh() {
if (this.searchKeyword.trim()) {
await this.performSearch();
}
},

// 获取前10景点数据
async loadTop10Attractions() {
try {
const res = await axios.get('api/getTop10Attractions');
console.log("前10景点数据:", res.data);
this.top10Attractions = res.data || [];
} catch (error) {
console.error('获取前10景点失败:', error);
// 使用默认景点列表
this.top10Attractions = [
"故宫博物院", "天安门广场", "颐和园", "长城", "天坛", 
"北海公园", "景山公园", "中山公园", "劳动人民文化宫", "太庙"
];
}
},

// 选择景点
selectAttraction(attraction) {
this.searchKeyword = attraction;
// 停止之前的自动刷新
this.stopAutoRefresh();
// 自动执行搜索
this.mySearch();
}
},
}
</script>
<style scoped>
.pyecharts-container {
padding: 20px;
}

.query-info-section {
background: #f8f9fa;
border-radius: 8px;
padding: 20px;
margin-bottom: 20px;
border: 1px solid #e9ecef;
}

.query-notice {
color: #6c757d;
margin-bottom: 15px;
font-size: 14px;
}

.top10-attractions {
margin-bottom: 0;
}

.top10-attractions strong {
color: #495057;
display: block;
margin-bottom: 10px;
font-size: 14px;
}

.attraction-tags {
display: flex;
flex-wrap: wrap;
gap: 8px;
}

.attraction-tag {
display: inline-block;
padding: 6px 12px;
background: #e9ecef;
color: #495057;
border-radius: 20px;
cursor: pointer;
font-size: 12px;
transition: all 0.3s ease;
border: 1px solid #dee2e6;
}

.attraction-tag:hover {
background: #007bff;
color: white;
border-color: #007bff;
transform: translateY(-1px);
box-shadow: 0 2px 4px rgba(0,123,255,0.3);
}

.attraction-tag.selected {
background: #28a745;
color: white;
border-color: #28a745;
font-weight: bold;
}

.image-container {
display: grid;
grid-template-columns: repeat(2, 1fr);
gap: 20px;
max-width: 100%;
margin: 0;
}

.chart-section {
display: flex;
flex-direction: column;
border: 1px solid #eee;
border-radius: 8px;
background: white;
overflow: hidden;
box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.chart-title {
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
color: white;
padding: 15px 20px;
text-align: center;
border-bottom: 1px solid #e9ecef;
}

.chart-title h3 {
margin: 0;
font-size: 16px;
font-weight: 600;
letter-spacing: 0.5px;
}

.chart-header {
padding: 15px;
background: #f8f9fa;
border-bottom: 1px solid #eee;
display: flex;
flex-direction: column;
align-items: center;
gap: 10px;
}

.search-box {
display: flex;
gap: 10px;
align-items: center;
}

.search-input {
padding: 8px 12px;
border: 1px solid #ddd;
border-radius: 4px;
font-size: 14px;
width: 200px;
}

.search-btn, .regenerate-btn, .refresh-btn {
padding: 8px 16px;
border: none;
border-radius: 4px;
cursor: pointer;
font-size: 14px;
transition: all 0.3s ease;
white-space: nowrap;
}

.refresh-btn {
padding: 8px 12px;
background: #17a2b8;
color: white;
margin-left: 8px;
}

.refresh-btn:hover:not(:disabled) {
background: #138496;
}

.refresh-btn:disabled {
background: #6c757d;
cursor: not-allowed;
opacity: 0.6;
}

.refresh-icon {
display: inline-block;
transition: transform 0.3s ease;
}

.refresh-icon.spinning {
animation: spin 1s linear infinite;
}

@keyframes spin {
from { transform: rotate(0deg); }
to { transform: rotate(360deg); }
}

.refresh-status {
text-align: center;
}

.status-text {
font-size: 12px;
color: #6c757d;
padding: 4px 8px;
background: #e9ecef;
border-radius: 4px;
display: inline-block;
}

.search-btn {
background: #007bff;
color: white;
}

.search-btn:hover {
background: #0056b3;
}

.regenerate-btn {
background: #28a745;
color: white;
}

.regenerate-btn:hover:not(:disabled) {
background: #1e7e34;
}

.regenerate-btn:disabled {
background: #6c757d;
cursor: not-allowed;
opacity: 0.6;
}

.image-item {
padding: 0;
height: 400px;
display: flex;
align-items: center;
justify-content: center;
background: white;
overflow: hidden;
box-sizing: border-box;
}

.chart-image {
width: 100%;
height: 400px;
object-fit: contain;
display: block;
margin: 0;
background: #f9f9f9;
}
</style>