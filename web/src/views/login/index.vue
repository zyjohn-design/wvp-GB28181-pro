<template>
  <div class="login-container">
    <div class="login-shell">
      <aside class="login-intro">
        <div class="login-brand">
          <span class="brand-mark">W</span>
          <strong>WVP 视频平台</strong>
        </div>
        <div class="intro-copy">
          <p>WVP VIDEO PLATFORM</p>
          <h1>让视频接入与运维<br>清晰、稳定、简单</h1>
          <span>统一管理设备、流媒体服务与实时预览，快速掌握平台运行状态。</span>
        </div>
        <div class="feature-pills">
          <span>GB28181</span>
          <span>实时预览</span>
          <span>设备管理</span>
        </div>
      </aside>

      <main class="login-panel">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
        <div class="title-container">
          <span class="mobile-brand-mark">W</span>
          <p>欢迎回来</p>
          <h3 class="title">登录 WVP 视频平台</h3>
          <small>请输入账号信息以继续访问控制台</small>
        </div>

        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>

        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>

      </el-form>
      <p class="login-footer">安全 · 稳定 · 高效</p>
      </main>
    </div>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      callback()
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch((error) => {
              this.$message({
                showClose: true,
                message: error,
                type: 'error'
              })
            })
            .finally(() => {
              this.loading = false
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
$bg: #fff;
$light_gray: #111827;
$cursor: #4f46e5;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  margin: 0;
  width: 100%;
  height: 100%;
  user-select: none;
  .el-input {
    display: inline-block;
    height: 47px;
    width: calc(100% - 34px);

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 8px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid #d1d5db;
    background: #fff;
    border-radius: 10px;
    color: #454545;
    transition: border-color 150ms ease-out, box-shadow 150ms ease-out;

    &:focus-within {
      border-color: #6366f1;
      box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.12);
    }
  }
}
</style>

<style lang="scss" scoped>
$dark_gray: #6b7280;
$light_gray: #111827;

.login-container {
  min-height: 100%;
  width: 100%;
  padding: 32px;
  background:
    radial-gradient(circle at 12% 15%, rgba(99, 102, 241, 0.12), transparent 32%),
    #f3f4f6;
  overflow: hidden;

  .login-shell {
    display: grid;
    grid-template-columns: minmax(360px, 1.08fr) minmax(420px, 0.92fr);
    width: min(1120px, 100%);
    min-height: calc(100vh - 64px);
    margin: 0 auto;
    overflow: hidden;
    border: 1px solid #e5e7eb;
    border-radius: 24px;
    background: #fff;
    box-shadow: 0 24px 60px rgba(15, 23, 42, 0.12);
  }

  .login-intro {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 48px;
    color: #fff;
    background:
      radial-gradient(circle at 82% 15%, rgba(129, 140, 248, 0.38), transparent 32%),
      linear-gradient(145deg, #111827 0%, #1f2937 62%, #312e81 100%);
  }

  .login-brand {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 17px;

    .brand-mark {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 38px;
      height: 38px;
      border-radius: 11px;
      background: #6366f1;
      box-shadow: 0 8px 20px rgba(99, 102, 241, 0.35);
      font-weight: 800;
    }
  }

  .intro-copy {
    max-width: 500px;

    p {
      margin: 0 0 20px;
      color: #a5b4fc;
      font-size: 12px;
      font-weight: 700;
      letter-spacing: 0.16em;
    }

    h1 {
      margin: 0 0 24px;
      font-size: clamp(34px, 4vw, 52px);
      line-height: 1.18;
      letter-spacing: -0.04em;
    }

    > span {
      display: block;
      max-width: 430px;
      color: #cbd5e1;
      font-size: 15px;
      line-height: 1.8;
    }
  }

  .feature-pills {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;

    span {
      padding: 8px 13px;
      border: 1px solid rgba(255, 255, 255, 0.13);
      border-radius: 999px;
      background: rgba(255, 255, 255, 0.07);
      color: #e5e7eb;
      font-size: 12px;
    }
  }

  .login-panel {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 56px 72px 32px;
  }

  .login-form {
    width: 100%;
    max-width: 100%;
    padding: 0;
    margin: 0;
  }

  .svg-container {
    padding: 6px 4px 6px 14px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    margin-bottom: 34px;

    .mobile-brand-mark {
      display: none;
    }

    p {
      margin: 0 0 8px;
      color: #6366f1;
      font-size: 13px;
      font-weight: 700;
    }

    .title {
      margin: 0 0 10px;
      color: $light_gray;
      font-size: 27px;
      line-height: 1.3;
      letter-spacing: -0.02em;
    }

    small {
      color: #9ca3af;
      font-size: 13px;
    }
  }

  .login-footer {
    margin: auto 0 0;
    color: #9ca3af;
    font-size: 12px;
    letter-spacing: 0.18em;
  }

  ::v-deep .el-button--primary {
    height: 46px;
    margin-top: 6px;
    border-color: #6366f1;
    border-radius: 10px;
    background: #6366f1;
    box-shadow: 0 10px 20px rgba(99, 102, 241, 0.18);
    font-weight: 600;

    &:hover,
    &:focus {
      border-color: #4f46e5;
      background: #4f46e5;
    }

    &:active {
      transform: scale(0.97);
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  @media (max-width: 900px) {
    padding: 20px;

    .login-shell {
      grid-template-columns: 1fr;
      min-height: calc(100vh - 40px);
    }

    .login-intro {
      display: none;
    }

    .login-panel {
      padding: 48px 32px 28px;
    }

    .title-container .mobile-brand-mark {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 42px;
      height: 42px;
      margin-bottom: 28px;
      border-radius: 12px;
      background: #6366f1;
      color: #fff;
      font-weight: 800;
    }
  }

  @media (max-width: 520px) {
    padding: 0;

    .login-shell {
      min-height: 100vh;
      border: 0;
      border-radius: 0;
    }

    .login-panel {
      padding: 36px 24px 24px;
    }
  }
}
</style>
