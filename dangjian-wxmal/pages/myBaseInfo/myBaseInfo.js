//获取应用实例
const app = getApp()
const util = require('../../utils/util.js');
const { $Toast } = require('../../dist/base/index');
const ctx = wx.createCanvasContext('shareCanvas')
Page({
  data: {
    active: 0,
    userInfo: {},
    loginInfo:{},
    hiddenName: true,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),  //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canvasWidth: "",
    canvasHeight: "",
    canvasLeft: "",
    canvasTop: "",
    showhaibao:false,//隐藏显示
    maskHidden: true//隐藏显示
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that=this;
    //获取用户的当前设置。返回值中只会出现小程序已经向用户请求过的权限。
    wx.getSetting({
      success: res => {
        console.log('获取小程序已经向用户请求过的权限:')
        console.log(res)
        if (res.authSetting['scope.userInfo']) {         
          // 已经授权，可以直接调用 本地缓存 获取头像昵称
          console.log('已经授权')

          try {
            var value = wx.getStorageSync('userInfo')
            console.log(value);
            if (value) {
              app.globalData.userInfo = value;
              that.setData({ userInfo: value})
            }else{
                that.setData({ hiddenName: false });
                console.log('缓存中无数据');
            }
            var _loginInfo = wx.getStorageSync('loginInfo');
            if(_loginInfo){
              that.setData({loginInfo:_loginInfo});
            }
          } catch (e) {
              console.log(e);
          }

        } else {
          that.setData({ userInfo: app.globalData.userInfo});
            //未授权
          this.setData({hiddenName: false})
        }
      }
    })
  }, 
  
  // /**
  //  * 用户点击右上角分享
  //  */
  // onShareAppMessage: function (res) {
  //   if (res.from === 'button') {
  //     // 来自页面内转发按钮
  //     console.log(res.target)
  //   }
  //   return {
  //     title: '上研党建云',
  //     path: '/pages/index/index'
  //     // imageUrl:"" 
  //   }
  // },
  onReady: function () {
  
    // this.create();
    //创建初始化图片

  },
  onShow:function(){
    this.setData({
      maskHidden: true,
      showhaibao: false
    })
  },


  tapMyBacklogs(e) {
    let that = this;
        wx.showModal({
        title: '提示',
        content: '已绑定手机号，您确定要解绑吗？',
        showCancel: true,
        confirmText: "确定",
        success: function (res) {
          if (res.cancel) {
             //点击取消,默认隐藏弹框
          } else {
             //点击确定
             that.unBindPhone();
          }
       }
      })
  },

  unBindPhone(){
    var that = this;
    var _loginInfo = this.data.loginInfo;
    
    console.log(_loginInfo);

    wx.request({
      url: util.basePath + '/api/whites/unBindPhone',
      method: "post",
      data: {
        whiteId:_loginInfo.whiteId,
        openId:_loginInfo.openId
      },
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        if (res.data.code == 200) {
          wx.clearStorage();
          wx.reLaunch({
            url: '/pages/userinfo/userinfo',
         })

          $Toast({
            content: '解绑成功',
            type: 'success'
          });
        } else if(res.data.code == 500) {
            
          $Toast({
            content: res.data.msg,
            type: 'error'
          });
        } else {
          that.cancel();
          $Toast({
            content: res.data.msg,
            type: 'error'
          });
        }
      }
    });
  },

  /*
  获取图片，一般我们生成海报，海报上的二维码都是动态生成的，每次生成的二维码都不一样，且都是通过后台返回的图片地址。
  包括海报背景也是动态，后台返回会来的。所以我们现下载图片，生成临时路径。
  使用promise主要是海报可能有多个图片组成，必须等图片全部下载完成再去生成
  */
  getImage: function (url) {
    return new Promise((resolve, reject) => {
      wx.getImageInfo({
        src: url,
        success: function (res) {
          resolve(res)
        },
        fail: function () {
          reject("")
        }
      })
    })
  },
  getImageAll: function (image_src) {
    let that = this;
    var all = [];
    image_src.map(function (item) {
      all.push(that.getImage(item))
    })
    return Promise.all(all)
  },
  //创建
  create: function () {
    let that = this;
    //图片一把是通过接口请求后台，返回俩点地址，或者网络图片
    let bg = 'https://www.moohoy.com:9090/upload/2018/11/170644l325qooyabhioyaa20181224181300736.jpg';
    let qr = 'https://www.moohoy.com:9090/upload/2018/11/gh_389b881555ad_25820181225234435110.jpg';
    //图片区别下载完成，生成临时路径后，在尽心绘制
    this.getImageAll([bg, qr]).then((res) => {
      let bg = res[0];
      let qr = res[1];
      //设置canvas width height position-left,  为图片宽高
      this.setData({
        canvasWidth: bg.width + 'px',
        canvasHeight: bg.height + 'px',
        canvasLeft: `-${bg.width + 100}px`,
      })
      let ctx = wx.createCanvasContext('canvas');
      ctx.drawImage(bg.path, 0, 0, bg.width, bg.height);
      ctx.drawImage(qr.path, bg.width - qr.width - 100, bg.height - qr.height - 150, qr.width * 0.8, qr.height * 0.8)
      ctx.setFontSize(20)
      ctx.setFillStyle('black')
      ctx.setTextAlign('center')
      ctx.fillText('一款简单的小程序博客', bg.width - qr.width - 1, bg.height - qr.height - 190)
      ctx.draw(that.save());

      // wx.showModal({
      //   title: '提示',
      //   content: '图片绘制完成',
      //   showCancel: false,
      //   confirmText: "点击保存",
      //   success: function () {
      //     that.save()
      //   }
      // })


    })
  },
  //保存
  save: function () {
    var that = this;
    //canvas 生成图片 生成临时路径
    wx.canvasToTempFilePath({
      canvasId: 'canvas',
      success: function (res) {
        console.log(res.tempFilePath);
        var tempFilePath = res.tempFilePath;
        that.setData({imagePath: tempFilePath});
      
      }
    })
  },
  gotoSubmit: function (e) {
    $Toast({ content: '图片生成中...',type: 'loading', duration:0});
    var _this = this
    setTimeout(function () {
      _this.create();
      _this.setData({
        maskHidden: false,
        showhaibao: true
      })
      $Toast.hide();
    },2000)

  },
  //点击图片进行预览，长按保存分享图片
  previewImg: function (e) {
    var img = this.data.imagePath
    wx.previewImage({
      current: img, // 当前显示图片的http链接
      urls: [img] // 需要预览的图片http链接列表
    })
  },
  savelocalImg:function(){
    var _this = this
     wx.saveImageToPhotosAlbum({ 
          //下载图片
       filePath: _this.data.imagePath,
          success: function () {
            wx.showToast({
              title: "保存成功",
              icon: "success",
            })
            _this.setData({
              maskHidden: false,
              showhaibao: true
            })
          }
        });
  }, onPullDownRefresh() {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    setTimeout(function () {
      // 隐藏导航栏加载框
      wx.hideNavigationBarLoading();
      //停止当前页面下拉刷新。
      wx.stopPullDownRefresh()
    }, 1000)
  }
})
