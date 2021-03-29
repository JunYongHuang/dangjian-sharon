<#compress >
<#include "module/_macro.ftl">
<@head>${options.blog_title!} | <@spring.message code='admin.leaves.title' /></@head>
<div class="content-wrapper">
    <style type="text/css" rel="stylesheet">
        .draft,.publish,.trash{list-style:none;float:left;margin:0;padding-bottom:10px}
        .pretty{margin: 0;}
    </style>
    <section class="content-header" id="animated-header">
        <h1 style="display: inline-block;"><@spring.message code='admin.leaves.title' /></h1>
<#--        <a data-pjax="false" class="btn-header" id="btnNewLeave" href="/admin/leaves/write">-->
<#--            <@spring.message code='admin.leaves.btn.new-leave' />-->
<#--        </a>-->
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="/admin">
                    <i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' />
                </a>
            </li>
            <li><a data-pjax="true" href="javascript:void(0)"><@spring.message code='admin.leaves.title' /></a></li>
            <li class="active"><@spring.message code='admin.leaves.bread.all-leaves' /></li>
        </ol>
    </section>
    <section class="content container-fluid" id="animated-content">
        <div class="row">
<#--           <div class="col-xs-12">-->
<#--                <ul style="list-style: none;padding-left: 0">-->
<#--                    <li class="publish">-->
<#--                        <a data-pjax="true" href="/admin/leaves" <#if status==0>style="color: #000" </#if>><@spring.message code='common.status.published' /><span class="count">(${publishCount})</span></a>&nbsp;|&nbsp;-->
<#--                    </li>-->
<#--                    <li class="draft">-->
<#--                        <a data-pjax="true" href="/admin/leaves?status=1" <#if status==1>style="color: #000" </#if>><@spring.message code='common.status.draft' /><span class="count">(${draftCount})</span></a>&nbsp;|&nbsp;-->
<#--                    </li>-->
<#--                    <li class="trash">-->
<#--                        <a data-pjax="true" href="/admin/leaves?status=2" <#if status==2>style="color: #000" </#if>><@spring.message code='common.status.recycle-bin' /><span class="count">(${trashCount})</span></a>-->
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
                                    <th>会议标题</th>
                                    <th>姓名</th>
                                    <th>是否参加</th>
                                    <th>请假内容</th>
                                    <th>新建日期</th>

                                    <th><@spring.message code='common.th.control' /></th>
                                </tr>
                                <#if leaves.content?size gt 0>
                                    <#list leaves.content as leave>
                                        <tr>
                                            <td>
                                                <#if leave.post??>
                                                    <#if leave.post.postStatus==0>
                                                    <a target="_blank" href="/meetings/${leave.post.postUrl}">${leave.post.postTitle}</a>
                                                <#else>
                                                    ${leave.post.postTitle}
                                                </#if>

                                                </#if>

                                            </td>

                                            <td>
                                                ${leave.white.whiteName}
                                            </td>

                                            <td>
                                                <#if leave.status == 1>
                                                    <label>不参加</label>
                                                <#elseif leave.status == 2>
                                                    <label>参加</label>
                                                <#else >
                                                    <label>未确认</label>
                                                </#if>
                                            </td>

                                            <td>
                                                    ${leave.leaveContent}
                                            </td>

                                            <td>${leave.leaveDate!?string("yyyy-MM-dd HH:mm")}</td>
                                            <td>

                                                        <a href="/admin/leaves/edit?leaveId=${leave.leaveId?c}" class="btn btn-info btn-xs ">查看</a>
<#--                                                        <a href="/admin/leaves/edit?leaveId=${leave.leaveId?c}" class="btn btn-info btn-xs "><@spring.message code='common.btn.edit' /></a>-->

                                                        <button class="btn btn-danger btn-xs " onclick="modelShow('/admin/leaves/remove?leaveId=${leave.leaveId?c}','<@spring.message code="common.text.tips.to-delete" />')"><@spring.message code='common.btn.delete' /></button>

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
                            <@spring.message code='admin.pageinfo.text.no' />${leaves.number+1}/${leaves.totalPages}<@spring.message code='admin.pageinfo.text.page' />
                        </div>
                        <div class="btn-group pull-right btn-group-sm" role="group">
                            <a data-pjax="true" class="btn btn-default <#if !leaves.hasPrevious()>disabled</#if>" href="/admin/leaves?">
                                <@spring.message code='admin.pageinfo.btn.first' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !leaves.hasPrevious()>disabled</#if>" href="/admin/leaves?page=${leaves.number-1}">
                                <@spring.message code='admin.pageinfo.btn.pre' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !leaves.hasNext()>disabled</#if>" href="/admin/leaves?page=${leaves.number+1}">
                                <@spring.message code='admin.pageinfo.btn.next' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !leaves.hasNext()>disabled</#if>" href="/admin/leaves?page=${leaves.totalPages-1}">
                                <@spring.message code='admin.pageinfo.btn.last' />
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- 删除确认弹出层 -->
    <div class="modal fade" id="removeLeaveModal">
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
            $('#removeLeaveModal').modal();
        }
        function removeIt(){
            var url=$.trim($("#url").val());
            window.location.href=url;
        }
    </script>
</div>
<@footer></@footer>
</#compress>
