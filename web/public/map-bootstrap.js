/* HiMap 的脚本依赖仍按解析顺序加载，兼容 SDK 内部 document.write。
 * 入口由前端镜像管理；现场的 /map/himap-config-*.js 继续独立维护。
 */
(function () {
  'use strict';
  var state = window._MapLoadState = window._MapLoadState || { phase: 'config', error: false, message: '' };
  var version = new URLSearchParams(window.location.search).get('v') || Date.now().toString(36);
  var engineReady = false;
  var scriptsParsed = false;
  var initialized = false;
  var options = {};

  function url(path) {
    return path + (path.indexOf('?') < 0 ? '?' : '&') + '_mapv=' + encodeURIComponent(version);
  }
  function attr(value) {
    return value.replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/</g, '&lt;');
  }
  function fail(message, resource) {
    state.error = true;
    state.message = message + (resource ? '：' + resource : '');
    state.url = resource;
    console.error('[HiMap]', state.phase, state.message);
  }
  function writeScript(path, extra) {
    // 旧 SDK 通过 src 以 himap.js 结尾识别 maptype/onready，不能附加查询参数。
    document.write('<script src="' + attr(path) + '" ' + (extra || '') +
      ' onerror="HiMapBootstrap.fail(\'地图脚本加载失败\',this.src)"><\/script>');
  }
  function initialize() {
    if (!engineReady || !scriptsParsed || initialized || state.error) return;
    initialized = true;
    state.phase = 'initialize';
    try {
      var container = document.getElementById('map');
      if (!container) throw new Error('地图容器不存在');
      var config = {
        whoUse: 'react', subSystem: 'face',
        centerPoint: window.CENTERPOINT, initLevel: window.INITLEVEL
      };
      if (options.pitch !== undefined) config.pitch = options.pitch;
      var mapApp = new window.HiMap(container, config);
      if (window.HiMapConfig) window.HiMapConfig.whoUse = 'react';
      window._MapApp = mapApp;
      // 管控扩展属于附加能力；扩展缺失不能使已经创建的底图消失。
      if (typeof window.extendHiMap === 'function') {
        try { window.extendHiMap(); } catch (error) { console.warn('[HiMap] 管控扩展初始化失败', error); }
      }
      if (typeof window.TrackPlay !== 'undefined') mapApp.TrackPlay = window.TrackPlay;
      state.phase = 'map';
    } catch (error) {
      fail('地图初始化失败：' + error.message);
    }
  }
  // 定义回调、准备 DOM 后再加载 SDK，消除缓存命中时回调提前到达的竞态。
  window.mapready = function () { engineReady = true; initialize(); };
  window.HiMapBootstrap = {
    fail: fail,
    loadEngine: function (config) {
      options = config || {};
      if (state.error) return;
      if (typeof window.HOSTNAME !== 'string' || !window.HOSTNAME.trim()) {
        fail('地图配置无效，缺少 HOSTNAME');
        return;
      }
      state.phase = 'engine';
      writeScript(window.HOSTNAME + 'himap.js', 'maptype="minemap" onready="mapready"');
      document.write('<link rel="stylesheet" href="' + attr(url(window.HOSTNAME + 'himap.css')) +
        '" onerror="HiMapBootstrap.fail(\'地图样式加载失败\',this.href)">');
      var extension = (window.gkmapHOSTNAME || window.HOSTNAME) + 'assets/js/gkm/index/himap_gkpt.js';
      document.write('<script src="' + attr(url(extension)) +
        '" onload="HiMapBootstrap.scriptsLoaded()" onerror="console.warn(\'[HiMap] 管控扩展加载失败\',this.src);HiMapBootstrap.scriptsLoaded()"><\/script>');
    },
    scriptsLoaded: function () { scriptsParsed = true; initialize(); }
  };
  var script = document.currentScript;
  if (script && script.getAttribute('data-himap-autostart') === 'true') {
    var pitch = script.getAttribute('data-pitch');
    window.HiMapBootstrap.loadEngine(pitch === null ? {} : { pitch: Number(pitch) });
  }
}());
