package cc.ryanc.halo.model.tag;

import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.PostTypeEnum;
import cc.ryanc.halo.service.*;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 *     FreeMarker自定义标签
 * </pre>
 *
 * @author : HJY
 * @date : 2018/4/26
 */
@Component
public class CommonTagDirective implements TemplateDirectiveModel {

    private static final String METHOD_KEY = "method";

    @Autowired
    private MenuService menuService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private SpecialTypeService specialTypeService;

    @Autowired
    private LinkService linkService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (map.containsKey(METHOD_KEY)) {
            String method = map.get(METHOD_KEY).toString();
            switch (method) {
                case "menus":
                    environment.setVariable("menus", builder.build().wrap(menuService.findAll()));
                    break;
                case "categories":
                    environment.setVariable("categories", builder.build().wrap(categoryService.findAll()));
                    break;
                case "tags":
                    environment.setVariable("tags", builder.build().wrap(tagService.findAll()));
                    break;
                case "specialTypes":
                    environment.setVariable("specialTypes", builder.build().wrap(specialTypeService.findAll()));
                    break;
                case "links":
                    environment.setVariable("links", builder.build().wrap(linkService.findAll()));
                    break;
                case "newComments":
                    environment.setVariable("newComments", builder.build().wrap(commentService.findAll(1)));
                    break;
                case "newSpecials":
                    Integer postStatus = 1;
                    environment.setVariable("newSpecials", builder.build().wrap(postService.findAllByPostTypeAndPostStatus(PostTypeEnum.POST_TYPE_SPECIAL.getDesc(), postStatus)));
                    break;
                default:
                    break;
            }
        }
        templateDirectiveBody.render(environment.getOut());
    }
}
