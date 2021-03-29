// 查看事件文档https://developers.weixin.qq.com/miniprogram/dev/api/media/editor/EditorContext.html
const app = getApp();
const util = require('../../utils/util.js');
const loginutil = require('../../utils/loginutil.js');
const { $Toast } = require('../../dist/base/index');
var id=1;
Page({
  data: {
    formats: {},
    bottom: 0,
    readOnly: false,
    placeholder: '介绍一下你的专题吧，支持文字和图片...',
    _focus: false,
    postTitle:"",
    post:{}
  },
  readOnlyChange() {
    this.setData({
      readOnly: !this.data.readOnly
    })
  },
    /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    id=options.id;
    if(!id)
      id=838;
    console.log('id='+id);
    
  },

  // 编辑器初始化完成时触发
  onEditorReady() {
    const that = this;
    wx.createSelectorQuery().select('#editor').context(function(res) {
      that.editorCtx = res.context;

      var isLogin = loginutil.isLoginWithTip();
      if(!isLogin)
        return;
        that.getDetails(id);

    }).exec();
  },
  undo() {
    this.editorCtx.undo();
  },
  redo() {
    this.editorCtx.redo();
  },
  format(e) {
    let {
      name,
      value
    } = e.target.dataset;
    if (!name) return;
    // console.log('format', name, value)
    this.editorCtx.format(name, value);
  },
  // 通过 Context 方法改变编辑器内样式时触发，返回选区已设置的样式
  onStatusChange(e) {
    const formats = e.detail;
    this.setData({
      formats
    });
  },
  // 插入分割线
  insertDivider() {
    this.editorCtx.insertDivider({
      success: function() {
        console.log('insert divider success')
      }
    });
  },
  // 清除
  clear() {
    this.editorCtx.clear({
      success: function(res) {
        console.log("clear success")
      }
    });
  },
  // 移除样式
  removeFormat() {
    this.editorCtx.removeFormat();
  },
  // 插入当前日期
  insertDate() {
    const date = new Date()
    const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
    this.editorCtx.insertText({
      text: formatDate
    });
  },
  // 插入图片
  insertImage() {

    this.uploadPhoto();
    return;
    wx.chooseImage({
      count: 1,
      success: () => {
        this.editorCtx.insertImage({
          src: 'https://www.moohoy.com:9090//upload/2020/10/%E5%8D%95%E4%BD%8D%E8%B4%A6%E6%88%B7%E5%BC%80%E7%AB%8B%E9%80%9A%E7%9F%A5%E4%B9%A62020110512225780.jpg',
          width:'100%',
          data: {
            id: 'abcd',
            role: 'god'
          },
          success: () => {
            console.log('insert image success')
          }
        })
      }
    });
  },
  //选择图片
  chooseImage(e) {
    wx.chooseImage({
      sizeType: ['original', 'compressed'], //可选择原图或压缩后的图片
      sourceType: ['album', 'camera'], //可选择性开放访问相册、相机
      success: res => {
        const images = this.data.images.concat(res.tempFilePaths);
        this.data.images = images.length <= 3 ? images : images.slice(0, 3);
      }
    })
  },

   uploadPhoto() {
   var that = this; 
   wx.chooseImage({
   count: 1, // 默认9
   sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
   sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
   success: function (res) {
   // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
   var tempFilePaths = res.tempFilePaths;
   that.upload(that, tempFilePaths);
   }
   })
   },
  
   upload:function (page, path) {
     var that = this;
     var _loginInfo = wx.getStorageSync('loginInfo');
   wx.showToast({
   icon: "loading",
   title: "正在上传"
   }),
   wx.uploadFile({
   url: util.basePath + "/api/specials/upload",
   filePath: path[0], 
   name: 'file',
   header: { "Content-Type": "multipart/form-data" },
   formData: {
    //和服务器约定的token, 一般也可以放在header中
         whiteId:_loginInfo.whiteId
    },
   success: function (res) {
   console.log(res);

   if (res.statusCode != 200) { 
    wx.showModal({
    title: '提示',
    content: '上传失败',
    showCancel: false
    })
    return;
   }
   var realData = JSON.parse(res.data);

   if (realData.code == 200) {
    var imgUrl = realData.result;
    var _src = util.basePath + imgUrl;
    that.editorCtx.insertImage({
     src: _src,
     width:'100%',
     data: {
       id: 'abcd',
       role: 'god'
     },
     success: () => {
       console.log('insert image success')
     }
   })
  } else if(realData.code == 500) {
    $MyToast({
      content: realData.msg,
      type: 'error'
    });    
  } else {

    $Toast({
      content: '登陆失败',
      type: 'warn'
    });
  }
   },
   fail: function (e) {
   console.log(e);
   wx.showModal({
    title: '提示',
    content: '上传失败',
    showCancel: false
   })
   },
   complete: function () {
   wx.hideToast(); //隐藏Toast
   }
   })
  },
  
  titleInput:function(e){
    this.setData({postTitle:e.detail.value});

  },
  //查看详细页面
  toDeatil() {
    var that = this;
    this.editorCtx.getContents({
      success: (res) => {
        console.log(res.html);
        app.globalData.postTitle = that.data.postTitle;
        app.globalData.html = res.html;
        wx.navigateTo({
          url: '../specialDetail/specialDetail'
        })
     
      },
      fail: (res) => {
        console.log("fail：" , res);
      }
    });
  },
  //提交专题
  submit_article() {
    var that = this;
    this.editorCtx.getContents({
      success: (res) => {
        
        var postTitle = that.data.postTitle;
        var postContent = res.html;
        if(postTitle == "" || postContent == ""){
          $Toast({
            content: "标题或者内容不能为空！",
            type: 'error'
          });
          return;

        }

        console.log(postContent)
        this.realSubmit(postContent);
        
     
      },
      fail: (res) => {
        console.log("fail：" , res);
      }
    });
  },
  realSubmit:function(_postContent){
    var that = this;
    // if(this.data.isSetRead == true){
    //   return;
    // }
    // this.data.isSetRead = true;
    var _loginInfo = wx.getStorageSync('loginInfo');
    wx.request({
      url: util.basePath + '/api/specials/updateSpecial',
      method:"post",
      data: {
        whiteId:_loginInfo.whiteId,
        openId:_loginInfo.openId,
        postId:id,
        postTitle:that.data.postTitle,
        postContent:_postContent
      },
      success: res => {
        console.log(res);
        if(res.data.code==200){
          var postId = res.data.result;
          // $Toast({
          //   content: '发布成功',
          //   type: 'success'
          // });

          wx.redirectTo({
            url: '../mySpecialDetails/mySpecialDetails?id='+postId,
          })
          
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
          var _post = res.data.result;

          that.editorCtx.setContents({
                  html: _post.postContent,
                  success: (res) => {
                    console.log(res)
                  },
                  fail: (res) => {
                    console.log(res)
                  }
                });

          that.setData({ 
            post : _post,
            postTitle:_post.postTitle
            
            });
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
  }
  
})