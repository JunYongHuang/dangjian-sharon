const util = require('../../utils/util.js');
const loginutil = require('../../utils/loginutil.js');
const { $Toast } = require('../../dist/base/index');
var id=1;

Page({
  data: {
    timer:null,
    timeNum:0,
    isSetRead:false,
    postType:"",
    post:{},
    myLeave:{},
    showhaibao:false

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    id=options.id;
    console.log('id='+id);
    var isLogin = loginutil.isLoginWithTip();
      if(!isLogin)
        return;
    that.getDetails(id);
  },
    /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
 

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  },

  // /**
  //  * 用户点击右上角分享
  //  */
  // onShareAppMessage: function () {
  //   if (res.from === 'button') {
  //     // 来自页面内转发按钮
  //     console.log(res.target)
  //   }
  //   return {
  //     title: '上研党建云',
  //     path: '/pages/details/details?id='+id
  //   }
  // },
  wxmlTagATap(e) {
    console.log(e);
  },

  getDetails:function(id){
    let that = this;
    var _loginInfo = wx.getStorageSync('loginInfo');
    wx.request({
      url: util.basePath + '/api/posts/mySpecials/'+id,
      method:"post",
      data: {
        whiteId:_loginInfo.whiteId,
        openId:_loginInfo.openId
      },
      success: res => {
        console.log(res);
        if(res.data.code==200){
            wx.stopPullDownRefresh();
          this.setData({ 
            post : res.data.result,
            myLeave:res.data.result.myLeave,
            postDate: util.getDateDiff(res.data.result.postDate )
            
            });
            // that.startTimer();
        } else if(res.data.code == 500) {
            
          $Toast({
            content: res.data.msg,
            type: 'error'
          });
        }else{
            $Toast({ content: '请求错误', type: 'error' });
        }
      }
    })
  },



})