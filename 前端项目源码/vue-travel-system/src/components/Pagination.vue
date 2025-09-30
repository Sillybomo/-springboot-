/**
* 分页组件
*/ 
<template>
  <el-pagination 
    class="page-box" 
    @size-change="handleSizeChange" 
    @current-change="handleCurrentChange" 
    background 
    :current-page="childMsg.currentPage" 
    :page-sizes="[10, 20, 30, 40]" 
    :page-size="childMsg.pageSize" 
    layout="total, sizes, prev, pager, next, jumper" 
    :total="childMsg.total">
  </el-pagination>
</template>
<script>
export default {
  name: 'Pagination',
  props: ['childMsg'],
  data() {
    return {
      pageparm: {
        currentPage: this.childMsg.currentPage,
        pageSize: this.childMsg.pageSize
      }
    }
  },
  watch: {
    // 监听父组件传递的childMsg变化，同步更新内部数据
    childMsg: {
      handler(newVal) {
        if (newVal) {
          this.pageparm.currentPage = newVal.currentPage || 1;
          this.pageparm.pageSize = newVal.pageSize || 10;
        }
      },
      immediate: true,
      deep: true
    }
  },
  created() {},
  methods: {
    handleSizeChange(val) {
      this.pageparm.pageSize = val
      this.$emit('callFather', this.pageparm)
    },
    handleCurrentChange(val) {
      this.pageparm.currentPage = val
      this.$emit('callFather', this.pageparm)
    }
  }
}
</script>

<style>
.page-box {
  margin: 10px auto;
}
</style>
