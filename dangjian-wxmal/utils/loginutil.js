
const { $Toast } = require('../dist/base/index');


function isLoginWithTip(){
  if(isLogin() == false){
    $Toast({
      content: "没有登录，请登录",
      type: 'error'
    });
    return false;
  }
  return true;
}

function isCommitteeWithTip(){
  if(isCommittee() == false){
    $Toast({
      content: "没有党委权限",
      type: 'error'
    });
    return false;
  }
  return true;
}

function isBranchWithTip(){
  if(isBranch() == false){
    $Toast({
      content: "没有党支部权限",
      type: 'error'
    });
    return false;
  }
  return true;
}

function isLogin(){
  return isCommittee() || isBranch();
}
/**
 * 是否是党委
 */
function isCommittee(){
  var loginInfo = wx.getStorageSync("loginInfo");
  if(loginInfo!=null && loginInfo.loginEnable=="true"){
      return loginInfo.roleId == 1;
  }
  return false;
}
/**
 * 是否是党支部
 */
function isBranch(){
  var loginInfo = wx.getStorageSync("loginInfo");
  if(loginInfo!=null && loginInfo.loginEnable=="true"){
    return loginInfo.roleId == 2;
  }
  return false;
}
;

module.exports = {
  isLogin,
  isLoginWithTip,
  isCommitteeWithTip,
  isBranchWithTip
}