<script setup lang="ts">
</script>

<template>
  <header class="top-bar">
    <input class="search" placeholder="搜索项目或图解..." />
    <div class="right">Crafting Session</div>
  </header>
</template>

<style scoped>
.top-bar {
  height: 64px;
  border-bottom: 1px solid #e7ebdf;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: #fafbf8;
}

.search {
  width: 360px;
  max-width: 60%;
  border: 1px solid #d6dccf;
  border-radius: 999px;
  background: #fff;
  padding: 9px 14px;
}

.right {
  color: #60705a;
  font-size: 13px;
}
</style>
