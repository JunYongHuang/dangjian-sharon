<#compress >
<#include "module/_macro.ftl">
<@head>${options.blog_title!} | <@spring.message code='admin.specials.edit.title' /></@head>
<div class="content-wrapper">
    <link rel="stylesheet" href="/static/halo-backend/plugins/simplemde/simplemde.min.css">
    <link rel="stylesheet" href="/static/halo-backend/plugins/jquery-tageditor/jquery.tag-editor.css">
    <link rel="stylesheet" href="/static/halo-backend/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <style type="text/css">
        #postTitle{font-weight: 400;}
    </style>
    <section class="content-header" id="animated-header">
        <h1 style="display: inline-block;"><@spring.message code='admin.specials.edit.title' /></h1>
        <a class="btn-header" id="btnOpenAttach" href="javascript:void(0)" onclick="halo.layerModal('/admin/attachments/select?type=post','<@spring.message code="common.js.all-attachment" />')">
            <@spring.message code='admin.editor.btn.attachs' />
        </a>
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="javascript:void(0)"><i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' /></a>
            </li>
            <li>
                <a data-pjax="true" href="/admin/specials"><@spring.message code='admin.specials.title' /></a>
            </li>
            <li class="active"><@spring.message code='admin.specials.edit.title' /></li>
        </ol>
    </section>
    <section class="content" id="animated-content">
        <div class="row">
            <div class="col-md-9">
                <input type="hidden" id="postId" name="postId" value="${post.postId?c}">
                <div style="margin-bottom: 10px;">
                    <input type="text" class="form-control input-lg" id="postTitle" name="postTitle" placeholder="<@spring.message code='admin.specials.edit.form.title.placeholder' />" onblur="autoComplateUrl();" value="${post.postTitle!}">
                </div>
                <div style="display: block;margin-bottom: 10px;">
                    <span>
                        <@spring.message code='admin.editor.form.url' />
                        <a href="javascript:void(0)">${options.blog_url!}/specials/<span id="postUrl">${post.postUrl!}</span>/</a>
                        <button class="btn btn-default btn-sm " id="btn_input_postUrl"><@spring.message code='common.btn.edit' /></button>
                        <button class="btn btn-default btn-sm " id="btn_change_postUrl" onclick="urlOnBlurAuto()" style="display: none;"><@spring.message code='common.btn.define' /></button>
                    </span>
                </div>
</#compress>
                <div class="box box-primary">
                    <!-- Editor.md编辑器 -->
                    <div class="box-body pad">
                        <div id="markdown-editor">
                            <textarea id="editorarea" style="display:none;">${post.postContentMd!}</textarea>
                        </div>
                    </div>
                </div>
<#compress >
            </div>
            <div class="col-md-3">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title"><@spring.message code='admin.editor.text.push' /></h3>
                        <div class="box-tools">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="form-group">
                            <label for="allowComment" class="control-label"><@spring.message code='admin.editor.allow-comment' /></label>
                            <select class="form-control" id="allowComment" name="allowComment">
                                <option value="1" <#if (post.allowComment!1)==1>selected</#if>><@spring.message code='common.select.yes' /></option>
                                <option value="0" <#if (post.allowComment!)==0>selected</#if>><@spring.message code='common.select.no' /></option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="postDate" class="control-label">发布时间：</label>
                            <input type="text" class="form-control" id="postDate" name="postDate" value="${post.postDate!?string('yyyy-MM-dd HH:mm')}">
                        </div>

                        <div class="form-group">
                            <label for="examineEnable" class="control-label">审核状态：</label>
                            <div class="control-radio">
                                <div class="pretty p-default p-round">
                                    <input type="radio" name="examineEnable" id="examineEnable" value="1" ${((post.examineStatus!1)==1)?string('checked','')}>
                                    <div class="state p-primary">
                                        <label>通过</label>
                                    </div>
                                </div>
                                <div class="pretty p-default p-round">
                                    <input type="radio" name="examineEnable" id="examineEnable" value="2" ${((post.examineStatus!2)==2)?string('checked','')}>
                                    <div class="state p-primary">
                                        <label>不通过</label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="examineContent" class="control-label">不通过原因：

                            </label>
                                <textarea class="form-control" id="examineContent" name="examineContent">${post.examineContent!}</textarea>

                        </div>
                    </div>
                    <div class="box-footer">
                        <button onclick="push(1)" class="btn btn-default btn-sm "><@spring.message code='admin.editor.save-draft' /></button>
                        <button onclick="push(0)" class="btn btn-primary btn-sm pull-right " data-loading-text="<@spring.message code='admin.editor.btn.pushing' />">
                            <@spring.message code='admin.editor.btn.update' />
                        </button>
                    </div>
                </div>


                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">主题类型</h3>
                        <div class="box-tools">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse" title="Collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="box-body">

                        <select class="form-control" id="chooseSpecialType" name="chooseSpecialType">
                            <@commonTag method="specialTypes">
                                <#if specialTypes??>
                                    <option value="">请选择</option>
                                    <#list specialTypes as specialType>
                                        <option value="${specialType.specialTypeId}" <#if post.specialType?? && specialType.specialTypeId = post.specialType.specialTypeId>selected="selected"</#if>>${specialType.specialTypeName}</option>
                                    </#list>
                                <#else>
                                    <option><@spring.message code='common.text.no-data' /></option>
                                </#if>
                            </@commonTag>
                        </select>
                    </div>
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
                            <img src="${post.postThumbnail!'/static/halo-frontend/images/thumbnail/thumbnail.png'}" class="img-responsive img-thumbnail" id="selectImg" onclick="halo.layerModal('/admin/attachments/select?id=selectImg','<@spring.message code="common.js.all-attachment" />')" style="cursor: pointer;">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <script src="/static/halo-backend/plugins/simplemde/simplemde.min.js"></script>
    <script src="/static/halo-backend/plugins/inline-attachment/codemirror-4.inline-attachment.min.js"></script>
    <script src="/static/halo-backend/plugins/jquery-tageditor/jquery.tag-editor.min.js"></script>
    <script src="/static/halo-backend/plugins/jquery-tageditor/jquery.caret.min.js"></script>
    <script src="/static/halo-backend/plugins/hz2py/jQuery.Hz2Py-min.js"></script>
    <script src="/static/halo-backend/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/static/halo-backend/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <#--    <script src="//cdnjs.loli.net/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML"></script>-->
    <script>

        $('#postDate').datetimepicker({
            format: 'yyyy-mm-dd hh:ii',
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1
        });

        // MathJax.Hub.Config({
        //     tex2jax: {inlineMath: [["$","$"],["\\(","\\)"]]}
        // });
        //
        // var QUEUE = MathJax.Hub.queue;

        /**
         * 加载编辑器
         */
        var simplemde = new SimpleMDE({
            element: document.getElementById("editorarea"),
            autoDownloadFontAwesome: false,
            autofocus: true,
            autosave: {
                enabled: true,
                uniqueId: "editor-temp-${post.postId!}",
                delay: 10000
            },
            renderingConfig: {
                codeSyntaxHighlighting: true
            },
            previewRender: function(plainText) {
                var preview = document.getElementsByClassName("editor-preview-side")[0];
                preview.innerHTML = this.parent.markdown(plainText);
                preview.setAttribute('id','editor-preview');
                //MathJax.Hub.Queue(["Typeset",MathJax.Hub,"editor-preview"]);
                return preview.innerHTML;
            },
            showIcons: ["code", "table"],
            status: ["autosave", "lines", "words"],
            tabSize: 4
        });

        /**
         * 方法来自https://gitee.com/supperzh/zb-blog/blob/master/src/main/resources/templates/article/publish.html#L255
         */
        $(function () {
            inlineAttachment.editors.codemirror4.attach(simplemde.codemirror, {
                uploadUrl: "/admin/attachments/upload"
            });
        })



        /**
         * 加载该文章已有的标签
         */
        <#if post.tags?size gt 0>
            <#list post.tags as tag>
            $('#tagList').tagEditor('addTag','${tag.tagName}');
            </#list>
        </#if>

        $('#chooseTag').change(function () {
            $('#tagList').tagEditor('addTag',$(this).val());
        });

        /**
         * 自动填充路径，并且将汉字转化成拼音以-隔开
         */
        function autoComplateUrl() {
            var titleVal = $("#postTitle").val();
            if(titleVal!="" && titleVal!=null && $("#postUrl").html()==''){
                var result = $("#postTitle").toPinyin().toLowerCase();
                $("#postUrl").html(result.substring(0,result.length-1));
            }
        }

        /**
         * 检测是否已经存在该链接
         * @constructor
         */
        function urlOnBlurAuto() {
            if($('#newPostUrl').val()===""){
                halo.showMsg("<@spring.message code='admin.editor.js.no-url' />",'info',2000);
                return;
            }
            $.ajax({
                type: 'GET',
                url: '/admin/specials/checkUrl',
                async: false,
                data: {
                    'postUrl': $('#newPostUrl').val()
                },
                success: function (data) {
                    if(data.code==0){
                        halo.showMsg(data.msg,'error',2000);
                        return;
                    }else{
                        $('#postUrl').html($('#newPostUrl').val());
                        $('#btn_change_postUrl').hide();
                        $('#btn_input_postUrl').show();
                    }
                }
            });
        }
        $('#btn_input_postUrl').click(function () {
            var postUrl = $("#postUrl").html();
            $('#postUrl').html("<input type='text' id='newPostUrl' onblur='urlOnBlurAuto()' value='"+postUrl+"'>");
            $(this).hide();
            $('#btn_change_postUrl').show();
        });
        var postTitle = $("#postTitle");
        // var cateList = new Array();

        /**
         * 提交文章
         * @param status 文章状态
         */
        function push(status) {
            var Title = "";
            var specialTypeId = $('#chooseSpecialType').val();
            var examineStatus = $('input:radio[name="examineEnable"]:checked').val();
            var examineContent = $('#examineContent').val();

            if(postTitle.val()){
                Title = postTitle.val();
            }else{
                halo.showMsg("<@spring.message code='admin.editor.js.no-title' />",'info',2000);
                return;
            }
            if(specialTypeId===""){
                halo.showMsg("请选择主题",'info',2000);
                return;
            }
            if($('#postUrl').html()===""){
                halo.showMsg("<@spring.message code='admin.editor.js.no-url' />",'info',2000);
                return;
            }
            $.ajax({
                type: 'POST',
                url: '/admin/specials/update',
                async: false,
                data: {
                    'postId': $('#postId').val(),
                    'postStatus': status,
                    'postTitle': Title,
                    'postUrl' : $('#postUrl').html().toString(),
                    'postContentMd': simplemde.value(),
                    'postThumbnail': $('#selectImg').attr('src'),
                    'specialTypeId' : specialTypeId,
                    'allowComment' : $('#allowComment').val(),
                    'examineStatus' : examineStatus,
                    'examineContent' : examineContent,

                    'postDate' : $("#postDate").val()
                },
                success: function (data) {
                    if(data.code==1){
                        //清除自动保存的内容
                        simplemde.clearAutosavedValue();
                        halo.showMsgAndRedirect(data.msg,'success',1000,'/admin/specials');
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
