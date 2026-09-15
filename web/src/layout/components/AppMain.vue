<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <KeepAlive :include="cachedViews">
        <router-view :key="key" />
      </KeepAlive>
    </transition>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    },
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    }
  }
}
</script>

<style scoped>
.app-main {
  min-height: calc(100vh - 64px);
  width: 100%;
  position: relative;
  overflow: hidden;
  background: #f3f4f6;
}
.fixed-header+.app-main {
  padding-top: 64px;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
