<#compress >
<#include "module/_macro.ftl">
<@head>${options.blog_title!} | <@spring.message code='admin.whites.title' /></@head>
<div class="content-wrapper">
    <style type="text/css" rel="stylesheet">
        .draft,.publish,.trash{list-style:none;float:left;margin:0;padding-bottom:10px}
        .pretty{margin: 0;}
    </style>
    <section class="content-header" id="animated-header">
        <h1 style="display: inline-block;"><@spring.message code='admin.whites.title' /></h1>
        <a data-pjax="false" class="btn-header" id="btnNewWhite" href="/admin/whites/write">
            <@spring.message code='admin.whites.btn.new-white' />
        </a>
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="/admin">
                    <i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' />
                </a>
            </li>
            <li><a data-pjax="true" href="javascript:void(0)"><@spring.message code='admin.whites.title' /></a></li>
            <li class="active"><@spring.message code='admin.whites.bread.all-whites' /></li>
        </ol>
    </section>
    <section class="content container-fluid" id="animated-content">
        <div class="row">
<#--           <div class="col-xs-12">-->
<#--                <ul style="list-style: none;padding-left: 0">-->
<#--                    <li class="publish">-->
<#--                        <a data-pjax="true" href="/admin/whites" <#if status==0>style="color: #000" </#if>><@spring.message code='common.status.published' /><span class="count">(${publishCount})</span></a>&nbsp;|&nbsp;-->
<#--                    </li>-->
<#--                    <li class="draft">-->
<#--                        <a data-pjax="true" href="/admin/whites?status=1" <#if status==1>style="color: #000" </#if>><@spring.message code='common.status.draft' /><span class="count">(${draftCount})</span></a>&nbsp;|&nbsp;-->
<#--                    </li>-->
<#--                    <li class="trash">-->
<#--                        <a data-pjax="true" href="/admin/whites?status=2" <#if status==2>style="color: #000" </#if>><@spring.message code='common.status.recycle-bin' /><span class="count">(${trashCount})</span></a>-->
<#--                    </li>-->
<#--                </ul>-->
<#--            </div>-->
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary">
                    <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                            <tbody>
                                <tr>
                                    <th>姓名</th>
                                    <th>手机号</th>
                                    <th>所属</th>
                                    <th>性别</th>
                                    <th>登录</th>
                                    <th>新建日期</th>
                                    <th><@spring.message code='common.th.control' /></th>
                                </tr>
                                <#if whites.content?size gt 0>
                                    <#list whites.content as white>
                                        <tr>
                                            <td>
                                                    ${white.whiteName}
                                            </td>
                                            <td>
                                                ${white.phoneNo}
                                            </td>
                                            <td>
                                                <#if white.roleId == 1>
                                                    <label>党委</label>
                                                <#elseif white.roleId == 2>
                                                    <label>党支部</label>
                                                <#else >
                                                    <label>未设置</label>
                                                </#if>
                                            </td>
                                            <td>
                                                <#if white.sex == 0>
                                                    <label>男</label>
                                                <#else >
                                                    <label>女</label>
                                                </#if>
                                            </td>
                                            <td>
                                                <#if white.loginEnable == 'true'>
                                                    <label>启用</label>
                                                <#else >
                                                    <label>禁用</label>
                                                </#if>
                                            </td>
                                            <td>${white.whiteDate!?string("yyyy-MM-dd HH:mm")}</td>
                                            <td>

                                                        <a href="/admin/whites/edit?whiteId=${white.whiteId?c}" class="btn btn-info btn-xs "><@spring.message code='common.btn.edit' /></a>

                                                        <button class="btn btn-danger btn-xs " onclick="modelShow('/admin/whites/remove?whiteId=${white.whiteId?c}','<@spring.message code="common.text.tips.to-delete" />')"><@spring.message code='common.btn.delete' /></button>

                                            </td>
                                        </tr>
                                    </#list>
                                <#else>
                                    <tr>
                                        <th colspan="7" style="text-align: center"><@spring.message code='common.text.no-data' /></th>
                                    </tr>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                    <div class="box-footer clearfix">
                        <div class="no-margin pull-left">
                            <@spring.message code='admin.pageinfo.text.no' />${whites.number+1}/${whites.totalPages}<@spring.message code='admin.pageinfo.text.page' />
                        </div>
                        <div class="btn-group pull-right btn-group-sm" role="group">
                            <a data-pjax="true" class="btn btn-default <#if !whites.hasPrevious()>disabled</#if>" href="/admin/whites?">
                                <@spring.message code='admin.pageinfo.btn.first' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !whites.hasPrevious()>disabled</#if>" href="/admin/whites?page=${whites.number-1}">
                                <@spring.message code='admin.pageinfo.btn.pre' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !whites.hasNext()>disabled</#if>" href="/admin/whites?page=${whites.number+1}">
                                <@spring.message code='admin.pageinfo.btn.next' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !whites.hasNext()>disabled</#if>" href="/admin/whites?page=${whites.totalPages-1}">
                                <@spring.message code='admin.pageinfo.btn.last' />
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- 删除确认弹出层 -->
    <div class="modal fade" id="removeWhiteModal">
        <div class="modal-dialog">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title"><@spring.message code='common.text.tips' /></h4>
                </div>
                <div class="modal-body">
                    <p id="message"></p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="url"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><@spring.message code='common.btn.cancel' /></button>
                    <a onclick="removeIt()" class="btn btn-danger" data-dismiss="modal"><@spring.message code='common.btn.define' /></a>
                </div>
            </div>
        </div>
    </div>
    <script>
        function modelShow(url,message) {
            $('#url').val(url);
            $('#message').html(message);
            $('#removeWhiteModal').modal();
        }
        function removeIt(){
            var url=$.trim($("#url").val());
            window.location.href=url;
        }
    </script>
</div>
<@footer></@footer>
</#compress>
