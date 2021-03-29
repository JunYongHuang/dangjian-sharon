<#compress >
<#include "module/_macro.ftl">
<@head>${options.blog_title!} | <@spring.message code='admin.leaves.edit.title' /></@head>
<div class="content-wrapper">
    <link rel="stylesheet" href="/static/halo-backend/plugins/simplemde/simplemde.min.css">
    <link rel="stylesheet" href="/static/halo-backend/plugins/jquery-tageditor/jquery.tag-editor.css">
    <style type="text/css">
        #leaveTitle{font-weight: 400;}
    </style>
    <section class="content-header" id="animated-header">
        <h1 style="display: inline-block;"><@spring.message code='admin.leaves.edit.title' /></h1>
<#--        <a class="btn-header" id="btnOpenAttach" href="javascript:void(0)" onclick="halo.layerModal('/admin/attachments/select?type=leave','<@spring.message code="common.js.all-attachment" />')">-->
<#--            <@spring.message code='admin.editor.btn.attachs' />-->
<#--        </a>-->
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="javascript:void(0)"><i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' /></a>
            </li>
            <li>
                <a data-pjax="true" href="/admin/leaves"><@spring.message code='admin.leaves.title' /></a>
            </li>
            <li class="active"><@spring.message code='admin.leaves.edit.title' /></li>
        </ol>
    </section>




    <!-- tab选项卡 -->
    <section class="content container-fluid" id="animated-content">
        <div class="row">
            <div class="col-md-12">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a>基本资料</a>
                        </li>
                        
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="general">
                            <form method="post" class="form-horizontal" id="profileForm">

                                <div class="box-body">
                                    <div class="form-group">
                                        <label for="leaveContent" class="col-lg-2 col-sm-4 control-label">请假内容：

                                        </label>
                                        <div class="col-lg-4 col-sm-8">
                                            <textarea class="form-control" id="leaveContent" name="leaveContent" ></textarea>
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
             * 提交请假单
             * @param status 请假单状态
             */
            function push() {

                var msgs = [];
                var errors = [];

                var name = $("#leaveContent").val();

                var postId = 1191;


                if(name == ""){
                    errors.push("请假内容 是必填项");
// 		}else if(name.length>4 || name.length<2 || (!/^[\u4e00-\u9fa5]+$/gi.test(name))){
// 			errors.push("姓名 必须为2-4个汉字");
                }else{
                    msgs.push("请假内容："+name);
                }


                if(postId == ""){
                    errors.push("会议id 是必填项");
                }else{
                    msgs.push("会议id："+postId);
                }


                if(errors.length > 0){
                    halo.showMsg(errors.join('\n'),'info',2000);
                    return false;

                }

                $.ajax({
                    type: 'POST',
                    url: '/admin/leaves/save',
                    async: false,
                    data: {
                        'leaveContent': name,
                        'postId': postId,
                        'whiteId' : 548
                    },
                    success: function (data) {
                        if(data.code==1){

                            halo.showMsgAndRedirect(data.msg,'success',1000,'/admin/leaves');
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
