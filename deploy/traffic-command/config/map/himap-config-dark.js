//HiMap 部署地址
var HOSTNAME = "http://219.147.31.25:30001/HiMap/"; // 公共地图
var gkmapHOSTNAME = "http://219.147.31.25:30001/HiMap/"; // 管控扩展地图

// 最初基于那种地图开发就配成哪种地图; 切换其它地图时,不需要再改此参数,只需要配置对应的级别
var BASICTYPE = "arcgis"; // arcgis  minemap  mapabc supermap_leaflet

// 地图中心
var CENTERPOINT = "114.2667,30.5833"; // 武汉：114.2667,30.5833 青岛：'120.3968628,36.3062376';
// 初始化缩放级别
var INITLEVEL = 4;
var DOWNLOADVERSION = "2.0";

var PrintConfigUrl = "";
if (window.location.href) {
  var hrefArr = window.location.href.split("/");
  if (hrefArr && hrefArr.length > 0) {
    hrefArr.splice(hrefArr.length - 1, 1);
    PrintConfigUrl = hrefArr.join("/");
  }
}

// geoServer 地址 (提供 WMS等服务)
var GEOSERVERURL = "http://10.18.221.134:8088/geoserver";

// 地图类型切换时 对应的级别
var zoomExtentA = [0, 9]; // arcgis   地图级别范围
var zoomExtentMine = [5, 17]; // minemap  地图级别范围
var zoomExtentM = [8, 17]; // mapabc   地图级别范围
var zoomExtentB = [8, 17]; // bmap   地图级别范围
var zoomExtentS_L = [7, 16]; // supermap 地图级别范围 BY JCC 2020.2.10

//******************** arcgis google离线地图配置项 ********************
var arcgisCS = "WGS84"; // 地图坐标系 'WGS84': 地心坐标系，GPS原始坐标体系     'GCJ02':国测局坐标，火星坐标系    'BD09':百度坐标系
var WKID = "3857";
var INITIALEXTENT = [
  -20037508.342787, -20037508.342787, 20037508.342787, 20037508.342787,
];
//var INITIALEXTENT = [13300042.921620506800 ,4232776.878319868820 ,13520181.563081812100,4460253.474496549930];
var FULLEXTENT = [
  -20037508.342787, -20037508.342787, 20037508.342787, 20037508.342787,
];
//var FULLEXTENT = [13300042.921620506800 ,4232776.878319868820 ,13520181.563081812100,4460253.474496549930];
var TILEINFO_ORIGINX = -20037508.342787;
var TILEINFO_ORIGINY = 20037508.342787;
var ROWS = 256;
var COLS = 256;
var LODS = [
  { level: 0, scale: 591657527.591555, resolution: 156543.033928 },
  { level: 1, scale: 295828763.795777, resolution: 78271.5169639999 },
  { level: 2, scale: 147914381.897889, resolution: 39135.7584820001 },
  { level: 3, scale: 73957190.948944, resolution: 19567.8792409999 },
  { level: 4, scale: 36978595.474472, resolution: 9783.93962049996 },
  { level: 5, scale: 18489297.737236, resolution: 4891.96981024998 },
  { level: 6, scale: 9244648.868618, resolution: 2445.98490512499 },
  { level: 7, scale: 4622324.434309, resolution: 1222.99245256249 },
  { level: 8, scale: 2311162.217155, resolution: 611.49622628138 },
  { level: 9, scale: 1155581.108577, resolution: 305.748113140558 },
  { level: 10, scale: 577790.554289, resolution: 152.874056570411 },
  { level: 11, scale: 288895.277144, resolution: 76.4370282850732 },
  { level: 12, scale: 144447.638572, resolution: 38.2185141425366 },
  { level: 13, scale: 72223.819286, resolution: 19.1092570712683 },
  { level: 14, scale: 36111.909643, resolution: 9.55462853563415 },
  { level: 15, scale: 18055.954822, resolution: 4.77731426794937 },
  { level: 16, scale: 9027.977411, resolution: 2.38865713397468 },
  { level: 17, scale: 4513.988705, resolution: 1.19432856685505 },
  { level: 18, scale: 2256.994353, resolution: 0.597164283559817 },
  { level: 19, scale: 1128.497176, resolution: 0.298582141647617 },
  { level: 20, scale: 564.248588, resolution: 0.14929107082380833 },
  { level: 21, scale: 282.124294, resolution: 0.07464553541190416 },
  { level: 22, scale: 141.062147, resolution: 0.03732276770595208 },
  { level: 23, scale: 70.5310735, resolution: 0.01866138385297604 },
];
var lods = [
  { level: 6, scale: 9244648.868618, resolution: 2445.98490512499 },
  { level: 7, scale: 4622324.434309, resolution: 1222.99245256249 },
  { level: 8, scale: 2311162.217155, resolution: 611.49622628138 },
  { level: 9, scale: 1155581.108577, resolution: 305.748113140558 },
  { level: 10, scale: 577790.554289, resolution: 152.874056570411 },
  { level: 11, scale: 288895.277144, resolution: 76.4370282850732 },
  { level: 12, scale: 144447.638572, resolution: 38.2185141425366 },
  { level: 13, scale: 72223.819286, resolution: 19.1092570712683 },
  { level: 14, scale: 36111.909643, resolution: 9.55462853563415 },
  { level: 15, scale: 18055.954822, resolution: 4.77731426794937 },
  { level: 16, scale: 9027.977411, resolution: 2.38865713397468 },
  { level: 17, scale: 4513.988705, resolution: 1.19432856685505 },
  // { level: 18, scale: 2256.994353, resolution: 0.597164283559817 }
];
/*
var GETTILEURL = function(level,row,col){
    // hicon-manager 瓦片
    //如果瓦片是2.0的，修改下面瓦片的路径即可
	// return "http://10.16.3.45:8060/manager/mapfile/title?col=" + col + "&row=" + row + "&level=" + level;
    // 如果瓦片是1.0的 用下面的配置
    //return "http://10.18.221.134:804/HiMap/getTiledFile?Service=getImage&Type=RGB&Col="+col+"&Row="+row+"&Zoom="+level+"&V=1.0&FILEPATH=TILEDFILEPATH";

   return 'http://10.16.1.56:9499/HiMap/getTiledFile?Service=getImage&Type=RGB&Col='+col+'&Row='+row+'&Zoom='+level+'&V=1.0&FILEPATH=TILEDFILEPATH';//地图服务
};*/

var GETTILEURL = function (level, row, col) {
  // var url = HiMapConfig.HOSTNAME+ 'getTiledFile?Service=getImage&Type=RGB&Col={col}&Row={row}&Zoom={zoom}&V=' +
  // DOWNLOADVERSION + '&FILEPATH=TILEDFILEPATH';
  // url = url.replace(/{row}/, row);
  // url = url.replace(/{col}/, col);
  // url = url.replace(/{zoom}/, level);
  // url = url.replace(/{z}/,level);
  // return url;
  // var url =  "http://10.16.3.178:8101/HiatmpView/sdvector/" + level + "/"  + col + "/" + row + ".png";
  //return url;
  var row16 = Number(row).toString(16).padStart(6, "0");
  var col16 = Number(col).toString(16).padStart(6, "0");
  var firstPath = col16.substring(0, 1) + "-" + row16.substring(0, 1);
  var secondPath = col16.substring(0, 2) + "-" + row16.substring(0, 2);
  var thirdPath = col16.substring(0, 3) + "-" + row16.substring(0, 3);
  var fourthPath = col16.substring(0, 4) + "-" + row16.substring(0, 4);
  var fifthPath = col16.substring(0, 5) + "-" + row16.substring(0, 5);
  return (
    "http://219.147.31.25:30001/HiMap/vectors/dark/" +
    level +
    "/" +
    firstPath +
    "/" +
    secondPath +
    "/" +
    thirdPath +
    "/" +
    fourthPath +
    "/" +
    fifthPath +
    "/g_" +
    col16 +
    "_" +
    row16 +
    "_" +
    level +
    ".png"
  );
};
//******************** arcgis WMS地图配置项 ********************
var WMSLAYERNAME = "sde:OSMROADS"; // 'sde:osm_roads';

//******************** minemap地图配置项 ********************
// var minemapCS = 'GCJ02'; // 地图坐标系 'WGS84': 地心坐标系，GPS原始坐标体系     'GCJ02':国测局坐标，火星坐标系    'BD09':百度坐标系
// var MINEMAPURL = HOSTNAME + "vendor/MineMap/3.0/minemap.js"; //如果配置，则加载此js;  如果不配置,加载HiMap 内部的js
// var MINEMAPCSSURL = HOSTNAME + "vendor/MineMap/3.0/minemap.css"; //如果配置，则加载此css;  如果不配置,加载HiMap 内部的css
// var SUPPORTCSS='https://minedata.cn/support/static/api/demo/javascript-3d-api/zh/css/demo.css';
// // var fontsUrl = "http://172.29.89.141" + "/minemapapi/"+"v2.0.0"+"/fonts";
// var domainUrl = "https://minedata.cn";     // 外网: '//minedata.cn';"https://minedata.cn";
// var dataDomainUrl = 'https://minedata.cn';//['https://datahive.minedata.cn','https://datahive01.minedata.cn','https://datahive02.minedata.cn','https://datahive03.minedata.cn','https://datahive04.minedata.cn']; //minemap2.0需要此参数; 内网图吧不需要此参数,配置上也无影响
// var spriteUrl = 'https://minedata.cn/minemapapi/v3.2.0/sprite/sprite';//'//10.16.3.55/minemapapi/v1.3/sprite/sprite';        //'https://minedata.cn/minemapapi/v2.1.0/sprite/sprite';
// var serviceUrl ='https://mineservice.minedata.cn/service/'; //"//10.16.3.55//service" ; //外网: '//minedata.cn/service''https://mineservice.minedata.cn/service/'
// var serverDomainUrl = 'https://minedata.cn';
// // var accessToken ='25cc55a69ea7422182d00d6b7c0ffa93';//; '16be596e00c44c86bb1569cb53424dc9';
// var solution =12877;    //12878;
// var key = '16be596e00c44c86bb1569cb53424dc9';
// var style = 'https://mineservice.minedata.cn/service/solu/style/id/12878'; //"https://mineservice.minedata.cn/service/solu/style/id/12878";//外网: '//minedata.cn/service/solu/style/id/2365'
// var pitch = 55;
// var dragRotate = true
var baseLayerType = "vector"; //  raster 栅格
var minemapCS = "GCJ02"; //'WGS84'; // 地图坐标系 'WGS84': 地心坐标系，GPS原始坐标体系     'GCJ02':国测局坐标，火星坐标系    'BD09':百度坐标系
var MINEMAPURL = "https://minemap.minedata.cn/minemapapi/v2.1.1/minemap.js"; //'//219.147.31.25:30001/minemapapi/v3.4.0/minemap.js';
var MINEMAPCSSURL = "https://minemap.minedata.cn/minemapapi/v2.1.1/minemap.css"; //'//219.147.31.25:30001/minemapapi/v3.4.0/minemap.css';
var SUPPORTCSS =
  "https://minedata.cn/nce-static/support/demo/js-api/zh/css/demo.css";
var domainUrl = "https://minemap.minedata.cn"; //'//219.147.31.25:30001';
var dataDomainUrl = "https://minemap.minedata.cn"; //'//219.147.31.25:30001';
var spriteUrl = "https://minemap.minedata.cn/minemapapi/v2.1.1/sprite/sprite"; //'//219.147.31.25:30001/minemapapi/v3.4.0/sprite/sprite';
//var serviceUrl = '//219.147.31.25:30001/service';
var serviceUrl = "https://service.minedata.cn/service";
var serverDomainUrl = "https://ol-data.minedata.cn";
// 深色底图样式 id：11003 极夜蓝；同服务上还有 11004 淡雅白(浅色)、11002 都市黑夜
//var solution = '1342057518176727040'; //'1328730278982643712'; //11003
var solution = "11003";
var style = "https://service.minedata.cn/map/solu/style/" + solution; //1328730278982643712';
//var key = '78c2bfce61bb48d599560c5079bbadcf';
var key = "efd950ce811d46faa1c74c7f651716a5";
var pitch = 0;
var dragRotate = true;
var projection = "MERCATOR";

//******************** mapabc地图配置项 ********************
var mapabcCS = "GCJ02"; // 地图坐标系 'WGS84': 地心坐标系，GPS原始坐标体系     'GCJ02':国测局坐标，火星坐标系    'BD09':百度坐标系
var MAPABCURL = HOSTNAME + "vendor/MapABC/1.0/js/API.js"; //MapABC引擎地址
var host = "10.16.3.58";
var mapabcGLStyle = "mapabc80";
var ROADNETURL = "http://10.16.3.58:25033/v3/tile?x={x}&y={y}&z={z}";
//var MAPABCURL = "http://10.16.3.58:25001/as/webapi/js/auth?v=1.0&t=jsmap&ak=ec85d3648154874552835438ac6a02b2";
var MAPABCEXTENT = [
  119.476318359375, 35.505400093441, 121.453857421875, 37.151560502237,
]; //地图范围

//******************** supermap_leaflet地图配置项  BY JCC 2020.2.10 ********************
//var SUPERMAPURL = HOSTNAME + "vendor/SuperMap/leaflet/dist/include-leaflet.js"; //如果配置，则加载此js;  如果不配置,加载HiMap 内部的js
var supermap_leafletCS = "GCJ02"; // 地图坐标系 'WGS84': 地心坐标系，GPS原始坐标体系     'GCJ02':国测局坐标，火星坐标系    'BD09':百度坐标系
var leafletPlugins = "editable,heat,echarts,miniMap"; //加载插件包含的功能: editable-绘制功能， heat-热力图，echarts-echart功能，miniMap-鹰眼功能

//地图配置
var SUPERMAPSERVER =
  "http://10.16.1.2:8090/iserver/services/map-China100/rest/maps/China"; // "http://10.16.1.2:8090/iserver/services/map-China100/rest/maps/ChinaDark"
var mapCRS = {
  name: "EPSG:3857",
  value:
    "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs",
}; //坐标转换参数，不设置 默认4326
var boundsL = [-20037508.34, -20037508.34]; //地图全幅范围-左下
var boundsR = [20037508.34, 20037508.34]; //地图全幅范围-右上
var scaleDenominators = null; // [1000000,500000, 250000, 100000, 50000, 25000, 10000, 5000];//显示比例尺数组

//******************** bmap地图配置项  ****	****************
// 百度地图类型  offLine: 加载离线瓦片   server:加载百度地图服务发布的地图
var BMAPSTYLE = "offLine";
var bmapCS = "BD09"; // 地图坐标系 'WGS84': 地心坐标系，GPS原始坐标体系     'GCJ02':国测局坐标，火星坐标系    'BD09':百度坐标系

//******************** bmap离线地图配置项 （BMAPSTYLE == 'offLine' 时配置此部分参数）********************
var BMAPURL = HOSTNAME + "vendor/BMap/"; //BMap引擎地址
var BMAPRoadURL = "/HiMap/baidumaps/roadmap/"; //街道图(地图服务地址)
var BMAPSatelliteURL = "/HiMap/baidumaps/satellite/"; //卫星图(地图服务地址)
var BMAPOverlayURL = "/HiMap/baidumaps/overlay/"; //混合图(地图服务地址)
//var BMAPEXTENT = [119.487304687500000,35.550105335885505,121.014404296875000,37.169071577130111];//地图范围
var BMAPEXTENT = [-180, -90, 180, 90]; //地图范围
var FILEEXTENSION = "png";

//******************** 超图cesium地图配置项  ********************
var TILESSERVER =
  "http://10.16.1.56:9499/HiMap/getTiledFile?Service=getImage&Type=RGB&Col={TileCol}&Row={TileRow}&Zoom={TileMatrix}&V=1.0&FILEPATH=TILEDFILEPATH"; //地图服务
//var TILESSERVER ="http://10.16.3.45:8060/manager/mapfile/title?col={TileCol}&row={TileRow}&level={TileMatrix}";
var VIEWHEIGHT = [
  { level: 0, scale: 384000 },
  { level: 1, scale: 192000 },
  { level: 2, scale: 192000 },
  { level: 3, scale: 96000 },
  { level: 4, scale: 48000 },
  { level: 5, scale: 24000 },
  { level: 6, scale: 12000 },
  { level: 7, scale: 6000 },
  { level: 8, scale: 3000 },
  { level: 9, scale: 1500 },
  { level: 10, scale: 750 },
];
