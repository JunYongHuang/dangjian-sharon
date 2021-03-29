<#compress >
<#include "module/_macro.ftl">
<@head>${options.blog_title!} | <@spring.message code='admin.specialTypes.title' /></@head>
<div class="content-wrapper">
    <section class="content-header" id="animated-header">
        <h1>
            <@spring.message code='admin.specialTypes.title' />
            <small></small>
        </h1>
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="/admin">
                    <i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' /></a>
            </li>
            <li><a data-pjax="true" href="javascript:void(0)"><@spring.message code='admin.specialTypes.bread.posts' /></a></li>
            <li class="active"><@spring.message code='admin.specialTypes.title' /></li>
        </ol>
    </section>
    <section class="content container-fluid" id="animated-content">
        <div class="row">
            <div class="col-md-5">
                <div class="box box-primary">
                    <#if updateSpecialType??>
                        <div class="box-header with-border">
                            <h3 class="box-title"><@spring.message code='admin.specialTypes.text.edit-specialType' /> <#if updateSpecialType??>[${updateSpecialType.specialTypeName}]</#if></h3>
                        </div>
<#--                        <form action="/admin/specialType/save" method="post" specialType="form" id="specialTypeAddForm">-->
                            <input type="hidden" id="specialTypeId" name="specialTypeId" value="${updateSpecialType.specialTypeId?c}">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="specialTypeName"><@spring.message code='admin.specialTypes.form.specialType-name' /></label>
                                    <input type="text" class="form-control" id="specialTypeName" name="specialTypeName" value="${updateSpecialType.specialTypeName}">
<#--                                    <small><@spring.message code='admin.specialTypes.form.specialType-name-tips' /></small>-->
                                </div>
                                <div class="form-group">
                                    <label for="specialTypeUrl"><@spring.message code='admin.specialTypes.form.specialType-url' /></label>
                                    <input type="text" class="form-control" id="specialTypeUrl" name="specialTypeUrl" value="${updateSpecialType.specialTypeUrl}">
                                    <small><@spring.message code='admin.specialTypes.form.specialType-url-tips' /></small>
                                </div>
                                <div class="form-group">
                                    <label for="specialTypeDesc" class="control-label"><@spring.message code='admin.specialTypes.form.specialType-desc' /></label>
                                    <textarea class="form-control" rows="3" id="specialTypeDesc" name="specialTypeDesc" style="resize: none">${updateSpecialType.specialTypeDesc}</textarea>
<#--                                    <small><@spring.message code='admin.specialTypes.form.specialType-desc-tips' /></small>-->
                                </div>

                                <div class="box box-primary">
                                    <div class="box-header with-border">
                                        <h3 class="box-title"><@spring.message code='admin.editor.text.thumbnail' /></h3>
                                        <div class="box-tools">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                                                <i class="fa fa-minus"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="box-body">
                                        <div>
                                            <img src="${updateSpecialType.postThumbnail!'/static/halo-frontend/images/thumbnail/thumbnail.png'}" class="img-responsive img-thumbnail" id="selectImg" onclick="halo.layerModal('/admin/attachments/select?id=selectImg','<@spring.message code="common.js.all-attachment" />')" style="cursor: pointer;">

                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="box-footer">
                                <button onclick="saveSpecialType()" class="btn btn-primary btn-sm "><@spring.message code='common.btn.define-edit' /></button>
                                <a data-pjax="true" href="/admin/specialType" class="btn btn-info btn-sm "><@spring.message code='common.btn.back-to-add' /></a>
                            </div>
<#--                        </form>-->
                    <#else >
                        <div class="box-header with-border">
                            <h3 class="box-title"><@spring.message code='admin.specialTypes.text.add-specialType' /></h3>
                        </div>
<#--                        <form action="/admin/specialType/save" method="post" specialType="form" id="specialTypeAddForm" onsubmit="return checkCate()">-->
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="specialTypeName"><@spring.message code='admin.specialTypes.form.specialType-name' /></label>
                                    <input type="text" class="form-control" id="specialTypeName" name="specialTypeName" placeholder="">
<#--                                    <small><@spring.message code='admin.specialTypes.form.specialType-name-tips' /></small>-->
                                </div>
                                <div class="form-group">
                                    <label for="specialTypeUrl"><@spring.message code='admin.specialTypes.form.specialType-url' /></label>
                                    <input type="text" class="form-control" id="specialTypeUrl" name="specialTypeUrl" placeholder="">
                                    <small><@spring.message code='admin.specialTypes.form.specialType-url-tips' /></small>
                                </div>
                                <div class="form-group">
                                    <label for="specialTypeDesc" class="control-label"><@spring.message code='admin.specialTypes.form.specialType-desc' /></label>
                                    <textarea class="form-control" rows="3" id="specialTypeDesc" name="specialTypeDesc" style="resize: none"></textarea>
<#--                                    <small><@spring.message code='admin.specialTypes.form.specialType-desc-tips' /></small>-->
                                </div>

                                <div class="box box-primary">
                                    <div class="box-header with-border">
                                        <h3 class="box-title"><@spring.message code='admin.editor.text.thumbnail' /></h3>
                                        <div class="box-tools">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                                                <i class="fa fa-minus"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="box-body">
                                        <div>
                                            <img src="/static/halo-frontend/images/thumbnail/thumbnail.png" class="img-responsive img-thumbnail" id="selectImg" onclick="halo.layerModal('/admin/attachments/select?id=selectImg','<@spring.message code="common.js.all-attachment" />')" style="cursor: pointer;">

                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="box-footer">
                                <button onclick="saveSpecialType()" class="btn btn-primary btn-sm "><@spring.message code='common.btn.define-add' /></button>
                            </div>
<#--                        </form>-->
                    </#if>
                </div>
            </div>
            <div class="col-md-7">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title"><@spring.message code='admin.specialTypes.text.all-specialTypes' /></h3>
                    </div>
                    <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                            <tbody>
                                <tr>
                                    <th><@spring.message code='common.th.name' /></th>
                                    <th><@spring.message code='common.th.url' /></th>
                                    <th><@spring.message code='common.th.desc' /></th>
                                    <th>专题数</th>
                                    <th><@spring.message code='common.th.control' /></th>
                                </tr>
                                <@commonTag method="specialTypes">
                                    <#if specialTypes?? && specialTypes?size gt 0>
                                        <#list specialTypes as specialType>
                                            <tr>
                                                <td>${specialType.specialTypeName}</td>
                                                <td>${specialType.specialTypeUrl}</td>
                                                <td>${(specialType.specialTypeDesc)!}</td>
                                                <td>
                                                    <span class="label" style="background-color: #d6cdcd;">${specialType.posts?size}</span>
                                                </td>
                                                <td>
                                                    <#if updateSpecialType?? && updateSpecialType.specialTypeId?c==specialType.specialTypeId?c>
                                                        <a href="javascript:void(0)" class="btn btn-primary btn-xs " disabled><@spring.message code='common.btn.editing' /></a>
                                                    <#else >
                                                        <a data-pjax="true" href="/admin/specialType/edit?specialTypeId=${specialType.specialTypeId?c}" class="btn btn-primary btn-xs "><@spring.message code='common.btn.modify' /></a>
                                                    </#if>
                                                    <button class="btn btn-danger btn-xs " onclick="modelShow('/admin/specialType/remove?specialTypeId=${specialType.specialTypeId?c}')"><@spring.message code='common.btn.delete' /></button>
                                                </td>
                                            </tr>
                                        </#list>
                                    </#if>
                                </@commonTag>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- 删除确认弹出层 -->
    <div class="modal fade" id="removeCateModal">
        <div class="modal-dialog">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title"><@spring.message code='common.text.tips' /></h4>
                </div>
                <div class="modal-body">
                    <p><@spring.message code='common.text.tips.to-delete' /></p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="url"/>
                    <button type="button" class="btn btn-default " data-dismiss="modal"><@spring.message code='common.btn.cancel' /></button>
                    <a onclick="removeIt()" class="btn btn-danger " data-dismiss="modal"><@spring.message code='common.btn.define' /></a>
                </div>
            </div>
        </div>
    </div>
    <script>
        function modelShow(url) {
            $('#url').val(url);
            $('#removeCateModal').modal();
        }
        function removeIt(){
            var url=$.trim($("#url").val());
            window.location.href=url;
        }



        function saveSpecialType() {
            var specialTypeId = $('#specialTypeId').val();
            var specialTypeName = $('#specialTypeName').val();
            var specialTypeUrl =  $('#specialTypeUrl').val();
            var specialTypeDesc = $('#specialTypeDesc').val();
            var postThumbnail = $('#selectImg').attr('src');
            var result = true;
            if(specialTypeName==""||specialTypeDesc==""){
                halo.showMsg("<@spring.message code='common.js.info-no-complete' />",'info',2000);
                return false;
            }
            var _data = {};
            if(specialTypeId != ""){
                _data = {
                    'specialTypeId':specialTypeId,
                    'specialTypeName':specialTypeName,
                    'specialTypeUrl':specialTypeUrl,
                    'specialTypeDesc':specialTypeDesc,
                    'postThumbnail':postThumbnail
                };
            }else{
                _data = {
                    'specialTypeName':specialTypeName,
                    'specialTypeUrl':specialTypeUrl,
                    'specialTypeDesc':specialTypeDesc,
                    'postThumbnail':postThumbnail
                };
            }

            $.ajax({
                type: 'POST',
                url: '/admin/specialType/save',
                async: false,
                data: _data,
                success: function (data) {
                    if(data.code==1){
                        halo.showMsgAndRedirect(data.msg,'success',1000,'/admin/specialType');
                    }else{
                        halo.showMsg(data.msg,'error',2000);
                    }
                }

            // $.ajax({
            //     type: 'GET',
            //     url: '/admin/specialType/checkUrl',
            //     async: false,
            //     data: {
            //         'specialTypeUrl' : url
            //     },
            //     success: function (data) {
            //         if(data.code==0){
            //             halo.showMsg(data.msg,'error',2000);
            //             result = false;
            //         }
            //     }
            });
            return result;
        }
    </script>
</div>
<@footer></@footer>
</#compress>
