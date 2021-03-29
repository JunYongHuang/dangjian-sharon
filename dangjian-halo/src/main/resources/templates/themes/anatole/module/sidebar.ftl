<div class="sidebar animated fadeInDown">
    <div class="logo-title">
        <div class="title">
            <img src="${options.blog_logo?default("/anatole/source/images/logo@2x.png")}" style="width:127px;<#if options.anatole_style_avatar_circle?default('false')=='true'>border-radius:50%</#if>" />
            <h3 title="">
                <a href="/">${options.blog_title?default("ANATOLE")}</a>
            </h3>
            <div class="description">
                <#if options.anatole_style_hitokoto?default("false")=="true">
                    <p id="yiyan">获取中...</p>
                <#else >
                    <p>${user.userDesc?default("")}</p>
                </#if>
            </div>
        </div>
    </div>
<#--    <#include "social-list.ftl">-->
    <div class="footer">
        <a target="_blank" href="#">
            <#-- 不允许修改该主题信息，也不能删除。
            <span>Designed by </span>
            <a href="https://www.caicai.me">CaiCai</a> -->
            <#-- 使用了GPL协议~
            <div class="by_halo">
                <a href="https://github.com/ruibaby/halo" target="_blank">Proudly published with Halo&#65281;</a>
            </div> -->
            <strong>欢迎使用上党小程序管理后台</strong>
            <div class="by_halo"><a target="_blank" href="http://www.ryanc.cc/">党建中心</a></div>


            <div class="footer_text">
                <@footer_info></@footer_info>
            </div>
        </a>
    </div>
</div>
