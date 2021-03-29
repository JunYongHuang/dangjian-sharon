<#include "module/macro.ftl">
<@head title="${post.postTitle} · ${options.blog_title?default('Anatole')}" keywords="${post.postTitle},${options.seo_keywords?default('Anatole')},${tagWords}" description="${post.postSummary?if_exists}"></@head>
<#include "module/sidebar.ftl">
<div class="main">
    <link href="/anatole/source/plugins/prism/prism.css" type="text/css" rel="stylesheet" />
    <style>
        code, tt {
            font-size: 1.2em;
        }
        table {
            border-spacing: 0;
            border-collapse: collapse;
            margin-top: 0;
            margin-bottom: 16px;
            display: block;
            width: 100%;
            overflow: auto;

        }
        table th {
            font-weight: 600;
        }
        table th,
        table td {
            padding: 6px 13px;
            border: 1px solid #dfe2e5;
        }
        table tr {
            background-color: #fff;
            border-top: 1px solid #c6cbd1;
        }
        table tr:nth-child(2n) {
            background-color: #f6f8fa;
        }
        .post{
            width: 22rem;
        }
    </style>

    <#include "module/page-top.ftl">
    <div class="autopagerize_page_element">
        <div class="content">
            <div class="post-page  animated fadeInDown">
<#--                <div class="post animated fadeInDown"  id="fromHTMLtestdiv">-->
                <div class="post"  id="fromHTMLtestdiv">
                    <div class="post-title">
                        <h3>
                            <a>${post.postTitle}</a>
                        </h3>
                    </div>
                    <div class="post-content">
                        ${post.postContent?if_exists}
                    </div>
                    <style type="text/css">
                        @media print {
                            .noprint{display:none;}
                        }
                    </style>
                    <div class="post-footer">
                        <div class="meta">
                            <div class="info">
                                <i class="fa fa-sun-o"></i>
                                <span class="date">${post.postDate?string("yyyy-MM-dd")}</span>
                                <i class="fa fa-file-pdf-o noprint"></i>
<#--                                <a href="/archives/${post.postUrl}#comment_widget">Comments</a>-->
                                <a href="javascript:demoFromHTML();"  class="noprint">下载pdf</a>

                                <i class="fa fa-image noprint"></i>
                                <#--                                <a href="/archives/${post.postUrl}#comment_widget">Comments</a>-->
                                <a href="javascript:imgFromHTML();"  class="noprint">下载图片</a>



                                <#if post.tags?size gt 0>
                                    <i class="fa fa-tag"></i>
                                    <#list post.tags as tag>
                                        <a href="/tags/${tag.tagUrl}" class="tag">&nbsp;${tag.tagName}</a>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="share" style="display: inline-flex">
                    阅读情况:
                    <#if post.backlogs?size gt 0>
                        <i class="fa fa-eye"></i>
                        <#list post.backlogs as backlog>
                            <#if backlog.white??>
                                <#if post.categories?size gt 0>
                                                    <#list post.categories as cate>
                                                        <#if cate.roleId == backlog.white.roleId>
                                                            <label class="tag">&nbsp;${backlog.white.whiteName} </label>
                                                            <#if backlog.backlogStatus == 1>
                                                                <label>已读</label>
                                                            <#elseif backlog.backlogStatus == 2>
                                                                <label>已读5秒以上</label>
                                                            <#else >
                                                                <label>未读</label>
                                                            </#if>

                                                        </#if>
                                                    </#list>
                                                <#else >

                                                </#if>






                            </#if>

                        </#list>
                    </#if>
                </div>
<#--                <div class="share" style="display: inline-flex">-->
<#--                    <div class="evernote">-->
<#--                        <a href="javascript:(function(){EN_CLIP_HOST='http://www.evernote.com';try{var%20x=document.createElement('SCRIPT');x.type='text/javascript';x.src=EN_CLIP_HOST+'/public/bookmarkClipper.js?'+(new%20Date().getTime()/100000);document.getElementsByTagName('head')[0].appendChild(x);}catch(e){location.href=EN_CLIP_HOST+'/clip.action?url='+encodeURIComponent(location.href)+'&title='+encodeURIComponent(document.title);}})();"-->
<#--                           ref="nofollow" target="_blank" class="fa fa-bookmark"></a>-->
<#--                    </div>-->
<#--                    <div class="weibo">-->
<#--                        <a href="javascript:void((function(s,d,e){try{}catch(e){}var f='http://service.weibo.com/share/share.php?',u=d.location.href,p=['url=',e(u),'&title=',e(d.title),'&appkey=2924220432'].join('');function a(){if(!window.open([f,p].join(''),'mb',['toolbar=0,status=0,resizable=1,width=620,height=450,left=',(s.width-620)/2,',top=',(s.height-450)/2].join('')))u.href=[f,p].join('');};if(/Firefox/.test(navigator.userAgent)){setTimeout(a,0)}else{a()}})(screen,document,encodeURIComponent));"-->
<#--                           class="fa fa-weibo"></a>-->
<#--                    </div>-->
<#--                    <div class="twitter">-->
<#--                        <a href="http://twitter.com/home?status=${options.blog_url}/archives/${post.postUrl} ,${options.blog_title?if_exists},${post.postTitle},;"-->
<#--                           class="fa fa-twitter"></a>-->
<#--                    </div>-->
<#--                </div>-->
                <div class="pagination">
                    <ul class="clearfix">
                        <#if afterPost??>
                        <li class="pre pagbuttons">
                            <a class="btn" role="navigation" href="/archives/${afterPost.postUrl}" title="${afterPost.postTitle}">上一篇</a>
                        </li>
                        </#if>
                        <#if beforePost??>
                        <li class="next pagbuttons">
                            <a class="btn" role="navigation" href="/archives/${beforePost.postUrl}" title="${beforePost.postTitle}">下一篇</a>
                        </li>
                        </#if>
                    </ul>
                </div>
                <div id="comment_widget">
<#--                    <#include "module/comment.ftl">-->
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/anatole/source/plugins/prism/prism.js"></script>
<#--<script type="text/javascript" src="/anatole/source/plugins/jsPDF/jspdf.min.js"></script>-->

<#--<script src="/anatole/source/plugins/jspdf_font/jspdf.customfonts.min.js"></script>-->
<#--<script src="/anatole/source/plugins/jspdf_font/default_vfs.js"></script>-->

<script type="text/javascript">
    // var CssColors = {
    //     colorNameToHex:function (rgb){
    //         var regexp = /[0-9]{0,3}/g;
    //         var re = rgb.match(regexp);//利用正则表达式去掉多余的部分，将rgb中的数字提取
    //         var hexColor = "#"; var hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'];
    //         for (var i = 0; i < re.length; i++) {
    //             var r = null, c = re[i], l = c;
    //             var hexAr = [];
    //             while (c > 16){
    //                 r = c % 16;
    //                 c = (c / 16) >> 0;
    //                 hexAr.push(hex[r]);
    //             } hexAr.push(hex[c]);
    //             if(l < 16&&l != ""){
    //                 hexAr.push(0)
    //             }
    //             hexColor += hexAr.reverse().join('');
    //         }
    //         //alert(hexColor)
    //         return hexColor;
    //     }
    // }
    function demoFromHTML() {
        $("#fromHTMLtestdiv").printArea();
    }
    function imgFromHTML() {

        //imgArr为图片地址的一个数组
        var imgArr=$("#fromHTMLtestdiv img");

        for (var i = 0; i < imgArr.length; i++) {
            if (imgArr.get(i)) {
                var url = imgArr.get(i).src;
                console.log("imgUrl: " + url);


                fetch(url).then(res => res.blob().then(blob => {
                    var a = document.createElement('a');
                    var url = window.URL.createObjectURL(blob);
                    //var filename = 'myfile.zip';
                    var http = url.split('?')[0];
                    let file = http.split('/');
                    var filename = file[file.length - 1];

                    a.href = url;
                    a.download = filename;
                    a.click();
                    window.URL.revokeObjectURL(url);
                }))
            }
        }
    }











    // function demoFromHTML() {
    //     var pdf = new jsPDF('p', 'pt', 'letter')
    //     // 添加并设置字体
    //
    //
    //         // source can be HTML-formatted string, or a reference
    //         // to an actual DOM element from which the text will be scraped.
    //         , source = $('#fromHTMLtestdiv')[0]
    //
    //         // we support special element handlers. Register them with jQuery-style
    //         // ID selector for either ID or node name. ("#iAmID", "div", "span" etc.)
    //         // There is no support for any other type of selectors
    //         // (class, of compound) at this time.
    //         , specialElementHandlers = {
    //             // element with id of "bypass" - jQuery style selector
    //             '#bypassme': function(element, renderer){
    //                 // true = "handled elsewhere, bypass text extraction"
    //                 return true
    //             }
    //         }
    //
    //     margins = {
    //         top: 80,
    //         bottom: 60,
    //         left: 40,
    //         width: 522
    //     };
    //     pdf.addFont('SourceHanSans-Normal.ttf', 'SourceHanSans-Normal', 'normal');
    //     pdf.setFont('SourceHanSans-Normal');
    //     // pdf.addFont('NotoSansCJKtc-Regular.ttf', 'NotoSansCJKtc', 'normal');
    //     // pdf.setFont('NotoSansCJKtc');
    //     // all coords and widths are in jsPDF instance's declared units
    //     // 'inches' in this case
    //     pdf.fromHTML(
    //         source // HTML string or DOM elem ref.
    //         , margins.left // x coord
    //         , margins.top // y coord
    //         , {
    //             'width': margins.width // max width of content on PDF
    //             , 'elementHandlers': specialElementHandlers
    //         },
    //         function (dispose) {
    //             // dispose: object with X, Y of the last line add to the PDF
    //             //          this allow the insertion of new lines after html
    //             pdf.save('Test.pdf');
    //         },
    //         margins
    //     )
    // }
</script>
<@footer></@footer>

<script type="text/javascript" src="/anatole/source/plugins/jq_printarea/jquery.printarea.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.fileDownload/1.4.2/jquery.fileDownload.min.js"></script>