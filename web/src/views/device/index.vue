<template>
  <div id="device" class="app-container">
    <deviceList
      v-show="deviceId === null"
      :access-type="accessType"
      @show-channel="showChannelList"
    />
    <channelList
      v-if="deviceId !== null"
      :device-id="deviceId"
      :access-type="accessType"
      @show-device="showDevice"
    />
  </div>
</template>

<script>
import deviceList from './list.vue'
import channelList from './channel/index.vue'

export default {
  name: 'Device',
  components: {
    deviceList,
    channelList
  },
  data() {
    return {
      deviceId: null
    }
  },
  computed: {
    accessType() {
      return this.$route.meta.accessType || 'DEVICE'
    }
  },
  watch: {
    '$route.meta.accessType'() {
      this.deviceId = null
    }
  },
  methods: {
    showChannelList: function(deviceId) {
      this.deviceId = deviceId
    },
    showDevice: function() {
      this.deviceId = null
    }
  }
}
</script>
