<#compress >
<#include "module/_macro.ftl">
<@head>${options.blog_title!} | <@spring.message code='admin.whites.edit.title' /></@head>
<div class="content-wrapper">
    <link rel="stylesheet" href="/static/halo-backend/plugins/simplemde/simplemde.min.css">
    <link rel="stylesheet" href="/static/halo-backend/plugins/jquery-tageditor/jquery.tag-editor.css">
    <link rel="stylesheet" href="/static/halo-backend/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <style type="text/css">
        #whiteTitle{font-weight: 400;}
    </style>
    <section class="content-header" id="animated-header">
        <h1 style="display: inline-block;"><@spring.message code='admin.whites.edit.title' /></h1>
<#--        <a class="btn-header" id="btnOpenAttach" href="javascript:void(0)" onclick="halo.layerModal('/admin/attachments/select?type=white','<@spring.message code="common.js.all-attachment" />')">-->
<#--            <@spring.message code='admin.editor.btn.attachs' />-->
<#--        </a>-->
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="javascript:void(0)"><i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' /></a>
            </li>
            <li>
                <a data-pjax="true" href="/admin/whites"><@spring.message code='admin.whites.title' /></a>
            </li>
            <li class="active"><@spring.message code='admin.whites.edit.title' /></li>
        </ol>
    </section>
    <!-- tab选项卡 -->
    <section class="content container-fluid" id="animated-content">
        <div class="row">
            <div class="col-md-12">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            基本资料
                        </li>

                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="general">
                            <form method="post" class="form-horizontal" id="profileForm">
                                <input type="hidden" id="whiteId" name="whiteId" value="${white.whiteId?c}">
                                <div class="box-body">
                                    <div class="form-group">
                                        <label for="whiteName" class="col-lg-2 col-sm-4 control-label">姓名：

                                        </label>
                                        <div class="col-lg-4 col-sm-8">
                                            <input type="text" class="form-control" id="whiteName" name="whiteName" value="${white.whiteName!}">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="phoneNo" class="col-lg-2 col-sm-4 control-label">手机号:

                                        </label>
                                        <div class="col-lg-4 col-sm-8">
                                            <input type="text" class="form-control" id="phoneNo" name="phoneNo" value="${white.phoneNo!}">
                                        </div>
                                    </div>



                                    <div class="form-group">
                                        <label for="roleId" class="col-lg-2 col-sm-4 control-label">所属:</label>
                                        <div class="col-lg-4 col-sm-8">
                                        <select class="form-control" id="roleId" name="roleId">
                                            <option value="1" <#if (white.roleId!1)==1>selected</#if>>党委</option>
                                            <option value="2" <#if (white.roleId!2)==2>selected</#if>>党支部</option>
                                        </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="sex" class="col-lg-2 col-sm-4 control-label">性别：</label>
                                        <div class="col-sm-8 control-radio">
                                            <div class="pretty p-default p-round">
                                                <input type="radio" name="sex" id="sex" value="0" ${(white.sex==0)?string('checked','')}>
                                                <div class="state p-primary">
                                                    <label>男</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default p-round">
                                                <input type="radio" name="sex" id="sex" value="1" ${(white.sex==1)?string('checked','')}>
                                                <div class="state p-primary">
                                                    <label>女</label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <label for="loginEnable" class="col-lg-2 col-sm-4 control-label">登录：</label>
                                        <div class="col-sm-8 control-radio">
                                            <div class="pretty p-default p-round">
                                                <input type="radio" name="loginEnable" id="loginEnable" value="true" ${((white.loginEnable!'true')=='true')?string('checked','')}>
                                                <div class="state p-primary">
                                                    <label>启用</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default p-round">
                                                <input type="radio" name="loginEnable" id="loginEnable" value="false" ${((white.loginEnable!'true')=='false')?string('checked','')}>
                                                <div class="state p-primary">
                                                    <label>禁用</label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>


                                </div>
                                <div class="box-footer">
                                    <button type="button" class="btn btn-primary btn-sm " onclick="push()"><@spring.message code='common.btn.save' /></button>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </section>

    <script>
        $(function () {
            $('[data-toggle="tooltip"]').tooltip()
        });

        /**
         * 提交文章
         * @param status 文章状态
         */
        function push() {
            var msgs = [];
            var errors = [];

            var name = $("#whiteName").val();

            var phonenum = $("#phoneNo").val();


            if(name == ""){
                errors.push("姓名 是必填项");
// 		}else if(name.length>4 || name.length<2 || (!/^[\u4e00-\u9fa5]+$/gi.test(name))){
// 			errors.push("姓名 必须为2-4个汉字");
            }else{
                msgs.push("姓名："+name);
            }


            if(phonenum == ""){
                errors.push("手机号 是必填项");
            }else if(phonenum.length!=11 || (! /^\d+$/.test(phonenum) )){
                errors.push("手机号  必须为11位有效数字");
            }else{
                msgs.push("手机："+phonenum);
            }


            if(errors.length > 0){
                halo.showMsg(errors.join('\n'),'info',2000);
                return false;

            }
            $.ajax({
                type: 'POST',
                url: '/admin/whites/update',
                async: false,
                data: {
                    'whiteId': $('#whiteId').val(),
                    'whiteName': name,
                    'phoneNo': phonenum,
                    'sex' : $('input:radio[name="sex"]:checked').val(),
                    'loginEnable' : $('input:radio[name="loginEnable"]:checked').val(),
                    'roleId' : $('#roleId').val()
                },
                success: function (data) {
                    if(data.code==1){

                        halo.showMsgAndRedirect(data.msg,'success',1000,'/admin/whites');
                    }else{
                        halo.showMsg(data.msg,'error',2000);
                    }
                }
            });
        }
    </script>
</div>
<@footer></@footer>
</#compress>
