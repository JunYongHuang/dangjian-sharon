<aside class="main-sidebar">
    <section class="sidebar">
        <div class="user-panel">
            <div class="pull-left image">
                <img src="<#if user.userAvatar?if_exists!="">${user.userAvatar!}<#else >/static/halo-backend/images/default.png</#if>" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>${user.userDisplayName!}</p><a href="/admin/profile"><i class="fa fa-circle text-success"></i><@spring.message code='admin.menu.profile' /></a>
            </div>
        </div>
        <ul class="sidebar-menu" data-widget="tree">
            <li class="header">MENUS</li>
            <li>
                <a data-pjax="true" href="/admin">
                    <i class="fa fa-dashboard"></i>
                    <span><@spring.message code='admin.menu.dashboard' /></span>
                </a>
            </li>
            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa  fa-book"></i>
                    <span><@spring.message code='admin.menu.posts' /></span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                <ul class="treeview-menu" style="">
                    <li><a data-pjax="true" href="/admin/posts"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.all-posts' /></a></li>
                    <li><a data-pjax="false" href="/admin/posts/write"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-post' /></a></li>
                    <li><a data-pjax="true" href="/admin/category"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.categories' /></a></li>
                    <li><a data-pjax="true" href="/admin/tag"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.tags' /></a></li>
                </ul>
            </li>

            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa  fa-book"></i>
                    <span><@spring.message code='admin.menu.specials' /></span>
                    <span class="pull-right-container">

                        <i class="fa fa-angle-left pull-right"></i>
<#--                        <#if newSpecials?size gt 0>-->
<#--                            <span class="label label-primary pull-right">${newSpecials?size}</span>-->
<#--                        </#if>-->
                    </span>
                </a>
                <ul class="treeview-menu" style="">
                    <li><a data-pjax="true" href="/admin/specials">
                            <i class="fa fa-circle-o"></i>


                            <span><@spring.message code='admin.menu.all-specials' /></span>
                            <span class="pull-right-container">
                        <#if newSpecials?size gt 0>
                            <span class="label label-primary pull-right">${newSpecials?size}</span>
                        </#if>
                    </span>
                        </a></li>
<#--                    <li><a data-pjax="false" href="/admin/specials/write"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-special' /></a></li>-->
                    <li><a data-pjax="true" href="/admin/specialType"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.special-types' /></a></li>
                </ul>
            </li>



            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa  fa-book"></i>
                    <span><@spring.message code='admin.menu.meetings' /></span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                <ul class="treeview-menu" style="">
                    <li><a data-pjax="true" href="/admin/meetings"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.all-meetings' /></a></li>
                    <li><a data-pjax="false" href="/admin/meetings/write"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-meeting' /></a></li>
                    <li><a data-pjax="true" href="/admin/leaves"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.leaves' /></a></li>
<#--                    <li><a data-pjax="false" href="/admin/leaves/write"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-leave' /></a></li>-->

                </ul>
            </li>



            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa  fa-book"></i>
                    <span><@spring.message code='admin.menu.whites' /></span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                <ul class="treeview-menu" style="">
                    <li><a data-pjax="true" href="/admin/whites"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.all-whites' /></a></li>
                    <li><a data-pjax="false" href="/admin/whites/write"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-white' /></a></li>
<#--                    <li><a data-pjax="true" href="/admin/role"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.roles' /></a></li>-->
                    <li><a data-pjax="true" href="/admin/backlogs"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.backlogs' /></a></li>
<#--                    <li><a data-pjax="false" href="/admin/backlogs/write"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-backlog' /></a></li>-->

                </ul>
            </li>

            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa fa-desktop"></i>
                    <span><@spring.message code='admin.menu.pages' /></span>
                    <span class="pull-right-container">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu">
                    <li><a data-pjax="true" href="/admin/page"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.all-pages' /></a></li>
                    <li><a data-pjax="false" href="/admin/page/new"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.new-page' /></a></li>
                </ul>
            </li>
            <li>
                <a data-pjax="true" href="/admin/attachments">
                    <i class="fa fa-picture-o"></i>
                    <span><@spring.message code='admin.menu.attachments' /></span>
                </a>
            </li>
<#--            <li>-->
<#--                <a data-pjax="true" href="/admin/comments">-->
<#--                    <i class="fa fa-comment"></i>-->
<#--                    <span><@spring.message code='admin.menu.comments' /></span>-->
<#--                    <span class="pull-right-container">-->
<#--                        <#if newComments?size gt 0>-->
<#--                            <span class="label label-primary pull-right">${newComments?size}</span>-->
<#--                        </#if>-->
<#--                    </span>-->
<#--                </a>-->
<#--            </li>-->
            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa fa-paint-brush"></i>
                    <span><@spring.message code='admin.menu.appearance' /></span>
                    <span class="pull-right-container">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu">
                    <li><a data-pjax="true" href="/admin/themes"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.themes' /></a></li>
                    <li><a data-pjax="true" href="/admin/menus"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.menus' /></a></li>
                    <li><a data-pjax="false" href="/admin/themes/editor"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.edit-theme' /></a></li>
                </ul>
            </li>
            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa fa-user-o"></i>
                    <span><@spring.message code='admin.menu.user' /></span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                <ul class="treeview-menu">
                    <li><a data-pjax="true" href="/admin/profile"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.profile' /></a></li>
                </ul>
            </li>
            <li class="treeview">
                <a data-pjax="true" href="javascript:void(0)">
                    <i class="fa fa-cog"></i>
                    <span><@spring.message code='admin.menu.settings' /></span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                <ul class="treeview-menu">
                    <li><a data-pjax="true" href="/admin/option"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.blog-settings' /></a></li>
                    <li><a data-pjax="true" href="/admin/backup"><i class="fa fa-circle-o"></i><@spring.message code='admin.menu.blog-backup' /></a></li>
                    <li><a data-pjax="true" href="/admin/tools"><i class="fa fa-circle-o"></i>小工具</a></li>
                </ul>
            </li>
        </ul>
    </section>
</aside>
