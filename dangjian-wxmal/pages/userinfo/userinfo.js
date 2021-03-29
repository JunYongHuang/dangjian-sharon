const util = require('../../utils/util.js');
const loginutil = require('../../utils/loginutil.js');
const {$MyToast} = require('../../utils/myToast.js');
//获取应用实例
const app = getApp();
const {
  $Toast
} = require('../../dist/base/index');

var _lastTime = null;//点击授权按钮控制时间
var gapTime = 1500;
Page({
  data: {
    active: 0,
    userInfo: {},
    hiddenmodalput: true,
    hiddenName: true,
    canIUse: wx.canIUse('button.open-type.getUserInfo'), 
    canvasWidth: "",
    canvasHeight: "",
    canvasLeft: "",
    canvasTop: "",
    maskHidden: true, //隐藏显示
    registInfo: {},
    code_disabled: false,
    currenttime: '获取验证码',
    unreadCount: 0,
    account: "",
    account: ""
  },

  aboutUs: function() {
    wx.navigateTo({
      url: '/pages/details/details?id=21893',
    });
  },

  onLoad: function() {
    var that = this;

    this.setData({   
			checkStatus: loginutil.isLogin(),
      unreadCount: wx.getStorageSync("unreadCount"),
      unreadMessage: wx.getStorageSync("unreadMessage")
    });

    //获取用户的当前设置。返回值中只会出现小程序已经向用户请求过的权限。
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          try {
            var value = wx.getStorageSync('userInfo');
            var loginInfo = wx.getStorageSync("loginInfo");

            if (value && loginInfo) {
              app.globalData.userInfo = value;
              that.setData({
                userInfo: value
              });
            } else {
              // $Toast({
              //   content: '请先登录',
              //   type: 'warn'
              // });
              that.setData({
                hiddenName: false
              });
            }
          } catch (e) {
            console.log(e);
          }

        } else {
          that.setData({
            userInfo: app.globalData.userInfo,
            hiddenName: false
          });
          // $Toast({
          //   content: '请先登录',
          //   type: 'warn'
          // });
        }
      }
    });
  },

  exitLogin: function() {
    wx.clearStorage();
    wx.reLaunch({
      url: '/pages/userinfo/userinfo',
    })
  },

  message: function() {
    wx.navigateTo({
      url: '/pages/message/message',
    });
  },

  bindGetUserInfo: function(e) {
    var that = this;

    let _nowTime = + new Date();
        if (_nowTime - _lastTime > gapTime || !_lastTime) {
            
            _lastTime = _nowTime
        }else{
          return;
        }

    //授权按钮
    if (e.detail.userInfo) {
      // //本地缓存
      // wx.setStorageSync('userInfo', e.detail.userInfo);
      // this.setData({
      //   userInfo: e.detail.userInfo,
      //   hasUserInfo: true
      // });
      // this.setData({
      //   hiddenName: true
      // });
      //调用登录
      wx.login({
        success(res) {
          if (res.code) {
            var code = res.code;
            // 必须是在用户已经授权的情况下调用
            wx.getUserInfo({
              success(res) {
                var userInfo = res.userInfo;
                //通过code换取openid
                wx.request({
                  url: util.basePath + '/api/whites/getOpenidInfo',
                  method: "post",
                  data: {
                    code: code
                  },
                  header: {
                    'content-type': 'application/json'
                  },
                  success(res) {
                    if (res.data.code == 200) {
                      var result = res.data.result;
                      if(result.whiteId>0){
                        wx.setStorageSync('loginInfo',res.data.result);
                        // that.setData({
                        //   hiddenmodalput: !that.data.hiddenmodalput
                        // });
              
                    wx.setStorageSync('userInfo', userInfo);
                    that.setData({
                      userInfo: userInfo,
                      hasUserInfo: true
                    });
                    that.setData({
                      checkStatus: loginutil.isLogin(),
                      hiddenName: true
                    });
              
                        $Toast({
                          content: '登录成功',
                          type: 'success'
                        });
                      }else{//没有绑定过手机
                        userInfo.openid = res.data.result.openId;
                        userInfo.sessionKey = res.data.result.sessionKey;
                        //若openid未注册，则弹出绑定手机号弹框获取验证码绑定手机号
                        that.setData({
                          hiddenmodalput: !that.data.hiddenmodalput,
                          registInfo: userInfo,
                          userInfo: userInfo
                        });
                      }



                    } else if(res.data.code == 500) {
            
                      $MyToast({
                        content: res.data.msg,
                        type: 'error'
                      });
                      
                    } else {
                      // wx.setStorage({
                      //   key: 'loginInfo',
                      //   data: res.data.err,
                      // });
                      $Toast({
                        content: '登陆失败',
                        type: 'warn'
                      });
                    }

                    // that.checkStatus();
                  }
                });
              }
            });
          }
        }
      });
    } else {
      //用户不同意授权
      this.setData({
        hiddenName: false
      });
    }
  },

  //获取手机号输入框的值
  accountInput: function(e) {
    this.setData({
      account: e.detail.value
    });
  },

  //获取验证码输入框的值
  codeInput: function(e) {
    this.setData({
      verify_code: e.detail.value
    });
  },
  //获取验证码
  apply_code: function(e) {
    var that = this;

    var errors = [];
    var phonenum = that.data.account;
// 		var userId = getUserId();
// 		if(userId == 0){
// 			errors.push("没有登录");
// 		}
		if(!phonenum || phonenum == ""){
			errors.push("手机号不能为空!");
		}else if(phonenum.length!=11 || ( !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(phonenum)) ) ){
			errors.push("手机号  必须为11位有效数字");			
		}
		
		if(errors.length > 0){
      $MyToast({
        content: errors.join('\n'),
        type: 'error'
      });
			
			return false;
			
		}

    that.setData({
      currenttime: 60
    });

    
      wx.request({
        url: util.basePath + '/api/whites/apply_code',
        method: "post",
        data: {
          account: that.data.account,
          app_sid: 'nyl',
          type: '0'
        },
        header: {
          'content-type': 'application/json'
        },
        success(res) {
          if (res.data.code == 200) {

            var currenttime = that.data.currenttime;

            var interval = setInterval(function() {
              that.setData({
                currenttime: currenttime - 1 + '秒',
                code_disabled: true
              });

              currenttime--;

              if (currenttime <= 0) {
                clearInterval(interval);
                that.setData({
                  currenttime: '获取验证码',
                  code_disabled: false
                });
              }
            }, 1000);
          } else if(res.data.code == 500) {
            
            $MyToast({
              content: res.data.msg,
              type: 'error'
            });
          } else {
            that.cancel();
            $Toast({
              content: '获取验证码异常!',
              type: 'error'
            });
          }
        }
      });
    
  },

  onReady: function() {

  },
  //取消绑定手机号
  cancel: function() {
    wx.clearStorage("userinfo");
    this.setData({
      userInfo: app.globalData.userInfo
    });
    this.setData({
      hiddenName: false
    });
    this.setData({
      hiddenmodalput: !this.data.hiddenmodalput
    });
  },
  onShow: function() {
    this.setData({
      maskHidden: true,
      showhaibao: false
    });
  },
  develop: function() {
    wx.showToast({
      title: "尚待开发!",
      icon: "warn",
    });
  },
  tapMyBaseInfo(e) {
    //跳转基本信息
    wx.navigateTo({
      url: '../myBaseInfo/myBaseInfo'
    })
  },
  tapMyBacklogs(e) {
    //跳转我的待办
    wx.navigateTo({
      url: '../myBacklogs/myBacklogs'
    })
  },
  tapMySpecials(e) {
    //跳转我的上传
    wx.navigateTo({
      url: '../mySpecialList/mySpecialList'
    })
  },
  //点击图片进行预览，长按保存分享图片
  previewImg: function(e) {
    var img = this.data.imagePath
    wx.previewImage({
      current: img, // 当前显示图片的http链接
      urls: [img] // 需要预览的图片http链接列表
    })
  },
  onPullDownRefresh() {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    setTimeout(function() {
      // 隐藏导航栏加载框
      wx.hideNavigationBarLoading();
      //停止当前页面下拉刷新。
      wx.stopPullDownRefresh();
    }, 1000);
  },
  confirm: function() {
    var that = this;
    var _userInfo = this.data.registInfo;
    _userInfo.type = '0';
    _userInfo.account = that.data.account;
    _userInfo.verify_code = that.data.verify_code;
    console.log(_userInfo);

    wx.request({
      url: util.basePath + '/api/whites/wx_regist',
      method: "post",
      data: _userInfo,
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        if (res.data.code == 200) {
          wx.setStorageSync('loginInfo',res.data.result);
          that.setData({
            hiddenmodalput: !that.data.hiddenmodalput
          });

      wx.setStorageSync('userInfo', _userInfo);
      that.setData({
        userInfo: _userInfo,
        hasUserInfo: true
      });
      that.setData({
        checkStatus: loginutil.isLogin(),
        hiddenName: true
      });

          $Toast({
            content: '绑定成功',
            type: 'success'
          });
        } else if(res.data.code == 500) {
            
          $MyToast({
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

  //通过绑定手机号登录
  // 获取手机号码
  getPhoneNumber: function (e) {
    var res_d = e;
    // var iv = e.detail.iv;
    // var encryptedData = e.detail.encryptedData;
    var that = this;
          var _userInfo = this.data.registInfo;
          _userInfo.type = '0';
          _userInfo.iv = e.detail.iv;
          _userInfo.encryptedData = e.detail.encryptedData;
          console.log(_userInfo);


    if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '未授权',
        success: function (res) { }
      })
    } else {
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '同意授权',
        success: function (res) {

          
      
          wx.request({
            url: util.basePath + '/api/whites/wx_phonenum',
            method: "post",
            data: _userInfo,
            header: {
              'content-type': 'application/json'
            },
            success(res) {
              if (res.data.code == 200) {
                wx.setStorageSync('loginInfo',res.data.result);
                that.setData({
                  hiddenmodalput: !that.data.hiddenmodalput
                });
      
            wx.setStorageSync('userInfo', _userInfo);
            that.setData({
              userInfo: _userInfo,
              hasUserInfo: true
            });
            that.setData({
              checkStatus: loginutil.isLogin(),
              hiddenName: true
            });
      
                $Toast({
                  content: '绑定成功',
                  type: 'success'
                });
              } else if(res.data.code == 500) {
                  
                $MyToast({
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
            },
            error: function (e) {
              wx.showToast({
                title: '网络异常！',
                duration: 2000
              });
            }
          });






          // wx.request({
          //   url: app.d.laikeUrl + '&action=user&m=secret_key',
          //   method: 'post',
          //   data: {
          //     encryptedData: encryptedData, // 加密数据
          //     iv: iv, // 加密算法
          //     sessionId: app.globalData.userInfo.session_key
          //   },
          //   header: {
          //     'Content-Type': 'application/x-www-form-urlencoded'
          //   },
          //   success: function (res) {
          //     var status = res.data.status;
          //     if (status == 1) {
          //       that.setData({
          //         islogin: true,
          //         mobile: res.data.info
          //       })
          //     } else {
          //       app.getUserInfo(that, res_d);
          //     }
          //   },
          //   error: function (e) {
          //     wx.showToast({
          //       title: '网络异常！',
          //       duration: 2000
          //     });
          //   }
          // })
        }
      })
    }
  },

});