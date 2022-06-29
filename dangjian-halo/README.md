<h1 align="center"><a href="https://github.com/JunYongHuang/dangjian-sharon" target="_blank">Dangjian-Halo</a></h1>

> Dangjian-Halo 可能是最好的 Java 党建后台系统。


[![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-723201457-yellowgreen.svg)](https://jq.qq.com/?_wv=1027&k=19odMmjy)
[![码云](https://img.shields.io/badge/Gitee-%E7%A0%81%E4%BA%91-yellow.svg)](https://gitee.com/hjy1234123/dangjian-sharon)
[![Github](https://img.shields.io/badge/Github-Github-red.svg)](https://github.com/JunYongHuang/dangjian-sharon)

#### 项目介绍
简单党建小程序

建党百年已进入倒计时，希望能帮助到各位小伙伴。

# 说明
- [感谢。W 项目基于blog-sharon开发](https://gitee.com/qinxuewu/blog-sharon/tree/master/blog-halo)

------------------------------
### 体验地址
- 上研党云体验版:

![上研党云体验版](https://images.gitee.com/uploads/images/2021/0408/131609_d86447b1_893422.png "微信图片_20210408131535.png")
- 相同框架版:

![同城生活](https://images.gitee.com/uploads/images/2021/0408/142504_1a6edb0b_893422.jpeg "0.jpg")

## 简介

**Halo** [ˈheɪloʊ]，意为光环。当然，你也可以当成拼音读(哈喽)。

轻快，简洁，功能强大，使用 Java 开发的党建后台应用系统。



## 快速开始

源码部署：
```bash
进入pom目录  cd halo # 

#打包 mvn clean package -Pprod  

#上传文件到服务器   target/dist/halo/halo-latest.jar

#运行  cd /target/dist/halo
nohup java -Xms256m -Xmx256m -jar halo-latest.jar  &
```



> 注意：如使用 Idea，Eclipse 等IDE运行的话，需要安装Lombok插件，另外暂不支持JDK10，主题管理和主题上传会有问题。



## 主题

除了内置的 [Anatole](https://github.com/hi-caicai/farbox-theme-Anatole) 和 [Material](https://github.com/viosey/hexo-theme-material) ，还有下列主题没有集成在项目里，如有需要，请自行下载之后通过后台上传上去使用。

- [Vno](https://github.com/ruibaby/vno-halo) - 来自Jekyll的一款主题，作者 [Wei Wang](https://onevcat.com/)。
- [Hux](https://github.com/ruibaby/hux-halo) - 来自Jekyll的一款主题，作者 [Xuan Huang](https://huangxuan.me/)。
- [Story](https://github.com/ruibaby/story-halo) - 来自Typecho的一款主题，作者 [Trii Hsia](https://yumoe.com/)。
- [NexT](https://github.com/ruibaby/next-halo) - 来自Hexo的一款主题，作者 [iissnan](https://notes.iissnan.com/)。
- [Casper](https://github.com/ruibaby/casper-halo) - 来自Ghost的一款主题，作者 [Ghost](https://github.com/TryGhost)。

> 声明：不接受任何对**移植主题**功能上的意见和建议。

## 许可证

[![license](https://img.shields.io/github/license/ruibaby/halo.svg?style=flat-square)](https://github.com/JunYongHuang/dangjian-sharon/blob/master/LICENSE)

> Dangjian-Halo 使用 GPL-v3.0 协议开源，请尽量遵守开源协议，即便是在中国。

## 感谢

Halo 的诞生离不开下面这些项目：

- [Spring Boot](https://github.com/spring-projects/spring-boot)：Spring 的快速开发框架
- [Freemarker](https://freemarker.apache.org/)：模板引擎，使页面静态化
- [H2 Database](https://github.com/h2database/h2database)：嵌入式数据库，无需安装
- [Spring-data-jpa](https://github.com/spring-projects/spring-data-jpa.git)：不需要写 sql 语句的持久层框架
- [Ehcache](http://www.ehcache.org/)：缓存框架
- [Lombok](https://www.projectlombok.org/)：让代码更简洁
- [oh-my-email](https://github.com/biezhi/oh-my-email)：可能是最小的 Java 邮件发送库了，支持抄送、附件、模板等
- [Hutool](https://github.com/looly/hutool)：一个 Java 基础工具类库
- [Thumbnailator](https://github.com/coobird/thumbnailator)：缩略图生成库
- [AdminLTE](https://github.com/almasaeed2010/AdminLTE)：基于 Bootstrap 的后台模板
- [Bootstrap](https://github.com/twbs/bootstrap.git)：使用最广泛的前端 ui 框架
- [Animate](https://github.com/daneden/animate.css.git)：非常好用的 css 动效库
- [SimpleMDE - Markdown Editor](https://github.com/sparksuite/simplemde-markdown-editor)：简洁，功能够用，且轻量级的 Markdown 编辑器
- [Bootstrap-FileInput](https://github.com/kartik-v/bootstrap-fileinput.git)：基于 Bootstrap 的文件上传组件
- [Font-awesome](https://github.com/FortAwesome/Font-Awesome.git)：使用最广泛的字体图标库
- [JQuery](https://github.com/jquery/jquery.git)：使用最广泛的 JavaScript 框架
- [Layer](https://github.com/sentsin/layer.git)：用户认为最实用最好看的弹出层组件，没有之一
- [JQuery-Toast](https://github.com/kamranahmedse/jquery-toast-plugin)：消息提示组件
- [Pjax](https://github.com/defunkt/jquery-pjax.git)：pushState + ajax = pjax
- [OwO](https://github.com/DIYgod/OwO)：前端表情库


## 后台界面展示


![首页](https://images.gitee.com/uploads/images/2021/0408/145551_37b1bc04_893422.png "12.png")
![提示单管理](https://images.gitee.com/uploads/images/2021/0408/145608_31a0b480_893422.png "13.png")
![提示单编辑](https://images.gitee.com/uploads/images/2021/0408/145617_25689d26_893422.jpeg "14.jpg")
![白名单](https://images.gitee.com/uploads/images/2021/0408/145624_af55e621_893422.png "15.png")
![图片附件](https://images.gitee.com/uploads/images/2021/0408/145633_82100f48_893422.png "16.png")

## 开源小项目
- [dangjian-sharon](https://github.com/JunYongHuang/dangjian-sharon):   一款简单党建小程序
- [dangjian-halo](https://github.com/JunYongHuang/dangjian-sharon/dangjian-halo):  Java后台项目 
- 建议使用idea开发工具
- [dangjian-sharon](https://github.com/JunYongHuang/dangjian-sharon/dangjian-sharon): 微信小程序项目
- 建议使用微信开发者工具

## 您的支持是我坚持开源的最大动力

![捐助支持](https://images.gitee.com/uploads/images/2021/0408/142645_bba4c69b_893422.jpeg "8.jpg")