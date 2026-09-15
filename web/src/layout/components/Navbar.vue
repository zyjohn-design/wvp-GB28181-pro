<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <span class="user-avatar">{{ avatarText }}</span>
          <span class="user-copy">
            <strong>{{ name || '管理员' }}</strong>
            <small>平台管理员</small>
          </span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <el-dropdown-item @click.native="changePassword">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="logout">
            <span style="display:block;">注销</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <changePasswordDialog ref="changePasswordDialog"></changePasswordDialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import changePasswordDialog from './dialog/changePassword.vue'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    changePasswordDialog
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name'
    ]),
    avatarText() {
      return (this.name || '管').trim().slice(0, 1).toUpperCase()
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      console.log('logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    changePassword() {
      this.$refs.changePasswordDialog.openDialog(this.logout)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 64px;
  overflow: hidden;
  position: relative;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
  box-shadow: none;

  .hamburger-container {
    line-height: 60px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background-color 150ms ease-out, color 150ms ease-out;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      color: #4f46e5;
      background: #f5f3ff;
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 64px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      height: 100%;
      margin-right: 22px;

      .avatar-wrapper {
        height: 100%;
        position: relative;
        cursor: pointer;
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 0 24px 0 10px;
        border-radius: 8px;
        transition: background-color 150ms ease-out;

        &:hover {
          background: #f9fafb;
        }

        .user-avatar {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          width: 34px;
          height: 34px;
          border-radius: 9px;
          color: #4338ca;
          background: #eef2ff;
          font-size: 14px;
          font-weight: 700;
          line-height: 34px;
        }

        .user-copy {
          display: flex;
          flex-direction: column;
          line-height: 1.25;
          color: #111827;

          strong {
            max-width: 120px;
            overflow: hidden;
            font-size: 13px;
            font-weight: 600;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          small {
            margin-top: 2px;
            color: #9ca3af;
            font-size: 11px;
          }
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: 7px;
          top: 26px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
