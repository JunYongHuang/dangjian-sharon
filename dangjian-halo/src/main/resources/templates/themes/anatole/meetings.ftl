<#include "module/macro.ftl">
<@head title="归档 · ${options.blog_title?default('Anatole')}" keywords="文章归档,${options.seo_keywords?default('Anatole')}" description="${options.seo_desc?default('Anatole')}"></@head>
<#include "module/sidebar.ftl">
<div class="main">
    <#include "module/page-top.ftl">
    <div class="autopagerize_page_element">
        <div class="content">
            <div class="archive animated fadeInDown">
                <ul class="list-with-title">
                    <@articleTag method="meetingsLess">
                        <#list meetingsLess as meeting>
                            <div class="listing-title">${meeting.year}</div>
                            <ul class="listing">
                                <#list meeting.posts?sort_by("postDate")?reverse as post>
                                    <div class="listing-item">
                                        <div class="listing-post">
                                            <a href="/meetings/${post.postUrl}" title="${post.postTitle}">${post.postTitle}</a>
                                            <div class="post-time">
                                                <span class="date">${post.postDate?string("yyyy-MM-dd")}</span>
                                            </div>
                                        </div>
                                    </div>
                                </#list>
                            </ul>
                        </#list>
                    </@articleTag>
                </ul>
            </div>
        </div>
    </div>
</div>
<@footer></@footer>