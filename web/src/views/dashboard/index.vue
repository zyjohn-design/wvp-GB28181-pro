<template>
  <div class="dashboard-page app-container">
    <header class="page-heading">
      <div>
        <p class="page-eyebrow">SYSTEM OVERVIEW</p>
        <h1>控制台</h1>
        <p class="page-description">实时查看平台资源、网络状态与媒体节点负载</p>
      </div>
      <div class="live-status"><span />数据每 2 秒更新</div>
    </header>

    <div class="dashboard-grid">
      <section class="dashboard-card">
        <consoleCPU ref="consoleCPU" />
      </section>
      <section class="dashboard-card">
        <consoleMem ref="consoleMem" />
      </section>
      <section class="dashboard-card resource-card">
        <consoleResource ref="consoleResource" />
      </section>
      <section class="dashboard-card network-card">
        <consoleNet ref="consoleNet" />
      </section>
      <section class="dashboard-card">
        <consoleDisk ref="consoleDisk" />
      </section>
      <section class="dashboard-card node-card">
        <consoleNodeLoad ref="consoleNodeLoad" />
      </section>
    </div>
  </div>
</template>

<script>
import consoleCPU from './console/ConsoleCPU.vue'
import consoleMem from './console/ConsoleMEM.vue'
import consoleNet from './console/ConsoleNet.vue'
import consoleNodeLoad from './console/ConsoleNodeLoad.vue'
import consoleDisk from './console/ConsoleDisk.vue'
import consoleResource from './console/ConsoleResource.vue'

export default {
  name: 'Dashboard',
  components: {
    consoleCPU,
    consoleMem,
    consoleNet,
    consoleNodeLoad,
    consoleDisk,
    consoleResource
  },
  data() {
    return {
      timer: null
    }
  },
  created() {
    this.getSystemInfo()
    this.getLoad()
    this.getResourceInfo()
    this.loopForSystemInfo()
  },
  destroyed() {
    window.clearImmediate(this.timer)
  },
  methods: {
    loopForSystemInfo: function() {
      if (this.timer !== null) {
        window.clearTimeout(this.timer)
      }
      this.timer = setTimeout(() => {
        console.log(this.$route.name)
        if (this.$route.name === '控制台') {
          this.getSystemInfo()
          this.getLoad()
          this.timer = null
          this.loopForSystemInfo()
          this.getResourceInfo()
        }
      }, 2000)
    },
    getSystemInfo: function() {
      this.$store.dispatch('server/getSystemInfo')
        .then(data => {
          this.$refs.consoleCPU.setData(data.cpu)
          this.$refs.consoleMem.setData(data.mem)
          this.$refs.consoleNet.setData(data.net, data.netTotal)
          this.$refs.consoleDisk.setData(data.disk)
        })
    },
    getLoad: function() {
      this.$store.dispatch('server/getMediaServerLoad')
        .then(data => {
          this.$refs.consoleNodeLoad.setData(data)
        })
    },
    getResourceInfo: function() {
      this.$store.dispatch('server/getResourceInfo')
        .then(data => {
          this.$refs.consoleResource.setData(data)
        })
    }
  }
}
</script>

<style scoped>
.dashboard-page {
  min-height: calc(100vh - 104px);
  background: #f3f4f6;
}

.page-heading {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 22px;
}

.page-eyebrow {
  margin: 0 0 7px;
  color: #6366f1;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.12em;
}

.page-heading h1 {
  margin: 0;
  color: #111827;
  font-size: 28px;
  font-weight: 700;
  line-height: 1.25;
}

.page-description {
  margin: 7px 0 0;
  color: #6b7280;
  font-size: 14px;
}

.live-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 7px 11px;
  color: #047857;
  background: #ecfdf5;
  border: 1px solid #a7f3d0;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

.live-status span {
  width: 7px;
  height: 7px;
  background: #10b981;
  border-radius: 50%;
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.13);
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
}

.dashboard-card {
  height: 340px;
  overflow: hidden;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
  transition: transform 150ms ease-out, box-shadow 150ms ease-out, border-color 150ms ease-out;
}

.dashboard-card:hover {
  transform: translateY(-2px);
  border-color: #d1d5db;
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.07);
}

.network-card {
  grid-column: span 2;
}

.node-card {
  grid-column: span 3;
}

@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .network-card,
  .node-card {
    grid-column: span 2;
  }
}

@media (max-width: 768px) {
  .page-heading {
    align-items: flex-start;
    flex-direction: column;
    gap: 14px;
  }

  .dashboard-grid {
    grid-template-columns: 1fr;
  }

  .network-card,
  .node-card {
    grid-column: span 1;
  }

  .dashboard-card {
    height: 320px;
  }
}
</style>
